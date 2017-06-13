## Modules

### Contents

[Modules 38](#modules-1)

[Defining the *module.xml* 39](#defining-the-module.xml)

[Module header 39](#module-header)

[Documents 40](#documents)

[Queries 40](#queries)

[Generic Queries 40](#generic-queries)

[Declaring Queries 41](#declaring-queries)

[Query Column Definition 42](#query-column-definition)

[Queries for Reference Attributes 43](#queries-for-reference-attributes)

[Roles 44](#roles)

[Document Scope 45](#document-scope)

[Worked Example 45](#worked-example)

[Requirement 45](#requirement)

[Implementation 45](#implementation)

[Results 45](#results)

[Role Documentation 46](#role-documentation)

[Menus 46](#menus)

[Module Documentation 47](#module-documentation)

[Overriding Modules 47](#overriding-modules)

[Java Implementation 47](#java-implementation)

[Documents 49](#documents-1)

[Document.xml 49](#document.xml)

[Document.xml Sections 50](#document.xml-sections)

[Metadata 50](#metadata)

[BizKey 51](#bizkey)

[Attributes 51](#attributes)

[Attribute Types 51](#attribute-types)

[Conditions 59](#conditions)

[Constraints 59](#constraints)

[Documentation 59](#documentation-1)

[Overriding Documents 60](#overriding-documents)

[Database Persistence of Relationships and Key Constraints
61](#database-persistence-of-relationships-and-key-constraints)

[Database Indexes 62](#database-indexes)

[Java Classes 62](#java-classes)

[Bean 63](#bean-1)

[Persistent Bean 63](#persistent-bean)

[Persistence and Locking 64](#persistence-and-locking)

[Converters 65](#converters-1)

[Defined Converters 65](#defined-converters)

[Worked Example 66](#worked-example-1)

[Requirement 66](#requirement-1)

[Implementation 66](#implementation-1)

[Results 66](#results-1)

[Bizlets 68](#bizlets)

[Lifecycle 68](#lifecycle)

[Bean level: 68](#bean-level)

[User Interface level: 69](#user-interface-level)

[Implicit Actions 69](#implicit-actions)

[Views 71](#views)

[Containers 71](#containers)

[Autofit Behaviour 71](#autofit-behaviour)

[Form 74](#form)

[Form Example 74](#form-example)

[Table 74](#table)

[Item 74](#item)

[Widget 76](#widget)

[OnChange Event Action (Client-side Events)
84](#onchange-event-action-client-side-events)

[The lookupDescription Widget in Detail
84](#the-lookupdescription-widget-in-detail)

[Multi-column Drop-down/Combo 85](#multi-column-drop-downcombo)

[Filter Parameters 85](#filter-parameters)

[OnChange Handlers 86](#onchange-handlers)

[dataGrid 86](#datagrid)

[Columns 88](#columns)

[listGrid 88](#listgrid)

[Filter Parameters 89](#filter-parameters-1)

[Actions 90](#actions-1)

[Report Action 91](#report-action)

[New Parameter 91](#new-parameter)

[Actions 93](#actions-2)

[OnChange Event Actions (Client-side Events)
94](#onchange-event-actions-client-side-events)

[Reports 95](#reports)

[Custom Reports 95](#custom-reports)

[Automatic Customer Resource Parameter
96](#automatic-customer-resource-parameter)

[Object data source 96](#object-data-source)

[Ad-hoc Reports 96](#ad-hoc-reports)

[Offline Reporting Jobs 96](#offline-reporting-jobs)

[Jobs 97](#jobs)

[Job Classes 97](#job-classes)

[Utility Classes 99](#utility-classes)

[Persistence 99](#persistence)

[Insecure SQL 100](#insecure-sql)

[DocumentQuery 100](#documentquery)

[Common Patterns 102](#common-patterns)

[Identify Current User Contact 102](#identify-current-user-contact)

[Identify if Current User has Role
102](#identify-if-current-user-has-role)

[Save a Document Instance 102](#save-a-document-instance)

[Instantiate a New Document Instance
102](#instantiate-a-new-document-instance)

[Building a Variant Domain List 103](#building-a-variant-domain-list)

[Schedule an Offline Job 103](#schedule-an-offline-job)

[Persist Scalar Values Without Traversing Bean Structure
103](#persist-scalar-values-without-traversing-bean-structure)

[Retrieve and Iterate Through Beans
104](#retrieve-and-iterate-through-beans)

[Singleton Documents (Parameter /Configuration Documents)
104](#singleton-documents-parameter-configuration-documents)

[User-scoped Documents (Personal preferences Documents)
105](#user-scoped-documents-personal-preferences-documents)

[Customise Document and Document Attribute Names
105](#customise-document-and-document-attribute-names)

[Persistence 107](#persistence-1)

[Skyve Persistence Mechanisms 108](#skyve-persistence-mechanisms)

[Generic Naming Conventions 108](#generic-naming-conventions)

[Relationship naming convention 109](#relationship-naming-convention)

[Ordering and bizOrdinal 109](#ordering-and-bizordinal)

[UUID Enterprise-level Guaranteed Uniqueness
109](#uuid-enterprise-level-guaranteed-uniqueness)

[Optimistic Lock concurrency controls
110](#optimistic-lock-concurrency-controls)

[Enterprise-wide consistent reference representation
110](#enterprise-wide-consistent-reference-representation)

[Multi-tenant Support 110](#multi-tenant-support)

[Collaborative record flagging 110](#collaborative-record-flagging)

[Document scoping row-level security & source identification
111](#document-scoping-row-level-security-source-identification)

[Platform Tools 112](#platform-tools)

[Ant Utilities 113](#ant-utilities)

[Generate Domain 114](#generate-domain)

[Generate a Default Edit View 114](#generate-a-default-edit-view)

[Generating a Deployable Archive - copyProdEAR
114](#generating-a-deployable-archive---copyprodear)

[Javadoc 115](#javadoc)

[Content Repository Tools 118](#content-repository-tools)

[Backing up the Repository 118](#backing-up-the-repository)

[Reindexing the Repository 118](#reindexing-the-repository)

[Bizport 119](#bizport-1)

[Working With Bizport 119](#working-with-bizport)

[Using Bizport 119](#using-bizport)

[To enter bulk data using Bizport
119](#to-enter-bulk-data-using-bizport)

[To remove bulk data using Bizport
119](#to-remove-bulk-data-using-bizport)

[WILDCAT Conversion Tool 120](#wildcat-conversion-tool)

[NOTE: Refer to the WCT developer guide for full details. Using the
Skyve Converstion Tool (WCT)
120](#note-refer-to-the-wct-developer-guide-for-full-details.-using-the-skyve-converstion-tool-wct)

[Development Approach and Roundtripping
122](#development-approach-and-roundtripping)

[Report Conversion 122](#report-conversion)

[Appendix 123](#appendix)

[Appendix 1. Deploying a Skyve Application
124](#deploying-a-skyve-application)

[Appendix 2. Installing and configuring the Skyve Development
Environment
125](#installing-and-configuring-the-skyve-development-environment)

[Prerequisites checklist 125](#prerequisites-checklist)

[Configuring Java 125](#configuring-java)

[Configuring the IDE (Windows example)
125](#configuring-the-ide-windows-example)

[Configuring the workspace 125](#configuring-the-workspace)

[Importing Projects 125](#importing-projects)

[Creating the server 126](#creating-the-server)

[Configuring JBoss 127](#configuring-jboss)

[Starting the server 127](#starting-the-server)

[Deploying your solution 127](#deploying-your-solution)

[Appendix 3. Example Deployment Instructions with Single Sign-on
130](#example-deployment-instructions-with-single-sign-on)

Modules define self-contained application pieces and correspond to menus
within the system accordion menu pane. The repository/apps folder
contains all application metadata and code, organised as application
modules.

Each module folder contains a *module.xml* manifest file (declaring the
existence of the module components including jobs, documents, queries,
roles and menus) document packages (one package per document) and a
domain folder (which contains the generated domain classes).

Code in the domain folder is never manipulated by the developer
directly; all application changes are done via metadata and API-level
code.

![](media/image39.emf){width="6.954861111111111in" height="4.333333333333333in"} {#section-4 .Picture}
--------------------------------------------------------------------------------

Figure 17 Skyve in the Eclipse IDE

The *module.xml* file is located in the top level directory of the
module and defines the following:

  -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  Attribute/Section   Definition
  ------------------- -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  name                The name used by the developer to define and distinguish the module.

  title               The title of the module as presented in the user interface.

                      The module title will be the label shown on the accordion pane containing the module menu.

  homeRef             Whether the homeDocument (see below) will open in list or edit view.

                      This attribute is optional – if not included Skyve assumes a homeRef of *list* (i.e. a list view rather than a detail view).

  homeDocument        The document which will open by default when the module is accessed by the user.

  documents           The list of documents.

                      Documents may be persistent or transient. Each persistent document maps to a database table. (Transient documents exist only in memory.)

  queries             The queries referenced within the application metadata.

                      Each list view is based on a metadata query specified in the *module.xml* and any queries referenced within document metadata (e.g. for collections or references) must be declared here. Skyve will generate default queries for each document unless a specific metadata query is declared.

  roles               The user roles specified for the application.

                      Each role specifies permission levels for each document and actions which that role is permitted to execute.

  menu                The menu specifies which menu items are applicable for each role specified in the roles section.
  -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

Table 2 Module.xml sections

Defining the *module.xml* {#defining-the-module.xml .Chaptersubheading}
-------------------------

### Module header {#module-header .Sectionheading}

An example module header is provided below. Note the key attributes of
*schemaLocation*, name, title *homeRef* and *homeDocument*.

![](media/image40.emf){width="6.969444444444444in" height="1.3784722222222223in"} {#section-5 .Picture}
---------------------------------------------------------------------------------

Figure 18 Module definition header

\
Documents {#documents .Chaptersubheading}
---------

The *module.xml* includes declarations for each document.

If documents from another module are referenced, the source module must
be nominated as *moduleRef*. Document privileges and scoping is always
only defined in the home module, to avoid the potential of conflicting
scoping or permissions.

![](media/image41.png){width="5.666666666666667in"
height="2.151388888888889in"}

Figure 19 Module definition - document manifest

Documents listed in the *module.xml* may reference documents from other
modules (*moduleRef*) or are matched with document packages within the
module folder.

![](media/image42.png){width="3.0909722222222222in" height="1.636111111111111in"} {#section-6 .Picture}
---------------------------------------------------------------------------------

Figure 20 Document packages

Queries {#queries .Chaptersubheading}
-------

The *module.xml* file can include definitions of queries used in the
application. Queries declared in the *module.xml* are called *metadata
queries* to distinguish them from other queries which may exist as views
on the database server or as insecure SQL strings within developer code.

Each document can specify a *defaultQueryName* – which is the name of
the metadata query to use by default wherever lists of document
instances may be required (e.g. lists and lookups for document
references).

If a query name is not supplied Skyve will generate a default or
*generic* query which will include all columns for all document
attributes.

Generic Queries {#generic-queries .Chaptersubheading}
---------------

When Skyve generates a query (in the situation where a query is required
but none has been specified), this *generic* query will contain all
document attributes for the *driving document* and the *bizKey* value
for all references. Columns will be in the order of document attributes
(as specified in the *document.xml*) with ascending ordering applied to
the first column. All columns will be non-editable inline in the list.

Declaring Queries {#declaring-queries .Chaptersubheading}
-----------------

Skyve metadata queries use object references, rather than SQL.

Metadata queries must specify the *documentName*; the name of the
document which is the subject of the query.

If the query is the basis of a *listGrid*, then double-clicking in the
listGrid will zoom to the *driving document*.

![](media/image43.png){width="6.9847222222222225in" height="4.454861111111111in"} {#section-7 .Picture}
---------------------------------------------------------------------------------

Figure 21 Metadata query definition

![](media/image44.png){width="4.151388888888889in" height="3.0in"}

Figure 22 Query description displays as the list title

### Query Column Definition {#query-column-definition .Sectionheading}

  ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  Query Column Attributes   Description
  ------------------------- -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  binding                   The document value to be shown in the query column.

                            A compound binding can be used where the value to be shown is in a related document.

  displayName               An alias for the query column.

                            If no displayName is specified in the query, the list column title will be the displayName specified for the document attribute.

  editable                  Whether the column is editable in the list view inline.

                            By default editable is set to false.

  expression                A valid OQL expression which defines the value to be shown in the list column.

  filterable                Whether a filter can be applied to this column in the list view.

  filterExpression          A literal value or one of a number of expressions.

                            Defined expressions include:

                            -   {DATE} - current date

                            -   {DATETIME} - current date and time

                            -   {USERID} - bizId of the current user

                            -   {USER} – the userName of the current user

                            -   {CONTACTID} – the id of the contact who is the current user

                            -   {DATAGROUPID} – the id of the data group of the current user

                            -   {CUSTOMER} – the name of the customer context in which the current user operates


  filterOperator            One of the following operators:

                            -   equal, notEqual,

                            -   greater, less,

                            -   greaterEqual, lessEqual,

                            -   like, notLike,

                            -   notNull, isNull,

                            -   nullOrEqual, nullOrNotEqual,

                            -   nullOrGreater, nullOrLess,

                            -   nullOrGreaterEqual, nullOrLessEqual,

                            -   nullOrLike, nullOrNotLike;


  hidden                    Whether the query column will be hidden by default in a list view.

                            Hidden columns are hidden by default, but can be un-hidden by the user unless the column has projected=false.

  name                      You can include calculated or derived values in a query however you must create a non-persistent field in the driving document to hold the value. The *name* is the name of the non-persistent document field which holds the calculated value.

                            Note that the name must correspond to a transient (i.e. non persistent) field in the document which describes other aspects of the expression result (such as type, length, display format etc.).

  projected                 Whether the column will exist in the result set.

                            By default all query columns are projected unless this attribute is *false*.

  sortable                  Whether the query can be sorted by this column in the list view.

  sortOrder                 The sorting order (ascending or descending) to use by default when this query is displayed.

                            If the column is sortable, the user will be able to re-sort the list results.
  ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

Table 3 Query column definitions

Driving documents can be the subject of many queries.

Queries defined in the *module.xml* are also available to developers in
*Bizlet* code. Queries can also be declared as the source for document
attribute associations and collections. When used in this way, the query
determines eligible references for membership in the association or
collection.

### Queries for Reference Attributes {#queries-for-reference-attributes .Sectionheading}

If a query is specified for an association attribute, Skyve will use
that query for association selections in the application user interface.
This can also be overridden by specifying a query in the view.

For an association, the query will be used for record selection in the
view.

For example, selecting a document association normally uses a
*lookupDescription* widget. The *lookupDescription* combo will display a
list of the *bizKeys* of the driving document instances in the same
order as the default query (or ordered by the first column if no query
is specified).

![](media/image45.png){width="6.211805555555555in" height="0.89375in"}

Figure 23 Queries as a source for record selection

However, *lookupDescription* combos can display multiple columns (as
above) if a query is specified for the *lookupDescription* in the view.

\
Roles {#roles .Chaptersubheading}
-----

The *module.xml* declares roles for the module.

Each role specifies the privilege levels for documents the role will
access (and associated actions). The role name is the name displayed
when assigning roles to user security groups in the admin module.

Roles specified within the module.xml are available for selection within
the admin module at run-time.

![](media/image46.emf){width="2.60625in" height="2.0in"} {#section-8 .Picture}
--------------------------------------------------------

Figure 24 Assigning roles

For each document, the privilege level is specified in terms of C
(Create) R (Read) U (Update) D (Delete) L (List) P (Pick) and the
document scope access level, either G, C, D or U. The underscore
character (\_) means no permission is granted.

![](media/image47.emf){width="4.863888888888889in" height="2.5305555555555554in"} {#section-9 .Picture}
---------------------------------------------------------------------------------

Figure 25 Example role declaration

For example, a document privilege of CRUDLPC means the role has access
to *Create*, *Read*, *Update*, *Delete*, *List* and *Pick* the document,
and that the document is scoped C (i.e. *Customer*) so for that role,
the document is available to all users within the customer context.

A document privilege of *\_R\_\_LPU* means that the role will only have
access to *Read*, *List* and *Pick* the document. In this case, the
document is scoped U (i.e. *User*), which means that for this role, each
document instance will only be accessible to the user who created it.

Role privileges are applied to all select statements generated
internally by Skyve. This means that privileges automatically apply to
developer code (except for insecure SQL). The benefit of this is that
developer code does not have to handle security issues and therefore the
developer is not able to make inadvertent security holes.

### Document Scope {#document-scope .Sectionheading}

While document privileges define what type of activities a role may
perform on a document, the document scope defines which document
instances are accessible to that role. As such, document scoping is a
method to provide pervasive row-level security, declaratively.

Document scope criteria are applied to all select statements generated
internally by Skyve. This means that document scoping applies
automatically to developer code (except for insecure SQL).

Documents may be scoped *Global* (G), *Customer* (C), *DataGroup* (D) or
*User* (U).

*Customer* scope means all data created within the customer context is
accessible for that role.

A *Global* scope entitles the role to access data across all customers –
this is useful for reference documents like Post Code lists which can be
shared across customers within the Skyve instance.

*DataGroup* scope means that the role only has access to data created
within the same *DataGroup* context as the user.

A *User* scope means that for that role, only data created within a
user’s context can be viewed by that user.

Worked Example {#worked-example .Chaptersubheading}
--------------

### Requirement {#requirement .Sectionheading}

An application must allow each user to create and manage but not delete
their personal preferences securely, while allowing administrators to
maintain read, update and delete but not create preferences.

### Implementation {#implementation .Sectionheading}

The module.xml declares two roles, User and Administrator:

![](media/image48.png){width="4.439583333333333in"
height="1.5756944444444445in"}

Figure 26 Worked document scope example: role declaration in the
module.xml

The user is assigned privileges (CRU\_LPU) to create, read, update, list
and pick their own (i.e user-scoped) personal preference document.

The administrator role has privileges (\_RUDLPC) to documents within the
customer scope.

### Results {#results .Sectionheading}

Skyve will guarantee the no user activity or developer code can bypass
the declared permissions.

Users can only access document instances within their user scope, while
administrators have access to all personal preference document instances
within the customer context.

If a user is given both roles, the privileges are added and result in
(CRUDLPC) which equates to all privileges within the customer context.
Administrator users, if they also require personal preferences, are
assigned both roles.

### Role Documentation {#role-documentation .Sectionheading}

Role definition can include documentation within a &lt;doc&gt; tag which
is available to the application and to the documentation generation
module (Doctor).

![](media/image49.png){width="5.863888888888889in" height="2.4694444444444446in"} {#section-10 .Picture}
---------------------------------------------------------------------------------

Menus {#menus .Chaptersubheading}
-----

The application menu is defined in terms of groups and items. A menu
group is an expandable menu (submenu).

![](media/image50.emf){width="4.257638888888889in" height="1.9548611111111112in"} {#section-11 .Picture}
---------------------------------------------------------------------------------

Figure 27 Menu definition

![](media/image51.png){width="1.89375in" height="1.1819444444444445in"} {#section-12 .Picture}
-----------------------------------------------------------------------

Figure 28 Menu display

Roles included in the menu item stanza have access to that menu item. If
the menu item specifies a role, then users with that role will see the
menu item.

Module Documentation {#module-documentation .Chaptersubheading}
--------------------

Module definition can include detailed documentation about the module
within the *&lt;doc&gt;* tag. This documentation is used by the
documentation generation module (Doctor).

![](media/image52.png){width="6.878472222222222in" height="0.8638888888888889in"} {#section-13 .Picture}
---------------------------------------------------------------------------------

Figure 29 Example of detailed module documentation

Overriding Modules {#overriding-modules .Chaptersubheading}
------------------

Module definitions can be overridden to provide a bespoke experience of
the application.

All aspects of the module can be overridden including:

-   inclusion or exclusion of documents,

-   jobs,

-   queries,

-   roles,

-   document scoping, and

-   menus (structure, names and targets).

To override a *module.xml*, place the overriding *module.xml* file into
the customer package.

![](media/image53.png){width="2.651388888888889in" height="1.60625in"}

Figure 30 Example module override

As the *module.xml* file is a single artefact, it must contain all
elements of the module available for the bespoke experience and not
simply the components that differ. This is because the module override
can be subtractive, by not including elements contained within the
generic module.

Java Implementation {#java-implementation .Chaptersubheading}
-------------------

Java classes are contained within the domain folder situated within the
module package.

![](media/image42.png){width="2.3333333333333335in"
height="1.242361111111111in"}

Figure 31 Domain classes are located within the module package

The domain folder includes a Hibernate object-relational mapping
definition file for the module, named *&lt;module&gt;\_orm.hbm.xml*, as
well as classes for all module documents.

Where the *module.xml* is overridden for a customer within a
multi-tenant paradigm, a mapping file will be generated into the
customer module override folder, within the customer package.

All domain classes are regenerated by the *generateDomain* ant task and
should not be modified in any way by developers. However, inspection of
the domain classes can be a useful process to analyse validation
problems within the module.
