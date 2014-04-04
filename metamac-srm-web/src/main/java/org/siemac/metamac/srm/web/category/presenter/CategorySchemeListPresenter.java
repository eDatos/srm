package org.siemac.metamac.srm.web.category.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.navigation.shared.NameTokens;
import org.siemac.metamac.srm.web.category.enums.CategoriesToolStripButtonEnum;
import org.siemac.metamac.srm.web.category.view.handlers.CategorySchemeListUiHandlers;
import org.siemac.metamac.srm.web.category.widgets.presenter.CategoriesToolStripPresenterWidget;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.constants.SrmWebConstants;
import org.siemac.metamac.srm.web.client.enums.ExportDetailEnum;
import org.siemac.metamac.srm.web.client.enums.ExportReferencesEnum;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.MetamacWebCriteriaClientUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.client.utils.WaitingAsyncCallbackHandlingExportResult;
import org.siemac.metamac.srm.web.shared.ExportSDMXResourceAction;
import org.siemac.metamac.srm.web.shared.category.CancelCategorySchemeValidityAction;
import org.siemac.metamac.srm.web.shared.category.CancelCategorySchemeValidityResult;
import org.siemac.metamac.srm.web.shared.category.DeleteCategorySchemesAction;
import org.siemac.metamac.srm.web.shared.category.DeleteCategorySchemesResult;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemesAction;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemesResult;
import org.siemac.metamac.srm.web.shared.category.SaveCategorySchemeAction;
import org.siemac.metamac.srm.web.shared.category.SaveCategorySchemeResult;
import org.siemac.metamac.srm.web.shared.criteria.CategorySchemeWebCriteria;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.utils.WaitingAsyncCallbackHandlingError;

import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
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

        // Search
        void clearSearchSection();
        CategorySchemeWebCriteria getCategorySchemeWebCriteria();
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
        retrieveCategorySchemes(SrmWebConstants.SCHEME_LIST_FIRST_RESULT, SrmWebConstants.SCHEME_LIST_MAX_RESULTS,
                MetamacWebCriteriaClientUtils.addLastVersionConditionToCategorySchemeWebCriteria(new CategorySchemeWebCriteria()));
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
        dispatcher.execute(new GetCategorySchemesAction(firstResult, maxResults, categorySchemeWebCriteria), new WaitingAsyncCallbackHandlingError<GetCategorySchemesResult>(this) {

            @Override
            public void onWaitSuccess(GetCategorySchemesResult result) {
                getView().setCategorySchemePaginatedList(result);
            }
        });
    }

    @Override
    public void createCategoryScheme(CategorySchemeMetamacDto categorySchemeDto) {
        dispatcher.execute(new SaveCategorySchemeAction(categorySchemeDto), new WaitingAsyncCallbackHandlingError<SaveCategorySchemeResult>(this) {

            @Override
            public void onWaitSuccess(SaveCategorySchemeResult result) {
                fireSuccessMessage(getMessages().categorySchemeSaved());
                retrieveCategorySchemes(SrmWebConstants.SCHEME_LIST_FIRST_RESULT, SrmWebConstants.SCHEME_LIST_MAX_RESULTS,
                        MetamacWebCriteriaClientUtils.addLastVersionConditionToCategorySchemeWebCriteria(getView().getCategorySchemeWebCriteria()));
            }
        });
    }

    @Override
    public void deleteCategorySchemes(List<String> urns) {
        dispatcher.execute(new DeleteCategorySchemesAction(urns), new WaitingAsyncCallbackHandlingError<DeleteCategorySchemesResult>(this) {

            @Override
            public void onWaitSuccess(DeleteCategorySchemesResult result) {
                fireSuccessMessage(getMessages().categorySchemeDeleted());
            }

            @Override
            protected void afterResult() {
                retrieveCategorySchemes(SrmWebConstants.SCHEME_LIST_FIRST_RESULT, SrmWebConstants.SCHEME_LIST_MAX_RESULTS,
                        MetamacWebCriteriaClientUtils.addLastVersionConditionToCategorySchemeWebCriteria(getView().getCategorySchemeWebCriteria()));
            }
        });
    }

    @Override
    public void exportCategorySchemes(List<String> urns, ExportDetailEnum detail, ExportReferencesEnum references) {
        dispatcher.execute(new ExportSDMXResourceAction(urns, RelatedResourceTypeEnum.CATEGORY_SCHEME, detail, references), new WaitingAsyncCallbackHandlingExportResult(this));
    }

    @Override
    public void cancelValidity(List<String> urns) {
        dispatcher.execute(new CancelCategorySchemeValidityAction(urns), new WaitingAsyncCallbackHandlingError<CancelCategorySchemeValidityResult>(this) {

            @Override
            public void onWaitSuccess(CancelCategorySchemeValidityResult result) {
                fireSuccessMessage(getMessages().categorySchemeCanceledValidity());
            }

            @Override
            protected void afterResult() {
                retrieveCategorySchemes(SrmWebConstants.SCHEME_LIST_FIRST_RESULT, SrmWebConstants.SCHEME_LIST_MAX_RESULTS,
                        MetamacWebCriteriaClientUtils.addLastVersionConditionToCategorySchemeWebCriteria(getView().getCategorySchemeWebCriteria()));
            }
        });
    }

    //
    // NAVIGATION
    //

    @Override
    public void goTo(List<PlaceRequest> location) {
        if (location != null && !location.isEmpty()) {
            placeManager.revealPlaceHierarchy(location);
        }
    }

    @Override
    public void goToCategoryScheme(String urn) {
        if (!StringUtils.isBlank(urn)) {
            placeManager.revealRelativePlace(PlaceRequestUtils.buildRelativeCategorySchemePlaceRequest(urn));
        }
    }
}
