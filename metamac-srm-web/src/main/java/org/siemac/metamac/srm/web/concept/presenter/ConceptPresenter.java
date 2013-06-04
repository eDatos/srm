package org.siemac.metamac.srm.web.concept.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.Date;
import java.util.List;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.srm.core.concept.domain.shared.ConceptMetamacVisualisationResult;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacBasicDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptTypeDto;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptRoleEnum;
import org.siemac.metamac.srm.navigation.shared.NameTokens;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.concept.enums.ConceptsToolStripButtonEnum;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptUiHandlers;
import org.siemac.metamac.srm.web.concept.widgets.presenter.ConceptsToolStripPresenterWidget;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesAction;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.srm.web.shared.StructuralResourcesRelationEnum;
import org.siemac.metamac.srm.web.shared.code.GetVariableFamiliesAction;
import org.siemac.metamac.srm.web.shared.code.GetVariableFamiliesResult;
import org.siemac.metamac.srm.web.shared.code.GetVariablesAction;
import org.siemac.metamac.srm.web.shared.code.GetVariablesResult;
import org.siemac.metamac.srm.web.shared.concept.DeleteConceptAction;
import org.siemac.metamac.srm.web.shared.concept.DeleteConceptResult;
import org.siemac.metamac.srm.web.shared.concept.FindAllConceptTypesAction;
import org.siemac.metamac.srm.web.shared.concept.FindAllConceptTypesResult;
import org.siemac.metamac.srm.web.shared.concept.GetConceptAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptResult;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemeResult;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemesWithConceptsCanBeExtendedAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemesWithConceptsCanBeExtendedResult;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemesWithConceptsCanBeRoleAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemesWithConceptsCanBeRoleResult;
import org.siemac.metamac.srm.web.shared.concept.GetConceptsBySchemeAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptsBySchemeResult;
import org.siemac.metamac.srm.web.shared.concept.GetConceptsCanBeExtendedAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptsCanBeExtendedResult;
import org.siemac.metamac.srm.web.shared.concept.GetConceptsCanBeRoleAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptsCanBeRoleResult;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptAction;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptResult;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptSchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptWebCriteria;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.ApplicationEditionLanguages;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
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

public class ConceptPresenter extends Presenter<ConceptPresenter.ConceptView, ConceptPresenter.ConceptProxy> implements ConceptUiHandlers {

    private final DispatchAsync              dispatcher;
    private final PlaceManager               placeManager;

    private String                           conceptSchemeUrn;

    private ConceptsToolStripPresenterWidget conceptsToolStripPresenterWidget;

    private List<ConceptTypeDto>             conceptTypeDtos = null;

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbConcept();
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.conceptPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface ConceptProxy extends Proxy<ConceptPresenter>, Place {
    }

    public interface ConceptView extends View, HasUiHandlers<ConceptUiHandlers> {

        void setConcept(ConceptMetamacDto conceptDto, List<RelatedResourceDto> roles, List<ConceptMetamacBasicDto> relatedConcepts, ConceptSchemeMetamacDto conceptSchemeMetamacDto);
        void setConcept(ConceptMetamacDto conceptDto, List<RelatedResourceDto> roles, List<ConceptMetamacBasicDto> relatedConcepts);
        void setConceptList(ConceptSchemeMetamacDto conceptSchemeMetamacDto, List<ConceptMetamacVisualisationResult> itemVisualisationResults);

        void setConceptTypes(List<ConceptTypeDto> conceptTypeDtos);

        void setCodelistsOrConceptSchemesForEnumeratedRepresentation(GetRelatedResourcesResult result);

        void setConceptSchemesWithConceptsThatCanBeRole(List<RelatedResourceDto> conceptSchemes);
        void setConceptSchemesWithConceptsThatCanBeExtended(List<RelatedResourceDto> conceptSchemes);
        void setConceptThatCanBeRoles(List<RelatedResourceDto> conceptDtos, int firstResult, int totalResults);
        void setConceptThatCanBeExtended(List<RelatedResourceDto> conceptDtos, int firstResult, int totalResults);

        void setVariables(GetVariablesResult result);
        void setVariableFamilies(GetVariableFamiliesResult result);
    }

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaContentConcept         = new Type<RevealContentHandler<?>>();

    public static final Object                        TYPE_SetContextAreaContentConceptsToolBar = new Object();

