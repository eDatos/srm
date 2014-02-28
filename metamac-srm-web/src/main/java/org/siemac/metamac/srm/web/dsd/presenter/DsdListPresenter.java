package org.siemac.metamac.srm.web.dsd.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacBasicDto;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.navigation.shared.NameTokens;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.constants.SrmWebConstants;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.model.record.DsdRecord;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;
import org.siemac.metamac.srm.web.client.utils.MetamacWebCriteriaClientUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdListUiHandlers;
import org.siemac.metamac.srm.web.shared.ExportSDMXResourceAction;
import org.siemac.metamac.srm.web.shared.ExportSDMXResourceResult;
import org.siemac.metamac.srm.web.shared.concept.GetConceptsAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptsResult;
import org.siemac.metamac.srm.web.shared.concept.GetStatisticalOperationsAction;
import org.siemac.metamac.srm.web.shared.concept.GetStatisticalOperationsResult;
import org.siemac.metamac.srm.web.shared.criteria.ConceptWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.DataStructureDefinitionWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.StatisticalOperationWebCriteria;
import org.siemac.metamac.srm.web.shared.dsd.CancelDsdValidityAction;
import org.siemac.metamac.srm.web.shared.dsd.CancelDsdValidityResult;
import org.siemac.metamac.srm.web.shared.dsd.DeleteDsdsAction;
import org.siemac.metamac.srm.web.shared.dsd.DeleteDsdsResult;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdsAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdsResult;
import org.siemac.metamac.srm.web.shared.dsd.SaveDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.SaveDsdResult;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.utils.WaitingAsyncCallbackHandlingError;

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
import com.smartgwt.client.widgets.grid.events.HasRecordClickHandlers;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;

public class DsdListPresenter extends Presenter<DsdListPresenter.DsdListView, DsdListPresenter.DsdListProxy> implements DsdListUiHandlers {

    private final DispatchAsync                       dispatcher;
    private final PlaceManager                        placeManager;

    /**
     * Use this in leaf presenters, inside their {@link #revealInParent} method.
     */
    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaContentDsdList = new Type<RevealContentHandler<?>>();

    @ProxyCodeSplit
    @NameToken(NameTokens.dsdListPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface DsdListProxy extends Proxy<DsdListPresenter>, Place {
    }

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbDsdList();
    }

    public interface DsdListView extends View, HasUiHandlers<DsdListUiHandlers> {

        public void setDsds(List<DataStructureDefinitionMetamacBasicDto> dataStructureDefinitionDtos, int firstResult, int totalResults);
        HasRecordClickHandlers getSelectedDsd();
        List<String> getSelectedDsdUrns();
        void onNewDsdCreated();

        com.smartgwt.client.widgets.events.HasClickHandlers getDelete();

        void setOperations(GetStatisticalOperationsResult result);

        // Search section
        void setOperationsForSearchSection(GetStatisticalOperationsResult result);
        void setDimensionConceptsForSearchSection(GetConceptsResult result);
        void setAttributeConceptsForSearchSection(GetConceptsResult result);
        void clearSearchSection();
        DataStructureDefinitionWebCriteria getDataStructureDefinitionWebCriteria();
    }

    @Inject
    public DsdListPresenter(EventBus eventBus, DsdListView dsdListView, DsdListProxy dsdListProxy, DispatchAsync dispatcher, PlaceManager placeManager) {
        super(eventBus, dsdListView, dsdListProxy);
        this.dispatcher = dispatcher;
        this.placeManager = placeManager;
        getView().setUiHandlers(this);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        // Load DSDs
        retrieveDsdList(SrmWebConstants.SCHEME_LIST_FIRST_RESULT, SrmWebConstants.SCHEME_LIST_MAX_RESULTS,
                MetamacWebCriteriaClientUtils.addLastVersionConditionToDataStructureDefinitionWebCriteria(new DataStructureDefinitionWebCriteria()));
        // Clear search section
        getView().clearSearchSection();
    }

