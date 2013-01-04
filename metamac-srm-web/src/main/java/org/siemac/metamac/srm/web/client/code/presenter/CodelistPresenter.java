package org.siemac.metamac.srm.web.client.code.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.code.view.handlers.CodelistUiHandlers;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.shared.code.CancelCodelistValidityAction;
import org.siemac.metamac.srm.web.shared.code.CancelCodelistValidityResult;
import org.siemac.metamac.srm.web.shared.code.DeleteCodeAction;
import org.siemac.metamac.srm.web.shared.code.DeleteCodeResult;
import org.siemac.metamac.srm.web.shared.code.GetCodelistAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistResult;
import org.siemac.metamac.srm.web.shared.code.GetCodelistVersionsAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistVersionsResult;
import org.siemac.metamac.srm.web.shared.code.GetCodesByCodelistAction;
import org.siemac.metamac.srm.web.shared.code.GetCodesByCodelistResult;
import org.siemac.metamac.srm.web.shared.code.SaveCodeAction;
import org.siemac.metamac.srm.web.shared.code.SaveCodeResult;
import org.siemac.metamac.srm.web.shared.code.SaveCodelistAction;
import org.siemac.metamac.srm.web.shared.code.SaveCodelistResult;
import org.siemac.metamac.srm.web.shared.code.UpdateCodelistProcStatusAction;
import org.siemac.metamac.srm.web.shared.code.UpdateCodelistProcStatusResult;
import org.siemac.metamac.srm.web.shared.code.VersionCodelistAction;
import org.siemac.metamac.srm.web.shared.code.VersionCodelistResult;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.UrnUtils;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TitleFunction;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

public class CodelistPresenter extends Presenter<CodelistPresenter.CodelistView, CodelistPresenter.CodelistProxy> implements CodelistUiHandlers {

    private final DispatchAsync dispatcher;
    private final PlaceManager  placeManager;

    private CodelistMetamacDto  codelistMetamacDto;

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbCodelist();
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.codelistPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface CodelistProxy extends Proxy<CodelistPresenter>, Place {
    }

    public interface CodelistView extends View, HasUiHandlers<CodelistUiHandlers> {

        void setCodelist(CodelistMetamacDto codelistMetamacDto);
        void setCodelistVersions(List<CodelistMetamacDto> codelistMetamacDtos);
        void setCodes(List<ItemHierarchyDto> codeDtos);
    }

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaContentCodelist = new Type<RevealContentHandler<?>>();

    @Inject
    public CodelistPresenter(EventBus eventBus, CodelistView view, CodelistProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager) {
        super(eventBus, view, proxy);
        this.placeManager = placeManager;
        this.dispatcher = dispatcher;
        getView().setUiHandlers(this);
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        SetTitleEvent.fire(this, getConstants().codelist());
        SelectMenuButtonEvent.fire(this, ToolStripButtonEnum.CODELISTS);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        String urn = PlaceRequestUtils.getCodelistParamFromUrl(placeManager);
        if (!StringUtils.isBlank(urn)) {
            retrieveCodelist(urn);
        } else {
            MetamacSrmWeb.showErrorPage();
        }
    }

    @Override
    public void retrieveCodelist(String identifier) {
        // Retrieve codelist by URN
        String urn = UrnUtils.generateUrn(UrnConstants.URN_SDMX_CLASS_CODELIST_PREFIX, identifier);
        if (!StringUtils.isBlank(urn)) {
            retrieveCodelistByUrn(urn);
        }
    }

