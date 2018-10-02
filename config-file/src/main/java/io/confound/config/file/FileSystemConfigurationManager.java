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
import java.util.stream.Stream;

import javax.annotation.*;

import io.clogr.Clogged;
import io.confound.config.*;

/**
 * A configuration factory that can load and parse a configuration from the file system.
 * @author Garret Wilson
 */
public abstract class FileSystemConfigurationManager extends AbstractFileConfigurationManager implements Clogged {

	/** Information about the determined configuration path, or <code>null</code> if the configuration path has not been determined or has been validated. */
	private PathInfo configurationPathInfo = null;

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
			try (final Stream<Path> candidatePaths = configurationFileCandidatePaths()) { //be sure to close the stream of paths
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

	/**
	 * Returns a stream of paths to check to use for loading the configuration. Each path must have a non-<code>null</code> {@link Path#getFileName()}.
	 * @return The paths for searching for a configuration file.
	 */
	protected abstract Stream<Path> configurationFileCandidatePaths();

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
	 * Creates a configuration manager that loads a configuration from a given path as necessary.
	 * @param configurationPath The path at which to find the configuration file.
	 * @return A configuration manager for the file at the given path.
	 */
	public static FileSystemConfigurationManager forPath(@Nonnull final Path configurationPath) {
		return forCandidatePaths(configurationPath);
	}

	/**
	 * Creates a configuration manager that loads a configuration from one of the given paths.
	 * <p>
	 * The first configuration file with a supported format is used.
	 * </p>
	 * @param configurationCandidatePaths The potential paths at which to find the configuration file.
	 * @return A configuration manager for one of the files at the given paths.
	 */
	public static FileSystemConfigurationManager forCandidatePaths(@Nonnull final Path... configurationCandidatePaths) {
		for(final Path candidatePath : configurationCandidatePaths) {
			requireNonNull(candidatePath);
			checkArgument(candidatePath.getFileName() != null, "Candidate configuration path %s has no filename.", candidatePath);
		}
		return new FileSystemConfigurationManager() {
			@Override
			protected Stream<Path> configurationFileCandidatePaths() {
				return Stream.of(configurationCandidatePaths);
			}
		};
	}

	//TODO add static factory methods for base path and regex/glob

}
