package org.siemac.metamac.srm.web.code.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.dto.VariableFamilyDto;
import org.siemac.metamac.srm.navigation.shared.NameTokens;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.code.enums.CodesToolStripButtonEnum;
import org.siemac.metamac.srm.web.code.view.handlers.VariableFamilyListUiHandlers;
import org.siemac.metamac.srm.web.code.widgets.presenter.CodesToolStripPresenterWidget;
import org.siemac.metamac.srm.web.shared.code.DeleteVariableFamiliesAction;
import org.siemac.metamac.srm.web.shared.code.DeleteVariableFamiliesResult;
import org.siemac.metamac.srm.web.shared.code.GetVariableFamiliesAction;
import org.siemac.metamac.srm.web.shared.code.GetVariableFamiliesResult;
import org.siemac.metamac.srm.web.shared.code.SaveVariableFamilyAction;
import org.siemac.metamac.srm.web.shared.code.SaveVariableFamilyResult;
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

public class VariableFamilyListPresenter extends Presenter<VariableFamilyListPresenter.VariableFamilyListView, VariableFamilyListPresenter.VariableFamilyListProxy>
        implements
            VariableFamilyListUiHandlers {

    public final static int                           FAMILY_LIST_FIRST_RESULT               = 0;
    public final static int                           FAMILY_LIST_MAX_RESULTS                = 30;

    private final DispatchAsync                       dispatcher;
    private final PlaceManager                        placeManager;

    private CodesToolStripPresenterWidget             codesToolStripPresenterWidget;

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetStructuralResourcesToolBar     = new Type<RevealContentHandler<?>>();

    public static final Object                        TYPE_SetContextAreaContentCodesToolBar = new Object();

    @ProxyCodeSplit
    @NameToken(NameTokens.variableFamilyListPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface VariableFamilyListProxy extends Proxy<VariableFamilyListPresenter>, Place {
    }

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbVariableFamilyList();
    }

    public interface VariableFamilyListView extends View, HasUiHandlers<VariableFamilyListUiHandlers> {

        void setVariableFamilyPaginatedList(GetVariableFamiliesResult variableFamiliesPaginatedList);
        void clearSearchSection();
    }

    @Inject
    public VariableFamilyListPresenter(EventBus eventBus, VariableFamilyListView variableFamilyListView, VariableFamilyListProxy variableFamilyListProxy, DispatchAsync dispatcher,
            PlaceManager placeManager, CodesToolStripPresenterWidget codesToolStripPresenterWidget) {
        super(eventBus, variableFamilyListView, variableFamilyListProxy);
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
        retrieveVariableFamilies(FAMILY_LIST_FIRST_RESULT, FAMILY_LIST_MAX_RESULTS, null);
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        setInSlot(TYPE_SetContextAreaContentCodesToolBar, codesToolStripPresenterWidget);

        SetTitleEvent.fire(this, getConstants().variableFamilies());
        SelectMenuButtonEvent.fire(this, ToolStripButtonEnum.CODELISTS);
        codesToolStripPresenterWidget.selectCodesMenuButton(CodesToolStripButtonEnum.VARIABLE_FAMILIES);
    }

    @Override
    public void retrieveVariableFamilies(int firstResult, int maxResults, final String criteria) {
        dispatcher.execute(new GetVariableFamiliesAction(firstResult, maxResults, criteria), new WaitingAsyncCallback<GetVariableFamiliesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(VariableFamilyListPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().variableFamilyErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetVariableFamiliesResult result) {
                getView().setVariableFamilyPaginatedList(result);
                if (StringUtils.isBlank(criteria)) {
                    getView().clearSearchSection();
                }
            }
        });
    }

    @Override
    public void createVariableFamily(VariableFamilyDto variableFamilyDto) {
        dispatcher.execute(new SaveVariableFamilyAction(variableFamilyDto), new WaitingAsyncCallback<SaveVariableFamilyResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(VariableFamilyListPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().variableFamilyErrorSave()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(SaveVariableFamilyResult result) {
                ShowMessageEvent.fire(VariableFamilyListPresenter.this, ErrorUtils.getMessageList(getMessages().variableFamilySaved()), MessageTypeEnum.SUCCESS);
                retrieveVariableFamilies(FAMILY_LIST_FIRST_RESULT, FAMILY_LIST_MAX_RESULTS, null);
            }
        });
    }

    @Override
    public void deleteVariableFamilies(List<String> urns) {
        dispatcher.execute(new DeleteVariableFamiliesAction(urns), new WaitingAsyncCallback<DeleteVariableFamiliesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(VariableFamilyListPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().variableFamilyErrorDelete()), MessageTypeEnum.ERROR);
                retrieveVariableFamilies(FAMILY_LIST_FIRST_RESULT, FAMILY_LIST_MAX_RESULTS, null);
            }
            @Override
            public void onWaitSuccess(DeleteVariableFamiliesResult result) {
                ShowMessageEvent.fire(VariableFamilyListPresenter.this, ErrorUtils.getMessageList(getMessages().variableFamilyDeleted()), MessageTypeEnum.SUCCESS);
                retrieveVariableFamilies(FAMILY_LIST_FIRST_RESULT, FAMILY_LIST_MAX_RESULTS, null);
            }
        });
    }

    @Override
    public void goToVariableFamily(String code) {
        if (!StringUtils.isBlank(code)) {
            placeManager.revealRelativePlace(PlaceRequestUtils.buildRelativeVariableFamilyPlaceRequest(code));
        }
    }
}
