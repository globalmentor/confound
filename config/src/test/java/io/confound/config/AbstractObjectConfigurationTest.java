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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.nio.file.*;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

import org.junit.jupiter.api.*;

/**
 * Test base implemented methods of {@link AbstractObjectConfiguration}.
 * @author Garret Wilson
 */
public class AbstractObjectConfigurationTest {

	/** Class for making test objects. */
	private static class FooBar {
	}

	/** @see AbstractObjectConfiguration#findObject(String) */
	@Test
	public void testFindObject() {
		final FooBar fooBar = new FooBar();
		final AbstractObjectConfiguration configuration = mock(AbstractObjectConfiguration.class, CALLS_REAL_METHODS);
		when(configuration.findConfigurationValueImpl("foo")).thenReturn(Optional.of(fooBar));
		assertThat(configuration.findObject("foo"), isPresentAnd(sameInstance(fooBar)));
	}

	/**
	 * Tests that requesting a specific type of object will make the appropriate conversions for numbers.
	 * @see AbstractObjectConfiguration#findObject(String, Class)
	 * @see Number
	 */
	@Test
	public void testFindObjectTypeNumber() {
		final AbstractObjectConfiguration configuration = mock(AbstractObjectConfiguration.class, CALLS_REAL_METHODS);
		when(configuration.findConfigurationValueImpl("integerVal")).thenReturn(Optional.of(123));
		when(configuration.findConfigurationValueImpl("floatVal")).thenReturn(Optional.of(123.45));
		assertThat(configuration.findObject("integerVal", Byte.class), isPresentAndIs(Byte.valueOf((byte)123)));
		assertThat(configuration.findObject("integerVal", Short.class), isPresentAndIs(Short.valueOf((short)123)));
		assertThat(configuration.findObject("integerVal", Integer.class), isPresentAndIs(Integer.valueOf(123)));
		assertThat(configuration.findObject("integerVal", Long.class), isPresentAndIs(Long.valueOf(123)));
		assertThat(configuration.findObject("integerVal", Integer.class), isPresentAndIs(Integer.valueOf(123)));
		assertThat(configuration.findObject("floatVal", Float.class), isPresentAndIs(Float.valueOf(123.45f)));
		assertThat(configuration.findObject("floatVal", Double.class), isPresentAndIs(Double.valueOf(123.45)));
	}

	/** @see AbstractObjectConfiguration#findDouble(String) */
	@Test
	public void testFindDouble() {
		final AbstractObjectConfiguration configuration = mock(AbstractObjectConfiguration.class, CALLS_REAL_METHODS);
		when(configuration.findConfigurationValueImpl("fooByte")).thenReturn(Optional.of(Byte.valueOf((byte)123)));
		when(configuration.findConfigurationValueImpl("fooShort")).thenReturn(Optional.of(Short.valueOf((short)456)));
		when(configuration.findConfigurationValueImpl("fooInteger")).thenReturn(Optional.of(Integer.valueOf(789)));
		when(configuration.findConfigurationValueImpl("fooLong")).thenReturn(Optional.of(Long.valueOf(101112)));
		when(configuration.findConfigurationValueImpl("fooBigInteger")).thenReturn(Optional.of(BigInteger.valueOf(131415)));
		when(configuration.findConfigurationValueImpl("fooFloat")).thenReturn(Optional.of(Float.valueOf(1.23f)));
		when(configuration.findConfigurationValueImpl("fooDouble")).thenReturn(Optional.of(Double.valueOf(4.56)));
		when(configuration.findConfigurationValueImpl("fooBigDecimal")).thenReturn(Optional.of(BigDecimal.valueOf(789.101112)));
		assertThat(configuration.findDouble("fooByte"), is(OptionalDouble.of(123)));
		assertThat(configuration.findDouble("fooShort"), is(OptionalDouble.of(456)));
		assertThat(configuration.findDouble("fooInteger"), is(OptionalDouble.of(789)));
		assertThat(configuration.findDouble("fooLong"), is(OptionalDouble.of(101112)));
		assertThat(configuration.findDouble("fooBigInteger"), is(OptionalDouble.of(131415)));
		assertThat(configuration.findDouble("fooFloat"), is(OptionalDouble.of(1.23f)));
		assertThat(configuration.findDouble("fooDouble"), is(OptionalDouble.of(4.56)));
		assertThat(configuration.findDouble("fooBigDecimal"), is(OptionalDouble.of(789.101112)));
	}

