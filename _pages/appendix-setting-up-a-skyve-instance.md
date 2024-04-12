---
title: "Setting up a Skyve instance"
permalink: /appendix-setting-up-a-skyve-instance/
excerpt: "Setting up a Skyve instance"
toc: true
sidebar:
  title: "Index"
  nav: docs
---

The following page describes the steps required to set up an on-premise Skyve server instance. This is a high-level overview and assumes a basic understanding of the technologies involved. For more detailed information, refer to the documentation for the specific technologies.

## Recommended requirements

The minimum server requirements are: 

- 4GB RAM for Linux and 8GB RAM for Windows
- Java JDK 17 (this is the JDK for Java 17)
- Wildfly 30+
- Disk space requirements vary based on the application, especially if the database and content repository share the same drive. However, most applications should run smoothly with at least 50GB of drive space.

## Installation steps

### Windows

1. Download and install Java 17 or 21 (requires UAC)
    1. This can be downloaded from https://adoptium.net/temurin/releases/?os=windows&arch=x64&package=jdk
    2. Change the defaults to also install the `JAVA_HOME` environment variable
    3. Complete the wizard and close after the installation has completed
1. Configure Environment Variables (requires UAC)
    1. Search for `environment` in the Start menu and open *Edit the system environment variables*
    2. Click the *Environment Variables* button
    3. If `JAVA_HOME` is not present
        1. Click *New...* in the System variables section
        1. Enter `JAVA_HOME` for the variable name
        1. Click Browse Directory... and browse to where the JDK was just installed, should be similar to `C:\Program Files\Eclipse Adoptium\jdk-17xxx`
        1. Click *OK*
    4. If the system has 8GB memory or more, click *New...* in the System variables section
    5. Enter "JAVA_OPTS" for the variable name
    6. Enter variable value `-Xms4G -Xmx4G -XX:MetaspaceSize=96M -XX:MaxMetaspaceSize=2G`
    7. Click OK 3 times and then close the System window
1. Download and install Wildfly
    1. This can be downloaded from https://www.wildfly.org/downloads/
    1. Unzip the downloaded `wildfly-x.x.x.Final.zip`
    1. Move to the root of a drive, e.g. `C:\`
1. Install any database specific drivers into Wildfly, e.g. for Microsoft SQL Server:
    1. Download the [SQL Server JDBC Driver](https://go.microsoft.com/fwlink/?linkid=2262747)
    1. Extract the JDBC driver
    1. Navigate to `C:\wildfly-26.1.2.Final\modules\system\layers\base\com\microsoft\`
    1. Create a new folder `sqlserver`
    1. Open `sqlserver` and create a new folder `main`
    1. Copy the downloaded `mssql-jdbc-12.6.1.jre11.jar` into main
    1. Create a new file `module.xml` and paste the contents

```xml
<?xml version="1.0" encoding="utf-8"?> 
<module xmlns="urn:jboss:module:1.3" name="com.microsoft.sqlserver"> 
  <resources> 
    <resource-root path="mssql-jdbc-12.6.1.jre11.jar"/> 
  </resources> 
  <dependencies> 
    <module name="javax.api"/> 
    <module name="javax.transaction.api"/> 
  </dependencies> 
</module>
```

1. Edit `wildfly/standalone/configuration.xml`
    1. Add the SQL server driver configuration to the `drivers` section of the xml
	
```xml
<driver name="sqlserver" module="com.microsoft.sqlserver">
    <xa-datasource-class>com.microsoft.sqlserver.jdbc.SQLServerXADataSource</xa-datasource-class>
</driver>
```

1. Depending on your port-forwarding configuration, you may need to change the HTTPS listen port from `8443` to `443`

```xml
<socket-binding name="https" port="${jboss.https.port:443}"/>
```

1. Find <interface name=”public”> and change to the following

```xml
<interface name="public">
    <inet-address value="${jboss.bind.address:0.0.0.0}"/>
