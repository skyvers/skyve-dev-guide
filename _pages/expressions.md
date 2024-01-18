---
title: "Expressions"
permalink: /expressions/
excerpt: "Working with expressions in Skyve"
toc: true
sidebar:
  title: "Index"
  nav: docs
---

Expressions can be used throughout Skyve applications, both within the metadata as well as within the source code, to evaluate and return dynamic values. The most common example of this within Skyve is the [bizKey](./../_pages/concepts.md#bizkey---the-business-key-concept).

## Expression Types

There are different types of expressions supported by Skyve. 

Expressions take the form: 

`{<evaluator-prefix>:<expression>|<formatter-suffix>}`

Using a specific expression type is activated with the following prefixes below. An expression with no prefix specified defaults to the `bean` binding expression for backwards compatability.

An optional formatter suffix can be specified to format the result of the expression. The formatter suffix is a pipe character (`|`) followed by the formatter name. Built-in formatters are defined in the [FormatterName](https://github.com/skyvers/skyve/blob/master/skyve-core/src/main/java/org/skyve/metadata/FormatterName.java) enum. The formatter method must take a single argument of type `Object` and return a `String`. The formatter method is called with the result of the expression evaluation.

Evaluator Type Prefix | Description
----------------|------------
`{bean}` (or no prefix) | bean binding expression
`{el}`          | Validated Java EL expression (note the EL context contains bean, stash, user, DATE, TIME, DATETIME and TIMESTAMP)
`{rtel}`        | Runtime Java EL expression, not validated
`{desc}`        | description of the requested attribute from the document metadata
`{disp}`        | display name of the requested attribute from the document metadata
`{i18n}`        | internationalisation resource bundle key
`{role}`        | returns true if user is in the {module.role}, otherwise false
`{stash}`       | stashed value by key or null / "" if not found
`{user}`        | user attribute by key or null / "" if not found

## Implicit Expressions

The following special expressions are built into Skyve:

Expression  | Expression Result
------------|------------
{USER}      | The current logged in user
{USERID}    | The bizId of the current user
{USERNAME}  | The name of the current user contact
{DATAGROUPID} | The bizId of the data group of the current user
{CONTACTID} | The bizId of the current user's contact
{CUSTOMER}  | The current customer
{DATE}      | The current date with the time component cleared (0:00:00) (`DateOnly`)
{TIME}      | The current time (`TimeOnly`)
{DATETIME}  | The current date with the time component set (`DateTime`)
{TIMESTAMP} | The current timestamp (`Timestamp`)
{URL}       | - Util.getDocumentUrl(bean);

## Example Expressions

Below are some example expressions:

Expression  | Expression Result
------------|------------
{text} | The value of the `text` attribute of the current bean, if it has one
{bean:text} | Equivalent to `{text}`
{el:bean.text} | Equivalent to `{text}`
{el:stash['text']} | The value of the `text` key within the stash
{el:user.attributes['text']} | The value of the `text` user attribute
{el:newDateOnlyFromLocalDate(newDateOnly().toLocalDate().plusDays(1))} | Returns the day after the the current date (tomorrow)
{el:newDateOnly().toLocalDate().getYear()} | Returns the current year as an integer
{i18n:some.bundle.key} | The value of the localised key within the resource bundle if one is defined, otherwise the value of the requested key
{role:admin.BasicUser} | True if the current user has the `admin.BasicUser` role
{stash:text} | The value of the `text` key within the stash
{el:stash['text']} | Equivalent to `{stash:text}`
{user:text} | The value of the `text` user attribute
{el:user.attributes['text']} | Equivalent to `{user:text}`
{disp:text} | The display name of the `text` attribute on the specified bean
{desc:text} | The description of the `text` attribute on the specified bean

### EL Expressions

The EL expression evaluator (`{el:}`) is a validated Java EL 3.0 expression. The EL expression language is documented [here](https://download.oracle.com/javaee-archive/el-spec.java.net/users/att-0034/EL3.0.PFD.RC1.pdf).

## Expression Usage

### Views

Expressions can be used within views within Skyve, as per the example below from the admin module whih uses the `i18n` expression to look up the localised message for the `admin.selfRegistration.postRegister` key:

```xml
	<blurb escape="false">
		<![CDATA[
			<div style="text-align: center">
				{i18n:admin.selfRegistration.postRegister}
			</div>
		]]>
	</blurb>
```

Expressions can be combined with a formatter suffix in views to format specific values, as per the following examples:

```xml
	<blurb>{decimal2|OneDecimalPlace}</blurb>
```

and used within a DataGrid:

```xml
<dataGrid binding="resultTable">
	<column binding="resultValue" formatter="TenOptionalDecimalPlaces"/>
</dataGrid>
```

### Conditions

Expressions can be used within conditions within Skyve, as per the following examples:

```xml
	<condition>
		<expression>
			<![CDATA[
				{el:bean.amount > 100 || bean.amount < 50}
			]]>
		</expression>
	</condition>
```

or this can alternatively use `gt` and `lt` operators as substitutes for `>` and `<` and the `CDATA` will not be required.

```xml
	<condition>
		<expression>
			{el:bean.amount gt 100 || bean.amount lt 50}
		</expression>
	</condition>
```

Read more in the [Conditions](./../_pages/documents.md#expression-conditions) section of the Documents page.

### Code

Within your application code, expressions can be evaluated from the `Binder` utility class, as per the following examples:

```java
// place some values into the stash and the user attributes
CORE.getStash().put("text", "Stash");
CORE.getUser().getAttributes().put("text", "Attribute");

// retrive the values
Binder.formatMessage("{stash:text}", bean); // returns Stash
Binder.formatMessage("{user:text}", bean); // returns Attribute
Binder.formatMessage("{el:newDecimal2(100)|TwoOptionalDecimalPlaces}", bean); // returns 100
```

**[⬆ back to top](#expressions)**
