package org.siemac.metamac.srm.core.security;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
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

    public static void canCreateConceptScheme(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeMetamac) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canCreateConceptScheme(getMetamacPrincipal(ctx), conceptSchemeMetamac.getType(), getOperationCode(conceptSchemeMetamac))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canUpdateConceptScheme(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeMetamacOld, ConceptSchemeTypeEnum typeNew, String operationNew) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canUpdateConceptScheme(getMetamacPrincipal(ctx), conceptSchemeMetamacOld.getProcStatus(), conceptSchemeMetamacOld.getType(),
                getOperationCode(conceptSchemeMetamacOld), typeNew, operationNew)) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }
    public static void canDeleteConceptScheme(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeMetamac) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canDeleteConceptScheme(getMetamacPrincipal(ctx), conceptSchemeMetamac.getType(), getOperationCode(conceptSchemeMetamac))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canFindConceptSchemesByCondition(ServiceContext ctx) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canFindConceptSchemesByCondition(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canSendConceptSchemeToProductionValidation(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeMetamac) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canSendConceptSchemeToProductionValidation(getMetamacPrincipal(ctx), conceptSchemeMetamac.getType(), getOperationCode(conceptSchemeMetamac))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canSendConceptSchemeToDiffusionValidation(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeMetamac) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canSendConceptSchemeToDiffusionValidation(getMetamacPrincipal(ctx), conceptSchemeMetamac.getType(), getOperationCode(conceptSchemeMetamac))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canRejectConceptSchemeValidation(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeMetamac) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canRejectConceptSchemeValidation(getMetamacPrincipal(ctx), conceptSchemeMetamac.getProcStatus(), conceptSchemeMetamac.getType(),
                getOperationCode(conceptSchemeMetamac))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canPublishConceptSchemeInternally(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeMetamac) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canPublishConceptSchemeInternally(getMetamacPrincipal(ctx), conceptSchemeMetamac.getType(), getOperationCode(conceptSchemeMetamac))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canPublishConceptSchemeExternally(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeMetamac) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canPublishConceptSchemeExternally(getMetamacPrincipal(ctx), conceptSchemeMetamac.getType(), getOperationCode(conceptSchemeMetamac))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canVersioningConceptScheme(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeMetamac) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canVersioningConceptScheme(getMetamacPrincipal(ctx), conceptSchemeMetamac.getType(), getOperationCode(conceptSchemeMetamac))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canAnnounceConceptScheme(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeMetamac) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canAnnounceConceptScheme(getMetamacPrincipal(ctx), conceptSchemeMetamac.getType(), getOperationCode(conceptSchemeMetamac))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canCancelConceptSchemeValidity(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeMetamac) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canCancelConceptSchemeValidity(getMetamacPrincipal(ctx), conceptSchemeMetamac.getType(), getOperationCode(conceptSchemeMetamac))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    //
    // CONCEPTS
    //

    public static void canCreateConcept(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeMetamac) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canCreateConcept(getMetamacPrincipal(ctx), conceptSchemeMetamac.getProcStatus(), conceptSchemeMetamac.getType(), getOperationCode(conceptSchemeMetamac))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canUpdateConcept(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeMetamac) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canUpdateConcept(getMetamacPrincipal(ctx), conceptSchemeMetamac.getProcStatus(), conceptSchemeMetamac.getType(), getOperationCode(conceptSchemeMetamac))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canRetrieveConceptByUrn(ServiceContext ctx) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canRetrieveConceptByUrn(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canDeleteConcept(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeMetamac) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canDeleteConcept(getMetamacPrincipal(ctx), conceptSchemeMetamac.getProcStatus(), conceptSchemeMetamac.getType(), getOperationCode(conceptSchemeMetamac))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canRetrieveConceptsByConceptSchemeUrn(ServiceContext ctx) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canRetrieveConceptsByConceptSchemeUrn(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canAddConceptRelation(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeMetamac) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canAddConceptRelation(getMetamacPrincipal(ctx), conceptSchemeMetamac.getProcStatus(), conceptSchemeMetamac.getType(), getOperationCode(conceptSchemeMetamac))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canDeleteConceptRelation(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeMetamac) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canDeleteConceptRelation(getMetamacPrincipal(ctx), conceptSchemeMetamac.getProcStatus(), conceptSchemeMetamac.getType(),
                getOperationCode(conceptSchemeMetamac))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canRetrieveRelatedConcepts(ServiceContext ctx) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canRetrieveRelatedConcepts(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }
    
    public static void canRetrieveRelatedConceptsRoles(ServiceContext ctx) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canRetrieveRelatedConceptsRoles(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canRetrieveConceptTypeByIdentifier(ServiceContext ctx) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canRetrieveConceptTypeByIdentifier(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }
    public static void canFindAllConceptTypes(ServiceContext ctx) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canFindAllConceptTypes(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }
    //
    // OTHER
    //

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
    private static String getOperationCode(ConceptSchemeVersionMetamac conceptSchemeMetamac) {
        return conceptSchemeMetamac.getRelatedOperation() != null ? conceptSchemeMetamac.getRelatedOperation().getCode() : null;
    }
}
