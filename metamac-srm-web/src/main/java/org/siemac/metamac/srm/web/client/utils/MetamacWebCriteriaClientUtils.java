package org.siemac.metamac.srm.web.client.utils;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.shared.criteria.CategorySchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.CategoryWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.CodeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptSchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.DataStructureDefinitionWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ItemWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.OrganisationSchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.VersionableResourceWebCriteria;

public class MetamacWebCriteriaClientUtils {

    // CONCEPTS

    public static ConceptSchemeWebCriteria addLastVersionConditionToConceptSchemeWebCriteria(ConceptSchemeWebCriteria conceptSchemeWebCriteria) {
        return (ConceptSchemeWebCriteria) getVersionableResourceWebCriteriaForLastVersion(conceptSchemeWebCriteria);
    }

    // CATEGORIES

    public static CategorySchemeWebCriteria addLastVersionConditionToCategorySchemeWebCriteria(CategorySchemeWebCriteria categorySchemeWebCriteria) {
        return (CategorySchemeWebCriteria) getVersionableResourceWebCriteriaForLastVersion(categorySchemeWebCriteria);
    }

    public static CategoryWebCriteria addLastVersionConditionToCategoryWebCriteria(CategoryWebCriteria categoryWebCriteria) {
        return (CategoryWebCriteria) getItemWebCriteriaForLastVersion(categoryWebCriteria);
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

    public static CodelistWebCriteria getCodelistWebCriteriaForLastVersion() {
        return (CodelistWebCriteria) getVersionableResourceWebCriteriaForLastVersion(new CodelistWebCriteria());
    }

    public static CodelistWebCriteria getCodelistWebCriteriaForCodelistsThatCanBeReplaced() {
        CodelistWebCriteria codelistWebCriteria = new CodelistWebCriteria();
        codelistWebCriteria.setProcStatus(ProcStatusEnum.EXTERNALLY_PUBLISHED);
        return codelistWebCriteria;
    }

    public static CodeWebCriteria getCodeWebCriteriaForLastVersion() {
        return (CodeWebCriteria) getItemWebCriteriaForLastVersion(new CodeWebCriteria());
    }

    // ORGANISATIONS

    public static OrganisationSchemeWebCriteria getOrganisationSchemeWebCriteriaForLastVersion() {
        return (OrganisationSchemeWebCriteria) getVersionableResourceWebCriteriaForLastVersion(new OrganisationSchemeWebCriteria());
    }

    // DSD

    public static DataStructureDefinitionWebCriteria getDataStructureDefinitionWebCriteriaForLastVersion() {
        return (DataStructureDefinitionWebCriteria) getVersionableResourceWebCriteriaForLastVersion(new DataStructureDefinitionWebCriteria());
    }

    // GENERAL

    public static VersionableResourceWebCriteria getVersionableResourceWebCriteriaForLastVersion(VersionableResourceWebCriteria versionableResourceWebCriteria) {
        versionableResourceWebCriteria.setIsLastVersion(true);
        return versionableResourceWebCriteria;
    }

    public static ItemWebCriteria getItemWebCriteriaForLastVersion(ItemWebCriteria itemWebCriteria) {
        itemWebCriteria.setIsLastVersion(true);
        return itemWebCriteria;
    }
}
