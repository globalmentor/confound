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

package io.confound.config.urf;

import static org.junit.Assert.*;

import java.util.*;

import static com.github.npathai.hamcrestopt.OptionalMatchers.*;
import static org.hamcrest.Matchers.*;
import org.junit.*;

import io.urf.model.UrfObject;

/**
 * Tests of {@link UrfConfiguration}.
 * @author Garret Wilson
 */
public class UrfConfigurationTest {

	@Test
	public void testRootUrfObject() {
		final UrfObject urfObject = new UrfObject("Configuration");
		urfObject.setPropertyValue("foo", "bar");
		urfObject.setPropertyValue("flag", Boolean.TRUE);
		final UrfConfiguration urfConfiguration = new UrfConfiguration(urfObject);
		assertThat(urfConfiguration.hasParameter("foo"), is(true));
		assertThat(urfConfiguration.getString("foo"), is("bar"));
		assertThat(urfConfiguration.hasParameter("flag"), is(true));
		assertThat(urfConfiguration.getBoolean("flag"), is(true));
		assertThat(urfConfiguration.hasParameter("none"), is(false));
		assertThat(urfConfiguration.getOptionalParameter("none"), isEmpty());
	}

	@Test
	public void testRootMap() {
		final Map<String, Object> map = new HashMap<>();
		map.put("foo", "bar");
		map.put("flag", Boolean.TRUE);
		final UrfConfiguration urfConfiguration = new UrfConfiguration(map);
		assertThat(urfConfiguration.hasParameter("foo"), is(true));
		assertThat(urfConfiguration.getString("foo"), is("bar"));
		assertThat(urfConfiguration.hasParameter("flag"), is(true));
		assertThat(urfConfiguration.getBoolean("flag"), is(true));
		assertThat(urfConfiguration.hasParameter("none"), is(false));
		assertThat(urfConfiguration.getOptionalParameter("none"), isEmpty());
	}

}
