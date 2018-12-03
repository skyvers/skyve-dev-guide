---
title: "Utility Classes"
permalink: /utility-classes/
excerpt: "Utility Classes"
toc: true
sidebar:
  title: "Index"
  nav: docs
---

## Utility Classes

Skyve provides the following utility classes:

  Class/Library | Description
  ------------- | -----------
  Util          | Bean-level generic utilities, including generic bean methods e.g. *cloneToTransientBySerialisation*().
  Binder        | Provides methods for generic bean binding manipulations, including: <br><br><li>generic get() and set() methods for a bean with nominated binding, <li>*createCompoundBinding*() for correctly constructing compound bindings (i.e. across document references), and <li>read and interpret the nature of bean properties.
  ModulesUtil   |  Contains a number of business-focused methods and enumerations for cross-module functionality, including: <br><br><li>*getNextDocumentNumber*() to create unique formatted serial document identifiers, <li> methods for working with business periods (month, quarter, half-year etc.) and frequency, <li> *currentAdminUser*() which identifies the current conversation user as an admin module user, <li> coalesce methods, and <li> basic Tax and loan calculation methods.
  JobScheduler  |  Provides methods to schedule declared jobs, e.g. *runOneShotJob*().
  Persistence   |  Provides access to interact directly with the singleton persistence mechanism (detailed below).
  DocumentQuery | Provides methods for constructing object queries without resorting to constructing OQL or SQL strings.

_Utility classes and libraries_

### Persistence

Skyve's Persistence mechanism is described in detail in
* [Skyve Persistence Mechanisms](_pages/skyve-persistence-mechanisms.md)

The following is specific to the use of the Persistence utility class.

The *Persistence* class provides access to persistence-specific
functionality which may be required by developers, while ensuring that
developer code will comply with security and threading mandates.

Key persistence methods are:

  Method           | Description
  ---------------- | ------------------
  get              | Instantiates a *Persistence* object
  getUser          | Gets the current conversation *metadata.user*
  begin, rollback, commit |   Control the state of the current transaction
  evictAllCached, <br><br>evitCached | Evict beans from cached memory.<br><br>These methods are useful where code interactions with *persistentBeans* may be in contention with default actions resulting from user activity (like pressing the Save button). <br><br>For example, if a user executes an action which impacts on the bean displayed in the view, and modified beans are not evicted, the user's action will be in contention with the action. In this case, the user's subsequent attempt to Save the bean will be met with a message stating that the current bean has already been modified by the user and can't be saved. <br><br>Evicting cached beans at the conclusion of the action will avoid such a contention issue.
  delete | deletes a bean instance
  executeDML, <br><br>executeInsecureSQLDML | Execute persistence data manipulation language queries, either object DML or Insecure SQL DML.
  flush | pushes all pending DML statements to the database (without _commit_)
  upsertBeanTuple, <br><br>upsertCollectionTuples | Persists values only within the top-most level of the bean structure. <br><br>During an *upsert*, no bean validation is performed, and reference ID values are persisted without traversing into the related bean.
  retrieve(ProjectionQuery), <br><br>retrieveInsecureSQL |  Retrieve a bean or collection of beans using a project query or via Insecure SQL
  retrieve(Bean)    | Retrieve the current bean as it was last persisted.

_Key methods of the Persistence utility class_

#### Insecure SQL

Methods using SQL are provided, but not recommended and are to be used
with care. Unlike object query methods, SQL is implementation specific,
but more importantly, the Skyve platform cannot assert automatic
customer scoping and other platform features in insecure SQL.

### DocumentQuery

*DocumentQuery* extends *ProjectionQuery* and provides the ability to
retrieve persisted beans in a type-safe and secure way, without building
SQL or OQL Strings.

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

In the example provided in the above example, the DocumentQuery is used to
retrieve all VineyardChange beans. The beans are returned in a typed
List and document permissions and scoping rules are automatically
enforced by Skyve.

The use of DocumentFilter allows for correct enforcement of types at
compile-time to reduce the possibility of errors arising from implicit
type conversion which may arise if SQL strings were used.

**[⬆ back to top](#contents)**

---
**Next [Common Patterns](./../_pages/common-patterns.md)**<br>
**Previous [Jobs](./../_pages/jobs.md)**