---
title: "Actions"
permalink: /actions/
excerpt: "Actions"
toc: true
sidebar:
  title: "Index"
  nav: docs
---

## Actions

*Actions* represent behaviours that impact the application state or data - but the *Action* concept is independent of the way the user interacts to effect the change, whether that is via a button, a hyperlink, an API method call, via a change event (e.g. blur) on an input widget or as the result of a job.

Skyve offers *default* actions and developers can add to or override these as required.

Skyve uses the metadata declaration to infer when specific actions are appropriate. For example, if a user has *Update* privileges declared for a document, then Skyve will offer a *Save* action to the user when editing that document. Similarly if the user does not have *Update* privileges on a particular document, Skyve will hide the *Save* button as part of centralised security measures, as the action is not appropriate at that time.

Additionally, developers can provide customised view declarations to show or hide buttons or place them within specific areas within the view. Custom actions must also be included in the Skyve privileges declaration and Skyve will override visibility if a user does not have privileges for a particular action - even if it is a custom action created by the developer. 

The term *implicit* actions refers to actions that could be made available to the user based on the declaration of privileges and document relationships. For example, if a document declares a child collection, Skyve implies that the user will need to be able to *Add* and *Remove* members of that collection. Since these actions are *implicit* they do not require a declaration.

The term *default* *Actions* refers to the actions that will be available to the user unless otherwise specified by the developer.

Skyve offers the following action types:

  Action type | Description
  ----------- | -----------------------------------------------
  action      | the generic type - a custom action created by the developer that implements org.skyve.metadata.controller.ServerSideAction
  add         | the implicit Skyve action to add a member to the dataGrid/collection (deprecated)
  cancel      | the implicit Skyve action to cancel editing of the current view
  defaults    | the collection of implicit Skyve actions - specific actions will be determined by user access privileges and context (for example whether zoomed in from a dataGrid or listGrid, or "pinned"/navigated to directly)
  delete      | the implicit Skyve action to delete a document instance (not relevant for a dataGrid/collection member, not relevant unless the user has _Delete_ privileges)
  download    | a special case of the custom action that extends org.skyve.metadata.controller.DownloadAction, capable of delivery of an artefact/files to the user
  import      | a special case of custom action using Skyve's bizport capability to import and process data
  export      | a special case of custom action using Skyve's bizport capability to export data
  new         | the implicit Skyve action to create a new document instance (not relevant for dataGrid/collection member, not relevant unless the user has _Create_ privileges)
  ok          | the implicit Skyve action to *Save and return* to the list context (not relevant unless the user has _Update_ privileges)
  print       | calls the Skyve report generator to layout the view as a report
  remove      | the implicit Skyve action to remove the current document instance from the collection (relevant for dataGrid/collections, not relevant unless the user has _Delete_ privileges)
  report      | run a report (for example using the Jasper report framework)
  save        | the implicit Skyve action to *save* the current document instance (not relevant unless the user has _Update_ privileges)
  upload      | a special case of custom action that extends org.skyve.metadata.controller.UploadAction, capable of receiving (and processing) a file uploaded by the user
  zoomOut     | the implicit Skyve action to return from a collection member to the bean that owns the collection (relevant for dataGrid/collection members, not relevant unless the user has _Update_ privileges)

In addition to implicit actions provided by Skyve, developers can create Custom actions, or override Implicit action behaviour in the document *Bizlet*.

If no view has been declared for a document, Skyve will include all actions the user has access to (declared via role permissions) in the default view. When a view declaration is supplied, the view declaration will control visibility and other properties of the action button.

Action classes are located within the actions folder in a document package and correlate to action buttons in the user interface. All actions implementing *ServerSideAction* cause the view to be re-rendered.

![Trivial action example](../assets/images/actions/image135.png "Trivial action example")

### Creating a custom action

To create a new action behaviour:
* create an action class in the *actions* package within the document package that implements org.skyve.metadata.controller.ServerSideAction (or one of the other subtypes)
* declare the action in the *<actions/>* section of the view declaration
* set privileges to execute the action within the role definitions section of the *module.xml* file.

Note that actions can also be triggered by declaring *onChangeHandlers* for a specific widget in a view.

#### Declaring actions

To declare an action, include the action declaration in the `view.xml` and also declare privileges for the action in the `module.xml`. If no `view.xml` is provided, Skyve will offer the user appropriate *implicit* actions based on the user context.

When a view declaration is supplied, the action must be declared in the
*actions* section of the *view.xml*.

```xml
  <actions>
  	<defaults/>
    <action displayName="Calculate" className="Calculate" inActionPanel="true" />
  </actions>
</view>
```

_Example action section of a view definition_

The action element of the view definition indicates the *className* of
the action as well as the *displayName* (button text). Action properties
are explained in full in the previous chapter.

Note that the action section of a view definition may also include implicit
actions, for example, with the `<defaults/>` tag.

Once the custom action is declared in the view, Skyve will recognise that the custom action exists, however unless privileges to the custom action are declared in the module, the custom action will remain unavailable to the user.

To make the custom action available to the user, the action must be declared in the role privileges declaration in the `module.xml` as follows:

