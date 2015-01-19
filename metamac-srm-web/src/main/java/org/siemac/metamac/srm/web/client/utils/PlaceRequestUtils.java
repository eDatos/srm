package org.siemac.metamac.srm.web.client.utils;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
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

    public static List<PlaceRequest> buildAbsoluteDsdGeneralPlaceRequest(String dsdUrn) {
        List<PlaceRequest> placeRequests = buildAbsoluteDsdPlaceRequest(dsdUrn);
        PlaceRequest dsdPlace = new PlaceRequest(NameTokens.dsdGeneralPage);
        placeRequests.add(dsdPlace);
        return placeRequests;
    }

    public static List<PlaceRequest> buildAbsoluteDsdPrimaryMeasurePlaceRequest(String dsdUrn) {
        List<PlaceRequest> placeRequests = buildAbsoluteDsdPlaceRequest(dsdUrn);
        PlaceRequest dsdPlace = new PlaceRequest(NameTokens.dsdPrimaryMeasurePage);
        placeRequests.add(dsdPlace);
        return placeRequests;
    }

    public static List<PlaceRequest> buildAbsoluteDsdDimensionsPlaceRequest(String dsdUrn) {
        List<PlaceRequest> placeRequests = buildAbsoluteDsdPlaceRequest(dsdUrn);
        PlaceRequest dsdPlace = new PlaceRequest(NameTokens.dsdDimensionsPage);
        placeRequests.add(dsdPlace);
        return placeRequests;
    }

    public static List<PlaceRequest> buildAbsoluteDsdAttributesPlaceRequest(String dsdUrn) {
        List<PlaceRequest> placeRequests = buildAbsoluteDsdPlaceRequest(dsdUrn);
        PlaceRequest dsdPlace = new PlaceRequest(NameTokens.dsdAttributesPage);
        placeRequests.add(dsdPlace);
        return placeRequests;
    }

    public static List<PlaceRequest> buildAbsoluteDsdGroupKeysPlaceRequest(String dsdUrn) {
        List<PlaceRequest> placeRequests = buildAbsoluteDsdPlaceRequest(dsdUrn);
        PlaceRequest dsdPlace = new PlaceRequest(NameTokens.dsdGroupKeysPage);
        placeRequests.add(dsdPlace);
        return placeRequests;
    }

    public static List<PlaceRequest> buildAbsoluteDsdCategorisationsPlaceRequest(String dsdUrn) {
        List<PlaceRequest> placeRequests = buildAbsoluteDsdPlaceRequest(dsdUrn);
        PlaceRequest dsdPlace = new PlaceRequest(NameTokens.dsdCategorisationsPage);
        placeRequests.add(dsdPlace);
        return placeRequests;
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

    public static String getDsdBreadCrumbTitle(PlaceRequest placeRequest) {
        String dsdTripletIdentifier = placeRequest.getParameter(PlaceRequestParams.dsdParamId, null);
        if (!StringUtils.isBlank(dsdTripletIdentifier)) {
            return UrnUtils.getStructureCodeFromUrnWithoutPrefix(dsdTripletIdentifier);
        }
        return getConstants().breadcrumbDSD();
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

    public static String getConceptSchemeBreadCrumbTitle(PlaceRequest placeRequest) {
        String conceptSchemeTripletIdentifier = placeRequest.getParameter(PlaceRequestParams.conceptSchemeParamId, null);
        if (!StringUtils.isBlank(conceptSchemeTripletIdentifier)) {
            return UrnUtils.getItemSchemeCodeFromUrnWithoutPrefix(conceptSchemeTripletIdentifier);
        }
        return getConstants().breadcrumbConceptScheme();
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

    public static List<PlaceRequest> buildAbsoluteConceptPlaceRequest(String conceptUrn) {
        String conceptSchemeUrn = SrmUrnParserUtils.getConceptSchemeUrnFromConceptUrn(conceptUrn);
        return buildAbsoluteConceptPlaceRequest(conceptSchemeUrn, conceptUrn);
    }

    public static PlaceRequest buildRelativeConceptPlaceRequest(String conceptUrn) {
        String[] splitUrn = UrnUtils.splitUrnByDots(UrnUtils.removePrefix(conceptUrn));
        PlaceRequest placeRequest = new PlaceRequest(NameTokens.conceptPage).with(PlaceRequestParams.conceptParamId, splitUrn[splitUrn.length - 1]);
        return placeRequest;
    }

    public static String getConceptBreadCrumbTitle(PlaceRequest placeRequest) {
        String conceptCode = placeRequest.getParameter(PlaceRequestParams.conceptParamId, null);
        if (!StringUtils.isBlank(conceptCode)) {
            return conceptCode;
        }
        return getConstants().breadcrumbConcept();
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

    public static String getOrganisationSchemeBreadCrumbTitle(PlaceRequest placeRequest) {
        String organisationTripletIdentifier = placeRequest.getParameter(PlaceRequestParams.organisationSchemeParamId, null);
        if (!StringUtils.isBlank(organisationTripletIdentifier)) {
            return UrnUtils.getItemSchemeCodeFromUrnWithoutPrefix(organisationTripletIdentifier);
        }
        return getConstants().breadcrumbOrganisationScheme();
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

    public static String getOrganisationBreadCrumbTitle(PlaceRequest placeRequest) {
        String organisationCode = placeRequest.getParameter(PlaceRequestParams.organisationParamId, null);
        if (!StringUtils.isBlank(organisationCode)) {
            return organisationCode;
        }
        return getConstants().breadcrumbOrganisation();
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

    public static String getCategorySchemeBreadCrumbTitle(PlaceRequest placeRequest) {
        String categorySchemeTripletIdentifier = placeRequest.getParameter(PlaceRequestParams.categorySchemeParamId, null);
        if (!StringUtils.isBlank(categorySchemeTripletIdentifier)) {
            return UrnUtils.getItemSchemeCodeFromUrnWithoutPrefix(categorySchemeTripletIdentifier);
        }
        return getConstants().breadcrumbCategoryScheme();
    }

    // Categories

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
        String[] splitIdentifier = UrnUtils.splitUrnItem(categoryUrn);
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

    public static String getCategoryBreadCrumbTitle(PlaceRequest placeRequest) {
        String categoryCode = placeRequest.getParameter(PlaceRequestParams.categoryParamId, null);
        if (!StringUtils.isBlank(categoryCode)) {
            return categoryCode;
        }
        return getConstants().breadcrumbCategory();
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

    public static String getCodelistBreadCrumbTitle(PlaceRequest placeRequest) {
        String codelistTripletIdentifier = placeRequest.getParameter(PlaceRequestParams.codelistParamId, null);
        if (!StringUtils.isBlank(codelistTripletIdentifier)) {
            return UrnUtils.getItemSchemeCodeFromUrnWithoutPrefix(codelistTripletIdentifier);
        }
        return getConstants().breadcrumbCodelist();
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

    public static String getCodeBreadCrumbTitle(PlaceRequest placeRequest) {
        String codeCode = placeRequest.getParameter(PlaceRequestParams.codeParamId, null);
        if (!StringUtils.isBlank(codeCode)) {
            return codeCode;
        }
        return getConstants().breadcrumbCode();
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

    public static String getCodelistFamilyBreadCrumbTitle(PlaceRequest placeRequest) {
        String familyCode = placeRequest.getParameter(PlaceRequestParams.codelistFamilyParamId, null);
        if (!StringUtils.isBlank(familyCode)) {
            return familyCode;
        }
        return getConstants().breadcrumbCodelistFamily();
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

    public static String getVariableFamilyBreadCrumbTitle(PlaceRequest placeRequest) {
        String familyCode = placeRequest.getParameter(PlaceRequestParams.variableFamilyParamId, null);
        if (!StringUtils.isBlank(familyCode)) {
            return familyCode;
        }
        return getConstants().breadcrumbVariableFamily();
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

    public static String getVariableBreadCrumbTitle(PlaceRequest placeRequest) {
        String variableCode = placeRequest.getParameter(PlaceRequestParams.variableParamId, null);
        if (!StringUtils.isBlank(variableCode)) {
            return variableCode;
        }
        return getConstants().breadcrumbVariable();
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

    public static List<PlaceRequest> buildAbsoluteVariableElementPlaceRequest(String variableElementUrn) {
        String variableUrn = SrmUrnParserUtils.getVariableUrnFromVariableElementUrn(variableElementUrn);
        String variableCode = SrmUrnParserUtils.getVariableCodeFromVariableUrn(variableUrn);
        String variableElementCode = SrmUrnParserUtils.getVariableElementCodeFromVariableElementUrn(variableElementUrn);
        return buildAbsoluteVariableElementPlaceRequest(variableCode, variableElementCode);
    }

    public static String getVariableElementBreadCrumbTitle(PlaceRequest placeRequest) {
        String variableElementCode = placeRequest.getParameter(PlaceRequestParams.variableElementParamId, null);
        if (!StringUtils.isBlank(variableElementCode)) {
            return variableElementCode;
        }
        return getConstants().breadcrumbVariableElement();
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
            } else if (SrmUrnParserUtils.isCategorySchemeUrn(urn)) {
                return buildAbsoluteCategorySchemePlaceRequest(urn);
            } else if (SrmUrnParserUtils.isCategoryUrn(urn)) {
                String categorySchemeUrn = SrmUrnParserUtils.getCategorySchemeUrnFromCategoryUrn(urn);
                return buildAbsoluteCategoryPlaceRequest(categorySchemeUrn, urn);
            } else if (SrmUrnParserUtils.isConceptSchemeUrn(urn)) {
                return buildAbsoluteConceptSchemePlaceRequest(urn);
            } else if (SrmUrnParserUtils.isConceptUrn(urn)) {
                String conceptSchemeUrn = SrmUrnParserUtils.getConceptSchemeUrnFromConceptUrn(urn);
                return buildAbsoluteConceptPlaceRequest(conceptSchemeUrn, urn);
            } else if (SrmUrnParserUtils.isAgencySchemeUrn(urn)) {
                return buildAbsoluteOrganisationSchemePlaceRequest(urn, OrganisationSchemeTypeEnum.AGENCY_SCHEME);
            } else if (SrmUrnParserUtils.isAgencyUrn(urn)) {
                String agencySchemeUrn = SrmUrnParserUtils.getAgencySchemeUrnFromAgencyUrn(urn);
                return buildAbsoluteOrganisationPlaceRequest(agencySchemeUrn, urn, OrganisationSchemeTypeEnum.AGENCY_SCHEME);
            } else if (SrmUrnParserUtils.isDataConsumerSchemeUrn(urn)) {
                return buildAbsoluteOrganisationSchemePlaceRequest(urn, OrganisationSchemeTypeEnum.DATA_CONSUMER_SCHEME);
            } else if (SrmUrnParserUtils.isDataConsumerUrn(urn)) {
                String dataConsumerSchemeUrn = SrmUrnParserUtils.getDataConsumerSchemeUrnFromDataConsumerUrn(urn);
                return buildAbsoluteOrganisationPlaceRequest(dataConsumerSchemeUrn, urn, OrganisationSchemeTypeEnum.DATA_CONSUMER_SCHEME);
            } else if (SrmUrnParserUtils.isDataProviderSchemeUrn(urn)) {
                return buildAbsoluteOrganisationSchemePlaceRequest(urn, OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME);
            } else if (SrmUrnParserUtils.isDataProviderUrn(urn)) {
                String dataProviderSchemeUrn = SrmUrnParserUtils.getDataProviderSchemeUrnFromDataProviderUrn(urn);
                return buildAbsoluteOrganisationPlaceRequest(dataProviderSchemeUrn, urn, OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME);
            } else if (SrmUrnParserUtils.isOrganisationUnitSchemeUrn(urn)) {
                return buildAbsoluteOrganisationSchemePlaceRequest(urn, OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME);
            } else if (SrmUrnParserUtils.isOrganisationUnitUrn(urn)) {
                String organisationUnitSchemeUrn = SrmUrnParserUtils.getOrganisationUnitSchemeUrnFromOrganisationUnitUrn(urn);
                return buildAbsoluteOrganisationPlaceRequest(organisationUnitSchemeUrn, urn, OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME);
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
                String value = request.getParameter(paramName, null);
                if (value != null) {
                    return decodeURI(value);
                }
            }
        }
        return null;
    }

    private static native String decodeURI(String s)
    /*-{
        return decodeURIComponent(s);
     }-*/;
}
