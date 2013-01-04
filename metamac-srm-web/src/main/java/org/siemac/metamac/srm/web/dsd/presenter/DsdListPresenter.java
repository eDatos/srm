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
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.dsd.events.SelectDsdAndDescriptorsEvent;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdListUiHandlers;
import org.siemac.metamac.srm.web.shared.dsd.CancelDsdValidityAction;
import org.siemac.metamac.srm.web.shared.dsd.CancelDsdValidityResult;
import org.siemac.metamac.srm.web.shared.dsd.DeleteDsdListAction;
import org.siemac.metamac.srm.web.shared.dsd.DeleteDsdListResult;
import org.siemac.metamac.srm.web.shared.dsd.ExportDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.ExportDsdResult;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdAndDescriptorsAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdAndDescriptorsResult;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdListAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdListResult;
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
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;
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
        DataStructureDefinitionMetamacDto getNewDsd();
        void onNewDsdCreated();

        void clearSearchSection();

        HasClickHandlers getCreateDsd();
        com.smartgwt.client.widgets.events.HasClickHandlers getDelete();

        boolean validate();
        void closeDsdWindow();
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
        retrieveDsdList(DSD_LIST_FIRST_RESULT, DSD_LIST_MAX_RESULTS, null);
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

        // Save DSD
        registerHandler(getView().getCreateDsd().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (getView().validate()) {
                    DataStructureDefinitionMetamacDto dsd = getView().getNewDsd();
                    saveDsd(dsd);
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

    /**
     * Go to a new DSD
     */
    @Override
    public void goToDsd(String urn) {
        if (!StringUtils.isBlank(urn)) {
            placeManager.revealRelativePlace(PlaceRequestUtils.buildDsdPlaceRequest(urn));
            dispatcher.execute(new GetDsdAndDescriptorsAction(urn), new WaitingAsyncCallback<GetDsdAndDescriptorsResult>() {

                @Override
                public void onWaitFailure(Throwable caught) {
                    ShowMessageEvent.fire(DsdListPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorRetrievingData()), MessageTypeEnum.ERROR);
                }
                @Override
                public void onWaitSuccess(GetDsdAndDescriptorsResult result) {
                    SelectDsdAndDescriptorsEvent.fire(DsdListPresenter.this, result.getDsd(), result.getPrimaryMeasure(), result.getDimensions(), result.getAttributes(), result.getGroupKeys());
                }
            });
        }
    }

    @Override
    public void saveDsd(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        dispatcher.execute(new SaveDsdAction(dataStructureDefinitionMetamacDto), new WaitingAsyncCallback<SaveDsdResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                getView().closeDsdWindow();
                ShowMessageEvent.fire(DsdListPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorSave()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(SaveDsdResult result) {
                getView().closeDsdWindow();
                retrieveDsdList(DSD_LIST_FIRST_RESULT, DSD_LIST_MAX_RESULTS, null);
                ShowMessageEvent.fire(DsdListPresenter.this, ErrorUtils.getMessageList(MetamacSrmWeb.getMessages().dsdSaved()), MessageTypeEnum.SUCCESS);
            }
        });
    }

    @Override
    public void deleteDsds(List<String> urns) {
        dispatcher.execute(new DeleteDsdListAction(urns), new WaitingAsyncCallback<DeleteDsdListResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                retrieveDsdList(DSD_LIST_FIRST_RESULT, DSD_LIST_MAX_RESULTS, null);
                ShowMessageEvent.fire(DsdListPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorDelete()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(DeleteDsdListResult result) {
                retrieveDsdList(DSD_LIST_FIRST_RESULT, DSD_LIST_MAX_RESULTS, null);
                ShowMessageEvent.fire(DsdListPresenter.this, ErrorUtils.getMessageList(MetamacSrmWeb.getMessages().dsdDeleted()), MessageTypeEnum.SUCCESS);
            }
        });
    }

    /**
     * AsyncCallback to fetch DSDs
     */
    @Override
    public void retrieveDsdList(int firstResult, int maxResults, final String dsd) {
        dispatcher.execute(new GetDsdListAction(firstResult, maxResults, dsd), new WaitingAsyncCallback<GetDsdListResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdListPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetDsdListResult result) {
                getView().setDsds(result.getDsdDtos(), result.getFirstResultOut(), result.getTotalResults());
                if (StringUtils.isBlank(dsd)) {
                    getView().clearSearchSection();
                }
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
                retrieveDsdList(DSD_LIST_FIRST_RESULT, DSD_LIST_MAX_RESULTS, null);
            }
            @Override
            public void onWaitSuccess(CancelDsdValidityResult result) {
                ShowMessageEvent.fire(DsdListPresenter.this, ErrorUtils.getMessageList(getMessages().dsdCanceledValidity()), MessageTypeEnum.SUCCESS);
                retrieveDsdList(DSD_LIST_FIRST_RESULT, DSD_LIST_MAX_RESULTS, null);
            }
        });
    }

}
