package org.siemac.metamac.srm.web.dsd.presenter;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.domain.srm.dto.DescriptorDto;
import org.siemac.metamac.domain.srm.dto.DimensionComponentDto;
import org.siemac.metamac.domain.srm.enume.domain.TypeComponentList;
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
import org.siemac.metamac.srm.web.dsd.utils.CommonUtils;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdGroupKeysTabUiHandlers;
import org.siemac.metamac.srm.web.shared.dsd.DeleteDescriptorListForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.DeleteDescriptorListForDsdResult;
import org.siemac.metamac.srm.web.shared.dsd.FindDescriptorForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.FindDescriptorForDsdResult;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdAndDescriptorsAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdAndDescriptorsResult;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdResult;
import org.siemac.metamac.srm.web.shared.dsd.SaveDescriptorForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.SaveDescriptorForDsdResult;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

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

public class DsdGroupKeysTabPresenter extends Presenter<DsdGroupKeysTabPresenter.DsdGroupKeysTabView, DsdGroupKeysTabPresenter.DsdGroupKeysTabProxy>
        implements
            DsdGroupKeysTabUiHandlers,
            SelectDsdAndDescriptorsHandler,
            UpdateDimensionsHandler {

    private final DispatchAsync         dispatcher;
    private final PlaceManager          placeManager;

    private Long                        idDsd;
    private List<DescriptorDto>         groupKeys;
    private List<DimensionComponentDto> dimensionComponentDtos;

    @ProxyCodeSplit
    @NameToken(NameTokens.dsdGroupKeysPage)
    public interface DsdGroupKeysTabProxy extends Proxy<DsdGroupKeysTabPresenter>, Place {

    }

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbGroupKeys();
    }

    public interface DsdGroupKeysTabView extends View, HasUiHandlers<DsdGroupKeysTabUiHandlers> {

        void setDsdGroupKeys(List<DimensionComponentDto> dimensionComponentDtos, List<DescriptorDto> descriptorDtos);
        void setGroupKeys(DescriptorDto descriptorDto);
        DescriptorDto getGroupKeys();
        List<DescriptorDto> getSelectedGroupKeys();
        boolean validate();
        HasClickHandlers getSave();
        HasClickHandlers getDelete();
        void onGroupKeysSaved(DescriptorDto descriptorDto);
    }

    @Inject
    public DsdGroupKeysTabPresenter(EventBus eventBus, DsdGroupKeysTabView dsdGroupKeysTabView, DsdGroupKeysTabProxy dsdGroupKeysTabProxy, DispatchAsync dispatcher, PlaceManager placeManager) {
        super(eventBus, dsdGroupKeysTabView, dsdGroupKeysTabProxy);
        this.dispatcher = dispatcher;
        this.placeManager = placeManager;
        getView().setUiHandlers(this);
    }

    @Override
    protected void onBind() {
        super.onBind();

        registerHandler(getView().getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (getView().validate()) {
                    saveGroupKeys(getView().getGroupKeys());
                }
            }
        }));

        registerHandler(getView().getDelete().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                List<DescriptorDto> descriptorToDelete = getView().getSelectedGroupKeys();
                deleteGroupKeys(descriptorToDelete);
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
        String id = PlaceRequestUtils.getDsdParamFromUrl(placeManager);
        if (id != null) {
            if (idDsd == null || (idDsd != null && !idDsd.equals(Long.valueOf(id)))) {
                idDsd = Long.valueOf(id);
                retrieveDsd(idDsd);
            }
        }
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        SelectViewDsdDescriptorEvent.fire(this, TypeComponentList.GROUP_DIMENSION_DESCRIPTOR);
    }

    @Override
    protected void onReset() {
        super.onReset();

    }

    @ProxyEvent
    @Override
    public void onSelectDsdAndDescriptors(SelectDsdAndDescriptorsEvent event) {
        idDsd = event.getDataStructureDefinitionDto().getId();
        dimensionComponentDtos = CommonUtils.getDimensionComponents(event.getDimensions());
        groupKeys = event.getGroupKeys();
        getView().setDsdGroupKeys(dimensionComponentDtos, groupKeys);
    }

    @ProxyEvent
    @Override
    public void onUpdateDimensions(final UpdateDimensionsEvent event) {
        // Update Group Keys
        groupKeys = new ArrayList<DescriptorDto>();
        dispatcher.execute(new FindDescriptorForDsdAction(idDsd, TypeComponentList.GROUP_DIMENSION_DESCRIPTOR), new WaitingAsyncCallback<FindDescriptorForDsdResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdGroupKeysTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(FindDescriptorForDsdResult result) {
                groupKeys = result.getDescriptorDtos();
                dimensionComponentDtos = event.getDimensionComponentDtos();
                getView().setDsdGroupKeys(dimensionComponentDtos, groupKeys);
                UpdateGroupKeysEvent.fire(DsdGroupKeysTabPresenter.this, groupKeys);
                // Update Dimensions
                getView().setDsdGroupKeys(event.getDimensionComponentDtos(), groupKeys);
            }
        });
    }

    @Override
    public void saveGroupKeys(DescriptorDto descriptorDto) {
        // Update DSD only if a new group keys descriptor is saved
        final boolean updateDsd = descriptorDto.getId() == null;
        dispatcher.execute(new SaveDescriptorForDsdAction(idDsd, descriptorDto), new WaitingAsyncCallback<SaveDescriptorForDsdResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdGroupKeysTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdGrouKeysErrorSave()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(SaveDescriptorForDsdResult result) {
                ShowMessageEvent.fire(DsdGroupKeysTabPresenter.this, ErrorUtils.getMessageList(MetamacSrmWeb.getMessages().dsdGroupKeysSaved()), MessageTypeEnum.SUCCESS);
                updateGroupKeysList(false); // Do no update the view!! The method onDimensionSaved updates the dimension list in the view
                getView().onGroupKeysSaved(result.getDescriptorDtoSaved());
                if (updateDsd) {
                    updateDsd(); // Update DSD every time the DSD descriptors are modified
                }
            }
        });
    }

    @Override
    public void deleteGroupKeys(List<DescriptorDto> descriptorsToDelete) {
        dispatcher.execute(new DeleteDescriptorListForDsdAction(idDsd, descriptorsToDelete), new WaitingAsyncCallback<DeleteDescriptorListForDsdResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                updateGroupKeysList(true);
                updateDsd(); // Update DSD every time the DSD descriptors are modified
                ShowMessageEvent.fire(DsdGroupKeysTabPresenter.this, ErrorUtils.getMessageList(MetamacSrmWeb.getMessages().dsdGroupKeysErrorDelete()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(DeleteDescriptorListForDsdResult result) {
                updateGroupKeysList(true);
                updateDsd(); // Update DSD every time the DSD descriptors are modified
                ShowMessageEvent.fire(DsdGroupKeysTabPresenter.this, ErrorUtils.getMessageList(MetamacSrmWeb.getMessages().dsdGroupKeysDeleted()), MessageTypeEnum.SUCCESS);
            }
        });
    }

    @Override
    public void retrieveDsd(Long id) {
        dispatcher.execute(new GetDsdAndDescriptorsAction(id), new WaitingAsyncCallback<GetDsdAndDescriptorsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdGroupKeysTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetDsdAndDescriptorsResult result) {
                SelectDsdAndDescriptorsEvent.fire(DsdGroupKeysTabPresenter.this, result.getDsd(), result.getPrimaryMeasure(), result.getDimensions(), result.getAttributes(), result.getGroupKeys());
            }
        });
    }

    private void updateGroupKeysList(final boolean updateView) {
        groupKeys = new ArrayList<DescriptorDto>();
        dispatcher.execute(new FindDescriptorForDsdAction(idDsd, TypeComponentList.GROUP_DIMENSION_DESCRIPTOR), new WaitingAsyncCallback<FindDescriptorForDsdResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdGroupKeysTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(FindDescriptorForDsdResult result) {
                groupKeys = result.getDescriptorDtos();
                if (updateView) {
                    getView().setDsdGroupKeys(dimensionComponentDtos, groupKeys);
                }
                UpdateGroupKeysEvent.fire(DsdGroupKeysTabPresenter.this, groupKeys);
            }
        });
    }

    private void updateDsd() {
        dispatcher.execute(new GetDsdAction(idDsd), new WaitingAsyncCallback<GetDsdResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdGroupKeysTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetDsdResult result) {
                UpdateDsdEvent.fire(DsdGroupKeysTabPresenter.this, result.getDsd());
            }
        });
    }

}
