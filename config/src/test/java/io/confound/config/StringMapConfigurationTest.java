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

package io.confound.config;

import static org.junit.Assert.*;

import java.util.*;

import static com.github.npathai.hamcrestopt.OptionalMatchers.*;
import static org.hamcrest.Matchers.*;
import org.junit.*;

/**
 * Tests of {@link StringMapConfiguration}.
 * @author Garret Wilson
 */
public class StringMapConfigurationTest {

	@Test
	public void testMap() {
		final Map<String, String> map = new HashMap<>();
		map.put("foo", "bar");
		map.put("flag", "true");
		final StringMapConfiguration stringMapConfiguration = new StringMapConfiguration(map);
		assertThat(stringMapConfiguration.hasConfigurationValue("foo"), is(true));
		assertThat(stringMapConfiguration.getString("foo"), is("bar"));
		assertThat(stringMapConfiguration.hasConfigurationValue("flag"), is(true));
		assertThat(stringMapConfiguration.getBoolean("flag"), is(true));
		assertThat(stringMapConfiguration.hasConfigurationValue("none"), is(false));
		assertThat(stringMapConfiguration.getOptionalObject("none"), isEmpty());
	}

}