    @Inject
    public ConceptPresenter(EventBus eventBus, ConceptView view, ConceptProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager,
            ConceptsToolStripPresenterWidget conceptsToolStripPresenterWidget) {
        super(eventBus, view, proxy);
        this.dispatcher = dispatcher;
        this.placeManager = placeManager;
        this.conceptsToolStripPresenterWidget = conceptsToolStripPresenterWidget;
        getView().setUiHandlers(this);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }

    @Override
    protected void onBind() {
        super.onBind();
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        setInSlot(TYPE_SetContextAreaContentConceptsToolBar, conceptsToolStripPresenterWidget);

        SetTitleEvent.fire(this, MetamacSrmWeb.getConstants().concept());
        SelectMenuButtonEvent.fire(this, ToolStripButtonEnum.CONCEPTS);
        conceptsToolStripPresenterWidget.selectConceptsMenuButton(ConceptsToolStripButtonEnum.CONCEPTS);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        String schemeParam = PlaceRequestUtils.getConceptSchemeParamFromUrl(placeManager);
        String conceptCode = PlaceRequestUtils.getConceptParamFromUrl(placeManager);
        if (!StringUtils.isBlank(schemeParam) && !StringUtils.isBlank(conceptCode)) {
            this.conceptSchemeUrn = UrnUtils.generateUrn(UrnConstants.URN_SDMX_CLASS_CONCEPTSCHEME_PREFIX, schemeParam);
            String urn = UrnUtils.generateUrn(UrnConstants.URN_SDMX_CLASS_CONCEPT_PREFIX, schemeParam, conceptCode);
            retrieveConcept(urn);
        } else {
            MetamacSrmWeb.showErrorPage();
        }
    }

