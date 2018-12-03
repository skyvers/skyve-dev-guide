---
title: "Bizlet and extension classes"
permalink: /bizlets/
excerpt: "Bizlet and extension classes"
toc: true
sidebar:
  title: "Index"
  nav: docs
---

## Bizlets

A *Bizlet* is a class related to a document which extends default bean
behaviours and extends org.skyve.metadata.model.document.Bizlet

*Bizlets* can reference other classes without restriction and may be the
point of connection to highly specific application code.

The intention of the *Bizlet* is to contain only code relevant and
contextualised to a specific document, and so *Bizlet* classes are
located within the document package. Cross-document code or module-level
code should be included in a class created at the module level or
higher.

![Bizlet file location](../assets/images/bizlets/image92.png "Bizlet file location within the document package")

Bizlet classes only contain the minimum required code and methods, e.g.
only the necessary overrides. No code is required (in fact no Bizlet
class is required) for generic or default behaviour.

A Bizlet class may override bean-lifecycle methods.

![Bizlet method override](../assets/images/bizlets/image93.png "Bizlet method override")

### Lifecycle

Lifecycle event timing is as follows:

#### Bean level:

  Event | Description
  ----- | -----------
  newInstance() | called after instantiation through the *document.newInstance()*<br><br>The *newInstance()* method is commonly overridden to set application default values for the document.<br><br>If an exception occurs the bean will not be instantiated.
  postLoad() | called after instantiation and population of data store values<br><br>The *postLoad()* method can be called to recalculate values which are visible in a document list or detail view.<br><br>If an exception occurs, the associated view will open with an error dialog.
  preSave() | called before flushing the values to the data store or User press of \[Save\] button (before document validation)<br><br>The *preSave()* method can be overridden to ensure application rules or calculated fields are up to date before the bean is persisted.<br><br>If an exception occurs at this stage, the transaction will roll back and the change will not be saved.
  validate() | called after *preSave()* (and after document validation) but before flushing the values to the data store<br><br>The *validate()* method is commonly overridden to include additional complex document validation (other than implicit validation of requiredness etc.).<br><br>If an exception occurs at this stage, the transaction will roll back and changes will not be saved.
  postSave() | called after flushing values to the data store and after all integrity and validation checks have been performed<br><br>The *postSave()* method might be overridden to perform an action only on a successful save of the bean, e.g. to send a confirmation email.<br><br>If an exception occurs at this stage, the transaction will roll back and changes will not be saved.
  preDelete() | called before deletion from the data store<br><br>The *preDelete()* method might be overridden to perform a logical check that the record can be deleted, according to rules which cannot be enforced by a simple constraint.<br><br>If an exception occurs at this stage, the transaction will roll back and the data will not be deleted.

_Bean level events_

#### User Interface level:

  Event | Description
  ----- | -----------
  getConstantDomainValues() | called when rendering the pertinent field (including in list view) - Constant domain values may be baked into the view and remain invariant for the system life. 
  getVariantDomainValues() | called when rendering the pertinent field (including in list view) - Variant domain values are guaranteed to be evaluated each request-response cycle.
  getDynamicDomainValues() | called when rendering the pertinent field (including in list view) - Dynamic domain values are guaranteed to be evaluated as for variant values, except that the bean is provided to the method for value generation.
  preExecute() | called before an implicit action is executed
  preRerender() | called before the rerender action is executed (immediately before a view is re-rendered after another action)

_User Interface level events_

### Implicit Actions

Skyve provides a number of implicit actions which do not require
developer code.

In each case overriding the *preExecute*() method for an
*ImplicitActionName* gives the developer the opportunity to add business
logic in anticipation of each action type.

The *ImplicitActionName* is an enumeration of Skyve actions.

Implicit Action | Definition
----------------|-----------
Defaults | Produce default button for the view type
Ok | OK on edit view
Save | Save on edit view
Delete | Delete on edit view
Add | Add on child edit view
ZoomOut | Change on child edit view
Cancel | Cancel on edit view and child edit view
Remove | Remove on child edit view
New |  Add new item from a list view
Edit | Edit an item from a list view
Report | Fire up a report dialog from a button
Navigate |Navigate to a binding within a conversation
Import | Import data using the BizPort capability
Export | Export data using the BizPort capability
Download | Create and stream a file for Download
Upload | Upload and process a file
Print |	Prints the current view

_ImplictActionName enumerated values_

### Extension classes

Skyve allows for extension of the automatically generated domain classes for each document, 
enabling developers to extend default skyve code generation.

Extension classes can be used for domain-specific business logic to override and further enrich 
the domain model provided by Skyve (see anaemic versus rich domain models).

```
public class ControlPanelExtension extends ControlPanel {
	private static final long serialVersionUID = -6204655500999983605L;

	public void trapException(Exception e) {
		StringWriter sw = new StringWriter(512);
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		setResults(sw.toString());
	}
	
	/**
	 * Overriden to escape {, < & >.
	 * Add a new line out the front to line up all lines to the left of the blurb.
	 */
	@Override
	public void setResults(String results) {
		if (results == null) {
			super.setResults(null);
		}
		else {
			super.setResults('\n' + results.replace("{", "\\{").replace("<", "&lt;").replace(">", "&gt;"));
		}
	}
	
	@Override
	public Boolean getBizletTrace() {
		return Boolean.valueOf(UtilImpl.BIZLET_TRACE);
	}
```
_Example of convenience and overridden methods in an Extension class_

Domain generation uses the extension class (if it exists).

By convention, the extension class for a document is located within the document package and 
named corresponding to the document, for example:

![](../assets/images/bizlets/extension-class-location.png "Location and naming of an extension class within the document package")


**[⬆ back to top](#contents)**

---
**Next [Views](./../_pages/views.md)**  
**Previous [Converters](./../_pages/converters.md)**
