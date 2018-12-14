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
import java.nio.file.Path;
import java.util.Optional;

import javax.annotation.*;

/**
 * Abstract wrapper configuration that forwards calls to the decorated configuration.
 * @author Garret Wilson
 */
public abstract class AbstractConfigurationDecorator extends AbstractConfiguration {

	private final Configuration configuration;

	/**
	 * Returns the wrapped configuration.
	 * @return The decorated configuration delegate instance.
	 */
	protected Configuration getConfiguration() {
		return configuration;
	}

	/**
	 * Wrapped configuration constructor.
	 * @param configuration The configuration to decorate.
	 * @throws NullPointerException if the given configuration is <code>null</code>.
	 */
	public AbstractConfigurationDecorator(@Nonnull final Configuration configuration) {
		this.configuration = requireNonNull(configuration);
	}

	@Override
	public boolean hasConfigurationValue(final String key) throws ConfigurationException {
		return getConfiguration().hasConfigurationValue(key);
	}

	//Object

	@Override
	public <T> T getObject(final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return getConfiguration().getObject(key);
	}

	@Override
	public <T> Optional<T> getOptionalObject(final String key) throws ConfigurationException {
		return getConfiguration().getOptionalObject(key);
	}

	//Boolean

	@Override
	public boolean getBoolean(final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return getConfiguration().getBoolean(key);
	}

	@Override
	public Optional<Boolean> getOptionalBoolean(final String key) throws ConfigurationException {
		return getConfiguration().getOptionalBoolean(key);
	}

	//double

	@Override
	public double getDouble(final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return getConfiguration().getDouble(key);
	}

	@Override
	public Optional<Double> getOptionalDouble(final String key) throws ConfigurationException {
		return getConfiguration().getOptionalDouble(key);
	}

	//int

	@Override
	public int getInt(final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return getConfiguration().getInt(key);
	}

	@Override
	public Optional<Integer> getOptionalInt(final String key) throws ConfigurationException {
		return getConfiguration().getOptionalInt(key);
	}

	//long

	@Override
	public int getLong(final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return getConfiguration().getLong(key);
	}

	@Override
	public Optional<Long> getOptionalLong(final String key) throws ConfigurationException {
		return getConfiguration().getOptionalLong(key);
	}

	//Path

	@Override
	public Path getPath(final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return getConfiguration().getPath(key);
	}

	@Override
	public Optional<Path> getOptionalPath(final String key) throws ConfigurationException {
		return getConfiguration().getOptionalPath(key);
	}

	//String

	@Override
	public String getString(final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return getConfiguration().getString(key);
	}

	@Override
	public Optional<String> getOptionalString(final String key) throws ConfigurationException {
		return getConfiguration().getOptionalString(key);
	}

	//URI

	@Override
	public URI getUri(final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return getConfiguration().getUri(key);
	}

	@Override
	public Optional<URI> getOptionalUri(final String key) throws ConfigurationException {
		return getConfiguration().getOptionalUri(key);
	}

}
