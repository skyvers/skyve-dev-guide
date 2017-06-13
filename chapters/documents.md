Documents {#documents-1 .ChapterHeading}
=========

Skyve uses the term document to indicate the business-focused nature of
application objects.

In an office environment, users interact with paper or electronic
documents and are familiar with a documents being self-contained
artefacts with different types of content, often created using a
template.

Skyve documents are the core components of Skyve applications. All user
application data is contained within documents.

![](media/image54.emf){width="3.0909722222222222in" height="1.4548611111111112in"} {#section-14 .Picture}
----------------------------------------------------------------------------------

Figure 32 Document package organisation for the Invoice document

Within the application file structure, each document is assigned a
package.

The document package includes declarations of actions, reports, views
and the associated *Bizlet* file. The *Bizlet* file contains
document-specific behaviours including overrides of default action
behaviours and document bean lifecycle events (e.g. *newInstance*,
*preSave, etc.*).

Other java class files can be located within the document package if
required.

### Document.xml {#document.xml .Sectionheading}

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

![](media/image55.png){width="6.530555555555556in"
height="5.288194444444445in"}

Figure 33 Example of a document declaration

Document.xml Sections {#document.xml-sections .Chaptersubheading}
---------------------

### Metadata {#metadata .Sectionheading}

Each document.xml includes the following metadata:

  ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  Metadata           Description
  ------------------ -----------------------------------------------------------------------------------------------------------------------------------------------------------------------
  name               The unique name of the document within the module context.

                     This name defines the Java class name and how the document is referred to within developer code, e.g. “Contact”.

                     Because documents are implemented as Java interface classes, document names are Titlecase.

  persistentName     The name of the database table in which instances of the document will be persisted as tuples.

                     By convention, persistentNames are prefixed for each module, e.g. “ADM\_Contact”.

  singularAlias      The name of a document instance as used by users of the application, e.g. “Weekly Timesheet”.

  pluralAlias        The plural form of the singularAlias, so that a collection of document instances can be referred to in a grammatically correct way, e.g. “Timesheets”.

  shortDescription   A brief description of the document suitable for tool-tip help or similar.

  parentDocument     The name of the document which is the parent of this document (if applicable), e.g. if the document is TimesheetLine, the value of parentDocument would be Timesheet.
  ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

Table 4 Document.xml sections

### BizKey {#bizkey .Sectionheading}

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

![](media/image56.emf){width="2.5in" height="0.42430555555555555in"}

Figure 34 Example bizKey defintion

In this example, method *getName*() (with no import) is a generated
method returning the value of the name attribute. Because *getName()* is
an implicit method (*name* is an attribute of the document), the
generated *Impl* class will already contain the correct import.

### Attributes {#attributes .Sectionheading}

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

### Attribute Types {#attribute-types .Sectionheading}

Developers should note that database specific implementations of each
type are defined by Hibernate™ mapping settings and not by the Skyve
specification.

The following attribute types are available:

  ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  Attribute Type   Description                                                                                                                                                                                                                                                                                                             Default Widget                                          Example
  ---------------- ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- ------------------------------------------------------- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  association      A reference to another document.                                                                                                                                                                                                                                                                                        lookup                                                  In this example, the document has a reference to the Contact document called Contact.

                   Associations may be either an aggregation or composition and must include the name of the document which is the subject of the association.                                                                                                                                                                                                                                     If the association above was type composition (rather than aggregation), deletion of the document would cascade and delete referenced Contacts.

                   Associations may optionally include queryName – the name of a metadata query defined in the *module.xml* which provides eligible document instances.                                                                                                                                                                                                                            Note that specifying a *queryName* is optional. If no *queryName* is specified, Skyve will supply the default or generic inferred query for the associated document.

                   Associations of type composition will cascade delete.

  boolean          True or False                                                                                                                                                                                                                                                                                                           checkBox                                                ![](media/image58.png){width="4.954861111111111in" height="0.5305555555555556in"}

                   If a boolean attribute is not marked as required, the value may also be null.                                                                                                                                                                                                                                                                                                   In this example, the attribute is not required. Non-requiredness means that null is a valid value for the attribute.

  collection       An orderable collection of references to another document.                                                                                                                                                                                                                                                              dataGrid (child, composition)                           ![](media/image59.png){width="3.60625in" height="1.3180555555555555in"}

                   Collections must specify the *documentName* – the name of the document which is the subject of the collection.                                                                                                                                                                                                          OR                                                      In this example, the document has a collection of children called *Interactions*. Since minCardinality is 0, the document will be valid even if no children exist. The collection will be automatically reordered as defined by the ordering clause (*interactionDate*, then *interactionTime*, then *interactionType*) whenever the bean is saved.

                   Collections may be either *child*, *aggregation* or *composition* type.                                                                                                                                                                                                                                                 dataGrid with a row containing a lookup (aggregation)   All interactions will have a reference to the document called *parent*.

                   *Child* collections belong to the document and cascade deletion will occur. Members of *child* collections have a *parent* reference automatically created and assigned. By default, interactions with *child* members will occur in modal windows and within the same transaction as interactions with the *parent*.                                                           If the document is deleted, all children will also be deleted.

                   Collections must have a *minCardinality* (which can be 0) and may optionally have a *maxCardinality*.                                                                                                                                                                                                                                                                           ![](media/image60.png){width="5.10625in" height="1.60625in"}

                   Collections may be ordered, either implicitly by the user (drag and drop within a grid) or specifically by the developer (using the order by clause).                                                                                                                                                                                                                           In this example, the collection is ordered, meaning that the user can re-order the collection by drag-drop in the view. The ordering performed by the user will be preserved when the bean is saved.

                   Collections may specify a uniqueness constraint within the context of the *parent*’s collection (rather than within the customer’s scope).                                                                                                                                                                                                                                      This example includes a uniqueness constraint applied within the context of the collection to prevent the same ContactCategory being added twice to the same collection.

                   Aggregation and composition collections may specify a *queryName* unless the document’s default query is to be used to define eligible document instances.                                                                                                                                                                                                                      Because the collection is of type *aggregation* (rather than *composition*) deleting the document will not cause cascade delete of the ContactCategory members of the collection.

  colour           A colour.                                                                                                                                                                                                                                                                                                               colourPicker                                            ![](media/image61.png){width="3.10625in" height="0.39375in"}

                                                                                                                                                                                                                                                                                                                                                                                                   In this example no *shortDescripiton* is declared and as such no tool-tip help icon will be displayed next to widgets bound to this attribute.

  content          Complex objects like movies, sound, word processing documents, spreadsheets, etc.                                                                                                                                                                                                                                       contentLink                                             ![](media/image62.png){width="4.636111111111111in" height="0.5305555555555556in"}

                   Content objects are automatically indexed by the textual indexer.

  date             A calendar date (year, month, day).                                                                                                                                                                                                                                                                                     textField                                               ![](media/image63.png){width="2.8784722222222223in" height="0.40902777777777777in"}

                   By default, Skyve will provide a calendar widget for selection, but the date can be modified as text.

                   Note that if a converter has been declared, the representation within the *textField* component will reflect that conversion.

  dateTime         Date with time (hours, minutes).                                                                                                                                                                                                                                                                                        textField                                               ![](media/image64.png){width="2.636111111111111in" height="0.39375in"}

                   By default, Skyve will provide a calendar with time widget for selection, but the date and time can be modified as text.

                   Note that if a converter has been declared, the representation within the *textField* component will reflect that conversion.

  decimal2         Decimal number rounded to 2 decimal places, commonly used for currency and percentages.                                                                                                                                                                                                                                 textField                                               In this example, the converter DollarsAndCents is automatically applied wherever this attribute is displayed.

                   Note that if a converter has been declared, the representation within the *textField* component will reflect that conversion.

  decimal5         Decimal number rounded to 5 decimal places.                                                                                                                                                                                                                                                                             textField                                               ![](media/image66.png){width="3.3180555555555555in" height="0.5451388888888888in"}

                   Note that if a converter has been declared, the representation within the *textField* component will reflect that conversion.

  decimal10        Decimal number rounded to 10 decimal places.                                                                                                                                                                                                                                                                            textField                                               ![](media/image67.png){width="5.0152777777777775in" height="0.5in"}

                   Note that if a converter has been declared, the representation within the *textField* component will reflect that conversion.

  enum             An enumeration is a text field which is constrained to be one of a set of values.                                                                                                                                                                                                                                       combo                                                   ![](media/image68.png){width="4.1819444444444445in" height="1.2118055555555556in"}

                   If enumerations are not marked required, they may be set to null.                                                                                                                                                                                                                                                                                                               In this example, an enumeration will, by default, be represented as a simple combo box, with the four nominated descriptions in the combo list.

                   Each enumeration value must have a code, the value which is persisted when this value is selected.                                                                                                                                                                                                                                                                              Because descriptions have been supplied, the codes will never be seen by the user, but will be persisted in the database.

                   Values may optionally also have a description, which is how the value is displayed to the user, and a name, which is how the value is referred to in developer code and metadata. If description or name is not supplied, the code will be used.                                                                                                                                Because name attributes have not been supplied for the values, the domain class for the document will include a Java enumeration with elements named as valid Java identifiers based on the description values, as shown:

                   Value descriptions may contain spaces while names must be compilable as Java identifiers.                                                                                                                                                                                                                                                                                       ![](media/image69.png){width="3.7118055555555554in" height="0.7270833333333333in"}

                   The value stored in the persistence mechanism is the code.

  integer          An integer between the values of 2\^31-1 and -2\^31-1.                                                                                                                                                                                                                                                                  textField                                               ![](media/image70.png){width="4.3180555555555555in" height="1.6215277777777777in"}

                   Note that if a converter has been declared, the representation within the *textField* component will reflect that conversion.                                                                                                                                                                                                                                                   In this example the attribute has a *shortDescription* containing markup to provide expansive formatted tool-tip help.

                                                                                                                                                                                                                                                                                                                                                                                                   The defaultValue setting in this case will yield a default value in the generated domain class as follows:

                                                                                                                                                                                                                                                                                                                                                                                                   ![](media/image71.png){width="3.4694444444444446in" height="0.2423611111111111in"}

  longInteger      A large integer.                                                                                                                                                                                                                                                                                                        textField                                               ![](media/image72.png){width="5.469444444444444in" height="0.5756944444444444in"}

                   Note that if a converter has been declared, the representation within the *textField* component will reflect that conversion.

  markup           Text with embedded markup tags.                                                                                                                                                                                                                                                                                         richText                                                ![](media/image73.png){width="3.151388888888889in" height="0.5305555555555556in"}

                   Markup is the same as a *memo* where the textual indexer knows it will be HTML and will ignore HTML tags.                                                                                                                                                                                                                                                                       In this example the length will be applied and the user will be prevented from entering more than 5000 characters in the client (on keyPress event).

                                                                                                                                                                                                                                                                                                                                                                                                   If the developer makes an error in code and attempts to set a longer value, an exception will be thrown when the code attempts to persist the bean.

                                                                                                                                                                                                                                                                                                                                                                                                   The markup tags are included in the character count (but won’t be indexed).

  memo             Generic text values which are intended for large or formatted text values.                                                                                                                                                                                                                                              textArea                                                ![](media/image74.png){width="4.045138888888889in" height="0.6666666666666666in"}

                   Memo attributes are automatically indexed for textual searching.

  text             Generic text values.                                                                                                                                                                                                                                                                                                    textField                                               ![](media/image75.png){width="5.0in" height="1.60625in"}

                   Text attributes can be marked as indexed either for textual searching, or as a database index.                                                                                                                                                                                                                          OR                                                      In this example the method attribute is textually indexed.

                   Text attributes may specify a value domain either constant, variant or dynamic - loosely typed value lists generated by methods in the document *Bizlet*.                                                                                                                                                               combo if a domain is specified                          Normally constant domains are implemented as enum type attributes, but in this case, the developer will be responsible for generating the domain value set (by overriding the *getConstantDomainValues*() in the *Bizlet*. By default the attribute will be displayed as a simple combo with the generated value set included in the combo list to constrain users to that value set.

                                                                                                                                                                                                                                                                                                                                                                                                   If the developer sets a value which is not returned by the *getConstantDomainValues*() method (i.e. not in the domain value set), the bean validation will throw an exception to prevent the value from being persisted.

  time             Time (hours, minutes, seconds).                                                                                                                                                                                                                                                                                         textField                                               ![](media/image76.png){width="3.9243055555555557in" height="0.5305555555555556in"}

                   Note that if a converter has been declared, the representation within the *textField* component will reflect that conversion.

  timestamp        Date with time (hours, minutes, seconds).                                                                                                                                                                                                                                                                               textField                                               ![](media/image77.png){width="4.454861111111111in" height="0.5451388888888888in"}

                   Note that if a converter has been declared, the representation within the *textField* component will reflect that conversion.
  ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

Table 5 Document attribute types

### Conditions {#conditions .Sectionheading}

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

![](media/image79.png){width="3.848611111111111in"
height="0.42430555555555555in"}

Figure 35 Example condition definition

Conditions become methods of the document *Impl* classes when generated.
The code within the condition definition is placed verbatim in the
method allowing condition methods to be used in developer server-side
code (including *Bizlet* code).

For each condition declaration Skyve generates pairs of condition
methods (both positive and negative methods are generated).

![](media/image80.png){width="3.7118055555555554in"
height="1.257638888888889in"}

Figure 36 Resulting generated methods in the document Impl class

### Constraints {#constraints .Sectionheading}

Uniqueness constraints can be defined within the *document.xml*.

Constraints are checked using row locking (where available, e.g. using
the *select for update* syntax or the update hint in MS SQL Server).

Constraints can combine any number of attribute references (refs).

![](media/image81.png){width="4.151388888888889in"
height="0.7729166666666667in"}

Figure 37 Example definition of a document constraint

### Documentation {#documentation-1 .Sectionheading}

The *document.xml* includes a *&lt;doc&gt;* tag to allow document
specific documentation to be included with the document definition.

![](media/image82.png){width="6.969444444444444in"
height="0.6215277777777778in"}

Figure 38 Example doc tag for a document

Overriding Documents {#overriding-documents .Chaptersubheading}
--------------------

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

![](media/image83.png){width="2.3027777777777776in"
height="3.636111111111111in"}

[[]{#_Ref316455062 .anchor}]{#_Ref316455056 .anchor}Figure 39 Example
document override

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

Database Persistence of Relationships and Key Constraints {#database-persistence-of-relationships-and-key-constraints .Chaptersubheading}
---------------------------------------------------------

To ensure referential integrity, Skyve (via Hibernate™) persists foreign
key constraints in the data tier, as follows:

  ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  Relationship              Description                                                                                                                                                                                                                                                                                                                                                  Logical Types
  ------------------------- ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ ---------------
  aggregation association   When an aggregated association is declared from A to B, the database table for document A will have a UUID field created named according to the name declared on the association in document A, suffixed \_id.                                                                                                                                               Many-to-one

                            For example, if Invoice has an aggregated association to Employee, typically the name of the association might also be employee (camelCase). If the persistent name of the Invoice document (i.e. the name of the database table) is INV\_Invoice, then this table will have a field called employee\_id which holds the UUID key for the Employee record.

                            In this example, Invoice.employee would be a reference to Employee.bizId and would be stored in the database as Invoice.employee\_id.

                            Hibernate will ensure that a Foreign Key constraint is created on INV\_Invoice table to ensure referential integrity and the Employee record will not be able to be deleted until the relationship is nulled or changed or the corresponding Invoice record is deleted.

  composition association   As for aggregation association except with cascade delete.                                                                                                                                                                                                                                                                                                   Many-to-one

                            For example, if an Employee is a Contact, then Employee will have a composition association to Contact. If the Employee is deleted, the referred Contact will be deleted automatically (i.e. cascade).

  aggregation collection    When an aggregation collection of document B is declared on document A, a directional joining table is created to express the relationship.                                                                                                                                                                                                                  Many-to-many

                            The joining table will have two fields: owner\_id and element\_id with foreign key constraints such that owner\_id references document A and element\_id references document B (i.e. where Bs are elements of A’s collection).

                            The joining table is named according to the direction of the relationship using the persistent names of both documents.

                            For example, a Depot may have a collection of Taxi. If the persistent names of the documents are TRAN\_Depot and TRAN\_Taxi, the joining table will be named TRAN\_Depot\_Taxi and the owner\_id will reference TRAN\_Depot and the element\_id will reference TRAN\_Taxi.

                            The joining table is directional because if another relationship is declared from document B to document A (i.e. the other way around) another joining table is created such that the owner is document B and elements are document A.

                            When an element of the collection is deleted or removed, the tuple in the joining table is deleted automatically by Skyve.

                            When the owner of the relationship is deleted, the tuple in the joining table is also deleted automatically.

  composition collection    As for aggregation collection except with cascade delete.                                                                                                                                                                                                                                                                                                    Many-to-many

                            For example, if Depot has a collection of Taxi which is of type composition, then deletion of the Depot will result in cascade deletion of the associated Taxis and therefore also the tuples in the joining table will be deleted.

  parent-child collection   When a parent child relationship is declared from A to B, document B will have a parent property (with corresponding .getParent() and .setParent() methods) and the table for document B will have a parent\_id field created.                                                                                                                               One-to-many

                            For example, if the Invoice document is declared as parent to InvoiceLine and the persistent name of the InvoiceLine document is INV\_InvoiceLine then this table will have a column parent\_id which holds the UUID key of the parent Invoice.
  ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

### Database Indexes {#database-indexes .Sectionheading}

Hibernate™ ensures that all primary keys are indexed unique and that all
tables have a primary key defined.

Skyve platform columns *bizCustomer* (the customer context in which the
data was created), *bizKey* (the Java String representation of the bean)
and *bizUserId* (the user who created the data) are also indexed.

Java Classes {#java-classes .Chaptersubheading}
------------

Skyve provides a *generateDomain* ant task which generates java classes
as required for each document. These classes are located in the module
*domain* folder.

For each document a generic document interface class is generated as
well as an implementation class (*Impl*). *Bizlet* code uses these
interfaces so compile errors are generated if developer code is
inconsistent with the application specification metadata.

An *Impl* class is also generated for each customer-override of the
document (located in the customer’s *module.document* override package).

Bean {#bean-1 .Chaptersubheading}
----

Each document class extends the *Bean* interface.

Beans include a number of attributes to support Skyve interactions,
specifically:

  Attribute        Description                                                                               Logical Type
  ---------------- ----------------------------------------------------------------------------------------- --------------------------------------
  bizId            A guaranteed unique primary identifier for the instance of the bean                       UUID (universally unique identifier)
  bizCustomer      The name of the customer/tenant the instance belongs to.                                  String
  bizDataGroupId   The id of the datagroup context which the bean instance belongs to.                       UUID
  bizUserId        The id of the user who created the bean.                                                  UUID
  bizModule        The name of the Skyve module in which the instance exists.                                String
  bizDocument      The name of the Skyve document which this bean is an instance of.                         String
  bizKey           A simple scalar representation of the bean.                                               String
  bizOrdinal       The ordinal position of the bean if it exists as a member of a collection.                Integer
  persisted        Whether the bean has been persisted. For transient beans this will always return false.   Boolean
  created          Whether the bean has been validly created.                                                Boolean

Table 6 Bean attributes

Each generated document interface class implements either
*PersistentBean* or *TransientBean* and represents the intersection of
all customer-overrides.

Persistent Bean {#persistent-bean .Chaptersubheading}
---------------

In addition to the basic attributes required for beans,
*PersistentBeans* contain attributes required for persistence (including
attributes to allow record locking and contention management) so that
any persistent document created within Skyve has all aspects required
for secure multi-user interactions.

  --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  Attribute        Description                                                                                                                                                                                                Logical Type
  ---------------- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- ----------------
  bizLock          Platform and database independent optimistic locking information consisting of a timestamp and username.                                                                                                   OptimisticLock

                   Comparing lock values allows the platform not only to determine *IF* the bean has been altered by another conversation since it was loaded into the current conversation, but also by *WHOM* and *WHEN*.

  bizVersion       An incrementing integer that is used to detect if changes have been made by another conversation.                                                                                                          Integer

  bizFlagComment   Users can add to comments the document instance as part of generic list functionality called Flags.                                                                                                        String

  bizTagged        Indicates whether a bean is a part of the currently selected tag within the context of the conversation. While relevant for persistent beans, the value of *bizTagged* is not itself persisted.            Boolean
  --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

Table 7 PersistentBean attributes

Persistence and Locking {#persistence-and-locking .Chaptersubheading}
-----------------------

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

Pessimistic locking happens in the data tier (page, row, table DB locks)
- these are for each database connection and handled by the DBMS.

Optimistic locking happens during user wait time (also called long
transactions) and relies on a piece of data which changes each time
someone changes a tuple. Optimistic locking is required because holding
database connections open while users interact with the system would
severely impact on performance and infrastructure requirements for
multi-user systems.
