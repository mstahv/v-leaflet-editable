package org.vaadin.addon.leaflet.demoandtestapp;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import org.vaadin.addon.leaflet.*;
import org.vaadin.addon.leaflet.editable.FeatureModifiedEvent;
import org.vaadin.addon.leaflet.editable.FeatureModifiedListener;
import org.vaadin.addon.leaflet.editable.LEditable;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.layouts.MHorizontalLayout;

public class TogglingEditing extends AbstractTest implements
        FeatureModifiedListener {

    private LPolygon polygon;

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

        polygon = new LPolygon(new Point(0, 0), new Point(30, 30),
                new Point(30, 0));

        leafletMap.addComponents(polygon);
        clickListener = new LeafletClickListener() {

            @Override
            public void onClick(LeafletClickEvent event) {
                System.out.println("Clicked");
            }
        };

        leafletMap.addClickListener(clickListener);

        return leafletMap;
    }
    private LeafletClickListener clickListener;

    @Override
    protected void setup() {
        super.setup();

        HorizontalLayout tools = new MHorizontalLayout();

        for (final Component c : leafletMap) {
            if (c instanceof AbstractLeafletVector) {
                Button button = new Button("Edit"
                        + c.getClass().getSimpleName());
                button.addClickListener(new ClickListener() {

                    @Override
                    public void buttonClick(ClickEvent event) {
                        leafletMap.removeClickListener(clickListener);
                        if (lEditing != null && lEditing.getParent() != null) {
                            lEditing.remove();
                        }
                        lEditing = new LEditable((AbstractLeafletVector) c);
                        lEditing.addFeatureModifiedListener(new FeatureModifiedListener() {

                            @Override
                            public void featureModified(
                                    FeatureModifiedEvent event) {
                                Notification.show("Modified :" + ((AbstractLeafletVector) c).getGeometry().toText());
                                leafletMap.addClickListener(clickListener);
                            }
                        });
                    }
                });
                tools.addComponent(button);
            }
        }

        tools.addComponent(new Button("Stop editing",
                new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                if (lEditing.getParent() != null) {
                    lEditing.remove();
                    leafletMap.addClickListener(clickListener);
                }
            }
        }));

        tools.addComponent(new Button("Add hole",
                new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                if (lEditing != null && lEditing.getParent() != null) {
                    lEditing.remove();
                }
                lEditing = new LEditable(polygon);
                lEditing.newHole();
                leafletMap.zoomToContent(polygon);
                leafletMap.removeClickListener(clickListener);
                lEditing.addFeatureModifiedListener(new FeatureModifiedListener() {

                    @Override
                    public void featureModified(
                            FeatureModifiedEvent event) {
                        Notification.show("Modified :" + ((AbstractLeafletVector) event.getModifiedFeature()).getGeometry().toText());
                        leafletMap.addClickListener(clickListener);
                    }
                });

            }
        }));

        content.addComponent(tools);

        leafletMap.setSizeFull();
        content.setExpandRatio(leafletMap, 1);

    }

    @Override
    public void featureModified(FeatureModifiedEvent event) {
        Notification.show("Edited"
                + event.getModifiedFeature().getClass().getSimpleName());
    }
}
