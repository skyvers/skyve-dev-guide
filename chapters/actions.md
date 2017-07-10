## Actions

### Contents

* **[Chapter 12: Actions](#actions-2)**
   * [OnChange Event Actions (Client-side Events)](#onchange-event-actions-client-side-events)
  
*Implicit Actions* are actions which are provided by Skyve by default based on the context of the current user gesture. By default, a Skyve view will contain a default collection of Implicit actions.
Skyve provides the following Implicit Actions:

 ImplicitActionName|Description|
---|---|
 DEFAULTS | A way to refer to the default set of *Implicit* actions for the view type and gesture context |
 OK | *OK* button pressed by user while viewing the document edit view |
 Save | *Save* button pressed by user while viewing the document edit view |
 Delete | *Delete* button pressed by user while viewing the document edit view |
 Add | *Add* button pressed by user while viewing the child document edit view |
 ZoomOut | *Zoom Out* button pressed by user while viewing the child document edit view |
 Cancel| *Cancel* button pressed by user either at document or child-document edit view |
 Remove	| *Remove* button pressed by user while viewing the document edit view |
 New | user adds a new item to the list view |
 Edit | user edits an item from the list view |
 Report	| user presses a Report action button (useful for dialog and prompts before the report is opened) |
 Navigate |	user navigates to a binding within a conversation |
 BizImport | user attempts to import data using the *Bizport* capability |
 BizExport |	user attempts to export data using the *Bizport* capability |
 *Table 20 Implicit Actions*
 
In addition to implicit actions provided by Skyve, developers can create Custom actions, or override Implicit action behaviour in the document *Bizlet*.

To create a new action behaviour, developers create an action class and set permissions to execute the action within the role definitions section of the *module.xml* file. 

When a user has access to execute an action (declared via role permissions) Skyve will generate a button by default in the detail view. When a view definition is supplied the view definition will control visibility and other properties of the action button.

The trivial *ServerSideAction* causes the view to be refreshed.

Action classes implement *ServerSideAction* and are located within the actions folder in a document package and correlate to action buttons in the user interface. 

![Figure 64](media/image100.png "Figure 64 Example trivial action")
