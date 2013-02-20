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

    // CODELISTS THAT CAN BE REPLACED

    public static CodelistWebCriteria getCodelistWebCriteriaForCodelistsThatCanBeReplaced() {
        CodelistWebCriteria codelistWebCriteria = new CodelistWebCriteria();
        codelistWebCriteria.setProcStatus(ProcStatusEnum.EXTERNALLY_PUBLISHED);
        return codelistWebCriteria;
    }

    // CRITERIA TO FIND LAST VERSION RESOURCES

    // Concepts

    public static ConceptSchemeWebCriteria addLastVersionConditionToConceptSchemeWebCriteria(ConceptSchemeWebCriteria conceptSchemeWebCriteria) {
        return (ConceptSchemeWebCriteria) getVersionableResourceWebCriteriaForLastVersion(conceptSchemeWebCriteria);
    }

    // Categories

    public static CategorySchemeWebCriteria addLastVersionConditionToCategorySchemeWebCriteria(CategorySchemeWebCriteria categorySchemeWebCriteria) {
        return (CategorySchemeWebCriteria) getVersionableResourceWebCriteriaForLastVersion(categorySchemeWebCriteria);
    }

    public static CategoryWebCriteria addLastVersionConditionToCategoryWebCriteria(CategoryWebCriteria categoryWebCriteria) {
        return (CategoryWebCriteria) getItemWebCriteriaForLastVersion(categoryWebCriteria);
    }

    public static CategorySchemeWebCriteria addCategorisationConditionToCategorySchemeWebCriteria(CategorySchemeWebCriteria categorySchemeWebCriteria) {
        categorySchemeWebCriteria.setProcStatus(ProcStatusEnum.EXTERNALLY_PUBLISHED);
        return categorySchemeWebCriteria;
    }

    // Codes

    public static CodelistWebCriteria getCodelistWebCriteriaForLastVersion() {
        return (CodelistWebCriteria) getVersionableResourceWebCriteriaForLastVersion(new CodelistWebCriteria());
    }

    public static CodeWebCriteria getCodeWebCriteriaForLastVersion() {
        return (CodeWebCriteria) getItemWebCriteriaForLastVersion(new CodeWebCriteria());
    }

    // Organisations

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
