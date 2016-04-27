package org.siemac.metamac.srm.web.concept.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.srm.core.concept.domain.shared.ConceptMetamacVisualisationResult;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.navigation.shared.NameTokens;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.enums.ExportDetailEnum;
import org.siemac.metamac.srm.web.client.enums.ExportReferencesEnum;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.client.utils.WaitingAsyncCallbackHandlingExportResult;
import org.siemac.metamac.srm.web.concept.enums.ConceptsToolStripButtonEnum;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptSchemeUiHandlers;
import org.siemac.metamac.srm.web.concept.widgets.presenter.ConceptsToolStripPresenterWidget;
import org.siemac.metamac.srm.web.shared.ExportSDMXResourceAction;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesAction;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.srm.web.shared.StructuralResourcesRelationEnum;
import org.siemac.metamac.srm.web.shared.category.CancelCategorisationValidityAction;
import org.siemac.metamac.srm.web.shared.category.CancelCategorisationValidityResult;
import org.siemac.metamac.srm.web.shared.category.CreateCategorisationAction;
import org.siemac.metamac.srm.web.shared.category.CreateCategorisationResult;
import org.siemac.metamac.srm.web.shared.category.DeleteCategorisationsAction;
import org.siemac.metamac.srm.web.shared.category.DeleteCategorisationsResult;
import org.siemac.metamac.srm.web.shared.category.GetCategorisationsByArtefactAction;
import org.siemac.metamac.srm.web.shared.category.GetCategorisationsByArtefactResult;
import org.siemac.metamac.srm.web.shared.concept.CancelConceptSchemeValidityAction;
import org.siemac.metamac.srm.web.shared.concept.CancelConceptSchemeValidityResult;
import org.siemac.metamac.srm.web.shared.concept.CopyConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.concept.CopyConceptSchemeResult;
import org.siemac.metamac.srm.web.shared.concept.CreateConceptSchemeTemporalVersionAction;
import org.siemac.metamac.srm.web.shared.concept.CreateConceptSchemeTemporalVersionResult;
import org.siemac.metamac.srm.web.shared.concept.DeleteConceptAction;
import org.siemac.metamac.srm.web.shared.concept.DeleteConceptResult;
import org.siemac.metamac.srm.web.shared.concept.DeleteConceptSchemesAction;
import org.siemac.metamac.srm.web.shared.concept.DeleteConceptSchemesResult;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemeResult;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemeVersionsAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemeVersionsResult;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemesAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemesResult;
import org.siemac.metamac.srm.web.shared.concept.GetConceptsBySchemeAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptsBySchemeResult;
import org.siemac.metamac.srm.web.shared.concept.GetStatisticalOperationsAction;
import org.siemac.metamac.srm.web.shared.concept.GetStatisticalOperationsResult;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptAction;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptResult;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptSchemeResult;
import org.siemac.metamac.srm.web.shared.concept.UpdateConceptSchemeProcStatusAction;
import org.siemac.metamac.srm.web.shared.concept.UpdateConceptSchemeProcStatusResult;
import org.siemac.metamac.srm.web.shared.concept.VersionConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.concept.VersionConceptSchemeResult;
import org.siemac.metamac.srm.web.shared.criteria.CategorySchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.CategoryWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptSchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.StatisticalOperationWebCriteria;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
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

public class ConceptSchemePresenter extends Presenter<ConceptSchemePresenter.ConceptSchemeView, ConceptSchemePresenter.ConceptSchemeProxy> implements ConceptSchemeUiHandlers {

    private final DispatchAsync              dispatcher;
    private final PlaceManager               placeManager;

    private ConceptSchemeMetamacDto          conceptSchemeDto;

    private ConceptsToolStripPresenterWidget conceptsToolStripPresenterWidget;

