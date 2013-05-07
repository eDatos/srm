package org.siemac.metamac.srm.web.organisation.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.navigation.shared.NameTokens;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.utils.MetamacWebCriteriaClientUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.organisation.enums.OrganisationsToolStripButtonEnum;
import org.siemac.metamac.srm.web.organisation.utils.CommonUtils;
import org.siemac.metamac.srm.web.organisation.view.handlers.OrganisationsUiHandlers;
import org.siemac.metamac.srm.web.organisation.widgets.presenter.OrganisationsToolStripPresenterWidget;
import org.siemac.metamac.srm.web.shared.criteria.OrganisationWebCriteria;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationsAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationsResult;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;
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

public class OrganisationsPresenter extends Presenter<OrganisationsPresenter.OrganisationsView, OrganisationsPresenter.OrganisationsProxy> implements OrganisationsUiHandlers {

    public final static int                           ITEM_LIST_FIRST_RESULT                         = 0;
    public final static int                           ITEM_LIST_MAX_RESULTS                          = 30;

    private final DispatchAsync                       dispatcher;
    private final PlaceManager                        placeManager;

    private OrganisationsToolStripPresenterWidget     organisationsToolStripPresenterWidget;

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetStructuralResourcesToolBar             = new Type<RevealContentHandler<?>>();

    public static final Object                        TYPE_SetContextAreaContentOrganisationsToolBar = new Object();

    @ProxyCodeSplit
    @NameToken(NameTokens.organisationsPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface OrganisationsProxy extends Proxy<OrganisationsPresenter>, Place {
    }

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbOrganisations();
    }

    public interface OrganisationsView extends View, HasUiHandlers<OrganisationsUiHandlers> {

        void setOrganisations(GetOrganisationsResult result);
        void clearSearchSection();
    }

    @Inject
    public OrganisationsPresenter(EventBus eventBus, OrganisationsView organisationsView, OrganisationsProxy organisationsProxy, DispatchAsync dispatcher, PlaceManager placeManager,
            OrganisationsToolStripPresenterWidget organisationsToolStripPresenterWidget) {
        super(eventBus, organisationsView, organisationsProxy);
        this.placeManager = placeManager;
        this.dispatcher = dispatcher;
        this.organisationsToolStripPresenterWidget = organisationsToolStripPresenterWidget;
        getView().setUiHandlers(this);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        // Load organisations
        retrieveOrganisations(ITEM_LIST_FIRST_RESULT, ITEM_LIST_MAX_RESULTS, MetamacWebCriteriaClientUtils.addLastVersionConditionToOrganisationWebCriteria(new OrganisationWebCriteria()));
        // Clear search section
        getView().clearSearchSection();
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        setInSlot(TYPE_SetContextAreaContentOrganisationsToolBar, organisationsToolStripPresenterWidget);

        SetTitleEvent.fire(this, getConstants().organisations());
        SelectMenuButtonEvent.fire(this, ToolStripButtonEnum.ORGANISATIONS);
        organisationsToolStripPresenterWidget.selectOrganisationsMenuButton(OrganisationsToolStripButtonEnum.ORGANISATIONS);
    }

    @Override
    public void retrieveOrganisations(int firstResult, int maxResults, OrganisationWebCriteria organisationWebCriteria) {
        dispatcher.execute(new GetOrganisationsAction(firstResult, maxResults, organisationWebCriteria), new WaitingAsyncCallback<GetOrganisationsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(OrganisationsPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().organisationErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetOrganisationsResult result) {
                getView().setOrganisations(result);
            }
        });
    }

    @Override
    public void goToOrganisation(String organisationSchemeUrn, String organisationUrn, OrganisationTypeEnum organisationTypeEnum) {
        if (!StringUtils.isBlank(organisationSchemeUrn) && !StringUtils.isBlank(organisationUrn)) {
            OrganisationSchemeTypeEnum organisationSchemeTypeEnum = CommonUtils.getOrganisationSchemeTypeEnum(organisationTypeEnum);
            placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteOrganisationPlaceRequest(organisationSchemeUrn, organisationUrn, organisationSchemeTypeEnum));
        }
    }
}
