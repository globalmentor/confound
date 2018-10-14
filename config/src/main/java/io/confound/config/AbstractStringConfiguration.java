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
 * An implementing subclass must override {@link #findParameterImpl(String)} for local raw string retrieval. This class retrieves all parameters as stored in
 * string format accessed via {@link #findParameterImpl(String)}, and afterwards dereferenced using {@link #dereferenceString(String)}.
 * </p>
 * @author Garret Wilson
 */
public abstract class AbstractStringConfiguration extends BaseConfiguration<String> {

	/**
	 * Parent configuration constructor.
	 * @param parentConfiguration The parent configuration to use for fallback lookup, or <code>null</code> if there is no parent configuration.
	 */
	public AbstractStringConfiguration(@Nullable final Configuration parentConfiguration) {
		super(parentConfiguration);
	}

	/**
	 * Evaluates and replaces any references in the given string.
	 * <p>
	 * This method does not need to be called if the underlying configuration implementation already supports expression replacement.
	 * </p>
	 * @param string The string for which expressions should be evaluated.
	 * @return A string with expressions evaluated, which may be the original string.
	 * @throws NullPointerException if the given string is <code>null</code>.
	 * @throws ConfigurationException if an expression is not in the correct format, or if no parameter is associated with a key in an expression.
	 */
	protected @Nonnull String dereferenceString(@Nonnull final String string) {
		return requireNonNull(string); //TODO implement; allow for dereference strategy
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation normalizes the key, delegates to {@link #findParameter(String)}, and then dereferences the string using
	 * {@link #dereferenceString(String)}.
	 * </p>
	 * @see #dereferenceString(String)
	 */
	protected Optional<String> findParameter(@Nonnull final String key) throws ConfigurationException {
		return findParameterImpl(normalizeKey(key)).map(this::dereferenceString); //find the string parameter and evaluate references before passing it back
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation parses the value using {@link Boolean#valueOf(String)}.
	 * </p>
	 */
	@Override
	public Optional<Boolean> getOptionalBoolean(final String key) throws ConfigurationException {
		return or(findParameter(key).map(Boolean::valueOf), () -> getParentConfiguration().flatMap(configuration -> configuration.getOptionalBoolean(key)));
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
			return or(findParameter(key).map(Double::valueOf), () -> getParentConfiguration().flatMap(configuration -> configuration.getOptionalDouble(key)));
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
			return or(findParameter(key).map(Integer::valueOf), () -> getParentConfiguration().flatMap(configuration -> configuration.getOptionalInt(key)));
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
			return or(findParameter(key).map(Long::valueOf), () -> getParentConfiguration().flatMap(configuration -> configuration.getOptionalLong(key)));
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
			return or(findParameter(key).map(Paths::get).map(this::resolvePath),
					() -> getParentConfiguration().flatMap(configuration -> configuration.getOptionalPath(key)));
		} catch(final IllegalArgumentException illegalArgumentException) {
			throw new ConfigurationException(illegalArgumentException);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation delegates to {@link #findParameter(String)}.
	 * </p>
	 */
	@Override
	public final Optional<String> getOptionalString(final String key) throws ConfigurationException {
		return or(findParameter(key), () -> getParentConfiguration().flatMap(configuration -> configuration.getOptionalString(key)));
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
			return or(findParameter(key).map(URI::create), () -> getParentConfiguration().flatMap(configuration -> configuration.getOptionalUri(key)));
		} catch(final IllegalArgumentException illegalArgumentException) {
			throw new ConfigurationException(illegalArgumentException);
		}
	}

}
