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
 * Wrapper configuration that forwards calls to the decorated configuration, falling back to an optional parent configuration.
 * <p>
 * If this is configuration has a parent configuration, each configuration lookup method such as {@link #getString(String)} is expected to attempt to look up
 * the value in the parent configuration if the key is not found in this configuration instance.
 * </p>
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
	public boolean hasConfigurationValue(@Nonnull final String key) throws ConfigurationException {
		if(getConfiguration().hasConfigurationValue(key)) {
			return true;
		}
		//this may appear inefficient, but Boolean.valueOf() prevents new object creation so there is probably little overhead
		return getParentConfiguration().map(configuration -> Boolean.valueOf(configuration.hasConfigurationValue(key))).orElse(false);
	}

	//TODO do we need to forward the default methods as well?

	@Override
	public <T> Optional<T> getOptionalObject(@Nonnull final String key) throws ConfigurationException {
		return or(getConfiguration().getOptionalObject(key), () -> getParentConfiguration().flatMap(configuration -> configuration.getOptionalObject(key)));
	}

	//Boolean

	@Override
	public Optional<Boolean> getOptionalBoolean(@Nonnull final String key) throws ConfigurationException {
		return or(getConfiguration().getOptionalBoolean(key), () -> getParentConfiguration().flatMap(configuration -> configuration.getOptionalBoolean(key)));
	}

	//double

	@Override
	public Optional<Double> getOptionalDouble(@Nonnull final String key) throws ConfigurationException {
		return or(getConfiguration().getOptionalDouble(key), () -> getParentConfiguration().flatMap(configuration -> configuration.getOptionalDouble(key)));
	}

	//int

	@Override
	public Optional<Integer> getOptionalInt(@Nonnull final String key) throws ConfigurationException {
		return or(getConfiguration().getOptionalInt(key), () -> getParentConfiguration().flatMap(configuration -> configuration.getOptionalInt(key)));
	}

	//long

	@Override
	public Optional<Long> getOptionalLong(@Nonnull final String key) throws ConfigurationException {
		return or(getConfiguration().getOptionalLong(key), () -> getParentConfiguration().flatMap(configuration -> configuration.getOptionalLong(key)));
	}

	//Path

	@Override
	public Optional<Path> getOptionalPath(@Nonnull final String key) throws ConfigurationException {
		return or(getConfiguration().getOptionalPath(key), () -> getParentConfiguration().flatMap(configuration -> configuration.getOptionalPath(key)));
	}

	//String

	@Override
	public Optional<String> getOptionalString(@Nonnull final String key) throws ConfigurationException {
		return or(getConfiguration().getOptionalString(key), () -> getParentConfiguration().flatMap(configuration -> configuration.getOptionalString(key)));
	}

	//URI

	@Override
	public Optional<URI> getOptionalUri(@Nonnull final String key) throws ConfigurationException {
		return or(getConfiguration().getOptionalUri(key), () -> getParentConfiguration().flatMap(configuration -> configuration.getOptionalUri(key)));
	}

}
