package org.siemac.metamac.srm.rest.internal.v1_0.service.utils;

import java.util.HashMap;
import java.util.Map;

import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.navigation.shared.NameTokens;
import org.siemac.metamac.srm.navigation.shared.PlaceRequestParams;
import org.springframework.web.util.UriTemplate;

public class InternalWebApplicationNavigation {

    private final String      PATH_STRUCTURAL_RESOURCES = "#" + NameTokens.structuralResourcesPage;
    private final String      SEPARATOR                 = "/";
    private final String      ITEM_SCHEME_PARAMETER     = "itemSchemeParam";
    private final String      RESOURCE_ID_PARAMETER     = "resourceParam";
    private final String      RESOURCE_TYPE_PARAMETER   = "resourceTypeParam";

    private final UriTemplate dataStructureTemplate;
    private final UriTemplate organisationSchemeTemplate;
    private final UriTemplate organisationTemplate;
    private final UriTemplate categorySchemeTemplate;
    private final UriTemplate categoryTemplate;
    private final UriTemplate conceptSchemeTemplate;
    private final UriTemplate conceptTemplate;
    private final UriTemplate codelistTemplate;
    private final UriTemplate codeTemplate;
    private final UriTemplate variableTemplate;
    private final UriTemplate variableFamilyTemplate;
    private final UriTemplate codelistFamilyTemplate;

    public InternalWebApplicationNavigation(String webApplicationPath) {
        // Dsd
        dataStructureTemplate = new UriTemplate(webApplicationPath + SEPARATOR + PATH_STRUCTURAL_RESOURCES + SEPARATOR + NameTokens.dsdListPage + SEPARATOR + NameTokens.dsdPage + ";"
                + PlaceRequestParams.dsdParamId + "=" + "{" + RESOURCE_ID_PARAMETER + "}");
        // Organisation
        organisationSchemeTemplate = new UriTemplate(webApplicationPath + SEPARATOR + PATH_STRUCTURAL_RESOURCES + SEPARATOR + NameTokens.organisationSchemeListPage + SEPARATOR
                + NameTokens.organisationSchemePage + ";" + PlaceRequestParams.organisationSchemeParamType + "={" + RESOURCE_TYPE_PARAMETER + "};" + PlaceRequestParams.organisationSchemeParamId + "="
                + "{" + RESOURCE_ID_PARAMETER + "}");
        organisationTemplate = new UriTemplate(webApplicationPath + SEPARATOR + PATH_STRUCTURAL_RESOURCES + SEPARATOR + NameTokens.organisationSchemeListPage + SEPARATOR
                + NameTokens.organisationSchemePage + ";" + PlaceRequestParams.organisationSchemeParamType + "={" + RESOURCE_TYPE_PARAMETER + "};" + PlaceRequestParams.organisationSchemeParamId + "="
                + "{" + ITEM_SCHEME_PARAMETER + "}/" + NameTokens.organisationPage + ";" + PlaceRequestParams.organisationParamId + "={" + RESOURCE_ID_PARAMETER + "}");
        // Categories
        categorySchemeTemplate = new UriTemplate(webApplicationPath + SEPARATOR + PATH_STRUCTURAL_RESOURCES + SEPARATOR + NameTokens.categorySchemeListPage + SEPARATOR + NameTokens.categorySchemePage
                + ";" + PlaceRequestParams.categorySchemeParamId + "=" + "{" + RESOURCE_ID_PARAMETER + "}");
        categoryTemplate = new UriTemplate(webApplicationPath + SEPARATOR + PATH_STRUCTURAL_RESOURCES + SEPARATOR + NameTokens.categorySchemeListPage + SEPARATOR + NameTokens.categorySchemePage + ";"
                + PlaceRequestParams.categorySchemeParamId + "=" + "{" + ITEM_SCHEME_PARAMETER + "}/" + NameTokens.categoryPage + ";" + PlaceRequestParams.categoryParamId + "={"
                + RESOURCE_ID_PARAMETER + "}");
        // Concepts
        conceptSchemeTemplate = new UriTemplate(webApplicationPath + SEPARATOR + PATH_STRUCTURAL_RESOURCES + SEPARATOR + NameTokens.conceptSchemeListPage + SEPARATOR + NameTokens.conceptSchemePage
                + ";" + PlaceRequestParams.conceptSchemeParamId + "=" + "{" + RESOURCE_ID_PARAMETER + "}");
        conceptTemplate = new UriTemplate(webApplicationPath + SEPARATOR + PATH_STRUCTURAL_RESOURCES + SEPARATOR + NameTokens.conceptSchemeListPage + SEPARATOR + NameTokens.conceptSchemePage + ";"
                + PlaceRequestParams.conceptSchemeParamId + "=" + "{" + ITEM_SCHEME_PARAMETER + "}/" + NameTokens.conceptPage + ";" + PlaceRequestParams.conceptParamId + "={" + RESOURCE_ID_PARAMETER
                + "}");
        // Codes
        codelistTemplate = new UriTemplate(webApplicationPath + SEPARATOR + PATH_STRUCTURAL_RESOURCES + SEPARATOR + NameTokens.codelistListPage + SEPARATOR + NameTokens.codelistPage + ";"
                + PlaceRequestParams.codelistParamId + "=" + "{" + RESOURCE_ID_PARAMETER + "}");
        codeTemplate = new UriTemplate(webApplicationPath + SEPARATOR + PATH_STRUCTURAL_RESOURCES + SEPARATOR + NameTokens.codelistListPage + SEPARATOR + NameTokens.codelistPage + ";"
                + PlaceRequestParams.codelistParamId + "=" + "{" + ITEM_SCHEME_PARAMETER + "}/" + NameTokens.codePage + ";" + PlaceRequestParams.codeParamId + "={" + RESOURCE_ID_PARAMETER + "}");
        // Variables, families
        variableFamilyTemplate = new UriTemplate(webApplicationPath + SEPARATOR + PATH_STRUCTURAL_RESOURCES + SEPARATOR + NameTokens.variableFamilyListPage + SEPARATOR + NameTokens.variableFamilyPage
                + ";" + PlaceRequestParams.variableFamilyParamId + "=" + "{" + RESOURCE_ID_PARAMETER + "}");
        codelistFamilyTemplate = new UriTemplate(webApplicationPath + SEPARATOR + PATH_STRUCTURAL_RESOURCES + SEPARATOR + NameTokens.codelistFamilyListPage + SEPARATOR + NameTokens.codelistFamilyPage
                + ";" + PlaceRequestParams.codelistFamilyParamId + "=" + "{" + RESOURCE_ID_PARAMETER + "}");
        variableTemplate = new UriTemplate(webApplicationPath + SEPARATOR + PATH_STRUCTURAL_RESOURCES + SEPARATOR + NameTokens.variableListPage + SEPARATOR + NameTokens.variablePage + ";"
                + PlaceRequestParams.variableParamId + "=" + "{" + RESOURCE_ID_PARAMETER + "}");
    }

