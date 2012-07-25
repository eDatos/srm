package org.siemac.metamac.srm.web.dsd.presenter;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.PlaceRequestParams;
import org.siemac.metamac.srm.web.client.model.record.DsdRecord;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.widgets.presenter.ToolStripPresenterWidget;
import org.siemac.metamac.srm.web.dsd.events.SelectDsdAndDescriptorsEvent;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdListUiHandlers;
import org.siemac.metamac.srm.web.shared.dsd.DeleteDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.DeleteDsdListAction;
import org.siemac.metamac.srm.web.shared.dsd.DeleteDsdListResult;
import org.siemac.metamac.srm.web.shared.dsd.DeleteDsdResult;
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
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.UrnUtils;
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

    private final DispatchAsync                       dispatcher;
    private final PlaceManager                        placeManager;

    private ToolStripPresenterWidget                  toolStripPresenterWidget;

    private List<DataStructureDefinitionDto>          dsdList;

    /**
     * Use this in leaf presenters, inside their {@link #revealInParent} method.
     */
    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaContentDsdList        = new Type<RevealContentHandler<?>>();

    public static final Object                        TYPE_SetContextAreaContentDsdListToolBar = new Object();

    @ProxyCodeSplit
    @NameToken(NameTokens.dsdListPage)
    public interface DsdListProxy extends Proxy<DsdListPresenter>, Place {
    }

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbDsdList();
    }

    public interface DsdListView extends View, HasUiHandlers<DsdListUiHandlers> {

        public void setDsds(List<DataStructureDefinitionDto> dataStructureDefinitionDtos);
        HasRecordClickHandlers getSelectedDsd();
        List<DataStructureDefinitionDto> getSelectedDsds();
        DataStructureDefinitionDto getNewDsd();

        HasClickHandlers getCreateDsd();
        com.smartgwt.client.widgets.events.HasClickHandlers getDelete();

        boolean validate();
        void closeDsdWindow();
    }

    @Inject
    public DsdListPresenter(EventBus eventBus, DsdListView dsdListView, DsdListProxy dsdListProxy, DispatchAsync dispatcher, PlaceManager placeManager,
            ToolStripPresenterWidget toolStripPresenterWidget) {
        super(eventBus, dsdListView, dsdListProxy);
        this.dispatcher = dispatcher;
        this.placeManager = placeManager;
        this.toolStripPresenterWidget = toolStripPresenterWidget;
        getView().setUiHandlers(this);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        retrieveDsds();
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
                    DataStructureDefinitionDto dsd = getView().getNewDsd();
                    saveDsd(dsd);
                }
            }
        }));

        // Delete DSD
        registerHandler(getView().getDelete().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                List<DataStructureDefinitionDto> dsdsToDelete = getView().getSelectedDsds();
                deleteDsds(dsdsToDelete);
            }
        }));

    }

    @Override
    protected void onReset() {
        super.onReset();
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        setInSlot(TYPE_SetContextAreaContentDsdListToolBar, toolStripPresenterWidget);
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
            placeManager.revealRelativePlace(new PlaceRequest(NameTokens.dsdPage).with(PlaceRequestParams.dsdParam, UrnUtils.removePrefix(urn)));
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
    public void saveDsd(DataStructureDefinitionDto dataStructureDefinitionDto) {
        dispatcher.execute(new SaveDsdAction(dataStructureDefinitionDto), new WaitingAsyncCallback<SaveDsdResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                getView().closeDsdWindow();
                ShowMessageEvent.fire(DsdListPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorSave()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(SaveDsdResult result) {
                getView().closeDsdWindow();
                DataStructureDefinitionDto dsdSaved = result.getDsdSaved();
                goToDsd(dsdSaved.getUrn());
            }
        });
    }

    @Override
    public void deleteDsd(DataStructureDefinitionDto dataStructureDefinitionDto) {
        dispatcher.execute(new DeleteDsdAction(dataStructureDefinitionDto), new WaitingAsyncCallback<DeleteDsdResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdListPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorDelete()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(DeleteDsdResult result) {
                ShowMessageEvent.fire(DsdListPresenter.this, ErrorUtils.getMessageList(MetamacSrmWeb.getMessages().dsdDeleted()), MessageTypeEnum.SUCCESS);
                retrieveDsds();
            }
        });
    }

    @Override
    public void deleteDsds(List<DataStructureDefinitionDto> dataStructureDefinitionDtos) {
        dispatcher.execute(new DeleteDsdListAction(dataStructureDefinitionDtos), new WaitingAsyncCallback<DeleteDsdListResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                retrieveDsds();
                ShowMessageEvent.fire(DsdListPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorDelete()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(DeleteDsdListResult result) {
                retrieveDsds();
                ShowMessageEvent.fire(DsdListPresenter.this, ErrorUtils.getMessageList(MetamacSrmWeb.getMessages().dsdDeleted()), MessageTypeEnum.SUCCESS);
            }
        });
    }

    /**
     * AsyncCallback to fetch DSDs
     */
    @Override
    public void retrieveDsds() {
        dispatcher.execute(new GetDsdListAction(), new WaitingAsyncCallback<GetDsdListResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(DsdListPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetDsdListResult result) {
                dsdList = result.getDsdDtos();
                getView().setDsds(dsdList);
            }
        });
    }

    @Override
    public void dsdSuccessfullyImported(String fileName) {
        ShowMessageEvent.fire(DsdListPresenter.this, ErrorUtils.getMessageList(MetamacSrmWeb.getMessages().dsdImported()), MessageTypeEnum.SUCCESS);
        // Reload DSD list
        retrieveDsds();
    }

    @Override
    public void dsdImportFailed(String fileName) {
        ShowMessageEvent.fire(DsdListPresenter.this, ErrorUtils.getMessageList(MetamacSrmWeb.getMessages().dsdErrorImport()), MessageTypeEnum.ERROR);
    }

    @Override
    public void exportDsd(DataStructureDefinitionDto dsd) {
        dispatcher.execute(new ExportDsdAction(dsd), new WaitingAsyncCallback<ExportDsdResult>() {

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

}
