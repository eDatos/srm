package org.siemac.metamac.srm.web.dsd.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.model.record.DsdRecord;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.utils.MetamacWebCriteriaClientUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdListUiHandlers;
import org.siemac.metamac.srm.web.shared.concept.GetStatisticalOperationsAction;
import org.siemac.metamac.srm.web.shared.concept.GetStatisticalOperationsResult;
import org.siemac.metamac.srm.web.shared.criteria.DataStructureDefinitionWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.StatisticalOperationWebCriteria;
import org.siemac.metamac.srm.web.shared.dsd.CancelDsdValidityAction;
import org.siemac.metamac.srm.web.shared.dsd.CancelDsdValidityResult;
import org.siemac.metamac.srm.web.shared.dsd.DeleteDsdsAction;
import org.siemac.metamac.srm.web.shared.dsd.DeleteDsdsResult;
import org.siemac.metamac.srm.web.shared.dsd.ExportDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.ExportDsdResult;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdsAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdsResult;
import org.siemac.metamac.srm.web.shared.dsd.SaveDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.SaveDsdResult;
import org.siemac.metamac.srm.web.shared.utils.SharedTokens;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.http.client.URL;
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

    public final static int                           DSD_LIST_FIRST_RESULT             = 0;
    public final static int                           DSD_LIST_MAX_RESULTS              = 30;

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

        public void setDsds(List<DataStructureDefinitionMetamacDto> dataStructureDefinitionDtos, int firstResult, int totalResults);
        HasRecordClickHandlers getSelectedDsd();
        List<String> getSelectedDsdUrns();
        void onNewDsdCreated();
        void clearSearchSection();

        void setOperations(GetStatisticalOperationsResult result);
        void setOperationsForSearchSection(GetStatisticalOperationsResult result);

        com.smartgwt.client.widgets.events.HasClickHandlers getDelete();
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
        retrieveDsdList(DSD_LIST_FIRST_RESULT, DSD_LIST_MAX_RESULTS,
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
                    goToDsd(record.getDsd().getUrn());
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
    public void goToDsd(String urn) {
        if (!StringUtils.isBlank(urn)) {
            placeManager.revealRelativePlace(PlaceRequestUtils.buildRelativeDsdPlaceRequest(urn));
        }
    }

    @Override
    public void saveDsd(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        dispatcher.execute(new SaveDsdAction(dataStructureDefinitionMetamacDto), new WaitingAsyncCallback<SaveDsdResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdListPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorSave()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(SaveDsdResult result) {
                retrieveDsdList(DSD_LIST_FIRST_RESULT, DSD_LIST_MAX_RESULTS,
                        MetamacWebCriteriaClientUtils.addLastVersionConditionToDataStructureDefinitionWebCriteria(new DataStructureDefinitionWebCriteria()));
                ShowMessageEvent.fire(DsdListPresenter.this, ErrorUtils.getMessageList(MetamacSrmWeb.getMessages().dsdSaved()), MessageTypeEnum.SUCCESS);
            }
        });
    }

    @Override
    public void deleteDsds(List<String> urns) {
        dispatcher.execute(new DeleteDsdsAction(urns), new WaitingAsyncCallback<DeleteDsdsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                retrieveDsdList(DSD_LIST_FIRST_RESULT, DSD_LIST_MAX_RESULTS,
                        MetamacWebCriteriaClientUtils.addLastVersionConditionToDataStructureDefinitionWebCriteria(new DataStructureDefinitionWebCriteria()));
                ShowMessageEvent.fire(DsdListPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorDelete()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(DeleteDsdsResult result) {
                retrieveDsdList(DSD_LIST_FIRST_RESULT, DSD_LIST_MAX_RESULTS,
                        MetamacWebCriteriaClientUtils.addLastVersionConditionToDataStructureDefinitionWebCriteria(new DataStructureDefinitionWebCriteria()));
                ShowMessageEvent.fire(DsdListPresenter.this, ErrorUtils.getMessageList(MetamacSrmWeb.getMessages().dsdDeleted()), MessageTypeEnum.SUCCESS);
            }
        });
    }

    /**
     * AsyncCallback to fetch DSDs
     */
    @Override
    public void retrieveDsdList(int firstResult, int maxResults, DataStructureDefinitionWebCriteria dataStructureDefinitionWebCriteria) {
        dispatcher.execute(new GetDsdsAction(firstResult, maxResults, dataStructureDefinitionWebCriteria), new WaitingAsyncCallback<GetDsdsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdListPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetDsdsResult result) {
                getView().setDsds(result.getDsdDtos(), result.getFirstResultOut(), result.getTotalResults());
            }
        });
    }

    @Override
    public void dsdSuccessfullyImported(String fileName) {
        ShowMessageEvent.fire(DsdListPresenter.this, ErrorUtils.getMessageList(MetamacSrmWeb.getMessages().dsdImported()), MessageTypeEnum.SUCCESS);
        getView().onNewDsdCreated();
    }

    @Override
    public void dsdImportFailed(String fileName) {
        ShowMessageEvent.fire(DsdListPresenter.this, ErrorUtils.getMessageList(MetamacSrmWeb.getMessages().dsdErrorImport()), MessageTypeEnum.ERROR);
    }

    @Override
    public void exportDsd(DataStructureDefinitionMetamacDto dsd) {
        dispatcher.execute(new ExportDsdAction(dsd.getUrn()), new WaitingAsyncCallback<ExportDsdResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdListPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorExport()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(ExportDsdResult result) {
                // TODO Is it better to use com.google.gwt.http.client.RequestBuilder to send the request?
                StringBuffer url = new StringBuffer();
                url.append(URL.encode(MetamacSrmWeb.getRelativeURL(SharedTokens.FILE_DOWNLOAD_DIR_PATH)));
                url.append("?").append(URL.encode(SharedTokens.PARAM_FILE_NAME)).append("=").append(URL.encode(result.getFileName()));
                Window.open(url.toString(), "_blank", "");
            }
        });
    }

    @Override
    public void cancelValidity(List<String> urns) {
        dispatcher.execute(new CancelDsdValidityAction(urns), new WaitingAsyncCallback<CancelDsdValidityResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdListPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().dsdErrorCancelValidity()), MessageTypeEnum.ERROR);
                retrieveDsdList(DSD_LIST_FIRST_RESULT, DSD_LIST_MAX_RESULTS,
                        MetamacWebCriteriaClientUtils.addLastVersionConditionToDataStructureDefinitionWebCriteria(new DataStructureDefinitionWebCriteria()));
            }
            @Override
            public void onWaitSuccess(CancelDsdValidityResult result) {
                ShowMessageEvent.fire(DsdListPresenter.this, ErrorUtils.getMessageList(getMessages().dsdCanceledValidity()), MessageTypeEnum.SUCCESS);
                retrieveDsdList(DSD_LIST_FIRST_RESULT, DSD_LIST_MAX_RESULTS,
                        MetamacWebCriteriaClientUtils.addLastVersionConditionToDataStructureDefinitionWebCriteria(new DataStructureDefinitionWebCriteria()));
            }
        });
    }

    @Override
    public void retrieveStatisticalOperations(int firstResult, int maxResults, String criteria) {
        StatisticalOperationWebCriteria statisticalOperationWebCriteria = new StatisticalOperationWebCriteria(criteria);
        dispatcher.execute(new GetStatisticalOperationsAction(firstResult, maxResults, statisticalOperationWebCriteria), new WaitingAsyncCallback<GetStatisticalOperationsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdListPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorRetrievingOperations()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetStatisticalOperationsResult result) {
                getView().setOperations(result);
            }
        });
    }

    @Override
    public void retrieveStatisticalOperationsForSearchSection(int firstResult, int maxResults, String criteria) {

        StatisticalOperationWebCriteria statisticalOperationWebCriteria = new StatisticalOperationWebCriteria(criteria);
        statisticalOperationWebCriteria.setNoFilterByUserPrincipal(true);

        dispatcher.execute(new GetStatisticalOperationsAction(firstResult, maxResults, statisticalOperationWebCriteria), new WaitingAsyncCallback<GetStatisticalOperationsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdListPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorRetrievingOperations()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetStatisticalOperationsResult result) {
                getView().setOperationsForSearchSection(result);
            }
        });
    }
}
