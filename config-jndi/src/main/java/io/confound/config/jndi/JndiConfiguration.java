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

package io.confound.config.jndi;

import java.util.Hashtable;
import java.util.Optional;

import javax.annotation.Nullable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import io.confound.config.AbstractStringConfiguration;
import io.confound.config.ConfigurationException;

/**
 * Configuration implementation backed by JNDI variables.
 * 
 * @author Magno Nascimento
 */
public class JndiConfiguration extends AbstractStringConfiguration {

	public static final String JNDI_NAMESPACE = "java:comp/env";

	private final Context context;

	/**
	 * Constructor of the JNDI Configuration without any JNDI environment properties.
	 * @throws NamingException If an error occur while creating an initial JNDI context.
	 */
	public JndiConfiguration() throws NamingException {
		this(null);
	}

	/**
	 * Constructor of the JNDI Configuration providing environment properties but no fallback parent configuration.
	 * @param environment The JNDI environment properties to be used.
	 * @throws NamingException If an error occur while creating an initial JNDI context.
	 */
	public JndiConfiguration(@Nullable Hashtable<String, String> environment) throws NamingException {
		this.context = (Context)(environment == null ? new InitialContext() : new InitialContext(environment));
	}

	@Override
	protected Optional<String> findConfigurationValueImpl(String key) throws ConfigurationException {
		try {
			return Optional.ofNullable(((Context)context.lookup(JNDI_NAMESPACE)).lookup(key)).map(Object::toString);
		} catch(final NamingException namingException) {
			throw new ConfigurationException("The parameter could not be found on the JNDI context.", namingException);
		}
	}

}
