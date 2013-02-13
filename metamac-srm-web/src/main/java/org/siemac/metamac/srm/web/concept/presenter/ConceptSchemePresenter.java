package org.siemac.metamac.srm.web.concept.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptSchemeUiHandlers;
import org.siemac.metamac.srm.web.shared.category.CreateCategorisationAction;
import org.siemac.metamac.srm.web.shared.category.CreateCategorisationResult;
import org.siemac.metamac.srm.web.shared.category.DeleteCategorisationsAction;
import org.siemac.metamac.srm.web.shared.category.DeleteCategorisationsResult;
import org.siemac.metamac.srm.web.shared.category.GetCategoriesAction;
import org.siemac.metamac.srm.web.shared.category.GetCategoriesResult;
import org.siemac.metamac.srm.web.shared.category.GetCategorisationsByArtefactAction;
import org.siemac.metamac.srm.web.shared.category.GetCategorisationsByArtefactResult;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemesAction;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemesResult;
import org.siemac.metamac.srm.web.shared.concept.CancelConceptSchemeValidityAction;
import org.siemac.metamac.srm.web.shared.concept.CancelConceptSchemeValidityResult;
import org.siemac.metamac.srm.web.shared.concept.DeleteConceptAction;
import org.siemac.metamac.srm.web.shared.concept.DeleteConceptResult;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemeResult;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemeVersionsAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemeVersionsResult;
import org.siemac.metamac.srm.web.shared.concept.GetConceptsBySchemeAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptsBySchemeResult;
import org.siemac.metamac.srm.web.shared.concept.GetStatisticalOperationsAction;
import org.siemac.metamac.srm.web.shared.concept.GetStatisticalOperationsResult;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptAction;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptResult;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptSchemeResult;
import org.siemac.metamac.srm.web.shared.concept.UpdateConceptSchemeProcStatusAction;
import org.siemac.metamac.srm.web.shared.concept.UpdateConceptSchemeProcStatusResult;
import org.siemac.metamac.srm.web.shared.concept.VersionConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.concept.VersionConceptSchemeResult;
import org.siemac.metamac.srm.web.shared.criteria.CategorySchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.CategoryWebCriteria;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.UrnUtils;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
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

public class ConceptSchemePresenter extends Presenter<ConceptSchemePresenter.ConceptSchemeView, ConceptSchemePresenter.ConceptSchemeProxy> implements ConceptSchemeUiHandlers {

    private final DispatchAsync     dispatcher;
    private final PlaceManager      placeManager;

    private ConceptSchemeMetamacDto conceptSchemeDto;

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbConceptScheme();
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.conceptSchemePage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface ConceptSchemeProxy extends Proxy<ConceptSchemePresenter>, Place {
    }

    public interface ConceptSchemeView extends View, HasUiHandlers<ConceptSchemeUiHandlers> {

        void setConceptScheme(ConceptSchemeMetamacDto conceptSchemeDto);
        void setConcepts(List<ItemHierarchyDto> itemHierarchyDtos);
        void setConceptSchemeVersions(List<ConceptSchemeMetamacDto> conceptSchemeDtos);
        void setOperations(GetStatisticalOperationsResult result);

        // Categorisations

        void setCategorisations(List<CategorisationDto> categorisationDtos);
        void setCategorySchemesForCategorisations(GetCategorySchemesResult result);
        void setCategoriesForCategorisations(GetCategoriesResult result);
    }

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaContentConceptScheme = new Type<RevealContentHandler<?>>();

    @Inject
    public ConceptSchemePresenter(EventBus eventBus, ConceptSchemeView view, ConceptSchemeProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager) {
        super(eventBus, view, proxy);
        this.placeManager = placeManager;
        this.dispatcher = dispatcher;
        getView().setUiHandlers(this);
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        SetTitleEvent.fire(this, MetamacSrmWeb.getConstants().conceptScheme());
        SelectMenuButtonEvent.fire(this, ToolStripButtonEnum.CONCEPTS);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        String identifier = PlaceRequestUtils.getConceptSchemeParamFromUrl(placeManager);
        if (identifier != null) {
            retrieveConceptScheme(identifier);
        }
    }

