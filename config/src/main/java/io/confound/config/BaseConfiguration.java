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
 * Concrete implementations must override {@link #findParameterImpl(String)}, and may also override {@link #hasParameterImpl(String)} for added efficiency.
 * </p>
 * <p>
 * This class provides a facility {@link #normalizeKey(String)} for modifying the requested key if necessary before ultimate retrieval via
 * {@link #findParameterImpl(String)}. Most implementations will not need this facility, and will use the default implementation which uses the requested key
 * unmodified. In any case, the implementation must use the original key, not the normalized key, when delegating to the parent configuration, if any.
 * </p>
 * @param <T> The type of values used in the underlying storage.
 * @author Garret Wilson
 */
public abstract class BaseConfiguration<T> extends AbstractConfiguration {

	/**
	 * Parent configuration constructor.
	 * @param parentConfiguration The parent configuration to use for fallback lookup, or <code>null</code> if there is no parent configuration.
	 */
	public BaseConfiguration(@Nullable final Configuration parentConfiguration) {
		super(parentConfiguration);
	}

	/**
	 * Normalizes a requested key if required by this implementation.
	 * <p>
	 * The default implementation returns the key unmodified after checking for <code>null</code>.
	 * </p>
	 * @param key The parameter key.
	 * @return The requested parameter key, modified as needed for lookup in this implementation.
	 */
	protected String normalizeKey(@Nonnull final String key) {
		return requireNonNull(key);
	}

	/**
	 * Determines whether a parameter of some type exists for the given parameter key.
	 * <p>
	 * This implementation normalizes the key and delegates to {@link #hasParameterImpl(String)}. Most subclasses should not override this method, and instead
	 * override {@link #hasParameterImpl(String)}.
	 * </p>
	 * @param key The parameter key.
	 * @return <code>true</code> if a parameter of type type could be retrieved from these parameters using the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified parameter.
	 * @throws ConfigurationException if there is a parameter value stored in an invalid format.
	 */
	@Override
	public boolean hasParameter(@Nonnull final String key) throws ConfigurationException {
		if(hasParameterImpl(normalizeKey(key))) {
			return true;
		}
		//this may appear inefficient, but Boolean.valueOf() prevents new object creation so there is probably little overhead
		return getParentConfiguration().map(configuration -> Boolean.valueOf(configuration.hasParameter(key))).orElse(false);
	}

	/**
	 * Determines whether a parameter is present in the underlying storage.
	 * <p>
	 * The given parameter key is assumed to already be normalized, and should not be modified.
	 * </p>
	 * <p>
	 * This method must not fall back to parent configuration; only local values must be returned.
	 * </p>
	 * <p>
	 * The default implementation delegates to {@link #findParameterImpl(String)}.
	 * </p>
	 * @param key The normalized parameter key.
	 * @return <code>true</code> if a parameter of type type could be retrieved from these parameters using the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified parameter.
	 * @throws ConfigurationException if there is a parameter value stored in an invalid format.
	 */
	protected boolean hasParameterImpl(@Nonnull final String key) throws ConfigurationException {
		return findParameterImpl(key).isPresent();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation delegates to {@link #findParameter(String)}.
	 * </p>
	 */
	@Override
	public <P> Optional<P> getOptionalParameter(final String key) throws ConfigurationException {
		@SuppressWarnings("unchecked")
		final Optional<P> optionalObject = (Optional<P>)findParameter(key);
		return or(optionalObject, () -> getParentConfiguration().flatMap(configuration -> configuration.getOptionalParameter(key)));
	}

	/**
	 * Tries to retrieves a general parameter from the underlying storage. The key need not be normalized; it will be normalized as necessary.
	 * <p>
	 * This method must not fall back to parent configuration; only local strings must be returned.
	 * </p>
	 * <p>
	 * This is an internal API call that should be used by child classes to funnel requests to the underlying storage. Normally child classes will not override
	 * this method, but override {@link #findParameterImpl(String)} instead.
	 * </p>
	 * @param key The parameter key, which may not be normalized.
	 * @return The optional value of the parameter associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified parameter.
	 * @throws ConfigurationException if there is a parameter value stored in an invalid format.
	 * @see #normalizeKey(String)
	 */
	protected Optional<T> findParameter(@Nonnull final String key) throws ConfigurationException {
		return findParameterImpl(normalizeKey(key));
	}

	/**
	 * Implementation to retrieves a general parameter that may not be present from the underlying storage.
	 * <p>
	 * The given parameter key is assumed to already be normalized, and should not be modified.
	 * </p>
	 * <p>
	 * This method must not fall back to parent configuration; only local values must be returned.
	 * </p>
	 * @param key The normalized parameter key.
	 * @return The optional value of the parameter associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified parameter.
	 * @throws ConfigurationException if there is a parameter value stored in an invalid format.
	 */
	protected abstract Optional<T> findParameterImpl(@Nonnull final String key) throws ConfigurationException;

}
