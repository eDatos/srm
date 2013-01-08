package org.siemac.metamac.srm.web.concept.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
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
import org.siemac.metamac.srm.web.shared.concept.CancelConceptSchemeValidityAction;
import org.siemac.metamac.srm.web.shared.concept.CancelConceptSchemeValidityResult;
import org.siemac.metamac.srm.web.shared.concept.DeleteConceptAction;
import org.siemac.metamac.srm.web.shared.concept.DeleteConceptResult;
import org.siemac.metamac.srm.web.shared.concept.GetConceptListBySchemeAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptListBySchemeResult;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemeResult;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemeVersionsAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemeVersionsResult;
import org.siemac.metamac.srm.web.shared.concept.GetStatisticalOperationsPaginatedListAction;
import org.siemac.metamac.srm.web.shared.concept.GetStatisticalOperationsPaginatedListResult;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptAction;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptResult;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptSchemeResult;
import org.siemac.metamac.srm.web.shared.concept.UpdateConceptSchemeProcStatusAction;
import org.siemac.metamac.srm.web.shared.concept.UpdateConceptSchemeProcStatusResult;
import org.siemac.metamac.srm.web.shared.concept.VersionConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.concept.VersionConceptSchemeResult;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.UrnUtils;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

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
        void setConceptList(List<ItemHierarchyDto> itemHierarchyDtos);
        void setConceptSchemeVersions(List<ConceptSchemeMetamacDto> conceptSchemeDtos);
        void setOperations(List<ExternalItemDto> operations, int firstResult, int totalResults);
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
        String urn = PlaceRequestUtils.getConceptSchemeParamFromUrl(placeManager);
        if (urn != null) {
            retrieveConceptScheme(urn);
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
        String schemeUrn = UrnUtils.generateUrn(UrnConstants.URN_SDMX_CLASS_CONCEPTSCHEME_PREFIX, param);
        retrieveConceptSchemeByUrn(schemeUrn);
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
    public void sendToProductionValidation(String urn, ProcStatusEnum procStatus) {
        dispatcher.execute(new UpdateConceptSchemeProcStatusAction(urn, ProcStatusEnum.PRODUCTION_VALIDATION, procStatus), new WaitingAsyncCallback<UpdateConceptSchemeProcStatusResult>() {

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
    public void sendToDiffusionValidation(String urn, ProcStatusEnum procStatus) {
        dispatcher.execute(new UpdateConceptSchemeProcStatusAction(urn, ProcStatusEnum.DIFFUSION_VALIDATION, procStatus), new WaitingAsyncCallback<UpdateConceptSchemeProcStatusResult>() {

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
    public void rejectValidation(String urn, ProcStatusEnum procStatus) {
        dispatcher.execute(new UpdateConceptSchemeProcStatusAction(urn, ProcStatusEnum.VALIDATION_REJECTED, procStatus), new WaitingAsyncCallback<UpdateConceptSchemeProcStatusResult>() {

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
    public void publishInternally(String urn, ProcStatusEnum procStatus) {
        dispatcher.execute(new UpdateConceptSchemeProcStatusAction(urn, ProcStatusEnum.INTERNALLY_PUBLISHED, procStatus), new WaitingAsyncCallback<UpdateConceptSchemeProcStatusResult>() {

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
    public void publishExternally(String urn, ProcStatusEnum procStatus) {
        dispatcher.execute(new UpdateConceptSchemeProcStatusAction(urn, ProcStatusEnum.EXTERNALLY_PUBLISHED, procStatus), new WaitingAsyncCallback<UpdateConceptSchemeProcStatusResult>() {

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
        dispatcher.execute(new GetConceptListBySchemeAction(conceptSchemeUrn), new WaitingAsyncCallback<GetConceptListBySchemeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorRetrievingConceptList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetConceptListBySchemeResult result) {
                getView().setConceptList(result.getItemHierarchyDtos());
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
    public void goToConcept(String urn) {
        placeManager.revealRelativePlace(PlaceRequestUtils.buildRelativeConceptPlaceRequest(urn));
    }

    @Override
    public void goToConceptScheme(String urn) {
        if (!StringUtils.isBlank(urn)) {
            placeManager.revealRelativePlace(PlaceRequestUtils.buildRelativeConceptSchemePlaceRequest(urn), -1);
        }
    }

    @Override
    public void retrieveStatisticalOperations(int firstResult, int maxResults, String operation) {
        dispatcher.execute(new GetStatisticalOperationsPaginatedListAction(firstResult, maxResults, operation), new WaitingAsyncCallback<GetStatisticalOperationsPaginatedListResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorRetrievingOperations()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetStatisticalOperationsPaginatedListResult result) {
                getView().setOperations(result.getOperations(), result.getFirstResultOut(), result.getTotalResults());
            }
        });
    }
}
