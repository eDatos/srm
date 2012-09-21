package org.siemac.metamac.srm.web.organisation.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.organisation.view.handlers.OrganisationSchemeListUiHandlers;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;

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

    private final DispatchAsync                       dispatcher;
    private final PlaceManager                        placeManager;

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetStructuralResourcesToolBar                 = new Type<RevealContentHandler<?>>();

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

    }

    @Inject
    public OrganisationSchemeListPresenter(EventBus eventBus, OrganisationSchemeListView organisationSchemeListView, OrganisationSchemeListProxy organisationSchemeListProxy, DispatchAsync dispatcher,
            PlaceManager placeManager) {
        super(eventBus, organisationSchemeListView, organisationSchemeListProxy);
        this.dispatcher = dispatcher;
        this.placeManager = placeManager;
        getView().setUiHandlers(this);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }

    @Override
    protected void onReset() {
        super.onReset();
        SetTitleEvent.fire(this, getConstants().organisations());

        // TODO retrieve organisation schemes
    }

}
