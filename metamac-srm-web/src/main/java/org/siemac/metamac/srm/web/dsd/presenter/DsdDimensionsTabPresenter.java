package org.siemac.metamac.srm.web.dsd.presenter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.navigation.shared.NameTokens;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.dsd.enums.DsdTabTypeEnum;
import org.siemac.metamac.srm.web.dsd.events.SelectViewDsdDescriptorEvent;
import org.siemac.metamac.srm.web.dsd.utils.CommonUtils;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdDimensionsTabUiHandlers;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesAction;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.srm.web.shared.StructuralResourcesRelationEnum;
import org.siemac.metamac.srm.web.shared.concept.GetConceptAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptResult;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptSchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptWebCriteria;
import org.siemac.metamac.srm.web.shared.dsd.DeleteDimensionListForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.DeleteDimensionListForDsdResult;
import org.siemac.metamac.srm.web.shared.dsd.GetDefaultDimensionForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDefaultDimensionForDsdResult;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdAndDescriptorsAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdAndDescriptorsResult;
import org.siemac.metamac.srm.web.shared.dsd.SaveComponentForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.SaveComponentForDsdResult;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DescriptorDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DimensionComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RepresentationTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.SpecialDimensionTypeEnum;
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

public class DsdDimensionsTabPresenter extends Presenter<DsdDimensionsTabPresenter.DsdDimensionsTabView, DsdDimensionsTabPresenter.DsdDimensionsTabProxy> implements DsdDimensionsTabUiHandlers {

    private final DispatchAsync               dispatcher;
    private final PlaceManager                placeManager;

    private DataStructureDefinitionMetamacDto dataStructureDefinitionDto;
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
        void setDefaultDimensionToCreate(DimensionComponentDto dimensionComponentDto);

        DimensionComponentDto getDsdDimension();
        List<DimensionComponentDto> getSelectedDimensions();
        void onDimensionSaved(DimensionComponentDto dimensionComponentDto);
        boolean validate();

        void setConceptsAsRole(List<RelatedResourceDto> roles, int firstResult, int totalResults);

