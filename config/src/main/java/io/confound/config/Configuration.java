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

package io.confound.config;

import static java.util.Objects.*;

import java.net.URI;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Optional;

import javax.annotation.*;

/**
 * Access to configuration values.
 * @author Garret Wilson
 */
public interface Configuration {

	/** @return An empty configuration. */
	public static Configuration empty() {
		return EmptyConfiguration.INSTANCE;
	}

	/**
	 * Retrieves a required configuration from an {@link Optional}, throwing a {@link MissingConfigurationKeyException} if the configuration key was not present.
	 * @apiNote This method is primarily used to check the result of a configuration lookup call for the non-optional convenience configuration lookup versions.
	 * @implSpec The default implementation delegates to {@link #createMissingConfigurationKeyException(String)}.
	 * @param <T> The type of configuration value to check.
	 * @param value The retrieved value.
	 * @param key The configuration key.
	 * @return The retrieved value.
	 * @throws MissingConfigurationKeyException if the given configuration is not present.
	 * @see Optional#isPresent()
	 */
	public default <T> T requireConfiguration(@Nonnull final Optional<T> value, @Nonnull final String key) throws MissingConfigurationKeyException {
		return value.orElseThrow(() -> createMissingConfigurationKeyException(key));
	}

	/**
	 * Creates an exception indicating that a given configuration key could not be found.
	 * @param key The configuration key.
	 * @return The new exception.
	 */
	public default MissingConfigurationKeyException createMissingConfigurationKeyException(@Nonnull final String key) {
		return new MissingConfigurationKeyException(String.format("Missing configuration for key %s.", key), requireNonNull(key));
	}

	/**
	 * Determines whether a configuration of some type exists for the given configuration key.
	 * @param key The configuration key.
	 * @return <code>true</code> if a value of any type could be retrieved from this configuration using the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified configuration.
	 * @throws ConfigurationException if there is a configuration value stored in an invalid format.
	 */
	public boolean hasConfigurationValue(@Nonnull final String key) throws ConfigurationException;

	/**
	 * Retrieves a general configuration object.
	 * @implSpec This default version delegates to {@link #findObject(String)}.
	 * @param <T> The type of configuration object expected.
	 * @param key The configuration key.
	 * @return The configuration value associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified configuration.
	 * @throws MissingConfigurationKeyException if no configuration is associated with the given key.
	 * @throws ConfigurationException if there is a configuration value stored in an invalid format.
	 */
	public default @Nonnull <T> T getObject(@Nonnull final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return requireConfiguration(findObject(key), key);
	}

	/**
	 * Retrieves a general configuration object that may not be present.
	 * @param <T> The type of configuration object expected.
	 * @param key The configuration key.
	 * @return The optional configuration value associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified configuration.
	 * @throws ConfigurationException if there is a configuration value stored in an invalid format.
	 */
	public <T> Optional<T> findObject(@Nonnull final String key) throws ConfigurationException;

	//Boolean

	/**
	 * Retrieves a Boolean configuration value.
	 * @implSpec This default version delegates to {@link #findBoolean(String)}.
	 * @param key The configuration key.
	 * @return The configuration value associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified configuration.
	 * @throws MissingConfigurationKeyException if no configuration is associated with the given key.
	 * @throws ConfigurationException if there is a configuration value stored in an invalid format.
	 */
	public default @Nonnull boolean getBoolean(@Nonnull final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return requireConfiguration(findBoolean(key), key).booleanValue();
	}

	/**
	 * Retrieves a Boolean configuration value that may not be present.
	 * @param key The configuration key.
	 * @return The optional configuration value associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified configuration.
	 * @throws ConfigurationException if there is a configuration value stored in an invalid format.
	 */
	public Optional<Boolean> findBoolean(@Nonnull final String key) throws ConfigurationException;

	//double

	/**
	 * Retrieves a floating point configuration value.
	 * @implSpec This default version delegates to {@link #findDouble(String)}.
	 * @param key The configuration key.
	 * @return The configuration value associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified configuration.
	 * @throws MissingConfigurationKeyException if no configuration is associated with the given key.
	 * @throws ConfigurationException if there is a configuration value stored in an invalid format.
	 */
	public default @Nonnull double getDouble(@Nonnull final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return requireConfiguration(findDouble(key), key).doubleValue();
	}

	/**
	 * Retrieves a floating point configuration value that may not be present.
	 * @param key The configuration key.
	 * @return The optional configuration value associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified configuration.
	 * @throws ConfigurationException if there is a configuration value stored in an invalid format.
	 */
	public Optional<Double> findDouble(@Nonnull final String key) throws ConfigurationException;

	//int

	/**
	 * Retrieves an integer configuration value.
	 * @implSpec This default version delegates to {@link #findInt(String)}.
	 * @param key The configuration key.
	 * @return The configuration value associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified configuration.
	 * @throws MissingConfigurationKeyException if no configuration is associated with the given key.
	 * @throws ConfigurationException if there is a configuration value stored in an invalid format.
	 */
	public default @Nonnull int getInt(@Nonnull final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return requireConfiguration(findInt(key), key).intValue();
	}

	/**
	 * Retrieves an integer configuration value that may not be present.
	 * @param key The configuration key.
	 * @return The optional configuration value associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified configuration.
	 * @throws ConfigurationException if there is a configuration value stored in an invalid format.
	 */
	public Optional<Integer> findInt(@Nonnull final String key) throws ConfigurationException;

	//long

