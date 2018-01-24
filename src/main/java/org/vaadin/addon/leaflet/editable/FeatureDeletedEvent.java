
package org.vaadin.addon.leaflet.editable;

import java.util.EventObject;
import org.vaadin.addon.leaflet.LeafletLayer;

public class FeatureDeletedEvent extends EventObject {
    private LeafletLayer deleted;

    public FeatureDeletedEvent(Object source, LeafletLayer deletedLayer) {
        super(source);
        this.deleted = deletedLayer;
    }

    public LeafletLayer getDeletedFeature() {
        return deleted;
    }

}
