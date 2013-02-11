package org.siemac.metamac.srm.web.category.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.List;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.web.category.view.handlers.CategoryUiHandlers;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.shared.category.DeleteCategoryAction;
import org.siemac.metamac.srm.web.shared.category.DeleteCategoryResult;
import org.siemac.metamac.srm.web.shared.category.GetCategoriesBySchemeAction;
import org.siemac.metamac.srm.web.shared.category.GetCategoriesBySchemeResult;
import org.siemac.metamac.srm.web.shared.category.GetCategoryAction;
import org.siemac.metamac.srm.web.shared.category.GetCategoryResult;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemeAction;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemeResult;
import org.siemac.metamac.srm.web.shared.category.SaveCategoryAction;
import org.siemac.metamac.srm.web.shared.category.SaveCategoryResult;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.UrnUtils;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
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

public class CategoryPresenter extends Presenter<CategoryPresenter.CategoryView, CategoryPresenter.CategoryProxy> implements CategoryUiHandlers {

    private final DispatchAsync dispatcher;
    private final PlaceManager  placeManager;

    private String              categorySchemeUrn;
    private CategoryMetamacDto  categoryMetamacDto;

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbCategory();
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.categoryPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface CategoryProxy extends Proxy<CategoryPresenter>, Place {
    }

    public interface CategoryView extends View, HasUiHandlers<CategoryUiHandlers> {

        void setCategory(CategoryMetamacDto categoryDto);
        void setCategoryList(CategorySchemeMetamacDto categorySchemeMetamacDto, List<ItemHierarchyDto> itemHierarchyDtos);
    }

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaContentcategory = new Type<RevealContentHandler<?>>();

    @Inject
    public CategoryPresenter(EventBus eventBus, CategoryView view, CategoryProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager) {
        super(eventBus, view, proxy);
        this.dispatcher = dispatcher;
        this.placeManager = placeManager;
        getView().setUiHandlers(this);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        SetTitleEvent.fire(this, MetamacSrmWeb.getConstants().category());
        SelectMenuButtonEvent.fire(this, ToolStripButtonEnum.CATEGORIES);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        String schemeParam = PlaceRequestUtils.getCategorySchemeParamFromUrl(placeManager);
        String categoryIdentifier = PlaceRequestUtils.getCategoryParamFromUrl(placeManager);
        if (!StringUtils.isBlank(schemeParam) && !StringUtils.isBlank(categoryIdentifier)) {
            this.categorySchemeUrn = UrnUtils.generateUrn(UrnConstants.URN_SDMX_CLASS_CATEGORYSCHEME_PREFIX, schemeParam);
            String urn = UrnUtils.generateUrn(UrnConstants.URN_SDMX_CLASS_CATEGORY_PREFIX, schemeParam, categoryIdentifier);
            retrieveCategory(urn);
        } else {
            MetamacSrmWeb.showErrorPage();
        }
    }

    @Override
    public void retrieveCategory(String categoryUrn) {
        dispatcher.execute(new GetCategoryAction(categoryUrn), new WaitingAsyncCallback<GetCategoryResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CategoryPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categoryErrorRetrieve()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCategoryResult result) {
                getView().setCategory(result.getCategoryDto());
            }
        });
    }

    @Override
    public void saveCategory(CategoryMetamacDto categoryDto) {
        dispatcher.execute(new SaveCategoryAction(categoryDto), new WaitingAsyncCallback<SaveCategoryResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CategoryPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categoryErrorSave()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(SaveCategoryResult result) {
                categoryMetamacDto = result.getCategorySaved();
                ShowMessageEvent.fire(CategoryPresenter.this, ErrorUtils.getMessageList(getMessages().categorySchemeSaved()), MessageTypeEnum.SUCCESS);
                getView().setCategory(result.getCategorySaved());

                // Update URL
                PlaceRequest placeRequest = PlaceRequestUtils.buildRelativeCategoryPlaceRequest(categoryMetamacDto.getUrn());
                placeManager.updateHistory(placeRequest, true);
            }
        });
    }

    @Override
    public void deleteCategory(final ItemDto itemDto) {
        dispatcher.execute(new DeleteCategoryAction(itemDto.getUrn()), new WaitingAsyncCallback<DeleteCategoryResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CategoryPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categoryErrorDelete()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(DeleteCategoryResult result) {
                ShowMessageEvent.fire(CategoryPresenter.this, ErrorUtils.getMessageList(getMessages().categoryDeleted()), MessageTypeEnum.SUCCESS);
                // If deleted category had a category parent, go to this category parent. If not, go to the category scheme.
                if (itemDto.getItemParentUrn() != null) {
                    goToCategory(itemDto.getItemParentUrn());
                } else {
                    goToCategoryScheme(itemDto.getItemSchemeVersionUrn());
                }
            }
        });
    }

    @Override
    public void retrieveCategoryListByScheme(String categorySchemeUrn) {
        dispatcher.execute(new GetCategoriesBySchemeAction(categorySchemeUrn), new WaitingAsyncCallback<GetCategoriesBySchemeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CategoryPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categorySchemeErrorRetrievingCategoryList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCategoriesBySchemeResult result) {
                final List<ItemHierarchyDto> itemHierarchyDtos = result.getCategories();
                dispatcher.execute(new GetCategorySchemeAction(CategoryPresenter.this.categorySchemeUrn), new WaitingAsyncCallback<GetCategorySchemeResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fire(CategoryPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categorySchemeErrorRetrieve()), MessageTypeEnum.ERROR);
                    }
                    @Override
                    public void onWaitSuccess(GetCategorySchemeResult result) {
                        getView().setCategoryList(result.getCategorySchemeMetamacDto(), itemHierarchyDtos);
                    }
                });
            }
        });
    }

    @Override
    public void goToCategory(String urn) {
        placeManager.revealRelativePlace(PlaceRequestUtils.buildRelativeCategoryPlaceRequest(urn), -1);
    }

    private void goToCategoryScheme(String urn) {
        placeManager.revealRelativePlace(PlaceRequestUtils.buildRelativeCategorySchemePlaceRequest(urn), -2);
    }

    @Override
    public void retrieveCategorisations(String artefactCategorisedUrn) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void createCategorisations(List<String> categoryUrns) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteCategorisations(List<String> urns) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void retrieveCategorySchemesForCategorisations(int firstResult, int maxResults, String criteria) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void retrieveCategoriesForCategorisations(int firstResult, int maxResults, String criteria, String categorySchemeUrn) {
        throw new UnsupportedOperationException();
    }
}
