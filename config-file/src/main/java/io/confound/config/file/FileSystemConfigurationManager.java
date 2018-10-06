/*
 * Copyright © 2018 GlobalMentor, Inc. <http://www.globalmentor.com/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.confound.config.file;

import static com.globalmentor.java.Conditions.*;
import static java.nio.file.Files.*;
import static java.util.Objects.*;

import java.io.*;
import java.nio.file.*;
import java.time.Instant;
import java.util.*;
import java.util.function.Supplier;
import java.util.regex.*;
import java.util.stream.Stream;

import javax.annotation.*;

import io.clogr.Clogged;
import io.confound.config.*;

/**
 * A configuration factory that can load and parse a configuration from the file system.
 * @author Garret Wilson
 */
public class FileSystemConfigurationManager extends AbstractFileConfigurationManager implements Clogged {

	/** The strategy for determining candidate paths for finding a configuration. */
	private final Supplier<Stream<Path>> configurationFileCandidatePathsSupplier;

	/** Information about the determined configuration path, or <code>null</code> if the configuration path has not been determined or has been validated. */
	private PathInfo configurationPathInfo = null;

	/**
	 * Constructor for an optional configuration.
	 * <p>
	 * The path supplier is allowed to throw {@link UncheckedIOException} when the stream is returned and during stream iteration.
	 * </p>
	 * @param configurationFileCandidatePathsSupplier The strategy for determining candidate paths for finding a configuration.
	 */
	public FileSystemConfigurationManager(@Nonnull Supplier<Stream<Path>> configurationFileCandidatePathsSupplier) {
		this(configurationFileCandidatePathsSupplier, false);
	}

	/**
	 * Constructor.
	 * <p>
	 * The path supplier is allowed to throw {@link UncheckedIOException} when the stream is returned and during stream iteration.
	 * </p>
	 * @param configurationFileCandidatePathsSupplier The strategy for determining candidate paths for finding a configuration.
	 * @param required Whether the manager requires a configuration to be determined when loading.
	 */
	public FileSystemConfigurationManager(@Nonnull Supplier<Stream<Path>> configurationFileCandidatePathsSupplier, final boolean required) {
		super(defaultFileFormats(), required);
		this.configurationFileCandidatePathsSupplier = requireNonNull(configurationFileCandidatePathsSupplier);
	}

