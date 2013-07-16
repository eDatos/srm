package org.siemac.metamac.srm.web.concept.widgets.presenter;

import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.concept.enums.ConceptsToolStripButtonEnum;

import com.google.web.bindery.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.HasClickHandlers;

public class ConceptsToolStripPresenterWidget extends PresenterWidget<ConceptsToolStripPresenterWidget.ConceptsToolStripView> {

    private final PlaceManager placeManager;

    public interface ConceptsToolStripView extends View {

        HasClickHandlers getConceptSchemesButton();
        HasClickHandlers getConceptButton();

        void selectButton(ConceptsToolStripButtonEnum button);
    }

    @Inject
    public ConceptsToolStripPresenterWidget(EventBus eventBus, ConceptsToolStripView toolStripView, PlaceManager placeManager) {
        super(eventBus, toolStripView);
        this.placeManager = placeManager;
    }

    @Override
    protected void onBind() {
        super.onBind();

        registerHandler(getView().getConceptSchemesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteConceptSchemesPlaceRequest());
            }
        }));

        registerHandler(getView().getConceptButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteConceptsPlaceRequest());
            }
        }));
    }

    public void selectConceptsMenuButton(ConceptsToolStripButtonEnum button) {
        getView().selectButton(button);
    }
}
