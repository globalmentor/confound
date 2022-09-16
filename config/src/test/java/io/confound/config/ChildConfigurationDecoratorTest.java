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

import static com.github.npathai.hamcrestopt.OptionalMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.*;

import org.junit.jupiter.api.*;

/**
 * Tests methods of {@link ChildConfigurationDecorator}.
 * @author Garret Wilson
 */
public class ChildConfigurationDecoratorTest {

	/** @see ChildConfigurationDecorator#findString(String) */
	@Test
	public void testFindOptionalConfigurationValues() {

		//TODO use Java 9 Map.of()
		final Map<String, String> parentConfigurationMap = new HashMap<>();
		parentConfigurationMap.put("test", "parent");
		parentConfigurationMap.put("foo", "bar");
		final Configuration parentConfiguration = new StringMapConfiguration(parentConfigurationMap);

		final Map<String, String> configurationMap = new HashMap<>();
		configurationMap.put("test", "child");
		final Configuration configuration = new StringMapConfiguration(configurationMap);

		final ChildConfigurationDecorator childConfiguration = new ChildConfigurationDecorator(configuration, parentConfiguration);

		assertThat(childConfiguration.findString("test"), isPresentAndIs("child"));
		assertThat(childConfiguration.findString("foo"), isPresentAndIs("bar"));
	}

}
