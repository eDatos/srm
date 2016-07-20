package org.siemac.metamac.srm.web.code.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.Arrays;
import java.util.List;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementOperationDto;
import org.siemac.metamac.srm.core.code.enume.domain.VariableElementOperationTypeEnum;
import org.siemac.metamac.srm.navigation.shared.NameTokens;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.code.enums.CodesToolStripButtonEnum;
import org.siemac.metamac.srm.web.code.view.handlers.VariableUiHandlers;
import org.siemac.metamac.srm.web.code.widgets.presenter.CodesToolStripPresenterWidget;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesAction;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.srm.web.shared.StructuralResourcesRelationEnum;
import org.siemac.metamac.srm.web.shared.code.CreateVariableElementOperationAction;
import org.siemac.metamac.srm.web.shared.code.CreateVariableElementOperationResult;
import org.siemac.metamac.srm.web.shared.code.DeleteVariableElementOperationsAction;
import org.siemac.metamac.srm.web.shared.code.DeleteVariableElementOperationsResult;
import org.siemac.metamac.srm.web.shared.code.DeleteVariableElementsAction;
import org.siemac.metamac.srm.web.shared.code.DeleteVariableElementsResult;
import org.siemac.metamac.srm.web.shared.code.DeleteVariablesAction;
import org.siemac.metamac.srm.web.shared.code.DeleteVariablesResult;
import org.siemac.metamac.srm.web.shared.code.ExportVariableElementsAction;
import org.siemac.metamac.srm.web.shared.code.ExportVariableElementsResult;
import org.siemac.metamac.srm.web.shared.code.GetVariableAction;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementOperationsByVariableAction;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementOperationsByVariableResult;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementsAction;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementsResult;
import org.siemac.metamac.srm.web.shared.code.GetVariableFamiliesAction;
import org.siemac.metamac.srm.web.shared.code.GetVariableFamiliesResult;
import org.siemac.metamac.srm.web.shared.code.GetVariableResult;
import org.siemac.metamac.srm.web.shared.code.GetVariablesAction;
import org.siemac.metamac.srm.web.shared.code.GetVariablesResult;
import org.siemac.metamac.srm.web.shared.code.SaveVariableAction;
import org.siemac.metamac.srm.web.shared.code.SaveVariableElementAction;
import org.siemac.metamac.srm.web.shared.code.SaveVariableElementResult;
import org.siemac.metamac.srm.web.shared.code.SaveVariableResult;
import org.siemac.metamac.srm.web.shared.criteria.CodeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.VariableElementWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.VariableWebCriteria;
import org.siemac.metamac.web.common.client.events.ChangeWaitPopupVisibilityEvent;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
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

public class VariablePresenter extends Presenter<VariablePresenter.VariableView, VariablePresenter.VariableProxy> implements VariableUiHandlers {

    public final static int               ELEMENT_LIST_FIRST_RESULT = 0;
    public final static int               ELEMENT_LIST_MAX_RESULTS  = 15;

    private final DispatchAsync           dispatcher;
    private final PlaceManager            placeManager;

    private CodesToolStripPresenterWidget codesToolStripPresenterWidget;

    private VariableDto                   variableDto;

