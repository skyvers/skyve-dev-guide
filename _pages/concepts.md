---
title: "Concepts"
permalink: /concepts/
excerpt: "Skyve Enteprise Platform concepts."
toc: true
sidebar:
  title: "Index"
  nav: docs
---
## Concepts

### Rendering Modes
Skyve has two rendering modes to suit different user agents/device types. 
Skyve applications will (by default) automatically switch to use the most suitable
rendering for the user agent or device type.

![Rendering Modes](media/concepts_rendering_modes.png "Rendering Modes")
_Rendering Modes_

The SmartClient AJAX framework rendering mode provides a feature rich user experience, mimicking thick-client
applications. This mode is suitable for office environments and users who regularly use the software
on desktop or laptop devices.

The PrimeFaces rendering mode is a responsive renderer suitable for phones and tablet devices. 
The responsive renderer offers a reduced capability set (compared with the SmartClient rendering mode) 
as a simplified mode suitable for mobile devices and occasional use.

Skyve's powerful routing capability gives developers the ability to override Skyve's default activity 
and decide which rendering options suit their applications best.

### Domain Model
An application domain model can be thought of as a conceptual model
which describes the various entities, attributes, roles, relationships
and constraints that constitute the application.

Skyve applications allow developers to specify the domain model in
human-readable xml metadata files together with small amounts of Java
code. Skyve provides an ant task to generate the Java domain classes;
the implementation of the model. In Skyve applications, the domain
classes are never modified or maintained manually, but are regenerated
each time the domain model metadata is modified.

The aim of Skyve is to allow, as far as possible, the entire domain
model to be declared in metadata rather than code.

### Module and Document

Modules are self-contained reusable segments of application capability.
In Skyve, modules are presented to the user as separate accordion menus.

The document concept corresponds loosely to a business document (e.g. a
timesheet or invoice). Documents generally correspond to rows of data
within the list view. Documents may contain collections of, and
references to, other documents, with each instance persisted as a tuple
in the database.

![Basic UI Layout](media/image8.png "Basic UI layout")
_Basic UI layout_

Both module and document objects are implemented in the Skyve API and
are available to developers. Using the API, developers can create
module-level classes and document-level classes (called *Bizlets*) to
supplement application capability.

### Types

Skyve provides developers with a range of high-level business types
(e.g. *DateOnly*, *DateTime*, *Decimal5*, *Text*, etc.). Each type
implies a user control (or *widget*) which provides the capability for
users to interact with the data (e.g. calendars, sliders, etc.).
Constant enumerations created by the developer are implemented as
*types* within Skyve applications.

The Skyve API enforces strong typing and provides type-specific methods
for data manipulation. Skyve will enforce types at all levels of the
application stack, i.e. the user interface, business and persistence
(data) tiers.

Skyve validates application metadata to ensure types are consistently
and correctly applied throughout the application, while Java compilation
ensures that typing is correctly implemented in developer code.

### List and Edit

Most applications present data in both list and detail views. Skyve
assumes this fact and provides rich features for each type of view.

#### List Views

Typically, users of applications begin with a list view of all document
instances and then select a document instance to edit. This pattern of
behaviour is assumed in Skyve.

Skyve will provide generic/default list and edit views unless specific
definitions are provided in the application metadata by the developer.
This supports rapid prototyping of the domain model and gives the
developer an ability to begin interacting with the application at an
early stage.

List views are based on queries which generally include only key
document attributes for searching and review. Each row of the list
corresponds to a document instance however the list may represent data
from related documents within the document's object hierarchy.

![List view](media/image9.png "List view")
_List view_

List views are provided by a generic list component (called a
*listGrid*) which offers searching, filtering, tagging, exporting,
summary and inline editing features. No developer code is required to
provide the list feature.

Skyve provides flagging and tagging features for all lists.

Flags are small amounts of rich-text attached to a data row - displayed
as tool-tip comments when the document instance is browsed.

Unlike basic multi-select capability in many applications, Skyve's Tag
capability allows users to save the collection of rows and name that
collection for later recall.

Tags are named collections of rows within a specific user context.
Tagging rows provides users with the ability to create and work with
their own groups of records for specific purposes. Tags are
heterogeneous - meaning that a single tag can include different document
types, e.g. a user can create a tag which holds a collection of both
Contacts and Timesheets.

Skyve provides a Tag management tools for set-based operations
intersection, union etc on tags, and the ability to export Tag
selections or import (i.e. load) Tags from external sources.

### Filtering

