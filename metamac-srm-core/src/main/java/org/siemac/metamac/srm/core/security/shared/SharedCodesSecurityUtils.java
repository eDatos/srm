package org.siemac.metamac.srm.core.security.shared;

import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.JEFE_NORMALIZACION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.TECNICO_APOYO_NORMALIZACION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.TECNICO_NORMALIZACION;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.sso.client.MetamacPrincipal;

public class SharedCodesSecurityUtils extends SharedItemsSecurityUtils {

    public static boolean canRetrieveCodelistByUrn(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canFindCodelistsByCondition(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canRetrieveCodelistVersions(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canCreateCodelist(MetamacPrincipal metamacPrincipal) {
        return canCreateItemScheme(metamacPrincipal);
    }

    public static boolean canUpdateCodelist(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus) {
        return canUpdateItemScheme(metamacPrincipal, procStatus);
    }

    public static boolean canDeleteCodelist(MetamacPrincipal metamacPrincipal) {
        return canDeleteItemScheme(metamacPrincipal);
    }

    public static boolean canSendCodelistToProductionValidation(MetamacPrincipal metamacPrincipal) {
        return canSendItemSchemeToProductionValidation(metamacPrincipal);
    }

    public static boolean canSendCodelistToDiffusionValidation(MetamacPrincipal metamacPrincipal) {
        return canSendItemSchemeToDiffusionValidation(metamacPrincipal);
    }

    public static boolean canRejectCodelistValidation(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus) {
        return canRejectItemSchemeValidation(metamacPrincipal, procStatus);
    }

    public static boolean canPublishCodelistInternally(MetamacPrincipal metamacPrincipal) {
        return canPublishItemSchemeInternally(metamacPrincipal);
    }

    public static boolean canPublishCodelistExternally(MetamacPrincipal metamacPrincipal) {
        return canPublishItemSchemeExternally(metamacPrincipal);
    }

    public static boolean canCopyCodelist(MetamacPrincipal metamacPrincipal) {
        return canCopyItemScheme(metamacPrincipal);
    }

    public static boolean canVersioningCodelist(MetamacPrincipal metamacPrincipal) {
        return canVersioningItemScheme(metamacPrincipal);
    }

    public static boolean canCreateCodelistTemporalVersion(MetamacPrincipal metamacPrincipal) {
        return canVersioningCodelist(metamacPrincipal);
    }

    public static boolean canEndCodelistValidity(MetamacPrincipal metamacPrincipal) {
        return canEndItemSchemeValidity(metamacPrincipal);
    }

    public static boolean canRetrieveCodeByUrn(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canRetrieveCodesByCodelistUrn(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canModifyCodeFromCodelist(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus) {
        return canModifyItemFromItemScheme(metamacPrincipal, procStatus);
    }

    public static boolean canFindCodesByCondition(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canNormaliseVariableElementsToCodes(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canRetrieveOrFindCodelistOrderVisualisation(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canCrudCodelistOrderVisualisation(MetamacPrincipal metamacPrincipal, ProcStatusEnum codelistProcStatus) {
        return canUpdateItemScheme(metamacPrincipal, codelistProcStatus);
    }

    public static boolean canImportCodelistOrderVisualisations(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus) {
        return canUpdateCode(metamacPrincipal, procStatus);
    }

    public static boolean canImportCodes(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus) {
        return canCreateCode(metamacPrincipal, procStatus);
    }

    public static boolean canImportVariableElements(MetamacPrincipal metamacPrincipal) {
        return canCrudVariableElement(metamacPrincipal);
    }

    public static boolean canExportCodesTsv(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canExportCodeOrdersTsv(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canRetrieveOrFindCodelistOpennessVisualisation(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canCreateCode(MetamacPrincipal metamacPrincipal, ProcStatusEnum codelistProcStatus) {
        return canModifyItemFromItemScheme(metamacPrincipal, codelistProcStatus);
    }

    public static boolean canUpdateCode(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus) {
        return canModifyItemFromItemScheme(metamacPrincipal, procStatus);
    }

    public static boolean canCrudCodelistOpennessVisualisation(MetamacPrincipal metamacPrincipal, ProcStatusEnum codelistProcStatus) {
        return canUpdateItemScheme(metamacPrincipal, codelistProcStatus);
    }

    public static boolean canRetrieveOrFindCodelistFamily(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canCrudCodelistFamily(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
    }

    public static boolean canRetrieveOrFindVariableFamily(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canCrudVariableFamily(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
    }

    public static boolean canRetrieveOrFindVariable(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canCrudVariable(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
    }

    public static boolean canRetrieveOrFindVariableElement(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canCrudVariableElement(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, TECNICO_APOYO_NORMALIZACION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
    }

    public static boolean canUpdateCodeVariableElement(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus) {
        if (ProcStatusEnum.INTERNALLY_PUBLISHED.equals(procStatus) || ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
        } else {
            return canModifyItemFromItemScheme(metamacPrincipal, procStatus);
        }
    }

    public static boolean canAddVariablesToVariableFamily(MetamacPrincipal metamacPrincipal) {
        return canCrudVariableFamily(metamacPrincipal) && canCrudVariable(metamacPrincipal);
    }

    public static boolean canRemoveVariableFromVariableFamily(MetamacPrincipal metamacPrincipal) {
        return canCrudVariableFamily(metamacPrincipal) && canCrudVariable(metamacPrincipal);
    }

    public static boolean canAddVariableElementsToVariable(MetamacPrincipal metamacPrincipal) {
        return canCrudVariable(metamacPrincipal) && canCrudVariableElement(metamacPrincipal);
    }
}