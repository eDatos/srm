package org.siemac.metamac.srm.web.code.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.utils.MetamacWebCriteriaClientUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.code.utils.CodesToolStripButtonEnum;
import org.siemac.metamac.srm.web.code.view.handlers.CodesUiHandlers;
import org.siemac.metamac.srm.web.code.widgets.CodesToolStripPresenterWidget;
import org.siemac.metamac.srm.web.shared.code.GetCodesAction;
import org.siemac.metamac.srm.web.shared.code.GetCodesResult;
import org.siemac.metamac.srm.web.shared.criteria.CodeWebCriteria;
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

public class CodesPresenter extends Presenter<CodesPresenter.CodesView, CodesPresenter.CodesProxy> implements CodesUiHandlers {

    public final static int                           ITEM_LIST_FIRST_RESULT                 = 0;
    public final static int                           ITEM_LIST_MAX_RESULTS                  = 30;

    private final DispatchAsync                       dispatcher;
    private final PlaceManager                        placeManager;

    private CodesToolStripPresenterWidget             codesToolStripPresenterWidget;

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetStructuralResourcesToolBar     = new Type<RevealContentHandler<?>>();

    public static final Object                        TYPE_SetContextAreaContentCodesToolBar = new Object();

    @ProxyCodeSplit
    @NameToken(NameTokens.codesPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface CodesProxy extends Proxy<CodesPresenter>, Place {
    }

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbCodes();
    }

    public interface CodesView extends View, HasUiHandlers<CodesUiHandlers> {

        void setCodes(GetCodesResult result);
        void clearSearchSection();
    }

    @Inject
    public CodesPresenter(EventBus eventBus, CodesView codesView, CodesProxy codesProxy, DispatchAsync dispatcher, PlaceManager placeManager,
            CodesToolStripPresenterWidget codesToolStripPresenterWidget) {
        super(eventBus, codesView, codesProxy);
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
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        // Load concept schemes
        retrieveCodes(ITEM_LIST_FIRST_RESULT, ITEM_LIST_MAX_RESULTS, MetamacWebCriteriaClientUtils.getCodeWebCriteriaForLastVersion());
        // Clear search section
        getView().clearSearchSection();
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        setInSlot(TYPE_SetContextAreaContentCodesToolBar, codesToolStripPresenterWidget);

        SetTitleEvent.fire(this, getConstants().codes());
        SelectMenuButtonEvent.fire(this, ToolStripButtonEnum.CODELISTS);
        codesToolStripPresenterWidget.selectCodesMenuButton(CodesToolStripButtonEnum.CODES);
    }

    @Override
    public void retrieveCodes(int firstResult, int maxResults, CodeWebCriteria codeWebCriteria) {
        dispatcher.execute(new GetCodesAction(firstResult, maxResults, codeWebCriteria), new WaitingAsyncCallback<GetCodesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(CodesPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().codeErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCodesResult result) {
                getView().setCodes(result);
            }
        });
    }

    @Override
    public void goToCode(String codelistUrn, String codeUrn) {
        if (!StringUtils.isBlank(codelistUrn) && !StringUtils.isBlank(codeUrn)) {
            placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteCodePlaceRequest(codelistUrn, codeUrn));
        }
    }
}
