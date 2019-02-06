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
 * Concrete implementations must override {@link #findConfigurationValueImpl(String)}, and may also override {@link #hasConfigurationValueImpl(String)} for
 * added efficiency.
 * </p>
 * <p>
 * This class provides a facility {@link #normalizeKey(String)} for modifying the requested key if necessary before ultimate retrieval via
 * {@link #findConfigurationValueImpl(String)}. Most implementations will not need this facility, and will use the default implementation which uses the
 * requested key unmodified. In any case, the implementation must use the original key, not the normalized key, when delegating to the parent configuration, if
 * any.
 * </p>
 * @param <T> The type of values used in the underlying storage.
 * @author Garret Wilson
 */
public abstract class BaseConfiguration<T> extends AbstractConfiguration {

	/**
	 * Normalizes a requested key if required by this implementation.
	 * @implSpec The default implementation returns the key unmodified after checking for <code>null</code>.
	 * @param key The configuration key.
	 * @return The requested configuration key, modified as needed for lookup in this implementation.
	 */
	protected String normalizeKey(@Nonnull final String key) {
		return requireNonNull(key);
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation normalizes the key and delegates to {@link #hasConfigurationValueImpl(String)}.
	 * @implNote Most subclasses should not override this method, and instead override {@link #hasConfigurationValueImpl(String)}.
	 * @param key The configuration key.
	 * @return <code>true</code> if a value of any type could be retrieved from this configuration using the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified configuration.
	 * @throws ConfigurationException if there is a configuration value stored in an invalid format.
	 */
	@Override
	public boolean hasConfigurationValue(@Nonnull final String key) throws ConfigurationException {
		return hasConfigurationValueImpl(normalizeKey(key));
	}

	/**
	 * Determines whether a configuration value is present in the underlying storage.
	 * <p>
	 * The given configuration key is assumed to already be normalized, and should not be modified.
	 * </p>
	 * <p>
	 * This method must not fall back to parent configuration; only local values must be returned.
	 * </p>
	 * @implSpec The default implementation delegates to {@link #findConfigurationValueImpl(String)}.
	 * @param key The normalized configuration key.
	 * @return <code>true</code> if a value of any type could be retrieved from this configuration using the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified configuration.
	 * @throws ConfigurationException if there is a configuration value stored in an invalid format.
	 */
	protected boolean hasConfigurationValueImpl(@Nonnull final String key) throws ConfigurationException {
		return findConfigurationValueImpl(key).isPresent();
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation normalizes the key and delegates to {@link #findConfigurationValue(String)}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <P> Optional<P> findObject(final String key) throws ConfigurationException {
		return (Optional<P>)findConfigurationValue(key);
	}

	/**
	 * Tries to retrieves a general configuration value from the underlying storage. The key need not be normalized; it will be normalized as necessary.
	 * <p>
	 * This method must not fall back to parent configuration; only local strings must be returned.
	 * </p>
	 * @implNote This is an internal API call that should be used by child classes to funnel requests to the underlying storage. Normally child classes will not
	 *           override this method, but override {@link #findConfigurationValueImpl(String)} instead.
	 * @param key The configuration key, which may not be normalized.
	 * @return The optional configuration value associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified configuration.
	 * @throws ConfigurationException if there is a configuration value stored in an invalid format.
	 * @see #normalizeKey(String)
	 */
	protected Optional<T> findConfigurationValue(@Nonnull final String key) throws ConfigurationException {
		return findConfigurationValueImpl(normalizeKey(key));
	}

	/**
	 * Implementation to retrieves a general configuration value that may not be present from the underlying storage.
	 * <p>
	 * The given configuration key is assumed to already be normalized, and should not be modified.
	 * </p>
	 * <p>
	 * This method must not fall back to parent configuration; only local values must be returned.
	 * </p>
	 * @param key The normalized configuration key.
	 * @return The optional configuration value associated with the given key.
	 * @throws NullPointerException if the given key is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to the specified configuration.
	 * @throws ConfigurationException if there is a configuration value stored in an invalid format.
	 */
	protected abstract Optional<T> findConfigurationValueImpl(@Nonnull final String key) throws ConfigurationException;

}
