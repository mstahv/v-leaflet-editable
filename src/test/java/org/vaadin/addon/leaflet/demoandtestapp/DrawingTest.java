package org.vaadin.addon.leaflet.demoandtestapp;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import org.vaadin.addon.leaflet.*;
import org.vaadin.addon.leaflet.editable.FeatureDrawnEvent;
import org.vaadin.addon.leaflet.editable.FeatureDrawnListener;
import org.vaadin.addon.leaflet.editable.FeatureModifiedEvent;
import org.vaadin.addon.leaflet.editable.FeatureModifiedListener;
import org.vaadin.addon.leaflet.editable.LEditableMap;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.layouts.MHorizontalLayout;

public class DrawingTest extends AbstractTest implements
        FeatureDrawnListener {

    @Override
    public String getDescription() {
        return "Test leaflet draws editing feature";
    }

    LFeatureGroup layoutgroup = new LFeatureGroup();

    private LMap leafletMap;
    private LEditableMap editableMap;

    @Override
    public Component getTestComponent() {
        leafletMap = new LMap();
        leafletMap.setCustomInitOption("editable", true);
        leafletMap.setHeight("300px");
        leafletMap.setCenter(0, 0);
        leafletMap.setZoomLevel(0);
        leafletMap.addLayer(new LTileLayer(
                "http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"));

        editableMap = new LEditableMap(leafletMap);

        editableMap.addFeatureDrawnListener(this);

        return leafletMap;
    }

    @Override
    protected void setup() {
        super.setup();

        HorizontalLayout tools = new MHorizontalLayout();

        tools.addComponent(new Button("Start polygon",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        editableMap.startPolygon();
                    }
                }));

        tools.addComponent(new Button("Start polyline",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        editableMap.startPolyline();
                    }
                }));

        content.addComponent(tools);

        leafletMap.setSizeFull();
        content.setExpandRatio(leafletMap, 1);

    }

    @Override
    public void featureDrawn(FeatureDrawnEvent event) {
        Notification.show("Created"
                + event.getDrawnFeature().getClass().getSimpleName());
        final AbstractLeafletVector drawnFeature = (AbstractLeafletVector) event.getDrawnFeature();

        drawnFeature.setColor("green");
        // Save the layer
        leafletMap.addLayer(drawnFeature);

    }
}
