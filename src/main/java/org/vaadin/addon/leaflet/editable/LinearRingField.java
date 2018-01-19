package org.vaadin.addon.leaflet.editable;

import org.vaadin.addon.leaflet.LPolygon;
import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addon.leaflet.util.JTSUtil;

import com.vividsolutions.jts.geom.LinearRing;

public class LinearRingField extends AbstractEditableJTSField<LinearRing> {

    private LPolygon lPolyline;

    public LinearRingField() {
    }

    public LinearRingField(String caption) {
        this();
        setCaption(caption);
    }

    @Override
    protected void prepareEditing() {
        if (lPolyline == null) {
            lPolyline = new LPolygon();
            map.addLayer(lPolyline);
        }
        Point[] lPointArray = JTSUtil.toLeafletPointArray(getCrsTranslator()
                .toPresentation(getValue()));
        lPolyline.setPoints(lPointArray);
        lEditable = new LEditable(lPolyline);
        lEditable.addFeatureModifiedListener(new FeatureModifiedListener() {

            @Override
            public void featureModified(FeatureModifiedEvent event) {
                setValue(getCrsTranslator().toModel(
                        JTSUtil.toLinearRing(lPolyline)));
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

        getEditableMap().addFeatureDrawnListener(this);
        getEditableMap().startPolygon();
    }

    @Override
    public void featureDrawn(FeatureDrawnEvent event) {
        setValue(getCrsTranslator().toModel(
                JTSUtil.toLinearRing((LPolygon) event
                        .getDrawnFeature())));
        getEditableMap().removeFeatureDrawnListener(this);
    }
}