The List View (*ListGrid*) feature provides two methods for filtering
large data sets:

-   Simple Filter line, and
-   Advanced Filter constructor.

#### Simple Filter line

The simple filter lines provides rapid ability to filter the list of
data accessible from List View. Driven from the underlying metadata
query (or implicit query), columns are defined as either filterable or
not. If a column is defined as filterable, a filter widget will be
available above the column header in the list.

![](media/image10.png)

The simple filter line provides basic filtering capability. Values
entered in the filter line are the basis of an inclusive "Like" filter
applied to the data set.

The list supports any number of filter parameters set in the filter
line. The filter is activated using the filter tool
![](media/image11.png), or by keying &lt;Enter&gt; in any of
the parameter controls, or by refreshing the list, using the refresh
tool ![](media/image12.png) (from the tool bar or context menu).

The filter is cleared by either clearing the values from the filter line
controls, or by using the clear filter tool
![](media/image13.png).

![](media/image14.png)

#### Advanced Filter constructor

The Advanced Filter constructor operates in three styles:

-   Flat,
-   Nested, or
-   Inline.

    The advanced filter is applied using the filter tool
    ![](media/image15.png) which is available in the
    construction area.

    Whereas the simple filter line applies inclusive like operators to
    the filter values supplied, the Advanced filter constructor allows
    selection of a wide range of filtering operators pertinent to the
    specific data type of the attribute (column) being filtered.

    ![](media/image16.png)

The red minus tool ![](media/image17.png) allows for criteria to be removed.

The green plus tool ![](media/image18.png) allows for additional criteria to be added.

The advanced filter is applied using the filter tool
![](media/image15.png) which is available in the construction
area.

#### Flat style advanced search

In flat style search, any number of criteria can be applied to filter the list.

In flat style the criteria are applied according to the method selected
(either "Match All", "Match Any" or "Match None").

![Flat style advanced filter](media/image19.png "Flat style advanced filter")
_Flat style advanced filter_

Using the "Match All" method will apply all criteria specified to the
resulting data. In the example above, the list will be filtered for
Timesheets where the "Week ending" date is between the specified dates ***AND*** 
which have "Total hours" is greater than the specified value.

Using the "Match Any" method will apply any of the criteria to the data.
In the example above, switching to "Match Any" would return all
Timesheets where either the "Week ending" date is between the specified dates ***OR***
where the "Total hours" is greater than the specified value.

Using the "Match None" method applies all of the criteria but in the
negative sense. In the example above, switching to the "Match None"
method would return all Timesheets which ***do not*** have "Week ending" date is between the specified dates ***AND*** 
which ***do not*** have "Total hours" greater than the specified value.

The advanced filter is applied using the filter tool
![](media/image15.png) which is available in the construction
area.

The red minus tool ![](media/image17.png) allows for criteria to be removed.

The green plus tool ![](media/image18.png) allows for additional criteria to be
added.

The advanced filter is applied using the filter tool
![](media/image15.png) which is available in the construction
area.

#### Nested style

Nested style allows complex and sophisticated filter criteria.

![](media/image21.png)

In Nested style, ***AND*** an ***OR*** operators are explicitly selected
with logical subclauses. The scope of each subclause is indicated by the
square bracket.

![](media/image22.png)

The red minus tool ![](media/image17.png) allows for criteria to be removed.

The green plus tool ![](media/image18.png) allows for additional criteria to be
added.

The advanced filter is applied using the filter tool
![](media/image15.png) which is available in the construction
area.

#### Inline style

Inline style is a simplified version of the Nested style, which allows
the ***AND*** and ***OR*** operators to be specified for each criteria,
however without the complexity of nesting.

![](media/image23.png)

The red minus tool ![](media/image17.png) allows for criteria to be removed.

The green plus tool ![](media/image18.png) allows for additional criteria to be added.

The advanced filter is applied using the filter tool
![](media/image15.png) which is available in the construction
area.

#### Edit Views

Edit views provide detailed access to document attributes within the
context of the document instance.

![Edit View](media/image25.png "Edit View")
_Edit View_

If no specific view metadata is provided, Skyve will generate a default
edit view which will include all attributes, actions and reports defined
within the document package. Skyve also provides the basic CRUD (Create,
Read, Update and Delete) actions by default according to the permissions
defined in the user role definition for that document in the
*module.xml*. Default views will include *dataGrids* for collections and
each collection will be presented in a separate tab.

The default view is often satisfactory, but can be overridden with view
definition file (*edit.xml)*.

