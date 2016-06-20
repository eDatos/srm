package org.siemac.metamac.srm.web.category.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.navigation.shared.NameTokens;
import org.siemac.metamac.srm.web.category.enums.CategoriesToolStripButtonEnum;
import org.siemac.metamac.srm.web.category.view.handlers.CategorySchemeUiHandlers;
import org.siemac.metamac.srm.web.category.widgets.presenter.CategoriesToolStripPresenterWidget;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.enums.ExportDetailEnum;
import org.siemac.metamac.srm.web.client.enums.ExportReferencesEnum;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.client.utils.WaitingAsyncCallbackHandlingExportResult;
import org.siemac.metamac.srm.web.shared.ExportSDMXResourceAction;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesAction;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.srm.web.shared.StructuralResourcesRelationEnum;
import org.siemac.metamac.srm.web.shared.category.CancelCategorisationValidityAction;
import org.siemac.metamac.srm.web.shared.category.CancelCategorisationValidityResult;
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
import org.siemac.metamac.srm.web.shared.category.DeleteCategorySchemesAction;
import org.siemac.metamac.srm.web.shared.category.DeleteCategorySchemesResult;
import org.siemac.metamac.srm.web.shared.category.ExportCategoriesAction;
import org.siemac.metamac.srm.web.shared.category.ExportCategoriesResult;
import org.siemac.metamac.srm.web.shared.category.GetCategoriesBySchemeAction;
import org.siemac.metamac.srm.web.shared.category.GetCategoriesBySchemeResult;
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
import org.siemac.metamac.srm.web.shared.concept.CopyCategorySchemeAction;
import org.siemac.metamac.srm.web.shared.concept.CopyCategorySchemeResult;
import org.siemac.metamac.srm.web.shared.criteria.CategorySchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.CategoryWebCriteria;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.events.ChangeWaitPopupVisibilityEvent;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.ApplicationEditionLanguages;
import org.siemac.metamac.web.common.client.utils.WaitingAsyncCallbackHandlingError;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;
import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;
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

public class CategorySchemePresenter extends Presenter<CategorySchemePresenter.CategorySchemeView, CategorySchemePresenter.CategorySchemeProxy> implements CategorySchemeUiHandlers {

    private final DispatchAsync                      dispatcher;
    private final PlaceManager                       placeManager;

    private CategorySchemeMetamacDto                 categorySchemeMetamacDto;

    private final CategoriesToolStripPresenterWidget categoriesToolStripPresenterWidget;

