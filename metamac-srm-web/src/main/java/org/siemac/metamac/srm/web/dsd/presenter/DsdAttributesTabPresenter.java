package org.siemac.metamac.srm.web.dsd.presenter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.navigation.shared.NameTokens;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.dsd.enums.DsdTabTypeEnum;
import org.siemac.metamac.srm.web.dsd.events.SelectViewDsdDescriptorEvent;
import org.siemac.metamac.srm.web.dsd.utils.CommonUtils;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdAttributesTabUiHandlers;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesAction;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.srm.web.shared.StructuralResourcesRelationEnum;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptSchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptWebCriteria;
import org.siemac.metamac.srm.web.shared.dsd.DeleteAttributesForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.DeleteAttributesForDsdResult;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdAndDescriptorsAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdAndDescriptorsResult;
import org.siemac.metamac.srm.web.shared.dsd.SaveComponentForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.SaveComponentForDsdResult;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataAttributeDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DescriptorDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DimensionComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.SpecialAttributeTypeEnum;
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
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.HasClickHandlers;

public class DsdAttributesTabPresenter extends Presenter<DsdAttributesTabPresenter.DsdAttributesTabView, DsdAttributesTabPresenter.DsdAttributesTabProxy> implements DsdAttributesTabUiHandlers {

    private final DispatchAsync               dispatcher;
    private final PlaceManager                placeManager;

    private DataStructureDefinitionMetamacDto dataStructureDefinitionDto;
    private List<DataAttributeDto>            dataAttributeDtos;

    @ProxyCodeSplit
    @NameToken(NameTokens.dsdAttributesPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface DsdAttributesTabProxy extends Proxy<DsdAttributesTabPresenter>, Place {

    }

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbAttributes();
    }

    public interface DsdAttributesTabView extends View, HasUiHandlers<DsdAttributesTabUiHandlers> {

        void setConceptSchemes(GetRelatedResourcesResult result);
        void setConcepts(GetRelatedResourcesResult result);

        void setConceptSchemesForAttributeRole(GetRelatedResourcesResult result);
        void setConceptsForAttributeRole(GetRelatedResourcesResult result);

        void setCodelistsForEnumeratedRepresentation(GetRelatedResourcesResult result);
        void setConceptSchemesForEnumeratedRepresentation(GetRelatedResourcesResult result);

        void setDimensions(List<DimensionComponentDto> dimensionComponentDtos);
        void setGroupKeys(List<DescriptorDto> descriptorDtos);

        void setDsdAttributes(DataStructureDefinitionMetamacDto dsd, List<DataAttributeDto> dataAttributeDtos);
        DataAttributeDto getDsdAttribute();
        List<DataAttributeDto> getSelectedAttributes();
        void onAttributeSaved(DataAttributeDto dataAttributeDto);
        boolean validate();

        HasClickHandlers getSave();
        HasClickHandlers getDelete();
    }

    @Inject
    public DsdAttributesTabPresenter(EventBus eventBus, DsdAttributesTabView dsdAttributesTabView, DsdAttributesTabProxy dsdAttributesTabProxy, DispatchAsync dispatcher, PlaceManager placeManager) {
        super(eventBus, dsdAttributesTabView, dsdAttributesTabProxy);
        this.dispatcher = dispatcher;
        this.placeManager = placeManager;
        getView().setUiHandlers(this);
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        SelectViewDsdDescriptorEvent.fire(this, DsdTabTypeEnum.ATTRIBUTES);
    }

    @Override
    protected void onReset() {
        super.onReset();
    }

