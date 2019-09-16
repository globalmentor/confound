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
import java.util.Map;
import java.util.stream.Stream;

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
	 * Transforms the key appropriately before it is passed to the decorated configuration. The key may be given some prefix or have some prefix removed, for
	 * example, or the case of the key may be changed.
	 * <p>
	 * If no key is returned, it indicates that the given key does not represent a key in this configuration view. For example, if the decorator represents a
	 * subset of the decorator configuration only for those keys starting with <code>foo.</code>, this method would return an empty configuration if a key did not
	 * being with <code>foo.</code>.
	 * </p>
	 * @implSpec The default implementation merely returns the key unchanged.
	 * @apiNote This method is useful for implementing subsets and supersets.
	 * @param key The key as provided by the caller.
	 * @return The key transformed appropriately to be passed to the wrapped configuration.
	 */
	protected Optional<String> decorateKey(@Nonnull final String key) {
		return Optional.of(key);
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
		return decorateKey(key).map(getConfiguration()::hasConfigurationValue).orElse(false);
	}

	//Object

	@Override
	public Object getObject(final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return requireConfiguration(decorateKey(key).map(getConfiguration()::getObject), key);
	}

	@Override
	public Optional<Object> findObject(final String key) throws ConfigurationException {
		return decorateKey(key).map(getConfiguration()::findObject);
	}

	@Override
	public <O> O getObject(final String key, final Class<O> type) throws MissingConfigurationKeyException, ConfigurationException {
		return requireConfiguration(decorateKey(key).map(decoratedKey -> getConfiguration().getObject(decoratedKey, type)), key);
	}

	@Override
	public <O> Optional<O> findObject(final String key, final Class<O> type) throws ConfigurationException {
		return decorateKey(key).flatMap(decoratedKey -> getConfiguration().findObject(decoratedKey, type));
	}

	//Section

	@Override
	public Section getSection(final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return requireConfiguration(decorateKey(key).map(getConfiguration()::getSection), key);
	}

	@Override
	public Optional<Section> findSection(final String key) throws ConfigurationException {
		return decorateKey(key).flatMap(getConfiguration()::findSection);
	}

	@Override
	public Stream<Map.Entry<Optional<String>, Section>> sections() {
		return getConfiguration().sections();
	}

	//Boolean

	@Override
	public boolean getBoolean(final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return requireConfiguration(decorateKey(key).map(getConfiguration()::getBoolean), key);
	}

	@Override
	public Optional<Boolean> findBoolean(final String key) throws ConfigurationException {
		return decorateKey(key).flatMap(getConfiguration()::findBoolean);
	}

	//double

	@Override
	public double getDouble(final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return decorateKey(key).map(getConfiguration()::getDouble).orElseThrow(() -> createMissingConfigurationKeyException(key));
	}

	@Override
	public OptionalDouble findDouble(final String key) throws ConfigurationException {
		return decorateKey(key).map(getConfiguration()::findDouble).orElse(OptionalDouble.empty());
	}

	//int

	@Override
	public int getInt(final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return decorateKey(key).map(getConfiguration()::getInt).orElseThrow(() -> createMissingConfigurationKeyException(key));
	}

	@Override
	public OptionalInt findInt(final String key) throws ConfigurationException {
		return decorateKey(key).map(getConfiguration()::findInt).orElse(OptionalInt.empty());
	}

	//long

	@Override
	public long getLong(final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return decorateKey(key).map(getConfiguration()::getLong).orElseThrow(() -> createMissingConfigurationKeyException(key));
	}

	@Override
	public OptionalLong findLong(final String key) throws ConfigurationException {
		return decorateKey(key).map(getConfiguration()::findLong).orElse(OptionalLong.empty());
	}

	//Path

	@Override
	public Path getPath(final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return requireConfiguration(decorateKey(key).map(getConfiguration()::getPath), key);
	}

	@Override
	public Optional<Path> findPath(final String key) throws ConfigurationException {
		return decorateKey(key).flatMap(getConfiguration()::findPath);
	}

	//String

	@Override
	public String getString(final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return requireConfiguration(decorateKey(key).map(getConfiguration()::getString), key);
	}

	@Override
	public Optional<String> findString(final String key) throws ConfigurationException {
		return decorateKey(key).flatMap(getConfiguration()::findString);
	}

	//URI

	@Override
	public URI getUri(final String key) throws MissingConfigurationKeyException, ConfigurationException {
		return requireConfiguration(decorateKey(key).map(getConfiguration()::getUri), key);
	}

	@Override
	public Optional<URI> findUri(final String key) throws ConfigurationException {
		return decorateKey(key).flatMap(getConfiguration()::findUri);
	}

}
