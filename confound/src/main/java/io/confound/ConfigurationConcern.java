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

package io.confound;

import javax.annotation.*;

import io.confound.config.*;
import io.csar.*;

/**
 * The concern for configuration.
 * <p>
 * Once this concern is acquired, it provides access to a specific {@link Configuration}.
 * </p>
 * @author Garret Wilson
 * @see Csar
 */
public interface ConfigurationConcern extends Concern {

	@Override
	public default Class<ConfigurationConcern> getConcernType() {
		return ConfigurationConcern.class;
	}

	/**
	 * Retrieves the configured configuration.
	 * @return The configured configuration.
	 * @throws ConfigurationException if there is a configuration error.
	 */
	public @Nonnull Configuration getConfiguration() throws ConfigurationException;

}
