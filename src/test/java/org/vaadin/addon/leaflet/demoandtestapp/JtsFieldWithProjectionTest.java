package org.vaadin.addon.leaflet.demoandtestapp;


import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.vaadin.addon.leaflet.editable.LineStringField;
import org.vaadin.addon.leaflet.util.CRSTranslator;
import org.vaadin.addon.leaflet.util.PointField;
import org.vaadin.addonhelpers.AbstractTest;

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
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

public class JtsFieldWithProjectionTest extends AbstractTest {

	private static final int EPSG_3067 = 3067;

        @Override
	public String getDescription() {
		return "A test for JTS Field implementations with custom projection";
	}

	private JtsPojo pojo = new JtsPojo();
	private Label display = new Label();

	private TextField name = new TextField("Name");
	private DateField date = new DateField("Date");
	private PointField point = new PointField("Point");
	private LineStringField lineString = new LineStringField("LineStringField");
	//private LinearRingField linearRing = new LinearRingField("LinearRingField");

	@Override
	public Component getTestComponent() {
		content.setMargin(true);

		try {
			
			/*
			 * Build geotools based coordinate reference system translator
			 * ETRS-TM35FIN (model) <> WGS84 (presentation)
			 */
			
			CoordinateReferenceSystem tm35fin = CRS.decode("EPSG:" + EPSG_3067);
			// GIS weirdness, x/y whatever, always...
			boolean longitudeFirst = true;
			CRSAuthorityFactory factory = CRS
					.getAuthorityFactory(longitudeFirst);
			CoordinateReferenceSystem wsg86 = factory
					.createCoordinateReferenceSystem("EPSG:4326");
			boolean lenient = true; // allow for some error due to different
									// datums
			final MathTransform toWsg86 = CRS.findMathTransform(tm35fin, wsg86,
					lenient);
			final MathTransform toModel = toWsg86.inverse();
			
			// Build the actual v-leaflet JTS field translator
			CRSTranslator<Geometry> translator = new CRSTranslator<Geometry>() {

				@Override
				public Geometry toPresentation(Geometry geom) {
					try {
						return JTS.transform(geom, toWsg86);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}

				@Override
				public Geometry toModel(Geometry geom) {
					try {
						return JTS.transform(geom, toModel);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			};
			point.setCRSTranslator(translator);
//			linearRing.setCRSTranslator(translator);
			lineString.setCRSTranslator(translator);
			// Also can be set web app global CRFTranslator
			// AbstractJTSField.setDefaultCRFTranslator(translator);
		} catch (NoSuchAuthorityCodeException e) {
			throw new RuntimeException(e);
		} catch (FactoryException e) {
			throw new RuntimeException(e);
		} catch (MismatchedDimensionException e) {
			throw new RuntimeException(e);
		} catch (TransformException e) {
			throw new RuntimeException(e);
		}

		// Vaadin office at ETRS-TM35FIN (EPSG:3067)
		GeometryFactory geometryFactory = new GeometryFactory(
				new PrecisionModel(), EPSG_3067);
		Point p = geometryFactory.createPoint(new Coordinate(241637, 6711028));
		pojo.setPoint(p);

		display.setContentMode(ContentMode.PREFORMATTED);
		display.setCaption("Pojo state:");
		display.setValue(pojo.toString());

		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSizeFull();

		VerticalLayout editorform = new VerticalLayout();
		editorform.setSizeFull();

		editorform.setSpacing(true);
		editorform.setCaption("Edit JTS pojo:");

		TabSheet jtsFields = new TabSheet(point, lineString/*, linearRing*/);
		jtsFields.setCaption("JTS fiels:");
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

		Button c = new Button("Save", new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					binder.writeBean(pojo);
					display.setValue(pojo.toString());
				} catch (ValidationException e) {
					e.printStackTrace();
				}
			}
		});
		c.setId("SSS");
		editorform.addComponent(c);

		horizontalLayout.addComponents(editorform, display);
		horizontalLayout.setExpandRatio(editorform, 1);
		horizontalLayout.setExpandRatio(display, 1);

		return horizontalLayout;
	}

}
