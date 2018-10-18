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

package io.confound.config.urf;

import static java.util.Objects.*;

import java.util.*;

import javax.annotation.*;

import io.confound.config.*;
import io.urf.model.UrfObject;

/**
 * Configuration implementation backed by an URF object graph.
 * @author Garret Wilson
 * @see <a href="https://io.urf/">Uniform Resource Framework (URF)</a>
 */
public class UrfConfiguration extends AbstractObjectConfiguration {

	private final Object root;

	/**
	 * URF object graph root constructor.
	 * @param root The root object of the URF object graph.
	 * @throws NullPointerException if the given object is <code>null</code>.
	 */
	public UrfConfiguration(@Nonnull final Object root) {
		this(root, null);
	}

	/**
	 * Parent configuration and map constructor.
	 * @param root The root object of the URF object graph.
	 * @param parentConfiguration The parent configuration to use for fallback lookup, or <code>null</code> if there is no parent configuration.
	 * @throws NullPointerException if the given object is <code>null</code>.
	 */
	public UrfConfiguration(@Nonnull final Object root, @Nullable final Configuration parentConfiguration) {
		super(parentConfiguration);
		this.root = requireNonNull(root);
	}

	//TODO override hasParameterImpl() if can be made more efficient

	@Override
	protected Optional<Object> findParameterImpl(final String key) throws ConfigurationException {
		//TODO add support for hierarchical keys
		if(root instanceof UrfObject) {
			return ((UrfObject)root).getPropertyValue(key);
		} else if(root instanceof Map) {
			return Optional.ofNullable(((Map<?, ?>)root).get(requireNonNull(key)));
		} else {
			return Optional.empty();
		}
	}

}
