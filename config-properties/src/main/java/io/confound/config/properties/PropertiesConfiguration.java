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

package io.confound.config.properties;

import static java.util.Objects.*;

import java.util.*;

import javax.annotation.*;

import io.confound.config.*;

/**
 * Configuration implementation backed by {@link Properties}.
 * @author Garret Wilson
 * @see Properties
 * @see Properties#getProperty(String)
 */
public class PropertiesConfiguration extends AbstractStringConfiguration {

	private final Properties properties;

	/**
	 * Properties constructor.
	 * @param properties The properties to back this configuration.
	 * @throws NullPointerException if the given properties is <code>null</code>.
	 */
	public PropertiesConfiguration(@Nonnull final Properties properties) {
		this(properties, null);
	}

	/**
	 * Parent configuration and properties constructor.
	 * @param properties The properties to back this configuration.
	 * @param parentConfiguration The parent configuration to use for fallback lookup, or <code>null</code> if there is no parent configuration.
	 * @throws NullPointerException if the given properties is <code>null</code>.
	 */
	public PropertiesConfiguration(@Nonnull final Properties properties, @Nullable final Configuration parentConfiguration) {
		super(parentConfiguration);
		this.properties = requireNonNull(properties);
	}

	/** {@inheritDoc} This implementation delegates to {@link Properties#contains(Object)}. */
	@Override
	protected boolean hasParameterImpl(final String key) throws ConfigurationException {
		return properties.containsKey(key);
	}

	/** {@inheritDoc} This implementation delegates to {@link Properties#getProperty(String)}. */
	@Override
	protected Optional<String> findParameterImpl(String key) throws ConfigurationException {
		return Optional.ofNullable(properties.getProperty(key));
	}

}
