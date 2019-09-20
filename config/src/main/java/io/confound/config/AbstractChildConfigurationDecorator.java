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
import java.util.Collection;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

/**
 * Wrapper configuration that forwards calls to the decorated configuration, falling back to an optional parent configuration.
 * <p>
 * If this is configuration has a parent configuration, each configuration lookup method such as {@link #getString(String)} is expected to attempt to look up
 * the value in the parent configuration if the key is not found in this configuration instance.
 * </p>
 * @implNote This class does not permit <code>getXXX()</code> methods from being overridden, as their default implementation is required to call local methods
 *           in order for fallback to work correctly.
 * @param <C> The type of configuration being decorated.
 * @author Garret Wilson
 */
public abstract class AbstractChildConfigurationDecorator<C extends Configuration> extends AbstractConfiguration {

	/** @return The parent configuration for fallback lookup. */
	protected abstract Optional<C> getParentConfiguration();

	/**
	 * Returns the wrapped configuration.
	 * @return The decorated configuration delegate instance.
	 * @throws ConfigurationException if there is an error retrieving the configuration.
	 */
	protected abstract C getConfiguration() throws ConfigurationException;

	/**
	 * {@inheritDoc}
	 * @implNote This method searches the parent configuration hierarchy if this configuration has a parent configuration and no configuration value is available
	 *           for the key in this instance.
	 */
	@Override
	public boolean hasConfigurationValue(final String key) throws ConfigurationException {
		if(getConfiguration().hasConfigurationValue(key)) {
			return true;
		}
		//this may appear inefficient, but Boolean.valueOf() prevents new object creation so there is probably little overhead
		return getParentConfiguration().map(configuration -> Boolean.valueOf(configuration.hasConfigurationValue(key))).orElse(false);
	}

	//Object

	@Override
	public final Object getObject(final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return super.getObject(key);
	}

	@Override
	public Optional<Object> findObject(final String key) throws ConfigurationException {
		return or(getConfiguration().findObject(key), () -> getParentConfiguration().flatMap(configuration -> configuration.findObject(key)));
	}

	@Override
	public final <O> O getObject(final String key, final Class<O> type) throws MissingConfigurationKeyException, ConfigurationException {
		return super.getObject(key, type);
	}

	@Override
	public <O> Optional<O> findObject(final String key, final Class<O> type) throws ConfigurationException {
		return or(getConfiguration().findObject(key, type), () -> getParentConfiguration().flatMap(configuration -> configuration.findObject(key, type)));
	}

	//Section

	@Override
	public Section getSection(final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return super.getSection(key);
	}

	@Override
	public Optional<Section> findSection(String key) throws ConfigurationException {
		return or(getConfiguration().findSection(key), () -> getParentConfiguration().flatMap(configuration -> configuration.findSection(key)));
	}

	//Collection

	@Override
	public Collection<Object> getCollection(final String key) throws ConfigurationException {
		return super.getCollection(key);
	}

	@Override
	public Optional<Collection<Object>> findCollection(String key) throws ConfigurationException {
		return or(getConfiguration().findCollection(key), () -> getParentConfiguration().flatMap(configuration -> configuration.findCollection(key)));
	}

	@Override
	public <E> Collection<E> getCollection(final String key, final Class<E> elementType) throws ConfigurationException {
		return super.getCollection(key, elementType);
	}

	@Override
	public <E> Optional<Collection<E>> findCollection(final String key, final Class<E> elementType) throws ConfigurationException {
		return or(getConfiguration().findCollection(key, elementType),
				() -> getParentConfiguration().flatMap(configuration -> configuration.findCollection(key, elementType)));
	}

	//Boolean

	@Override
	public final boolean getBoolean(final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return super.getBoolean(key);
	}

	@Override
	public Optional<Boolean> findBoolean(final String key) throws ConfigurationException {
		return or(getConfiguration().findBoolean(key), () -> getParentConfiguration().flatMap(configuration -> configuration.findBoolean(key)));
	}

	//double

	@Override
	public final double getDouble(final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return super.getDouble(key);
	}

	@Override
	public OptionalDouble findDouble(final String key) throws ConfigurationException {
		OptionalDouble value = getConfiguration().findDouble(key);
		if(!value.isPresent()) {
			final Optional<C> parent = getParentConfiguration();
			if(parent.isPresent()) {
				value = parent.get().findDouble(key);
			}
		}
		return value;
	}

	//int

	@Override
	public final int getInt(final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return super.getInt(key);
	}

	@Override
	public OptionalInt findInt(final String key) throws ConfigurationException {
		OptionalInt value = getConfiguration().findInt(key);
		if(!value.isPresent()) {
			final Optional<C> parent = getParentConfiguration();
			if(parent.isPresent()) {
				value = parent.get().findInt(key);
			}
		}
		return value;
	}

	//long

	@Override
	public final long getLong(final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return super.getLong(key);
	}

	@Override
	public OptionalLong findLong(final String key) throws ConfigurationException {
		OptionalLong value = getConfiguration().findLong(key);
		if(!value.isPresent()) {
			final Optional<C> parent = getParentConfiguration();
			if(parent.isPresent()) {
				value = parent.get().findLong(key);
			}
		}
		return value;
	}

	//Path

	@Override
	public final Path getPath(final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return super.getPath(key);
	}

	@Override
	public Optional<Path> findPath(final String key) throws ConfigurationException {
		return or(getConfiguration().findPath(key), () -> getParentConfiguration().flatMap(configuration -> configuration.findPath(key)));
	}

	//String

	@Override
	public final String getString(final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return super.getString(key);
	}

	@Override
	public Optional<String> findString(final String key) throws ConfigurationException {
		return or(getConfiguration().findString(key), () -> getParentConfiguration().flatMap(configuration -> configuration.findString(key)));
	}

	//URI

	@Override
	public final URI getUri(final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return super.getUri(key);
	}

	@Override
	public Optional<URI> findUri(final String key) throws ConfigurationException {
		return or(getConfiguration().findUri(key), () -> getParentConfiguration().flatMap(configuration -> configuration.findUri(key)));
	}

}
