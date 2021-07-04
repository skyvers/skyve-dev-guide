---
title: "Installing and configuring a Skyve development environment"
permalink: /appendix-installing-and-configuring/
excerpt: "Installing and configuring a Skyve development environment"
toc: true
sidebar:
  title: "Index"
  nav: docs
---

## Installing and configuring a Skyve development environment

These instructions describe the process required to install and configure the 
development environment for Skyve. These instructions assume that you are 
using Windows and SQL Server. Some changes will need to be made if using a 
different operating system or database.

### Prerequisites checklist

Before you begin, ensure you have the following:

* Java ([www.oracle.com](http://www.oracle.com/technetwork/java/javase/downloads/)), JDK 11
* Eclipse IDE for Java EE developers ([www.eclipse.org](https://www.eclipse.org/downloads/)), so that the installation
is in `C:\eclipse\`
* NOTE - When downloading Eclipse, ensure you have selected Eclipse for Java EE (Enterprise Edition) Developers as the non-Enterprise Edition is missing libraries that Skyve utilises, as well as plugins that are used for local deployment. 
* Wildfly 20+ (select the last final version available) ([http://wildfly.org](http://wildfly.org/downloads/))
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
* Download jdk11 (if you haven't already) 
  (http://www.oracle.com/technetwork/java/javase/downloads/) 
  and install it to your machine.
* Go your Control Panel->System and Security->System (for Windows). Now select 
  'Advanced system settings' and click the "Environment Variables..." button.
* Locate 'Path' under System variables double click it, click the _New_ button and 
  enter this (with the semicolon) `;<jdk11 installation folder>\bin\` and 
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
* Change compiler compliance level to 11 (Window -> Preferences -> Java -> Compiler) 
press 'Apply' - press Yes for full build, and then press OK.
* To manage the Wildfly application server from Eclipse:
  * Open the server explorer window if it is not already in your workspace (Window -> Show View -> Servers)
  * Right click inside the server explorer and select New
  * Expand JBoss Community
  * If WildFly 20+ is not in the list of adapters, you will need to download them:
    * Choose JBoss, WildFly & EAP Server Tools and click Next
    * Accept the licence terms and click Finish
    * Restart Eclipse when prompted
  * Select WildFly 20+ and click _Next_
  * Accept the defaults and click _Next_
  * Click _Finish_
  
See additional details in [Setting up a Skyve instance](./../pages/appendix_setting-up-a-skyve-instance)

### Importing an existing Skyve project from Git

*Note: These instructions are for the Eclipse IDE, you will need to locate instructions for your IDE if not using Eclipse.*

In Eclipse, 

* choose File->Import...>Git->Projects from Git->Next->Clone URI and set the URI (for example type in https://github.com/skyvers/skyve.git as URI),
* then click the _Next_ button, 
* choose the master and click the _Next_ button. 
* Choose your destination directory, in this example, we have chosen `C:\\_\ directory`.
* Then click the _Next_ button. 
* The import wizard should be displayed and cloning the Skyve project.

After cloning the master, go to Project -> Clean - Select clean all projects and press OK - wait for activity to cease in bottom right corner of the Eclipse window.

### Starting the server

#### Development environment
Skyve provides a bootstrap user configuration (specified in the `.json` file) - this will insert a user with all module roles as a way to get started. The bootstrap configuration is disabled if the instance is a production instance.

#### Other environments
In UAT and PROD environments, Wildfly should be configured as a service. Refer to Wildfly documentation for detailed instructions.

#### Connecting to your local instance from a mobile device

It is useful to be able to test your Skyve application from your own mobile devices as you develop locally. For example, if you connect your developer PC and mobile device to the same network (for example, your phone's hotspot) use IP address assigned to your PC for the URL in your `.json` settings file - then connect from your phone to your developer PC.

To find the IP address assigned to your dveloper PC in Windows, in a `cmd` window, run:

```
ipconfig
```

![Local IP Configuration](./../assets/images/appendix/local_ip_config.png "Local IP Configuration")

Then set the `server url` setting in the `.json` properties file to match the assigned address, for example `http://192.168.43.182:8080/` (matching your port settings etc) and keep your existing context setting, e.g. `/myapp`.

You will also need to set Wildfly to be able to serve clients other than localhost. To do this double click on your Wildfly server from eclipse to open the configuration and change the standalone arguments to include `-b 0.0.0.0` (note this can also be done directly in the `standalone.xml` file).

![Setting Wildfly for external access](./../assets/images/appendix/wildfly_external_access.png "Setting Wildfly for external access)

Restart Wildfly so the changes take effect.

You can then use the browser on your mobile device to connect to the local Skyve instance using the ural:

```
http://192.168.43.182:8080/myapp
```

**[⬆ back to top](#installing-and-configuring-a-skyve-development-environment)**

---
**Next [Setting up a Skyve instance](./../_pages/appendix-setting-up-a-skyve-instance.md)**<br>
**Previous [Deploying a Skyve application](./../_pages/appendix-deploying-a-skyve-application.md)**
