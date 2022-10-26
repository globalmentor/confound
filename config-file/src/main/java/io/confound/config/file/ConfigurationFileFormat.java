/*
 * Copyright © 2018 GlobalMentor, Inc. <https://www.globalmentor.com/>
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

package io.confound.config.file;

import java.io.*;
import java.nio.charset.CharacterCodingException;
import java.util.Set;

import javax.annotation.*;

import io.confound.config.Configuration;

/**
 * A strategy for loading a configuration from an input stream.
 * @author Garret Wilson
 */
public interface ConfigurationFileFormat {

	/**
	 * Retrieves the extensions of files supported by this file format.
	 * <p>
	 * An <dfn>extension</dfn> is merely a file suffix of any type, except that it is understood that the full stop <code>.</code> character <code>U+002E</code>
	 * will be prepended to the suffix. For example, an extension of <code>foo.bar</code> would match a file ending in <code>.foo.bar</code>, such as
	 * <code>example.foo.bar</code>.
	 * </p>
	 * @return The extensions of filenames for file type supported by this loader.
	 */
	public Set<String> getFilenameExtensions();

	/**
	 * Loads a configuration from the given input stream.
	 * <p>
	 * The given input stream is guaranteed to support {@link InputStream#mark(int)} and {@link InputStream#reset()}.
	 * </p>
	 * <p>
	 * The input stream must <em>not</em> be closed by this method.
	 * </p>
	 * @param inputStream The input stream from which the configuration will be loaded.
	 * @return A new configuration, loaded from the given input stream.
	 * @throws IOException if there is an error loading a configuration from the giving input stream.
	 * @throws CharacterCodingException if the given input stream is for a text document and contains an invalid byte sequence for the charset indicated by the
	 *           BOM; or if no BOM was present, an invalid byte sequence for the assumed charset, depending on the file format.
	 * @see InputStream#markSupported()
	 */
	public @Nonnull Configuration load(@Nonnull final InputStream inputStream) throws IOException;

}
