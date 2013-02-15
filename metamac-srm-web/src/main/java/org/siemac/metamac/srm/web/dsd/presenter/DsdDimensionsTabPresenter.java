package org.siemac.metamac.srm.web.dsd.presenter;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.dsd.events.SelectDsdAndDescriptorsEvent;
import org.siemac.metamac.srm.web.dsd.events.SelectDsdAndDescriptorsEvent.SelectDsdAndDescriptorsHandler;
import org.siemac.metamac.srm.web.dsd.events.SelectViewDsdDescriptorEvent;
import org.siemac.metamac.srm.web.dsd.events.UpdateDimensionsEvent;
import org.siemac.metamac.srm.web.dsd.events.UpdateDsdEvent;
import org.siemac.metamac.srm.web.dsd.utils.CommonUtils;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdDimensionsTabUiHandlers;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesAction;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.srm.web.shared.StructuralResourcesRelationEnum;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptSchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptWebCriteria;
import org.siemac.metamac.srm.web.shared.dsd.DeleteDimensionListForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.DeleteDimensionListForDsdResult;
import org.siemac.metamac.srm.web.shared.dsd.FindDescriptorForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.FindDescriptorForDsdResult;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdAndDescriptorsAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdAndDescriptorsResult;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdResult;
import org.siemac.metamac.srm.web.shared.dsd.SaveComponentForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.SaveComponentForDsdResult;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.UrnUtils;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DescriptorDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DimensionComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeComponentList;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeDimensionComponent;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.TitleFunction;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.HasClickHandlers;

