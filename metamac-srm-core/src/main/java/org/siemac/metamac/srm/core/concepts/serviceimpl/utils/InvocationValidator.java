package org.siemac.metamac.srm.core.concepts.serviceimpl.utils;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils;
import org.siemac.metamac.srm.core.concept.domain.Concept;
import org.siemac.metamac.srm.core.concept.domain.ConceptScheme;

public class InvocationValidator {

    // ------------------------------------------------------------------------------------
    // CONCEPT SCHEMES
    // ------------------------------------------------------------------------------------

    public static void checkFindConceptSchemeById(Long id, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(id, "ID", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }


    public static void checkFindAllConceptSchemes(List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // nothing to validate

        ExceptionUtils.throwIfException(exceptions);

    }

    public static void checkCreateConceptScheme(ConceptScheme conceptScheme, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkConceptScheme(conceptScheme, exceptions);
        ValidationUtils.checkMetadataEmpty(conceptScheme.getId(), "CONCEPT_SCHEME.ID", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateConceptScheme(ConceptScheme conceptScheme, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkConceptScheme(conceptScheme, exceptions);
        ValidationUtils.checkMetadataRequired(conceptScheme.getId(), "CONCEPT_SCHEME.ID", exceptions);
        ValidationUtils.checkMetadataRequired(conceptScheme.getUuid(), "CONCEPT_SCHEME.UUID", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkDeleteConceptScheme(Long id, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(id, "ID", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindConceptSchemeByCondition(List<ConditionalCriteria> conditions, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // nothing to validate

        ExceptionUtils.throwIfException(exceptions);

    }

    // ------------------------------------------------------------------------------------
    // PRIVATE METHODS
    // ------------------------------------------------------------------------------------
    private static void checkConceptScheme(ConceptScheme conceptScheme, List<MetamacExceptionItem> exceptions) {
        // ValidationUtils.checkParameterRequired(role, "ROLE", exceptions);
        // ValidationUtils.checkMetadataRequired(role.getCode(), "ROLE.CODE", exceptions);
        // ValidationUtils.checkMetadataRequired(role.getTitle(), "ROLE.TITLE", exceptions);
    }

    private static void checkConcept(Concept concept, List<MetamacExceptionItem> exceptions) {
        // ValidationUtils.checkParameterRequired(app, "APP", exceptions);
        // ValidationUtils.checkMetadataRequired(app.getCode(), "APP.CODE", exceptions);
        // ValidationUtils.checkMetadataRequired(app.getTitle(), "APP.TITLE", exceptions);
    }
}
