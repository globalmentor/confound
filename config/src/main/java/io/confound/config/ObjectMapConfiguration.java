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

import java.util.Map;
import java.util.Optional;

import javax.annotation.*;

/**
 * Configuration implementation backed by a {@link Map} with values stored as objects.
 * @author Garret Wilson
 * @see Map
 * @see Map#get(Object)
 */
public class ObjectMapConfiguration extends AbstractObjectConfiguration {

	private final Map<String, ?> map;

	/**
	 * Map constructor.
	 * @param map The map to back this configuration.
	 * @throws NullPointerException if the given map is <code>null</code>.
	 */
	public ObjectMapConfiguration(@Nonnull final Map<String, ?> map) {
		this(map, null);
	}

	/**
	 * Parent configuration and map constructor.
	 * @param map The map to back this configuration.
	 * @param parentConfiguration The parent configuration to use for fallback lookup, or <code>null</code> if there is no parent configuration.
	 * @throws NullPointerException if the given map is <code>null</code>.
	 */
	public ObjectMapConfiguration(@Nonnull final Map<String, ?> map, @Nullable final Configuration parentConfiguration) {
		super(parentConfiguration);
		this.map = requireNonNull(map);
	}

	/** {@inheritDoc} This implementation delegates to {@link Map#containsKey(Object)}. */
	@Override
	protected boolean hasParameterImpl(final String key) throws ConfigurationException {
		return map.containsKey(key);
	}

	/** {@inheritDoc} This implementation delegates to {@link Map#get(Object)}. */
	@Override
	protected Optional<Object> findParameterImpl(final String key) throws ConfigurationException {
		return Optional.ofNullable(map.get(key));
	}

}
