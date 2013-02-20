package org.siemac.metamac.srm.web.code.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistOrderVisualisationDto;
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
import org.siemac.metamac.srm.web.code.utils.CommonUtils;
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
import org.siemac.metamac.srm.web.shared.code.DeleteCodeAction;
import org.siemac.metamac.srm.web.shared.code.DeleteCodeResult;
import org.siemac.metamac.srm.web.shared.code.DeleteCodelistOrdersAction;
import org.siemac.metamac.srm.web.shared.code.DeleteCodelistOrdersResult;
import org.siemac.metamac.srm.web.shared.code.GetCodelistAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistFamiliesAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistFamiliesResult;
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
import org.siemac.metamac.srm.web.shared.code.SaveCodelistOrderAction;
import org.siemac.metamac.srm.web.shared.code.SaveCodelistOrderResult;
import org.siemac.metamac.srm.web.shared.code.SaveCodelistResult;
import org.siemac.metamac.srm.web.shared.code.UpdateCodeInOrderAction;
import org.siemac.metamac.srm.web.shared.code.UpdateCodeInOrderResult;
import org.siemac.metamac.srm.web.shared.code.UpdateCodeParentAction;
import org.siemac.metamac.srm.web.shared.code.UpdateCodeParentResult;
import org.siemac.metamac.srm.web.shared.code.UpdateCodelistProcStatusAction;
import org.siemac.metamac.srm.web.shared.code.UpdateCodelistProcStatusResult;
import org.siemac.metamac.srm.web.shared.code.VersionCodelistAction;
import org.siemac.metamac.srm.web.shared.code.VersionCodelistResult;
import org.siemac.metamac.srm.web.shared.criteria.CategorySchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.CategoryWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.UrnUtils;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
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

    private final DispatchAsync           dispatcher;
    private final PlaceManager            placeManager;

    private CodelistMetamacDto            codelistMetamacDto;
    private String                        codelistOrderVisualisationUrn;

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

        void setCodelist(CodelistMetamacDto codelistMetamacDto);
        void setCodelistVersions(List<CodelistMetamacDto> codelistMetamacDtos);
        void setCodes(List<ItemHierarchyDto> codeDtos, CodelistOrderVisualisationDto codelistOrder);
        void setCodelistOrders(List<CodelistOrderVisualisationDto> orders);
        void selectCodelistOrder(String codelistOrderUrn);

        void setFamilies(List<RelatedResourceDto> families, int firstResult, int totalResults);
        void setVariables(GetVariablesResult result);
        void setCodelistsToReplace(List<RelatedResourceDto> codelists, int firstResult, int totalResults);

        // Categorisations

        void setCategorisations(List<CategorisationDto> categorisationDtos);
        void setCategorySchemesForCategorisations(GetCategorySchemesResult result);
        void setCategoriesForCategorisations(GetCategoriesResult result);
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
            retrieveCodelist(identifier);
        } else {
            MetamacSrmWeb.showErrorPage();
        }
    }

    @Override
    public void retrieveCodelist(String identifier) {
        // Retrieve codelist by URN
        String urn = UrnUtils.generateUrn(UrnConstants.URN_SDMX_CLASS_CODELIST_PREFIX, identifier);
        if (!StringUtils.isBlank(urn)) {
            retrieveCodelistAndCodesByUrn(urn, null); // Load the codes ordered by the default order
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
                retrieveCodelistAndCodesByUrn(codelistMetamacDto.getUrn(), codelistOrderVisualisationUrn);
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
                retrieveCodesByCodelist(codelistOrderVisualisationUrn);
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
                PlaceRequest placeRequest = PlaceRequestUtils.buildRelativeCodelistPlaceRequest(codelistMetamacDto.getUrn());
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
                retrieveCodelistAndCodesByUrn(urn, codelistOrderVisualisationUrn);
            }
        });
    }

    @Override
    public void retrieveCodesByCodelist(final String orderUrn) {
        dispatcher.execute(new GetCodesByCodelistAction(codelistMetamacDto.getUrn(), orderUrn), new WaitingAsyncCallback<GetCodesByCodelistResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistErrorRetrievingCodeList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCodesByCodelistResult result) {
                codelistOrderVisualisationUrn = result.getCodelistOrderVisualisationDto() != null ? result.getCodelistOrderVisualisationDto().getUrn() : null;
                getView().setCodes(CommonUtils.getItemHierarchyDtosFromCodeHierarchyDtos(result.getCodeHierarchyDtos()), result.getCodelistOrderVisualisationDto());
                getView().selectCodelistOrder(orderUrn);
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
    public void versioning(String urn, VersionTypeEnum versionType, boolean versionCodes) {
        dispatcher.execute(new VersionCodelistAction(urn, versionType, versionCodes), new WaitingAsyncCallback<VersionCodelistResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistErrorVersioning()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(VersionCodelistResult result) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getMessageList(getMessages().codelistVersioned()), MessageTypeEnum.SUCCESS);
                codelistMetamacDto = result.getCodelistMetamacDto();
                retrieveCodelistAndCodesByUrn(codelistMetamacDto.getUrn(), codelistOrderVisualisationUrn);

                // Update URL
                PlaceRequest placeRequest = PlaceRequestUtils.buildRelativeCodelistPlaceRequest(codelistMetamacDto.getUrn());
                placeManager.updateHistory(placeRequest, true);
            }
        });
    }

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
    public void saveCodelistOrder(final CodelistOrderVisualisationDto codelistOrderVisualisationDto) {
        dispatcher.execute(new SaveCodelistOrderAction(codelistMetamacDto.getUrn(), codelistOrderVisualisationDto), new WaitingAsyncCallback<SaveCodelistOrderResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistOrderErrorSave()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(SaveCodelistOrderResult result) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getMessageList(getMessages().codelistOrderSaved()), MessageTypeEnum.SUCCESS);
                retrieveCodelistOrders(codelistMetamacDto.getUrn());
                retrieveCodesByCodelist(result.getCodelistOrderSaved().getUrn());
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
                retrieveCodelistAndCodesByUrn(codelistMetamacDto.getUrn(), null);
            }
        });
    }

    @Override
    public void updateCodeInOrder(String codeUrn, final String codelistOrderUrn, Long newCodeIndex) {
        dispatcher.execute(new UpdateCodeInOrderAction(codeUrn, codelistOrderUrn, newCodeIndex), new WaitingAsyncCallback<UpdateCodeInOrderResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codeErrorUpdatingPosition()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(UpdateCodeInOrderResult result) {
                retrieveCodesByCodelist(codelistOrderUrn);
            }
        });
    }

    @Override
    public void updateCodeParent(String codeUrn, String newParentUrn, final String codelistOrderUrn) {
        dispatcher.execute(new UpdateCodeParentAction(codeUrn, newParentUrn), new WaitingAsyncCallback<UpdateCodeParentResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codeErrorUpdatingPosition()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(UpdateCodeParentResult result) {
                retrieveCodesByCodelist(codelistOrderUrn);
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

    private void retrieveCodelistAndCodesByUrn(String urn, final String codelistOrderVisualisation) {
        dispatcher.execute(new GetCodelistAction(urn), new WaitingAsyncCallback<GetCodelistResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistErrorRetrieve()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCodelistResult result) {
                codelistMetamacDto = result.getCodelistMetamacDto();
                getView().setCodelist(codelistMetamacDto);

                // If the specified order is null, load the default order (if is exists)
                String order = codelistOrderVisualisation == null
                        ? (codelistMetamacDto.getDefaultOrderVisualisation() != null ? codelistMetamacDto.getDefaultOrderVisualisation().getUrn() : null)
                        : codelistOrderVisualisation;

                retrieveCodesByCodelist(order);
                retrieveCodelistVersions(result.getCodelistMetamacDto().getUrn());
                retrieveCodelistOrders(result.getCodelistMetamacDto().getUrn());
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
        categorySchemeWebCriteria.setProcStatus(ProcStatusEnum.EXTERNALLY_PUBLISHED);
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
        categoryWebCriteria.setIsExternallyPublished(true);
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
}
