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

package io.confound.config.file.format.turf;

import static org.junit.Assert.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

import org.junit.*;

import io.confound.config.Configuration;
import io.confound.config.MissingParameterException;
import io.confound.config.StringMapConfiguration;

/**
 * Tests of {@link TurfConfigurationFileFormat}.
 * 
 * @author Garret Wilson
 */
public class TurfConfigurationFileFormatTest {

	/** The name of the test configuration file in the Java test resources. */
	public static final String CONFIG_RESOURCE_NAME = "config.turf";

	/**
	 * Tests whether {@link TurfConfigurationFileFormat} is loading correctly a value.
	 * 
	 * @see TurfConfigurationFileFormat#load(InputStream)
	 * @throws IOException if there was an error preparing or loading the configuration.
	 */
	@Test
	public void testLoad() throws IOException {
		final TurfConfigurationFileFormat format = new TurfConfigurationFileFormat();
		final Configuration configuration;
		try (final InputStream inputStream = new BufferedInputStream(getClass().getResourceAsStream(CONFIG_RESOURCE_NAME))) {
			configuration = format.load(inputStream);
		}
		assertThat(configuration.getString("foo"), is("bar"));
		assertThat(configuration.getInt("test"), is(123));
		assertThat(configuration.getBoolean("flag"), is(true));
	}

	/**
	 * Tests whether {@link TurfConfigurationFileFormat} is loading correctly a value from its parent configuration, when not present in the given one.
	 * 
	 * @see TurfConfigurationFileFormat#load(InputStream, Configuration)
	 * @throws IOException if there was an error preparing or loading the configuration.
	 */
	@Test
	public void testLoadWithParentConfiguration() throws IOException {
		//TODO use Java 9 Map.of()
		final Map<String, String> parentConfigurationMap = new HashMap<>();
		parentConfigurationMap.put("foobar", "foo+bar");

		final Configuration parentConfiguration = new StringMapConfiguration(parentConfigurationMap);

		final TurfConfigurationFileFormat format = new TurfConfigurationFileFormat();
		final Configuration configuration;
		try (final InputStream inputStream = new BufferedInputStream(getClass().getResourceAsStream(CONFIG_RESOURCE_NAME))) {
			configuration = format.load(inputStream, parentConfiguration);
		}
		assertThat(configuration.getString("foo"), is("bar"));
		assertThat(configuration.getInt("test"), is(123));
		assertThat(configuration.getBoolean("flag"), is(true));

		assertThat(configuration.getString("foobar"), is("foo+bar"));
	}

	/**
	 * Tests whether {@link TurfConfigurationFileFormat} is failing when retrieving a value with a non-existent parameter on the file.
	 * 
	 * @see TurfConfigurationFileFormat#load(InputStream)
	 * @throws IOException if there was an error preparing or loading the configuration.
	 */
	@Test(expected = MissingParameterException.class)
	public void testLoadNonExistingParameter() throws IOException {
		final TurfConfigurationFileFormat format = new TurfConfigurationFileFormat();
		final Configuration configuration;
		try (final InputStream inputStream = new BufferedInputStream(getClass().getResourceAsStream(CONFIG_RESOURCE_NAME))) {
			configuration = format.load(inputStream);
		}
		configuration.getString("foobar");
	}

}
