package org.siemac.metamac.srm.core.concept.serviceimpl.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
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

        checkConceptScheme(conceptSchemeVersion, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateConceptScheme(ConceptSchemeVersionMetamac conceptSchemeVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        ValidationUtils.checkParameterRequired(conceptSchemeVersion, ServiceExceptionParameters.CONCEPT_SCHEME, exceptions);
        if (conceptSchemeVersion == null) {
            return;
        }

        checkConceptScheme(conceptSchemeVersion, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkSendConceptSchemeToProductionValidation(String urn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        ValidationUtils.checkParameterRequired(urn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkSendConceptSchemeToDiffusionValidation(String urn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        ValidationUtils.checkParameterRequired(urn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRejectConceptSchemeProductionValidation(String urn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        ValidationUtils.checkParameterRequired(urn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRejectConceptSchemeDiffusionValidation(String urn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        ValidationUtils.checkParameterRequired(urn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkPublishInternallyConceptScheme(String urn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        ValidationUtils.checkParameterRequired(urn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkPublishExternallyConceptScheme(String urn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        ValidationUtils.checkParameterRequired(urn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkCancelConceptSchemeValidity(String urn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        ValidationUtils.checkParameterRequired(urn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkCreateConcept(ConceptSchemeVersionMetamac conceptSchemeVersion, ConceptMetamac concept, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(conceptSchemeVersion, ServiceExceptionParameters.CONCEPT_SCHEME, exceptions);
        if (conceptSchemeVersion != null) {
            if (ConceptSchemeTypeEnum.OPERATION.equals(conceptSchemeVersion.getType()) || ConceptSchemeTypeEnum.TRANSVERSAL.equals(conceptSchemeVersion.getType())) {
                ValidationUtils.checkMetadataRequired(conceptSchemeVersion, ServiceExceptionParameters.CONCEPT_SDMX_RELATED_ARTEFACT, exceptions);
            } else {
                ValidationUtils.checkMetadataEmpty(conceptSchemeVersion, ServiceExceptionParameters.CONCEPT_SDMX_RELATED_ARTEFACT, exceptions);
            }
        }

        ValidationUtils.checkParameterRequired(concept, ServiceExceptionParameters.CONCEPT, exceptions);
        if (concept == null) {
            return;
        }
        checkConcept(concept, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    private static void checkConceptScheme(ConceptSchemeVersionMetamac conceptSchemeVersion, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkMetadataRequired(conceptSchemeVersion.getType(), ServiceExceptionParameters.CONCEPT_SCHEME_TYPE, exceptions);
        if (ConceptSchemeTypeEnum.OPERATION.equals(conceptSchemeVersion.getType())) {
            ValidationUtils.checkMetadataRequired(conceptSchemeVersion.getRelatedOperation(), ServiceExceptionParameters.CONCEPT_SCHEME_RELATED_OPERATION, exceptions);
        } else {
            ValidationUtils.checkMetadataEmpty(conceptSchemeVersion.getRelatedOperation(), ServiceExceptionParameters.CONCEPT_SCHEME_RELATED_OPERATION, exceptions);
        }
        if (conceptSchemeVersion.getMaintainableArtefact() != null && BooleanUtils.isTrue(conceptSchemeVersion.getMaintainableArtefact().getIsExternalReference())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.MAINTAINABLE_ARTEFACT_IS_EXTERNAL_REFERENCE));
        }
    }

    private static void checkConcept(ConceptMetamac concept, List<MetamacExceptionItem> exceptions) {
    }
}
