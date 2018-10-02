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
import java.util.Properties;

import static java.nio.charset.StandardCharsets.*;
import static org.hamcrest.Matchers.*;

import org.junit.*;

import io.confound.config.Configuration;

/**
 * Tests of {@link PropertiesConfigurationFileFormat}.
 * 
 * @author Garret Wilson
 *
 */
public class PropertiesConfigurationFileFormatTest {

	/**
	 * @see PropertiesConfigurationFileFormat#load(InputStream, Configuration)
	 * @throws IOException if there was an error preparing or loading the configuration.
	 */
	@Test
	public void testLoad() throws IOException {
		final Properties properties = new Properties();
		properties.setProperty("foo", "bar");
		properties.setProperty("test", "123");

		final byte[] contents;
		try (final StringWriter writer = new StringWriter()) {
			properties.store(writer, "test");
			contents = writer.toString().getBytes(UTF_8);
		}

		final PropertiesConfigurationFileFormat format = new PropertiesConfigurationFileFormat();
		final Configuration configuration;
		try (final InputStream inputStream = new ByteArrayInputStream(contents)) {
			configuration = format.load(inputStream);
		}

		assertThat(configuration.getString("foo"), is("bar"));
		assertThat(configuration.getInt("test"), is(123));

	}

}
