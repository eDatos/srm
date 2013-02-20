package org.siemac.metamac.srm.web.category.widgets.presenter;

import org.siemac.metamac.srm.web.category.enums.CategoriesToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.HasClickHandlers;

public class CategoriesToolStripPresenterWidget extends PresenterWidget<CategoriesToolStripPresenterWidget.CategoriesToolStripView> {

    private final PlaceManager placeManager;

    public interface CategoriesToolStripView extends View {

        HasClickHandlers getCategorySchemesButton();
        HasClickHandlers getCategoriesButton();

        void selectButton(CategoriesToolStripButtonEnum button);
    }

    @Inject
    public CategoriesToolStripPresenterWidget(EventBus eventBus, CategoriesToolStripView toolStripView, PlaceManager placeManager) {
        super(eventBus, toolStripView);
        this.placeManager = placeManager;
    }

    @Override
    protected void onBind() {
        super.onBind();

        registerHandler(getView().getCategorySchemesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteCategorySchemeListPlaceRequest());
            }
        }));

        registerHandler(getView().getCategoriesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                // TODO
            }
        }));
    }

    public void selectCategoriesMenuButton(CategoriesToolStripButtonEnum button) {
        getView().selectButton(button);
    }
}
