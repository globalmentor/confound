# Confound Application Demo

Demonstration of using Confound in an application.

The application requires that you specify an application data directory using either a system propery `app.data.dir` or an environment variable `APP\_DATA\_DIR`. This data directory may be absolute, or relative to the user home directory. A configuration file will be discovered in this directory with the filename `config.*` based upon the supported file formats.

The application will print the value of the `foo` configuration parameter.
