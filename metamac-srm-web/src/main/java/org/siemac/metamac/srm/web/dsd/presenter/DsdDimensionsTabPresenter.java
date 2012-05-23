package org.siemac.metamac.srm.web.dsd.presenter;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemBtDto;
import org.siemac.metamac.domain.srm.dto.ComponentDto;
import org.siemac.metamac.domain.srm.dto.DescriptorDto;
import org.siemac.metamac.domain.srm.dto.DimensionComponentDto;
import org.siemac.metamac.domain.srm.enume.domain.TypeComponentList;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.dsd.enums.RepresentationTypeEnum;
import org.siemac.metamac.srm.web.dsd.events.SelectDsdAndDescriptorsEvent;
import org.siemac.metamac.srm.web.dsd.events.SelectDsdAndDescriptorsEvent.SelectDsdAndDescriptorsHandler;
import org.siemac.metamac.srm.web.dsd.events.SelectViewDsdDescriptorEvent;
import org.siemac.metamac.srm.web.dsd.events.UpdateDimensionsEvent;
import org.siemac.metamac.srm.web.dsd.events.UpdateDsdEvent;
import org.siemac.metamac.srm.web.dsd.model.record.DimensionRecord;
import org.siemac.metamac.srm.web.dsd.utils.CommonUtils;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdDimensionsTabUiHandlers;
import org.siemac.metamac.srm.web.shared.DeleteDimensionListForDsdAction;
import org.siemac.metamac.srm.web.shared.DeleteDimensionListForDsdResult;
import org.siemac.metamac.srm.web.shared.FindCodeListsAction;
import org.siemac.metamac.srm.web.shared.FindCodeListsResult;
import org.siemac.metamac.srm.web.shared.FindConceptsAction;
import org.siemac.metamac.srm.web.shared.FindConceptsResult;
import org.siemac.metamac.srm.web.shared.FindDescriptorForDsdAction;
import org.siemac.metamac.srm.web.shared.FindDescriptorForDsdResult;
import org.siemac.metamac.srm.web.shared.GetDsdAction;
import org.siemac.metamac.srm.web.shared.GetDsdAndDescriptorsAction;
import org.siemac.metamac.srm.web.shared.GetDsdAndDescriptorsResult;
import org.siemac.metamac.srm.web.shared.GetDsdResult;
import org.siemac.metamac.srm.web.shared.SaveComponentForDsdAction;
import org.siemac.metamac.srm.web.shared.SaveComponentForDsdResult;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.events.UpdateConceptSchemesEvent;
import org.siemac.metamac.web.common.client.events.UpdateConceptSchemesEvent.UpdateConceptSchemesHandler;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
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

