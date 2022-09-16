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

package io.confound.config.env;

import java.util.*;

import org.junit.jupiter.api.*;

import static com.github.npathai.hamcrestopt.OptionalMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * Tests of {@link EnvironmentConfiguration}.
 * @author Garret Wilson
 */
public class EnvironmentConfigurationTest {

	/** Tests a fake "environment" using a local map, looking up environment variables using the literal keys provided. */
	@Test
	public void testLiteralMapEnvironment() {
		final Map<String, String> envMap = new HashMap<>();
		envMap.put("FOO_BAR", "test");
		final EnvironmentConfiguration envConfiguration = new EnvironmentConfiguration(envMap, false); //disable normalization
		assertThat(envConfiguration.findString("foo.bar"), isEmpty());
		assertThat(envConfiguration.findString("FOO_BAR"), isPresentAnd(is("test")));
		assertThat(envConfiguration.getString("FOO_BAR"), is("test"));

	}

	/** Tests a fake "environment" using a local map, normalizing requested keys to match environment variable naming conventions. */
	@Test
	public void testNormalizedMapEnvironment() {
		final Map<String, String> envMap = new HashMap<>();
		envMap.put("FOO_BAR", "test");
		final EnvironmentConfiguration envConfiguration = new EnvironmentConfiguration(envMap, true); //enable normalization
		assertThat(envConfiguration.getString("foo.bar"), is("test"));
		assertThat(envConfiguration.findString("foo.bar"), isPresentAnd(is("test")));
		assertThat(envConfiguration.getString("FOO_BAR"), is("test"));
		assertThat(envConfiguration.findString("FOO_BAR"), isPresentAnd(is("test")));
	}

}