	/**
	 * Retrieves a long integer configuration value.
	 * @implSpec This default version delegates to {@link #findLong(String)}.
	 * @param key The configuration key.
	 * @return The configuration value associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified configuration.
	 * @throws MissingConfigurationKeyException if no configuration is associated with the given key.
	 * @throws ConfigurationException if there is a configuration value stored in an invalid format.
	 */
	public default @Nonnull long getLong(@Nonnull final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return requireConfiguration(findLong(key), key).longValue();
	}

	/**
	 * Retrieves a long integer configuration value that may not be present.
	 * @param key The configuration key.
	 * @return The optional configuration value associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified configuration.
	 * @throws ConfigurationException if there is a configuration value stored in an invalid format.
	 */
	public default Optional<Long> findLong(@Nonnull final String key) throws ConfigurationException {
		return findInt(key).map(Integer::intValue).map(Long::valueOf); //this apparently uses auto-unboxing and autoboxing  
	}

	//Path

	/**
	 * Retrieves a path configuration value.
	 * <p>
	 * The path will be resolved using {@link #resolvePath(Path)}.
	 * </p>
	 * @implSpec This default version delegates to {@link #findPath(String)}.
	 * @param key The configuration key.
	 * @return The configuration value associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified configuration.
	 * @throws MissingConfigurationKeyException if no configuration is associated with the given key.
	 * @throws ConfigurationException if there is a configuration value stored in an invalid format.
	 */
	public default @Nonnull Path getPath(@Nonnull final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return requireConfiguration(findPath(key), key);
	}

	/**
	 * Retrieves a path configuration value that may not be present.
	 * <p>
	 * The path will be resolved using {@link #resolvePath(Path)}.
	 * </p>
	 * @param key The configuration key.
	 * @return The optional configuration value associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified configuration.
	 * @throws ConfigurationException if there is a configuration value stored in an invalid format.
	 */
	public Optional<Path> findPath(@Nonnull final String key) throws ConfigurationException;

	/**
	 * Resolves the given path as appropriate. Absolute paths should not be modified. Relative paths may be resolved to some standard or configured absolute path,
	 * depending on the implementation. A common base path may be configured separately, stored elsewhere in the configuration, or encoded in the path string
	 * itself for example.
	 * @implSpec The default implementation merely returns the given path.
	 * @param path The path to resolve.
	 * @return A resolved form of the path if appropriate.
	 */
	public default Path resolvePath(@Nonnull final Path path) { //TODO probably move out of interfaces; should not be part of decoration
		return requireNonNull(path);
	}

	//String

	/**
	 * Retrieves a string configuration value.
	 * <p>
	 * TODO discuss dereferencing
	 * </p>
	 * @implSpec This default version delegates to {@link #findString(String)}.
	 * @param key The configuration key.
	 * @return The configuration value associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified configuration.
	 * @throws MissingConfigurationKeyException if no configuration is associated with the given key.
	 * @throws ConfigurationException if there is a configuration value stored in an invalid format.
	 * @see MessageFormat#format(Object)
	 */
	public default @Nonnull String getString(@Nonnull final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return requireConfiguration(findString(key), key);
	}

	/**
	 * Retrieves a string configuration value that may not be present.
	 * <p>
	 * TODO discuss dereferencing
	 * </p>
	 * @param key The configuration key.
	 * @return The optional configuration value associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified configuration.
	 * @throws ConfigurationException if there is a configuration value stored in an invalid format.
	 * @see MessageFormat#format(Object)
	 */
	public Optional<String> findString(@Nonnull final String key) throws ConfigurationException;

	//URI

	/**
	 * Retrieves a URI configuration value.
	 * @implSpec This default version delegates to {@link #findUri(String)}.
	 * @param key The configuration key.
	 * @return The configuration value associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified configuration.
	 * @throws MissingConfigurationKeyException if no configuration is associated with the given key.
	 * @throws ConfigurationException if there is a configuration value stored in an invalid format.
	 */
	public default @Nonnull URI getUri(@Nonnull final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return requireConfiguration(findUri(key), key);
	}

	/**
	 * Retrieves a URI configuration value that may not be present.
	 * @param key The configuration key.
	 * @return The optional configuration value associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified configuration.
	 * @throws ConfigurationException if there is a configuration value stored in an invalid format.
	 */
	public Optional<URI> findUri(@Nonnull final String key) throws ConfigurationException;

	/**
	 * Returns a configuration equivalent to this configuration but that will fall back to a specified parent configuration if a value is not present. This
	 * configuration will remain unmodified.
	 * @param fallbackConfiguration The fallback configuration.
	 * @return A version of this configuration that uses fallback lookup.
	 * @throws NullPointerException if the given fallback configuration is <code>null</code>.
	 */
	public default Configuration withFallback(@Nonnull final Configuration fallbackConfiguration) {
		return new ChildConfigurationDecorator(this, fallbackConfiguration);
	}

	/**
	 * Utility method that returns a configuration equivalent to the given configuration but that will fall back to an optional parent configuration if a value is
	 * not present. The given configuration will remain unmodified.
	 * @param configuration The configuration to optionally be given a fallback.
	 * @param fallbackConfiguration The optional fallback configuration.
	 * @return A version of the configuration that uses fallback lookup or, if no fallback is present, the given configuration.
	 * @throws NullPointerException if the given configuration and/or optional fallback configuration is <code>null</code>.
	 * @see #withFallback(Configuration)
	 */
	public static Configuration withFallback(@Nonnull final Configuration configuration, @Nonnull final Optional<Configuration> fallbackConfiguration) {
		return fallbackConfiguration.map(configuration::withFallback).orElse(configuration);
	}

}
