package org.siemac.metamac.srm.web.concept.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.PlaceRequestParams;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.widgets.presenter.ToolStripPresenterWidget;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptSchemeListUiHandlers;
import org.siemac.metamac.srm.web.shared.concept.CancelConceptSchemeValidityAction;
import org.siemac.metamac.srm.web.shared.concept.CancelConceptSchemeValidityResult;
import org.siemac.metamac.srm.web.shared.concept.DeleteConceptSchemeListAction;
import org.siemac.metamac.srm.web.shared.concept.DeleteConceptSchemeListResult;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemePaginatedListAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemePaginatedListResult;
import org.siemac.metamac.srm.web.shared.concept.GetStatisticalOperationsPaginatedListAction;
import org.siemac.metamac.srm.web.shared.concept.GetStatisticalOperationsPaginatedListResult;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptSchemeResult;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.UrnUtils;
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
import com.gwtplatform.mvp.client.annotations.TitleFunction;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

public class ConceptSchemeListPresenter extends Presenter<ConceptSchemeListPresenter.ConceptSchemeListView, ConceptSchemeListPresenter.ConceptSchemeListProxy> implements ConceptSchemeListUiHandlers {

    public final static int                           SCHEME_LIST_FIRST_RESULT                           = 0;
    public final static int                           SCHEME_LIST_MAX_RESULTS                            = 30;

    private final DispatchAsync                       dispatcher;
    private final PlaceManager                        placeManager;

    private ToolStripPresenterWidget                  toolStripPresenterWidget;

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetStructuralResourcesToolBar                 = new Type<RevealContentHandler<?>>();

    public static final Object                        TYPE_SetContextAreaContentConceptSchemeListToolBar = new Object();

    @ProxyCodeSplit
    @NameToken(NameTokens.conceptSchemeListPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface ConceptSchemeListProxy extends Proxy<ConceptSchemeListPresenter>, Place {
    }

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbConceptSchemeList();
    }

    public interface ConceptSchemeListView extends View, HasUiHandlers<ConceptSchemeListUiHandlers> {

        void setConceptSchemePaginatedList(GetConceptSchemePaginatedListResult conceptSchemesPaginatedList);
        void goToConceptSchemeListLastPageAfterCreate();
        void setOperations(List<ExternalItemDto> operations, int firstResult, int totalResults);
        void clearSearchSection();
    }

    @Inject
    public ConceptSchemeListPresenter(EventBus eventBus, ConceptSchemeListView conceptSchemeListView, ConceptSchemeListProxy conceptSchemeListProxy, DispatchAsync dispatcher,
            PlaceManager placeManager, ToolStripPresenterWidget toolStripPresenterWidget) {
        super(eventBus, conceptSchemeListView, conceptSchemeListProxy);
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

        retrieveConceptSchemes(SCHEME_LIST_FIRST_RESULT, SCHEME_LIST_MAX_RESULTS, null);
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        setInSlot(TYPE_SetContextAreaContentConceptSchemeListToolBar, toolStripPresenterWidget);
    }

    @Override
    public void retrieveConceptSchemes(int firstResult, int maxResults, final String conceptScheme) {
        dispatcher.execute(new GetConceptSchemePaginatedListAction(firstResult, maxResults, conceptScheme), new WaitingAsyncCallback<GetConceptSchemePaginatedListResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemeListPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorRetrieveList()), MessageTypeEnum.ERROR);
            }

            @Override
            public void onWaitSuccess(GetConceptSchemePaginatedListResult result) {
                getView().setConceptSchemePaginatedList(result);
                if (StringUtils.isBlank(conceptScheme)) {
                    getView().clearSearchSection();
                }
            }
        });
    }

    @Override
    public void createConceptScheme(ConceptSchemeMetamacDto conceptSchemeDto) {
        dispatcher.execute(new SaveConceptSchemeAction(conceptSchemeDto), new WaitingAsyncCallback<SaveConceptSchemeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemeListPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorSave()), MessageTypeEnum.ERROR);
            }

            @Override
            public void onWaitSuccess(SaveConceptSchemeResult result) {
                ShowMessageEvent.fire(ConceptSchemeListPresenter.this, ErrorUtils.getMessageList(getMessages().conceptSchemeSaved()), MessageTypeEnum.SUCCESS);
                retrieveConceptSchemes(SCHEME_LIST_FIRST_RESULT, SCHEME_LIST_MAX_RESULTS, null);
                getView().goToConceptSchemeListLastPageAfterCreate();
            }
        });
    }

    @Override
    public void deleteConceptSchemes(List<String> urns) {
        dispatcher.execute(new DeleteConceptSchemeListAction(urns), new WaitingAsyncCallback<DeleteConceptSchemeListResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemeListPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorDelete()), MessageTypeEnum.ERROR);
                retrieveConceptSchemes(SCHEME_LIST_FIRST_RESULT, SCHEME_LIST_MAX_RESULTS, null);
            }
            @Override
            public void onWaitSuccess(DeleteConceptSchemeListResult result) {
                ShowMessageEvent.fire(ConceptSchemeListPresenter.this, ErrorUtils.getMessageList(getMessages().conceptSchemeDeleted()), MessageTypeEnum.SUCCESS);
                retrieveConceptSchemes(SCHEME_LIST_FIRST_RESULT, SCHEME_LIST_MAX_RESULTS, null);
            }
        });
    }

    @Override
    public void cancelValidity(List<String> urns) {
        dispatcher.execute(new CancelConceptSchemeValidityAction(urns), new WaitingAsyncCallback<CancelConceptSchemeValidityResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemeListPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorCancelValidity()), MessageTypeEnum.ERROR);
                retrieveConceptSchemes(SCHEME_LIST_FIRST_RESULT, SCHEME_LIST_MAX_RESULTS, null);
            }
            @Override
            public void onWaitSuccess(CancelConceptSchemeValidityResult result) {
                ShowMessageEvent.fire(ConceptSchemeListPresenter.this, ErrorUtils.getMessageList(getMessages().conceptSchemeDeleted()), MessageTypeEnum.SUCCESS);
                retrieveConceptSchemes(SCHEME_LIST_FIRST_RESULT, SCHEME_LIST_MAX_RESULTS, null);
            }
        });
    }

    @Override
    public void goToConceptScheme(String urn) {
        if (!StringUtils.isBlank(urn)) {
            placeManager.revealRelativePlace(new PlaceRequest(NameTokens.conceptSchemePage).with(PlaceRequestParams.conceptSchemeParam, UrnUtils.removePrefix(urn)));
        }
    }

    @Override
    public void retrieveStatisticalOperations(int firstResult, int maxResults, String operation) {
        dispatcher.execute(new GetStatisticalOperationsPaginatedListAction(firstResult, maxResults, operation), new WaitingAsyncCallback<GetStatisticalOperationsPaginatedListResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemeListPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorRetrievingOperations()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetStatisticalOperationsPaginatedListResult result) {
                getView().setOperations(result.getOperations(), result.getFirstResultOut(), result.getTotalResults());
            }
        });
    }

}
