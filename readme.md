# Confound

The _Configuration Foundation_ ([**Confound**](https://confound.io/)) library provides a lightweight yet powerful model for accessing various types of application configuration facilities via [Csar](https://csar.io).

More complex configuration may be using `Confound.setDefaultConfigurationConcern(ConfigurationConcern)` with the concern of choice, as in the following example:

```java
Confound.setDefaultConfigurationConcern(new MyConfigurationConcern());
```

## Quick Start

_This initial release of Confound only supports system variables and environment variables._

An application can easily integrate Confound configuration support with just a few steps:

1. Include the dependency `io.confound:confound:x.x.x`.
2. Pass configuration information to the application using an environment variable such as `FOO_BAR` or a system property such as `foo.bar`.
3. Call `Confound.getConfiguration().getXXX("foo.bar")` to retrieve the type of parameter you desire. _This will retrieve the system property `foo.bar` or, if not present, the environment variable `FOO\_BAR`, using the appropriate case automatically._

Note: Confounded also provides a mixin interface `Confounded` that allows an instance of `MyClass` to call `getConfiguration().getXXX("foo.bar")`.  

Other Confound implementations in addition to system and environment variables will be available in the future.

## Download

Confound is available in the Maven Central Repository in group [io.confoud](https://search.maven.org/#search|ga|1|g%3A%22io.confound%22).

## Issues

Issues tracked by [JIRA](https://globalmentor.atlassian.net/projects/CONFOUND).

## Changelog

- 0.5.0: First release.
