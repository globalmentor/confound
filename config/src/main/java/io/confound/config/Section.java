/*
 * Copyright © 2019 GlobalMentor, Inc. <http://www.globalmentor.com/>
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

package io.confound.config;

import java.util.Optional;

/**
 * A section of a configuration that divides part of the configuration and possibly indicates some classification.
 * @apiNote Any root {@link Configuration}, such as loaded from a file, may or may not be a {@link Section} depending on the implementation.
 * @author Garret Wilson
 */
public interface Section extends Configuration {

	/** @return An identifier indicating the classification of the section, if available. */
	public Optional<String> getSectionType();

}
