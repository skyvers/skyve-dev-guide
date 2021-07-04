---
title: "Deploying a Skyve application"
permalink: /appendix-deploying-a-skyve-application/
excerpt: "Deploying a Skyve application"
toc: true
sidebar:
  title: "Index"
  nav: docs
---

## Deploying a Skyve application

Skyve builds applications as a single web archive (`.war`) folder, containing the application metadata and Skyve platform components. By default, Skyve `.war` folders are deployed 'exploded' or 'unzipped'.

The `.war` folder is deployed with a `.json` settings file and a `-ds.xml` datasource file. 

For example, to deploy a Skyve application called `myApplication` using a Wildfly application server, you will need to place the following into the Wildfly `deployment/` area:

```
myApplication.war
myApplication-ds.xml
myApplication.json
```

where 
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

Additional steps are required for Single Sign-On configuration, 
the creation of service user accounts, SPNs and port configuration.

**[⬆ back to top](#deploying-a-skyve-application)**

---
**Next [Installing and configuring a Skyve development environment](./../_pages/appendix-installing-and-configuring.md)**<br>
**Previous [Changing database dialect](./../_pages/appendix-changing-database-dialect.md)**
