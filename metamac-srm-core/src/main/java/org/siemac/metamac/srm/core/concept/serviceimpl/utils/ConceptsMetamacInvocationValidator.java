package org.siemac.metamac.srm.core.concept.serviceimpl.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.core.common.exception.CommonServiceExceptionType;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.core.common.util.TimeUtils;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.service.utils.SrmValidationUtils;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.Quantity;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.concept.serviceimpl.utils.shared.QuantityUtils;

import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.ValidationUtils;
import com.arte.statistic.sdmx.srm.core.concept.serviceimpl.utils.ConceptsInvocationValidator;

public class ConceptsMetamacInvocationValidator extends ConceptsInvocationValidator {

    public static void checkCreateConceptScheme(ConceptSchemeVersionMetamac conceptSchemeVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        ValidationUtils.checkParameterRequired(conceptSchemeVersion, ServiceExceptionParameters.CONCEPT_SCHEME, exceptions);
        if (conceptSchemeVersion != null) {
            checkConceptScheme(conceptSchemeVersion, true, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateConceptScheme(ConceptSchemeVersionMetamac conceptSchemeVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        ValidationUtils.checkParameterRequired(conceptSchemeVersion, ServiceExceptionParameters.CONCEPT_SCHEME, exceptions);
        if (conceptSchemeVersion != null) {
            checkConceptScheme(conceptSchemeVersion, false, exceptions);
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
            checkConcept(conceptSchemeVersion, concept, true, true, true, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateConcept(ConceptSchemeVersionMetamac conceptSchemeVersionMetamac, ConceptMetamac concept, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(concept, ServiceExceptionParameters.CONCEPT, exceptions);
        if (concept != null) {
            checkConcept(conceptSchemeVersionMetamac, concept, false, true, true, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveConceptsByConceptSchemeUrn(String conceptSchemeUrn, String locale, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(conceptSchemeUrn, ServiceExceptionParameters.URN, exceptions);
        ValidationUtils.checkParameterRequired(locale, ServiceExceptionParameters.LOCALE, exceptions);

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

    public static void checkConceptScheme(ConceptSchemeVersionMetamac conceptSchemeVersion, boolean creating, List<MetamacExceptionItem> exceptions) {

        if (SrmValidationUtils.mustValidateMetadataRequired(conceptSchemeVersion, creating)) {
            ValidationUtils.checkMetadataRequired(conceptSchemeVersion.getType(), ServiceExceptionParameters.CONCEPT_SCHEME_TYPE, exceptions);
            if (ConceptSchemeTypeEnum.OPERATION.equals(conceptSchemeVersion.getType())) {
                ValidationUtils.checkMetadataRequired(conceptSchemeVersion.getRelatedOperation(), ServiceExceptionParameters.CONCEPT_SCHEME_RELATED_OPERATION, exceptions);
                if (conceptSchemeVersion.getRelatedOperation() != null) {
                    // urn in ExternalItems is optional, but it is required for statistical operation
                    ValidationUtils.checkMetadataRequired(conceptSchemeVersion.getRelatedOperation().getUrn(), ServiceExceptionParameters.CONCEPT_SCHEME_RELATED_OPERATION, exceptions);
                    ValidationUtils.checkMetadataEmpty(conceptSchemeVersion.getRelatedOperation().getUrnInternal(), ServiceExceptionParameters.CONCEPT_SCHEME_RELATED_OPERATION, exceptions);
                }
            } else if (ConceptSchemeTypeEnum.MEASURE.equals(conceptSchemeVersion.getType())) {
                // relatedOperation is optional
            } else {
                ValidationUtils.checkMetadataEmpty(conceptSchemeVersion.getRelatedOperation(), ServiceExceptionParameters.CONCEPT_SCHEME_RELATED_OPERATION, exceptions);
            }
            if (conceptSchemeVersion.getRelatedOperation() != null) {
                // title of operation can change and it can not be saved
                ValidationUtils.checkMetadataEmpty(conceptSchemeVersion.getRelatedOperation().getTitle(), ServiceExceptionParameters.CONCEPT_SCHEME_RELATED_OPERATION_TITLE, exceptions);
            }
        }

        if (conceptSchemeVersion.getId() != null) {
            ValidationUtils.checkMetadataRequired(conceptSchemeVersion.getIsTypeUpdated(), ServiceExceptionParameters.CONCEPT_SCHEME_IS_TYPE_UPDATED, exceptions);
        }

        if (conceptSchemeVersion.getMaintainableArtefact() != null && BooleanUtils.isTrue(conceptSchemeVersion.getMaintainableArtefact().getIsExternalReference())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.MAINTAINABLE_ARTEFACT_IS_EXTERNAL_REFERENCE));
        }
        if (BooleanUtils.isTrue(conceptSchemeVersion.getIsPartial())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.ITEM_SCHEME_IS_PARTIAL));
        }
    }

    /**
     * @param checkOptionalInternationalStrings False to avoid extra queries. When publishing, international strings are checked with native queries
     * @param checkQuantity False to avoid extra queries. Quantity is checked when create or update concept
     */
    public static void checkConcept(ConceptSchemeVersionMetamac conceptSchemeVersion, ConceptMetamac concept, boolean creating, boolean checkOptionalInternationalStrings, boolean checkQuantity,
            List<MetamacExceptionItem> exceptions) {

        if (checkOptionalInternationalStrings) {
            ValidationUtils.checkMetadataOptionalIsValid(concept.getPluralName(), ServiceExceptionParameters.CONCEPT_PLURAL_NAME, exceptions);
            ValidationUtils.checkMetadataOptionalIsValid(concept.getAcronym(), ServiceExceptionParameters.CONCEPT_ACRONYM, exceptions);
            ValidationUtils.checkMetadataOptionalIsValid(concept.getDescriptionSource(), ServiceExceptionParameters.CONCEPT_DESCRIPTION_SOURCE, exceptions);
            ValidationUtils.checkMetadataOptionalIsValid(concept.getContext(), ServiceExceptionParameters.CONCEPT_CONTEXT, exceptions);
            ValidationUtils.checkMetadataOptionalIsValid(concept.getDocMethod(), ServiceExceptionParameters.CONCEPT_DOC_METHOD, exceptions);
            ValidationUtils.checkMetadataOptionalIsValid(concept.getDerivation(), ServiceExceptionParameters.CONCEPT_DERIVATION, exceptions);
            ValidationUtils.checkMetadataOptionalIsValid(concept.getLegalActs(), ServiceExceptionParameters.CONCEPT_LEGAL_ACTS, exceptions);
        }

        // Quantity
        if (checkQuantity) {
            checkConceptQuantity(concept.getQuantity(), exceptions);
        }

        // note: variable is optional. If enumerated representation is codelist, Service will assign automatically to concept the variable of codelist

        if (conceptSchemeVersion != null) {
            if (SrmValidationUtils.mustValidateMetadataRequired(conceptSchemeVersion, creating)) {
                ValidationUtils.checkMetadataRequired(conceptSchemeVersion.getType(), ServiceExceptionParameters.CONCEPT_SCHEME_TYPE, exceptions);
                // Sdmx related artefact
                if (ConceptSchemeTypeEnum.OPERATION.equals(conceptSchemeVersion.getType()) || ConceptSchemeTypeEnum.TRANSVERSAL.equals(conceptSchemeVersion.getType())
                        || ConceptSchemeTypeEnum.MEASURE.equals(conceptSchemeVersion.getType())) {
                    ValidationUtils.checkMetadataRequired(concept.getSdmxRelatedArtefact(), ServiceExceptionParameters.CONCEPT_SDMX_RELATED_ARTEFACT, exceptions);
                } else {
                    ValidationUtils.checkMetadataEmpty(concept.getSdmxRelatedArtefact(), ServiceExceptionParameters.CONCEPT_SDMX_RELATED_ARTEFACT, exceptions);
                }
                // Variable TODO para qué tipos?
                if (ConceptSchemeTypeEnum.OPERATION.equals(conceptSchemeVersion.getType()) || ConceptSchemeTypeEnum.TRANSVERSAL.equals(conceptSchemeVersion.getType())) {
                    // variable is optional
                } else {
                    ValidationUtils.checkMetadataEmpty(concept.getVariable(), ServiceExceptionParameters.CONCEPT_VARIABLE, exceptions);
                }
            }
        }

        // common metadata in sdmx are checked in Sdmx module
    }

    private static void checkConceptQuantity(Quantity quantity, List<MetamacExceptionItem> exceptions) {

        if (ValidationUtils.isEmpty(quantity)) {
            // it is optional
            return;
        }

        // checks invalid
        if (!ValidationUtils.isEmpty(quantity.getUnitMultiplier())) {
            if (quantity.getUnitMultiplier().intValue() != 1 && quantity.getUnitMultiplier().intValue() % 10 != 0) {
                // must be 1, 10, 100...
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.CONCEPT_QUANTITY_UNIT_MULTIPLIER));
            }
        }
        if (!ValidationUtils.isEmpty(quantity.getBaseTime()) && !TimeUtils.isTimeValue(quantity.getBaseTime())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.CONCEPT_QUANTITY_BASE_TIME));
        }
        // checks required
        ValidationUtils.checkMetadataRequired(quantity.getQuantityType(), ServiceExceptionParameters.CONCEPT_QUANTITY_TYPE, exceptions);
        ValidationUtils.checkMetadataRequired(quantity.getUnitCode(), ServiceExceptionParameters.CONCEPT_QUANTITY_UNIT_CODE, exceptions);
        ValidationUtils.checkMetadataRequired(quantity.getUnitSymbolPosition(), ServiceExceptionParameters.CONCEPT_QUANTITY_UNIT_SYMBOL_POSITION, exceptions);
        ValidationUtils.checkMetadataRequired(quantity.getUnitMultiplier(), ServiceExceptionParameters.CONCEPT_QUANTITY_UNIT_MULTIPLIER, exceptions);
        // required by quantity type
        if (QuantityUtils.isRatioOrExtension(quantity.getQuantityType())) {
            ValidationUtils.checkMetadataRequired(quantity.getIsPercentage(), ServiceExceptionParameters.CONCEPT_QUANTITY_IS_PERCENTAGE, exceptions);
        }
        if (QuantityUtils.isIndexOrExtension(quantity.getQuantityType())) {
            if (ValidationUtils.isEmpty(quantity.getBaseValue()) && ValidationUtils.isEmpty(quantity.getBaseTime()) && ValidationUtils.isEmpty(quantity.getBaseLocation())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.CONCEPT_QUANTITY_BASE_VALUE,
                        ServiceExceptionParameters.CONCEPT_QUANTITY_BASE_TIME, ServiceExceptionParameters.CONCEPT_QUANTITY_BASE_LOCATION));
            }
        }

        // Quantity: checks unexpected
        if (!QuantityUtils.isAmountOrExtension(quantity.getQuantityType())) {
            // nothing
        }
        if (!QuantityUtils.isMagnitudeOrExtension(quantity.getQuantityType())) {
            ValidationUtils.checkMetadataEmpty(quantity.getMinimum(), ServiceExceptionParameters.CONCEPT_QUANTITY_MIN, exceptions);
            ValidationUtils.checkMetadataEmpty(quantity.getMaximum(), ServiceExceptionParameters.CONCEPT_QUANTITY_MAX, exceptions);
        }
        if (!QuantityUtils.isFractionOrExtension(quantity.getQuantityType())) {
            ValidationUtils.checkMetadataEmpty(quantity.getNumerator(), ServiceExceptionParameters.CONCEPT_QUANTITY_NUMERATOR, exceptions);
            ValidationUtils.checkMetadataEmpty(quantity.getDenominator(), ServiceExceptionParameters.CONCEPT_QUANTITY_DENOMINATOR, exceptions);
        }
        if (!QuantityUtils.isRatioOrExtension(quantity.getQuantityType())) {
            ValidationUtils.checkMetadataEmpty(quantity.getIsPercentage(), ServiceExceptionParameters.CONCEPT_QUANTITY_IS_PERCENTAGE, exceptions);
            ValidationUtils.checkMetadataEmpty(quantity.getPercentageOf(), ServiceExceptionParameters.CONCEPT_QUANTITY_PERCENTAGE_OF, exceptions);
        }
        if (!QuantityUtils.isRateOrExtension(quantity.getQuantityType())) {
            // nothing
        }
        if (!QuantityUtils.isIndexOrExtension(quantity.getQuantityType())) {
            ValidationUtils.checkMetadataEmpty(quantity.getBaseValue(), ServiceExceptionParameters.CONCEPT_QUANTITY_BASE_VALUE, exceptions);
            ValidationUtils.checkMetadataEmpty(quantity.getBaseTime(), ServiceExceptionParameters.CONCEPT_QUANTITY_BASE_TIME, exceptions);
            ValidationUtils.checkMetadataEmpty(quantity.getBaseLocation(), ServiceExceptionParameters.CONCEPT_QUANTITY_BASE_LOCATION, exceptions);
        } else {
            // must be filled only one of followings
            if (!ValidationUtils.isEmpty(quantity.getBaseValue())) {
                ValidationUtils.checkMetadataEmpty(quantity.getBaseTime(), ServiceExceptionParameters.CONCEPT_QUANTITY_BASE_TIME, exceptions);
                ValidationUtils.checkMetadataEmpty(quantity.getBaseLocation(), ServiceExceptionParameters.CONCEPT_QUANTITY_BASE_LOCATION, exceptions);
            } else if (!ValidationUtils.isEmpty(quantity.getBaseTime())) {
                ValidationUtils.checkMetadataEmpty(quantity.getBaseValue(), ServiceExceptionParameters.CONCEPT_QUANTITY_BASE_VALUE, exceptions);
                ValidationUtils.checkMetadataEmpty(quantity.getBaseLocation(), ServiceExceptionParameters.CONCEPT_QUANTITY_BASE_LOCATION, exceptions);
            } else if (!ValidationUtils.isEmpty(quantity.getBaseLocation())) {
                ValidationUtils.checkMetadataEmpty(quantity.getBaseTime(), ServiceExceptionParameters.CONCEPT_QUANTITY_BASE_TIME, exceptions);
                ValidationUtils.checkMetadataEmpty(quantity.getBaseValue(), ServiceExceptionParameters.CONCEPT_QUANTITY_BASE_VALUE, exceptions);
            }
        }
        if (!QuantityUtils.isChangeRateOrExtension(quantity.getQuantityType())) {
            ValidationUtils.checkMetadataEmpty(quantity.getBaseQuantity(), ServiceExceptionParameters.CONCEPT_QUANTITY_BASE_QUANTITY, exceptions);
        }
    }

    public static void checkFindCodelistsCanBeEnumeratedRepresentationForConceptByCondition(List<ConditionalCriteria> conditions, PagingParameter pagingParameter, String conceptUrn,
            String variableUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        if (conceptUrn == null && variableUrn == null) {
            exceptions.add(new MetamacExceptionItem(CommonServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.URN));
        }

        ExceptionUtils.throwIfException(exceptions);
    }
}