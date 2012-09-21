package org.siemac.metamac.srm.web.dsd.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.PlaceRequestParams;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.dsd.events.SelectDsdAndDescriptorsEvent;
import org.siemac.metamac.srm.web.dsd.events.SelectDsdAndDescriptorsEvent.SelectDsdAndDescriptorsHandler;
import org.siemac.metamac.srm.web.dsd.events.SelectViewDsdDescriptorEvent;
import org.siemac.metamac.srm.web.dsd.events.UpdateDsdEvent;
import org.siemac.metamac.srm.web.dsd.events.UpdateDsdEvent.UpdateDsdHandler;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdGeneralTabUiHandlers;
import org.siemac.metamac.srm.web.shared.dsd.CancelDsdValidityAction;
import org.siemac.metamac.srm.web.shared.dsd.CancelDsdValidityResult;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdAndDescriptorsAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdAndDescriptorsResult;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdVersionsAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdVersionsResult;
import org.siemac.metamac.srm.web.shared.dsd.SaveDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.SaveDsdResult;
import org.siemac.metamac.srm.web.shared.dsd.UpdateDsdProcStatusAction;
import org.siemac.metamac.srm.web.shared.dsd.UpdateDsdProcStatusResult;
import org.siemac.metamac.srm.web.shared.dsd.VersionDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.VersionDsdResult;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.UrnUtils;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.VersionTypeEnum;
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
import com.smartgwt.client.widgets.events.HasClickHandlers;

public class DsdGeneralTabPresenter extends Presenter<DsdGeneralTabPresenter.DsdGeneralTabView, DsdGeneralTabPresenter.DsdGeneralTabProxy>
        implements
            DsdGeneralTabUiHandlers,
            SelectDsdAndDescriptorsHandler,
            UpdateDsdHandler {

    private final DispatchAsync               dispatcher;
    private final PlaceManager                placeManager;

    private DataStructureDefinitionMetamacDto dsd;

    @ProxyCodeSplit
    @NameToken(NameTokens.dsdGeneralPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface DsdGeneralTabProxy extends Proxy<DsdGeneralTabPresenter>, Place {

    }

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbGeneral();
    }

    public interface DsdGeneralTabView extends View, HasUiHandlers<DsdGeneralTabUiHandlers> {

        void setDsd(DataStructureDefinitionMetamacDto dataStructureDefinitionDto);
        void setDsdVersions(List<DataStructureDefinitionMetamacDto> dataStructureDefinitionMetamacDtos);
        DataStructureDefinitionMetamacDto getDataStructureDefinitionDto();
        HasClickHandlers getSave();
        void onDsdSaved(DataStructureDefinitionMetamacDto dsd);
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
    public void saveDsd(DataStructureDefinitionMetamacDto dataStructureDefinitionDto) {
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
                retrieveDsdVersions(dsd.getUrn());
            }
        });
    }

    @Override
    public void sendToProductionValidation(final String urn, ProcStatusEnum currentProcStatus) {
        dispatcher.execute(new UpdateDsdProcStatusAction(urn, ProcStatusEnum.PRODUCTION_VALIDATION, currentProcStatus), new WaitingAsyncCallback<UpdateDsdProcStatusResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorSendingToProductionValidation()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(UpdateDsdProcStatusResult result) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getMessageList(MetamacSrmWeb.getMessages().dsdSentToProductionValidation()), MessageTypeEnum.SUCCESS);
                retrieveDsd(urn);
            }
        });
    }

    @Override
    public void sendToDiffusionValidation(final String urn, ProcStatusEnum currentProcStatus) {
        dispatcher.execute(new UpdateDsdProcStatusAction(urn, ProcStatusEnum.DIFFUSION_VALIDATION, currentProcStatus), new WaitingAsyncCallback<UpdateDsdProcStatusResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorSendingToDiffusionValidation()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(UpdateDsdProcStatusResult result) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getMessageList(MetamacSrmWeb.getMessages().dsdSentToDiffusionValidation()), MessageTypeEnum.SUCCESS);
                retrieveDsd(urn);
            }
        });
    }

    @Override
    public void rejectValidation(final String urn, ProcStatusEnum currentProcStatus) {
        dispatcher.execute(new UpdateDsdProcStatusAction(urn, ProcStatusEnum.VALIDATION_REJECTED, currentProcStatus), new WaitingAsyncCallback<UpdateDsdProcStatusResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorRejecting()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(UpdateDsdProcStatusResult result) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getMessageList(MetamacSrmWeb.getMessages().dsdRejected()), MessageTypeEnum.SUCCESS);
                retrieveDsd(urn);
            }
        });
    }

    @Override
    public void publishInternally(final String urn, ProcStatusEnum currentProcStatus) {
        dispatcher.execute(new UpdateDsdProcStatusAction(urn, ProcStatusEnum.INTERNALLY_PUBLISHED, currentProcStatus), new WaitingAsyncCallback<UpdateDsdProcStatusResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorPublishingInternally()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(UpdateDsdProcStatusResult result) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getMessageList(MetamacSrmWeb.getMessages().dsdPublishedInternally()), MessageTypeEnum.SUCCESS);
                retrieveDsd(urn);
            }
        });
    }

    @Override
    public void publishExternally(final String urn, ProcStatusEnum currentProcStatus) {
        dispatcher.execute(new UpdateDsdProcStatusAction(urn, ProcStatusEnum.EXTERNALLY_PUBLISHED, currentProcStatus), new WaitingAsyncCallback<UpdateDsdProcStatusResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorPublishingExternally()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(UpdateDsdProcStatusResult result) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getMessageList(MetamacSrmWeb.getMessages().dsdPublishedExternally()), MessageTypeEnum.SUCCESS);
                retrieveDsd(urn);
            }
        });
    }

    @Override
    public void versioning(String urn, VersionTypeEnum versionType) {
        dispatcher.execute(new VersionDsdAction(urn, versionType), new WaitingAsyncCallback<VersionDsdResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorVersioning()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(VersionDsdResult result) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getMessageList(MetamacSrmWeb.getMessages().dsdVersioned()), MessageTypeEnum.SUCCESS);
                goToDsd(result.getDataStructureDefinitionMetamacDto().getUrn());
            }
        });
    }

    @Override
    public void retrieveDsdVersions(String dsdUrn) {
        dispatcher.execute(new GetDsdVersionsAction(dsdUrn), new WaitingAsyncCallback<GetDsdVersionsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorRetrievingVersions()), MessageTypeEnum.ERROR);
            }

            @Override
            public void onWaitSuccess(GetDsdVersionsResult result) {
                getView().setDsdVersions(result.getDataStructureDefinitionMetamacDtos());
            }
        });
    }

    @Override
    public void goToDsd(String urn) {
        if (!StringUtils.isBlank(urn)) {
            placeManager.revealRelativePlace(new PlaceRequest(NameTokens.dsdPage).with(PlaceRequestParams.dsdParam, UrnUtils.removePrefix(urn)), -2);
        }
    }

    @Override
    public void cancelValidity(final String urn) {
        List<String> urns = new ArrayList<String>();
        urns.add(urn);
        dispatcher.execute(new CancelDsdValidityAction(urns), new WaitingAsyncCallback<CancelDsdValidityResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().dsdErrorCancelValidity()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(CancelDsdValidityResult result) {
                ShowMessageEvent.fire(DsdGeneralTabPresenter.this, ErrorUtils.getMessageList(getMessages().dsdCanceledValidity()), MessageTypeEnum.SUCCESS);
                retrieveDsd(urn);
            }
        });
    }

}
