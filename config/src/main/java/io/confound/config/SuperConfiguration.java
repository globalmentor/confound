/*
 * Copyright © 2019 GlobalMentor, Inc. <https://www.globalmentor.com/>
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

import java.util.Optional;

import javax.annotation.*;

/**
 * A configuration that decorates another configuration, providing a view of a parent tree of the keyspace. The superconfiguration acts as a live view, but all
 * keys will effectively have the given prefix plus {@value #KEY_SEGMENT_SEPARATOR} added. For example if a key prefix of <code>foo.bar</code> is specified, and
 * the decorated configuration has a key <code>example</code>, in the superconfiguration it will only be accessible using the key <code>foo.bar.example</code>.
 * @author Garret Wilson
 */
public class SuperConfiguration extends AbstractConfigurationDecorator {

	/** The literal key prefix, including the delimiter, e.g. <code>foo.bar.</code>. */
	private final String keyPrefix;

	/**
	 * Wrapped configuration constructor.
	 * @param configuration The configuration to decorate.
	 * @param prefixKey The prefix not including the final segment separator {@value #KEY_SEGMENT_SEPARATOR}.
	 * @throws NullPointerException if the given configuration and/or prefix key is <code>null</code>.
	 */
	public SuperConfiguration(@Nonnull final Configuration configuration, @Nonnull final String prefixKey) {
		super(configuration);
		keyPrefix = prefixKey + KEY_SEGMENT_SEPARATOR;
	}

	@Override
	protected Optional<String> decorateKey(final String key) {
		if(!key.startsWith(keyPrefix)) { //keys not in the superconfiguration
			return Optional.empty();
		}
		return super.decorateKey(key.substring(keyPrefix.length())); //remove the prefix
	}

}
