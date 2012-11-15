package org.siemac.metamac.srm.core.concept.serviceimpl.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;

import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.ValidationUtils;
import com.arte.statistic.sdmx.srm.core.concept.serviceimpl.utils.ConceptsInvocationValidator;

public class ConceptsMetamacInvocationValidator extends ConceptsInvocationValidator {

    public static void checkCreateConceptScheme(ConceptSchemeVersionMetamac conceptSchemeVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        ValidationUtils.checkParameterRequired(conceptSchemeVersion, ServiceExceptionParameters.CONCEPT_SCHEME, exceptions);
        if (conceptSchemeVersion != null) {
            checkConceptScheme(conceptSchemeVersion, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateConceptScheme(ConceptSchemeVersionMetamac conceptSchemeVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        ValidationUtils.checkParameterRequired(conceptSchemeVersion, ServiceExceptionParameters.CONCEPT_SCHEME, exceptions);
        if (conceptSchemeVersion != null) {
            checkConceptScheme(conceptSchemeVersion, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkCreateConcept(ConceptSchemeVersionMetamac conceptSchemeVersion, ConceptMetamac concept, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(conceptSchemeVersion, ServiceExceptionParameters.CONCEPT_SCHEME, exceptions);
        ValidationUtils.checkParameterRequired(concept, ServiceExceptionParameters.CONCEPT, exceptions);
        if (concept != null && conceptSchemeVersion != null) {
            checkConcept(conceptSchemeVersion, concept, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateConcept(ConceptSchemeVersionMetamac conceptSchemeVersionMetamac, ConceptMetamac concept, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(concept, ServiceExceptionParameters.CONCEPT, exceptions);
        if (concept != null) {
            checkConcept(conceptSchemeVersionMetamac, concept, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkAddRoleConcept(String urn, String conceptRoleUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(urn, ServiceExceptionParameters.URN, exceptions);
        ValidationUtils.checkParameterRequired(conceptRoleUrn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkDeleteRoleConcept(String urn, String conceptRoleUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(urn, ServiceExceptionParameters.URN, exceptions);
        ValidationUtils.checkParameterRequired(conceptRoleUrn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveRoleConcepts(String urn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(urn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkAddRelatedConcept(String urn1, String urn2, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(urn1, ServiceExceptionParameters.URN, exceptions);
        ValidationUtils.checkParameterRequired(urn2, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkDeleteRelatedConcept(String urn1, String urn2, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(urn1, ServiceExceptionParameters.URN, exceptions);
        ValidationUtils.checkParameterRequired(urn2, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveRelatedConcepts(String urn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(urn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindAllConceptTypes(List<MetamacExceptionItem> exceptions) throws MetamacException {
        // nothing
    }

    public static void checkRetrieveConceptTypeByIdentifier(String identifier, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(identifier, ServiceExceptionParameters.IDENTIFIER, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveConceptSchemeByConceptUrn(String conceptUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(conceptUrn, ServiceExceptionParameters.URN, exceptions);

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

    private static void checkConcept(ConceptSchemeVersionMetamac conceptSchemeVersion, ConceptMetamac concept, List<MetamacExceptionItem> exceptions) {

        // Not same concept scheme
        if (concept.getConceptExtends() != null) {
            if (conceptSchemeVersion.getItemScheme().getId().equals(concept.getConceptExtends().getItemSchemeVersion().getItemScheme().getId())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.CONCEPT_EXTENDS));
            }
        }
        ValidationUtils.checkMetadataOptionalIsValid(concept.getPluralName(), ServiceExceptionParameters.CONCEPT_PLURAL_NAME, exceptions);
        ValidationUtils.checkMetadataOptionalIsValid(concept.getAcronym(), ServiceExceptionParameters.CONCEPT_ACRONYM, exceptions);
        ValidationUtils.checkMetadataOptionalIsValid(concept.getDescriptionSource(), ServiceExceptionParameters.CONCEPT_DESCRIPTION_SOURCE, exceptions);
        ValidationUtils.checkMetadataOptionalIsValid(concept.getContext(), ServiceExceptionParameters.CONCEPT_CONTEXT, exceptions);
        ValidationUtils.checkMetadataOptionalIsValid(concept.getDocMethod(), ServiceExceptionParameters.CONCEPT_DOC_METHOD, exceptions);
        ValidationUtils.checkMetadataOptionalIsValid(concept.getDerivation(), ServiceExceptionParameters.CONCEPT_DERIVATION, exceptions);
        ValidationUtils.checkMetadataOptionalIsValid(concept.getLegalActs(), ServiceExceptionParameters.CONCEPT_LEGAL_ACTS, exceptions);

        if (conceptSchemeVersion != null) {
            if (ConceptSchemeTypeEnum.OPERATION.equals(conceptSchemeVersion.getType()) || ConceptSchemeTypeEnum.TRANSVERSAL.equals(conceptSchemeVersion.getType())) {
                ValidationUtils.checkMetadataRequired(concept.getSdmxRelatedArtefact(), ServiceExceptionParameters.CONCEPT_SDMX_RELATED_ARTEFACT, exceptions);
            } else {
                ValidationUtils.checkMetadataEmpty(concept.getSdmxRelatedArtefact(), ServiceExceptionParameters.CONCEPT_SDMX_RELATED_ARTEFACT, exceptions);
            }
        }

        // common metadata in sdmx are checked in Sdmx module
    }
}