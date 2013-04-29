package org.siemac.metamac.srm.core.security.shared;

import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.JEFE_NORMALIZACION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.JEFE_PRODUCCION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.TECNICO_APOYO_NORMALIZACION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.TECNICO_APOYO_PRODUCCION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.TECNICO_NORMALIZACION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.TECNICO_PRODUCCION;

import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum;
import org.siemac.metamac.sso.client.MetamacPrincipal;

public class SharedConceptsSecurityUtils extends SharedItemsSecurityUtils {

    //
    // CONCEPT SCHEMES
    //

    public static boolean canRetrieveConceptSchemeByUrn(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canRetrieveConceptSchemeVersions(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canCreateConceptScheme(MetamacPrincipal metamacPrincipal, ConceptSchemeTypeEnum type, String operationCode) {
        SrmRoleEnum[] roles = {JEFE_NORMALIZACION};
        return canCreateItemScheme(metamacPrincipal) && (!isOperationConceptSchemeType(type) || isOperationAllowed(metamacPrincipal, operationCode, roles));
    }

    public static boolean canUpdateConceptScheme(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, ConceptSchemeTypeEnum typeOld, String operationCodeOld, ConceptSchemeTypeEnum typeNew,
            String operationCodeNew) {
        return canUpdateConceptScheme(metamacPrincipal, procStatus, typeOld, operationCodeOld) && canUpdateConceptScheme(metamacPrincipal, procStatus, typeNew, operationCodeNew);
    }

    public static boolean canDeleteConceptScheme(MetamacPrincipal metamacPrincipal, ConceptSchemeTypeEnum type, String operationCode) {
        if (isNonOperationConceptSchemeType(type)) {
            return canDeleteItemScheme(metamacPrincipal);
        } else if (isOperationConceptSchemeType(type)) {
            SrmRoleEnum roles[] = {JEFE_NORMALIZACION, JEFE_PRODUCCION};
            return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
        }
        return false;
    }

    public static boolean canFindConceptSchemesByCondition(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canSendConceptSchemeToProductionValidation(MetamacPrincipal metamacPrincipal, ConceptSchemeTypeEnum type, String operationCode) {
        if (isNonOperationConceptSchemeType(type)) {
            return canSendItemSchemeToProductionValidation(metamacPrincipal);
        } else if (isOperationConceptSchemeType(type)) {
            return isAnySrmRole(metamacPrincipal)
                    && isOperationAllowed(metamacPrincipal, operationCode, TECNICO_APOYO_PRODUCCION, TECNICO_APOYO_NORMALIZACION, TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_PRODUCCION,
                            JEFE_NORMALIZACION);
        }
        return false;
    }

    public static boolean canSendConceptSchemeToDiffusionValidation(MetamacPrincipal metamacPrincipal, ConceptSchemeTypeEnum type, String operationCode) {
        if (isNonOperationConceptSchemeType(type)) {
            return canSendItemSchemeToDiffusionValidation(metamacPrincipal);
        } else if (isOperationConceptSchemeType(type)) {
            SrmRoleEnum[] roles = {TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION, JEFE_PRODUCCION};
            return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
        }
        return false;
    }

    public static boolean canRejectConceptSchemeValidation(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        if (isNonOperationConceptSchemeType(type)) {
            return canRejectItemSchemeValidation(metamacPrincipal, procStatus);
        } else if (isOperationConceptSchemeType(type)) {
            if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
                SrmRoleEnum[] roles = {TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION, JEFE_PRODUCCION};
                return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
                SrmRoleEnum[] roles = {JEFE_NORMALIZACION};
                return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        }
        return false;
    }

    public static boolean canPublishConceptSchemeInternally(MetamacPrincipal metamacPrincipal, ConceptSchemeTypeEnum type, String operationCode) {
        if (isNonOperationConceptSchemeType(type)) {
            return canPublishItemSchemeInternally(metamacPrincipal);
        } else if (isOperationConceptSchemeType(type)) {
            SrmRoleEnum[] roles = {JEFE_NORMALIZACION};
            return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
        }
        return false;
    }

    public static boolean canPublishConceptSchemeExternally(MetamacPrincipal metamacPrincipal, ConceptSchemeTypeEnum type, String operationCode) {
        if (isNonOperationConceptSchemeType(type)) {
            return canPublishItemSchemeExternally(metamacPrincipal);
        } else if (isOperationConceptSchemeType(type)) {
            SrmRoleEnum roles[] = {JEFE_NORMALIZACION, JEFE_PRODUCCION};
            return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
        }
        return false;
    }

    public static boolean canCopyConceptScheme(MetamacPrincipal metamacPrincipal, ConceptSchemeTypeEnum type, String operationCode) {
        return canCreateConceptScheme(metamacPrincipal, type, operationCode);
    }

    public static boolean canVersioningConceptScheme(MetamacPrincipal metamacPrincipal, ConceptSchemeTypeEnum type, String operationCode) {
        if (isNonOperationConceptSchemeType(type)) {
            return canVersioningItemScheme(metamacPrincipal);
        } else if (isOperationConceptSchemeType(type)) {
            SrmRoleEnum roles[] = {JEFE_NORMALIZACION};
            return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
        }
        return false;
    }

    public static boolean canCreateConceptSchemeTemporalVersion(MetamacPrincipal metamacPrincipal, ConceptSchemeTypeEnum type, String operationCode) {
        return canVersioningConceptScheme(metamacPrincipal, type, operationCode);
    }

    public static boolean canAnnounceConceptScheme(MetamacPrincipal metamacPrincipal, ConceptSchemeTypeEnum type, String operationCode) {
        if (isNonOperationConceptSchemeType(type)) {
            return canAnnounceItemScheme(metamacPrincipal);
        } else if (isOperationConceptSchemeType(type)) {
            SrmRoleEnum roles[] = {TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION, JEFE_PRODUCCION};
            return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
        }
        return false;
    }

    public static boolean canEndConceptSchemeValidity(MetamacPrincipal metamacPrincipal, ConceptSchemeTypeEnum type, String operationCode) {
        if (isNonOperationConceptSchemeType(type)) {
            return canEndItemSchemeValidity(metamacPrincipal);
        } else if (isOperationConceptSchemeType(type)) {
            SrmRoleEnum roles[] = {JEFE_NORMALIZACION, JEFE_PRODUCCION};
            return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
        }
        return false;
    }

    // Concepts

    public static boolean canCreateConcept(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        return canModifyConceptFromConceptScheme(metamacPrincipal, procStatus, type, operationCode);
    }

    public static boolean canUpdateConcept(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        return canModifyConceptFromConceptScheme(metamacPrincipal, procStatus, type, operationCode);
    }

    public static boolean canRetrieveConceptByUrn(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canDeleteConcept(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        return canModifyConceptFromConceptScheme(metamacPrincipal, procStatus, type, operationCode);
    }

    public static boolean canRetrieveConceptsByConceptSchemeUrn(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canFindConceptsByCondition(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canAddRelatedConcept(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        return canModifyConceptFromConceptScheme(metamacPrincipal, procStatus, type, operationCode);
    }

    public static boolean canAddRoleConcept(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        return canModifyConceptFromConceptScheme(metamacPrincipal, procStatus, type, operationCode);
    }

    public static boolean canDeleteRelatedConcept(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        return canModifyConceptFromConceptScheme(metamacPrincipal, procStatus, type, operationCode);
    }

    public static boolean canDeleteRoleConcept(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        return canModifyConceptFromConceptScheme(metamacPrincipal, procStatus, type, operationCode);
    }

    public static boolean canRetrieveRelatedConcepts(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canRetrieveRoleConcepts(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canRetrieveConceptTypeByIdentifier(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canFindAllConceptTypes(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    // Categorisations

    public static boolean canModifyCategorisation(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        if (ProcStatusEnum.INTERNALLY_PUBLISHED.equals(procStatus) || ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(procStatus)) {
            return canPublishConceptSchemeExternally(metamacPrincipal, type, operationCode);
        } else {
            return canUpdateConceptScheme(metamacPrincipal, procStatus, type, operationCode);
        }
    }

    private static boolean canUpdateConceptScheme(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        if (isNonOperationConceptSchemeType(type)) {
            return canUpdateItemScheme(metamacPrincipal, procStatus);
        } else if (isOperationConceptSchemeType(type)) {
            if (ProcStatusEnum.DRAFT.equals(procStatus)) {
                SrmRoleEnum[] roles = {TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_PRODUCCION, JEFE_NORMALIZACION};
                return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            } else if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
                SrmRoleEnum[] roles = {TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_PRODUCCION, JEFE_NORMALIZACION};
                return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
                SrmRoleEnum[] roles = {JEFE_NORMALIZACION, JEFE_PRODUCCION};
                return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        }
        return false;
    }

    private static boolean canModifyConceptFromConceptScheme(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        if (isNonOperationConceptSchemeType(type)) {
            return canModifyItemFromItemScheme(metamacPrincipal, procStatus);
        } else if (isOperationConceptSchemeType(type)) {
            if (ProcStatusEnum.DRAFT.equals(procStatus)) {
                return isAnySrmRole(metamacPrincipal)
                        && isOperationAllowed(metamacPrincipal, operationCode, TECNICO_APOYO_PRODUCCION, TECNICO_APOYO_NORMALIZACION, TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_PRODUCCION,
                                JEFE_NORMALIZACION);
            } else if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
                SrmRoleEnum[] roles = {TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION, JEFE_PRODUCCION};
                return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
                SrmRoleEnum[] roles = {JEFE_NORMALIZACION, JEFE_PRODUCCION};
                return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        }
        return false;
    }
}