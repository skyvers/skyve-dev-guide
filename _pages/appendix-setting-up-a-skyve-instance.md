---
title: "Setting up a Skyve instance"
permalink: /appendix-setting-up-a-skyve-instance/
excerpt: "Setting up a Skyve instance"
toc: true
sidebar:
  title: "Index"
  nav: docs
---

## Recommended requirements 
We recommend the following:
- 4GB RAM for Linux and 8GB RAM for Windows
- Java JDK 11 (this is the JDK for Java 11)
- Wildfly 20+
- Disk space requirements depend on the nature of the application especially if the database and content repository are located on the same drive, however, for most common applications, 50GB drive space will probably be sufficient.

## Installation of prerequisites
To run a Skyve application, the server requires:

Java 11 - while the JRE is sufficient, the JDK is recommended.
 - Download the Java JDK 11 from https://www.oracle.com/technetwork/java/javase/downloads 

Wildfly 20+
 - Download from http://wildfly.org/downloads/   
 - This link may assist (even though it is for an older version of Wildfly)- https://linuxtechlab.com/wildfly-10-10-1-0-installation/ 

## Installing database driver
For database access, load the appropriate driver and declare this driver in the Wildfly standalone.xml configuration file.

For example, for SQL Server:
- load the sqljdbc42.jar into wildfy.../modules/system/layers/base/com/microsoft/sqlserver/main/
- copy the following definition into a new file  wildfy.../modules/system/layers/base/com/microsoft/sqlserver/main/module.xml

```xml
    <?xml version="1.0" encoding="utf-8"?> 
        <module xmlns="urn:jboss:module:1.3" name="com.microsoft.sqlserver"> 
        <resources> 
                <resource-root path="sqljdbc42.jar"/> 
        </resources> 
        <dependencies> 
                <module name="javax.api"/> 
                <module name="javax.transaction.api"/>
            <module name="javax.xml.bind.api"/>
        </dependencies> 
    </module>
```

- declare the driver in the wildfly configuration file wildfly/standalone/configuration/standalone.xml `<drivers>` stanza as follows:

```xml
    <driver name="sqlserver" module="com.microsoft.sqlserver">
        <xa-datasource-class>com.microsoft.sqlserver.jdbc.SQLServerXADataSource</xa-datasource-class>
    </driver>
```

## Other datasource options

There are a number of optional settings for the application data source file `myApplication-ds.xml`. The file provided from the Skyve project creator is usually satisfactory, however the following describes other options which you may need to consider.

Option | Values | Description
-------|--------|----------
*jta* | *true*/*false* | this should be *false* for Skyve applications unless you are using ccm (see below) - refer to http://www.mastertheboss.com/jboss-server/jboss-datasource/demystifying-datasource-jta-and-xa-settings-on-jboss-wildfly 
*use-ccm* |  *true*/*false* | Essentially this will log leaked connections - "The Cached Connection Manager is used for debugging data source connections and supporting lazy enlistment of a data source connection in a transaction, tracking whether they are used and released properly by the application. At the cost of some overhead, it can provide tracing of the usage, and make sure that connections from a data source are not leaked by your application. Although that seems like an advantage, in some instances it's considered an anti-pattern and so to be avoided." (from <a href="https://access.redhat.com/documentation/en-us/jboss_enterprise_application_platform/5/html/performance_tuning_guide/chap-performance_tuning_guide-servlet_container#sect-Performance_Tuning_Guide-Servlet_Container-Cached_Connection_Manager">JBoss AS 5 Administration Guide</a>).

Additional information is available from a number of sources, for example <a href="https://developer.jboss.org/wiki/ConfigDataSources">for example</a>.

## Additional configuration

The validation stanza provides Wildfly with the required configuration to create new connections as needed.

From <a href="https://stackoverflow.com/questions/128527/is-there-any-way-to-have-the-jboss-connection-pool-reconnect-to-oracle-when-conn">this page</a>  *"If the validation query executes successfully, the pool will return that connection. If the query does not execute successfully, the pool will create a new connection"*.
A typical validation stanza is as follows:

