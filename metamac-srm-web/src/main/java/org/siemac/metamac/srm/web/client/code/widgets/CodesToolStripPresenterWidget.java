package org.siemac.metamac.srm.web.client.code.widgets;

import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.HasClickHandlers;

public class CodesToolStripPresenterWidget extends PresenterWidget<CodesToolStripPresenterWidget.CodesToolStripView> {

    private final PlaceManager placeManager;

    public interface CodesToolStripView extends View {

        HasClickHandlers getCodelistFamiliesButton();
        HasClickHandlers getCodelistsButton();
        HasClickHandlers getVariableFamiliesButton();
        HasClickHandlers getVariablesButton();
    }

    @Inject
    public CodesToolStripPresenterWidget(EventBus eventBus, CodesToolStripView toolStripView, PlaceManager placeManager) {
        super(eventBus, toolStripView);
        this.placeManager = placeManager;
    }

    @Override
    protected void onBind() {
        super.onBind();

        registerHandler(getView().getCodelistFamiliesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteCodelistFamilyListPlaceRequest());
            }
        }));

        registerHandler(getView().getCodelistsButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteCodelistListPlaceRequest());
            }
        }));

        registerHandler(getView().getVariableFamiliesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteVariableFamilyListPlaceRequest());
            }
        }));

        registerHandler(getView().getVariablesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteVariableListPlaceRequest());
            }
        }));
    }
}
