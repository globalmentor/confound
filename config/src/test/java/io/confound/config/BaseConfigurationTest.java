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
 * Test base implemented methods of {@link BaseConfiguration}.
 * @author Garret Wilson
 */
public class BaseConfigurationTest {

	/** @see BaseConfiguration#getOptionalString(String) */
	@Test
	public void testGetOptionalString() {
		//test direct configuration access
		final BaseConfiguration parentConfiguration = mock(BaseConfiguration.class, CALLS_REAL_METHODS);
		when(parentConfiguration.getOptionalStringImpl("foo")).thenReturn(Optional.of("bar"));
		assertThat(parentConfiguration.getOptionalString("foo"), is(Optional.of("bar")));
		//test parent configuration fallback
		final BaseConfiguration childConfiguration = mock(BaseConfiguration.class, CALLS_REAL_METHODS);
		when(childConfiguration.getParentConfiguration()).thenReturn(Optional.of(parentConfiguration));
		when(childConfiguration.getOptionalStringImpl("foo")).thenReturn(Optional.empty());
		assertThat(childConfiguration.getOptionalString("foo"), is(Optional.of("bar")));
	}
}
