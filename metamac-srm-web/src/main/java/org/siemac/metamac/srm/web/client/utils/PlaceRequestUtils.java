package org.siemac.metamac.srm.web.client.utils;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.srm.core.common.service.utils.shared.SrmUrnParserUtils;
import org.siemac.metamac.srm.navigation.shared.NameTokens;
import org.siemac.metamac.srm.navigation.shared.PlaceRequestParams;

import com.arte.statistic.sdmx.srm.core.common.service.utils.shared.SdmxSrmUrnParserUtils;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;

public class PlaceRequestUtils {

    // ---------------------------------------------------------------------------
    // DATA STRUCTURE DEFINITION
    // ---------------------------------------------------------------------------

    public static String getDsdParamFromUrl(PlaceManager placeManager) {
        return getParamFromUrl(placeManager, NameTokens.dsdPage, PlaceRequestParams.dsdParamId);
    }

    public static PlaceRequest buildRelativeDsdPlaceRequest(String dsdUrn) {
        PlaceRequest placeRequest = new PlaceRequest(NameTokens.dsdPage).with(PlaceRequestParams.dsdParamId, UrnUtils.removePrefix(dsdUrn));
        return placeRequest;
    }

    public static List<PlaceRequest> buildAbsoluteDsdsPlaceRequest() {
        List<PlaceRequest> placeRequestHierarchy = new ArrayList<PlaceRequest>();
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.structuralResourcesPage));
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.dsdListPage));
        return placeRequestHierarchy;
    }

    public static List<PlaceRequest> buildAbsoluteDsdPlaceRequest(String urn) {
        List<PlaceRequest> placeRequests = buildAbsoluteDsdsPlaceRequest();
        PlaceRequest dsdPlace = buildRelativeDsdPlaceRequest(urn);
        placeRequests.add(dsdPlace);
        return placeRequests;
    }

    // ---------------------------------------------------------------------------
    // CONCEPTS
    // ---------------------------------------------------------------------------

    // Concept schemes

    public static String getConceptSchemeParamFromUrl(PlaceManager placeManager) {
        return getParamFromUrl(placeManager, NameTokens.conceptSchemePage, PlaceRequestParams.conceptSchemeParamId);
    }

    public static PlaceRequest buildRelativeConceptSchemePlaceRequest(String conceptSchemeUrn) {
        PlaceRequest placeRequest = new PlaceRequest(NameTokens.conceptSchemePage).with(PlaceRequestParams.conceptSchemeParamId, UrnUtils.removePrefix(conceptSchemeUrn));
        return placeRequest;
    }

    public static List<PlaceRequest> buildAbsoluteConceptSchemesPlaceRequest() {
        List<PlaceRequest> placeRequestHierarchy = new ArrayList<PlaceRequest>();
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.structuralResourcesPage));
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.conceptSchemeListPage));
        return placeRequestHierarchy;
    }

    public static List<PlaceRequest> buildAbsoluteConceptSchemePlaceRequest(String urn) {
        List<PlaceRequest> placeRequests = buildAbsoluteConceptSchemesPlaceRequest();
        PlaceRequest conceptSchemePlace = buildRelativeConceptSchemePlaceRequest(urn);
        placeRequests.add(conceptSchemePlace);
        return placeRequests;
    }

    // Concepts

    public static String getConceptParamFromUrl(PlaceManager placeManager) {
        return getParamFromUrl(placeManager, NameTokens.conceptPage, PlaceRequestParams.conceptParamId);
    }

    public static List<PlaceRequest> buildAbsoluteConceptsPlaceRequest() {
        List<PlaceRequest> placeRequestHierarchy = new ArrayList<PlaceRequest>();
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.structuralResourcesPage));
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.conceptsPage));
        return placeRequestHierarchy;
    }

    public static List<PlaceRequest> buildAbsoluteConceptPlaceRequest(String conceptSchemeUrn, String conceptUrn) {
        List<PlaceRequest> placeRequests = buildAbsoluteConceptSchemesPlaceRequest();
        PlaceRequest conceptSchemePlace = buildRelativeConceptSchemePlaceRequest(conceptSchemeUrn);
        PlaceRequest conceptPlace = buildRelativeConceptPlaceRequest(conceptUrn);
        placeRequests.add(conceptSchemePlace);
        placeRequests.add(conceptPlace);
        return placeRequests;
    }

    public static PlaceRequest buildRelativeConceptPlaceRequest(String conceptUrn) {
        String[] splitUrn = UrnUtils.splitUrnByDots(UrnUtils.removePrefix(conceptUrn));
        PlaceRequest placeRequest = new PlaceRequest(NameTokens.conceptPage).with(PlaceRequestParams.conceptParamId, splitUrn[splitUrn.length - 1]);
        return placeRequest;
    }

    // ---------------------------------------------------------------------------
    // ORGANISATIONS
    // ---------------------------------------------------------------------------

    // Organisation schemes

    public static String getOrganisationSchemeIdParamFromUrl(PlaceManager placeManager) {
        return getParamFromUrl(placeManager, NameTokens.organisationSchemePage, PlaceRequestParams.organisationSchemeParamId);
    }

    public static String getOrganisationSchemeTypeParamFromUrl(PlaceManager placeManager) {
        return getParamFromUrl(placeManager, NameTokens.organisationSchemePage, PlaceRequestParams.organisationSchemeParamType);
    }

    public static PlaceRequest buildRelativeOrganisationSchemePlaceRequest(String organisationSchemeUrn, OrganisationSchemeTypeEnum organisationSchemeType) {
        PlaceRequest placeRequest = new PlaceRequest(NameTokens.organisationSchemePage).with(PlaceRequestParams.organisationSchemeParamType, organisationSchemeType.name()).with(
                PlaceRequestParams.organisationSchemeParamId, UrnUtils.removePrefix(organisationSchemeUrn));
        return placeRequest;
    }

    public static List<PlaceRequest> buildAbsoluteOrganisationSchemesPlaceRequest() {
        List<PlaceRequest> placeRequestHierarchy = new ArrayList<PlaceRequest>();
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.structuralResourcesPage));
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.organisationSchemeListPage));
        return placeRequestHierarchy;
    }

    public static List<PlaceRequest> buildAbsoluteOrganisationSchemePlaceRequest(String urn, OrganisationSchemeTypeEnum type) {
        List<PlaceRequest> placeRequests = buildAbsoluteOrganisationSchemesPlaceRequest();
        PlaceRequest organisationSchemePlace = buildRelativeOrganisationSchemePlaceRequest(urn, type);
        placeRequests.add(organisationSchemePlace);
        return placeRequests;
    }

    // Organisations

    public static String getOrganisationParamFromUrl(PlaceManager placeManager) {
        return getParamFromUrl(placeManager, NameTokens.organisationPage, PlaceRequestParams.organisationParamId);
    }

    public static List<PlaceRequest> buildAbsoluteOrganisationsPlaceRequest() {
        List<PlaceRequest> placeRequestHierarchy = new ArrayList<PlaceRequest>();
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.structuralResourcesPage));
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.organisationsPage));
        return placeRequestHierarchy;
    }

    public static PlaceRequest buildRelativeOrganisationPlaceRequest(String organisationUrn) {
        String[] splitUrn = UrnUtils.splitUrnByDots(UrnUtils.removePrefix(organisationUrn));
        PlaceRequest placeRequest = new PlaceRequest(NameTokens.organisationPage).with(PlaceRequestParams.organisationParamId, splitUrn[splitUrn.length - 1]);
        return placeRequest;
    }

    public static List<PlaceRequest> buildAbsoluteOrganisationPlaceRequest(String organisationSchemeUrn, String organisationUrn, OrganisationSchemeTypeEnum organisationSchemeTypeEnum) {
        List<PlaceRequest> placeRequests = buildAbsoluteOrganisationSchemesPlaceRequest();
        PlaceRequest organisationSchemePlace = buildRelativeOrganisationSchemePlaceRequest(organisationSchemeUrn, organisationSchemeTypeEnum);
        PlaceRequest organisationPlace = buildRelativeOrganisationPlaceRequest(organisationUrn);
        placeRequests.add(organisationSchemePlace);
        placeRequests.add(organisationPlace);
        return placeRequests;
    }

    // ---------------------------------------------------------------------------
    // CATEGORIES
    // ---------------------------------------------------------------------------

    // Category schemes

    public static String getCategorySchemeParamFromUrl(PlaceManager placeManager) {
        return getParamFromUrl(placeManager, NameTokens.categorySchemePage, PlaceRequestParams.categorySchemeParamId);
    }

    public static PlaceRequest buildRelativeCategorySchemePlaceRequest(String categorySchemeUrn) {
        PlaceRequest placeRequest = new PlaceRequest(NameTokens.categorySchemePage).with(PlaceRequestParams.categorySchemeParamId, UrnUtils.removePrefix(categorySchemeUrn));
        return placeRequest;
    }

    public static List<PlaceRequest> buildAbsoluteCategorySchemesPlaceRequest() {
        List<PlaceRequest> placeRequestHierarchy = new ArrayList<PlaceRequest>();
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.structuralResourcesPage));
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.categorySchemeListPage));
        return placeRequestHierarchy;
    }

    public static List<PlaceRequest> buildAbsoluteCategorySchemePlaceRequest(String urn) {
        List<PlaceRequest> placeRequests = buildAbsoluteCategorySchemesPlaceRequest();
        PlaceRequest categorySchemePlace = buildRelativeCategorySchemePlaceRequest(urn);
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
        return getParamFromUrl(placeManager, NameTokens.categoryPage, PlaceRequestParams.categoryParamId);
    }

    public static List<PlaceRequest> buildAbsoluteCategoriesPlaceRequest() {
        List<PlaceRequest> placeRequestHierarchy = new ArrayList<PlaceRequest>();
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.structuralResourcesPage));
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.categoriesPage));
        return placeRequestHierarchy;
    }

    public static PlaceRequest buildRelativeCategoryPlaceRequest(String categoryUrn) {
        String identifier = UrnUtils.removePrefix(categoryUrn);
        String[] splitIdentifier = identifier.split(CATEGORY_IDENTIFIER_SPLITTER_BY_VERSION_REG_EXP);
        PlaceRequest placeRequest = new PlaceRequest(NameTokens.categoryPage).with(PlaceRequestParams.categoryParamId, splitIdentifier[splitIdentifier.length - 1]);
        return placeRequest;
    }

    public static List<PlaceRequest> buildAbsoluteCategoryPlaceRequest(String categorySchemeUrn, String categoryUrn) {
        List<PlaceRequest> placeRequests = buildAbsoluteCategorySchemesPlaceRequest();
        PlaceRequest categorySchemePlace = buildRelativeCategorySchemePlaceRequest(categorySchemeUrn);
        PlaceRequest categoryPlace = buildRelativeCategoryPlaceRequest(categoryUrn);
        placeRequests.add(categorySchemePlace);
        placeRequests.add(categoryPlace);
        return placeRequests;
    }

    // ---------------------------------------------------------------------------
    // CODES
    // ---------------------------------------------------------------------------

    // Codelists

    public static String getCodelistParamFromUrl(PlaceManager placeManager) {
        return getParamFromUrl(placeManager, NameTokens.codelistPage, PlaceRequestParams.codelistParamId);
    }

    public static PlaceRequest buildRelativeCodelistPlaceRequest(String codelistUrn) {
        PlaceRequest placeRequest = new PlaceRequest(NameTokens.codelistPage).with(PlaceRequestParams.codelistParamId, UrnUtils.removePrefix(codelistUrn));
        return placeRequest;
    }

    public static List<PlaceRequest> buildAbsoluteCodelistsPlaceRequest() {
        List<PlaceRequest> placeRequestHierarchy = new ArrayList<PlaceRequest>();
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.structuralResourcesPage));
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.codelistListPage));
        return placeRequestHierarchy;
    }

    public static List<PlaceRequest> buildAbsoluteCodelistPlaceRequest(String urn) {
        List<PlaceRequest> placeRequests = buildAbsoluteCodelistsPlaceRequest();
        PlaceRequest codelistPlace = buildRelativeCodelistPlaceRequest(urn);
        placeRequests.add(codelistPlace);
        return placeRequests;
    }

    // Codes

    public static List<PlaceRequest> buildAbsoluteCodesPlaceRequest() {
        List<PlaceRequest> placeRequestHierarchy = new ArrayList<PlaceRequest>();
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.structuralResourcesPage));
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.codesPage));
        return placeRequestHierarchy;
    }

    public static String getCodeParamFromUrl(PlaceManager placeManager) {
        return getParamFromUrl(placeManager, NameTokens.codePage, PlaceRequestParams.codeParamId);
    }

    public static PlaceRequest buildRelativeCodePlaceRequest(String codeUrn) {
        String[] splitUrn = UrnUtils.splitUrnByDots(UrnUtils.removePrefix(codeUrn));
        PlaceRequest placeRequest = new PlaceRequest(NameTokens.codePage).with(PlaceRequestParams.codeParamId, splitUrn[splitUrn.length - 1]);
        return placeRequest;
    }

    public static List<PlaceRequest> buildAbsoluteCodePlaceRequest(String codelistUrn, String codeUrn) {
        List<PlaceRequest> placeRequests = buildAbsoluteCodelistsPlaceRequest();
        PlaceRequest codelistPlace = buildRelativeCodelistPlaceRequest(codelistUrn);
        PlaceRequest codePlace = buildRelativeCodePlaceRequest(codeUrn);
        placeRequests.add(codelistPlace);
        placeRequests.add(codePlace);
        return placeRequests;
    }

    // Codelist families

    public static String getCodelistFamilyParamFromUrl(PlaceManager placeManager) {
        return getParamFromUrl(placeManager, NameTokens.codelistFamilyPage, PlaceRequestParams.codelistFamilyParamId);
    }

    public static List<PlaceRequest> buildAbsoluteCodelistFamiliesPlaceRequest() {
        List<PlaceRequest> placeRequestHierarchy = new ArrayList<PlaceRequest>();
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.structuralResourcesPage));
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.codelistFamilyListPage));
        return placeRequestHierarchy;
    }

    public static PlaceRequest buildRelativeCodelistFamilyPlaceRequest(String familyCode) {
        PlaceRequest placeRequest = new PlaceRequest(NameTokens.codelistFamilyPage).with(PlaceRequestParams.codelistFamilyParamId, familyCode);
        return placeRequest;
    }

    public static List<PlaceRequest> buildAbsoluteCodelistFamilyPlaceRequest(String code) {
        List<PlaceRequest> placeRequests = buildAbsoluteCodelistFamiliesPlaceRequest();
        PlaceRequest codelistPlace = buildRelativeCodelistFamilyPlaceRequest(code);
        placeRequests.add(codelistPlace);
        return placeRequests;
    }

    // Variable families

    public static String getVariableFamilyParamFromUrl(PlaceManager placeManager) {
        return getParamFromUrl(placeManager, NameTokens.variableFamilyPage, PlaceRequestParams.variableFamilyParamId);
    }

    public static PlaceRequest buildRelativeVariableFamilyPlaceRequest(String familyCode) {
        PlaceRequest placeRequest = new PlaceRequest(NameTokens.variableFamilyPage).with(PlaceRequestParams.variableFamilyParamId, familyCode);
        return placeRequest;
    }

    public static List<PlaceRequest> buildAbsoluteVariableFamilyPlaceRequest(String variableFamilyCode) {
        List<PlaceRequest> placeRequests = buildAbsoluteVariableFamiliesPlaceRequest();
        PlaceRequest variableFamilyPlace = buildRelativeVariableFamilyPlaceRequest(variableFamilyCode);
        placeRequests.add(variableFamilyPlace);
        return placeRequests;
    }

    public static List<PlaceRequest> buildAbsoluteVariableFamiliesPlaceRequest() {
        List<PlaceRequest> placeRequestHierarchy = new ArrayList<PlaceRequest>();
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.structuralResourcesPage));
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.variableFamilyListPage));
        return placeRequestHierarchy;
    }

    // Variables

    public static String getVariableParamFromUrl(PlaceManager placeManager) {
        return getParamFromUrl(placeManager, NameTokens.variablePage, PlaceRequestParams.variableParamId);
    }

    public static PlaceRequest buildRelativeVariablePlaceRequest(String variableCode) {
        PlaceRequest placeRequest = new PlaceRequest(NameTokens.variablePage).with(PlaceRequestParams.variableParamId, variableCode);
        return placeRequest;
    }

    public static List<PlaceRequest> buildAbsoluteVariablesPlaceRequest() {
        List<PlaceRequest> placeRequestHierarchy = new ArrayList<PlaceRequest>();
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.structuralResourcesPage));
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.variableListPage));
        return placeRequestHierarchy;
    }

    public static List<PlaceRequest> buildAbsoluteVariablePlaceRequest(String code) {
        List<PlaceRequest> placeRequests = buildAbsoluteVariablesPlaceRequest();
        PlaceRequest variablePlace = buildRelativeVariablePlaceRequest(code);
        placeRequests.add(variablePlace);
        return placeRequests;
    }

    // Variable elements

    public static String getVariableElementParamFromUrl(PlaceManager placeManager) {
        return getParamFromUrl(placeManager, NameTokens.variableElementPage, PlaceRequestParams.variableElementParamId);
    }

    public static PlaceRequest buildRelativeVariableElementPlaceRequest(String variableElementCode) {
        PlaceRequest placeRequest = new PlaceRequest(NameTokens.variableElementPage).with(PlaceRequestParams.variableElementParamId, variableElementCode);
        return placeRequest;
    }

    public static List<PlaceRequest> buildAbsoluteVariableElementsPlaceRequest() {
        List<PlaceRequest> placeRequestHierarchy = new ArrayList<PlaceRequest>();
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.structuralResourcesPage));
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.variableElementsPage));
        return placeRequestHierarchy;
    }

    public static List<PlaceRequest> buildAbsoluteVariableElementPlaceRequest(String variableCode, String variableElementCode) {
        List<PlaceRequest> placeRequestHierarchy = buildAbsoluteVariablePlaceRequest(variableCode);
        placeRequestHierarchy.add(buildRelativeVariableElementPlaceRequest(variableElementCode));
        return placeRequestHierarchy;
    }

    // ---------------------------------------------------------------------------
    // GENERIC METHODS
    // ---------------------------------------------------------------------------

    public static List<PlaceRequest> buildAbsoluteResourcePlaceRequest(RelatedResourceDto relatedResourceDto) {
        if (relatedResourceDto != null) {
            String urn = relatedResourceDto.getUrn();
            if (SdmxSrmUrnParserUtils.isCodelistUrn(urn)) {
                return buildAbsoluteCodelistPlaceRequest(urn);
            } else if (SrmUrnParserUtils.isCodeUrn(urn)) {
                String codelistUrn = SrmUrnParserUtils.getCodelistUrnFromCodeUrn(urn);
                return buildAbsoluteCodePlaceRequest(codelistUrn, urn);
            } else if (SrmUrnParserUtils.isCodelistFamilyUrn(urn)) {
                return buildAbsoluteCodelistFamilyPlaceRequest(relatedResourceDto.getCode());
            } else if (SrmUrnParserUtils.isVariableFamilyUrn(urn)) {
                return buildAbsoluteVariableFamilyPlaceRequest(relatedResourceDto.getCode());
            } else if (SrmUrnParserUtils.isVariableUrn(urn)) {
                return buildAbsoluteVariablePlaceRequest(relatedResourceDto.getCode());
            } else if (SrmUrnParserUtils.isVariableElementUrn(urn)) {
                String variableUrn = SrmUrnParserUtils.getVariableUrnFromVariableElementUrn(urn);
                String variableCode = UrnUtils.removePrefix(variableUrn);
                return buildAbsoluteVariableElementPlaceRequest(variableCode, relatedResourceDto.getCode());
            } else if (SrmUrnParserUtils.isAgencyUrn(urn)) {
                String agencySchemeUrn = SrmUrnParserUtils.getAgencySchemeUrnFromAgencyUrn(urn);
                return buildAbsoluteOrganisationPlaceRequest(agencySchemeUrn, urn, OrganisationSchemeTypeEnum.AGENCY_SCHEME);
            }
        }
        return null;
    }

    // ---------------------------------------------------------------------------
    // PRIVATE METHODS
    // ---------------------------------------------------------------------------

    private static String getParamFromUrl(PlaceManager placeManager, String nameToken, String paramName) {
        for (PlaceRequest request : placeManager.getCurrentPlaceHierarchy()) {
            if (nameToken.equals(request.getNameToken())) {
                return request.getParameter(paramName, null);
            }
        }
        return null;
    }
}
