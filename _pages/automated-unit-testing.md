---
title: "Automated unit testing"
permalink: /automated-unit-testing/
excerpt: "Automated unit testing"
toc: true
sidebar:
  title: "Index"
  nav: docs
---

Along with the ability to generate domain files for [Documents](./../_pages/documents.md), Skyve also generates CRUD unit tests against these documents as well as tests for any defined actions.

This is intended to give developers:

- a starting test coverage which grows alongside rapid development and application changes
- an extensible reference example for any specific tests the developer may wish to add
- a growing regression suite which can be immediately placed into a Continuous Integration environment

## Configuration
By default, automated test generation is configured for all modules detected from the metadata for a Skyve project (see [Customers](./../_pages/customers.md)). Execution of the `generate domain` step during development is responsible for keeping the generated test artefacts in sync with new documents and actions as they are added to a project.

The following sections describe the configuration which enables and creates the automated tests.

### Maven parameters

All projects created with the Skyve [project creator](https://foundry.skyve.org/foundry/project.xhtml) come pre-configured with the `skyve-maven-plugin` and the `build-helper-maven-plugin`. These can usually be left with all their default values, but some of the options for the `<configuration>` section are explained below:

* `<srcDir>` - The source path where the project’s source code (specifically the Skyve modules package) is located, defaults to `/src/main/java/`
* `<generatedDir>` - The generated path where the project’s generated code is located, defaults to `src/generated/java/` (note: this can be the same path as the source path, e.g. `src/main/java`)
* `<testDir>` - The test path where the project’s test code is located, defaults to `/src/test/java/`
* `<generatedTestDir>` - The generated test path, where generated test files should be stored, defaults to `/src/generatedTest/java/`
* `<excludedModules>` - Modules excluded from automatic unit test generation. This is a comma separated string (no white space) of modules in the project not to generate tests for, e.g. `admin,test`.

```xml
<plugin>
	<groupId>org.skyve</groupId>
	<artifactId>skyve-maven-plugin</artifactId>
	<version><!--supplied--></version>
	<dependencies>
		<dependency>
			<groupId>org.skyve</groupId>
			<artifactId>skyve-core</artifactId>
			<version>${skyve.version}</version>
		</dependency>
	</dependencies>
	<configuration>
		<skyveDir>../skyve</skyveDir>
		<customer>bizhub</customer>
		<generateDomainConfig>
			<debug>false</debug>
			<dialect>MYSQL_5</dialect>
			<excludedModules />
		</generateDomainConfig>
		<generateEditViewConfig>
			<document>${document}</document>
			<module>${module}</module>
		</generateEditViewConfig>
		<newDocumentConfig>
			<defaultModule>studio</defaultModule>
		</newDocumentConfig>
	</configuration>
</plugin>
```

The `build-helper-maven-plugin` configures your generated and generated test directory to be source directories for your project. This should only need to be updated if you provided a non-default `<generatedDir>` or `<generatedTestDir>` to your `skyve-maven-plugin` configuration.

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

### AbstractH2Test
The `AbstractH2Test.java` file is provided as a base unit test for all of the generated domain and action tests, but can also be extended for all unit tests for the project. It uses an in-memory H2 database to generate a new database each run and rolls back any writes in between tests.

The base test class uses the JUnit testing framework by default. See the [TestNG section](#testng) for configuring your tests to run with TestNG instead.

### AbstractDomainTest
`AbstractDomainTest.java` is the base domain test class and is used to test CRUD and Bizlet operations against every persistent document in your project. These tests are included in your project so that they can be modified or extended if required.

### AbstractActionTest
`AbstractActionTest.java` is the base action test class and is used to test execution of all `ServerSideAction`s in your project. As the test is unable to determine generically what state the beans need to be in for particular actions, the action test is intended more as a regression base as your project grows, checking that actions can still be called without throwing exceptions.

### TestNG
The provided action and domain tests are intended to be run in JUnit. If required, these can be converted easily to run with TestNG instead. The following changes will need to be made in addition to adding the required TestNG jars to your project class path:

_AbstractH2Test.java_

The JUnit `@Before` and `@After` annotations need to be replaced with the TestNG `@BeforeMethod` and `@AfterMethod` annotations.

_AbstractActionTest.java, AbstractDomainTest.java_

Remove the import to `org.junit.Test` and replace it with the correct import for a TestNG test.

## Fixtures

### DataBuilder 

DataBuilder is used to construct random instances of domain objects generated from Skyve document definitions for use in tests. It follows the builder pattern and has options for including and excluding both scalar (single valued) attributes as well as references (associations and collections). The attributes are populated with random data that conforms to the data types, lengths and constraints declared in the Document metadata. DataBuilder can be recursive, enabling the instantiation and population of an entire object tree graph from a single starting point.

Fixtures are named groupings of methods, that when executed together can collaboratively produce a data set for a specific purpose. Fixtures can be named with a String name, or there are implicit fixture types that are defined in the FixtureType enum. Annotating the methods in a Data Factory with either a fixture name or fixture type acts like a filter, ensuring that only suitable methods for each fixture (or use case) are called. Methods can be given a combination of multiple fixture names and types.

For example:

```java
Contact c = new DataBuilder()
	.fixture(FixtureType.crud)
	.build(Contact.MODULE_NAME, Contact.DOCUMENT_NAME);
```

Please be careful when calling other methods within a Factory class as you may inadvertently create a recursive loop if the method you are calling from is not annotated as a SkyveFixture. Make sure if you annotate a method with a fixture type, you specify the `factoryBuild` method.

For example, never call

```java
@SkyveFixture(types = FixtureType.crud)
public static Account crudInstance() throws Exception {
	Account bean = new DataBuilder()
		.fixture(FixtureType.crud)
		.build(Account.MODULE_NAME, Account.DOCUMENT_NAME);
```

or the new DataBuilder() call will call the `crudInstance` method

you need to write

```java
@SkyveFixture(types = FixtureType.crud)
public static Account crudInstance() throws Exception {
	Account bean = new DataBuilder()
		.fixture(FixtureType.crud)
		.factoryBuild(Account.MODULE_NAME, Account.DOCUMENT_NAME);
```
        
or use a databuilder without a fixture type.

### Fixture factories

Factories are useful for customising a DataBuilder to specify specific test data for a certain fixture type, or if the randomly selected data from the DataBuilder does not pass validation, or for defining different fixtures for different types, e.g. CRUD vs [SAIL](./../_pages/automated-ui-testing.md). By convention, a document can have a corresponding factory by defining a class called `<Document-Name>Factory`, similar to Bizlets. DataBuilder will find these classes when it needs to construct an instance of the document. It looks for `public static` or instance methods that take no arguments and returns the domain object type required. If there is more than one candidate method that can be called, DataBuilder will randomly call one of the methods.

Not all documents will need a factory, but if specific test data is required to be returned to pass validation or setup data in a way to pass tests, defining a factory gives you a place to customise your fixture.

For example:

```java
public class CommunicationFactory {
	@SkyveFixture(types = FixtureType.crud)
	public static Communication crudInstance() {
		Communication bean = new DataBuilder()
			.build(Communication.MODULE_NAME, Communication.DOCUMENT_NAME);
		bean.setSystem(Boolean.FALSE);
		bean.setTag(new DataBuilder().fixture(FixtureType.crud).build(Tag.MODULE_NAME, Tag.DOCUMENT_NAME));

		return bean;
	}
}
```

See the source `modules.admin.*` package in your project for example factories for the Skyve admin module.

### The `@SkyveFactory` annotation

The `@SkyveFactory` annotation can be applied to any Factory class for finer grained control over which tests get generated for a particular document. The annotation can be used to:
- skip domain test generation for the document (`@SkyveFactory(testDomain = false)`)
- skip action test generation for the document’s actions (`@SkyveFactory(testAction = false)`)
- skip specified action test generation for some of the document's actions (`@SkyveFactory(excludedActions = { Check.class, Next.class })`)

Any combination of these can be provided, but if `testAction` is set to false, `excludedActions` will be ignored as all actions will be skipped.

After annotating a Factory in the document package of the module you are testing, you will need to run generate domain again for Skyve to detect the factory and update the generated tests. Any skipped domain or action tests should be removed from the generated test directory automatically. If they aren't, check the output log file from generate domain for any warnings or errors.

### Data files

`DataBuilder()` will construct random data based on the metadata type of the attribute, e.g. random text or integers. It will also look at format masks and validators to attempt to create compliant tests values. However, some fields require more sensible test data, e.g. a phone number field, or a suburb name. If you would like to seed an attribute with specific random data, Skyve will automatically look in the `/src/main/java/resources/data` directory and read any text file whose filename matches the attribute name.

For example, a firstName field would look for `/src/main/java/resources/data/firstName.txt`. If it finds this file, it will randomly select a value from its content, where each value should be separated by a line break. Example test files are included with new projects.

Note: The data files are included in `src/main/java` so that they can be used to create test data in a running application, e.g. in an Action. If they were in `src/test`, they would be excluded from the build at runtime.

### The `@DataMap` annotation
A `Factory` can also be annotated with `@DataMap` when the test file to use does not match the attribute name, e.g. 

```java
@DataMap(attributeName = Contact.namePropertyName, fileName = "personName.txt")
public class ContactFactory {
```

If multiple attribute mappings are required, they need to be specified as part of a `@SkyveFactory` definition to allow repetition, e.g. 

```java
@SkyveFactory(value = {
		@DataMap(attributeName = Contact.namePropertyName, fileName = "personName.txt"),
		@DataMap(attributeName = Contact.email1PropertyName, fileName = "email.txt")
})
public class ContactFactory {
```

## Extending automated tests
For pure metadata projects (only XML files) with no custom development, automated unit tests are sufficient to provide test coverage and regression of the generated application source code. Specific testing of Skyve platform validation logic and other Skyve platform behaviour is already covered in the Skyve platform project and is not required to be duplicated in your project.

Once you start adding custom behaviour to your project, such as action classes, or bizlets, it is good development practice to test desired behaviour, boundary conditions, expected and unexpected inputs manually. The automated unit tests will check that your bizlet lifecycle and action methods will pass with a known good result. To extend this behaviour, developer created unit tests should be created in your test path (usually `/src/test/java/` in a maven project).

For example, let's create a test for the `GeneratePassword` action of the admin User document. This action should generate a new password and set the password expired flag for the current User. This action already has an automated test created to verify the action runs through without errors, but the automated test doesn't know how to validate the expected behaviour of this action. Lets create a specific test for that now.

While not strictly necessary, it is recommended to create the test file in the _same_ package as the source file in the corresponding test source directory. This provides benefits like being able to test `default` scoped methods without having to make them `public` in your class. So to test a User action, we create the file in the same `modules.admin.User.actions` package.

File: *src/test/java/modules/admin/User/actions/GeneratePasswordActionTest.java*

```java
public class GeneratePasswordActionTest extends AbstractH2Test {

	@Test
	public void testExecuteGeneratesNewPassword() throws Exception {
		// setup the test data
		User user = new DataBuilder().fixture(FixtureType.crud).build(User.MODULE_NAME, User.DOCUMENT_NAME);
		user.setConfirmPassword(null);
		user.setGeneratedPassword(null);
		user.setPasswordExpired(Boolean.FALSE);

		// call the method under test
		new GeneratePassword().execute(user, null);

		// verify the result
		assertThat(user.getPasswordExpired(), is(Boolean.TRUE));
		assertThat(user.getGeneratedPassword(), is(notNullValue()));
		assertThat(user.getConfirmPassword(), is(notNullValue()));
	}
}
```

Let's break this test down:

* we extend `AbstractH2Test` which is our Skyve test harness and sets up things like persistence and dependency injection for us
* the `new DataBuilder().fixture(FixtureType.crud)` creates a new User, and if there is a `UserFactory` defined, will return the crud fixture from that Factory
* we make sure the attributes we are testing for are in the desired state during fixture setup, e.g. we're hoping this method will generate a password so we set generated password to `null`
* we invoke the method we are testing
* we then assert the expected outcomes

**[⬆ back to top](#automated-unit-testing)**

---
**Next [Automated UI Testing and SAIL](./../_pages/automated-ui-testing.md)**<br>
**Previous [Wildcat Conversion Tool](./../_pages/wildcat-conversion-tool.md)**