	/** @see AbstractObjectConfiguration#findInt(String) */
	@Test
	public void testFindInt() {
		final AbstractObjectConfiguration configuration = mock(AbstractObjectConfiguration.class, CALLS_REAL_METHODS);
		when(configuration.findConfigurationValueImpl("fooByte")).thenReturn(Optional.of(Byte.valueOf((byte)123)));
		when(configuration.findConfigurationValueImpl("fooShort")).thenReturn(Optional.of(Short.valueOf((short)456)));
		when(configuration.findConfigurationValueImpl("fooInteger")).thenReturn(Optional.of(Integer.valueOf(789)));
		when(configuration.findConfigurationValueImpl("fooLong")).thenReturn(Optional.of(Long.valueOf(101112)));
		when(configuration.findConfigurationValueImpl("fooBigInteger")).thenReturn(Optional.of(BigInteger.valueOf(131415)));
		assertThat(configuration.findInt("fooByte"), is(OptionalInt.of(123)));
		assertThat(configuration.findInt("fooShort"), is(OptionalInt.of(456)));
		assertThat(configuration.findInt("fooInteger"), is(OptionalInt.of(789)));
		assertThat(configuration.findInt("fooLong"), is(OptionalInt.of(101112)));
		assertThat(configuration.findInt("fooBigInteger"), is(OptionalInt.of(131415)));
	}

	/** @see AbstractObjectConfiguration#findLong(String) */
	@Test
	public void testFindLong() {
		final AbstractObjectConfiguration configuration = mock(AbstractObjectConfiguration.class, CALLS_REAL_METHODS);
		when(configuration.findConfigurationValueImpl("fooByte")).thenReturn(Optional.of(Byte.valueOf((byte)123)));
		when(configuration.findConfigurationValueImpl("fooShort")).thenReturn(Optional.of(Short.valueOf((short)456)));
		when(configuration.findConfigurationValueImpl("fooInteger")).thenReturn(Optional.of(Integer.valueOf(789)));
		when(configuration.findConfigurationValueImpl("fooLong")).thenReturn(Optional.of(Long.valueOf(101112)));
		when(configuration.findConfigurationValueImpl("fooBigInteger")).thenReturn(Optional.of(BigInteger.valueOf(131415)));
		assertThat(configuration.findLong("fooByte"), is(OptionalLong.of(123)));
		assertThat(configuration.findLong("fooShort"), is(OptionalLong.of(456)));
		assertThat(configuration.findLong("fooInteger"), is(OptionalLong.of(789)));
		assertThat(configuration.findLong("fooLong"), is(OptionalLong.of(101112)));
		assertThat(configuration.findLong("fooBigInteger"), is(OptionalLong.of(131415)));
	}

	/** @see AbstractObjectConfiguration#findPath(String) */
	@Test
	public void testFindPath() {
		final Path userFooBarPath = Paths.get(System.getProperty("user.home")).resolve("foo").resolve("bar");
		final AbstractObjectConfiguration configuration = mock(AbstractObjectConfiguration.class, CALLS_REAL_METHODS);
		when(configuration.findConfigurationValueImpl("foo")).thenReturn(Optional.of(userFooBarPath));
		assertThat(configuration.findPath("foo"), isPresentAndIs(userFooBarPath));
	}

	/** @see AbstractObjectConfiguration#findString(String) */
	@Test
	public void testFindString() {
		final AbstractObjectConfiguration configuration = mock(AbstractObjectConfiguration.class, CALLS_REAL_METHODS);
		when(configuration.findConfigurationValueImpl("foo")).thenReturn(Optional.of("bar"));
		assertThat(configuration.findString("foo"), isPresentAndIs("bar"));
	}

	/** @see AbstractObjectConfiguration#findUri(String) */
	@Test
	public void testFindUri() {
		final AbstractObjectConfiguration configuration = mock(AbstractObjectConfiguration.class, CALLS_REAL_METHODS);
		when(configuration.findConfigurationValueImpl("foo")).thenReturn(Optional.of(URI.create("http://example.com/bar")));
		assertThat(configuration.findUri("foo"), isPresentAndIs(URI.create("http://example.com/bar")));
	}

}
