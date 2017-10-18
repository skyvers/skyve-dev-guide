# Appendix

### Contents

  * [Chapter 1: Architectural Overview](../README.md)
  * [Chapter 2: Concepts](./../chapters/concepts.md)
  * [Chapter 3: Identifying the Skyve Version](./../chapters/concepts.md)
  * [Chapter 4: Security, Persistence and Access control](./../chapters/security-persistence-and-access-control.md)
  * [Chapter 5: Exception Handling](./../chapters/exception-handling.md)
  * [Chapter 6: Customers](./../chapters/customers.md)
  * [Chapter 7: Modules](./../chapters/modules.md)
  * [Chapter 8: Documents](./../chapters/documents.md)
  * [Chapter 9: Converters](./../chapters/converters.md)
  * [Chapter 10: Bizlets](./../chapters/bizlets.md)
  * [Chapter 11: Views](./../chapters/views.md)
  * [Chapter 12: Actions](./../chapters/actions.md)
  * [Chapter 13: Reports](./../chapters/reports.md)
  * [Chapter 14: Jobs](./../chapters/jobs.md)
  * [Chapter 15: Utility Classes](./../chapters/utility-classes.md)
  * [Chapter 16: Common Patterns](./../chapters/common-patterns.md)
  * [Chapter 17: Skyve Persistence Mechanisms](./../chapters/skyve-persistence-mechanisms.md)
  * [Chapter 18: Ant Utilities](./../chapters/ant-utilities.md)
  * [Chapter 19: Content Repository Tools](./../chapters/content-repository-tools.md)
  * [Chapter 20: Bizport](./../chapters/bizport.md)
  * [Chapter 21: WILDCAT Conversion Tool](./../chapters/wildcat-conversion-tool.md)
  * [Chapter 22: Automated Unit Testing](./../chapters/automated-unit-testing.md)
