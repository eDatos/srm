package org.siemac.metamac.srm.core.concept.serviceimpl.utils;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils;
import org.siemac.metamac.srm.core.concept.domain.ConceptScheme;
import org.siemac.metamac.srm.core.error.ServiceExceptionParameters;

public class InvocationValidator {

    public static void checkFindConceptSchemeById(Long id, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(id, ServiceExceptionParameters.ID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkCreateConceptScheme(ConceptScheme entity, List<MetamacExceptionItem> exceptions) throws MetamacException {
        // TODO
    }

    public static void checkUpdateConceptScheme(ConceptScheme entity, List<MetamacExceptionItem> exceptions) throws MetamacException {
     // TODO
    }

    public static void checkDeleteConceptScheme(Long id, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(id, ServiceExceptionParameters.ID, exceptions);

        ExceptionUtils.throwIfException(exceptions);

    }

    public static void checkFindAllConceptSchemes(List<MetamacExceptionItem> exceptions) throws MetamacException {
     // TODO
    }

    public static void checkFindConceptSchemeByCondition(MetamacCriteria criteria, List<MetamacExceptionItem> exceptions) throws MetamacException {
     // TODO
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

    public static void  checkUpdateConcept(ConceptScheme entity, List<MetamacExceptionItem> exceptions) throws MetamacException {
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

}
