package org.siemac.metamac.srm.web.concept.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.List;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptTypeDto;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.PlaceRequestParams;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.client.widgets.presenter.ToolStripPresenterWidget;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptUiHandlers;
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
import org.siemac.metamac.srm.web.shared.concept.SaveConceptAction;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptResult;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.UrnUtils;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

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

        void setConcept(ConceptMetamacDto conceptDto);
        void setConceptList(ConceptSchemeMetamacDto conceptSchemeMetamacDto, List<ItemHierarchyDto> itemHierarchyDtos);

        void setConceptTypes(List<ConceptTypeDto> conceptTypeDtos);
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
                getView().setConcept(result.getConceptDto());
            }
        });
    }

    @Override
    public void retrieveConceptListByScheme(String conceptSchemeUrn) {
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

    @Override
    public void saveConcept(ConceptMetamacDto conceptDto) {
        dispatcher.execute(new SaveConceptAction(conceptDto), new WaitingAsyncCallback<SaveConceptResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptErrorSave()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(SaveConceptResult result) {
                ShowMessageEvent.fire(ConceptPresenter.this, ErrorUtils.getMessageList(getMessages().conceptSaved()), MessageTypeEnum.SUCCESS);
                getView().setConcept(result.getConceptDto());
            }
        });
    }

    @Override
    public void deleteConcept(String urn) {
        dispatcher.execute(new DeleteConceptAction(urn), new WaitingAsyncCallback<DeleteConceptResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptErrorDelete()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(DeleteConceptResult result) {
                retrieveConceptListByScheme(conceptSchemeUrn);
            }
        });
    }

    @Override
    public void goToConcept(String urn) {
        String[] splitUrn = UrnUtils.splitUrnByDots(UrnUtils.removePrefix(urn));
        placeManager.revealRelativePlace(new PlaceRequest(NameTokens.conceptPage).with(PlaceRequestParams.conceptParam, splitUrn[splitUrn.length - 1]), -1);
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

}
