package org.siemac.metamac.srm.web.code.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.Date;
import java.util.List;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.domain.shared.CodeToCopy;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.navigation.shared.NameTokens;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.code.enums.CodesToolStripButtonEnum;
import org.siemac.metamac.srm.web.code.view.handlers.CodeUiHandlers;
import org.siemac.metamac.srm.web.code.widgets.presenter.CodesToolStripPresenterWidget;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesAction;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.srm.web.shared.StructuralResourcesRelationEnum;
import org.siemac.metamac.srm.web.shared.code.CopyCodesInCodelistAction;
import org.siemac.metamac.srm.web.shared.code.CopyCodesInCodelistResult;
import org.siemac.metamac.srm.web.shared.code.DeleteCodeAction;
import org.siemac.metamac.srm.web.shared.code.DeleteCodeResult;
import org.siemac.metamac.srm.web.shared.code.GetCodeAction;
import org.siemac.metamac.srm.web.shared.code.GetCodeResult;
import org.siemac.metamac.srm.web.shared.code.GetCodelistAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistResult;
import org.siemac.metamac.srm.web.shared.code.GetCodelistsAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistsResult;
import org.siemac.metamac.srm.web.shared.code.GetCodesByCodelistAction;
import org.siemac.metamac.srm.web.shared.code.GetCodesByCodelistResult;
import org.siemac.metamac.srm.web.shared.code.SaveCodeAction;
import org.siemac.metamac.srm.web.shared.code.SaveCodeResult;
import org.siemac.metamac.srm.web.shared.code.UpdateCodeParentAction;
import org.siemac.metamac.srm.web.shared.code.UpdateCodeParentResult;
import org.siemac.metamac.srm.web.shared.concept.UpdateCodeVariableElementAction;
import org.siemac.metamac.srm.web.shared.concept.UpdateCodeVariableElementResult;
import org.siemac.metamac.srm.web.shared.criteria.CategorySchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.CategoryWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.VariableElementWebCriteria;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.ApplicationEditionLanguages;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

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
    public static String getTranslatedTitle(PlaceRequest placeRequest) {
        return PlaceRequestUtils.getCodeBreadCrumbTitle(placeRequest);
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.codePage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface CodeProxy extends Proxy<CodePresenter>, Place {
    }

    public interface CodeView extends View, HasUiHandlers<CodeUiHandlers> {

        void setCode(CodeMetamacDto codeDto);
        void setCode(CodeMetamacDto codeDto, CodelistMetamacDto codelistMetamacDto);
        void setCodes(CodelistMetamacDto codelistMetamacDto, List<CodeMetamacVisualisationResult> codes);
        void setVariableElements(GetRelatedResourcesResult result);

        // Complex codelists
        void setCodelistsToCreateComplexCodelist(GetCodelistsResult result);
        void setCodesToCreateComplexCodelist(CodelistMetamacDto codelistDto, List<CodeMetamacVisualisationResult> codes);
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
        codesToolStripPresenterWidget.selectCodesMenuButton(CodesToolStripButtonEnum.CODES);
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
                ShowMessageEvent.fireErrorMessage(CodePresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetCodeResult result) {
                getView().setCode(result.getCodeDto(), result.getCodelistMetamacDto());
            }
        });
    }

    @Override
    public void retrieveCodesByCodelist(final String codelistUrn) {
        dispatcher.execute(new GetCodelistAction(codelistUrn), new WaitingAsyncCallback<GetCodelistResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(CodePresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetCodelistResult result) {
                final CodelistMetamacDto codelistMetamacDto = result.getCodelistMetamacDto();
                String defaultOrderUrn = org.siemac.metamac.srm.web.code.utils.CommonUtils.getDefaultCodelistOrderUrn(codelistMetamacDto);
                dispatcher.execute(new GetCodesByCodelistAction(codelistUrn, defaultOrderUrn, null, ApplicationEditionLanguages.getCurrentLocale()),
                        new WaitingAsyncCallback<GetCodesByCodelistResult>() {

                            @Override
                            public void onWaitFailure(Throwable caught) {
                                ShowMessageEvent.fireErrorMessage(CodePresenter.this, caught);
                            }
                            @Override
                            public void onWaitSuccess(GetCodesByCodelistResult result) {
                                getView().setCodes(codelistMetamacDto, result.getCodes());
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
                ShowMessageEvent.fireErrorMessage(CodePresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(SaveCodeResult result) {
                ShowMessageEvent.fireSuccessMessage(CodePresenter.this, getMessages().codeSaved());
                getView().setCode(result.getCodeDto());

                // Update URL
                PlaceRequest placeRequest = PlaceRequestUtils.buildRelativeCodePlaceRequest(result.getCodeDto().getUrn());
                placeManager.updateHistory(placeRequest, true);
            }
        });
    }

    @Override
    public void copyCodesInCodelist(String codelistSourceUrn, final String codelistTargetUrn, List<CodeToCopy> codesToCopy) {
        dispatcher.execute(new CopyCodesInCodelistAction(codelistSourceUrn, codelistTargetUrn, codesToCopy), new WaitingAsyncCallback<CopyCodesInCodelistResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(CodePresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(CopyCodesInCodelistResult result) {
                ShowMessageEvent.fireSuccessMessage(CodePresenter.this, getMessages().codesCopiedInCodelist());
                retrieveCodesByCodelist(codelistTargetUrn);
            }
        });
    }

    @Override
    public void deleteCode(final String codelistUrn, final CodeMetamacVisualisationResult code) {
        dispatcher.execute(new DeleteCodeAction(code.getUrn()), new WaitingAsyncCallback<DeleteCodeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(CodePresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(DeleteCodeResult result) {
                // If deleted code had a code parent, go to this code parent. If not, go to the codelist.
                if (code.getParent() != null) {
                    goToCode(code.getParent().getUrn());
                } else {
                    goToCodelist(codelistUrn);
                }
            }
        });
    }

    @Override
    public void deleteCode(final CodeMetamacDto codeMetamacDto) {
        dispatcher.execute(new DeleteCodeAction(codeMetamacDto.getUrn()), new WaitingAsyncCallback<DeleteCodeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(CodePresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(DeleteCodeResult result) {
                // If deleted code had a code parent, go to this code parent. If not, go to the codelist.
                if (codeMetamacDto.getItemParentUrn() != null) {
                    goToCode(codeMetamacDto.getItemParentUrn());
                } else {
                    goToCodelist(codeMetamacDto.getItemSchemeVersionUrn());
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
                        ShowMessageEvent.fireErrorMessage(CodePresenter.this, caught);
                    }
                    @Override
                    public void onWaitSuccess(GetRelatedResourcesResult result) {
                        getView().setVariableElements(result);
                    }
                });
    }

    @Override
    public void updateCodeInOrder(String codeUrn, final String codelistOrderIdentifier, Integer newCodeIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateCodeParent(final String codeUrn, String newParentUrn) {
        dispatcher.execute(new UpdateCodeParentAction(codeUrn, newParentUrn), new WaitingAsyncCallback<UpdateCodeParentResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(CodePresenter.this, caught);
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
                ShowMessageEvent.fireErrorMessage(CodePresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(UpdateCodeVariableElementResult result) {
                ShowMessageEvent.fireSuccessMessage(CodePresenter.this, getMessages().codeVariableElementUpdated());
                retrieveCode(codeUrn);
            }
        });
    }

    @Override
    public void retrieveCodelistsForCreateComplexCodelists(int firstResult, int maxResults, CodelistWebCriteria criteria) {
        dispatcher.execute(new GetCodelistsAction(firstResult, maxResults, criteria), new WaitingAsyncCallback<GetCodelistsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(CodePresenter.this, caught);
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
                ShowMessageEvent.fireErrorMessage(CodePresenter.this, caught);
            }

            @Override
            public void onWaitSuccess(GetCodelistResult result) {
                final CodelistMetamacDto codelistMetamacDto = result.getCodelistMetamacDto();
                String defaultOrderUrn = org.siemac.metamac.srm.web.code.utils.CommonUtils.getDefaultCodelistOrderUrn(codelistMetamacDto);
                dispatcher.execute(new GetCodesByCodelistAction(codelistUrn, defaultOrderUrn, null, ApplicationEditionLanguages.getCurrentLocale()),
                        new WaitingAsyncCallback<GetCodesByCodelistResult>() {

                            @Override
                            public void onWaitFailure(Throwable caught) {
                                ShowMessageEvent.fireErrorMessage(CodePresenter.this, caught);
                            }
                            @Override
                            public void onWaitSuccess(GetCodesByCodelistResult result) {
                                getView().setCodesToCreateComplexCodelist(codelistMetamacDto, result.getCodes());
                            }
                        });
            }
        });
    }

    @Override
    public void retrieveCategorisations(String artefactCategorisedUrn) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void createCategorisations(List<String> categoryUrns) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void cancelCategorisationValidity(List<String> urns, Date validTo) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteCategorisations(List<String> urns) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void retrieveCategorySchemesForCategorisations(int firstResult, int maxResults, CategorySchemeWebCriteria categorySchemeWebCriteria) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void retrieveCategoriesForCategorisations(int firstResult, int maxResults, CategoryWebCriteria categoryWebCriteria) {
        throw new UnsupportedOperationException();
    }

    //
    // NAVIGATION
    //

    @Override
    public void goTo(List<PlaceRequest> location) {
        if (location != null && !location.isEmpty()) {
            placeManager.revealPlaceHierarchy(location);
        }
    }

    @Override
    public void goToCode(String urn) {
        placeManager.revealRelativePlace(PlaceRequestUtils.buildRelativeCodePlaceRequest(urn), -1);
    }

    @Override
    public void goToCodelist(String urn) {
        goTo(PlaceRequestUtils.buildAbsoluteCodelistPlaceRequest(urn));
    }
}
