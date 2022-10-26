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

package io.confound.config;

import java.io.IOException;
import java.util.Optional;

import javax.annotation.*;

/**
 * A strategy for retrieving and storing configurations.
 * @author Garret Wilson
 */
public interface ConfigurationManager {

	/**
	 * Returns whether this configuration manager requires a configuration to be determined. If a configuration is required, attempting to load a configuration
	 * will throw an exception if a configuration cannot be determined; otherwise, it will return {@link Optional#empty()}.
	 * @return <code>true</code> if a configuration is required and this manager will always return a configuration and throw an exception if one cannot be
	 *         determined.
	 * @see #loadConfiguration()
	 */
	public boolean isRequired();

	/**
	 * Loads a configuration.
	 * <p>
	 * It is imperative that the configuration manager assign the given parent configuration, if any, to the loaded configuration so that chained configuration
	 * resolution will work.
	 * </p>
	 * <p>
	 * After this method has been called, it is expected that {@link #isStale(Configuration)} would return <code>false</code> barring concurrent changes.
	 * </p>
	 * @return The loaded configuration, which will not be present if no appropriate configuration was found.
	 * @throws IOException if an I/O error occurs loading the configuration.
	 * @throws ConfigurationException If there is invalid data or invalid state preventing the configuration from being loaded, or if no configuration was found
	 *           for a required configuration.
	 * @see #isRequired()
	 */
	public Optional<Configuration> loadConfiguration() throws IOException, ConfigurationException;

	/**
	 * Saves the configuration.
	 * @param configuration The configuration to save.
	 * @throws IOException if an error occurs saving the configuration.
	 */
	public void saveConfiguration(@Nonnull final Configuration configuration) throws IOException;

	/**
	 * Determines whether the currently managed configuration is stale.
	 * <p>
	 * If this method returns <code>false</code>, the caller should call {@link #invalidate()} before reloading the configuration.
	 * </p>
	 * @param configuration The currently loaded configuration.
	 * @return Whether the currently loaded configuration is stale and needs to be reloaded.
	 * @throws IOException if an error occurs checking the configuration.
	 */
	public default boolean isStale(@Nonnull final Configuration configuration) throws IOException {
		return false;
	}

	/**
	 * Invalidates any cached information about the managed configuration.
	 * @implSpec The default implementation does nothing.
	 */
	public default void invalidate() {
	}

}
