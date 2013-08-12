package org.siemac.metamac.srm.web.code.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.navigation.shared.NameTokens;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.code.enums.CodesToolStripButtonEnum;
import org.siemac.metamac.srm.web.code.view.handlers.VariableListUiHandlers;
import org.siemac.metamac.srm.web.code.widgets.presenter.CodesToolStripPresenterWidget;
import org.siemac.metamac.srm.web.shared.code.DeleteVariablesAction;
import org.siemac.metamac.srm.web.shared.code.DeleteVariablesResult;
import org.siemac.metamac.srm.web.shared.code.GetVariableFamiliesAction;
import org.siemac.metamac.srm.web.shared.code.GetVariableFamiliesResult;
import org.siemac.metamac.srm.web.shared.code.GetVariablesAction;
import org.siemac.metamac.srm.web.shared.code.GetVariablesResult;
import org.siemac.metamac.srm.web.shared.code.SaveVariableAction;
import org.siemac.metamac.srm.web.shared.code.SaveVariableResult;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
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

public class VariableListPresenter extends Presenter<VariableListPresenter.VariableListView, VariableListPresenter.VariableListProxy> implements VariableListUiHandlers {

    public final static int                           VARIABLE_LIST_FIRST_RESULT             = 0;
    public final static int                           VARIABLE_LIST_MAX_RESULTS              = 30;

    public final static int                           FAMILY_LIST_FIRST_RESULT               = 0;
    public final static int                           FAMILY_LIST_MAX_RESULTS                = 6;

    private final DispatchAsync                       dispatcher;
    private final PlaceManager                        placeManager;

    private CodesToolStripPresenterWidget             codesToolStripPresenterWidget;

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetStructuralResourcesToolBar     = new Type<RevealContentHandler<?>>();

    public static final Object                        TYPE_SetContextAreaContentCodesToolBar = new Object();

    @ProxyCodeSplit
    @NameToken(NameTokens.variableListPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface VariableListProxy extends Proxy<VariableListPresenter>, Place {
    }

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbVariableList();
    }

    public interface VariableListView extends View, HasUiHandlers<VariableListUiHandlers> {

        void setVariablePaginatedList(GetVariablesResult variablesPaginatedList);

        // Search
        void clearSearchSection();
        String getVariableCriteria();

        void setVariableFamilies(List<RelatedResourceDto> families, int firstResult, int totalResults);
    }

    @Inject
    public VariableListPresenter(EventBus eventBus, VariableListView variableListView, VariableListProxy variableListProxy, DispatchAsync dispatcher, PlaceManager placeManager,
            CodesToolStripPresenterWidget codesToolStripPresenterWidget) {
        super(eventBus, variableListView, variableListProxy);
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
        retrieveVariables(VARIABLE_LIST_FIRST_RESULT, VARIABLE_LIST_MAX_RESULTS, null);
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        setInSlot(TYPE_SetContextAreaContentCodesToolBar, codesToolStripPresenterWidget);

        SetTitleEvent.fire(this, getConstants().variables());
        SelectMenuButtonEvent.fire(this, ToolStripButtonEnum.CODELISTS);
        codesToolStripPresenterWidget.selectCodesMenuButton(CodesToolStripButtonEnum.VARIABLES);
    }

    @Override
    public void goToVariable(String code) {
        if (!StringUtils.isBlank(code)) {
            placeManager.revealRelativePlace(PlaceRequestUtils.buildRelativeVariablePlaceRequest(code));
        }
    }

    @Override
    public void createVariable(VariableDto variableDto) {
        dispatcher.execute(new SaveVariableAction(variableDto), new WaitingAsyncCallback<SaveVariableResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(VariableListPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(SaveVariableResult result) {
                ShowMessageEvent.fireSuccessMessage(VariableListPresenter.this, getMessages().variableSaved());
                retrieveVariables(VARIABLE_LIST_FIRST_RESULT, VARIABLE_LIST_MAX_RESULTS, getView().getVariableCriteria());
            }
        });
    }

    @Override
    public void deleteVariables(List<String> urns) {
        dispatcher.execute(new DeleteVariablesAction(urns), new WaitingAsyncCallback<DeleteVariablesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(VariableListPresenter.this, caught);
                retrieveVariables(VARIABLE_LIST_FIRST_RESULT, VARIABLE_LIST_MAX_RESULTS, getView().getVariableCriteria());
            }
            @Override
            public void onWaitSuccess(DeleteVariablesResult result) {
                ShowMessageEvent.fireSuccessMessage(VariableListPresenter.this, getMessages().variableDeleted());
                retrieveVariables(VARIABLE_LIST_FIRST_RESULT, VARIABLE_LIST_MAX_RESULTS, getView().getVariableCriteria());
            }
        });
    }

    @Override
    public void retrieveVariables(int firstResult, int maxResults, final String criteria) {
        dispatcher.execute(new GetVariablesAction(firstResult, maxResults, criteria, null), new WaitingAsyncCallback<GetVariablesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(VariableListPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetVariablesResult result) {
                getView().setVariablePaginatedList(result);
                if (StringUtils.isBlank(criteria)) {
                    getView().clearSearchSection();
                }
            }
        });
    }

    @Override
    public void retrieveVariableFamilies(int firstResult, int maxResults, String criteria) {
        dispatcher.execute(new GetVariableFamiliesAction(firstResult, maxResults, criteria), new WaitingAsyncCallback<GetVariableFamiliesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(VariableListPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetVariableFamiliesResult result) {
                getView().setVariableFamilies(RelatedResourceUtils.getVariableFamilyBasicDtosAsRelatedResourceDtos(result.getFamilies()), result.getFirstResultOut(), result.getTotalResults());
            }
        });
    }
}
