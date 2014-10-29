
package org.vaadin.addon.leaflet.editable;

import com.vaadin.server.AbstractClientConnector;
import java.util.EventObject;
import org.vaadin.addon.leaflet.LeafletLayer;

/**
 *
 * @author Matti Tahvonen <matti@vaadin.com>
 */
public class FeatureModifiedEvent extends EventObject {
    private LeafletLayer modifiedLayer;

    public FeatureModifiedEvent(AbstractClientConnector connector,
            LeafletLayer modifiedLayer) {
        super(connector);
        this.modifiedLayer = modifiedLayer;
    }

    public LeafletLayer getModifiedFeature() {
        return modifiedLayer;
    }

}
