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

package io.confound.demo.webapp;

import static io.confound.Confound.*;
import static java.nio.charset.StandardCharsets.*;

import java.io.IOException;

import javax.annotation.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 * Demonstration of using Confound in a webapp servlet, by default mapped to <code>…confound-demo-webapp/demo</code>.
 * <p>
 * This application demonstrates using a single <code>confound-jndi-system-provider</code> dependency to automatically find a <code>foo</code> configuration
 * property stored in JNDI. In this demo the value is stored in the <code>WEB-INF/web.xml</code> file:
 * </p>
 * 
 * <pre>
 * {@code	<env-entry>
 *   <env-entry-name>foo</env-entry-name>
 *   <env-entry-type>java.lang.String</env-entry-type>
 *   <env-entry-value>bar</env-entry-value>
 * </env-entry>}
 * </pre>
 * <p>
 * The JNDI properties can be overridden by system properties or environment variables.
 * </p>
 * @author Garret Wilson
 */
@WebServlet("/demo")
public class ConfoundDemoServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * Main application entry point.
	 * @param args Command-line arguments.
	 */
	public static void main(@Nonnull final String[] args) {
		System.out.println(String.format("Foo is %s.", getConfiguration().findString("foo").orElse("[missing]")));
	}

	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding(UTF_8.name());
		response.getWriter().println(String.format("Foo is %s.", getConfiguration().findString("foo").orElse("[missing]")));
	}

}
