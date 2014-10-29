package org.vaadin.addon.leaflet.editable;

import org.vaadin.addon.leaflet.AbstractLeafletVector;
import org.vaadin.addon.leaflet.LCircle;
import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.editable.client.EditableServerRcp;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.server.AbstractExtension;
import com.vaadin.shared.Connector;
import org.vaadin.addon.leaflet.editable.client.EditableClientRcp;

/**
 * Editing extension for Leaflet vectors.
 * <p>
 * Can be used to edit just a specific layer. The
 * constructor adds the extension to {@link AbstractLeafletVector}. The
 * extension is automatically removed after first edit event or on removal.
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
				c.setRadius(radius);
				c.setPoint(latLng);
				fireEvent(new FeatureModifiedEvent(LEditable.this, c));
				remove();
			}

			@Override
			public void polylineModified(Connector plc, Point[] pointArray) {
				LPolyline pl = (LPolyline) plc;
				pl.setPoints(pointArray);
				fireEvent(new FeatureModifiedEvent(LEditable.this, pl));
				remove();
			}

		});
	}

	@Override
	public void remove() {
		if (getParent() != null) {
			super.remove();
		}
	}

    public void newHole() {
        getRpcProxy(EditableClientRcp.class).newHole();
    }

}
