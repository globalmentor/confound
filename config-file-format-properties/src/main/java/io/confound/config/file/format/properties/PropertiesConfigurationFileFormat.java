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

package io.confound.config.file.format.properties;

import static java.nio.charset.StandardCharsets.*;
import static java.util.Collections.*;

import java.io.*;
import java.util.*;

import com.globalmentor.io.BOMInputStreamReader;

import io.confound.config.Configuration;
import io.confound.config.file.ConfigurationFileFormat;
import io.confound.config.properties.PropertiesConfiguration;

/**
 * File format implementation for a configuration stored in standard properties format, using the UTF-8 charset by default, or any UTF-* encoding (including
 * UTF-8, UTF-16BE, UTF-16LE, UTF-32BE, and UTF-32LE) for which a Byte Order Mark (BOM) is present.
 * <p>
 * This implementation recognizes files with the extension suffix {@value #FILENAME_EXTENSION}.
 * </p>
 * @author Garret Wilson
 */
public class PropertiesConfigurationFileFormat implements ConfigurationFileFormat {

	/** The supported filename extension. */
	public static final String FILENAME_EXTENSION = "properties";

	@Override
	public Set<String> getFilenameExtensions() {
		return singleton(FILENAME_EXTENSION);
	}

	@Override
	public Configuration load(final InputStream inputStream) throws IOException {
		final Properties properties = new Properties();
		properties.load(new BOMInputStreamReader(inputStream, UTF_8)); //detect and default to UTF-8 
		return new PropertiesConfiguration(properties);
	}

}