* **[Section 5: Appendix](#appendix)**
  * [Appendix 1: Deploying a Skyve Application](#deploying-a-skyve-application)
  * [Appendix 2: Installing and configuring the Skyve Development Environment](#installing-and-configuring-the-skyve-development-environment)
    * [Prerequisites checklist](#prerequisites-checklist)
    * [Configuring Java](#configuring-java)
    * [Configuring the IDE (Windows example)](#configuring-the-ide-windows-example)      
      * [Importing Projects](#importing-projects)
      * [Configuring Wildfly](#configuring-wildfly)
      * [Starting the server](#starting-the-server)      
  * [Appendix 3: Example Deployment Instructions with Single Sign-on](#example-deployment-instructions-with-single-sign-on)

## Deploying a Skyve Application

Skyve applications are deployed in two parts, the application metadata
and the Skyve enterprise archive.

Application metadata is deployed by copying the metadata Apps package to
the destination location. The Enterprise Archive is deployed by copying
the `.ear` package to the application server deployment area.

For example, to deploy a Skyve application to a Windows server:

- Install Wildfly 8 or later,
- Copy the application metadata package to `C:\_\`,
- Copy the Skyve `.ear` package and the `*ds.xml` file to `<wildfly>\standalone\deployments\`,
- Update the `*ds.xml` with a valid connection string,
- Update the `<wildfly>\standalone\configuration\standalone.xml` with a
valid JDBC connection string, (usually the same connection string in the
\*ds.xml file), and
- Touch the server

See [Configuring Wildfly](#configuring-wildfly) for more detailed Wildfly setup 
instructions. Additional steps may be required for single sign-on configuration, 
and the creation of service user accounts, SPNs and port configuration as required.

**[⬆ back to top](#contents)**

## Installing and configuring the Skyve Development Environment

These instructions describe the process required to install and configure the 
development environment for Skyve. These instructions assume that you are 
using Windows and SQL Server. Some changes will need to be made if using a 
different operating system or database.

### Prerequisites checklist

Before you begin, ensure you have the following:

* Java ([www.oracle.com](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)), at least JDK 1.7
* Eclipse IDE for Java EE developers ([www.eclipse.org](https://www.eclipse.org/downloads/)), so that the installation
is in C:\\eclipse\\
* Wildfly 10 (select the last final version available) ([http://wildfly.org](http://wildfly.org/downloads/))
* A RDBMS which is supported by Hibernate ([www.hibernate.org](http://www.hibernate.org)) – ensure you record the
  administrator username and password. For this example, we are going to use MS SQL Server as the database for the Skyve project.
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
* On the same dialog box, choose Default database as skyve, now go to Server Roles 
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

* Create C:\\\_\\ (just go to C:\\->right click->New->Folder then type "_" as the 
folder name). You may use any folder for you workspace in Eclipse, just do remember 
the folder you've chosen and try to ensure that the folder name has no spaces to avoid 
any issues with Java paths and spaces.
* Start Eclipse uisng Eclipse.exe and select C:\\\_\\ as workspace, tick 'Use as 
default option - do not ask again', Cancel the welcome wizard and Close the welcome 
tab in the editor frame.
* Change compiler compliance level to 1.8 (Window -> Preferences -> Java -> Compiler) 
press ‘Apply’ - press Yes for full build, and then press OK.
* To manage the Wildfly application server from Eclipse:
  * Open the server explorer window if it is not already in your workspace (Window -> Show View -> Servers)
  * Right click inside the server explorer and select New
  * Expand JBoss Community
  * If WildFly 10.x is not in the list of adapters, you will need to download them:
    * Choose JBoss, WildFly & EAP Server Tools and click Next
    * Accept the licence terms and click Finish
    * Restart Eclipse when prompted
  * Select WildFly 10.x and click _Next_
  * Accept the defaults and click _Next_
  * Click _Finish_

#### Importing Projects

In Eclipse, choose File->Import.. ->Git->Projects from Git->Next->Clone URI and 
type in https://github.com/skyvers/skyve.git as URI, then click the _Next_ button, 
choose the master and click the _Next_ button. Choose your destination directory, 
in this example, we have chosen C:\\\_\\ directory. Then click the _Next_ button. 
The import wizard should be displayed and cloning the Skyve project.

After cloning the master, go to Project -> Clean - Select clean all projects and press OK - wait for activity to cease in bottom right corner of the eclipse window.

#### Configuring Wildfly

* Locate the 'java-ee' project->javaee->`skyve-ds.xml` file and delete that file. 
  It is no longer needed as we will be entering SQL Server later in Wildfly 
  standalone.xml.
* Download the SQL server JDBC sqljdbc4-3.0.jar file from here - 
  http://www.java2s.com/Code/Jar/s/Downloadsqljdbc430jar.htm
* From your wildfly installation folder, create the 
  `{wildfly directory}\modules\system\layers\base\com\microsoft\sqlserver\main\` 
  folder structure and copy the JDBC jar inside the `main` folder, and then 
  create a new XML file in the same location named `module.xml` and with the 
  following contents:

```xml
<?xml version="1.0" encoding="utf-8"?>
<module xmlns="urn:jboss:module:1.3" name="com.microsoft.sqlserver">
  <resources>
    <resource-root path="sqljdbc4-3.0.jar"/>
  </resources>
  <dependencies>
    <module name="javax.api"/>
    <module name="javax.transaction.api"/>
    <module name="javax.xml.bind.api"/>
  </dependencies>
</module>
```
* In Eclipse, go to "Server" view, then Filesets->Configuration File folder->open 
  up `standalone.xml` file then click the 'Source' tab and find the 
  `<subsystem xmlns="urn:jboss:domain:datasources:4.0">` and delete that whole 
  entry of that line. Also find and delete the line:
```xml
<default-bindings context-service="java:jboss/ee/concurrency/context/default" datasource="java:jboss/datasources/ExampleDS" managed-executor-service="java:jboss/ee/concurrency/executor/default" managed-scheduled-executor-service="java:jboss/ee/concurrency/scheduler/default" managed-thread-factory="java:jboss/ee/concurrency/factory/default"/>
```
* These are all default Wildfly DB setup which you won’t need when using 
  MS SQL Server. Whilst on this file, you need to add other some entries for 
  Skyve security domain, so locate the `<security-domains...>` and add this 
  entry within the body of `<security-domains...>` :

```xml
<security-domain name="skyve" cache-type="default">
  <authentication>
    <login-module code="Database" flag="required">
      <module-option name="dsJndiName" value="java:/DefaultDB"/>
      <module-option name="principalsQuery" value="select password from ADM_SecurityUser where bizCustomer || '/' || userName = ?"/>
      <module-option name="rolesQuery" value="select 'Nobody', 'Roles' from ADM_SecurityUser where bizCustomer || '/' || userName = ?"/>
      <module-option name="hashAlgorithm" value="MD5"/>
      <module-option name="hashEncoding" value="base64"/>
    </login-module>
  </authentication>
</security-domain>
```
* Find the `<deployment-scanner..>` and underneath the `<deployment-scanner path="deployments" relative-to="jboss.server.base.dir"..>` add the following entry (remember where you have cloned the Skyve project from Git, in this example, we have cloned it in C:\\\_\\ directory).

```xml
<deployment-scanner name="skyve" path="/C:/_/skyve/skyve-ee/javaee" scan-interval="5000" runtime-failure-causes-rollback="${jboss.deployment.scanner.rollback.on.failure:false}"/>
```

* Now find the ```<subsystem xmlns="urn:jboss:domain:ee:4.0">``` then add the following inside the body of that entry.

```xml
<global-modules>
  <module name="com.microsoft.sqlserver" slot="main"/>
</global-modules>
```

* Locate the `<profile>` and enter these SQL Server settings inside the `<profile>`.
```xml
<subsystem xmlns="urn:jboss:domain:datasources:4.0">
  <datasources>
    <datasource jndi-name="java:/DefaultDS" pool-name="DefaultDS" enabled="true" use-java-context="true">
      <connection-url>jdbc:sqlserver://localhost:1433;databasename=skyve</connection-url>
      <driver>sqlserver</driver>
      <security>
        <user-name>[your SQL Server user]</user-name>
        <password>[your SQL Server password]</password>
      </security>
    </datasource>
    <drivers>
      <driver name="sqlserver" module="com.microsoft.sqlserver">
        <xa-datasource-class>com.microsoft.sqlserver.jdbc.SQLServerXADataSource</xa-datasource-class>
      </driver>
    </drivers>
  </datasources>
</subsystem>
```
* Open `skyve/skyve-ee/javaee/skyve.json`, the find the following settings entry.

```javascript
dataStores: {
  // Skyve data store
  "skyve": {
    // JNDI name
    jndi: "java:/H2Demo", 
    // Dialect
    dialect: "org.skyve.impl.persistence.hibernate.dialect.H2SpatialDialect"
  }
},
```

and replace it with the following:

```javascript
dataStores: {
  // Skyve data store
  "skyve": {
    // JNDI name
    jndi: "java:/H2Demo", 
    // Dialect
    dialect: "org.skyve.impl.persistence.hibernate.dialect.SQLServer2008SpatialDialect"
  }
},
```

For more information, refer to the Wildfly documentation.

#### Starting the server

* Right click on the **skyve** project, and select Run As->Maven install. 
* Expand the **skyve-ee** project and find the pom.xml
* Right click on that file and select Run As->Maven install. After all the 
  dependencies have been downloaded, refresh ALL projects.
* Open the Ant view. Windows->Show View->Other... then select Ant.
* From here, drag the skyve/ skyve-ee/skyve-build.xml and the 
  skyve/skyve-ee/build.xml to the Ant view and expand them.
* In Ant view, skyve/skyve-ee/skyve-build.xml, double click "generateDomain[defult]"
* Refresh all the projects (highlight all the skyve projects and press F5)
* In skyve/skyve-ee/skyve-build.xml, double click "build [default]"
* You may now deploy the project, so in Ant view, skyve/skyve-ee/build.xml, double 
  click "touch" then start the Wildfly server.
* Whilst the server is starting and application is being deployed, it will create 
  various tables within the 'skyve' database you've created earlier. Once, the 
  application and the server have started successfully, open you SQL Server 
  Management Studio and run this SQL script:

```sql
USE skyve;
GO

INSERT INTO adm_contact (bizId,bizVersion,bizLock,bizCustomer,bizUserId,bizKey,name,mobile,email1,contactType) VALUES
('3b596402-553b-4046-8660-c4c0b47e58ec',72,'20080114123937714mike','demo','781e8526-0795-49a9-926b-de40b8c4fb9e','Mike Sands','Mike Sands',NULL,'mike.sands@bizhub.com.au','Person');

INSERT INTO adm_securitygroup (bizId,bizVersion,bizLock,bizCustomer,bizUserId,bizKey, name,description) VALUES
('397f731c-6b7c-40f9-bf35-142d4d30d55b',200,'20080114123937714mike','demo','781e8526-0795-49a9-926b-de40b8c4fb9e','Everything','Everything','The kitchen sink');

INSERT INTO adm_securitygrouprole (bizId,bizVersion,bizLock,bizCustomer,bizUserId,bizKey, roleName,parent_id) VALUES
('4d3e4e15-fd50-48ca-ac00-72d1d6e44430',69,'20080114123937714mike','demo','781e8526-0795-49a9-926b-de40b8c4fb9e','admin.BasicUser','admin.BasicUser','397f731c-6b7c-40f9-bf35-142d4d30d55b'),
('6686f21b-f9e6-4d94-9cf9-ca22dd4a731f',69,'20080114123937714mike','demo','781e8526-0795-49a9-926b-de40b8c4fb9e','admin.ContactViewer','admin.ContactViewer','397f731c-6b7c-40f9-bf35-142d4d30d55b'),
('a9af46cd-292c-4810-ab23-db3344e81d41',69,'20080114123937714mike','demo','781e8526-0795-49a9-926b-de40b8c4fb9e','admin.StyleMaintainer','admin.StyleMaintainer','397f731c-6b7c-40f9-bf35-142d4d30d55b'),
('be6c3124-1774-4925-90fc-0a1cdba1c52c',69,'20080114123937714mike','demo','781e8526-0795-49a9-926b-de40b8c4fb9e','admin.SecurityAdministrator','admin.SecurityAdministrator','397f731c-6b7c-40f9-bf35-142d4d30d55b');

INSERT INTO adm_securityuser (bizId,bizVersion,bizLock,bizCustomer,bizUserId,bizKey, userName,password,contact_id) VALUES
('781e8526-0795-49a9-926b-de40b8c4fb9e',57,'20080114123937698mike','demo','781e8526-0795-49a9-926b-de40b8c4fb9e','mike','mike','GBJue9P4Sz8+TfCU3vW33g==','3b596402-553b-4046-8660-c4c0b47e58ec');

INSERT INTO adm_securityuser_groups (owner_id,element_id) VALUES
('781e8526-0795-49a9-926b-de40b8c4fb9e','397f731c-6b7c-40f9-bf35-142d4d30d55b');
```
* You may now login using the following URL http://localhost:8080/skyve
* The credentials are as follows:
Customer : demo, Username : mike , Password : mike

## Example Deployment Instructions with Single Sign-on

The following steps are to install an instance of XXX onto a vanilla
Windows 7.

--JAVA

Install Java - jdk-6u25-windows-i586

Set JAVA\_HOME to location of the Java root directory - e.g.
C:\\Java\\jdk1.6.0\_25

Set Path to include Java\\bin - e.g. C:\\Java\\jdk1.6.0\_25\\bin

copy sqljdbc4.jar and sqljdbc\_auth.dll to java\\jdk\\jre\\lib\\ext

--JBOSS

Install jboss (unzip and copy to c:\\)

either: jboss-4.0.5.GA.zip or jboss-as-distribution-6.0.0.Final.zip

check/update jboss\\bin\\run.conf settings for memory -
JAVA\_OPTS="-Xms1024m -Xmx1024m -XX:MaxPermSize=512m (1 occurence)

check/update jboss\\bin\\service.bat settings for calls to run.bat to
include the options -c default -b 0.0.0.0 (2 occurrences)

e.g. call run.bat -c default -b 0.0.0.0 &lt; .r.lock &gt;&gt; run.log
2&gt;&1

as administrator, install service (from command prompt, in jboss\\bin\\
"service install"

set service to run as XXX\\svc.jboss user (from service msc, change user
and startup type - don't check name as it will try to resolve as
svc.jboss@NET.XXX)

--KERBEROS

copy krb5.conf to jboss\\bin

copy spnego-r7.jar to jboss\\server\\default\\lib\\

add clauses from web.xml\_amendment.txt
(jboss\\..deploy\\jbossweb.sar\\web.xml or
jboss\\..\\deploy\\jbossweb-tomcat55.sar\\conf\\web.xml)

add clauses from login-config.xml\_amendment.txt
(jboss\\..\\default\\conf\\login-config.xml)

copy hello\_spnego.jsp jboss\\..\\root.war

check HTTP SPNs added to both svc.jboss user and machine

--DEPLOYMENT

Stop JBoss service

Copy \\\\dev\\C\$\\\_\\Example to C:\\\_\\Example

check C:\\\_\\Example\\javaee\\arc\_ds.xml connection string

check C:\\\_\\Example\\content\\repository.xml connection strings (x2)

check C:\\\_\\Example\\content\\workspaces\\arc\\workspace.xml
connection string

check C:\\\_\\Example\\content\\workspaces\\default\\workspace.xml
connection string

check C:\\\_\\Example\\apps\\repository\\customers\\arc\\arc.xml icon

Copy C:\\\_\\Example\\javaee\\arc.ear & arc\_ds.xml to
C:\\jbossxxx\\server\\default\\deploy\\

Start JBoss service

--CHECKS

check http://&lt;server&gt;.net.arc:8080/hello\_spnego.jsp

if "Failure unspecified at GSS-API level (Mechanism level: Checksum
failed)- check SPNs

check http://&lt;server&gt;.net.arc:8080/arc/init.biz

if error - check that you have an account in XXX, check
C:\\jbossxxx\\server\\default\\log\\server.log to see your user
principal is being recognised

**[⬆ back to top](#contents)**

## More information

For more information, email <a href="mailto:info@bizhub.com.au">**info@bizhub.com.au**</a> or call us on **+61 (0) 433 209 943**.

<div style="text-align: center; font-weight: bold; margin: 25px 0">
  Brought to you by Biz Hub Australia Pty Ltd.<br><br>
  1, Northcote St, Torrensville, SA, 5031, AUSTRALIA.<br><br>
  www.bizhub.com.au<br><br>
  <img src="media/bizhub_logo_main.png" alt="Biz Hub Logo" width=300 />
</div>