    public String buildDataStructureDefinitionUrl(DataStructureDefinitionVersionMetamac dataStructureDefinition) {
        Map<String, String> parameters = new HashMap<String, String>(1);
        String dsdUrlPart = UrnUtils.removePrefix(dataStructureDefinition.getMaintainableArtefact().getUrn());
        parameters.put(RESOURCE_ID_PARAMETER, dsdUrlPart);
        return dataStructureTemplate.expand(parameters).toString();
    }

    public String buildOrganisationSchemeUrl(OrganisationSchemeVersionMetamac organisationScheme) {
        Map<String, String> parameters = new HashMap<String, String>(2);
        String organisationSchemeUrlPart = UrnUtils.removePrefix(organisationScheme.getMaintainableArtefact().getUrn());
        parameters.put(RESOURCE_TYPE_PARAMETER, organisationScheme.getOrganisationSchemeType().getName());
        parameters.put(RESOURCE_ID_PARAMETER, organisationSchemeUrlPart);
        return organisationSchemeTemplate.expand(parameters).toString();
    }

    public String buildOrganisationUrl(OrganisationMetamac organisation) {
        String organisationSchemeUrlPart = UrnUtils.removePrefix(organisation.getItemSchemeVersion().getMaintainableArtefact().getUrn());
        String organisationUrlPart = organisation.getNameableArtefact().getCode();
        Map<String, String> parameters = new HashMap<String, String>(3);
        parameters.put(ITEM_SCHEME_PARAMETER, organisationSchemeUrlPart);
        parameters.put(RESOURCE_TYPE_PARAMETER, SrmRestInternalUtils.toOrganisationSchemeType(organisation.getOrganisationType()).getName());
        parameters.put(RESOURCE_ID_PARAMETER, organisationUrlPart);
        return organisationTemplate.expand(parameters).toString();
    }

