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

import static com.globalmentor.io.Filenames.*;
import static com.globalmentor.java.Classes.*;
import static com.globalmentor.java.Conditions.*;
import static java.util.Collections.*;
import static java.util.Objects.*;

import java.io.*;
import java.net.URL;
import java.util.*;

import javax.annotation.*;

import io.clogr.Clogged;
import io.confound.config.*;

/**
 * A configuration manager that can load and parse a configuration from class resources.
 * <p>
 * This configuration manager is similar to a {@link FileSystemConfigurationManager}, except that it only has two algorithms for looking up resources: a single
 * resource path (either relative or absolute), or a resource path and base resource filename. This configuration manager by default requires a configuration,
 * as it is assumed that no resources configuration would be configured unless it were known ahead of time that the configuration resource exists.
 * </p>
 * @author Garret Wilson
 */
public class ResourcesConfigurationManager extends AbstractFileConfigurationManager implements Clogged {

	/** The class loader to use in loading the configuration resource. */
	private final ClassLoader classLoader;

	/** The absolute resource path to use in loading; will be considered a directory and combined with the resource base name if present. */
	private final String resourcePath;

	/**
	 * The base name to use when searching for resources, relative to the resource path; or <code>null</code> if the resource path should be used directly.
	 */
	private final String resourceBaseName;

	/**
	 * Information about the determined configuration resource, or <code>null</code> if the configuration resource has not been determined or has been
	 * invalidated.
	 */
	private ResourceInfo configurationResourceInfo = null;

	/**
	 * Class loader and resource path constructor for a required configuration file.
	 * @param classLoader The class loader to use for loading a configuration resource.
	 * @param resourcePath The full configuration resource path, relative to the classpath, including configuration filename.
	 * @throws IllegalArgumentException if the given resource path does not include a filename.
	 */
	public ResourcesConfigurationManager(@Nonnull ClassLoader classLoader, @Nonnull final String resourcePath) {
		this(classLoader, resourcePath, true);
	}

	/**
	 * Class loader, resource path, and optional required constructor.
	 * @param classLoader The class loader to use for loading a configuration resource.
	 * @param resourcePath The full configuration resource path, relative to the classpath, including configuration filename.
	 * @param required Whether the manager requires a configuration to be determined when loading.
	 * @throws IllegalArgumentException if the given resource path does not include a filename.
	 */
	public ResourcesConfigurationManager(@Nonnull ClassLoader classLoader, @Nonnull final String resourcePath, final boolean required) {
		this(defaultFileFormats(), classLoader, resourcePath, null, required);
	}

	/**
	 * Class loader, resource base path, and resource base name constructor for a required configuration file. Class loader, resource directory, and optional
	 * required constructor.
	 * @param classLoader The class loader to use for loading a configuration resource.
	 * @param resourceBasePath The configuration resource base path, relative to the classpath, not including configuration filename.
	 * @param resourceBaseName The base filename to use for the resource, not including extension.
	 * @throws IllegalArgumentException if the given resource path includes a filename.
	 */
	public ResourcesConfigurationManager(@Nonnull ClassLoader classLoader, @Nonnull final String resourceBasePath, @Nonnull final String resourceBaseName) {
		this(classLoader, resourceBasePath, resourceBaseName, true);
	}

	/**
	 * Class loader, resource base path, resource base name, and optional required constructor.
	 * @param classLoader The class loader to use for loading a configuration resource.
	 * @param resourceBasePath The configuration resource base path, relative to the classpath, not including configuration filename.
	 * @param resourceBaseName The base filename to use for the resource, not including extension.
	 * @param required Whether the manager requires a configuration to be determined when loading.
	 * @throws IllegalArgumentException if the given resource path includes a filename.
	 */
	public ResourcesConfigurationManager(@Nonnull ClassLoader classLoader, @Nonnull final String resourceBasePath, @Nonnull final String resourceBaseName,
			final boolean required) {
		this(defaultFileFormats(), classLoader, resourceBasePath, resourceBaseName, required);
	}

