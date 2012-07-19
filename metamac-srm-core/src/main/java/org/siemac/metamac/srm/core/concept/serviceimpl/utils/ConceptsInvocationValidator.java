package org.siemac.metamac.srm.core.concept.serviceimpl.utils;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils;
import org.siemac.metamac.srm.core.base.serviceimpl.utils.BaseInvocationValidator;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.concept.domain.ConceptScheme;

public class ConceptsInvocationValidator {

    public static void checkFindConceptSchemeById(Long id, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(id, ServiceExceptionParameters.ID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkCreateConceptScheme(ConceptScheme conceptScheme, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkConceptScheme(conceptScheme, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateConceptScheme(ConceptScheme conceptScheme, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkConceptScheme(conceptScheme, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkDeleteConceptScheme(Long id, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(id, ServiceExceptionParameters.ID, exceptions);

        ExceptionUtils.throwIfException(exceptions);

    }

    public static void checkFindAllConceptSchemes(List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindConceptSchemeByCondition(List<ConditionalCriteria> conditions, PagingParameter pagingParameter, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    // ------------------------------------------------------------------------------------
    // CONCEPTS
    // ------------------------------------------------------------------------------------

    public static void checkFindConceptById(Long id, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(id, ServiceExceptionParameters.ID, exceptions);

        ExceptionUtils.throwIfException(exceptions);

    }

    public static void checkCreateConcept(Long conceptSchemeId, ConceptScheme entity, List<MetamacExceptionItem> exceptions) throws MetamacException {
        // TODO
    }

    public static void checkUpdateConcept(ConceptScheme entity, List<MetamacExceptionItem> exceptions) throws MetamacException {
        // TODO
    }

    public void checkDeleteConcept(Long id, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(id, ServiceExceptionParameters.ID, exceptions);

        ExceptionUtils.throwIfException(exceptions);

    }

    public static void checkFindConceptSchemeConcepts(Long conceptSchemeId, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(conceptSchemeId, ServiceExceptionParameters.CONCEPT_SCHEME_ID, exceptions);

        ExceptionUtils.throwIfException(exceptions);

    }

    private static void checkConceptScheme(ConceptScheme conceptScheme, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkParameterRequired(conceptScheme, ServiceExceptionParameters.CONCEPT_SCHEME, exceptions);
        if (conceptScheme == null) {
            return;
        }
        BaseInvocationValidator.checkItemScheme(conceptScheme.getItemScheme(), exceptions);
    }

}
