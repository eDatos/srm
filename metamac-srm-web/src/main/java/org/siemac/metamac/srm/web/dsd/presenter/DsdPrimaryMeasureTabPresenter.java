package org.siemac.metamac.srm.web.dsd.presenter;

import java.util.List;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.dsd.events.SelectDsdAndDescriptorsEvent;
import org.siemac.metamac.srm.web.dsd.events.SelectDsdAndDescriptorsEvent.SelectDsdAndDescriptorsHandler;
import org.siemac.metamac.srm.web.dsd.events.SelectViewDsdDescriptorEvent;
import org.siemac.metamac.srm.web.dsd.events.UpdateDsdEvent;
import org.siemac.metamac.srm.web.dsd.utils.CommonUtils;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdPrimaryMeasureTabUiHandlers;
import org.siemac.metamac.srm.web.shared.FindCodeListsAction;
import org.siemac.metamac.srm.web.shared.FindCodeListsResult;
import org.siemac.metamac.srm.web.shared.FindConceptsAction;
import org.siemac.metamac.srm.web.shared.FindConceptsResult;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdAndDescriptorsAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdAndDescriptorsResult;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdResult;
import org.siemac.metamac.srm.web.shared.dsd.SaveComponentForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.SaveComponentForDsdResult;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.events.UpdateConceptSchemesEvent;
import org.siemac.metamac.web.common.client.events.UpdateConceptSchemesEvent.UpdateConceptSchemesHandler;
import org.siemac.metamac.web.common.client.utils.UrnUtils;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataStructureDefinitionDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DescriptorDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeComponent;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeComponentList;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeRepresentationEnum;
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
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.HasClickHandlers;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.form.fields.events.HasChangeHandlers;

public class DsdPrimaryMeasureTabPresenter extends Presenter<DsdPrimaryMeasureTabPresenter.DsdPrimaryMeasureTabView, DsdPrimaryMeasureTabPresenter.DsdPrimaryMeasureTabProxy>
        implements
            DsdPrimaryMeasureTabUiHandlers,
            SelectDsdAndDescriptorsHandler,
            UpdateConceptSchemesHandler {

    private final DispatchAsync        dispatcher;
    private final PlaceManager         placeManager;

    private DataStructureDefinitionDto dataStructureDefinitionDto;
    private boolean                    isNewDescriptor;
    private ComponentDto               primaryMeasure;

    // Storing selected concept and representation type allows improving performance when loading code lists
    private String                     selectedConceptUri;
    private boolean                    enumeratedRepresentation;

    @ProxyCodeSplit
    @NameToken(NameTokens.dsdPrimaryMeasurePage)
    public interface DsdPrimaryMeasureTabProxy extends Proxy<DsdPrimaryMeasureTabPresenter>, Place {

    }

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbPrimaryMeasure();
    }

    public interface DsdPrimaryMeasureTabView extends View, HasUiHandlers<DsdPrimaryMeasureTabUiHandlers> {

        void setConceptSchemes(List<ExternalItemDto> concepts);
        void setConcepts(List<ExternalItemDto> concepts);
        HasChangeHandlers onConceptSchemeChange();
        HasChangeHandlers onConceptChange();

        void setCodeLists(List<ExternalItemDto> codeLists);
        HasChangeHandlers onRepresentationTypeChange();

        void setDsdPrimaryMeasure(ComponentDto componentDto);
        ComponentDto getDsdPrimaryMeasure(ComponentDto componentDto);
        void onPrimaryMeasureSaved(ComponentDto componentDto);
        boolean validate();

        HasClickHandlers getSave();

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
    protected void onBind() {
        super.onBind();

        registerHandler(getView().getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (getView().validate()) {
                    savePrimaryMeasure(getView().getDsdPrimaryMeasure(primaryMeasure));
                }
            }
        }));

        registerHandler(getView().onConceptSchemeChange().addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                if (event.getValue() != null) {
                    populateConcepts(event.getValue().toString());
                }
            }
        }));

        registerHandler(getView().onConceptChange().addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                selectedConceptUri = event.getValue() != null ? (String) event.getValue() : null;
                // if selected representation is enumerated, load the appropriate code lists
                if (enumeratedRepresentation) {
                    populateCodeLists(selectedConceptUri);
                }
            }
        }));

        registerHandler(getView().onRepresentationTypeChange().addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                enumeratedRepresentation = CommonUtils.isRepresentationTypeEnumerated(event.getValue() != null ? (String) event.getValue() : "");
                if (enumeratedRepresentation) {
                    populateCodeLists(selectedConceptUri);
                }
            }
        }));
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        SelectViewDsdDescriptorEvent.fire(this, TypeComponentList.MEASURE_DESCRIPTOR);
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
        enumeratedRepresentation = TypeRepresentationEnum.ENUMERATED.equals(primaryMeasure.getLocalRepresentation() != null
                ? primaryMeasure.getLocalRepresentation().getTypeRepresentationEnum()
                : false);
        getView().setDsdPrimaryMeasure(primaryMeasure);
    }

    @ProxyEvent
    @Override
    public void onUpdateConceptSchemes(UpdateConceptSchemesEvent event) {
        getView().setConceptSchemes(event.getConceptSchemes());
    }

    @Override
    public void savePrimaryMeasure(ComponentDto component) {
        dispatcher.execute(new SaveComponentForDsdAction(dataStructureDefinitionDto.getId(), component, TypeComponentList.MEASURE_DESCRIPTOR), new WaitingAsyncCallback<SaveComponentForDsdResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdPrimaryMeasureTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdPrimaryMeasureErrorSave()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(SaveComponentForDsdResult result) {
                ShowMessageEvent.fire(DsdPrimaryMeasureTabPresenter.this, ErrorUtils.getMessageList(MetamacSrmWeb.getMessages().dsdPrimaryMeasureSaved()), MessageTypeEnum.SUCCESS);
                primaryMeasure = result.getComponentDtoSaved();
                getView().onPrimaryMeasureSaved(primaryMeasure);
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

    @Override
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

    private void populateConcepts(String uriConceptScheme) {
        dispatcher.execute(new FindConceptsAction(uriConceptScheme), new WaitingAsyncCallback<FindConceptsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdPrimaryMeasureTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().conceptErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(FindConceptsResult result) {
                getView().setConcepts(result.getConcepts());
            }
        });
    }

    private void populateCodeLists(String uriConcept) {
        dispatcher.execute(new FindCodeListsAction(uriConcept), new WaitingAsyncCallback<FindCodeListsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdPrimaryMeasureTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().codeListsErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(FindCodeListsResult result) {
                getView().setCodeLists(result.getCodeLists());
            }
        });
    }
}