	/**
	 * File formats, class loader, resource (base) path, resource base name, and optional required constructor.
	 * @param fileFormats The file formats to support.
	 * @param classLoader The class loader to use for loading a configuration resource.
	 * @param resourcePath The configuration resource base path, relative to the classpath, not including configuration filename; or if no resource base name is
	 *          specified, the full configuration resource path, relative to the classpath, including configuration filename.
	 * @param resourceBaseName The base filename to use for the resource, not including extension, or <code>null</code> if the resource path should be considered
	 *          the full configuration path.
	 * @param required Whether the manager requires a configuration to be determined when loading.
	 * @throws IllegalArgumentException if a resource base name is given and the given resource path includes a filename, or if a resource base name is not given
	 *           and the resource path does not contain a filename.
	 */
	protected ResourcesConfigurationManager(@Nonnull final Iterable<ConfigurationFileFormat> fileFormats, @Nonnull ClassLoader classLoader,
			@Nonnull final String resourcePath, @Nonnull final String resourceBaseName, final boolean required) {
		super(fileFormats, required);
		this.classLoader = requireNonNull(classLoader);
		this.resourcePath = requireNonNull(resourcePath);
		this.resourceBaseName = resourceBaseName;
		final boolean resourcePathContainsName = getResourceName(resourcePath).isPresent();
		if(resourceBaseName != null) {
			checkArgument(!resourcePathContainsName, "Resource base path %s must end in a slash and cannot contain a filename.", resourcePath);
		} else {
			checkArgument(resourcePathContainsName, "Resource path %s does not contain a filename.", resourcePath);
		}
	}

