---
title: "Utility classes"
permalink: /utility-classes/
excerpt: "Utility classes"
toc: true
sidebar:
  title: "Index"
  nav: docs
---

Skyve provides the following utility classes:

<table>
<thead>
	<th>Class/Library</th>
	<th>Description</th>
</thead>
<tbody>
  <tr>
    <td>BeanValidator</td>
    <td>Various methods for validation
      <ul>
        <li><code>validateBeanAgainstBizlet()</code> - Validate a bean against its bizlet .validate() - NB This validation method does NOT recursively validate using bizlets through the base document hierarchy as the bizlet class should be arranged in such a way as to extend the bizlet methods required of the base bizlet classes through the standard java extension mechanism.</li>
        <li><code>validateBeanAgainstDocument()</code> - Validate a document instance against its metadata.</li>
        <li><code>validateBeanPropertyAgainstAttribute()</code> - Validate a document attribute against its metadata</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td>Binder</td>
    <td>Provides methods for generic bean binding manipulations, including: <br><br><ul><li>generic get() and set() methods for a bean with nominated binding, </li><li><code>formatMessage()</code> for using binding substitutions in string outputs using the correct converter</li><li><code>convertAndSet()</code> for setting a converted string value using the correct converter</li><li><code>createCompoundBinding()</code> for correctly constructing compound bindings (i.e. across document references), and </li><li>read and interpret the nature of bean properties.</li></ul></td>
  </tr>
  <tr>
    <td>CORE</td>
    <td>Utility class for the core Skyve API.</td>
  </tr>
  <tr>
    <td>DocumentQuery</td>
    <td>Provides methods for constructing object queries without resorting to constructing OQL or SQL strings.</td>
  </tr>
  <tr>
    <td>EXT</td>
    <td>External and extra dependency APIs (Bizport and POI, mail, reporting, jobs and developer access to the Skyve tagging feature).</td>
  </tr>
  <tr>
    <td>ModulesUtil</td>
    <td>Contains a number of business-focused methods and enumerations for cross-module functionality, including: <br><br><ul><li> methods for working with business periods (month, quarter, half-year etc.) and frequency, </li><li><code>currentAdminUser()</code> and <code>currentAdminUserProxy()</code> which identifies the current conversation user as an admin module user, </li><li> coalesce methods, and </li><li> basic Tax and loan calculation methods.</li></ul></td>
  </tr>
  <tr>
    <td>Persistence</td>
    <td>Provides access to interact directly with the singleton persistence mechanism (detailed below).</td>
  </tr>
  <tr>
    <td>Time</td>
    <td>Helper class to work with Skyve Date and Time classes.</td>
  </tr>
  <tr>
    <td>Util</td>
    <td>Bean-level generic utilities, including generic bean methods e.g. <code>cloneToTransientBySerialisation()</code>.</td>
  </tr>
</tbody>
</table>

## Document Number pattern
The Skyve `NumberGenerator` pattern is designed for business focused identifiers (e.g. Purchase order numbers, Quotation numbers, Invoice numbers etc) from a central issuing authority, and handles concurrent use by multiple users.

Skyve automatically maintains `bizId` as a guaranteed unique identifier which can be generated independently within the client (or from a disconnected device or 3rd party system if required), but this is for internal use only and is not intended to be used for business purposes.

The `getNumberGenerator().next()` is intended for indelible/auditable business processes, where numbers are never reassigned (even if the owning document might be deleted), and as such, would normally be assigned when the owning document is saved (rather than created). If the number is assigned when the document is created, if the user decides not to save the document, that number will have been allocated, but there will be no owning document.

The usual pattern for using `next()` is as follows:

First add either a `blurb` or disabled `textField` to the view, with visibility controlled by `persisted` (i.e. there is no number to show until the document is saved).

```xml
<item>
	<blurb visible="persisted">
		{purchaseOrderNumber}
	</blurb>
</item>
```

To assign the numbers during save, place the following code into the `preSave()` method in the document's bizLet class:

