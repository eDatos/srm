package org.siemac.metamac.srm.web.client.code.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.List;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.code.utils.CommonUtils;
import org.siemac.metamac.srm.web.client.code.view.handlers.CodeUiHandlers;
import org.siemac.metamac.srm.web.client.code.widgets.CodesToolStripPresenterWidget;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesAction;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.srm.web.shared.StructuralResourcesRelationEnum;
import org.siemac.metamac.srm.web.shared.code.DeleteCodeAction;
import org.siemac.metamac.srm.web.shared.code.DeleteCodeResult;
import org.siemac.metamac.srm.web.shared.code.GetCodeAction;
import org.siemac.metamac.srm.web.shared.code.GetCodeResult;
import org.siemac.metamac.srm.web.shared.code.GetCodelistAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistResult;
import org.siemac.metamac.srm.web.shared.code.GetCodesByCodelistAction;
import org.siemac.metamac.srm.web.shared.code.GetCodesByCodelistResult;
import org.siemac.metamac.srm.web.shared.code.SaveCodeAction;
import org.siemac.metamac.srm.web.shared.code.SaveCodeResult;
import org.siemac.metamac.srm.web.shared.code.UpdateCodeParentAction;
import org.siemac.metamac.srm.web.shared.code.UpdateCodeParentResult;
import org.siemac.metamac.srm.web.shared.concept.UpdateCodeVariableElementAction;
import org.siemac.metamac.srm.web.shared.concept.UpdateCodeVariableElementResult;
import org.siemac.metamac.srm.web.shared.criteria.VariableElementWebCriteria;
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

public class CodePresenter extends Presenter<CodePresenter.CodeView, CodePresenter.CodeProxy> implements CodeUiHandlers {

    private final DispatchAsync           dispatcher;
    private final PlaceManager            placeManager;

    private CodesToolStripPresenterWidget codesToolStripPresenterWidget;

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbCode();
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.codePage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface CodeProxy extends Proxy<CodePresenter>, Place {
    }

    public interface CodeView extends View, HasUiHandlers<CodeUiHandlers> {

        void setCode(CodeMetamacDto codeDto);
        void setCodes(CodelistMetamacDto codelistMetamacDto, List<ItemHierarchyDto> codes);
        void setVariableElements(GetRelatedResourcesResult result);
    }

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaContentCode         = new Type<RevealContentHandler<?>>();

    public static final Object                        TYPE_SetContextAreaContentCodesToolBar = new Object();

    @Inject
    public CodePresenter(EventBus eventBus, CodeView view, CodeProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager, CodesToolStripPresenterWidget codesToolStripPresenterWidget) {
        super(eventBus, view, proxy);
        this.dispatcher = dispatcher;
        this.placeManager = placeManager;
        this.codesToolStripPresenterWidget = codesToolStripPresenterWidget;
        getView().setUiHandlers(this);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        setInSlot(TYPE_SetContextAreaContentCodesToolBar, codesToolStripPresenterWidget);

        SetTitleEvent.fire(this, MetamacSrmWeb.getConstants().code());
        SelectMenuButtonEvent.fire(this, ToolStripButtonEnum.CODELISTS);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        String schemeParam = PlaceRequestUtils.getCodelistParamFromUrl(placeManager);
        String codeCode = PlaceRequestUtils.getCodeParamFromUrl(placeManager);
        if (!StringUtils.isBlank(schemeParam) && !StringUtils.isBlank(codeCode)) {
            // this.codelistUrn = UrnUtils.generateUrn(UrnConstants.URN_SDMX_CLASS_CODELIST_PREFIX, schemeParam);
            String urn = UrnUtils.generateUrn(UrnConstants.URN_SDMX_CLASS_CODE_PREFIX, schemeParam, codeCode);
            retrieveCode(urn);
        } else {
            MetamacSrmWeb.showErrorPage();
        }
    }

