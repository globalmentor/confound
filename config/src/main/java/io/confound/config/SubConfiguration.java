/*
 * Copyright © 2019 GlobalMentor, Inc. <http://www.globalmentor.com/>
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
 * A configuration that decorates another configuration, providing a view of a subtree of the keyspace with some key prefix. The subconfiguration acts as a live
 * view, but only has access to keys with the specified prefix plus {@value #KEY_SEGMENT_SEPARATOR}, and those keys will effectively have the prefix and
 * delimiter removed. For example if a key prefix of <code>foo.bar</code> is specified, only keys from the decorating logically starting with
 * <code>foo.bar.</code> would be accessible in the subconfiguration. A setting in a configuration with the key <code>foo.bar.example</code> would be accessible
 * in the subconfiguration as <code>example</code>.
 * @author Garret Wilson
 */
public class SubConfiguration extends AbstractConfigurationDecorator {

	/** The literal key prefix, including the delimiter, e.g. <code>foo.bar.</code>. */
	private final String keyPrefix;

	/**
	 * Wrapped configuration constructor.
	 * @param configuration The configuration to decorate.
	 * @param prefixKey The prefix not including the final segment separator {@value #KEY_SEGMENT_SEPARATOR}.
	 * @throws NullPointerException if the given configuration and/or prefix key is <code>null</code>.
	 */
	public SubConfiguration(@Nonnull final Configuration configuration, @Nonnull final String prefixKey) {
		super(configuration);
		keyPrefix = prefixKey + KEY_SEGMENT_SEPARATOR;
	}

	@Override
	protected Optional<String> decorateKey(final String key) {
		return super.decorateKey(keyPrefix + key);
	}

}
