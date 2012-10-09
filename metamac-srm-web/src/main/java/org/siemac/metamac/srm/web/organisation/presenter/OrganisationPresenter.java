package org.siemac.metamac.srm.web.organisation.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.widgets.presenter.ToolStripPresenterWidget;
import org.siemac.metamac.srm.web.organisation.view.handlers.OrganisationUiHandlers;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationResult;
import org.siemac.metamac.srm.web.shared.organisation.SaveOrganisationAction;
import org.siemac.metamac.srm.web.shared.organisation.SaveOrganisationResult;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

import com.arte.statistic.sdmx.v2_1.domain.dto.organisation.OrganisationDto;
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

public class OrganisationPresenter extends Presenter<OrganisationPresenter.OrganisationView, OrganisationPresenter.OrganisationProxy> implements OrganisationUiHandlers {

    private final DispatchAsync      dispatcher;
    private final PlaceManager       placeManager;
    private ToolStripPresenterWidget toolStripPresenterWidget;

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbOrganisation();
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.organisationPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface OrganisationProxy extends Proxy<OrganisationPresenter>, Place {
    }

    public interface OrganisationView extends View, HasUiHandlers<OrganisationUiHandlers> {

        void setOrganisation(OrganisationDto organisationDto);
    }

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaContentOrganisation        = new Type<RevealContentHandler<?>>();

    public static final Object                        TYPE_SetContextAreaContentOrganisationToolBar = new Object();

    @Inject
    public OrganisationPresenter(EventBus eventBus, OrganisationView view, OrganisationProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager,
            ToolStripPresenterWidget toolStripPresenterWidget) {
        super(eventBus, view, proxy);
        this.placeManager = placeManager;
        this.dispatcher = dispatcher;
        this.toolStripPresenterWidget = toolStripPresenterWidget;
        getView().setUiHandlers(this);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        setInSlot(TYPE_SetContextAreaContentOrganisationToolBar, toolStripPresenterWidget);
        MainPagePresenter.getMasterHead().setTitleLabel(getConstants().organisation());
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        // String schemeParam = PlaceRequestUtils.getOrganisationSchemeParamFromUrl(placeManager);
        // String organisationCode = PlaceRequestUtils.getOrganisationParamFromUrl(placeManager);
        // if (!StringUtils.isBlank(schemeParam) && !StringUtils.isBlank(organisationCode)) {
        // this.organisationSchemeUrn = UrnUtils.generateUrn(UrnConstants.URN_SDMX_CLASS_ORGANISATIONSCHEME_PREFIX, schemeParam);
        // String urn = UrnUtils.generateUrn(UrnConstants.URN_SDMX_CLASS_ORGANISATION_PREFIX, schemeParam, organisationCode);
        retrieveOrganisation("TODO");
        // }
    }

    @Override
    public void retrieveOrganisation(String organisationUrn) {
        dispatcher.execute(new GetOrganisationAction(organisationUrn), new WaitingAsyncCallback<GetOrganisationResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(OrganisationPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().organisationErrorRetrieve()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetOrganisationResult result) {
                getView().setOrganisation(result.getOrganisationDto());
            }
        });
    }

    @Override
    public void saveOrganisation(OrganisationDto organisationDto) {
        dispatcher.execute(new SaveOrganisationAction(organisationDto), new WaitingAsyncCallback<SaveOrganisationResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(OrganisationPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().organisationErrorSave()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(SaveOrganisationResult result) {
                ShowMessageEvent.fire(OrganisationPresenter.this, ErrorUtils.getMessageList(getMessages().organisationSchemeSaved()), MessageTypeEnum.SUCCESS);
                getView().setOrganisation(result.getOrganisationSaved());
            }
        });
    }

}
