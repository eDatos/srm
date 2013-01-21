package org.siemac.metamac.srm.web.client.code.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.code.view.handlers.VariableElementUiHandlers;
import org.siemac.metamac.srm.web.client.code.widgets.CodesToolStripPresenterWidget;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementAction;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementResult;
import org.siemac.metamac.srm.web.shared.code.GetVariablesAction;
import org.siemac.metamac.srm.web.shared.code.GetVariablesResult;
import org.siemac.metamac.srm.web.shared.code.SaveVariableElementAction;
import org.siemac.metamac.srm.web.shared.code.SaveVariableElementResult;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.UrnUtils;
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

public class VariableElementPresenter extends Presenter<VariableElementPresenter.VariableElementView, VariableElementPresenter.VariableElementProxy> implements VariableElementUiHandlers {

    private final DispatchAsync           dispatcher;
    private final PlaceManager            placeManager;

    private CodesToolStripPresenterWidget codesToolStripPresenterWidget;

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbVariableElement();
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.variableElementPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface VariableElementProxy extends Proxy<VariableElementPresenter>, Place {
    }

    public interface VariableElementView extends View, HasUiHandlers<VariableElementUiHandlers> {

        void setVariableElement(VariableElementDto variableElementDto);
        void setVariables(GetVariablesResult result);
    }

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaContentCode         = new Type<RevealContentHandler<?>>();

    public static final Object                        TYPE_SetContextAreaContentCodesToolBar = new Object();

    @Inject
    public VariableElementPresenter(EventBus eventBus, VariableElementView view, VariableElementProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager,
            CodesToolStripPresenterWidget codesToolStripPresenterWidget) {
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

        SetTitleEvent.fire(this, MetamacSrmWeb.getConstants().variableElement());
        SelectMenuButtonEvent.fire(this, ToolStripButtonEnum.CODELISTS);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        String variableIdentifier = PlaceRequestUtils.getVariableParamFromUrl(placeManager);
        String elementIdentifier = PlaceRequestUtils.getVariableElementParamFromUrl(placeManager);
        if (!StringUtils.isBlank(variableIdentifier) && !StringUtils.isBlank(elementIdentifier)) {
            retrieveVariableElement(variableIdentifier, elementIdentifier);
        } else {
            MetamacSrmWeb.showErrorPage();
        }
    }

    private void retrieveVariableElement(String variableIdentifier, String elementIdentifier) {
        String urn = UrnUtils.generateUrn(UrnConstants.URN_SIEMAC_CLASS_CODELIST_VARIABLE_ELEMENT_PREFIX, variableIdentifier, elementIdentifier);
        if (!StringUtils.isBlank(urn)) {
            retrieveVariableElementByUrn(urn);
        }
    }

    private void retrieveVariableElementByUrn(String urn) {
        dispatcher.execute(new GetVariableElementAction(urn), new WaitingAsyncCallback<GetVariableElementResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(VariableElementPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().variableElementErrorRetrieve()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetVariableElementResult result) {
                getView().setVariableElement(result.getVariableElementDto());
            }
        });
    }

    @Override
    public void saveVariableElement(VariableElementDto variableElementDto, final boolean updatePlaceRequestHierarchy) {
        dispatcher.execute(new SaveVariableElementAction(variableElementDto), new WaitingAsyncCallback<SaveVariableElementResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(VariableElementPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().variableElementErrorSave()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(SaveVariableElementResult result) {
                ShowMessageEvent.fire(VariableElementPresenter.this, ErrorUtils.getMessageList(getMessages().variableElementSaved()), MessageTypeEnum.SUCCESS);
                VariableElementDto variableElementDto = result.getSavedVariableElementDto();
                getView().setVariableElement(variableElementDto);

                if (updatePlaceRequestHierarchy) {
                    placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteVariableElementPlaceRequest(variableElementDto.getVariable().getCode(), variableElementDto.getCode()));
                } else {
                    // Update URL
                    PlaceRequest placeRequest = PlaceRequestUtils.buildRelativeVariablePlaceRequest(result.getSavedVariableElementDto().getCode());
                    placeManager.updateHistory(placeRequest, true);
                }
            }
        });
    }

    @Override
    public void retrieveVariables(int firstResult, int maxResults, String criteria) {
        dispatcher.execute(new GetVariablesAction(firstResult, maxResults, criteria, null), new WaitingAsyncCallback<GetVariablesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(VariableElementPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().variableErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetVariablesResult result) {
                getView().setVariables(result);
            }
        });
    }
}
