package org.siemac.metamac.srm.web.concept.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptSchemeListUiHandlers;
import org.siemac.metamac.srm.web.shared.concept.CancelConceptSchemeValidityAction;
import org.siemac.metamac.srm.web.shared.concept.CancelConceptSchemeValidityResult;
import org.siemac.metamac.srm.web.shared.concept.DeleteConceptSchemesAction;
import org.siemac.metamac.srm.web.shared.concept.DeleteConceptSchemesResult;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemesAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemesResult;
import org.siemac.metamac.srm.web.shared.concept.GetStatisticalOperationsAction;
import org.siemac.metamac.srm.web.shared.concept.GetStatisticalOperationsResult;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptSchemeResult;
import org.siemac.metamac.srm.web.shared.criteria.ConceptSchemeWebCriteria;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
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
import com.gwtplatform.mvp.client.annotations.TitleFunction;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

public class ConceptSchemeListPresenter extends Presenter<ConceptSchemeListPresenter.ConceptSchemeListView, ConceptSchemeListPresenter.ConceptSchemeListProxy> implements ConceptSchemeListUiHandlers {

    public final static int                           SCHEME_LIST_FIRST_RESULT           = 0;
    public final static int                           SCHEME_LIST_MAX_RESULTS            = 30;

    private final DispatchAsync                       dispatcher;
    private final PlaceManager                        placeManager;

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetStructuralResourcesToolBar = new Type<RevealContentHandler<?>>();

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

        void setConceptSchemePaginatedList(GetConceptSchemesResult conceptSchemesPaginatedList);
        void goToConceptSchemeListLastPageAfterCreate();
        void setOperations(GetStatisticalOperationsResult result);
        void clearSearchSection();
    }

    @Inject
    public ConceptSchemeListPresenter(EventBus eventBus, ConceptSchemeListView conceptSchemeListView, ConceptSchemeListProxy conceptSchemeListProxy, DispatchAsync dispatcher, PlaceManager placeManager) {
        super(eventBus, conceptSchemeListView, conceptSchemeListProxy);
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
        // Load concept schemes
        retrieveConceptSchemesLastVersion();
        // Clear search section
        getView().clearSearchSection();
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        SetTitleEvent.fire(this, getConstants().conceptSchemes());
        SelectMenuButtonEvent.fire(this, ToolStripButtonEnum.CONCEPTS);
    }

    @Override
    public void retrieveConceptSchemes(int firstResult, int maxResults, final ConceptSchemeWebCriteria conceptSchemeWebCriteria) {
        dispatcher.execute(new GetConceptSchemesAction(firstResult, maxResults, conceptSchemeWebCriteria), new WaitingAsyncCallback<GetConceptSchemesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemeListPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetConceptSchemesResult result) {
                getView().setConceptSchemePaginatedList(result);
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
                retrieveConceptSchemesLastVersion();
            }
        });
    }

    @Override
    public void deleteConceptSchemes(List<String> urns) {
        dispatcher.execute(new DeleteConceptSchemesAction(urns), new WaitingAsyncCallback<DeleteConceptSchemesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemeListPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorDelete()), MessageTypeEnum.ERROR);
                retrieveConceptSchemesLastVersion();
            }
            @Override
            public void onWaitSuccess(DeleteConceptSchemesResult result) {
                ShowMessageEvent.fire(ConceptSchemeListPresenter.this, ErrorUtils.getMessageList(getMessages().conceptSchemeDeleted()), MessageTypeEnum.SUCCESS);
                retrieveConceptSchemesLastVersion();
            }
        });
    }

    @Override
    public void cancelValidity(List<String> urns) {
        dispatcher.execute(new CancelConceptSchemeValidityAction(urns), new WaitingAsyncCallback<CancelConceptSchemeValidityResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemeListPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorCancelValidity()), MessageTypeEnum.ERROR);
                retrieveConceptSchemesLastVersion();
            }
            @Override
            public void onWaitSuccess(CancelConceptSchemeValidityResult result) {
                ShowMessageEvent.fire(ConceptSchemeListPresenter.this, ErrorUtils.getMessageList(getMessages().conceptSchemeCanceledValidity()), MessageTypeEnum.SUCCESS);
                retrieveConceptSchemesLastVersion();
            }
        });
    }

    @Override
    public void goToConceptScheme(String urn) {
        if (!StringUtils.isBlank(urn)) {
            placeManager.revealRelativePlace(PlaceRequestUtils.buildRelativeConceptSchemePlaceRequest(urn));
        }
    }

    @Override
    public void retrieveStatisticalOperations(int firstResult, int maxResults, String criteria) {
        dispatcher.execute(new GetStatisticalOperationsAction(firstResult, maxResults, criteria), new WaitingAsyncCallback<GetStatisticalOperationsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemeListPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorRetrievingOperations()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetStatisticalOperationsResult result) {
                getView().setOperations(result);
            }
        });
    }

    private void retrieveConceptSchemesLastVersion() {
        ConceptSchemeWebCriteria conceptSchemeWebCriteria = new ConceptSchemeWebCriteria();
        conceptSchemeWebCriteria.setIsLastVersion(true);
        retrieveConceptSchemes(SCHEME_LIST_FIRST_RESULT, SCHEME_LIST_MAX_RESULTS, conceptSchemeWebCriteria);
    }
}
