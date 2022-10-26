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

package io.confound;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;

import org.junit.jupiter.api.*;

import io.confound.config.Configuration;
import io.confound.config.StringMapConfiguration;
import io.csar.Csar;

/**
 * Tests of {@link Confound}.
 * <p>
 * For full testing, the testing harness must be configured to set two environment variables:
 * </p>
 * <dl>
 * <dt><code>TEST</code></dt>
 * <dd><code>example</code></dd>
 * <dt><code>FOO_BAR</code></dt>
 * <dd><code>baz</code></dd>
 * </dl>
 * <p>
 * If those environment variables are not set, the related tests will be skipped.
 * </p>
 * @author Garret Wilson
 */
public class ConfoundTest {

	@AfterEach
	public void clearSystemProperties() {
		System.clearProperty("test");
		System.clearProperty("foo.bar");
	}

	@AfterEach
	public void resetCounfound() {
		Csar.unregisterDefaultConcern(ConfigurationConcern.class); //unregister any default concern 
	}

	//no Confound configuration

	@Test
	public void noConfigurationUseSystemConfiguration() {
		//make sure the environment has been configured correctly for the tests
		//(IDEs such as Eclipse may not pick up the Maven Surefire configuration)
		if(System.getenv("TEST") != null && System.getenv("FOO_BAR") != null) {
			assertThat(Confound.getConfiguration().getString("test"), is("example"));
			assertThat(Confound.getConfiguration().getString("foo.bar"), is("baz"));
			System.setProperty("foo.bar", "override");
			assertThat(Confound.getConfiguration().getString("test"), is("example"));
			//the system property takes precedent overrides the environment variable
			assertThat(Confound.getConfiguration().getString("foo.bar"), is("override"));
		}
	}

	//default concern

	/**
	 * @see Confound#setDefaultConfigurationConcern(ConfigurationConcern)
	 * @see Confound#getConfiguration()
	 */
	@Test
	public void defaultConcernConfoundGetConfigurationIsConcernConfiguration() {
		final ConfigurationConcern defaultConcern = mock(ConfigurationConcern.class);
		final Configuration testConfiguration = new StringMapConfiguration(new HashMap<>());
		when(defaultConcern.getConfiguration()).thenReturn(testConfiguration);
		Confound.setDefaultConfigurationConcern(defaultConcern);
		assertThat(Confound.getConfiguration(), sameInstance(testConfiguration));
	}

}
