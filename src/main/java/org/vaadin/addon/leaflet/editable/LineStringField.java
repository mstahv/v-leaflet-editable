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
    public Class<? extends LineString> getType() {
        return LineString.class;
    }

    @Override
    protected void prepareEditing() {
        if (lPolyline == null) {
            lPolyline = new LPolyline();
            map.addLayer(lPolyline);
        }
        Point[] lPointArray = JTSUtil.toLeafletPointArray(getCrsTranslator()
                .toPresentation(getInternalValue()));
        lPolyline.setPoints(lPointArray);
        lEditable = new LEditable(lPolyline);
        lEditable.addFeatureModifiedListener(new FeatureModifiedListener() {

            @Override
            public void featureModified(FeatureModifiedEvent event) {
                setValue(getCrsTranslator().toModel(
                        JTSUtil.toLineString(lPolyline)));
            }
        });
        map.zoomToExtent(new Bounds(lPolyline.getPoints()));
    }
    
    @Override
    protected final void prepareDrawing() {
    	getEditableMap().addFeatureDrawnListener(this);
        getEditableMap().startPolyline();
    }
    
    @Override
    public void featureDrawn(FeatureDrawnEvent event) {
        setValue(getCrsTranslator().toModel(
                JTSUtil.toLineString((LPolyline) event
                        .getDrawnFeature())));
        getEditableMap().removeFeatureDrawnListener(this);
    }
}
