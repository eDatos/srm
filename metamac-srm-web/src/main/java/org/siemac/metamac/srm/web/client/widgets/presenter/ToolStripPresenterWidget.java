package org.siemac.metamac.srm.web.client.widgets.presenter;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.navigation.shared.NameTokens;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.HasClickHandlers;

@Deprecated
public class ToolStripPresenterWidget extends PresenterWidget<ToolStripPresenterWidget.ToolStripView> {

    private final PlaceManager placeManager;

    public interface ToolStripView extends View {

        HasClickHandlers getDsdsButton();
        HasClickHandlers getConceptSchemesButton();
        HasClickHandlers getOrganisationSchemesButton();
        HasClickHandlers getCategorySchemesButton();
        HasClickHandlers getCodelistButton();
    }

    @Inject
    public ToolStripPresenterWidget(EventBus eventBus, ToolStripView toolStripView, PlaceManager placeManager) {
        super(eventBus, toolStripView);
        this.placeManager = placeManager;
    }

    @Override
    protected void onBind() {
        super.onBind();

        registerHandler(getView().getDsdsButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                PlaceRequest resourcesRequest = new PlaceRequest(NameTokens.structuralResourcesPage);
                PlaceRequest dsdListRequest = new PlaceRequest(NameTokens.dsdListPage);
                List<PlaceRequest> placeRequestHierarchy = new ArrayList<PlaceRequest>();
                placeRequestHierarchy.add(resourcesRequest);
                placeRequestHierarchy.add(dsdListRequest);
                placeManager.revealPlaceHierarchy(placeRequestHierarchy);
            }
        }));

        registerHandler(getView().getConceptSchemesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                PlaceRequest resourcesRequest = new PlaceRequest(NameTokens.structuralResourcesPage);
                PlaceRequest conceptSchemeListRequest = new PlaceRequest(NameTokens.conceptSchemeListPage);
                List<PlaceRequest> placeRequestHierarchy = new ArrayList<PlaceRequest>();
                placeRequestHierarchy.add(resourcesRequest);
                placeRequestHierarchy.add(conceptSchemeListRequest);
                placeManager.revealPlaceHierarchy(placeRequestHierarchy);
            }
        }));

        registerHandler(getView().getOrganisationSchemesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                PlaceRequest resourcesRequest = new PlaceRequest(NameTokens.structuralResourcesPage);
                PlaceRequest organisationSchemeListRequest = new PlaceRequest(NameTokens.organisationSchemeListPage);
                List<PlaceRequest> placeRequestHierarchy = new ArrayList<PlaceRequest>();
                placeRequestHierarchy.add(resourcesRequest);
                placeRequestHierarchy.add(organisationSchemeListRequest);
                placeManager.revealPlaceHierarchy(placeRequestHierarchy);
            }
        }));

        registerHandler(getView().getCategorySchemesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                PlaceRequest resourcesRequest = new PlaceRequest(NameTokens.structuralResourcesPage);
                PlaceRequest categorySchemeListRequest = new PlaceRequest(NameTokens.categorySchemeListPage);
                List<PlaceRequest> placeRequestHierarchy = new ArrayList<PlaceRequest>();
                placeRequestHierarchy.add(resourcesRequest);
                placeRequestHierarchy.add(categorySchemeListRequest);
                placeManager.revealPlaceHierarchy(placeRequestHierarchy);
            }
        }));
        registerHandler(getView().getCodelistButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                PlaceRequest resourcesRequest = new PlaceRequest(NameTokens.structuralResourcesPage);
                PlaceRequest codelistListRequest = new PlaceRequest(NameTokens.codelistListPage);
                List<PlaceRequest> placeRequestHierarchy = new ArrayList<PlaceRequest>();
                placeRequestHierarchy.add(resourcesRequest);
                placeRequestHierarchy.add(codelistListRequest);
                placeManager.revealPlaceHierarchy(placeRequestHierarchy);
            }
        }));
    }
}
