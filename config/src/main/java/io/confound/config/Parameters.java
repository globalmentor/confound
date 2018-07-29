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
import java.util.*;

import javax.annotation.*;

/**
 * General access to configuration parameters.
 * @author Garret Wilson
 */
public interface Parameters {

	/**
	 * Retrieves a required parameter from an {@link Optional}, throwing a {@link MissingParameterException} if the parameter not present.
	 * <p>
	 * This method is primarily used to check the result of a parameter lookup call for the non-optional convenience parameter lookup versions.
	 * </p>
	 * @param <T> The type of parameter to check.
	 * @param parameter The retrieved parameter.
	 * @param key The parameter key.
	 * @return The retrieved parameter.
	 * @throws MissingParameterException if the given parameter is not present.
	 * @see Optional#isPresent()
	 */
	public default <T> T requireParameter(@Nonnull final Optional<T> parameter, @Nonnull final String key) throws MissingParameterException {
		return parameter.orElseThrow(() -> new MissingParameterException(String.format("Missing parameter for key %s.", key), requireNonNull(key)));
	}

	/**
	 * Determines whether a parameter of some type exists for the given parameter key.
	 * @param key The parameter key.
	 * @return <code>true</code> if a parameter of type type could be retrieved from these parameters using the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified parameter.
	 * @throws ConfigurationException if there is a parameter value stored in an invalid format.
	 */
	public boolean hasParameter(@Nonnull final String key) throws ConfigurationException;

	/**
	 * Retrieves a general parameter.
	 * @param <T> The type of parameter expected.
	 * @param key The parameter key.
	 * @return The value of the parameter associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified parameter.
	 * @throws MissingParameterException if no parameter is associated with the given key.
	 * @throws ConfigurationException if there is a parameter value stored in an invalid format.
	 */
	public default @Nonnull <T> T getParameter(@Nonnull final String key) throws MissingParameterException, ConfigurationException {
		return requireParameter(getOptionalParameter(key), key);
	}

	/**
	 * Retrieves a general parameter that may not be present.
	 * @param <T> The type of parameter expected.
	 * @param key The parameter key.
	 * @return The optional value of the parameter associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified parameter.
	 * @throws ConfigurationException if there is a parameter value stored in an invalid format.
	 */
	public <T> Optional<T> getOptionalParameter(@Nonnull final String key) throws ConfigurationException;

	//Boolean

	/**
	 * Retrieves a Boolean parameter.
	 * @param key The parameter key.
	 * @return The value of the parameter associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified parameter.
	 * @throws MissingParameterException if no parameter is associated with the given key.
	 * @throws ConfigurationException if there is a parameter value stored in an invalid format.
	 */
	public default @Nonnull boolean getBoolean(@Nonnull final String key) throws MissingParameterException, ConfigurationException {
		return requireParameter(getOptionalBoolean(key), key).booleanValue();
	}

	/**
	 * Retrieves a Boolean parameter that may not be present.
	 * @param key The parameter key.
	 * @return The optional value of the parameter associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified parameter.
	 * @throws ConfigurationException if there is a parameter value stored in an invalid format.
	 */
	public Optional<Boolean> getOptionalBoolean(@Nonnull final String key) throws ConfigurationException;

	//double

	/**
	 * Retrieves a floating point parameter.
	 * @param key The parameter key.
	 * @return The value of the parameter associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified parameter.
	 * @throws MissingParameterException if no parameter is associated with the given key.
	 * @throws ConfigurationException if there is a parameter value stored in an invalid format.
	 */
	public default @Nonnull double getDouble(@Nonnull final String key) throws MissingParameterException, ConfigurationException {
		return requireParameter(getOptionalDouble(key), key).doubleValue();
	}

	/**
	 * Retrieves a floating point parameter that may not be present.
	 * @param key The parameter key.
	 * @return The optional value of the parameter associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified parameter.
	 * @throws ConfigurationException if there is a parameter value stored in an invalid format.
	 */
	public Optional<Double> getOptionalDouble(@Nonnull final String key) throws ConfigurationException;

	//int

	/**
	 * Retrieves an integer parameter.
	 * @param key The parameter key.
	 * @return The value of the parameter associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified parameter.
	 * @throws MissingParameterException if no parameter is associated with the given key.
	 * @throws ConfigurationException if there is a parameter value stored in an invalid format.
	 */
	public default @Nonnull int getInt(@Nonnull final String key) throws MissingParameterException, ConfigurationException {
		return requireParameter(getOptionalInt(key), key).intValue();
	}

