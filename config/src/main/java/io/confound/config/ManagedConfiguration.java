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

import static java.util.Objects.*;

import java.io.IOException;
import java.util.Optional;

import javax.annotation.*;

/**
 * A configuration that wraps manages a cached configuration, reloading when needed.
 * @author Garret Wilson
 */
public class ManagedConfiguration extends AbstractParametersDecorator implements Configuration {

	private final Optional<Configuration> parentConfiguration;

	@Override
	public Optional<Configuration> getParentConfiguration() {
		return parentConfiguration;
	}

	private final ConfigurationManager configurationManager;

	/** @return The associated configuration manager. */
	protected ConfigurationManager getConfigurationManager() {
		return configurationManager;
	}

	/** The cached parameters serving as the decorated configuration. */
	private volatile Parameters parameters = null;

	/**
	 * Determines if the current configuration stale.
	 * <p>
	 * This method also calls {@link ConfigurationManager#isStale(Parameters)}.
	 * </p>
	 * @param parameters The current parameters as last known, or <code>null</code> if the configuration is not loaded.
	 * @return <code>true</code> if the given parameters are stale and need to be reloaded.
	 * @throws IOException if there is an I/O exception checking to see if the current configuration is stale.
	 */
	protected boolean isStale(@Nullable final Parameters parameters) throws IOException {
		if(parameters == null) {
			return true;
		}
		return getConfigurationManager().isStale(parameters);
	}

	@Override
	protected Parameters getParameters() throws ConfigurationException {
		Parameters parameters = this.parameters;
		try {
			if(isStale(parameters)) {
				//TODO improve so that multiple threads don't trigger reloading at the same time
				parameters = reload();
				assert parameters != null : "Parameters should have been loaded at this point.";
			}
		} catch(final IOException ioException) {
			throw new ConfigurationException("Error loading configuration parameters.", ioException);
		}
		return parameters;
	}

	/**
	 * Parent configuration constructor.
	 * @param parentConfiguration The parent configuration to use for fallback lookup, or <code>null</code> if there is no parent configuration.
	 * @param configurationManager The manager for loading and saving the configuration.
	 * @throws NullPointerException if the given parent configuration is <code>null</code>.
	 */
	public ManagedConfiguration(@Nullable final Configuration parentConfiguration, @Nonnull final ConfigurationManager configurationManager) {
		this.parentConfiguration = Optional.ofNullable(parentConfiguration);
		this.configurationManager = requireNonNull(configurationManager);
	}

	/**
	 * Reloads the managed configuration.
	 * <p>
	 * The configuration is invalidated using {@link #invalidate()} and the configuration manager is asked to reload the configuration.
	 * </p>
	 * <p>
	 * This may be called at any time to manually reload refresh the configuration information, but normally consumers never need to call this directly, as the
	 * configuration is managed automatically.
	 * </p>
	 * @return The freshly reloaded configuration.
	 * @throws IOException if an I/O error occurs loading the configuration.
	 * @throws ConfigurationException If there is invalid data or invalid state preventing the configuration from being loaded.
	 */
	public synchronized Configuration reload() throws IOException, ConfigurationException {
		invalidate();
		final Configuration parentConfiguration = getParentConfiguration().orElse(null);
		//if no configuration could be determined, use an empty configuration
		final Configuration configuration = getConfigurationManager().loadConfiguration(parentConfiguration)
				.orElseGet(() -> new EmptyConfiguration(parentConfiguration));
		this.parameters = configuration; //save the local configuration cache, which will be used until it is stale again
		return configuration;
	}

	/**
	 * Invalidates the managed configuration so that it will be lazily reloaded when next requested.
	 * <p>
	 * This method additionally calls {@link ConfigurationManager#invalidate()}.
	 * </p>
	 * @see ConfigurationManager#invalidate()
	 */
	public void invalidate() {
		parameters = null;
		getConfigurationManager().invalidate();
	}

}
