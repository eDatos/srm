package org.siemac.metamac.srm.web.client.presenter;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.PlaceRequestParams;
import org.siemac.metamac.srm.web.client.model.record.DsdRecord;
import org.siemac.metamac.srm.web.client.view.handlers.StructuralResourcesUiHandlers;
import org.siemac.metamac.srm.web.client.widgets.presenter.ToolStripPresenterWidget;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdListAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdListResult;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.Window;
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
import com.smartgwt.client.widgets.grid.events.HasRecordClickHandlers;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;

public class StructuralResourcesPresenter extends Presenter<StructuralResourcesPresenter.StructuralResourcesView, StructuralResourcesPresenter.StructuralResourcesProxy>
        implements
            StructuralResourcesUiHandlers {

    private final DispatchAsync              dispatcher;
    private final PlaceManager               placeManager;

    private ToolStripPresenterWidget         toolStripPresenterWidget;

    private List<DataStructureDefinitionDto> dsdList;

    @ProxyCodeSplit
    @NameToken(NameTokens.structuralResourcesPage)
    public interface StructuralResourcesProxy extends Proxy<StructuralResourcesPresenter>, Place {

    }

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbStrucResourcesDashboard();
    }

    public interface StructuralResourcesView extends View, HasUiHandlers<StructuralResourcesUiHandlers> {

        void setResultSetDsd(List<DataStructureDefinitionDto> resultSet);
        HasRecordClickHandlers getSelectedDsd();
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
    protected void onBind() {
        super.onBind();

        // Operation click
        registerHandler(getView().getSelectedDsd().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                DsdRecord record = (DsdRecord) event.getRecord();
                goToDsd(record.getIdentifier());
            }
        }));
    }

    @Override
    protected void onReset() {
        super.onReset();
        getView().resetView();
        retrieveDsds(); // Fetch DSDs
        // retrieveConceptSchemes(); // Fetch ConceptSchemes
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

    /**
     * AsyncCallback to fetch DSDs
     */
    protected void retrieveDsds() {
        dispatcher.execute(new GetDsdListAction(), new WaitingAsyncCallback<GetDsdListResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                Window.alert("Error fetching DSDs" + caught.toString());
            }
            @Override
            public void onWaitSuccess(GetDsdListResult result) {
                dsdList = result.getDsdDtos();
                getView().setResultSetDsd(dsdList);
            }
        });
    }

    /**************************************************************************
     * OperationsUiHandlers methods
     **************************************************************************/

    @Override
    public void goToDsd(Long id) {
        if (id != null) {
            PlaceRequest structuralResourcesPlace = new PlaceRequest(NameTokens.structuralResourcesPage);
            PlaceRequest dsdListPlace = new PlaceRequest(NameTokens.dsdListPage);
            PlaceRequest dsdPlace = new PlaceRequest(NameTokens.dsdPage).with(PlaceRequestParams.dsdParam, id.toString());
            List<PlaceRequest> placeRequests = new ArrayList<PlaceRequest>();
            placeRequests.add(structuralResourcesPlace);
            placeRequests.add(dsdListPlace);
            placeRequests.add(dsdPlace);
            placeManager.revealPlaceHierarchy(placeRequests);
        }
    }

    @Override
    public void goToConceptScheme(String idLogic) {
        if (idLogic != null) {
            PlaceRequest structuralResourcesPlace = new PlaceRequest(NameTokens.structuralResourcesPage);
            PlaceRequest schemesListPlace = new PlaceRequest(NameTokens.conceptSchemeListPage);
            PlaceRequest conceptPlace = new PlaceRequest(NameTokens.conceptSchemePage).with(PlaceRequestParams.conceptSchemeParam, idLogic);
            List<PlaceRequest> placeRequests = new ArrayList<PlaceRequest>();
            placeRequests.add(structuralResourcesPlace);
            placeRequests.add(schemesListPlace);
            placeRequests.add(conceptPlace);
            placeManager.revealPlaceHierarchy(placeRequests);
        }
    }

}
