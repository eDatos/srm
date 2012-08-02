package org.siemac.metamac.srm.web.dsd.presenter;

import java.util.ArrayList;
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
import org.siemac.metamac.srm.web.dsd.events.UpdateDimensionsEvent;
import org.siemac.metamac.srm.web.dsd.events.UpdateDimensionsEvent.UpdateDimensionsHandler;
import org.siemac.metamac.srm.web.dsd.events.UpdateDsdEvent;
import org.siemac.metamac.srm.web.dsd.events.UpdateGroupKeysEvent;
import org.siemac.metamac.srm.web.dsd.events.UpdateGroupKeysEvent.UpdateGroupKeysHandler;
import org.siemac.metamac.srm.web.dsd.model.record.AttributeRecord;
import org.siemac.metamac.srm.web.dsd.utils.CommonUtils;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdAttributesTabUiHandlers;
import org.siemac.metamac.srm.web.shared.FindCodeListsAction;
import org.siemac.metamac.srm.web.shared.FindCodeListsResult;
import org.siemac.metamac.srm.web.shared.FindConceptsAction;
import org.siemac.metamac.srm.web.shared.FindConceptsResult;
import org.siemac.metamac.srm.web.shared.dsd.DeleteAttributeListForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.DeleteAttributeListForDsdResult;
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
import org.siemac.metamac.web.common.client.events.UpdateConceptSchemesEvent;
import org.siemac.metamac.web.common.client.events.UpdateConceptSchemesEvent.UpdateConceptSchemesHandler;
import org.siemac.metamac.web.common.client.utils.UrnUtils;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataAttributeDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataStructureDefinitionDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DescriptorDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DimensionComponentDto;
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
import com.smartgwt.client.widgets.grid.events.HasSelectionChangedHandlers;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;