    private void retrieveCodelistByUrn(String urn) {
        dispatcher.execute(new GetCodelistAction(urn), new WaitingAsyncCallback<GetCodelistResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistErrorRetrieve()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCodelistResult result) {
                codelistMetamacDto = result.getCodelistMetamacDto();
                getView().setCodelist(codelistMetamacDto);
                retrieveCodesByCodelist(result.getCodelistMetamacDto().getUrn());
                retrieveCodelistVersions(result.getCodelistMetamacDto().getUrn());
            }
        });
    }

    @Override
    public void goToCodelist(String urn) {
        if (!StringUtils.isBlank(urn)) {
            placeManager.revealRelativePlace(PlaceRequestUtils.buildCodelistPlaceRequest(urn), -1);
        }
    }

    @Override
    public void goToCode(String urn) {
        placeManager.revealRelativePlace(PlaceRequestUtils.buildCodePlaceRequest(urn));
    }

    @Override
    public void saveCode(CodeMetamacDto codeDto) {
        dispatcher.execute(new SaveCodeAction(codeDto), new WaitingAsyncCallback<SaveCodeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codeErrorSave()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(SaveCodeResult result) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getMessageList(getMessages().codeSaved()), MessageTypeEnum.SUCCESS);
                retrieveCodelistByUrn(codelistMetamacDto.getUrn());
            }
        });
    }

    @Override
    public void deleteCode(ItemDto itemDto) {
        dispatcher.execute(new DeleteCodeAction(itemDto.getUrn()), new WaitingAsyncCallback<DeleteCodeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codeErrorDelete()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(DeleteCodeResult result) {
                retrieveCodesByCodelist(codelistMetamacDto.getUrn());
            }
        });
    }

    @Override
    public void retrieveCodelistVersions(String codelistUrn) {
        dispatcher.execute(new GetCodelistVersionsAction(codelistUrn), new WaitingAsyncCallback<GetCodelistVersionsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistErrorRetrievingVersions()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCodelistVersionsResult result) {
                getView().setCodelistVersions(result.getCodelistMetamacDtos());
            }
        });
    }

    @Override
    public void saveCodelist(CodelistMetamacDto codelist) {
        dispatcher.execute(new SaveCodelistAction(codelist), new WaitingAsyncCallback<SaveCodelistResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistErrorSave()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(SaveCodelistResult result) {
                codelistMetamacDto = result.getSavedCodelistDto();
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getMessageList(getMessages().codelistSaved()), MessageTypeEnum.SUCCESS);
                getView().setCodelist(codelistMetamacDto);

                // Update URL
                PlaceRequest placeRequest = PlaceRequestUtils.buildCodelistPlaceRequest(codelistMetamacDto.getUrn());
                placeManager.updateHistory(placeRequest, true);
            }
        });
    }

    @Override
    public void cancelValidity(final String urn) {
        List<String> urns = new ArrayList<String>();
        urns.add(urn);
        dispatcher.execute(new CancelCodelistValidityAction(urns), new WaitingAsyncCallback<CancelCodelistValidityResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistErrorCancelValidity()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(CancelCodelistValidityResult result) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getMessageList(getMessages().codelistCanceledValidity()), MessageTypeEnum.SUCCESS);
                retrieveCodelistByUrn(urn);
            }
        });
    }

    @Override
    public void retrieveCodesByCodelist(String codelistUrn) {
        dispatcher.execute(new GetCodesByCodelistAction(codelistUrn), new WaitingAsyncCallback<GetCodesByCodelistResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistErrorRetrievingCodeList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCodesByCodelistResult result) {
                getView().setCodes(result.getItemHierarchyDtos());
            }
        });
    }

    @Override
    public void sendToProductionValidation(String urn, ProcStatusEnum currentProcStatus) {
        dispatcher.execute(new UpdateCodelistProcStatusAction(urn, ProcStatusEnum.PRODUCTION_VALIDATION, currentProcStatus), new WaitingAsyncCallback<UpdateCodelistProcStatusResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistErrorSendingToProductionValidation()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(UpdateCodelistProcStatusResult result) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getMessageList(getMessages().codelistSentToProductionValidation()), MessageTypeEnum.SUCCESS);
                codelistMetamacDto = result.getCodelistMetamacDto();
                getView().setCodelist(codelistMetamacDto);
            }
        });
    }

    @Override
    public void sendToDiffusionValidation(String urn, ProcStatusEnum currentProcStatus) {
        dispatcher.execute(new UpdateCodelistProcStatusAction(urn, ProcStatusEnum.DIFFUSION_VALIDATION, currentProcStatus), new WaitingAsyncCallback<UpdateCodelistProcStatusResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistErrorSendingToDiffusionValidation()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(UpdateCodelistProcStatusResult result) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getMessageList(getMessages().codelistSentToDiffusionValidation()), MessageTypeEnum.SUCCESS);
                codelistMetamacDto = result.getCodelistMetamacDto();
                getView().setCodelist(codelistMetamacDto);
            }
        });
    }

    @Override
    public void rejectValidation(String urn, ProcStatusEnum currentProcStatus) {
        dispatcher.execute(new UpdateCodelistProcStatusAction(urn, ProcStatusEnum.VALIDATION_REJECTED, currentProcStatus), new WaitingAsyncCallback<UpdateCodelistProcStatusResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistErrorRejecting()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(UpdateCodelistProcStatusResult result) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getMessageList(getMessages().codelistRejected()), MessageTypeEnum.SUCCESS);
                codelistMetamacDto = result.getCodelistMetamacDto();
                getView().setCodelist(codelistMetamacDto);
            }
        });
    }

    @Override
    public void publishInternally(String urn, ProcStatusEnum currentProcStatus) {
        dispatcher.execute(new UpdateCodelistProcStatusAction(urn, ProcStatusEnum.INTERNALLY_PUBLISHED, currentProcStatus), new WaitingAsyncCallback<UpdateCodelistProcStatusResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistErrorPublishingInternally()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(UpdateCodelistProcStatusResult result) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getMessageList(getMessages().codelistPublishedInternally()), MessageTypeEnum.SUCCESS);
                codelistMetamacDto = result.getCodelistMetamacDto();
                getView().setCodelist(codelistMetamacDto);
            }
        });
    }

    @Override
    public void publishExternally(String urn, ProcStatusEnum currentProcStatus) {
        dispatcher.execute(new UpdateCodelistProcStatusAction(urn, ProcStatusEnum.EXTERNALLY_PUBLISHED, currentProcStatus), new WaitingAsyncCallback<UpdateCodelistProcStatusResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistErrorPublishingExternally()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(UpdateCodelistProcStatusResult result) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getMessageList(getMessages().codelistPublishedExternally()), MessageTypeEnum.SUCCESS);
                codelistMetamacDto = result.getCodelistMetamacDto();
                getView().setCodelist(codelistMetamacDto);
            }
        });
    }

    @Override
    public void versioning(String urn, VersionTypeEnum versionType) {
        dispatcher.execute(new VersionCodelistAction(urn, versionType), new WaitingAsyncCallback<VersionCodelistResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistErrorVersioning()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(VersionCodelistResult result) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getMessageList(getMessages().codelistVersioned()), MessageTypeEnum.SUCCESS);
                codelistMetamacDto = result.getCodelistMetamacDto();
                retrieveCodelistByUrn(codelistMetamacDto.getUrn());

                // Update URL
                PlaceRequest placeRequest = PlaceRequestUtils.buildCodelistPlaceRequest(codelistMetamacDto.getUrn());
                placeManager.updateHistory(placeRequest, true);
            }
        });
    }
}