```java
if (bean.getPurchaseOrderNumber() == null) {
	// get the next assistance id
	bean.setPurchaseOrderNumber(CORE.getNumberGenerator().next(PurchaseOrder.MODULE_NAME, PurchaseOrder.DOCUMENT_NAME, PurchaseOrder.purchaseOrderPropertyName)));
}
```

Or alternatively, if you want to have prefixed numbers like 'PO0001', use the alternative signature:

```java
if (bean.getPurchaseOrderNumber() == null) {
	// get the next assistance id
	bean.setPurchaseOrderNumber(CORE.getNumberGenerator().next("PO", PurchaseOrder.MODULE_NAME, PurchaseOrder.DOCUMENT_NAME, PurchaseOrder.purchaseOrderPropertyName, 4)));
}
```

## Binder

Binder is a utility class which handles beans in a generic way, taking into account customer default settings and customer overriding.

### formatMessage() and convertAndSet()

Developers can take advantage of the `formatMessage()` to construct valid String output using the correct converter, as declared in customer or document metadata. Similarly `convertAndSet()` takes a String argument and sets the bean attribute using the correct converter `fromDisplayValue()` method.

`formatMessage()` allows bean varargs, and the binding substitution will occur in the order in which the beans are supplied.
```java
Binder.formatMessage(CORE.getCustomer()
	, "The timesheet for {employeeCode} ({contact.name}) weekending {weekEndingDate} is overdue"
	, timesheet
	, employee);
```

In the above example, `formatMessage()` will attempt to substitute bindings from *timesheet*, then *employee*. The `{weekEndingDate}` binding will be substituted using the correct converter either as specified as the customer default conversion (for example `DD_MMM_YYYY`), or as specified in the document attribute declaration (which may be different).

Skyve takes advantage of `formatMessage()` as the basis of *bizKey* expressions in the metadata, supplying the bean as parameter implicitly. For example:

```xml
<bizKey expression="{employeeCode} ({contact.name})"/>
```

Similarly, `convertAndSet()` takes a String, applies the correct converter `fromDisplayValue()` method and sets the binding with the result value.

```java
Binder.convertAndSet(timesheet, WeeklyTimesheet.weekEndingDatePropertyName, "12-Dec-2019");
```

### Binder generic get() and set()

Using the Binder for generic approaches to getting and setting attribute values provides a powerful way to manipulate beans.

```java		
DateOnly weekEndingDate = Binder.get(timesheet, WeeklyTimesheet.weekEndingDatePropertyName);
```

``` java		
Binder.set(timesheet, WeeklyTimesheet.weekEndingDatePropertyName, weekEndingDate);		
```

## CORE

CORE offers a number of key convenience methods.

Method | Description/Usage
-------|------------
`getUser()` | returns the metadata user/user principal (as distinct from the current `modules.admin.domainUser`)
`getCustomer()` | returns the current customer in for the current metadata user/user principal
`getNumberGenerator()` | returns the Skyve sequence function to create unique formatted serial document identifiers
`getPersistence()` | See Persistence below
`getStash()` | returns a convenience Map for the current conversation available to the developer

### Session and conversation storage

Skyve offers a temporary cache/stash at both the conversation and session context level. Use of the stash can impact performance and developers must consider the implications carefully. Minimizing the overheads in keeping the conversation and session state increases application scaleability.

`Core.getStash()` provides conversation-level storage for developer use. The stash is a `Map` that developers can use to store objects for later recall within the conversation context.

```java
CORE.getStash().put("someKey", someObject);
```

and then to retrieve

```java
someOtherObject = CORE.getStash().get("someKey");
```

The conversation stash exists for the duration of the conversation. Developers are responsible for managing the stash for the duration of the conversation, including removing objects no longer required.

Similarly, the `User` attributes `Map` is available for session-level storage.

```java
CORE.getPersistence().getUser().getAttributes().put("someKey", someObject);
```

and then to retrieve

