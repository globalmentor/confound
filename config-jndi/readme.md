# JNDI Context Configuration

Confound configuration implementation backed by JNDI context variables.

One way JNDI context variables could be configured would be in a `WEB-INF/web.xml` file, for example:

```xml
	<env-entry>
		<env-entry-name>foo</env-entry-name>
		<env-entry-type>java.lang.String</env-entry-type>
		<env-entry-value>bar</env-entry-value>
	</env-entry>
```

## Download

JNDI Context Configuration is available in the Maven Central Repository as [io.confound:config-jndi](https://search.maven.org/search?q=g:io.confound%20and%20a:config-jndi).

## Issues

Issues tracked by [JIRA](https://globalmentor.atlassian.net/projects/CONFOUND).

## Changelog

- 0.5.0: First release.