</interface>
```

1. Configure Wildfly as a Windows service (requires UAC)
    1. go to the directory `C:\wildfly\docs\contrib\scripts`
    1. copy the folder `service`
    1. go to the directory `C:\wildfly\bin`
    1. paste the service folder there
    1. Hold down shift and right click on the `C:\wildfly\bin\service` folder
    1. Select *Open Powershell Window Here*
    1. Type `.\service.bat install` and press *Enter*
    1. Click *Yes* to the User Account Control prompt
    1. The last line of text should say "Service Wildfly installed"
    1. Close the Powershell window
    1. Click the Windows key and type _services_, and open the Services control panel
    1. Find the *Wildfly* service and double click
    1. Change *Startup Type* to *Automatic (Delayed Start)*
    1. Click *Start* then *OK*
    1. Check the service starts and does not stop straight away
1. Create a new folder called `content` in `C:\`
1. Create a new folder called `addins` in `C:\content\`
1. Update your application `appName.json` and specify the `content` path, `addins` path and environment `identifier`
1. Deploy the application
    1. Copy `skyve-content-x.x.x.zip` to `C:\content\addins`
    1. Copy `appName.war.zip`, `appName.json` and `appName-ds.xml` to `C:\wildfly-x.x.x.Final\standalone\deployments`, replacing any existing files
    1. Unzip `appName.war.zip`
    1. Right click -> New Text File
    1. Rename the text file to `appName.war.dodeploy` (make sure no `.txt` on the end)
1. Once authenticated, remove the `identifier` and `bootstrap` from the json file and redeploy the application

1. If required, create an inbound firewall rule for TCP port 443
    1. Click start and type firewall and select "Windows Defender Firewall"
    1. Click "Advanced settings"
    1. Click "Inbound Rules" on the select, then "New Rule..." on the right
    1. Select Port and click Next
    1. Leave TCP selected and type `443` into "Specific local ports" and click Next
    1. Leave "Allow the connection" selected and click Next
    1. Uncheck Private and Public networks and click Next
    1. Enter Wildfly as the name, and "Network port for Wildfly application server for Skyve application." as the Description
    1. Click Finish

### Linux (Ubuntu)

 Assume you are working from a `sudoer`

- Install OpenJDK >= 17
    - `sudo apt update`
    - `sudo apt install default-jdk`
    - Test java is installed
        - `which java`
        - `java -version`
- Create the wildfly user
    - `sudo groupadd -r wildfly`
    - `sudo useradd -r -g wildfly -d /opt/wildfly -s /sbin/nologin wildfly`
- Install wildfly >= 27
    - `wget https://github.com/wildfly/wildfly/releases/download/31.0.1.Final/wildfly-31.0.1.Final.tar.gz -P /tmp`
    - Untar to /opt
        - `sudo tar xf /tmp/wildfly-31.0.1.Final.tar.gz -C /opt/`
    - Create a symbollic link
        - `sudo ln -s /opt/wildfly-31.0.1.Final /opt/wildfly`
    - Change wildfly install owner
        - `sudo chown -RH wildfly: /opt/wildfly`
- Run as a service
    - `sudo mkdir -p /etc/wildfly`
    - `sudo cp /opt/wildfly/docs/contrib/scripts/systemd/wildfly.conf /etc/wildfly/`
    - `sudo cp /opt/wildfly/docs/contrib/scripts/systemd/launch.sh /opt/wildfly/bin/`
    - Make wildfly/bin content executable
        - `sudo sh -c 'chmod +x /opt/wildfly/bin/*.sh'`
    - `sudo cp /opt/wildfly/docs/contrib/scripts/systemd/wildfly.service /etc/systemd/system/`
    - Reload service definitions
        - `sudo systemctl daemon-reload`
    - Start the wildfly service
        - `sudo systemctl start wildfly`
        - Check it
            - `sudo systemctl status wildfly`
    - Start the service automatically at boot time
        - `sudo systemctl enable wildfly`
- Optionally open port 8080
    - `sudo ufw allow 8080/tcp`
- Optionally change Wildfly JVM memory settings
	- Edit `/opt/wildfly/bin/standalone.conf` and locate 
	
```
#
# Specify options to pass to the Java VM.
#
if [ "x$JBOSS_JAVA_SIZING" = "x" ]; then
   JBOSS_JAVA_SIZING="-Xms64m -Xmx512m -XX:MetaspaceSize=96M -XX:MaxMetaspaceSize=256m"
fi
```

and replacing with (for example)

```
if [ "x$JBOSS_JAVA_SIZING" = "x" ]; then
   JBOSS_JAVA_SIZING="-Xms4g -Xmx4g -XX:MetaspaceSize=96M -XX:MaxMetaspaceSize=512m"
fi
```

- To start, stop, restart the wildfly service:
    - `sudo service wildfly restart`
    - availabile options are `start`/`status`/`stop`/`restart`

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
