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

public class SharedConceptsSecurityUtils extends SharedSecurityUtils {

    //
    // CONCEPT SCHEMES
    //

    public static boolean canRetrieveConceptSchemeByUrn(MetamacPrincipal metamacPrincipal) {
        return isAnySrmRole(metamacPrincipal);
    }

    public static boolean canRetrieveConceptSchemeVersions(MetamacPrincipal metamacPrincipal) {
        return isAnySrmRole(metamacPrincipal);
    }

    public static boolean canCreateConceptScheme(MetamacPrincipal metamacPrincipal, ConceptSchemeTypeEnum type, String operationCode) {
        SrmRoleEnum[] roles = {JEFE_NORMALIZACION};
        return isSrmRoleAllowed(metamacPrincipal, roles) && (!isOperationConceptSchemeType(type) || isOperationAllowed(metamacPrincipal, operationCode, roles));
    }

    public static boolean canUpdateConceptScheme(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, ConceptSchemeTypeEnum typeOld, String operationCodeOld, ConceptSchemeTypeEnum typeNew,
            String operationCodeNew) {
        return canUpdateConceptScheme(metamacPrincipal, procStatus, typeOld, operationCodeOld) && canUpdateConceptScheme(metamacPrincipal, procStatus, typeNew, operationCodeNew);
    }

    public static boolean canDeleteConceptScheme(MetamacPrincipal metamacPrincipal, ConceptSchemeTypeEnum type, String operationCode) {
        if (isNonOperationConceptSchemeType(type)) {
            return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
        } else if (isOperationConceptSchemeType(type)) {
            SrmRoleEnum roles[] = {JEFE_NORMALIZACION, JEFE_PRODUCCION};
            return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
        }
        return false;
    }

    public static boolean canFindConceptSchemesByCondition(MetamacPrincipal metamacPrincipal) {
        return isAnySrmRole(metamacPrincipal);
    }

