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

package io.confound.config.file.format.properties;

import java.io.*;

import org.junit.jupiter.api.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import io.confound.config.Configuration;
import io.confound.config.MissingConfigurationKeyException;

/**
 * Tests of {@link XmlPropertiesConfigurationFileFormat}.
 * 
 * @author Garret Wilson
 */
public class XmlPropertiesConfigurationFileFormatTest {

	/** The name of the test configuration file in the Java test resources. */
	private static final String CONFIG_RESOURCE_NAME = "config.properties.xml";

	/**
	 * Tests whether {@link XmlPropertiesConfigurationFileFormat} is loading correctly a value.
	 * 
	 * @see XmlPropertiesConfigurationFileFormat#load(InputStream)
	 * @throws IOException if there was an error preparing or loading the configuration.
	 */
	@Test
	public void testLoad() throws IOException {
		final XmlPropertiesConfigurationFileFormat format = new XmlPropertiesConfigurationFileFormat();
		final Configuration configuration;
		try (final InputStream inputStream = getClass().getResourceAsStream(CONFIG_RESOURCE_NAME)) {
			configuration = format.load(inputStream);
		}

		assertThat(configuration.getString("foo"), is("bar"));
		assertThat(configuration.getInt("test"), is(123));
	}

	/**
	 * Tests whether {@link XmlPropertiesConfigurationFileFormat} is failing when retrieving a value with a non-existent configuration key in the file.
	 * 
	 * @see XmlPropertiesConfigurationFileFormat#load(InputStream)
	 * @throws IOException if there was an error preparing or loading the configuration.
	 */
	@Test
	public void testLoadNonExistingConfigurationKey() throws IOException {
		final XmlPropertiesConfigurationFileFormat format = new XmlPropertiesConfigurationFileFormat();
		final Configuration configuration;
		try (final InputStream inputStream = getClass().getResourceAsStream(CONFIG_RESOURCE_NAME)) {
			configuration = format.load(inputStream);
		}

		assertThrows(MissingConfigurationKeyException.class, () -> configuration.getString("foobar"));
	}
}
