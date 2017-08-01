package org.vaadin.addon.leaflet.demoandtestapp;

import java.time.LocalDate;

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
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class JtsFieldTest extends AbstractTest {

	public static class JtsPojo {
		private String name;
		private LocalDate date;
		private Point point;
		private LineString lineString;
		private LinearRing linearRing;
		private Polygon polygon;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public LocalDate getDate() {
			return date;
		}

		public void setDate(LocalDate date) {
			this.date = date;
		}

		public Point getPoint() {
			return point;
		}

		public void setPoint(Point point) {
			this.point = point;
		}

		public LineString getLineString() {
			return lineString;
		}

		public void setLineString(LineString lineString) {
			this.lineString = lineString;
		}

		public Polygon getPolygon() {
			return polygon;
		}

		public void setPolygon(Polygon polygon) {
			this.polygon = polygon;
		}

		@Override
		public String toString() {
			return "JtsPojo [\n name=" + name + ",\n date=" + date
					+ ",\n point=" + point + ",\n lineString=" + lineString
					+ ",\n linearRing=" + linearRing + ",\n polygon=" + polygon
					+ "\n]";
		}

		public LinearRing getLinearRing() {
			return linearRing;
		}

		public void setLinearRing(LinearRing linearRing) {
			this.linearRing = linearRing;
		}
	}

	@Override
	public String getDescription() {
		return "A test for JTS Field implementations";
	}

	private JtsPojo pojo = new JtsPojo();
	private Label display = new Label();

	private TextField name = new TextField("Name");
	private DateField date = new DateField("Date");
	private PointField point = new PointField("PointField");
	private LineStringField lineString = new LineStringField("LineStringField");
	private LinearRingField linearRing = new LinearRingField("LinearRingField");
	private PolygonField polygon = new PolygonField("PolygonField");

	private boolean readOnly = false;
	
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

		TabSheet jtsFields = new TabSheet(point, lineString , linearRing, polygon);
		jtsFields.setCaption("JTS fields:");
		jtsFields.setSizeFull();
		editorform.addComponents(new HorizontalLayout(name, date), jtsFields
		// ,polygon
				);
		editorform.setExpandRatio(jtsFields, 1);

		Binder<JtsPojo> binder = new Binder<>(JtsPojo.class);
		binder.bind(name, JtsPojo::getName, JtsPojo::setName);
		binder.bind(date, JtsPojo::getDate, JtsPojo::setDate);
		binder.bind(point, JtsPojo::getPoint, JtsPojo::setPoint);
		binder.bind(lineString, JtsPojo::getLineString, JtsPojo::setLineString);
		binder.bind(linearRing, JtsPojo::getLinearRing, JtsPojo::setLinearRing);
		binder.bind(polygon, JtsPojo::getPolygon, JtsPojo::setPolygon);
		
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
		
		editorform.addComponent(buttonLayout);
		
		horizontalLayout.addComponents(editorform, display);
		horizontalLayout.setExpandRatio(editorform, 1);
		horizontalLayout.setExpandRatio(display, 1);

		return horizontalLayout;
	}

}
