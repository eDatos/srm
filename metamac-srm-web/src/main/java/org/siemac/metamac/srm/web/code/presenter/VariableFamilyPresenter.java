package org.siemac.metamac.srm.web.code.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.Arrays;
import java.util.List;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.srm.core.code.dto.VariableFamilyDto;
import org.siemac.metamac.srm.navigation.shared.NameTokens;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.code.enums.CodesToolStripButtonEnum;
import org.siemac.metamac.srm.web.code.view.handlers.VariableFamilyUiHandlers;
import org.siemac.metamac.srm.web.code.widgets.presenter.CodesToolStripPresenterWidget;
import org.siemac.metamac.srm.web.shared.code.AddVariablesToVariableFamilyAction;
import org.siemac.metamac.srm.web.shared.code.AddVariablesToVariableFamilyResult;
import org.siemac.metamac.srm.web.shared.code.DeleteVariableFamiliesAction;
import org.siemac.metamac.srm.web.shared.code.DeleteVariableFamiliesResult;
import org.siemac.metamac.srm.web.shared.code.GetVariableFamilyAction;
import org.siemac.metamac.srm.web.shared.code.GetVariableFamilyResult;
import org.siemac.metamac.srm.web.shared.code.GetVariablesAction;
import org.siemac.metamac.srm.web.shared.code.GetVariablesResult;
import org.siemac.metamac.srm.web.shared.code.RemoveVariablesFromVariableFamilyAction;
import org.siemac.metamac.srm.web.shared.code.RemoveVariablesFromVariableFamilyResult;
import org.siemac.metamac.srm.web.shared.code.SaveVariableFamilyAction;
import org.siemac.metamac.srm.web.shared.code.SaveVariableFamilyResult;
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

public class VariableFamilyPresenter extends Presenter<VariableFamilyPresenter.VariableFamilyView, VariableFamilyPresenter.VariableFamilyProxy> implements VariableFamilyUiHandlers {

    public final static int               VARIABLE_LIST_FIRST_RESULT = 0;
    public final static int               VARIABLE_LIST_MAX_RESULTS  = 30;

    private final DispatchAsync           dispatcher;
    private final PlaceManager            placeManager;

    private CodesToolStripPresenterWidget codesToolStripPresenterWidget;

    @TitleFunction
    public static String getTranslatedTitle(PlaceRequest placeRequest) {
        return PlaceRequestUtils.getVariableFamilyBreadCrumbTitle(placeRequest);
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.variableFamilyPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface VariableFamilyProxy extends Proxy<VariableFamilyPresenter>, Place {
    }

    public interface VariableFamilyView extends View, HasUiHandlers<VariableFamilyUiHandlers> {

        void setVariableFamily(VariableFamilyDto variableFamilyDto);
        void setVariables(GetVariablesResult result);
        void setVariablesOfFamily(GetVariablesResult result);
    }

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaContentCodelist     = new Type<RevealContentHandler<?>>();

    public static final Object                        TYPE_SetContextAreaContentCodesToolBar = new Object();

    @Inject
    public VariableFamilyPresenter(EventBus eventBus, VariableFamilyView view, VariableFamilyProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager,
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

        SetTitleEvent.fire(this, getConstants().variableFamily());
        SelectMenuButtonEvent.fire(this, ToolStripButtonEnum.CODELISTS);
        codesToolStripPresenterWidget.selectCodesMenuButton(CodesToolStripButtonEnum.VARIABLE_FAMILIES);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        String identifier = PlaceRequestUtils.getVariableFamilyParamFromUrl(placeManager);
        if (!StringUtils.isBlank(identifier)) {
            retrieveVariableFamily(identifier);
        } else {
            MetamacSrmWeb.showErrorPage();
        }
    }

    private void retrieveVariableFamily(String identifier) {
        String urn = UrnUtils.generateUrn(UrnConstants.URN_SIEMAC_CLASS_CODELIST_VARIABLE_FAMILY_PREFIX, identifier);
        if (!StringUtils.isBlank(urn)) {
            retrieveVariableFamilyByUrn(urn);
            retrieveVariablesByFamily(VARIABLE_LIST_FIRST_RESULT, VARIABLE_LIST_MAX_RESULTS, null, urn);
        }
    }

    @Override
    public void retrieveVariableFamilyByUrn(String urn) {
        dispatcher.execute(new GetVariableFamilyAction(urn), new WaitingAsyncCallback<GetVariableFamilyResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(VariableFamilyPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetVariableFamilyResult result) {
                getView().setVariableFamily(result.getVariableFamilyDto());
            }
        });
    }

