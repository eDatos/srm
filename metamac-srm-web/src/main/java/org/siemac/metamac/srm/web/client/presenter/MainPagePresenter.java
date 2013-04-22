package org.siemac.metamac.srm.web.client.presenter;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent.SelectMenuButtonHandler;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.client.view.handlers.MainPageUiHandlers;
import org.siemac.metamac.srm.web.client.widgets.BreadCrumbsPanel;
import org.siemac.metamac.srm.web.shared.GetUserGuideUrlAction;
import org.siemac.metamac.srm.web.shared.GetUserGuideUrlResult;
import org.siemac.metamac.srm.web.shared.utils.SharedTokens;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.HideMessageEvent;
import org.siemac.metamac.web.common.client.events.HideMessageEvent.HideMessageHandler;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.SetTitleEvent.SetTitleHandler;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent.ShowMessageHandler;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.widgets.MasterHead;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;
import org.siemac.metamac.web.common.shared.CloseSessionAction;
import org.siemac.metamac.web.common.shared.CloseSessionResult;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;
import com.gwtplatform.mvp.client.proxy.SetPlaceTitleHandler;

public class MainPagePresenter extends Presenter<MainPagePresenter.MainPageView, MainPagePresenter.MainPageProxy>
        implements
            MainPageUiHandlers,
            ShowMessageHandler,
            HideMessageHandler,
            SetTitleHandler,
            SelectMenuButtonHandler {

    private static Logger       logger = Logger.getLogger(MainPagePresenter.class.getName());

    private final PlaceManager  placeManager;
    private final DispatchAsync dispatcher;

    private static MasterHead   masterHead;

    @ProxyStandard
    @NameToken(NameTokens.mainPage)
    @NoGatekeeper
    public interface MainPageProxy extends Proxy<MainPagePresenter>, Place {

    }

    public interface MainPageView extends View, HasUiHandlers<MainPageUiHandlers> {

        void setTitle(String title);

        MasterHead getMasterHead();

        BreadCrumbsPanel getBreadCrumbsPanel();
        void clearBreadcrumbs(int size, PlaceManager placeManager);
        void setBreadcrumbs(int index, String title);

        void showMessage(List<String> messages, MessageTypeEnum type);
        void hideMessages();

        void selectStructuralResourceMenuButton(ToolStripButtonEnum type);
    }

    /**
     * Use this in leaf presenters, inside their {@link #revealInParent} method.
     * Is used to define a type to use in child presenters when you want to
     * include them inside this page.
     */
    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaContent = new Type<RevealContentHandler<?>>();

    @Inject
    public MainPagePresenter(EventBus eventBus, MainPageView view, MainPageProxy proxy, PlaceManager placeManager, DispatchAsync dispatcher) {
        super(eventBus, view, proxy);
        getView().setUiHandlers(this);
        this.placeManager = placeManager;
        this.dispatcher = dispatcher;
        MainPagePresenter.masterHead = getView().getMasterHead();
    }

    @Override
    protected void onBind() {
        super.onBind();
    }

    @Override
    protected void onReveal() {
        super.onReveal();
    }

    @Override
    protected void onReset() {
        super.onReset();
        hideMessages();
        int size = placeManager.getHierarchyDepth();
        getView().clearBreadcrumbs(size, placeManager);
        for (int i = 0; i < size; ++i) {
            final int index = i;
            placeManager.getTitle(i, new SetPlaceTitleHandler() {

                @Override
                public void onSetPlaceTitle(String title) {
                    getView().setBreadcrumbs(index, title);
                }
            });
        }
    }

    @Override
    protected void revealInParent() {
        RevealRootContentEvent.fire(this, this);
    }

    @Override
    public void onNavigationPaneSectionHeaderClicked(String place) {
        if (place.length() != 0) {
            PlaceRequest placeRequest = new PlaceRequest(place);
            placeManager.revealPlace(placeRequest);
        }
    }

    @Override
    public void onNavigationPaneSectionClicked(String place) {
        if (place.length() != 0) {
            PlaceRequest placeRequest = new PlaceRequest(place);
            placeManager.revealPlace(placeRequest);
        }
    }

    @ProxyEvent
    @Override
    public void onShowMessage(ShowMessageEvent event) {
        getView().showMessage(event.getMessages(), event.getMessageType());
    }

    @ProxyEvent
    @Override
    public void onHideMessage(HideMessageEvent event) {
        hideMessages();
    }

    @ProxyEvent
    @Override
    public void onSetTitle(SetTitleEvent event) {
        getView().setTitle(event.getTitle());
    }

    @ProxyEvent
    @Override
    public void onSelectMenuButton(SelectMenuButtonEvent event) {
        getView().selectStructuralResourceMenuButton(event.getResourceType());
    }

    public static MasterHead getMasterHead() {
        return masterHead;
    }

    private void hideMessages() {
        getView().hideMessages();
    }

    @Override
    public void closeSession() {
        dispatcher.execute(new CloseSessionAction(), new AsyncCallback<CloseSessionResult>() {

            @Override
            public void onFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error closing session");
            }
            @Override
            public void onSuccess(CloseSessionResult result) {
                Window.Location.assign(result.getLogoutPageUrl());
            }
        });
    }

    @Override
    public void downloadUserGuide() {
        dispatcher.execute(new GetUserGuideUrlAction(), new WaitingAsyncCallback<GetUserGuideUrlResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(MainPagePresenter.this, ErrorUtils.getErrorMessages(caught, MetamacWebCommon.getMessages().errorDownloadingUserGuide()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetUserGuideUrlResult result) {
                CommonWebUtils.showDownloadFileWindow(SharedTokens.FILE_DOWNLOAD_DIR_PATH, SharedTokens.PARAM_FILE_NAME, result.getUserGuideUrl());
            }
        });
    }

    //
    // IMPORTATION
    //

    @Override
    public void sDMXResourceImportationFailed(String fileName) {
        ShowMessageEvent.fire(MainPagePresenter.this, ErrorUtils.getMessageList(MetamacSrmWeb.getMessages().sDMXResourceErrorImport()), MessageTypeEnum.ERROR);
    }

    @Override
    public void sDMXResourceImportationSucceed(String fileName) {
        ShowMessageEvent.fire(MainPagePresenter.this, ErrorUtils.getMessageList(MetamacSrmWeb.getMessages().sDMXResourceImportationPlanned()), MessageTypeEnum.SUCCESS);
    }

    //
    // PLACES
    //

    @Override
    public void goToConcepts() {
        placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteConceptSchemesPlaceRequest());
    }

    @Override
    public void goToCodelists() {
        placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteCodelistsPlaceRequest());
    }

    @Override
    public void goToDsds() {
        placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteDsdsPlaceRequest());
    }

    @Override
    public void goToOrganisations() {
        placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteOrganisationSchemesPlaceRequest());
    }

    @Override
    public void goToCategories() {
        placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteCategorySchemesPlaceRequest());
    }
}
