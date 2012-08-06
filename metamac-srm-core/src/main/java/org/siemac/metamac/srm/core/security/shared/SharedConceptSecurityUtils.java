package org.siemac.metamac.srm.core.security.shared;

import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.ANY_ROLE_ALLOWED;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.JEFE_NORMALIZACION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.JEFE_PRODUCCION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.TECNICO_APOYO_NORMALIZACION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.TECNICO_NORMALIZACION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.TECNICO_PRODUCCION;

import org.siemac.metamac.srm.core.enume.domain.MaintainableArtefactProcStatusEnum;
import org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum;
import org.siemac.metamac.sso.client.MetamacPrincipal;

import com.arte.statistic.sdmx.v2_1.domain.enume.concept.domain.ConceptSchemeTypeEnum;

public class SharedConceptSecurityUtils extends SharedSecurityUtils {

    // Schemes

    public static boolean canCreateConceptScheme(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
    }

    public static boolean canUpdateConceptScheme(MetamacPrincipal metamacPrincipal, MaintainableArtefactProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        if (MaintainableArtefactProcStatusEnum.DRAFT.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isSrmRoleAllowed(metamacPrincipal, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                SrmRoleEnum[] roles = {TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_PRODUCCION, JEFE_NORMALIZACION};
                return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        } else if (MaintainableArtefactProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isSrmRoleAllowed(metamacPrincipal, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                SrmRoleEnum[] roles = {TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_PRODUCCION, JEFE_NORMALIZACION};
                return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        } else if (MaintainableArtefactProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                SrmRoleEnum[] roles = {JEFE_NORMALIZACION, JEFE_PRODUCCION};
                return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        }
        return false;
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

    public static boolean canSendConceptSchemeToProductionValidation(MetamacPrincipal metamacPrincipal, ConceptSchemeTypeEnum type, String operationCode) {
        if (isNonOperationConceptSchemeType(type)) {
            return isSrmRoleAllowed(metamacPrincipal, TECNICO_APOYO_NORMALIZACION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
        } else if (isOperationConceptSchemeType(type)) {
            return isAnySrmRole(metamacPrincipal) && isOperationAllowed(metamacPrincipal, operationCode, ANY_ROLE_ALLOWED);
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

    public static boolean canRejectConceptSchemeValidation(MetamacPrincipal metamacPrincipal, MaintainableArtefactProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        if (MaintainableArtefactProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isSrmRoleAllowed(metamacPrincipal, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                SrmRoleEnum[] roles = {TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION, JEFE_PRODUCCION};
                return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        } else if (MaintainableArtefactProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                SrmRoleEnum[] roles = {JEFE_NORMALIZACION};
                return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        }
        return false;
    }

    public static boolean canPublishInternallyConceptScheme(MetamacPrincipal metamacPrincipal, ConceptSchemeTypeEnum type, String operationCode) {
        if (isNonOperationConceptSchemeType(type)) {
            return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
        } else if (isOperationConceptSchemeType(type)) {
            SrmRoleEnum[] roles = {JEFE_NORMALIZACION};
            return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
        }
        return false;
    }

    public static boolean canPublishExternallyConceptScheme(MetamacPrincipal metamacPrincipal, ConceptSchemeTypeEnum type, String operationCode) {
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

    public static boolean canCancelConceptSchemeValidity(MetamacPrincipal metamacPrincipal, ConceptSchemeTypeEnum type, String operationCode) {
        if (isNonOperationConceptSchemeType(type)) {
            return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
        } else if (isOperationConceptSchemeType(type)) {
            SrmRoleEnum roles[] = {JEFE_NORMALIZACION, JEFE_PRODUCCION};
            return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
        }
        return false;
    }

    // Concepts

    public static boolean canCreateConcept(MetamacPrincipal metamacPrincipal, MaintainableArtefactProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        if (MaintainableArtefactProcStatusEnum.DRAFT.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isSrmRoleAllowed(metamacPrincipal, TECNICO_APOYO_NORMALIZACION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                return isAnySrmRole(metamacPrincipal) && isOperationAllowed(metamacPrincipal, operationCode, ANY_ROLE_ALLOWED);
            }
        } else if (MaintainableArtefactProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isSrmRoleAllowed(metamacPrincipal, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                SrmRoleEnum[] roles = {TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION, JEFE_PRODUCCION};
                return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        } else if (MaintainableArtefactProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                SrmRoleEnum[] roles = {JEFE_NORMALIZACION, JEFE_PRODUCCION};
                return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        }
        return false;
    }

    public static boolean canUpdateConcept(MetamacPrincipal metamacPrincipal, MaintainableArtefactProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        if (MaintainableArtefactProcStatusEnum.DRAFT.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isSrmRoleAllowed(metamacPrincipal, TECNICO_APOYO_NORMALIZACION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                return isAnySrmRole(metamacPrincipal) && isOperationAllowed(metamacPrincipal, operationCode, ANY_ROLE_ALLOWED);
            }
        } else if (MaintainableArtefactProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isSrmRoleAllowed(metamacPrincipal, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                SrmRoleEnum[] roles = {TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION, JEFE_PRODUCCION};
                return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        } else if (MaintainableArtefactProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                SrmRoleEnum[] roles = {JEFE_NORMALIZACION, JEFE_PRODUCCION};
                return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        }
        return false;
    }

    public static boolean canDeleteConcept(MetamacPrincipal metamacPrincipal, MaintainableArtefactProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        if (MaintainableArtefactProcStatusEnum.DRAFT.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isSrmRoleAllowed(metamacPrincipal, TECNICO_APOYO_NORMALIZACION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                return isAnySrmRole(metamacPrincipal) && isOperationAllowed(metamacPrincipal, operationCode, ANY_ROLE_ALLOWED);
            }
        } else if (MaintainableArtefactProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isSrmRoleAllowed(metamacPrincipal, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                SrmRoleEnum[] roles = {TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION, JEFE_PRODUCCION};
                return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        } else if (MaintainableArtefactProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
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
