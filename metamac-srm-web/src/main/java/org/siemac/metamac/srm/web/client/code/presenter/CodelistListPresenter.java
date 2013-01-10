package org.siemac.metamac.srm.web.client.code.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.code.view.handlers.CodelistListUiHandlers;
import org.siemac.metamac.srm.web.client.code.widgets.CodesToolStripPresenterWidget;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.shared.code.CancelCodelistValidityAction;
import org.siemac.metamac.srm.web.shared.code.CancelCodelistValidityResult;
import org.siemac.metamac.srm.web.shared.code.DeleteCodelistListAction;
import org.siemac.metamac.srm.web.shared.code.DeleteCodelistListResult;
import org.siemac.metamac.srm.web.shared.code.GetCodelistsAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistsResult;
import org.siemac.metamac.srm.web.shared.code.SaveCodelistAction;
import org.siemac.metamac.srm.web.shared.code.SaveCodelistResult;
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
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

public class CodelistListPresenter extends Presenter<CodelistListPresenter.CodelistListView, CodelistListPresenter.CodelistListProxy> implements CodelistListUiHandlers {

    public final static int                           SCHEME_LIST_FIRST_RESULT               = 0;
    public final static int                           SCHEME_LIST_MAX_RESULTS                = 30;

    private final DispatchAsync                       dispatcher;
    private final PlaceManager                        placeManager;

    private CodesToolStripPresenterWidget             codesToolStripPresenterWidget;

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetStructuralResourcesToolBar     = new Type<RevealContentHandler<?>>();

    public static final Object                        TYPE_SetContextAreaContentCodesToolBar = new Object();

    @ProxyCodeSplit
    @NameToken(NameTokens.codelistListPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface CodelistListProxy extends Proxy<CodelistListPresenter>, Place {
    }

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbCodelistList();
    }

    public interface CodelistListView extends View, HasUiHandlers<CodelistListUiHandlers> {

        void setCodelistPaginatedList(GetCodelistsResult codelistsPaginatedList);
        void goToCodelistListLastPageAfterCreate();
        void clearSearchSection();
    }

    @Inject
    public CodelistListPresenter(EventBus eventBus, CodelistListView codelistListView, CodelistListProxy codelistListProxy, DispatchAsync dispatcher, PlaceManager placeManager,
            CodesToolStripPresenterWidget codesToolStripPresenterWidget) {
        super(eventBus, codelistListView, codelistListProxy);
        this.placeManager = placeManager;
        this.dispatcher = dispatcher;
        this.codesToolStripPresenterWidget = codesToolStripPresenterWidget;
        getView().setUiHandlers(this);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }

    @Override
    protected void onReset() {
        super.onReset();
        retrieveCodelists(SCHEME_LIST_FIRST_RESULT, SCHEME_LIST_MAX_RESULTS, null);
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        setInSlot(TYPE_SetContextAreaContentCodesToolBar, codesToolStripPresenterWidget);

        SetTitleEvent.fire(this, getConstants().codelists());
        SelectMenuButtonEvent.fire(this, ToolStripButtonEnum.CODELISTS);
    }

    @Override
    public void goToCodelist(String urn) {
        if (!StringUtils.isBlank(urn)) {
            placeManager.revealRelativePlace(PlaceRequestUtils.buildRelativeCodelistPlaceRequest(urn));
        }
    }

    @Override
    public void createCodelist(CodelistMetamacDto codelistMetamacDto) {
        dispatcher.execute(new SaveCodelistAction(codelistMetamacDto), new WaitingAsyncCallback<SaveCodelistResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistListPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistErrorSave()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(SaveCodelistResult result) {
                ShowMessageEvent.fire(CodelistListPresenter.this, ErrorUtils.getMessageList(getMessages().codelistSaved()), MessageTypeEnum.SUCCESS);
                retrieveCodelists(SCHEME_LIST_FIRST_RESULT, SCHEME_LIST_MAX_RESULTS, null);
            }
        });
    }

    @Override
    public void deleteCodelists(List<String> urns) {
        dispatcher.execute(new DeleteCodelistListAction(urns), new WaitingAsyncCallback<DeleteCodelistListResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistListPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistErrorDelete()), MessageTypeEnum.ERROR);
                retrieveCodelists(SCHEME_LIST_FIRST_RESULT, SCHEME_LIST_MAX_RESULTS, null);
            }
            @Override
            public void onWaitSuccess(DeleteCodelistListResult result) {
                ShowMessageEvent.fire(CodelistListPresenter.this, ErrorUtils.getMessageList(getMessages().codelistDeleted()), MessageTypeEnum.SUCCESS);
                retrieveCodelists(SCHEME_LIST_FIRST_RESULT, SCHEME_LIST_MAX_RESULTS, null);
            }
        });
    }

    @Override
    public void retrieveCodelists(int firstResult, int maxResults, final String criteria) {
        dispatcher.execute(new GetCodelistsAction(firstResult, maxResults, criteria, null), new WaitingAsyncCallback<GetCodelistsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistListPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCodelistsResult result) {
                getView().setCodelistPaginatedList(result);
                if (StringUtils.isBlank(criteria)) {
                    getView().clearSearchSection();
                }
            }
        });
    }

    @Override
    public void cancelValidity(List<String> urns) {
        dispatcher.execute(new CancelCodelistValidityAction(urns), new WaitingAsyncCallback<CancelCodelistValidityResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodelistListPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codelistErrorCancelValidity()), MessageTypeEnum.ERROR);
                retrieveCodelists(SCHEME_LIST_FIRST_RESULT, SCHEME_LIST_MAX_RESULTS, null);
            }
            @Override
            public void onWaitSuccess(CancelCodelistValidityResult result) {
                ShowMessageEvent.fire(CodelistListPresenter.this, ErrorUtils.getMessageList(getMessages().codelistCanceledValidity()), MessageTypeEnum.SUCCESS);
                retrieveCodelists(SCHEME_LIST_FIRST_RESULT, SCHEME_LIST_MAX_RESULTS, null);
            }
        });
    }
}
