package org.siemac.metamac.srm.web.code.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
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
import org.siemac.metamac.srm.web.code.enums.CodesToolStripButtonEnum;
import org.siemac.metamac.srm.web.code.view.handlers.CodelistListUiHandlers;
import org.siemac.metamac.srm.web.code.widgets.presenter.CodesToolStripPresenterWidget;
import org.siemac.metamac.srm.web.shared.ExportSDMXResourceAction;
import org.siemac.metamac.srm.web.shared.code.CancelCodelistValidityAction;
import org.siemac.metamac.srm.web.shared.code.CancelCodelistValidityResult;
import org.siemac.metamac.srm.web.shared.code.DeleteCodelistsAction;
import org.siemac.metamac.srm.web.shared.code.DeleteCodelistsResult;
import org.siemac.metamac.srm.web.shared.code.GetCodelistsAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistsResult;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementsAction;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementsResult;
import org.siemac.metamac.srm.web.shared.code.GetVariablesAction;
import org.siemac.metamac.srm.web.shared.code.GetVariablesResult;
import org.siemac.metamac.srm.web.shared.code.SaveCodelistAction;
import org.siemac.metamac.srm.web.shared.code.SaveCodelistResult;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.VariableElementWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.VariableWebCriteria;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.WaitingAsyncCallbackHandlingError;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

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

public class CodelistListPresenter extends Presenter<CodelistListPresenter.CodelistListView, CodelistListPresenter.CodelistListProxy> implements CodelistListUiHandlers {

    private final DispatchAsync                       dispatcher;
    private final PlaceManager                        placeManager;

    private CodesToolStripPresenterWidget             codesToolStripPresenterWidget;

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetStructuralResourcesToolBar     = new Type<RevealContentHandler<?>>();

    public static final Object                        TYPE_SetContextAreaContentCodesToolBar = new Object();

    @ProxyCodeSplit
    @NameToken(NameTokens.codelistListPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface CodelistListProxy extends Proxy<CodelistListPresenter>, Place {
    }

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbCodelistList();
    }

    public interface CodelistListView extends View, HasUiHandlers<CodelistListUiHandlers> {

        void setCodelists(GetCodelistsResult codelistsPaginatedList);
        void setVariables(GetVariablesResult result);

        // Search
        void clearSearchSection();
        CodelistWebCriteria getCodelistWebCriteria();

        void setVariablesForSearch(GetVariablesResult result);

        void setVariableElementsForSearch(GetVariableElementsResult result);
    }

    @Inject
    public CodelistListPresenter(EventBus eventBus, CodelistListView codelistListView, CodelistListProxy codelistListProxy, DispatchAsync dispatcher, PlaceManager placeManager,
            CodesToolStripPresenterWidget codesToolStripPresenterWidget) {
        super(eventBus, codelistListView, codelistListProxy);
        this.placeManager = placeManager;
        this.dispatcher = dispatcher;
        this.codesToolStripPresenterWidget = codesToolStripPresenterWidget;
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
        retrieveCodelists(SrmWebConstants.SCHEME_LIST_FIRST_RESULT, SrmWebConstants.SCHEME_LIST_MAX_RESULTS,
                MetamacWebCriteriaClientUtils.addLastVersionConditionToCodelistWebCriteria(new CodelistWebCriteria()));
        // Clear search section
        getView().clearSearchSection();
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        setInSlot(TYPE_SetContextAreaContentCodesToolBar, codesToolStripPresenterWidget);

        SetTitleEvent.fire(this, getConstants().codelists());
        SelectMenuButtonEvent.fire(this, ToolStripButtonEnum.CODELISTS);
        codesToolStripPresenterWidget.selectCodesMenuButton(CodesToolStripButtonEnum.CODELISTS);
    }

    @Override
    public void createCodelist(CodelistMetamacDto codelistMetamacDto) {
        dispatcher.execute(new SaveCodelistAction(codelistMetamacDto), new WaitingAsyncCallbackHandlingError<SaveCodelistResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(CodelistListPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(SaveCodelistResult result) {
                fireSuccessMessage(getMessages().codelistSaved());
                retrieveCodelists(SrmWebConstants.SCHEME_LIST_FIRST_RESULT, SrmWebConstants.SCHEME_LIST_MAX_RESULTS,
                        MetamacWebCriteriaClientUtils.addLastVersionConditionToCodelistWebCriteria(getView().getCodelistWebCriteria()));
            }
        });
    }

