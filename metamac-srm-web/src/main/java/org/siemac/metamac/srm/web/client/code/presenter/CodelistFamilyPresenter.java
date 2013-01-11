package org.siemac.metamac.srm.web.client.code.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.dto.CodelistFamilyDto;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.code.view.handlers.CodelistFamilyUiHandlers;
import org.siemac.metamac.srm.web.client.code.widgets.CodesToolStripPresenterWidget;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.shared.code.AddCodelistsToCodelistFamilyAction;
import org.siemac.metamac.srm.web.shared.code.AddCodelistsToCodelistFamilyResult;
import org.siemac.metamac.srm.web.shared.code.GetCodelistFamilyAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistFamilyResult;
import org.siemac.metamac.srm.web.shared.code.GetCodelistsAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistsResult;
import org.siemac.metamac.srm.web.shared.code.RemoveCodelistsFromCodelistFamilyAction;
import org.siemac.metamac.srm.web.shared.code.RemoveCodelistsFromCodelistFamilyResult;
import org.siemac.metamac.srm.web.shared.code.SaveCodelistFamilyAction;
import org.siemac.metamac.srm.web.shared.code.SaveCodelistFamilyResult;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
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

public class CodelistFamilyPresenter extends Presenter<CodelistFamilyPresenter.CodelistFamilyView, CodelistFamilyPresenter.CodelistFamilyProxy> implements CodelistFamilyUiHandlers {

    public final static int               CODELIST_LIST_FIRST_RESULT = 0;
    public final static int               CODELIST_LIST_MAX_RESULTS  = 30;

    private final DispatchAsync           dispatcher;
    private final PlaceManager            placeManager;

    private CodesToolStripPresenterWidget codesToolStripPresenterWidget;

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbCodelistFamily();
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.codelistFamilyPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface CodelistFamilyProxy extends Proxy<CodelistFamilyPresenter>, Place {
    }

    public interface CodelistFamilyView extends View, HasUiHandlers<CodelistFamilyUiHandlers> {

        void setCodelistFamily(CodelistFamilyDto codelistFamilyDto);
        void setCodelists(GetCodelistsResult result);
        void setCodelistsOfFamily(GetCodelistsResult result);
    }

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaContentCodelist     = new Type<RevealContentHandler<?>>();

    public static final Object                        TYPE_SetContextAreaContentCodesToolBar = new Object();

    @Inject
    public CodelistFamilyPresenter(EventBus eventBus, CodelistFamilyView view, CodelistFamilyProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager,
            CodesToolStripPresenterWidget codesToolStripPresenterWidget) {
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

        SetTitleEvent.fire(this, getConstants().codelistFamily());
        SelectMenuButtonEvent.fire(this, ToolStripButtonEnum.CODELISTS);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        String identifier = PlaceRequestUtils.getCodelistFamilyParamFromUrl(placeManager);
        if (!StringUtils.isBlank(identifier)) {
            retrieveCodelistFamily(identifier);
        } else {
            MetamacSrmWeb.showErrorPage();
        }
    }

    private void retrieveCodelistFamily(String identifier) {
        // TODO Family URN
        String urn = "urn:" + identifier;
        retrieveCodelistFamilyByUrn(urn);
        retrieveCodelistsByFamily(CODELIST_LIST_FIRST_RESULT, CODELIST_LIST_MAX_RESULTS, null, urn);
    }

    @Override
    public void retrieveCodelistFamilyByUrn(String urn) {
        dispatcher.execute(new GetCodelistFamilyAction(urn), new WaitingAsyncCallback<GetCodelistFamilyResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistFamilyPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistFamilyErrorRetrieve()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCodelistFamilyResult result) {
                getView().setCodelistFamily(result.getCodelistFamilyDto());
            }
        });
    }

    @Override
    public void retrieveCodelists(int firstResult, int maxResults, String criteria) {
        dispatcher.execute(new GetCodelistsAction(firstResult, maxResults, criteria, null, null), new WaitingAsyncCallback<GetCodelistsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistFamilyPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCodelistsResult result) {
                getView().setCodelists(result);
            }
        });
    }

    @Override
    public void retrieveCodelistsByFamily(int firstResult, int maxResults, final String criteria, String familyUrn) {
        dispatcher.execute(new GetCodelistsAction(firstResult, maxResults, criteria, null, familyUrn), new WaitingAsyncCallback<GetCodelistsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistFamilyPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCodelistsResult result) {
                getView().setCodelistsOfFamily(result);
            }
        });
    }

    @Override
    public void goToCodelist(String urn) {
        if (!StringUtils.isBlank(urn)) {
            placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteCodelistPlaceRequest(urn));
        }
    }

    @Override
    public void saveCodelistFamily(CodelistFamilyDto codelistFamilyDto) {
        dispatcher.execute(new SaveCodelistFamilyAction(codelistFamilyDto), new WaitingAsyncCallback<SaveCodelistFamilyResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistFamilyPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistFamilyErrorSave()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(SaveCodelistFamilyResult result) {
                ShowMessageEvent.fire(CodelistFamilyPresenter.this, ErrorUtils.getMessageList(getMessages().codelistFamilySaved()), MessageTypeEnum.SUCCESS);
                getView().setCodelistFamily(result.getSavedCodelistFamilyDto());
            }
        });
    }

    @Override
    public void addCodelistsToFamily(List<String> codelistUrns, final String familyUrn) {
        dispatcher.execute(new AddCodelistsToCodelistFamilyAction(codelistUrns, familyUrn), new WaitingAsyncCallback<AddCodelistsToCodelistFamilyResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistFamilyPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistErrorAddingToFamily()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(AddCodelistsToCodelistFamilyResult result) {
                ShowMessageEvent.fire(CodelistFamilyPresenter.this, ErrorUtils.getMessageList(getMessages().codelistsAddedToFamily()), MessageTypeEnum.SUCCESS);
                retrieveCodelistsByFamily(CODELIST_LIST_FIRST_RESULT, CODELIST_LIST_MAX_RESULTS, null, familyUrn);
            }
        });
    }

    @Override
    public void removeCodelistsFromFamily(List<String> codelistUrns, final String familyUrn) {
        dispatcher.execute(new RemoveCodelistsFromCodelistFamilyAction(codelistUrns, familyUrn), new WaitingAsyncCallback<RemoveCodelistsFromCodelistFamilyResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistFamilyPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistErrorRemovingFromFamily()), MessageTypeEnum.ERROR);
                retrieveCodelistsByFamily(CODELIST_LIST_FIRST_RESULT, CODELIST_LIST_MAX_RESULTS, null, familyUrn);
            }
            @Override
            public void onWaitSuccess(RemoveCodelistsFromCodelistFamilyResult result) {
                ShowMessageEvent.fire(CodelistFamilyPresenter.this, ErrorUtils.getMessageList(getMessages().codelistsRemovedFromFamily()), MessageTypeEnum.SUCCESS);
                retrieveCodelistsByFamily(CODELIST_LIST_FIRST_RESULT, CODELIST_LIST_MAX_RESULTS, null, familyUrn);
            }
        });
    }
}
