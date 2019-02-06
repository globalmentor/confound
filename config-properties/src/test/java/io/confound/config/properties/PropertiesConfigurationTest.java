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

package io.confound.config.properties;

import static org.junit.Assert.*;

import java.util.Properties;

import static com.github.npathai.hamcrestopt.OptionalMatchers.*;
import static org.hamcrest.Matchers.*;
import org.junit.*;

/**
 * Tests of {@link PropertiesConfiguration}.
 * @author Garret Wilson
 */
public class PropertiesConfigurationTest {

	@Test
	public void testProperties() {
		final Properties properties = new Properties();
		properties.setProperty("foo", "bar");
		properties.put("flag", "true");
		final PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration(properties);
		assertThat(propertiesConfiguration.hasConfigurationValue("foo"), is(true));
		assertThat(propertiesConfiguration.getString("foo"), is("bar"));
		assertThat(propertiesConfiguration.hasConfigurationValue("flag"), is(true));
		assertThat(propertiesConfiguration.getBoolean("flag"), is(true));
		assertThat(propertiesConfiguration.hasConfigurationValue("none"), is(false));
		assertThat(propertiesConfiguration.findObject("none"), isEmpty());
	}

}
