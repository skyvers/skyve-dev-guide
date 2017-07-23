## Common Patterns

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
* **[Chapter 16: Common Patterns](#common-patterns)**
  * [Identify Current User Contact](#identify-current-user-contact)
  * [Identify if Current User has Role](#identify-if-current-user-has-role)
  * [Save a Document Instance](#save-a-document-instance)
  * [Instantiate a New Document Instance](#instantiate-a-new-document-instance)
  * [Building a Variant Domain List](#building-a-variant-domain-list)
  * [Schedule an Offline Job](#schedule-an-offline-job)
  * [Persist Scalar Values Without Traversing Bean Structure](#persist-scalar-values-without-traversing-bean-structure)
  * [Retrieve and Iterate Through Beans](#retrieve-and-iterate-through-beans)
  * [Singleton Documents (Parameter /Configuration Documents)](#singleton-documents-parameter-configuration-documents)
  * [User-scoped Documents (Personal preferences Documents)](#user-scoped-documents-personal-preferences-documents)
  * [Customise Document and Document Attribute Names](#customise-document-and-document-attribute-names)

### Identify Current User Contact

To identify the current user in Bizlet code, instantiate the Persistence
class. The Persistence class provides the *getUser*() method.

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

_Figure 75 - Example code to retrieve the current user contact_

In the example above, the method first obtains the Persistence
mechanism, then the current user, the customer context in which that
user is logged in, and the application module and document of the
Contact to be retrieved.

When the bean is retrieved from the persistence layer, the bean is
correctly typed.

### Identify if Current User has Role

```java
@Override
public boolean isManager() {
  return isUserInRole("time", "TimesheetManager");
}
```

_Figure 76 - Example of isUserInRole_

The above example establishes whether the current user has the role of
TimesheetManager in the time module.

### Save a Document Instance

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

_Figure 77 - Example code to save a bean_

### Instantiate a New Document Instance

```java
ContactInteraction interaction = ContactInteraction.newInstance();
```

_Figure 78 - Example code to instantiate a new document instance_

### Building a Variant Domain List

![Figure 79](media/image150.png "Figure 79 - Example code to create a variant domain set")

_Figure 79 - Example code to create a variant domain set_

The above example creates a list of domain values (for a selection)
where the relationship to invoices has not been modelled or is ad-hoc.

Normally, generating a result list is not required, or can be handled
automatically by specifying a relationship and relying on the
defaultQuery. However in some circumstances it may be useful to generate
domain lists via code (as above).

### Schedule an Offline Job

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

_Figure 80 - Example code to schedule a oneShot Job_

Note when scheduling a Job, the customer and user context must be
established so that the job will run correctly within the specified
security architecture.

### Persist Scalar Values Without Traversing Bean Structure

Usually, when saving beans, Skyve traverses the entire structure of the
bean to enforce specified validation rules. However for performance
reasons, this may not be required.

Use the *upsertBeanTuple*() method to save the values of the top-most
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

_Figure 81 - Example upsertBeanTuple()_

### Retrieve and Iterate Through Beans

```java
DocumentQuery q = CORE.getPersistence().newDocumentQuery(FileCategory.MODULE_NAME, FileCategory.DOCUMENT_NAME);
q.addOrdering(FileCategory.namePropertyName);

List<FileCategory> categories = q.beanResults();
for(FileCategory cat : categories) {

}
```

_Figure 82 - Example code to retrieve and iterate through a list of beans_

### Singleton Documents (Parameter / Configuration Documents)

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

_Figure 83 - Example edit menu target_

Next, override the *newInstance*() method in the document *Bizlet* to
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

_Figure 84 - Example newInstance method which sets the current Bean_

Because the document will always show in edit mode (i.e. is not accessed
from a list), the view should not offer the OK action as this implies
“save and return to the list”. The developer must consider whether each
action is sensible in the particular context.

### User-scoped Documents (Personal preferences Documents)

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
during newInstance in the Timesheet *Bizlet* class).

### Customise Document and Document Attribute Names

To customise document attribute names, place an override of the
*document.xml* file into the customer package and modify the document
attribute *displayName* and *shortDescription* values accordingly.

To complete the customisation, also place an override of `module.xml`
for each module and update query, role and menu text as required.

![Figure 86](media/image157.png "Figure 86 - Example of customer override of the Contact document, Bizlet and view")

_Figure 86 - Example of customer override of the Contact document, Bizlet and view_

Validation will ensure that both the “vanilla” and overridden artefacts
are consistent with the rest of the application module.

**[⬆ back to top](#contents)**

---
**Next [Chapter 17: Skyve Persistence Mechanisms](./../chapters/skyve-persistence-mechanisms.md)**  
**Previous [Chapter 15: Utility Classes](./../chapters/utility-classes.md)**
