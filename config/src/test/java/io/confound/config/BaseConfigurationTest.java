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
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.*;

/**
 * Tests base implemented methods of {@link BaseConfiguration}.
 * @author Garret Wilson
 */
public class BaseConfigurationTest {

	/** @see BaseConfiguration#findObject(String) */
	@Test
	public void testFindOptionalConfigurationValues() {
		//test direct configuration access
		@SuppressWarnings("unchecked")
		final BaseConfiguration<String> parentConfiguration = mock(BaseConfiguration.class, CALLS_REAL_METHODS);
		when(parentConfiguration.findConfigurationValueImpl("foo")).thenReturn(Optional.of("bar"));
		assertThat(parentConfiguration.findObject("foo"), is(Optional.of("bar")));
	}

	//TODO add test of normalizeKey()
}
