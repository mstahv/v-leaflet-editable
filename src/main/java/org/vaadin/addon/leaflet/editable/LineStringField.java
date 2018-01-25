package org.vaadin.addon.leaflet.editable;

import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addon.leaflet.util.JTSUtil;

import com.vividsolutions.jts.geom.LineString;

public class LineStringField extends AbstractEditableJTSField<LineString> {

    private LPolyline lPolyline;

    public LineStringField() {
    }

    public LineStringField(String caption) {
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
                        JTSUtil.toLineString(lPolyline)), true);
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
        setValue(getCrsTranslator().toModel(
                JTSUtil.toLineString((LPolyline) event
                        .getDrawnFeature())));
        featureDrawnListenerReg.remove();
    }

}