    @Override
    protected void onBind() {
        super.onBind();

        registerHandler(getView().getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (getView().validate()) {
                    saveAttribute(getView().getDsdAttribute());
                }
            }
        }));

        registerHandler(getView().getDelete().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                List<DataAttributeDto> attributesToDelete = getView().getSelectedAttributes();
                deleteAttributes(attributesToDelete);
            }
        }));
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
            retrieveAttributes(UrnUtils.generateUrn(UrnConstants.URN_SDMX_CLASS_DATASTRUCTURE_PREFIX, dsdIdentifier));
        }
    }

    @Override
    public void saveAttribute(DataAttributeDto attribute) {
        dispatcher.execute(new SaveComponentForDsdAction(dataStructureDefinitionDto.getUrn(), attribute, TypeComponentList.ATTRIBUTE_DESCRIPTOR),
                new WaitingAsyncCallback<SaveComponentForDsdResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fireErrorMessage(DsdAttributesTabPresenter.this, caught);
                    }

                    @Override
                    public void onWaitSuccess(SaveComponentForDsdResult result) {
                        ShowMessageEvent.fireSuccessMessage(DsdAttributesTabPresenter.this, MetamacSrmWeb.getMessages().dsdAttributeSaved());
                        dataStructureDefinitionDto = result.getDataStructureDefinitionMetamacDto();
                        updateAttributeList(false);
                        getView().onAttributeSaved((DataAttributeDto) result.getComponentDtoSaved());
                    }
                });
    }

    @Override
    public void deleteAttributes(List<DataAttributeDto> attributesToDelete) {
        dispatcher.execute(new DeleteAttributesForDsdAction(dataStructureDefinitionDto.getUrn(), attributesToDelete), new WaitingAsyncCallback<DeleteAttributesForDsdResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                updateAttributeList(true);
                ShowMessageEvent.fireErrorMessage(DsdAttributesTabPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(DeleteAttributesForDsdResult result) {
                updateAttributeList(true);
                ShowMessageEvent.fireSuccessMessage(DsdAttributesTabPresenter.this, MetamacSrmWeb.getMessages().dsdAttributeDeleted());
            }
        });
    }

    public void retrieveAttributes(String urn) {
        Set<TypeComponentList> descriptorsToRetrieve = new HashSet<TypeComponentList>();
        descriptorsToRetrieve.add(TypeComponentList.DIMENSION_DESCRIPTOR);
        descriptorsToRetrieve.add(TypeComponentList.ATTRIBUTE_DESCRIPTOR);
        descriptorsToRetrieve.add(TypeComponentList.GROUP_DIMENSION_DESCRIPTOR);
        dispatcher.execute(new GetDsdAndDescriptorsAction(urn, descriptorsToRetrieve), new WaitingAsyncCallback<GetDsdAndDescriptorsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(DsdAttributesTabPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetDsdAndDescriptorsResult result) {
                dataStructureDefinitionDto = result.getDsd();
                DescriptorDto attributesDescriptor = result.getAttributes();
                dataAttributeDtos = CommonUtils.getAttributeComponents(attributesDescriptor);
                getView().setDimensions(CommonUtils.getDimensionComponents(result.getDimensions()));
                getView().setDsdAttributes(dataStructureDefinitionDto, CommonUtils.getAttributeComponents(result.getAttributes()));
                getView().setGroupKeys(result.getGroupKeys());
            }
        });
    }

    @Override
    public void retrieveConceptSchemes(int firstResult, int maxResults) {
        dispatcher.execute(new GetRelatedResourcesAction(StructuralResourcesRelationEnum.CONCEPT_SCHEMES_WITH_DSD_ATTRIBUTE, firstResult, maxResults, new ConceptSchemeWebCriteria(null,
                dataStructureDefinitionDto.getUrn())), new WaitingAsyncCallback<GetRelatedResourcesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(DsdAttributesTabPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetRelatedResourcesResult result) {
                getView().setConceptSchemes(result);
            }
        });
    }

    @Override
    public void retrieveConcepts(int firstResult, int maxResults, String criteria, String conceptSchemeUrn) {
        ConceptWebCriteria conceptWebCriteria = new ConceptWebCriteria(criteria);
        conceptWebCriteria.setItemSchemeUrn(conceptSchemeUrn);
        conceptWebCriteria.setDsdUrn(dataStructureDefinitionDto.getUrn());
        dispatcher.execute(new GetRelatedResourcesAction(StructuralResourcesRelationEnum.CONCEPT_WITH_DSD_ATTRIBUTE, firstResult, maxResults, conceptWebCriteria),
                new WaitingAsyncCallback<GetRelatedResourcesResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fireErrorMessage(DsdAttributesTabPresenter.this, caught);
                    }
                    @Override
                    public void onWaitSuccess(GetRelatedResourcesResult result) {
                        getView().setConcepts(result);
                    }
                });
    }

    @Override
    public void retrieveConceptSchemesForAttributeRole(int firstResult, int maxResults) {
        dispatcher.execute(new GetRelatedResourcesAction(StructuralResourcesRelationEnum.CONCEPT_SCHEMES_WITH_DSD_ROLES, firstResult, maxResults, new ConceptSchemeWebCriteria(null,
                dataStructureDefinitionDto.getUrn())), new WaitingAsyncCallback<GetRelatedResourcesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(DsdAttributesTabPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetRelatedResourcesResult result) {
                getView().setConceptSchemesForAttributeRole(result);
            }
        });
    }

    @Override
    public void retrieveConceptsForAttributeRole(int firstResult, int maxResults, String criteria, String conceptSchemeUrn) {
        ConceptWebCriteria conceptWebCriteria = new ConceptWebCriteria(criteria);
        conceptWebCriteria.setItemSchemeUrn(conceptSchemeUrn);
        conceptWebCriteria.setDsdUrn(dataStructureDefinitionDto.getUrn());
        dispatcher.execute(new GetRelatedResourcesAction(StructuralResourcesRelationEnum.CONCEPTS_WITH_DSD_ROLES, firstResult, maxResults, conceptWebCriteria),
                new WaitingAsyncCallback<GetRelatedResourcesResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fireErrorMessage(DsdAttributesTabPresenter.this, caught);
                    }
                    @Override
                    public void onWaitSuccess(GetRelatedResourcesResult result) {
                        getView().setConceptsForAttributeRole(result);
                    }
                });
    }

    @Override
    public void retrieveCodelistsForEnumeratedRepresentation(int firstResult, int maxResults, String criteria, String conceptUrn, SpecialAttributeTypeEnum attributeTypeEnum) {

        StructuralResourcesRelationEnum structuralResourcesRelationEnum = SpecialAttributeTypeEnum.SPATIAL_EXTENDS.equals(attributeTypeEnum)
                ? StructuralResourcesRelationEnum.CODELIST_WITH_DSD_SPATIAL_ATTRIBUTE_ENUMERATED_REPRESENTATION
                : StructuralResourcesRelationEnum.CODELIST_WITH_DSD_ATTRIBUTE_ENUMERATED_REPRESENTATION;

        dispatcher.execute(new GetRelatedResourcesAction(structuralResourcesRelationEnum, firstResult, maxResults, new CodelistWebCriteria(criteria, conceptUrn)),
                new WaitingAsyncCallback<GetRelatedResourcesResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fireErrorMessage(DsdAttributesTabPresenter.this, caught);
                    }
                    @Override
                    public void onWaitSuccess(GetRelatedResourcesResult result) {
                        getView().setCodelistsForEnumeratedRepresentation(result);
                    }
                });
    }

    @Override
    public void retrieveConceptSchemesForEnumeratedRepresentation(int firstResult, int maxResults, String criteria) {
        dispatcher.execute(new GetRelatedResourcesAction(StructuralResourcesRelationEnum.CONCEPT_SCHEME_WITH_DSD_MEASURE_ATTRIBUTE_ENUMERATED_REPRESENTATION, firstResult, maxResults,
                new ConceptSchemeWebCriteria(criteria, dataStructureDefinitionDto.getUrn())), new WaitingAsyncCallback<GetRelatedResourcesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(DsdAttributesTabPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetRelatedResourcesResult result) {
                getView().setConceptSchemesForEnumeratedRepresentation(result);
            }
        });
    }

    private void updateAttributeList(final boolean updateView) {
        dataAttributeDtos = new ArrayList<DataAttributeDto>();

        Set<TypeComponentList> descriptorsToRetrieve = new HashSet<TypeComponentList>();
        descriptorsToRetrieve.add(TypeComponentList.ATTRIBUTE_DESCRIPTOR);
        dispatcher.execute(new GetDsdAndDescriptorsAction(dataStructureDefinitionDto.getUrn(), descriptorsToRetrieve), new WaitingAsyncCallback<GetDsdAndDescriptorsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(DsdAttributesTabPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetDsdAndDescriptorsResult result) {
                dataStructureDefinitionDto = result.getDsd();
                dataAttributeDtos = CommonUtils.getAttributeComponents(result.getAttributes());
                if (updateView) {
                    getView().setDsdAttributes(dataStructureDefinitionDto, dataAttributeDtos);
                }
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
}
