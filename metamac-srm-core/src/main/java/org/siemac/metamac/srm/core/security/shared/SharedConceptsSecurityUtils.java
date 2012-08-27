package org.siemac.metamac.srm.core.security.shared;

import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.JEFE_NORMALIZACION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.JEFE_PRODUCCION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.TECNICO_APOYO_NORMALIZACION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.TECNICO_APOYO_PRODUCCION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.TECNICO_NORMALIZACION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.TECNICO_PRODUCCION;

import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.enume.domain.ItemSchemeMetamacProcStatusEnum;
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

    public static boolean canCreateConceptScheme(MetamacPrincipal metamacPrincipal) {
        // Note: when conceptScheme is Operation type, do not check if user has access to operation because all normalization roles have access to all operations
        return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
    }

    public static boolean canUpdateConceptScheme(MetamacPrincipal metamacPrincipal, ItemSchemeMetamacProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        if (ItemSchemeMetamacProcStatusEnum.DRAFT.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isSrmRoleAllowed(metamacPrincipal, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                SrmRoleEnum[] roles = {TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_PRODUCCION, JEFE_NORMALIZACION};
                return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        } else if (ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isSrmRoleAllowed(metamacPrincipal, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                SrmRoleEnum[] roles = {TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_PRODUCCION, JEFE_NORMALIZACION};
                return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        } else if (ItemSchemeMetamacProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
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

    public static boolean canRejectConceptSchemeValidation(MetamacPrincipal metamacPrincipal, ItemSchemeMetamacProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        if (ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isSrmRoleAllowed(metamacPrincipal, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                SrmRoleEnum[] roles = {TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION, JEFE_PRODUCCION};
                return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        } else if (ItemSchemeMetamacProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
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

    public static boolean canCreateConcept(MetamacPrincipal metamacPrincipal, ItemSchemeMetamacProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        if (ItemSchemeMetamacProcStatusEnum.DRAFT.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isSrmRoleAllowed(metamacPrincipal, TECNICO_APOYO_NORMALIZACION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                return isAnySrmRole(metamacPrincipal)
                        && isOperationAllowed(metamacPrincipal, operationCode, TECNICO_APOYO_PRODUCCION, TECNICO_APOYO_NORMALIZACION, TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_PRODUCCION,
                                JEFE_NORMALIZACION);
            }
        } else if (ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isSrmRoleAllowed(metamacPrincipal, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                SrmRoleEnum[] roles = {TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION, JEFE_PRODUCCION};
                return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        } else if (ItemSchemeMetamacProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                SrmRoleEnum[] roles = {JEFE_NORMALIZACION, JEFE_PRODUCCION};
                return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        }
        return false;
    }

    public static boolean canUpdateConcept(MetamacPrincipal metamacPrincipal, ItemSchemeMetamacProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        if (ItemSchemeMetamacProcStatusEnum.DRAFT.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isSrmRoleAllowed(metamacPrincipal, TECNICO_APOYO_NORMALIZACION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                return isAnySrmRole(metamacPrincipal)
                        && isOperationAllowed(metamacPrincipal, operationCode, TECNICO_APOYO_PRODUCCION, TECNICO_APOYO_NORMALIZACION, TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_PRODUCCION,
                                JEFE_NORMALIZACION);
            }
        } else if (ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isSrmRoleAllowed(metamacPrincipal, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                SrmRoleEnum[] roles = {TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION, JEFE_PRODUCCION};
                return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        } else if (ItemSchemeMetamacProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                SrmRoleEnum[] roles = {JEFE_NORMALIZACION, JEFE_PRODUCCION};
                return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        }
        return false;
    }

    public static boolean canDeleteConcept(MetamacPrincipal metamacPrincipal, ItemSchemeMetamacProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        if (ItemSchemeMetamacProcStatusEnum.DRAFT.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isSrmRoleAllowed(metamacPrincipal, TECNICO_APOYO_NORMALIZACION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                return isAnySrmRole(metamacPrincipal)
                        && isOperationAllowed(metamacPrincipal, operationCode, TECNICO_APOYO_PRODUCCION, TECNICO_APOYO_NORMALIZACION, TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_PRODUCCION,
                                JEFE_NORMALIZACION);
            }
        } else if (ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isSrmRoleAllowed(metamacPrincipal, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                SrmRoleEnum[] roles = {TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION, JEFE_PRODUCCION};
                return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        } else if (ItemSchemeMetamacProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
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
