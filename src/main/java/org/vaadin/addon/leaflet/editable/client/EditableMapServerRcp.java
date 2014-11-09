package org.vaadin.addon.leaflet.editable.client;

import com.vaadin.shared.communication.ServerRpc;

public interface EditableMapServerRcp extends ServerRpc {

	public void vectorCreated(String geojson);

}
