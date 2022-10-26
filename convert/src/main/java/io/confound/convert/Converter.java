/*
 * Copyright © 2007-2019 GlobalMentor, Inc. <https://www.globalmentor.com/>
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

package io.confound.convert;

/**
 * Converts an object from one type to another.
 * @param <C> The type of conversion this converter supports.
 * @apiNote This interface evolved from <code>com.globalmentor.model.TypeConverter</code>.
 * @apiNote This interface will likely involve into a framework for pluggable converters and registry, as the use case needs become more apparent.
 * @author Garret Wilson
 */
public interface Converter<C> {

	/**
	 * Indicates whether this converter supports a conversion between two different types. This method does not guarantee that a conversion would be possible
	 * between every instance of the given types.
	 * @param fromType The source type.
	 * @param toType The destination type.
	 * @return <code>true</code> if this converter supports a conversion between the two given types in general.
	 * @throws NullPointerException if either of the given types is <code>null</code>.
	 */
	public boolean supportsConvert(final Class<?> fromType, final Class<?> toType) throws IllegalArgumentException;

	/**
	 * Converts an object from one type to another. If the object is already of the correct type, no action occurs.
	 * @param <T> The type of object required.
	 * @param object The object to convert.
	 * @param convertType The class representing the required type of the object.
	 * @return The object as the required type.
	 * @throws NullPointerException if the given object and/or convert type is <code>null</code>.
	 * @throws IllegalArgumentException if the given object cannot be converted to the requested type; if not a {@link ConversionException}, indicates that the
	 *           object type and/or the conversion type is not supported by this converter.
	 * @throws ConversionException if the given object should be able to be converted to the required type but something about its state, format, or contents
	 *           prevented the conversion.
	 */
	public <T extends C> T convert(final Object object, final Class<T> convertType) throws IllegalArgumentException;

}
