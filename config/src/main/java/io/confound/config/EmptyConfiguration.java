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
import java.nio.file.Path;
import java.util.Optional;

import javax.annotation.*;

/**
 * A configuration implementation that contains no definitions.
 * <p>
 * This implementation will automatically delegate to the parent configuration, if any.
 * </p>
 * @author Garret Wilson
 */
public final class EmptyConfiguration extends AbstractConfiguration {

	/** No-argument constructor. */
	public EmptyConfiguration() {
		this(Optional.empty());
	}

	/**
	 * Context class and parent configuration constructor.
	 * @param parentConfiguration The parent configuration for fallback lookup.
	 * @throws NullPointerException if the given parent configuration is <code>null</code>.
	 */
	public EmptyConfiguration(@Nonnull final Configuration parentConfiguration) {
		this(Optional.of(parentConfiguration));
	}

	/**
	 * Optional parent configuration constructor.
	 * @param parentConfiguration The parent configuration for fallback lookup.
	 * @throws NullPointerException if the given parent configuration is <code>null</code>.
	 */
	public EmptyConfiguration(@Nonnull final Optional<Configuration> parentConfiguration) {
		super(parentConfiguration);
	}

	@Override
	public boolean hasParameter(String key) throws ConfigurationException {
		return false;
	}

	@Override
	public <T> Optional<T> getOptionalParameter(String key) throws ConfigurationException {
		return getParentConfiguration().flatMap(configuration -> configuration.getOptionalParameter(key));
	}

	@Override
	public Optional<Double> getOptionalDouble(String key) throws ConfigurationException {
		return getParentConfiguration().flatMap(configuration -> configuration.getOptionalDouble(key));
	}

	@Override
	public Optional<Boolean> getOptionalBoolean(String key) throws ConfigurationException {
		return getParentConfiguration().flatMap(configuration -> configuration.getOptionalBoolean(key));
	}

	@Override
	public Optional<Integer> getOptionalInt(String key) throws ConfigurationException {
		return getParentConfiguration().flatMap(configuration -> configuration.getOptionalInt(key));
	}

	@Override
	public Optional<Long> getOptionalLong(String key) throws ConfigurationException {
		return getParentConfiguration().flatMap(configuration -> configuration.getOptionalLong(key));
	}

	@Override
	public Optional<String> getOptionalString(String key) throws ConfigurationException {
		return getParentConfiguration().flatMap(configuration -> configuration.getOptionalString(key));
	}

	@Override
	public Optional<Path> getOptionalPath(String key) throws ConfigurationException {
		return getParentConfiguration().flatMap(configuration -> configuration.getOptionalPath(key));
	}

	@Override
	public Optional<URI> getOptionalUri(String key) throws ConfigurationException {
		return getParentConfiguration().flatMap(configuration -> configuration.getOptionalUri(key));
	}

}
