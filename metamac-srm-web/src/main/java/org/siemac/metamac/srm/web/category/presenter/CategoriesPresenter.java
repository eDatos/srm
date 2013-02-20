package org.siemac.metamac.srm.web.category.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.web.category.enums.CategoriesToolStripButtonEnum;
import org.siemac.metamac.srm.web.category.view.handlers.CategoriesUiHandlers;
import org.siemac.metamac.srm.web.category.widgets.presenter.CategoriesToolStripPresenterWidget;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.utils.MetamacWebCriteriaClientUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.shared.category.GetCategoriesAction;
import org.siemac.metamac.srm.web.shared.category.GetCategoriesResult;
import org.siemac.metamac.srm.web.shared.criteria.CategoryWebCriteria;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TitleFunction;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

public class CategoriesPresenter extends Presenter<CategoriesPresenter.CategoriesView, CategoriesPresenter.CategoriesProxy> implements CategoriesUiHandlers {

    public final static int                           ITEM_LIST_FIRST_RESULT                      = 0;
    public final static int                           ITEM_LIST_MAX_RESULTS                       = 30;

    private final DispatchAsync                       dispatcher;
    private final PlaceManager                        placeManager;

    private CategoriesToolStripPresenterWidget        categoriesToolStripPresenterWidget;

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetStructuralResourcesToolBar          = new Type<RevealContentHandler<?>>();

    public static final Object                        TYPE_SetContextAreaContentCategoriesToolBar = new Object();

    @ProxyCodeSplit
    @NameToken(NameTokens.categoriesPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface CategoriesProxy extends Proxy<CategoriesPresenter>, Place {
    }

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbCategories();
    }

    public interface CategoriesView extends View, HasUiHandlers<CategoriesUiHandlers> {

        void setCategories(GetCategoriesResult result);
        void clearSearchSection();
    }

    @Inject
    public CategoriesPresenter(EventBus eventBus, CategoriesView categoriesView, CategoriesProxy categoriesProxy, DispatchAsync dispatcher, PlaceManager placeManager,
            CategoriesToolStripPresenterWidget categoriesToolStripPresenterWidget) {
        super(eventBus, categoriesView, categoriesProxy);
        this.placeManager = placeManager;
        this.dispatcher = dispatcher;
        this.categoriesToolStripPresenterWidget = categoriesToolStripPresenterWidget;
        getView().setUiHandlers(this);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        // Load concept schemes
        retrieveCategories(ITEM_LIST_FIRST_RESULT, ITEM_LIST_MAX_RESULTS, MetamacWebCriteriaClientUtils.addLastVersionConditionToCategoryWebCriteria(new CategoryWebCriteria()));
        // Clear search section
        getView().clearSearchSection();
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        setInSlot(TYPE_SetContextAreaContentCategoriesToolBar, categoriesToolStripPresenterWidget);

        SetTitleEvent.fire(this, getConstants().categories());
        SelectMenuButtonEvent.fire(this, ToolStripButtonEnum.CATEGORIES);
        categoriesToolStripPresenterWidget.selectCategoriesMenuButton(CategoriesToolStripButtonEnum.CATEGORIES);
    }

    @Override
    public void retrieveCategories(int firstResult, int maxResults, CategoryWebCriteria categoryWebCriteria) {
        dispatcher.execute(new GetCategoriesAction(firstResult, maxResults, categoryWebCriteria), new WaitingAsyncCallback<GetCategoriesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CategoriesPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categoryErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCategoriesResult result) {
                getView().setCategories(result);
            }
        });
    }

    @Override
    public void goToCategory(String categorySchemeUrn, String categoryUrn) {
        if (!StringUtils.isBlank(categorySchemeUrn) && !StringUtils.isBlank(categoryUrn)) {
            placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteCategoryPlaceRequest(categorySchemeUrn, categoryUrn));
        }
    }
}
