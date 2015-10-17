package org.vaadin.addon.leaflet.editable.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.ui.Connect;
import org.peimari.gleaflet.client.Circle;
import org.peimari.gleaflet.client.EditableFeature;
import org.peimari.gleaflet.client.FeatureEditedListener;
import org.peimari.gleaflet.client.Polyline;
import org.peimari.gleaflet.client.resources.LeafletEditableResourceInjector;
import org.vaadin.addon.leaflet.client.AbstractLeafletLayerConnector;
import org.vaadin.addon.leaflet.client.AbstractLeafletVectorConnector;
import org.vaadin.addon.leaflet.client.LeafletCircleConnector;
import org.vaadin.addon.leaflet.client.U;
import org.vaadin.addon.leaflet.editable.LEditable;

@Connect(LEditable.class)
public class EditableConnector extends AbstractExtensionConnector {
    
	static {
		LeafletEditableResourceInjector.ensureInjected();
	}

	private final EditableServerRcp rpc = RpcProxy.create(EditableServerRcp.class, this);

	private EditableFeature ef;

	@Override
	protected void extend(final ServerConnector target) {
        registerRpc(EditableClientRcp.class, new EditableClientRcp() {

			@Override
			public void newHole() {
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {

					@Override
					public void execute() {
						getFeature().newHole();
					}
				});
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
						if(isEnabled() && ef.isEnabled()) {
							if (c instanceof LeafletCircleConnector) {
								Circle circle = (Circle) c.getLayer();
								rpc.circleModified(c, U.toPoint(circle.getLatLng()), circle.getRadius());

							} else {
								// Polyline/gon
								Polyline p = (Polyline) c.getLayer();
								rpc.vectorModified(c, p.toGeoJSONString());
							}
						}
					}
				});
				ef.enableEdit();
			}
		});
	}

	public EditableFeature getFeature() {
		AbstractLeafletVectorConnector parent = (AbstractLeafletVectorConnector) getParent();
		ef = (EditableFeature) parent.getLayer();
		return ef;
	}
	
	@Override
	public void onUnregister() {
		super.onUnregister();
		ef.disableEdit();
        ef.removeEditListener();
	}

}
