---
title: "Skyve application configuration"
permalink: /appendix-skyve-application-configuration/
excerpt: "Skyve application configuration"
toc: true
sidebar:
  title: "Index"
  nav: docs
---

## Skyve application configuration
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

### Configuring Recaptcha for the sign in page 

First, sign up for a Google Recaptcha key as follows:
1. Visit [Google Recaptcha](https://www.google.com/recaptcha/intro/v3.html) console and sign in
2. Click Admin console in the top right corner
3. Click the + button in the toolbar to create a new site
4. Enter a label to identify what this key will be for (the name of your Application)
5. Select reCAPTCHA v2 and select the type of challenge (the default is fine)
6. Enter the URL of your Skyve application
7. Read and accept the terms of use
8. Click Submit

Once you have received your *site* key, copy your key into the Startup Configuration section of the Admin menu of your Skyve application:
1. From the Admin menu, open Configuration
2. Change tabs to the Startup Configuration tab
3. Scroll down to the Security Settings section, enter the Key and then press Save at the top of the form.

Alternatively, you can place the API key in the project JSON file under `api` -> `googleRecaptchaSiteKey:` 

```
	// API Settings
	api: {
		googleRecaptchaSiteKey: "xxxxxxxxxxx",
```

### Changing the project URL

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

5. Rename the `myApplication-ds.xml` to `tax_management-ds.xml`
6. Maven update the project (for example, in eclipse, right-click the project, choose Maven and update Project)
7. Add the project to your wildfly server

Note that 
* when deploying, you may need to manually create the `tax_management-ds.xml.dodeploy` and `tax_management.war.dodeploy` signal files. 
* You need to manually remove the previous `myApplication.war` if you didn't in the preparation stage. 
* If you didn't rename the previous `myApplication.json` and `myApplication-ds.xml`, you should these files to avoid confusion (for example if the datasource name is the same for both the old `myApplication-ds.xml` and `tax_management-ds.xml`).

### Protecting the ds.xml credentials

The ds.xml password is clear text. While it is possible to use an encrypted password for the datasource connection, this is of limited use.

Instructions are available if this is required, for example [Encrypt password for Wildfly](https://stackoverflow.com/questions/33538117/wildfly-encrypt-password-and-username-for-database).

This approach has limited use as it is really just a level of indirection. The credentials need to be decrypted and so a key needs to be stored. This means the file containing the key then needs to be protected on the file system - and if this is possible, it would seem reasonable to simply apply that level of protection to the ds.xml file (and other config) directly.


### Example deployment instructions for Single Sign-on

The following steps are to install an instance of XXX onto a vanilla
Windows 10.

--JAVA

Install Java JDK

Set `JAVA\_HOME` to location of the Java root directory - e.g.
`C:\Java\jdk11...`

Set `PATH` to include `Java\bin` - e.g. `C:\Java\jdk11...\bin`

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

**[⬆ back to top](#skyve-application-configuration)**

---

**Next [Changing database dialect](./../_pages/appendix-deploying-a-skyve-application.md)**<br>
**Previous [Changing database dialect](./../_pages/appendix-changing-database-dialect.md)**
