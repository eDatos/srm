package org.siemac.metamac.srm.web.organisation.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.client.widgets.presenter.ToolStripPresenterWidget;
import org.siemac.metamac.srm.web.organisation.view.handlers.OrganisationSchemeUiHandlers;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemeAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemeResult;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.VersionTypeEnum;
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

public class OrganisationSchemePresenter extends Presenter<OrganisationSchemePresenter.OrganisationSchemeView, OrganisationSchemePresenter.OrganisationSchemeProxy>
        implements
            OrganisationSchemeUiHandlers {

    private final DispatchAsync          dispatcher;
    private final PlaceManager           placeManager;
    private ToolStripPresenterWidget     toolStripPresenterWidget;

    private OrganisationSchemeMetamacDto organisationSchemeMetamacDto;

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbOrganisationScheme();
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.organisationSchemePage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface OrganisationSchemeProxy extends Proxy<OrganisationSchemePresenter>, Place {
    }

    public interface OrganisationSchemeView extends View, HasUiHandlers<OrganisationSchemeUiHandlers> {

        void setOrganisationScheme(OrganisationSchemeMetamacDto organisationSchemeMetamacDto);
    }

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaContentOrganisationScheme        = new Type<RevealContentHandler<?>>();

    public static final Object                        TYPE_SetContextAreaContentOrganisationSchemeToolBar = new Object();

    @Inject
    public OrganisationSchemePresenter(EventBus eventBus, OrganisationSchemeView view, OrganisationSchemeProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager,
            ToolStripPresenterWidget toolStripPresenterWidget) {
        super(eventBus, view, proxy);
        this.placeManager = placeManager;
        this.dispatcher = dispatcher;
        this.toolStripPresenterWidget = toolStripPresenterWidget;
        getView().setUiHandlers(this);
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        setInSlot(TYPE_SetContextAreaContentOrganisationSchemeToolBar, toolStripPresenterWidget);
        MainPagePresenter.getMasterHead().setTitleLabel(getConstants().organisationScheme());
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        String urn = PlaceRequestUtils.getOrganisationSchemeParamFromUrl(placeManager);
        if (urn != null) {
            retrieveOrganisationSchemeByUrn(urn);
        }
    }

    private void retrieveOrganisationSchemeByUrn(String urn) {
        dispatcher.execute(new GetOrganisationSchemeAction(urn), new WaitingAsyncCallback<GetOrganisationSchemeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().organisationSchemeErrorRetrieve()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetOrganisationSchemeResult result) {
                organisationSchemeMetamacDto = result.getOrganisationSchemeMetamacDto();
                getView().setOrganisationScheme(organisationSchemeMetamacDto);
            }
        });
    }

    @Override
    public void retrieveOrganisationSchemeVersions(String organisationSchemeUrn) {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveOrganisationScheme(OrganisationSchemeMetamacDto organisationScheme) {
        // TODO Auto-generated method stub

    }

    @Override
    public void cancelValidity(String urn) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendToProductionValidation(String urn, ProcStatusEnum currentProcStatus) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendToDiffusionValidation(String urn, ProcStatusEnum currentProcStatus) {
        // TODO Auto-generated method stub

    }

    @Override
    public void rejectValidation(String urn, ProcStatusEnum currentProcStatus) {
        // TODO Auto-generated method stub

    }

    @Override
    public void publishInternally(String urn, ProcStatusEnum currentProcStatus) {
        // TODO Auto-generated method stub

    }

    @Override
    public void publishExternally(String urn, ProcStatusEnum currentProcStatus) {
        // TODO Auto-generated method stub

    }

    @Override
    public void versioning(String urn, VersionTypeEnum versionType) {
        // TODO Auto-generated method stub

    }

}
