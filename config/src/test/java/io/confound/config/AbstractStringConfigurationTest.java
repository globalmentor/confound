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
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import java.net.URI;
import java.nio.file.*;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

import org.junit.jupiter.api.*;

/**
 * Test base implemented methods of {@link AbstractStringConfiguration}.
 * @author Garret Wilson
 */
public class AbstractStringConfigurationTest {

	/** @see AbstractStringConfiguration#findObject(String) */
	@Test
	public void testFindObject() {
		final AbstractStringConfiguration configuration = mock(AbstractStringConfiguration.class, CALLS_REAL_METHODS);
		when(configuration.findConfigurationValueImpl("foo")).thenReturn(Optional.of("abc123"));
		assertThat(configuration.findObject("foo"), isPresentAndIs("abc123"));
	}

	/**
	 * Tests that requesting a specific type of object will make the appropriate string conversions.
	 * @see AbstractStringConfiguration#findObject(String, Class)
	 */
	@Test
	public void testFindObjectType() {
		final Path userFooBarPath = Paths.get(System.getProperty("user.home")).resolve("foo").resolve("bar");
		final AbstractStringConfiguration configuration = mock(AbstractStringConfiguration.class, CALLS_REAL_METHODS);
		when(configuration.findConfigurationValueImpl("integerVal")).thenReturn(Optional.of("123"));
		when(configuration.findConfigurationValueImpl("floatVal")).thenReturn(Optional.of("123.45"));
		when(configuration.findConfigurationValueImpl("pathVal")).thenReturn(Optional.of(userFooBarPath.toString()));
		when(configuration.findConfigurationValueImpl("urlVal")).thenReturn(Optional.of("https://example.com/"));
		assertThat(configuration.findObject("integerVal", Byte.class), isPresentAndIs(Byte.valueOf((byte)123)));
		assertThat(configuration.findObject("integerVal", Short.class), isPresentAndIs(Short.valueOf((short)123)));
		assertThat(configuration.findObject("integerVal", Integer.class), isPresentAndIs(Integer.valueOf(123)));
		assertThat(configuration.findObject("integerVal", Long.class), isPresentAndIs(Long.valueOf(123)));
		assertThat(configuration.findObject("integerVal", Integer.class), isPresentAndIs(Integer.valueOf(123)));
		assertThat(configuration.findObject("floatVal", Float.class), isPresentAndIs(Float.valueOf(123.45f)));
		assertThat(configuration.findObject("floatVal", Double.class), isPresentAndIs(Double.valueOf(123.45)));
		assertThat(configuration.findObject("pathVal", Path.class), isPresentAndIs(userFooBarPath));
	}

	/** @see AbstractStringConfiguration#findDouble(String) */
	@Test
	public void testFindDouble() {
		final AbstractStringConfiguration configuration = mock(AbstractStringConfiguration.class, CALLS_REAL_METHODS);
		when(configuration.findConfigurationValueImpl("foo")).thenReturn(Optional.of("1.23"));
		assertThat(configuration.findDouble("foo"), is(OptionalDouble.of(1.23)));
	}

	/** @see AbstractStringConfiguration#findInt(String) */
	@Test
	public void testFindInt() {
		final AbstractStringConfiguration configuration = mock(AbstractStringConfiguration.class, CALLS_REAL_METHODS);
		when(configuration.findConfigurationValueImpl("foo")).thenReturn(Optional.of("123"));
		assertThat(configuration.findInt("foo"), is(OptionalInt.of(123)));
	}

	/** @see AbstractStringConfiguration#findLong(String) */
	@Test
	public void testFindLong() {
		final AbstractStringConfiguration configuration = mock(AbstractStringConfiguration.class, CALLS_REAL_METHODS);
		when(configuration.findConfigurationValueImpl("foo")).thenReturn(Optional.of("123456789"));
		assertThat(configuration.findLong("foo"), is(OptionalLong.of(123456789L)));
	}

	/** @see AbstractStringConfiguration#findPath(String) */
	@Test
	public void testFindPath() {
		final Path userFooBarPath = Paths.get(System.getProperty("user.home")).resolve("foo").resolve("bar");
		final AbstractStringConfiguration configuration = mock(AbstractStringConfiguration.class, CALLS_REAL_METHODS);
		when(configuration.findConfigurationValueImpl("foo")).thenReturn(Optional.of(userFooBarPath.toString()));
		assertThat(configuration.findPath("foo"), isPresentAndIs(userFooBarPath));
	}

	/** @see AbstractStringConfiguration#findString(String) */
	@Test
	public void testFindString() {
		final AbstractStringConfiguration configuration = mock(AbstractStringConfiguration.class, CALLS_REAL_METHODS);
		when(configuration.findConfigurationValueImpl("foo")).thenReturn(Optional.of("bar"));
		assertThat(configuration.findString("foo"), isPresentAndIs("bar"));
	}

	/** @see AbstractStringConfiguration#findUri(String) */
	@Test
	public void testFindUri() {
		final AbstractStringConfiguration configuration = mock(AbstractStringConfiguration.class, CALLS_REAL_METHODS);
		when(configuration.findConfigurationValueImpl("foo")).thenReturn(Optional.of("http://example.com/bar"));
		assertThat(configuration.findUri("foo"), isPresentAndIs(URI.create("http://example.com/bar")));
	}

}
