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

There are different types of expressions supported by Skyve. Using a specific expression type is activated with the following prefixes below. An expression with no prefix acts as a `bean` binding expression for backwards compatability.

Expression Type Prefix | Description
----------------|------------
`{bean}` (or no prefix) | bean binding expression
`{el}`          | Java EL expression (note the EL context contains bean, stash, user, DATE, TIME, DATETIME and TIMESTAMP)
`{i18n}`        | internationalisation resource bundle key
`{role}`        | returns true if user is in the {module.role}, otherwise false
`{stash}`       | stashed value by key
`{user}`        | user attribute by key
`{disp}`        | display name of the requested attribute from the document metadata
`{desc}`        | description of the requested attribute from the document metadata

## Example Expressions

Below are some example expressions:

Expression | Expression Result
-----------|------------
{USER}     | The username of the current user
{USERID}   | The bizId of the current user
{USERNAME} | The name of the current user contact
{DATAGROUPID} | The bizId of the data group of the current bean
{CONTACTID} | The bizId of the current contact
{CUSTOMER} | The customer of the current bean
{DATE}     | The current date with the time component cleared (0:00:00)
{TIME}     | The current time
{DATETIME} | The current date with the time component set
{TIMESTAMP} | The current timestamp
{text} | The value of the `text` attribute of the current bean, if it has one
{bean:text} | Equivalent to `{text}`
{el:bean.text} | Equivalent to `{text}`
{el:stash['text']} | The value of the `text` key within the stash
{el:user.attributes['text']} | The value of the `text` user attribute
{el:DATE} | Equivalent to `{DATE}`
{el:TIME} | Equivalent to `{TIME}`
{el:DATETIME} | Equivalent to `{DATETIME}`
{el:TIMESTAMP} | Equivalent to `{TIMESTAMPE}`
{el:newDateOnlyFromLocalDate(newDateOnly().toLocalDate().plusDays(1))} | Returns the day after the the current date (tomorrow)
{el:newDateOnly().toLocalDate().getYear()} | Returns the current year as an integer
{i18n:some.bundle.key} | The value of the localised key within the resource bundle if one is defined, otherwise the value of the requested key
{role:admin.BasicUser} | True if the current user has the `admin.BasicUser` role
{stash:text} | The value of the `text` key within the stash
{user:text} | The value of the `text` user attribute
{disp:text} | The display name of the `text` attribute on the specified bean
{desc:text} | The description of the `text` attribute on the specified bean

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

### Code

Within your application code, expressions can be evaluated from the `Binder` utility class, as per the following examples:

```java
// place some values into the stash and the user attributes
CORE.getStash().put("text", "Stash");
CORE.getUser().getAttributes().put("text", "Attribute");

// retrive the values
Binder.formatMessage("{stash:text}", bean); // returns Stash
Binder.formatMessage("{user:text}", bean); // returns Attribute
```

**[⬆ back to top](#expressions)**
