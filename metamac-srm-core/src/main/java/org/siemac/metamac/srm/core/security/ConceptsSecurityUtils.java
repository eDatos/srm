package org.siemac.metamac.srm.core.security;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.security.shared.SharedConceptsSecurityUtils;
import org.siemac.metamac.sso.utils.SecurityUtils;

public class ConceptsSecurityUtils extends SecurityUtils {

    //
    // CONCEPT SCHEMES
    //

    public static void canRetrieveConceptSchemeByUrn(ServiceContext ctx) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canRetrieveConceptSchemeByUrn(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveConceptSchemeVersions(ServiceContext ctx) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canRetrieveConceptSchemeVersions(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canCreateConceptScheme(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeMetamac) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canCreateConceptScheme(getMetamacPrincipal(ctx), conceptSchemeMetamac.getType(), getOperationCode(conceptSchemeMetamac))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canUpdateConceptScheme(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeMetamacOld, ConceptSchemeTypeEnum typeNew, String operationNew) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canUpdateConceptScheme(getMetamacPrincipal(ctx), conceptSchemeMetamacOld.getLifeCycleMetadata().getProcStatus(), conceptSchemeMetamacOld.getType(),
                getOperationCode(conceptSchemeMetamacOld), typeNew, operationNew)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canDeleteConceptScheme(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeMetamac) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canDeleteConceptScheme(getMetamacPrincipal(ctx), conceptSchemeMetamac.getType(), getOperationCode(conceptSchemeMetamac))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canFindConceptSchemesByCondition(ServiceContext ctx) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canFindConceptSchemesByCondition(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canSendConceptSchemeToProductionValidation(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeMetamac) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canSendConceptSchemeToProductionValidation(getMetamacPrincipal(ctx), conceptSchemeMetamac.getType(), getOperationCode(conceptSchemeMetamac))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canSendConceptSchemeToDiffusionValidation(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeMetamac) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canSendConceptSchemeToDiffusionValidation(getMetamacPrincipal(ctx), conceptSchemeMetamac.getType(), getOperationCode(conceptSchemeMetamac))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRejectConceptSchemeValidation(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeMetamac) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canRejectConceptSchemeValidation(getMetamacPrincipal(ctx), conceptSchemeMetamac.getLifeCycleMetadata().getProcStatus(), conceptSchemeMetamac.getType(),
                getOperationCode(conceptSchemeMetamac))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canPublishConceptSchemeInternally(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeMetamac) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canPublishConceptSchemeInternally(getMetamacPrincipal(ctx), conceptSchemeMetamac.getType(), getOperationCode(conceptSchemeMetamac))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canPublishConceptSchemeExternally(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeMetamac) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canPublishConceptSchemeExternally(getMetamacPrincipal(ctx), conceptSchemeMetamac.getType(), getOperationCode(conceptSchemeMetamac))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canCopyConceptScheme(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeMetamac) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canCopyConceptScheme(getMetamacPrincipal(ctx), conceptSchemeMetamac.getType(), getOperationCode(conceptSchemeMetamac))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canVersioningConceptScheme(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeMetamac) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canVersioningConceptScheme(getMetamacPrincipal(ctx), conceptSchemeMetamac.getType(), getOperationCode(conceptSchemeMetamac))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canCreateConceptSchemeTemporalVersion(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeMetamac) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canCreateConceptSchemeTemporalVersion(getMetamacPrincipal(ctx), conceptSchemeMetamac.getType(), getOperationCode(conceptSchemeMetamac))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canAnnounceConceptScheme(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeMetamac) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canAnnounceConceptScheme(getMetamacPrincipal(ctx), conceptSchemeMetamac.getType(), getOperationCode(conceptSchemeMetamac))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canEndConceptSchemeValidity(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeMetamac) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canEndConceptSchemeValidity(getMetamacPrincipal(ctx), conceptSchemeMetamac.getType(), getOperationCode(conceptSchemeMetamac))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    //
    // CONCEPTS
    //

    public static void canCreateConcept(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeMetamac) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canCreateConcept(getMetamacPrincipal(ctx), conceptSchemeMetamac.getLifeCycleMetadata().getProcStatus(), conceptSchemeMetamac.getType(),
                getOperationCode(conceptSchemeMetamac))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canUpdateConcept(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeMetamac) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canUpdateConcept(getMetamacPrincipal(ctx), conceptSchemeMetamac.getLifeCycleMetadata().getProcStatus(), conceptSchemeMetamac.getType(),
                getOperationCode(conceptSchemeMetamac))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveConceptByUrn(ServiceContext ctx) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canRetrieveConceptByUrn(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canDeleteConcept(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeMetamac) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canDeleteConcept(getMetamacPrincipal(ctx), conceptSchemeMetamac.getLifeCycleMetadata().getProcStatus(), conceptSchemeMetamac.getType(),
                getOperationCode(conceptSchemeMetamac))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveConceptsByConceptSchemeUrn(ServiceContext ctx) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canRetrieveConceptsByConceptSchemeUrn(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canFindConceptsByCondition(ServiceContext ctx) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canFindConceptsByCondition(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canAddRelatedConcept(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeMetamac) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canAddRelatedConcept(getMetamacPrincipal(ctx), conceptSchemeMetamac.getLifeCycleMetadata().getProcStatus(), conceptSchemeMetamac.getType(),
                getOperationCode(conceptSchemeMetamac))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canAddRoleConcept(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeMetamac) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canAddRoleConcept(getMetamacPrincipal(ctx), conceptSchemeMetamac.getLifeCycleMetadata().getProcStatus(), conceptSchemeMetamac.getType(),
                getOperationCode(conceptSchemeMetamac))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canDeleteRelatedConcept(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeMetamac) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canDeleteRelatedConcept(getMetamacPrincipal(ctx), conceptSchemeMetamac.getLifeCycleMetadata().getProcStatus(), conceptSchemeMetamac.getType(),
                getOperationCode(conceptSchemeMetamac))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canDeleteRoleConcept(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeMetamac) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canDeleteRoleConcept(getMetamacPrincipal(ctx), conceptSchemeMetamac.getLifeCycleMetadata().getProcStatus(), conceptSchemeMetamac.getType(),
                getOperationCode(conceptSchemeMetamac))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveRelatedConcepts(ServiceContext ctx) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canRetrieveRelatedConcepts(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveRoleConcepts(ServiceContext ctx) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canRetrieveRoleConcepts(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveConceptTypeByIdentifier(ServiceContext ctx) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canRetrieveConceptTypeByIdentifier(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }
    public static void canFindAllConceptTypes(ServiceContext ctx) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canFindAllConceptTypes(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    // CATEGORISATIONS

    public static void canModifyCategorisation(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeMetamac) throws MetamacException {
        if (!SharedConceptsSecurityUtils.canModifyCategorisation(getMetamacPrincipal(ctx), conceptSchemeMetamac.getLifeCycleMetadata().getProcStatus(), conceptSchemeMetamac.getType(),
                getOperationCode(conceptSchemeMetamac))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    //
    // PRIVATE METHODS
    //
    private static String getOperationCode(ConceptSchemeVersionMetamac conceptSchemeMetamac) {
        return conceptSchemeMetamac.getRelatedOperation() != null ? conceptSchemeMetamac.getRelatedOperation().getCode() : null;
    }

}
