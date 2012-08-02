package org.siemac.metamac.srm.web.client.presenter;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.PlaceRequestParams;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.view.handlers.StructuralResourcesUiHandlers;
import org.siemac.metamac.srm.web.client.widgets.presenter.ToolStripPresenterWidget;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemePaginatedListAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemePaginatedListResult;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdListAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdListResult;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.UrnUtils;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

import com.arte.statistic.sdmx.v2_1.domain.dto.concept.ConceptSchemeDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataStructureDefinitionDto;
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
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

public class StructuralResourcesPresenter extends Presenter<StructuralResourcesPresenter.StructuralResourcesView, StructuralResourcesPresenter.StructuralResourcesProxy>
        implements
            StructuralResourcesUiHandlers {

    public final static int          RESOURCE_LIST_FIRST_RESULT = 0;
    public final static int          RESOURCE_LIST_MAX_RESULTS  = 10;

    private final DispatchAsync      dispatcher;
    private final PlaceManager       placeManager;

    private ToolStripPresenterWidget toolStripPresenterWidget;

    @ProxyCodeSplit
    @NameToken(NameTokens.structuralResourcesPage)
    public interface StructuralResourcesProxy extends Proxy<StructuralResourcesPresenter>, Place {

    }

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbStrucResourcesDashboard();
    }

    public interface StructuralResourcesView extends View, HasUiHandlers<StructuralResourcesUiHandlers> {

        void setDsdList(List<DataStructureDefinitionDto> dataStructureDefinitionDtos);
        void setConceptSchemeList(List<ConceptSchemeDto> conceptSchemeDtos);

        void resetView();
    }

    /**
     * Use this in leaf presenters, inside their {@link #revealInParent} method.
     */
    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaContent        = new Type<RevealContentHandler<?>>();

    public static final Object                        TYPE_SetContextAreaContentToolBar = new Object();

    @Inject
    public StructuralResourcesPresenter(EventBus eventBus, StructuralResourcesView structuralResourcesView, StructuralResourcesProxy structuralResourcesProxy, DispatchAsync dispatcher,
            PlaceManager placeManager, ToolStripPresenterWidget toolStripPresenterWidget) {
        super(eventBus, structuralResourcesView, structuralResourcesProxy);
        this.placeManager = placeManager;
        this.dispatcher = dispatcher;
        this.toolStripPresenterWidget = toolStripPresenterWidget;
        getView().setUiHandlers(this);
    }

    @Override
    protected void onReset() {
        super.onReset();
        getView().resetView();
        retrieveDsds();
        retrieveConceptSchemes();
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        setInSlot(TYPE_SetContextAreaContentToolBar, toolStripPresenterWidget);
        MainPagePresenter.getMasterHead().setTitleLabel(MetamacSrmWeb.getConstants().structuralResourcesDahsboard());
    }

    @Override
    protected void onHide() {
        super.onHide();
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
    }

    protected void retrieveDsds() {
        dispatcher.execute(new GetDsdListAction(RESOURCE_LIST_FIRST_RESULT, RESOURCE_LIST_MAX_RESULTS, null), new WaitingAsyncCallback<GetDsdListResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(StructuralResourcesPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetDsdListResult result) {
                getView().setDsdList(result.getDsdDtos());
            }
        });
    }

    protected void retrieveConceptSchemes() {
        dispatcher.execute(new GetConceptSchemePaginatedListAction(RESOURCE_LIST_FIRST_RESULT, RESOURCE_LIST_MAX_RESULTS, null), new WaitingAsyncCallback<GetConceptSchemePaginatedListResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(StructuralResourcesPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().conceptSchemeErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetConceptSchemePaginatedListResult result) {
                getView().setConceptSchemeList(result.getConceptSchemeList());
            }
        });
    }

    /**************************************************************************
     * OperationsUiHandlers methods
     **************************************************************************/

    @Override
    public void goToDsd(String urn) {
        if (!StringUtils.isBlank(urn)) {
            PlaceRequest structuralResourcesPlace = new PlaceRequest(NameTokens.structuralResourcesPage);
            PlaceRequest dsdListPlace = new PlaceRequest(NameTokens.dsdListPage);
            PlaceRequest dsdPlace = new PlaceRequest(NameTokens.dsdPage).with(PlaceRequestParams.dsdParam, UrnUtils.removePrefix(urn));
            List<PlaceRequest> placeRequests = new ArrayList<PlaceRequest>();
            placeRequests.add(structuralResourcesPlace);
            placeRequests.add(dsdListPlace);
            placeRequests.add(dsdPlace);
            placeManager.revealPlaceHierarchy(placeRequests);
        }
    }

    @Override
    public void goToConceptScheme(String urn) {
        if (urn != null) {
            PlaceRequest structuralResourcesPlace = new PlaceRequest(NameTokens.structuralResourcesPage);
            PlaceRequest schemesListPlace = new PlaceRequest(NameTokens.conceptSchemeListPage);
            PlaceRequest conceptPlace = new PlaceRequest(NameTokens.conceptSchemePage).with(PlaceRequestParams.conceptSchemeParam, UrnUtils.removePrefix(urn));
            List<PlaceRequest> placeRequests = new ArrayList<PlaceRequest>();
            placeRequests.add(structuralResourcesPlace);
            placeRequests.add(schemesListPlace);
            placeRequests.add(conceptPlace);
            placeManager.revealPlaceHierarchy(placeRequests);
        }
    }

}
