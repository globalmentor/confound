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

import java.util.*;

import javax.annotation.*;

/**
 * Abstract configuration implementation providing common base functionality.
 * <p>
 * An implementing subclass must override {@link #getOptionalStringImpl(String)} for local raw string retrieval.
 * </p>
 * <p>
 * This class provides a facility {@link #normalizeKey(String)} for modifying the requested key if necessary before ultimate retrieval via
 * {@link #getOptionalStringImpl(String)}. Most implementations will not need this facility, and will use the default implementation which uses the requested
 * key unmodified. In any case, the implementation must use the original key, not the normalized key, when delegating to the parent configuration, if any.
 * </p>
 * @author Garret Wilson
 */
public abstract class BaseConfiguration extends AbstractConfiguration {

	/**
	 * Parent configuration constructor.
	 * @param parentConfiguration The parent configuration to use for fallback lookup, or <code>null</code> if there is no parent configuration.
	 * @throws NullPointerException if the given parent configuration is <code>null</code>.
	 */
	public BaseConfiguration(@Nullable final Configuration parentConfiguration) {
		super(parentConfiguration);
	}

	/** {@inheritDoc} This implementation normalizes the key using {@link #normalizeKey(String)}. */
	@Override
	public final Optional<String> getOptionalString(final String key) throws ConfigurationException {
		return or(getOptionalDereferencedString(normalizeKey(key)), () -> getParentConfiguration().flatMap(configuration -> configuration.getOptionalString(key)));
	}

	/**
	 * Implementation for ultimately retrieving a string parameter.
	 * <p>
	 * This method must not fall back to parent configuration; only local strings must be returned.
	 * </p>
	 * <p>
	 * This implementation delegates to {@link #getOptionalStringImpl(String)}.
	 * </p>
	 * @param key The parameter key.
	 * @return The value of the string parameter associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified parameter.
	 * @throws ConfigurationException if an expression is not in the correct format, or if no parameter is associated with a key in an expression.
	 */
	protected final Optional<String> getOptionalDereferencedString(final String key) throws ConfigurationException {
		return getOptionalStringImpl(key).map(this::dereferenceString); //get the string parameter and evaluate references before passing it back
	}

	/**
	 * Normalizes a requested key if required by this implementation. The default implementation returns the key unmodified after checking for <code>null</code>.
	 * @param key The parameter key.
	 * @return The requested parameter key, modified as needed for lookup in this implementation.
	 */
	protected String normalizeKey(@Nonnull final String key) {
		return requireNonNull(key);
	}

	/**
	 * Implementation for ultimately retrieving a raw string parameter.
	 * <p>
	 * This method should usually be implemented but not called directly by other classes. Callers must invoke {@link #dereferenceString(String)} on the returned
	 * string value.
	 * </p>
	 * <p>
	 * The given parameter key is assumed to already be normalized, and should not be modified.
	 * </p>
	 * <p>
	 * This method must not fall back to parent configuration; only local strings must be returned.
	 * </p>
	 * @param key The exact parameter key.
	 * @return The value of the parameter associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified parameter.
	 * @throws ConfigurationException if an expression is not in the correct format, or if no parameter is associated with a key in an expression.
	 */
	protected abstract Optional<String> getOptionalStringImpl(final String key) throws ConfigurationException;

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

}
