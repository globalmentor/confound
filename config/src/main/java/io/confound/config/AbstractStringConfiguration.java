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
import java.nio.file.*;
import java.util.Optional;

import javax.annotation.*;

/**
 * Abstract configuration implementation for which the underlying storage is based solely on strings.
 * <p>
 * An implementing subclass must override {@link #findConfigurationValueImpl(String)} for local raw string retrieval. This class retrieves all values as stored
 * in string format accessed via {@link #findConfigurationValueImpl(String)}, and afterwards dereferenced using {@link #dereferenceString(String)}.
 * </p>
 * @author Garret Wilson
 */
public abstract class AbstractStringConfiguration extends BaseConfiguration<String> {

	/**
	 * Evaluates and replaces any references in the given string.
	 * <p>
	 * This method does not need to be called if the underlying configuration implementation already supports expression replacement.
	 * </p>
	 * @param string The string for which expressions should be evaluated.
	 * @return A string with expressions evaluated, which may be the original string.
	 * @throws NullPointerException if the given string is <code>null</code>.
	 * @throws ConfigurationException if an expression is not in the correct format, or if no value is associated with a key in an expression.
	 */
	protected @Nonnull String dereferenceString(@Nonnull final String string) {
		return requireNonNull(string); //TODO implement; allow for dereference strategy
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation normalizes the key, delegates to {@link #findConfigurationValueImpl(String)}, and then dereferences the string using
	 *           {@link #dereferenceString(String)}.
	 * @see #dereferenceString(String)
	 */
	protected Optional<String> findConfigurationValue(@Nonnull final String key) throws ConfigurationException {
		return findConfigurationValueImpl(normalizeKey(key)).map(this::dereferenceString); //find the string value and evaluate references before passing it back
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation parses the value using {@link Boolean#valueOf(String)}.
	 */
	@Override
	public Optional<Boolean> getOptionalBoolean(final String key) throws ConfigurationException {
		return findConfigurationValue(key).map(Boolean::valueOf);
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation parses the value using {@link Double#valueOf(String)}.
	 */
	@Override
	public Optional<Double> getOptionalDouble(final String key) throws ConfigurationException {
		try {
			return findConfigurationValue(key).map(Double::valueOf);
		} catch(final NumberFormatException numberFormatException) {
			throw new ConfigurationException(numberFormatException);
		}
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation parses the value using {@link Integer#valueOf(String)}.
	 */
	@Override
	public Optional<Integer> getOptionalInt(final String key) throws ConfigurationException {
		try {
			return findConfigurationValue(key).map(Integer::valueOf);
		} catch(final NumberFormatException numberFormatException) {
			throw new ConfigurationException(numberFormatException);
		}
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation parses the value using {@link Long#valueOf(long)}.
	 */
	@Override
	public Optional<Long> getOptionalLong(final String key) throws ConfigurationException {
		try {
			return findConfigurationValue(key).map(Long::valueOf);
		} catch(final NumberFormatException numberFormatException) {
			throw new ConfigurationException(numberFormatException);
		}
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation parses the value using {@link URI#create(String)} and then resolves the path using {@link #resolvePath(Path)}.
	 */
	@Override
	public Optional<Path> getOptionalPath(final String key) throws ConfigurationException {
		try {
			return findConfigurationValue(key).map(Paths::get).map(this::resolvePath);
		} catch(final IllegalArgumentException illegalArgumentException) {
			throw new ConfigurationException(illegalArgumentException);
		}
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation delegates to {@link #findConfigurationValue(String)}.
	 */
	@Override
	public Optional<String> getOptionalString(final String key) throws ConfigurationException {
		return findConfigurationValue(key);
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation parses the value using {@link URI#create(String)}.
	 */
	@Override
	public Optional<URI> getOptionalUri(final String key) throws ConfigurationException {
		try {
			return findConfigurationValue(key).map(URI::create);
		} catch(final IllegalArgumentException illegalArgumentException) {
			throw new ConfigurationException(illegalArgumentException);
		}
	}

}