```xml                
<validation>
  <check-valid-connection-sql>select 1</check-valid-connection-sql>
  <validate-on-match>true</validate-on-match>
  <background-validation>false</background-validation>
  <background-validation-millis>0</background-validation-millis>
</validation>
<statement>        
  <prepared-statement-cache-size>0</prepared-statement-cache-size>
  <share-prepared-statements>false</share-prepared-statements>
</statement>
```

Note that the sql statement for `<check-valid-connection-sql>` is dialect specific - so consider your specific configuration carefully.

From the <a href="https://docs.jboss.org/jbossas/docs/Server_Configuration_Guide/beta500/html/ch13s13.html">documentation</a>:
- `<prepared-statement-cache-size>` - the number of prepared statements per connection to be kept open and reused in subsequent requests. They are stored in a LRU cache. The default is 0 (zero), meaning no cache.
-  `<share-prepared-statements>` - with prepared statement cache enabled whether two requests in the same transaction should return the same statement

## Configuring ports
To configure which ports will be used for accessing the application, modify the <socket-binding-group> section in the wildfly configuration file wildfly/standalone/configuration/standalone.xml for http and https:

```xml
    <socket-binding name="http" port="${jboss.http.port:8080}"/>
    <socket-binding name="https" port="${jboss.https.port:8443}"/>
```

For example, for external access, typically you would assign as follows:

```xml
    <socket-binding name="http" port="${jboss.http.port:80}"/>
    <socket-binding name="https" port="${jboss.https.port:443}"/>
```

## Create a folder for content
Skyve includes content management - for file uploads and images - the repository requires a dedicated folder to persist files. The user credential running wildfly (for example) will need read-write permissions to this folder.

## Create a folder for addins
Typically, it's easiest to add one add-ins subfolder for all your projects, and refer to it directly in all project JSON settings files *(note the trailing slash)*:

```json
	// Add-ins settings
	"addins": {
		// Where to look for add-ins - defaults to <content.directory>/addins/
		"directory": "C:/_/content/addins/"
	},	
```

Load the `skyve-content-<version>.zip` (this manages content locally) into the addins by:
* right-click your project and Run As -> Maven install
* the `skyve-content-<version>.zip` will be downloaded to your target folder - the results of the maven install will include this location, usually `<project>\target\`

```
[INFO] --- maven-dependency-plugin:2.8:copy (copy-content-addin-dependency) @ myproject ---
[INFO] Configured Artifact: org.skyve:skyve-content:8.0.0:zip
[INFO] Copying skyve-content-8.0.0.zip to C:\_\j11\myproject\target\skyve-content-8.0.0.zip
```

* copy the `skyve-content-<version>.zip` to the directory specified in the JSON settings file above.

## Install Wildfly as a Service

So that the Skyve application will be always available, install the wildfly service, ensuring that the service will have read/write access to the content folder.

The following [link](https://community.i2b2.org/wiki/display/getstarted/2.4.2.3+Run+Wildfly+as+a+Linux+Service) may be useful for linux installations.

For Windows:
1. Configure Wildfly as a Windows service (requires UAC)
    1. go to the directory `C:\wildfly\docs\contrib\scripts`
    1. copy the folder "service"
    1. go to the directory `C:\wildfly\bin`
    1. paste the service folder there
    1. Hold down shift and right click on the `C:\wildfly\bin\service` folder
    1. Select *Open Powershell Window Here*
    1. Type `.\service.bat install` and press *Enter*
    1. Click *Yes* to the User Account Control prompt
    1. The last line of text should say "Service Wildfly installed"
    1. Close the Powershell window
    1. Click the Windows key and type services and click the Services control panel
    1. Find the *Wildfly* service and double click
    1. Change *Startup Type* to *Automatic (Delayed Start)*
    1. Click *Apply*
    1. Click *Start* then *OK*
    1. Check the service starts and does not stop straight away
    1. Open your browser and go to http://localhost:8080 and check the Wildfly splash screen shows

**[⬆ back to top](#recommended-requirements)**

---
**Next [Common Wildfly configuration settings](./../_pages/appendix-common-wildfly-configuration-settings.md)**<br>
**Previous [Installing and configuring a Skyve development environment](./../_pages/appendix-installing-and-configuring.md)**
