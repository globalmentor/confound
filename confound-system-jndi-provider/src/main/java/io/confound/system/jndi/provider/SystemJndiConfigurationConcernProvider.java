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

package io.confound.system.jndi.provider;

import static io.confound.Confound.*;

import java.util.stream.Stream;

import javax.naming.NamingException;

import io.confound.DefaultConfigurationConcern;
import io.confound.config.ConfigurationException;
import io.confound.config.jndi.JndiConfiguration;
import io.csar.Concern;
import io.csar.ConcernProvider;

/**
 * Automatically creates a configuration concern that loads a configuration from JNDI, which can be overridden by system properties or environment variables.
 * @author Garret Wilson
 */
public class SystemJndiConfigurationConcernProvider implements ConcernProvider {

	@Override
	public Stream<Concern> concerns() {
		final JndiConfiguration jndiConfiguration;
		try {
			jndiConfiguration = new JndiConfiguration();
		} catch(final NamingException namingException) {
			throw new ConfigurationException(namingException);
		}
		return Stream.of(new DefaultConfigurationConcern(jndiConfiguration.withFallback(getSystemConfiguration())));
	}

}
