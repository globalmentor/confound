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

import java.net.URI;
import java.nio.file.*;
import java.util.Optional;

import javax.annotation.*;

/**
 * Abstract configuration implementation for which the underlying storage is based on general objects.
 * <p>
 * In the current implementation, this class throws a {@link ConfigurationException} if the requested object is not of the requested type. A future version will
 * allow values to be converted between compatible types.
 * </p>
 * @author Garret Wilson
 */
public abstract class AbstractObjectConfiguration extends BaseConfiguration<Object> {

	/**
	 * Converts a configuration value from its actual type in the underlying storage to the requested type.
	 * <p>
	 * The current implementation merely checked to see if the value can be cast to the requested type; otherwise, an exception is thrown.
	 * </p>
	 * @param <T> The requested conversion type.
	 * @param value The value to convert.
	 * @param convertClass The class indicating the requested conversion type.
	 * @return The value converted to the requested type.
	 * @throws ConfigurationException if the value is present and cannot be converted to the requested type.
	 */
	protected <T> Optional<T> convertValue(@Nonnull final Optional<Object> value, @Nonnull final Class<T> convertClass) throws ConfigurationException {
		try {
			return value.map(convertClass::cast);
		} catch(final ClassCastException classCastException) {
			throw new ConfigurationException(classCastException.getMessage(), classCastException);
		}
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation converts the value using {@link #convertValue(Optional, Class)}.
	 */
	@Override
	public Optional<Boolean> getOptionalBoolean(final String key) throws ConfigurationException {
		return convertValue(findConfigurationValue(key), Boolean.class);
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation converts the value using {@link #convertValue(Optional, Class)}.
	 */
	@Override
	public Optional<Double> getOptionalDouble(final String key) throws ConfigurationException {
		return convertValue(findConfigurationValue(key), Double.class);
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation converts the value using {@link #convertValue(Optional, Class)}.
	 */
	@Override
	public Optional<Integer> getOptionalInt(final String key) throws ConfigurationException {
		return convertValue(findConfigurationValue(key), Integer.class);
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation converts the value using {@link #convertValue(Optional, Class)}.
	 */
	@Override
	public Optional<Long> getOptionalLong(final String key) throws ConfigurationException {
		return convertValue(findConfigurationValue(key), Long.class);
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation normalizes the key using {@link #normalizeKey(String)}, converts the value using {@link #convertValue(Optional, Class)}, and
	 *           then resolves the path using {@link #resolvePath(Path)}.
	 */
	@Override
	public Optional<Path> getOptionalPath(final String key) throws ConfigurationException {
		return convertValue(findConfigurationValue(key), Path.class).map(this::resolvePath);
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation converts the value using {@link #convertValue(Optional, Class)}.
	 */
	@Override
	public final Optional<String> getOptionalString(final String key) throws ConfigurationException {
		return convertValue(findConfigurationValue(key), String.class);
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation converts the value using {@link #convertValue(Optional, Class)}.
	 */
	@Override
	public Optional<URI> getOptionalUri(final String key) throws ConfigurationException {
		return convertValue(findConfigurationValue(key), URI.class);
	}

}
