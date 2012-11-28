package org.siemac.metamac.srm.web.client.category.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.category.view.handlers.CategorySchemeUiHandlers;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.client.widgets.presenter.ToolStripPresenterWidget;
import org.siemac.metamac.srm.web.shared.category.CancelCategorySchemeValidityAction;
import org.siemac.metamac.srm.web.shared.category.CancelCategorySchemeValidityResult;
import org.siemac.metamac.srm.web.shared.category.DeleteCategoryAction;
import org.siemac.metamac.srm.web.shared.category.DeleteCategoryResult;
import org.siemac.metamac.srm.web.shared.category.GetCategoryListBySchemeAction;
import org.siemac.metamac.srm.web.shared.category.GetCategoryListBySchemeResult;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemeAction;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemeResult;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemeVersionListAction;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemeVersionListResult;
import org.siemac.metamac.srm.web.shared.category.SaveCategoryAction;
import org.siemac.metamac.srm.web.shared.category.SaveCategoryResult;
import org.siemac.metamac.srm.web.shared.category.SaveCategorySchemeAction;
import org.siemac.metamac.srm.web.shared.category.SaveCategorySchemeResult;
import org.siemac.metamac.srm.web.shared.category.UpdateCategorySchemeProcStatusAction;
import org.siemac.metamac.srm.web.shared.category.UpdateCategorySchemeProcStatusResult;
import org.siemac.metamac.srm.web.shared.category.VersionCategorySchemeAction;
import org.siemac.metamac.srm.web.shared.category.VersionCategorySchemeResult;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
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

public class CategorySchemePresenter extends Presenter<CategorySchemePresenter.CategorySchemeView, CategorySchemePresenter.CategorySchemeProxy> implements CategorySchemeUiHandlers {

    private final DispatchAsync      dispatcher;
    private final PlaceManager       placeManager;
    private ToolStripPresenterWidget toolStripPresenterWidget;

    private CategorySchemeMetamacDto categorySchemeMetamacDto;

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbCategoryScheme();
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.categorySchemePage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface CategorySchemeProxy extends Proxy<CategorySchemePresenter>, Place {
    }

    public interface CategorySchemeView extends View, HasUiHandlers<CategorySchemeUiHandlers> {

        void setCategoryScheme(CategorySchemeMetamacDto categorySchemeMetamacDto);
        void setCategorySchemeVersions(List<CategorySchemeMetamacDto> categorySchemeMetamacDtos);
        void setCategoryList(List<ItemHierarchyDto> categoryDtos);
    }

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaContentCategoryScheme        = new Type<RevealContentHandler<?>>();

    public static final Object                        TYPE_SetContextAreaContentCategorySchemeToolBar = new Object();

    @Inject
    public CategorySchemePresenter(EventBus eventBus, CategorySchemeView view, CategorySchemeProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager,
            ToolStripPresenterWidget toolStripPresenterWidget) {
        super(eventBus, view, proxy);
        this.placeManager = placeManager;
        this.dispatcher = dispatcher;
        this.toolStripPresenterWidget = toolStripPresenterWidget;
        getView().setUiHandlers(this);
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        setInSlot(TYPE_SetContextAreaContentCategorySchemeToolBar, toolStripPresenterWidget);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        String urn = PlaceRequestUtils.getCategorySchemeParamFromUrl(placeManager);
        if (!StringUtils.isBlank(urn)) {
            retrieveCategoryScheme(urn);
        } else {
            MetamacSrmWeb.showErrorPage();
        }
    }

    @Override
    public void retrieveCategoryScheme(String identifier) {
        // Retrieve category scheme by URN
        String urn = UrnUtils.generateUrn(UrnConstants.URN_SDMX_CLASS_CATEGORYSCHEME_PREFIX, identifier);
        if (!StringUtils.isBlank(urn)) {
            retrieveCategorySchemeByUrn(urn);
        }
    }