	/**
	 * Determines the file format to use for the given path based on the registered formats and the path filename extension(s).
	 * @param path The path for which the file format should be determined.
	 * @return The configured file format for the path, if any.
	 * @throws IllegalArgumentException if the given path has no filename.
	 */
	public Optional<ConfigurationFileFormat> getFileFormat(@Nonnull final Path path) {
		final Path filenamePath = path.getFileName();
		checkArgument(filenamePath != null, "Configuration path %s has no filename.", path);
		return getFileFormatForFilename(filenamePath.toString());
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation uses the existing configuration path if it has been determined. If the configuration path has not been determined, such as if this
	 * manager has been invalidated using {@link #invalidate()}, it determines a new configuration path and updates the record.
	 * </p>
	 */
	@Override
	public synchronized Optional<Configuration> loadConfiguration(@Nullable final Configuration parentConfiguration) throws IOException, ConfigurationException {
		Path configurationPath = this.configurationPathInfo != null ? this.configurationPathInfo.getPath().orElse(null) : null;
		ConfigurationFileFormat fileFormat = null; //we may determine the file format during searching the candidate paths, or directly
		if(configurationPath == null || !isRegularFile(configurationPath)) { //find a configuration path if we don't already have one, or it doesn't exist anymore
			try {
				try (final Stream<Path> candidatePaths = configurationFileCandidatePathsSupplier.get()) { //be sure to close the stream of paths
					for(final Path candidatePath : (Iterable<Path>)candidatePaths::iterator) {
						if(!isRegularFile(candidatePath)) {
							getLogger().debug("Candidate configuration path {} does not exist.", candidatePath);
							continue;
						}
						getLogger().debug("Checking configuration file at path {}.", candidatePath);
						final Optional<ConfigurationFileFormat> candidateFileFormat = getFileFormat(candidatePath);
						if(!candidateFileFormat.isPresent()) { //if we have support for this file format
							getLogger().warn("Configuration file at path {} does not have a supported format.", candidatePath);
							continue;
						}
						configurationPath = candidatePath; //use this configuration path
						fileFormat = candidateFileFormat.get(); //use this file format 
						break;
					}
				}
			} catch(final UncheckedIOException uncheckedIOException) {
				throw uncheckedIOException.getCause();
			}
		}
		final Configuration configuration; //load the configuration or set it to `null`
		if(configurationPath != null) { //if we had or determined a configuration path
			if(fileFormat == null) { //if we don't yet know the format of the file
				final Path fileFormatPath = configurationPath;
				fileFormat = getFileFormat(fileFormatPath)
						.orElseThrow(() -> new ConfigurationException(String.format("Configuration file at path %s does not have a supported format.", fileFormatPath)));
			}
			assert fileFormat != null : "We expect to have determined the configuration file format.";
			try (final InputStream inputStream = new BufferedInputStream(newInputStream(configurationPath))) {
				configuration = fileFormat.load(inputStream, parentConfiguration);
			}
		} else { //if we couldn't determine a configuration path
			if(isRequired()) {
				throw new ConfigurationException("No supported configuration file found.");
			}
			configuration = null;
		}
		this.configurationPathInfo = new PathInfo(configurationPath); //save our current configuration path; we are now no longer stale
		return Optional.ofNullable(configuration);

	}

	/** {@inheritDoc} This implementation does not yet support saving configurations, and will throw an exception. */
	@Override
	public void saveConfiguration(final Parameters parameters) throws IOException {
		throw new UnsupportedOperationException("Saving configurations not yet supported.");
	}

	/** {@inheritDoc} This version additionally checks to see if whether there is a cached configuration path. */
	@Override
	public boolean isStale(final Parameters parameters) throws IOException {
		if(super.isStale(parameters)) {
			return true;
		}
		if(configurationPathInfo == null) {
			return true;
		}
		return false;
	}

	/** {@inheritDoc} This version additionally removes any cached configuration path. */
	@Override
	public synchronized void invalidate() {
		super.invalidate();
		configurationPathInfo = null;
	}

	/**
	 * Encapsulates determined path information and related information.
	 * @author Garret Wilson
	 *
	 */
	private static class PathInfo {

		private final Path path;

		/** @return The path, or {@link Optional#empty()} if none was determined. */
		public Optional<Path> getPath() {
			return Optional.ofNullable(path);
		}

		private final Instant determinedAt;

		/** The time at which the path was determined or determined not to exist. */
		@SuppressWarnings("unused") //TODO implement periodic checking for file change
		public Instant getDeterminedAt() {
			return determinedAt;
		}

		/**
		 * Constructor. The path resolution time will be set automatically to the current instant.
		 * @param path The path, or <code>null</code> if none was determined.
		 */
		public PathInfo(@Nullable final Path path) {
			this.path = path;
			this.determinedAt = Instant.now();
		}

	}

	/**
	 * Creates a configuration manager that loads an optional configuration from a given path as necessary.
	 * @param configurationPath The path at which to find the configuration file.
	 * @return A configuration manager for the file at the given path.
	 */
	public static FileSystemConfigurationManager forPath(@Nonnull final Path configurationPath) {
		return forCandidatePaths(configurationPath);
	}

	/**
	 * Creates a configuration manager that discovers an optional configuration file from one of the given paths.
	 * <p>
	 * The first configuration file with a supported format is used.
	 * </p>
	 * @param configurationCandidatePaths The potential paths at which to find the configuration file.
	 * @return A configuration manager for one of the files at the given paths.
	 * @throws NullPointerException if one of the candidate paths is <code>null</code>.
	 * @throws IllegalArgumentException if no paths are given, or if one of the given paths has no filename.
	 */
	public static FileSystemConfigurationManager forCandidatePaths(@Nonnull final Path... configurationCandidatePaths) {
		return new Builder().candidatePaths(configurationCandidatePaths).build();
	}

	/**
	 * Creates a configuration manager that discovers an optional configuration file using a base filename.
	 * @param directory The source directory for configuration file discovery.
	 * @param baseFilename The base filename, such as <code>base</code>, to return files with any extension, such as <code>base.foo</code> and
	 *          <code>base.foo.bar</code>, or no extension at all.
	 * @return A configuration manager to use a configuration file with the given base name.
	 * @throws NullPointerException if the directory and/or base filename is <code>null</code>.
	 */
	public static FileSystemConfigurationManager forCandidateBaseFilename(@Nonnull final Path directory, @Nonnull final String baseFilename) {
		return new Builder().candidateBaseFilename(directory, baseFilename).build();
	}

	/**
	 * Creates a configuration manager that discovers an optional configuration file using a filename glob.
	 * <p>
	 * Only a single directory level is searched, regardless of the glob.
	 * </p>
	 * @param directory The source directory for configuration file discovery.
	 * @param filenameGlob The glob for matching files in the directory, such as <code>foo?.{properties,xml}</code> that would match both <code>foot.xml</code>
	 *          and <code>food.properties</code>.
	 * @return A configuration manager to use a configuration file matched by the given filename glob.
	 * @throws NullPointerException if the directory and/or filename glob is <code>null</code>.
	 */
	public static FileSystemConfigurationManager forCandidateFilenameGlob(@Nonnull final Path directory, @Nonnull final String filenameGlob) {
		return new Builder().candidateFilenameGlob(directory, filenameGlob).build();
	}

	/**
	 * Creates a configuration manager that discovers an optional configuration file using a filename pattern.
	 * @param directory The source directory for configuration file discovery.
	 * @param filenamePattern The regular expression pattern for matching files in the directory.
	 * @return A configuration manager to use a configuration file matched by the given filename pattern.
	 * @throws NullPointerException if the directory and/or filename pattern is <code>null</code>.
	 */
	public static FileSystemConfigurationManager forCandidateFilenamePattern(@Nonnull final Path directory, @Nonnull final Pattern filenamePattern) {
		return new Builder().candidateFilenamePattern(directory, filenamePattern).build();
	}

	/**
	 * Builder for the manager.
	 * <p>
	 * By default the configuration will be optional.
	 * </p>
	 * @author Garret Wilson
	 */
	public static class Builder {

		private Configuration parentConfiguration;

		/**
		 * Sets the parent configuration to use for fallback lookup.
		 * @param parentConfiguration The parent, fallback configuration.
		 * @return This builder.
		 */
		public Builder parentConfiguration(@Nonnull Configuration parentConfiguration) {
			this.parentConfiguration = requireNonNull(parentConfiguration);
			return this;
		}

		private boolean required = false;

		/**
		 * Sets whether the configuration file is required to be discovered.
		 * @param required <code>true</code> if a configuration is required and the manager will always return a configuration and throw an exception if one cannot
		 *          be determined.
		 * @return This builder.
		 */
		public Builder required(final boolean required) {
			this.required = required;
			return this;
		}

		private Supplier<Stream<Path>> candidatePathsSupplier;

		/**
		 * Uses a series of paths as candidate paths for configuration file discovery.
		 * <p>
		 * The first configuration file with a supported format is used.
		 * </p>
		 * @param candidatePaths The potential paths at which to find the configuration file.
		 * @return This builder.
		 * @throws NullPointerException if one of the candidate paths is <code>null</code>.
		 * @throws IllegalArgumentException if no paths are given, or if one of the given paths has no filename.
		 */
		public Builder candidatePaths(@Nonnull final Path... candidatePaths) {
			final Path[] candidatePathsCopy = candidatePaths.clone(); //make a defensive copy of the paths 
			checkArgument(candidatePathsCopy.length > 0, "At least one candidate path must be given.");
			for(final Path candidatePath : candidatePathsCopy) {
				requireNonNull(candidatePath);
				checkArgument(candidatePath.getFileName() != null, "Candidate configuration path %s has no filename.", candidatePath);
			}
			this.candidatePathsSupplier = () -> Stream.of(candidatePathsCopy);
			return this;
		}

		/**
		 * Uses a base path and a base filename.
		 * @param directory The source directory for configuration file discovery.
		 * @param baseFilename The base filename, such as <code>base</code>, to return files with any extension, such as <code>base.foo</code> and
		 *          <code>base.foo.bar</code>, or no extension at all.
		 * @return This builder.
		 * @throws NullPointerException if the directory and/or base filename is <code>null</code>.
		 */
		public Builder candidateBaseFilename(@Nonnull final Path directory, @Nonnull final String baseFilename) {
			return candidateFilenamePattern(directory, Pattern.compile(Pattern.quote(baseFilename) + "\\..+"));
		}

		/**
		 * Uses a base path and a filename glob to discover the configuration file.
		 * <p>
		 * Only a single directory level is searched, regardless of the glob.
		 * </p>
		 * @param directory The source directory for configuration file discovery.
		 * @param filenameGlob The glob for matching files in the directory, such as <code>foo?.{properties,xml}</code> that would match both <code>foot.xml</code>
		 *          and <code>food.properties</code>.
		 * @return This builder.
		 * @throws NullPointerException if the directory and/or filename glob is <code>null</code>.
		 */
		public Builder candidateFilenameGlob(@Nonnull final Path directory, @Nonnull final String filenameGlob) {
			final PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:" + filenameGlob);
			candidatePathsSupplier = () -> {
				try {
					return Files.list(directory).filter(Files::isRegularFile).filter(path -> {
						final Path filename = path.getFileName();
						return filename != null && pathMatcher.matches(filename);
					});
				} catch(final IOException ioException) {
					throw new UncheckedIOException(ioException);
				}
			};
			return this;
		}

		/**
		 * Uses a base path and a filename pattern to discover the configuration file.
		 * @param directory The source directory for configuration file discovery.
		 * @param filenamePattern The regular expression pattern for matching files in the directory.
		 * @return This builder.
		 * @throws NullPointerException if the directory and/or filename pattern is <code>null</code>.
		 */
		public Builder candidateFilenamePattern(@Nonnull final Path directory, @Nonnull final Pattern filenamePattern) {
			final Matcher filenamePatternMatcher = filenamePattern.matcher(""); //create a reusable matcher TODO make more efficient using a wrapper class
			candidatePathsSupplier = () -> {
				try {
					return Files.list(directory).filter(Files::isRegularFile).filter(path -> {
						final Path filename = path.getFileName();
						return filename != null && filenamePatternMatcher.reset(filename.toString()).matches();
					});
				} catch(final IOException ioException) {
					throw new UncheckedIOException(ioException);
				}
			};
			return this;
		}

		/**
		 * Builds a configuration manager.
		 * @return A new manager built to these specifications.
		 * @throws IllegalStateException if no candidate path(s) have been specified.
		 */
		public FileSystemConfigurationManager build() {
			checkState(candidatePathsSupplier != null, "Configuration file candidate path(s) not specified.");
			return new FileSystemConfigurationManager(candidatePathsSupplier, required);
		}

		/**
		 * Builds a manage configured, managed by new file system configuration manager, and using the specified parent configuration, if any.
		 * @return A new configuration managed by a manager built to these specifications.
		 * @throws IllegalStateException if no candidate path(s) have been specified.
		 * @see #parentConfiguration(Configuration)
		 */
		public ManagedConfiguration buildConfiguration() {
			return new ManagedConfiguration(build(), parentConfiguration);
		}

	}

}