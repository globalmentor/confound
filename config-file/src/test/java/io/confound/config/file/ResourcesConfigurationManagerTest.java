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

package io.confound.config.file;

import java.io.*;

import org.junit.jupiter.api.*;

import static com.globalmentor.io.Filenames.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import io.confound.config.Configuration;

/**
 * Tests of {@link ResourcesConfigurationManager}.
 * @author Garret Wilson
 */
public class ResourcesConfigurationManagerTest {

	/** The base name of the test configuration file in the Java test resources. */
	public static final String CONFIG_RESOURCE_BASE_NAME = "configuration";

	/** The name of the test configuration file in the Java test resources. */
	public static final String CONFIG_RESOURCE_NAME = addExtension(CONFIG_RESOURCE_BASE_NAME, TestConfigurationFileFormat.FILENAME_EXTENSION);

	/** @throws IOException if there was an error preparing or loading the configuration. */
	@Test
	public void testConfigResourceName() throws IOException {
		final Configuration configuration = new ResourcesConfigurationManager.Builder().fileFormat(new TestConfigurationFileFormat()).contextClass(getClass())
				.resourceName(CONFIG_RESOURCE_NAME).buildConfiguration();
		assertThat(configuration.getString("test"), is("resources"));
	}

	/** @throws IOException if there was an error preparing or loading the configuration. */
	@Test
	public void testConfigResourceBaseName() throws IOException {
		final Configuration configuration = new ResourcesConfigurationManager.Builder().fileFormat(new TestConfigurationFileFormat()).contextClass(getClass())
				.resourceBaseName(CONFIG_RESOURCE_BASE_NAME).buildConfiguration();
		assertThat(configuration.getString("test"), is("resources"));
	}

}