    public String buildCategorySchemeUrl(CategorySchemeVersionMetamac categoryScheme) {
        Map<String, String> parameters = new HashMap<String, String>(1);
        String categorySchemeUrlPart = UrnUtils.removePrefix(categoryScheme.getMaintainableArtefact().getUrn());
        parameters.put(RESOURCE_ID_PARAMETER, categorySchemeUrlPart);
        return categorySchemeTemplate.expand(parameters).toString();
    }

    public String buildCategoryUrl(CategoryMetamac category) {
        String categorySchemeUrlPart = UrnUtils.removePrefix(category.getItemSchemeVersion().getMaintainableArtefact().getUrn());
        String categoryUrlPart = category.getNameableArtefact().getCodeFull();
        Map<String, String> parameters = new HashMap<String, String>(2);
        parameters.put(ITEM_SCHEME_PARAMETER, categorySchemeUrlPart);
        parameters.put(RESOURCE_ID_PARAMETER, categoryUrlPart);
        return categoryTemplate.expand(parameters).toString();
    }

    public String buildConceptSchemeUrl(ConceptSchemeVersionMetamac conceptScheme) {
        Map<String, String> parameters = new HashMap<String, String>(1);
        String conceptSchemeUrlPart = UrnUtils.removePrefix(conceptScheme.getMaintainableArtefact().getUrn());
        parameters.put(RESOURCE_ID_PARAMETER, conceptSchemeUrlPart);
        return conceptSchemeTemplate.expand(parameters).toString();
    }

    public String buildConceptUrl(ConceptMetamac concept) {
        String conceptSchemeUrlPart = UrnUtils.removePrefix(concept.getItemSchemeVersion().getMaintainableArtefact().getUrn());
        String conceptUrlPart = concept.getNameableArtefact().getCode();
        Map<String, String> parameters = new HashMap<String, String>(2);
        parameters.put(ITEM_SCHEME_PARAMETER, conceptSchemeUrlPart);
        parameters.put(RESOURCE_ID_PARAMETER, conceptUrlPart);
        return conceptTemplate.expand(parameters).toString();
    }

    public String buildCodelistUrl(CodelistVersionMetamac codelist) {
        Map<String, String> parameters = new HashMap<String, String>(1);
        String codelistUrlPart = UrnUtils.removePrefix(codelist.getMaintainableArtefact().getUrn());
        parameters.put(RESOURCE_ID_PARAMETER, codelistUrlPart);
        return codelistTemplate.expand(parameters).toString();
    }

    public String buildCodeUrl(CodeMetamac code) {
        String codelistUrlPart = UrnUtils.removePrefix(code.getItemSchemeVersion().getMaintainableArtefact().getUrn());
        String codeUrlPart = code.getNameableArtefact().getCode();
        Map<String, String> parameters = new HashMap<String, String>(2);
        parameters.put(ITEM_SCHEME_PARAMETER, codelistUrlPart);
        parameters.put(RESOURCE_ID_PARAMETER, codeUrlPart);
        return codeTemplate.expand(parameters).toString();
    }

    public String buildVariableFamilyUrl(VariableFamily variableFamily) {
        Map<String, String> parameters = new HashMap<String, String>(1);
        parameters.put(RESOURCE_ID_PARAMETER, variableFamily.getNameableArtefact().getCode());
        return variableFamilyTemplate.expand(parameters).toString();
    }

    public String buildCodelistFamilyUrl(CodelistFamily codelistFamily) {
        Map<String, String> parameters = new HashMap<String, String>(1);
        parameters.put(RESOURCE_ID_PARAMETER, codelistFamily.getNameableArtefact().getCode());
        return codelistFamilyTemplate.expand(parameters).toString();
    }

    public String buildVariableUrl(Variable variable) {
        Map<String, String> parameters = new HashMap<String, String>(1);
        parameters.put(RESOURCE_ID_PARAMETER, variable.getNameableArtefact().getCode());
        return variableTemplate.expand(parameters).toString();
    }
}