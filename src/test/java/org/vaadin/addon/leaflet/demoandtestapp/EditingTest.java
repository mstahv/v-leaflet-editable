package org.vaadin.addon.leaflet.demoandtestapp;

import org.vaadin.addon.leaflet.AbstractLeafletVector;
import org.vaadin.addon.leaflet.LCircle;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LPolygon;
import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.LTileLayer;
import org.vaadin.addon.leaflet.editable.FeatureModifiedEvent;
import org.vaadin.addon.leaflet.editable.FeatureModifiedListener;
import org.vaadin.addon.leaflet.editable.LEditable;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import org.vaadin.addonhelpers.AbstractTest;

public class EditingTest extends AbstractTest implements
        FeatureModifiedListener {

    @Override
    public String getDescription() {
        return "Test leaflet draws editing feature";
    }

    private LMap leafletMap;
    private LEditable lEditing;

    @Override
    public Component getTestComponent() {
        leafletMap = new LMap();
        leafletMap.setCustomInitOption("editable", true);
        leafletMap.setHeight("300px");
        leafletMap.setCenter(0, 0);
        leafletMap.setZoomLevel(0);
        leafletMap.addLayer(new LTileLayer(
                "http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"));

        LCircle circle = new LCircle(new Point(-50, 0), 3000 * 1000);
        LPolyline polyline = new LPolyline(new Point(40, 0), new Point(70, 0));

        LPolygon polygon = new LPolygon(new Point(0, 0), new Point(30, 30),
                new Point(30, 0));
        lEditing = new LEditable(polygon);
        lEditing.addFeatureModifiedListener(this);

        leafletMap.addComponents(polyline, circle, polygon);

        return leafletMap;
    }

    @Override
    protected void setup() {
        super.setup();

        for (final Component c : leafletMap) {
            if (c instanceof AbstractLeafletVector) {
                Button button = new Button("Edit"
                        + c.getClass().getSimpleName());
                button.addClickListener(new ClickListener() {

                    @Override
                    public void buttonClick(ClickEvent event) {
                        if (lEditing != null && lEditing.getParent() != null) {
                            lEditing.remove();
                        }
                        lEditing = new LEditable((AbstractLeafletVector) c);
                    }
                });
                content.addComponent(button);
            }
        }

        content.addComponent(new Button("Stop editing",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        if (lEditing.getParent() != null) {
                            lEditing.remove();
                        }
                    }
                }));

        content.addComponent(new Button("Add hole",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        if (lEditing.getParent() != null) {
                            lEditing.newHole();
                        }
                    }
                }));

    }

    @Override
    public void featureModified(FeatureModifiedEvent event) {
        Notification.show("Edited"
                + event.getModifiedFeature().getClass().getSimpleName());
    }
}