	/**
	 * Retrieves an integer parameter that may not be present.
	 * @param key The parameter key.
	 * @return The optional value of the parameter associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified parameter.
	 * @throws ConfigurationException if there is a parameter value stored in an invalid format.
	 */
	public Optional<Integer> getOptionalInt(@Nonnull final String key) throws ConfigurationException;

	//long

	/**
	 * Retrieves a long integer parameter.
	 * @param key The parameter key.
	 * @return The value of the parameter associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified parameter.
	 * @throws MissingParameterException if no parameter is associated with the given key.
	 * @throws ConfigurationException if there is a parameter value stored in an invalid format.
	 */
	public default @Nonnull int getLong(@Nonnull final String key) throws MissingParameterException, ConfigurationException {
		return requireParameter(getOptionalLong(key), key).intValue();
	}

	/**
	 * Retrieves a long integer parameter that may not be present.
	 * <p>
	 * The default implementation delegates to {@link #getOptionalInt(String)}.
	 * </p>
	 * @param key The parameter key.
	 * @return The optional value of the parameter associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified parameter.
	 * @throws ConfigurationException if there is a parameter value stored in an invalid format.
	 */
	public default Optional<Long> getOptionalLong(@Nonnull final String key) throws ConfigurationException {
		return getOptionalInt(key).map(Integer::intValue).map(Long::valueOf); //this apparently uses auto-unboxing and autoboxing  
	}

	//Path

	/**
	 * Retrieves a path parameter.
	 * <p>
	 * The path will be resolved using {@link #resolvePath(Path)}.
	 * </p>
	 * @param key The parameter key.
	 * @return The value of the parameter associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified parameter.
	 * @throws MissingParameterException if no parameter is associated with the given key.
	 * @throws ConfigurationException if there is a parameter value stored in an invalid format.
	 */
	public default @Nonnull Path getPath(@Nonnull final String key) throws MissingParameterException, ConfigurationException {
		return requireParameter(getOptionalPath(key), key);
	}

	/**
	 * Retrieves a path parameter that may not be present.
	 * <p>
	 * The path will be resolved using {@link #resolvePath(Path)}.
	 * </p>
	 * @param key The parameter key.
	 * @return The optional value of the parameter associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified parameter.
	 * @throws ConfigurationException if there is a parameter value stored in an invalid format.
	 */
	public Optional<Path> getOptionalPath(@Nonnull final String key) throws ConfigurationException;

	/**
	 * Resolves the given path as appropriate. Absolute paths should not be modified. Relative paths may be resolved to some standard or configured absolute path,
	 * depending on the implementation. A common base path may be configured separately, stored elsewhere in the parameters, or encoded in the path string itself
	 * for example.
	 * <p>
	 * The default implementation merely returns the given path.
	 * </p>
	 * @param path The path to resolve.
	 * @return A resolved form of the path if appropriate.
	 */
	public default Path resolvePath(@Nonnull final Path path) {
		return requireNonNull(path);
	}

	//String

	/**
	 * Retrieves a string parameter.
	 * <p>
	 * TODO discuss dereferencing
	 * </p>
	 * @param key The parameter key.
	 * @return The value of the parameter associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified parameter.
	 * @throws MissingParameterException if no parameter is associated with the given key.
	 * @throws ConfigurationException if there is a parameter value stored in an invalid format.
	 * @see MessageFormat#format(Object)
	 */
	public default @Nonnull String getString(@Nonnull final String key) throws MissingParameterException, ConfigurationException {
		return requireParameter(getOptionalString(key), key);
	}

	/**
	 * Retrieves a string parameter that may not be present.
	 * <p>
	 * TODO discuss dereferencing
	 * </p>
	 * @param key The parameter key.
	 * @return The optional value of the parameter associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified parameter.
	 * @throws ConfigurationException if there is a parameter value stored in an invalid format.
	 * @see MessageFormat#format(Object)
	 */
	public Optional<String> getOptionalString(@Nonnull final String key) throws ConfigurationException;

	//URI

	/**
	 * Retrieves a URI parameter.
	 * @param key The parameter key.
	 * @return The value of the parameter associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified parameter.
	 * @throws MissingParameterException if no parameter is associated with the given key.
	 * @throws ConfigurationException if there is a parameter value stored in an invalid format.
	 */
	public default @Nonnull URI getUri(@Nonnull final String key) throws MissingParameterException, ConfigurationException {
		return requireParameter(getOptionalUri(key), key);
	}

	/**
	 * Retrieves a URI parameter that may not be present.
	 * @param key The parameter key.
	 * @return The optional value of the parameter associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified parameter.
	 * @throws ConfigurationException if there is a parameter value stored in an invalid format.
	 */
	public Optional<URI> getOptionalUri(@Nonnull final String key) throws ConfigurationException;

}
