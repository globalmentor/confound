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

import java.util.Optional;

import javax.annotation.*;

/**
 * A wrapper configuration that forwards calls to the decorated configuration, falling back to a parent configuration.
 * @author Garret Wilson
 */
public class ChildConfigurationDecorator extends AbstractChildConfigurationDecorator {

	private final Optional<Configuration> parentConfiguration;

	/**
	 * {@inheritDoc}
	 * @implSpec This version will never return {@link Optional#empty()}.
	 */
	@Override
	protected Optional<Configuration> getParentConfiguration() {
		return parentConfiguration;
	}

	private final Configuration configuration;

	@Override
	protected Configuration getConfiguration() throws ConfigurationException {
		return configuration;
	}

	/**
	 * Wrapped configuration constructor.
	 * @param configuration The configuration to decorate.
	 * @throws NullPointerException if the given configuration is <code>null</code>.
	 */
	public ChildConfigurationDecorator(@Nonnull final Configuration configuration) {
		this(configuration, null);
	}

	/**
	 * Wrapped configuration and parent configuration constructor.
	 * @param configuration The configuration to decorate.
	 * @param parentConfiguration The parent configuration to use for fallback lookup.
	 * @throws NullPointerException if the given configuration and/or parent configuration is <code>null</code>.
	 */
	public ChildConfigurationDecorator(@Nonnull Configuration configuration, @Nonnull final Configuration parentConfiguration) {
		this.configuration = requireNonNull(configuration);
		this.parentConfiguration = Optional.of(requireNonNull(parentConfiguration));
	}

}
