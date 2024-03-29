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

package io.confound;

import java.util.*;

import javax.annotation.*;

import io.confound.config.*;
import io.confound.config.env.EnvironmentConfiguration;
import io.confound.config.properties.PropertiesConfiguration;
import io.csar.*;

/**
 * The Configuration Foundation (Confound) library provides a lightweight yet powerful model for accessing various types of application configuration facilities
 * via {@link Csar}.
 * <p>
 * More complex configurations may be set up using {@link Confound#setDefaultConfigurationConcern(ConfigurationConcern)} with the concern of choice, as in the
 * following example:
 * </p>
 * 
 * <pre>
 * {@code
 * Confound.setDefaultConfigurationConcern(new MyConfigurationConcern());
 * }
 * </pre>
 * 
 * @author Garret Wilson
 * @see Csar
 */
public class Confound {

	/** Cached system properties configuration, lazily loaded. */
	private static volatile Configuration systemPropertiesConfiguration = null;

	/**
	 * Retrieves a configuration based on the system properties.
	 * @return Configuration based on system properties.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the system properties.
	 * @see System#getProperties()
	 */
	public static Configuration getSystemPropertiesConfiguration() {
		if(systemPropertiesConfiguration == null) { //the race condition here is benign
			systemPropertiesConfiguration = new PropertiesConfiguration(System.getProperties());
		}
		return systemPropertiesConfiguration;
	}

	/** Cached environment variables configuration, lazily loaded. */
	private static volatile Configuration environmentConfiguration = null;

	/**
	 * Retrieves a configuration based on environment variables.
	 * @return Configuration based on environment variables.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to environment variables.
	 * @see System#getenv()
	 */
	public static Configuration getEnvironmentConfiguration() {
		return getEnvironmentConfiguration(null);
	}

	/**
	 * Retrieves a configuration based on environment variables, with an optional fallback parent configuration.
	 * @param fallbackConfiguration The parent configuration to use for fallback lookup, or <code>null</code> if there is no parent configuration.
	 * @return Configuration based on environment variables.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to environment variables.
	 * @see System#getenv()
	 */
	public static Configuration getEnvironmentConfiguration(@Nullable final Configuration fallbackConfiguration) {
		Configuration configuration = environmentConfiguration;
		if(configuration == null) { //the race condition here is benign
			configuration = new EnvironmentConfiguration(System.getenv());
			environmentConfiguration = configuration; //cache the environment configuration
		}
		if(fallbackConfiguration != null) {
			configuration = configuration.withFallback(fallbackConfiguration);
		}
		return configuration;
	}

	/** Cached system configuration, lazily loaded. */
	private static volatile Configuration systemConfiguration = null;

	/**
	 * Retrieves a configuration for the system, representing system properties that fall back to environment variables. That is the returned configuration
	 * recognizes both environment variables and system properties, with system properties taking precedent. Keys are normalized to match environment variable
	 * conventions, so that a request for <code>foo.bar</code> will match a system property named <code>foo.bar</code> or, if none exists, an environment variable
	 * named <code>FOO_BAR</code>.
	 * @return A configuration for system properties and environment variables.
	 * @see System#getProperties()
	 * @see System#getenv()
	 */
	public static Configuration getSystemConfiguration() {
		return getSystemConfiguration(null);
	}

	/**
	 * Retrieves a configuration for the system, representing system properties that fall back to environment variables, with an optional fallback parent
	 * configuration. That is the returned configuration recognizes both environment variables and system properties, with system properties taking precedent.
	 * Keys are normalized to match environment variable conventions, so that a request for <code>foo.bar</code> will match a system property named
	 * <code>foo.bar</code> or, if none exists, an environment variable named <code>FOO_BAR</code>.
	 * @param fallbackConfiguration The parent configuration to use for fallback lookup, or <code>null</code> if there is no parent configuration.
	 * @return A configuration for system properties and environment variables.
	 * @see System#getProperties()
	 * @see System#getenv()
	 */
	public static Configuration getSystemConfiguration(@Nullable final Configuration fallbackConfiguration) {
		Configuration configuration = systemConfiguration;
		if(configuration == null) { //the race condition here is benign
			configuration = new PropertiesConfiguration(System.getProperties()).withFallback(getEnvironmentConfiguration());
			systemConfiguration = configuration; //cache the system configuration
		}
		if(fallbackConfiguration != null) {
			configuration = configuration.withFallback(fallbackConfiguration);
		}
		return configuration;
	}

	/**
	 * A configuration concern that returns a configuration for system properties and environment variables.
	 * @see #getSystemConfiguration()
	 */
	private static final ConfigurationConcern SYSTEM_CONFIGURATION_CONCERN = new ConfigurationConcern() {

		@Override
		public Configuration getConfiguration() throws ConfigurationException {
			return getSystemConfiguration();
		}

	};

	/**
	 * Returns the default configuration concern.
	 * @return The default configuration concern, if any.
	 * @see Csar#findDefaultConcern(Class)
	 */
	public static Optional<ConfigurationConcern> findDefaultConfigurationConcern() {
		return Csar.findDefaultConcern(ConfigurationConcern.class);
	}

	/**
	 * Sets the default configuration concern.
	 * @param configurationConcern The default configuration concern to set.
	 * @return The previous concern, if any.
	 * @throws NullPointerException if the given concern is <code>null</code>.
	 * @see Csar#registerDefaultConcern(Class, Concern)
	 */
	public static Optional<ConfigurationConcern> setDefaultConfigurationConcern(@Nonnull final ConfigurationConcern configurationConcern) {
		return Csar.registerDefaultConcern(ConfigurationConcern.class, configurationConcern);
	}

	/**
	 * Sets a configuration as the default by installing it in a default configuration concern.
	 * @param configuration The default configuration to set.
	 * @throws NullPointerException if the given configuration is <code>null</code>.
	 * @see DefaultConfigurationConcern
	 * @see #setDefaultConfigurationConcern(ConfigurationConcern)
	 */
	public static void setDefaultConfiguration(@Nonnull final Configuration configuration) {
		setDefaultConfigurationConcern(new DefaultConfigurationConcern(configuration));
	}

	/**
	 * Returns the configured configuration concern.
	 * <p>
	 * If no configuration concern is configured, and no default configuration concern is registered, a configuration concern will be returned that provides
	 * access to the system properties with fallback to environment variables.
	 * </p>
	 * @return The configured configuration concern.
	 * @see Csar#getConcern(Class)
	 * @see #getSystemConfiguration()
	 */
	public static @Nonnull ConfigurationConcern getConfigurationConcern() {
		return Csar.findConcern(ConfigurationConcern.class).orElse(SYSTEM_CONFIGURATION_CONCERN);
	}

	/**
	 * Retrieves the configured configuration.
	 * <p>
	 * This is a convenience method that requests the configuration from the current configuration concern.
	 * </p>
	 * @return Access to configured configuration.
	 * @throws ConfigurationException if there is a configuration error.
	 * @see #getConfigurationConcern()
	 * @see ConfigurationConcern#getConfiguration()
	 */
	public static @Nonnull Configuration getConfiguration() throws ConfigurationException {
		return getConfigurationConcern().getConfiguration();
	}

}