    @Override
    public void retrieveConcept(String conceptUrn) {
        dispatcher.execute(new GetConceptAction(conceptUrn), new WaitingAsyncCallback<GetConceptResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetConceptResult result) {
                getView().setConcept(result.getConceptDto(), result.getRoles(), result.getRelatedConcepts(), result.getConceptSchemeMetamacDto());
            }
        });
    }

    @Override
    public void retrieveConceptsByScheme(String conceptSchemeUrn) {
        dispatcher.execute(new GetConceptsBySchemeAction(conceptSchemeUrn, ApplicationEditionLanguages.getCurrentLocale()), new WaitingAsyncCallback<GetConceptsBySchemeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorRetrievingConceptList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetConceptsBySchemeResult result) {
                final List<ConceptMetamacVisualisationResult> itemVisualisationResults = result.getItemVisualisationResults();
                dispatcher.execute(new GetConceptSchemeAction(ConceptPresenter.this.conceptSchemeUrn), new WaitingAsyncCallback<GetConceptSchemeResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fire(ConceptPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorRetrieve()), MessageTypeEnum.ERROR);
                    }
                    @Override
                    public void onWaitSuccess(GetConceptSchemeResult result) {
                        getView().setConceptList(result.getConceptSchemeDto(), itemVisualisationResults);
                    }
                });
            }
        });
    }

    /**
     * Only call this method to create concepts, not to update them!
     */
    @Override
    public void saveConcept(ConceptMetamacDto conceptDto) {
        // Create concept
        saveConcept(conceptDto, null, null);
    }

    @Override
    public void saveConcept(ConceptMetamacDto conceptDto, List<String> roles, List<String> relatedConcepts) {
        dispatcher.execute(new SaveConceptAction(conceptDto, roles, relatedConcepts), new WaitingAsyncCallback<SaveConceptResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptErrorSave()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(SaveConceptResult result) {
                ShowMessageEvent.fire(ConceptPresenter.this, ErrorUtils.getMessageList(getMessages().conceptSaved()), MessageTypeEnum.SUCCESS);
                getView().setConcept(result.getConceptDto(), result.getRoles(), result.getRelatedConcepts());

                // Update URL
                PlaceRequest placeRequest = PlaceRequestUtils.buildRelativeConceptPlaceRequest(result.getConceptDto().getUrn());
                placeManager.updateHistory(placeRequest, true);
            }
        });
    }

    @Override
    public void deleteConcept(final ItemVisualisationResult itemVisualisationResult) {
        dispatcher.execute(new DeleteConceptAction(itemVisualisationResult.getUrn()), new WaitingAsyncCallback<DeleteConceptResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptErrorDelete()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(DeleteConceptResult result) {
                // If deleted concept had a concept parent, go to this concept parent. If not, go to the concept scheme.
                if (itemVisualisationResult.getParent() != null && itemVisualisationResult.getParent().getUrn() != null) {
                    goToConcept(itemVisualisationResult.getParent().getUrn());
                } else {
                    goToConceptScheme(conceptSchemeUrn);
                }
            }
        });
    }

    @Override
    public void deleteConcept(final ConceptMetamacDto conceptMetamacDto) {
        dispatcher.execute(new DeleteConceptAction(conceptMetamacDto.getUrn()), new WaitingAsyncCallback<DeleteConceptResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptErrorDelete()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(DeleteConceptResult result) {
                // If deleted concept had a concept parent, go to this concept parent. If not, go to the concept scheme.
                if (conceptMetamacDto.getItemParentUrn() != null) {
                    goToConcept(conceptMetamacDto.getItemParentUrn());
                } else {
                    goToConceptScheme(conceptSchemeUrn);
                }
            }
        });
    }

    @Override
    public void retrieveConceptTypes() {
        if (conceptTypeDtos == null) {
            dispatcher.execute(new FindAllConceptTypesAction(), new WaitingAsyncCallback<FindAllConceptTypesResult>() {

                @Override
                public void onWaitFailure(Throwable caught) {
                    ShowMessageEvent.fire(ConceptPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptErrorRetrievingConceptTypeList()), MessageTypeEnum.ERROR);
                }
                @Override
                public void onWaitSuccess(FindAllConceptTypesResult result) {
                    conceptTypeDtos = result.getConceptTypeDtos();
                    getView().setConceptTypes(conceptTypeDtos);
                }
            });
        } else {
            getView().setConceptTypes(conceptTypeDtos);
        }
    }

    @Override
    public void retrieveCodelistsOrConceptSchemesForEnumeratedRepresentation(ConceptRoleEnum conceptRole, String variableUrn, int firstResult, int maxResults, String criteria, String conceptUrn) {

        if (ConceptRoleEnum.MEASURE_DIMENSION.equals(conceptRole)) {

            // the enumerated representation of a measure dimension concept must be a concept scheme

            ConceptSchemeWebCriteria conceptSchemeWebCriteria = new ConceptSchemeWebCriteria(criteria);
            conceptSchemeWebCriteria.setConceptUrn(conceptUrn);

            dispatcher.execute(new GetRelatedResourcesAction(StructuralResourcesRelationEnum.CONCEPT_SCHEME_WITH_CONCEPT_ENUMERATED_REPRESENTATION, firstResult, maxResults, conceptSchemeWebCriteria),
                    new WaitingAsyncCallback<GetRelatedResourcesResult>() {

                        @Override
                        public void onWaitFailure(Throwable caught) {
                            ShowMessageEvent.fire(ConceptPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().conceptSchemeErrorRetrieveList()), MessageTypeEnum.ERROR);
                        }
                        @Override
                        public void onWaitSuccess(GetRelatedResourcesResult result) {
                            getView().setCodelistsOrConceptSchemesForEnumeratedRepresentation(result);
                        }
                    });

        } else {

            // for the rest of the concepts, the enumerated representation must be a codelist

            CodelistWebCriteria codelistWebCriteria = new CodelistWebCriteria(criteria);
            if (!StringUtils.isBlank(variableUrn)) {

                // The concept URN is not set in the criteria because if it were set, the query to find the codelists will take into account the concept that is in the DB, and we want to take into
                // account the concept that is being edited at this moment (in the facade, if the concept is null, take into account the variable passed as paramter, not the one that is in the DB)

                codelistWebCriteria.setVariableUrn(variableUrn);

            } else {
                codelistWebCriteria.setConceptUrn(conceptUrn);
            }

            dispatcher.execute(new GetRelatedResourcesAction(StructuralResourcesRelationEnum.CODELIST_WITH_CONCEPT_ENUMERATED_REPRESENTATION, firstResult, maxResults, codelistWebCriteria),
                    new WaitingAsyncCallback<GetRelatedResourcesResult>() {

                        @Override
                        public void onWaitFailure(Throwable caught) {
                            ShowMessageEvent.fire(ConceptPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().codelistErrorRetrieveList()), MessageTypeEnum.ERROR);
                        }
                        @Override
                        public void onWaitSuccess(GetRelatedResourcesResult result) {
                            getView().setCodelistsOrConceptSchemesForEnumeratedRepresentation(result);
                        }
                    });
        }
    }

    @Override
    public void retrieveConceptsThatCanBeRole(int firstResult, int maxResults, String criteria, String conceptSchemeUrn) {
        ConceptWebCriteria conceptWebCriteria = new ConceptWebCriteria(criteria);
        conceptWebCriteria.setItemSchemeUrn(conceptSchemeUrn);
        dispatcher.execute(new GetConceptsCanBeRoleAction(firstResult, maxResults, conceptWebCriteria), new WaitingAsyncCallback<GetConceptsCanBeRoleResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().conceptErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetConceptsCanBeRoleResult result) {
                getView().setConceptThatCanBeRoles(result.getConcepts(), result.getFirstResultOut(), result.getTotalResults());
            }
        });
    }

    @Override
    public void retrieveConceptsThatCanBeExtended(int firstResult, int maxResults, String criteria, String conceptSchemeUrn) {
        ConceptWebCriteria conceptWebCriteria = new ConceptWebCriteria(criteria);
        conceptWebCriteria.setItemSchemeUrn(conceptSchemeUrn);
        dispatcher.execute(new GetConceptsCanBeExtendedAction(firstResult, maxResults, conceptWebCriteria), new WaitingAsyncCallback<GetConceptsCanBeExtendedResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().conceptErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetConceptsCanBeExtendedResult result) {
                getView().setConceptThatCanBeExtended(result.getConceptList(), result.getFirstResultOut(), result.getTotalResults());
            }
        });
    }

    @Override
    public void retrieveConceptSchemesWithConceptsThatCanBeRole(int firstResult, int maxResults) {
        dispatcher.execute(new GetConceptSchemesWithConceptsCanBeRoleAction(firstResult, maxResults, null), new WaitingAsyncCallback<GetConceptSchemesWithConceptsCanBeRoleResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().conceptSchemeErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetConceptSchemesWithConceptsCanBeRoleResult result) {
                getView().setConceptSchemesWithConceptsThatCanBeRole(result.getConceptSchemes());
            }
        });
    }

    @Override
    public void retrieveConceptSchemesWithConceptsThatCanBeExtended(int firstResult, int maxResults) {
        dispatcher.execute(new GetConceptSchemesWithConceptsCanBeExtendedAction(firstResult, maxResults, null), new WaitingAsyncCallback<GetConceptSchemesWithConceptsCanBeExtendedResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().conceptSchemeErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetConceptSchemesWithConceptsCanBeExtendedResult result) {
                getView().setConceptSchemesWithConceptsThatCanBeExtended(result.getConceptSchemes());
            }
        });
    }

    @Override
    public void retrieveVariables(int firstResult, int maxResults, String criteria, String variableFamilyUrn) {
        dispatcher.execute(new GetVariablesAction(firstResult, maxResults, criteria, variableFamilyUrn), new WaitingAsyncCallback<GetVariablesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().variableErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetVariablesResult result) {
                getView().setVariables(result);
            }
        });
    }

    @Override
    public void retrieveVariableFamilies(int firstResult, int maxResults, String criteria) {
        dispatcher.execute(new GetVariableFamiliesAction(firstResult, maxResults, criteria), new WaitingAsyncCallback<GetVariableFamiliesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().variableFamilyErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetVariableFamiliesResult result) {
                getView().setVariableFamilies(result);
            }
        });
    }

    @Override
    public void retrieveCategorisations(String artefactCategorisedUrn) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void createCategorisations(List<String> categoryUrns) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void cancelCategorisationValidity(List<String> urns, Date validTo) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteCategorisations(List<String> urns) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void retrieveCategorySchemesForCategorisations(int firstResult, int maxResults, String criteria) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void retrieveCategoriesForCategorisations(int firstResult, int maxResults, String criteria, String categorySchemeUrn) {
        throw new UnsupportedOperationException();
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
    public void goToConcept(String urn) {
        placeManager.revealRelativePlace(PlaceRequestUtils.buildRelativeConceptPlaceRequest(urn), -1);
    }

    @Override
    public void goToConceptScheme(String urn) {
        goTo(PlaceRequestUtils.buildAbsoluteConceptSchemePlaceRequest(urn));
    }
}
