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

package io.confound.config.jndi;

import java.util.Optional;

import javax.annotation.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import io.confound.config.AbstractStringConfiguration;
import io.confound.config.ConfigurationException;
import io.confound.config.Section;

/**
 * Configuration implementation backed by JNDI variables.
 * 
 * @author Magno Nascimento
 */
public class JndiConfiguration extends AbstractStringConfiguration {

	/** The naming context namespace for JNDI. */
	public static final String JNDI_NAMESPACE = "java:comp/env";

	private final Context context;

	/**
	 * Constructor of the JNDI Configuration. A new initial context will be created automatically.
	 * 
	 * @throws NamingException If an error occur while creating an initial JNDI context.
	 */
	public JndiConfiguration() throws NamingException {
		this(new InitialContext());
	}

	/**
	 * Constructor of the JNDI Configuration providing an initial context.
	 * 
	 * @param initialContext The JNDI initial context to be used.
	 * @throws NamingException If an error occur while creating an initial JNDI context.
	 */
	public JndiConfiguration(@Nonnull final InitialContext initialContext) throws NamingException {
		this.context = initialContext;
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation always returns {@link Optional#empty()}, as JNDI does not support sections.
	 */
	@Override
	public Optional<Section> findSection(final String key) throws ConfigurationException {
		return Optional.empty();
	}

	@Override
	protected Optional<String> findConfigurationValueImpl(String key) throws ConfigurationException {
		try {
			return Optional.of(((Context)context.lookup(JNDI_NAMESPACE)).lookup(key)).map(Object::toString);
		} catch(final NameNotFoundException nameNotFoundException) {
			return Optional.empty();
		} catch(final NamingException namingException) {
			throw new ConfigurationException(String.format("The parameter \"%s\" could not be found on the JNDI context.", key), namingException);
		}
	}

}
