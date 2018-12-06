---
title: "Appendix"
permalink: /appendix/
excerpt: "Appendix"
toc: true
sidebar:
  title: "Index"
  nav: docs
---

## Deploying a Skyve Application

Skyve builds applications as a single web archive (`.war`) folder, containing the application metadata and Skyve platform components. By default, Skyve `.war` folders are deployed 'exploded' or 'unzipped'.

The `.war` folder is deployed with a `.json` settings file and a `-ds.xml` datasource file. 

For example, to deploy a Skyve application called `myApplication` using a Wildfly application server, you will need to place the following into the Wildfly `deployment/` area:

```
myApplication.war
myApplication-ds.xml
myApplication.json
```

Where 
* `myApplication.war` is the self-contained web archive containing application metadata and Skyve platform libraries
* `myApplication-ds.xml` is the datasource file containing the jdbc connection string and credentials
* `myApplication.json` is the instance-specific settings file, containing all of the settings specific to the particular instance.

You can manually signal Wildfly to deploy the datasource and application by creating files named with `.dodeploy` - i.e.:
`myApplication-ds.xml.dodeploy` and `myApplication.war.dodeploy` (the contents of the files is irrelevant - creating empty files with the correct names is sufficient). If Wildfly is running, it will detect the presence of these files and attempt to deploy them, resulting in the creation of either a `.deployed` or a `.failed` signal file.

When moving an application from DEV->TEST->PROD, the settings files on each instance remain in place unchanged - only the application web archive is updated.

This approach is part of the Skyve risk reduction strategy, to minimise the risk of problems when updating new versions of your application. Once each instance is configured, moving from DEV -> TEST -> PROD becomes a low-risk trivial activity, requiring no reconfiguration (unless the different instance have significantly different set ups).

### Deploying a new application version
Where an instance has already been configured, to deploy a new application version on each instance:
- undeploy the project (or stop Wildfly)
- remove the existing `.war` from the deployment area
- place the new `.war` folder into the deployment area
- redeploy the project (or restart Wildfly)

If the server has multiple Skyve application deployments, you can replace one of these without impacting on other deployments by signalling an undeployment and deployment as follows:

To undeploy, create an `myApplication.war.undeploy` file in the `wildfly/standalone/deployment/` folder corresponding to the name of your application (an empty text file with that name is all that is required). After about a minute, Wildlfly will replace this file with a file named `myApplication.war.undeployed`. 

To redeploy, create a `myApplication.war.dodeploy` file in the `wildfly/standalone/deployment/` folder corresponding to the name of your application (again - an empty text file with that name is all that is required). Wildfly will replace this file with `myApplication.war.isdeploying` and once deployment is successful, Wildfly will replace this with either `myApplication.war.deployed` (if successful) or `myApplication.war.failed` (if unsuccesful).

