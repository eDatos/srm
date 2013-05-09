package org.siemac.metamac.srm.web.client.view.handlers;

import java.util.List;

import com.gwtplatform.mvp.client.UiHandlers;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;

public interface BaseUiHandlers extends UiHandlers {

    void goTo(List<PlaceRequest> location);
}
