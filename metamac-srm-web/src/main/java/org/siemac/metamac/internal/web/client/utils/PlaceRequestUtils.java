package org.siemac.metamac.internal.web.client.utils;

import org.siemac.metamac.internal.web.client.NameTokens;
import org.siemac.metamac.internal.web.client.PlaceRequestParams;

import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;

public class PlaceRequestUtils {

    public static String getDsdParamFromUrl(PlaceManager placeManager) {
        for (PlaceRequest request : placeManager.getCurrentPlaceHierarchy()) {
            if (NameTokens.dsdPage.equals(request.getNameToken())) {
                return request.getParameter(PlaceRequestParams.dsdParam, null);
            }
        }
        return null;
    }

}
