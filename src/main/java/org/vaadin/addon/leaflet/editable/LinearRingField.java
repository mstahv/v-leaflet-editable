package org.vaadin.addon.leaflet.editable;

import org.locationtech.jts.geom.CoordinateList;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.vaadin.addon.leaflet.AbstractLeafletVector;
import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addon.leaflet.util.JTSUtil;

import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.PrecisionModel;
import org.vaadin.addon.leaflet.LPolyline;

public class LinearRingField extends AbstractEditableJTSField<LinearRing> {

    private LPolyline lPolyline;

    public LinearRingField() {
    }

    public LinearRingField(String caption) {
        this();
        setCaption(caption);
    }

    @Override
    protected void prepareEditing(boolean userOriginatedValuechangeEvent) {
        if (userOriginatedValuechangeEvent) {
            return;
        }
        if (lPolyline != null) {
            map.removeComponent(lPolyline);
        }
        lPolyline = new LPolyline();
        map.addLayer(lPolyline);
        Point[] lPointArray = JTSUtil.toLeafletPointArray(getCrsTranslator()
                .toPresentation(getValue()));
        lPolyline.setPoints(lPointArray);
        lEditable = new LEditable(lPolyline);
        lEditable.addFeatureModifiedListener(new FeatureModifiedListener() {

            @Override
            public void featureModified(FeatureModifiedEvent event) {
                setValue(getCrsTranslator().toModel(
                        JTSUtil.toLinearRing(lPolyline)), true);
            }
        });
        map.zoomToExtent(new Bounds(lPolyline.getPoints()));
    }

    @Override
    protected final void prepareDrawing() {
        if (lPolyline != null) {
            map.removeLayer(lPolyline);
            lPolyline = null;
        }

        featureDrawnListenerReg = getEditableMap().addFeatureDrawnListener(this);
        getEditableMap().startPolyline();
    }

    @Override
    public void featureDrawn(FeatureDrawnEvent event) {
        LPolyline drawnFeature = (LPolyline) event.getDrawnFeature();
        LineString str = (LineString) drawnFeature.getGeometry();
        CoordinateList coordinateList = new CoordinateList(str.getCoordinates());
        coordinateList.closeRing();
        GeometryFactory factory = new GeometryFactory(new PrecisionModel(),
                4326);
        LinearRing ring = factory.createLinearRing(coordinateList.toCoordinateArray());
        setValue(getCrsTranslator().toModel(ring));
        featureDrawnListenerReg.remove();
    }

}
