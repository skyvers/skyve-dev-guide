---
title: "Customers"
permalink: /customers/
excerpt: "Understanding Skyve customers"
toc: true
sidebar:
  title: "Index"
  nav: docs
---
## Customers

### Customer declaration
Skyve supports multi-tenant Software-As-A-Service (SAAS) applications
with extensive ability to customise the application for each
tenant/customer.

Each customer's experience of the application is declared within the
customer package. The customer package includes a `customer.xml`
manifest file and a resources folder.

By convention, developers are encouraged to locate all
application-specific artefacts (such as project management and reference
materials) within the customer package.

![Example customer package](../assets/images/customers/image37.png "Example customer package")

The `customer.xml` file declares which modules the customer has access
to, which default converters to use, the location of key resources, and
the default (or home) module.

![Example customer.xml file](../assets/images/customers/image38.png "Example customer.xml file")

The `customer.xml` file must declare the customer name. The customer
name is used to differentiate ownership of data rows within the database
(and therefore also users at the default login page).

The `customer.xml` file also declares the location of the customer logo
file (displayed in the top left hand corner of the UI above the
accordion pane), and assumes the file is located within the customer's
resources folder.

#### Internationalisation
Internationalisation and language support can also be specified when declaring the customer using the _language_ attribute
, and Skyve supports both left-to-right and right-to-left languages.

![Example Arabic with right-to-left](../assets/images/customers/arabic-detail.png "Example Arabic view with right-to-left")

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<customer name="projecta"
            xmlns="http://www.skyve.org/xml/customer"
            xsi:schemaLocation="http://www.skyve.org/xml/customer ../../schemas/customer.xsd"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            language="ar">
</customer>
```

If the language (in this case "ar") is not specified in the `customer.xml`, Skyve will detect the language set in the browser, provided there is an internationalisation file provided.

![Language resource files](../assets/images/customers/image38-1.png "Language resource files")

For more details, see [Internationalisation](./../_pages/internationalisation.md)

### Resources

Skyve provides a resource hierarchy to allow for the inclusion of
additional resources in a generic way.

Throughout the application, resources (like the *relativeIconFileName*
for actions) resolve the applicable resource location from the resources
hierarchy.

Resources (e.g. button icon files) can be defined for each module within
the module *resources* folder. Resources can also be defined per
customer within the customer *resources* folder (e.g. the company logo
file).

A module *resources* folder can be overridden as part of a customer's
module override by placing files of the same name as occur in the
original module.

So that report templates can references resources in a generic way, the
absolute file location of the customer resources folder is resolved at
run-time and included automatically as a report parameter.

### Modules

The `customer.xml` file lists all modules accessible to the customer.

The order of modules listed in the `customer.xml` file is the order that
module-level accordion menus appear in the accordion pane. Additionally,
the order of modules defines the compile order. If developers create
cross-module code, the compile order must be considered for that
customer.

The home or default module is declared here but can be overridden per
user from within the user functionality (within the admin module).

### Adding a new customer

Skyve is designed around the ability to build applications, then re-sell them to multiple _customers_ or _tennants_ as Software-as-a-Service (SaaS). 
The _customer_ concept allows you to share and reuse the code you create for your application, 
and customise it for each _customer_ (where appropriate).

In a multi-tenanted environment, each user must specify the customer scope in which their account exists.

![Signing in](../assets/images/customers/skyve_customer_sign_in.png "Signing in")

In Skyve the term _Customer_ connotes customisation. If there's no (or only trivial) customisation required,
 you might resell the software to millions of users under the one Skyve customer context.

### Setting a default customer

Note that if you only ever want to use a single customer, you can specify a default customer in the .json file of your instance. This will also avoid having to enter a customer name at the sign in prompt.

1. Edit the file '.json' instance settings file (in the `wildfly/standalone/deployments/` folder)
2. Search for the ```// Customer Default``` comment
3. Change the default customer setting from null to your customer name, for example changing ```customer: null,``` to ```customer: "acme",```
4. Save the file and restart your wildfly server (or redeploy the application by renaming ```myApplication.war.deployed``` to ```myApplication.war.dodeploy```.

![Signing in with a default customer](../assets/images/customers/skyve_default_customer_sign_in.png "Signing in with a default customer")

**[⬆ back to top](#contents)**

---
**Next [Modules](./../_pages/modules.md)**  
**Previous [Building Applications](./../_pages/building-applications.md)**