    @Override
    public void retrieveVariables(int firstResult, int maxResults, String criteria) {
        dispatcher.execute(new GetVariablesAction(firstResult, maxResults, criteria, null), new WaitingAsyncCallback<GetVariablesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(VariableFamilyPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetVariablesResult result) {
                getView().setVariables(result);
            }
        });
    }

    @Override
    public void retrieveVariablesByFamily(int firstResult, int maxResults, final String criteria, String familyUrn) {
        dispatcher.execute(new GetVariablesAction(firstResult, maxResults, criteria, familyUrn), new WaitingAsyncCallback<GetVariablesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(VariableFamilyPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetVariablesResult result) {
                getView().setVariablesOfFamily(result);
            }
        });
    }

    @Override
    public void goToVariable(String code) {
        if (!StringUtils.isBlank(code)) {
            placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteVariablePlaceRequest(code));
        }
    }

    @Override
    public void saveVariableFamily(VariableFamilyDto variableFamilyDto) {
        dispatcher.execute(new SaveVariableFamilyAction(variableFamilyDto), new WaitingAsyncCallback<SaveVariableFamilyResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(VariableFamilyPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(SaveVariableFamilyResult result) {
                ShowMessageEvent.fireSuccessMessage(VariableFamilyPresenter.this, getMessages().variableFamilySaved());
                getView().setVariableFamily(result.getSavedVariableFamilyDto());

                // Update URL
                PlaceRequest placeRequest = PlaceRequestUtils.buildRelativeVariableFamilyPlaceRequest(result.getSavedVariableFamilyDto().getCode());
                placeManager.updateHistory(placeRequest, true);
            }
        });
    }

    @Override
    public void addVariablesToFamily(List<String> variableUrns, final String familyUrn) {
        dispatcher.execute(new AddVariablesToVariableFamilyAction(variableUrns, familyUrn), new WaitingAsyncCallback<AddVariablesToVariableFamilyResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(VariableFamilyPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(AddVariablesToVariableFamilyResult result) {
                ShowMessageEvent.fireSuccessMessage(VariableFamilyPresenter.this, getMessages().variablesAddedToFamily());
                retrieveVariablesByFamily(VARIABLE_LIST_FIRST_RESULT, VARIABLE_LIST_MAX_RESULTS, null, familyUrn);
            }
        });
    }

    @Override
    public void removeVariablesFromFamily(List<String> variableUrns, final String familyUrn) {
        dispatcher.execute(new RemoveVariablesFromVariableFamilyAction(variableUrns, familyUrn), new WaitingAsyncCallback<RemoveVariablesFromVariableFamilyResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(VariableFamilyPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(RemoveVariablesFromVariableFamilyResult result) {
                ShowMessageEvent.fireSuccessMessage(VariableFamilyPresenter.this, getMessages().variablesRemovedFromFamily());
                retrieveVariablesByFamily(VARIABLE_LIST_FIRST_RESULT, VARIABLE_LIST_MAX_RESULTS, null, familyUrn);
            }
        });
    }

    @Override
    public void deleteVariableFamily(String urn) {
        dispatcher.execute(new DeleteVariableFamiliesAction(Arrays.asList(urn)), new WaitingAsyncCallback<DeleteVariableFamiliesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(VariableFamilyPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(DeleteVariableFamiliesResult result) {
                ShowMessageEvent.fireSuccessMessage(VariableFamilyPresenter.this, getMessages().variableFamilyDeleted());
                placeManager.revealRelativePlace(-1);
            }
        });
    }
}
