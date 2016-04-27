package org.vaadin.addon.leaflet.editable;

import org.vaadin.addon.leaflet.util.AbstractJTSField;

import com.vividsolutions.jts.geom.Geometry;

public abstract class AbstractEditableJTSField<T extends Geometry> extends AbstractJTSField<T> implements FeatureDrawnListener {
	private LEditableMap editableMap;

	protected LEditable lEditable;

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
}
