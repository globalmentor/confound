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

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.Test;

/**
 * Test default methods of {@link Configuration}.
 * @author Garret Wilson
 */
public class ConfigurationTest {

	/** Tests the {@link Configuration#check(boolean)} and the {@link Configuration#check(boolean, String, Object...)} methods. */
	@Test
	public void testConfigurationCheck() {
		Configuration.check(true);
		Configuration.check(true, "error message");
		Configuration.check(true, "error message", 123);
		Configuration.check(true, "error message %d", 123);
	}

	/** Tests the {@link Configuration#check(boolean)} method with a false statement. */
	@Test
	public void testConfigurationCheckWithAFalseStatement() {
		assertThrows(ConfigurationException.class, () -> Configuration.check(false));
	}

	/**
	 * Tests the {@link Configuration#check(boolean)} and {@link Configuration#check(boolean, String, Object...)} methods with a false statement and how the
	 * messages are being formatted.
	 */
	@Test
	public void testConfigurationCheckErrorMessage() {

		try {
			Configuration.check(false);
			fail("The statement above should have thrown an ConfigurationException");
		} catch(final ConfigurationException configurationException) {
			assertThat(configurationException.getMessage(), equalTo(null));
		}

		try {
			Configuration.check(false, "error message");
			fail("The statement above should have thrown an ConfigurationException");
		} catch(final ConfigurationException configurationException) {
			assertThat(configurationException.getMessage(), equalTo("error message"));
		}

		try {
			Configuration.check(false, "error message %d", 123);
			fail("The statement above should have thrown an ConfigurationException");
		} catch(final ConfigurationException configurationException) {
			assertThat(configurationException.getMessage(), equalTo("error message 123"));
		}

		try {
			Configuration.check(false, "error message", 123); // The arguments of the error message should be ignored.
			fail("The statement above should have thrown an ConfigurationException");
		} catch(final ConfigurationException configurationException) {
			assertThat(configurationException.getMessage(), equalTo("error message"));
		}
	}

	/** @see Configuration#findLong(String) */
	@Test
	public void testFindLong() {
		final Configuration configuration = mock(Configuration.class, CALLS_REAL_METHODS);
		when(configuration.findInt("foo")).thenReturn(OptionalInt.of(123));
		//the default version delegates to the int version
		assertThat(configuration.findLong("foo"), is(OptionalLong.of(123L)));
	}

}
