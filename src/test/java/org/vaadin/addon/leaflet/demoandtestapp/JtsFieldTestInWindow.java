package org.vaadin.addon.leaflet.demoandtestapp;

import com.vaadin.data.Binder;
import com.vaadin.shared.ui.ContentMode;

import org.vaadin.addon.leaflet.util.PointField;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.vaadin.addon.leaflet.editable.LineStringField;
import org.vaadin.addon.leaflet.editable.LinearRingField;
import org.vaadin.addon.leaflet.editable.PolygonField;
import org.vaadin.addonhelpers.AbstractTest;

public class JtsFieldTestInWindow extends AbstractTest {

    @Override
    public String getDescription() {
        return "A test for JTS Field implementations";
    }

    private JtsPojo pojo = new JtsPojo();
    private Label display = new Label();

    private TextField name = new TextField("Name");
    private DateField date = new DateField("Date");
    private PointField point = new PointField("Point");
    private LineStringField lineString = new LineStringField("LineStringField");
    private LinearRingField linearRing = new LinearRingField("LinearRingField");
    private PolygonField polygon = new PolygonField("PolygonField");

    @Override
    public Component getTestComponent() {
        content.setMargin(true);

        display.setContentMode(ContentMode.PREFORMATTED);
        display.setCaption("Pojo state:");
        display.setValue(pojo.toString());

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSizeFull();

        VerticalLayout editorform = new VerticalLayout();
        editorform.setSizeFull();

        editorform.setSpacing(true);
        editorform.setCaption("Edit JTS pojo:");

        Binder<JtsPojo> binder = new Binder<>(JtsPojo.class);
        binder.bindInstanceFields(this);
        binder.setBean(pojo);

        editorform.addComponent(new Button("Save", e -> {
            display.setValue(pojo.toString());
        }));

        horizontalLayout.addComponents(editorform, display);
        horizontalLayout.setExpandRatio(editorform, 1);
        horizontalLayout.setExpandRatio(display, 1);

        Button button = new Button("Open in popup");
        button.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                Window window = new Window();
                window.setCaption("Polygon editor");
                window.setHeight("50%");
                window.setWidth("50%");

                window.setContent(polygon);

                addWindow(window);
            }
        });

        horizontalLayout.addComponent(button);

        return horizontalLayout;
    }

}
