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

package io.confound.config;

import java.util.*;

import org.junit.jupiter.api.*;

import static com.github.npathai.hamcrestopt.OptionalMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * Tests of {@link ObjectMapConfiguration}.
 * @author Garret Wilson
 */
public class ObjectMapConfigurationTest {

	@Test
	public void testMap() {
		final Map<String, Object> map = new HashMap<>();
		map.put("foo", "bar");
		map.put("flag", Boolean.TRUE);
		final ObjectMapConfiguration objectMapConfiguration = new ObjectMapConfiguration(map);
		assertThat(objectMapConfiguration.hasConfigurationValue("foo"), is(true));
		assertThat(objectMapConfiguration.getString("foo"), is("bar"));
		assertThat(objectMapConfiguration.hasConfigurationValue("flag"), is(true));
		assertThat(objectMapConfiguration.getBoolean("flag"), is(true));
		assertThat(objectMapConfiguration.hasConfigurationValue("none"), is(false));
		assertThat(objectMapConfiguration.findObject("none"), isEmpty());
	}

}
