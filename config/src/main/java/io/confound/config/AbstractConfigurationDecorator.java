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
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

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
	public Object getObject(final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return getConfiguration().getObject(key);
	}

	@Override
	public Optional<Object> findObject(final String key) throws ConfigurationException {
		return getConfiguration().findObject(key);
	}

	@Override
	public <O> O getObject(final String key, final Class<O> type) throws MissingConfigurationKeyException, ConfigurationException {
		return getConfiguration().getObject(key, type);
	}

	@Override
	public <O> Optional<O> findObject(final String key, final Class<O> type) throws ConfigurationException {
		return getConfiguration().findObject(key, type);
	}

	//Boolean

	@Override
	public boolean getBoolean(final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return getConfiguration().getBoolean(key);
	}

	@Override
	public Optional<Boolean> findBoolean(final String key) throws ConfigurationException {
		return getConfiguration().findBoolean(key);
	}

	//double

	@Override
	public double getDouble(final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return getConfiguration().getDouble(key);
	}

	@Override
	public OptionalDouble findDouble(final String key) throws ConfigurationException {
		return getConfiguration().findDouble(key);
	}

	//int

	@Override
	public int getInt(final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return getConfiguration().getInt(key);
	}

	@Override
	public OptionalInt findInt(final String key) throws ConfigurationException {
		return getConfiguration().findInt(key);
	}

	//long

	@Override
	public long getLong(final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return getConfiguration().getLong(key);
	}

	@Override
	public OptionalLong findLong(final String key) throws ConfigurationException {
		return getConfiguration().findLong(key);
	}

	//Path

	@Override
	public Path getPath(final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return getConfiguration().getPath(key);
	}

	@Override
	public Optional<Path> findPath(final String key) throws ConfigurationException {
		return getConfiguration().findPath(key);
	}

	//String

	@Override
	public String getString(final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return getConfiguration().getString(key);
	}

	@Override
	public Optional<String> findString(final String key) throws ConfigurationException {
		return getConfiguration().findString(key);
	}

	//URI

	@Override
	public URI getUri(final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return getConfiguration().getUri(key);
	}

	@Override
	public Optional<URI> findUri(final String key) throws ConfigurationException {
		return getConfiguration().findUri(key);
	}

}