The *edit.xml* file defines the layout of document attributes, the
particulars of custom action buttons (visibility, confirmation prompts
etc.) and parameters passed to reports.

Edit views also set the context for instance-specific behaviours
(actions).

Apart from providing custom actions, developers can also extend default
behaviours for each document.

### Overriding

Skyve offers developers the option of using default options and
behaviours for their applications unless they specifically override that
part of the application. The purpose of this is to try to encourage
developers to use consistent approaches except where absolutely
necessary.

#### Example - overriding data entry widgets

For example, in Skyve an Invoice document may have a Quantity attribute.
Once the attribute is declared as type *Integer*, the developer can rely
on Skyve to provide a simple numeric text-widget for data entry
everywhere that attribute is accessible.

The developer may choose to override this default by declaring their own
default widget (e.g. a slider) as part of the attribute declaration. On
a particular view, the developer may choose to override again by
specifying a spinner widget in the view declaration. For a particular
customer, the developer may choose to override again by overriding the
view declaration for the customer and specify another kind of widget.

![Override levels for a document widget declaration](media/image26.png "Override levels for a document widget declaration")
_Override levels for a document widget declaration_

At each context level (application, view, customer), the developer has
the option to rely on the default characteristic or override this with
for the particular context. This means that the developer only needs to
be specific where required.

#### Example - overriding query definitions for lists of documents

Similarly, wherever Invoices are presented in a list, the developer can
rely on Skyve to provide all attributes of the Invoice in the list by
default - this is the inferred default query. The developer may choose
to override this by supplying a *defaultQueryName* for the Invoice
document. When declaring a reference to Invoices from another document,
the developer may choose to override the *defaultQueryName* with another
query. When displaying a *listGrid* in a view, the developer may choose
to override the default characteristic and declare another query for the
list, and so on.

![Override levels for a document widget declaration](media/image27.png "Override levels for a document widget declaration")
_Override levels for a document widget declaration_

### BizKey - the business key concept

To enable the application to display references simply, each document
must define a business key (*bizKey*) definition (similar to a Java
*toString*() method for the document). The *bizKey* is the default
representation of the entire document instance.

For example, a contact might have a *bizKey* of `<name>` and a
timesheet might have a *bizKey* composed of `<user>` and `<week
ending date>`.

By mandating that every document has a *bizKey* there is always a
default representation available to the platform.

The *bizKey* is presumed to be unique and uniqueness is supported in
Skyve through the use of the constraint definition within the
*document.xml*, however Skyve does not enforce uniqueness of *bizKey*.

The *bizKey* is persisted for performance reasons so that it is
available for filtering.

The bizKey can be expressed as an expression in XML, or can be refer to a static method in Java which returns a valid *bizKey* String expression.

```xml
<bizKey expression="{user} - {weekEndingDate}" />
```

```xml
<bizKey>
	<![CDATA[
	  	return modules.admin.Contact.ContactBizlet.bizKey(this);
	]]>
</bizKey>
```

### Zoom

Zoom is a core concept for the management of logical relationships.

Where one document (A) references another document (B), the default
widget will provide the ability for the user to move to the referred
document B (i.e. Zoom In) from A, and to return to A (i.e. Zoom Out)
without losing their starting context.

Skyve supports n-level zoom contained within a single transaction. The
context for the transaction is the document instance at the lowest zoom
level (usually from the list view), i.e. the starting document.

Each zoom level blocks access to higher (i.e. previous) levels of the
transaction. Only the lowest zoom level view is accessible, and users
must Zoom Out of the current level to continue with the transaction at a
higher level. This ensures proper transaction demarcation and ensures
referential integrity can be maintained while allowing on-the-fly
creation of referenced document instances.

Skyve automatically modifies default actions depending on the zoom level
or transaction context.

![Zoom levels and transactions](media/image28.png "Zoom levels and transactions")
_Zoom levels and transactions_

#### Concurrent conversations and transactional demarcation

Skyve supports conversation level transaction demarcation, meaning that
the user can have any number of concurrent browser conversations.

Conflicting actions between conversations (e.g. two attempts to update
the same record from within different browser windows, or by different
users) are handled within the conversation context, and users will be
notified if the action they are performing conflicts within an action
from another conversation. The usual rule of first-in-wins applies; the
first conversation to complete a transaction and commit will be
successful, and any subsequent concurrent transaction involving the same
record will be notified that their attempts to update the same record
are no longer valid.

### Customer

