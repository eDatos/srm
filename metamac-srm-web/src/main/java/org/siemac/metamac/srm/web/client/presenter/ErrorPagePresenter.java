package org.siemac.metamac.srm.web.client.presenter;

import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.view.handlers.ErrorPageUiHandlers;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class ErrorPagePresenter extends Presenter<ErrorPagePresenter.ErrorPageView, ErrorPagePresenter.ErrorPageProxy> implements ErrorPageUiHandlers {

    @ProxyCodeSplit
    @NameToken(NameTokens.errorPage)
    @NoGatekeeper
    public interface ErrorPageProxy extends Proxy<ErrorPagePresenter>, Place {

    }

    public interface ErrorPageView extends View, HasUiHandlers<ErrorPageUiHandlers> {

    }

    @Inject
    public ErrorPagePresenter(EventBus eventBus, ErrorPageView view, ErrorPageProxy proxy) {
        super(eventBus, view, proxy);
        getView().setUiHandlers(this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
    }

    @Override
    protected void onReveal() {
        super.onReveal();
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }

}
