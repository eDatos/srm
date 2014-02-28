package org.siemac.metamac.srm.web.organisation.widgets.presenter;

import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.organisation.enums.OrganisationsToolStripButtonEnum;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.HasClickHandlers;

public class OrganisationsToolStripPresenterWidget extends PresenterWidget<OrganisationsToolStripPresenterWidget.OrganisationsToolStripView> {

    private final PlaceManager placeManager;

    public interface OrganisationsToolStripView extends View {

        HasClickHandlers getOrganisationSchemesButton();
        HasClickHandlers getOrganisationButton();

        void selectButton(OrganisationsToolStripButtonEnum button);
    }

    @Inject
    public OrganisationsToolStripPresenterWidget(EventBus eventBus, OrganisationsToolStripView toolStripView, PlaceManager placeManager) {
        super(eventBus, toolStripView);
        this.placeManager = placeManager;
    }

    @Override
    protected void onBind() {
        super.onBind();

        registerHandler(getView().getOrganisationSchemesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteOrganisationSchemesPlaceRequest());
            }
        }));

        registerHandler(getView().getOrganisationButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteOrganisationsPlaceRequest());
            }
        }));
    }

    public void selectOrganisationsMenuButton(OrganisationsToolStripButtonEnum button) {
        getView().selectButton(button);
    }
}
