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

package io.confound.config.file.format.properties;

import static org.junit.Assert.*;

import java.io.*;

import static org.hamcrest.Matchers.*;

import org.junit.*;

import io.confound.config.Configuration;
import io.confound.config.MissingConfigurationKeyException;

/**
 * Tests of {@link PropertiesConfigurationFileFormat}.
 * 
 * @author Garret Wilson
 */
public class PropertiesConfigurationFileFormatTest {

	/** The name of the test configuration file in the Java test resources. */
	private static final String CONFIG_RESOURCE_NAME = "config.properties";

	/**
	 * Tests whether {@link PropertiesConfigurationFileFormat} is loading correctly a value.
	 * 
	 * @see PropertiesConfigurationFileFormat#load(InputStream)
	 * @throws IOException if there was an error preparing or loading the configuration.
	 */
	@Test
	public void testLoad() throws IOException {
		final PropertiesConfigurationFileFormat format = new PropertiesConfigurationFileFormat();
		final Configuration configuration;
		try (final InputStream inputStream = getClass().getResourceAsStream(CONFIG_RESOURCE_NAME)) {
			configuration = format.load(inputStream);
		}

		assertThat(configuration.getString("foo"), is("bar"));
		assertThat(configuration.getInt("test"), is(123));
	}

	/**
	 * Tests whether {@link PropertiesConfigurationFileFormat} is failing when retrieving a value with a non-existent configuration in the file.
	 * 
	 * @see PropertiesConfigurationFileFormat#load(InputStream)
	 * @throws IOException if there was an error preparing or loading the configuration.
	 */
	@Test(expected = MissingConfigurationKeyException.class)
	public void testLoadNonExistingConfigurationKey() throws IOException {
		final PropertiesConfigurationFileFormat format = new PropertiesConfigurationFileFormat();
		final Configuration configuration;
		try (final InputStream inputStream = getClass().getResourceAsStream(CONFIG_RESOURCE_NAME)) {
			configuration = format.load(inputStream);
		}

		configuration.getString("foobar");
	}

}
