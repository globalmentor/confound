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

import javax.annotation.*;

/**
 * A configuration exception indicating that a configuration key was not found.
 * @author Garret Wilson
 */
public class MissingConfigurationKeyException extends ConfigurationException {

	private static final long serialVersionUID = 6748106646702212490L;

	private final String key;

	/** @return The key for the missing configuration. */
	public String getKey() {
		return key;
	}

	/**
	 * Key constructor.
	 * @param key The key of the configuration configuration that was not found.
	 */
	public MissingConfigurationKeyException(@Nonnull final String key) {
		this(null, key);
	}

	/**
	 * Message and key constructor.
	 * @param message An explanation of why the input could not be parsed, or <code>null</code> if a default message should be used.
	 * @param key The key of the configuration configuration that was not found.
	 */
	public MissingConfigurationKeyException(@Nullable final String message, @Nonnull final String key) {
		this(message, key, null);
	}

	/**
	 * Cause constructor.
	 * @param key The key of the configuration configuration that was not found.
	 * @param cause The cause error or <code>null</code> if the cause is nonexistent or unknown.
	 */
	public MissingConfigurationKeyException(@Nonnull final String key, @Nullable final Throwable cause) {
		this(null, key, cause);
	}

	/**
	 * Message, key, and cause constructor.
	 * @param message An explanation of why the input could not be parsed, or <code>null</code> if a default message should be used.
	 * @param key The key of the configuration configuration that was not found.
	 * @param cause The cause error or <code>null</code> if the cause is nonexistent or unknown.
	 */
	public MissingConfigurationKeyException(@Nullable final String message, @Nonnull final String key, @Nullable final Throwable cause) {
		super(message, cause);
		this.key = requireNonNull(key);
	}

}
