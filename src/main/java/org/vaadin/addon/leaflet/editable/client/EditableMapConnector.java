package org.vaadin.addon.leaflet.editable.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Window;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.ui.Connect;
import org.peimari.gleaflet.client.EditableMap;
import org.peimari.gleaflet.client.FeatureCreatedListener;
import org.peimari.gleaflet.client.LayerCreatedEvent;
import org.peimari.gleaflet.client.LayerGroup;
import org.peimari.gleaflet.client.resources.LeafletEditableResourceInjector;
import org.vaadin.addon.leaflet.client.LeafletMapConnector;
import org.vaadin.addon.leaflet.editable.LEditableMap;

@Connect(LEditableMap.class)
public class EditableMapConnector extends AbstractExtensionConnector {
    
	static {
		LeafletEditableResourceInjector.ensureInjected();
	}

	private final EditableMapServerRcp rpc = RpcProxy.create(EditableMapServerRcp.class, this);

	private EditableMap map;

	@Override
	protected void extend(final ServerConnector target) {
        registerRpc(EditableMapClientRcp.class, new EditableMapClientRcp() {

			@Override
			public void startPolygon() {
                getMap().startPolygon();
			}

            @Override
            public void startPolyline() {
                getMap().startPolyline();
            }
		});
	}

	@Override
	public void onStateChanged(StateChangeEvent stateChangeEvent) {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {

                getMap().addCreatedListener(new FeatureCreatedListener() {

                    @Override
                    public void onCreated(LayerCreatedEvent event) {
                        // send to server
                        rpc.vectorCreated(event.getLayer().toGeoJSONString());
                        // remove created layer lazily (server side should add it back if needed
                        // TODO lazily
                        LayerGroup lg = getMap().getNewFeatureLayer();
                        lg.removeLayer(event.getLayer());
                    }
                });
			}
		});
	}

	public EditableMap getMap() {
		LeafletMapConnector parent = (LeafletMapConnector) getParent();
		map = (EditableMap) parent.getMap().cast();
		return map;
	}
	
	@Override
	public void onUnregister() {
		super.onUnregister();
	}

}
