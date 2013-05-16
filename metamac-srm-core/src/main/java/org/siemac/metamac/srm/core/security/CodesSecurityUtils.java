package org.siemac.metamac.srm.core.security;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.security.shared.SharedCodesSecurityUtils;

public class CodesSecurityUtils extends CommonSecurityUtils {

    // CODELISTS

    public static void canRetrieveCodelistByUrn(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canRetrieveCodelistByUrn(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveCodelistVersions(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canRetrieveCodelistVersions(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canCreateCodelist(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canCreateCodelist(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canUpdateCodelist(ServiceContext ctx, ProcStatusEnum procStatus) throws MetamacException {
        if (!SharedCodesSecurityUtils.canUpdateCodelist(getMetamacPrincipal(ctx), procStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }
    public static void canDeleteCodelist(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canDeleteCodelist(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canFindCodelistsByCondition(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canFindCodelistsByCondition(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canSendCodelistToProductionValidation(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canSendCodelistToProductionValidation(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canSendCodelistToDiffusionValidation(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canSendCodelistToDiffusionValidation(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRejectCodelistValidation(ServiceContext ctx, ProcStatusEnum procStatus) throws MetamacException {
        if (!SharedCodesSecurityUtils.canRejectCodelistValidation(getMetamacPrincipal(ctx), procStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canPublishCodelistInternally(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canPublishCodelistInternally(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canPublishCodelistExternally(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canPublishCodelistExternally(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canCopyCodelist(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canCopyCodelist(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canVersioningCodelist(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canVersioningCodelist(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canCreateCodelistTemporalVersion(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canCreateCodelistTemporalVersion(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canEndCodelistValidity(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canEndCodelistValidity(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    // ITEMS

    public static void canCreateCode(ServiceContext ctx, ProcStatusEnum codelistProcStatus) throws MetamacException {
        if (!SharedCodesSecurityUtils.canCreateCode(getMetamacPrincipal(ctx), codelistProcStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canUpdateCode(ServiceContext ctx, ProcStatusEnum codelistProcStatus) throws MetamacException {
        if (!SharedCodesSecurityUtils.canUpdateCode(getMetamacPrincipal(ctx), codelistProcStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canDeleteCode(ServiceContext ctx, CodelistVersionMetamac codelistVersion) throws MetamacException {
        if (!SharedCodesSecurityUtils.canModifyCodeFromCodelist(getMetamacPrincipal(ctx), codelistVersion.getLifeCycleMetadata().getProcStatus())) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveCodeByUrn(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canRetrieveCodeByUrn(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveCodesByCodelistUrn(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canRetrieveCodesByCodelistUrn(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canFindCodesByCondition(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canFindCodesByCondition(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canNormaliseVariableElementsToCodes(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canNormaliseVariableElementsToCodes(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    // CATEGORISATIONS

    public static void canModifyCategorisation(ServiceContext ctx, CodelistVersionMetamac codelistVersion) throws MetamacException {
        if (!SharedCodesSecurityUtils.canModifyCategorisation(getMetamacPrincipal(ctx), codelistVersion.getLifeCycleMetadata().getProcStatus())) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    // VISUALISATIONS

    public static void canRetrieveOrFindCodelistOrderVisualisation(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canRetrieveOrFindCodelistOrderVisualisation(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canCrudCodelistOrderVisualisation(ServiceContext ctx, ProcStatusEnum codelistProcStatus) throws MetamacException {
        if (!SharedCodesSecurityUtils.canCrudCodelistOrderVisualisation(getMetamacPrincipal(ctx), codelistProcStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveOrFindCodelistOpennessVisualisation(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canRetrieveOrFindCodelistOpennessVisualisation(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canCrudCodelistOpennessVisualisation(ServiceContext ctx, ProcStatusEnum codelistProcStatus) throws MetamacException {
        if (!SharedCodesSecurityUtils.canCrudCodelistOpennessVisualisation(getMetamacPrincipal(ctx), codelistProcStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    // VARIABLES, FAMILIES...

    public static void canRetrieveOrFindCodelistFamily(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canRetrieveOrFindCodelistFamily(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canCrudCodelistFamily(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canCrudCodelistFamily(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveOrFindVariableFamily(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canRetrieveOrFindVariableFamily(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canCrudVariableFamily(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canCrudVariableFamily(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveOrFindVariable(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canRetrieveOrFindVariable(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canCrudVariable(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canCrudVariable(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveOrFindVariableElement(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canRetrieveOrFindVariableElement(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canCrudVariableElement(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canCrudVariableElement(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canUpdateCodeVariableElement(ServiceContext ctx, ProcStatusEnum procStatus) throws MetamacException {
        if (!SharedCodesSecurityUtils.canUpdateCodeVariableElement(getMetamacPrincipal(ctx), procStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canAddVariablesToVariableFamily(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canAddVariablesToVariableFamily(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRemoveVariableFromVariableFamily(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canRemoveVariableFromVariableFamily(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canAddVariableElementsToVariable(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canAddVariableElementsToVariable(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    // IMPORTATIONS

    public static void canImportCodelistOrderVisualisations(ServiceContext ctx, ProcStatusEnum codelistProcStatus) throws MetamacException {
        if (!SharedCodesSecurityUtils.canImportCodelistOrderVisualisations(getMetamacPrincipal(ctx), codelistProcStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canImportCodes(ServiceContext ctx, ProcStatusEnum codelistProcStatus) throws MetamacException {
        if (!SharedCodesSecurityUtils.canImportCodes(getMetamacPrincipal(ctx), codelistProcStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canImportVariableElements(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canImportVariableElements(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    // EXPORTATIONS

    public static void canExportCodesTsv(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canExportCodesTsv(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canExportCodeOrdersTsv(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canExportCodeOrdersTsv(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }
}
