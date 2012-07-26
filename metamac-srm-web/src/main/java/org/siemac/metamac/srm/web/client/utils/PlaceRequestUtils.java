package org.siemac.metamac.srm.web.client.utils;

import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.PlaceRequestParams;

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

    public static String getConceptSchemeParamFromUrl(PlaceManager placeManager) {
        for (PlaceRequest request : placeManager.getCurrentPlaceHierarchy()) {
            if (NameTokens.conceptSchemePage.equals(request.getNameToken())) {
                return request.getParameter(PlaceRequestParams.conceptSchemeParam, null);
            }
        }
        return null;
    }

    public static String getConceptParamFromUrl(PlaceManager placeManager) {
        for (PlaceRequest request : placeManager.getCurrentPlaceHierarchy()) {
            if (NameTokens.conceptPage.equals(request.getNameToken())) {
                return request.getParameter(PlaceRequestParams.conceptParam, null);
            }
        }
        return null;
    }

}
