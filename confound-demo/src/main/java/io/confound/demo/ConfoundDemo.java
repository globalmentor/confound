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

package io.confound.demo;

import static io.confound.Confound.*;

import java.nio.file.*;
import java.util.*;

import javax.annotation.*;

import io.confound.*;
import io.confound.config.*;
import io.confound.config.file.*;
import io.csar.Csar;

/**
 * Demonstration of using Confound for system properties, environment variables, class resources, and files; as well as creating separate configuration contexts
 * using Csar.
 * <p>
 * General demo of configuring and using Confound. To test Confound using this application, do one of the following:
 * </p>
 * <ul>
 * <li>Set the environment variable <code>FOO</code>.</li>
 * <li>Set the system property <code>foo</code>.</li>
 * <li>Place a configuration file defining the <code>foo</code> parameter in <code>~/.confound-demo</code> using one of the following file formats:
 * <ul>
 * <li>config.properties</li>
 * <li>config.xml.properties</li>
 * <li>config.turf</li>
 * </ul>
 * </li>
 * </ul>
 * <p>
 * If no overriding configuration is given, this demo falls back to the built-in configuration file in the resources, which has <code>foo</code> set to
 * <code>bar</code>.
 * </p>
 * @author Garret Wilson
 */
public class ConfoundDemo {

	/**
	 * Main application entry point.
	 * @param args Command-line arguments.
	 */
	public static void main(@Nonnull final String[] args) {

		//#default configuration
		final Path configDirectory = Paths.get(System.getProperty("user.home"), ".confound-demo");

		//the main configuration is from resources with default base name "config.*"
		final Configuration resourcesConfig = new ManagedConfiguration(ResourcesConfigurationManager.forClass(ConfoundDemo.class));

		//the secondary configuration is from a file with default base filename "config.*"
		final Configuration fileConfig = new ManagedConfiguration(FileSystemConfigurationManager.forDirectory(configDirectory), resourcesConfig);

		//the system configuration can override the file configuration
		final Configuration config = Confound.getSystemConfiguration(fileConfig);

		setDefaultConfiguration(config);

		//#local configuration
		final Map<String, Object> localConfigMap = new HashMap<String, Object>(); //TODO use Java 9 Map.of()
		localConfigMap.put("foo", "some other value");
		final Configuration localConfig = new ObjectMapConfiguration(localConfigMap);
		//Java 9+: final Configuration localConfig = new ObjectMapConfiguration(Map.of("foo", "some other value"));
		final ConfigurationConcern localConfigConcern = new DefaultConfigurationConcern(localConfig);

		//#print "foo" value in two separate contexts
		System.out.println(String.format("By default foo is %s.", getConfiguration().getOptionalString("foo").orElse("[missing]")));

		Csar.run(localConfigConcern, () -> {
			System.out.println(String.format("In another context foo is %s.", getConfiguration().getOptionalString("foo").orElse("[missing]")));
		});

	}

}
