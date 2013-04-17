package org.siemac.metamac.srm.web.category.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.category.enums.CategoriesToolStripButtonEnum;
import org.siemac.metamac.srm.web.category.view.handlers.CategorySchemeUiHandlers;
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
import org.siemac.metamac.srm.web.shared.category.CreateCategorisationAction;
import org.siemac.metamac.srm.web.shared.category.CreateCategorisationResult;
import org.siemac.metamac.srm.web.shared.category.CreateCategorySchemeTemporalVersionAction;
import org.siemac.metamac.srm.web.shared.category.CreateCategorySchemeTemporalVersionResult;
import org.siemac.metamac.srm.web.shared.category.DeleteCategorisationsAction;
import org.siemac.metamac.srm.web.shared.category.DeleteCategorisationsResult;
import org.siemac.metamac.srm.web.shared.category.DeleteCategoryAction;
import org.siemac.metamac.srm.web.shared.category.DeleteCategoryResult;
import org.siemac.metamac.srm.web.shared.category.GetCategoriesAction;
import org.siemac.metamac.srm.web.shared.category.GetCategoriesBySchemeAction;
import org.siemac.metamac.srm.web.shared.category.GetCategoriesBySchemeResult;
import org.siemac.metamac.srm.web.shared.category.GetCategoriesResult;
import org.siemac.metamac.srm.web.shared.category.GetCategorisationsByArtefactAction;
import org.siemac.metamac.srm.web.shared.category.GetCategorisationsByArtefactResult;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemeAction;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemeResult;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemeVersionsAction;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemeVersionsResult;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemesAction;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemesResult;
import org.siemac.metamac.srm.web.shared.category.SaveCategoryAction;
import org.siemac.metamac.srm.web.shared.category.SaveCategoryResult;
import org.siemac.metamac.srm.web.shared.category.SaveCategorySchemeAction;
import org.siemac.metamac.srm.web.shared.category.SaveCategorySchemeResult;
import org.siemac.metamac.srm.web.shared.category.UpdateCategorySchemeProcStatusAction;
import org.siemac.metamac.srm.web.shared.category.UpdateCategorySchemeProcStatusResult;
import org.siemac.metamac.srm.web.shared.category.VersionCategorySchemeAction;
import org.siemac.metamac.srm.web.shared.category.VersionCategorySchemeResult;
import org.siemac.metamac.srm.web.shared.criteria.CategorySchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.CategoryWebCriteria;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.UrnUtils;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;
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

    private final DispatchAsync                dispatcher;
    private final PlaceManager                 placeManager;

    private CategorySchemeMetamacDto           categorySchemeMetamacDto;

    private CategoriesToolStripPresenterWidget categoriesToolStripPresenterWidget;

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
        void setCategories(List<ItemHierarchyDto> categoryDtos);
        void startCategorySchemeEdition();

        // Categorisations

        void setCategorisations(List<CategorisationDto> categorisationDtos);
        void setCategorySchemesForCategorisations(GetCategorySchemesResult result);
        void setCategoriesForCategorisations(GetCategoriesResult result);
    }

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaContentCategoryScheme    = new Type<RevealContentHandler<?>>();

    public static final Object                        TYPE_SetContextAreaContentCategoriesToolBar = new Object();

    @Inject
    public CategorySchemePresenter(EventBus eventBus, CategorySchemeView view, CategorySchemeProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager,
            CategoriesToolStripPresenterWidget categoriesToolStripPresenterWidget) {
        super(eventBus, view, proxy);
        this.placeManager = placeManager;
        this.dispatcher = dispatcher;
        this.categoriesToolStripPresenterWidget = categoriesToolStripPresenterWidget;
        getView().setUiHandlers(this);
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        setInSlot(TYPE_SetContextAreaContentCategoriesToolBar, categoriesToolStripPresenterWidget);

        SetTitleEvent.fire(this, getConstants().categoryScheme());
        SelectMenuButtonEvent.fire(this, ToolStripButtonEnum.CATEGORIES);
        categoriesToolStripPresenterWidget.selectCategoriesMenuButton(CategoriesToolStripButtonEnum.CATEGORY_SCHEMES);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        String param = PlaceRequestUtils.getCategorySchemeParamFromUrl(placeManager);
        if (!StringUtils.isBlank(param)) {
            retrieveCompleteCategorySchemeByIdentifier(param);
        } else {
            MetamacSrmWeb.showErrorPage();
        }
    }

    //
    // CATEGORY SCHEME
    //

    private void retrieveCompleteCategorySchemeByIdentifier(String identifier) {
        // Retrieve category scheme by URN
        String urn = UrnUtils.generateUrn(UrnConstants.URN_SDMX_CLASS_CATEGORYSCHEME_PREFIX, identifier);
        if (!StringUtils.isBlank(urn)) {
            retrieveCompleteCategorySchemeByUrn(urn);
        }
    }

    private void retrieveCompleteCategorySchemeByUrn(String urn) {
        retrieveCompleteCategorySchemeByUrn(urn, false);
    }

    private void retrieveCompleteCategorySchemeByUrn(String urn, boolean startEdition) {
        retrieveCategorySchemeByUrn(urn, startEdition);
        retrieveCategorisations(urn);
    }

    private void retrieveCategorySchemeByUrn(String urn) {
        retrieveCategorySchemeByUrn(urn, false);
    }

    private void retrieveCategorySchemeByUrn(String urn, final boolean startEdition) {
        dispatcher.execute(new GetCategorySchemeAction(urn), new WaitingAsyncCallback<GetCategorySchemeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categorySchemeErrorRetrieve()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCategorySchemeResult result) {
                categorySchemeMetamacDto = result.getCategorySchemeMetamacDto();

                getView().setCategoryScheme(categorySchemeMetamacDto);
                if (startEdition) {
                    getView().startCategorySchemeEdition();
                }

                retrieveCategoriesByScheme(result.getCategorySchemeMetamacDto().getUrn());
                retrieveCategorySchemeVersions(result.getCategorySchemeMetamacDto().getUrn());
            }
        });
    }

    @Override
    public void retrieveCategorySchemeVersions(String categorySchemeUrn) {
        dispatcher.execute(new GetCategorySchemeVersionsAction(categorySchemeUrn), new WaitingAsyncCallback<GetCategorySchemeVersionsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categorySchemeErrorRetrievingVersions()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCategorySchemeVersionsResult result) {
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

                updateUrl();
            }
        });
    }

    //
    // CATEGORY SCHEME LIFECYLE
    //

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
                ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getMessageList(getMessages().categorySchemeCanceledValidity()), MessageTypeEnum.SUCCESS);
                retrieveCategorySchemeByUrn(urn);
            }
        });
    }

    @Override
    public void sendToProductionValidation(String urn, ProcStatusEnum currentProcStatus) {
        dispatcher.execute(new UpdateCategorySchemeProcStatusAction(urn, ProcStatusEnum.PRODUCTION_VALIDATION, currentProcStatus, null),
                new WaitingAsyncCallback<UpdateCategorySchemeProcStatusResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categorySchemeErrorSendingToProductionValidation()),
                                MessageTypeEnum.ERROR);
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
        dispatcher.execute(new UpdateCategorySchemeProcStatusAction(urn, ProcStatusEnum.DIFFUSION_VALIDATION, currentProcStatus, null),
                new WaitingAsyncCallback<UpdateCategorySchemeProcStatusResult>() {

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
        dispatcher.execute(new UpdateCategorySchemeProcStatusAction(urn, ProcStatusEnum.VALIDATION_REJECTED, currentProcStatus, null),
                new WaitingAsyncCallback<UpdateCategorySchemeProcStatusResult>() {

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
    public void publishInternally(final String urnToPublish, ProcStatusEnum currentProcStatus) {
        Boolean forceLatestFinal = null; // FIXME
        dispatcher.execute(new UpdateCategorySchemeProcStatusAction(urnToPublish, ProcStatusEnum.INTERNALLY_PUBLISHED, currentProcStatus, forceLatestFinal),
                new WaitingAsyncCallback<UpdateCategorySchemeProcStatusResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categorySchemeErrorPublishingInternally()), MessageTypeEnum.ERROR);
                    }
                    @Override
                    public void onWaitSuccess(UpdateCategorySchemeProcStatusResult result) {
                        ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getMessageList(getMessages().categorySchemePublishedInternally()), MessageTypeEnum.SUCCESS);
                        categorySchemeMetamacDto = result.getCategorySchemeDto();
                        getView().setCategoryScheme(categorySchemeMetamacDto);

                        // If the version published was a temporal version, reload the version list and the URL. Wwhen a temporal version is published, is automatically converted into a normal version
                        // (the URN changes!).
                        if (org.siemac.metamac.core.common.util.shared.UrnUtils.isTemporalUrn(urnToPublish)) {
                            retrieveCategorySchemeVersions(categorySchemeMetamacDto.getUrn());
                            updateUrl();
                        }
                    }
                });
    }

    @Override
    public void publishExternally(String urn, ProcStatusEnum currentProcStatus) {
        dispatcher.execute(new UpdateCategorySchemeProcStatusAction(urn, ProcStatusEnum.EXTERNALLY_PUBLISHED, currentProcStatus, null),
                new WaitingAsyncCallback<UpdateCategorySchemeProcStatusResult>() {

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
                retrieveCompleteCategorySchemeByUrn(categorySchemeMetamacDto.getUrn());
                updateUrl();
            }
        });
    }

    @Override
    public void createTemporalVersion(String urn) {
        dispatcher.execute(new CreateCategorySchemeTemporalVersionAction(urn), new WaitingAsyncCallback<CreateCategorySchemeTemporalVersionResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().resourceErrorEditingPublishedResource()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(CreateCategorySchemeTemporalVersionResult result) {
                CategorySchemePresenter.this.categorySchemeMetamacDto = result.getCategorySchemeMetamacDto();
                retrieveCompleteCategorySchemeByUrn(result.getCategorySchemeMetamacDto().getUrn(), true);
                updateUrl();
            }
        });
    }

    //
    // CATEGORIES
    //

    private void retrieveCategoriesByScheme(String categorySchemeUrn) {
        dispatcher.execute(new GetCategoriesBySchemeAction(categorySchemeUrn), new WaitingAsyncCallback<GetCategoriesBySchemeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categoryErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCategoriesBySchemeResult result) {
                getView().setCategories(result.getCategories());
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
                retrieveCategoriesByScheme(categorySchemeMetamacDto.getUrn());
            }
        });
    }

    // CATEGORISATIONS

    @Override
    public void retrieveCategorisations(String artefactCategorisedUrn) {
        dispatcher.execute(new GetCategorisationsByArtefactAction(artefactCategorisedUrn), new WaitingAsyncCallback<GetCategorisationsByArtefactResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categorisationErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCategorisationsByArtefactResult result) {
                getView().setCategorisations(result.getCategorisationDtos());
            }
        });
    }

    @Override
    public void createCategorisations(List<String> categoryUrns) {
        dispatcher.execute(new CreateCategorisationAction(categoryUrns, categorySchemeMetamacDto.getUrn(), RelatedResourceUtils.getDefaultMaintainerAsRelatedResourceDto().getUrn()),
                new WaitingAsyncCallback<CreateCategorisationResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categorisationErrorCreate()), MessageTypeEnum.ERROR);
                    }
                    @Override
                    public void onWaitSuccess(CreateCategorisationResult result) {
                        ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getMessageList(getMessages().categorisationCreated()), MessageTypeEnum.SUCCESS);
                        retrieveCategorisations(categorySchemeMetamacDto.getUrn());
                    }
                });
    }

    @Override
    public void deleteCategorisations(List<String> urns) {
        dispatcher.execute(new DeleteCategorisationsAction(urns), new WaitingAsyncCallback<DeleteCategorisationsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categorisationErrorDelete()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(DeleteCategorisationsResult result) {
                ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getMessageList(getMessages().categorisationDeleted()), MessageTypeEnum.SUCCESS);
                retrieveCategorisations(categorySchemeMetamacDto.getUrn());
            }
        });
    }

    @Override
    public void retrieveCategorySchemesForCategorisations(int firstResult, int maxResults, String criteria) {
        // The categories must be externally published
        CategorySchemeWebCriteria categorySchemeWebCriteria = new CategorySchemeWebCriteria(criteria);
        categorySchemeWebCriteria = MetamacWebCriteriaClientUtils.addCategorisationConditionToCategorySchemeWebCriteria(categorySchemeWebCriteria);
        dispatcher.execute(new GetCategorySchemesAction(firstResult, maxResults, categorySchemeWebCriteria), new WaitingAsyncCallback<GetCategorySchemesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categorySchemeErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCategorySchemesResult result) {
                getView().setCategorySchemesForCategorisations(result);
            }
        });
    }

    @Override
    public void retrieveCategoriesForCategorisations(int firstResult, int maxResults, String criteria, String categorySchemeUrn) {
        // The categories must be externally published
        CategoryWebCriteria categoryWebCriteria = new CategoryWebCriteria(criteria);
        categoryWebCriteria.setItemSchemeUrn(categorySchemeUrn);
        categoryWebCriteria = MetamacWebCriteriaClientUtils.addCategorisationConditionToCategoryWebCriteria(categoryWebCriteria);
        dispatcher.execute(new GetCategoriesAction(firstResult, maxResults, categoryWebCriteria), new WaitingAsyncCallback<GetCategoriesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CategorySchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categoryErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCategoriesResult result) {
                getView().setCategoriesForCategorisations(result);
            }
        });
    }

    @Override
    public void goToCategoryScheme(String urn) {
        if (!StringUtils.isBlank(urn)) {
            placeManager.revealRelativePlace(PlaceRequestUtils.buildRelativeCategorySchemePlaceRequest(urn), -1);
        }
    }

    @Override
    public void goToCategory(String urn) {
        if (!StringUtils.isBlank(urn)) {
            placeManager.revealRelativePlace(PlaceRequestUtils.buildRelativeCategoryPlaceRequest(urn));
        }
    }

    private void updateUrl() {
        PlaceRequest placeRequest = PlaceRequestUtils.buildRelativeCategorySchemePlaceRequest(categorySchemeMetamacDto.getUrn());
        placeManager.updateHistory(placeRequest, true);
    }
}