public class DsdDimensionsTabPresenter extends Presenter<DsdDimensionsTabPresenter.DsdDimensionsTabView, DsdDimensionsTabPresenter.DsdDimensionsTabProxy>
        implements
            DsdDimensionsTabUiHandlers,
            SelectDsdAndDescriptorsHandler {

    private final DispatchAsync               dispatcher;
    private final PlaceManager                placeManager;

    private DataStructureDefinitionMetamacDto dataStructureDefinitionDto;
    private boolean                           isNewDescriptor;
    private List<DimensionComponentDto>       dimensionComponentDtos;

    @ProxyCodeSplit
    @NameToken(NameTokens.dsdDimensionsPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface DsdDimensionsTabProxy extends Proxy<DsdDimensionsTabPresenter>, Place {

    }

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbDimensions();
    }

    public interface DsdDimensionsTabView extends View, HasUiHandlers<DsdDimensionsTabUiHandlers> {

        void setConceptSchemes(GetRelatedResourcesResult result);
        void setConcepts(GetRelatedResourcesResult result);

        void setConceptSchemesForDimensionRole(GetRelatedResourcesResult result);
        void setConceptsForDimensionRole(GetRelatedResourcesResult result);

        void setConceptSchemesForMeasureDimensionEnumeratedRepresentation(GetRelatedResourcesResult result);
        void setCodelistsForEnumeratedRepresentation(GetRelatedResourcesResult result);

        void setDsdDimensions(DataStructureDefinitionMetamacDto dsd, List<DimensionComponentDto> dimensionComponentDtos);
        DimensionComponentDto getDsdDimension();
        List<DimensionComponentDto> getSelectedDimensions();
        void onDimensionSaved(DimensionComponentDto dimensionComponentDto);
        boolean validate();

        void setConceptsAsRole(List<RelatedResourceDto> roles, int firstResult, int totalResults);

        HasClickHandlers getSave();
        HasClickHandlers getDelete();
    }

    @Inject
    public DsdDimensionsTabPresenter(EventBus eventBus, DsdDimensionsTabView dsdDimensionTabView, DsdDimensionsTabProxy dsdDimensionTabProxy, DispatchAsync dispatcher, PlaceManager placeManager) {
        super(eventBus, dsdDimensionTabView, dsdDimensionTabProxy);
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
            // Load DSD completely if it hasn't been loaded previously
            if (dataStructureDefinitionDto == null || !dsdIdentifier.equals(UrnUtils.removePrefix(dataStructureDefinitionDto.getUrn()))) {
                retrieveDsd(UrnUtils.generateUrn(UrnConstants.URN_SDMX_CLASS_DATASTRUCTURE_PREFIX, dsdIdentifier));
            }
        }
    }

    @Override
    protected void onBind() {
        super.onBind();

        registerHandler(getView().getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (getView().validate()) {
                    saveDimension(getView().getDsdDimension());
                }
            }
        }));

        registerHandler(getView().getDelete().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                List<DimensionComponentDto> dimensionsToDelete = getView().getSelectedDimensions();
                deleteDimensions(dimensionsToDelete);
            }
        }));
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        SelectViewDsdDescriptorEvent.fire(this, TypeComponentList.DIMENSION_DESCRIPTOR);
    }

    @ProxyEvent
    @Override
    public void onSelectDsdAndDescriptors(SelectDsdAndDescriptorsEvent event) {
        dataStructureDefinitionDto = event.getDataStructureDefinitionDto();
        DescriptorDto dimensionsDescriptor = event.getDimensions();
        isNewDescriptor = dimensionsDescriptor.getId() == null;
        dimensionComponentDtos = CommonUtils.getDimensionComponents(dimensionsDescriptor);
        setDsdDimensions(dataStructureDefinitionDto, dimensionComponentDtos);
    }

    @Override
    public void saveDimension(DimensionComponentDto dimensionToSave) {
        dispatcher.execute(new SaveComponentForDsdAction(dataStructureDefinitionDto.getUrn(), dimensionToSave, TypeComponentList.DIMENSION_DESCRIPTOR),
                new WaitingAsyncCallback<SaveComponentForDsdResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fire(DsdDimensionsTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdDimensionErrorSave()), MessageTypeEnum.ERROR);
                    }

                    @Override
                    public void onWaitSuccess(SaveComponentForDsdResult result) {
                        ShowMessageEvent.fire(DsdDimensionsTabPresenter.this, ErrorUtils.getMessageList(MetamacSrmWeb.getMessages().dsdDimensionSaved()), MessageTypeEnum.SUCCESS);
                        updateDimensionList(false); // Do no update the view!! The method onDimensionSaved updates the dimension list in the view
                        getView().onDimensionSaved((DimensionComponentDto) result.getComponentDtoSaved());
                        if (isNewDescriptor) {
                            // The first time a descriptor is saved, the DSD version changes.
                            isNewDescriptor = false;
                            updateDsd();
                        }
                    }
                });
    }

    @Override
    public void deleteDimensions(List<DimensionComponentDto> dimensionsToDelete) {
        dispatcher.execute(new DeleteDimensionListForDsdAction(dataStructureDefinitionDto.getUrn(), dimensionsToDelete), new WaitingAsyncCallback<DeleteDimensionListForDsdResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                updateDimensionList(true);
                ShowMessageEvent.fire(DsdDimensionsTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdDimensionErrorDeleteDetails()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(DeleteDimensionListForDsdResult result) {
                updateDimensionList(true);
                ShowMessageEvent.fire(DsdDimensionsTabPresenter.this, ErrorUtils.getMessageList(MetamacSrmWeb.getMessages().dsdDimensionDeleted()), MessageTypeEnum.SUCCESS);
            }
        });
    }

    @Override
    public void retrieveDsd(String urn) {
        dispatcher.execute(new GetDsdAndDescriptorsAction(urn), new WaitingAsyncCallback<GetDsdAndDescriptorsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdDimensionsTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetDsdAndDescriptorsResult result) {
                SelectDsdAndDescriptorsEvent.fire(DsdDimensionsTabPresenter.this, result.getDsd(), result.getPrimaryMeasure(), result.getDimensions(), result.getAttributes(), result.getGroupKeys());
            }
        });
    }

    private void updateDimensionList(final boolean updateView) {
        dimensionComponentDtos = new ArrayList<DimensionComponentDto>();
        dispatcher.execute(new FindDescriptorForDsdAction(dataStructureDefinitionDto.getUrn(), TypeComponentList.DIMENSION_DESCRIPTOR), new WaitingAsyncCallback<FindDescriptorForDsdResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdDimensionsTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(FindDescriptorForDsdResult result) {
                List<DescriptorDto> descriptorDtos = result.getDescriptorDtos();
                if (!descriptorDtos.isEmpty()) {
                    DescriptorDto descriptorDto = descriptorDtos.get(0);
                    if (descriptorDto != null) {
                        for (ComponentDto componentDto : descriptorDto.getComponents()) {
                            if (componentDto instanceof DimensionComponentDto) {
                                DimensionComponentDto dimensionComponentDto = (DimensionComponentDto) componentDto;
                                dimensionComponentDtos.add(dimensionComponentDto);
                            }
                        }
                    }
                }
                if (updateView) {
                    setDsdDimensions(dataStructureDefinitionDto, dimensionComponentDtos);
                }
                UpdateDimensionsEvent.fire(DsdDimensionsTabPresenter.this, dimensionComponentDtos);
            }
        });
    }

    private void updateDsd() {
        dispatcher.execute(new GetDsdAction(dataStructureDefinitionDto.getUrn()), new WaitingAsyncCallback<GetDsdResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdDimensionsTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetDsdResult result) {
                UpdateDsdEvent.fire(DsdDimensionsTabPresenter.this, result.getDsd());
            }
        });
    }

    @Override
    public void retrieveConceptSchemes(TypeDimensionComponent dimensionType, int firstResult, int maxResults) {
        StructuralResourcesRelationEnum relationType = getRelationTypeForConceptScheme(dimensionType);
        dispatcher.execute(new GetRelatedResourcesAction(relationType, firstResult, maxResults, new ConceptSchemeWebCriteria(null, dataStructureDefinitionDto.getUrn())),
                new WaitingAsyncCallback<GetRelatedResourcesResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fire(DsdDimensionsTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().conceptSchemeErrorRetrieveList()), MessageTypeEnum.ERROR);
                    }
                    @Override
                    public void onWaitSuccess(GetRelatedResourcesResult result) {
                        getView().setConceptSchemes(result);
                    }
                });
    }

    @Override
    public void retrieveConcepts(TypeDimensionComponent dimensionType, int firstResult, int maxResults, String criteria, String conceptSchemeUrn) {
        StructuralResourcesRelationEnum relationType = getRelationTypeForConcept(dimensionType);
        dispatcher.execute(new GetRelatedResourcesAction(relationType, firstResult, maxResults, new ConceptWebCriteria(criteria, dataStructureDefinitionDto.getUrn(), conceptSchemeUrn)),
                new WaitingAsyncCallback<GetRelatedResourcesResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fire(DsdDimensionsTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().conceptErrorRetrieveList()), MessageTypeEnum.ERROR);
                    }
                    @Override
                    public void onWaitSuccess(GetRelatedResourcesResult result) {
                        getView().setConcepts(result);
                    }
                });
    }

    @Override
    public void retrieveConceptSchemesForMeasureDimensionEnumeratedRepresentation(int firstResult, int maxResults, String criteria) {
        dispatcher.execute(new GetRelatedResourcesAction(StructuralResourcesRelationEnum.CONCEPT_SCHEME_WITH_DSD_MEASURE_DIMENSION_ENUMERATED_REPRESENTATION, firstResult, maxResults,
                new ConceptSchemeWebCriteria(criteria, dataStructureDefinitionDto.getUrn())), new WaitingAsyncCallback<GetRelatedResourcesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdDimensionsTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().conceptSchemeErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetRelatedResourcesResult result) {
                getView().setConceptSchemesForMeasureDimensionEnumeratedRepresentation(result);
            }
        });
    }

    @Override
    public void retrieveCodelistsForEnumeratedRepresentation(int firstResult, int maxResults, String criteria, String conceptUrn) {
        dispatcher.execute(new GetRelatedResourcesAction(StructuralResourcesRelationEnum.CODELIST_WITH_DSD_DIMENSION_ENUMERATED_REPRESENTATION, firstResult, maxResults, new CodelistWebCriteria(
                criteria, conceptUrn)), new WaitingAsyncCallback<GetRelatedResourcesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdDimensionsTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().codelistErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetRelatedResourcesResult result) {
                getView().setCodelistsForEnumeratedRepresentation(result);
            }
        });
    }

    @Override
    public void retrieveConceptSchemesForDimensionRole(int firstResult, int maxResults) {
        dispatcher.execute(new GetRelatedResourcesAction(StructuralResourcesRelationEnum.CONCEPT_SCHEMES_WITH_DSD_ROLES, firstResult, maxResults, new ConceptSchemeWebCriteria(null,
                dataStructureDefinitionDto.getUrn())), new WaitingAsyncCallback<GetRelatedResourcesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdDimensionsTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().conceptSchemeErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetRelatedResourcesResult result) {
                getView().setConceptSchemesForDimensionRole(result);
            }
        });
    }

    @Override
    public void retrieveConceptsForDimensionRole(int firstResult, int maxResults, String criteria, String conceptSchemeUrn) {
        dispatcher.execute(
                new GetRelatedResourcesAction(StructuralResourcesRelationEnum.CONCEPTS_WITH_DSD_ROLES, firstResult, maxResults, new ConceptWebCriteria(criteria, dataStructureDefinitionDto.getUrn(),
                        conceptSchemeUrn)), new WaitingAsyncCallback<GetRelatedResourcesResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fire(DsdDimensionsTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().conceptErrorRetrievingConceptsAsRole()),
                                MessageTypeEnum.ERROR);
                    }
                    @Override
                    public void onWaitSuccess(GetRelatedResourcesResult result) {
                        getView().setConceptsForDimensionRole(result);
                    }
                });
    }

    private void setDsdDimensions(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto, List<DimensionComponentDto> dimensionComponentDtos) {
        getView().setDsdDimensions(dataStructureDefinitionMetamacDto, dimensionComponentDtos);
    }

    private StructuralResourcesRelationEnum getRelationTypeForConceptScheme(TypeDimensionComponent dimensionType) {
        StructuralResourcesRelationEnum relationType = null;
        if (TypeDimensionComponent.DIMENSION.equals(dimensionType)) {
            relationType = StructuralResourcesRelationEnum.CONCEPT_SCHEMES_WITH_DSD_DIMENSION;
        } else if (TypeDimensionComponent.MEASUREDIMENSION.equals(dimensionType)) {
            relationType = StructuralResourcesRelationEnum.CONCEPT_SCHEMES_WITH_DSD_MEASURE_DIMENSION;
        } else if (TypeDimensionComponent.TIMEDIMENSION.equals(dimensionType)) {
            relationType = StructuralResourcesRelationEnum.CONCEPT_SCHEMES_WITH_DSD_TIME_DIMENSION;
        }
        return relationType;
    }

    private StructuralResourcesRelationEnum getRelationTypeForConcept(TypeDimensionComponent dimensionType) {
        StructuralResourcesRelationEnum relationType = null;
        if (TypeDimensionComponent.DIMENSION.equals(dimensionType)) {
            relationType = StructuralResourcesRelationEnum.CONCEPT_WITH_DSD_DIMENSION;
        } else if (TypeDimensionComponent.MEASUREDIMENSION.equals(dimensionType)) {
            relationType = StructuralResourcesRelationEnum.CONCEPT_WITH_DSD_MEASURE_DIMENSION;
        } else if (TypeDimensionComponent.TIMEDIMENSION.equals(dimensionType)) {
            relationType = StructuralResourcesRelationEnum.CONCEPT_WITH_DSD_TIME_DIMENSION;
        }
        return relationType;
    }
}
