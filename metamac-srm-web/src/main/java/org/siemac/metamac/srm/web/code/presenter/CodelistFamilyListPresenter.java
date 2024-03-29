package org.siemac.metamac.srm.web.code.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.dto.CodelistFamilyDto;
import org.siemac.metamac.srm.navigation.shared.NameTokens;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.code.enums.CodesToolStripButtonEnum;
import org.siemac.metamac.srm.web.code.view.handlers.CodelistFamilyListUiHandlers;
import org.siemac.metamac.srm.web.code.widgets.presenter.CodesToolStripPresenterWidget;
import org.siemac.metamac.srm.web.shared.code.DeleteCodelistFamiliesAction;
import org.siemac.metamac.srm.web.shared.code.DeleteCodelistFamiliesResult;
import org.siemac.metamac.srm.web.shared.code.GetCodelistFamiliesAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistFamiliesResult;
import org.siemac.metamac.srm.web.shared.code.SaveCodelistFamilyAction;
import org.siemac.metamac.srm.web.shared.code.SaveCodelistFamilyResult;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.WaitingAsyncCallbackHandlingError;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
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

public class CodelistFamilyListPresenter extends Presenter<CodelistFamilyListPresenter.CodelistFamilyListView, CodelistFamilyListPresenter.CodelistFamilyListProxy>
        implements
            CodelistFamilyListUiHandlers {

    public final static int                           FAMILY_LIST_FIRST_RESULT               = 0;
    public final static int                           FAMILY_LIST_MAX_RESULTS                = 30;

    private final DispatchAsync                       dispatcher;
    private final PlaceManager                        placeManager;

    private CodesToolStripPresenterWidget             codesToolStripPresenterWidget;

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetStructuralResourcesToolBar     = new Type<RevealContentHandler<?>>();

    public static final Object                        TYPE_SetContextAreaContentCodesToolBar = new Object();

    @ProxyCodeSplit
    @NameToken(NameTokens.codelistFamilyListPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface CodelistFamilyListProxy extends Proxy<CodelistFamilyListPresenter>, Place {
    }

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbCodelistFamilyList();
    }

    public interface CodelistFamilyListView extends View, HasUiHandlers<CodelistFamilyListUiHandlers> {

        void setCodelistFamilyPaginatedList(GetCodelistFamiliesResult codelistFamiliesPaginatedList);

        // Search
        void clearSearchSection();
        String getCodelistFamilyCriteria();
    }

    @Inject
    public CodelistFamilyListPresenter(EventBus eventBus, CodelistFamilyListView codelistFamilyListView, CodelistFamilyListProxy codelistFamilyListProxy, DispatchAsync dispatcher,
            PlaceManager placeManager, CodesToolStripPresenterWidget codesToolStripPresenterWidget) {
        super(eventBus, codelistFamilyListView, codelistFamilyListProxy);
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
        retrieveCodelistFamilies(FAMILY_LIST_FIRST_RESULT, FAMILY_LIST_MAX_RESULTS, null);
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        setInSlot(TYPE_SetContextAreaContentCodesToolBar, codesToolStripPresenterWidget);

        SetTitleEvent.fire(this, getConstants().codelistFamilies());
        SelectMenuButtonEvent.fire(this, ToolStripButtonEnum.CODELISTS);
        codesToolStripPresenterWidget.selectCodesMenuButton(CodesToolStripButtonEnum.CODELIST_FAMILIES);
    }

    @Override
    public void retrieveCodelistFamilies(int firstResult, int maxResults, final String criteria) {
        dispatcher.execute(new GetCodelistFamiliesAction(firstResult, maxResults, criteria), new WaitingAsyncCallbackHandlingError<GetCodelistFamiliesResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(CodelistFamilyListPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetCodelistFamiliesResult result) {
                getView().setCodelistFamilyPaginatedList(result);
                if (StringUtils.isBlank(criteria)) {
                    getView().clearSearchSection();
                }
            }
        });
    }

    @Override
    public void createCodelistFamily(CodelistFamilyDto codelistFamilyDto) {
        dispatcher.execute(new SaveCodelistFamilyAction(codelistFamilyDto), new WaitingAsyncCallbackHandlingError<SaveCodelistFamilyResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(CodelistFamilyListPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(SaveCodelistFamilyResult result) {
                fireSuccessMessage(getMessages().codelistFamilySaved());
                retrieveCodelistFamilies(FAMILY_LIST_FIRST_RESULT, FAMILY_LIST_MAX_RESULTS, getView().getCodelistFamilyCriteria());
            }
        });
    }

    @Override
    public void deleteCodelistFamilies(List<String> urns) {
        dispatcher.execute(new DeleteCodelistFamiliesAction(urns), new WaitingAsyncCallbackHandlingError<DeleteCodelistFamiliesResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(CodelistFamilyListPresenter.this, caught);
                retrieveCodelistFamilies(FAMILY_LIST_FIRST_RESULT, FAMILY_LIST_MAX_RESULTS, getView().getCodelistFamilyCriteria());
            }
            @Override
            public void onWaitSuccess(DeleteCodelistFamiliesResult result) {
                fireSuccessMessage(getMessages().codelistFamilyDeleted());
                retrieveCodelistFamilies(FAMILY_LIST_FIRST_RESULT, FAMILY_LIST_MAX_RESULTS, getView().getCodelistFamilyCriteria());
            }
        });
    }

    @Override
    public void goToCodelistFamily(String code) {
        if (!StringUtils.isBlank(code)) {
            placeManager.revealRelativePlace(PlaceRequestUtils.buildRelativeCodelistFamilyPlaceRequest(code));
        }
    }
}