```java
someOtherObject = CORE.getPersistence().getUser().getAttributes().get("someKey");
```

## DocumentQuery

*DocumentQuery* extends *ProjectionQuery* and provides the ability to
retrieve persisted beans in a type-safe and secure way, without building
SQL or OQL Strings.

Key benefits of the DocumentQuery approach are:
1. to abstract the developer from implementation-specific SQL - so that applications can remain database independent, 
2. to allow compile-time checks on query code, and
3. to ensure a consistent security and performance via Skyve's Persistence singleton object.

A trivial example of DocumentQuery is to retrieve typed beans using a simple filter:

```java
// retrieve a list of inactive users
DocumentQuery q = CORE.getPersistence().newDocumentQuery(User.MODULE_NAME, User.DOCUMENT_NAME);
q.getFilter().addEquals(User.inactivePropertyName, Boolean.TRUE);

List<UserExtension> inactiveUsers = q.beanResults();
for(UserExtension u : inactiveUsers) {
  ...
}
```

### Complex Example 

For more complex queries, developers can construct queries using a range of extended filter features:

```java
// collect settlements between range, with area > 0
// either from or to the current Grower
DocumentQuery q = CORE.getPersistence().newDocumentQuery(VineyardChange.MODULE_NAME, VineyardChange.DOCUMENT_NAME);
DocumentFilter dFrom = q.newDocumentFilter();
DocumentFilter dTo = q.newDocumentFilter();
q.getFilter().addBetween(VineyardChange.madePropertyName, firstDayOfYear, new DateOnly());
q.getFilter().addGreaterThan(VineyardChange.transferredAreaPropertyName, Decimal5.ZERO);

dFrom.addEquals(VineyardChange.fromGrowerPropertyName, bean);
dTo.addEquals(VineyardChange.toGrowerPropertyName, bean);
dTo.addOr(dFrom);

q.getFilter().addAnd(dTo);

List<VineyardChange> settlements = q.beanResults();
for(VineyardChange settlement : settlements) {
  settlement.setStatus(Status.completed);
}
```

_Example DocumentQuery_

In the example provided in the above example, the `DocumentQuery` is used to
retrieve all `VineyardChange` beans. The beans are returned in a typed
List and document permissions and scoping rules are automatically
enforced by Skyve.

The use of `DocumentFilter` allows for correct enforcement of types at
compile-time to reduce the possibility of errors arising from implicit
type conversion which may arise if SQL strings were used.

However, where necessary, developers can take advantage of other querying options including:
1. creating queries using SQL and BizQL (similar to HQL)
2. re-using and manipulating no-code queries declared in the module.xml

### Aggregate Functions

Another example of using a `DocumentQuery` is performing aggeregate functions. In this example, 
it returns a count of the number of `Contacts` in the database:

```java
DocumentQuery qCount = persistence.newDocumentQuery(Contact.MODULE_NAME,
    Contact.DOCUMENT_NAME);
qCount.addAggregateProjection(AggregateFunction.Count, Bean.DOCUMENT_ID, "CountOfId");
int numberOfContacts = qCount.scalarResult(Number.class).intValue();
```

Other aggregate functions available include `Sum`, `Min`, `Max` and `Avg`.

