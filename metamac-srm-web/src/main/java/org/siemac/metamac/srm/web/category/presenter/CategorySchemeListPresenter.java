package org.siemac.metamac.srm.web.category.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.web.category.enums.CategoriesToolStripButtonEnum;
import org.siemac.metamac.srm.web.category.view.handlers.CategorySchemeListUiHandlers;
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
import org.siemac.metamac.srm.web.shared.category.CancelCategorySchemeValidityAction;
import org.siemac.metamac.srm.web.shared.category.CancelCategorySchemeValidityResult;
import org.siemac.metamac.srm.web.shared.category.DeleteCategorySchemesAction;
import org.siemac.metamac.srm.web.shared.category.DeleteCategorySchemesResult;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemesAction;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemesResult;
import org.siemac.metamac.srm.web.shared.category.SaveCategorySchemeAction;
import org.siemac.metamac.srm.web.shared.category.SaveCategorySchemeResult;
import org.siemac.metamac.srm.web.shared.criteria.CategorySchemeWebCriteria;
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

public class CategorySchemeListPresenter extends Presenter<CategorySchemeListPresenter.CategorySchemeListView, CategorySchemeListPresenter.CategorySchemeListProxy>
        implements
            CategorySchemeListUiHandlers {

    public final static int                           SCHEME_LIST_FIRST_RESULT                    = 0;
    public final static int                           SCHEME_LIST_MAX_RESULTS                     = 30;

    private final DispatchAsync                       dispatcher;
    private final PlaceManager                        placeManager;

    private CategoriesToolStripPresenterWidget        categoriesToolStripPresenterWidget;

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetStructuralResourcesToolBar          = new Type<RevealContentHandler<?>>();

    public static final Object                        TYPE_SetContextAreaContentCategoriesToolBar = new Object();

    @ProxyCodeSplit
    @NameToken(NameTokens.categorySchemeListPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface CategorySchemeListProxy extends Proxy<CategorySchemeListPresenter>, Place {
    }

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbCategorySchemeList();
    }

    public interface CategorySchemeListView extends View, HasUiHandlers<CategorySchemeListUiHandlers> {

        void setCategorySchemePaginatedList(GetCategorySchemesResult categorySchemesPaginatedList);
        void goToCategorySchemeListLastPageAfterCreate();
        void clearSearchSection();
    }

    @Inject
    public CategorySchemeListPresenter(EventBus eventBus, CategorySchemeListView categorySchemeListView, CategorySchemeListProxy categorySchemeListProxy, DispatchAsync dispatcher,
            PlaceManager placeManager, CategoriesToolStripPresenterWidget categoriesToolStripPresenterWidget) {
        super(eventBus, categorySchemeListView, categorySchemeListProxy);
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
        retrieveCategorySchemes(SCHEME_LIST_FIRST_RESULT, SCHEME_LIST_MAX_RESULTS, MetamacWebCriteriaClientUtils.getCategorySchemeWebCriteriaForLastVersion());
        // Clear search section
        getView().clearSearchSection();
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        setInSlot(TYPE_SetContextAreaContentCategoriesToolBar, categoriesToolStripPresenterWidget);

        SetTitleEvent.fire(this, getConstants().categorySchemes());
        SelectMenuButtonEvent.fire(this, ToolStripButtonEnum.CATEGORIES);
        categoriesToolStripPresenterWidget.selectCategoriesMenuButton(CategoriesToolStripButtonEnum.CATEGORY_SCHEMES);
    }

    @Override
    public void retrieveCategorySchemes(int firstResult, int maxResults, CategorySchemeWebCriteria categorySchemeWebCriteria) {
        dispatcher.execute(new GetCategorySchemesAction(firstResult, maxResults, categorySchemeWebCriteria), new WaitingAsyncCallback<GetCategorySchemesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CategorySchemeListPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categorySchemeErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCategorySchemesResult result) {
                getView().setCategorySchemePaginatedList(result);
            }
        });
    }

    @Override
    public void createCategoryScheme(CategorySchemeMetamacDto categorySchemeDto) {
        dispatcher.execute(new SaveCategorySchemeAction(categorySchemeDto), new WaitingAsyncCallback<SaveCategorySchemeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CategorySchemeListPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categorySchemeErrorSave()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(SaveCategorySchemeResult result) {
                ShowMessageEvent.fire(CategorySchemeListPresenter.this, ErrorUtils.getMessageList(getMessages().categorySchemeSaved()), MessageTypeEnum.SUCCESS);
                retrieveCategorySchemes(SCHEME_LIST_FIRST_RESULT, SCHEME_LIST_MAX_RESULTS, MetamacWebCriteriaClientUtils.getCategorySchemeWebCriteriaForLastVersion());
            }
        });
    }

    @Override
    public void deleteCategorySchemes(List<String> urns) {
        dispatcher.execute(new DeleteCategorySchemesAction(urns), new WaitingAsyncCallback<DeleteCategorySchemesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CategorySchemeListPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categorySchemeErrorDelete()), MessageTypeEnum.ERROR);
                retrieveCategorySchemes(SCHEME_LIST_FIRST_RESULT, SCHEME_LIST_MAX_RESULTS, MetamacWebCriteriaClientUtils.getCategorySchemeWebCriteriaForLastVersion());
            }
            @Override
            public void onWaitSuccess(DeleteCategorySchemesResult result) {
                ShowMessageEvent.fire(CategorySchemeListPresenter.this, ErrorUtils.getMessageList(getMessages().categorySchemeDeleted()), MessageTypeEnum.SUCCESS);
                retrieveCategorySchemes(SCHEME_LIST_FIRST_RESULT, SCHEME_LIST_MAX_RESULTS, MetamacWebCriteriaClientUtils.getCategorySchemeWebCriteriaForLastVersion());
            }
        });
    }

    @Override
    public void cancelValidity(List<String> urns) {
        dispatcher.execute(new CancelCategorySchemeValidityAction(urns), new WaitingAsyncCallback<CancelCategorySchemeValidityResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CategorySchemeListPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categorySchemeErrorCancelValidity()), MessageTypeEnum.ERROR);
                retrieveCategorySchemes(SCHEME_LIST_FIRST_RESULT, SCHEME_LIST_MAX_RESULTS, MetamacWebCriteriaClientUtils.getCategorySchemeWebCriteriaForLastVersion());
            }
            @Override
            public void onWaitSuccess(CancelCategorySchemeValidityResult result) {
                ShowMessageEvent.fire(CategorySchemeListPresenter.this, ErrorUtils.getMessageList(getMessages().categorySchemeCanceledValidity()), MessageTypeEnum.SUCCESS);
                retrieveCategorySchemes(SCHEME_LIST_FIRST_RESULT, SCHEME_LIST_MAX_RESULTS, MetamacWebCriteriaClientUtils.getCategorySchemeWebCriteriaForLastVersion());
            }
        });
    }

    @Override
    public void goToCategoryScheme(String urn) {
        if (!StringUtils.isBlank(urn)) {
            placeManager.revealRelativePlace(PlaceRequestUtils.buildRelativeCategorySchemePlaceRequest(urn));
        }
    }
}
