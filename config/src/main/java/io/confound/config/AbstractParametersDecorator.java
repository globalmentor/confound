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
 * Wrapper parameters that forwards calls to the decorated delegate parameters.
 * @author Garret Wilson
 */
public abstract class AbstractParametersDecorator implements Parameters {

	/**
	 * Returns the wrapped parameters.
	 * @return The decorated parameters delegate instance.
	 * @throws ConfigurationException if there is an error retrieving the parameters.
	 */
	protected abstract Parameters getParameters() throws ConfigurationException;

	@Override
	public boolean hasParameter(@Nonnull final String key) throws ConfigurationException {
		return getParameters().hasParameter(key);
	}

	@Override
	public <T> Optional<T> getOptionalParameter(@Nonnull final String key) throws ConfigurationException {
		return getParameters().getOptionalParameter(key);
	}

	@Override
	public Optional<Boolean> getOptionalBoolean(@Nonnull final String key) throws ConfigurationException {
		return getParameters().getOptionalBoolean(key);
	}

	@Override
	public Optional<Double> getOptionalDouble(@Nonnull final String key) throws ConfigurationException {
		return getParameters().getOptionalDouble(key);
	}

	@Override
	public Optional<Integer> getOptionalInt(@Nonnull final String key) throws ConfigurationException {
		return getParameters().getOptionalInt(key);
	}

	@Override
	public Optional<Long> getOptionalLong(@Nonnull final String key) throws ConfigurationException {
		return getParameters().getOptionalLong(key);
	}

	@Override
	public Optional<Path> getOptionalPath(@Nonnull final String key) throws ConfigurationException {
		return getParameters().getOptionalPath(key);
	}

	@Override
	public Optional<String> getOptionalString(@Nonnull final String key) throws ConfigurationException {
		return getParameters().getOptionalString(key);
	}

	@Override
	public Optional<URI> getOptionalUri(@Nonnull final String key) throws ConfigurationException {
		return getParameters().getOptionalUri(key);
	}

}
