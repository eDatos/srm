package org.siemac.metamac.srm.web.client.utils;

import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.PlaceRequestParams;
import org.siemac.metamac.web.common.client.utils.UrnUtils;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;

public class PlaceRequestUtils {

    //
    // DATA STRUCTURE DEFINITION
    //

    public static String getDsdParamFromUrl(PlaceManager placeManager) {
        for (PlaceRequest request : placeManager.getCurrentPlaceHierarchy()) {
            if (NameTokens.dsdPage.equals(request.getNameToken())) {
                return request.getParameter(PlaceRequestParams.dsdParamId, null);
            }
        }
        return null;
    }

    // CONCEPTS

    public static String getConceptSchemeParamFromUrl(PlaceManager placeManager) {
        for (PlaceRequest request : placeManager.getCurrentPlaceHierarchy()) {
            if (NameTokens.conceptSchemePage.equals(request.getNameToken())) {
                return request.getParameter(PlaceRequestParams.conceptSchemeParamId, null);
            }
        }
        return null;
    }

    public static String getConceptParamFromUrl(PlaceManager placeManager) {
        for (PlaceRequest request : placeManager.getCurrentPlaceHierarchy()) {
            if (NameTokens.conceptPage.equals(request.getNameToken())) {
                return request.getParameter(PlaceRequestParams.conceptParamId, null);
            }
        }
        return null;
    }

    //
    // ORGANISATIONS
    //

    // Organisation schemes

    public static String getOrganisationSchemeIdParamFromUrl(PlaceManager placeManager) {
        for (PlaceRequest request : placeManager.getCurrentPlaceHierarchy()) {
            if (NameTokens.organisationSchemePage.equals(request.getNameToken())) {
                return request.getParameter(PlaceRequestParams.organisationSchemeParamId, null);
            }
        }
        return null;
    }

    public static String getOrganisationSchemeTypeParamFromUrl(PlaceManager placeManager) {
        for (PlaceRequest request : placeManager.getCurrentPlaceHierarchy()) {
            if (NameTokens.organisationSchemePage.equals(request.getNameToken())) {
                return request.getParameter(PlaceRequestParams.organisationSchemeParamType, null);
            }
        }
        return null;
    }

    public static PlaceRequest buildOrganisationSchemePlaceRequest(String organisationSchemeUrn, OrganisationSchemeTypeEnum organisationSchemeType) {
        PlaceRequest placeRequest = new PlaceRequest(NameTokens.organisationSchemePage).with(PlaceRequestParams.organisationSchemeParamType, organisationSchemeType.name()).with(
                PlaceRequestParams.organisationSchemeParamId, UrnUtils.removePrefix(organisationSchemeUrn));
        return placeRequest;
    }

    // Organisations

    public static String getOrganisationParamFromUrl(PlaceManager placeManager) {
        for (PlaceRequest request : placeManager.getCurrentPlaceHierarchy()) {
            if (NameTokens.organisationPage.equals(request.getNameToken())) {
                return request.getParameter(PlaceRequestParams.organisationParamId, null);
            }
        }
        return null;
    }

    public static PlaceRequest buildOrganisationPlaceRequest(String organisationUrn) {
        String[] splitUrn = UrnUtils.splitUrnByDots(UrnUtils.removePrefix(organisationUrn));
        PlaceRequest placeRequest = new PlaceRequest(NameTokens.organisationPage).with(PlaceRequestParams.organisationParamId, splitUrn[splitUrn.length - 1]);
        return placeRequest;
    }

}
