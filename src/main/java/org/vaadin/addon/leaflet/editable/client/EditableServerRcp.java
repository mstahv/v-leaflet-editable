package org.vaadin.addon.leaflet.editable.client;

import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.shared.Connector;
import com.vaadin.shared.annotations.Delayed;
import com.vaadin.shared.communication.ServerRpc;

public interface EditableServerRcp extends ServerRpc {

	@Delayed(lastOnly=true)
	public void circleModified(Connector cc, Point latLng, double radius);

	@Delayed(lastOnly=true)
	public void vectorModified(Connector plc, String geojson);

}
