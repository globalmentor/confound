/*
 * Copyright © 2007-2019 GlobalMentor, Inc. <http://www.globalmentor.com/>
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

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Converts an object from one type to another.
 * @apiNote This interface evolved from <code>com.globalmentor.model.DefaultTypeConverter</code>.
 * @implSpec This implementation supports the wrappers for all the primitive number types, as well as {@link BigInteger} and {@link BigDecimal}.
 * @author Garret Wilson
 */
public class NumberConverter implements Converter<Number> {

	/** The shared default instance of a number converter. */
	public static final NumberConverter INSTANCE = new NumberConverter();

	/** This class can be overridden but not directly instantiated. */
	protected NumberConverter() {
	}

	@Override
	public <T extends Number> T convert(final Object object, final Class<T> convertType) throws IllegalArgumentException {
		final Class<?> objectType = object.getClass(); //get the type of the object

		if(!(object instanceof Number)) {
			throw new IllegalArgumentException(String.format("Number converter cannot convert from non-number type %s.", objectType));
		}

		if(convertType.isAssignableFrom(objectType)) { //if we expect this type
			return convertType.cast(object); //use the object as-is
		}

		//check the types in order of supposed most common usage
		//TODO check for overflow and precision loss, e.g. floating point to integer and big integer to long
		final Number number = (Number)object;
		if(Integer.class.isAssignableFrom(convertType)) {
			return convertType.cast(Integer.valueOf(number.intValue()));
		} else if(Long.class.isAssignableFrom(convertType)) {
			return convertType.cast(Long.valueOf(number.longValue()));
		} else if(Double.class.isAssignableFrom(convertType)) {
			return convertType.cast(Double.valueOf(number.doubleValue()));
		} else if(Float.class.isAssignableFrom(convertType)) {
			return convertType.cast(Float.valueOf(number.floatValue()));
		} else if(Short.class.isAssignableFrom(convertType)) {
			return convertType.cast(Short.valueOf(number.shortValue()));
		} else if(Byte.class.isAssignableFrom(convertType)) {
			return convertType.cast(Byte.valueOf(number.byteValue()));
		} else if(BigInteger.class.isAssignableFrom(convertType)) {
			return convertType.cast(BigInteger.valueOf(number.longValue())); //TODO improve conversion
		} else if(BigDecimal.class.isAssignableFrom(convertType)) {
			return convertType.cast(BigDecimal.valueOf(number.doubleValue()));
		}

		throw new IllegalArgumentException(String.format("Number converter does not support conversion to %s.", convertType));
	}

}
