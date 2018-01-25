package org.vaadin.addon.leaflet.editable;

import org.vaadin.addon.leaflet.LPolygon;
import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addon.leaflet.util.JTSUtil;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vividsolutions.jts.geom.Polygon;

public class PolygonField extends AbstractEditableJTSField<Polygon> implements
        FeatureDrawnListener {

    private LPolygon lPolyline;

    protected Button addHole;

    public PolygonField() {
    }

    public PolygonField(String caption) {
        this();
        setCaption(caption);
    }

    @Override
    protected Component initContent() {
        // configures map etc.
        super.initContent();
        AbsoluteLayout absoluteLayout = new AbsoluteLayout();
        absoluteLayout.addComponent(getMap());
        absoluteLayout.addComponent(getToolbar(), "top:5px; right:5px;z-index:1000");
        return absoluteLayout;
    }

    protected Component getToolbar() {
        return new HorizontalLayout(getAddHoleButton());
    }

    public Button getAddHoleButton() {
        if (addHole == null) {
            addHole = new Button(FontAwesome.SCISSORS);
            addHole.setEnabled(false);
            addHole.addClickListener(new Button.ClickListener() {

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    lEditable.newHole();
                }
            });
        }
        return addHole;
    }

    @Override
    protected void prepareEditing(boolean userOriginatedValuechangeEvent) {
        if (userOriginatedValuechangeEvent) {
            return;
        }
        if (lPolyline != null) {
            map.removeComponent(lPolyline);
        }
        lPolyline = new LPolygon();
        map.addLayer(lPolyline);
        final Polygon internalValue = getValue();
        final Polygon toPresentation = getCrsTranslator()
                .toPresentation(internalValue);
        lPolyline.setGeometry(toPresentation);
        lEditable = new LEditable(lPolyline);
        getAddHoleButton().setEnabled(true);
        lEditable.addFeatureModifiedListener(new FeatureModifiedListener() {

            @Override
            public void featureModified(FeatureModifiedEvent event) {
                setValue(getCrsTranslator().toModel(
                        JTSUtil.toPolygon(lPolyline)), true);
            }
        });
        map.zoomToExtent(new Bounds(lPolyline.getPoints()));
    }

    @Override
    protected final void prepareDrawing() {
        if (lPolyline != null) {
            map.removeLayer(lPolyline);
            lPolyline = null;
        }

        featureDrawnListenerReg = getEditableMap().addFeatureDrawnListener(this);
        getEditableMap().startPolygon();
    }

    @Override
    public void featureDrawn(FeatureDrawnEvent event) {
        setValue(getCrsTranslator().toModel(
                JTSUtil.toPolygon((LPolygon) event
                        .getDrawnFeature())));
        featureDrawnListenerReg.remove();
    }

    @Override
    protected void prepareViewing() {
        super.prepareViewing();
        getAddHoleButton().setEnabled(false);
    }

}
