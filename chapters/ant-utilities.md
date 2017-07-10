# Platform Tools

## Ant Utilities

### Contents

* [Section 4: Platform Tools](#platform-tools)
  * **[Chapter 18: Ant Utilities](#ant-utilities)**
    * [Generate Domain](#generate-domain)
    * [Generate a Default Edit View](#generate-a-default-edit-view)
    * [Generating a Deployable Archive - copyProdEAR](#generating-a-deployable-archive---copyprodear)
    * [Javadoc](#javadoc)

Specific to the Java implementation of Skyve, Skyve projects include a
build.xml file which contains a number of utilities to assist
developers. To access the utilities, drag the build.xml file into the
ant view in Eclipse.

![](media/image158.png){width="6.969444444444444in"
height="5.227083333333334in"}

Figure 88 Accessing the ant utilities

The default target is generateDomain – which performs model validation
and generates the domain classes.

![](media/image159.png){width="2.10625in" height="1.8180555555555555in"}

Figure 89 Ant tasks

Developers can customise the build.xml, adding their own targets as
required, or customising those provided.

### Generate Domain {#generate-domain .Sectionheading}

From the ant view, double-click the *generateDomain* target to validate
the application model and generate domain classes. The *generateDomain*
target parameter is the path to the src folder being validated (normally
“src/”).

### Generate a Default Edit View {#generate-a-default-edit-view .Sectionheading}

To save time, it can be useful when creating a custom view to start with
the default view as Skyve would generate for a document on-the-fly.

From the ant view, edit the target and nominate the customer, module and
document arguments.

![](media/image160.png){width="4.454861111111111in"
height="1.0909722222222222in"}

Figure 90 Setting the parameters to create a default edit view

When you run the target, a file generatedEdit.xml will be created at the
in the file system at the top level of the project.

![](media/image161.png){width="4.909027777777778in"
height="1.8027777777777778in"}

Figure 91 Locating the generated edit view

### Generating a Deployable Archive - copyProdEAR {#generating-a-deployable-archive---copyprodear .Sectionheading}

The copyProdEAR target copies all files required for deployment, without
the associated git or subversion files into a deployment ear folder in
the project prod folder.

![](media/image162.png){width="2.136111111111111in"
height="2.045138888888889in"}

Figure 92 Creating a deployable archive

### \
Javadoc {#javadoc .Sectionheading}

Skyve provides a javadoc ant target which generates a documentation set
incorporating:

-   doc metadata attributes as specified in the Skyve metadata,

-   generic javadoc, and

-   logical model graph, generated using graphviz “dot” application.

![](media/image163.png){width="6.969444444444444in"
height="6.060416666666667in"}

To ensure that graphviz can generate model graphs, ensure that
..\\Graphviz\\bin is in your environment path.

Custom javadoc can also be created using the usual doclet interface.

The javadoc utility includes a combination of the application metadata
formatted and combined with the embedded documentation.

![](media/image164.png){width="5.621527777777778in"
height="3.8180555555555555in"}

Figure 93 Example of formatted application specification combining
metadata and embedded documentation

Skyve includes documentation attributes at every level of application
metadata.

Users can include basic html-style documentation within the metadata
"doc" attributes. The utility assembles this html and combines it with
self-describing metadata (like tool-tip definitions already embedded in
the metadata), according to the concepts inherent in the platform.

The utility creates a full documentation set including:

-   Titles,

-   Overviews,

-   Indexes and Table of Contents for each section,

-   Automatic numbered references for each section, table and figure,

-   Internal links, and

-   External links.

Because the utility generates documentation for every part of the
application specification, it encourages developers and technical
writers to be thorough and cover all aspects of the application they are
documenting.

![](media/image165.png){width="6.954861111111111in"
height="2.863888888888889in"}

Figure 94 Customise the javadoc task to specify the package and
destination directory
