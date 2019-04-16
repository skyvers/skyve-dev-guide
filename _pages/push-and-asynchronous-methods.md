---
title: "Push and asynchronous methods"
permalink: /push-and-asynchronous-methods/
excerpt: "Push and asynchronous methods"
toc: true
sidebar:
  title: "Index"
  nav: docs
---

## Push and asynchronous methods

### Push

Skyve provides methods to support pushing to user conversations. 

Push can be useful for:
* notifying a user that a long running job is complete
* notifying a user of another action occurring in the system which may impact on their current activity
* chaining another action to occur after an action is complete
* updating the current user view as a result of one of the above

Push methods | Description
-------------|------------
growl() | display a popup message to the user(s) 
message() | display a modal dialog with ok button
rerender() | cause the current view to rerender
user() | limit push to a specified user
execute() | execute an action

For example, to notify the current user using a growl message that Task X has failed:

```java
User userToNotify = CORE.getUser();
EXT.push(new PushMessage().user(user).growl(MessageSeverity.error, "Task X has failed"));
```

In the above example, the `user()` method is used to limit the push to the specified user.

![Example growl](../assets/images/push-and-asynchronous-methods/growl.png "Example growl")

The `WebContext` also provides a `growl` convenience method, for example:

```java
webContext.growl(MessageSeverity.info, SENT_SUCCESSFULLY_MESSAGE);
```

The `WebContext` `growl()` method will only provide a growl to the webContext, whereas `Ext.push` can provide a growl to all currently logged in users (or a specified user). 

#### Broadcast message

The Push method can be used to broadcast messages, growls or rerender to all current conversations, for example:

```java
Ext.push(new PushMessage().growl(MessageSeverity.warn, "System will be offline for maintenance in 5 minutes..."));
```

### Rerender considerations

When using the `rerender()` method for push actions, the developer needs to consider the impact this may have on the user, who may experience disruption - for example, if they are in the process of data entry.

Consider the use case of a simple team discussion/chat application. Users post messages and while they do this, other user messages appear in the chat history. 

The following action would post a message to the topic history and update the history of discussion items for all users.

```java
@Override
public ServerSideActionResult<Topic> execute(Topic bean, WebContext webContext) throws Exception {
	
	TopicMessage tm = TopicMessage.newInstance();
	tm.setTopic(bean);
	tm.setMessageText(bean.getNewMessage());
	tm.setPosted(new Timestamp());
	tm.setUser(ModulesUtil.currentAdminUser());
	
	CORE.getPersistence().upsertBeanTuple(tm);
	
	EXT.push(new PushMessage().rerender());

	return new ServerSideActionResult<>(bean); // stay on the same form
}
```

To avoid disrupting ongoing user data entry (another user entering their next chat item), you can take advantage of PrimeFaces `remoteCommand` tag and `update` property in a custom xhtml page, as follows:

```xml
<p:remoteCommand name="pushRerender" actionListener="#{skyve.rerender('push', false)}" process="@this" update="msgHistory" />

<s:view module="#{skyve.bizModuleParameter}"
			document="#{skyve.bizDocumentParameter}"
			managedBean="skyve" widgetId="topicHistory" id="msgHistory" />

<s:view module="#{skyve.bizModuleParameter}"
			document="#{skyve.bizDocumentParameter}"
			managedBean="skyve" widgetId="topicAdd" />
```

In the above extract, the customised view contains two view tags - each with a unique `widgetId` drawn from the view declaration in the document view `edit.xml`. The `remoteCommand` tag specifies that push messages will only `update` the component with id=`msgHistory`. This would allow the user to continue entering data in the `topicAdd` view component without disruption while the `topicHistory` section of the view is updated.


Note that the `Ext.push` method does not specify a user - and so is effectively a broadcast push to all user conversations.

For more information on `view` declaration and `update`, see 
[View update property](./../pages/views.md#update-property "View update property").

**[⬆ back to top](#push-and-asynchronous-methods)**

---
**Next [Working with content](./../_pages/working-with-content.md)**   
**Previous [Skyve persistence mechanisms](./../_pages/skyve-persistence-mechanisms.md)**
