package org.vaadin.addon.leaflet.editable.client;

import com.vaadin.shared.communication.ClientRpc;

public interface EditableMapClientRcp extends ClientRpc {

    public void startPolygon();

    public void startPolyline();

}
