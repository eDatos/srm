package org.siemac.metamac.srm.web.client.presenter;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacBasicDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacBasicDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacBasicDto;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.client.view.handlers.StructuralResourcesUiHandlers;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemesAction;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemesResult;
import org.siemac.metamac.srm.web.shared.code.GetCodelistsAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistsResult;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemesAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemesResult;
import org.siemac.metamac.srm.web.shared.criteria.CategorySchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptSchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.DataStructureDefinitionWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.OrganisationSchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdsAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdsResult;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemesAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemesResult;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
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

public class StructuralResourcesPresenter extends Presenter<StructuralResourcesPresenter.StructuralResourcesView, StructuralResourcesPresenter.StructuralResourcesProxy>
        implements
            StructuralResourcesUiHandlers {

    public final static int     RESOURCE_LIST_FIRST_RESULT = 0;
    public final static int     RESOURCE_LIST_MAX_RESULTS  = 10;

    private final DispatchAsync dispatcher;
    private final PlaceManager  placeManager;

    @ProxyCodeSplit
    @NameToken(NameTokens.structuralResourcesPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface StructuralResourcesProxy extends Proxy<StructuralResourcesPresenter>, Place {

    }

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbStrucResourcesDashboard();
    }

    public interface StructuralResourcesView extends View, HasUiHandlers<StructuralResourcesUiHandlers> {

        void setDsdList(List<DataStructureDefinitionMetamacBasicDto> dataStructureDefinitionMetamacDtos);
        void setConceptSchemeList(List<ConceptSchemeMetamacBasicDto> conceptSchemeDtos);
        void setOrganisationSchemeList(List<OrganisationSchemeMetamacBasicDto> organisationSchemeMetamacDtos);
        void setCategorySchemesList(List<CategorySchemeMetamacBasicDto> categorySchemeMetamacDtos);
        void setCodelistList(List<CodelistMetamacBasicDto> codelistMetamacDtos);

        void resetView();
    }

    /**
     * Use this in leaf presenters, inside their {@link #revealInParent} method.
     */
    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaContent = new Type<RevealContentHandler<?>>();

    @Inject
    public StructuralResourcesPresenter(EventBus eventBus, StructuralResourcesView structuralResourcesView, StructuralResourcesProxy structuralResourcesProxy, DispatchAsync dispatcher,
            PlaceManager placeManager) {
        super(eventBus, structuralResourcesView, structuralResourcesProxy);
        this.placeManager = placeManager;
        this.dispatcher = dispatcher;
        getView().setUiHandlers(this);
    }

    @Override
    protected void onReset() {
        super.onReset();
        getView().resetView();
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        SetTitleEvent.fire(this, MetamacSrmWeb.getConstants().structuralResourcesDahsboard());
        SelectMenuButtonEvent.fire(StructuralResourcesPresenter.this, null);
    }

    @Override
    protected void onHide() {
        super.onHide();
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        retrieveDsds();
        retrieveConceptSchemes();
        retrieveOrganisationSchemes();
        retrieveCategorySchemes();
        retrieveCodelists();
    }

    private void retrieveDsds() {
        DataStructureDefinitionWebCriteria dataStructureDefinitionWebCriteria = new DataStructureDefinitionWebCriteria();
        dataStructureDefinitionWebCriteria.setIsLastVersion(true);
        dispatcher.execute(new GetDsdsAction(RESOURCE_LIST_FIRST_RESULT, RESOURCE_LIST_MAX_RESULTS, dataStructureDefinitionWebCriteria), new WaitingAsyncCallback<GetDsdsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(StructuralResourcesPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().dsdErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetDsdsResult result) {
                getView().setDsdList(result.getDsdDtos());
            }
        });
    }

    private void retrieveConceptSchemes() {
        ConceptSchemeWebCriteria conceptSchemeWebCriteria = new ConceptSchemeWebCriteria();
        conceptSchemeWebCriteria.setIsLastVersion(true);
        dispatcher.execute(new GetConceptSchemesAction(RESOURCE_LIST_FIRST_RESULT, RESOURCE_LIST_MAX_RESULTS, conceptSchemeWebCriteria), new WaitingAsyncCallback<GetConceptSchemesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(StructuralResourcesPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().conceptSchemeErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetConceptSchemesResult result) {
                getView().setConceptSchemeList(result.getConceptSchemeList());
            }
        });
    }

    private void retrieveOrganisationSchemes() {
        OrganisationSchemeWebCriteria organisationSchemeWebCriteria = new OrganisationSchemeWebCriteria();
        organisationSchemeWebCriteria.setIsLastVersion(true);
        dispatcher.execute(new GetOrganisationSchemesAction(RESOURCE_LIST_FIRST_RESULT, RESOURCE_LIST_MAX_RESULTS, organisationSchemeWebCriteria),
                new WaitingAsyncCallback<GetOrganisationSchemesResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fire(StructuralResourcesPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().organisationSchemeErrorRetrieveList()),
                                MessageTypeEnum.ERROR);
                    }
                    @Override
                    public void onWaitSuccess(GetOrganisationSchemesResult result) {
                        getView().setOrganisationSchemeList(result.getOrganisationSchemeMetamacDtos());
                    }
                });
    }

    private void retrieveCategorySchemes() {
        CategorySchemeWebCriteria categorySchemeWebCriteria = new CategorySchemeWebCriteria();
        categorySchemeWebCriteria.setIsLastVersion(true);
        dispatcher.execute(new GetCategorySchemesAction(RESOURCE_LIST_FIRST_RESULT, RESOURCE_LIST_MAX_RESULTS, categorySchemeWebCriteria), new WaitingAsyncCallback<GetCategorySchemesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(StructuralResourcesPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().categorySchemeErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCategorySchemesResult result) {
                getView().setCategorySchemesList(result.getCategorySchemeList());
            }
        });
    }

    private void retrieveCodelists() {
        CodelistWebCriteria codelistWebCriteria = new CodelistWebCriteria();
        codelistWebCriteria.setIsLastVersion(true);
        dispatcher.execute(new GetCodelistsAction(RESOURCE_LIST_FIRST_RESULT, RESOURCE_LIST_MAX_RESULTS, codelistWebCriteria), new WaitingAsyncCallback<GetCodelistsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(StructuralResourcesPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().codelistErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCodelistsResult result) {
                getView().setCodelistList(result.getCodelists());
            }
        });
    }

    /**************************************************************************
     * OperationsUiHandlers methods
     **************************************************************************/

    @Override
    public void goToDsd(String urn) {
        if (!StringUtils.isBlank(urn)) {
            placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteDsdPlaceRequest(urn));
        }
    }

    @Override
    public void goToConceptScheme(String urn) {
        if (urn != null) {
            placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteConceptSchemePlaceRequest(urn));
        }
    }

    @Override
    public void goToOrganisationScheme(String urn, OrganisationSchemeTypeEnum type) {
        if (urn != null) {
            placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteOrganisationSchemePlaceRequest(urn, type));
        }
    }

    @Override
    public void goToCategoryScheme(String urn) {
        if (urn != null) {
            placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteCategorySchemePlaceRequest(urn));
        }
    }

    @Override
    public void goToCodelist(String urn) {
        if (urn != null) {
            placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteCodelistPlaceRequest(urn));
        }
    }
}
