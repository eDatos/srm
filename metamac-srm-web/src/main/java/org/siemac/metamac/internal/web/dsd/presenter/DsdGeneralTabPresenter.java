package org.siemac.metamac.internal.web.dsd.presenter;

import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionDto;
import org.siemac.metamac.internal.web.client.MetamacInternalWeb;
import org.siemac.metamac.internal.web.client.NameTokens;
import org.siemac.metamac.internal.web.client.utils.ErrorUtils;
import org.siemac.metamac.internal.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.internal.web.dsd.events.SelectDsdAndDescriptorsEvent;
import org.siemac.metamac.internal.web.dsd.events.SelectDsdAndDescriptorsEvent.SelectDsdAndDescriptorsHandler;
import org.siemac.metamac.internal.web.dsd.events.SelectViewDsdDescriptorEvent;
import org.siemac.metamac.internal.web.dsd.events.UpdateDsdEvent;
import org.siemac.metamac.internal.web.dsd.events.UpdateDsdEvent.UpdateDsdHandler;
import org.siemac.metamac.internal.web.dsd.view.handlers.DsdGeneralTabUiHandlers;
import org.siemac.metamac.internal.web.shared.GetDsdAndDescriptorsAction;
import org.siemac.metamac.internal.web.shared.GetDsdAndDescriptorsResult;
import org.siemac.metamac.internal.web.shared.SaveDsdAction;
import org.siemac.metamac.internal.web.shared.SaveDsdResult;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;

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
import com.smartgwt.client.widgets.events.HasClickHandlers;

public class DsdGeneralTabPresenter extends Presenter<DsdGeneralTabPresenter.DsdGeneralTabView, DsdGeneralTabPresenter.DsdGeneralTabProxy>
        implements
            DsdGeneralTabUiHandlers,
            SelectDsdAndDescriptorsHandler,
            UpdateDsdHandler {

    private final DispatchAsync        dispatcher;
    private final PlaceManager         placeManager;

    private Long                       idDsd;
    private DataStructureDefinitionDto dsd;

    @ProxyCodeSplit
    @NameToken(NameTokens.dsdGeneralPage)
    public interface DsdGeneralTabProxy extends Proxy<DsdGeneralTabPresenter>, Place {

    }

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacInternalWeb.getConstants().breadcrumbGeneral();
    }

    public interface DsdGeneralTabView extends View, HasUiHandlers<DsdGeneralTabUiHandlers> {

        void setDsd(DataStructureDefinitionDto dataStructureDefinitionDto);
        DataStructureDefinitionDto getDataStructureDefinitionDto(DataStructureDefinitionDto dsd);
        boolean validate();
        HasClickHandlers getSave();
        void onDsdSaved(DataStructureDefinitionDto dsd);
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
        SelectViewDsdDescriptorEvent.fire(this, null);
    }

    @Override
    protected void onReset() {
        super.onReset();
    }

    @ProxyEvent
    @Override
    public void onSelectDsdAndDescriptors(SelectDsdAndDescriptorsEvent event) {
        idDsd = event.getDataStructureDefinitionDto().getId();
        dsd = event.getDataStructureDefinitionDto();
        getView().setDsd(dsd);
    }

    @ProxyEvent
    @Override
    public void onUpdateDsd(UpdateDsdEvent event) {
        idDsd = event.getDataStructureDefinitionDto().getId();
        dsd = event.getDataStructureDefinitionDto();
        getView().setDsd(dsd);
    }

    @Override
    protected void onBind() {
        super.onBind();

        registerHandler(getView().getSave().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                if (getView().validate()) {
                    saveDsd(getView().getDataStructureDefinitionDto(dsd));
                }
            }
        }));
    }

    @Override
    public void saveDsd(DataStructureDefinitionDto dataStructureDefinitionDto) {
        dispatcher.execute(new SaveDsdAction(dataStructureDefinitionDto), new AsyncCallback<SaveDsdResult>() {

            @Override
            public void onFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacInternalWeb.getMessages().dsdErrorSave()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onSuccess(SaveDsdResult result) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getMessageList(MetamacInternalWeb.getMessages().dsdSaved()), MessageTypeEnum.SUCCESS);
                dsd = result.getDsdSaved();
                getView().onDsdSaved(dsd);
                UpdateDsdEvent.fire(DsdGeneralTabPresenter.this, dsd);
            }
        });
    }

    @Override
    public void retrieveDsd(Long id) {
        dispatcher.execute(new GetDsdAndDescriptorsAction(id), new AsyncCallback<GetDsdAndDescriptorsResult>() {

            @Override
            public void onFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacInternalWeb.getMessages().dsdErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onSuccess(GetDsdAndDescriptorsResult result) {
                dsd = result.getDsd();
                SelectDsdAndDescriptorsEvent.fire(DsdGeneralTabPresenter.this, dsd, result.getPrimaryMeasure(), result.getDimensions(), result.getAttributes(), result.getGroupKeys());
            }
        });
    }

}
