package org.siemac.metamac.srm.web.dsd.presenter;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.dsd.events.SelectDsdAndDescriptorsEvent;
import org.siemac.metamac.srm.web.dsd.events.SelectDsdAndDescriptorsEvent.SelectDsdAndDescriptorsHandler;
import org.siemac.metamac.srm.web.dsd.events.SelectViewDsdDescriptorEvent;
import org.siemac.metamac.srm.web.dsd.events.SelectViewDsdDescriptorEvent.SelectViewDsdDescriptorHandler;
import org.siemac.metamac.srm.web.dsd.events.UpdateDsdEvent;
import org.siemac.metamac.srm.web.dsd.events.UpdateDsdEvent.UpdateDsdHandler;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdUiHandlers;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdVersionsAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdVersionsResult;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeComponentList;
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

public class DsdPresenter extends Presenter<DsdPresenter.DsdView, DsdPresenter.DsdProxy> implements DsdUiHandlers, SelectDsdAndDescriptorsHandler, UpdateDsdHandler, SelectViewDsdDescriptorHandler {

    private final DispatchAsync                       dispatcher;
    private final PlaceManager                        placeManager;

    private DataStructureDefinitionMetamacDto         dsd;

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
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbDSD();
    }

    public interface DsdView extends View, HasUiHandlers<DsdUiHandlers> {

        void setDsd(DataStructureDefinitionMetamacDto dataStructureDefinitionDto);
        void setDsdVersions(List<DataStructureDefinitionMetamacDto> dataStructureDefinitionMetamacDtos);

        TabSet getDsdTabSet();
        HasTabSelectedHandlers getGeneralTab();
        HasTabSelectedHandlers getPrimaryMeasureTab();
        HasTabSelectedHandlers getDimensionsTab();
        HasTabSelectedHandlers getAttributesTab();
        HasTabSelectedHandlers getGroupKeysTab();
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
            placeManager.revealRelativePlace(new PlaceRequest(NameTokens.dsdGeneralPage));
        }
    }

    @Override
    protected void onBind() {
        super.onBind();

        getView().getGeneralTab().addTabSelectedHandler(new TabSelectedHandler() {

            @Override
            public void onTabSelected(TabSelectedEvent event) {
                if (NameTokens.dsdPage.equals(placeManager.getCurrentPlaceRequest().getNameToken())) {
                    placeManager.revealRelativePlace(new PlaceRequest(NameTokens.dsdGeneralPage));
                } else {
                    placeManager.revealRelativePlace(new PlaceRequest(NameTokens.dsdGeneralPage), -1);
                }
            }
        });
        getView().getPrimaryMeasureTab().addTabSelectedHandler(new TabSelectedHandler() {

            @Override
            public void onTabSelected(TabSelectedEvent event) {
                if (NameTokens.dsdPage.equals(placeManager.getCurrentPlaceRequest().getNameToken())) {
                    placeManager.revealRelativePlace(new PlaceRequest(NameTokens.dsdPrimaryMeasurePage));
                } else {
                    placeManager.revealRelativePlace(new PlaceRequest(NameTokens.dsdPrimaryMeasurePage), -1);
                }
            }
        });
        getView().getDimensionsTab().addTabSelectedHandler(new TabSelectedHandler() {

            @Override
            public void onTabSelected(TabSelectedEvent event) {
                if (NameTokens.dsdPage.equals(placeManager.getCurrentPlaceRequest().getNameToken())) {
                    placeManager.revealRelativePlace(new PlaceRequest(NameTokens.dsdDimensionsPage));
                } else {
                    placeManager.revealRelativePlace(new PlaceRequest(NameTokens.dsdDimensionsPage), -1);
                }
            }
        });
        getView().getAttributesTab().addTabSelectedHandler(new TabSelectedHandler() {

            @Override
            public void onTabSelected(TabSelectedEvent event) {
                if (NameTokens.dsdPage.equals(placeManager.getCurrentPlaceRequest().getNameToken())) {
                    placeManager.revealRelativePlace(new PlaceRequest(NameTokens.dsdAttributesPage));
                } else {
                    placeManager.revealRelativePlace(new PlaceRequest(NameTokens.dsdAttributesPage), -1);
                }
            }
        });
        getView().getGroupKeysTab().addTabSelectedHandler(new TabSelectedHandler() {

            @Override
            public void onTabSelected(TabSelectedEvent event) {
                if (NameTokens.dsdPage.equals(placeManager.getCurrentPlaceRequest().getNameToken())) {
                    placeManager.revealRelativePlace(new PlaceRequest(NameTokens.dsdGroupKeysPage));
                } else {
                    placeManager.revealRelativePlace(new PlaceRequest(NameTokens.dsdGroupKeysPage), -1);
                }
            }
        });
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        MainPagePresenter.getMasterHead().setTitleLabel(MetamacSrmWeb.getConstants().dsd());
        SelectMenuButtonEvent.fire(DsdPresenter.this, ToolStripButtonEnum.DSD_LIST);
    }

    @Override
    protected void onHide() {
        super.onHide();
    }

    @ProxyEvent
    @Override
    public void onSelectDsdAndDescriptors(SelectDsdAndDescriptorsEvent event) {
        dsd = event.getDataStructureDefinitionDto();
        getView().setDsd(dsd);
        // Select general tab
        getView().getDsdTabSet().selectTab(0);
        if (NameTokens.dsdPage.equals(placeManager.getCurrentPlaceRequest().getNameToken())) {
            placeManager.revealRelativePlace(new PlaceRequest(NameTokens.dsdGeneralPage));
        } else {
            placeManager.revealRelativePlace(new PlaceRequest(NameTokens.dsdGeneralPage), -1);
        }

        retrieveDsdVersions(dsd.getUrn());
    }

    @ProxyEvent
    @Override
    public void onUpdateDsd(UpdateDsdEvent event) {
        dsd = event.getDataStructureDefinitionDto();
        getView().setDsd(dsd);
    }

    @ProxyEvent
    @Override
    public void onSelectViewDsdDescriptor(SelectViewDsdDescriptorEvent event) {
        TypeComponentList type = event.getDescriptorType();
        if (TypeComponentList.MEASURE_DESCRIPTOR.equals(type)) {
            getView().getDsdTabSet().selectTab(1);
        } else if (TypeComponentList.DIMENSION_DESCRIPTOR.equals(type)) {
            getView().getDsdTabSet().selectTab(2);
        } else if (TypeComponentList.ATTRIBUTE_DESCRIPTOR.equals(type)) {
            getView().getDsdTabSet().selectTab(3);
        } else if (TypeComponentList.GROUP_DIMENSION_DESCRIPTOR.equals(type)) {
            getView().getDsdTabSet().selectTab(4);
        } else {
            getView().getDsdTabSet().selectTab(0);
        }
    }

    public void retrieveDsdVersions(String dsdUrn) {
        dispatcher.execute(new GetDsdVersionsAction(dsdUrn), new WaitingAsyncCallback<GetDsdVersionsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorRetrievingVersions()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetDsdVersionsResult result) {
                getView().setDsdVersions(result.getDataStructureDefinitionMetamacDtos());
            }
        });
    }

    @Override
    public void goToDsd(String urn) {
        if (!StringUtils.isBlank(urn)) {
            placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteDsdPlaceRequest(urn));
        }
    }
}