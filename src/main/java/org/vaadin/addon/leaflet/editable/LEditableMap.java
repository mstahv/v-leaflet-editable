package org.vaadin.addon.leaflet.editable;

import com.vaadin.server.AbstractExtension;
import com.vaadin.shared.Registration;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;
import org.vaadin.addon.leaflet.LeafletLayer;

import java.io.IOException;
import org.geotools.geojson.feature.FeatureJSON;
import org.opengis.feature.simple.SimpleFeature;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LPolygon;
import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.editable.client.EditableMapClientRcp;
import org.vaadin.addon.leaflet.editable.client.EditableMapServerRcp;

/**
 * Extension for LMap to start drawing new vectors.
 * <p>
 */
public class LEditableMap extends AbstractExtension {

    public Registration addFeatureDrawnListener(FeatureDrawnListener listener) {
        Registration registration = addListener("featureDrawn", FeatureDrawnEvent.class, listener,
                FeatureDrawnListener.drawnMethod);
        markAsDirty();
        return registration;
    }

    public LEditableMap(LMap map) {
        extend(map);
        registerRpc();
    }

    private void registerRpc() {
        registerRpc(new EditableMapServerRcp() {

            @Override
            public void vectorCreated(String geojson) {

                try {
                    FeatureJSON featureJSON = new FeatureJSON();
                    SimpleFeature readFeature = featureJSON.readFeature(geojson);

                    Object defaultGeometry = readFeature.getDefaultGeometry();

                    LeafletLayer createdLayer;
                    if (defaultGeometry instanceof Polygon) {
                        createdLayer = new LPolygon((Polygon) defaultGeometry);
                    } else if (defaultGeometry instanceof LineString) {
                        createdLayer = new LPolyline((LineString) defaultGeometry);
                    } else {
                        throw new IllegalArgumentException("Unknown geometry");
                    }
                    fireEvent(new FeatureDrawnEvent(LEditableMap.this, createdLayer));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    @Override
    public void remove() {
        if (getParent() != null) {
            super.remove();
        }
    }

    public void startPolygon() {
        getRpcProxy(EditableMapClientRcp.class).startPolygon();
    }

    public void startPolyline() {
        getRpcProxy(EditableMapClientRcp.class).startPolyline();
    }

}