Because Skyve is intended and designed for multi-tenancy, data is
assumed to exist within a customer (i.e. tenant) context. Skyve
automatically limits all user interactions to data within the same
customer context (except where developers intentionally use insecure SQL
methods).

### Customer Overriding

Any individual piece of Skyve application metadata can be overridden for
tenants within a multi-tenant environment, enabling comprehensively
mass-customised Software-As-A-Service (SAAS).

Skyve's unique customer overriding paradigm not only allows
comprehensive customisation but also provides a significantly more
maintainable solution than attempting to cater for all varying customer
experiences within the one application definition.

Customer overrides are managed completely within the customer package so
that identifying and managing the particular customer experience is as
simple as possible.

### Role Privileges

Skyve allows developers to specify application security roles in terms
of access permissions on a per-document basis.

A role declares a set of document privileges, and each document
privilege within a role specifies that role's ability to *Create*,
*Read*, *Update*, *Delete*, *List* and *Pick* documents.

Each privilege also declares the document scope and what custom actions
may be executed by the role.

If users are assigned multiple roles, their access to documents
represents the highest level of access across all roles assigned to
them.

### Document Scoping

Skyve allows document privileges to be scoped either as *Global* `(G)`,
*Customer* `(C)`, *Data Group* `(D)` or *User* `(U)` level.

Skyve applies the document scope which has been declared for the role
universally and transparently throughout the application, including
custom code developed using the API. This provides for highly secure
application development with no opportunity for developers to
inadvertently allow access other than according to the declared scope
and role.

The use of insecure SQL bypasses the inbuilt scoping of Skyve, so
developers using insecure SQL must take responsibility to ensure they
respect data contexts.

### Data Group

Data Groups roughly correspond to the concept of a department or
business group, where most users only interact with data relevant to
their organisational context, but administration/managing users work
with a federated collection of the organisation's data.

The Data Group concept is for row-level security. Users within a Data
Group can interact only with data which was created for that group,
whilst users who have no group specified (i.e. managers or
administrators) have access across all Data Groups.

### Bean

Skyve implements document instances as Enterprise Java Beans.

Beans provide developers with encapsulated objects and context-specific
methods. Utility classes are provided as part of the API to allow
developers to manipulate beans securely and within proper transactional
boundaries.

### Bizlet

Skyve allows developers to expand on default *CRUD* and validation
behaviours with document-specific Java classes called *Bizlets*.

*Bizlets* can reference other Java classes without restriction and may
be the point of connection to highly customised application code.
*Bizlet* classes can contain overrides for events within the bean
lifecycle.

*Bizlets* can also be overridden per customer. Module validation and
compilation will ensure that *Bizlet* code is valid and compilable for
each customer override.

### Actions

An action is how a Java method will be represented to the user within a
given view.

Actions are presented as buttons or hyperlinks in the user interface.

Default actions include *Create*, *Delete*, *Save*, *Cancel*, *Zoom In*
and *Zoom Out*. Default actions require no code or code stubs in Skyve
however default actions can be extended by overriding call-back methods
in *Bizlet* classes.

Custom actions are implemented as Java classes (containing an execute
method) within the document package.

An action is declared as part of the action declaration section of a
view. Each declaration nominates the Java class and representation
information specific to that view, including the action's button text,
icon, visibility, confirmation text, tool-tip text etc. Skyve will not
allow an action to be displayed on a view unless the user's role
declares execute privileges for the action.

### Enumerations

Skyve provides an *enum* attribute type which allows the developer to
declare the set of values applicable for the attribute. Enumerations
defined in metadata can be referenced across modules and documents,
where two or more attributes share the same value set. Developers can
also create normal Java enumerations (*enums*) without restriction.

For example, an enumeration to specify a person's gender might be the
set of values {male, female, intersex, not specified}.

A traditional approach might require the developer to:

1.  declare a text attribute,
2.  create a Java *enum* class,
3.  modify the database table definition to include an SQL constraint,
4.  specify a combo control in the view, and
5.  fill the combo with those values at run-time.

By contrast, Skyve allows a developer to:

1.  declare that the attribute is an *enum* type, and
2.  declare the applicable values directly in the document definition
    metadata.

Skyve will automatically include the Java *enum* in the generated domain
classes and provide a combo with the values by default wherever
required.

### Domain Values and Types

Domain values are groups of values applicable for a particular
situation.

Skyve includes a number of domain value concepts to allow the developer
to optimise application performance.

There are three domain types - *constant*, *variant* (depending on the
user context) and *dynamic* (depending on the bean instance context). In
most cases, *constant* domains should be declared as *enums*.

