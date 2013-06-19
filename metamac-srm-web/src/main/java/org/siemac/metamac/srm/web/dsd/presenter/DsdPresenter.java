package org.siemac.metamac.srm.web.dsd.presenter;

import java.util.List;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacBasicDto;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.navigation.shared.NameTokens;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.events.UpdateMaintainableArtefactEvent;
import org.siemac.metamac.srm.web.client.events.UpdateMaintainableArtefactEvent.UpdateMaintainableArtefactHandler;
import org.siemac.metamac.srm.web.client.events.UpdateMaintainableArtefactVersionsEvent;
import org.siemac.metamac.srm.web.client.events.UpdateMaintainableArtefactVersionsEvent.UpdateMaintainableArtefactVersionsHandler;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.dsd.enums.DsdTabTypeEnum;
import org.siemac.metamac.srm.web.dsd.events.SelectViewDsdDescriptorEvent;
import org.siemac.metamac.srm.web.dsd.events.SelectViewDsdDescriptorEvent.SelectViewDsdDescriptorHandler;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdUiHandlers;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdResult;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdVersionsAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdVersionsResult;
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
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.TitleFunction;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.HasTabSelectedHandlers;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

public class DsdPresenter extends Presenter<DsdPresenter.DsdView, DsdPresenter.DsdProxy>
        implements
            DsdUiHandlers,
            SelectViewDsdDescriptorHandler,
            UpdateMaintainableArtefactVersionsHandler,
            UpdateMaintainableArtefactHandler {

    private final DispatchAsync                       dispatcher;
    private final PlaceManager                        placeManager;

    private String                                    dsdUrn;

    /**
     * Use this in leaf presenters, inside their {@link #revealInParent} method.
     */
    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaContentDsd = new Type<RevealContentHandler<?>>();

    @ProxyCodeSplit
    @NameToken(NameTokens.dsdPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface DsdProxy extends Proxy<DsdPresenter>, Place {

    }

    @TitleFunction
    public static String getTranslatedTitle(PlaceRequest placeRequest) {
        return PlaceRequestUtils.getDsdBreadCrumbTitle(placeRequest);
    }

    public interface DsdView extends View, HasUiHandlers<DsdUiHandlers> {

        void setDsd(DataStructureDefinitionMetamacDto dataStructureDefinitionDto);
        void setDsdVersions(List<DataStructureDefinitionMetamacBasicDto> dataStructureDefinitionMetamacDtos);

        TabSet getDsdTabSet();
        HasTabSelectedHandlers getGeneralTab();
        HasTabSelectedHandlers getPrimaryMeasureTab();
        HasTabSelectedHandlers getDimensionsTab();
        HasTabSelectedHandlers getAttributesTab();
        HasTabSelectedHandlers getGroupKeysTab();
        HasTabSelectedHandlers getCategorisationsTab();
    }

    @Inject
    public DsdPresenter(EventBus eventBus, DsdView dsdView, DsdProxy dsdProxy, PlaceManager placeManager, DispatchAsync dispatcher) {
        super(eventBus, dsdView, dsdProxy);
        this.placeManager = placeManager;
        this.dispatcher = dispatcher;
        getView().setUiHandlers(this);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);

        // Redirect to general tab
        getView().getDsdTabSet().selectTab(0);
        if (NameTokens.dsdPage.equals(placeManager.getCurrentPlaceRequest().getNameToken())) {
            String dsdIdentifier = PlaceRequestUtils.getDsdParamFromUrl(placeManager);// DSD identifier is the URN without the prefix
            this.dsdUrn = UrnUtils.generateUrn(UrnConstants.URN_SDMX_CLASS_DATASTRUCTURE_PREFIX, dsdIdentifier);
            placeManager.revealRelativePlace(new PlaceRequest(NameTokens.dsdGeneralPage));
        }
    }

    @Override
    protected void onBind() {
        super.onBind();

        getView().getGeneralTab().addTabSelectedHandler(new TabSelectedHandler() {

            @Override
            public void onTabSelected(TabSelectedEvent event) {
                if (!StringUtils.isBlank(dsdUrn)) {
                    placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteDsdGeneralPlaceRequest(dsdUrn));
                }
            }
        });
        getView().getPrimaryMeasureTab().addTabSelectedHandler(new TabSelectedHandler() {

            @Override
            public void onTabSelected(TabSelectedEvent event) {
                if (!StringUtils.isBlank(dsdUrn)) {
                    placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteDsdPrimaryMeasurePlaceRequest(dsdUrn));
                }
            }
        });
        getView().getDimensionsTab().addTabSelectedHandler(new TabSelectedHandler() {

            @Override
            public void onTabSelected(TabSelectedEvent event) {
                if (!StringUtils.isBlank(dsdUrn)) {
                    placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteDsdDimensionsPlaceRequest(dsdUrn));
                }
            }
        });
        getView().getAttributesTab().addTabSelectedHandler(new TabSelectedHandler() {

            @Override
            public void onTabSelected(TabSelectedEvent event) {
                if (!StringUtils.isBlank(dsdUrn)) {
                    placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteDsdAttributesPlaceRequest(dsdUrn));
                }
            }
        });
        getView().getGroupKeysTab().addTabSelectedHandler(new TabSelectedHandler() {

            @Override
            public void onTabSelected(TabSelectedEvent event) {
                if (!StringUtils.isBlank(dsdUrn)) {
                    placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteDsdGroupKeysPlaceRequest(dsdUrn));
                }
            }
        });
        getView().getCategorisationsTab().addTabSelectedHandler(new TabSelectedHandler() {

            @Override
            public void onTabSelected(TabSelectedEvent event) {
                if (!StringUtils.isBlank(dsdUrn)) {
                    placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteDsdCategorisationsPlaceRequest(dsdUrn));
                }
            }
        });
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        MainPagePresenter.getMasterHead().setTitleLabel(MetamacSrmWeb.getConstants().dsd());
        SelectMenuButtonEvent.fire(DsdPresenter.this, ToolStripButtonEnum.DSD_LIST);

        // Load DSD
        String dsdIdentifier = PlaceRequestUtils.getDsdParamFromUrl(placeManager);// DSD identifier is the URN without the prefix
        if (!StringUtils.isBlank(dsdIdentifier)) {
            this.dsdUrn = UrnUtils.generateUrn(UrnConstants.URN_SDMX_CLASS_DATASTRUCTURE_PREFIX, dsdIdentifier);

            // The DSD should be loaded before loading the DSD versions
            retrieveDsdAndVersions(dsdUrn);
        }
    }

    @Override
    protected void onHide() {
        super.onHide();
    }

    @ProxyEvent
    @Override
    public void onSelectViewDsdDescriptor(SelectViewDsdDescriptorEvent event) {
        DsdTabTypeEnum type = event.getDsdTabType();
        if (DsdTabTypeEnum.PRIMARY_MEASURE.equals(type)) {
            getView().getDsdTabSet().selectTab(1);
        } else if (DsdTabTypeEnum.DIMENSIONS.equals(type)) {
            getView().getDsdTabSet().selectTab(2);
        } else if (DsdTabTypeEnum.ATTRIBUTES.equals(type)) {
            getView().getDsdTabSet().selectTab(3);
        } else if (DsdTabTypeEnum.GROUP_KEYS.equals(type)) {
            getView().getDsdTabSet().selectTab(4);
        } else if (DsdTabTypeEnum.CATEGORISATIONS.equals(type)) {
            getView().getDsdTabSet().selectTab(5);
        } else {
            getView().getDsdTabSet().selectTab(0);
        }
    }

    @ProxyEvent
    @Override
    public void onUpdateMaintainableArtefactVersions(UpdateMaintainableArtefactVersionsEvent event) {
        retrieveDsdAndVersions(event.getUrn()); // Load also the DSD to select it in the versions list
    }

    @ProxyEvent
    @Override
    public void onUpdateMaintainableArtefact(UpdateMaintainableArtefactEvent event) {
        this.dsdUrn = event.getUrn();
    }

    private void retrieveDsdAndVersions(String urn) {
        dispatcher.execute(new GetDsdAction(urn), new WaitingAsyncCallback<GetDsdResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(DsdPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetDsdResult result) {
                getView().setDsd(result.getDsd());
                retrieveDsdVersions(result.getDsd().getUrn());
            }
        });
    }

    private void retrieveDsdVersions(String dsdUrn) {
        dispatcher.execute(new GetDsdVersionsAction(dsdUrn), new WaitingAsyncCallback<GetDsdVersionsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(DsdPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetDsdVersionsResult result) {
                getView().setDsdVersions(result.getDataStructureDefinitionMetamacDtos());
            }
        });
    }

    //
    // NAVIGATION
    //

    @Override
    public void goToDsd(String urn) {
        if (!StringUtils.isBlank(urn)) {
            placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteDsdPlaceRequest(urn));
        }
    }
}