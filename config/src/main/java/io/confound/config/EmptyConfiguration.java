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

/**
 * A configuration implementation that contains no definitions.
 * @author Garret Wilson
 */
final class EmptyConfiguration extends AbstractConfiguration {

	/** The singleton instance of an empty configuration. */
	public static final Configuration INSTANCE = new EmptyConfiguration();

	/** This class cannot be publicly instantiated. */
	private EmptyConfiguration() {
	}

	@Override
	public boolean hasConfigurationValue(final String key) throws ConfigurationException {
		return false;
	}

	@Override
	public <T> Optional<T> getOptionalObject(final String key) throws ConfigurationException {
		return Optional.empty();
	}

	@Override
	public Optional<Double> getOptionalDouble(final String key) throws ConfigurationException {
		return Optional.empty();
	}

	@Override
	public Optional<Boolean> getOptionalBoolean(final String key) throws ConfigurationException {
		return Optional.empty();
	}

	@Override
	public Optional<Integer> getOptionalInt(final String key) throws ConfigurationException {
		return Optional.empty();
	}

	@Override
	public Optional<Long> getOptionalLong(final String key) throws ConfigurationException {
		return Optional.empty();
	}

	@Override
	public Optional<String> getOptionalString(final String key) throws ConfigurationException {
		return Optional.empty();
	}

	@Override
	public Optional<Path> getOptionalPath(final String key) throws ConfigurationException {
		return Optional.empty();
	}

	@Override
	public Optional<URI> getOptionalUri(final String key) throws ConfigurationException {
		return Optional.empty();
	}

}
