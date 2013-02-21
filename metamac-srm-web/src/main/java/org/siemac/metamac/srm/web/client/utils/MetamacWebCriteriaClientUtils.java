package org.siemac.metamac.srm.web.client.utils;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.shared.criteria.CategorySchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.CategoryWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.CodeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptSchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.DataStructureDefinitionWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ItemWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.OrganisationSchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.VersionableResourceWebCriteria;

public class MetamacWebCriteriaClientUtils {

    // CONCEPTS

    public static ConceptSchemeWebCriteria addLastVersionConditionToConceptSchemeWebCriteria(ConceptSchemeWebCriteria conceptSchemeWebCriteria) {
        return (ConceptSchemeWebCriteria) getVersionableResourceWebCriteriaForLastVersion(conceptSchemeWebCriteria);
    }

    public static ConceptWebCriteria addLastVersionConditionToConceptWebCriteria(ConceptWebCriteria conceptWebCriteria) {
        return (ConceptWebCriteria) addLastVersionConditionToItemWebCriteria(conceptWebCriteria);
    }

    // CATEGORIES

    public static CategorySchemeWebCriteria addLastVersionConditionToCategorySchemeWebCriteria(CategorySchemeWebCriteria categorySchemeWebCriteria) {
        return (CategorySchemeWebCriteria) getVersionableResourceWebCriteriaForLastVersion(categorySchemeWebCriteria);
    }

    public static CategoryWebCriteria addLastVersionConditionToCategoryWebCriteria(CategoryWebCriteria categoryWebCriteria) {
        return (CategoryWebCriteria) addLastVersionConditionToItemWebCriteria(categoryWebCriteria);
    }

    // To create a categorisation, the category must be externally published
    public static CategorySchemeWebCriteria addCategorisationConditionToCategorySchemeWebCriteria(CategorySchemeWebCriteria categorySchemeWebCriteria) {
        categorySchemeWebCriteria.setProcStatus(ProcStatusEnum.EXTERNALLY_PUBLISHED);
        return categorySchemeWebCriteria;
    }

    // To create a categorisation, the category must be externally published
    public static CategoryWebCriteria addCategorisationConditionToCategoryWebCriteria(CategoryWebCriteria categoryWebCriteria) {
        categoryWebCriteria.setIsExternallyPublished(true);
        return categoryWebCriteria;
    }

    // CODES

    public static CodelistWebCriteria addLastVersionConditionToCodelistWebCriteria(CodelistWebCriteria codelistWebCriteria) {
        return (CodelistWebCriteria) getVersionableResourceWebCriteriaForLastVersion(codelistWebCriteria);
    }

    public static CodelistWebCriteria addCanBeReplacedConditionToCodelistWebCriteria(CodelistWebCriteria codelistWebCriteria) {
        codelistWebCriteria.setProcStatus(ProcStatusEnum.EXTERNALLY_PUBLISHED);
        return codelistWebCriteria;
    }

    public static CodeWebCriteria addLastVersionConditionToCodeWebCriteria(CodeWebCriteria codeWebCriteria) {
        return (CodeWebCriteria) addLastVersionConditionToItemWebCriteria(codeWebCriteria);
    }

    // ORGANISATIONS

    public static OrganisationSchemeWebCriteria addLastVersionConditionToOrganisationSchemeWebCriteria(OrganisationSchemeWebCriteria organisationSchemeWebCriteria) {
        return (OrganisationSchemeWebCriteria) getVersionableResourceWebCriteriaForLastVersion(organisationSchemeWebCriteria);
    }

    // DSD

    public static DataStructureDefinitionWebCriteria addLastVersionConditionToDataStructureDefinitionWebCriteria(DataStructureDefinitionWebCriteria dataStructureDefinitionWebCriteria) {
        return (DataStructureDefinitionWebCriteria) getVersionableResourceWebCriteriaForLastVersion(dataStructureDefinitionWebCriteria);
    }

    // GENERAL

    public static VersionableResourceWebCriteria getVersionableResourceWebCriteriaForLastVersion(VersionableResourceWebCriteria versionableResourceWebCriteria) {
        versionableResourceWebCriteria.setIsLastVersion(true);
        return versionableResourceWebCriteria;
    }

    public static ItemWebCriteria addLastVersionConditionToItemWebCriteria(ItemWebCriteria itemWebCriteria) {
        itemWebCriteria.setIsLastVersion(true);
        return itemWebCriteria;
    }
}
