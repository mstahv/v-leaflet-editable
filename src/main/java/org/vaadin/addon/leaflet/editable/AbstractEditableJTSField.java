package org.vaadin.addon.leaflet.editable;

import com.vaadin.shared.Registration;
import org.locationtech.jts.geom.Geometry;
import org.vaadin.addon.leaflet.util.AbstractJTSField;

public abstract class AbstractEditableJTSField<T extends Geometry> extends AbstractJTSField<T> implements FeatureDrawnListener {

    private LEditableMap editableMap;

    protected LEditable lEditable;

    protected Registration featureDrawnListenerReg;

    @Override
    protected void prepareViewing() {
        getEditableMap().remove();
        editableMap = null;
        if (lEditable != null) {
            lEditable.remove();
            lEditable = null;
        }
    }

    protected final LEditableMap getEditableMap() {
        if (editableMap == null) {
            editableMap = new LEditableMap(getMap());
        }
        return editableMap;
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial);
        if (getValue() == null) {
            prepareDrawing();
        }
    }

}
