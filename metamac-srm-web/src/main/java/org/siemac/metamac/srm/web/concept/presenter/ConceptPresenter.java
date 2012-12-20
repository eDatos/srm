package org.siemac.metamac.srm.web.concept.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.List;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptTypeDto;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.client.widgets.presenter.ToolStripPresenterWidget;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptUiHandlers;
import org.siemac.metamac.srm.web.shared.FindCodeListsAction;
import org.siemac.metamac.srm.web.shared.FindCodeListsResult;
import org.siemac.metamac.srm.web.shared.concept.DeleteConceptAction;
import org.siemac.metamac.srm.web.shared.concept.DeleteConceptResult;
import org.siemac.metamac.srm.web.shared.concept.FindAllConceptTypesAction;
import org.siemac.metamac.srm.web.shared.concept.FindAllConceptTypesResult;
import org.siemac.metamac.srm.web.shared.concept.GetConceptAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptListBySchemeAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptListBySchemeResult;
import org.siemac.metamac.srm.web.shared.concept.GetConceptResult;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemeResult;
import org.siemac.metamac.srm.web.shared.concept.GetConceptsCanBeExtendedAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptsCanBeExtendedResult;
import org.siemac.metamac.srm.web.shared.concept.GetConceptsCanBeRoleAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptsCanBeRoleResult;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptAction;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptResult;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.UrnUtils;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
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

public class ConceptPresenter extends Presenter<ConceptPresenter.ConceptView, ConceptPresenter.ConceptProxy> implements ConceptUiHandlers {

    private final DispatchAsync      dispatcher;
    private final PlaceManager       placeManager;
    private ToolStripPresenterWidget toolStripPresenterWidget;

    private String                   conceptSchemeUrn;

    private List<ConceptTypeDto>     conceptTypeDtos = null;

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

        void setConcept(ConceptMetamacDto conceptDto, List<RelatedResourceDto> roles, List<ConceptMetamacDto> relatedConcepts);
        void setConceptExtended(ConceptMetamacDto conceptDto);
        void setConceptList(ConceptSchemeMetamacDto conceptSchemeMetamacDto, List<ItemHierarchyDto> itemHierarchyDtos);

        void setConceptTypes(List<ConceptTypeDto> conceptTypeDtos);
        void setCodeLists(List<ExternalItemDto> codeLists);

        void setConceptThatCanBeRoles(List<RelatedResourceDto> conceptDtos, int firstResult, int totalResults);
        void setConceptThatCanBeExtended(List<RelatedResourceDto> conceptDtos, int firstResult, int totalResults);
    }

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaContentConcept        = new Type<RevealContentHandler<?>>();

    public static final Object                        TYPE_SetContextAreaContentConceptToolBar = new Object();

    @Inject
    public ConceptPresenter(EventBus eventBus, ConceptView view, ConceptProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager, ToolStripPresenterWidget toolStripPresenterWidget) {
        super(eventBus, view, proxy);
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
    protected void onBind() {
        super.onBind();
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        setInSlot(TYPE_SetContextAreaContentConceptToolBar, toolStripPresenterWidget);
        MainPagePresenter.getMasterHead().setTitleLabel(MetamacSrmWeb.getConstants().concept());
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
                getView().setConcept(result.getConceptDto(), result.getRoles(), result.getRelatedConcepts());
            }
        });
    }

    @Override
    public void retrieveConceptExtended(String conceptUrn) {
        dispatcher.execute(new GetConceptAction(conceptUrn), new WaitingAsyncCallback<GetConceptResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetConceptResult result) {
                getView().setConceptExtended(result.getConceptDto());
            }
        });
    }

    @Override
    public void retrieveConceptsByScheme(String conceptSchemeUrn) {
        dispatcher.execute(new GetConceptListBySchemeAction(conceptSchemeUrn), new WaitingAsyncCallback<GetConceptListBySchemeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorRetrievingConceptList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetConceptListBySchemeResult result) {
                final List<ItemHierarchyDto> itemHierarchyDtos = result.getItemHierarchyDtos();
                dispatcher.execute(new GetConceptSchemeAction(ConceptPresenter.this.conceptSchemeUrn), new WaitingAsyncCallback<GetConceptSchemeResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fire(ConceptPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorRetrieve()), MessageTypeEnum.ERROR);
                    }
                    @Override
                    public void onWaitSuccess(GetConceptSchemeResult result) {
                        getView().setConceptList(result.getConceptSchemeDto(), itemHierarchyDtos);
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
                PlaceRequest placeRequest = PlaceRequestUtils.buildConceptPlaceRequest(result.getConceptDto().getUrn());
                placeManager.updateHistory(placeRequest, true);
            }
        });
    }

    @Override
    public void deleteConcept(final ItemDto itemDto) {
        dispatcher.execute(new DeleteConceptAction(itemDto.getUrn()), new WaitingAsyncCallback<DeleteConceptResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptErrorDelete()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(DeleteConceptResult result) {
                // If deleted concept had a concept parent, go to this concept parent. If not, go to the concept scheme.
                if (itemDto.getItemParentUrn() != null) {
                    goToConcept(itemDto.getItemParentUrn());
                } else {
                    goToConceptScheme(itemDto.getItemSchemeVersionUrn());
                }
            }
        });
    }

    @Override
    public void goToConcept(String urn) {
        placeManager.revealRelativePlace(PlaceRequestUtils.buildConceptPlaceRequest(urn), -1);
    }

    private void goToConceptScheme(String urn) {
        placeManager.revealRelativePlace(PlaceRequestUtils.buildConceptSchemePlaceRequest(urn), -2);
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
    public void retrieveCodeLists(String conceptUrn) {
        dispatcher.execute(new FindCodeListsAction(conceptUrn), new WaitingAsyncCallback<FindCodeListsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptPresenter.this, ErrorUtils.getErrorMessages(caught, MetamacSrmWeb.getMessages().codeListsErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(FindCodeListsResult result) {
                getView().setCodeLists(result.getCodeLists());
            }
        });
    }

    @Override
    public void retrieveConceptsThatCanBeRole(int firstResult, int maxResults, String concept, String conceptSchemeUrn) {
        dispatcher.execute(new GetConceptsCanBeRoleAction(firstResult, maxResults, concept, conceptSchemeUrn), new WaitingAsyncCallback<GetConceptsCanBeRoleResult>() {

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
    public void retrieveConceptsThatCanBeExtended(int firstResult, int maxResults, String concept, String conceptSchemeUrn) {
        dispatcher.execute(new GetConceptsCanBeExtendedAction(firstResult, maxResults, concept, conceptSchemeUrn), new WaitingAsyncCallback<GetConceptsCanBeExtendedResult>() {

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
}
