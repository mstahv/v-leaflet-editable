package org.vaadin.addon.leaflet.editable.client;

import org.peimari.gleaflet.client.resources.LeafletEditableResourceInjector;

import com.google.gwt.core.client.EntryPoint;

public class EagerEditableLoader implements EntryPoint {

	@Override
	public void onModuleLoad() {
		LeafletEditableResourceInjector.ensureInjected();
	}

}
