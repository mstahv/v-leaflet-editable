
package org.vaadin.addon.leaflet.editable;

import com.vaadin.shared.Connector;
import java.util.EventObject;
import org.vaadin.addon.leaflet.LeafletLayer;

public class FeatureDrawnEvent extends EventObject {
    private LeafletLayer drawnLayer;

    public FeatureDrawnEvent(Connector lDraw, LeafletLayer drawnLayer) {
        super(lDraw);
        this.drawnLayer = drawnLayer;
    }

    public LeafletLayer getDrawnFeature() {
        return drawnLayer;
    }

}
