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
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

import javax.annotation.*;

import io.confound.convert.Converter;
import io.confound.convert.NumberConverter;

/**
 * Abstract configuration implementation for which the underlying storage is based on general objects.
 * @implSpec The current implementation supports conversion between number types; otherwise this class throws a {@link ConfigurationException} if the requested
 *           object is not of the requested type.
 * @author Garret Wilson
 */
public abstract class AbstractObjectConfiguration extends BaseConfiguration<Object> {

	//currently we use a hard-coded converter that only supports converting numbers
	private final static Converter<?> CONVERTER = NumberConverter.INSTANCE;

	/**
	 * Converts a configuration value from its actual type in the underlying storage to the requested type.
	 * @implSpec The current implementation supports conversion between number types; otherwise this implementation merely checked to see if the value can be cast
	 *           to the requested type.
	 * @param <O> The requested conversion type.
	 * @param value The value to convert.
	 * @param convertClass The class indicating the requested conversion type.
	 * @return The value converted to the requested type.
	 * @throws ConfigurationException if the value is present and cannot be converted to the requested type.
	 */
	protected <O> Optional<O> convertValue(@Nonnull final Optional<Object> value, @Nonnull final Class<O> convertClass) throws ConfigurationException {
		return value.map(object -> {
			try {
				final O convertedObject;
				if(CONVERTER.supportsConvert(object.getClass(), convertClass)) {
					@SuppressWarnings("unchecked")
					final Converter<O> converter = (Converter<O>)CONVERTER;
					convertedObject = converter.convert(object, convertClass);
				} else {
					convertedObject = convertClass.cast(object);
				}
				return convertedObject;
			} catch(final IllegalArgumentException | ClassCastException classCastException) {
				throw new ConfigurationException(classCastException.getMessage(), classCastException);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation converts the value using {@link #convertValue(Optional, Class)}.
	 */
	@Override
	public Optional<Boolean> findBoolean(final String key) throws ConfigurationException {
		return convertValue(findConfigurationValue(key), Boolean.class);
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation converts the value using {@link #convertValue(Optional, Class)}.
	 */
	@Override
	public OptionalDouble findDouble(final String key) throws ConfigurationException {
		final Optional<Double> optionalValue = convertValue(findConfigurationValue(key), Double.class);
		return optionalValue.isPresent() ? OptionalDouble.of(optionalValue.get().doubleValue()) : OptionalDouble.empty();
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation converts the value using {@link #convertValue(Optional, Class)}.
	 */
	@Override
	public OptionalInt findInt(final String key) throws ConfigurationException {
		final Optional<Integer> optionalValue = convertValue(findConfigurationValue(key), Integer.class);
		return optionalValue.isPresent() ? OptionalInt.of(optionalValue.get().intValue()) : OptionalInt.empty();
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation converts the value using {@link #convertValue(Optional, Class)}.
	 */
	@Override
	public OptionalLong findLong(final String key) throws ConfigurationException {
		final Optional<Long> optionalValue = convertValue(findConfigurationValue(key), Long.class);
		return optionalValue.isPresent() ? OptionalLong.of(optionalValue.get().longValue()) : OptionalLong.empty();
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation normalizes the key using {@link #normalizeKey(String)}, converts the value using {@link #convertValue(Optional, Class)}, and
	 *           then resolves the path using {@link #resolvePath(Path)}.
	 */
	@Override
	public Optional<Path> findPath(final String key) throws ConfigurationException {
		return convertValue(findConfigurationValue(key), Path.class).map(this::resolvePath);
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation converts the value using {@link #convertValue(Optional, Class)}.
	 */
	@Override
	public final Optional<String> findString(final String key) throws ConfigurationException {
		return convertValue(findConfigurationValue(key), String.class);
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation converts the value using {@link #convertValue(Optional, Class)}.
	 */
	@Override
	public Optional<URI> findUri(final String key) throws ConfigurationException {
		return convertValue(findConfigurationValue(key), URI.class);
	}

}