public class DsdAttributesTabPresenter extends Presenter<DsdAttributesTabPresenter.DsdAttributesTabView, DsdAttributesTabPresenter.DsdAttributesTabProxy>
        implements
            DsdAttributesTabUiHandlers,
            SelectDsdAndDescriptorsHandler,
            UpdateConceptSchemesHandler,
            UpdateDimensionsHandler,
            UpdateGroupKeysHandler {

    private final DispatchAsync        dispatcher;
    private final PlaceManager         placeManager;

    private DataStructureDefinitionDto dataStructureDefinitionDto;
    private boolean                    isNewDescriptor;
    private List<DataAttributeDto>     dataAttributeDtos;

    // Storing selected concept and representation type allows improving performance when loading code lists
    private String                     selectedConceptUri;
    private boolean                    enumeratedRepresentation;

    @ProxyCodeSplit
    @NameToken(NameTokens.dsdAttributesPage)
    public interface DsdAttributesTabProxy extends Proxy<DsdAttributesTabPresenter>, Place {

    }

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbAttributes();
    }

    public interface DsdAttributesTabView extends View, HasUiHandlers<DsdAttributesTabUiHandlers> {

        void setConcepts(List<ExternalItemDto> concepts);
        void setConceptSchemes(List<ExternalItemDto> conceptSchemes);
        void setRoleConcepts(List<ExternalItemDto> roleConcepts);
        HasChangeHandlers onConceptSchemeChange();
        HasChangeHandlers onConceptChange();
        HasChangeHandlers onRoleConceptSchemeChange();

        void setCodeLists(List<ExternalItemDto> codeLists);
        HasSelectionChangedHandlers onAttributeSelected();
        HasChangeHandlers onRepresentationTypeChange();

        void setDimensions(List<DimensionComponentDto> dimensionComponentDtos);
        void setGroupKeys(List<DescriptorDto> descriptorDtos);

        void setDsdAttributes(List<DataAttributeDto> dataAttributeDtos);
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
        SelectViewDsdDescriptorEvent.fire(this, TypeComponentList.ATTRIBUTE_DESCRIPTOR);
    }

    @Override
    protected void onReset() {
        super.onReset();
    }

    @ProxyEvent
    @Override
    public void onSelectDsdAndDescriptors(SelectDsdAndDescriptorsEvent event) {
        dataStructureDefinitionDto = event.getDataStructureDefinitionDto();
        DescriptorDto attributesDescriptor = event.getAttributes();
        isNewDescriptor = attributesDescriptor.getId() == null;
        dataAttributeDtos = CommonUtils.getAttributeComponents(attributesDescriptor);
        getView().setDimensions(CommonUtils.getDimensionComponents(event.getDimensions()));
        getView().setDsdAttributes(CommonUtils.getAttributeComponents(event.getAttributes()));
        getView().setGroupKeys(event.getGroupKeys());
    }

    @ProxyEvent
    @Override
    public void onUpdateConceptSchemes(UpdateConceptSchemesEvent event) {
        getView().setConceptSchemes(event.getConceptSchemes());
    }

    @ProxyEvent
    @Override
    public void onUpdateDimensions(final UpdateDimensionsEvent event) {
        // Update Attributes
        dataAttributeDtos = new ArrayList<DataAttributeDto>();
        dispatcher.execute(new FindDescriptorForDsdAction(dataStructureDefinitionDto.getId(), TypeComponentList.ATTRIBUTE_DESCRIPTOR), new WaitingAsyncCallback<FindDescriptorForDsdResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdAttributesTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(FindDescriptorForDsdResult result) {
                List<DescriptorDto> descriptorDtos = result.getDescriptorDtos();
                if (!descriptorDtos.isEmpty()) {
                    DescriptorDto descriptorDto = descriptorDtos.get(0);
                    if (descriptorDto != null) {
                        for (ComponentDto componentDto : descriptorDto.getComponents()) {
                            if (componentDto instanceof DataAttributeDto) {
                                DataAttributeDto dataAttributeDto = (DataAttributeDto) componentDto;
                                dataAttributeDtos.add(dataAttributeDto);
                            }
                        }
                    }
                    getView().setDsdAttributes(dataAttributeDtos);
                }
                // Update Dimensions
                getView().setDimensions(event.getDimensionComponentDtos());
            }
        });
    }

    @ProxyEvent
    @Override
    public void onUpdateGroupKeys(final UpdateGroupKeysEvent event) {
        // Update Attributes
        dataAttributeDtos = new ArrayList<DataAttributeDto>();
        dispatcher.execute(new FindDescriptorForDsdAction(dataStructureDefinitionDto.getId(), TypeComponentList.ATTRIBUTE_DESCRIPTOR), new WaitingAsyncCallback<FindDescriptorForDsdResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdAttributesTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(FindDescriptorForDsdResult result) {
                List<DescriptorDto> descriptorDtos = result.getDescriptorDtos();
                if (!descriptorDtos.isEmpty()) {
                    DescriptorDto descriptorDto = descriptorDtos.get(0);
                    if (descriptorDto != null) {
                        for (ComponentDto componentDto : descriptorDto.getComponents()) {
                            if (componentDto instanceof DataAttributeDto) {
                                DataAttributeDto dataAttributeDto = (DataAttributeDto) componentDto;
                                dataAttributeDtos.add(dataAttributeDto);
                            }
                        }
                    }
                    getView().setDsdAttributes(dataAttributeDtos);
                }
                // Update Group Keys
                getView().setGroupKeys(event.getGroupKeys());
            }
        });
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

        registerHandler(getView().onConceptSchemeChange().addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                if (event.getValue() != null) {
                    populateConcepts(event.getValue().toString());
                }
            }
        }));

        registerHandler(getView().onRoleConceptSchemeChange().addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                if (event.getValue() != null) {
                    populateRoleConcepts(event.getValue().toString());
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

        registerHandler(getView().onAttributeSelected().addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (event.getSelection() != null && event.getSelection().length == 1) {
                    AttributeRecord record = (AttributeRecord) event.getSelectedRecord();
                    if (record != null && record.getDataAttributeDto() != null) {
                        selectedConceptUri = record.getDataAttributeDto().getCptIdRef() != null ? record.getDataAttributeDto().getCptIdRef().getCode() : null;
                        enumeratedRepresentation = TypeRepresentationEnum.ENUMERATED.equals(record.getDataAttributeDto().getLocalRepresentation() != null ? record.getDataAttributeDto()
                                .getLocalRepresentation().getTypeRepresentationEnum() : "");
                    }
                }
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
            // Load DSD completely if it hasn't been loaded previously
            if (dataStructureDefinitionDto == null || !dsdIdentifier.equals(UrnUtils.removePrefix(dataStructureDefinitionDto.getUrn()))) {
                retrieveDsd(UrnUtils.generateUrn(UrnConstants.URN_SDMX_CLASS_DATASTRUCTURE_PREFIX, dsdIdentifier));
            }
        }
    }

    @Override
    public void saveAttribute(DataAttributeDto attribute) {
        dispatcher.execute(new SaveComponentForDsdAction(dataStructureDefinitionDto.getId(), attribute, TypeComponentList.ATTRIBUTE_DESCRIPTOR), new WaitingAsyncCallback<SaveComponentForDsdResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdAttributesTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdAttributeErrorSave()), MessageTypeEnum.ERROR);
            }

            @Override
            public void onWaitSuccess(SaveComponentForDsdResult result) {
                ShowMessageEvent.fire(DsdAttributesTabPresenter.this, ErrorUtils.getMessageList(MetamacSrmWeb.getMessages().dsdAttributeSaved()), MessageTypeEnum.SUCCESS);
                updateAttributeList(false);
                getView().onAttributeSaved((DataAttributeDto) result.getComponentDtoSaved());
                if (isNewDescriptor) {
                    // The first time a descriptor is saved, the DSD version changes.
                    isNewDescriptor = false;
                    updateDsd();
                }
            }
        });
    }

    @Override
    public void deleteAttributes(List<DataAttributeDto> attributesToDelete) {
        dispatcher.execute(new DeleteAttributeListForDsdAction(dataStructureDefinitionDto.getId(), attributesToDelete, TypeComponentList.ATTRIBUTE_DESCRIPTOR),
                new WaitingAsyncCallback<DeleteAttributeListForDsdResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        updateAttributeList(true);
                        ShowMessageEvent.fire(DsdAttributesTabPresenter.this, ErrorUtils.getMessageList(MetamacSrmWeb.getMessages().dsdAttributeErrorDelete()), MessageTypeEnum.ERROR);
                    }
                    @Override
                    public void onWaitSuccess(DeleteAttributeListForDsdResult result) {
                        updateAttributeList(true);
                        ShowMessageEvent.fire(DsdAttributesTabPresenter.this, ErrorUtils.getMessageList(MetamacSrmWeb.getMessages().dsdAttributeDeleted()), MessageTypeEnum.SUCCESS);
                    }
                });
    }

    @Override
    public void retrieveDsd(String urn) {
        dispatcher.execute(new GetDsdAndDescriptorsAction(urn), new WaitingAsyncCallback<GetDsdAndDescriptorsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdAttributesTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetDsdAndDescriptorsResult result) {
                SelectDsdAndDescriptorsEvent.fire(DsdAttributesTabPresenter.this, result.getDsd(), result.getPrimaryMeasure(), result.getDimensions(), result.getAttributes(), result.getGroupKeys());
            }
        });
    }

    private void updateAttributeList(final boolean updateView) {
        dataAttributeDtos = new ArrayList<DataAttributeDto>();
        dispatcher.execute(new FindDescriptorForDsdAction(dataStructureDefinitionDto.getId(), TypeComponentList.ATTRIBUTE_DESCRIPTOR), new WaitingAsyncCallback<FindDescriptorForDsdResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdAttributesTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(FindDescriptorForDsdResult result) {
                List<DescriptorDto> descriptorDtos = result.getDescriptorDtos();
                if (!descriptorDtos.isEmpty()) {
                    DescriptorDto descriptorDto = descriptorDtos.get(0);
                    if (descriptorDto != null) {
                        for (ComponentDto componentDto : descriptorDto.getComponents()) {
                            if (componentDto instanceof DataAttributeDto) {
                                DataAttributeDto dataAttributeDto = (DataAttributeDto) componentDto;
                                dataAttributeDtos.add(dataAttributeDto);
                            }
                        }
                    }
                    if (updateView) {
                        getView().setDsdAttributes(dataAttributeDtos);
                    }
                }
            }
        });
    }

    private void updateDsd() {
        dispatcher.execute(new GetDsdAction(dataStructureDefinitionDto.getUrn()), new WaitingAsyncCallback<GetDsdResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdAttributesTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetDsdResult result) {
                UpdateDsdEvent.fire(DsdAttributesTabPresenter.this, result.getDsd());
            }
        });
    }

    private void populateConcepts(String uriConceptScheme) {
        dispatcher.execute(new FindConceptsAction(uriConceptScheme), new WaitingAsyncCallback<FindConceptsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdAttributesTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().conceptErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(FindConceptsResult result) {
                getView().setConcepts(result.getConcepts());
            }
        });
    }

    private void populateRoleConcepts(String uriConceptScheme) {
        dispatcher.execute(new FindConceptsAction(uriConceptScheme), new WaitingAsyncCallback<FindConceptsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdAttributesTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().conceptErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(FindConceptsResult result) {
                getView().setRoleConcepts(result.getConcepts());
            }
        });
    }

    private void populateCodeLists(String uriConcept) {
        dispatcher.execute(new FindCodeListsAction(uriConcept), new WaitingAsyncCallback<FindCodeListsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdAttributesTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().codeListsErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(FindCodeListsResult result) {
                getView().setCodeLists(result.getCodeLists());
            }
        });
    }

}
