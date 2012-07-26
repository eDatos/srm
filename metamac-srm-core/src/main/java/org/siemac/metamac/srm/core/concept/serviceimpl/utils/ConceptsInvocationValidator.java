package org.siemac.metamac.srm.core.concept.serviceimpl.utils;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils;
import org.siemac.metamac.domain.concept.enums.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.base.serviceimpl.utils.BaseInvocationValidator;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersion;

public class ConceptsInvocationValidator {

    public static void checkCreateConceptScheme(ConceptSchemeVersion conceptSchemeVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkConceptScheme(conceptSchemeVersion, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindConceptSchemeByUrn(String urn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(urn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkDeleteConceptScheme(String urn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(urn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void retrieveConceptSchemeVersions(String urn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(urn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindConceptSchemeByCondition(List<ConditionalCriteria> conditions, PagingParameter pagingParameter, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    // TODO nombre de los parámetros de las excepciones? ¿poner conceptScheme en lugar del genérico itemScheme?
    private static void checkConceptScheme(ConceptSchemeVersion conceptSchemeVersion, List<MetamacExceptionItem> exceptions) {

        // Common metadata of item scheme
        BaseInvocationValidator.checkItemScheme(conceptSchemeVersion, exceptions);

        // Metadata of concept scheme
        if (conceptSchemeVersion == null) {
            return;
        }
        if (ConceptSchemeTypeEnum.OPERATION.equals(conceptSchemeVersion.getType())) {
            ValidationUtils.checkMetadataRequired(conceptSchemeVersion.getRelatedOperation(), ServiceExceptionParameters.CONCEPT_SCHEME_RELATED_OPERATION, exceptions);
        } else {
            ValidationUtils.checkMetadataEmpty(conceptSchemeVersion.getRelatedOperation(), ServiceExceptionParameters.CONCEPT_SCHEME_RELATED_OPERATION, exceptions);
        }
        if (conceptSchemeVersion.getMaintainableArtefact() != null) {
            ValidationUtils.checkMetadataEmpty(conceptSchemeVersion.getMaintainableArtefact().getUri(), ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_URI, exceptions);
        }
    }
}
