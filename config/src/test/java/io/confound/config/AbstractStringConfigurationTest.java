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

import java.net.URI;
import java.nio.file.*;
import java.util.Optional;

import org.junit.*;

/**
 * Test base implemented methods of {@link AbstractStringConfiguration}.
 * @author Garret Wilson
 */
public class AbstractStringConfigurationTest {

	/** @see AbstractStringConfiguration#getOptionalParameter(String) */
	@Test
	public void testGetOptionalObject() {
		final AbstractStringConfiguration configuration = mock(AbstractStringConfiguration.class, CALLS_REAL_METHODS);
		when(configuration.findParameterImpl("foo")).thenReturn(Optional.of("abc123"));
		assertThat(configuration.getOptionalParameter("foo"), is(Optional.of("abc123")));
	}

	/** @see AbstractStringConfiguration#getOptionalDouble(String) */
	@Test
	public void testGetOptionalDouble() {
		final AbstractStringConfiguration configuration = mock(AbstractStringConfiguration.class, CALLS_REAL_METHODS);
		when(configuration.findParameterImpl("foo")).thenReturn(Optional.of("1.23"));
		assertThat(configuration.getOptionalDouble("foo"), is(Optional.of(1.23)));
	}

	/** @see AbstractStringConfiguration#getOptionalInt(String) */
	@Test
	public void testGetOptionalInt() {
		final AbstractStringConfiguration configuration = mock(AbstractStringConfiguration.class, CALLS_REAL_METHODS);
		when(configuration.findParameterImpl("foo")).thenReturn(Optional.of("123"));
		assertThat(configuration.getOptionalInt("foo"), is(Optional.of(123)));
	}

	/** @see AbstractStringConfiguration#getOptionalLong(String) */
	@Test
	public void testGetOptionalLong() {
		final AbstractStringConfiguration configuration = mock(AbstractStringConfiguration.class, CALLS_REAL_METHODS);
		when(configuration.findParameterImpl("foo")).thenReturn(Optional.of("123456789"));
		assertThat(configuration.getOptionalLong("foo"), is(Optional.of(123456789L)));
	}

	/** @see AbstractStringConfiguration#getOptionalPath(String) */
	@Test
	public void testGetOptionalPath() {
		final Path userFooBarPath = Paths.get(System.getProperty("user.home")).resolve("foo").resolve("bar");
		final AbstractStringConfiguration configuration = mock(AbstractStringConfiguration.class, CALLS_REAL_METHODS);
		when(configuration.findParameterImpl("foo")).thenReturn(Optional.of(userFooBarPath.toString()));
		assertThat(configuration.getOptionalPath("foo"), is(Optional.of(userFooBarPath)));
	}

	/** @see AbstractStringConfiguration#getOptionalString(String, Object...) */
	@Test
	public void testGetOptionalString() {
		final AbstractStringConfiguration configuration = mock(AbstractStringConfiguration.class, CALLS_REAL_METHODS);
		when(configuration.findParameterImpl("foo")).thenReturn(Optional.of("bar"));
		assertThat(configuration.getOptionalString("foo"), is(Optional.of("bar")));
	}

	/** @see AbstractStringConfiguration#getOptionalUri(String) */
	@Test
	public void testGetOptionalUri() {
		final AbstractStringConfiguration configuration = mock(AbstractStringConfiguration.class, CALLS_REAL_METHODS);
		when(configuration.findParameterImpl("foo")).thenReturn(Optional.of("http://example.com/bar"));
		assertThat(configuration.getOptionalUri("foo"), is(Optional.of(URI.create("http://example.com/bar"))));
	}

}
