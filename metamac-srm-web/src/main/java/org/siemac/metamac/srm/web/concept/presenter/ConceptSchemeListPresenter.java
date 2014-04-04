package org.siemac.metamac.srm.web.concept.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.navigation.shared.NameTokens;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.constants.SrmWebConstants;
import org.siemac.metamac.srm.web.client.enums.ExportDetailEnum;
import org.siemac.metamac.srm.web.client.enums.ExportReferencesEnum;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.MetamacWebCriteriaClientUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.client.utils.WaitingAsyncCallbackHandlingExportResult;
import org.siemac.metamac.srm.web.concept.enums.ConceptsToolStripButtonEnum;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptSchemeListUiHandlers;
import org.siemac.metamac.srm.web.concept.widgets.presenter.ConceptsToolStripPresenterWidget;
import org.siemac.metamac.srm.web.shared.ExportSDMXResourceAction;
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
import org.siemac.metamac.srm.web.shared.criteria.StatisticalOperationWebCriteria;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.WaitingAsyncCallbackHandlingError;

import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
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

    private final DispatchAsync                       dispatcher;
    private final PlaceManager                        placeManager;

    private ConceptsToolStripPresenterWidget          conceptsToolStripPresenterWidget;

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetStructuralResourcesToolBar        = new Type<RevealContentHandler<?>>();

    public static final Object                        TYPE_SetContextAreaContentConceptsToolBar = new Object();

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

        // Search
        void clearSearchSection();
        void setOperationsForSearchSection(GetStatisticalOperationsResult result);
        ConceptSchemeWebCriteria getConceptSchemeWebCriteria();
    }

    @Inject
    public ConceptSchemeListPresenter(EventBus eventBus, ConceptSchemeListView conceptSchemeListView, ConceptSchemeListProxy conceptSchemeListProxy, DispatchAsync dispatcher,
            PlaceManager placeManager, ConceptsToolStripPresenterWidget conceptsToolStripPresenterWidget) {
        super(eventBus, conceptSchemeListView, conceptSchemeListProxy);
        this.placeManager = placeManager;
        this.dispatcher = dispatcher;
        this.conceptsToolStripPresenterWidget = conceptsToolStripPresenterWidget;
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
        retrieveConceptSchemes(SrmWebConstants.SCHEME_LIST_FIRST_RESULT, SrmWebConstants.SCHEME_LIST_MAX_RESULTS,
                MetamacWebCriteriaClientUtils.addLastVersionConditionToConceptSchemeWebCriteria(new ConceptSchemeWebCriteria()));
        // Clear search section
        getView().clearSearchSection();
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        setInSlot(TYPE_SetContextAreaContentConceptsToolBar, conceptsToolStripPresenterWidget);

        SetTitleEvent.fire(this, getConstants().conceptSchemes());
        SelectMenuButtonEvent.fire(this, ToolStripButtonEnum.CONCEPTS);
        conceptsToolStripPresenterWidget.selectConceptsMenuButton(ConceptsToolStripButtonEnum.CONCEPT_SCHEMES);
    }

    @Override
    public void retrieveConceptSchemes(int firstResult, int maxResults, final ConceptSchemeWebCriteria conceptSchemeWebCriteria) {
        dispatcher.execute(new GetConceptSchemesAction(firstResult, maxResults, conceptSchemeWebCriteria), new WaitingAsyncCallbackHandlingError<GetConceptSchemesResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(ConceptSchemeListPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetConceptSchemesResult result) {
                getView().setConceptSchemePaginatedList(result);
            }
        });
    }

    @Override
    public void createConceptScheme(ConceptSchemeMetamacDto conceptSchemeDto) {
        dispatcher.execute(new SaveConceptSchemeAction(conceptSchemeDto), new WaitingAsyncCallbackHandlingError<SaveConceptSchemeResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(ConceptSchemeListPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(SaveConceptSchemeResult result) {
                fireSuccessMessage(getMessages().conceptSchemeSaved());
                retrieveConceptSchemes(SrmWebConstants.SCHEME_LIST_FIRST_RESULT, SrmWebConstants.SCHEME_LIST_MAX_RESULTS,
                        MetamacWebCriteriaClientUtils.addLastVersionConditionToConceptSchemeWebCriteria(getView().getConceptSchemeWebCriteria()));
            }
        });
    }

    @Override
    public void deleteConceptSchemes(List<String> urns) {
        dispatcher.execute(new DeleteConceptSchemesAction(urns), new WaitingAsyncCallbackHandlingError<DeleteConceptSchemesResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(ConceptSchemeListPresenter.this, caught);
                retrieveConceptSchemes(SrmWebConstants.SCHEME_LIST_FIRST_RESULT, SrmWebConstants.SCHEME_LIST_MAX_RESULTS,
                        MetamacWebCriteriaClientUtils.addLastVersionConditionToConceptSchemeWebCriteria(getView().getConceptSchemeWebCriteria()));
            }
            @Override
            public void onWaitSuccess(DeleteConceptSchemesResult result) {
                fireSuccessMessage(getMessages().conceptSchemeDeleted());
                retrieveConceptSchemes(SrmWebConstants.SCHEME_LIST_FIRST_RESULT, SrmWebConstants.SCHEME_LIST_MAX_RESULTS,
                        MetamacWebCriteriaClientUtils.addLastVersionConditionToConceptSchemeWebCriteria(getView().getConceptSchemeWebCriteria()));
            }
        });
    }

    @Override
    public void exportConceptSchemes(List<String> urns, ExportDetailEnum detail, ExportReferencesEnum references) {
        dispatcher.execute(new ExportSDMXResourceAction(urns, RelatedResourceTypeEnum.CONCEPT_SCHEME, detail, references), new WaitingAsyncCallbackHandlingExportResult(this));
    }

    @Override
    public void cancelValidity(List<String> urns) {
        dispatcher.execute(new CancelConceptSchemeValidityAction(urns), new WaitingAsyncCallbackHandlingError<CancelConceptSchemeValidityResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(ConceptSchemeListPresenter.this, caught);
                retrieveConceptSchemes(SrmWebConstants.SCHEME_LIST_FIRST_RESULT, SrmWebConstants.SCHEME_LIST_MAX_RESULTS,
                        MetamacWebCriteriaClientUtils.addLastVersionConditionToConceptSchemeWebCriteria(getView().getConceptSchemeWebCriteria()));
            }
            @Override
            public void onWaitSuccess(CancelConceptSchemeValidityResult result) {
                fireSuccessMessage(getMessages().conceptSchemeCanceledValidity());
                retrieveConceptSchemes(SrmWebConstants.SCHEME_LIST_FIRST_RESULT, SrmWebConstants.SCHEME_LIST_MAX_RESULTS,
                        MetamacWebCriteriaClientUtils.addLastVersionConditionToConceptSchemeWebCriteria(getView().getConceptSchemeWebCriteria()));
            }
        });
    }

    @Override
    public void retrieveStatisticalOperations(int firstResult, int maxResults, String criteria) {
        StatisticalOperationWebCriteria statisticalOperationWebCriteria = new StatisticalOperationWebCriteria(criteria);
        dispatcher.execute(new GetStatisticalOperationsAction(firstResult, maxResults, statisticalOperationWebCriteria), new WaitingAsyncCallbackHandlingError<GetStatisticalOperationsResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(ConceptSchemeListPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetStatisticalOperationsResult result) {
                getView().setOperations(result);
            }
        });
    }

    //
    // SEARCH SECTION CRITERIA
    //

    @Override
    public void retrieveStatisticalOperationsForSearchSection(int firstResult, int maxResults, String criteria) {

        StatisticalOperationWebCriteria statisticalOperationWebCriteria = new StatisticalOperationWebCriteria(criteria);
        statisticalOperationWebCriteria.setNoFilterByUserPrincipal(true);

        dispatcher.execute(new GetStatisticalOperationsAction(firstResult, maxResults, statisticalOperationWebCriteria), new WaitingAsyncCallbackHandlingError<GetStatisticalOperationsResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(ConceptSchemeListPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetStatisticalOperationsResult result) {
                getView().setOperationsForSearchSection(result);
            }
        });
    }

    //
    // NAVIGATION
    //

    @Override
    public void goTo(List<PlaceRequest> location) {
        if (location != null && !location.isEmpty()) {
            placeManager.revealPlaceHierarchy(location);
        }
    }

    @Override
    public void goToConceptScheme(String urn) {
        if (!StringUtils.isBlank(urn)) {
            placeManager.revealRelativePlace(PlaceRequestUtils.buildRelativeConceptSchemePlaceRequest(urn));
        }
    }
}
