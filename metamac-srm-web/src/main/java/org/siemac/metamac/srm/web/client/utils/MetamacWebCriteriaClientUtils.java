package org.siemac.metamac.srm.web.client.utils;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.shared.criteria.CategorySchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptSchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.DataStructureDefinitionWebCriteria;
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

    public static ConceptSchemeWebCriteria getConceptSchemeWebCriteriaForLastVersion() {
        return (ConceptSchemeWebCriteria) getVersionableResourceWebCriteriaForLastVersion(new ConceptSchemeWebCriteria());
    }

    public static CategorySchemeWebCriteria getCategorySchemeWebCriteriaForLastVersion() {
        return (CategorySchemeWebCriteria) getVersionableResourceWebCriteriaForLastVersion(new CategorySchemeWebCriteria());
    }

    public static CodelistWebCriteria getCodelistWebCriteriaForLastVersion() {
        return (CodelistWebCriteria) getVersionableResourceWebCriteriaForLastVersion(new CodelistWebCriteria());
    }

    public static OrganisationSchemeWebCriteria getOrganisationSchemeWebCriteriaForLastVersion() {
        return (OrganisationSchemeWebCriteria) getVersionableResourceWebCriteriaForLastVersion(new OrganisationSchemeWebCriteria());
    }

    public static DataStructureDefinitionWebCriteria getDataStructureDefinitionWebCriteriaForLastVersion() {
        return (DataStructureDefinitionWebCriteria) getVersionableResourceWebCriteriaForLastVersion(new DataStructureDefinitionWebCriteria());
    }

    public static VersionableResourceWebCriteria getVersionableResourceWebCriteriaForLastVersion(VersionableResourceWebCriteria versionableResourceWebCriteria) {
        versionableResourceWebCriteria.setIsLastVersion(true);
        return versionableResourceWebCriteria;
    }
}