    @TitleFunction
    public static String getTranslatedTitle(PlaceRequest placeRequest) {
        return PlaceRequestUtils.getVariableBreadCrumbTitle(placeRequest);
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.variablePage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface VariableProxy extends Proxy<VariablePresenter>, Place {
    }

    public interface VariableView extends View, HasUiHandlers<VariableUiHandlers> {

        void setVariable(VariableDto variableFamilyDto);
        void setVariableFamilies(GetVariableFamiliesResult result);
        void setVariables(GetVariablesResult result);

        void setVariableElements(GetVariableElementsResult result);
        void setVariableElementsForFusion(GetVariableElementsResult result);
        void setVariableElementsForSegregation(GetVariableElementsResult result);
        void setVariableElementOperations(List<VariableElementOperationDto> variableElementOperationDtos);

        void setCodelistsForVariableElementGeographicalGranularity(GetRelatedResourcesResult result);
        void setCodesForVariableElementGeographicalGranularity(GetRelatedResourcesResult result);

        void clearSearchSection();
    }

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaContentCodelist     = new Type<RevealContentHandler<?>>();

    public static final Object                        TYPE_SetContextAreaContentCodesToolBar = new Object();

    @Inject
    public VariablePresenter(EventBus eventBus, VariableView view, VariableProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager, CodesToolStripPresenterWidget codesToolStripPresenterWidget) {
        super(eventBus, view, proxy);
        this.placeManager = placeManager;
        this.dispatcher = dispatcher;
        this.codesToolStripPresenterWidget = codesToolStripPresenterWidget;
        getView().setUiHandlers(this);
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        setInSlot(TYPE_SetContextAreaContentCodesToolBar, codesToolStripPresenterWidget);

        SetTitleEvent.fire(this, getConstants().variable());
        SelectMenuButtonEvent.fire(this, ToolStripButtonEnum.CODELISTS);
        codesToolStripPresenterWidget.selectCodesMenuButton(CodesToolStripButtonEnum.VARIABLES);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        String identifier = PlaceRequestUtils.getVariableParamFromUrl(placeManager);
        if (!StringUtils.isBlank(identifier)) {
            retrieveVariable(identifier);
            getView().clearSearchSection();
        } else {
            MetamacSrmWeb.showErrorPage();
        }
    }

    private void retrieveVariable(String identifier) {
        String urn = UrnUtils.generateUrn(UrnConstants.URN_SIEMAC_CLASS_CODELIST_VARIABLE_PREFIX, identifier);
        if (!StringUtils.isBlank(urn)) {
            retrieveVariableByUrn(urn);
            retrieveVariableElementsByVariable(ELEMENT_LIST_FIRST_RESULT, ELEMENT_LIST_MAX_RESULTS, null, urn);
            retrieveVariableElementOperations(urn);
        }
    }

    @Override
    public void retrieveVariableByUrn(String urn) {
        dispatcher.execute(new GetVariableAction(urn), new WaitingAsyncCallbackHandlingError<GetVariableResult>(this) {

            @Override
            public void onWaitSuccess(GetVariableResult result) {
                VariablePresenter.this.variableDto = result.getVariableDto();
                getView().setVariable(result.getVariableDto());
            }
        });
    }

    @Override
    public void retrieveVariableFamilies(int firstResult, int maxResults, String criteria) {
        dispatcher.execute(new GetVariableFamiliesAction(firstResult, maxResults, criteria), new WaitingAsyncCallbackHandlingError<GetVariableFamiliesResult>(this) {

            @Override
            public void onWaitSuccess(GetVariableFamiliesResult result) {
                getView().setVariableFamilies(result);
            }
        });
    }

    @Override
    public void saveVariable(VariableDto variableDto) {
        dispatcher.execute(new SaveVariableAction(variableDto), new WaitingAsyncCallbackHandlingError<SaveVariableResult>(this) {

            @Override
            public void onWaitSuccess(SaveVariableResult result) {
                VariablePresenter.this.variableDto = result.getSavedVariableDto();
                fireSuccessMessage(getMessages().variableSaved());
                getView().setVariable(result.getSavedVariableDto());

                // Update URL
                PlaceRequest placeRequest = PlaceRequestUtils.buildRelativeVariablePlaceRequest(result.getSavedVariableDto().getCode());
                placeManager.updateHistory(placeRequest, true);
            }
        });
    }

    @Override
    public void deleteVariable(String urn) {
        dispatcher.execute(new DeleteVariablesAction(Arrays.asList(urn)), new WaitingAsyncCallbackHandlingError<DeleteVariablesResult>(this) {

            @Override
            public void onWaitSuccess(DeleteVariablesResult result) {
                fireSuccessMessage(getMessages().variableDeleted());
                placeManager.revealRelativePlace(-1);
            }
        });
    }

    @Override
    public void retrieveVariables(int firstResult, int maxResults, VariableWebCriteria criteria) {
        dispatcher.execute(new GetVariablesAction(firstResult, maxResults, criteria, null), new WaitingAsyncCallbackHandlingError<GetVariablesResult>(this) {

            @Override
            public void onWaitSuccess(GetVariablesResult result) {
                getView().setVariables(result);
            }
        });
    }

    @Override
    public void retrieveVariableElementsByVariable(int firstResult, int maxResults, String criteria, String variableUrn) {
        VariableElementWebCriteria variableElementWebCriteria = new VariableElementWebCriteria(criteria);
        variableElementWebCriteria.setVariableUrn(variableUrn);
        dispatcher.execute(new GetVariableElementsAction(firstResult, maxResults, variableElementWebCriteria), new WaitingAsyncCallbackHandlingError<GetVariableElementsResult>(this) {

            @Override
            public void onWaitSuccess(GetVariableElementsResult result) {
                getView().setVariableElements(result);
            }
        });
    }

    @Override
    public void retrieveVariableElementsByVariableForFusionOperation(int firstResult, int maxResults, String criteria, String variableUrn) {
        VariableElementWebCriteria variableElementWebCriteria = new VariableElementWebCriteria(criteria);
        variableElementWebCriteria.setVariableUrn(variableUrn);
        dispatcher.execute(new GetVariableElementsAction(firstResult, maxResults, variableElementWebCriteria), new WaitingAsyncCallbackHandlingError<GetVariableElementsResult>(this) {

            @Override
            public void onWaitSuccess(GetVariableElementsResult result) {
                getView().setVariableElementsForFusion(result);
            }
        });
    }

    @Override
    public void retrieveVariableElementsByVariableForSegregationOperation(int firstResult, int maxResults, String criteria, String variableUrn) {
        VariableElementWebCriteria variableElementWebCriteria = new VariableElementWebCriteria(criteria);
        variableElementWebCriteria.setVariableUrn(variableUrn);
        dispatcher.execute(new GetVariableElementsAction(firstResult, maxResults, variableElementWebCriteria), new WaitingAsyncCallbackHandlingError<GetVariableElementsResult>(this) {

            @Override
            public void onWaitSuccess(GetVariableElementsResult result) {
                getView().setVariableElementsForSegregation(result);
            }
        });
    }

    @Override
    public void createVariableElement(VariableElementDto variableElementDto) {
        dispatcher.execute(new SaveVariableElementAction(variableElementDto), new WaitingAsyncCallbackHandlingError<SaveVariableElementResult>(this) {

            @Override
            public void onWaitSuccess(SaveVariableElementResult result) {
                retrieveVariableByUrn(result.getSavedVariableElementDto().getVariable().getUrn()); // To update the metadata 'hasVariableElements'
                retrieveVariableElementsByVariable(ELEMENT_LIST_FIRST_RESULT, ELEMENT_LIST_MAX_RESULTS, null, variableDto.getUrn());
                fireSuccessMessage(MetamacSrmWeb.getMessages().variableElementSaved());
            }
        });
    }

    @Override
    public void deleteVariableElements(List<String> urns) {
        dispatcher.execute(new DeleteVariableElementsAction(urns), new WaitingAsyncCallbackHandlingError<DeleteVariableElementsResult>(this) {

            @Override
            public void onWaitSuccess(DeleteVariableElementsResult result) {
                retrieveVariableElementsByVariable(ELEMENT_LIST_FIRST_RESULT, ELEMENT_LIST_MAX_RESULTS, null, variableDto.getUrn());
                fireSuccessMessage(MetamacSrmWeb.getMessages().variableElementDeleted());
            }
        });
    }

    @Override
    public void createSegregation(String variableElementUrn, List<String> variableElementUrns) {
        createVariableElementOperation(VariableElementOperationTypeEnum.SEGREGATION, variableElementUrns, variableElementUrn);
    }

    @Override
    public void createFusion(List<String> variableElementUrns, String variableElementUrn) {
        createVariableElementOperation(VariableElementOperationTypeEnum.FUSION, variableElementUrns, variableElementUrn);
    }

    @Override
    public void deleteVariableElementOperations(List<String> codes) {
        dispatcher.execute(new DeleteVariableElementOperationsAction(codes), new WaitingAsyncCallbackHandlingError<DeleteVariableElementOperationsResult>(this) {

            @Override
            public void onWaitSuccess(DeleteVariableElementOperationsResult result) {
                retrieveVariableElementOperations(variableDto.getUrn());
            }
        });
    }

    private void retrieveVariableElementOperations(String variableUrn) {
        dispatcher.execute(new GetVariableElementOperationsByVariableAction(variableUrn), new WaitingAsyncCallbackHandlingError<GetVariableElementOperationsByVariableResult>(this) {

            @Override
            public void onWaitSuccess(GetVariableElementOperationsByVariableResult result) {
                getView().setVariableElementOperations(result.getOperations());
            }
        });
    }

    private void createVariableElementOperation(final VariableElementOperationTypeEnum type, List<String> variableElementUrns, String variableElementUrn) {
        dispatcher.execute(new CreateVariableElementOperationAction(type, variableElementUrns, variableElementUrn), new WaitingAsyncCallbackHandlingError<CreateVariableElementOperationResult>(this) {

            @Override
            public void onWaitSuccess(CreateVariableElementOperationResult result) {
                if (VariableElementOperationTypeEnum.FUSION.equals(type)) {
                    fireSuccessMessage(MetamacSrmWeb.getMessages().variableElementFusioned());
                } else {
                    fireSuccessMessage(MetamacSrmWeb.getMessages().variableElementSegregated());
                }
                retrieveVariableElementOperations(variableDto.getUrn());
            }
        });
    }

    //
    // RELATED RESOURCES
    //

    @Override
    public void retrieveCodelistsForVariableElementGeographicalGranularity(int firstResult, int maxResults, CodelistWebCriteria codelistWebCriteria) {
        dispatcher.execute(new GetRelatedResourcesAction(StructuralResourcesRelationEnum.CODELIST_WITH_VARIABLE_ELEMENT_GEOGRAPHICAL_GRANULARITY, firstResult, maxResults, codelistWebCriteria),
                new WaitingAsyncCallbackHandlingError<GetRelatedResourcesResult>(this) {

                    @Override
                    public void onWaitSuccess(GetRelatedResourcesResult result) {
                        getView().setCodelistsForVariableElementGeographicalGranularity(result);
                    }
                });
    }

    @Override
    public void retrieveCodesForVariableElementGeographicalGranularity(int firstResult, int maxResults, CodeWebCriteria codeWebCriteria) {
        dispatcher.execute(new GetRelatedResourcesAction(StructuralResourcesRelationEnum.CODES_WITH_VARIABLE_ELEMENT_GEOGRAPHICAL_GRANULARITY, firstResult, maxResults, codeWebCriteria),
                new WaitingAsyncCallbackHandlingError<GetRelatedResourcesResult>(this) {

                    @Override
                    public void onWaitSuccess(GetRelatedResourcesResult result) {
                        getView().setCodesForVariableElementGeographicalGranularity(result);
                    }
                });
    }

    //
    // IMPORTATION
    //

    @Override
    public void resourceImportationSucceed(String successMessage) {
        ShowMessageEvent.fireSuccessMessage(VariablePresenter.this, successMessage);
        retrieveVariableElementsByVariable(ELEMENT_LIST_FIRST_RESULT, ELEMENT_LIST_MAX_RESULTS, null, variableDto.getUrn());
        ChangeWaitPopupVisibilityEvent.fire(this, false);
    }

    @Override
    public void resourceImportationFailed(String errorMessage) {
        ShowMessageEvent.fireErrorMessage(VariablePresenter.this, errorMessage);
        ChangeWaitPopupVisibilityEvent.fire(this, false);
    }

    //
    // EXPORTATION
    //

    @Override
    public void exportVariableElements(String variableUrn) {
        dispatcher.execute(new ExportVariableElementsAction(variableUrn), new WaitingAsyncCallbackHandlingError<ExportVariableElementsResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(VariablePresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(ExportVariableElementsResult result) {
                CommonUtils.downloadFile(result.getFileName());
            }
        });
    };

    @Override
    public void showWaitPopup() {
        ChangeWaitPopupVisibilityEvent.fire(this, true);
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
    public void goToVariableElement(String code) {
        if (!StringUtils.isBlank(code)) {
            placeManager.revealRelativePlace(PlaceRequestUtils.buildRelativeVariableElementPlaceRequest(code));
        }
    }
}
