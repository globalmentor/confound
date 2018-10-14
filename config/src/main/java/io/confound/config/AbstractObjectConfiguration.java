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
	 * Parent configuration constructor.
	 * @param parentConfiguration The parent configuration to use for fallback lookup, or <code>null</code> if there is no parent configuration.
	 */
	public AbstractObjectConfiguration(@Nullable final Configuration parentConfiguration) {
		super(parentConfiguration);
	}

	/**
	 * Converts a parameter from its actual type in the underlying storage to the requested type.
	 * <p>
	 * The current implementation merely checked to see if the parameter can be cast to the requested type; otherwise, an exception is thrown.
	 * </p>
	 * @param <C> The requested conversion type.
	 * @param parameter The parameter to convert.
	 * @param convertClass The class indicating the requested conversion type.
	 * @return The parameter converted to the requested type.
	 * @throws ConfigurationException if the parameter is present and cannot be converted to the requested type.
	 */
	protected <C> Optional<C> convertParameter(@Nonnull final Optional<Object> parameter, @Nonnull final Class<C> convertClass) throws ConfigurationException {
		try {
			return parameter.map(convertClass::cast);
		} catch(final ClassCastException classCastException) {
			throw new ConfigurationException(classCastException.getMessage(), classCastException);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation converts the value using {@link #convertParameter(Optional, Class)}.
	 * </p>
	 */
	@Override
	public Optional<Boolean> getOptionalBoolean(final String key) throws ConfigurationException {
		return or(convertParameter(findParameter(key), Boolean.class),
				() -> getParentConfiguration().flatMap(configuration -> configuration.getOptionalBoolean(key)));
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation converts the value using {@link #convertParameter(Optional, Class)}.
	 * </p>
	 */
	@Override
	public Optional<Double> getOptionalDouble(final String key) throws ConfigurationException {
		return or(convertParameter(findParameter(key), Double.class),
				() -> getParentConfiguration().flatMap(configuration -> configuration.getOptionalDouble(key)));
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation converts the value using {@link #convertParameter(Optional, Class)}.
	 * </p>
	 */
	@Override
	public Optional<Integer> getOptionalInt(final String key) throws ConfigurationException {
		return or(convertParameter(findParameter(key), Integer.class), () -> getParentConfiguration().flatMap(configuration -> configuration.getOptionalInt(key)));
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation converts the value using {@link #convertParameter(Optional, Class)}.
	 * </p>
	 */
	@Override
	public Optional<Long> getOptionalLong(final String key) throws ConfigurationException {
		return or(convertParameter(findParameter(key), Long.class), () -> getParentConfiguration().flatMap(configuration -> configuration.getOptionalLong(key)));
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation normalizes the key using {@link #normalizeKey(String)}, converts the value using {@link #convertParameter(Optional, Class)}, and then
	 * resolves the path using {@link #resolvePath(Path)}.
	 * </p>
	 */
	@Override
	public Optional<Path> getOptionalPath(final String key) throws ConfigurationException {
		return or(convertParameter(findParameter(key), Path.class).map(this::resolvePath),
				() -> getParentConfiguration().flatMap(configuration -> configuration.getOptionalPath(key)));
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation converts the value using {@link #convertParameter(Optional, Class)}.
	 * </p>
	 */
	@Override
	public final Optional<String> getOptionalString(final String key) throws ConfigurationException {
		return or(convertParameter(findParameter(key), String.class),
				() -> getParentConfiguration().flatMap(configuration -> configuration.getOptionalString(key)));
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation converts the value using {@link #convertParameter(Optional, Class)}.
	 * </p>
	 */
	@Override
	public Optional<URI> getOptionalUri(final String key) throws ConfigurationException {
		return or(convertParameter(findParameter(key), URI.class), () -> getParentConfiguration().flatMap(configuration -> configuration.getOptionalUri(key)));
	}

}
