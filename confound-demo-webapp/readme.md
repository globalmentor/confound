# Confound Web Application Demo

Demonstration of using Confound in a web application.

The configuration will automatically be set up to retrieve values via JNDI as provided in the `WEB-INF/web.xml` file in a `<env-entry>`, although these can be overridden with system properties or environment variables.

The application will print the value of the `foo` configuration parameter at the `…/confound-demo-webapp/demo` path.