    @Override
    public void retrieveCode(String codeUrn) {
        dispatcher.execute(new GetCodeAction(codeUrn), new WaitingAsyncCallback<GetCodeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codeErrorRetrieve()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCodeResult result) {
                getView().setCode(result.getCodeDto());
            }
        });
    }

    @Override
    public void retrieveCodesByCodelist(final String codelistUrn) {
        dispatcher.execute(new GetCodelistAction(codelistUrn), new WaitingAsyncCallback<GetCodelistResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistErrorRetrieve()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCodelistResult result) {
                final CodelistMetamacDto codelistMetamacDto = result.getCodelistMetamacDto();
                String defaultOrder = codelistMetamacDto.getDefaultOrderVisualisation() != null ? codelistMetamacDto.getDefaultOrderVisualisation().getUrn() : null;
                dispatcher.execute(new GetCodesByCodelistAction(codelistUrn, defaultOrder), new WaitingAsyncCallback<GetCodesByCodelistResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fire(CodePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistErrorRetrievingCodeList()), MessageTypeEnum.ERROR);
                    }
                    @Override
                    public void onWaitSuccess(GetCodesByCodelistResult result) {
                        List<ItemHierarchyDto> itemHierarchyDtos = CommonUtils.getItemHierarchyDtosFromCodeHierarchyDtos(result.getCodeHierarchyDtos());
                        getView().setCodes(codelistMetamacDto, itemHierarchyDtos);
                    }
                });
            }
        });
    }

    @Override
    public void saveCode(CodeMetamacDto codeDto) {
        dispatcher.execute(new SaveCodeAction(codeDto), new WaitingAsyncCallback<SaveCodeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codeErrorSave()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(SaveCodeResult result) {
                ShowMessageEvent.fire(CodePresenter.this, ErrorUtils.getMessageList(getMessages().codeSaved()), MessageTypeEnum.SUCCESS);
                getView().setCode(result.getCodeDto());

                // Update URL
                PlaceRequest placeRequest = PlaceRequestUtils.buildRelativeCodePlaceRequest(result.getCodeDto().getUrn());
                placeManager.updateHistory(placeRequest, true);
            }
        });
    }

    @Override
    public void deleteCode(final ItemDto itemDto) {
        dispatcher.execute(new DeleteCodeAction(itemDto.getUrn()), new WaitingAsyncCallback<DeleteCodeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codeErrorDelete()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(DeleteCodeResult result) {
                // If deleted code had a code parent, go to this code parent. If not, go to the codelist.
                if (itemDto.getItemParentUrn() != null) {
                    goToCode(itemDto.getItemParentUrn());
                } else {
                    goToCodelist(itemDto.getItemSchemeVersionUrn());
                }
            }
        });
    }

    @Override
    public void retrieveVariableElements(int firstResult, int maxResults, String criteria, String codelistUrn) {
        VariableElementWebCriteria variableElementWebCriteria = new VariableElementWebCriteria(criteria);
        variableElementWebCriteria.setCodelistUrn(codelistUrn);
        dispatcher.execute(new GetRelatedResourcesAction(StructuralResourcesRelationEnum.VARIABLE_ELEMENT_WITH_CODE, firstResult, maxResults, variableElementWebCriteria),
                new WaitingAsyncCallback<GetRelatedResourcesResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fire(CodePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().variableElementErrorRetrieveList()), MessageTypeEnum.ERROR);
                    }
                    @Override
                    public void onWaitSuccess(GetRelatedResourcesResult result) {
                        getView().setVariableElements(result);
                    }
                });
    }

    @Override
    public void updateCodeInOrder(String codeUrn, final String codelistOrderIdentifier, Long newCodeIndex) {
        // This method should not be called
    }

    @Override
    public void updateCodeParent(final String codeUrn, String newParentUrn, final String codelistOrderIdentifier) {
        dispatcher.execute(new UpdateCodeParentAction(codeUrn, newParentUrn), new WaitingAsyncCallback<UpdateCodeParentResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codeErrorUpdatingPosition()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(UpdateCodeParentResult result) {
                retrieveCode(codeUrn);
            }
        });
    }

    @Override
    public void updateVariableElement(final String codeUrn, String variableElementUrn) {
        dispatcher.execute(new UpdateCodeVariableElementAction(codeUrn, variableElementUrn), new WaitingAsyncCallback<UpdateCodeVariableElementResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codeVariableElementErrorUpdate()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(UpdateCodeVariableElementResult result) {
                ShowMessageEvent.fire(CodePresenter.this, ErrorUtils.getMessageList(getMessages().codeVariableElementUpdated()), MessageTypeEnum.SUCCESS);
                retrieveCode(codeUrn);
            }
        });
    }

    @Override
    public void goToCode(String urn) {
        placeManager.revealRelativePlace(PlaceRequestUtils.buildRelativeCodePlaceRequest(urn), -1);
    }

    private void goToCodelist(String urn) {
        placeManager.revealRelativePlace(PlaceRequestUtils.buildRelativeCodelistPlaceRequest(urn), -2);
    }
}
