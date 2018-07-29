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

import io.confound.config.Configuration;
import io.confound.config.ConfigurationException;

/**
 * Mixin interface to provide quick-and-easy configuration support to a class.
 * <p>
 * A class implementing this interface can simply call the {@link #getConfiguration()} method to retrieve the current configuration.
 * </p>
 * @author Garret Wilson
 */
public interface Confounded {

	/**
	 * Retrieves the configured configuration.
	 * @return The current configuration.
	 * @throws ConfigurationException if there is a configuration error.
	 * @see Confound#getConfiguration()
	 */
	public default @Nonnull Configuration getConfiguration() throws ConfigurationException {
		return Confound.getConfiguration();
	}

}