    private void retrieveCategorySchemeByUrn(String urn) {
        dispatcher.execute(new GetCategorySchemeAction(urn), new WaitingAsyncCallback<GetCategorySchemeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categorySchemeErrorRetrieve()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCategorySchemeResult result) {
                categorySchemeMetamacDto = result.getCategorySchemeMetamacDto();
                getView().setCategoryScheme(categorySchemeMetamacDto);
                retrieveCategoryListByScheme(result.getCategorySchemeMetamacDto().getUrn());
                retrieveCategorySchemeVersions(result.getCategorySchemeMetamacDto().getUrn());
            }
        });
    }

    @Override
    public void retrieveCategoryListByScheme(String categorySchemeUrn) {
        dispatcher.execute(new GetCategoryListBySchemeAction(categorySchemeUrn), new WaitingAsyncCallback<GetCategoryListBySchemeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categoryErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCategoryListBySchemeResult result) {
                getView().setCategoryList(result.getCategories());
            }
        });
    }

    @Override
    public void retrieveCategorySchemeVersions(String categorySchemeUrn) {
        dispatcher.execute(new GetCategorySchemeVersionListAction(categorySchemeUrn), new WaitingAsyncCallback<GetCategorySchemeVersionListResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categorySchemeErrorRetrievingVersions()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCategorySchemeVersionListResult result) {
                getView().setCategorySchemeVersions(result.getCategorySchemeMetamacDtos());
            }
        });
    }

    @Override
    public void saveCategoryScheme(CategorySchemeMetamacDto categoryScheme) {
        dispatcher.execute(new SaveCategorySchemeAction(categoryScheme), new WaitingAsyncCallback<SaveCategorySchemeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categorySchemeErrorSave()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(SaveCategorySchemeResult result) {
                categorySchemeMetamacDto = result.getCategorySchemeSaved();
                ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getMessageList(getMessages().categorySchemeSaved()), MessageTypeEnum.SUCCESS);
                getView().setCategoryScheme(categorySchemeMetamacDto);

                // Update URL
                PlaceRequest placeRequest = PlaceRequestUtils.buildCategorySchemePlaceRequest(categorySchemeMetamacDto.getUrn());
                placeManager.updateHistory(placeRequest, true);
            }
        });
    }

    @Override
    public void cancelValidity(final String urn) {
        List<String> urns = new ArrayList<String>();
        urns.add(urn);
        dispatcher.execute(new CancelCategorySchemeValidityAction(urns), new WaitingAsyncCallback<CancelCategorySchemeValidityResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categorySchemeErrorCancelValidity()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(CancelCategorySchemeValidityResult result) {
                ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getMessageList(getMessages().categorySchemeDeleted()), MessageTypeEnum.SUCCESS);
                retrieveCategorySchemeByUrn(urn);
            }
        });
    }

    @Override
    public void sendToProductionValidation(String urn, ProcStatusEnum currentProcStatus) {
        dispatcher.execute(new UpdateCategorySchemeProcStatusAction(urn, ProcStatusEnum.PRODUCTION_VALIDATION, currentProcStatus), new WaitingAsyncCallback<UpdateCategorySchemeProcStatusResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categorySchemeErrorSendingToProductionValidation()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(UpdateCategorySchemeProcStatusResult result) {
                ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getMessageList(getMessages().categorySchemeSentToProductionValidation()), MessageTypeEnum.SUCCESS);
                categorySchemeMetamacDto = result.getCategorySchemeDto();
                getView().setCategoryScheme(categorySchemeMetamacDto);
            }
        });
    }

    @Override
    public void sendToDiffusionValidation(String urn, ProcStatusEnum currentProcStatus) {
        dispatcher.execute(new UpdateCategorySchemeProcStatusAction(urn, ProcStatusEnum.DIFFUSION_VALIDATION, currentProcStatus), new WaitingAsyncCallback<UpdateCategorySchemeProcStatusResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categorySchemeErrorSendingToDiffusionValidation()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(UpdateCategorySchemeProcStatusResult result) {
                ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getMessageList(getMessages().categorySchemeSentToDiffusionValidation()), MessageTypeEnum.SUCCESS);
                categorySchemeMetamacDto = result.getCategorySchemeDto();
                getView().setCategoryScheme(categorySchemeMetamacDto);
            }
        });
    }

    @Override
    public void rejectValidation(String urn, ProcStatusEnum currentProcStatus) {
        dispatcher.execute(new UpdateCategorySchemeProcStatusAction(urn, ProcStatusEnum.VALIDATION_REJECTED, currentProcStatus), new WaitingAsyncCallback<UpdateCategorySchemeProcStatusResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categorySchemeErrorRejecting()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(UpdateCategorySchemeProcStatusResult result) {
                ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getMessageList(getMessages().categorySchemeRejected()), MessageTypeEnum.SUCCESS);
                categorySchemeMetamacDto = result.getCategorySchemeDto();
                getView().setCategoryScheme(categorySchemeMetamacDto);
            }
        });
    }

    @Override
    public void publishInternally(String urn, ProcStatusEnum currentProcStatus) {
        dispatcher.execute(new UpdateCategorySchemeProcStatusAction(urn, ProcStatusEnum.INTERNALLY_PUBLISHED, currentProcStatus), new WaitingAsyncCallback<UpdateCategorySchemeProcStatusResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categorySchemeErrorPublishingInternally()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(UpdateCategorySchemeProcStatusResult result) {
                ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getMessageList(getMessages().categorySchemePublishedInternally()), MessageTypeEnum.SUCCESS);
                categorySchemeMetamacDto = result.getCategorySchemeDto();
                getView().setCategoryScheme(categorySchemeMetamacDto);
            }
        });
    }

    @Override
    public void publishExternally(String urn, ProcStatusEnum currentProcStatus) {
        dispatcher.execute(new UpdateCategorySchemeProcStatusAction(urn, ProcStatusEnum.EXTERNALLY_PUBLISHED, currentProcStatus), new WaitingAsyncCallback<UpdateCategorySchemeProcStatusResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categorySchemeErrorPublishingExternally()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(UpdateCategorySchemeProcStatusResult result) {
                ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getMessageList(getMessages().categorySchemePublishedExternally()), MessageTypeEnum.SUCCESS);
                categorySchemeMetamacDto = result.getCategorySchemeDto();
                getView().setCategoryScheme(categorySchemeMetamacDto);
            }
        });
    }

    @Override
    public void versioning(String urn, VersionTypeEnum versionType) {
        dispatcher.execute(new VersionCategorySchemeAction(urn, versionType), new WaitingAsyncCallback<VersionCategorySchemeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categorySchemeErrorVersioning()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(VersionCategorySchemeResult result) {
                ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getMessageList(getMessages().categorySchemeVersioned()), MessageTypeEnum.SUCCESS);
                categorySchemeMetamacDto = result.getCategorySchemeMetamacDto();
                retrieveCategorySchemeByUrn(categorySchemeMetamacDto.getUrn());

                // Update URL
                PlaceRequest placeRequest = PlaceRequestUtils.buildCategorySchemePlaceRequest(categorySchemeMetamacDto.getUrn());
                placeManager.updateHistory(placeRequest, true);
            }
        });
    }

    @Override
    public void saveCategory(CategoryMetamacDto categoryDto) {
        dispatcher.execute(new SaveCategoryAction(categoryDto), new WaitingAsyncCallback<SaveCategoryResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categoryErrorSave()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(SaveCategoryResult result) {
                ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getMessageList(getMessages().categorySaved()), MessageTypeEnum.SUCCESS);
                retrieveCategorySchemeByUrn(categorySchemeMetamacDto.getUrn());
            }
        });
    }

    @Override
    public void deleteCategory(ItemDto itemDto) {
        dispatcher.execute(new DeleteCategoryAction(itemDto.getUrn()), new WaitingAsyncCallback<DeleteCategoryResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categoryErrorDelete()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(DeleteCategoryResult result) {
                ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getMessageList(getMessages().categoryDeleted()), MessageTypeEnum.SUCCESS);
                retrieveCategoryListByScheme(categorySchemeMetamacDto.getUrn());
            }
        });
    }

    @Override
    public void goToCategoryScheme(String urn) {
        if (!StringUtils.isBlank(urn)) {
            placeManager.revealRelativePlace(PlaceRequestUtils.buildCategorySchemePlaceRequest(urn), -1);
        }
    }

    @Override
    public void goToCategory(String urn) {
        if (!StringUtils.isBlank(urn)) {
            placeManager.revealRelativePlace(PlaceRequestUtils.buildCategoryPlaceRequest(urn));
        }
    }

}
