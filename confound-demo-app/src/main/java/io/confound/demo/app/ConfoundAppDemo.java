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

package io.confound.demo.app;

import static io.confound.Confound.*;

import javax.annotation.*;

/**
 * Demonstration of using Confound in an application.
 * <p>
 * This application demonstrates using a single <code>confound-app-provider</code> dependency to automatically discover an application data directory in
 * <code>app.data.dir</code>, indicated as a system property or as an environment variable. The directory can be absolute or relative to the user home
 * directory. The configuration file will be discovered in that directory with the base filename <code>config</code> and an extension based upon the file format
 * providers installed.
 * </p>
 * <p>
 * By default properties files and XML properties files are supported.
 * </p>
 * @author Garret Wilson
 */
public class ConfoundAppDemo {

	/**
	 * Main application entry point.
	 * @param args Command-line arguments.
	 */
	public static void main(@Nonnull final String[] args) {
		System.out.println(String.format("Foo is %s.", getConfiguration().getOptionalString("foo").orElse("[missing]")));
	}

}
