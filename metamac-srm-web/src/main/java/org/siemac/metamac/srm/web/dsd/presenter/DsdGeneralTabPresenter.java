package org.siemac.metamac.srm.web.dsd.presenter;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.dsd.events.SelectDsdAndDescriptorsEvent;
import org.siemac.metamac.srm.web.dsd.events.SelectDsdAndDescriptorsEvent.SelectDsdAndDescriptorsHandler;
import org.siemac.metamac.srm.web.dsd.events.SelectViewDsdDescriptorEvent;
import org.siemac.metamac.srm.web.dsd.events.UpdateDsdEvent;
import org.siemac.metamac.srm.web.dsd.events.UpdateDsdEvent.UpdateDsdHandler;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdGeneralTabUiHandlers;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdAndDescriptorsAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdAndDescriptorsResult;
import org.siemac.metamac.srm.web.shared.dsd.SaveDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.SaveDsdResult;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.UrnUtils;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataStructureDefinitionDto;
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
import com.smartgwt.client.widgets.events.HasClickHandlers;

public class DsdGeneralTabPresenter extends Presenter<DsdGeneralTabPresenter.DsdGeneralTabView, DsdGeneralTabPresenter.DsdGeneralTabProxy>
        implements
            DsdGeneralTabUiHandlers,
            SelectDsdAndDescriptorsHandler,
            UpdateDsdHandler {

    private final DispatchAsync        dispatcher;
    private final PlaceManager         placeManager;

    private DataStructureDefinitionDto dsd;

    @ProxyCodeSplit
    @NameToken(NameTokens.dsdGeneralPage)
    public interface DsdGeneralTabProxy extends Proxy<DsdGeneralTabPresenter>, Place {

    }

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbGeneral();
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
        String dsdIdentifier = PlaceRequestUtils.getDsdParamFromUrl(placeManager);// DSD identifier is the URN without the prefix
        if (!StringUtils.isBlank(dsdIdentifier)) {
            // Load DSD completely if it hasn't been loaded previously
            if (dsd == null || !dsdIdentifier.equals(UrnUtils.removePrefix(dsd.getUrn()))) {
                retrieveDsd(UrnUtils.generateUrn(UrnConstants.URN_SDMX_CLASS_DATASTRUCTURE_PREFIX, dsdIdentifier));
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
        dsd = event.getDataStructureDefinitionDto();
        getView().setDsd(dsd);
    }

    @ProxyEvent
    @Override
    public void onUpdateDsd(UpdateDsdEvent event) {
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
        dispatcher.execute(new SaveDsdAction(dataStructureDefinitionDto), new WaitingAsyncCallback<SaveDsdResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorSave()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(SaveDsdResult result) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getMessageList(MetamacSrmWeb.getMessages().dsdSaved()), MessageTypeEnum.SUCCESS);
                dsd = result.getDsdSaved();
                getView().onDsdSaved(dsd);
                UpdateDsdEvent.fire(DsdGeneralTabPresenter.this, dsd);
            }
        });
    }

    @Override
    public void retrieveDsd(String urn) {
        dispatcher.execute(new GetDsdAndDescriptorsAction(urn), new WaitingAsyncCallback<GetDsdAndDescriptorsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetDsdAndDescriptorsResult result) {
                dsd = result.getDsd();
                SelectDsdAndDescriptorsEvent.fire(DsdGeneralTabPresenter.this, dsd, result.getPrimaryMeasure(), result.getDimensions(), result.getAttributes(), result.getGroupKeys());
            }
        });
    }

}
