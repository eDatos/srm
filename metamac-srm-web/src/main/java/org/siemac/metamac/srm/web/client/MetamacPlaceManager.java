package org.siemac.metamac.internal.web.client;

import org.siemac.metamac.internal.web.client.gin.DefaultPlace;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.proxy.PlaceManagerImpl;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.TokenFormatter;

// see ClientModule
// bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.signInPage);

public class MetamacPlaceManager extends PlaceManagerImpl {

    private final PlaceRequest defaultPlaceRequest;

    @Inject
    public MetamacPlaceManager(EventBus eventBus, TokenFormatter tokenFormatter, @DefaultPlace String defaultNameToken) {
        super(eventBus, tokenFormatter);
        this.defaultPlaceRequest = new PlaceRequest(defaultNameToken);
    }

    @Override
    public void revealDefaultPlace() {
        revealPlace(defaultPlaceRequest);
    }

    /*
     * TODO Implement this method when authentication needed
     * @Override
     * public void revealUnauthorizedPlace(String unauthorizedHistoryToken) {
     * PlaceRequest placeRequest = new PlaceRequest(NameTokens.signInPage);
     * placeRequest = placeRequest.with("redirect", unauthorizedHistoryToken);
     * revealPlace(placeRequest);
     * }
     */

    @Override
    public void revealErrorPlace(String invalidHistoryToken) {
        PlaceRequest placeRequest = new PlaceRequest(NameTokens.errorPage);
        revealPlace(placeRequest);
    }

}
