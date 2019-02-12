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
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

/**
 * A configuration implementation that contains no definitions.
 * @author Garret Wilson
 */
public class EmptyConfiguration extends AbstractConfiguration {

	/** The singleton instance of an empty configuration. */
	public static final Configuration INSTANCE = new EmptyConfiguration();

	/** This class cannot be publicly instantiated; only by subclasses. */
	protected EmptyConfiguration() {
	}

	@Override
	public final boolean hasConfigurationValue(final String key) throws ConfigurationException {
		return false;
	}

	@Override
	public <T> Optional<T> findObject(final String key, final Class<T> type) throws ConfigurationException {
		return Optional.empty();
	}

	@Override
	public final OptionalDouble findDouble(final String key) throws ConfigurationException {
		return OptionalDouble.empty();
	}

	@Override
	public final Optional<Boolean> findBoolean(final String key) throws ConfigurationException {
		return Optional.empty();
	}

	@Override
	public final OptionalInt findInt(final String key) throws ConfigurationException {
		return OptionalInt.empty();
	}

	@Override
	public final OptionalLong findLong(final String key) throws ConfigurationException {
		return OptionalLong.empty();
	}

	@Override
	public final Optional<String> findString(final String key) throws ConfigurationException {
		return Optional.empty();
	}

	@Override
	public final Optional<Path> findPath(final String key) throws ConfigurationException {
		return Optional.empty();
	}

	@Override
	public final Optional<URI> findUri(final String key) throws ConfigurationException {
		return Optional.empty();
	}

}
