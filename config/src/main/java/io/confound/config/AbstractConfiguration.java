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
import java.util.function.Supplier;

import javax.annotation.*;

/**
 * Abstract configuration implementation.
 * @author Garret Wilson
 */
public abstract class AbstractConfiguration implements Configuration {

	private final Optional<Configuration> parentConfiguration;

	@Override
	public Optional<Configuration> getParentConfiguration() {
		return parentConfiguration;
	}

	/**
	 * Parent configuration constructor.
	 * @param parentConfiguration The parent configuration for fallback lookup.
	 * @throws NullPointerException if the given parent configuration is <code>null</code>.
	 */
	public AbstractConfiguration(@Nonnull final Optional<Configuration> parentConfiguration) {
		this.parentConfiguration = requireNonNull(parentConfiguration);
	}

	/**
	 * Returns another another optional from a supplier if the given optional is not present.
	 * <p>
	 * This method is also found in <code>com.globalmentor.util.Optionals</code> and is repeated here to prevent an additional dependency.
	 * </p>
	 * <p>
	 * This method duplicates functionality in Java 9.
	 * </p>
	 * @param <T> The type of value contained in the optional.
	 * @param optional The optional to check
	 * @param supplier The supplier of an alternative optional if the value of the given optional is not present.
	 * @return The given optional or, if the value is not present, an optional one returned by the given supplier.
	 * @throws NullPointerException if the supplier is <code>null</code> or returns <code>null</code>.
	 * @see <a href="https://bugs.openjdk.java.net/browse/JDK-8080418">JDK-8080418</a>
	 * @see <a href="https://docs.oracle.com/javase/9/docs/api/java/util/Optional.html#or-java.util.function.Supplier-">Optional.or()</a>
	 */
	protected static <T> Optional<T> or(@Nonnull final Optional<T> optional, @Nonnull final Supplier<Optional<T>> supplier) {
		requireNonNull(supplier);
		return optional.isPresent() ? optional : requireNonNull(supplier.get());
	}

}