    @TitleFunction
    public static String getTranslatedTitle(PlaceRequest placeRequest) {
        return PlaceRequestUtils.getConceptSchemeBreadCrumbTitle(placeRequest);
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.conceptSchemePage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface ConceptSchemeProxy extends Proxy<ConceptSchemePresenter>, Place {
    }

    public interface ConceptSchemeView extends View, HasUiHandlers<ConceptSchemeUiHandlers> {

        void setConceptScheme(ConceptSchemeMetamacDto conceptSchemeDto);
        void startConceptSchemeEdition();
        void setConcepts(List<ConceptMetamacVisualisationResult> itemHierarchyDtos);
        void setConceptSchemeVersions(List<ConceptSchemeMetamacBasicDto> conceptSchemeDtos);
        void setOperations(GetStatisticalOperationsResult result);
        void setLatestConceptSchemeForInternalPublication(GetConceptSchemesResult result);

        void selectConceptSchemeTab();

        // Categorisations

        void setCategorisations(List<CategorisationDto> categorisationDtos);
        void setCategorySchemesForCategorisations(GetRelatedResourcesResult result);
        void setCategoriesForCategorisations(GetRelatedResourcesResult result);
    }

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaContentConceptScheme   = new Type<RevealContentHandler<?>>();

    public static final Object                        TYPE_SetContextAreaContentConceptsToolBar = new Object();

    @Inject
    public ConceptSchemePresenter(EventBus eventBus, ConceptSchemeView view, ConceptSchemeProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager,
            ConceptsToolStripPresenterWidget conceptsToolStripPresenterWidget) {
        super(eventBus, view, proxy);
        this.placeManager = placeManager;
        this.dispatcher = dispatcher;
        this.conceptsToolStripPresenterWidget = conceptsToolStripPresenterWidget;
        getView().setUiHandlers(this);
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        setInSlot(TYPE_SetContextAreaContentConceptsToolBar, conceptsToolStripPresenterWidget);

        SetTitleEvent.fire(this, MetamacSrmWeb.getConstants().conceptScheme());
        SelectMenuButtonEvent.fire(this, ToolStripButtonEnum.CONCEPTS);
        conceptsToolStripPresenterWidget.selectConceptsMenuButton(ConceptsToolStripButtonEnum.CONCEPT_SCHEMES);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        String identifier = PlaceRequestUtils.getConceptSchemeParamFromUrl(placeManager);
        if (identifier != null) {
            retrieveCompleteConceptSchemeByIdentifier(identifier);
        }
        getView().selectConceptSchemeTab();
    }

    private void retrieveCompleteConceptSchemeByIdentifier(String identifier) {
        String conceptSchemeUrn = UrnUtils.generateUrn(UrnConstants.URN_SDMX_CLASS_CONCEPTSCHEME_PREFIX, identifier);
        retrieveCompleteConceptSchemeByUrn(conceptSchemeUrn);
    }

    private void retrieveCompleteConceptSchemeByUrn(String urn) {
        retrieveCompleteConceptSchemeByUrn(urn, false);
    }

    private void retrieveCompleteConceptSchemeByUrn(String urn, boolean startEdition) {
        retrieveConceptSchemeByUrn(urn, startEdition);
        retrieveCategorisations(urn);
    }

    private void retrieveConceptSchemeByUrn(String urn) {
        retrieveConceptSchemeByUrn(urn, false);
    }

    //
    // CONCEPT SCHEME
    //

    private void retrieveConceptSchemeByUrn(String urn, final boolean startEdition) {
        dispatcher.execute(new GetConceptSchemeAction(urn), new WaitingAsyncCallbackHandlingError<GetConceptSchemeResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(ConceptSchemePresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetConceptSchemeResult result) {
                ConceptSchemePresenter.this.conceptSchemeDto = result.getConceptSchemeDto();

                getView().setConceptScheme(result.getConceptSchemeDto());
                if (startEdition) {
                    getView().startConceptSchemeEdition();
                }

                retrieveConceptsByScheme(result.getConceptSchemeDto().getUrn());
                retrieveConceptSchemeVersions(result.getConceptSchemeDto().getUrn());
            }
        });
    }

