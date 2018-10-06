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
 * Abstract configuration implementation for which the underlying storage is based solely on strings.
 * <p>
 * This class retrieves all parameters as stored in string format based upon {@link #getOptionalStringImpl(String)}.
 * </p>
 * @author Garret Wilson
 */
public abstract class AbstractStringConfiguration extends BaseConfiguration {

	/**
	 * Parent configuration constructor.
	 * @param parentConfiguration The parent configuration to use for fallback lookup, or <code>null</code> if there is no parent configuration.
	 */
	public AbstractStringConfiguration(@Nullable final Configuration parentConfiguration) {
		super(parentConfiguration);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation delegates to {@link #getOptionalStringImpl(String)}.
	 * </p>
	 */
	@Override
	public boolean hasParameter(@Nonnull final String key) throws ConfigurationException {
		return getOptionalStringImpl(key).isPresent();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation delegates to {@link #getOptionalDereferencedString(String)}.
	 * </p>
	 */
	@Override
	public <T> Optional<T> getOptionalParameter(final String key) throws ConfigurationException {
		@SuppressWarnings("unchecked")
		final Optional<T> optionalObject = (Optional<T>)getOptionalDereferencedString(key); //use the dereferenced string as the object
		return or(optionalObject, () -> getParentConfiguration().flatMap(configuration -> configuration.getOptionalParameter(key)));
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation parses the value using {@link Boolean#valueOf(String)}.
	 * </p>
	 */
	@Override
	public Optional<Boolean> getOptionalBoolean(final String key) throws ConfigurationException {
		return or(getOptionalDereferencedString(key).map(Boolean::valueOf),
				() -> getParentConfiguration().flatMap(configuration -> configuration.getOptionalBoolean(key)));
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation parses the value using {@link Double#valueOf(String)}.
	 * </p>
	 */
	@Override
	public Optional<Double> getOptionalDouble(final String key) throws ConfigurationException {
		try {
			return or(getOptionalDereferencedString(key).map(Double::valueOf),
					() -> getParentConfiguration().flatMap(configuration -> configuration.getOptionalDouble(key)));
		} catch(final NumberFormatException numberFormatException) {
			throw new ConfigurationException(numberFormatException);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation parses the value using {@link Integer#valueOf(String)}.
	 * </p>
	 */
	@Override
	public Optional<Integer> getOptionalInt(final String key) throws ConfigurationException {
		try {
			return or(getOptionalDereferencedString(key).map(Integer::valueOf),
					() -> getParentConfiguration().flatMap(configuration -> configuration.getOptionalInt(key)));
		} catch(final NumberFormatException numberFormatException) {
			throw new ConfigurationException(numberFormatException);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation parses the value using {@link Long#valueOf(long)}.
	 * </p>
	 */
	@Override
	public Optional<Long> getOptionalLong(final String key) throws ConfigurationException {
		try {
			return or(getOptionalDereferencedString(key).map(Long::valueOf),
					() -> getParentConfiguration().flatMap(configuration -> configuration.getOptionalLong(key)));
		} catch(final NumberFormatException numberFormatException) {
			throw new ConfigurationException(numberFormatException);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation parses the value using {@link URI#create(String)} and then resolves the path using {@link #resolvePath(Path)}.
	 * </p>
	 */
	@Override
	public Optional<Path> getOptionalPath(final String key) throws ConfigurationException {
		try {
			return or(getOptionalDereferencedString(key).map(Paths::get).map(this::resolvePath),
					() -> getParentConfiguration().flatMap(configuration -> configuration.getOptionalPath(key)));
		} catch(final IllegalArgumentException illegalArgumentException) {
			throw new ConfigurationException(illegalArgumentException);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation parses the value using {@link URI#create(String)}.
	 * </p>
	 */
	@Override
	public Optional<URI> getOptionalUri(final String key) throws ConfigurationException {
		try {
			return or(getOptionalDereferencedString(key).map(URI::create),
					() -> getParentConfiguration().flatMap(configuration -> configuration.getOptionalUri(key)));
		} catch(final IllegalArgumentException illegalArgumentException) {
			throw new ConfigurationException(illegalArgumentException);
		}
	}

}
