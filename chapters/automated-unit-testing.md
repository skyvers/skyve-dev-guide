## Automated Unit Testing

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
* [Chapter 16: Common Patterns](./../chapters/common-patterns.md)
* [Chapter 17: Skyve Persistence Mechanisms](./../chapters/skyve-persistence-mechanisms.md)
* [Chapter 18: Ant Utilities](./../chapters/ant-utilities.md)
* [Chapter 19: Content Repository Tools](./../chapters/content-repository-tools.md)
* [Chapter 20: Bizport](./../chapters/bizport.md)
* [Chapter 21: WILDCAT Conversion Tool](./../chapters/wildcat-conversion-tool.md)
* **[Chapter 22: Automated Unit Testing](#automated-unit-testing)**
	* [Configuration](#configuration)
		* [Build.xml parameters](#buildxml-parameters)
		* [Maven parameters](#maven-parameters)
		* [AbstractH2Test](#abstracth2test)
		* [AbstractDomainTest](#abstractdomaintest)
		* [AbstractActionTest](#abstractactiontest)
		* [AbstractDomainFactory](#abstractdomainfactory)
		* [TestNG](#testng)
	* [Fixtures](#fixtures)
		* [DataBuilder](#databuilder)
		* [Fixture Factories](#fixture-factories)
		* [The @SkyveFactory annotation](#the-skyvefactory-annotation)
		* [Data Files](#data-files)
		* [The @DataMap annotation](#the-datamap-annotation)
	* [Upgrading existing projects](#upgrading-existing-projects)

Along with the ability to generate domain files for [Documents](./../chapters/documents.md), Skyve also generates CRUD unit tests against these documents as well as tests for any defined actions.

This is intended to give developers:

- a starting test coverage which grows alongside rapid development and application changes
- an extensible reference example for any specific tests the developer may wish to add
- a growing regression suite which can be immediately placed into a Continuous Integration environment

### Configuration
By default, automated test generation is configured for all modules detected from the metatdata for a Skyve project (see [Chapter 6: Customers](./../chapters/customers.md)). Execution of the generate domain step during development is responsible for keeping the generated test artefacts in sync with new documents and actions as they are added to a project.

The following sections describe the configuration which enables and creates the automated tests.

#### Build.xml parameters
The `build.xml` generate-domain ant task supports seven parameters, four of which are required. The order of the parameters is important and must be followed.

1. The source path - where the project's source code (specifically the Skyve modules package) is located, e.g. `/src/skyve/`
2. The generated path - where the project's generated code is located, e.g. `/src/generated` (note: this can be the same path as the source path, e.g. `src/skyve`)
3. The test path - where the project's test code is located, e.g. `/src/test/`
4. The generated test path, where generated test files should be stored, e.g. `/src/generatedTest/`
5. The debug option, true or false
6. The dialect option, which controls the bizKey index length and some dialect specific options, one of `H2`, `H2_NO_INDEXES`, `MYSQL`, `MSSQL_2014`, `MSSQL_2016`
6. Excluded modules (optional). This is a comma separated string (no white space) of modules in the project not to generate tests for, e.g. `admin,test`.

For an ant project, you will need to assign the generated test directory specified in argument #4 as a test source directory for your project in your chosen IDE.

#### Maven parameters
The Maven parameters should be specified in the same order as the ant build.xml parameters, but in the maven syntax:

```xml
<execution>
<!-- Domain Generation: mvn exec:java@generateDomain -->
<id>generateDomain</id>
<goals>
	<goal>java</goal>
</goals>
<configuration>
	<mainClass>org.skyve.impl.generate.DomainGenerator</mainClass>
	<arguments>
		<argument>src/main/java/</argument> <!-- source path -->
		<argument>src/generated/java/</argument> <!-- generated path -->
		<argument>src/test/java/</argument> <!-- test path -->
		<argument>src/generatedTest/java/</argument> <!-- generated test path -->
		<argument>false</argument> <!-- debug -->
		<argument>H2</argument> <!-- sql dialect -->
		<argument>admin</argument> <!-- excluded modules -->
	</arguments>
	<additionalClasspathElements>
		<additionalClasspathElement>${basedir}/target/test-classes</additionalClasspathElement>
	</additionalClasspathElements>
</configuration>
</execution>
```

In addition to configuring the additional generate-domain arguments, you will need to configure the _build-helper_ plugin to configure your generated and generated test directory to be source directories for your project:

```xml
<plugin>
  <groupId>org.codehaus.mojo</groupId>
  <artifactId>build-helper-maven-plugin</artifactId>
  <version>1.7</version>
  <executions>
    <!-- required if generatd path is not src/main/java/ -->
    <execution>
      <phase>generate-sources</phase>
      <goals>
        <goal>add-source</goal>
      </goals>
      <configuration>
      <sources>
        <source>src/generated/java</source>
      </sources>
      </configuration>
    </execution>
    <execution>
      <id>add-test-source</id>
      <phase>generate-test-sources</phase>
      <goals>
        <goal>add-test-source</goal>
      </goals>
      <configuration>
      <sources>
        <source>src/generatedTest/java</source>
      </sources>
      </configuration>
    </execution>
  </executions>
</plugin>
```

#### AbstractH2Test
The `AbstractH2Test.java` file is provided as a base unit test for all of the generated domain and action tests, but can also be extended for all unit tests for the project. It uses an in-memory H2 database to generate a new database each run and rolls back any writes in between tests.

The base test class uses the JUnit testing framework by default. See the [TestNG section](#testng) for configuring your tests to run with TestNG instead.

#### AbstractDomainTest
`AbstractDomainTest.java` is the base domain test class and is used to test CRUD operations against every persistent document in your project. These tests are included in your project so that they can be modified or extended if required.

#### AbstractActionTest
`AbstractActionTest.java` is the base action test class and is used to test execution of all `ServerSideAction`s in your project. As the test is unable to determine generically what state the beans need to be in for particular actions, the action test is intended more as a regression base as your project grows, checking that actions can still be called without throwing exceptions.

#### TestNG
The provided action and domain tests are intended to be run in jUnit. If required, these can be converted easily to run with TestNG instead. The following changes will need to be made in addition to adding the required TestNG jars to your project class path:

**AbstractH2Test.java**
The jUnit `@Before` and `@After` annotations need to be replaced with the TestNG `@BeforeMethod` and `@AfterMethod` annotations.

**AbstractActionTest.java, AbstractDomainTest.java**
Remove the import to `org.junit.Test` and replace it with the correct import for a TestNG test.

### Fixtures

#### DataBuilder 

DataBuilder is used to construct random instances of domain objects generated from Skyve document definitions for use in tests. It follows the builder pattern and has options for including and excluding both scalar (single valued) attributes as well as references (associations and collections). The attributes are populated with random data that conforms to the data types, lengths and constraints declared in the Document metadata. DataBuilder can be recursive enabling the instantiation and populate of an entire object tree graph from 1 starting point.

Fixtures are named groupings of methods that when executed together can collaboratively produce a data set for a specific purpose. Fixtures can be named with a String name, or there are implicit fixture types that are defined in the FixtureType enum. Annotating the methods in a Data Factory with either a fixture name or fixture type acts like a filter ensuring that only suitable methods for each fixture (or use case) are called. Methods can be given a combination of multiple fixture names and types.

For example:

```java
Contact c = new DataBuilder().fixture(FixtureType.crud).build(Contact.MODULE_NAME, Contact.DOCUMENT_NAME);
```

### Fixture Factories

Factories are useful for customising a DataBuilder to specify specific test data for a certain fixture type, or if the randomly selected data from the DataBuilder does not pass validation, or for defining different fixtures for different types, e.g. crud vs sail. By convention, a document can have a corresponding factory by defining a class called `<Document-Name>Factory`, similar to Bizlets. DataBuilder will find these classes when it needs to construct an instance of the document. It looks for public static or instance methods that take no arguments and returns the domain object type required. If there is more than one candidate method that can be called, DataBuilder will randomly call one of the methods.

Not all documents will need a factory, but if specific test data is required to be returned to pass validation or setup data in a way to pass tests, defining a factory gives you a place to customise your fixture.

For example:

```java
public class CommunicationFactory {

	@SkyveFixture(types = FixtureType.crud)
	public static Communication crudInstance() {
		Communication bean = new DataBuilder().build(Communication.MODULE_NAME, Communication.DOCUMENT_NAME);
		bean.setSystem(Boolean.FALSE);
		bean.setTag(new DataBuilder().fixture(FixtureType.crud).build(Tag.MODULE_NAME, Tag.DOCUMENT_NAME));

		return bean;
	}
}
```

See the source `modules.admin.*` package in your project for example factories for the Skyve admin module.

#### The `@SkyveFactory` annotation
The `@SkyveFactory` annotation can be applied to any Factory class for finer grained control over which tests get generated for a particular document. The annotation can be used to:
- skip domain test generation for the document (`@SkyveFactory(testDomain = false)`)
- skip action test generation for the document’s actions (`@SkyveFactory(testAction = false)`)
- skip specified action test generation for some of the document’s actions (`@SkyveFactory(excludedActions = { Check.class, Next.class })`)

Any combination of these can be provided, but if `testAction` is set to false, `excludedActions` will be ignored as all actions will be skipped.

After annotating a Factory in the document package of the module you are testing, you will need to run generate domain again for Skyve to detect the factory and update the generated tests. Any skipped domain or action tests should be removed from the generated test directory automatically. If they aren't, check the output log file from generate domain for any warnings or errors.

#### Data Files
`DataBuilder()` will construct random data based on the metadata type of the attribute, e.g. random text or integers. It will also look at format masks and validators to attempt to create compliant tests values. However, some fields require more sensible test data, e.g. a phone number field, or a suburb name. If you would like to seed an attribute with specific random data, Skyve will automatically look in the `/src/test/resources/data` directory and read any text file whose filename matches the attribute name.

For example, a firstName field would look for `/src/test/resources/data/firstName.txt`. If it finds this file, it will randomly select a value from its content, where each value should be separated by a line break. Example test files are included with new projects.

#### The `@DataMap` annotation
A `Factory` can also be annotated with `@DataMap` when the test file to use does not match the attribute name, e.g. 
```@DataMap(attributeName = User.homeModulePropertyName, fileName = "firstName.txt")```

If multiple attribute mappings are required, they need to be specified as part of a `@SkyveFactory` definition to allow repetition, e.g. 

```java
@SkyveFactory(excludedActions = { Check.class, Next.class }, value = {
		@DataMap(attributeName = User.userNamePropertyName, fileName = "lastName.txt"),
		@DataMap(attributeName = User.homeModulePropertyName, fileName = "firstName.txt")
})
```

### Upgrading existing projects
Automated test generation was introduced in Skyve version **20170810**. If you are working on a project which was created prior to this version, then some additional files are required in your project in order to function correctly.

After performing the typical upgrade steps to update your project to the latest Skyve version, please copy the following assets into your project:

- AbstractH2Test.java from Skyve-ee
- AbstractActionTest
- AbstractDomainTest
- _src.modules.admin.Factory*_ into your source path configured in your [ant configuration](#build-xml-parameters) or your [maven configuration](#maven-configuration)
- test data friles from _src.test.resources.data_ into your _src.test.resources.data_ directory
- Update your build file following the relevant configuration depending on whether you are using maven or ant

**[⬆ back to top](#contents)**

---
**Next [Section 5: Appendix](./../chapters/appendix.md)**<br>
**Previous [Chapter 21: Wildcat Conversion Tool](./../chapters/wildcat-conversion-tool.md)**
