/*
 * Copyright © 2018 GlobalMentor, Inc. <https://www.globalmentor.com/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
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
		this.properties = requireNonNull(properties);
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation always returns {@link Optional#empty()}, as {@link Properties} do not support sections.
	 */
	@Override
	public Optional<Section> findSection(final String key) throws ConfigurationException {
		return Optional.empty();
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation delegates to {@link Properties#contains(Object)}.
	 */
	@Override
	protected boolean hasConfigurationValueImpl(final String key) throws ConfigurationException {
		return properties.containsKey(key);
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation delegates to {@link Properties#getProperty(String)}.
	 */
	@Override
	protected Optional<String> findConfigurationValueImpl(String key) throws ConfigurationException {
		return Optional.ofNullable(properties.getProperty(key));
	}

}
