package org.siemac.metamac.srm.web.dsd.presenter;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.dsd.enums.DsdTabTypeEnum;
import org.siemac.metamac.srm.web.dsd.events.SelectDsdAndDescriptorsEvent;
import org.siemac.metamac.srm.web.dsd.events.SelectDsdAndDescriptorsEvent.SelectDsdAndDescriptorsHandler;
import org.siemac.metamac.srm.web.dsd.events.SelectViewDsdDescriptorEvent;
import org.siemac.metamac.srm.web.dsd.events.UpdateDsdEvent;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdPrimaryMeasureTabUiHandlers;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesAction;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.srm.web.shared.StructuralResourcesRelationEnum;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptSchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptWebCriteria;
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

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DescriptorDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeComponent;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeComponentList;
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

public class DsdPrimaryMeasureTabPresenter extends Presenter<DsdPrimaryMeasureTabPresenter.DsdPrimaryMeasureTabView, DsdPrimaryMeasureTabPresenter.DsdPrimaryMeasureTabProxy>
        implements
            DsdPrimaryMeasureTabUiHandlers,
            SelectDsdAndDescriptorsHandler {

    private final DispatchAsync               dispatcher;
    private final PlaceManager                placeManager;

    private DataStructureDefinitionMetamacDto dataStructureDefinitionDto;
    private boolean                           isNewDescriptor;
    private ComponentDto                      primaryMeasure;

    @ProxyCodeSplit
    @NameToken(NameTokens.dsdPrimaryMeasurePage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface DsdPrimaryMeasureTabProxy extends Proxy<DsdPrimaryMeasureTabPresenter>, Place {

    }

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbPrimaryMeasure();
    }

    public interface DsdPrimaryMeasureTabView extends View, HasUiHandlers<DsdPrimaryMeasureTabUiHandlers> {

        void setConceptSchemes(GetRelatedResourcesResult result);
        void setConcepts(GetRelatedResourcesResult result);

        void setCodelistsForEnumeratedRepresentation(GetRelatedResourcesResult result);

        void setDsdPrimaryMeasure(DataStructureDefinitionMetamacDto dsd, ComponentDto componentDto);
        void onPrimaryMeasureSaved(DataStructureDefinitionMetamacDto dsd, ComponentDto componentDto);
    }

    @Inject
    public DsdPrimaryMeasureTabPresenter(EventBus eventBus, DsdPrimaryMeasureTabView dsdPrimaryMeasureTabView, DsdPrimaryMeasureTabProxy dsdPrimaryMeasureTabProxy, DispatchAsync dispatcher,
            PlaceManager placeManager) {
        super(eventBus, dsdPrimaryMeasureTabView, dsdPrimaryMeasureTabProxy);
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
    protected void onReveal() {
        super.onReveal();
        SelectViewDsdDescriptorEvent.fire(this, DsdTabTypeEnum.PRIMARY_MEASURE);
    }

    @Override
    protected void onReset() {
        super.onReset();
    }

    @ProxyEvent
    @Override
    public void onSelectDsdAndDescriptors(SelectDsdAndDescriptorsEvent event) {
        dataStructureDefinitionDto = event.getDataStructureDefinitionDto();
        DescriptorDto primaryMeasureDescriptor = event.getPrimaryMeasure();
        isNewDescriptor = primaryMeasureDescriptor.getId() == null;
        primaryMeasure = primaryMeasureDescriptor.getComponents().isEmpty() ? new ComponentDto(TypeComponent.PRIMARY_MEASURE, null) : primaryMeasureDescriptor.getComponents().iterator().next();
        getView().setDsdPrimaryMeasure(dataStructureDefinitionDto, primaryMeasure);
    }

    @Override
    public void savePrimaryMeasure(ComponentDto component) {
        dispatcher.execute(new SaveComponentForDsdAction(dataStructureDefinitionDto.getUrn(), component, TypeComponentList.MEASURE_DESCRIPTOR), new WaitingAsyncCallback<SaveComponentForDsdResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdPrimaryMeasureTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdPrimaryMeasureErrorSave()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(SaveComponentForDsdResult result) {
                ShowMessageEvent.fire(DsdPrimaryMeasureTabPresenter.this, ErrorUtils.getMessageList(MetamacSrmWeb.getMessages().dsdPrimaryMeasureSaved()), MessageTypeEnum.SUCCESS);
                primaryMeasure = result.getComponentDtoSaved();
                getView().onPrimaryMeasureSaved(dataStructureDefinitionDto, primaryMeasure);
                if (isNewDescriptor) {
                    // The first time a descriptor is saved, the DSD version changes.
                    isNewDescriptor = false;
                    updateDsd();
                }
            }
        });
    }

    private void updateDsd() {
        dispatcher.execute(new GetDsdAction(dataStructureDefinitionDto.getUrn()), new WaitingAsyncCallback<GetDsdResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdPrimaryMeasureTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetDsdResult result) {
                UpdateDsdEvent.fire(DsdPrimaryMeasureTabPresenter.this, result.getDsd());
            }
        });
    }

    public void retrieveDsd(String urn) {
        dispatcher.execute(new GetDsdAndDescriptorsAction(urn), new WaitingAsyncCallback<GetDsdAndDescriptorsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdPrimaryMeasureTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetDsdAndDescriptorsResult result) {
                SelectDsdAndDescriptorsEvent.fire(DsdPrimaryMeasureTabPresenter.this, result.getDsd(), result.getPrimaryMeasure(), result.getDimensions(), result.getAttributes(),
                        result.getGroupKeys());
            }
        });
    }

    @Override
    public void retrieveConceptSchemes(int firstResult, int maxResults) {
        dispatcher.execute(new GetRelatedResourcesAction(StructuralResourcesRelationEnum.CONCEPT_SCHEMES_WITH_DSD_PRIMARY_MEASURE, firstResult, maxResults, new ConceptSchemeWebCriteria(null,
                dataStructureDefinitionDto.getUrn())), new WaitingAsyncCallback<GetRelatedResourcesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdPrimaryMeasureTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().conceptSchemeErrorRetrieveList()), MessageTypeEnum.ERROR);
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
        conceptWebCriteria.setDsdUrn(dataStructureDefinitionDto.getUrn());
        conceptWebCriteria.setItemSchemeUrn(conceptSchemeUrn);
        dispatcher.execute(new GetRelatedResourcesAction(StructuralResourcesRelationEnum.CONCEPT_WITH_DSD_PRIMARY_MEASURE, firstResult, maxResults, conceptWebCriteria),
                new WaitingAsyncCallback<GetRelatedResourcesResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fire(DsdPrimaryMeasureTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().conceptErrorRetrieveList()), MessageTypeEnum.ERROR);
                    }
                    @Override
                    public void onWaitSuccess(GetRelatedResourcesResult result) {
                        getView().setConcepts(result);
                    }
                });
    }

    @Override
    public void retrieveCodelistsForEnumeratedRepresentation(int firstResult, int maxResults, String criteria) {
        dispatcher.execute(new GetRelatedResourcesAction(StructuralResourcesRelationEnum.CODELIST_WITH_DSD_PRIMARY_MEASURE_ENUMERATED_REPRESENTATION, firstResult, maxResults, new CodelistWebCriteria(
                criteria)), new WaitingAsyncCallback<GetRelatedResourcesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdPrimaryMeasureTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().codelistErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetRelatedResourcesResult result) {
                getView().setCodelistsForEnumeratedRepresentation(result);
            }
        });
    }
}