    @TitleFunction
    public static String getTranslatedTitle(PlaceRequest placeRequest) {
        return PlaceRequestUtils.getCategorySchemeBreadCrumbTitle(placeRequest);
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.categorySchemePage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface CategorySchemeProxy extends Proxy<CategorySchemePresenter>, Place {
    }

    public interface CategorySchemeView extends View, HasUiHandlers<CategorySchemeUiHandlers> {

        void setCategoryScheme(CategorySchemeMetamacDto categorySchemeMetamacDto);
        void setCategorySchemeVersions(List<CategorySchemeMetamacBasicDto> categorySchemeMetamacDtos);
        void setCategories(List<ItemVisualisationResult> categoryDtos);
        void startCategorySchemeEdition();
        void setLatestCategorySchemeForInternalPublication(GetCategorySchemesResult result);
        void selectCategorySchemeTab();

        // Categorisations

        void setCategorisations(List<CategorisationDto> categorisationDtos);
        void setCategorySchemesForCategorisations(GetRelatedResourcesResult result);
        void setCategoriesForCategorisations(GetRelatedResourcesResult result);
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
        getView().selectCategorySchemeTab();
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
        dispatcher.execute(new GetCategorySchemeAction(urn), new WaitingAsyncCallbackHandlingError<GetCategorySchemeResult>(this) {

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
        dispatcher.execute(new GetCategorySchemeVersionsAction(categorySchemeUrn), new WaitingAsyncCallbackHandlingError<GetCategorySchemeVersionsResult>(this) {

            @Override
            public void onWaitSuccess(GetCategorySchemeVersionsResult result) {
                getView().setCategorySchemeVersions(result.getCategorySchemeMetamacDtos());
            }
        });
    }

    @Override
    public void retrieveLatestCategoryScheme(CategorySchemeMetamacDto categorySchemeMetamacDto) {
        CategorySchemeWebCriteria criteria = new CategorySchemeWebCriteria();
        criteria.setCodeEQ(categorySchemeMetamacDto.getCode());
        criteria.setMaintainerUrn(categorySchemeMetamacDto.getMaintainer().getUrn());
        criteria.setIsLatestFinal(true);
        dispatcher.execute(new GetCategorySchemesAction(0, 1, criteria), new WaitingAsyncCallbackHandlingError<GetCategorySchemesResult>(this) {

            @Override
            public void onWaitSuccess(GetCategorySchemesResult result) {
                getView().setLatestCategorySchemeForInternalPublication(result);
            }
        });
    }

    @Override
    public void saveCategoryScheme(CategorySchemeMetamacDto categoryScheme) {
        dispatcher.execute(new SaveCategorySchemeAction(categoryScheme), new WaitingAsyncCallbackHandlingError<SaveCategorySchemeResult>(this) {

            @Override
            public void onWaitSuccess(SaveCategorySchemeResult result) {
                categorySchemeMetamacDto = result.getCategorySchemeSaved();
                fireSuccessMessage(getMessages().categorySchemeSaved());
                getView().setCategoryScheme(categorySchemeMetamacDto);

                updateUrl();
                retrieveCategorySchemeVersions(result.getCategorySchemeSaved().getUrn());
            }
        });
    }

    @Override
    public void deleteCategoryScheme(String urn) {
        dispatcher.execute(new DeleteCategorySchemesAction(Arrays.asList(urn)), new WaitingAsyncCallbackHandlingError<DeleteCategorySchemesResult>(this) {

            @Override
            public void onWaitSuccess(DeleteCategorySchemesResult result) {
                fireSuccessMessage(getMessages().categorySchemeDeleted());
                goTo(PlaceRequestUtils.buildAbsoluteCategorySchemesPlaceRequest());
            }
        });
    }

    @Override
    public void exportCategorisations(List<String> urns, ExportDetailEnum detail, ExportReferencesEnum references) {
        dispatcher.execute(new ExportSDMXResourceAction(urns, RelatedResourceTypeEnum.CATEGORISATION, detail, references), new WaitingAsyncCallbackHandlingExportResult(this));
    }

    @Override
    public void cancelCategorisationValidity(List<String> urns, Date validTo) {
        dispatcher.execute(new CancelCategorisationValidityAction(urns, validTo), new WaitingAsyncCallbackHandlingError<CancelCategorisationValidityResult>(this) {

            @Override
            public void onWaitSuccess(CancelCategorisationValidityResult result) {
                fireSuccessMessage(getMessages().categorisationDeleted());
                retrieveCategorisations(categorySchemeMetamacDto.getUrn());
            }
        });
    }

    @Override
    public void exportCategoryScheme(String urn, ExportDetailEnum detail, ExportReferencesEnum references) {
        dispatcher.execute(new ExportSDMXResourceAction(Arrays.asList(urn), RelatedResourceTypeEnum.CATEGORY_SCHEME, detail, references), new WaitingAsyncCallbackHandlingExportResult(this));
    }

    @Override
    public void copyCategoryScheme(String urn) {
        copyCategoryScheme(urn, null);
    }

    @Override
    public void copyCategoryScheme(String urn, String code) {
        dispatcher.execute(new CopyCategorySchemeAction(urn, code), new WaitingAsyncCallbackHandlingError<CopyCategorySchemeResult>(this) {

            @Override
            public void onWaitSuccess(CopyCategorySchemeResult result) {
                fireSuccessMessage(getMessages().maintainableArtefactCopied());
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
        dispatcher.execute(new CancelCategorySchemeValidityAction(urns), new WaitingAsyncCallbackHandlingError<CancelCategorySchemeValidityResult>(this) {

            @Override
            public void onWaitSuccess(CancelCategorySchemeValidityResult result) {
                fireSuccessMessage(getMessages().categorySchemeCanceledValidity());
                retrieveCategorySchemeByUrn(urn);
            }
        });
    }

    @Override
    public void sendToProductionValidation(String urn, ProcStatusEnum currentProcStatus) {
        dispatcher.execute(new UpdateCategorySchemeProcStatusAction(urn, ProcStatusEnum.PRODUCTION_VALIDATION, currentProcStatus, null),
                new WaitingAsyncCallbackHandlingError<UpdateCategorySchemeProcStatusResult>(this) {

                    @Override
                    public void onWaitSuccess(UpdateCategorySchemeProcStatusResult result) {
                        fireSuccessMessage(getMessages().categorySchemeSentToProductionValidation());
                        categorySchemeMetamacDto = result.getCategorySchemeDto();
                        retrieveCategorySchemeVersions(categorySchemeMetamacDto.getUrn());
                        getView().setCategoryScheme(categorySchemeMetamacDto);
                    }
                });
    }

    @Override
    public void sendToDiffusionValidation(String urn, ProcStatusEnum currentProcStatus) {
        dispatcher.execute(new UpdateCategorySchemeProcStatusAction(urn, ProcStatusEnum.DIFFUSION_VALIDATION, currentProcStatus, null),
                new WaitingAsyncCallbackHandlingError<UpdateCategorySchemeProcStatusResult>(this) {

                    @Override
                    public void onWaitSuccess(UpdateCategorySchemeProcStatusResult result) {
                        fireSuccessMessage(getMessages().categorySchemeSentToDiffusionValidation());
                        categorySchemeMetamacDto = result.getCategorySchemeDto();
                        retrieveCategorySchemeVersions(categorySchemeMetamacDto.getUrn());
                        getView().setCategoryScheme(categorySchemeMetamacDto);
                    }
                });
    }

    @Override
    public void rejectValidation(String urn, ProcStatusEnum currentProcStatus) {
        dispatcher.execute(new UpdateCategorySchemeProcStatusAction(urn, ProcStatusEnum.VALIDATION_REJECTED, currentProcStatus, null),
                new WaitingAsyncCallbackHandlingError<UpdateCategorySchemeProcStatusResult>(this) {

                    @Override
                    public void onWaitSuccess(UpdateCategorySchemeProcStatusResult result) {
                        fireSuccessMessage(getMessages().categorySchemeRejected());
                        categorySchemeMetamacDto = result.getCategorySchemeDto();
                        retrieveCategorySchemeVersions(categorySchemeMetamacDto.getUrn());
                        getView().setCategoryScheme(categorySchemeMetamacDto);
                    }
                });
    }

    @Override
    public void publishInternally(final String urnToPublish, ProcStatusEnum currentProcStatus, Boolean forceLatestFinal) {
        dispatcher.execute(new UpdateCategorySchemeProcStatusAction(urnToPublish, ProcStatusEnum.INTERNALLY_PUBLISHED, currentProcStatus, forceLatestFinal),
                new WaitingAsyncCallbackHandlingError<UpdateCategorySchemeProcStatusResult>(this) {

                    @Override
                    public void onWaitSuccess(UpdateCategorySchemeProcStatusResult result) {
                        firePublicationMessage(getMessages().categorySchemePublishedInternally(), getMessages().categorySchemePublishedInternallyWithNotificationError(),
                                result.getNotificationException());
                        categorySchemeMetamacDto = result.getCategorySchemeDto();
                        retrieveCategorySchemeVersions(categorySchemeMetamacDto.getUrn());
                        getView().setCategoryScheme(categorySchemeMetamacDto);

                        // If the version published was a temporal version, reload the complete category scheme and the URL. Wwhen a temporal version is published, is automatically converted into a
                        // normal version (the URN changes!).
                        if (org.siemac.metamac.core.common.util.shared.UrnUtils.isTemporalUrn(urnToPublish)) {
                            retrieveCompleteCategorySchemeByUrn(categorySchemeMetamacDto.getUrn());
                            updateUrl();
                        }
                    }
                });
    }

    @Override
    public void publishExternally(String urn, ProcStatusEnum currentProcStatus) {
        dispatcher.execute(new UpdateCategorySchemeProcStatusAction(urn, ProcStatusEnum.EXTERNALLY_PUBLISHED, currentProcStatus, null),
                new WaitingAsyncCallbackHandlingError<UpdateCategorySchemeProcStatusResult>(this) {

                    @Override
                    public void onWaitSuccess(UpdateCategorySchemeProcStatusResult result) {
                        firePublicationMessage(getMessages().categorySchemePublishedExternally(), getMessages().categorySchemePublishedExternallyWithNotificationError(),
                                result.getNotificationException());
                        categorySchemeMetamacDto = result.getCategorySchemeDto();
                        retrieveCategorySchemeVersions(categorySchemeMetamacDto.getUrn());
                        getView().setCategoryScheme(categorySchemeMetamacDto);
                    }
                });
    }

    @Override
    public void versioning(String urn, VersionTypeEnum versionType) {
        dispatcher.execute(new VersionCategorySchemeAction(urn, versionType), new WaitingAsyncCallbackHandlingError<VersionCategorySchemeResult>(this) {

            @Override
            public void onWaitSuccess(VersionCategorySchemeResult result) {
                fireSuccessMessage(getMessages().categorySchemeVersioned());
                categorySchemeMetamacDto = result.getCategorySchemeMetamacDto();
                retrieveCompleteCategorySchemeByUrn(categorySchemeMetamacDto.getUrn());
                updateUrl();
            }
        });
    }

    @Override
    public void createTemporalVersion(String urn) {
        dispatcher.execute(new CreateCategorySchemeTemporalVersionAction(urn), new WaitingAsyncCallbackHandlingError<CreateCategorySchemeTemporalVersionResult>(this) {

            @Override
            public void onWaitSuccess(CreateCategorySchemeTemporalVersionResult result) {
                CategorySchemePresenter.this.categorySchemeMetamacDto = result.getCategorySchemeMetamacDto();
                retrieveCompleteCategorySchemeByUrn(result.getCategorySchemeMetamacDto().getUrn(), false);
                updateUrl();
            }
        });
    }

    private void firePublicationMessage(String successMessage, String warningMessage, MetamacWebException notificationException) {
        if (notificationException == null) {
            ShowMessageEvent.fireSuccessMessage(this, successMessage);
        } else {
            ShowMessageEvent.fireWarningMessageWithError(this, warningMessage, notificationException);
        }
    }

    //
    // CATEGORIES
    //

    private void retrieveCategoriesByScheme(String categorySchemeUrn) {
        dispatcher.execute(new GetCategoriesBySchemeAction(categorySchemeUrn, ApplicationEditionLanguages.getCurrentLocale()),
                new WaitingAsyncCallbackHandlingError<GetCategoriesBySchemeResult>(this) {

                    @Override
                    public void onWaitSuccess(GetCategoriesBySchemeResult result) {
                        getView().setCategories(result.getCategories());
                    }
                });
    }

    @Override
    public void saveCategory(CategoryMetamacDto categoryDto) {
        dispatcher.execute(new SaveCategoryAction(categoryDto), new WaitingAsyncCallbackHandlingError<SaveCategoryResult>(this) {

            @Override
            public void onWaitSuccess(SaveCategoryResult result) {
                fireSuccessMessage(getMessages().categorySaved());
                retrieveCategorySchemeByUrn(categorySchemeMetamacDto.getUrn());
            }
        });
    }

    @Override
    public void deleteCategory(ItemVisualisationResult itemVisualisationResult) {
        dispatcher.execute(new DeleteCategoryAction(itemVisualisationResult.getUrn()), new WaitingAsyncCallbackHandlingError<DeleteCategoryResult>(this) {

            @Override
            public void onWaitSuccess(DeleteCategoryResult result) {
                fireSuccessMessage(getMessages().categoryDeleted());
                retrieveCategoriesByScheme(categorySchemeMetamacDto.getUrn());
            }
        });
    }

    @Override
    public void exportCategories(String categorySchemeUrn) {
        dispatcher.execute(new ExportCategoriesAction(categorySchemeUrn), new WaitingAsyncCallbackHandlingError<ExportCategoriesResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(CategorySchemePresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(ExportCategoriesResult result) {
                CommonUtils.downloadFile(result.getFileName());
            }
        });
    }

    @Override
    public void resourceImportationSucceed(String successMessage) {
        ShowMessageEvent.fireSuccessMessage(CategorySchemePresenter.this, successMessage);
        retrieveCategorySchemeByUrn(categorySchemeMetamacDto.getUrn());
        ChangeWaitPopupVisibilityEvent.fire(this, false);
    }

    @Override
    public void resourceImportationFailed(String errorMessage) {
        ShowMessageEvent.fireErrorMessage(CategorySchemePresenter.this, errorMessage);
        ChangeWaitPopupVisibilityEvent.fire(this, false);
    }

    @Override
    public void showWaitPopup() {
        ChangeWaitPopupVisibilityEvent.fire(this, true);
    }

    // CATEGORISATIONS

    @Override
    public void retrieveCategorisations(String artefactCategorisedUrn) {
        dispatcher.execute(new GetCategorisationsByArtefactAction(artefactCategorisedUrn), new WaitingAsyncCallbackHandlingError<GetCategorisationsByArtefactResult>(this) {

            @Override
            public void onWaitSuccess(GetCategorisationsByArtefactResult result) {
                getView().setCategorisations(result.getCategorisationDtos());
            }
        });
    }

    @Override
    public void createCategorisations(List<String> categoryUrns) {
        dispatcher.execute(new CreateCategorisationAction(categoryUrns, categorySchemeMetamacDto.getUrn(), RelatedResourceUtils.getDefaultMaintainerAsRelatedResourceDto().getUrn()),
                new WaitingAsyncCallbackHandlingError<CreateCategorisationResult>(this) {

                    @Override
                    public void onWaitSuccess(CreateCategorisationResult result) {
                        fireSuccessMessage(getMessages().categorisationCreated());
                        retrieveCategorisations(categorySchemeMetamacDto.getUrn());
                    }
                });
    }

    @Override
    public void deleteCategorisations(List<String> urns) {
        dispatcher.execute(new DeleteCategorisationsAction(urns), new WaitingAsyncCallbackHandlingError<DeleteCategorisationsResult>(this) {

            @Override
            public void onWaitSuccess(DeleteCategorisationsResult result) {
                fireSuccessMessage(getMessages().categorisationDeleted());
                retrieveCategorisations(categorySchemeMetamacDto.getUrn());
            }
        });
    }

    @Override
    public void retrieveCategorySchemesForCategorisations(int firstResult, int maxResults, CategorySchemeWebCriteria categorySchemeWebCriteria) {
        dispatcher.execute(new GetRelatedResourcesAction(StructuralResourcesRelationEnum.CATEGORY_SCHEMES_FOR_CATEGORISATIONS, firstResult, maxResults, categorySchemeWebCriteria),
                new WaitingAsyncCallbackHandlingError<GetRelatedResourcesResult>(this) {

                    @Override
                    public void onWaitSuccess(GetRelatedResourcesResult result) {
                        getView().setCategorySchemesForCategorisations(result);
                    }
                });
    }

    @Override
    public void retrieveCategoriesForCategorisations(int firstResult, int maxResults, CategoryWebCriteria categoryWebCriteria) {
        dispatcher.execute(new GetRelatedResourcesAction(StructuralResourcesRelationEnum.CATEGORIES_FOR_CATEGORISATIONS, firstResult, maxResults, categoryWebCriteria),
                new WaitingAsyncCallbackHandlingError<GetRelatedResourcesResult>(this) {

                    @Override
                    public void onWaitSuccess(GetRelatedResourcesResult result) {
                        getView().setCategoriesForCategorisations(result);
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
        goTo(PlaceRequestUtils.buildAbsoluteCategorySchemePlaceRequest(urn));
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
