package org.siemac.metamac.srm.web.dsd.presenter;

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
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdPrimaryMeasureTabUiHandlers;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesAction;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.srm.web.shared.StructuralResourcesRelationEnum;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptSchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptWebCriteria;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdAndDescriptorsAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdAndDescriptorsResult;
import org.siemac.metamac.srm.web.shared.dsd.SaveComponentForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.SaveComponentForDsdResult;
import org.siemac.metamac.web.common.client.utils.WaitingAsyncCallbackHandlingError;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DescriptorDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeComponent;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeComponentList;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
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

public class DsdPrimaryMeasureTabPresenter extends Presenter<DsdPrimaryMeasureTabPresenter.DsdPrimaryMeasureTabView, DsdPrimaryMeasureTabPresenter.DsdPrimaryMeasureTabProxy>
        implements
            DsdPrimaryMeasureTabUiHandlers {

    private final DispatchAsync               dispatcher;
    private final PlaceManager                placeManager;

    private DataStructureDefinitionMetamacDto dataStructureDefinitionDto;
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
            retrievePrimaryMeasure(UrnUtils.generateUrn(UrnConstants.URN_SDMX_CLASS_DATASTRUCTURE_PREFIX, dsdIdentifier));
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

    @Override
    public void savePrimaryMeasure(ComponentDto component) {
        dispatcher.execute(new SaveComponentForDsdAction(dataStructureDefinitionDto.getUrn(), component, TypeComponentList.MEASURE_DESCRIPTOR),
                new WaitingAsyncCallbackHandlingError<SaveComponentForDsdResult>(this) {

                    @Override
                    public void onWaitSuccess(SaveComponentForDsdResult result) {
                        fireSuccessMessage(MetamacSrmWeb.getMessages().dsdPrimaryMeasureSaved());
                        primaryMeasure = result.getComponentDtoSaved();
                        dataStructureDefinitionDto = result.getDataStructureDefinitionMetamacDto();
                        getView().onPrimaryMeasureSaved(dataStructureDefinitionDto, primaryMeasure);
                    }
                });
    }
    public void retrievePrimaryMeasure(String urn) {
        Set<TypeComponentList> descriptorsToRetrieve = new HashSet<TypeComponentList>();
        descriptorsToRetrieve.add(TypeComponentList.MEASURE_DESCRIPTOR);
        dispatcher.execute(new GetDsdAndDescriptorsAction(urn, descriptorsToRetrieve), new WaitingAsyncCallbackHandlingError<GetDsdAndDescriptorsResult>(this) {

            @Override
            public void onWaitSuccess(GetDsdAndDescriptorsResult result) {
                dataStructureDefinitionDto = result.getDsd();
                DescriptorDto primaryMeasureDescriptor = result.getPrimaryMeasure();
                primaryMeasure = primaryMeasureDescriptor.getComponents().isEmpty() ? new ComponentDto(TypeComponent.PRIMARY_MEASURE, null) : primaryMeasureDescriptor.getComponents().iterator()
                        .next();
                getView().setDsdPrimaryMeasure(dataStructureDefinitionDto, primaryMeasure);
            }
        });
    }

    @Override
    public void retrieveConceptSchemes(int firstResult, int maxResults, ConceptSchemeWebCriteria conceptSchemeWebCriteria) {
        dispatcher.execute(new GetRelatedResourcesAction(StructuralResourcesRelationEnum.CONCEPT_SCHEMES_WITH_DSD_PRIMARY_MEASURE, firstResult, maxResults, conceptSchemeWebCriteria),
                new WaitingAsyncCallbackHandlingError<GetRelatedResourcesResult>(this) {

                    @Override
                    public void onWaitSuccess(GetRelatedResourcesResult result) {
                        getView().setConceptSchemes(result);
                    }
                });
    }

    @Override
    public void retrieveConcepts(int firstResult, int maxResults, ConceptWebCriteria conceptWebCriteria) {
        dispatcher.execute(new GetRelatedResourcesAction(StructuralResourcesRelationEnum.CONCEPT_WITH_DSD_PRIMARY_MEASURE, firstResult, maxResults, conceptWebCriteria),
                new WaitingAsyncCallbackHandlingError<GetRelatedResourcesResult>(this) {

                    @Override
                    public void onWaitSuccess(GetRelatedResourcesResult result) {
                        getView().setConcepts(result);
                    }
                });
    }

    @Override
    public void retrieveCodelistsForEnumeratedRepresentation(int firstResult, int maxResults, CodelistWebCriteria codelistWebCriteria) {
        dispatcher.execute(new GetRelatedResourcesAction(StructuralResourcesRelationEnum.CODELIST_WITH_DSD_PRIMARY_MEASURE_ENUMERATED_REPRESENTATION, firstResult, maxResults, codelistWebCriteria),
                new WaitingAsyncCallbackHandlingError<GetRelatedResourcesResult>(this) {

                    @Override
                    public void onWaitSuccess(GetRelatedResourcesResult result) {
                        getView().setCodelistsForEnumeratedRepresentation(result);
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
