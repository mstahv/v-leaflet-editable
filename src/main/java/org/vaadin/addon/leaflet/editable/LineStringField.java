package org.vaadin.addon.leaflet.editable;

import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addon.leaflet.shared.Point;

import com.vividsolutions.jts.geom.LineString;
import org.vaadin.addon.leaflet.util.AbstractJTSField;
import org.vaadin.addon.leaflet.util.JTSUtil;

public class LineStringField extends AbstractJTSField<LineString> {

    private LPolyline lPolyline;
    private LEditableMap editableMap;

    public LineStringField() {
        getEditableMap().addFeatureDrawnListener(new FeatureDrawnListener() {

            @Override
            public void featureDrawn(FeatureDrawnEvent event) {
                setValue(getCrsTranslator().toModel(
                        JTSUtil.toLineString((LPolyline) event
                                .getDrawnFeature())));

            }
        });
    }

    public LineStringField(String caption) {
        this();
        setCaption(caption);
    }

    @Override
    public Class<? extends LineString> getType() {
        return LineString.class;
    }

    protected void prepareEditing() {
        if (lPolyline == null) {
            lPolyline = new LPolyline();
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
                        JTSUtil.toLineString(lPolyline)));
            }
        });
        map.zoomToExtent(new Bounds(lPolyline.getPoints()));
    }

    @Override
    protected final void prepareDrawing() {
        getEditableMap().startPolyline();
    }

    protected final LEditableMap getEditableMap() {
        if (editableMap == null) {
            editableMap = new LEditableMap(getMap());
        }
        return editableMap;
    }

}