```xml
<roles>
	<role name="Manager">
		<description>Manages offices and staff.</description>
		<privileges>
			<document name="Office" permission="CRUDC" />
			<document name="Staff" permission="CRUDC" />
			<document name="MyStatus" permission="_____" />
		</privileges>
	</role>
	<role name="StaffMember">
		<description>A staff member.</description>
		<privileges>
			<document name="Office" permission="_R__C" />
			<document name="Staff" permission="_RU_C" />
			<document name="MyStatus" permission="_____" >
				<action name="UpdateMyStatus"/>
			</document>
		</privileges>
	</role>
</roles>
``` 

In the above example, the custom action `UpdateMyStatus` will be made available only to users with the role *StaffMember*. If a user does not have the role *StaffMember* assigned, Skyve will override any visibility controls on that action (as declared in the view) to hide the action from the user.

### Overriding *implicit* actions

Developers may override *implicit* actions by overriding the specific action type in the Bizlet class corresponding to the relevant document.

Skyve also provides the opportunity for developers to execute additional code prior to the usual lifecycle events. For example, developers may either override the `preSave()` method in the Bizlet, or override the execution of the *implicit* `Save` action in the view.

```java
/**
 * Standardise the contact on press of Save button.
 */
@Override
public Contact preExecute(ImplicitActionName actionName, Contact bean, WebContext webContext) throws Exception {
  if(ImplicitActionName.Save.equals(actionName)) {
    AdminUtil.standardiseContact(bean);
  }

  return bean;
}
```

_Example customisation of the Save implicit action_

In the above example, the *implicit* action *Save* (i.e. when the user
presses the *Save* button) is customised to determine and set the state
of the *eligibility* attribute. This code will be executed prior to the
usual `preSave()` lifecycle event.

If the *implicit* action is overridden in this way, the developer code will only execute when the action is initiated by user gestures. 

By contrast, if the `preSave()` lifecycle event is overridden in the Bizlet class, the developer code will execute whenever the bean is saved irrespective of whether the save is occurring as the result of user activity, a job, an API method call or if inferred as part of a cascaded save when participating in a relationship.

A good practice approach will override only at the minimum level required - to avoid unnecessary processing and impacts not specifically considered by the developer.

### Events

OnChange events can trigger custom actions using the `<server/>` tag.
 
```xml
<combo binding="orgName" disabled="locked">
	<onChangedHandlers>
		<server action="UpdateModule" />
	</onChangedHandlers>
</combo>
```

_Example: the combo for *orgName* will call the custom action *UpdateModule* when changed_

The specific change events available depend on the widget type, for more details refer to [Views](./../_pages/views.md)

### The preRerender event

Developers may override the `preRerender()` event which is always the final event prior to the view being *rerendered*, including after any default or custom actions.

In addition, developers may declare that the view be *rerendered* on change of a widget.

For example, a view may include a widget declaration as follows:

```xml
<form border="true" borderTitle="Web">
	<column responsiveWidth="1" />
	<column responsiveWidth="1" />
	<column />
	<row>
		<item>
			<checkBox binding="httpTrace" triState="false">
				<onChangedHandlers>
					<rerender />
				</onChangedHandlers>
			</checkBox>
		</item>
	</row>
</form>
```

In the simple case, when the checkBox value is changed, the view will be rerendered - conditions declared in the `document.xml` will be re-evaluated and this may effect visibility controls in the view.

However, the developer may also wish to override this by overriding the `preRerender()` method in the Bizlet class - which will be called just before the view is rerendered. The `preRerender()` method provides the `source` parameter - which is the binding name of the widget which caused the rerender event. 

For example, the display of *restore* action hints in the admin.DataMaintenance view is overridden in the `DataMaintenanceBizlet` class as follows:

```java
@Override
public void preRerender(String source, DataMaintenance bean, WebContext webContext) throws Exception {

	if (DataMaintenance.restorePreProcessPropertyName.equals(source)) {

		String instructionHint = null;
		RestorePreProcess pre = bean.getRestorePreProcess();
		if (pre != null) {

			switch (pre) {
				case noProcessing:
					instructionHint="Use this option when you've created your database from scratch (or with the bootstrap) and you've let the Skyve create all DDL. You know the backup is from the same version and the schema is synchronised (matches the metadata).";	
					break;
				case createTablesFromBackup:
					instructionHint="Use this option when you've created a empty schema (manually or scripted).";
					break;
				case createTablesFromMetadata:
					instructionHint="Use this option when you have a empty schema but the backup application version doesn't match your version.";
					break;
				case deleteExistingTableDataUsingMetadata:
					instructionHint="Use this option when the backup is from the same version of the application and your data size is not large (i.e. just delete the data and then run the restore.)";
					break;
				case dropTablesUsingBackupDropsqlRecreateTablesFromBackupCreatesql:
					instructionHint="Use this option when your schema matches the application version of the backup (maybe your previous attempt to restore failed). You cant drop the schema without stopping the server and if you do that, you can't log in any more without restoring. Since the backup/restore only looks after tables under Skyve control, it could be that extra tables have constraints that you need to drop or other issues that you only find after trying to restore.";
					break;
```

In this way, the `preRerender()` method can be used by the developer as a central location for all code relating to results or basic user activity in the view which impact on how the view is rendered.

**[⬆ back to top](#actions)**

---
**Next [Reports](./../_pages/reports.md)**  
**Previous [Routing and rendering](./../_pages/routing.md)**
