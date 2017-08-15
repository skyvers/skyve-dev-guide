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
		* [Build.xml parameters](#build-xml-parameters)
		* [Maven parameters](#maven-parameters)
		* [AbstractH2Test](#abstracth2test)
		* [AbstractDomainTest](#abstractdomaintest)
		* [AbstractActionTest](#abstractactiontest)
		* [AbstractDomainFactory](#abstractdomainfactory)
		* [TestNG](#testng)
	* [The factory extension](#the-factory-extension)
	* [The @SkyveFactory annotation](#the-skyvefactory-annotation)
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
The `build.xml` generate-domain ant task supports six parameters, three of which are required. The order of the parameters is important and must be followed.

1. The source path - where the project's source code (specifically the Skyve modules package) is located, e.g. `/src/skyve/`
2. The test path - where the project's test code is located, e.g. `/src/test/`
3. The generated test path, where generated test files should be stored, e.g. `/src/generatedTest/`
4. The allowCascadeMerge global option, true or false
5. The debug option, true or false
6. Excluded modules (optional). This is a comma separated string (no white space) of modules in the project not to generate tests for. For example `admin,test`.

For an ant project, you will need to assign the generated test directory specified in argument #3 as a test source directory for your project in your chosen IDE.

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
		<argument>src/test/java/</argument> <!-- test path -->
		<argument>src/generatedTest/java/</argument> <!-- generated test path -->
		<argument>true</argument> <!-- allow cascade merge -->
		<argument>false</argument> <!-- debug -->
		<argument>admin</argument> <!-- excluded modules -->
	</arguments>
	<additionalClasspathElements>
		<additionalClasspathElement>${basedir}/target/test-classes</additionalClasspathElement>
	</additionalClasspathElements>
</configuration>
</execution>
```

In addition to configuring the additional generate-domain arguments, you will need to configure the _build-helper_ plugin to configure your generated test directory to be a test source for your project:

```xml
<plugin>
    <groupId>org.codehaus.mojo</groupId>
  <artifactId>build-helper-maven-plugin</artifactId>
  <version>1.7</version>
    <executions>
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
`AbstractDomainTest.java` is the base domain test class and is used to test CRUD operations against every persistent document  in your project. These tests are included in your project so that they can be modified or extended if required.

#### AbstractActionTest
`AbstractActionTest.java` is the base action test class and is used to test execution of all `ServerSideAction`s in your project. As the test is unable to determine generically what state the beans need to be in for particular actions, the action test is intended more as a regression base as your project grows, checking that actions can still be called without throwing exceptions.

#### AbstractDomainFactory
`AbstractDomainFactory.java` is an abstract class defining a single abstract method which is used by the action and domain tests to construct an instance of a bean for a test.

The `getInstance()` method must be implemented by each Factory which returns an instance of a bean which is in a state which can pass validation (for any save or update tests for example). During domain generation, Skyve creates a factory for every document in all modules which have not been excluded.  By default, Skyve will create an instance wiht a random value for all scalar attributes, and will populate all _required_ associations and collections.

If further customisation of the constructed instance is required, e.g. returning specific test data to pass validation, see the [factory extension](#the-factory-extension) section.

#### TestNG
The provided action and domain tests are intended to be run in jUnit. If required, these can be converted easily to run with TestNG instead. The following changes will need to be made in addition to adding the required TestNG jars to your project class path:

**AbstractH2Test.java**
The jUnit `@Before` and `@After` annotations need to be replaced with the TestNG `@BeforeMethod` and `@AfterMethod` annotations.

**AbstractActionTest.java, AbstractDomainTest.java**
Remove the import to `org.junit.Test` and replace it with the correct import for TestNG test.

### The factory extension
Extending the functionality of a generated factory class follows the same Extension convention as with generated domain classes. As all files within your generated test source directory are controlled by Skyve, edits need to be made outside of this so that they can be kept each time generate domain is executed.

Skyve will look for any _factory extension_ classes within your specified test directory as part of your build arguments. Unlike domain extension files which are in the same package as their declared document, Factory Extension classes are expected to be found within the util package of their respective module within the test directory. This is intended to keep them out of the way of developers package unit tests.

Not all documents will need a factory extension, but if specific test data is required to be returned to pass validation or setup data in a way to pass tests, defining a factory extension gives you a place to customise your fixture.

For example:

```java
public class CommunicationFactoryExtension extends CommunicationFactory {

	@Override
	public Communication getInstance() throws Exception {
		Communication comm = super.getInstance();
		comm.setSystem(Boolean.FALSE);

		return comm;
	}
}
```

After creating a new extension in the `util` package of the module you are testing, you will need to run generate domain again for Skyve to detect the factory extension and update the generated test. Opening the generated domain test or action test for the document should create an instance of the bean using the FactoryExtension instead of the factory if it has been detected correctly. If it doesn’t, check the output log file from generate domain for any warnings or errors.

See the test `admin.util.*` package in your project for example factory extensions for the Skyve admin module.

### The `@SkyveFactory` annotation
The `@SkyveFactory` annotation can be applied to any FactoryExtension class for finer grained control over which tests get generated for a particular document. The annotation can be used to:
- skip domain test generation for the document (`@SkyveFactory(testDomain = false)`)
- skip action test generation for the document’s actions (`@SkyveFactory(testAction = false)`)
- skip specified action test generation for some of the document’s actions (`@SkyveFactory(excludedActions = { Check.class, Next.class })`)

Any combination of these can be provided, but if `testAction` is set to false, `excludedActions` will be ignored as all actions will be skipped.

### Upgrading existing projects
Automated test generation was introduced in Skyve version **20170810**. If you are working on a project which was created prior to this version, then some additional files are required in your project in order to function correctly.

After performing the typical upgrade steps to update your project to the latest Skyve version, please copy the following assets into your project:

- AbstractH2Test.java from Skyve-ee
- AbstractActionTest
- AbstractDomainTest
- AbstractDomainFactory
- _src_test_modules_admin_until_* into your test path configured in your [ant configuration](#build-xml-parameters) or your [maven configuration](#maven-configuration)
- Update your build file following the relevant configuration depending on whether you are using maven or ant.

**[⬆ back to top](#contents)**

---
**Next [Section 5: Appendix](./../chapters/appendix.md)**<br>
**Previous [Chapter 21: Wildcat Conversion Tool](./../chapters/wildcat-conversion-tool.md)**
