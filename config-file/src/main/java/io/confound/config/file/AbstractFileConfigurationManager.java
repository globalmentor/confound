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

import static java.util.Collections.*;
import static java.util.Objects.*;

import java.util.*;

import javax.annotation.*;

import com.globalmentor.io.Filenames;

import io.confound.config.*;

/**
 * A configuration manager that knows how to load a configuration by parsing some document from an input stream.
 * @author Garret Wilson
 */
public abstract class AbstractFileConfigurationManager extends AbstractConfigurationManager {

	/**
	 * Determines the default configuration file formats. This method returns those formats that have been configured as service providers for the
	 * {@link ConfigurationFileFormat} class using the {@link ServiceLoader} mechanism.
	 * @return The default configured configuration file formats.
	 */
	protected static Iterable<ConfigurationFileFormat> defaultFileFormats() {
		return ServiceLoader.load(ConfigurationFileFormat.class);
	}

	/** The map of registered file formats, associated with their filename extension suffixes. */
	private Map<String, ConfigurationFileFormat> fileFormatsById;

	/** @return The available file extensions, in no particular order, paired with the supporting file format. */
	public Set<Map.Entry<String, ConfigurationFileFormat>> getFileFormatsByExtension() {
		return fileFormatsById.entrySet();
	}

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
	 * The value returned by each {@link ConfigurationFileFormat#getFilenameExtensions()} will be used as format identifiers. If multiple file file formats
	 * indicate the same filename extension suffix, the last file format will replace the others for that format. Otherwise the formats will be attempted in the
	 * order given.
	 * </p>
	 * @param fileFormats The specific file formats to support.
	 * @param required Whether the manager requires a configuration to be determined when loading.
	 */
	public AbstractFileConfigurationManager(@Nonnull final Iterable<ConfigurationFileFormat> fileFormats, final boolean required) {
		super(required);
		//use a LinkedHashMap to remember the given order of file formats
		final Map<String, ConfigurationFileFormat> fileFormatsById = new LinkedHashMap<>();
		fileFormats.forEach(fileFormat -> {
			fileFormat.getFilenameExtensions().forEach(format -> {
				fileFormatsById.put(format, fileFormat);
			});
		});
		this.fileFormatsById = unmodifiableMap(fileFormatsById);
	}

	/**
	 * Utility factory method for creating a configuration exception indicating that no supported configuration file could be found.
	 * @return A new configuration exception indicating no configuration file found.
	 */
	public static ConfigurationException createConfigurationNotFoundException() {
		throw new ConfigurationException("No supported configuration file found.");
	}

}