public class DsdDimensionsTabPresenter extends Presenter<DsdDimensionsTabPresenter.DsdDimensionsTabView, DsdDimensionsTabPresenter.DsdDimensionsTabProxy>
        implements
            DsdDimensionsTabUiHandlers,
            SelectDsdAndDescriptorsHandler,
            UpdateConceptSchemesHandler {

    private final DispatchAsync         dispatcher;
    private final PlaceManager          placeManager;

    private Long                        idDsd;
    private boolean                     isNewDescriptor;
    private List<DimensionComponentDto> dimensionComponentDtos;

    // Storing selected concept and representation type allows improving performance when loading code lists
    private String                      selectedConceptUri;
    private boolean                     enumeratedRepresentation;

    @ProxyCodeSplit
    @NameToken(NameTokens.dsdDimensionsPage)
    public interface DsdDimensionsTabProxy extends Proxy<DsdDimensionsTabPresenter>, Place {

    }

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbDimensions();
    }

    public interface DsdDimensionsTabView extends View, HasUiHandlers<DsdDimensionsTabUiHandlers> {

        void setConceptSchemes(List<ExternalItemBtDto> conceptSchemes);
        void setConcepts(List<ExternalItemBtDto> concepts);
        void setRoleConcepts(List<ExternalItemBtDto> roleConcepts);
        HasChangeHandlers onConceptSchemeChange();
        HasChangeHandlers onConceptChange();
        HasChangeHandlers onRoleConceptSchemeChange();

        void setCodeLists(List<ExternalItemBtDto> codeLists);
        HasSelectionChangedHandlers onDimensionSelected();
        HasChangeHandlers onRepresentationTypeChange();

        void setDsdDimensions(List<DimensionComponentDto> dimensionComponentDtos);
        DimensionComponentDto getDsdDimension();
        List<DimensionComponentDto> getSelectedDimensions();
        void onDimensionSaved(DimensionComponentDto dimensionComponentDto);
        boolean validate();

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
        String id = PlaceRequestUtils.getDsdParamFromUrl(placeManager);
        if (id != null) {
            if (idDsd == null || (idDsd != null && !idDsd.equals(Long.valueOf(id)))) {
                idDsd = Long.valueOf(id);
                retrieveDsd(idDsd);
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

        registerHandler(getView().onDimensionSelected().addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (event.getSelection() != null && event.getSelection().length == 1) {
                    DimensionRecord record = (DimensionRecord) event.getSelectedRecord();
                    if (record != null && record.getDimensionComponentDto() != null) {
                        selectedConceptUri = record.getDimensionComponentDto().getCptIdRef() != null ? record.getDimensionComponentDto().getCptIdRef().getCodeId() : null;
                        enumeratedRepresentation = RepresentationTypeEnum.ENUMERATED.equals(record.getDimensionComponentDto().getLocalRepresentation() != null ? record.getDimensionComponentDto()
                                .getLocalRepresentation().getTypeRepresentationEnum() : "");
                    }
                }
            }
        }));
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        SelectViewDsdDescriptorEvent.fire(this, TypeComponentList.DIMENSION_DESCRIPTOR);
    }

    @Override
    protected void onReset() {
        super.onReset();
    }

    @ProxyEvent
    @Override
    public void onSelectDsdAndDescriptors(SelectDsdAndDescriptorsEvent event) {
        idDsd = event.getDataStructureDefinitionDto().getId();
        DescriptorDto dimensionsDescriptor = event.getDimensions();
        isNewDescriptor = dimensionsDescriptor.getId() == null;
        dimensionComponentDtos = CommonUtils.getDimensionComponents(dimensionsDescriptor);
        getView().setDsdDimensions(dimensionComponentDtos);
    }

    @ProxyEvent
    @Override
    public void onUpdateConceptSchemes(UpdateConceptSchemesEvent event) {
        getView().setConceptSchemes(event.getConceptSchemes());
    }

    @Override
    public void saveDimension(DimensionComponentDto dimensionToSave) {
        // if (dimensionToSave.getId() == null) {
        // if (TypeDimensionComponent.DIMENSION.equals(dimensionToSave.getTypeDimensionComponent())) {
        // dimensionToSave.setOrderLogic(getLastDimensionPosition());
        // }
        // }

        dispatcher.execute(new SaveComponentForDsdAction(idDsd, dimensionToSave, TypeComponentList.DIMENSION_DESCRIPTOR), new AsyncCallback<SaveComponentForDsdResult>() {

            @Override
            public void onFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdDimensionsTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdDimensionErrorSave()), MessageTypeEnum.ERROR);
            }

            @Override
            public void onSuccess(SaveComponentForDsdResult result) {
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
        dispatcher.execute(new DeleteDimensionListForDsdAction(idDsd, dimensionsToDelete, TypeComponentList.DIMENSION_DESCRIPTOR), new AsyncCallback<DeleteDimensionListForDsdResult>() {

            @Override
            public void onFailure(Throwable caught) {
                updateDimensionList(true);
                ShowMessageEvent.fire(DsdDimensionsTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdDimensionErrorDeleteDetails()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onSuccess(DeleteDimensionListForDsdResult result) {
                updateDimensionList(true);
                ShowMessageEvent.fire(DsdDimensionsTabPresenter.this, ErrorUtils.getMessageList(MetamacSrmWeb.getMessages().dsdDimensionDeleted()), MessageTypeEnum.SUCCESS);
            }
        });
    }

    @Override
    public void retrieveDsd(Long id) {
        dispatcher.execute(new GetDsdAndDescriptorsAction(id), new AsyncCallback<GetDsdAndDescriptorsResult>() {

            @Override
            public void onFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdDimensionsTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onSuccess(GetDsdAndDescriptorsResult result) {
                SelectDsdAndDescriptorsEvent.fire(DsdDimensionsTabPresenter.this, result.getDsd(), result.getPrimaryMeasure(), result.getDimensions(), result.getAttributes(), result.getGroupKeys());
            }
        });
    }

    private void updateDimensionList(final boolean updateView) {
        dimensionComponentDtos = new ArrayList<DimensionComponentDto>();
        dispatcher.execute(new FindDescriptorForDsdAction(idDsd, TypeComponentList.DIMENSION_DESCRIPTOR), new AsyncCallback<FindDescriptorForDsdResult>() {

            @Override
            public void onFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdDimensionsTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onSuccess(FindDescriptorForDsdResult result) {
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
                    getView().setDsdDimensions(dimensionComponentDtos);
                }
                UpdateDimensionsEvent.fire(DsdDimensionsTabPresenter.this, dimensionComponentDtos);
            }
        });
    }

    // private Integer getLastDimensionPosition() {
    // Integer position = 0;
    // for (DimensionComponentDto d : dimensionComponentDtos) {
    // if (position.compareTo(d.getOrderLogic()) < 0) {
    // position = d.getOrderLogic();
    // }
    // }
    // return position + 1;
    // }

    private void updateDsd() {
        dispatcher.execute(new GetDsdAction(idDsd), new AsyncCallback<GetDsdResult>() {

            @Override
            public void onFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdDimensionsTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onSuccess(GetDsdResult result) {
                UpdateDsdEvent.fire(DsdDimensionsTabPresenter.this, result.getDsd());
            }
        });
    }

    private void populateConcepts(String uriConceptScheme) {
        dispatcher.execute(new FindConceptsAction(uriConceptScheme), new AsyncCallback<FindConceptsResult>() {

            @Override
            public void onFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdDimensionsTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().conceptErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onSuccess(FindConceptsResult result) {
                getView().setConcepts(result.getConcepts());
            }
        });
    }

    private void populateRoleConcepts(String uriConceptScheme) {
        dispatcher.execute(new FindConceptsAction(uriConceptScheme), new AsyncCallback<FindConceptsResult>() {

            @Override
            public void onFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdDimensionsTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().conceptErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onSuccess(FindConceptsResult result) {
                getView().setRoleConcepts(result.getConcepts());
            }
        });
    }

    private void populateCodeLists(String uriConcept) {
        dispatcher.execute(new FindCodeListsAction(uriConcept), new AsyncCallback<FindCodeListsResult>() {

            @Override
            public void onFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdDimensionsTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().codeListsErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onSuccess(FindCodeListsResult result) {
                getView().setCodeLists(result.getCodeLists());
            }
        });
    }

}
