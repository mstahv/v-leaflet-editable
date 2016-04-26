package org.vaadin.addon.leaflet.editable;

import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addon.leaflet.shared.Point;

import com.vividsolutions.jts.geom.LinearRing;
import org.vaadin.addon.leaflet.LPolygon;
import org.vaadin.addon.leaflet.util.AbstractJTSField;
import org.vaadin.addon.leaflet.util.JTSUtil;

public class LinearRingField extends AbstractJTSField<LinearRing> {

    private LPolygon lPolyline;
    private LEditableMap editableMap;

    public LinearRingField() {
        getEditableMap().addFeatureDrawnListener(new FeatureDrawnListener() {

            @Override
            public void featureDrawn(FeatureDrawnEvent event) {
                setValue(getCrsTranslator().toModel(
                        JTSUtil.toLinearRing((LPolygon) event
                                .getDrawnFeature())));

            }
        });
    }

    public LinearRingField(String caption) {
        this();
        setCaption(caption);
    }

    @Override
    public Class<? extends LinearRing> getType() {
        return LinearRing.class;
    }

    @Override
    protected void prepareEditing() {
        if (lPolyline == null) {
            lPolyline = new LPolygon();
            map.addLayer(lPolyline);
        }
        Point[] lPointArray = JTSUtil.toLeafletPointArray(getCrsTranslator()
                .toPresentation(getInternalValue()));
        lPolyline.setPoints(lPointArray);
        LEditable lEditable = new LEditable(lPolyline);
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
        getEditableMap().startPolygon();
    }

    @Override
    protected void prepareViewing() {
    	throw new IllegalArgumentException("ReadOnly mode is not supported for " + this.getClass().getSimpleName());
    }
    
    protected final LEditableMap getEditableMap() {
        if (editableMap == null) {
            editableMap = new LEditableMap(getMap());
        }
        return editableMap;
    }

}
