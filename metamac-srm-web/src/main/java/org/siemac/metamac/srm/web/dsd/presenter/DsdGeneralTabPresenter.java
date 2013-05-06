package org.siemac.metamac.srm.web.dsd.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.events.UpdateMaintainableArtefactVersionsEvent;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.dsd.utils.CommonUtils;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdGeneralTabUiHandlers;
import org.siemac.metamac.srm.web.shared.ExportSDMXResourceAction;
import org.siemac.metamac.srm.web.shared.ExportSDMXResourceResult;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemeResult;
import org.siemac.metamac.srm.web.shared.concept.GetConceptsBySchemeAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptsBySchemeResult;
import org.siemac.metamac.srm.web.shared.concept.GetStatisticalOperationsAction;
import org.siemac.metamac.srm.web.shared.concept.GetStatisticalOperationsResult;
import org.siemac.metamac.srm.web.shared.criteria.DataStructureDefinitionWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.StatisticalOperationWebCriteria;
import org.siemac.metamac.srm.web.shared.dsd.CancelDsdValidityAction;
import org.siemac.metamac.srm.web.shared.dsd.CancelDsdValidityResult;
import org.siemac.metamac.srm.web.shared.dsd.CreateDsdTemporalVersionAction;
import org.siemac.metamac.srm.web.shared.dsd.CreateDsdTemporalVersionResult;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdAndDescriptorsAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdAndDescriptorsResult;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdsAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdsResult;
import org.siemac.metamac.srm.web.shared.dsd.SaveDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.SaveDsdResult;
import org.siemac.metamac.srm.web.shared.dsd.UpdateDsdProcStatusAction;
import org.siemac.metamac.srm.web.shared.dsd.UpdateDsdProcStatusResult;
import org.siemac.metamac.srm.web.shared.dsd.VersionDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.VersionDsdResult;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.ApplicationEditionLanguages;
import org.siemac.metamac.web.common.client.utils.UrnUtils;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DimensionComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeComponentList;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TitleFunction;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.smartgwt.client.widgets.events.HasClickHandlers;

public class DsdGeneralTabPresenter extends Presenter<DsdGeneralTabPresenter.DsdGeneralTabView, DsdGeneralTabPresenter.DsdGeneralTabProxy> implements DsdGeneralTabUiHandlers {

    private final DispatchAsync dispatcher;
    private final PlaceManager  placeManager;

    private String              conceptSchemeUrnOfMeasureDimensionRepresentation; // URN of the concept scheme that is associated to the measure dimension representation

    @ProxyCodeSplit
    @NameToken(NameTokens.dsdGeneralPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface DsdGeneralTabProxy extends Proxy<DsdGeneralTabPresenter>, Place {

    }

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbGeneral();
    }

    public interface DsdGeneralTabView extends View, HasUiHandlers<DsdGeneralTabUiHandlers> {

        void setDsd(DataStructureDefinitionMetamacDto dataStructureDefinitionDto, List<DimensionComponentDto> dimensionComponentDtos);
        void startDsdEdition();
        DataStructureDefinitionMetamacDto getDataStructureDefinitionDto();
        HasClickHandlers getSave();
        void setLatestDsdForInternalPublication(GetDsdsResult result);

        void setConceptsForShowDecimalsPrecision(ConceptSchemeMetamacDto conceptSchemeMetamacDto, List<ItemVisualisationResult> itemVisualisationResults);

        void setOperations(GetStatisticalOperationsResult result);
    }

    @Inject
    public DsdGeneralTabPresenter(EventBus eventBus, DsdGeneralTabView dsdGeneralTabView, DsdGeneralTabProxy dsdGeneralTabProxy, DispatchAsync dispatcher, PlaceManager placeManager) {
        super(eventBus, dsdGeneralTabView, dsdGeneralTabProxy);
        this.dispatcher = dispatcher;
        this.placeManager = placeManager;
        getView().setUiHandlers(this);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, DsdPresenter.TYPE_SetContextAreaContentDsd, this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        String dsdIdentifier = PlaceRequestUtils.getDsdParamFromUrl(placeManager);// DSD identifier is the URN without the prefix
        if (!StringUtils.isBlank(dsdIdentifier)) {
            String dsdUrn = UrnUtils.generateUrn(UrnConstants.URN_SDMX_CLASS_DATASTRUCTURE_PREFIX, dsdIdentifier);
            retrieveCompleteDsd(dsdUrn);
        }
    }

    @Override
    protected void onReset() {
        super.onReset();
    }

    //
    // DSD
    //

