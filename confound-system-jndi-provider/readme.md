# Confound System JNDI Provider

Csar concern provider for a JNDI configuration, with overriding system properties or environment variables.

One way JNDI context variables could be configured would be in a `WEB-INF/web.xml` file, for example:

```xml
	<env-entry>
		<env-entry-name>foo</env-entry-name>
		<env-entry-type>java.lang.String</env-entry-type>
		<env-entry-value>bar</env-entry-value>
	</env-entry>
```

## Download

Confound JNDI System Provider is available in the Maven Central Repository as [io.confound:confound-system-jndi-provider](https://search.maven.org/search?q=g:io.confound%20and%20a:confound-system-jndi-provider).

## Issues

Issues tracked by [JIRA](https://globalmentor.atlassian.net/projects/CONFOUND).

## Changelog

- 0.6.0: First release.