    public static boolean canSendConceptSchemeToProductionValidation(MetamacPrincipal metamacPrincipal, ConceptSchemeTypeEnum type, String operationCode) {
        if (isNonOperationConceptSchemeType(type)) {
            return isSrmRoleAllowed(metamacPrincipal, TECNICO_APOYO_NORMALIZACION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
        } else if (isOperationConceptSchemeType(type)) {
            return isAnySrmRole(metamacPrincipal)
                    && isOperationAllowed(metamacPrincipal, operationCode, TECNICO_APOYO_PRODUCCION, TECNICO_APOYO_NORMALIZACION, TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_PRODUCCION,
                            JEFE_NORMALIZACION);
        }
        return false;
    }

    public static boolean canSendConceptSchemeToDiffusionValidation(MetamacPrincipal metamacPrincipal, ConceptSchemeTypeEnum type, String operationCode) {
        if (isNonOperationConceptSchemeType(type)) {
            return isSrmRoleAllowed(metamacPrincipal, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
        } else if (isOperationConceptSchemeType(type)) {
            SrmRoleEnum[] roles = {TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION, JEFE_PRODUCCION};
            return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
        }
        return false;
    }

    public static boolean canRejectConceptSchemeValidation(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isSrmRoleAllowed(metamacPrincipal, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                SrmRoleEnum[] roles = {TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION, JEFE_PRODUCCION};
                return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                SrmRoleEnum[] roles = {JEFE_NORMALIZACION};
                return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        }
        return false;
    }

    public static boolean canPublishConceptSchemeInternally(MetamacPrincipal metamacPrincipal, ConceptSchemeTypeEnum type, String operationCode) {
        if (isNonOperationConceptSchemeType(type)) {
            return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
        } else if (isOperationConceptSchemeType(type)) {
            SrmRoleEnum[] roles = {JEFE_NORMALIZACION};
            return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
        }
        return false;
    }

    public static boolean canPublishConceptSchemeExternally(MetamacPrincipal metamacPrincipal, ConceptSchemeTypeEnum type, String operationCode) {
        if (isNonOperationConceptSchemeType(type)) {
            return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
        } else if (isOperationConceptSchemeType(type)) {
            SrmRoleEnum roles[] = {JEFE_NORMALIZACION, JEFE_PRODUCCION};
            return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
        }
        return false;
    }

    public static boolean canVersioningConceptScheme(MetamacPrincipal metamacPrincipal, ConceptSchemeTypeEnum type, String operationCode) {
        if (isNonOperationConceptSchemeType(type)) {
            return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
        } else if (isOperationConceptSchemeType(type)) {
            SrmRoleEnum roles[] = {JEFE_NORMALIZACION};
            return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
        }
        return false;
    }

    public static boolean canAnnounceConceptScheme(MetamacPrincipal metamacPrincipal, ConceptSchemeTypeEnum type, String operationCode) {
        if (isNonOperationConceptSchemeType(type)) {
            return isSrmRoleAllowed(metamacPrincipal, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
        } else if (isOperationConceptSchemeType(type)) {
            SrmRoleEnum roles[] = {TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION, JEFE_PRODUCCION};
            return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
        }
        return false;
    }

    public static boolean canEndConceptSchemeValidity(MetamacPrincipal metamacPrincipal, ConceptSchemeTypeEnum type, String operationCode) {
        if (isNonOperationConceptSchemeType(type)) {
            return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
        } else if (isOperationConceptSchemeType(type)) {
            SrmRoleEnum roles[] = {JEFE_NORMALIZACION, JEFE_PRODUCCION};
            return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
        }
        return false;
    }

    // Concepts

    public static boolean canCreateConcept(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        return canCrudConceptInConceptScheme(metamacPrincipal, procStatus, type, operationCode);
    }

    public static boolean canUpdateConcept(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        return canCrudConceptInConceptScheme(metamacPrincipal, procStatus, type, operationCode);
    }

    public static boolean canRetrieveConceptByUrn(MetamacPrincipal metamacPrincipal) {
        return isAnySrmRole(metamacPrincipal);
    }

    public static boolean canDeleteConcept(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        return canCrudConceptInConceptScheme(metamacPrincipal, procStatus, type, operationCode);
    }

    public static boolean canRetrieveConceptsByConceptSchemeUrn(MetamacPrincipal metamacPrincipal) {
        return isAnySrmRole(metamacPrincipal);
    }

    public static boolean canFindConceptsByCondition(MetamacPrincipal metamacPrincipal) {
        return isAnySrmRole(metamacPrincipal);
    }

    public static boolean canAddConceptRelation(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        return canCrudConceptInConceptScheme(metamacPrincipal, procStatus, type, operationCode);
    }

    public static boolean canDeleteConceptRelation(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        return canCrudConceptInConceptScheme(metamacPrincipal, procStatus, type, operationCode);
    }

    public static boolean canRetrieveRelatedConcepts(MetamacPrincipal metamacPrincipal) {
        return isAnySrmRole(metamacPrincipal);
    }

    public static boolean canRetrieveRelatedConceptsRoles(MetamacPrincipal metamacPrincipal) {
        return isAnySrmRole(metamacPrincipal);
    }

    public static boolean canRetrieveConceptTypeByIdentifier(MetamacPrincipal metamacPrincipal) {
        return isAnySrmRole(metamacPrincipal);
    }

    public static boolean canFindAllConceptTypes(MetamacPrincipal metamacPrincipal) {
        return isAnySrmRole(metamacPrincipal);
    }

    private static boolean canUpdateConceptScheme(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        if (ProcStatusEnum.DRAFT.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isSrmRoleAllowed(metamacPrincipal, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                SrmRoleEnum[] roles = {TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_PRODUCCION, JEFE_NORMALIZACION};
                return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        } else if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isSrmRoleAllowed(metamacPrincipal, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                SrmRoleEnum[] roles = {TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_PRODUCCION, JEFE_NORMALIZACION};
                return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                SrmRoleEnum[] roles = {JEFE_NORMALIZACION, JEFE_PRODUCCION};
                return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        }
        return false;
    }

    private static boolean canCrudConceptInConceptScheme(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        if (ProcStatusEnum.DRAFT.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isSrmRoleAllowed(metamacPrincipal, TECNICO_APOYO_NORMALIZACION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                return isAnySrmRole(metamacPrincipal)
                        && isOperationAllowed(metamacPrincipal, operationCode, TECNICO_APOYO_PRODUCCION, TECNICO_APOYO_NORMALIZACION, TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_PRODUCCION,
                                JEFE_NORMALIZACION);
            }
        } else if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isSrmRoleAllowed(metamacPrincipal, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                SrmRoleEnum[] roles = {TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION, JEFE_PRODUCCION};
                return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                SrmRoleEnum[] roles = {JEFE_NORMALIZACION, JEFE_PRODUCCION};
                return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        }
        return false;
    }
}
