---
title: "Building applications"
permalink: /building-applications/
excerpt: "Building applications"
toc: true
sidebar:
  title: "Index"
  nav: docs
---
# Building applications

### Skyve readme

The skyve project contains a Readme at [github.com/skyvers/skyve](https://github.com/skyvers/skyve) with useful information for getting started, configuring Spring security and other steps to get you started.

You can also follow the instructions provided in the video at the Skyve Foundry - Skyve Project Creator

<div style="margin: 0 auto; text-align: center">
	<a href="https://youtu.be/Ar7y1y1xhHg" target="_blank">
		<img src="../assets/images/building-applications/skyve-project-creator.png" alt="Skyve Project Creator" width="500" border="10" />
	</a>
</div>

## Creating a new Skyve project

### Before you start:
Install Eclipse or an alternative Java based Integrated Development Environment and an application server. Detailed instructions are available in the Skyve Readme at [github.com/skyvers/skyve](https://github.com/skyvers/skyve).

### Overview:
1. Use the Project Creator [https://foundry.skyve.org/foundry/project.xhtml](https://foundry.skyve.org/foundry/project.xhtml) to create a Skyve project download and receive the link to the file via email.
2. Import the project as a maven project and run the Generate Domain run configuration.
3. Configure your application server security domain, create an empty database, and deploy your application.
4. Log into your application at localhost:8080/<projectName> with your bootstrap credentials and begin using the no-code application.

### Steps
To get started, go to the project creation page (at foundry.skyve.org/foundry/project.xhtml). The project creator will create a configured Java project, set up for maven dependency management, for common development environments like Eclipse and IntelliJ.
* Enter your email address (so you can be notified when the project is created and ready to download) and give your  project a name.
* Skyve supports multi-tennanted applications - so all data is secured against a customer or client name. Just enter your organisation name.
* Next choose the type of database you want for structured data - if you're not sure, the H2 file-based option is an easy way to get started -  and you can change to something else later.
* Finally, if you've got a Skyve Script you can add it here. If you don't, just leave this blank.
* Create your project, and in about 1 minute, you'll receive an email with a download link.
* Once you receive the email, use the link to download your project, and unzip it to your Java workspace. 
* From your development environment, import the project as a maven project.

### Configuring your new project
Add the project to your Wildfly server (in the Eclipse server window).

The project contains two configuration files - a data source xml and a project properties json file (for example `myApplication.json` and `myApplication-ds.xml`. Copy these into the Wildfly standalone deployment folder.

Now, create a folder for your noSQL data store - which will hold your content attachments and images. Skyve backups will also be placed here. You can place it anywhere, but its best to place it outside of your project, so that Eclipse doesn't waste time scanning it for changes. Then update the project json configuration to match that location.

If you're using SQL Server, you'll need to configure Wildfly by placing the jdbc jar and windows authentication dll in place. And if you've chosen to use MySQL or SQL Server, you'll also need to go and create an empty database - but if you're using H2, you can skip this step.

Don't worry about creating tables, Skyve will do that for your when you first deploy - for now, just update your datasource xml to match your local configuration.

Finally, run the Generate Domain target to build your project and resolve all dependencies.

### Deploying your new project and logging in

Start your wildfly server. 

Right click your project in the server tree and publish - Skyve will automatically create all the tables your database needs for you. Your project properties json file includes a bootstrap user - this is a user credential which will be automatically injected into your database so you can log in. Note that the bootstrap user is disabled if you're in a production environment, it's only for getting started.

Once your project is running, go to your local context and login in.

Detailed instructions are available in the Skyve Readme at [github.com/skyvers/skyve](https://github.com/skyvers/skyve).

**[⬆ back to top](#building-applications)**

---
**Next [Customers](./../_pages/customers.md)**  
**Previous [Exception Handling](./../_pages/exception-handling.md)**
