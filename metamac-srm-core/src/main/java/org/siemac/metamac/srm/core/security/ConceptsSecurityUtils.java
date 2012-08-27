package org.siemac.metamac.srm.core.security;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.enume.domain.ItemSchemeMetamacProcStatusEnum;
import org.siemac.metamac.srm.core.security.shared.SharedConceptsSecurityUtils;
import org.siemac.metamac.sso.client.MetamacPrincipal;
import org.siemac.metamac.sso.client.SsoClientConstants;

public class ConceptsSecurityUtils {

    //
    // CONCEPT SCHEMES
    //

    public static void canRetrieveConceptSchemeByUrn(ServiceContext ctx) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canRetrieveConceptSchemeByUrn(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canRetrieveConceptSchemeVersions(ServiceContext ctx) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canRetrieveConceptSchemeVersions(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canCreateConceptScheme(ServiceContext ctx) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canCreateConceptScheme(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canUpdateConceptScheme(ServiceContext ctx, ConceptSchemeMetamacDto conceptSchemeMetamacDto) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canUpdateConceptScheme(getMetamacPrincipal(ctx), getProcStatus(conceptSchemeMetamacDto), getType(conceptSchemeMetamacDto),
                getOperacionCode(conceptSchemeMetamacDto))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canDeleteConceptScheme(ServiceContext ctx, ConceptSchemeMetamacDto conceptSchemeMetamacDto) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canDeleteConceptScheme(getMetamacPrincipal(ctx), getType(conceptSchemeMetamacDto), getOperacionCode(conceptSchemeMetamacDto))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canFindConceptSchemesByCondition(ServiceContext ctx) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canFindConceptSchemesByCondition(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canSendConceptSchemeToProductionValidation(ServiceContext ctx, ConceptSchemeMetamacDto conceptSchemeMetamacDto) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canSendConceptSchemeToProductionValidation(getMetamacPrincipal(ctx), getType(conceptSchemeMetamacDto), getOperacionCode(conceptSchemeMetamacDto))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canSendConceptSchemeToDiffusionValidation(ServiceContext ctx, ConceptSchemeMetamacDto conceptSchemeMetamacDto) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canSendConceptSchemeToDiffusionValidation(getMetamacPrincipal(ctx), getType(conceptSchemeMetamacDto), getOperacionCode(conceptSchemeMetamacDto))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canRejectConceptSchemeValidation(ServiceContext ctx, ConceptSchemeMetamacDto conceptSchemeMetamacDto) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canRejectConceptSchemeValidation(getMetamacPrincipal(ctx), getProcStatus(conceptSchemeMetamacDto), getType(conceptSchemeMetamacDto),
                getOperacionCode(conceptSchemeMetamacDto))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canPublishConceptSchemeInternally(ServiceContext ctx, ConceptSchemeMetamacDto conceptSchemeMetamacDto) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canPublishConceptSchemeInternally(getMetamacPrincipal(ctx), getType(conceptSchemeMetamacDto), getOperacionCode(conceptSchemeMetamacDto))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canPublishConceptSchemeExternally(ServiceContext ctx, ConceptSchemeMetamacDto conceptSchemeMetamacDto) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canPublishConceptSchemeExternally(getMetamacPrincipal(ctx), getType(conceptSchemeMetamacDto), getOperacionCode(conceptSchemeMetamacDto))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canVersioningConceptScheme(ServiceContext ctx, ConceptSchemeMetamacDto conceptSchemeMetamacDto) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canVersioningConceptScheme(getMetamacPrincipal(ctx), getType(conceptSchemeMetamacDto), getOperacionCode(conceptSchemeMetamacDto))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canAnnounceConceptScheme(ServiceContext ctx, ConceptSchemeMetamacDto conceptSchemeMetamacDto) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canAnnounceConceptScheme(getMetamacPrincipal(ctx), getType(conceptSchemeMetamacDto), getOperacionCode(conceptSchemeMetamacDto))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canCancelConceptSchemeValidity(ServiceContext ctx, ConceptSchemeMetamacDto conceptSchemeMetamacDto) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canCancelConceptSchemeValidity(getMetamacPrincipal(ctx), getType(conceptSchemeMetamacDto), getOperacionCode(conceptSchemeMetamacDto))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    /**
     * Retrieves MetamacPrincipal in ServiceContext
     */
    public static MetamacPrincipal getMetamacPrincipal(ServiceContext ctx) throws MetamacException {
        Object principalProperty = ctx.getProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE);
        if (principalProperty == null) {
            throw new MetamacException(ServiceExceptionType.SECURITY_PRINCIPAL_NOT_FOUND);
        }
        MetamacPrincipal metamacPrincipal = (MetamacPrincipal) principalProperty;
        if (!metamacPrincipal.getUserId().equals(ctx.getUserId())) {
            throw new MetamacException(ServiceExceptionType.SECURITY_PRINCIPAL_NOT_FOUND);
        }
        return metamacPrincipal;
    }

    //
    // PRIVATE METHODS
    //

    private static ItemSchemeMetamacProcStatusEnum getProcStatus(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        return conceptSchemeMetamacDto != null ? conceptSchemeMetamacDto.getProcStatus() : null;
    }

    private static ConceptSchemeTypeEnum getType(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        return conceptSchemeMetamacDto != null ? conceptSchemeMetamacDto.getType() : null;
    }

    private static String getOperacionCode(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        return conceptSchemeMetamacDto != null && conceptSchemeMetamacDto.getRelatedOperation() != null ? conceptSchemeMetamacDto.getRelatedOperation().getCode() : null;
    }

}
