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
import static java.util.Objects.*;
import static java.util.stream.StreamSupport.*;

import java.util.*;
import java.util.stream.Stream;

import javax.annotation.*;

import com.globalmentor.io.Filenames;

import io.confound.config.ConfigurationManager;

/**
 * A configuration manager that knows how to load a configuration by parsing some document from an input stream.
 * @author Garret Wilson
 */
public abstract class AbstractFileConfigurationManager implements ConfigurationManager {

	/**
	 * Determines the default configuration file formats. This method returns those formats that have been configured as service providers for the
	 * {@link ConfigurationFileFormat} class using the {@link ServiceLoader} mechanism.
	 * @return The default configured configuration file formats.
	 */
	protected static Stream<ConfigurationFileFormat> defaultFileFormats() {
		return stream(ServiceLoader.load(ConfigurationFileFormat.class).spliterator(), false);
	}

	/** The map of registered file formats, associated with their filename extension suffixes. */
	private Map<String, ConfigurationFileFormat> fileFormatsById;

	/**
	 * Determines the file format to use for the given filename extension.
	 * @param id The filename extension suffix.
	 * @return The configured file format for the extension, if any.
	 */
	public Optional<ConfigurationFileFormat> getFileFormatForExtension(@Nonnull final String id) {
		return Optional.ofNullable(fileFormatsById.get(requireNonNull(id)));
	}

	/**
	 * Determines the file format to use for the given filename based on the registered formats and the filename extension(s). If the file has multiple
	 * extensions, the registered file format for the longest extension will be used.
	 * @param filename The filename for which the file format should be determined.
	 * @return The configured file format for the filename, if any.
	 */
	public Optional<ConfigurationFileFormat> getFileFormatForFilename(@Nonnull final CharSequence filename) {
		return Filenames.extensions(filename).map(fileFormatsById::get).filter(Objects::nonNull).findFirst();
	}

	/**
	 * Configuration file formats constructor. The provided file formats will be registered with this configuration manager.
	 * <p>
	 * The value returned by each {@link ConfigurationFileFormat#getFilenameExtensionSuffixes()} will be used as format identifiers. If multiple file file formats
	 * indicate the same filename extension suffix, the last file format will replace the others for that format. Otherwise the formats will be attempted in the
	 * order given.
	 * </p>
	 * @param fileFormats The specific file formats to support.
	 */
	public AbstractFileConfigurationManager(@Nonnull final Stream<ConfigurationFileFormat> fileFormats) {
		//use a LinkedHashMap to remember the given order of file formats
		final Map<String, ConfigurationFileFormat> fileFormatsById = new LinkedHashMap<>();
		fileFormats.forEach(fileFormat -> {
			fileFormat.getFilenameExtensionSuffixes().forEach(format -> {
				fileFormatsById.put(format, fileFormat);
			});
		});
		this.fileFormatsById = unmodifiableMap(fileFormatsById);
	}

	/** Constructor using the default configuration file format registered as service providers. */
	public AbstractFileConfigurationManager() {
		this(defaultFileFormats());
	}

}
