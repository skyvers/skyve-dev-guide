---
title: "Geometry and geospatial"
permalink: /geospatial/
excerpt: "Working with geometries, geospatial and maps in Skyve"
toc: true
sidebar:
  title: "Index"
  nav: docs
---

## Geometry and geospatial

Skyve provides rich map and geospatial features, including the ability to use mapping and combined scalar data and geospatial filtering - without requiring developer code. Developers can then provide additional code to extend applications to achieve genuinely sophisticated capabilities.

Your application configuration `.json` file allows you to customise behaviour for your region and typical use case, including 
* the default map centre lat-long,
* zoom level, and
* map layer(s) to load for each map implementation.

By default, spatial information is represented using <a href="https://en.wikipedia.org/wiki/Well-known_text">Well Known Text</a>

### Map APIs

Skyve provides two map API options - Leaflet (OpenStreetMap) and Google Maps. 

Skyve applications default to using Leaflet (OpenStreetMap).

You can switch between the two APIs at any time by changing the application properties .json file settings and redeploying your application - without requiring any changes to developer code. 

NOTE: _Whichever technology you choose, it is *your* responsibility to ensure you comply with accreditation and licencing requirements within your application._

#### Leaflet (OpenStreetMap)

OpenStreetMap&reg; is open data, licensed under the Open Data Commons Open Database License (ODbL) by the OpenStreetMap Foundation (OSMF).

According to OpenStreetMap - "you are free to copy, distribute, transmit and adapt our (i.e. _their) data, as long as you credit OpenStreetMap and its contributors. If you alter or build upon our data, you may distribute the result only under the same licence. The full legal code explains your rights and responsibilities." 

```json
// Map Settings
map: {
	type: "leaflet",
	layers: "[L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {maxZoom: 19})]",
	centre: "POINT(0 0)",
	zoom: 1
},
```

You can customise Skyve applications to take advantage of other map APIs however these APIs are not included in the open-source Skyve distribution. For assistance, contact us at skyve.org to discuss detailed steps for other integration options.

#### Google Maps

