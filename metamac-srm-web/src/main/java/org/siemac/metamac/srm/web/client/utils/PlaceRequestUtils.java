package org.siemac.metamac.srm.web.client.utils;

import java.util.ArrayList;
import java.util.List;

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

    public static PlaceRequest buildRelativeDsdPlaceRequest(String dsdUrn) {
        PlaceRequest placeRequest = new PlaceRequest(NameTokens.dsdPage).with(PlaceRequestParams.dsdParamId, UrnUtils.removePrefix(dsdUrn));
        return placeRequest;
    }

    public static List<PlaceRequest> buildAbsoluteDsdListPlaceRequest() {
        List<PlaceRequest> placeRequestHierarchy = new ArrayList<PlaceRequest>();
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.structuralResourcesPage));
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.dsdListPage));
        return placeRequestHierarchy;
    }

    public static List<PlaceRequest> buildAbsoluteDsdPlaceRequest(String urn) {
        List<PlaceRequest> placeRequests = buildAbsoluteDsdListPlaceRequest();
        PlaceRequest dsdPlace = PlaceRequestUtils.buildRelativeDsdPlaceRequest(urn);
        placeRequests.add(dsdPlace);
        return placeRequests;
    }

    //
    // CONCEPTS
    //

    // Concept schemes

    public static String getConceptSchemeParamFromUrl(PlaceManager placeManager) {
        for (PlaceRequest request : placeManager.getCurrentPlaceHierarchy()) {
            if (NameTokens.conceptSchemePage.equals(request.getNameToken())) {
                return request.getParameter(PlaceRequestParams.conceptSchemeParamId, null);
            }
        }
        return null;
    }

    public static PlaceRequest buildRelativeConceptSchemePlaceRequest(String conceptSchemeUrn) {
        PlaceRequest placeRequest = new PlaceRequest(NameTokens.conceptSchemePage).with(PlaceRequestParams.conceptSchemeParamId, UrnUtils.removePrefix(conceptSchemeUrn));
        return placeRequest;
    }

    public static List<PlaceRequest> buildAbsoluteConceptSchemeListPlaceRequest() {
        List<PlaceRequest> placeRequestHierarchy = new ArrayList<PlaceRequest>();
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.structuralResourcesPage));
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.conceptSchemeListPage));
        return placeRequestHierarchy;
    }

    public static List<PlaceRequest> buildAbsoluteConceptSchemePlaceRequest(String urn) {
        List<PlaceRequest> placeRequests = buildAbsoluteConceptSchemeListPlaceRequest();
        PlaceRequest conceptSchemePlace = PlaceRequestUtils.buildRelativeConceptSchemePlaceRequest(urn);
        placeRequests.add(conceptSchemePlace);
        return placeRequests;
    }

    // Concepts

    public static String getConceptParamFromUrl(PlaceManager placeManager) {
        for (PlaceRequest request : placeManager.getCurrentPlaceHierarchy()) {
            if (NameTokens.conceptPage.equals(request.getNameToken())) {
                return request.getParameter(PlaceRequestParams.conceptParamId, null);
            }
        }
        return null;
    }

    public static PlaceRequest buildRelativeConceptPlaceRequest(String conceptUrn) {
        String[] splitUrn = UrnUtils.splitUrnByDots(UrnUtils.removePrefix(conceptUrn));
        PlaceRequest placeRequest = new PlaceRequest(NameTokens.conceptPage).with(PlaceRequestParams.conceptParamId, splitUrn[splitUrn.length - 1]);
        return placeRequest;
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

    public static PlaceRequest buildRelativeOrganisationSchemePlaceRequest(String organisationSchemeUrn, OrganisationSchemeTypeEnum organisationSchemeType) {
        PlaceRequest placeRequest = new PlaceRequest(NameTokens.organisationSchemePage).with(PlaceRequestParams.organisationSchemeParamType, organisationSchemeType.name()).with(
                PlaceRequestParams.organisationSchemeParamId, UrnUtils.removePrefix(organisationSchemeUrn));
        return placeRequest;
    }

    public static List<PlaceRequest> buildAbsoluteOrganisationSchemeListPlaceRequest() {
        List<PlaceRequest> placeRequestHierarchy = new ArrayList<PlaceRequest>();
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.structuralResourcesPage));
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.organisationSchemeListPage));
        return placeRequestHierarchy;
    }

    public static List<PlaceRequest> buildAbsoluteOrganisationSchemePlaceRequest(String urn, OrganisationSchemeTypeEnum type) {
        List<PlaceRequest> placeRequests = buildAbsoluteOrganisationSchemeListPlaceRequest();
        PlaceRequest organisationSchemePlace = PlaceRequestUtils.buildRelativeOrganisationSchemePlaceRequest(urn, type);
        placeRequests.add(organisationSchemePlace);
        return placeRequests;
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

    public static PlaceRequest buildRelativeOrganisationPlaceRequest(String organisationUrn) {
        String[] splitUrn = UrnUtils.splitUrnByDots(UrnUtils.removePrefix(organisationUrn));
        PlaceRequest placeRequest = new PlaceRequest(NameTokens.organisationPage).with(PlaceRequestParams.organisationParamId, splitUrn[splitUrn.length - 1]);
        return placeRequest;
    }

    //
    // CATEGORIES
    //

    // Category schemes

    public static String getCategorySchemeParamFromUrl(PlaceManager placeManager) {
        for (PlaceRequest request : placeManager.getCurrentPlaceHierarchy()) {
            if (NameTokens.categorySchemePage.equals(request.getNameToken())) {
                return request.getParameter(PlaceRequestParams.categorySchemeParamId, null);
            }
        }
        return null;
    }

    public static PlaceRequest buildRelativeCategorySchemePlaceRequest(String categorySchemeUrn) {
        PlaceRequest placeRequest = new PlaceRequest(NameTokens.categorySchemePage).with(PlaceRequestParams.categorySchemeParamId, UrnUtils.removePrefix(categorySchemeUrn));
        return placeRequest;
    }

    public static List<PlaceRequest> buildAbsoluteCategorySchemeListPlaceRequest() {
        List<PlaceRequest> placeRequestHierarchy = new ArrayList<PlaceRequest>();
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.structuralResourcesPage));
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.categorySchemeListPage));
        return placeRequestHierarchy;
    }

    public static List<PlaceRequest> buildAbsoluteCategorySchemePlaceRequest(String urn) {
        List<PlaceRequest> placeRequests = buildAbsoluteCategorySchemeListPlaceRequest();
        PlaceRequest categorySchemePlace = PlaceRequestUtils.buildRelativeCategorySchemePlaceRequest(urn);
        placeRequests.add(categorySchemePlace);
        return placeRequests;
    }

    // Categories

    /**
     * Splits a category identifier (not code!) by its version.
     * For example: the identifier SDMX_AGENCY:category_scheme_0001(1.0).category1.category2 is split in two values:
     * · SDMX_AGENCY:category_scheme_0001
     * · category1.category2
     */
    public static final String CATEGORY_IDENTIFIER_SPLITTER_BY_VERSION_REG_EXP = "\\(\\d+\\.\\d+\\)\\.";

    public static String getCategoryParamFromUrl(PlaceManager placeManager) {
        for (PlaceRequest request : placeManager.getCurrentPlaceHierarchy()) {
            if (NameTokens.categoryPage.equals(request.getNameToken())) {
                return request.getParameter(PlaceRequestParams.categoryParamId, null);
            }
        }
        return null;
    }

    public static PlaceRequest buildRelativeCategoryPlaceRequest(String categoryUrn) {
        String identifier = UrnUtils.removePrefix(categoryUrn);
        String[] splitIdentifier = identifier.split(CATEGORY_IDENTIFIER_SPLITTER_BY_VERSION_REG_EXP);
        PlaceRequest placeRequest = new PlaceRequest(NameTokens.categoryPage).with(PlaceRequestParams.categoryParamId, splitIdentifier[splitIdentifier.length - 1]);
        return placeRequest;
    }

    //
    // CODES
    //

    // Codelists

    public static String getCodelistParamFromUrl(PlaceManager placeManager) {
        for (PlaceRequest request : placeManager.getCurrentPlaceHierarchy()) {
            if (NameTokens.codelistPage.equals(request.getNameToken())) {
                return request.getParameter(PlaceRequestParams.codelistParamId, null);
            }
        }
        return null;
    }

    public static PlaceRequest buildRelativeCodelistPlaceRequest(String codelistUrn) {
        PlaceRequest placeRequest = new PlaceRequest(NameTokens.codelistPage).with(PlaceRequestParams.codelistParamId, UrnUtils.removePrefix(codelistUrn));
        return placeRequest;
    }

    public static List<PlaceRequest> buildAbsoluteCodelistListPlaceRequest() {
        List<PlaceRequest> placeRequestHierarchy = new ArrayList<PlaceRequest>();
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.structuralResourcesPage));
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.codelistListPage));
        return placeRequestHierarchy;
    }

    public static List<PlaceRequest> buildAbsoluteCodelistPlaceRequest(String urn) {
        List<PlaceRequest> placeRequests = buildAbsoluteCodelistListPlaceRequest();
        PlaceRequest codelistPlace = PlaceRequestUtils.buildRelativeCodelistPlaceRequest(urn);
        placeRequests.add(codelistPlace);
        return placeRequests;
    }

    // Codes

    public static String getCodeParamFromUrl(PlaceManager placeManager) {
        for (PlaceRequest request : placeManager.getCurrentPlaceHierarchy()) {
            if (NameTokens.codePage.equals(request.getNameToken())) {
                return request.getParameter(PlaceRequestParams.codeParamId, null);
            }
        }
        return null;
    }

    public static PlaceRequest buildRelativeCodePlaceRequest(String codeUrn) {
        String[] splitUrn = UrnUtils.splitUrnByDots(UrnUtils.removePrefix(codeUrn));
        PlaceRequest placeRequest = new PlaceRequest(NameTokens.codePage).with(PlaceRequestParams.codeParamId, splitUrn[splitUrn.length - 1]);
        return placeRequest;
    }
}
