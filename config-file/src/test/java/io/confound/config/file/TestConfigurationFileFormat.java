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

package io.confound.config.file;

import static java.util.Collections.*;

import java.io.*;
import java.util.Set;

import com.globalmentor.io.BOMInputStreamReader;

import io.confound.config.*;

/**
 * A configuration file format used for testing.
 * <p>
 * This format recognizes the extension {@value #FILENAME_EXTENSION} and reads the first line from the file, returning it for the key
 * {@value TestConfiguration#TEST_KEY}.
 * </p>
 * @author Garret Wilson
 * @see TestConfiguration#TEST_KEY
 */
public class TestConfigurationFileFormat implements ConfigurationFileFormat {

	/** The supported test configuration filename extension. */
	public static final String FILENAME_EXTENSION = "test-config";

	@Override
	public Set<String> getFilenameExtensionSuffixes() {
		return singleton(FILENAME_EXTENSION);
	}

	@Override
	public Configuration load(final InputStream inputStream, final Configuration parentConfiguration) throws IOException {
		final String line;
		try (final BufferedReader bufferedReader = new BufferedReader(new BOMInputStreamReader(inputStream))) {
			line = bufferedReader.readLine();
		}
		final String testValue = line != null ? line : ""; //account for an empty file
		return new TestConfiguration(testValue, parentConfiguration);
	}
}