    private void setConceptScheme(ConceptSchemeMetamacDto conceptScheme) {
        getView().setConceptScheme(conceptScheme);
    }

    @Override
    public void saveConceptScheme(ConceptSchemeMetamacDto conceptScheme) {
        dispatcher.execute(new SaveConceptSchemeAction(conceptScheme), new WaitingAsyncCallback<SaveConceptSchemeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorSave()), MessageTypeEnum.ERROR);
            }

            @Override
            public void onWaitSuccess(SaveConceptSchemeResult result) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getMessageList(getMessages().conceptSchemeSaved()), MessageTypeEnum.SUCCESS);
                ConceptSchemePresenter.this.conceptSchemeDto = result.getSavedConceptSchemeDto();
                setConceptScheme(result.getSavedConceptSchemeDto());

                // Update URL
                PlaceRequest placeRequest = PlaceRequestUtils.buildRelativeConceptSchemePlaceRequest(conceptSchemeDto.getUrn());
                placeManager.updateHistory(placeRequest, true);
            }
        });
    }

    @Override
    public void retrieveConceptScheme(String param) {
        String conceptSchemeUrn = UrnUtils.generateUrn(UrnConstants.URN_SDMX_CLASS_CONCEPTSCHEME_PREFIX, param);
        retrieveConceptSchemeByUrn(conceptSchemeUrn);
        retrieveCategorisations(conceptSchemeUrn);
    }

    private void retrieveConceptSchemeByUrn(String urn) {
        dispatcher.execute(new GetConceptSchemeAction(urn), new WaitingAsyncCallback<GetConceptSchemeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorRetrieve()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetConceptSchemeResult result) {
                ConceptSchemePresenter.this.conceptSchemeDto = result.getConceptSchemeDto();
                setConceptScheme(result.getConceptSchemeDto());
                retrieveConceptListByScheme(result.getConceptSchemeDto().getUrn());
                retrieveConceptSchemeVersions(result.getConceptSchemeDto().getUrn());
            }
        });
    }

    @Override
    public void sendToProductionValidation(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        dispatcher.execute(new UpdateConceptSchemeProcStatusAction(conceptSchemeMetamacDto, ProcStatusEnum.PRODUCTION_VALIDATION), new WaitingAsyncCallback<UpdateConceptSchemeProcStatusResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorSendingToProductionValidation()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(UpdateConceptSchemeProcStatusResult result) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getMessageList(getMessages().conceptSchemeSentToProductionValidation()), MessageTypeEnum.SUCCESS);
                ConceptSchemePresenter.this.conceptSchemeDto = result.getConceptSchemeDto();
                setConceptScheme(result.getConceptSchemeDto());
            }
        });
    }

    @Override
    public void sendToDiffusionValidation(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        dispatcher.execute(new UpdateConceptSchemeProcStatusAction(conceptSchemeMetamacDto, ProcStatusEnum.DIFFUSION_VALIDATION), new WaitingAsyncCallback<UpdateConceptSchemeProcStatusResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorSendingToDiffusionValidation()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(UpdateConceptSchemeProcStatusResult result) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getMessageList(getMessages().conceptSchemeSentToDiffusionValidation()), MessageTypeEnum.SUCCESS);
                ConceptSchemePresenter.this.conceptSchemeDto = result.getConceptSchemeDto();
                setConceptScheme(result.getConceptSchemeDto());
            }
        });
    }

    @Override
    public void rejectValidation(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        dispatcher.execute(new UpdateConceptSchemeProcStatusAction(conceptSchemeMetamacDto, ProcStatusEnum.VALIDATION_REJECTED), new WaitingAsyncCallback<UpdateConceptSchemeProcStatusResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorRejecting()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(UpdateConceptSchemeProcStatusResult result) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getMessageList(getMessages().conceptSchemeRejected()), MessageTypeEnum.SUCCESS);
                ConceptSchemePresenter.this.conceptSchemeDto = result.getConceptSchemeDto();
                setConceptScheme(result.getConceptSchemeDto());
            }
        });
    }

    @Override
    public void publishInternally(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        dispatcher.execute(new UpdateConceptSchemeProcStatusAction(conceptSchemeMetamacDto, ProcStatusEnum.INTERNALLY_PUBLISHED), new WaitingAsyncCallback<UpdateConceptSchemeProcStatusResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorPublishingInternally()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(UpdateConceptSchemeProcStatusResult result) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getMessageList(getMessages().conceptSchemePublishedInternally()), MessageTypeEnum.SUCCESS);
                ConceptSchemePresenter.this.conceptSchemeDto = result.getConceptSchemeDto();
                setConceptScheme(result.getConceptSchemeDto());
            }
        });
    }

    @Override
    public void publishExternally(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        dispatcher.execute(new UpdateConceptSchemeProcStatusAction(conceptSchemeMetamacDto, ProcStatusEnum.EXTERNALLY_PUBLISHED), new WaitingAsyncCallback<UpdateConceptSchemeProcStatusResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorPublishingExternally()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(UpdateConceptSchemeProcStatusResult result) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getMessageList(getMessages().conceptSchemePublishedExternally()), MessageTypeEnum.SUCCESS);
                ConceptSchemePresenter.this.conceptSchemeDto = result.getConceptSchemeDto();
                setConceptScheme(result.getConceptSchemeDto());
            }
        });
    }

    @Override
    public void versioning(final String urn, VersionTypeEnum versionType) {
        dispatcher.execute(new VersionConceptSchemeAction(urn, versionType), new WaitingAsyncCallback<VersionConceptSchemeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorVersioning()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(VersionConceptSchemeResult result) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getMessageList(getMessages().conceptSchemeVersioned()), MessageTypeEnum.SUCCESS);
                ConceptSchemePresenter.this.conceptSchemeDto = result.getConceptSchemeDto();
                retrieveConceptSchemeByUrn(conceptSchemeDto.getUrn());

                // Update URL
                PlaceRequest placeRequest = PlaceRequestUtils.buildRelativeConceptSchemePlaceRequest(conceptSchemeDto.getUrn());
                placeManager.updateHistory(placeRequest, true);
            }
        });
    }

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
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptErrorCreate()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(SaveConceptResult result) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getMessageList(getMessages().conceptCreated()), MessageTypeEnum.SUCCESS);
                retrieveConceptSchemeByUrn(conceptSchemeDto.getUrn());
            }
        });
    }

    @Override
    public void retrieveConceptListByScheme(String conceptSchemeUrn) {
        dispatcher.execute(new GetConceptsBySchemeAction(conceptSchemeUrn), new WaitingAsyncCallback<GetConceptsBySchemeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorRetrievingConceptList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetConceptsBySchemeResult result) {
                getView().setConcepts(result.getItemHierarchyDtos());
            }
        });
    }

    @Override
    public void retrieveConceptSchemeVersions(String conceptSchemeUrn) {
        dispatcher.execute(new GetConceptSchemeVersionsAction(conceptSchemeUrn), new WaitingAsyncCallback<GetConceptSchemeVersionsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorRetrievingVersions()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetConceptSchemeVersionsResult result) {
                getView().setConceptSchemeVersions(result.getConceptSchemeDtos());
            }
        });
    }

    @Override
    public void deleteConcept(ItemDto itemDto) {
        dispatcher.execute(new DeleteConceptAction(itemDto.getUrn()), new WaitingAsyncCallback<DeleteConceptResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptErrorDelete()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(DeleteConceptResult result) {
                retrieveConceptListByScheme(conceptSchemeDto.getUrn());
            }
        });
    }

    @Override
    public void cancelValidity(final String urn) {
        List<String> urns = new ArrayList<String>();
        urns.add(urn);
        dispatcher.execute(new CancelConceptSchemeValidityAction(urns), new WaitingAsyncCallback<CancelConceptSchemeValidityResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorCancelValidity()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(CancelConceptSchemeValidityResult result) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getMessageList(getMessages().conceptSchemeCanceledValidity()), MessageTypeEnum.SUCCESS);
                retrieveConceptSchemeByUrn(urn);
            }
        });
    }

    @Override
    public void retrieveStatisticalOperations(int firstResult, int maxResults, String criteria) {
        dispatcher.execute(new GetStatisticalOperationsAction(firstResult, maxResults, criteria), new WaitingAsyncCallback<GetStatisticalOperationsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorRetrievingOperations()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetStatisticalOperationsResult result) {
                getView().setOperations(result);
            }
        });
    }

    @Override
    public void retrieveCategorisations(String artefactCategorisedUrn) {
        dispatcher.execute(new GetCategorisationsByArtefactAction(artefactCategorisedUrn), new WaitingAsyncCallback<GetCategorisationsByArtefactResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categorisationErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCategorisationsByArtefactResult result) {
                getView().setCategorisations(result.getCategorisationDtos());
            }
        });
    }

    @Override
    public void createCategorisations(List<String> categoryUrns) {
        dispatcher.execute(new CreateCategorisationAction(categoryUrns, conceptSchemeDto.getUrn(), RelatedResourceUtils.getDefaultMaintainerAsRelatedResourceDto().getUrn()),
                new WaitingAsyncCallback<CreateCategorisationResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categorisationErrorCreate()), MessageTypeEnum.ERROR);
                    }
                    @Override
                    public void onWaitSuccess(CreateCategorisationResult result) {
                        ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getMessageList(getMessages().categorisationCreated()), MessageTypeEnum.SUCCESS);
                        retrieveCategorisations(conceptSchemeDto.getUrn());
                    }
                });
    }

    @Override
    public void deleteCategorisations(List<String> urns) {
        dispatcher.execute(new DeleteCategorisationsAction(urns), new WaitingAsyncCallback<DeleteCategorisationsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categorisationErrorDelete()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(DeleteCategorisationsResult result) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getMessageList(getMessages().categorisationDeleted()), MessageTypeEnum.SUCCESS);
                retrieveCategorisations(conceptSchemeDto.getUrn());
            }
        });
    }

    @Override
    public void retrieveCategorySchemesForCategorisations(int firstResult, int maxResults, String criteria) {
        // The categories must be externally published
        CategorySchemeWebCriteria categorySchemeWebCriteria = new CategorySchemeWebCriteria(criteria);
        categorySchemeWebCriteria.setProcStatus(ProcStatusEnum.EXTERNALLY_PUBLISHED);
        dispatcher.execute(new GetCategorySchemesAction(firstResult, maxResults, categorySchemeWebCriteria), new WaitingAsyncCallback<GetCategorySchemesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categorySchemeErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCategorySchemesResult result) {
                getView().setCategorySchemesForCategorisations(result);
            }
        });
    }

    @Override
    public void retrieveCategoriesForCategorisations(int firstResult, int maxResults, String criteria, String categorySchemeUrn) {
        // The categories must be externally published
        CategoryWebCriteria categoryWebCriteria = new CategoryWebCriteria(criteria, categorySchemeUrn);
        categoryWebCriteria.setIsExternallyPublished(true);
        dispatcher.execute(new GetCategoriesAction(firstResult, maxResults, categoryWebCriteria), new WaitingAsyncCallback<GetCategoriesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categoryErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCategoriesResult result) {
                getView().setCategoriesForCategorisations(result);
            }
        });
    }

    @Override
    public void goToConcept(String urn) {
        placeManager.revealRelativePlace(PlaceRequestUtils.buildRelativeConceptPlaceRequest(urn));
    }

    @Override
    public void goToConceptScheme(String urn) {
        if (!StringUtils.isBlank(urn)) {
            placeManager.revealRelativePlace(PlaceRequestUtils.buildRelativeConceptSchemePlaceRequest(urn), -1);
        }
    }
}