For more querying options, we recommend developers read [Using bizQL](../queries/#using-bizql) and [Using SQL](../queries/#using-sql) and review the other query examples provided in that chapter.

## EXT

The EXT class provides developers access to additional APIs as follows:

  Method(s)        | Description/Usage
  ---------------- | ------------------
 `checkPassword()`, `hashPassword()` | check a password against a hash, or hash a password
 `getCaching()` | Obtains an instance of the the Cache Manager for working with caches.
 `getJobScheduler()` | Obtains an instance of the Job Scheduler for working with jobs in Skyve.
 `getReporting()`    | Obtains an instance of the Reporting service for working with reports.
 `getTagManager()`   | Obtains an instance of the Tag Manager for working with tags.
 `sendMail()`, `writeMail()` | Send or write mail items
 `getDataStoreConnection()`, `newSQLDataAccess()` | access connections
 `newContentManager()` | access the content repository
 `newBizPortStandardGenerator`, `newBizPortWorkbook()`, `newBizPortSheet()` | developer access to customise Skyve's BizPort feature.

## CommunicationUtil

For details of the CommunicationUtil class, see [Communication](./../_pages/communication.md)

## Job Scheduler

For more details of working with Jobs in Skyve, see [Jobs](./../_pages/jobs.md)

  Method        | Description/Usage
  ------------- | ------------------
  `getCustomerRunningJobs()` | Returns a list of currently executing jobs, reports and background tasks for the current customer.
  `runOneShotJob()`          | Executes a Skyve job once, starting immediately. It disappears from the Scheduler once it completes and a record is stored in the admin job log.
  `scheduleOneShotJob()`     | Executes a Skyve job once at a particular date and time.

## ModulesUtil

ModulesUtil is intended to provide common business focused convenience methods and enumerations and also to provide an introductory reference to Skyve developers for accessing framework concepts and features for real-world applications.

### Enumerations

Enumeration | Description
----------|--------
CalendarMonth | months of the year
OccurrenceFrequency | Common frequencies (weekly, monthly, quarterly etc)
OccurrencePeriod | Common periods (week, month, quarter etc)
DayOfWeek | days of the week

### Methods

  Method(s)        | Description/Usage
  ---------------- | ------------------
  `addDaysDateOnly()`, `addFrequency()`, `annualFrequencyCount()`, `annualPeriodCount()` | Date manipulation for the specified period or frequency
  `firstDayOfMonth()`, `lastDayOfMonth()`, `firstDayOfYear()`, `lastDayOfYear()` | convenience methods for important dates
  `calendarMonthName()`, `calendarToDay()`, `dayOfWeekToCalendar()`, `sqlDateFormatOnly()` | calendar conversion

### Comparison and String convenience methods

  Method(s)        | Description/Usage
  ---------------- | ------------------
  `bothNullOrEqual()` | handle nullable value comparison  
  `coalesce()`, `concatWithDelim()`, `enquote()`, `titleCase()` | basic String manipulations

### Unique document number generation

  Method(s)        | Description/Usage
  ---------------- | ------------------
  `incrementAlpha()` | increment alphanumeric value

### bean and User

  Method(s)        | Description/Usage
  ---------------- | ------------------
  `currentAdminUser()` | retrieve the modules.admin.domain.User from the user principal
  `currentAdminUserProxy()` | retrieve the modules.admin.domain.User from the user principal without groups and roles
  `getCurrentUserContact()` | retrieve the contact details for the user principal
  `hasModule()` | determine whether a user has access to a specified module
  `lookupBean()` | shorthand way of finding a bean using a legacy key value
  `getConditionName()` | returns a fomatted string representing the condition
  `getPersistentIdentifier()` | returns the database tablename for a given module.document
  `replaceBindingsInString()` | performs binding replacement whether the supplied string has the attribute displayName as the binding

### BizPort

  Method(s)        | Description/Usage
  ---------------- | ------------------
  `standardBeanBizExport()`, `standardBeanBizImport()` | Code example of BizPort

### Comparators

  Method(s)        | Description/Usage
  ---------------- | ------------------
  `DomainValueSortByCode`, `DomainValueSortByDescription` | Simple comparators for DomainValue lists.

## Persistence

Skyve's Persistence mechanism is described in detail in
* [Skyve Persistence Mechanisms](./../_pages/skyve-persistence-mechanisms.md)

The following is specific to the use of the Persistence utility class.

The *Persistence* class provides access to persistence-specific
functionality which may be required by developers, while ensuring that
developer code will comply with security and threading mandates.

Key persistence methods are:

  Method           | Description
  ---------------- | ------------------
  `getUser`          | Gets the current conversation *metadata.user*
  `getCustomer`	| Gets the current 
  `begin`, `rollback`, `commit` |   Control the state of the current transaction
  `evictAllCached`, <br><br>`evitCached` | Evict beans from cached memory.<br><br>These methods are useful where code interactions with *persistentBeans* may be in contention with default actions resulting from user activity (like pressing the Save button). <br><br>For example, if a user executes an action which impacts on the bean displayed in the view, and modified beans are not evicted, the user's action will be in contention with the action. In this case, the user's subsequent attempt to Save the bean will be met with a message stating that the current bean has already been modified by the user and can't be saved. <br><br>Evicting cached beans at the conclusion of the action will avoid such a contention issue.
  `delete` | deletes a bean instance
  `flush` | pushes all pending DML statements to the database (without _commit_)
  `save` | saves the bean or beans
  `upsertBeanTuple`, <br><br>`upsertCollectionTuples` | Persists values only within the top-most level of the bean structure. <br><br>During an *upsert*, no bean validation is performed, and reference ID values are persisted without traversing into the related bean.

_Key methods of the Persistence utility class_

### Unsecured SQL

Methods using SQL are provided, but not recommended and are to be used
with care. Unlike object query methods, SQL is implementation specific,
but more importantly, the Skyve platform cannot assert automatic
customer scoping and other platform features in unsecured SQL.

## Injection

Skyve supports injection of the following resources (using Contexts and Dependency Injection - CDI) into any into actions, bizlets, extension classes etc. This is useful when writing tests and you would like to mock the behaviour of one of the Skyve classes, or you have an alternate implementation to inject.

* Customer
* Persistence
* Reporting
* Repository
* Stash
* User 

Injection will survive serialisation.

Resources can be injected into a class like below: 

```java
  @Inject
  private transient Reporting reportService;
```

## Reporting

Developers can work with Freemarker and Jasper reports by obtaining an instance of the reporting service via `EXT`, or via [injection](#injection).

```java
  EXT.getReporting()
```

The reporting service provides many helper methods for working with reports and attachments. Some of the commonly used methods are:

Method(s)        | Description/Usage
---------------- | ------------------
`createFreemarkerBeanReportPDF` | Creates a new Freemarker PDF report and writes the file to disk ready to be processed further (e.g. DownloadAction, sent via email).
`getMailAttachmentFromJasperReport` | Produces a PDF from a Jasper report ready to include as a Mail Attachment in an email.
`runJasperBeanReport`, `runJasperSQLReport`, `runJasperReport` | Run a Jasper report


## Tag Manager

Developers can work with Tags by using EXT to obtain an instace of the tag manager.

```java
  EXT.getTagManager()
```

Once you have the tag manager, the following methods can be accessed:

  Methods       | Description/Usage
  ------------- | ------------------
  `clearTag()`, `createTag()`, `deleteTag()`, `getTagId()`, `getTags()`, `iterateTagged()`,` tag()`, `untag()` | Developer access to Skyve's Tag function, allowing the developer to create methods which respond to the user selection.

## Time

The `Time` utility class provides some frequently used helper methods for working with dates and times. A sample of common ones are listed below, there are many more to explore.

Method(s)        | Description/Usage
---------------- | ------------------
`addHours`, `addDays`, `addMonths` | Modifies the time component of an existing date.
`asDateOnly` | Converts a Java `LocalDate` to a Skyve `DateOnly`.
`getFinancialYear`, `getFinancialYearString` | Returns the Financial Year in which the given date exists (where financial year begins July 1).
`min`, `max` | Returns the earliest or latest date from a collection of dates
`numberOfDaysBetween` | Returns the number of days between two dates.
`withDate` | Creates a new `DateOnly` with the specified day, month, year.

Working with time is as follows: 

```java
import org.skyve.util.Time;

  // create a new date a week from today
  Time.addDaysToNew(new DateOnly(), 7);
```

**[⬆ back to top](#utility-classes)**

---
**Next [Common Patterns](./../_pages/common-patterns.md)**<br>
**Previous [Jobs](./../_pages/jobs.md)**
