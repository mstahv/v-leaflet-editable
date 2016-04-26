package org.vaadin.addon.leaflet.editable;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import org.vaadin.addon.leaflet.shared.Bounds;

import com.vividsolutions.jts.geom.Polygon;
import org.vaadin.addon.leaflet.LPolygon;
import org.vaadin.addon.leaflet.util.AbstractJTSField;
import org.vaadin.addon.leaflet.util.JTSUtil;

public class PolygonField extends AbstractJTSField<Polygon> implements
        FeatureDrawnListener {

    private LPolygon lPolyline;
    private LEditableMap editableMap;
    protected Button addHole;
    private LEditable lEditable;

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
    public Class<? extends Polygon> getType() {
        return Polygon.class;
    }

    @Override
    protected void prepareEditing() {
        if (lPolyline == null) {
            lPolyline = new LPolygon();
            map.addLayer(lPolyline);
        }
        final Polygon internalValue = getInternalValue();
        final Polygon toPresentation = getCrsTranslator()
                .toPresentation(internalValue);
        lPolyline.setGeometry(toPresentation);
        lEditable = new LEditable(lPolyline);
        getAddHoleButton().setEnabled(true);
        lEditable.addFeatureModifiedListener(new FeatureModifiedListener() {

            @Override
            public void featureModified(FeatureModifiedEvent event) {
                setValue(getCrsTranslator().toModel(
                        JTSUtil.toPolygon(lPolyline)));
            }
        });
        map.zoomToExtent(new Bounds(lPolyline.getPoints()));
    }

    @Override
    protected final void prepareDrawing() {
        getEditableMap().addFeatureDrawnListener(this);
        getEditableMap().startPolygon();
    }

    protected final LEditableMap getEditableMap() {
        if (editableMap == null) {
            editableMap = new LEditableMap(getMap());
        }
        return editableMap;
    }

    @Override
    public void featureDrawn(FeatureDrawnEvent event) {
        setValue(getCrsTranslator().toModel(
                JTSUtil.toPolygon((LPolygon) event
                        .getDrawnFeature())));
        getEditableMap().removeFeatureDrawnListener(this);
    }
    
    @Override
    protected void prepareViewing() {
    	throw new IllegalArgumentException("ReadOnly mode is not supported for " + this.getClass().getSimpleName());
    }

}
