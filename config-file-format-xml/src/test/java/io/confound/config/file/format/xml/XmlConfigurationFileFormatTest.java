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

package io.confound.config.file.format.xml;

import static org.junit.Assert.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

import org.junit.*;

import io.confound.config.Configuration;
import io.confound.config.MissingParameterException;
import io.confound.config.StringMapConfiguration;
import io.confound.config.file.format.xml.XmlConfigurationFileFormat;

/**
 * Tests of {@link XmlConfigurationFileFormat}.
 * 
 * @author Garret Wilson
 *
 */
public class XmlConfigurationFileFormatTest {

	/** The name of the test configuration file in the Java test resources. */
	public static final String CONFIG_RESOURCE_NAME = "config.xml";

	/**
	 * Tests whether {@link XmlConfigurationFileFormat} is loading properties correctly.
	 * 
	 * @see XmlConfigurationFileFormat#load(InputStream, Configuration)
	 * 
	 * @throws IOException if there was an error preparing or loading the configuration.
	 */
	@Test
	public void testLoad() throws IOException {

		final Configuration configuration;
		try (final InputStream inputStream = getClass().getResourceAsStream("config.xml")) {
			configuration = new XmlConfigurationFileFormat().load(inputStream);
		}

		assertThat(configuration.getString("name"), is("Jane"));
		assertThat(configuration.getString("lastName"), is("Doe"));
		assertThat(configuration.getString("alias"), is("JDoe"));

		assertThat(configuration.getString("company.name"), is("GlobalMentor, Inc."));

		assertThat(configuration.getString("company.address.state"), is("San Francisco"));
		assertThat(configuration.getString("company.address.country"), is("United States of America"));
	}

	/**
	 * Tests whether {@link XmlConfigurationFileFormat} is loading properties correctly when a parent {@link Configuration} is provided.
	 * 
	 * @see XmlConfigurationFileFormat#load(InputStream, Configuration)
	 * 
	 * @throws IOException if there was an error preparing or loading the configuration.
	 */
	@Test
	public void testLoadWithParentConfiguration() throws IOException {

		//TODO use Java 9 Map.of()
		final Map<String, String> parentConfigurationMap = new HashMap<>();
		parentConfigurationMap.put("foo", "bar");

		final Configuration parentConfiguration = new StringMapConfiguration(parentConfigurationMap);

		final Configuration configuration;
		try (final InputStream inputStream = getClass().getResourceAsStream("config.xml")) {
			configuration = new XmlConfigurationFileFormat().load(inputStream, parentConfiguration);
		}

		assertThat(configuration.getString("name"), is("Jane"));
		assertThat(configuration.getString("lastName"), is("Doe"));
		assertThat(configuration.getString("alias"), is("JDoe"));

		assertThat(configuration.getString("company.name"), is("GlobalMentor, Inc."));

		assertThat(configuration.getString("company.address.state"), is("San Francisco"));
		assertThat(configuration.getString("company.address.country"), is("United States of America"));

		assertThat(configuration.getString("foo"), is("bar"));
	}

	/**
	 * Tests whether {@link XmlConfigurationFileFormat} is failing when retrieving a value with a non-existent parameter on the file.
	 * 
	 * @see XmlConfigurationFileFormat#load(InputStream, Configuration)
	 * 
	 * @throws IOException if there was an error preparing or loading the configuration.
	 */
	@Test(expected = MissingParameterException.class)
	public void testLoadNonExistingParameter() throws IOException {

		final Configuration configuration;
		try (final InputStream inputStream = getClass().getResourceAsStream("config.xml")) {
			configuration = new XmlConfigurationFileFormat().load(inputStream);
		}

		configuration.getString("foo");
	}

}