        void setDefaultConceptSchemeEnumeratedRepresentation(RelatedResourceDto conceptScheme);

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
            retrieveDimensions(UrnUtils.generateUrn(UrnConstants.URN_SDMX_CLASS_DATASTRUCTURE_PREFIX, dsdIdentifier));
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
        SelectViewDsdDescriptorEvent.fire(this, DsdTabTypeEnum.DIMENSIONS);
    }

    @Override
    public void saveDimension(DimensionComponentDto dimensionToSave) {
        dispatcher.execute(new SaveComponentForDsdAction(dataStructureDefinitionDto.getUrn(), dimensionToSave, TypeComponentList.DIMENSION_DESCRIPTOR),
                new WaitingAsyncCallback<SaveComponentForDsdResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fireErrorMessage(DsdDimensionsTabPresenter.this, caught);
                    }

                    @Override
                    public void onWaitSuccess(SaveComponentForDsdResult result) {
                        ShowMessageEvent.fireSuccessMessage(DsdDimensionsTabPresenter.this, MetamacSrmWeb.getMessages().dsdDimensionSaved());
                        dataStructureDefinitionDto = result.getDataStructureDefinitionMetamacDto();
                        updateDimensionList(false); // Do no update the view!! The method onDimensionSaved updates the dimension list in the view
                        getView().onDimensionSaved((DimensionComponentDto) result.getComponentDtoSaved());
                    }
                });
    }

    @Override
    public void deleteDimensions(List<DimensionComponentDto> dimensionsToDelete) {
        dispatcher.execute(new DeleteDimensionListForDsdAction(dataStructureDefinitionDto.getUrn(), dimensionsToDelete), new WaitingAsyncCallback<DeleteDimensionListForDsdResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                updateDimensionList(true);
                ShowMessageEvent.fireErrorMessage(DsdDimensionsTabPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(DeleteDimensionListForDsdResult result) {
                updateDimensionList(true);
                ShowMessageEvent.fireSuccessMessage(DsdDimensionsTabPresenter.this, MetamacSrmWeb.getMessages().dsdDimensionDeleted());
            }
        });
    }

    public void retrieveDimensions(String urn) {
        Set<TypeComponentList> descriptorsToRetrieve = new HashSet<TypeComponentList>();
        descriptorsToRetrieve.add(TypeComponentList.DIMENSION_DESCRIPTOR);
        dispatcher.execute(new GetDsdAndDescriptorsAction(urn, descriptorsToRetrieve), new WaitingAsyncCallback<GetDsdAndDescriptorsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(DsdDimensionsTabPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetDsdAndDescriptorsResult result) {
                dataStructureDefinitionDto = result.getDsd();
                DescriptorDto dimensionsDescriptor = result.getDimensions();
                dimensionComponentDtos = CommonUtils.getDimensionComponents(dimensionsDescriptor);
                setDsdDimensions(dataStructureDefinitionDto, dimensionComponentDtos);
            }
        });
    }

    private void updateDimensionList(final boolean updateView) {
        dimensionComponentDtos = new ArrayList<DimensionComponentDto>();

        Set<TypeComponentList> descriptorsToRetrieve = new HashSet<TypeComponentList>();
        descriptorsToRetrieve.add(TypeComponentList.DIMENSION_DESCRIPTOR);
        dispatcher.execute(new GetDsdAndDescriptorsAction(dataStructureDefinitionDto.getUrn(), descriptorsToRetrieve), new WaitingAsyncCallback<GetDsdAndDescriptorsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(DsdDimensionsTabPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetDsdAndDescriptorsResult result) {
                dataStructureDefinitionDto = result.getDsd();
                dimensionComponentDtos = CommonUtils.getDimensionComponents(result.getDimensions());
                if (updateView) {
                    setDsdDimensions(dataStructureDefinitionDto, dimensionComponentDtos);
                }
            }
        });
    }

    @Override
    public void retrieveConceptSchemes(TypeDimensionComponent dimensionType, SpecialDimensionTypeEnum specialDimensionTypeEnum, int firstResult, int maxResults,
            ConceptSchemeWebCriteria conceptSchemeWebCriteria) {
        StructuralResourcesRelationEnum relationType = getRelationTypeForConceptScheme(dimensionType, specialDimensionTypeEnum);
        dispatcher.execute(new GetRelatedResourcesAction(relationType, firstResult, maxResults, conceptSchemeWebCriteria), new WaitingAsyncCallback<GetRelatedResourcesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(DsdDimensionsTabPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetRelatedResourcesResult result) {
                getView().setConceptSchemes(result);
            }
        });
    }

    @Override
    public void retrieveConcepts(TypeDimensionComponent dimensionType, SpecialDimensionTypeEnum specialDimensionType, int firstResult, int maxResults, ConceptWebCriteria conceptWebCriteria) {
        StructuralResourcesRelationEnum relationType = getRelationTypeForConcept(dimensionType, specialDimensionType);
        dispatcher.execute(new GetRelatedResourcesAction(relationType, firstResult, maxResults, conceptWebCriteria), new WaitingAsyncCallback<GetRelatedResourcesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(DsdDimensionsTabPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetRelatedResourcesResult result) {
                getView().setConcepts(result);
            }
        });
    }

    @Override
    public void retrieveConceptSchemesForMeasureDimensionEnumeratedRepresentation(int firstResult, int maxResults, ConceptSchemeWebCriteria conceptSchemeWebCriteria) {
        dispatcher.execute(new GetRelatedResourcesAction(StructuralResourcesRelationEnum.CONCEPT_SCHEME_WITH_DSD_MEASURE_DIMENSION_ENUMERATED_REPRESENTATION, firstResult, maxResults,
                conceptSchemeWebCriteria), new WaitingAsyncCallback<GetRelatedResourcesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(DsdDimensionsTabPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetRelatedResourcesResult result) {
                getView().setConceptSchemesForMeasureDimensionEnumeratedRepresentation(result);
            }
        });
    }

    @Override
    public void retrieveCodelistsForEnumeratedRepresentation(int firstResult, int maxResults, CodelistWebCriteria codelistWebCriteria, boolean isSpatialDimension) {

        StructuralResourcesRelationEnum structuralResourcesRelationEnum = isSpatialDimension
                ? StructuralResourcesRelationEnum.CODELIST_WITH_DSD_SPATIAL_DIMENSION_ENUMERATED_REPRESENTATION
                : StructuralResourcesRelationEnum.CODELIST_WITH_DSD_DIMENSION_ENUMERATED_REPRESENTATION;

        dispatcher.execute(new GetRelatedResourcesAction(structuralResourcesRelationEnum, firstResult, maxResults, codelistWebCriteria), new WaitingAsyncCallback<GetRelatedResourcesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(DsdDimensionsTabPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetRelatedResourcesResult result) {
                getView().setCodelistsForEnumeratedRepresentation(result);
            }
        });
    }

    @Override
    public void retrieveConceptSchemesForDimensionRole(int firstResult, int maxResults, ConceptSchemeWebCriteria conceptSchemeWebCriteria) {
        dispatcher.execute(new GetRelatedResourcesAction(StructuralResourcesRelationEnum.CONCEPT_SCHEMES_WITH_DSD_ROLES, firstResult, maxResults, conceptSchemeWebCriteria),
                new WaitingAsyncCallback<GetRelatedResourcesResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fireErrorMessage(DsdDimensionsTabPresenter.this, caught);
                    }
                    @Override
                    public void onWaitSuccess(GetRelatedResourcesResult result) {
                        getView().setConceptSchemesForDimensionRole(result);
                    }
                });
    }

    @Override
    public void retrieveConceptsForDimensionRole(int firstResult, int maxResults, ConceptWebCriteria conceptWebCriteria) {
        dispatcher.execute(new GetRelatedResourcesAction(StructuralResourcesRelationEnum.CONCEPTS_WITH_DSD_ROLES, firstResult, maxResults, conceptWebCriteria),
                new WaitingAsyncCallback<GetRelatedResourcesResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fireErrorMessage(DsdDimensionsTabPresenter.this, caught);
                    }
                    @Override
                    public void onWaitSuccess(GetRelatedResourcesResult result) {
                        getView().setConceptsForDimensionRole(result);
                    }
                });
    }

    @Override
    public void retrieveConceptSchemeEnumeratedRepresentationFromConcept(String conceptUrn) {
        dispatcher.execute(new GetConceptAction(conceptUrn), new WaitingAsyncCallback<GetConceptResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(DsdDimensionsTabPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetConceptResult result) {
                ConceptMetamacDto concept = result.getConceptDto();
                if (concept.getCoreRepresentation() != null && RepresentationTypeEnum.ENUMERATION.equals(concept.getCoreRepresentation().getRepresentationType())) {
                    RelatedResourceDto enumeration = concept.getCoreRepresentation().getEnumeration();
                    if (enumeration != null && RelatedResourceTypeEnum.CONCEPT_SCHEME.equals(enumeration.getType())) {
                        getView().setDefaultConceptSchemeEnumeratedRepresentation(enumeration);
                    }
                }
            }
        });
    }

    private void setDsdDimensions(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto, List<DimensionComponentDto> dimensionComponentDtos) {
        getView().setDsdDimensions(dataStructureDefinitionMetamacDto, dimensionComponentDtos);
    }

    @Override
    public void createDefaultDimension(String dsdUrn, TypeDimensionComponent dimensionType, SpecialDimensionTypeEnum specialDimensionType) {
        dispatcher.execute(new GetDefaultDimensionForDsdAction(dsdUrn, dimensionType, specialDimensionType), new WaitingAsyncCallback<GetDefaultDimensionForDsdResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(DsdDimensionsTabPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetDefaultDimensionForDsdResult result) {
                getView().setDefaultDimensionToCreate(result.getDimensionComponentDto());
            }
        });
    }

    private StructuralResourcesRelationEnum getRelationTypeForConceptScheme(TypeDimensionComponent dimensionType, SpecialDimensionTypeEnum specialDimensionTypeEnum) {
        StructuralResourcesRelationEnum relationType = null;
        if (TypeDimensionComponent.DIMENSION.equals(dimensionType)) {
            if (SpecialDimensionTypeEnum.SPATIAL.equals(specialDimensionTypeEnum)) {
                relationType = StructuralResourcesRelationEnum.CONCEPT_SCHEMES_WITH_DSD_SPATIAL_DIMENSION;
            } else {
                relationType = StructuralResourcesRelationEnum.CONCEPT_SCHEMES_WITH_DSD_DIMENSION;
            }
        } else if (TypeDimensionComponent.MEASUREDIMENSION.equals(dimensionType)) {
            relationType = StructuralResourcesRelationEnum.CONCEPT_SCHEMES_WITH_DSD_MEASURE_DIMENSION;
        } else if (TypeDimensionComponent.TIMEDIMENSION.equals(dimensionType)) {
            relationType = StructuralResourcesRelationEnum.CONCEPT_SCHEMES_WITH_DSD_TIME_DIMENSION;
        }
        return relationType;
    }

    private StructuralResourcesRelationEnum getRelationTypeForConcept(TypeDimensionComponent dimensionType, SpecialDimensionTypeEnum specialDimensionTypeEnum) {
        StructuralResourcesRelationEnum relationType = null;
        if (TypeDimensionComponent.DIMENSION.equals(dimensionType)) {
            if (SpecialDimensionTypeEnum.SPATIAL.equals(specialDimensionTypeEnum)) {
                relationType = StructuralResourcesRelationEnum.CONCEPT_WITH_DSD_SPATIAL_DIMENSION;
            } else {
                relationType = StructuralResourcesRelationEnum.CONCEPT_WITH_DSD_DIMENSION;
            }
        } else if (TypeDimensionComponent.MEASUREDIMENSION.equals(dimensionType)) {
            relationType = StructuralResourcesRelationEnum.CONCEPT_WITH_DSD_MEASURE_DIMENSION;
        } else if (TypeDimensionComponent.TIMEDIMENSION.equals(dimensionType)) {
            relationType = StructuralResourcesRelationEnum.CONCEPT_WITH_DSD_TIME_DIMENSION;
        }
        return relationType;
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
}
