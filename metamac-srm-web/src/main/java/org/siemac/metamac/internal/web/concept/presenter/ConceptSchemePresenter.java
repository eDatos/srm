package org.siemac.metamac.internal.web.concept.presenter;

import static org.siemac.metamac.internal.web.client.MetamacInternalWeb.getMessages;

import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;
import org.siemac.metamac.internal.web.client.NameTokens;
import org.siemac.metamac.internal.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.internal.web.client.utils.ErrorUtils;
import org.siemac.metamac.internal.web.concept.view.handlers.ConceptSchemeUiHandlers;
import org.siemac.metamac.internal.web.shared.SaveConceptSchemeAction;
import org.siemac.metamac.internal.web.shared.SaveConceptSchemeResult;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class ConceptSchemePresenter extends Presenter<ConceptSchemePresenter.ConceptSchemeView, ConceptSchemePresenter.ConceptSchemeProxy> implements ConceptSchemeUiHandlers {

    private final DispatchAsync dispatcher;
    private final PlaceManager  placeManager;

    @ProxyCodeSplit
    @NameToken(NameTokens.conceptSchemePage)
    public interface ConceptSchemeProxy extends Proxy<ConceptSchemePresenter>, Place {
    }

    public interface ConceptSchemeView extends View, HasUiHandlers<ConceptSchemeUiHandlers> {

        void setConceptScheme(ConceptSchemeDto conceptScheme);
    }

    @Inject
    public ConceptSchemePresenter(EventBus eventBus, ConceptSchemeView view, ConceptSchemeProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager) {
        super(eventBus, view, proxy);
        this.dispatcher = dispatcher;
        this.placeManager = placeManager;
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }

    /* UiHandlers */
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
                getView().setConceptScheme(result.getSavedConceptSchemeDto());
            }
        });
    }

    /* UiHandlers */
    @Override
    public void sendToPendingPublication(String conceptSchemeUuid) {

    }

    @Override
    public void publishExternally(String conceptSchemeUuid) {
        // TODO Auto-generated method stub

    }

    @Override
    public void publishInternally(String conceptSchemeUuid) {
        // TODO Auto-generated method stub

    }

    @Override
    public void rejectValidation(String conceptSchemeUuid) {
        // TODO Auto-generated method stub

    }

    @Override
    public void versioning(String conceptSchemeUuid) {
        // TODO Auto-generated method stub

    }

}
