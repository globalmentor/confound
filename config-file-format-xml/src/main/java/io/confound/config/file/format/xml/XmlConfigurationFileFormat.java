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

package io.confound.config.file.format.xml;

import static java.util.Collections.*;

import java.io.*;
import java.util.*;

import javax.annotation.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import io.confound.config.Configuration;
import io.confound.config.file.ConfigurationFileFormat;

/**
 * File format implementation for a configuration stored in XML format.
 * <p>
 * This implementation recognizes files with the extension suffix {@value #FILENAME_EXTENSION}.
 * </p>
 * @author Magno N A Cruz
 */
public class XmlConfigurationFileFormat implements ConfigurationFileFormat {

	/** The supported extension suffix. */
	public static final String FILENAME_EXTENSION = "xml";

	@Override
	public Set<String> getFilenameExtensions() {
		return singleton(FILENAME_EXTENSION);
	}

	@Override
	public Configuration load(@Nonnull final InputStream inputStream) throws IOException {

		try {
			return new XmlConfiguration(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream));
		} catch(final SAXException | ParserConfigurationException exception) {
			throw new IOException(exception);
		}

	}

}