    @Override
    public void deleteCodelists(List<String> urns) {
        dispatcher.execute(new DeleteCodelistsAction(urns), new WaitingAsyncCallbackHandlingError<DeleteCodelistsResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(CodelistListPresenter.this, caught);
                retrieveCodelists(SrmWebConstants.SCHEME_LIST_FIRST_RESULT, SrmWebConstants.SCHEME_LIST_MAX_RESULTS,
                        MetamacWebCriteriaClientUtils.addLastVersionConditionToCodelistWebCriteria(getView().getCodelistWebCriteria()));
            }
            @Override
            public void onWaitSuccess(DeleteCodelistsResult result) {
                fireSuccessMessage(getMessages().codelistDeleted());
                retrieveCodelists(SrmWebConstants.SCHEME_LIST_FIRST_RESULT, SrmWebConstants.SCHEME_LIST_MAX_RESULTS,
                        MetamacWebCriteriaClientUtils.addLastVersionConditionToCodelistWebCriteria(getView().getCodelistWebCriteria()));
            }
        });
    }

    @Override
    public void retrieveCodelists(int firstResult, int maxResults, CodelistWebCriteria codelistWebCriteria) {
        dispatcher.execute(new GetCodelistsAction(firstResult, maxResults, codelistWebCriteria), new WaitingAsyncCallbackHandlingError<GetCodelistsResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(CodelistListPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetCodelistsResult result) {
                getView().setCodelists(result);
            }
        });
    }

    @Override
    public void exportCodelists(List<String> urns, ExportDetailEnum detail, ExportReferencesEnum references) {
        dispatcher.execute(new ExportSDMXResourceAction(urns, RelatedResourceTypeEnum.CODELIST, detail, references), new WaitingAsyncCallbackHandlingExportResult(this));
    }

    @Override
    public void cancelValidity(List<String> urns) {
        dispatcher.execute(new CancelCodelistValidityAction(urns), new WaitingAsyncCallbackHandlingError<CancelCodelistValidityResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(CodelistListPresenter.this, caught);
                retrieveCodelists(SrmWebConstants.SCHEME_LIST_FIRST_RESULT, SrmWebConstants.SCHEME_LIST_MAX_RESULTS,
                        MetamacWebCriteriaClientUtils.addLastVersionConditionToCodelistWebCriteria(getView().getCodelistWebCriteria()));
            }
            @Override
            public void onWaitSuccess(CancelCodelistValidityResult result) {
                fireSuccessMessage(getMessages().codelistCanceledValidity());
                retrieveCodelists(SrmWebConstants.SCHEME_LIST_FIRST_RESULT, SrmWebConstants.SCHEME_LIST_MAX_RESULTS,
                        MetamacWebCriteriaClientUtils.addLastVersionConditionToCodelistWebCriteria(getView().getCodelistWebCriteria()));
            }
        });
    }

    @Override
    public void retrieveVariables(int firstResult, int maxResults, VariableWebCriteria criteria) {
        dispatcher.execute(new GetVariablesAction(firstResult, maxResults, criteria, null), new WaitingAsyncCallbackHandlingError<GetVariablesResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(CodelistListPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetVariablesResult result) {
                getView().setVariables(result);
            }
        });
    }

    @Override
    public void retrieveVariablesForSearch(int firstResult, int maxResults, VariableWebCriteria criteria) {
        dispatcher.execute(new GetVariablesAction(firstResult, maxResults, criteria, null), new WaitingAsyncCallbackHandlingError<GetVariablesResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(CodelistListPresenter.this, caught);
            }

            @Override
            public void onWaitSuccess(GetVariablesResult result) {
                getView().setVariablesForSearch(result);
            }
        });
    }

    @Override
    public void retrieveVariableElementsForSearch(int firstResult, int maxResults, MetamacWebCriteria metamacCriteria) {
        VariableElementWebCriteria criteria = new VariableElementWebCriteria(metamacCriteria != null ? metamacCriteria.getCriteria() : "");

        dispatcher.execute(new GetVariableElementsAction(firstResult, maxResults, criteria),
                new WaitingAsyncCallbackHandlingError<GetVariableElementsResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(CodelistListPresenter.this, caught);
            }

            @Override
                    public void onWaitSuccess(GetVariableElementsResult result) {
                        getView().setVariableElementsForSearch(result);
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
    public void goToCodelist(String urn) {
        if (!StringUtils.isBlank(urn)) {
            placeManager.revealRelativePlace(PlaceRequestUtils.buildRelativeCodelistPlaceRequest(urn));
        }
    }

}
