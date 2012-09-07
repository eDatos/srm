package org.siemac.metamac.srm.core.dsd.serviceimpl.utils;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;

import com.arte.statistic.sdmx.srm.core.concept.serviceimpl.utils.ConceptsInvocationValidator;

public class DsdsMetamacInvocationValidator extends ConceptsInvocationValidator {

//    public static void checkCreateConceptScheme(ConceptSchemeVersionMetamac conceptSchemeVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
//        if (exceptions == null) {
//            exceptions = new ArrayList<MetamacExceptionItem>();
//        }
//        ValidationUtils.checkParameterRequired(conceptSchemeVersion, ServiceExceptionParameters.CONCEPT_SCHEME, exceptions);
//        if (conceptSchemeVersion == null) {
//            return;
//        }
//
//        checkConceptScheme(conceptSchemeVersion, exceptions);
//
//        ExceptionUtils.throwIfException(exceptions);
//    }
//
//    public static void checkUpdateConceptScheme(ConceptSchemeVersionMetamac conceptSchemeVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
//        if (exceptions == null) {
//            exceptions = new ArrayList<MetamacExceptionItem>();
//        }
//        ValidationUtils.checkParameterRequired(conceptSchemeVersion, ServiceExceptionParameters.CONCEPT_SCHEME, exceptions);
//        if (conceptSchemeVersion == null) {
//            return;
//        }
//
//        checkConceptScheme(conceptSchemeVersion, exceptions);
//
//        ExceptionUtils.throwIfException(exceptions);
//    }

    public static void checkSendDataStructureDefinitionToProductionValidation(String urn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        ValidationUtils.checkParameterRequired(urn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkSendDataStructureDefinitionToDiffusionValidation(String urn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        ValidationUtils.checkParameterRequired(urn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRejectDataStructureDefinitionProductionValidation(String urn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        ValidationUtils.checkParameterRequired(urn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkDataStructureDefinitionDiffusionValidation(String urn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        ValidationUtils.checkParameterRequired(urn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkPublishInternallyDataStructureDefinition(String urn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        ValidationUtils.checkParameterRequired(urn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }
}