    @Override
    public void retrieveLatestConceptScheme(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        ConceptSchemeWebCriteria criteria = new ConceptSchemeWebCriteria();
        criteria.setCodeEQ(conceptSchemeMetamacDto.getCode());
        criteria.setMaintainerUrn(conceptSchemeMetamacDto.getMaintainer().getUrn());
        criteria.setIsLatestFinal(true);
        dispatcher.execute(new GetConceptSchemesAction(0, 1, criteria), new WaitingAsyncCallbackHandlingError<GetConceptSchemesResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(ConceptSchemePresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetConceptSchemesResult result) {
                getView().setLatestConceptSchemeForInternalPublication(result);
            }
        });
    }

    @Override
    public void saveConceptScheme(ConceptSchemeMetamacDto conceptScheme) {
        dispatcher.execute(new SaveConceptSchemeAction(conceptScheme), new WaitingAsyncCallbackHandlingError<SaveConceptSchemeResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(ConceptSchemePresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(SaveConceptSchemeResult result) {
                fireSuccessMessage(getMessages().conceptSchemeSaved());
                ConceptSchemePresenter.this.conceptSchemeDto = result.getSavedConceptSchemeDto();
                getView().setConceptScheme(result.getSavedConceptSchemeDto());

                updateUrl();
                retrieveConceptSchemeVersions(result.getSavedConceptSchemeDto().getUrn());
            }
        });
    }

    @Override
    public void deleteConceptScheme(String urn) {
        dispatcher.execute(new DeleteConceptSchemesAction(Arrays.asList(urn)), new WaitingAsyncCallbackHandlingError<DeleteConceptSchemesResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(ConceptSchemePresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(DeleteConceptSchemesResult result) {
                fireSuccessMessage(getMessages().conceptSchemeDeleted());
                goTo(PlaceRequestUtils.buildAbsoluteConceptSchemesPlaceRequest());
            }
        });
    }

    @Override
    public void retrieveConceptSchemeVersions(String conceptSchemeUrn) {
        dispatcher.execute(new GetConceptSchemeVersionsAction(conceptSchemeUrn), new WaitingAsyncCallbackHandlingError<GetConceptSchemeVersionsResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(ConceptSchemePresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetConceptSchemeVersionsResult result) {
                getView().setConceptSchemeVersions(result.getConceptSchemeDtos());
            }
        });
    }

    @Override
    public void exportConceptScheme(String urn, ExportDetailEnum detail, ExportReferencesEnum references) {
        dispatcher.execute(new ExportSDMXResourceAction(Arrays.asList(urn), RelatedResourceTypeEnum.CONCEPT_SCHEME, detail, references), new WaitingAsyncCallbackHandlingExportResult(this));
    }

    @Override
    public void copyConceptScheme(String urn) {
        copyConceptScheme(urn, null);
    }

    @Override
    public void copyConceptScheme(String urn, String code) {
        dispatcher.execute(new CopyConceptSchemeAction(urn, code), new WaitingAsyncCallbackHandlingError<CopyConceptSchemeResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(ConceptSchemePresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(CopyConceptSchemeResult result) {
                fireSuccessMessage(getMessages().maintainableArtefactCopied());
            }
        });
    }

    //
    // CONCEPT SCHEME LIFECYCLE
    //

    @Override
    public void sendToProductionValidation(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        dispatcher.execute(new UpdateConceptSchemeProcStatusAction(conceptSchemeMetamacDto, ProcStatusEnum.PRODUCTION_VALIDATION, null),
                new WaitingAsyncCallbackHandlingError<UpdateConceptSchemeProcStatusResult>(this) {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fireErrorMessage(ConceptSchemePresenter.this, caught);
                    }
                    @Override
                    public void onWaitSuccess(UpdateConceptSchemeProcStatusResult result) {
                        fireSuccessMessage(getMessages().conceptSchemeSentToProductionValidation());
                        conceptSchemeDto = result.getConceptSchemeDto();
                        retrieveConceptSchemeVersions(conceptSchemeDto.getUrn());
                        getView().setConceptScheme(result.getConceptSchemeDto());
                    }
                });
    }

    @Override
    public void sendToDiffusionValidation(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        dispatcher.execute(new UpdateConceptSchemeProcStatusAction(conceptSchemeMetamacDto, ProcStatusEnum.DIFFUSION_VALIDATION, null),
                new WaitingAsyncCallbackHandlingError<UpdateConceptSchemeProcStatusResult>(this) {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fireErrorMessage(ConceptSchemePresenter.this, caught);
                    }
                    @Override
                    public void onWaitSuccess(UpdateConceptSchemeProcStatusResult result) {
                        fireSuccessMessage(getMessages().conceptSchemeSentToDiffusionValidation());
                        ConceptSchemePresenter.this.conceptSchemeDto = result.getConceptSchemeDto();
                        retrieveConceptSchemeVersions(conceptSchemeDto.getUrn());
                        getView().setConceptScheme(result.getConceptSchemeDto());
                    }
                });
    }

    @Override
    public void rejectValidation(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        dispatcher.execute(new UpdateConceptSchemeProcStatusAction(conceptSchemeMetamacDto, ProcStatusEnum.VALIDATION_REJECTED, null),
                new WaitingAsyncCallbackHandlingError<UpdateConceptSchemeProcStatusResult>(this) {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fireErrorMessage(ConceptSchemePresenter.this, caught);
                    }
                    @Override
                    public void onWaitSuccess(UpdateConceptSchemeProcStatusResult result) {
                        fireSuccessMessage(getMessages().conceptSchemeRejected());
                        ConceptSchemePresenter.this.conceptSchemeDto = result.getConceptSchemeDto();
                        retrieveConceptSchemeVersions(conceptSchemeDto.getUrn());
                        getView().setConceptScheme(result.getConceptSchemeDto());
                    }
                });
    }

    @Override
    public void publishInternally(final ConceptSchemeMetamacDto conceptSchemeToPublish, Boolean forceLatestFinal) {
        dispatcher.execute(new UpdateConceptSchemeProcStatusAction(conceptSchemeToPublish, ProcStatusEnum.INTERNALLY_PUBLISHED, forceLatestFinal),
                new WaitingAsyncCallbackHandlingError<UpdateConceptSchemeProcStatusResult>(this) {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fireErrorMessage(ConceptSchemePresenter.this, caught);
                    }
                    @Override
                    public void onWaitSuccess(UpdateConceptSchemeProcStatusResult result) {

                        firePublicationMessage(getMessages().conceptSchemePublishedInternally(), getMessages().conceptSchemePublishedInternallyWithNotificationError(),
                                result.getNotificationException());

                        ConceptSchemePresenter.this.conceptSchemeDto = result.getConceptSchemeDto();
                        retrieveConceptSchemeVersions(conceptSchemeDto.getUrn());
                        getView().setConceptScheme(result.getConceptSchemeDto());

                        // If the version published was a temporal version, reload the complete concept scheme and the URL. When a temporal version is published, is automatically converted into a
                        // normal version (the URN changes!).
                        if (org.siemac.metamac.core.common.util.shared.UrnUtils.isTemporalUrn(conceptSchemeToPublish.getUrn())) {
                            retrieveCompleteConceptSchemeByUrn(conceptSchemeDto.getUrn());
                            updateUrl();
                        }
                    }
                });
    }

    @Override
    public void publishExternally(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        dispatcher.execute(new UpdateConceptSchemeProcStatusAction(conceptSchemeMetamacDto, ProcStatusEnum.EXTERNALLY_PUBLISHED, null),
                new WaitingAsyncCallbackHandlingError<UpdateConceptSchemeProcStatusResult>(this) {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fireErrorMessage(ConceptSchemePresenter.this, caught);
                    }
                    @Override
                    public void onWaitSuccess(UpdateConceptSchemeProcStatusResult result) {

                        firePublicationMessage(getMessages().conceptSchemePublishedExternally(), getMessages().conceptSchemePublishedExternallyWithNotificationError(),
                                result.getNotificationException());

                        ConceptSchemePresenter.this.conceptSchemeDto = result.getConceptSchemeDto();
                        retrieveConceptSchemeVersions(conceptSchemeDto.getUrn());
                        getView().setConceptScheme(result.getConceptSchemeDto());
                    }
                });
    }

    @Override
    public void versioning(final String urn, VersionTypeEnum versionType) {
        dispatcher.execute(new VersionConceptSchemeAction(urn, versionType), new WaitingAsyncCallbackHandlingError<VersionConceptSchemeResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(ConceptSchemePresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(VersionConceptSchemeResult result) {
                fireSuccessMessage(getMessages().conceptSchemeVersioned());
                ConceptSchemePresenter.this.conceptSchemeDto = result.getConceptSchemeDto();
                retrieveCompleteConceptSchemeByUrn(conceptSchemeDto.getUrn());
                updateUrl();
            }
        });
    }

    @Override
    public void cancelValidity(final String urn) {
        List<String> urns = new ArrayList<String>();
        urns.add(urn);
        dispatcher.execute(new CancelConceptSchemeValidityAction(urns), new WaitingAsyncCallbackHandlingError<CancelConceptSchemeValidityResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(ConceptSchemePresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(CancelConceptSchemeValidityResult result) {
                fireSuccessMessage(getMessages().conceptSchemeCanceledValidity());
                retrieveConceptSchemeByUrn(urn);
            }
        });
    }

    @Override
    public void createTemporalVersion(String urn) {
        dispatcher.execute(new CreateConceptSchemeTemporalVersionAction(urn), new WaitingAsyncCallbackHandlingError<CreateConceptSchemeTemporalVersionResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(ConceptSchemePresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(CreateConceptSchemeTemporalVersionResult result) {
                ConceptSchemePresenter.this.conceptSchemeDto = result.getConceptSchemeMetamacDto();
                retrieveCompleteConceptSchemeByUrn(result.getConceptSchemeMetamacDto().getUrn(), false);
                updateUrl();
            }
        });
    }

    // CONCEPTS

    @Override
    public void saveConcept(ConceptMetamacDto conceptDto) {
        // Create concept
        saveConcept(conceptDto, null, null);
    }

    @Override
    public void saveConcept(ConceptMetamacDto conceptDto, List<String> roles, List<String> relatedConcepts) {
        dispatcher.execute(new SaveConceptAction(conceptDto, roles, relatedConcepts), new WaitingAsyncCallbackHandlingError<SaveConceptResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(ConceptSchemePresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(SaveConceptResult result) {
                fireSuccessMessage(getMessages().conceptCreated());
                retrieveConceptSchemeByUrn(conceptSchemeDto.getUrn());
            }
        });
    }

    private void retrieveConceptsByScheme(String conceptSchemeUrn) {
        dispatcher.execute(new GetConceptsBySchemeAction(conceptSchemeUrn, ApplicationEditionLanguages.getCurrentLocale()), new WaitingAsyncCallbackHandlingError<GetConceptsBySchemeResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(ConceptSchemePresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetConceptsBySchemeResult result) {
                getView().setConcepts(result.getItemVisualisationResults());
            }
        });
    }

    @Override
    public void deleteConcept(ItemVisualisationResult itemVisualisationResult) {
        dispatcher.execute(new DeleteConceptAction(itemVisualisationResult.getUrn()), new WaitingAsyncCallbackHandlingError<DeleteConceptResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(ConceptSchemePresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(DeleteConceptResult result) {
                retrieveConceptsByScheme(conceptSchemeDto.getUrn());
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
    // RELATED RESOURCES
    //

    @Override
    public void retrieveStatisticalOperations(int firstResult, int maxResults, String criteria) {
        StatisticalOperationWebCriteria statisticalOperationWebCriteria = new StatisticalOperationWebCriteria(criteria);
        dispatcher.execute(new GetStatisticalOperationsAction(firstResult, maxResults, statisticalOperationWebCriteria), new WaitingAsyncCallbackHandlingError<GetStatisticalOperationsResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(ConceptSchemePresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetStatisticalOperationsResult result) {
                getView().setOperations(result);
            }
        });
    }

    //
    // CATEGORISATIONS
    //

    @Override
    public void retrieveCategorisations(String artefactCategorisedUrn) {
        dispatcher.execute(new GetCategorisationsByArtefactAction(artefactCategorisedUrn), new WaitingAsyncCallbackHandlingError<GetCategorisationsByArtefactResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(ConceptSchemePresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetCategorisationsByArtefactResult result) {
                getView().setCategorisations(result.getCategorisationDtos());
            }
        });
    }

    @Override
    public void createCategorisations(List<String> categoryUrns) {
        dispatcher.execute(new CreateCategorisationAction(categoryUrns, conceptSchemeDto.getUrn(), RelatedResourceUtils.getDefaultMaintainerAsRelatedResourceDto().getUrn()),
                new WaitingAsyncCallbackHandlingError<CreateCategorisationResult>(this) {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fireErrorMessage(ConceptSchemePresenter.this, caught);
                    }
                    @Override
                    public void onWaitSuccess(CreateCategorisationResult result) {
                        fireSuccessMessage(getMessages().categorisationCreated());
                        retrieveCategorisations(conceptSchemeDto.getUrn());
                    }
                });
    }

    @Override
    public void deleteCategorisations(List<String> urns) {
        dispatcher.execute(new DeleteCategorisationsAction(urns), new WaitingAsyncCallbackHandlingError<DeleteCategorisationsResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(ConceptSchemePresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(DeleteCategorisationsResult result) {
                fireSuccessMessage(getMessages().categorisationDeleted());
                retrieveCategorisations(conceptSchemeDto.getUrn());
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
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(ConceptSchemePresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(CancelCategorisationValidityResult result) {
                fireSuccessMessage(getMessages().categorisationDeleted());
                retrieveCategorisations(conceptSchemeDto.getUrn());
            }
        });
    }

    @Override
    public void retrieveCategorySchemesForCategorisations(int firstResult, int maxResults, CategorySchemeWebCriteria categorySchemeWebCriteria) {
        dispatcher.execute(new GetRelatedResourcesAction(StructuralResourcesRelationEnum.CATEGORY_SCHEMES_FOR_CATEGORISATIONS, firstResult, maxResults, categorySchemeWebCriteria),
                new WaitingAsyncCallbackHandlingError<GetRelatedResourcesResult>(this) {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fireErrorMessage(ConceptSchemePresenter.this, caught);
                    }
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
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fireErrorMessage(ConceptSchemePresenter.this, caught);
                    }
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
    public void goToConcept(String urn) {
        placeManager.revealRelativePlace(PlaceRequestUtils.buildRelativeConceptPlaceRequest(urn));
    }

    @Override
    public void goToConceptScheme(String urn) {
        goTo(PlaceRequestUtils.buildAbsoluteConceptSchemePlaceRequest(urn));
    }

    private void updateUrl() {
        PlaceRequest placeRequest = PlaceRequestUtils.buildRelativeConceptSchemePlaceRequest(conceptSchemeDto.getUrn());
        placeManager.updateHistory(placeRequest, true);
    }
}
