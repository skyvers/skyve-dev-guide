---
title: "Common Wildfly settings"
permalink: /appendix-common-wildfly-configuration-settings/
excerpt: "Common Wildfly settings"
toc: true
sidebar:
  title: "Index"
  nav: docs
---

## Common Wildfly configuration settings

### Configuring Wildfly for virtual hosts

It is possible to configure several applications to use the base context of different URLs on the same machine, by configuring virtual hosts.

Instructions are available, for example [Virtual hosts](http://www.mastertheboss.com/jboss-web/jbosswebserver/jboss-as-virtual-host-configuration).

For example, if you have 3 applications hosted on the same server, but you want to access them via different urls (rather than different contexts on the same url):

Declare the hosts in the standalone configuration.xml file as follows:

```xml
<host name="project1" alias="project1.skyve.org" default-web-module="project1.war" >
    <access-log prefix="project1"/>
</host>
<host name="project2" alias="project2.skyve.org" default-web-module="project2.war" >
    <access-log prefix="project2"/>
</host>
<host name="project3" alias="project3.skyve.org" default-web-module="project3.war" >
    <access-log prefix="project3"/>
</host>
```

To access your projects from the root context of the above URLs, specify the root context for each project in 

`.../WEB-INF/jboss-web.xml`

as follows:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<jboss-web xmlns="http://www.jboss.com/xml/ns/javaee"
		   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		   xsi:schemaLocation="http://www.jboss.com/xml/ns/javaee http://www.jboss.org/j2ee/schema/jboss-web_5_1.xsd">
	<context-root>/</context-root>
</jboss-web>
```

## Managing Wildfly server logs

You can either use the periodic rotating file handler:

```xml
<periodic-rotating-file-handler name="FILE" autoflush="true">
    <formatter>
        <named-formatter name="PATTERN"/>
    </formatter>
    <file relative-to="jboss.server.log.dir" path="server.log"/>
    <suffix value=".yyyy-MM-dd"/>
    <append value="true"/>
</periodic-rotating-file-handler>
```

or the size rotating file handler:

```xml
<size-rotating-file-handler name="FILE" autoflush="true" rotate-on-boot="true">
   <formatter>
       <named-formatter name="PATTERN"/>
   </formatter>
   <file relative-to="jboss.server.log.dir" path="server.log"/>
   <rotate-size value="40m"/>
   <max-backup-index value="5"/>
   <append value="true"/>
</size-rotating-file-handler>
```

## Managing Post Size (large file upload)

Add the `max-post-size` setting to the http-listener as follows:

```xml
<subsystem xmlns="urn:jboss:domain:undertow:3.1">
    <buffer-cache name="default"/>
    <server name="default-server">
        <http-listener name="default" socket-binding="http" max-post-size="4294967296" redirect-socket="https" enable-http2="true"/>
        <https-listener name="https" socket-binding="https" security-realm="ApplicationRealm" enable-http2="true"/>
        <host name="default-host" alias="localhost">
            <location name="/" handler="welcome-content"/>
            <filter-ref name="server-header"/>
            <filter-ref name="x-powered-by-header"/>
        </host>
	</server>
```

Specifying a `max-post-size` of `0` will disable the upload limit entirely.

**[⬆ back to top](#common-wildfly-configuration-settings)**

---
**Previous [Setting up a Skyve instance](./../_pages/appendix-setting-up-a-skyve-instance.md)**
