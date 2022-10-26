/*
 * Copyright © 1996-2018 GlobalMentor, Inc. <https://www.globalmentor.com/>
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

import javax.annotation.*;

/**
 * An unchecked illegal state exception to indicate that the system is not configured correctly for some operation. For example, this exception might be thrown
 * if no appropriate parser is available or a particular character encoding is not supported.
 * @author Garret Wilson
 */
public class ConfigurationException extends IllegalStateException {

	private static final long serialVersionUID = -7970040904023841449L;

	/** No-argument constructor. */
	public ConfigurationException() {
		this((String)null);
	}

	/**
	 * Message constructor.
	 * @param message An explanation of why the input could not be parsed, or <code>null</code> if no message should be used.
	 */
	public ConfigurationException(@Nullable final String message) {
		this(message, null);
	}

	/**
	 * Cause constructor. The message of the cause will be used if available.
	 * @param cause The cause error or <code>null</code> if the cause is nonexistent or unknown.
	 */
	public ConfigurationException(@Nullable final Throwable cause) {
		this(cause == null ? null : cause.toString(), cause);
	}

	/**
	 * Message and cause constructor.
	 * @param message An explanation of why the input could not be parsed, or <code>null</code> if a no message should be used.
	 * @param cause The cause error or <code>null</code> if the cause is nonexistent or unknown.
	 */
	public ConfigurationException(@Nullable final String message, @Nullable final Throwable cause) {
		super(message, cause);
	}

}
