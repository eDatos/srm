package org.siemac.metamac.srm.core.concept.serviceimpl.utils;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;

import com.arte.statistic.sdmx.srm.core.concept.serviceimpl.utils.ConceptsInvocationValidator;

public class ConceptsMetamacInvocationValidator extends ConceptsInvocationValidator {

    public static void checkCreateConceptScheme(ConceptSchemeVersionMetamac conceptSchemeVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        ValidationUtils.checkParameterRequired(conceptSchemeVersion, ServiceExceptionParameters.CONCEPT_SCHEME, exceptions);
        if (conceptSchemeVersion == null) {
            return;
        }

        ValidationUtils.checkMetadataRequired(conceptSchemeVersion.getType(), ServiceExceptionParameters.CONCEPT_SCHEME_TYPE, exceptions);
        if (ConceptSchemeTypeEnum.OPERATION.equals(conceptSchemeVersion.getType())) {
            ValidationUtils.checkMetadataRequired(conceptSchemeVersion.getRelatedOperation(), ServiceExceptionParameters.CONCEPT_SCHEME_RELATED_OPERATION, exceptions);
        } else {
            ValidationUtils.checkMetadataEmpty(conceptSchemeVersion.getRelatedOperation(), ServiceExceptionParameters.CONCEPT_SCHEME_RELATED_OPERATION, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateConceptScheme(ConceptSchemeVersionMetamac conceptSchemeVersion, List<MetamacExceptionItem> exceptionItems) throws MetamacException {
        // TODO
    }

    public static void checkSendConceptSchemeToProductionValidation(String urn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        ValidationUtils.checkParameterRequired(urn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }
}
