## Documents

### Contents

* [Chapter 1: Architectural Overview](../README.md)
* [Chapter 2: Concepts](./../chapters/concepts.md)
* [Chapter 3: Identifying the Skyve Version](./../chapters/concepts.md)
* [Chapter 4: Security, Persistence and Access control](./../chapters/security-persistence-and-access-control.md)
* [Chapter 5: Exception Handling](./../chapters/exception-handling.md)
* [Chapter 6: Customers](#customers)
* [Chapter 7: Modules](#modules)
* **[Chapter 8: Documents](#documents)**
  * [Document.xml](#document.xml)
  * [Document.xml Sections](#document.xml-sections)
    * [Metadata](#metadata)
    * [BizKey](#bizkey)
    * [Attributes](#attributes)
    * [Attribute Types](#attribute-types)
    * [Conditions](#conditions)
    * [Constraints](#constraints)
    * [Documentation](#documentation)
  * [Overriding Documents](#overriding-documents)
  * [Database Persistence of Relationships and Key Constraints](#database-persistence-of-relationships-and-key-constraints)
    * [Database Indexes](#database-indexes)
  * [Java Classes](#java-classes)
  * [Bean](#bean-1)
  * [Persistent Bean](#persistent-bean)
  * [Persistence and Locking](#persistence-and-locking)

Skyve uses the term document to indicate the business-focused nature of
application objects.

In an office environment, users interact with paper or electronic
documents and are familiar with a documents being self-contained
artefacts with different types of content, often created using a
template.

Skyve documents are the core components of Skyve applications. All user
application data is contained within documents.

![Figure 32](media/image54.png "Figure 32 Document package organisation for the Invoice document")

_Figure 32 - Document package organisation for the Invoice document_

Within the application file structure, each document is assigned a
package.

The document package includes declarations of actions, reports, views
and the associated *Bizlet* file. The *Bizlet* file contains
document-specific behaviours including overrides of default action
behaviours and document bean lifecycle events (e.g. *newInstance*,
*preSave, etc.*).

Other java class files can be located within the document package if
required.

### Document.xml

Inside the document package, the *document.xml* file defines aspects of
a document, including:

-   document metadata (name, description, aliases),
-   *bizKey* (business key),
-   attributes (fields & references),
-   conditions,
-   constraints, and
-   documentation (doc).

Documents may be persistent (persisted in a database) or transient
(memory only).

If a document exists as a child of another document and should not be
orphaned, the *document.xml* will nominate the parent document.

![Figure 33](media/image55.png "Figure 33 Example of a document declaration")

_Figure 33 - Example of a document declaration_

### Document.xml Sections

#### Metadata

Each document.xml includes the following metadata:

  Metadata         | Description
  ---------------- | ------------
  name             | The unique name of the document within the module context.<br>This name defines the Java class name and how the document is referred to within developer code, e.g. “Contact”.<br>Because documents are implemented as Java interface classes, document names are Titlecase.
  persistentName   | The name of the database table in which instances of the document will be persisted as tuples.<br>By convention, persistentNames are prefixed for each module, e.g. “ADM\_Contact”.
  singularAlias    | The name of a document instance as used by users of the application, e.g. “Weekly Timesheet”.
  pluralAlias      | The plural form of the singularAlias, so that a collection of document instances can be referred to in a grammatically correct way, e.g. “Timesheets”.
  shortDescription | A brief description of the document suitable for tool-tip help or similar.
  parentDocument   | The name of the document which is the parent of this document (if applicable), e.g. if the document is TimesheetLine, the value of parentDocument would be Timesheet.

_Table 4 - Document.xml sections_

#### BizKey

The *bizKey* section contains code (or call to a method in a *Bizlet*
class) which returns a Java String representation of the document
instance.

When the class is generated, the code included in the *bizKey* section
is placed verbatim in the generated implementation (*Impl*) class in the
*bizKey* method. This means that the bizKey code must be compilable
within the context of that class.

If the *bizKey* section contains invalid or uncompilable code, the
domain *Impl* class will not compile.

An example *bizKey* for a person Contact document might be:

```xml
<bizKey>
  <![CDATA[return getName();]]>
</bizKey>
```

_Figure 34 - Example bizKey definition_

In this example, method *getName*() (with no import) is a generated
method returning the value of the name attribute. Because *getName()* is
an implicit method (*name* is an attribute of the document), the
generated *Impl* class will already contain the correct import.

#### Attributes

Documents may have any number of attributes which can be scalar (simple
value) or complex (references to other documents, content etc).

The order in which attributes are defined in the *document.xml* implies
a default ordering for lists and views.

By default, all attributes are not required but are persisted, unless
otherwise indicated by the developer.

Attributes must have, as minimum, a name (the logical name as used by
the developer) and *displayName* (the business name as used by
application users).

Attribute declarations may include a *defaultWidget* declaration which
provides the platform with default representation information. If no
*defaultWidget* is supplied, Skyve will infer a widget from the
attribute’s type. The developer can also override a *defaultWidget* by
specifying a widget in a view definition.

If an attribute includes a *shortDescription*, this will be rendered as
tool-tip help button in the detail view. The *shortDescription* can
contain simple markup as it will be rendered as HTML.

#### Attribute Types

Developers should note that database specific implementations of each
type are defined by Hibernate™ mapping settings and not by the Skyve
specification.

The following attribute types are available:

  Attribute Type | Description | Default Widget | Example
  -------------- | ----------- | -------------- | -------
  association    | A reference to another document.<br>Associations may be either an aggregation or composition and must include the name of the document which is the subject of the association.<br>Associations may optionally include queryName – the name of a metadata query defined in the *module.xml* which provides eligible document instances.<br>Associations of type composition will cascade delete. | lookup | ![](media/image57.png)<br>In this example, the document has a reference to the Contact document called Contact.<br>If the association above was type composition (rather than aggregation), deletion of the document would cascade and delete referenced Contacts.<br>Note that specifying a *queryName* is optional. If no *queryName* is specified, Skyve will supply the default or generic inferred query for the associated document.
  boolean         | True or False<br>If a boolean attribute is not marked as required, the value may also be null. | checkBox | ![](media/image58.png)<br>In this example, the attribute is not required. Non-requiredness means that null is a valid value for the attribute.
  collection      | An orderable collection of references to another document.<br>Collections must specify the *documentName* – the name of the document which is the subject of the collection.<br>Collections may be either *child*, *aggregation* or *composition* type.<br>*Child* collections belong to the document and cascade deletion will occur. Members of *child* collections have a *parent* reference automatically created and assigned. By default, interactions with *child* members will occur in modal windows and within the same transaction as interactions with the *parent*.<br>Collections must have a *minCardinality* (which can be 0) and may optionally have a *maxCardinality*.<br>Collections may be ordered, either implicitly by the user (drag and drop within a grid) or specifically by the developer (using the order by clause).<br>Collections may specify a uniqueness constraint within the context of the *parent*’s collection (rather than within the customer’s scope).<br>Aggregation and composition collections may specify a *queryName* unless the document’s default query is to be used to define eligible document instances. | dataGrid (child, composition)<br>OR<br>dataGrid with a row containing a lookup (aggregation) | ![](media/image59.png)<br>In this example, the document has a collection of children called *Interactions*. Since minCardinality is 0, the document will be valid even if no children exist. The collection will be automatically reordered as defined by the ordering clause (*interactionDate*, then *interactionTime*, then *interactionType*) whenever the bean is saved.<br>All interactions will have a reference to the document called *parent*.<br>If the document is deleted, all children will also be deleted.<br>![](media/image60.png)<br>In this example, the collection is ordered, meaning that the user can re-order the collection by drag-drop in the view. The ordering performed by the user will be preserved when the bean is saved.<br>This example includes a uniqueness constraint applied within the context of the collection to prevent the same ContactCategory being added twice to the same collection.<br>Because the collection is of type *aggregation* (rather than *composition*) deleting the document will not cause cascade delete of the ContactCategory members of the collection.
  colour         | A colour. | colourPicker | ![](media/image61.png)<br>In this example no *shortDescripiton* is declared and as such no tool-tip help icon will be displayed next to widgets bound to this attribute.
  content        | Complex objects like movies, sound, word processing documents, spreadsheets, etc.<br>Content objects are automatically indexed by the textual indexer. | contentLink | ![](media/image62.png)
  date           | A calendar date (year, month, day).<br>By default, Skyve will provide a calendar widget for selection, but the date can be modified as text.<br>Note that if a converter has been declared, the representation within the *textField* component will reflect that conversion. | textField | ![](media/image63.png)
  dateTime       | Date with time (hours, minutes).<br>By default, Skyve will provide a calendar with time widget for selection, but the date and time can be modified as text.<br>Note that if a converter has been declared, the representation within the *textField* component will reflect that conversion. | textField | ![](media/image64.png)
  decimal2       | Decimal number rounded to 2 decimal places, commonly used for currency and percentages.<br>Note that if a converter has been declared, the representation within the *textField* component will reflect that conversion. | textField | ![](media/image65.png)<br>In this example, the converter DollarsAndCents is automatically applied wherever this attribute is displayed.
  decimal5       | Decimal number rounded to 5 decimal places.<br>Note that if a converter has been declared, the representation within the *textField* component will reflect that conversion. | textField | ![](media/image66.png)
  decimal10      | Decimal number rounded to 10 decimal places.<br>Note that if a converter has been declared, the representation within the *textField* component will reflect that conversion. | textField | ![](media/image67.png)
  enum           | An enumeration is a text field which is constrained to be one of a set of values.<br>If enumerations are not marked required, they may be set to null.<br>Each enumeration value must have a code, the value which is persisted when this value is selected.<br>Values may optionally also have a description, which is how the value is displayed to the user, and a name, which is how the value is referred to in developer code and metadata. If description or name is not supplied, the code will be used.<br>Value descriptions may contain spaces while names must be compilable as Java identifiers.<br>The value stored in the persistence mechanism is the code. | combo | ![](media/image68.png)<br>In this example, an enumeration will, by default, be represented as a simple combo box, with the four nominated descriptions in the combo list.<br>Because descriptions have been supplied, the codes will never be seen by the user, but will be persisted in the database.<br>Because name attributes have not been supplied for the values, the domain class for the document will include a Java enumeration with elements named as valid Java identifiers based on the description values, as shown:<br>![](media/image69.png)
  integer        | An integer between the values of 2\^31-1 and -2\^31-1.<br>Note that if a converter has been declared, the representation within the *textField* component will reflect that conversion. | textField | ![](media/image70.png)<br>In this example the attribute has a *shortDescription* containing markup to provide expansive formatted tool-tip help.<br>The defaultValue setting in this case will yield a default value in the generated domain class as follows:<br>![](media/image71.png)
  longInteger    | A large integer.<br>Note that if a converter has been declared, the representation within the *textField* component will reflect that conversion. | textField | ![](media/image72.png)
  markup         | Text with embedded markup tags.<br>Markup is the same as a *memo* where the textual indexer knows it will be HTML and will ignore HTML tags. | richText | ![](media/image73.png)<br>In this example the length will be applied and the user will be prevented from entering more than 5000 characters in the client (on keyPress event).<br>If the developer makes an error in code and attempts to set a longer value, an exception will be thrown when the code attempts to persist the bean.<br>The markup tags are included in the character count (but won’t be indexed).
  memo           | Generic text values which are intended for large or formatted text values.<br>Memo attributes are automatically indexed for textual searching. | textArea | ![](media/image74.png)
  text           | Generic text values.<br>Text attributes can be marked as indexed either for textual searching, or as a database index.<br>Text attributes may specify a value domain either constant, variant or dynamic - loosely typed value lists generated by methods in the document *Bizlet*. | textField<br>OR<br>combo if a domain is specified | ![](media/image75.png)<br>In this example the method attribute is textually indexed.<br>Normally constant domains are implemented as enum type attributes, but in this case, the developer will be responsible for generating the domain value set (by overriding the `getConstantDomainValues()` in the *Bizlet*. By default the attribute will be displayed as a simple combo with the generated value set included in the combo list to constrain users to that value set.<br>If the developer sets a value which is not returned by the `getConstantDomainValues()` method (i.e. not in the domain value set), the bean validation will throw an exception to prevent the value from being persisted.
  time           | Time (hours, minutes, seconds).<br>Note that if a converter has been declared, the representation within the *textField* component will reflect that conversion. | textField | ![](media/image76.png)
  timestamp      | Date with time (hours, minutes, seconds).<br>Note that if a converter has been declared, the representation within the *textField* component will reflect that conversion. | textField | ![](media/image77.png)

_Table 5 - Document attribute types_

#### Conditions

Document conditions are code snippets which return a Java boolean value,
and which can be used by view definitions.

By restricting client-side view conditions to server-side compiled code
rather than client-side Javascript (or other client script), the risks
of developer bugs is significantly reduced. Only the result of the
condition is passed to the client, so the chance of an invalid or
unintended result is minimised. This also means that client interactions
can have access to the results of the full breadth of the server-side
code base, utilities and libraries, while all code is maintained in one
central location. The additional benefit is that all application code is
in one language.

```xml
<condition name="personType">
  <![CDATA[ContactType.person.equals(getContactType())]]>
</condition>
```
_Although available to client-side views, Conditions can take advantage of strongly typed compiled code._

_Figure 35 - Example condition definition_

Conditions become methods of the document *Impl* classes when generated.
The code within the condition definition is placed verbatim in the
method allowing condition methods to be used in developer server-side
code (including *Bizlet* code).

For each condition declaration Skyve generates pairs of condition
methods (both positive and negative methods are generated).

```java
@Override
public boolean isPersonType() {
  return (ContactType.person.equals(getContactType()));
}

@Override
public boolean isNotPersonType() {
  return (ContactType.person.equals(getContactType()));
}
```
_Generated condition method pairs._

_Figure 36 - Resulting generated methods in the document Impl class_

#### Constraints

Uniqueness constraints can be defined within the *document.xml*.

Constraints are checked using row locking (where available, e.g. using
the *select for update* syntax or the update hint in MS SQL Server).

Constraints can combine any number of attribute references (refs).

```xml
<constraint name="uniqueEmployeeCode">
  <message>This code {employeeCode} is already used.</message>
  <fieldReference>
    <ref>employeeCode</ref>
  </fieldReference>
</constraint>
```
_The constraint message is displayed to the user when the constraint is violated, and may contain document bindings._

_Figure 37 - Example definition of a document constraint_

#### Documentation

The *document.xml* includes a `<doc>` tag to allow document
specific documentation to be included with the document definition.

```xml
<doc>
  <![CDATA[A Configuration document contains all of the configuration the details for the creation of documentation using Doctor.]]>
</doc>
```

_Figure 38 - Example doc tag for a document_

### Overriding Documents

Documents may be overridden for customers, to allow customised variants
of the document definition including:

-   attribute names and descriptions,
-   attribute types and sizes,
-   attribute converters,
-   default widgets,
-   conditional logic,
-   relationships and constraint particulars, and
-   views, actions, reports and *Bizlet* methods.

To override a document, view, action, report or *Bizlet* method, place
the overriding artefact into the customer package, mirroring the folder
structure of the originating artefact.

When overriding *Bizlet* methods, only the methods being overridden
should occur in the overriding *Bizlet* class (placed in the customer
package).

![](media/image83.png)

_Figure 39 - Example document override_

The example shown in Figure 39 demonstrates how a customer override is
achieved.

In this example the customer *pgibsa* has the Contact *document.xml* and
edit view overridden within the *admin* module. Note that only the
overridden artefacts are included in the override. All other aspects of
the module will respect the generic functionality declared in the module
package (rather than the customer package).

Note also that the implementation (*Impl*) class for Contact is also
overridden however this override is generated automatically by Skyve
using the *generateDomain* ant task.

### Database Persistence of Relationships and Key Constraints

To ensure referential integrity, Skyve (via Hibernate™) persists foreign
key constraints in the data tier, as follows:

  Relationship           | Description | Logical Types
  ---------------------- | ----------- | -------------
  aggregation association | When an aggregated association is declared from A to B, the database table for document A will have a UUID field created named according to the name declared on the association in document A, suffixed \_id.<br>For example, if Invoice has an aggregated association to Employee, typically the name of the association might also be employee (camelCase). If the persistent name of the Invoice document (i.e. the name of the database table) is `INV_Invoice`, then this table will have a field called employee\_id which holds the UUID key for the Employee record.<br>In this example, Invoice.employee would be a reference to Employee.bizId and would be stored in the database as Invoice.employee\_id.<br>Hibernate will ensure that a Foreign Key constraint is created on `INV_Invoice` table to ensure referential integrity and the Employee record will not be able to be deleted until the relationship is nulled or changed or the corresponding Invoice record is deleted. | Many-to-one
  composition association | As for aggregation association except with cascade delete.<br>For example, if an Employee is a Contact, then Employee will have a composition association to Contact. If the Employee is deleted, the referred Contact will be deleted automatically (i.e. cascade). | Many-to-one
  aggregation collection  | When an aggregation collection of document B is declared on document A, a directional joining table is created to express the relationship.<br>The joining table will have two fields: `owner_id` and `element_id` with foreign key constraints such that `owner_id` references document A and `element_id` references document B (i.e. where Bs are elements of A’s collection).<br>The joining table is named according to the direction of the relationship using the persistent names of both documents.<br>For example, a Depot may have a collection of Taxi. If the persistent names of the documents are TRAN\_Depot and TRAN\_Taxi, the joining table will be named TRAN\_Depot\_Taxi and the owner\_id will reference TRAN\_Depot and the element\_id will reference TRAN\_Taxi.<br>The joining table is directional because if another relationship is declared from document B to document A (i.e. the other way around) another joining table is created such that the owner is document B and elements are document A.<br>When an element of the collection is deleted or removed, the tuple in the joining table is deleted automatically by Skyve.<br>When the owner of the relationship is deleted, the tuple in the joining table is also deleted automatically. | Many-to-many
  composition collection  | As for aggregation collection except with cascade delete.<br>For example, if Depot has a collection of Taxi which is of type composition, then deletion of the Depot will result in cascade deletion of the associated Taxis and therefore also the tuples in the joining table will be deleted. | Many-to-many
  parent-child collection | When a parent child relationship is declared from A to B, document B will have a parent property (with corresponding .getParent() and .setParent() methods) and the table for document B will have a `parent_id` field created.<br>For example, if the Invoice document is declared as parent to InvoiceLine and the persistent name of the InvoiceLine document is `INV_Invoice`Line then this table will have a column `parent_id` which holds the UUID key of the parent Invoice. | One-to-many

#### Database Indexes

Hibernate™ ensures that all primary keys are indexed unique and that all
tables have a primary key defined.

Skyve platform columns *bizCustomer* (the customer context in which the
data was created), *bizKey* (the Java String representation of the bean)
and *bizUserId* (the user who created the data) are also indexed.

### Java Classes

Skyve provides a *generateDomain* ant task which generates java classes
as required for each document. These classes are located in the module
*domain* folder.

For each document a generic document interface class is generated as
well as an implementation class (*Impl*). *Bizlet* code uses these
interfaces so compile errors are generated if developer code is
inconsistent with the application specification metadata.

An *Impl* class is also generated for each customer-override of the
document (located in the customer’s *module.document* override package).

### Bean

Each document class extends the *Bean* interface.

Beans include a number of attributes to support Skyve interactions,
specifically:

  Attribute      | Description                                                                             | Logical Type
  -------------- | --------------------------------------------------------------------------------------- | ------------------------------------
  bizId          | A guaranteed unique primary identifier for the instance of the bean                     | UUID (universally unique identifier)
  bizCustomer    | The name of the customer/tenant the instance belongs to.                                | String
  bizDataGroupId | The id of the datagroup context which the bean instance belongs to.                     | UUID
  bizUserId      | The id of the user who created the bean.                                                | UUID
  bizModule      | The name of the Skyve module in which the instance exists.                              | String
  bizDocument    | The name of the Skyve document which this bean is an instance of.                       | String
  bizKey         | A simple scalar representation of the bean.                                             | String
  bizOrdinal     | The ordinal position of the bean if it exists as a member of a collection.              | Integer
  persisted      | Whether the bean has been persisted. For transient beans this will always return false. | Boolean
  created        | Whether the bean has been validly created.                                              | Boolean

_Table 6 - Bean attributes_

Each generated document interface class implements either
*PersistentBean* or *TransientBean* and represents the intersection of
all customer-overrides.

### Persistent Bean

In addition to the basic attributes required for beans,
*PersistentBeans* contain attributes required for persistence (including
attributes to allow record locking and contention management) so that
any persistent document created within Skyve has all aspects required
for secure multi-user interactions.

  Attribute      | Description | Logical Type
  -------------- | ----------- | ------------
  bizLock        | Platform and database independent optimistic locking information consisting of a timestamp and username.<br>Comparing lock values allows the platform not only to determine *IF* the bean has been altered by another conversation since it was loaded into the current conversation, but also by *WHOM* and *WHEN*. | OptimisticLock
  bizVersion     | An incrementing integer that is used to detect if changes have been made by another conversation. | Integer
  bizFlagComment | Users can add to comments the document instance as part of generic list functionality called Flags. | String
  bizTagged      | Indicates whether a bean is a part of the currently selected tag within the context of the conversation. While relevant for persistent beans, the value of *bizTagged* is not itself persisted. | Boolean

_Table 7 - PersistentBean attributes_

### Persistence and Locking

Persistent Skyve documents are persisted as tables in the data tier, and
document instances as data rows or tuples. Document attributes
containing content (i.e. file attachments) contain only a reference to
the actual content (which is managed within the file system).

Skyve tables always have columns for the *Bean* and *PersistentBean*
attributes defined above, in addition to those defined by the developer
in the *document.xml*. This guarantees that every table can
automatically be managed by Skyve in a secure, scalable and
transactional way.

While some DBMS implement their own proprietary contention and record
locking mechanisms, Skyve implements these in a platform and
database-independent way.

Pessimistic locking happens in the data tier (page, row, table DB locks) - these are for each database connection and handled by the DBMS.

Optimistic locking happens during user wait time (also called long
transactions) and relies on a piece of data which changes each time
someone changes a tuple. Optimistic locking is required because holding
database connections open while users interact with the system would
severely impact on performance and infrastructure requirements for
multi-user systems.

**[⬆ back to top](#contents)**

---
**Next [Chapter 9: Converters](./../chapters/converters.md)**  
**Previous [Chapter 7: Modules](./../chapters/modules.md)**