	/**
	 * Determines the file format to use for the given path based on the registered formats and the path filename extension(s).
	 * @param resourcePath The resource path for which the file format should be determined.
	 * @return The configured file format for the resource path, if any.
	 * @throws IllegalArgumentException if the given resource path has no filename.
	 */
	public Optional<ConfigurationFileFormat> getFileFormat(@Nonnull final String resourcePath) {
		final String resourceName = getResourceName(resourcePath)
				.orElseThrow(() -> new IllegalArgumentException(String.format("Configuration path %s has no filename.", resourcePath)));
		return getFileFormatForFilename(resourceName);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation uses the existing configuration path if it has been determined. If the configuration path has not been determined, such as if this
	 * manager has been invalidated using {@link #invalidate()}, it determines a new configuration path and updates the record.
	 * </p>
	 */
	@Override
	public synchronized Optional<Configuration> loadConfiguration(@Nullable final Configuration parentConfiguration) throws IOException, ConfigurationException {
		ConfigurationFileFormat fileFormat = null; //we may determine the file format during searching the candidate paths, or directly
		String configurationPath = this.configurationResourceInfo != null ? this.configurationResourceInfo.getPath().orElse(null) : null;
		URL configurationResourceUrl = configurationPath != null ? classLoader.getResource(configurationPath) : null; //we retrieve a URL to the resource to determine if it exists
		if(configurationPath == null || configurationResourceUrl == null) { //find a configuration path if we don't already have one, or it doesn't exist anymore (although that seems highly unlikely)
			configurationPath = null; //assume we can't find it (in case we had one and it no longer exists)
			if(resourceBaseName == null) { //if we have an absolute configuration path
				final String candidateResourcePath = resourcePath; //use the resource path as it is, as it should include the filename and everything
				getLogger().debug("Loading configuration resource from path {}.", candidateResourcePath);
				configurationResourceUrl = classLoader.getResource(candidateResourcePath);
				if(configurationResourceUrl != null) { //if we found a resource path that exists
					configurationPath = candidateResourcePath; //use this configuration path
				}
			} else { //if we have a resource path and a base filename
				final String baseResourcePath = resourcePath;
				for(final Map.Entry<String, ConfigurationFileFormat> candidateFormatEntry : (Iterable<Map.Entry<String, ConfigurationFileFormat>>)getFileFormatsByExtension()
						.stream().sorted((format1, format2) -> Integer.compare(format2.getKey().length(), format1.getKey().length()))::iterator) { //sort the extensions in from longest to shortest
					final String candidateExtension = candidateFormatEntry.getKey();
					final String candidateResourcePath = baseResourcePath + RESOURCE_PATH_SEPARATOR + addExtension(resourceBaseName, candidateExtension);
					getLogger().debug("Searching for configuration resource at path {}.", candidateResourcePath);
					configurationResourceUrl = classLoader.getResource(candidateResourcePath);
					if(configurationResourceUrl != null) { //if we found a resource path that exists
						configurationPath = candidateResourcePath; //use this configuration path
						fileFormat = candidateFormatEntry.getValue(); //use this file format
						break;
					}
				}
			}
		}
		final Configuration configuration; //load the configuration or set it to `null`
		if(configurationPath != null) { //if we had or determined a configuration path
			if(fileFormat == null) { //if we don't yet know the format of the file
				final String fileFormatPath = configurationPath;
				fileFormat = getFileFormat(fileFormatPath).orElseThrow(
						() -> new ConfigurationException(String.format("Configuration resource at path %s does not have a supported format.", fileFormatPath)));
			}
			assert configurationResourceUrl != null : "We expect to have determined the configuration resource URL.";
			assert fileFormat != null : "We expect to have determined the configuration file format.";
			try (final InputStream inputStream = new BufferedInputStream(configurationResourceUrl.openStream())) {
				configuration = fileFormat.load(inputStream, parentConfiguration);
			}
		} else { //if we couldn't determine a configuration path
			if(isRequired()) {
				throw new ConfigurationException("No supported configuration resource found.");
			}
			configuration = null;
		}
		this.configurationResourceInfo = new ResourceInfo(configurationPath); //save our current configuration path; we are now no longer stale
		return Optional.ofNullable(configuration);
	}

	/** {@inheritDoc} This implementation does not yet support saving configurations, and will throw an exception. */
	@Override
	public void saveConfiguration(final Parameters parameters) throws IOException {
		throw new UnsupportedOperationException("Saving configurations not yet supported.");
	}

	/** {@inheritDoc} This version additionally checks to see if whether there is a cached configuration resource path. */
	@Override
	public boolean isStale(final Parameters parameters) throws IOException {
		if(super.isStale(parameters)) {
			return true;
		}
		if(configurationResourceInfo == null) {
			return true;
		}
		return false;
	}

	/** {@inheritDoc} This version additionally removes any cached configuration path. */
	@Override
	public synchronized void invalidate() {
		super.invalidate();
		configurationResourceInfo = null;
	}

	/**
	 * Encapsulates determined resource path information and related information.
	 * @author Garret Wilson
	 *
	 */
	private static class ResourceInfo {

		private final String path;

		/** @return The path, or {@link Optional#empty()} if none was determined. */
		public Optional<String> getPath() {
			return Optional.ofNullable(path);
		}

		/**
		 * Constructor.
		 * @param path The resource path, or <code>null</code> if none was determined.
		 */
		public ResourceInfo(@Nullable final String path) {
			this.path = path;
		}

	}

	/**
	 * Creates a configuration manager that loads a required configuration from a complete path to a configuration resource, relative to a class, using the class'
	 * resource loader.
	 * @param classLoader The class loader to use for loading a configuration resource.
	 * @param resourcePath The complete resource path such as <code>com/example/foo.bar</code>, relative to the classpath, for loading a configuration file from
	 *          class resources.
	 * @return A configuration manager for the resource at the given path.
	 * @throws NullPointerException if the class loader and/or resource path is <code>null</code>.
	 */
	public static ResourcesConfigurationManager forResourcePath(@Nonnull ClassLoader classLoader, @Nonnull final String resourcePath) {
		return new Builder().classLoader(classLoader).resourcePath(resourcePath).build();
	}

	/**
	 * Creates a configuration manager that loads a required configuration using a complete resource filename relative to a class, using the class' resource
	 * loader.
	 * @param contextClass The class providing the resource context for loading.
	 * @param resourceName The resource filename, such as <code>foo.bar</code> for loading a configuration resource relative to the given class.
	 * @return A configuration manager for the resource with the given filename.
	 * @throws NullPointerException if the resource filename is <code>null</code>.
	 */
	public static ResourcesConfigurationManager forResourceName(@Nonnull final Class<?> contextClass, @Nonnull final String resourceName) {
		return new Builder().contextClass(contextClass).resourceName(resourceName).build();
	}

	/**
	 * Creates a configuration manager that loads a required configuration using a resource base filename relative to a class, using the class' resource loader.
	 * @param contextClass The class providing the resource context for loading.
	 * @param resourceBaseName The base filename, such as <code>base</code>, to locate resources with extensions, such as <code>base.foo</code>, supported by
	 *          installed configuration file formats.
	 * @return A configuration manager for determining a resource with the given base filename.
	 * @throws NullPointerException if the context class and/or resource base filename is <code>null</code>.
	 */
	public static ResourcesConfigurationManager forCandidateResourceBaseName(@Nonnull final Class<?> contextClass, @Nonnull final String resourceBaseName) {
		return new Builder().contextClass(contextClass).candidateResourceBaseName(resourceBaseName).build();
	}

	/**
	 * Builder for the manager.
	 * <p>
	 * By default the configuration will be required. By default the file formats installed from their providers will be used if none are specified.
	 * </p>
	 * @author Garret Wilson
	 */
	public static class Builder {

		private Configuration parentConfiguration;

		/**
		 * Sets the parent configuration to use for fallback lookup.
		 * @param parentConfiguration The parent, fallback configuration.
		 * @return This builder.
		 */
		public Builder parentConfiguration(@Nonnull Configuration parentConfiguration) {
			this.parentConfiguration = requireNonNull(parentConfiguration);
			return this;
		}

		private Iterable<ConfigurationFileFormat> fileFormats = AbstractFileConfigurationManager.defaultFileFormats();

		/**
		 * Sets a single file format to be supported by the configuration manager.
		 * @param fileFormat The file format to support.
		 * @return This builder.
		 */
		public Builder fileFormat(@Nonnull final ConfigurationFileFormat fileFormat) {
			return fileFormats(singleton(requireNonNull(fileFormat)));
		}

		/**
		 * Sets the file formats to be supported by the configuration manager.
		 * @param fileFormats The file formats to support.
		 * @return This builder.
		 */
		public Builder fileFormats(@Nonnull final Iterable<ConfigurationFileFormat> fileFormats) {
			this.fileFormats = requireNonNull(fileFormats);
			return this;
		}

		private boolean required = true;

		/**
		 * Sets whether the configuration file is required to be discovered.
		 * @param required <code>true</code> if a configuration is required and the manager will always return a configuration and throw an exception if one cannot
		 *          be determined.
		 * @return This builder.
		 */
		public Builder required(final boolean required) {
			this.required = required;
			return this;
		}

		private Class<?> contextClass = null;

		/**
		 * Sets the context class to use for a class loader if none is specified separately, and for determining a base path if none is specified separately.
		 * @param contextClass The class providing the resource context for loading.
		 * @return This builder.
		 * @throws NullPointerException if the context class is <code>null</code>.
		 */
		public Builder contextClass(@Nonnull final Class<?> contextClass) {
			this.contextClass = requireNonNull(contextClass);
			return this;
		}

		private ClassLoader classLoader = null;

		/**
		 * Sets the explicit class loader for loading resources.
		 * @param classLoader The class loader to use for loading a configuration resource.
		 * @return This builder.
		 * @throws NullPointerException if the class loader is <code>null</code>.
		 */
		public Builder classLoader(@Nonnull ClassLoader classLoader) {
			this.classLoader = requireNonNull(classLoader);
			return this;
		}

		private String resourcePath = null;

		private String resourceName = null;

		private String resourceBaseName = null;

		/**
		 * Uses a complete path to a configuration resource relative to the classpath.
		 * <p>
		 * Overrides any resource candidate base filename or resource filename.
		 * </p>
		 * @param resourcePath The complete resource path such as <code>com/example/foo.bar</code>, relative to the classpath, for loading a configuration file from
		 *          class resources.
		 * @return This builder.
		 * @throws NullPointerException if the resource path is <code>null</code>.
		 */
		public Builder resourcePath(@Nonnull final String resourcePath) {
			this.resourcePath = requireNonNull(resourcePath);
			this.resourceName = null;
			this.resourceBaseName = null;
			return this;
		}

		/**
		 * Uses a complete resource filename. A context class must be specified separately.
		 * <p>
		 * Overrides any resource path or resource candidate base filename.
		 * </p>
		 * @param resourceName The resource filename, such as <code>foo.bar</code> for loading a configuration resource relative to the given class.
		 * @return This builder.
		 * @throws NullPointerException if the resource filename is <code>null</code>.
		 */
		public Builder resourceName(@Nonnull final String resourceName) {
			this.resourceName = requireNonNull(resourceName);
			this.resourceBaseName = null;
			return this;
		}

		/**
		 * Uses a resource candidate base filename.
		 * <p>
		 * Overrides any resource path or resource candidate base filename.
		 * </p>
		 * @param resourceBaseName The base filename, such as <code>base</code>, to locate resources with extensions, such as <code>base.foo</code>, supported by
		 *          installed configuration file formats.
		 * @return This builder.
		 * @throws NullPointerException if the resource base filename is <code>null</code>.
		 */
		public Builder candidateResourceBaseName(@Nonnull final String resourceBaseName) {
			this.resourcePath = null;
			this.resourceName = null;
			this.resourceBaseName = requireNonNull(resourceBaseName);
			return this;
		}

		/**
		 * Builds a configuration manager.
		 * @return A new manager built to these specifications.
		 * @throws IllegalStateException if no candidate path(s) have been specified.
		 */
		public ResourcesConfigurationManager build() {
			//determine class loader
			ClassLoader classLoader = this.classLoader;
			if(classLoader == null) {
				checkState(this.contextClass != null, "No class loader could be determined.");
				classLoader = this.contextClass.getClassLoader();
			}
			assert classLoader != null;

			//determine resource (base) path
			String resourcePath = this.resourcePath;
			if(resourcePath == null) { //there must always be a resource path, either a full path or a base path
				if(this.resourceName != null) { //if a filename was given, determine the full path
					checkState(this.contextClass != null, "No context class for determining resource path from filename.");
					resourcePath = resolveResourcePath(contextClass, resourceName);
				} else if(this.resourceBaseName != null) { //if a base filename was given, determine the base path
					checkState(this.contextClass != null, "No context class for determining resource base path.");
					resourcePath = getResourceBasePath(contextClass);
				} else {
					throw new IllegalStateException("Insufficient information for determining configuration resource path.");
				}
			}
			assert resourcePath != null;

			return new ResourcesConfigurationManager(fileFormats, classLoader, resourcePath, resourceBaseName, required);
		}

		/**
		 * Builds a manage configured, managed by new resources configuration manager, and using the specified parent configuration, if any.
		 * @return A new configuration managed by a manager built to these specifications.
		 * @throws IllegalStateException if no candidate path(s) have been specified.
		 * @see #parentConfiguration(Configuration)
		 */
		public ManagedConfiguration buildConfiguration() {
			return new ManagedConfiguration(build(), parentConfiguration);
		}

	}

}