    @Override
    public void saveDsd(DataStructureDefinitionMetamacDto dataStructureDefinitionDto) {
        dispatcher.execute(new SaveDsdAction(dataStructureDefinitionDto), new WaitingAsyncCallback<SaveDsdResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorSave()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(SaveDsdResult result) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getMessageList(MetamacSrmWeb.getMessages().dsdSaved()), MessageTypeEnum.SUCCESS);

                // Redirect to the DSD page to update the URL
                goToDsd(result.getDsdSaved().getUrn()); // To update the URL (the method placeManager.updateHistory only allow to update the last placeRequest)
            }
        });
    }

    private void retrieveCompleteDsd(String urn) {
        retrieveCompleteDsd(urn, false);
    }

    private void retrieveCompleteDsd(String urn, final boolean startDsdEdition) {
        Set<TypeComponentList> descriptorsToRetrieve = new HashSet<TypeComponentList>();
        descriptorsToRetrieve.add(TypeComponentList.DIMENSION_DESCRIPTOR);
        dispatcher.execute(new GetDsdAndDescriptorsAction(urn, descriptorsToRetrieve), new WaitingAsyncCallback<GetDsdAndDescriptorsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetDsdAndDescriptorsResult result) {

                List<DimensionComponentDto> dimensionComponentDtos = CommonUtils.getDimensionComponents(result.getDimensions());

                getView().setDsd(result.getDsd(), dimensionComponentDtos);
                if (startDsdEdition) {
                    getView().startDsdEdition();
                }

                setConceptSchemeOfTheMeasureDimension(dimensionComponentDtos);
            }
        });
    }

    @Override
    public void retrieveLatestDsd(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        DataStructureDefinitionWebCriteria criteria = new DataStructureDefinitionWebCriteria();
        criteria.setCodeEQ(dataStructureDefinitionMetamacDto.getCode());
        criteria.setMaintainerUrn(dataStructureDefinitionMetamacDto.getMaintainer().getUrn());
        criteria.setIsLatestFinal(true);
        dispatcher.execute(new GetDsdsAction(0, 1, criteria), new WaitingAsyncCallback<GetDsdsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().dsdErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetDsdsResult result) {
                getView().setLatestDsdForInternalPublication(result);
            }
        });
    }

    @Override
    public void exportDsd(String urn) {
        dispatcher.execute(new ExportSDMXResourceAction(urn), new WaitingAsyncCallback<ExportSDMXResourceResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().resourceErrorExport()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(ExportSDMXResourceResult result) {
                org.siemac.metamac.srm.web.client.utils.CommonUtils.downloadFile(result.getFileName());
            }
        });
    }

    //
    // DSD LIFECYCLE
    //

    @Override
    public void sendToProductionValidation(final DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        dispatcher.execute(new UpdateDsdProcStatusAction(dataStructureDefinitionMetamacDto, ProcStatusEnum.PRODUCTION_VALIDATION, null), new WaitingAsyncCallback<UpdateDsdProcStatusResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorSendingToProductionValidation()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(UpdateDsdProcStatusResult result) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getMessageList(MetamacSrmWeb.getMessages().dsdSentToProductionValidation()), MessageTypeEnum.SUCCESS);
                retrieveCompleteDsd(dataStructureDefinitionMetamacDto.getUrn());
            }
        });
    }

    @Override
    public void sendToDiffusionValidation(final DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        dispatcher.execute(new UpdateDsdProcStatusAction(dataStructureDefinitionMetamacDto, ProcStatusEnum.DIFFUSION_VALIDATION, null), new WaitingAsyncCallback<UpdateDsdProcStatusResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorSendingToDiffusionValidation()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(UpdateDsdProcStatusResult result) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getMessageList(MetamacSrmWeb.getMessages().dsdSentToDiffusionValidation()), MessageTypeEnum.SUCCESS);
                retrieveCompleteDsd(dataStructureDefinitionMetamacDto.getUrn());
            }
        });
    }

    @Override
    public void rejectValidation(final DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        dispatcher.execute(new UpdateDsdProcStatusAction(dataStructureDefinitionMetamacDto, ProcStatusEnum.VALIDATION_REJECTED, null), new WaitingAsyncCallback<UpdateDsdProcStatusResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorRejecting()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(UpdateDsdProcStatusResult result) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getMessageList(MetamacSrmWeb.getMessages().dsdRejected()), MessageTypeEnum.SUCCESS);
                retrieveCompleteDsd(dataStructureDefinitionMetamacDto.getUrn());
            }
        });
    }

    @Override
    public void publishInternally(final DataStructureDefinitionMetamacDto dsdToPublish, Boolean forceLatestFinal) {
        dispatcher.execute(new UpdateDsdProcStatusAction(dsdToPublish, ProcStatusEnum.INTERNALLY_PUBLISHED, forceLatestFinal), new WaitingAsyncCallback<UpdateDsdProcStatusResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorPublishingInternally()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(UpdateDsdProcStatusResult result) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getMessageList(MetamacSrmWeb.getMessages().dsdPublishedInternally()), MessageTypeEnum.SUCCESS);
                retrieveCompleteDsd(result.getDataStructureDefinitionMetamacDto().getUrn());

                // If the version published was a temporal version, reload the version list and the URL. Wwhen a temporal version is published, is automatically converted into a normal version (the
                // URN changes!).
                goToDsd(result.getDataStructureDefinitionMetamacDto().getUrn()); // To update the URL (the method placeManager.updateHistory only allow to update the last placeRequest)
            }
        });
    }

    @Override
    public void publishExternally(final DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        dispatcher.execute(new UpdateDsdProcStatusAction(dataStructureDefinitionMetamacDto, ProcStatusEnum.EXTERNALLY_PUBLISHED, null), new WaitingAsyncCallback<UpdateDsdProcStatusResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorPublishingExternally()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(UpdateDsdProcStatusResult result) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getMessageList(MetamacSrmWeb.getMessages().dsdPublishedExternally()), MessageTypeEnum.SUCCESS);
                retrieveCompleteDsd(dataStructureDefinitionMetamacDto.getUrn());
            }
        });
    }

    @Override
    public void versioning(String urn, VersionTypeEnum versionType) {
        dispatcher.execute(new VersionDsdAction(urn, versionType), new WaitingAsyncCallback<VersionDsdResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorVersioning()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(VersionDsdResult result) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getMessageList(MetamacSrmWeb.getMessages().dsdVersioned()), MessageTypeEnum.SUCCESS);
                // Update the URL (the method placeManager.updateHistory only allow to update the last placeRequest)
                goToDsd(result.getDataStructureDefinitionMetamacDto().getUrn());
                // Update the version list
                UpdateMaintainableArtefactVersionsEvent.fire(DsdGeneralTabPresenter.this, result.getDataStructureDefinitionMetamacDto().getUrn());
            }
        });
    }

    @Override
    public void createTemporalVersion(String urn) {
        dispatcher.execute(new CreateDsdTemporalVersionAction(urn), new WaitingAsyncCallback<CreateDsdTemporalVersionResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().resourceErrorEditingPublishedResource()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(CreateDsdTemporalVersionResult result) {
                retrieveCompleteDsd(result.getDataStructureDefinitionMetamacDto().getUrn(), true);
                // Update the version list
                UpdateMaintainableArtefactVersionsEvent.fire(DsdGeneralTabPresenter.this, result.getDataStructureDefinitionMetamacDto().getUrn());
            }
        });
    }

    @Override
    public void cancelValidity(final String urn) {
        List<String> urns = new ArrayList<String>();
        urns.add(urn);
        dispatcher.execute(new CancelDsdValidityAction(urns), new WaitingAsyncCallback<CancelDsdValidityResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().dsdErrorCancelValidity()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(CancelDsdValidityResult result) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getMessageList(getMessages().dsdCanceledValidity()), MessageTypeEnum.SUCCESS);
                retrieveCompleteDsd(urn);
            }
        });
    }

    //
    // RELATED RESOURCES
    //

    @Override
    public void retrieveStatisticalOperations(int firstResult, int maxResults, String criteria) {
        StatisticalOperationWebCriteria statisticalOperationWebCriteria = new StatisticalOperationWebCriteria(criteria);
        dispatcher.execute(new GetStatisticalOperationsAction(firstResult, maxResults, statisticalOperationWebCriteria), new WaitingAsyncCallback<GetStatisticalOperationsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorRetrievingOperations()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetStatisticalOperationsResult result) {
                getView().setOperations(result);
            }
        });
    }

    private void retrieveConcepts(final String conceptSchemeUrn) {
        dispatcher.execute(new GetConceptSchemeAction(conceptSchemeUrn), new WaitingAsyncCallback<GetConceptSchemeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().conceptSchemeErrorRetrieve()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetConceptSchemeResult result) {
                final ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getConceptSchemeDto();
                dispatcher.execute(new GetConceptsBySchemeAction(conceptSchemeUrn, ApplicationEditionLanguages.getCurrentLocale()), new WaitingAsyncCallback<GetConceptsBySchemeResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().conceptErrorRetrieveList()), MessageTypeEnum.ERROR);
                    }
                    @Override
                    public void onWaitSuccess(GetConceptsBySchemeResult result) {
                        getView().setConceptsForShowDecimalsPrecision(conceptSchemeMetamacDto, result.getItemVisualisationResults());
                    }
                });

            }
        });
    }

    private void setConceptSchemeOfTheMeasureDimension(List<DimensionComponentDto> dimensionComponentDtos) {
        String conceptSchemeUrn = CommonUtils.getConceptSchemeUrnOfMeasureDimensionRepresentation(dimensionComponentDtos);
        // Load the concepts if the conceptScheme is not null and has not been retrieved previously
        // The concepts are used to fill 'showDecimalsPrecision' (a DSD visualisation metadata)
        if (conceptSchemeUrn != null && !StringUtils.equals(conceptSchemeUrn, conceptSchemeUrnOfMeasureDimensionRepresentation)) {
            this.conceptSchemeUrnOfMeasureDimensionRepresentation = conceptSchemeUrn;
            // Load concepts of the specified concept scheme
            retrieveConcepts(conceptSchemeUrn);
        } else {
            conceptSchemeUrn = null;
        }
    }

    @Override
    public void goToDsd(String urn) {
        if (!StringUtils.isBlank(urn)) {
            placeManager.revealRelativePlace(PlaceRequestUtils.buildRelativeDsdPlaceRequest(urn), -2);
        }
    }
}
