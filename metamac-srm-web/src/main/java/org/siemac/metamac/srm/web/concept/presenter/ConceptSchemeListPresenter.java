package org.siemac.metamac.srm.web.concept.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.PlaceRequestParams;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.presenter.PaginationPresenter;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.widgets.StatusBar;
import org.siemac.metamac.srm.web.client.widgets.presenter.ToolStripPresenterWidget;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptSchemeListUiHandlers;
import org.siemac.metamac.srm.web.shared.DeleteConceptSchemeListAction;
import org.siemac.metamac.srm.web.shared.DeleteConceptSchemeListResult;
import org.siemac.metamac.srm.web.shared.GetConceptSchemePaginatedListAction;
import org.siemac.metamac.srm.web.shared.GetConceptSchemePaginatedListResult;
import org.siemac.metamac.srm.web.shared.SaveConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.SaveConceptSchemeResult;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
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

public class ConceptSchemeListPresenter extends PaginationPresenter<ConceptSchemeListPresenter.ConceptSchemeListView, ConceptSchemeListPresenter.ConceptSchemeListProxy>
        implements
            ConceptSchemeListUiHandlers {

    public static final int                           DEFAULT_MAX_RESULTS                                = 30;

    private final DispatchAsync                       dispatcher;
    private final PlaceManager                        placeManager;

    private ToolStripPresenterWidget                  toolStripPresenterWidget;

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetStructuralResourcesToolBar                 = new Type<RevealContentHandler<?>>();

    public static final Object                        TYPE_SetContextAreaContentConceptSchemeListToolBar = new Object();

    @ProxyCodeSplit
    @NameToken(NameTokens.conceptSchemeListPage)
    public interface ConceptSchemeListProxy extends Proxy<ConceptSchemeListPresenter>, Place {
    }

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbConceptSchemeList();
    }

    public interface ConceptSchemeListView extends View, HasUiHandlers<ConceptSchemeListUiHandlers> {

        void setConceptSchemeList(List<ConceptSchemeDto> conceptSchemesDtos);

        StatusBar getStatusBar();
        void refreshStatusBar();
        void setNumberOfElements(int numberOfElements);
        void setPageNumber(int pageNumber);
    }

    @Inject
    public ConceptSchemeListPresenter(EventBus eventBus, ConceptSchemeListView conceptSchemeListView, ConceptSchemeListProxy conceptSchemeListProxy, DispatchAsync dispatcher,
            PlaceManager placeManager, ToolStripPresenterWidget toolStripPresenterWidget) {
        super(eventBus, conceptSchemeListView, conceptSchemeListProxy, dispatcher, DEFAULT_MAX_RESULTS);
        this.placeManager = placeManager;
        this.toolStripPresenterWidget = toolStripPresenterWidget;
        this.dispatcher = dispatcher;
        getView().setUiHandlers(this);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }

    @Override
    protected void onReset() {
        super.onReset();
        SetTitleEvent.fire(this, getConstants().conceptSchemes());

        initializePaginationSettings();

        retrieveResultSet();
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        setInSlot(TYPE_SetContextAreaContentConceptSchemeListToolBar, toolStripPresenterWidget);
    }

    @Override
    protected void retrieveResultSet() {
        dispatcher.execute(new GetConceptSchemePaginatedListAction(getMaxResults(), getFirstResult()), new WaitingAsyncCallback<GetConceptSchemePaginatedListResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemeListPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorRetrieveList()), MessageTypeEnum.ERROR);
            }

            @Override
            public void onWaitSuccess(GetConceptSchemePaginatedListResult result) {
                ConceptSchemeListPresenter.this.totalResults = result.getTotalResults();

                setNumberOfElements(result.getConceptSchemeList().size());

                // update Selected label e.g "0 of 50 selected"
                getView().setNumberOfElements(getNumberOfElements());
                getView().setPageNumber(getPageNumber());
                getView().refreshStatusBar();

                // Log.debug("onSuccess() - firstResult: " + firstResult +
                // " pageNumber: " + pageNumber + " numberOfElements: " +
                // numberOfElements);

                // enable/disable the pagination widgets
                if (getPageNumber() == 1) {
                    getView().getStatusBar().getResultSetFirstButton().disable();
                    getView().getStatusBar().getResultSetPreviousButton().disable();
                } else {
                    getView().getStatusBar().getResultSetFirstButton().enable();
                    getView().getStatusBar().getResultSetPreviousButton().enable();
                }

                // enable/disable the pagination widgets
                if ((result.getTotalResults() - (getPageNumber() - 1) * DEFAULT_MAX_RESULTS) > getNumberOfElements()) {
                    getView().getStatusBar().getResultSetNextButton().enable();
                    getView().getStatusBar().getResultSetLastButton().enable();
                } else {
                    getView().getStatusBar().getResultSetNextButton().disable();
                    getView().getStatusBar().getResultSetLastButton().disable();
                }

                // pass the result set to the View
                getView().setConceptSchemeList(result.getConceptSchemeList());
            }
        });
    }

    // Handlers
    @Override
    public void createConceptScheme(ConceptSchemeDto conceptSchemeDto) {
        dispatcher.execute(new SaveConceptSchemeAction(conceptSchemeDto), new WaitingAsyncCallback<SaveConceptSchemeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemeListPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorSave()), MessageTypeEnum.ERROR);
            }

            @Override
            public void onWaitSuccess(SaveConceptSchemeResult result) {
                ShowMessageEvent.fire(ConceptSchemeListPresenter.this, ErrorUtils.getMessageList(getMessages().conceptSchemeSaved()), MessageTypeEnum.SUCCESS);
                retrieveResultSet(); // Reload list
            }
        });
    }

    @Override
    public void deleteConceptSchemes(List<String> uuidsFromSelected) {
        dispatcher.execute(new DeleteConceptSchemeListAction(uuidsFromSelected), new WaitingAsyncCallback<DeleteConceptSchemeListResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemeListPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorDelete()), MessageTypeEnum.ERROR);
            }

            @Override
            public void onWaitSuccess(DeleteConceptSchemeListResult result) {
                ShowMessageEvent.fire(ConceptSchemeListPresenter.this, ErrorUtils.getMessageList(getMessages().conceptSchemeDeleted()), MessageTypeEnum.SUCCESS);
                retrieveResultSet(); // reload list
            }
        });
    }

    @Override
    public void goToConceptScheme(String idLogic) {
        PlaceRequest resourcesRequest = new PlaceRequest(NameTokens.structuralResourcesPage);
        PlaceRequest conceptSchemeListRequest = new PlaceRequest(NameTokens.conceptSchemePage).with(PlaceRequestParams.conceptSchemeParam, idLogic);
        List<PlaceRequest> placeRequestHierarchy = new ArrayList<PlaceRequest>();
        placeRequestHierarchy.add(resourcesRequest);
        placeRequestHierarchy.add(conceptSchemeListRequest);
        placeManager.revealPlaceHierarchy(placeRequestHierarchy);
    }

}
