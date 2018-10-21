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

package io.confound.app;

import static java.util.Objects.*;

import java.nio.file.*;

import javax.annotation.Nonnull;

import io.confound.*;
import io.confound.config.*;
import io.confound.config.file.FileSystemConfigurationManager;

/**
 * Convenience class to assist in creating a configuration stack for applications.
 * <p>
 * This builder sets up a configuration manager to find a configuration file in
 * <code><var>baseDirPath</var>/<var>appDataDir</var>/<var>configBaseName.*</var></code>. By default the user directory is used for
 * <code><var>baseDirPath</var></code> and <code>config</code> is used for <var>configBaseName.*</var>. Thus an application merely has to specify
 * <code><var>appDataDir</var></code>, either as an absolute path or a path relative to <code><var>baseDirPath</var></code>.
 * </p>
 * <p>
 * In addition this builder recognizes the {@value #CONFIG_KEY_APP_DATA_DIR} configuration key, and allows its value to be indicate in a system property or an
 * environment variable to specify the <code><var>appDataDir</var></code>, overriding one specified in this builder if present.
 * </p>
 * <p>
 * If no application directory can be determine, a configuration exception is thrown.
 * </p>
 * <p>
 * If a single configuration file is desired, one should create one specifically using {@link FileSystemConfigurationManager}.
 * </p>
 * @author Garret Wilson
 */
public class AppConfigurationConcernBuilder {

	/**
	 * The standard configuration key for the application data directory, either absolute or relative to the base directory (which defaults to the user home
	 * directory).
	 */
	public static final String CONFIG_KEY_APP_DATA_DIR = "app.data.dir";

	/** The base filename to use if none is specified. */
	public static final String DEFAULT_CONFIG_BASE_FILENAME = "config";

	private Path baseDirectory = Paths.get(System.getProperty("user.home"));

	/**
	 * Sets the base directory to use when calculating a relative data directory.
	 * <p>
	 * This value defaults to the user's home directory. This property is not relevant unless a relative application directory is also set. If you want to specify
	 * an absolute data directory for the application, use {@link #defaultAppDataDirectory(Path)} instead.
	 * </p>
	 * @param baseDirPath The base directory to use when calculating a relative data directory.
	 * @return This builder.
	 */
	public AppConfigurationConcernBuilder baseDirectoryPath(@Nonnull final Path baseDirPath) {
		this.baseDirectory = requireNonNull(baseDirPath);
		return this;
	}

	private Path defaultAppDataDirectory = null;

	/**
	 * Sets the default application data directory.
	 * @param defaultAppDataDir The default application data directory to use if none is specified in the system properties or an environment variable; either
	 *          absolute or relative to the base directory path.
	 * @return This builder.
	 * @see #baseDirectoryPath(Path)
	 */
	public AppConfigurationConcernBuilder defaultAppDataDirectory(@Nonnull final Path defaultAppDataDir) {
		this.defaultAppDataDirectory = requireNonNull(defaultAppDataDir);
		return this;
	}

	private String configBaseFilename = DEFAULT_CONFIG_BASE_FILENAME;

	//TODO document
	public ConfigurationConcern build() throws ConfigurationException {
		final Configuration systemConfiguration = Confound.getSystemConfiguration(); //TODO refactor to a method so subclasses can customize

		final Path baseDirectory = this.baseDirectory;
		final Path appDataDirectory = systemConfiguration.getOptionalPath(CONFIG_KEY_APP_DATA_DIR).orElse(this.defaultAppDataDirectory);
		if(appDataDirectory == null) {
			throw new ConfigurationException("No application data directory could be determined.");
		}
		final Path directory = baseDirectory.resolve(appDataDirectory); //the app data directory can be relative or absolute

		final Configuration fileSystemConfiguration = new FileSystemConfigurationManager.Builder().baseFilename(directory, configBaseFilename)
				.buildConfiguration();

		//TODO should we allow the system configuration to override the file system configuration?

		return new DefaultConfigurationConcern(fileSystemConfiguration);
	}

}
