package org.siemac.metamac.srm.web.client.utils;

import org.siemac.metamac.core.common.util.shared.ArrayUtils;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.category.model.ds.CategorySchemeDS;
import org.siemac.metamac.srm.web.code.model.ds.CodelistDS;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeDS;
import org.siemac.metamac.srm.web.dsd.model.ds.DataStructureDefinitionDS;
import org.siemac.metamac.srm.web.organisation.model.ds.OrganisationSchemeDS;

/**
 * This class contain methods that, given a procStatus of a resource, return the fields that are required to go to the next procStatus.
 */
public class RequiredFieldUtils {

    //
    // CONCEPTS
    //

    private static final String[] conceptSchemeFieldsToProductionValidation = new String[]{ConceptSchemeDS.IS_PARTIAL};
    private static final String[] conceptSchemeFieldsToDiffusionValidation  = conceptSchemeFieldsToProductionValidation;
    private static final String[] conceptSchemeFieldsToInternalPublication  = conceptSchemeFieldsToDiffusionValidation;
    private static final String[] conceptSchemeFieldsToExternalPublication  = conceptSchemeFieldsToInternalPublication;

    public static String[] getConceptSchemeRequiredFieldsToNextProcStatus(ProcStatusEnum currentProcStatus) {
        switch (currentProcStatus) {
            case DRAFT:
                return conceptSchemeFieldsToProductionValidation;
            case VALIDATION_REJECTED:
                return conceptSchemeFieldsToProductionValidation;
            case PRODUCTION_VALIDATION:
                return conceptSchemeFieldsToDiffusionValidation;
            case DIFFUSION_VALIDATION:
                return conceptSchemeFieldsToInternalPublication;
            case INTERNALLY_PUBLISHED:
                return conceptSchemeFieldsToExternalPublication;
            default:
                return new String[]{};
        }
    }

    //
    // CODES
    //

    private static final String[] codelistFieldsToProductionValidation = new String[]{CodelistDS.IS_PARTIAL};
    private static final String[] codelistFieldsToDiffusionValidation  = codelistFieldsToProductionValidation;
    private static final String[] codelistFieldsToInternalPublication  = ArrayUtils.addStringElementsToStringArray(codelistFieldsToDiffusionValidation, CodelistDS.ACCESS_TYPE,
                                                                               CodelistDS.DEFAULT_ORDER, CodelistDS.DEFAULT_OPENNESS_LEVEL);
    private static final String[] codelistFieldsToExternalPublication  = codelistFieldsToInternalPublication;

    public static String[] getCodelistRequiredFieldsToNextProcStatus(ProcStatusEnum currentProcStatus) {
        switch (currentProcStatus) {
            case DRAFT:
                return codelistFieldsToProductionValidation;
            case VALIDATION_REJECTED:
                return codelistFieldsToProductionValidation;
            case PRODUCTION_VALIDATION:
                return codelistFieldsToDiffusionValidation;
            case DIFFUSION_VALIDATION:
                return codelistFieldsToInternalPublication;
            case INTERNALLY_PUBLISHED:
                return codelistFieldsToExternalPublication;
            default:
                return new String[]{};
        }
    }

    //
    // DSDs
    //

    private static final String[] dsdFieldsToProductionValidation = new String[]{DataStructureDefinitionDS.AUTO_OPEN};
    private static final String[] dsdFieldsToDiffusionValidation  = dsdFieldsToProductionValidation;
    private static final String[] dsdFieldsToInternalPublication  = dsdFieldsToDiffusionValidation;
    private static final String[] dsdFieldsToExternalPublication  = dsdFieldsToInternalPublication;

    public static String[] getDsdRequiredFieldsToNextProcStatus(ProcStatusEnum currentProcStatus) {
        switch (currentProcStatus) {
            case DRAFT:
                return dsdFieldsToProductionValidation;
            case VALIDATION_REJECTED:
                return dsdFieldsToProductionValidation;
            case PRODUCTION_VALIDATION:
                return dsdFieldsToDiffusionValidation;
            case DIFFUSION_VALIDATION:
                return dsdFieldsToInternalPublication;
            case INTERNALLY_PUBLISHED:
                return dsdFieldsToExternalPublication;
            default:
                return new String[]{};
        }
    }

    //
    // ORGANISATIONS
    //

    private static final String[] organisationSchemeFieldsToProductionValidation = new String[]{OrganisationSchemeDS.IS_PARTIAL};
    private static final String[] organisationSchemeFieldsToDiffusionValidation  = organisationSchemeFieldsToProductionValidation;
    private static final String[] organisationSchemeFieldsToInternalPublication  = organisationSchemeFieldsToDiffusionValidation;
    private static final String[] organisationSchemeFieldsToExternalPublication  = organisationSchemeFieldsToInternalPublication;

    public static String[] getOrganisationSchemeRequiredFieldsToNextProcStatus(ProcStatusEnum currentProcStatus) {
        switch (currentProcStatus) {
            case DRAFT:
                return organisationSchemeFieldsToProductionValidation;
            case VALIDATION_REJECTED:
                return organisationSchemeFieldsToProductionValidation;
            case PRODUCTION_VALIDATION:
                return organisationSchemeFieldsToDiffusionValidation;
            case DIFFUSION_VALIDATION:
                return organisationSchemeFieldsToInternalPublication;
            case INTERNALLY_PUBLISHED:
                return organisationSchemeFieldsToExternalPublication;
            default:
                return new String[]{};
        }
    }

    //
    // CATEGORIES
    //

    private static final String[] categorySchemeFieldsToProductionValidation = new String[]{CategorySchemeDS.IS_PARTIAL};
    private static final String[] categorySchemeFieldsToDiffusionValidation  = categorySchemeFieldsToProductionValidation;
    private static final String[] categorySchemeFieldsToInternalPublication  = categorySchemeFieldsToDiffusionValidation;
    private static final String[] categorySchemeFieldsToExternalPublication  = categorySchemeFieldsToInternalPublication;

    public static String[] getCategorySchemeRequiredFieldsToNextProcStatus(ProcStatusEnum currentProcStatus) {
        switch (currentProcStatus) {
            case DRAFT:
                return categorySchemeFieldsToProductionValidation;
            case VALIDATION_REJECTED:
                return categorySchemeFieldsToProductionValidation;
            case PRODUCTION_VALIDATION:
                return categorySchemeFieldsToDiffusionValidation;
            case DIFFUSION_VALIDATION:
                return categorySchemeFieldsToInternalPublication;
            case INTERNALLY_PUBLISHED:
                return categorySchemeFieldsToExternalPublication;
            default:
                return new String[]{};
        }
    }
}
