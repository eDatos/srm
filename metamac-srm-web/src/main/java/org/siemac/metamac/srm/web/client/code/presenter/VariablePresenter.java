package org.siemac.metamac.srm.web.client.code.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.code.view.handlers.VariableUiHandlers;
import org.siemac.metamac.srm.web.client.code.widgets.CodesToolStripPresenterWidget;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.shared.code.GetVariableAction;
import org.siemac.metamac.srm.web.shared.code.GetVariableFamiliesAction;
import org.siemac.metamac.srm.web.shared.code.GetVariableFamiliesResult;
import org.siemac.metamac.srm.web.shared.code.GetVariableResult;
import org.siemac.metamac.srm.web.shared.code.SaveVariableAction;
import org.siemac.metamac.srm.web.shared.code.SaveVariableResult;
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

public class VariablePresenter extends Presenter<VariablePresenter.VariableView, VariablePresenter.VariableProxy> implements VariableUiHandlers {

    private final DispatchAsync           dispatcher;
    private final PlaceManager            placeManager;

    private CodesToolStripPresenterWidget codesToolStripPresenterWidget;

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbVariable();
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.variablePage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface VariableProxy extends Proxy<VariablePresenter>, Place {
    }

    public interface VariableView extends View, HasUiHandlers<VariableUiHandlers> {

        void setVariable(VariableDto variableFamilyDto);
        void setVariableFamilies(GetVariableFamiliesResult result);
    }

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaContentCodelist     = new Type<RevealContentHandler<?>>();

    public static final Object                        TYPE_SetContextAreaContentCodesToolBar = new Object();

    @Inject
    public VariablePresenter(EventBus eventBus, VariableView view, VariableProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager, CodesToolStripPresenterWidget codesToolStripPresenterWidget) {
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

        SetTitleEvent.fire(this, getConstants().variable());
        SelectMenuButtonEvent.fire(this, ToolStripButtonEnum.CODELISTS);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        String identifier = PlaceRequestUtils.getVariableParamFromUrl(placeManager);
        if (!StringUtils.isBlank(identifier)) {
            retrieveVariable(identifier);
        } else {
            MetamacSrmWeb.showErrorPage();
        }
    }

    private void retrieveVariable(String identifier) {
        // TODO Variable URN
        String urn = "urn:" + identifier;
        retrieveVariableByUrn(urn);
    }

    @Override
    public void retrieveVariableByUrn(String urn) {
        dispatcher.execute(new GetVariableAction(urn), new WaitingAsyncCallback<GetVariableResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(VariablePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().variableErrorRetrieve()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetVariableResult result) {
                getView().setVariable(result.getVariableDto());
            }
        });
    }

    @Override
    public void retrieveVariableFamilies(int firstResult, int maxResults, String criteria) {
        dispatcher.execute(new GetVariableFamiliesAction(firstResult, maxResults, criteria), new WaitingAsyncCallback<GetVariableFamiliesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(VariablePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().variableFamilyErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetVariableFamiliesResult result) {
                getView().setVariableFamilies(result);
            }
        });
    }

    @Override
    public void saveVariable(VariableDto variableDto) {
        dispatcher.execute(new SaveVariableAction(variableDto), new WaitingAsyncCallback<SaveVariableResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(VariablePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().variableErrorSave()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(SaveVariableResult result) {
                ShowMessageEvent.fire(VariablePresenter.this, ErrorUtils.getMessageList(getMessages().variableSaved()), MessageTypeEnum.SUCCESS);
                getView().setVariable(result.getSavedVariableDto());

                // Update URL
                PlaceRequest placeRequest = PlaceRequestUtils.buildRelativeVariablePlaceRequest(result.getSavedVariableDto().getCode());
                placeManager.updateHistory(placeRequest, true);
            }
        });
    }
}