For example, to define the state of a timesheet an application might
require the set of values {draft, submitted, authorised}.

If all three values are always applicable then the *constant* domain
type is applicable (and could be declared as an *enum*).

If only a manager can set a timesheet to authorised, then the
application requires a *variant* domain based on the user's role.

If an application should only offer authorised when the timesheet is
submitted and the manager's review has been completed, then the
application would declare a *dynamic* domain type (calculated based on
the current state of the timesheet bean).

#### Performance Implications of Domain Types

*Constant* domains are generated into *Javascript* code (for Web 2) and
into *html* (for Web 1). A *constant* domain represents the least
performance cost.

*Variant* domain values are generated once per request and reused during
the response rendering on the server. *Variant* domains are not cached
on the client.

*Dynamic* domain value generation takes the document bean as a parameter
and generate values based on the bean. *Dynamic* domains are regenerated
for every web request involving the document and are not cached during
request processing for response rendering to ensure they are always
valid. *Dynamic* domains represent the greatest performance cost and
should only be used where necessary.

Domain value generation is specified in Bizlet code by overriding the
methods *getConstantDomainValues*(), *getVariantDomainValues*() and/or
*getDynamicDomainValues*().

### Converters

Applications typically require some data types to be represented in
specific ways, e.g. time, date, currency, etc. Skyve provides a number
of type converters.

The *customer.xml* file declares default date and time converters
applicable for the customer. A converter can also be declared for an
attribute to ensure a consistent representation of that attribute
throughout the application.

For example, for the attribute *invoiceDate* in the Invoice document,
the developer might declare the *DD\_MMM\_YYYY* converter. The attribute
declaration could then be overridden for a customer in with a
*MM\_DD\_YYYYY* converter.

Skyve will automatically enforce the converter everywhere the attribute
is viewed or modified and mask input widgets accordingly.

Converters can also be used for developer code and are the recommended
approach to ensure consistent representation throughout the application.

For example, the use of the converter below is thread-safe and ensures
consistent representation of the values *actDate* for each customer in a
multi-tenant environment.

```java
Persistence pers = CORE.getPersistence();
User user = pers.getUser();
Customer customer = user.getCustomer();

StringBuilder sb = new StringBuilder();
sb.append("The invoice is overdue and should have been paid on ");
sb.append(customer.getDefaultDateConverter().toDisplayValue(bean.getActDate()));
```
_The use of converters ensures consistent representation_

### Resources

Skyve provides access to resources in a generic way to allow multiple
levels of overriding.

For example, rather than specify an absolute file location for a button
icon, the application will specify the icon filename within a resource
context. The absolute location of the file will be resolved at run-time
depending on the user's context.

#### Resource Hierarchy

Skyve provides icons for default action buttons - *Add*, *Save*,
*Cancel*, *Delete* etc. A customer may require a different icon set and
override the default icons by placing files of the same name in the
customer's resources folder.

A module may require particular icons for these default actions and in
this case the developer would place module-specific icons into the
module *resources* folder. To override these module-specific icons for a
specific customer, files with matching names are placed in the resources
folder of the module override in the customer package, and so on.

### Documentation

Skyve encourages developers to include documentation snippets as part of
the application metadata with the intention that documentation should be
developed along with application functionality.

Documentation (*doc*) tags within the xml metadata hold basic HTML style
documentation. *Doc* tags are also used by the javadoc *doclet* provided
by Skyve, but are also accessible to developers via the Skyve *API*.

### Bizport

*Bizport* is a unique feature which allows the user to import and export
bulk data. *Bizport* generates a subset of the underlying normalised
database as a workbook of multiple spreadsheets for bulk data entry or
manipulation.

Bizport workbooks are formatted to enable robust validation of data
being import back into the database. Each *Bizport* sheet corresponds to
a document (and therefore a database table) and includes Skyve internal
keys so that correct document relationships can be maintained.

Skyve provides a default *Bizport* capability which extracts data from
the current bean down through the document structure (associations and
collections).

On import, *Bizport* rows are validated according to the constraints and
validation rules specified in metadata and *Bizlet* code.

Bizport is fully extensible, and developers can override and extend the
*Bizport* behaviour as required.

**[⬆ back to top](#contents)**

---
**Next [Chapter 3: Security, Persistence and Access control](./../chapters/security-persistence-and-access-control.md)**  
**Previous [Chapter 1: Architectural Overview](../README.md)**  
