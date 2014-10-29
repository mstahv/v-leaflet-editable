package org.vaadin.addon.leaflet.editable.client;

import org.peimari.gleaflet.client.Circle;
import org.peimari.gleaflet.client.EditableFeature;
import org.peimari.gleaflet.client.FeatureEditedListener;
import org.peimari.gleaflet.client.Polyline;
import org.vaadin.addon.leaflet.client.AbstractLeafletLayerConnector;
import org.vaadin.addon.leaflet.client.LeafletCircleConnector;
import org.vaadin.addon.leaflet.client.U;
import org.vaadin.addon.leaflet.editable.LEditable;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.ui.Connect;
import org.peimari.gleaflet.client.resources.LeafletEditableResourceInjector;

@Connect(LEditable.class)
public class EditableConnector extends AbstractExtensionConnector {
    
	static {
		LeafletEditableResourceInjector.ensureInjected();
	}

	private EditableServerRcp rpc = RpcProxy.create(EditableServerRcp.class, this);

	private EditableFeature ef;

	@Override
	protected void extend(final ServerConnector target) {
        registerRpc(EditableClientRcp.class, new EditableClientRcp() {

            @Override
            public void newHole() {
                ef.newHole();
            }
        });
	}

	@Override
	public void onStateChanged(StateChangeEvent stateChangeEvent) {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				final AbstractLeafletLayerConnector c = (AbstractLeafletLayerConnector) getParent();
				ef = (EditableFeature) c.getLayer();

				ef.addEditListener(new FeatureEditedListener() {

					@Override
					public void onEdit() {

						if (c instanceof LeafletCircleConnector) {
							Circle circle = (Circle) c.getLayer();
							rpc.circleModified(c, U.toPoint(circle.getLatLng()), circle.getRadius());
						} else {
							// Polyline/gon
							Polyline p = (Polyline) c.getLayer();
							rpc.polylineModified(c,
									U.toPointArray(p.getLatLngs()));
						}
					}
				});
				ef.enableEdit();
			}
		});
	}
	
	@Override
	public void onUnregister() {
		super.onUnregister();
		ef.disableEdit();
	}

}