    @Override
    protected void onBind() {
        super.onBind();

        // Go to selected DSD
        registerHandler(getView().getSelectedDsd().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) {
                    DsdRecord record = (DsdRecord) event.getRecord();
                    goToDsd(record.getUrn());
                }
            }
        }));

        // Delete DSD
        registerHandler(getView().getDelete().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                List<String> dsdsToDelete = getView().getSelectedDsdUrns();
                deleteDsds(dsdsToDelete);
            }
        }));

    }

    @Override
    protected void onReveal() {
        super.onReveal();
        SetTitleEvent.fire(this, getConstants().dsds());
        SelectMenuButtonEvent.fire(DsdListPresenter.this, ToolStripButtonEnum.DSD_LIST);
    }

    @Override
    protected void onHide() {
        super.onHide();
    }

    @Override
    public void saveDsd(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        dispatcher.execute(new SaveDsdAction(dataStructureDefinitionMetamacDto), new WaitingAsyncCallbackHandlingError<SaveDsdResult>(this) {

            @Override
            public void onWaitSuccess(SaveDsdResult result) {
                retrieveDsdList(SrmWebConstants.SCHEME_LIST_FIRST_RESULT, SrmWebConstants.SCHEME_LIST_MAX_RESULTS,
                        MetamacWebCriteriaClientUtils.addLastVersionConditionToDataStructureDefinitionWebCriteria(getView().getDataStructureDefinitionWebCriteria()));
                fireSuccessMessage(MetamacSrmWeb.getMessages().dsdSaved());
            }
        });
    }

    @Override
    public void deleteDsds(List<String> urns) {
        dispatcher.execute(new DeleteDsdsAction(urns), new WaitingAsyncCallbackHandlingError<DeleteDsdsResult>(this) {

            @Override
            public void onWaitSuccess(DeleteDsdsResult result) {
                fireSuccessMessage(MetamacSrmWeb.getMessages().dsdDeleted());
            }

            @Override
            protected void afterResult() {
                retrieveDsdList(SrmWebConstants.SCHEME_LIST_FIRST_RESULT, SrmWebConstants.SCHEME_LIST_MAX_RESULTS,
                        MetamacWebCriteriaClientUtils.addLastVersionConditionToDataStructureDefinitionWebCriteria(getView().getDataStructureDefinitionWebCriteria()));
            }

        });
    }
    /**
     * AsyncCallback to fetch DSDs
     */
    @Override
    public void retrieveDsdList(int firstResult, int maxResults, DataStructureDefinitionWebCriteria dataStructureDefinitionWebCriteria) {
        dispatcher.execute(new GetDsdsAction(firstResult, maxResults, dataStructureDefinitionWebCriteria), new WaitingAsyncCallbackHandlingError<GetDsdsResult>(this) {

            @Override
            public void onWaitSuccess(GetDsdsResult result) {
                getView().setDsds(result.getDsdDtos(), result.getFirstResultOut(), result.getTotalResults());
            }
        });
    }

    @Override
    public void exportDsd(String urn) {
        dispatcher.execute(new ExportSDMXResourceAction(urn), new WaitingAsyncCallbackHandlingError<ExportSDMXResourceResult>(this) {

            @Override
            public void onWaitSuccess(ExportSDMXResourceResult result) {
                CommonUtils.downloadFile(result.getFileName());
            }
        });
    }

    @Override
    public void cancelValidity(List<String> urns) {
        dispatcher.execute(new CancelDsdValidityAction(urns), new WaitingAsyncCallbackHandlingError<CancelDsdValidityResult>(this) {

            @Override
            public void onWaitSuccess(CancelDsdValidityResult result) {
                fireSuccessMessage(getMessages().dsdCanceledValidity());
            }

            @Override
            protected void afterResult() {
                retrieveDsdList(SrmWebConstants.SCHEME_LIST_FIRST_RESULT, SrmWebConstants.SCHEME_LIST_MAX_RESULTS,
                        MetamacWebCriteriaClientUtils.addLastVersionConditionToDataStructureDefinitionWebCriteria(getView().getDataStructureDefinitionWebCriteria()));
            }
        });
    }

    @Override
    public void retrieveStatisticalOperations(int firstResult, int maxResults, String criteria) {
        StatisticalOperationWebCriteria statisticalOperationWebCriteria = new StatisticalOperationWebCriteria(criteria);
        dispatcher.execute(new GetStatisticalOperationsAction(firstResult, maxResults, statisticalOperationWebCriteria), new WaitingAsyncCallbackHandlingError<GetStatisticalOperationsResult>(this) {

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
            public void onWaitSuccess(GetStatisticalOperationsResult result) {
                getView().setOperationsForSearchSection(result);
            }
        });
    }

    @Override
    public void retrieveDimensionConceptsForSearchSection(int firstResult, int maxResults, String criteria) {
        ConceptWebCriteria conceptWebCriteria = new ConceptWebCriteria(criteria);
        dispatcher.execute(new GetConceptsAction(firstResult, maxResults, conceptWebCriteria), new WaitingAsyncCallbackHandlingError<GetConceptsResult>(this) {

            @Override
            public void onWaitSuccess(GetConceptsResult result) {
                getView().setDimensionConceptsForSearchSection(result);
            }
        });
    }

    @Override
    public void retrieveAttributeConceptsForSearchSection(int firstResult, int maxResults, String criteria) {
        ConceptWebCriteria conceptWebCriteria = new ConceptWebCriteria(criteria);
        dispatcher.execute(new GetConceptsAction(firstResult, maxResults, conceptWebCriteria), new WaitingAsyncCallbackHandlingError<GetConceptsResult>(this) {

            @Override
            public void onWaitSuccess(GetConceptsResult result) {
                getView().setAttributeConceptsForSearchSection(result);
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
    public void goToDsd(String urn) {
        if (!StringUtils.isBlank(urn)) {
            placeManager.revealRelativePlace(PlaceRequestUtils.buildRelativeDsdPlaceRequest(urn));
        }
    }
}
