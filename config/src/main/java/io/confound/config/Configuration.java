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

import java.util.Optional;

import javax.annotation.*;

/**
 * Access to configuration parameters.
 * <p>
 * Each parameter lookup method such as {@link #getString(String)} is expected to attempt to look up resources in the {@link #getParentConfiguration()} (if any)
 * if the resource is not found in this resources instance.
 * </p>
 * @author Garret Wilson
 */
public interface Configuration extends Parameters {

	/** @return The parent configuration for fallback lookup. */
	public Optional<Configuration> getParentConfiguration();

	/**
	 * {@inheritDoc}
	 * <p>
	 * This method searches the parent resources hierarchy if no parameter is available in this instance.
	 * </p>
	 */
	public boolean hasParameter(@Nonnull final String key) throws ConfigurationException;

}
