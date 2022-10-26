/*
 * Copyright © 2018 GlobalMentor, Inc. <https://www.globalmentor.com/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.confound;

import static java.util.Objects.*;

import javax.annotation.*;

import io.confound.config.*;

/**
 * Default implementation of a configuration concern.
 * @author Garret Wilson
 */
public class DefaultConfigurationConcern implements ConfigurationConcern {

	private final Configuration configuration;

	/**
	 * Configuration constructor.
	 * @param configuration The configuration to provide.
	 */
	public DefaultConfigurationConcern(@Nonnull final Configuration configuration) {
		this.configuration = requireNonNull(configuration);
	}

	@Override
	public Configuration getConfiguration() throws ConfigurationException {
		return configuration;
	}

}
