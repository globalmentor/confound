/*
 * Copyright © 2019 GlobalMentor, Inc. <http://www.globalmentor.com/>
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

package io.confound.convert;

import javax.annotation.*;

/**
 * An unchecked illegal argument exception to indicate that a value could not be converted because something about its state, format, or contents prevented the
 * conversion
 * @author Garret Wilson
 */
public class ConversionException extends IllegalArgumentException {

	private static final long serialVersionUID = -7667804489992065464L;

	/** No-argument constructor. */
	public ConversionException() {
		this((String)null);
	}

	/**
	 * Message constructor.
	 * @param message An explanation of why the conversion could not occur, or <code>null</code> if no message should be used.
	 */
	public ConversionException(@Nullable final String message) {
		this(message, null);
	}

	/**
	 * Cause constructor. The message of the cause will be used if available.
	 * @param cause The cause error or <code>null</code> if the cause is nonexistent or unknown.
	 */
	public ConversionException(@Nullable final Throwable cause) {
		this(cause == null ? null : cause.toString(), cause);
	}

	/**
	 * Message and cause constructor.
	 * @param message An explanation of why the conversion could not occur, or <code>null</code> if a no message should be used.
	 * @param cause The cause error or <code>null</code> if the cause is nonexistent or unknown.
	 */
	public ConversionException(@Nullable final String message, @Nullable final Throwable cause) {
		super(message, cause);
	}

}
