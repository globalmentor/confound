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

import static org.junit.Assert.*;

import java.io.*;
import java.util.Hashtable;

import javax.naming.*;
import javax.naming.spi.*;

import static org.hamcrest.Matchers.*;
import static com.github.npathai.hamcrestopt.OptionalMatchers.*;

import org.junit.*;

import io.confound.config.Configuration;

/**
 * Tests of the {@link JndiConfiguration} class.
 * 
 * @author Magno Nascimento
 */
public class TestJndiConfiguration {

	/**
	 * Tests whether {@link JndiConfiguration} is loading properties correctly.
	 * 
	 * @throws IOException if there was an error preparing or loading the configuration.
	 * @throws NamingException If an error occur while handling with JNDI.
	 */
	@Test
	public void testFindParameter() throws IOException, NamingException {
		setupInitialContext();

		final Configuration configuration = new JndiConfiguration();

		assertThat(configuration.getString("foo"), is("bar"));
		assertThat(configuration.getOptionalString("bar"), isEmpty());
	}

	/**
	 * Sets the custom initial context for testing.
	 * 
	 * @throws NamingException If an error occur while setting the initial context.
	 */
	private static void setupInitialContext() throws NamingException {
		NamingManager.setInitialContextFactoryBuilder(new InitialContextFactoryBuilder() {

			@Override
			public InitialContextFactory createInitialContextFactory(Hashtable<?, ?> environment) throws NamingException {
				return new InitialContextFactory() {

					@Override
					public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
						return new InitialContext() {

							@Override
							public Object lookup(String name) throws NamingException {

								if(name.equals(JndiConfiguration.JNDI_NAMESPACE)) {
									return new InitialContext() {

										@Override
										public Object lookup(String name) throws NamingException {
											if(name.equals("foo")) {
												return "bar";
											}

											return null;
										}

									};
								}

								return null;
							}
						};
					}

				};
			}

		});
	}
}
