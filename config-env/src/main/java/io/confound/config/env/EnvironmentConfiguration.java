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

package io.confound.config.env;

import java.util.*;

import javax.annotation.*;

import io.confound.config.*;

/**
 * Configuration implementation backed by environment variables.
 * <p>
 * This implementation provides a facility for converting queried configuration keys to match environment variable conventions. Best practices for Confound
 * configuration variables use lowercase or camelCase names with optional dot separators. Environment variables however are traditionally in uppercase with
 * underscore separators.
 * </p>
 * <p>
 * By default this implementation considers a query for a key <code>foo.bar</code> to be a request for the environment variable named <code>FOO_BAR</code>. This
 * allows a consistent pattern for key <em>requests</em> while allowing environment variables to follow traditional naming conventions.
 * </p>
 * @author Garret Wilson
 * @see System#getenv()
 */
public class EnvironmentConfiguration extends StringMapConfiguration {

	private final boolean normalNames;

	/**
	 * Indicates whether environment variables use uppercase/underscore (e.g. <code>FOO_BAR</code>) conventions, normalizing requested keys (e.g.
	 * <code>foo.bar</code>). If this is set to <code>false</code>, environment variables will only be retrieved if their names exactly match the requested keys.
	 * @return <code>true</code> if requested configuration keys will be normalized to match environment variable naming conventions.
	 */
	public boolean isNormalNames() {
		return normalNames;
	}

	/**
	 * System environment constructor.
	 * <p>
	 * Requested keys will be normalized to match environment variable naming conventions.
	 * </p>
	 * @throws NullPointerException if the given environment map is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to environment variables.
	 * @see System#getenv()
	 */
	public EnvironmentConfiguration() {
		this(System.getenv());
	}

	/**
	 * System environment constructor with normal names option.
	 * @param normalNames <code>true</code> if requested keys such as <code>foo.bar</code> should be normalized to match configuration keys using environment
	 *          variable naming convention such as <code>FOO_BAR</code>.
	 * @throws NullPointerException if the given environment map is <code>null</code>.
	 * @throws SecurityException If a security manager exists and it doesn't allow access to environment variables.
	 * @see System#getenv()
	 */
	public EnvironmentConfiguration(final boolean normalNames) {
		this(System.getenv(), normalNames);
	}

	/**
	 * Environment map constructor.
	 * <p>
	 * Requested keys will be normalized to match environment variable naming conventions.
	 * </p>
	 * @param env The map representing the environment.
	 * @throws NullPointerException if the given environment map is <code>null</code>.
	 */
	public EnvironmentConfiguration(@Nonnull final Map<String, String> env) {
		this(env, true);
	}

	/**
	 * Environment map constructor with normal names option.
	 * @param env The map representing the environment.
	 * @param normalNames <code>true</code> if requested keys such as <code>foo.bar</code> should be normalized to match configuration keys using environment
	 *          variable naming convention such as <code>FOO_BAR</code>.
	 * @throws NullPointerException if the given environment map is <code>null</code>.
	 */
	public EnvironmentConfiguration(@Nonnull final Map<String, String> env, final boolean normalNames) {
		super(env);
		this.normalNames = normalNames;
	}

	/**
	 * {@inheritDoc} This version normalizes requested keys by replacing <code>'.'</code> with <code>'_'</code> and converting the key to uppercase, but only if
	 * {@link #isNormalNames()} is enabled.
	 * @see #isNormalNames()
	 * @see Configuration#KEY_SEGMENT_SEPARATOR
	 */
	@Override
	protected String normalizeKey(String key) {
		key = super.normalizeKey(key); //perform default normalization, if any
		if(isNormalNames()) {
			key = key.replace(KEY_SEGMENT_SEPARATOR, '_').toUpperCase(Locale.ROOT); //foo.bar -> FOO_BAR
		}
		return key;
	}

}
