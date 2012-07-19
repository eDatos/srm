package org.siemac.metamac.srm.web.concept.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import org.siemac.metamac.domain.concept.dto.ConceptDto;
import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.client.widgets.presenter.ToolStripPresenterWidget;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptSchemeUiHandlers;
import org.siemac.metamac.srm.web.shared.GetConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.GetConceptSchemeResult;
import org.siemac.metamac.srm.web.shared.PublishConceptSchemeExternallyAction;
import org.siemac.metamac.srm.web.shared.PublishConceptSchemeExternallyResult;
import org.siemac.metamac.srm.web.shared.PublishConceptSchemeInternallyAction;
import org.siemac.metamac.srm.web.shared.PublishConceptSchemeInternallyResult;
import org.siemac.metamac.srm.web.shared.RejectConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.RejectConceptSchemeResult;
import org.siemac.metamac.srm.web.shared.SaveConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.SaveConceptSchemeResult;
import org.siemac.metamac.srm.web.shared.SendConceptSchemeToPendingPublicationAction;
import org.siemac.metamac.srm.web.shared.SendConceptSchemeToPendingPublicationResult;
import org.siemac.metamac.srm.web.shared.VersionConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.VersionConceptSchemeResult;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

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
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

public class ConceptSchemePresenter extends Presenter<ConceptSchemePresenter.ConceptSchemeView, ConceptSchemePresenter.ConceptSchemeProxy> implements ConceptSchemeUiHandlers {

    private final DispatchAsync      dispatcher;
    private final PlaceManager       placeManager;
    private ToolStripPresenterWidget toolStripPresenterWidget;

    public static final int          MAX_RESULTS = 10;
    private String                   conceptSchemeIdLogic;

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbConceptScheme();
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.conceptSchemePage)
    public interface ConceptSchemeProxy extends Proxy<ConceptSchemePresenter>, Place {
    }

    public interface ConceptSchemeView extends View, HasUiHandlers<ConceptSchemeUiHandlers> {

        void setConceptScheme(ConceptSchemeDto conceptScheme);
    }

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaContentConceptScheme        = new Type<RevealContentHandler<?>>();

    public static final Object                        TYPE_SetContextAreaContentConceptSchemeToolBar = new Object();

    @Inject
    public ConceptSchemePresenter(EventBus eventBus, ConceptSchemeView view, ConceptSchemeProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager,
            ToolStripPresenterWidget toolStripPresenterWidget) {
        super(eventBus, view, proxy);
        this.placeManager = placeManager;
        this.dispatcher = dispatcher;
        this.toolStripPresenterWidget = toolStripPresenterWidget;
        getView().setUiHandlers(this);
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        setInSlot(TYPE_SetContextAreaContentConceptSchemeToolBar, toolStripPresenterWidget);
        MainPagePresenter.getMasterHead().setTitleLabel(MetamacSrmWeb.getConstants().conceptScheme());
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        String id = PlaceRequestUtils.getConceptSchemeParamFromUrl(placeManager);
        if (id != null) {
            if (conceptSchemeIdLogic == null || (conceptSchemeIdLogic != null && !conceptSchemeIdLogic.equals(id))) {
                conceptSchemeIdLogic = id;
                retrieveConceptScheme(conceptSchemeIdLogic);
            }
        }
    }

    private void setConceptScheme(ConceptSchemeDto conceptScheme) {
        getView().setConceptScheme(conceptScheme);
    }

    @Override
    public void saveConceptScheme(ConceptSchemeDto conceptScheme) {
        dispatcher.execute(new SaveConceptSchemeAction(conceptScheme), new WaitingAsyncCallback<SaveConceptSchemeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorSave()), MessageTypeEnum.ERROR);
            }

            @Override
            public void onWaitSuccess(SaveConceptSchemeResult result) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getMessageList(getMessages().conceptSchemeSaved()), MessageTypeEnum.SUCCESS);
                setConceptScheme(result.getSavedConceptSchemeDto());
            }
        });
    }

    @Override
    public void retrieveConceptScheme(String conceptSchemeIdLogic) {
        dispatcher.execute(new GetConceptSchemeAction(conceptSchemeIdLogic), new WaitingAsyncCallback<GetConceptSchemeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorSave()), MessageTypeEnum.ERROR);
            }

            @Override
            public void onWaitSuccess(GetConceptSchemeResult result) {
                setConceptScheme(result.getConceptSchemeDto());
            }
        });
    }

    @Override
    public void sendToPendingPublication(Long id) {
        dispatcher.execute(new SendConceptSchemeToPendingPublicationAction(id), new WaitingAsyncCallback<SendConceptSchemeToPendingPublicationResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorSendingToPendingPublication()), MessageTypeEnum.ERROR);
            }

            @Override
            public void onWaitSuccess(SendConceptSchemeToPendingPublicationResult result) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getMessageList(getMessages().conceptSchemeSentToPendingPublication()), MessageTypeEnum.SUCCESS);
                setConceptScheme(result.getConceptSchemeDto());
            }
        });
    }

    @Override
    public void publishExternally(Long id) {
        dispatcher.execute(new PublishConceptSchemeExternallyAction(id), new WaitingAsyncCallback<PublishConceptSchemeExternallyResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorPublishingExternally()), MessageTypeEnum.ERROR);
            }

            @Override
            public void onWaitSuccess(PublishConceptSchemeExternallyResult result) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getMessageList(getMessages().conceptSchemePublishedExternally()), MessageTypeEnum.SUCCESS);
                setConceptScheme(result.getConceptSchemeDto());
            }
        });
    }

    @Override
    public void publishInternally(Long id) {
        dispatcher.execute(new PublishConceptSchemeInternallyAction(id), new WaitingAsyncCallback<PublishConceptSchemeInternallyResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorPublishingInternally()), MessageTypeEnum.ERROR);
            }

            @Override
            public void onWaitSuccess(PublishConceptSchemeInternallyResult result) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getMessageList(getMessages().conceptSchemePublishedInternally()), MessageTypeEnum.SUCCESS);
                setConceptScheme(result.getConceptSchemeDto());
            }
        });
    }

    @Override
    public void rejectValidation(Long id) {
        dispatcher.execute(new RejectConceptSchemeAction(id), new WaitingAsyncCallback<RejectConceptSchemeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorRejecting()), MessageTypeEnum.ERROR);
            }

            @Override
            public void onWaitSuccess(RejectConceptSchemeResult result) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getMessageList(getMessages().conceptSchemeRejected()), MessageTypeEnum.SUCCESS);
                setConceptScheme(result.getConceptSchemeDto());
            }
        });
    }

    @Override
    public void versioning(Long id) {
        dispatcher.execute(new VersionConceptSchemeAction(id), new WaitingAsyncCallback<VersionConceptSchemeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptSchemeErrorVersioning()), MessageTypeEnum.ERROR);
            }

            @Override
            public void onWaitSuccess(VersionConceptSchemeResult result) {
                ShowMessageEvent.fire(ConceptSchemePresenter.this, ErrorUtils.getMessageList(getMessages().conceptSchemeVersioned()), MessageTypeEnum.SUCCESS);
                setConceptScheme(result.getConceptSchemeDto());
            }
        });
    }

    @Override
    public void createConcept(ConceptDto conceptDto) {
        // TODO
    }
}
