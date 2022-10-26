/*
 * Copyright © 2018 GlobalMentor, Inc. <https://www.globalmentor.com/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.confound.config.file;

import static com.globalmentor.io.Filenames.*;
import static com.globalmentor.java.Conditions.*;
import static java.nio.file.Files.*;
import static java.util.Collections.*;
import static java.util.Objects.*;

import java.io.*;
import java.nio.file.*;
import java.time.Instant;
import java.util.*;
import java.util.function.Supplier;
import java.util.regex.*;
import java.util.stream.Stream;

import javax.annotation.*;

import com.globalmentor.io.Filenames;

import io.clogr.Clogged;
import io.confound.config.*;

/**
 * A configuration manager that can load and parse a configuration from the file system.
 * @author Garret Wilson
 */
public class FileSystemConfigurationManager extends AbstractFileConfigurationManager implements Clogged {

	/** The default base filename to use for determining a file system resource. */
	public static final String DEFAULT_BASE_FILENAME = "config";

	/** The strategy for determining candidate paths for finding a configuration. */
	private final Supplier<Stream<Path>> configurationFileCandidatePathsSupplier;

	/** Information about the determined configuration path, or <code>null</code> if the configuration path has not been determined or has been invalidated. */
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
		this(defaultFileFormats(), configurationFileCandidatePathsSupplier, required);
	}

	/**
	 * File formats, candidate paths supplier, and optional required constructor.
	 * <p>
	 * The path supplier is allowed to throw {@link UncheckedIOException} when the stream is returned and during stream iteration.
	 * </p>
	 * @param fileFormats The file formats to support.
	 * @param configurationFileCandidatePathsSupplier The strategy for determining candidate paths for finding a configuration.
	 * @param required Whether the manager requires a configuration to be determined when loading.
	 */
	protected FileSystemConfigurationManager(@Nonnull final Iterable<ConfigurationFileFormat> fileFormats,
			@Nonnull Supplier<Stream<Path>> configurationFileCandidatePathsSupplier, final boolean required) {
		super(fileFormats, required);
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
	 * @implSpec This implementation uses the existing configuration path if it has been determined. If the configuration path has not been determined, such as if
	 *           this manager has been invalidated using {@link #invalidate()}, it determines a new configuration path and updates the record.
	 */
	@Override
	public synchronized Optional<Configuration> loadConfiguration() throws IOException, ConfigurationException {
		Path configurationPath = this.configurationPathInfo != null ? this.configurationPathInfo.getPath().orElse(null) : null;
		ConfigurationFileFormat fileFormat = null; //we may determine the file format during searching the candidate paths, or directly
		if(configurationPath == null || !isRegularFile(configurationPath)) { //find a configuration path if we don't already have one, or it doesn't exist anymore
			configurationPath = null; //assume we can't find it (in case we had one and it no longer exists)
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
				configuration = fileFormat.load(inputStream);
			}
		} else { //if we couldn't determine a configuration path
			if(isRequired()) {
				throw createConfigurationNotFoundException();
			}
			configuration = null;
		}
		this.configurationPathInfo = new PathInfo(configurationPath); //save our current configuration path; we are now no longer stale
		return Optional.ofNullable(configuration);
	}

	/** {@inheritDoc} This implementation does not yet support saving configurations, and will throw an exception. */
	@Override
	public void saveConfiguration(final Configuration configuration) throws IOException {
		throw new UnsupportedOperationException("Saving configurations not yet supported.");
	}

	/** {@inheritDoc} This version additionally checks to see if whether there is a cached configuration path. */
	@Override
	public boolean isStale(final Configuration configuration) throws IOException {
		if(super.isStale(configuration)) {
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

	//static factory methods

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
	 * @throws NullPointerException if one of the paths is <code>null</code>.
	 * @throws IllegalArgumentException if no paths are given, or if one of the given paths has no filename.
	 */
	public static FileSystemConfigurationManager forCandidatePaths(@Nonnull final Path... configurationCandidatePaths) {
		return new Builder().candidatePaths(configurationCandidatePaths).build();
	}

	/**
	 * Creates a configuration manager that discovers an optional configuration file using the default base filename {@value #DEFAULT_BASE_FILENAME}.
	 * @param directory The source directory for configuration file discovery.
	 * @return A configuration manager to use a configuration file with the given base name.
	 * @throws NullPointerException if the directory is <code>null</code>.
	 * @see #forBaseFilename(Path, String)
	 * @see #DEFAULT_BASE_FILENAME
	 */
	public static FileSystemConfigurationManager forDirectory(@Nonnull final Path directory) {
		return forBaseFilename(directory, DEFAULT_BASE_FILENAME);
	}

	/**
	 * Creates a configuration manager that discovers an optional configuration file using a base filename.
	 * @param directory The source directory for configuration file discovery.
	 * @param baseFilename The base filename, such as <code>base</code>, to return files with any extension, such as <code>base.foo</code> and
	 *          <code>base.foo.bar</code>, or no extension at all.
	 * @return A configuration manager to use a configuration file with the given base name.
	 * @throws NullPointerException if the directory and/or base filename is <code>null</code>.
	 */
	public static FileSystemConfigurationManager forBaseFilename(@Nonnull final Path directory, @Nonnull final String baseFilename) {
		return new Builder().baseFilename(directory, baseFilename).build();
	}

	/**
	 * Creates a configuration manager that discovers an optional configuration file using a filename glob.
	 * <p>
	 * Only a single directory level is searched, regardless of the glob.
	 * </p>
	 * @param directory The source directory for configuration file discovery.
	 * @param filenameGlob The glob for matching files in the directory, such as <code>foo?.{properties,xml}</code> which would match both <code>foot.xml</code>
	 *          and <code>food.properties</code>.
	 * @return A configuration manager to use a configuration file matched by the given filename glob.
	 * @throws NullPointerException if the directory and/or filename glob is <code>null</code>.
	 */
	public static FileSystemConfigurationManager forFilenameGlob(@Nonnull final Path directory, @Nonnull final String filenameGlob) {
		return new Builder().filenameGlob(directory, filenameGlob).build();
	}

	/**
	 * Creates a configuration manager that discovers an optional configuration file using a filename pattern.
	 * @param directory The source directory for configuration file discovery.
	 * @param filenamePattern The regular expression pattern for matching files in the directory.
	 * @return A configuration manager to use a configuration file matched by the given filename pattern.
	 * @throws NullPointerException if the directory and/or filename pattern is <code>null</code>.
	 */
	public static FileSystemConfigurationManager forFilenamePattern(@Nonnull final Path directory, @Nonnull final Pattern filenamePattern) {
		return new Builder().filenamePattern(directory, filenamePattern).build();
	}

	//direct loading utility methods

	/**
	 * Loads an optional configuration from a given path as necessary.
	 * @param configurationPath The path at which to find the configuration file.
	 * @return The loaded configuration, which will not be present if no appropriate configuration was found.
	 * @throws IOException if an I/O error occurs loading the configuration.
	 * @throws ConfigurationException If there is invalid data or invalid state preventing the configuration from being loaded.
	 */
	public static Optional<Configuration> loadConfigurationForPath(@Nonnull final Path configurationPath) throws IOException, ConfigurationException {
		return loadConfigurationForCandidatePaths(configurationPath);
	}

	/**
	 * Discovers and loads an optional configuration file from one of the given paths.
	 * <p>
	 * The first configuration file with a supported format is used.
	 * </p>
	 * @param configurationCandidatePaths The potential paths at which to find the configuration file.
	 * @return The loaded configuration, which will not be present if no appropriate configuration was found.
	 * @throws NullPointerException if one of the paths is <code>null</code>.
	 * @throws IllegalArgumentException if no paths are given, or if one of the given paths has no filename.
	 * @throws IOException if an I/O error occurs loading the configuration.
	 * @throws ConfigurationException If there is invalid data or invalid state preventing the configuration from being loaded.
	 */
	public static Optional<Configuration> loadConfigurationForCandidatePaths(@Nonnull final Path... configurationCandidatePaths)
			throws IOException, ConfigurationException {
		return new Builder().candidatePaths(configurationCandidatePaths).build().loadConfiguration();
	}

	/**
	 * Discovers and loads an optional configuration file using the default base filename {@value #DEFAULT_BASE_FILENAME}.
	 * @param directory The source directory for configuration file discovery.
	 * @return The loaded configuration, which will not be present if no appropriate configuration was found.
	 * @throws NullPointerException if the directory is <code>null</code>.
	 * @throws IOException if an I/O error occurs loading the configuration.
	 * @throws ConfigurationException If there is invalid data or invalid state preventing the configuration from being loaded.
	 * @see #forBaseFilename(Path, String)
	 * @see #DEFAULT_BASE_FILENAME
	 */
	public static Optional<Configuration> loadConfigurationForDirectory(@Nonnull final Path directory) throws IOException, ConfigurationException {
		return loadConfigurationForBaseFilename(directory, DEFAULT_BASE_FILENAME);
	}

	/**
	 * Discovers and loads an optional configuration file using a base filename.
	 * @param directory The source directory for configuration file discovery.
	 * @param baseFilename The base filename, such as <code>base</code>, to return files with any extension, such as <code>base.foo</code> and
	 *          <code>base.foo.bar</code>, or no extension at all.
	 * @return The loaded configuration, which will not be present if no appropriate configuration was found.
	 * @throws NullPointerException if the directory and/or base filename is <code>null</code>.
	 * @throws IOException if an I/O error occurs loading the configuration.
	 * @throws ConfigurationException If there is invalid data or invalid state preventing the configuration from being loaded.
	 */
	public static Optional<Configuration> loadConfigurationForBaseFilename(@Nonnull final Path directory, @Nonnull final String baseFilename)
			throws IOException, ConfigurationException {
		return new Builder().baseFilename(directory, baseFilename).build().loadConfiguration();
	}

	/**
	 * Discovers and loads an optional configuration file using a filename glob.
	 * <p>
	 * Only a single directory level is searched, regardless of the glob.
	 * </p>
	 * @param directory The source directory for configuration file discovery.
	 * @param filenameGlob The glob for matching files in the directory, such as <code>foo?.{properties,xml}</code> which would match both <code>foot.xml</code>
	 *          and <code>food.properties</code>.
	 * @return The loaded configuration, which will not be present if no appropriate configuration was found.
	 * @throws NullPointerException if the directory and/or filename glob is <code>null</code>.
	 * @throws IOException if an I/O error occurs loading the configuration.
	 * @throws ConfigurationException If there is invalid data or invalid state preventing the configuration from being loaded.
	 */
	public static Optional<Configuration> loadConfigurationForFilenameGlob(@Nonnull final Path directory, @Nonnull final String filenameGlob)
			throws IOException, ConfigurationException {
		return new Builder().filenameGlob(directory, filenameGlob).build().loadConfiguration();
	}

	/**
	 * Discovers and loads an optional configuration file using a filename pattern.
	 * @param directory The source directory for configuration file discovery.
	 * @param filenamePattern The regular expression pattern for matching files in the directory.
	 * @return The loaded configuration, which will not be present if no appropriate configuration was found.
	 * @throws NullPointerException if the directory and/or filename pattern is <code>null</code>.
	 * @throws IOException if an I/O error occurs loading the configuration.
	 * @throws ConfigurationException If there is invalid data or invalid state preventing the configuration from being loaded.
	 */
	public static Optional<Configuration> loadConfigurationForFilenamePattern(@Nonnull final Path directory, @Nonnull final Pattern filenamePattern)
			throws IOException, ConfigurationException {
		return new Builder().filenamePattern(directory, filenamePattern).build().loadConfiguration();
	}

	//builder

	/**
	 * Builder for the manager.
	 * <p>
	 * By default the configuration will be optional. The file formats installed from their providers will be used if none are specified.
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

		/**
		 * Sets a single file format to be supported by the configuration manager.
		 * @param fileFormat The file format to support.
		 * @return This builder.
		 */
		public Builder fileFormat(@Nonnull final ConfigurationFileFormat fileFormat) {
			return fileFormats(singleton(requireNonNull(fileFormat)));
		}

		private Iterable<ConfigurationFileFormat> fileFormats = AbstractFileConfigurationManager.defaultFileFormats();

		/**
		 * Sets the file formats to be supported by the configuration manager.
		 * @param fileFormats The file formats to support.
		 * @return This builder.
		 */
		public Builder fileFormats(@Nonnull final Iterable<ConfigurationFileFormat> fileFormats) {
			this.fileFormats = requireNonNull(fileFormats);
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
		 * @see Filenames#getBaseFilenamePattern(String)
		 */
		public Builder baseFilename(@Nonnull final Path directory, @Nonnull final String baseFilename) {
			return filenamePattern(directory, getBaseFilenamePattern(baseFilename));
		}

		/**
		 * Uses a base path and a filename glob to discover the configuration file.
		 * <p>
		 * Only a single directory level is searched, regardless of the glob.
		 * </p>
		 * @param directory The source directory for configuration file discovery.
		 * @param filenameGlob The glob for matching files in the directory, such as <code>foo?.{properties,xml}</code> which would match both <code>foot.xml</code>
		 *          and <code>food.properties</code>.
		 * @return This builder.
		 * @throws NullPointerException if the directory and/or filename glob is <code>null</code>.
		 */
		public Builder filenameGlob(@Nonnull final Path directory, @Nonnull final String filenameGlob) {
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
		public Builder filenamePattern(@Nonnull final Path directory, @Nonnull final Pattern filenamePattern) {
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
			return new FileSystemConfigurationManager(fileFormats, candidatePathsSupplier, required);
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