See [Configuring Wildfly](#configuring-wildfly) for more detailed Wildfly setup 
instructions. Additional steps are required for Single Sign-On configuration, 
the creation of service user accounts, SPNs and port configuration.

## Installing and configuring the Skyve Development Environment

These instructions describe the process required to install and configure the 
development environment for Skyve. These instructions assume that you are 
using Windows and SQL Server. Some changes will need to be made if using a 
different operating system or database.

### Prerequisites checklist

Before you begin, ensure you have the following:

* Java ([www.oracle.com](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)), JDK 1.8
* Eclipse IDE for Java EE developers ([www.eclipse.org](https://www.eclipse.org/downloads/)), so that the installation
is in `C:\eclipse\`
* Wildfly 13 (select the last final version available) ([http://wildfly.org](http://wildfly.org/downloads/))
* A RDBMS which is supported by Hibernate ([www.hibernate.org](http://www.hibernate.org)) - ensure you record the
  administrator username and password. 

For this example, to use MS SQL Server as the database for the Skyve project:
* If you do not already have SQL Server installed:
  * download and install the latest version of the developer or express edition for your platform from the [Microsoft website](https://www.microsoft.com/en-au/sql-server/sql-server-downloads)
  * you will also need to download and install a copy of [SQL Server Management Studio](https://docs.microsoft.com/en-us/sql/ssms/download-sql-server-management-studio-ssms) to connect you your database and execute scripts
* Open SQL Server Management Studio, choose Authentication as Windows Authentication and click the 'Connect' button.
* Create a db named 'skyve', right click Databases->New Databases... and leave everything as is and click the _OK_ button.
* Create a user for skyve db in SQL Server Management Studio, right click Security->New->Login...
* Create a Login name (remember this login name as you will need it later), choose 
  SQL Server authentication and enter your password. Remember that SQL Server policy 
  requires a 'strong' password (remember this password as you will need it later), 
  untick the 'Enforce password expiration' and the 'User must change password at 
  next login'
* On the same dialog box, choose Default database as 'skyve', now go to Server Roles 
  on left hand pane and tick 'sysadmin', then go to User Mapping and tick 'skyve' 
  and finally, click the _OK_ button down the bottom right.
* If you've just installed SQL server, you will need to specify the port for this 
  database, see instructions [here](https://community.spiceworks.com/how_to/124598-find-the-port-sql-server-is-using-and-change-a-dynamic-port-to-static), 
  again, remember the port number you've entered.

### Configuring Java
* Download jdk1.8.0 (if you haven't already) 
  (http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) 
  and install it to your machine.
* Go your Control Panel->System and Security->System (for Windows). Now select 
  'Advanced system settings' and click the "Environment Variables..." button.
* Locate 'Path' under System variables double click it, click the _New_ button and 
  enter this (with the semicolon) `;<jdk1.8.0_xxx installation folder>\bin\` and 
  click the _OK_ button and _OK_, again _OK_ to close the System Properties 
  dialog box.

### Configuring the IDE (Windows example)

* Create `C:\\_\` (go to C:\->right click->New->Folder then type "_" as the 
folder name). You may use any folder for you workspace in Eclipse, just make sure you remember 
the folder you've chosen and try to ensure that the folder name has no spaces to avoid 
any issues with Java paths and spaces.
* Start Eclipse using Eclipse.exe and select `C:\\_\` as workspace, tick 'Use as 
default option - do not ask again', Cancel the welcome wizard and Close the welcome 
tab in the editor frame.
* Change compiler compliance level to 1.8 (Window -> Preferences -> Java -> Compiler) 
press 'Apply' - press Yes for full build, and then press OK.
* To manage the Wildfly application server from Eclipse:
  * Open the server explorer window if it is not already in your workspace (Window -> Show View -> Servers)
  * Right click inside the server explorer and select New
  * Expand JBoss Community
  * If WildFly 13.x is not in the list of adapters, you will need to download them:
    * Choose JBoss, WildFly & EAP Server Tools and click Next
    * Accept the licence terms and click Finish
    * Restart Eclipse when prompted
  * Select WildFly 13.x and click _Next_
  * Accept the defaults and click _Next_
  * Click _Finish_
  
See additional details in [Setting up a Skyve instance](#setting-up-a-skyve-instance)(below)

## Importing an existing Skyve project from Git

In Eclipse, choose File->Import...>Git->Projects from Git->Next->Clone URI and set the URI (for example type in https://github.com/skyvers/skyve.git as URI), then click the _Next_ button, 
choose the master and click the _Next_ button. Choose your destination directory, 
in this example, we have chosen `C:\\_\ directory`. Then click the _Next_ button. 
The import wizard should be displayed and cloning the Skyve project.

After cloning the master, go to Project -> Clean - Select clean all projects and press OK - wait for activity to cease in bottom right corner of the eclipse window.

#### Starting the server

##### Development environment
Skyve provides a bootstrap user configuration (specified in the `.json` file) - this will insert a user with all module roles as a way to get started. The bootstrap configuration is disabled if the instance is a production instance.

##### Other environments
In UAT and PROD environments, Wildfly should be configured as a service. Refer to Wildfly documentation for detailed instructions.

## Setting up a Skyve instance

### Recommended requirements 
We recommend the following:
- 4GB RAM for Linux and 8GB RAM for Windows
- Java JDK 8u191 (this is the JDK for Java 8)
- Wildfly 13
- Disk space requirements depend on the nature of the application especially if the database and content repository are located on the same drive, however, for most common applications, 50GB drive space will probably be sufficient.

### Installation of prerequisites
To run a Skyve application, the server requires:

Java 8 (also called 1.8) - while the JRE is sufficient, the JDK is recommended.
 - Download the Java JDK 8u191 from https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html 
 - These instructions may assist for linux - https://docs.oracle.com/javase/8/docs/technotes/guides/install/linux_jdk.html#BJFGGEFG (though note that this mentions an slightly older version of Java)

Wildfly 13 
 - Download from http://wildfly.org/downloads/   
 - This link may assist - https://linuxtechlab.com/wildfly-10-10-1-0-installation/ 

### Installing database driver
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

- declare the driver in the wildfly configuration file wildfly/standalone/configuration/standalone.xml <drivers> stanza as follows:

```xml
		<driver name="sqlserver" module="com.microsoft.sqlserver">
                        <xa-datasource-class>com.microsoft.sqlserver.jdbc.SQLServerXADataSource</xa-datasource-class>
                </driver>
```

### Configuring ports
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

### Create a folder for content
Skyve includes the elastic content repository - the repository requires a dedicated folder to persist files. The user credential running wildfly (for example) will need read-write permissions to this folder.

### Install the wildfly service
So that the Skyve application will be always available, install the wildfly service, ensuring that the service will have read/write access to the content folder.
The following may be useful for linux installations - https://community.i2b2.org/wiki/display/getstarted/2.4.2.3+Run+Wildfly+as+a+Linux+Service

### Skyve application configuration
To deploy a Skyve application, there are typically three artefacts:
- the application '.war' folder
- the datasource 'ds.xml' file
- the properties '.json' file

For example, if your Skyve application is called 'myApplication', these will be:
- 'myApplication.war'
- 'myApplication-ds.xml'
- 'myApplication.json'

The 'ds.xml' and '.json' remain on the server you are deploying to, and are customised for that specific instance, so that when you are  deploying a new version of your Skyve application, the instance settings do not need to be adjusted.

When deploying the Skyve application web archive 'war', ensure that matching configuration settings are updated in the associated 'ds.xml' and '.json' configuration files. 

Ensure the 'ds.xml' file uses a connection string and credentials corresponding to the setting for the database driver above

Ensure the '.json' properties file has been updated for the specific instance including:
- content directory
- context url
- database dialect
- smtp settings
- maps and other api keys
- environment identifier

Finally, ensure that the user credential that will run the wildfly service has read/write permissions to the wildfly folder and the content folder created above.

## Changing the project URL context
Normally, the project name will be the name of the `.war` - which will be the context on the URL for example `https://skyve.org/myApplication` where `myApplication` is also the name of the project.

If you only need to change the base URL (for example, from `https://skyve.org/` to `https://myDomain.com/`), you can do this by specifiying the URL in the `myApplication.json` settings file. Similarly, if your application will operate from the base URL then make the change to the URL in the `myApplication.json` file and set the context setting to `/` for example:

```json
	// URL settings - various SKYVE URL/URI fragments - useful for linking and mailing
	url: {
		// server URL
		server: "https://myDomain.com",
		// web context path
		context: "/",
		// home path
		home: "/"
	},
```

Note that URLs without a specified port will require you to change the Wildlfy port settings suitably, for example:

```xml
        <socket-binding name="http" port="${jboss.http.port:80}"/>
        <socket-binding name="https" port="${jboss.https.port:443}"/>
```

However if you need to change the project for a different URL context, like `https://skyve.org/tax_management`, then there's a few simple steps you need to take to make that work.

1. Remove the project from your wildfly server (and ensure the `myApplication.war` is removed from the deployments area). 

2. Edit the project `pom.xml` file to change the name of the `.war` to be built:

change

```xml
	<groupId>myApplication</groupId>
	<artifactId>myApplication</artifactId>
	<version>1.0</version>

	<packaging>war</packaging>
	<name>myApplication</name>
```

to

```xml
	<groupId>myApplication</groupId>
	<artifactId>tax_management</artifactId>
	<version>1.0</version>

	<packaging>war</packaging>
	<name>tax_management</name>
```

3. Rename the `.json` settings file from `myApplication.json` to `tax_management.json`

4. Update the project `.json` file for the new context
from

```json
	// URL settings - various SKYVE URL/URI fragments - useful for linking and mailing
	url: {
		// server URL
		server: "https://skyve.org",
		// web context path
		context: "/myApplication",
		// home path
		home: "/"
	},
```

to

```json
        // URL settings - various SKYVE URL/URI fragments - useful for linking and mailing
	url: {
		// server URL
		server: "https://skyve.org",
		// web context path
		context: "/tax_management",
		// home path
		home: "/"
	},
```

4. Rename the `myApplication-ds.xml` to `tax_management-ds.xml`

5. Maven update the project (for example, in eclipse, right-click the project, choose Maven and update Project)

6. Add the project to your wildfly server

Note that 
* when deploying, you may need to manually create the `tax_management-ds.xml.dodeploy` and `tax_management.war.dodeploy` signal files. 
* You need to manually remove the previous `myApplication.war` if you didn't in the preparation stage. 
* If you didn't rename the previous `myApplication.json` and `myApplication-ds.xml`, you should these files to avoid confusion (for example if the datasource name is the same for both the old `myApplication-ds.xml` and `tax_management-ds.xml`).

## Example Deployment Instructions with Single Sign-on

The following steps are to install an instance of XXX onto a vanilla
Windows 10.

--JAVA

Install Java JDK

Set `JAVA\_HOME` to location of the Java root directory - e.g.
`C:\Java\jdk1.8.0_181`

Set `PATH` to include `Java\bin` - e.g. `C:\Java\jdk1.8.0_181\bin`

copy `sqljdbc4.jar` and `sqljdbc\_auth.dll` to `java\jdk\jre\lib\ext`

--JBOSS

Install Wildfly (unzip and copy to `c:\`) 

check/update `wildfly\bin\run.conf` settings for memory -
`JAVA\_OPTS="-Xms1024m -Xmx1024m -XX:MaxPermSize=512m` (1 occurence)

check/update `wildfly\bin\service.bat` settings for calls to `run.bat` to
include the options `-c default -b 0.0.0.0` (2 occurrences)

e.g. call `run.bat -c default -b 0.0.0.0 &lt; .r.lock &gt;&gt; run.log
2&gt;&1`

as administrator, install service (from command prompt, in `wildfly\bin\` issue `service install`

set service to run as `XXX\svc.wildfly user` (from service msc, change user
and startup type - don't check name as it will try to resolve as
svc.wildfly@NET.XXX)

--KERBEROS

copy krb5.conf to `wildfly\bin`

copy spnego-r7.jar to `wildfly\server\default\lib\`

add clauses from `web.xml\_amendment.txt`
(`wildfly\..deploy\jbossweb.sar\web.xml` or
`wildfly\..\deploy\jbossweb-tomcat55.sar\conf\web.xml`)

add clauses from `login-config.xml\_amendment.txt`
(`wildfly\..\default\conf\login-config.xml`)

copy `hello\_spnego.jsp wildfly\..\root.war`

check HTTP SPNs added to both svc.wildfly user and machine

--DEPLOYMENT

Stop Wildfly service

Copy `\\dev\C\$\\_\Example` to `C:\\_\Example`

check `C:\\_\Example\javaee\arc\_ds.xml` connection string

check `C:\\_\Example\content\repository.xml` connection strings (x2)

check `C:\\_\Example\content\workspaces\arc\workspace.xml`
connection string

check `C:\\_\Example\content\workspaces\default\workspace.xml`
connection string

check `C:\\_\Example\apps\repository\customers\arc\arc.xml` icon

Copy `C:\\_\Example\javaee\arc.ear & arc\_ds.xml` to
`C:\wildfly\server\default\deploy\`

Start JBoss service

--CHECKS

check `http://&lt;server&gt;.net.arc:8080/hello\_spnego.jsp`

if "Failure unspecified at GSS-API level (Mechanism level: Checksum
failed)- check SPNs

check `http://&lt;server&gt;.net.arc:8080/arc/init.biz`

if error - check that you have an account in XXX, check
`C:\wildfly\standalone\log\server.log` to see your user
principal is being recognised

## Example Deployment Problems caused by problems in the .json file
Key problems in the `myApplication.json` configuration file block your project from deploying successfully and sometime yield non-obvious errors or stack output. The following provides three common examples.

### Example Output for incorrect Content folder
Incorrect content folder - the folder doesn't exist:

```json
	// Content settings
	content: {
		// directory path
		directory: "C:/skyve/content/", 
		// CRON Expression for CMS Garbage Collection job - run at 7 past the hour every hour
		gcCron: "0 7 0/1 1/1 * ? *", 
		// Attachments stored on file system or inline
		fileStorage: true
	},
```

In this case, the folder C:/skyve/content/ doesn't exist or the name is incorrect.

Attempting to deploy in this case yields results such as the following:
```
15:51:09,837 ERROR [org.jboss.msc.service.fail] (ServerService Thread Pool -- 61) MSC000001: Failed to start service jboss.undertow.deployment.default-server.default-host./phweb: org.jboss.msc.service.StartException in service jboss.undertow.deployment.default-server.default-host./phweb: java.lang.RuntimeException: java.lang.IllegalStateException: content.directory C:/skyve/content/ does not exist.
	at org.wildfly.extension.undertow.deployment.UndertowDeploymentService$1.run(UndertowDeploymentService.java:85)
	at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:511)
	at java.util.concurrent.FutureTask.run(FutureTask.java:266)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at java.lang.Thread.run(Thread.java:748)
	at org.jboss.threads.JBossThread.run(JBossThread.java:320)
Caused by: java.lang.RuntimeException: java.lang.IllegalStateException: *content.directory C:/skyve/content/ does not exist.*
	at io.undertow.servlet.core.DeploymentManagerImpl.deploy(DeploymentManagerImpl.java:236)
	at org.wildfly.extension.undertow.deployment.UndertowDeploymentService.startContext(UndertowDeploymentService.java:100)
	at org.wildfly.extension.undertow.deployment.UndertowDeploymentService$1.run(UndertowDeploymentService.java:82)
	... 6 more
Caused by: java.lang.IllegalStateException: content.directory C:/skyve/content/ does not exist.
	at org.skyve.impl.web.SkyveContextListener.contextInitialized(SkyveContextListener.java:102)
	at io.undertow.servlet.core.ApplicationListeners.contextInitialized(ApplicationListeners.java:187)
	at io.undertow.servlet.core.DeploymentManagerImpl$1.call(DeploymentManagerImpl.java:200)
	at io.undertow.servlet.core.DeploymentManagerImpl$1.call(DeploymentManagerImpl.java:171)
	at io.undertow.servlet.core.ServletRequestContextThreadSetupAction$1.call(ServletRequestContextThreadSetupAction.java:42)
	at io.undertow.servlet.core.ContextClassLoaderSetupAction$1.call(ContextClassLoaderSetupAction.java:43)
	at io.undertow.servlet.api.LegacyThreadSetupActionWrapper$1.call(LegacyThreadSetupActionWrapper.java:44)
	at io.undertow.servlet.api.LegacyThreadSetupActionWrapper$1.call(LegacyThreadSetupActionWrapper.java:44)
	at io.undertow.servlet.api.LegacyThreadSetupActionWrapper$1.call(LegacyThreadSetupActionWrapper.java:44)
	at io.undertow.servlet.api.LegacyThreadSetupActionWrapper$1.call(LegacyThreadSetupActionWrapper.java:44)
	at io.undertow.servlet.api.LegacyThreadSetupActionWrapper$1.call(LegacyThreadSetupActionWrapper.java:44)
	at io.undertow.servlet.core.DeploymentManagerImpl.deploy(DeploymentManagerImpl.java:234)
	... 8 more
```

### Example incorrect/invalid customer in bootstrap stanza
Incorrect customer in the bootstrap- there is no such customer defined:

```json
// bootstrap user settings - creates a user with all customer roles assigned, if the user does not already exist
bootstrap: {
	customer: "skyve",
	user: "admin",
	password: "admin"
}
```

In this case, there is no _skyve_ customer declaration file within the _customer_ folder.

Attempting to deploy in this case yields results such as the following:

```
15:48:03,814 ERROR [stderr] (ServerService Thread Pool -- 68) org.skyve.metadata.MetaDataException: A problem was encountered.
15:48:03,814 ERROR [stderr] (ServerService Thread Pool -- 68) 	at org.skyve.impl.metadata.repository.LocalDesignRepository.getCustomer(LocalDesignRepository.java:174)
15:48:03,814 ERROR [stderr] (ServerService Thread Pool -- 68) 	at org.skyve.impl.metadata.user.UserImpl.getCustomer(UserImpl.java:198)
15:48:03,814 ERROR [stderr] (ServerService Thread Pool -- 68) 	at org.skyve.impl.metadata.user.SuperUser.getAccessibleModuleNames(SuperUser.java:85)
15:48:03,814 ERROR [stderr] (ServerService Thread Pool -- 68) 	at org.skyve.impl.persistence.hibernate.AbstractHibernatePersistence.resetDocumentPermissionScopes(AbstractHibernatePersistence.java:528)
15:48:03,814 ERROR [stderr] (ServerService Thread Pool -- 68) 	at org.skyve.impl.persistence.hibernate.AbstractHibernatePersistence.setUser(AbstractHibernatePersistence.java:500)
15:48:03,814 ERROR [stderr] (ServerService Thread Pool -- 68) 	at org.skyve.impl.web.SkyveContextListener.contextInitialized(SkyveContextListener.java:276)
15:48:03,814 ERROR [stderr] (ServerService Thread Pool -- 68) 	at io.undertow.servlet.core.ApplicationListeners.contextInitialized(ApplicationListeners.java:187)
15:48:03,814 ERROR [stderr] (ServerService Thread Pool -- 68) 	at io.undertow.servlet.core.DeploymentManagerImpl$1.call(DeploymentManagerImpl.java:200)
15:48:03,815 ERROR [stderr] (ServerService Thread Pool -- 68) 	at io.undertow.servlet.core.DeploymentManagerImpl$1.call(DeploymentManagerImpl.java:171)
15:48:03,815 ERROR [stderr] (ServerService Thread Pool -- 68) 	at io.undertow.servlet.core.ServletRequestContextThreadSetupAction$1.call(ServletRequestContextThreadSetupAction.java:42)
15:48:03,815 ERROR [stderr] (ServerService Thread Pool -- 68) 	at io.undertow.servlet.core.ContextClassLoaderSetupAction$1.call(ContextClassLoaderSetupAction.java:43)
15:48:03,815 ERROR [stderr] (ServerService Thread Pool -- 68) 	at io.undertow.servlet.api.LegacyThreadSetupActionWrapper$1.call(LegacyThreadSetupActionWrapper.java:44)
15:48:03,815 ERROR [stderr] (ServerService Thread Pool -- 68) 	at io.undertow.servlet.api.LegacyThreadSetupActionWrapper$1.call(LegacyThreadSetupActionWrapper.java:44)
15:48:03,815 ERROR [stderr] (ServerService Thread Pool -- 68) 	at io.undertow.servlet.api.LegacyThreadSetupActionWrapper$1.call(LegacyThreadSetupActionWrapper.java:44)
15:48:03,815 ERROR [stderr] (ServerService Thread Pool -- 68) 	at io.undertow.servlet.api.LegacyThreadSetupActionWrapper$1.call(LegacyThreadSetupActionWrapper.java:44)
15:48:03,815 ERROR [stderr] (ServerService Thread Pool -- 68) 	at io.undertow.servlet.api.LegacyThreadSetupActionWrapper$1.call(LegacyThreadSetupActionWrapper.java:44)
15:48:03,815 ERROR [stderr] (ServerService Thread Pool -- 68) 	at io.undertow.servlet.core.DeploymentManagerImpl.deploy(DeploymentManagerImpl.java:234)
15:48:03,815 ERROR [stderr] (ServerService Thread Pool -- 68) 	at org.wildfly.extension.undertow.deployment.UndertowDeploymentService.startContext(UndertowDeploymentService.java:100)
15:48:03,816 ERROR [stderr] (ServerService Thread Pool -- 68) 	at org.wildfly.extension.undertow.deployment.UndertowDeploymentService$1.run(UndertowDeploymentService.java:82)
15:48:03,816 ERROR [stderr] (ServerService Thread Pool -- 68) 	at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:511)
15:48:03,816 ERROR [stderr] (ServerService Thread Pool -- 68) 	at java.util.concurrent.FutureTask.run(FutureTask.java:266)
15:48:03,816 ERROR [stderr] (ServerService Thread Pool -- 68) 	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
15:48:03,816 ERROR [stderr] (ServerService Thread Pool -- 68) 	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
15:48:03,816 ERROR [stderr] (ServerService Thread Pool -- 68) 	at java.lang.Thread.run(Thread.java:748)
15:48:03,816 ERROR [stderr] (ServerService Thread Pool -- 68) 	at org.jboss.threads.JBossThread.run(JBossThread.java:320)
15:48:03,816 ERROR [stderr] (ServerService Thread Pool -- 68) Caused by: org.skyve.metadata.MetaDataException: Could not unmarshal customer at /C:/_/pgibsa/Phylloxera/javaee/pgibsa.ear/apps.jar/customers/skyve/skyve.xml
15:48:03,816 ERROR [stderr] (ServerService Thread Pool -- 68) 	at org.skyve.impl.util.XMLMetaData.unmarshalCustomer(XMLMetaData.java:185)
15:48:03,816 ERROR [stderr] (ServerService Thread Pool -- 68) 	at org.skyve.impl.metadata.repository.LocalDesignRepository.getCustomer(LocalDesignRepository.java:164)
15:48:03,817 ERROR [stderr] (ServerService Thread Pool -- 68) 	... 24 more
15:48:03,817 ERROR [stderr] (ServerService Thread Pool -- 68) Caused by: java.io.FileNotFoundException: C:\_\pgibsa\Phylloxera\javaee\pgibsa.ear\apps.jar\customers\skyve\skyve.xml (The system cannot find the path specified)
15:48:03,817 ERROR [stderr] (ServerService Thread Pool -- 68) 	at java.io.FileInputStream.open0(Native Method)
15:48:03,817 ERROR [stderr] (ServerService Thread Pool -- 68) 	at java.io.FileInputStream.open(FileInputStream.java:195)
15:48:03,817 ERROR [stderr] (ServerService Thread Pool -- 68) 	at java.io.FileInputStream.<init>(FileInputStream.java:138)
15:48:03,817 ERROR [stderr] (ServerService Thread Pool -- 68) 	at java.io.FileInputStream.<init>(FileInputStream.java:93)
15:48:03,817 ERROR [stderr] (ServerService Thread Pool -- 68) 	at org.skyve.impl.util.XMLMetaData.unmarshalCustomer(XMLMetaData.java:173)
15:48:03,817 ERROR [stderr] (ServerService Thread Pool -- 68) 	... 25 more
```

### Missing comma or badly formed .json file

Missing comma or badly formed .json file:

```json
	// bootstrap user settings - creates a user with all customer roles assigned, if the user does not already exist
	bootstrap: {
		customer: "skyve",
		user: "admin",
		password: "admin"
	} 
	// When taking photos or uploading images they will be compressed to within the size below (if possible)
	maxUploadedFileSizeInBytes: 1000000 // 10 MB
}
```

For example, should have been a comma after the bootstrap stanza.

Attempting to deploy in this case yields results such as the following:

```
15:40:16,947 ERROR [org.jboss.msc.service.fail] (ServerService Thread Pool -- 69) MSC000001: Failed to start service jboss.undertow.deployment.default-server.default-host./phweb: org.jboss.msc.service.StartException in service jboss.undertow.deployment.default-server.default-host./phweb: java.lang.RuntimeException: java.lang.ClassCastException: java.lang.Long cannot be cast to java.util.Map
	at org.wildfly.extension.undertow.deployment.UndertowDeploymentService$1.run(UndertowDeploymentService.java:85)
	at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:511)
	at java.util.concurrent.FutureTask.run(FutureTask.java:266)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at java.lang.Thread.run(Thread.java:748)
	at org.jboss.threads.JBossThread.run(JBossThread.java:320)
Caused by: java.lang.RuntimeException: java.lang.ClassCastException: java.lang.Long cannot be cast to java.util.Map
	at io.undertow.servlet.core.DeploymentManagerImpl.deploy(DeploymentManagerImpl.java:236)
	at org.wildfly.extension.undertow.deployment.UndertowDeploymentService.startContext(UndertowDeploymentService.java:100)
	at org.wildfly.extension.undertow.deployment.UndertowDeploymentService$1.run(UndertowDeploymentService.java:82)
	... 6 more
Caused by: java.lang.ClassCastException: java.lang.Long cannot be cast to java.util.Map
	at org.skyve.impl.web.SkyveContextListener.getObject(SkyveContextListener.java:378)
	at org.skyve.impl.web.SkyveContextListener.contextInitialized(SkyveContextListener.java:253)
	at io.undertow.servlet.core.ApplicationListeners.contextInitialized(ApplicationListeners.java:187)
	at io.undertow.servlet.core.DeploymentManagerImpl$1.call(DeploymentManagerImpl.java:200)
	at io.undertow.servlet.core.DeploymentManagerImpl$1.call(DeploymentManagerImpl.java:171)
	at io.undertow.servlet.core.ServletRequestContextThreadSetupAction$1.call(ServletRequestContextThreadSetupAction.java:42)
	at io.undertow.servlet.core.ContextClassLoaderSetupAction$1.call(ContextClassLoaderSetupAction.java:43)
	at io.undertow.servlet.api.LegacyThreadSetupActionWrapper$1.call(LegacyThreadSetupActionWrapper.java:44)
	at io.undertow.servlet.api.LegacyThreadSetupActionWrapper$1.call(LegacyThreadSetupActionWrapper.java:44)
	at io.undertow.servlet.api.LegacyThreadSetupActionWrapper$1.call(LegacyThreadSetupActionWrapper.java:44)
	at io.undertow.servlet.api.LegacyThreadSetupActionWrapper$1.call(LegacyThreadSetupActionWrapper.java:44)
	at io.undertow.servlet.api.LegacyThreadSetupActionWrapper$1.call(LegacyThreadSetupActionWrapper.java:44)
	at io.undertow.servlet.core.DeploymentManagerImpl.deploy(DeploymentManagerImpl.java:234)
	... 8 more
```

## Installing Skyve in Production

The following are our personal instructions for deploying a Skyve application in a production environment. You may need to tweak these to suit your personal situation, and feel free to submit a pull request to update these instructions if you find something better or they become out of date.

### Wildfly Standalone Production Install (Windows)

These instructions apply to a standalone server installation of Wildfly 10 on Windows server connecting to Microsoft SQL Server.

- [download](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) and install the latest JDK 
- create a SYSTEM "JAVA_HOME" system environment variable and set it to where you installed the JDK to (`C:\Program Files\Java\jdk1.8.0_45` by default, substitute with correct Java version path)
- [download](http://wildfly.org/downloads/) Wildfly 10.1 Final 
  - extract to C:\wildfly
- [download](https://docs.microsoft.com/en-us/sql/connect/jdbc/microsoft-jdbc-driver-for-sql-server?view=sql-server-2017) and copy the sql server driver jar and module.xml (below) to `C:\wildfly\modules\system\layers\base\com\microsoft\sqlserver\main`

`module.xml` (modify the `resource-root` path to match your sql server jdbc jar name)

```xml
<?xml version="1.0" encoding="utf-8"?> 
<module xmlns="urn:jboss:module:1.3" name="com.microsoft.sqlserver"> 
  <resources> 
    <resource-root path="sqljdbc41.jar"/> 
  </resources> 
  <dependencies> 
    <module name="javax.api"/> 
    <module name="javax.transaction.api"/> 
  </dependencies> 
</module>
```

- edit `standalone.xml`
- if using integrated security to connect to the database:
  - install the sqljdbc_auth.dll driver into `C:\windows\system32`
  - Note: If you don't have permissions for the system32 directory, you may instead create a directory anywhere (e.g. `D:\java\libs`) and add it to the System Path. This can be done by Advanced System Settings → Environment Variables → System Variables)
- add the sql server driver

```xml
<driver name="sqlserver" module="com.microsoft.sqlserver">
    <xa-datasource-class>com.microsoft.sqlserver.jdbc.SQLServerDriver</xa-datasource-class>
</driver>
```

- add the security-domain (replace {projectNameDB} with your project name, e.g. exampleDB)

```xml
<security-domain name="skyve" cache-type="default">
    <authentication>
        <login-module code="Database" flag="required">
            <module-option name="dsJndiName" value="java:/{projectNameDB}"/>
            <module-option name="principalsQuery" value="select password from ADM_SecurityUser where bizCustomer + '/' + userName = ?;"/>
            <module-option name="rolesQuery" value="select 'Nobody', 'Roles' from ADM_SecurityUser where bizCustomer + '/' + userName = ?"/>
            <module-option name="hashAlgorithm" value="SHA1"/>
            <module-option name="hashEncoding" value="base64"/>
        </login-module>
    </authentication>
</security-domain>
```

or, if using Active Directory authentication:

```xml
<security-domain name="skyve" cache-type="default">
    <authentication>
        <login-module code="org.jboss.security.auth.spi.LdapLoginModule" flag="requisite">
            <module-option name="java.naming.factory.initial" value="com.sun.jndi.ldap.LdapCtxFactory"/>
            <module-option name="java.naming.provider.url" value="ldaps://{adServerName}:636/"/>
            <module-option name="java.naming.security.authentication" value="simple"/>
            <module-option name="throwValidateError" value="true"/>
            <module-option name="principalDNPrefix" value=""/>
            <module-option name="principalDNSuffix" value="@{adsuffix}"/>
            <module-option name="rolesCtxDN" value="cn={cn},dc={dc}"/>
            <module-option name="uidAttributeID" value="sAMAccountName"/>
            <module-option name="matchOnUserDN" value="false"/>
            <module-option name="roleAttributeID" value="memberOf"/>
            <module-option name="roleAttributeIsDN" value="true"/>
            <module-option name="roleNameAttributeID" value="name"/>
            <module-option name="password-stacking" value="useFirstPass"/>
        </login-module>
        <login-module code="Database" flag="optional">
            <module-option name="dsJndiName" value="java:/{projectNameDB}"/>
            <module-option name="principalsQuery" value="select password from ADM_SecurityUser where bizCustomer + '/' + userName = ?;"/>
            <module-option name="rolesQuery" value="select 'Nobody', 'Roles' from ADM_SecurityUser where userName =  ?"/>
            <module-option name="hashAlgorithm" value="SHA1"/>
            <module-option name="hashEncoding" value="base64"/>
            <module-option name="password-stacking" value="useFirstPass"/>
        </login-module>
    </authentication>
</security-domain>
```
- replace instances of `{adServerName}`, `{adsuffix}`, `{cn}`, `{dc}` and `{projectNameDB}` from the above

- remove the following filter-refs from the default-server definition in the undertow stanza

  ```xml
  <filter-ref name="server-header"/>
  <filter-ref name="x-powered-by-header"/>
  ```
  
- comment out the welcome content `<location name="/" handler="welcome-content"/>`
- replace <jsp-config/> in the default servlet-container config with

  ```xml
  <jsp-config x-powered-by="false"/>
  ```
  
- add Wildfly as a windows service
  - go to the directory `C:\wildfly\docs\contrib\scripts`
  - copy the folder "service"
  - go to the directory `C:\wildfly\bin`
  - paste the service folder there
  - edit `C:\wildfly\bin\service\service.bat`
  - search for: "set DESCRIPTION="
  - remove the double quotes, save and exit
  - open a new command prompt to `C:\wildfly\bin\service\`
  - from a command prompt, run: `service.bat install`
  - verify that the service has been installed, change the startup type to _Automatic (Delayed Start)_
  - set the system account that should be running the wildfly service

 - make Wildfly accessible outside of localhost
   - edit `standalone.xml`
   - find `<interface name="public">` and change to the following
  ```xml
  <interface name="public">
  <inet-address value="${jboss.bind.address:0.0.0.0}"/>
  </interface>
  ```
- to run on port 80 instead of 8080, search for `<socket-binding name="http" port="${jboss.http.port:8080}"/>` and replace with `<socket-binding name="http" port="${jboss.http.port:80}"/>`

- copy the datasource.xml file into wildfly\standalone\deployments
  - configure the connection url, JNDI name, and authentication
- copy the projectName.json into wildfly\standalone\deployments
  - configure the _server name_, _content path_ and _catalog_
  - check if the bootstrap user is required
- create the content directory if it does not exist
- start the wildfly service and make sure the datasources deploy successfully
- deploy projectName.war

#### Troubleshooting
If you receive an error like:

```
Caused by: org.hibernate.engine.jndi.JndiException: Unable to lookup JNDI name [java:jboss/datasources/{projectNameDB}]
    Caused by: javax.naming.NameNotFoundException: datasources/{projectNameDB} -- service jboss.naming.context.java.jboss.datasources.{projectNameDB}\"},
```

then check that your JNDI name in the standalone.xml (`<module-option name="dsJndiName" value="java:/{projectNameDB}"/>`) matches _exactly_ (case sensitive):
- the JNDI name in the JSON
  ```json
  "skyve": {
      // JNDI name
      jndi: "java:/{projectNameDB}",
  ```
- and the JNDI name in the ds.xml
  ```xml
  <datasource jndi-name="java:/{projectNameDB}" pool-name="skyve" enabled="true" jta="true" use-ccm="false">
  ```

### Wildfly Bitnami Production Install (Windows)

These instructions apply to Bitnami Wildfly 10 stack installation on Windows server modified to connect to Microsoft SQL Server.
	
* Download the latest Bitnami Wildfly Windows image (https://bitnami.com/stack/wildfly)
* Ideally install Bitnami to a location on a non-OS partition
* Rename serviceinstall.bat to _DO_NOT_RUN_serviceinstall.bat (rename this back if you are going to uninstall Bitnami)
* If not using MySQL, go to services, disable the MySql service (named `wildflyMySQL`)
* Install the SqlServer module into Wildfly:
  * Edit `standalone.xml` in Bitnami\wildfly-10.1.0-1\wildfly\standalone\configuration
  * Find the Drivers section in the document and add the following:
  ```xml
  <driver name="sqlserver" module="com.microsoft.sqlserver">
      <xa-datasource-class>com.microsoft.sqlserver.jdbc.SQLServerDriver</xa-datasource-class>
  </driver>
  ```
* Copy the files into Bitnami\wildfly-10.1.0-1\wildfly\modules\system\layers\base\com\microsoft\sqlserver\main
  * `module.xml` (below)
  * [download](https://docs.microsoft.com/en-us/sql/connect/jdbc/microsoft-jdbc-driver-for-sql-server?view=sql-server-2017) the latest SQL Server JDBC driver and copy the jbbc jar

`module.xml` (modify the `resource-root` path to match your sql server jdbc jar name)

```xml
<?xml version="1.0" encoding="utf-8"?> 
<module xmlns="urn:jboss:module:1.3" name="com.microsoft.sqlserver"> 
  <resources> 
    <resource-root path="sqljdbc41.jar"/> 
  </resources> 
  <dependencies> 
    <module name="javax.api"/> 
    <module name="javax.transaction.api"/> 
  </dependencies> 
</module>
```

* If using Integrated Security to authenticate with the database:
  * Install the `sqljdbc_auth.dll` driver into C:\windows\system32
  * Note: If you don't have permissions for the system32 directory, you may instead create a directory anywhere (e.g. D:\java\libs) and add it to the System Path. This can be done by Advanced System Settings → Environment Variables → System Variables)
* Stop the Wildfly service if it is running
* Add driver config to standalone.xml
* Add security domain to standalone.xml

```xml
<security-domain name="skyve" cache-type="default">
    <authentication>
        <login-module code="Database" flag="required">
            <module-option name="dsJndiName" value="java:/{projectNameDB}"/>
            <module-option name="principalsQuery" value="select password from ADM_SecurityUser where bizCustomer + '/' + userName = ?;"/>
            <module-option name="rolesQuery" value="select 'Nobody', 'Roles' from ADM_SecurityUser where bizCustomer + '/' + userName = ?"/>
            <module-option name="hashAlgorithm" value="MD5"/>
            <module-option name="hashEncoding" value="base64"/>
        </login-module>
    </authentication>
</security-domain>
```

* rename folder `Bitnami\wildfly-10.1.0-1\java` to `Bitnami\wildfly-10.1.0-1\javaold`
* Download latest Java 8 64 runtime but select to change destination folder during install to `Bitnami\wildfly-10.1.0-1\java`
* Add java_home env variable and append `%JAVA_HOME%\bin` to path env variable eg `JAVA_HOME=D:\Bitnami\wildfly-10.1.0-1\java`, `Path=%JAVA_HOME%\bin`
* Add env variable:
  * name: `JAVA_OPTS`
  * value: `-Xms4G -Xmx4G -XX:MetaspaceSize=96M -XX:MaxMetaspaceSize=2G`
* Ensure the following...
  * `ErrorDocument 503 /static/index.html` is in Bitnami\wildfly-10.1.0-1\apache2\conf\bitnami\bitnami.conf
* Copy static.zip to `Bitnami\wildfly-10.1.0-1\apache2\htdocs\`
* Copy static.zip to `Bitnami\wildfly-10.1.0-1\wildfly\welcome-content\`
* Change Bitnami\wildfly-10.1.0-1\wildfly\conf\wildfly.conf that contains
  ```xml
  <Location />
  ProxyPass http://localhost:8080/
  ProxyPassReverse http://localhost:8080/
  </Location>
  ```

to contain the following

```
ProxyPass /static !
ProxyPass / http://localhost:8080/ timeout=600 Keepalive=On
ProxyPassReverse / http://localhost:8080/
```
(literally replace the whole lot exactly as shown - so that there are no <Location/> tags)

* Change the path of the `welcome-content` path attribute in the file handler to point to `welcome-content\static\` in `Bitnami\wildfly-10.1.0-1\wildfly\standalone\configuration\standalone.xml` like so
  ```xml
  <file name="welcome-content" path="${jboss.home.dir}/welcome-content/static/"/>
  ```
* Change `standalone.xml`
  * Remove the following filter-refs from the default-server definition in the undertow stanza

    ```xml
    <filter-ref name="server-header"/>
    <filter-ref name="x-powered-by-header"/>
    ```
  * Replace ```<jsp-config/>``` in the default servlet container config with 
    ```xml
    <jsp-config x-powered-by="false"/>
    ```
* Copy the datasource xml files into wildfly\standalone\deployments
* Copy the `{projectName}.json` into wildfly\standalone\deployments and configure the server name and the content path
* Set the system account that should be running the wildfly service
* Start the wildfly service and make sure the datasources deploy successfully
* Deploy `{projectName}.war`

**[⬆ back to top](#contents)**

---
**Previous [Automated Unit Testing](./../_pages/automated-unit-testing.md)**
