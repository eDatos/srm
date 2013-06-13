package org.siemac.metamac.srm.web.code.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.List;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementOperationDto;
import org.siemac.metamac.srm.core.code.enume.domain.VariableElementOperationTypeEnum;
import org.siemac.metamac.srm.navigation.shared.NameTokens;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.code.view.handlers.VariableElementUiHandlers;
import org.siemac.metamac.srm.web.code.widgets.presenter.CodesToolStripPresenterWidget;
import org.siemac.metamac.srm.web.shared.code.CreateVariableElementOperationAction;
import org.siemac.metamac.srm.web.shared.code.CreateVariableElementOperationResult;
import org.siemac.metamac.srm.web.shared.code.DeleteVariableElementOperationsAction;
import org.siemac.metamac.srm.web.shared.code.DeleteVariableElementOperationsResult;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementAction;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementOperationsByVariableElementAction;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementOperationsByVariableElementResult;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementResult;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementsAction;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementsResult;
import org.siemac.metamac.srm.web.shared.code.SaveVariableElementAction;
import org.siemac.metamac.srm.web.shared.code.SaveVariableElementResult;
import org.siemac.metamac.srm.web.shared.criteria.VariableElementWebCriteria;
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

public class VariableElementPresenter extends Presenter<VariableElementPresenter.VariableElementView, VariableElementPresenter.VariableElementProxy> implements VariableElementUiHandlers {

    private final DispatchAsync           dispatcher;
    private final PlaceManager            placeManager;

    private CodesToolStripPresenterWidget codesToolStripPresenterWidget;

    private VariableElementDto            variableElementDto;

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

        void setVariableElementsForReplaceTo(GetVariableElementsResult result);
        void setVariableElementsForSegregation(GetVariableElementsResult result);

        void setVariableElementOperations(List<VariableElementOperationDto> variableElementOperationDtos);
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
            retrieveVariableElementOperations(urn);
        }
    }

    private void retrieveVariableElementByUrn(String urn) {
        dispatcher.execute(new GetVariableElementAction(urn), new WaitingAsyncCallback<GetVariableElementResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(VariableElementPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetVariableElementResult result) {
                VariableElementPresenter.this.variableElementDto = result.getVariableElementDto();
                getView().setVariableElement(result.getVariableElementDto());
            }
        });
    }

    @Override
    public void saveVariableElement(VariableElementDto variableElementDto) {
        dispatcher.execute(new SaveVariableElementAction(variableElementDto), new WaitingAsyncCallback<SaveVariableElementResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(VariableElementPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(SaveVariableElementResult result) {
                VariableElementPresenter.this.variableElementDto = result.getSavedVariableElementDto();
                ShowMessageEvent.fireSuccessMessage(VariableElementPresenter.this, getMessages().variableElementSaved());
                VariableElementDto variableElementDto = result.getSavedVariableElementDto();
                getView().setVariableElement(variableElementDto);

                // Update URL
                PlaceRequest placeRequest = PlaceRequestUtils.buildRelativeVariableElementPlaceRequest(result.getSavedVariableElementDto().getCode());
                placeManager.updateHistory(placeRequest, true);
            }
        });
    }

    @Override
    public void retrieveVariableElementsByVariableForReplaceTo(int firstResult, int maxResults, String criteria, String variableUrn) {
        VariableElementWebCriteria variableElementWebCriteria = new VariableElementWebCriteria(criteria);
        variableElementWebCriteria.setVariableUrn(variableUrn);
        dispatcher.execute(new GetVariableElementsAction(firstResult, maxResults, variableElementWebCriteria), new WaitingAsyncCallback<GetVariableElementsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(VariableElementPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetVariableElementsResult result) {
                getView().setVariableElementsForReplaceTo(result);
            }
        });
    }

    @Override
    public void retrieveVariableElementsByVariableForSegregationOperation(int firstResult, int maxResults, String criteria, String variableUrn) {
        VariableElementWebCriteria variableElementWebCriteria = new VariableElementWebCriteria(criteria);
        variableElementWebCriteria.setVariableUrn(variableUrn);
        dispatcher.execute(new GetVariableElementsAction(firstResult, maxResults, variableElementWebCriteria), new WaitingAsyncCallback<GetVariableElementsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(VariableElementPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetVariableElementsResult result) {
                getView().setVariableElementsForSegregation(result);
            }
        });
    }

    @Override
    public void createSegregation(String variableElementUrn, List<String> variableElementUrns) {
        dispatcher.execute(new CreateVariableElementOperationAction(VariableElementOperationTypeEnum.SEGREGATION, variableElementUrns, variableElementUrn),
                new WaitingAsyncCallback<CreateVariableElementOperationResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fireErrorMessage(VariableElementPresenter.this, caught);
                    }
                    @Override
                    public void onWaitSuccess(CreateVariableElementOperationResult result) {
                        ShowMessageEvent.fireSuccessMessage(VariableElementPresenter.this, MetamacSrmWeb.getMessages().variableElementSegregated());
                        retrieveVariableElementOperations(variableElementDto.getUrn());
                    }
                });
    }

    @Override
    public void createFusion(List<String> variableElementUrn, String variableElementUrns) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteVariableElementOperations(List<String> codes) {
        dispatcher.execute(new DeleteVariableElementOperationsAction(codes), new WaitingAsyncCallback<DeleteVariableElementOperationsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(VariableElementPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(DeleteVariableElementOperationsResult result) {
                retrieveVariableElementOperations(variableElementDto.getUrn());
            }
        });
    }

    private void retrieveVariableElementOperations(String variableElementUrn) {
        dispatcher.execute(new GetVariableElementOperationsByVariableElementAction(variableElementUrn), new WaitingAsyncCallback<GetVariableElementOperationsByVariableElementResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(VariableElementPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetVariableElementOperationsByVariableElementResult result) {
                getView().setVariableElementOperations(result.getOperations());
            }
        });
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
}
