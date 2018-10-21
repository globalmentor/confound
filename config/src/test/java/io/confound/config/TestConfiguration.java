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

import static java.util.Objects.*;

import java.util.Optional;

import javax.annotation.*;

/**
 * A fake configuration to facilitate testing.
 * <p>
 * This configuration only recognizes the key {@value #TEST_KEY}, for which it always returns the specified value (which defaults to
 * {@value #DEFAULT_TEST_VALUE} if not specified).
 * </p>
 * @author Garret Wilson
 */
public class TestConfiguration extends AbstractStringConfiguration {

	/** The key used for testing; the only key to return a value. */
	public static final String TEST_KEY = "test";

	/** The default test value to return if none is provided in the constructor. */
	public static final String DEFAULT_TEST_VALUE = "foobar";

	private final String testValue;

	/**
	 * No arguments constructor. The test value {@value #DEFAULT_TEST_VALUE} will be used.
	 * @see #DEFAULT_TEST_VALUE
	 */
	public TestConfiguration() {
		this(DEFAULT_TEST_VALUE);
	}

	/**
	 * Test value constructor.
	 * @param value The value to return if for the key {@value #TEST_KEY}.
	 */
	public TestConfiguration(@Nonnull final String value) {
		this(value, null);
	}

	/**
	 * Parent configuration constructor. The test value {@value #DEFAULT_TEST_VALUE} will be used.
	 * @param parentConfiguration The parent configuration to use for fallback lookup, or <code>null</code> if there is no parent configuration.
	 * @see #DEFAULT_TEST_VALUE
	 */
	public TestConfiguration(@Nullable final Configuration parentConfiguration) {
		this(DEFAULT_TEST_VALUE, parentConfiguration);
	}

	/**
	 * Parent configuration and test value constructor.
	 * @param value The value to return if for the key {@value #TEST_KEY}.
	 * @param parentConfiguration The parent configuration to use for fallback lookup, or <code>null</code> if there is no parent configuration.
	 * @throws NullPointerException if the given value is <code>null</code>.
	 */
	public TestConfiguration(@Nonnull final String value, @Nullable final Configuration parentConfiguration) {
		super(parentConfiguration);
		this.testValue = requireNonNull(value);
	}

	@Override
	protected Optional<String> findParameterImpl(String key) throws ConfigurationException {
		return key.equals(TEST_KEY) ? Optional.of(testValue) : Optional.empty();
	}

}
