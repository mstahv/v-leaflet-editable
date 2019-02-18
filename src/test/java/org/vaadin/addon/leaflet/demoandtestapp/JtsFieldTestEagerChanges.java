package org.vaadin.addon.leaflet.demoandtestapp;

import org.vaadin.addon.leaflet.editable.LineStringField;
import org.vaadin.addon.leaflet.editable.LinearRingField;
import org.vaadin.addon.leaflet.editable.PolygonField;
import org.vaadin.addon.leaflet.util.PointField;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vaadin.viritin.label.MLabel;

public class JtsFieldTestEagerChanges extends AbstractTest {

    @Override
    public String getDescription() {
        return "A test for JTS Field implementations";
    }

    private JtsPojo pojo = new JtsPojo();
    private Label display = new MLabel().withFullWidth();

    private Label eventlog = new MLabel().withFullWidth();

    private TextField name = new TextField("Name");
    private DateField date = new DateField("Date");
    private PointField point = new PointField("PointField");
    private LineStringField lineString = new LineStringField("LineStringField");
    private LinearRingField linearRing = new LinearRingField("LinearRingField");
    private PolygonField polygon = new PolygonField("PolygonField");

    private boolean readOnly = false;

    GeometryFactory geometryFactory = new GeometryFactory();

    WKTReader reader = new WKTReader(geometryFactory);

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

        TabSheet jtsFields = new TabSheet(point, lineString, linearRing, polygon);
        jtsFields.setCaption("JTS fields:");
        jtsFields.setSizeFull();
        editorform.addComponents(new HorizontalLayout(name, date), jtsFields
        // ,polygon
        );
        editorform.setExpandRatio(jtsFields, 1);

        Binder<JtsPojo> binder = new Binder<>(JtsPojo.class);
        binder.bindInstanceFields(this);
        eventlog.setContentMode(ContentMode.PREFORMATTED);
        binder.addValueChangeListener(e->{
            eventlog.setValue(eventlog.getValue() + "Value change " + e.getComponent().getClass().getName() + "\n");
        });

        MHorizontalLayout buttonLayout = new MHorizontalLayout();
        buttonLayout.addComponent(new Button("Save", new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    binder.writeBean(pojo);
                    display.setValue(pojo.toString());
                } catch (ValidationException e) {
                    e.printStackTrace();
                }
            }
        }));

        buttonLayout.addComponent(new Button("Toggle read only", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                readOnly = !readOnly;
                binder.setReadOnly(readOnly);
            }
        }));

        buttonLayout.addComponent(new Button("Assign polylated object", e -> {
            pojo = new JtsPojo();

            pojo.setDate(LocalDate.now());
            pojo.setName("Nimesi");
            try {
                pojo.setPoint((Point) reader.read("POINT(1 1)"));
                pojo.setLineString((LineString) reader.read("LINESTRING (-3.867188 59.778522, 29.179688 59.422728, 17.226563 55.254077)"));
                pojo.setLinearRing((LinearRing) reader.read("LINEARRING (-19.6875 58.170702, -28.125 22.390714, 55.898438 17.098792, 53.789063 65.856756, 14.414063 65.856756, 3.515625 65.275094, -8.789063 63.233627, -19.6875 58.170702)"));
                pojo.setPolygon((Polygon) reader.read("POLYGON ((18.28125 64.0722, 20.390625 57.961503, 38.671875 63.134503, 27.070313 66.705169, 18.28125 64.0722), (23.994141 64.404058, 24.169922 62.255139, 31.728516 63.280593, 29.355469 65.2268, 23.994141 64.404058))"));
            } catch (ParseException ex) {
                Logger.getLogger(JtsFieldTestEagerChanges.class.getName()).log(Level.SEVERE, null, ex);
            }
            binder.setBean(pojo);
        }));

        editorform.addComponent(buttonLayout);

        horizontalLayout.addComponents(editorform, display, eventlog);
        horizontalLayout.setExpandRatio(editorform, 1);
        horizontalLayout.setExpandRatio(display, 1);
        horizontalLayout.setExpandRatio(eventlog, 1);

        return horizontalLayout;
    }

}
