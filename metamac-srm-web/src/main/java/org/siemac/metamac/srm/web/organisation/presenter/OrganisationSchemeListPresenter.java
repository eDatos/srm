package org.siemac.metamac.srm.web.organisation.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.client.widgets.presenter.ToolStripPresenterWidget;
import org.siemac.metamac.srm.web.organisation.view.handlers.OrganisationSchemeListUiHandlers;
import org.siemac.metamac.srm.web.shared.organisation.CancelOrganisationSchemeValidityAction;
import org.siemac.metamac.srm.web.shared.organisation.CancelOrganisationSchemeValidityResult;
import org.siemac.metamac.srm.web.shared.organisation.DeleteOrganisationSchemeListAction;
import org.siemac.metamac.srm.web.shared.organisation.DeleteOrganisationSchemeListResult;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemeListAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemeListResult;
import org.siemac.metamac.srm.web.shared.organisation.SaveOrganisationSchemeAction;
import org.siemac.metamac.srm.web.shared.organisation.SaveOrganisationSchemeResult;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
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

public class OrganisationSchemeListPresenter extends Presenter<OrganisationSchemeListPresenter.OrganisationSchemeListView, OrganisationSchemeListPresenter.OrganisationSchemeListProxy>
        implements
            OrganisationSchemeListUiHandlers {

    public final static int                           SCHEME_LIST_FIRST_RESULT                                = 0;
    public final static int                           SCHEME_LIST_MAX_RESULTS                                 = 30;

    private final DispatchAsync                       dispatcher;
    private final PlaceManager                        placeManager;

    private ToolStripPresenterWidget                  toolStripPresenterWidget;

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetStructuralResourcesToolBar                      = new Type<RevealContentHandler<?>>();

    public static final Object                        TYPE_SetContextAreaContentOrganisationSchemeListToolBar = new Object();

    @ProxyCodeSplit
    @NameToken(NameTokens.organisationSchemeListPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface OrganisationSchemeListProxy extends Proxy<OrganisationSchemeListPresenter>, Place {

    }

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbOrganisationSchemeList();
    }

    public interface OrganisationSchemeListView extends View, HasUiHandlers<OrganisationSchemeListUiHandlers> {

        void setOrganisationSchemesPaginatedList(GetOrganisationSchemeListResult result);
        void clearSearchSection();
    }

    @Inject
    public OrganisationSchemeListPresenter(EventBus eventBus, OrganisationSchemeListView organisationSchemeListView, OrganisationSchemeListProxy organisationSchemeListProxy, DispatchAsync dispatcher,
            PlaceManager placeManager, ToolStripPresenterWidget toolStripPresenterWidget) {
        super(eventBus, organisationSchemeListView, organisationSchemeListProxy);
        this.dispatcher = dispatcher;
        this.placeManager = placeManager;
        this.toolStripPresenterWidget = toolStripPresenterWidget;
        getView().setUiHandlers(this);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }

    @Override
    protected void onReset() {
        super.onReset();
        SetTitleEvent.fire(this, getConstants().organisationSchemes());
        retrieveOrganisationSchemes(SCHEME_LIST_FIRST_RESULT, SCHEME_LIST_MAX_RESULTS, null);
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        setInSlot(TYPE_SetContextAreaContentOrganisationSchemeListToolBar, toolStripPresenterWidget);
    }

    @Override
    public void goToOrganisationScheme(String urn, OrganisationSchemeTypeEnum type) {
        if (!StringUtils.isBlank(urn)) {
            placeManager.revealRelativePlace(PlaceRequestUtils.buildOrganisationSchemePlaceRequest(urn, type));
        }
    }

    @Override
    public void createOrganisationScheme(OrganisationSchemeMetamacDto organisationSchemeMetamacDto) {
        dispatcher.execute(new SaveOrganisationSchemeAction(organisationSchemeMetamacDto), new WaitingAsyncCallback<SaveOrganisationSchemeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(OrganisationSchemeListPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().organisationSchemeErrorSave()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(SaveOrganisationSchemeResult result) {
                ShowMessageEvent.fire(OrganisationSchemeListPresenter.this, ErrorUtils.getMessageList(getMessages().organisationSchemeSaved()), MessageTypeEnum.SUCCESS);
                retrieveOrganisationSchemes(SCHEME_LIST_FIRST_RESULT, SCHEME_LIST_MAX_RESULTS);
            }
        });
    }

    @Override
    public void deleteOrganisationSchemes(List<String> urns) {
        dispatcher.execute(new DeleteOrganisationSchemeListAction(urns), new WaitingAsyncCallback<DeleteOrganisationSchemeListResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(OrganisationSchemeListPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().organisationSchemeErrorDelete()), MessageTypeEnum.ERROR);
                retrieveOrganisationSchemes(SCHEME_LIST_FIRST_RESULT, SCHEME_LIST_MAX_RESULTS);
            }
            @Override
            public void onWaitSuccess(DeleteOrganisationSchemeListResult result) {
                ShowMessageEvent.fire(OrganisationSchemeListPresenter.this, ErrorUtils.getMessageList(getMessages().organisationSchemeDeleted()), MessageTypeEnum.SUCCESS);
                retrieveOrganisationSchemes(SCHEME_LIST_FIRST_RESULT, SCHEME_LIST_MAX_RESULTS);
            }
        });
    }

    @Override
    public void retrieveOrganisationSchemes(int firstResult, int maxResults) {
        retrieveOrganisationSchemes(firstResult, maxResults, null);
    }

    @Override
    public void retrieveOrganisationSchemes(int firstResult, int maxResults, final String organisationScheme) {
        dispatcher.execute(new GetOrganisationSchemeListAction(firstResult, maxResults, organisationScheme), new WaitingAsyncCallback<GetOrganisationSchemeListResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(OrganisationSchemeListPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().organisationSchemeErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetOrganisationSchemeListResult result) {
                getView().setOrganisationSchemesPaginatedList(result);
                if (StringUtils.isBlank(organisationScheme)) {
                    getView().clearSearchSection();
                }
            }
        });
    }

    @Override
    public void cancelValidity(List<String> urns) {
        dispatcher.execute(new CancelOrganisationSchemeValidityAction(urns), new WaitingAsyncCallback<CancelOrganisationSchemeValidityResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(OrganisationSchemeListPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().organisationSchemeErrorCancelValidity()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(CancelOrganisationSchemeValidityResult result) {
                ShowMessageEvent.fire(OrganisationSchemeListPresenter.this, ErrorUtils.getMessageList(getMessages().organisationSchemeCanceledValidity()), MessageTypeEnum.SUCCESS);
                retrieveOrganisationSchemes(SCHEME_LIST_FIRST_RESULT, SCHEME_LIST_MAX_RESULTS);
            }
        });
    }

}
