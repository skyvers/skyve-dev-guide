If it is custom auth (via the headers or something)
You would want to make sure spring security lets it through in the security.xml
```<http auto-config="true" use-expressions="true" security="none" pattern="/MyRestService**" />```