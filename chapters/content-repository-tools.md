## Content Repository Tools

### Contents

* **[Chapter 19: Content Repository Tools](#content-repository-tools)**
  * [Backing up the Repository](#backing-up-the-repository)
  * [Reindexing the Repository](#reindexing-the-repository)

### Backing up the Repository

A backup batch file is located in &lt; Project Home Directory
&gt;/prod/backup/

### Reindexing the Repository

Skyve manages content (including full-text searching) using the
Jackrabbit implementation of the Java Content Repository (JCR)
specification. Skyve is database (& version) and operating system
agnostic.

In Skyve, whether a document attribute is indexed is declared directly
in the domain model.

When indexed, each piece of content is stored as subnodes in a tree
(i.e. the repository) holding the UUID of the document instance which
owns the piece of indexed data. The tree structure matches the
object/document structure as declared in the domain model which allows
Skyve to determine the context in which a match occurs and redirect the
user to see the document context in which the matching content resides.

For example, a user searches on the word "nanotechnology", the tree is
inspected and matches are found in a number of document attributes and
content files. Because the tree contains the path to each match, Skyve
can determine the document context in which each match occurs and take
the user (in the UI) to the document instance where the match occurs.

Skyve needs to be able to create files and folders within the
&lt;Project Home Directory&gt;\\content\\ directory because Skyve does
not store content as blobs in the database (which would be too closely
coupled to particular DB implementations). Content is stored within a
content folder in the file system. Indexes are also held on the file
system and when the indexation occurs, folders and files are created
(for example &lt;Project Home
Directory&gt;\\content\\workspaces\\arc\\index\\).

The CONTENT tables in the database are part of the implementation of
JCR. These hold the serialised node information (the paths to the
attributes). This means that Skyve needs to have CRUD permission on the
CONTENT tables within the project database.

Indexes are maintained automatically by Skyve, however if data is
inserted into the database by other means, the indexes will not be
updated automatically.

To reindex the repository, run the reindex.bat file located at &lt;
Project Home Directory &gt;/prod/backup/

**[⬆ back to top](#contents)**

---
**Next [Chapter 20: Bizport](./../chapters/bizport.md)**  
**Previous [Chapter 18: Ant Utilities](./../chapters/ant-utilities.md)**
