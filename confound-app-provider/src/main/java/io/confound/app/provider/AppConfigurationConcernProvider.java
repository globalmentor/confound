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

package io.confound.app.provider;

import java.util.stream.Stream;

import io.confound.app.AppConfigurationConcernBuilder;
import io.csar.Concern;
import io.csar.ConcernProvider;

/**
 * Automatically creates a configuration concern that loads a configuration file for an application.
 * <p>
 * The configuration manager will discover the configuration file in an <dfn>application data directory</dfn> indicated by a configuration base filename
 * {@value AppConfigurationConcernBuilder#DEFAULT_CONFIG_BASE_FILENAME} indicated as a system property or as an environment variable. The indicated application
 * data directory may be absolute, or relative to the user home directory.
 * </p>
 * <p>
 * If no application directory can be determine, a configuration exception is thrown.
 * </p>
 * @author Garret Wilson
 * @see AppConfigurationConcernBuilder
 */
public class AppConfigurationConcernProvider implements ConcernProvider {

	@Override
	public Stream<Concern> concerns() {
		return Stream.of(new AppConfigurationConcernBuilder().build());
	}

}
