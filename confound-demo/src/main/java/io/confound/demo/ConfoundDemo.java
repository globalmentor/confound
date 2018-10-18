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

import javax.annotation.*;

import io.confound.Confound;
import io.confound.config.*;
import io.confound.config.file.FileSystemConfigurationManager;

/**
 * Demonstration of using Confound.
 * <p>
 * General demo of configuring and using Confound. To test Confound using this application, do one of the following:
 * </p>
 * <ul>
 * <li>Set the environment variable <code>FOO</code>.</li>
 * <li>Set the system property <code>foo</code>.</li>
 * <li>Place a configuration file defining the <code>foo</code> parameter in <code>~/confound-demo</code> using one of the following file formats:
 * <ul>
 * <li>config.properties</li>
 * <li>config.xml.properties</li>
 * <li>config.turf</li>
 * </ul>
 * </li>
 * </ul>
 * @author Garret Wilson
 */
public class ConfoundDemo {

	/**
	 * Main application entry point.
	 * @param args Command-line arguments.
	 */
	public static void main(@Nonnull final String[] args) {
		final Path configDirectory = Paths.get(System.getProperty("user.home"), ".confound-demo");

		//the main configuration is from a file
		final Configuration fileConfig = new FileSystemConfigurationManager.Builder().candidateBaseFilename(configDirectory, "config").buildConfiguration();

		//the system configuration can override the file configuration
		final Configuration config = Confound.getSystemConfiguration(fileConfig);

		setDefaultConfiguration(config);

		System.out.println(String.format("Foo is %s.", getConfiguration().getOptionalString("foo").orElse("[missing]")));
	}

}
