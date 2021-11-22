---
title: "Extending Skyve"
permalink: /appendix-extending-skyve/
excerpt: "Extending and customising a Skyve application beyond the out of the box experience."
toc: true
sidebar:
  title: "Index"
  nav: docs
---

For speed of development, maintainability and consistency, Skyve provides a default experience to applications out of the box. Unlike some other no-code and low-code platforms however, Skyve is a complete enterprise platform and there are no real limitations to how much can be customised if you are willing to put in the effort.

## Theme Selection

Skyve applications support switching between different themes for its responsive renderer, and between some built-in options for its desktop renderer. 

Skyve includes support for two premium themes for the responsive renderer, Ultima and Ecuador, but due to licence limitations these cannot be included with open-source applications without purchase of a separate theme licence.

Theme selection can be made using the UxUi file (`router/UxUis.java`) within your Skyve application. You can preview the premium themes and colour selections using [Skyve Foundry](https://foundry.skyve.org/).

## REST Services

Skyve provides an automatic REST interface that can be consumed by other applications for every document. For security reasons, this is disabled by default for new applications but can be easily enabled via a configuration change in the application `web.xml`.

Please refer to the [Skyve Cookbook](https://github.com/skyvers/skyve-cookbook) for specific instructions on enabling and working with REST services for your Skyve application.

The cookbook also contains information on working with and consuming 3rd party REST APIs from your Skyve application.

## Custom Javascript and CSS

Custom javascript libraries and your own javascript can be inserted into a Skyve application at certain points in each renderer.

### Responsive Renderer

### Desktop Renderer

To include a javascript file to be accessible in every page in your desktop renderer, modify your `home.xhtml` file located in `/src/main/webapp/desktop/`. The desktop renderer runs as a single-page javascript (SPA) application, any additional javascript or CSS includes in this file will be accessible within every view within your application.

To use or work with javascript within a specific page, the desktop renderer provides an `<inject>` widget. An example of working with this widget is included in the `UserDashboard` document in the `admin` module's `edit.xml` file:

```xml
  <inject>
    <script>
      <![CDATA[
        SKYVE.Util.loadCSS('pages/css/admin.css?v=' + SKYVE.Util.v);
      ]]>
    </script>
  </inject>
```

The `v=` in the path to the `admin.css` file appends a version number, which prevents the browser caching previous versions of the CSS between Skyve releases.

A more detailed example of using the `inject` tag can be found within the [Skyve Cookbook](https://github.com/skyvers/skyve-cookbook).

_Note: changes to `home.xhtml` will be overwritten when you upgrade your application to a new Skyve version using the assemble process. It is important to review and re-apply any customisations to this file when upgrading._

### Responsive Renderer

To include a javascript file to be accessible in every page in your responsive renderer, modify the `template.xhtml` file located in your application's theme folder. By default, Skyve applications use the **Editorial** theme for the responsive renderer. The editorial template is located in `/src/main/webapp/WEB-INF/pages/templates/editorial/`. The reponsive renderer uses the template based on current `UxUi` chosen for the logged in user. This may be different based on their device (desktop/laptop/tablet/phone), and can be specified in your application's `router/UxUis.java` file. How each template is selected can be influenced in `router/DefaultUxUiSelector.java`.

The `inject` tag is not available in the responsive renderer, to use specific javascript or CSS within a view you must create a custom page which will be used instead. The Skyve router will route to the default UxUi page for the type of view being accesses (e.g. edit, list). Skyve supports a root level router (`/src/main/router/router.xml`), as well as per-module routers (e.g. `/src/main/router/admin/router.xml`). To prevent your custom routes from being overwritten when upgrading Skyve platform versions, it is recommended to use a module router.

If we wanted to customise an edit view for a document (`edit.xml`), the corresponding responsive template would be located in `/src/main/webapp/external/edit.xhtml`. This is defined by the default route in the root level router: 

```xml
	<uxui name="external">
    ...
		<route outcome="/external/edit.xhtml">
			<criteria webAction="e" />
		</route>
    ...
  </uxui>
```

Instead of modifying this file, which would affect all pages, you can duplicate `/external/edit.xhtml` into a new directory, e.g. `myapp` located at the same level within your application hierarchy, i.e. `/src/main/webapp/myapp/custom.xhtml`. To make Skyve route to your new custom page, you need to add a new route with the criteria of when to select this route, e.g.:

```xml
	<uxui name="external">
    ...
    <route outcome="/myapp/custom.xhtml">
			<criteria webAction="e" module="myapp" document="MyDocument" />
		</route>
		<route outcome="/external/edit.xhtml">
			<criteria webAction="e" />
		</route>
    ...
  </uxui>
```

In the above example, the criteria has three parameters:

Parameter | Desscription
---|---
`webAction` | The action which causes this criteria to match. The most common are `e` for an edit/detail view, `l` for list view (e.g. when being selected from the menu), or `m` for a map view.
`module` | Which module the document is located in
`document` | Which document to match on

If the criteria match (multiple criteria can be specified for the same route), Skyve will attempt to use the render the view using your custom page in `myapp/custom.xhtml` instead of the default catch-all `edit.xhtml`. Custom CSS/Javascript and other markup can now be specified in this custom page and this will be used when loading views for your specified module and document instead.

Note, the `<s:view>` tag within the xhtml pages is the insertion point for your Skyve view. View markup from your document's `edit.xml` will be inserted at this point within the rendered HTML for your page.

_Note: changes to `template.xhtml` will be overwritten when you upgrade your application to a new Skyve version using the assemble process. It is important to review and re-apply any customisations to these files when upgrading._

## Custom Widgets

While it is possible to add custom widgets to both of Skyve renderer's, it is much easier to add them to the responsive renderer.

Please refer to the [Skyve Cookbook](https://github.com/skyvers/skyve-cookbook) for an example on adding a custom widget to your Skyve application using the responsive renderer. Contact the Skyve team if you require a custom widget in the desktop renderer.

**[⬆ back to top](#extending-skyve)**