Skyve provides a Google maps API integration option, however it is your responsibility to consider usage, licencing and billing implications when used in your application. Refer to [Google](https://cloud.google.com/maps-platform/terms/) terms.

If you use google maps you should obtain an API key (googleMapsV3Key) and specify that key in the api section of the `.json` application configuration file.

To take advantage of Skyve's inbuilt map features with the default Google maps API, you must provide a valid [Google maps API key](https://developers.google.com/maps/documentation/javascript/get-api-key) in the Skyve application `.json` file.

```json
// Map Settings
map: {
	type: "gmap",
	layers: "google.maps.MapTypeId.ROADMAP",
	centre: "POINT(0 0)",
	zoom: 1
},
// API Settings
api: {
	googleMapsV3Key: "AIzaaaaaDDDDAAA-CMqvI0TIAPlye9g4Wr4Dg",
	googleRecaptchaSiteKey: null,
	ckEditorConfigFileUrl: null
},
```

### Overview

Skyve provides 3 map widgets, 2 are for geo-spatial input and 1 is for display only:
* `geometry` widget is a text field that has a map popup via a button. This widget has focus, blur and changed event handling. The widget can be disabled.
* `geometryMap` widget is an inline map that can be used for input. This widget has changed event handling. The widget can be disabled and this removes the geolocation and drawing controls.

Both of these widgets can constrain the type of geometry input allowed via the `type` property. 

Both of these widgets have a geo-locate tool on the map for marking the current GPS position (if HTML5 Navigation services are available).

* the `map` widget is a widget that allows rendering of map data. This widget can be utilised from the map menu item where a query with a geometry binding can be displayed on one of these widgets in a map view. 

This widget can also be used with a map _model_.

Attributes of interest include:-
* `showRefreshControls` - shows a spinner with number of seconds to automatically refresh and a checkbox to switch refreshing on and off.
* `refreshTimeInSeconds` - The refresh frequency in seconds
* `eager` loading (default) - In `eager` mode, the world extents are sent to the map `model` and the map expects to receive all data back to display. Zooming and panning display data stored on the client. 
* `lazy` loading - In `lazy` mode, the map viewport extents are sent to the map `model` so the `model` can return just the data that is contained in the current view. After the map is zoomed or panned, the `model` is called to get new data, whereas, in `eager` mode, the map `model` code won't be re-called.

A map model has one method to implement which takes a geometry called `mapBounds` that is either the world extents for an `eager` loading map, or the viewport bounds for a `lazy` loading map. If the bounds cross over the anti meridian line, the geometry will be a multi-geometry with 2 polygons representing the bounds intersected by the anti meridian line.

It is routine to use a query with an intersects filter operator to allow the persistence layer to return only the features that are relevant to the `mapBounds`, or to use the geometry methods to test - e.g. `mapBounds.intersects(myGeometry)`.

It is usually prudent to test both ways if using a `Persistence.filter` as some databases will include false positives in their results depending on the size of their quad-tree indexes, but the geometry methods test geometrically.

The map result has a list of map items with `bizId`, `bizModule`, `bizDocument` included for zooming in on a feature from the map and each map item contains a list of Map Features that will represent the item on the map. Icons, stroke and fill colours/opacity can be set for each feature within the model.

A click on any feature in an item will zoom into that item and display information from the item in the balloon on the map.

There are a couple of implementations of MapModel:
* `DefaultMapModel` includes addItem method that creates an item with 1 feature from a geometry derived from a binding in a bean, only if it is within the mapBounds.
* `DocumentQueryMapModel` displays map items from a document query.
* `ReferenceMapModel` displays map items from a document reference - association, collection, inverses.

### Geometry attribute type

Skyve provides a native `geometry` attribute type for geometrical and geospatial objects, shapes and locations. The `geometry` attribute takes advantage of Hibernate spatial dialects for storage and searching of spatial items.

Attributes storing geometric or geospatial points (locations) or shapes are declared in the `document.xml` as follows:

```xml
<geometry name="location">
	<displayName>Location</displayName>
</geometry>
```

### Map menu

The Map menu item provides map-based interactions to locate records spatially and then to navigate to those records via the info-window popup. 

To provide map-based navigation, declare a `map` menu item in your application `module.xml` file as follows:

```xml
<map name="Staff Locations" document="Staff" geometryBinding="location" >
	<role name="Manager" />
	<role name="StaffMember" />
</map>
```

The `geometryBinding` parameter specifies which document attribute is being displayed. In the above example, the map will display `geometry` data for the *location* attribute of the *Staff* document.

![Map menu](../assets/images/geospatial/map-menu.png "Map menu")

The info-window will display the record bizKey (by default) with a `Zoom` action - allowing the user to zoom into the corresponding record detail view.

The map menu item supports the following options:

 Option | Description
 ---------|---------
 document | all records will be displayed based on the user permissions, based on the specified `geometryBinding` attribute
 query | all records will be displayed based on the user permissions and query declaration included in the `module.xml`
 model | all records will be displayed based on the user permissions and model code
 refreshTimeInSeconds | not yet implemented - the time in seconds to automatically refresh the map
 showRefreshControls | not yet implemented - whether to include the refresh controls as provided by the map service

Note that to customise the map display options, including the info-window, requires a developer to create a map *model* (see below). 

### Spatial filters in the list menu item

Where `geometry` attributes are included in a list menu item, Skyve will provide suitable filters for that attribute (currently only supported in the `desktop` rendering mode). The simple filter line provides the geometry input widget, however the advanced filter options provide map-based rubber-band selection tool. 

![Spatial filtering in lists](../assets/images/geospatial/spatial-filter-for-lists.png "Spatial filtering in lists")

Note that Skyve's list control supports spatial filter criteria used as well as other criteria for combined effect.

### Geometry and geospatial widgets

The `geometry` values are displayed as <a href="https://en.wikipedia.org/wiki/Well-known_text">Well Known Text</a> by the default `geometry` widget.

![Default geometry widget](./../assets/images/geospatial/default-geometry-widget.png "Default geometry widget")

The `geometry` widget provides a map-based data entry tool option with basic drawing tools. 

[Map-based data entry tool](./../assets/images/geospatial/geometry-map-based-data-entry-tool.png "Map-based data entry tool")

### Map widget

To take advantage of the `map` widget, you must declare the `map` widget in the view *and* create a map *model* for the map.

The map widget is declared as follows:
```xml
<map modelName="OfficeMap" percentageWidth="100" percentageHeight="100" />
```

The `modelName` specifies the model class which controls the display options for the map.

Note that the `map` widget is a first-level container and is not specified within a `form` container. However, it may be contained within `vbox`, `hbox` and `tab` layout containers in the view.

### Map model

A map model is a java class which extends `org.skyve.metadata.view.model.map.MapModel` and must implement a `getResult()` method returning a `org.skyve.metadata.view.model.map.MapResult`.

According to the convention, the `model` java class must be declared within the corresponding document package, and located in a `models` package as shown:

![Model class location](../assets/images/geospatial/document-model-structure.png "Model class location")

MapModel provides the `getBean()` which returns the context bean (the bean for the view in which the model will be displayed) - if a bean context has been set. If a MapModel is specified as the `model` for a map menu item, `getBean()` will return null.

```java
package modules.whosinIntegrate.Office.models;

import java.util.ArrayList;
import java.util.List;

import modules.whosinIntegrate.domain.Office;
import modules.whosinIntegrate.domain.Staff;
import modules.whosinIntegrate.domain.Staff.Status;

import org.locationtech.jts.geom.Geometry;
import org.skyve.CORE;
import org.skyve.metadata.view.model.map.MapFeature;
import org.skyve.metadata.view.model.map.MapItem;
import org.skyve.metadata.view.model.map.MapModel;
import org.skyve.metadata.view.model.map.MapResult;
import org.skyve.persistence.DocumentQuery;
import org.skyve.persistence.Persistence;

public class OfficeMap extends MapModel<Office> {
	private static final long serialVersionUID = 7880044512360465355L;

	@Override
	public MapResult getResult(Geometry mapBounds) throws Exception {
		Office office = getBean();		
		
		List<MapItem> items = new ArrayList<>();

		// add the office feature
		Geometry boundary = office.getBoundary();
		if (boundary != null) {
			if (mapBounds.intersects(office.getBoundary())) {
				MapItem item = new MapItem();
				item.setBizId(office.getBizId());
				item.setModuleName(office.getBizModule());
				item.setDocumentName(office.getBizDocument());
				item.setInfoMarkup(office.getBizKey());
				
				MapFeature feature = new MapFeature();
				feature.setGeometry(office.getBoundary());
				feature.setFillColour("#FFFF00"); //yellow
				feature.setFillOpacity("0.8");
				feature.setStrokeColour("#BDB76B"); //dark khaki
				item.getFeatures().add(feature);

				items.add(item);
			}
		}
		
		// add the staff features
		if (office.isPersisted()) {
			Persistence p = CORE.getPersistence();
			DocumentQuery q = p.newDocumentQuery(Staff.MODULE_NAME, Staff.DOCUMENT_NAME);
			q.getFilter().addEquals(Staff.baseOfficePropertyName, office);

			List<Staff> staff = q.beanResults();
			for (Staff member : staff) {
				if (mapBounds.intersects(member.getLocation())) {
					MapItem item = new MapItem();
					item.setBizId(member.getBizId());
					item.setModuleName(member.getBizModule());
					item.setDocumentName(member.getBizDocument());
					
					Status memberStatus = member.getStatus();
					StringBuilder markup = new StringBuilder(64);
					markup.append(member.getContact().getName());
					if (memberStatus != null) {
						markup.append("<br/>").append(memberStatus.toDescription());
					}
					item.setInfoMarkup(markup.toString());
					
					MapFeature feature = new MapFeature();
					feature.setGeometry(member.getLocation());
					feature.setIconRelativeFilePath("icons/document/user16.png");
					feature.setIconAnchorX(Integer.valueOf(8));
					feature.setIconAnchorY(Integer.valueOf(8));
					item.getFeatures().add(feature);
					
					items.add(item);
				}
			}
		}
		
		return new MapResult(items, null);
	}
}
```

The above example constructs a `List` of `MapItem` based on the boundary of the Office bean and the staff associated with that office.

The `MapItem.infoMarkup` allows html markup for the map info-window popups such as the example shown below:

![Markup info-window](../assets/images/geospatial/markup-info-window.png "Markup info-window")

The example above takes advantage of the Skyve `content` servlet to include an image into the info-window display, similar to the following:

```java
StringBuilder markup = new StringBuilder();
markup.append("<table><tbody><tr><td>");
markup.append("<p><h2>").append(bean.getName()).append("</h2></p>");
markup.append("<p><i>").append(bean.getDescription()).append("</i></p>");
markup.append("</td><td>");
markup.append("<img src=\"content?_n='").append(bean.getImage()).append("'&_doc=sites.Site&_b=image&_w=32&_h=32\"/>");
markup.append("</td></tbody></table>");

item.setInfoMarkup(markup.toString());
```

For more information on the Skyve `content` servlet see [Content](./../_pages/working-with-content.md).

### Spatial queries

Provided a geospatial hibernate dialect is selected for the application, Skyve's document query supports combining spatial and other filter criteria for `Bizlet`, Extension class, `action` or other general application code.

```java
/**
 * 
 * Return a List of staff whose current location is within the office boundary
 * and whose status is `at lunch`
 * 
 * @param office
 * @return
 */
public static List<Staff> getStaffOnSite(Office office) {

	Persistence pers = CORE.getPersistence();
	DocumentQuery q = pers.newDocumentQuery(Staff.MODULE_NAME, Staff.DOCUMENT_NAME);
	q.getFilter().addWithin(Staff.locationPropertyName, office.getBoundary());
	q.getFilter().addEquals(Staff.statusPropertyName, Status.atLunch);
	
	List<Staff> results = q.beanResults();
	
	return results;
}
```

The document query filter object provides the following spatial criteria options:

Option | Description
-------|------------
addContains(binding, geometry), addNullOrContains(binding, geometry) | where the attribute specified by the `binding` is completely enclosed within the specified `geometry`  
addCrosses(binding, geometry), addNullOrCrosses(binding, geometry) | where the attribute specified by the `binding` crosses the specified `geometry`
addDisjoint(binding, geometry), addNullOrDisjoint(binding, geometry) | where the attribute specified by the `binding` does not cross, intersect nor is contained by the specified `geometry`
addEquals(binding, geometry), addNullOrEquals(binding, geometry) | where the attribute specified by the `binding` is concurrent or the same as the specified `geometry`
addIntersects(binding, geometry), addNullOrIntersects(binding, geometry) | where the attribute specified by the `binding` intersects the specified `geometry`
addOverlaps(binding, geometry), addNullOrOverlaps(binding, geometry) | where the attribute specified by the `binding` overlaps the specified `geometry`
addTouches(binding, geometry), addNullOrTouches(binding, geometry) | where the attribute specified by the `binding` touches some part of the specified `geometry`
addWithin(binding, geometry), addNullOrWithin(binding, geometry) | where the attribute specified by the `binding` is contained within the specified `geometry`


**[⬆ back to top](#geometry-and-geospatial)**

---
**Next [Communication](./../_pages/communication.md)**  
**Previous [Images](./../_pages/images.md)**
