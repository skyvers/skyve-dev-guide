---
title: "Common patterns"
permalink: /common-patterns/
excerpt: "Common patterns in Skyve applications"
toc: true
sidebar:
  title: "Index"
  nav: docs
---

## Common patterns

### Singleton documents
There are a variety of situations where (from the User perspective) there is only ever 1 record they deal with.

For example: 
* if the user has a personal settings page
* a "sticky" search page (which remembers a user's own search criteria) 
* a personal details page (for personal/private details)
* a systems settings page (where these contain settings or controls for the system - admin.DataMaintenance is an example of this)

The Skyve _edit_ view allows a user to interact with a single bean instance and normally, a user will navigate to the _edit_ view from a _list_ view which sets the context. However, with the above examples, the usual gesture of _list-then-edit_ makes little sense, and instead, there will almost always be a single record which is the one the user wants to interact with.

When the `edit` view is provided as a menu item, the user has no way of setting the context, as they do not navigate to the view from a list, and so Skyve implicitly creates a new bean instance for the view.

![User dashboard example of an edit menu item](../assets/images/modules/user-dashboard.png "User dashboard example of an edit menu item")
_The User dashboard (admin module) is an example of an edit menu item_

This implicit behaviour can be ambushed for the _singleton_ pattern, by retrieving the desired bean instance and returning that, instead of the instance implicitly created by Skyve.

Skyve offers two Bizlet implementations for Singletons: SingletonBizlet and SingletonCachedBizlet.

The *SingletonBizlet* retrieves the first (and presumably only) instance. The *SingletonCachedBizlet* caches the retrieved instance for subsequent `.newInstance()` calls - until the instance is evicted (called automatically by `postSave()` for this implementation).

To create a Singleton:
* set the scope of the document appropriately  (normally _User_ or _Customer_ scope - e.g. setting the document privelege in your module.xml to "CRUD*U*" for User-scoped)
* add an `edit` menu item to the `module.xml` for the document
* override the `newInstance()` Bizlet method to set any transient default values.

An example SingletonCachedBizlet is as follows:

```java

public class GeneralBizlet extends SingletonCachedBizlet<General> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1617067133911972551L;

	@Override
	public General newInstance(General bean) throws Exception {
	
		General result = super.newInstance(bean);
		
		result.setFeeTerm(result.getCurrentTerm());
		result.setFeeDate(new DateOnly());

		return result;
	}
}
```

In this example, there is assumed to be one and only one (i.e. singleton) persisted _General_ instance which the user has access to retrieve. The call to `super.newInstance(bean)` will retrieve the singleton instance of General, and the `newInstance()` method is overridden to set default values for the attributes `feeTerm` and `feeDate`.

Elsewhere in the application, to retrieve the singleton General, call `General.newInstance()` to retrieve the cached instance as follows:

```java

public static calculate(Transaction bean){

	General general = General.newInstance(); //retrieves the cached singleton instance and sets any default values

	bean.setTransactionTerm(general.getFeeTerm());
	bean.setTransactionDate(general.getFeeDate());

}
```

For another example, see admin.Configuration as an example of a _Customer-scoped_ singleton that overrides the `newInstance()` method to set default values.

### Identify the current user's Contact

To identify the current user in Bizlet code, instantiate the Persistence
class. The Persistence class provides the `getUser()` method.

```java
public static Contact getCurrentUserContact() throws MetadataException, DomainException {
  Persistence persistence = CORE.getPersistence();
  User user = persistence.getUser();
  Customer customer = user.getCustomer();
  Module module = customer.getModule(Contact.MODULE_NAME);
  Document document = module.getDocument(customer, Contact.DOCUMENT_NAME);

  Contact contact = persistence.retrieve(document, user.getContactId(), false);

  return contact;
}
```

In the example above, the method first obtains the Persistence
mechanism, then the current user, the customer context in which that
user is logged in, and the application module and document of the
Contact to be retrieved. When the bean is retrieved from the persistence layer, the bean is
correctly typed.

Note the distinction here between `org.skyve.metadata.user.User` and `modules.admin.domain.User` (see more below).

The Contact is declared in the application domain - in the admin module, and `modules.admin.domain.User` is similarly the modules.admin.domain.User is part of the declared application, not the Skyve platform itself.

`org.skyve.metadata.user.User` is a different type used internally by the Skyve platform and Persistence. 

An alternative to this approach is to use the convenience method provided in `ModulesUtil` as follows:
```java
	Contact contact = ModulesUtil.currentAdminUser().getContact();
```	


### Identify if current User has a role

```java
@Override
public boolean isManager() {
  return isUserInRole("time", "TimesheetManager");
}
```

_Example of isUserInRole_

The above example establishes whether the current user has the role of
TimesheetManager in the time module.

Note that there is a distinction between
* `modules.admin.domain.User` (or 'admin User') is the instance of the User document as declared in the Skyve 'admin' module.
* `org.skyve.metadata.user.User` (or 'MetaData User') which relates to the current session/conversation.

#### User convenience methods

`toMetaDataUser()` (in the UserExtension class) convenience method to retrieve the 'MetaDataUser User' from the 'admin User'. `toMetaDataUser()` will return `null` if the user has not yet been persisted.

`isInRole()` method returns if the metadata user has been assigned a module role.

`ModulesUtil.currentAdminUser()` returns the admin user associated with the conversation user.

For example, if a bean has an association to admin.User called myUser, to perform some steps if that user has been assigned the role 'Manager' in the module 'crm':

```java
// check if the user has been assigned the role 'Manager' in module 'crm'
modules.admin.domain.User user = bean.getMyUser();
if(user!=null 
	&& user.toMetaDataUser()!=null 
	&& user.toMetaDataUser().isInRole("crm","Manager")){ 
	
...

}
```


### Save a document instance

To save a document instance, you can identify the module and document
of the bean, or optionally save any subclass of `PersistentBean` directly.

```java
Persistence persistence = CORE.getPersistence();
Customer customer = persistence.getUser().getCustomer();
Module module = customer.getModule(Contact.MODULE_NAME);
Document document = module.getDocument(customer, Contact.DOCUMENT_NAME);

// save the bean specifying the document
bean = persistence.save(document, bean);

// or save the bean directly
bean = persistence.save(bean);
```

_Example code to save a bean_

### Instantiate a new document instance

```java
ContactInteraction interaction = ContactInteraction.newInstance();
```

_Example code to instantiate a new document instance_

Note that the developer can override the default Skyve `newInstance()` behaviour in the corresponding `Bizlet` class.

### Building a variant domain list

![Create a variant domain set](../assets/images/common-patterns/image150.png "Example code to create a variant domain set")

The above example creates a list of domain values (for a selection)
where the relationship to invoices has not been modelled or is ad-hoc.

Normally, generating a result list is not required, or can be handled
automatically by specifying a relationship and relying on the
defaultQuery. However in some circumstances it may be useful to generate
domain lists via code (as above).

### Schedule an offline Job

Declare the Job within the `module.xml` file and the Job class
(extending `org.skyve.job.Job`).

```java
/**
 * Kick off the annual returns job
 */
@Override
public ServerSideActionResult<GrowerSearchCriteria> execute(GrowerSearchCriteria search, WebContext WebContext) throws Exception {
  User user = CORE.getPersistence().getUser();
  Customer customer = user.getCustomer();
  Module module = customer.getModule(Grower.MODULE_NAME);
  Job job = module.getJob("jAnnualReturns");

  EXT.runOneShotJob(job, search, user);

  search.setReturnResults("The generation job has commenced.");

  return new ServerSideActionResult<>(search);
}
```

_Example code to schedule a oneShot Job_

Note when scheduling a Job, the customer and user context must be
established so that the job will run correctly within the specified
security architecture.

### Persist scalar values without traversing the bean structure

Usually, when saving beans, Skyve traverses the entire structure of the
bean to enforce specified validation rules. However for performance
reasons, this may not be required.

Use the `upsertBeanTuple()` method to save the values of the top-most
attributes of the bean, without traversing the entire bean structure.
This is useful if the task requires updates of trivial nature to beans
with substantial complexity, or if bean validation needs to be bypassed for some reason.

```java
DateOnly requestedDate = new DateOnly();
for(Subscription sub : subsToUpdate) {
  sub.setRequestedDateTime(requestedDate);
  CORE.getPersistence().upsertBeanTuple(sub);
}
```

_Example upsertBeanTuple()_

### Retrieve and iterate through beans

```java
DocumentQuery q = CORE.getPersistence().newDocumentQuery(FileCategory.MODULE_NAME, FileCategory.DOCUMENT_NAME);
q.addOrdering(FileCategory.namePropertyName);

List<FileCategory> categories = q.beanResults();
for(FileCategory cat : categories) {

}
```

_Example code to retrieve and iterate through a list of beans_

### Singleton documents (user, parameter and configuration documents)

A singleton document is a document of which there should only ever be
one instance within the current scope or context.

Singletons are commonly used for configuration or preference documents
which contain module configuration/preference settings. For example, a
Timesheet module may have a preference document specifying the expected
number of hours to be completed.

First, in the `module.xml` file, add a menu item for the document which
has an element type of _edit_.

```xml
<edit name="Configuration" document="Configuration">
	<role name="Administrator" />
</edit>
```

_Example edit menu_

Next, override the `newInstance()` method in the document *Bizlet* to
set the bean to be the first bean returned from *DocumentQuery*. Using
*DocumentQuery* will ensure that appropriate document scoping and
permissions will automatically be applied, restricting the beans
returned as declared.

```java
@Override
public Configuration newInstance(Configuration bean) throws Exception {
	Persistence p = CORE.getPersistence();
	DocumentQuery q = p.newDocumentQuery(Configuration.MODULE_NAME, Configuration.DOCUMENT_NAME);
	Configuration result = q.beanResult();
	if (result == null) {
		result = bean;
	}
	return result;
}
```

_Example newInstance method which sets the current Bean_

Because the document will always show in edit mode (i.e. is not accessed
from a list), the view should not offer the OK action as this implies
"save and return to the list". The developer must consider whether each
action is sensible in the particular context.

### User-scoped documents (personal preferences documents)

Create a singleton document (as described above), but additionally scope
the document to User scope in the `module.xml`.

Generally for this type of document, the *Delete* permission is not
assigned.

```xml
<document name="FinancialReports" permission="CRU_U" />
```

_Figure 85 - Example of user scoped document permission_

For example, a Timesheet module may have a User-scoped preference
document allowing users to set their default task (which could be set
during newInstance in the Timesheet `Bizlet` class).

### Customise document and document attribute names

To customise document attribute names, place an override of the
*document.xml* file into the customer package and modify the document
attribute *displayName* and *shortDescription* values accordingly.

To complete the customisation, also place an override of `module.xml`
for each module and update query, role and menu text as required.

![Customer override](../assets/images/common-patterns/image157.png "Example of customer override of the Contact document, Bizlet and view")

_Example of customer override of the Contact document, Bizlet and view_

Validation will ensure that both the "vanilla" and overridden artefacts
are consistent with the rest of the application module.

**[⬆ back to top](#common-patterns)**

---
**Next [Skyve persistence](./../_pages/skyve-persistence-mechanisms.md)**  
**Previous [Utility Classes](./../_pages/utility-classes.md)**
