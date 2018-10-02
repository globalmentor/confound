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

import java.io.IOException;
import java.util.Optional;

import javax.annotation.*;

/**
 * A strategy for retrieving and storing configurations.
 * @author Garret Wilson
 */
public interface ConfigurationManager {

	/**
	 * Returns whether this configuration manager requires a configuration. If a configuration is required, attempting to load a configuration will throw an
	 * exception if a configuration cannot be determined; otherwise, it will return {@link Optional#empty()}.
	 * <p>
	 * The default implementation returns <code>false</code>.
	 * </p>
	 * @return <code>true</code> if a configuration is required and this manager will always return a configuration and throw an exception if one cannot be
	 *         determined.
	 * @see #loadConfiguration(Configuration)
	 */
	public default boolean isRequired() { //TODO allow setting via AbstractConfigurationManager; remove default
		return false;
	}

	/**
	 * Loads a configuration.
	 * <p>
	 * It is imperative that the configuration manager assign the given parent configuration, if any, to the loaded configuration so that chained configuration
	 * resolution will work.
	 * </p>
	 * <p>
	 * After this method has been called, it is expected that {@link #isStale(Parameters)} would return <code>false</code> barring concurrent changes.
	 * </p>
	 * @param parentConfiguration The parent configuration to use for fallback lookup, or <code>null</code> if there is no parent configuration.
	 * @return The loaded configuration.
	 * @throws IOException if an I/O error occurs loading the configuration.
	 * @throws ConfigurationException If there is invalid data or invalid state preventing the configuration from being loaded.
	 */
	public Optional<Configuration> loadConfiguration(@Nullable final Configuration parentConfiguration) throws IOException, ConfigurationException;

	/**
	 * Saves the configuration.
	 * @param parameters The configuration parameters to save.
	 * @throws IOException if an error occurs saving the configuration.
	 */
	public void saveConfiguration(@Nonnull final Parameters parameters) throws IOException;

	/**
	 * Determines whether the currently managed configuration is stale.
	 * <p>
	 * If this method returns <code>false</code>, the caller should call {@link #invalidate()} before reloading the configuration.
	 * </p>
	 * @param parameters The currently loaded parameters.
	 * @return Whether the currently loaded parameters are stale and need to be reloaded.
	 * @throws IOException if an error occurs checking the configuration.
	 */
	public default boolean isStale(@Nonnull final Parameters parameters) throws IOException {
		return false;
	}

	/**
	 * Invalidates any cached information about the managed configuration.
	 * <p>
	 * The default implementation does nothing.
	 * </p>
	 */
	public default void invalidate() {
	}

}
