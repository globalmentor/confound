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

import java.io.*;
import java.util.Hashtable;

import javax.naming.*;
import javax.naming.spi.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static com.github.npathai.hamcrestopt.OptionalMatchers.*;

import org.junit.jupiter.api.*;

import io.confound.config.Configuration;
import io.confound.config.ConfigurationException;

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
		final Configuration configuration = new JndiConfiguration();

		assertThat(configuration.getString("foo"), is("bar"));
		assertThat(configuration.findString("bar"), isEmpty());
	}

	/**
	 * Tests whether {@link JndiConfiguration} is throwing an exception when a property key not existing on JNDI context is provided.
	 * 
	 * @throws IOException if there was an error preparing or loading the configuration.
	 * @throws NamingException If an error occur while handling with JNDI.
	 */
	@Test
	public void testFindParameterNotExisting() throws IOException, NamingException {
		final Configuration configuration = new JndiConfiguration();

		assertThrows(ConfigurationException.class, () -> configuration.getString("bar"));
	}

	/**
	 * Sets the custom initial context for testing.
	 * @implNote The base implementation for realizing this version came from <a href="https://stackoverflow.com/a/21733896">Scott Nelson's answer for
	 *           <cite>Initialcontext in a standalone Java program</cite></a> (<a href="https://stackoverflow.com">Stack Overflow</a>).
	 * @throws NamingException If an error occur while setting the initial context.
	 */
	@BeforeAll
	public static void setupInitialContext() throws NamingException {
		NamingManager.setInitialContextFactoryBuilder(new InitialContextFactoryBuilder() {

			@Override
			public InitialContextFactory createInitialContextFactory(Hashtable<?, ?> environment) throws NamingException {
				return new InitialContextFactory() {

					@Override
					public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
						return new InitialContext() {

							@Override
							public Object lookup(final String name) throws NamingException {
								if(name.isEmpty()) {
									return this; // made just to fulfill the contract for Context.lookup(...).
								}

								if(name.equals(JndiConfiguration.JNDI_NAMESPACE)) {
									return new InitialContext() {

										@Override
										public Object lookup(final String name) throws NamingException {
											if(name.isEmpty()) {
												return this; // made just to fulfill the contract for Context.lookup(...).
											}

											if(name.equals("foo")) {
												return "bar";
											}

											throw new NameNotFoundException(String.format("The property %s could not be found in the current JNDI context.", name));
										}

									};
								}

								throw new NameNotFoundException(String.format("The property %s could not be found in the current JNDI context.", name));
							}
						};
					}

				};
			}

		});
	}
}
