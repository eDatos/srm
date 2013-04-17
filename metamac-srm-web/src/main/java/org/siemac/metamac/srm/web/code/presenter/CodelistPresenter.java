package org.siemac.metamac.srm.web.code.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.utils.MetamacWebCriteriaClientUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.code.enums.CodesToolStripButtonEnum;
import org.siemac.metamac.srm.web.code.view.handlers.CodelistUiHandlers;
import org.siemac.metamac.srm.web.code.widgets.presenter.CodesToolStripPresenterWidget;
import org.siemac.metamac.srm.web.shared.category.CreateCategorisationAction;
import org.siemac.metamac.srm.web.shared.category.CreateCategorisationResult;
import org.siemac.metamac.srm.web.shared.category.DeleteCategorisationsAction;
import org.siemac.metamac.srm.web.shared.category.DeleteCategorisationsResult;
import org.siemac.metamac.srm.web.shared.category.GetCategoriesAction;
import org.siemac.metamac.srm.web.shared.category.GetCategoriesResult;
import org.siemac.metamac.srm.web.shared.category.GetCategorisationsByArtefactAction;
import org.siemac.metamac.srm.web.shared.category.GetCategorisationsByArtefactResult;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemesAction;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemesResult;
import org.siemac.metamac.srm.web.shared.code.AddCodelistsToCodelistFamilyAction;
import org.siemac.metamac.srm.web.shared.code.AddCodelistsToCodelistFamilyResult;
import org.siemac.metamac.srm.web.shared.code.CancelCodelistValidityAction;
import org.siemac.metamac.srm.web.shared.code.CancelCodelistValidityResult;
import org.siemac.metamac.srm.web.shared.code.CreateCodelistTemporalVersionAction;
import org.siemac.metamac.srm.web.shared.code.CreateCodelistTemporalVersionResult;
import org.siemac.metamac.srm.web.shared.code.DeleteCodeAction;
import org.siemac.metamac.srm.web.shared.code.DeleteCodeResult;
import org.siemac.metamac.srm.web.shared.code.DeleteCodelistOpennessLevelsAction;
import org.siemac.metamac.srm.web.shared.code.DeleteCodelistOpennessLevelsResult;
import org.siemac.metamac.srm.web.shared.code.DeleteCodelistOrdersAction;
import org.siemac.metamac.srm.web.shared.code.DeleteCodelistOrdersResult;
import org.siemac.metamac.srm.web.shared.code.GetCodelistAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistFamiliesAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistFamiliesResult;
import org.siemac.metamac.srm.web.shared.code.GetCodelistOpennessLevelsAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistOpennessLevelsResult;
import org.siemac.metamac.srm.web.shared.code.GetCodelistOrdersAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistOrdersResult;
import org.siemac.metamac.srm.web.shared.code.GetCodelistResult;
import org.siemac.metamac.srm.web.shared.code.GetCodelistVersionsAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistVersionsResult;
import org.siemac.metamac.srm.web.shared.code.GetCodelistsAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistsResult;
import org.siemac.metamac.srm.web.shared.code.GetCodesByCodelistAction;
import org.siemac.metamac.srm.web.shared.code.GetCodesByCodelistResult;
import org.siemac.metamac.srm.web.shared.code.GetVariablesAction;
import org.siemac.metamac.srm.web.shared.code.GetVariablesResult;
import org.siemac.metamac.srm.web.shared.code.SaveCodeAction;
import org.siemac.metamac.srm.web.shared.code.SaveCodeResult;
import org.siemac.metamac.srm.web.shared.code.SaveCodelistAction;
import org.siemac.metamac.srm.web.shared.code.SaveCodelistOpennessLevelAction;
import org.siemac.metamac.srm.web.shared.code.SaveCodelistOpennessLevelResult;
import org.siemac.metamac.srm.web.shared.code.SaveCodelistOrderAction;
import org.siemac.metamac.srm.web.shared.code.SaveCodelistOrderResult;
import org.siemac.metamac.srm.web.shared.code.SaveCodelistResult;
import org.siemac.metamac.srm.web.shared.code.UpdateCodeInOrderAction;
import org.siemac.metamac.srm.web.shared.code.UpdateCodeInOrderResult;
import org.siemac.metamac.srm.web.shared.code.UpdateCodeParentAction;
import org.siemac.metamac.srm.web.shared.code.UpdateCodeParentResult;
import org.siemac.metamac.srm.web.shared.code.UpdateCodelistProcStatusAction;
import org.siemac.metamac.srm.web.shared.code.UpdateCodelistProcStatusResult;
import org.siemac.metamac.srm.web.shared.code.UpdateCodesInOpennessVisualisationAction;
import org.siemac.metamac.srm.web.shared.code.UpdateCodesInOpennessVisualisationResult;
import org.siemac.metamac.srm.web.shared.code.VersionCodelistAction;
import org.siemac.metamac.srm.web.shared.code.VersionCodelistResult;
import org.siemac.metamac.srm.web.shared.criteria.CategorySchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.CategoryWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.ApplicationEditionLanguages;
import org.siemac.metamac.web.common.client.utils.UrnUtils;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
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

    private final DispatchAsync           dispatcher;
    private final PlaceManager            placeManager;

    private CodelistMetamacDto            codelistMetamacDto;

    private CodesToolStripPresenterWidget codesToolStripPresenterWidget;

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

        // Codelist
        void setCodelist(CodelistMetamacDto codelistMetamacDto);
        void setCodelistVersions(List<CodelistMetamacDto> codelistMetamacDtos);
        void startCodelistEdition();

        // Codes
        void setCodes(List<CodeMetamacVisualisationResult> codes);

        // Orders
        void setCodesWithOrder(List<CodeMetamacVisualisationResult> codes, CodelistVisualisationDto codelistOrder);
        void setCodelistOrders(List<CodelistVisualisationDto> orders);

        // Openness levels
        void setCodesWithOpennessLevel(List<CodeMetamacVisualisationResult> codes, CodelistVisualisationDto codelistOpennessVisualisationDto);
        void setCodelistOpennessLevels(List<CodelistVisualisationDto> opennessLevels);

        // Complex codelists
        void setCodelistsToCreateComplexCodelist(GetCodelistsResult result);
        void setCodesToCreateComplexCodelist(CodelistMetamacDto codelistDto, List<CodeMetamacVisualisationResult> codes);

        // Categorisations
        void setCategorisations(List<CategorisationDto> categorisationDtos);
        void setCategorySchemesForCategorisations(GetCategorySchemesResult result);
        void setCategoriesForCategorisations(GetCategoriesResult result);

        // Related resources
        void setFamilies(List<RelatedResourceDto> families, int firstResult, int totalResults);
        void setVariables(GetVariablesResult result);
        void setCodelistsToReplace(List<RelatedResourceDto> codelists, int firstResult, int totalResults);

        void showInformationMessage(String title, String message);
    }

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaContentCodelist     = new Type<RevealContentHandler<?>>();

    public static final Object                        TYPE_SetContextAreaContentCodesToolBar = new Object();

    @Inject
    public CodelistPresenter(EventBus eventBus, CodelistView view, CodelistProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager, CodesToolStripPresenterWidget codesToolStripPresenterWidget) {
        super(eventBus, view, proxy);
        this.placeManager = placeManager;
        this.dispatcher = dispatcher;
        this.codesToolStripPresenterWidget = codesToolStripPresenterWidget;
        getView().setUiHandlers(this);
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        setInSlot(TYPE_SetContextAreaContentCodesToolBar, codesToolStripPresenterWidget);

        SetTitleEvent.fire(this, getConstants().codelist());
        SelectMenuButtonEvent.fire(this, ToolStripButtonEnum.CODELISTS);
        codesToolStripPresenterWidget.selectCodesMenuButton(CodesToolStripButtonEnum.CODELISTS);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        String identifier = PlaceRequestUtils.getCodelistParamFromUrl(placeManager);
        if (!StringUtils.isBlank(identifier)) {
            retrieveCompleteCodelistbyIdentifier(identifier);
        } else {
            MetamacSrmWeb.showErrorPage();
        }
    }

    @Override
    public void goToCodelist(String urn) {
        if (!StringUtils.isBlank(urn)) {
            placeManager.revealRelativePlace(PlaceRequestUtils.buildRelativeCodelistPlaceRequest(urn), -1);
        }
    }

    @Override
    public void goToCode(String urn) {
        placeManager.revealRelativePlace(PlaceRequestUtils.buildRelativeCodePlaceRequest(urn));
    }

    //
    // CODELIST
    //

    private void retrieveCompleteCodelistbyIdentifier(String identifier) {
        // Retrieve codelist by URN
        String urn = UrnUtils.generateUrn(UrnConstants.URN_SDMX_CLASS_CODELIST_PREFIX, identifier);
        if (!StringUtils.isBlank(urn)) {
            retrieveCompleteCodelistByUrn(urn);
        }
    }

    private void retrieveCompleteCodelistByUrn(String urn) {
        retrieveCompleteCodelistByUrn(urn, false);
    }

    private void retrieveCompleteCodelistByUrn(String urn, boolean startCodelistEdition) {
        retrieveCodelistAndCodesByUrn(urn, startCodelistEdition);
        retrieveCategorisations(urn);
    }

    private void retrieveCodelistAndCodesByUrn(String urn) {
        retrieveCodelistAndCodesByUrn(urn, false);
    }

    private void retrieveCodelistAndCodesByUrn(String urn, final boolean startCodelistEdition) {
        dispatcher.execute(new GetCodelistAction(urn), new WaitingAsyncCallback<GetCodelistResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistErrorRetrieve()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCodelistResult result) {
                codelistMetamacDto = result.getCodelistMetamacDto();

                getView().setCodelist(codelistMetamacDto);
                if (startCodelistEdition) {
                    getView().startCodelistEdition();
                }

                retrieveCodes();
                retrieveCodelistVersions(result.getCodelistMetamacDto().getUrn());
                retrieveCodelistOrders(result.getCodelistMetamacDto().getUrn());
                retrieveCodelistOpennessLevels(result.getCodelistMetamacDto().getUrn());
            }
        });
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

                updateUrl();
            }
        });
    }

    //
    // CODELIST LIFECYCLE
    //

    @Override
    public void sendToProductionValidation(String urn, ProcStatusEnum currentProcStatus) {
        dispatcher.execute(new UpdateCodelistProcStatusAction(urn, ProcStatusEnum.PRODUCTION_VALIDATION, currentProcStatus, null), new WaitingAsyncCallback<UpdateCodelistProcStatusResult>() {

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
        dispatcher.execute(new UpdateCodelistProcStatusAction(urn, ProcStatusEnum.DIFFUSION_VALIDATION, currentProcStatus, null), new WaitingAsyncCallback<UpdateCodelistProcStatusResult>() {

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
        dispatcher.execute(new UpdateCodelistProcStatusAction(urn, ProcStatusEnum.VALIDATION_REJECTED, currentProcStatus, null), new WaitingAsyncCallback<UpdateCodelistProcStatusResult>() {

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
    public void publishInternally(final String urnToPublish, ProcStatusEnum currentProcStatus) {
        Boolean forceLatestFinal = null; // FIXME
        dispatcher.execute(new UpdateCodelistProcStatusAction(urnToPublish, ProcStatusEnum.INTERNALLY_PUBLISHED, currentProcStatus, forceLatestFinal),
                new WaitingAsyncCallback<UpdateCodelistProcStatusResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistErrorPublishingInternally()), MessageTypeEnum.ERROR);
                    }
                    @Override
                    public void onWaitSuccess(UpdateCodelistProcStatusResult result) {
                        ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getMessageList(getMessages().codelistPublishedInternally()), MessageTypeEnum.SUCCESS);
                        codelistMetamacDto = result.getCodelistMetamacDto();
                        getView().setCodelist(codelistMetamacDto);

                        // If the version published was a temporal version, reload the version list and the URL. Wwhen a temporal version is published, is automatically converted into a normal version
                        // (the URN changes!).
                        if (org.siemac.metamac.core.common.util.shared.UrnUtils.isTemporalUrn(urnToPublish)) {
                            retrieveCodelistVersions(codelistMetamacDto.getUrn());
                            updateUrl();
                        }
                    }
                });
    }

    @Override
    public void publishExternally(String urn, ProcStatusEnum currentProcStatus) {
        dispatcher.execute(new UpdateCodelistProcStatusAction(urn, ProcStatusEnum.EXTERNALLY_PUBLISHED, currentProcStatus, null), new WaitingAsyncCallback<UpdateCodelistProcStatusResult>() {

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
    public void versioning(String urn, VersionTypeEnum versionType, boolean versionCodes) {
        dispatcher.execute(new VersionCodelistAction(urn, versionType, versionCodes), new WaitingAsyncCallback<VersionCodelistResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistErrorVersioning()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(VersionCodelistResult result) {
                if (result.getIsPlannedInBackground()) {
                    // The codelist is versioning in background
                    getView().setCodelist(result.getCodelistMetamacDto());
                    getView().showInformationMessage(getMessages().codelistVersioning(), getMessages().codelistBackgroundVersionInProgress());
                } else {
                    // Codelist has been version synchronously
                    ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getMessageList(getMessages().codelistVersioned()), MessageTypeEnum.SUCCESS);
                    codelistMetamacDto = result.getCodelistMetamacDto();
                    retrieveCompleteCodelistByUrn(codelistMetamacDto.getUrn());

                    updateUrl();
                }
            }
        });
    }

    @Override
    public void createTemporalVersion(String urn) {
        dispatcher.execute(new CreateCodelistTemporalVersionAction(urn), new WaitingAsyncCallback<CreateCodelistTemporalVersionResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().resourceErrorEditingPublishedResource()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(CreateCodelistTemporalVersionResult result) {
                if (result.getIsPlannedInBackground()) {
                    // Temporal version is created in background
                    getView().setCodelist(result.getCodelistMetamacDto());
                    getView().showInformationMessage(getMessages().codelistEditionInfo(), getMessages().codelistBackgroundTemporalVersionInProgress());
                } else {
                    // Temporal version has been created synchronously
                    CodelistPresenter.this.codelistMetamacDto = result.getCodelistMetamacDto();
                    retrieveCompleteCodelistByUrn(codelistMetamacDto.getUrn(), true);
                    updateUrl();
                }
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
                retrieveCodelistAndCodesByUrn(urn);
            }
        });
    }

    //
    // CODELIST VERSIONS
    //

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

    //
    // CODES
    //

    @Override
    public void retrieveCodes() {
        dispatcher.execute(new GetCodesByCodelistAction(codelistMetamacDto.getUrn(), null, null, ApplicationEditionLanguages.getCurrentLocale()), new WaitingAsyncCallback<GetCodesByCodelistResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistErrorRetrievingCodeList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCodesByCodelistResult result) {
                getView().setCodes(result.getCodes());
            }
        });
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
                retrieveCodelistAndCodesByUrn(codelistMetamacDto.getUrn());
            }
        });
    }

    @Override
    public void deleteCode(String codelistUrn, CodeMetamacVisualisationResult code) {
        dispatcher.execute(new DeleteCodeAction(code.getUrn()), new WaitingAsyncCallback<DeleteCodeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codeErrorDelete()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(DeleteCodeResult result) {
                retrieveCodes();
            }
        });
    }

    @Override
    public void updateCodeParent(String codeUrn, String newParentUrn) {
        dispatcher.execute(new UpdateCodeParentAction(codeUrn, newParentUrn), new WaitingAsyncCallback<UpdateCodeParentResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codeErrorUpdatingPosition()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(UpdateCodeParentResult result) {
                retrieveCodes();
            }
        });
    }

    //
    // CODES ORDERS
    //

    @Override
    public void retrieveCodelistOrders(String codelistUrn) {
        dispatcher.execute(new GetCodelistOrdersAction(codelistUrn), new WaitingAsyncCallback<GetCodelistOrdersResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistOrderErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCodelistOrdersResult result) {
                getView().setCodelistOrders(result.getOrders());
            }
        });
    }

    @Override
    public void retrieveCodesWithOrder(final String orderUrn) {
        dispatcher.execute(new GetCodesByCodelistAction(codelistMetamacDto.getUrn(), orderUrn, null, ApplicationEditionLanguages.getCurrentLocale()),
                new WaitingAsyncCallback<GetCodesByCodelistResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistErrorRetrievingCodeList()), MessageTypeEnum.ERROR);
                    }
                    @Override
                    public void onWaitSuccess(GetCodesByCodelistResult result) {
                        getView().setCodesWithOrder(result.getCodes(), result.getCodelistOrderVisualisationDto());
                    }
                });
    }

    @Override
    public void saveCodelistOrder(final CodelistVisualisationDto codelistOrderVisualisationDto) {
        dispatcher.execute(new SaveCodelistOrderAction(codelistMetamacDto.getUrn(), codelistOrderVisualisationDto), new WaitingAsyncCallback<SaveCodelistOrderResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistOrderErrorSave()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(SaveCodelistOrderResult result) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getMessageList(getMessages().codelistOrderSaved()), MessageTypeEnum.SUCCESS);
                retrieveCodelistOrders(codelistMetamacDto.getUrn());
                retrieveCodesWithOrder(result.getCodelistOrderSaved().getUrn());
            }
        });
    }

    @Override
    public void deleteCodelistOrders(List<String> orderUrns) {
        dispatcher.execute(new DeleteCodelistOrdersAction(orderUrns), new WaitingAsyncCallback<DeleteCodelistOrdersResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistOrderErrorDelete()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(DeleteCodelistOrdersResult result) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getMessageList(getMessages().codelistOrderDeleted()), MessageTypeEnum.SUCCESS);
                retrieveCodelistOrders(codelistMetamacDto.getUrn());
            }
        });
    }

    @Override
    public void updateCodeInOrder(String codeUrn, final String codelistOrderUrn, Integer newCodeIndex) {
        dispatcher.execute(new UpdateCodeInOrderAction(codeUrn, codelistOrderUrn, newCodeIndex), new WaitingAsyncCallback<UpdateCodeInOrderResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codeErrorUpdatingPosition()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(UpdateCodeInOrderResult result) {
                retrieveCodesWithOrder(codelistOrderUrn);
            }
        });
    }

    //
    // CODES OPENNESS LEVELS
    //

    @Override
    public void retrieveCodesWithOpennessLevel(String opennessLevelUrn) {
        dispatcher.execute(new GetCodesByCodelistAction(codelistMetamacDto.getUrn(), null, opennessLevelUrn, ApplicationEditionLanguages.getCurrentLocale()),
                new WaitingAsyncCallback<GetCodesByCodelistResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistErrorRetrievingCodeList()), MessageTypeEnum.ERROR);
                    }
                    @Override
                    public void onWaitSuccess(GetCodesByCodelistResult result) {
                        getView().setCodesWithOpennessLevel(result.getCodes(), result.getCodelistOpennessVisualisationDto());
                    }
                });
    }

    @Override
    public void retrieveCodelistOpennessLevels(String codelistUrn) {
        dispatcher.execute(new GetCodelistOpennessLevelsAction(codelistUrn), new WaitingAsyncCallback<GetCodelistOpennessLevelsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistOpennessLevelErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCodelistOpennessLevelsResult result) {
                getView().setCodelistOpennessLevels(result.getCodelistOpennessVisualisationDtos());
            }
        });
    }

    @Override
    public void saveCodelistOpennessLevel(CodelistVisualisationDto codelistOpennessVisualisationDto) {
        dispatcher.execute(new SaveCodelistOpennessLevelAction(codelistMetamacDto.getUrn(), codelistOpennessVisualisationDto), new WaitingAsyncCallback<SaveCodelistOpennessLevelResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistOpennessLevelErrorSave()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(SaveCodelistOpennessLevelResult result) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getMessageList(getMessages().codelistOpennessLevelSaved()), MessageTypeEnum.SUCCESS);
                retrieveCodelistOpennessLevels(codelistMetamacDto.getUrn());
                retrieveCodesWithOpennessLevel(result.getCodelistOpennessVisualisationSaved().getUrn());
            }
        });
    }

    @Override
    public void deleteCodelistOpennessLevel(List<String> levelsUrns) {
        dispatcher.execute(new DeleteCodelistOpennessLevelsAction(levelsUrns), new WaitingAsyncCallback<DeleteCodelistOpennessLevelsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistOpennessLevelErrorDelete()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(DeleteCodelistOpennessLevelsResult result) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getMessageList(getMessages().codelistOpennessLevelDeleted()), MessageTypeEnum.SUCCESS);
                retrieveCodelistOpennessLevels(codelistMetamacDto.getUrn());
            }
        });
    }

    @Override
    public void updateCodesOpennessLevel(String opennessLevelUrn, Map<String, Boolean> opennessLevels) {
        dispatcher.execute(new UpdateCodesInOpennessVisualisationAction(opennessLevelUrn, opennessLevels), new WaitingAsyncCallback<UpdateCodesInOpennessVisualisationResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codeErrorUpdatingOpennessLevel()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(UpdateCodesInOpennessVisualisationResult result) {
                retrieveCodesWithOpennessLevel(result.getCodelistVisualisationDto().getUrn());
            }
        });
    }

    //
    // COMPLEX CODELISTS
    //

    @Override
    public void retrieveCodelistsForCreateComplexCodelists(int firstResult, int maxResults, CodelistWebCriteria criteria) {
        dispatcher.execute(new GetCodelistsAction(firstResult, maxResults, criteria), new WaitingAsyncCallback<GetCodelistsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCodelistsResult result) {
                getView().setCodelistsToCreateComplexCodelist(result);
            }
        });
    }

    @Override
    public void retrieveCodesForCreateComplexCodelists(final String codelistUrn) {
        dispatcher.execute(new GetCodelistAction(codelistUrn), new WaitingAsyncCallback<GetCodelistResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistErrorRetrieve()), MessageTypeEnum.ERROR);
            }

            @Override
            public void onWaitSuccess(GetCodelistResult result) {
                final CodelistMetamacDto codelistMetamacDto = result.getCodelistMetamacDto();
                dispatcher.execute(new GetCodesByCodelistAction(codelistUrn, null, null, ApplicationEditionLanguages.getCurrentLocale()), new WaitingAsyncCallback<GetCodesByCodelistResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistErrorRetrievingCodeList()), MessageTypeEnum.ERROR);
                    }
                    @Override
                    public void onWaitSuccess(GetCodesByCodelistResult result) {
                        getView().setCodesToCreateComplexCodelist(codelistMetamacDto, result.getCodes());
                    }
                });
            }
        });
    }

    //
    // CATEGORISATIONS
    //

    @Override
    public void retrieveCategorisations(String artefactCategorisedUrn) {
        dispatcher.execute(new GetCategorisationsByArtefactAction(artefactCategorisedUrn), new WaitingAsyncCallback<GetCategorisationsByArtefactResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categorisationErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCategorisationsByArtefactResult result) {
                getView().setCategorisations(result.getCategorisationDtos());
            }
        });
    }

    @Override
    public void createCategorisations(List<String> categoryUrns) {
        dispatcher.execute(new CreateCategorisationAction(categoryUrns, codelistMetamacDto.getUrn(), RelatedResourceUtils.getDefaultMaintainerAsRelatedResourceDto().getUrn()),
                new WaitingAsyncCallback<CreateCategorisationResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categorisationErrorCreate()), MessageTypeEnum.ERROR);
                    }
                    @Override
                    public void onWaitSuccess(CreateCategorisationResult result) {
                        ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getMessageList(getMessages().categorisationCreated()), MessageTypeEnum.SUCCESS);
                        retrieveCategorisations(codelistMetamacDto.getUrn());
                    }
                });
    }

    @Override
    public void deleteCategorisations(List<String> urns) {
        dispatcher.execute(new DeleteCategorisationsAction(urns), new WaitingAsyncCallback<DeleteCategorisationsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categorisationErrorDelete()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(DeleteCategorisationsResult result) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getMessageList(getMessages().categorisationDeleted()), MessageTypeEnum.SUCCESS);
                retrieveCategorisations(codelistMetamacDto.getUrn());
            }
        });
    }

    @Override
    public void retrieveCategorySchemesForCategorisations(int firstResult, int maxResults, String criteria) {
        // The categories must be externally published
        CategorySchemeWebCriteria categorySchemeWebCriteria = new CategorySchemeWebCriteria(criteria);
        categorySchemeWebCriteria = MetamacWebCriteriaClientUtils.addCategorisationConditionToCategorySchemeWebCriteria(categorySchemeWebCriteria);
        dispatcher.execute(new GetCategorySchemesAction(firstResult, maxResults, categorySchemeWebCriteria), new WaitingAsyncCallback<GetCategorySchemesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categorySchemeErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCategorySchemesResult result) {
                getView().setCategorySchemesForCategorisations(result);
            }
        });
    }

    @Override
    public void retrieveCategoriesForCategorisations(int firstResult, int maxResults, String criteria, String categorySchemeUrn) {
        // The categories must be externally published
        CategoryWebCriteria categoryWebCriteria = new CategoryWebCriteria(criteria);
        categoryWebCriteria.setItemSchemeUrn(categorySchemeUrn);
        categoryWebCriteria = MetamacWebCriteriaClientUtils.addCategorisationConditionToCategoryWebCriteria(categoryWebCriteria);
        dispatcher.execute(new GetCategoriesAction(firstResult, maxResults, categoryWebCriteria), new WaitingAsyncCallback<GetCategoriesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categoryErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCategoriesResult result) {
                getView().setCategoriesForCategorisations(result);
            }
        });
    }

    //
    // RELATED RESOURCE ACTIONS
    //

    @Override
    public void retrieveFamilies(int firstResult, int maxResults, String criteria) {
        dispatcher.execute(new GetCodelistFamiliesAction(firstResult, maxResults, criteria), new WaitingAsyncCallback<GetCodelistFamiliesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistFamilyErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCodelistFamiliesResult result) {
                List<RelatedResourceDto> families = RelatedResourceUtils.getCodelistFamilyDtosAsRelatedResourceDtos(result.getFamilies());
                getView().setFamilies(families, result.getFirstResultOut(), result.getTotalResults());
            }
        });
    }

    @Override
    public void retrieveCodelistsThatCanBeReplaced(int firstResult, int maxResults, String criteria) {
        // The codelists that can be replaced should be externally published
        CodelistWebCriteria codelistWebCriteria = new CodelistWebCriteria(criteria);
        codelistWebCriteria = MetamacWebCriteriaClientUtils.addCanBeReplacedConditionToCodelistWebCriteria(codelistWebCriteria);
        dispatcher.execute(new GetCodelistsAction(firstResult, maxResults, codelistWebCriteria), new WaitingAsyncCallback<GetCodelistsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCodelistsResult result) {
                List<RelatedResourceDto> codelists = RelatedResourceUtils.getCodelistDtosAsRelatedResourceDtos(result.getCodelists());
                getView().setCodelistsToReplace(codelists, result.getFirstResultOut(), result.getTotalResults());
            }
        });
    }

    @Override
    public void retrieveVariables(int firstResult, int maxResults, String criteria) {
        dispatcher.execute(new GetVariablesAction(firstResult, maxResults, criteria, null), new WaitingAsyncCallback<GetVariablesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().variableErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetVariablesResult result) {
                getView().setVariables(result);
            }
        });
    }

    @Override
    public void addCodelistToFamily(final String codelistUrn, String familyUrn) {
        List<String> codelistUrns = new ArrayList<String>(1);
        codelistUrns.add(codelistUrn);
        dispatcher.execute(new AddCodelistsToCodelistFamilyAction(codelistUrns, familyUrn), new WaitingAsyncCallback<AddCodelistsToCodelistFamilyResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistErrorAddingToFamily()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(AddCodelistsToCodelistFamilyResult result) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getMessageList(getMessages().codelistAddedToFamily()), MessageTypeEnum.SUCCESS);
                retrieveCodelistByUrn(codelistUrn);
            }
        });
    }

    private void updateUrl() {
        PlaceRequest placeRequest = PlaceRequestUtils.buildRelativeCodelistPlaceRequest(codelistMetamacDto.getUrn());
        placeManager.updateHistory(placeRequest, true);
    }
}
