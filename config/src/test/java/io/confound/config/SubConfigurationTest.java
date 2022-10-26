/*
 * Copyright © 2019 GlobalMentor, Inc. <https://www.globalmentor.com/>
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

import static com.github.npathai.hamcrestopt.OptionalMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.*;

import org.junit.jupiter.api.*;

/**
 * Test default methods of {@link SubConfiguration}.
 * @author Garret Wilson
 */
public class SubConfigurationTest {

	@Test
	public void test() {
		final Map<String, Object> map = new HashMap<>();
		map.put("foo.bar.example", "test");
		final Configuration configuration = new ObjectMapConfiguration(map);
		assertThat(configuration.hasConfigurationValue("foo.bar.example"), is(true));
		assertThat(configuration.hasConfigurationValue("example"), is(false));

		final Configuration subConfiguration = new SubConfiguration(configuration, "foo.bar");
		assertThat(subConfiguration.hasConfigurationValue("foo.bar.example"), is(false));
		assertThat(subConfiguration.hasConfigurationValue("example"), is(true));
		assertThat(subConfiguration.findString("example"), isPresentAndIs("test"));
	}

}
