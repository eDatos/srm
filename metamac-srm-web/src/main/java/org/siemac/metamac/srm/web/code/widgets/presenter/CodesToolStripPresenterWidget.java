package org.siemac.metamac.srm.web.code.widgets.presenter;

import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.code.enums.CodesToolStripButtonEnum;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
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
        HasClickHandlers getCodesButton();
        HasClickHandlers getVariableFamiliesButton();
        HasClickHandlers getVariablesButton();
        HasClickHandlers getVariableElementsButton();

        void selectButton(CodesToolStripButtonEnum button);
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
                placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteCodelistFamiliesPlaceRequest());
            }
        }));

        registerHandler(getView().getCodelistsButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteCodelistsPlaceRequest());
            }
        }));

        registerHandler(getView().getCodesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteCodesPlaceRequest());
            }
        }));

        registerHandler(getView().getVariableFamiliesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteVariableFamiliesPlaceRequest());
            }
        }));

        registerHandler(getView().getVariablesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteVariablesPlaceRequest());
            }
        }));

        registerHandler(getView().getVariableElementsButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteVariableElementsPlaceRequest());
            }
        }));
    }

    public void selectCodesMenuButton(CodesToolStripButtonEnum codesToolStripButton) {
        getView().selectButton(codesToolStripButton);
    }
}
