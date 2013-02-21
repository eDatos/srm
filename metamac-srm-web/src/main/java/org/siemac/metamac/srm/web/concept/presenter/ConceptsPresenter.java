package org.siemac.metamac.srm.web.concept.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.utils.MetamacWebCriteriaClientUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.concept.enums.ConceptsToolStripButtonEnum;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptsUiHandlers;
import org.siemac.metamac.srm.web.concept.widgets.presenter.ConceptsToolStripPresenterWidget;
import org.siemac.metamac.srm.web.shared.concept.GetConceptsAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptsResult;
import org.siemac.metamac.srm.web.shared.criteria.ConceptWebCriteria;
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

public class ConceptsPresenter extends Presenter<ConceptsPresenter.ConceptsView, ConceptsPresenter.ConceptsProxy> implements ConceptsUiHandlers {

    public final static int                           ITEM_LIST_FIRST_RESULT                    = 0;
    public final static int                           ITEM_LIST_MAX_RESULTS                     = 30;

    private final DispatchAsync                       dispatcher;
    private final PlaceManager                        placeManager;

    private ConceptsToolStripPresenterWidget          conceptsToolStripPresenterWidget;

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetStructuralResourcesToolBar        = new Type<RevealContentHandler<?>>();

    public static final Object                        TYPE_SetContextAreaContentConceptsToolBar = new Object();

    @ProxyCodeSplit
    @NameToken(NameTokens.conceptsPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface ConceptsProxy extends Proxy<ConceptsPresenter>, Place {
    }

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbConcepts();
    }

    public interface ConceptsView extends View, HasUiHandlers<ConceptsUiHandlers> {

        void setConcepts(GetConceptsResult result);
        void clearSearchSection();
    }

    @Inject
    public ConceptsPresenter(EventBus eventBus, ConceptsView conceptsView, ConceptsProxy conceptsProxy, DispatchAsync dispatcher, PlaceManager placeManager,
            ConceptsToolStripPresenterWidget conceptsToolStripPresenterWidget) {
        super(eventBus, conceptsView, conceptsProxy);
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
        // Load concepts
        retrieveConcepts(ITEM_LIST_FIRST_RESULT, ITEM_LIST_MAX_RESULTS, MetamacWebCriteriaClientUtils.addLastVersionConditionToConceptWebCriteria(new ConceptWebCriteria()));
        // Clear search section
        getView().clearSearchSection();
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        setInSlot(TYPE_SetContextAreaContentConceptsToolBar, conceptsToolStripPresenterWidget);

        SetTitleEvent.fire(this, getConstants().concepts());
        SelectMenuButtonEvent.fire(this, ToolStripButtonEnum.CONCEPTS);
        conceptsToolStripPresenterWidget.selectConceptsMenuButton(ConceptsToolStripButtonEnum.CONCEPTS);
    }

    @Override
    public void retrieveConcepts(int firstResult, int maxResults, ConceptWebCriteria conceptWebCriteria) {
        dispatcher.execute(new GetConceptsAction(firstResult, maxResults, conceptWebCriteria), new WaitingAsyncCallback<GetConceptsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptsPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetConceptsResult result) {
                getView().setConcepts(result);
            }
        });
    }

    @Override
    public void goToConcept(String conceptSchemeUrn, String conceptUrn) {
        if (!StringUtils.isBlank(conceptSchemeUrn) && !StringUtils.isBlank(conceptUrn)) {
            placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteConceptPlaceRequest(conceptSchemeUrn, conceptUrn));
        }
    }
}
