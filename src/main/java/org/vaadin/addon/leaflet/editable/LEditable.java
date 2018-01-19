package org.vaadin.addon.leaflet.editable;

import com.vaadin.server.AbstractExtension;
import com.vaadin.shared.Connector;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;
import org.vaadin.addon.leaflet.AbstractLeafletVector;
import org.vaadin.addon.leaflet.LCircle;
import org.vaadin.addon.leaflet.LeafletLayer;
import org.vaadin.addon.leaflet.editable.client.EditableClientRcp;
import org.vaadin.addon.leaflet.editable.client.EditableServerRcp;
import org.vaadin.addon.leaflet.shared.Point;

import java.io.IOException;
import org.geotools.geojson.feature.FeatureJSON;
import org.opengis.feature.simple.SimpleFeature;
import org.vaadin.addon.leaflet.LPolygon;
import org.vaadin.addon.leaflet.LPolyline;

/**
 * Editing extension for Leaflet vectors.
 * <p>
 * Can be used to edit just a specific layer. The constructor adds the extension
 * to {@link AbstractLeafletVector}. The extension is automatically removed
 * after first edit event or on removal.
 *
 */
public class LEditable extends AbstractExtension {

    public void addFeatureModifiedListener(FeatureModifiedListener listener) {
        addListener(FeatureModifiedEvent.class, listener,
                FeatureModifiedListener.modifiedMethod);
    }

    public void removeFeatureModifiedListener(FeatureModifiedListener listener) {
        removeListener(FeatureModifiedEvent.class, listener);
    }

    public LEditable(AbstractLeafletVector vector) {
        extend(vector);
        registerRpc();
    }

    private void registerRpc() {
        registerRpc(new EditableServerRcp() {

            @Override
            public void circleModified(Connector cc, Point latLng, double radius) {
                LCircle c = (LCircle) cc;
                c.setPointAndRadiusWithoutRepaint(radius, latLng);
                fireEvent(new FeatureModifiedEvent(LEditable.this, c));
            }

            @Override
            public void vectorModified(Connector plc, String geojson) {
                //LPolyline pl = (LPolyline) plc;
                //pl.setPoints(pointArray);
                try {
                    FeatureJSON featureJSON = new FeatureJSON();
                    SimpleFeature readFeature = featureJSON.readFeature(geojson);

                    Object defaultGeometry = readFeature.getDefaultGeometry();

                    if (plc instanceof LPolygon) {
                        LPolygon poly = (LPolygon) plc;
                        poly.setGeometryWithoutRepaint((Polygon) defaultGeometry);
                    } else if (plc instanceof LPolyline) {
                        LPolyline poly = (LPolyline) plc;
                        poly.setGeometryWithoutRepaint((LineString) defaultGeometry);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                fireEvent(new FeatureModifiedEvent(LEditable.this, (LeafletLayer) plc));
            }

        });
    }

    @Override
    public void remove() {
        if (getParent() != null) {
            super.remove();
        }
    }

    /**
     * Only available for polygons.
     */
    public void newHole() {
        getRpcProxy(EditableClientRcp.class).newHole();
    }

}
