package org.siemac.metamac.srm.core.structure.serviceimpl.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.ent.domain.ExternalItem;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.ExceptionLevelEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils;
import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum;
import org.siemac.metamac.srm.core.base.domain.Component;
import org.siemac.metamac.srm.core.base.domain.ComponentList;
import org.siemac.metamac.srm.core.base.domain.EnumeratedRepresentation;
import org.siemac.metamac.srm.core.base.domain.Facet;
import org.siemac.metamac.srm.core.base.domain.Representation;
import org.siemac.metamac.srm.core.base.domain.TextFormatRepresentation;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParametersInternal;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.structure.domain.AttributeDescriptor;
import org.siemac.metamac.srm.core.structure.domain.AttributeRelationship;
import org.siemac.metamac.srm.core.structure.domain.DataAttribute;
import org.siemac.metamac.srm.core.structure.domain.DataStructureDefinition;
import org.siemac.metamac.srm.core.structure.domain.Dimension;
import org.siemac.metamac.srm.core.structure.domain.DimensionDescriptor;
import org.siemac.metamac.srm.core.structure.domain.DimensionRelationship;
import org.siemac.metamac.srm.core.structure.domain.GroupDimensionDescriptor;
import org.siemac.metamac.srm.core.structure.domain.GroupRelationship;
import org.siemac.metamac.srm.core.structure.domain.MeasureDescriptor;
import org.siemac.metamac.srm.core.structure.domain.MeasureDimension;
import org.siemac.metamac.srm.core.structure.domain.NoSpecifiedRelationship;
import org.siemac.metamac.srm.core.structure.domain.PrimaryMeasure;
import org.siemac.metamac.srm.core.structure.domain.PrimaryMeasureRelationship;
import org.siemac.metamac.srm.core.structure.domain.ReportingYearStartDay;
import org.siemac.metamac.srm.core.structure.domain.TimeDimension;
import org.siemac.metamac.srm.core.structure.serviceimpl.utils.shared.DataStructureConstraintSharedValidator;

public class DataStructureConstraintValidator {

    public static void checkConstraintDsdGrouping(DataStructureDefinition dataStructureDefinition, ComponentList componentList, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // Check cardinals constraints in the relationship "grouping"
        List<ComponentList> groupDimDescFound = new ArrayList<ComponentList>();
        ComponentList dimensionDescFound = null;
        ComponentList measureDescFound = null;
        ComponentList attributeDescFound = null;

        for (ComponentList currentComponentList : dataStructureDefinition.getGrouping()) {
            if ((currentComponentList instanceof AttributeDescriptor) && (componentList instanceof AttributeDescriptor)) {
                attributeDescFound = currentComponentList;
            } else if ((currentComponentList instanceof DimensionDescriptor) && (componentList instanceof DimensionDescriptor)) {
                dimensionDescFound = currentComponentList;
            } else if ((currentComponentList instanceof GroupDimensionDescriptor) && (componentList instanceof GroupDimensionDescriptor)) {
                groupDimDescFound.add(currentComponentList);
            } else if ((currentComponentList instanceof MeasureDescriptor) && (componentList instanceof MeasureDescriptor)) {
                measureDescFound = currentComponentList;
            }
        }

        // Minimum cardinality not checked in this moment. Only MAX is checked.
        if (componentList instanceof AttributeDescriptor) {
            // Cardinality 0..1
            if (attributeDescFound != null) {
                // If is new or different
                if (componentList.getId() == null || componentList.getId().compareTo(attributeDescFound.getId()) != 0) {
                    // Exception
                    MetamacException metamacException = new MetamacException(ServiceExceptionType.SRM_VALIDATION_CONSTRAINT_CARDINALITY_MAX,
                            ServiceExceptionParameters.COMPONENT_LIST_ATTRIBUTE_DESCRIPTOR); // "Max attribute descriptor is 1"
                    metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
                    throw metamacException;
                }
            }
        } else if (componentList instanceof DimensionDescriptor) {
            // Cardinality 1
            if (dimensionDescFound != null) {
                // If is new or different
                if (componentList.getId() == null || componentList.getId().compareTo(dimensionDescFound.getId()) != 0) {
                    // Exception
                    MetamacException metamacException = new MetamacException(ServiceExceptionType.SRM_VALIDATION_CONSTRAINT_CARDINALITY_MAX,
                            ServiceExceptionParameters.COMPONENT_LIST_DIMENSION_DESCRIPTOR); // "Max dimension descriptor is 1"
                    metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
                    throw metamacException;
                }
            }
        } else if (componentList instanceof MeasureDescriptor) {
            // Cardinality 1
            if (measureDescFound != null) {
                // If is new or different
                if (componentList.getId() == null || componentList.getId().compareTo(measureDescFound.getId()) != 0) {
                    // Exception
                    MetamacException metamacException = new MetamacException(ServiceExceptionType.SRM_VALIDATION_CONSTRAINT_CARDINALITY_MAX,
                            ServiceExceptionParameters.COMPONENT_LIST_MEASURE_DESCRIPTOR); // "Max measure descriptor is 1"
                    metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
                    throw metamacException;
                }
            }
        } else if (componentList instanceof GroupDimensionDescriptor) {
            // Cardinality 0..*
        }
    }

    public static void checkComponent(Component component, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // "ConceptIdentity" type="common:ConceptReferenceType"
        if (component.getCptIdRef() == null) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.COMPONENT_CONCEPT_ID_REF));
        } else {
            if (!TypeExternalArtefactsEnum.CONCEPT.equals(component.getCptIdRef().getType())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.COMPONENT_CONCEPT_ID_REF));
            }
        }

        // LocalRepresentation
        if (component instanceof DataAttribute) {
            checkDataAttribute((DataAttribute) component, exceptions);
        }
        if (component instanceof ReportingYearStartDay) {
            checkReportingYearStartDay((ReportingYearStartDay) component, exceptions);
        } else if (component instanceof Dimension) {
            checkDimension((Dimension) component, exceptions);
        } else if (component instanceof MeasureDimension) {
            checkMeasureDimension((MeasureDimension) component, exceptions);
        } else if (component instanceof TimeDimension) {
            checkTimeDimension((TimeDimension) component, exceptions);
        } else if (component instanceof PrimaryMeasure) {
            checkPrimaryMeasure((PrimaryMeasure) component, exceptions);
        }
    }

    public static void checkDsdConstraintsToPublish(DataStructureDefinition dataStructureDefinition, List<MetamacExceptionItem> exceptions) throws MetamacException {
        // Check Minimum cardinals constraints in the relationship "grouping", Maximum cardinals was checked in creation time
        boolean minimunDimension = false;
        boolean minimunMeasure = false;
        for (ComponentList currentComponentList : dataStructureDefinition.getGrouping()) {
            if (currentComponentList instanceof DimensionDescriptor) {
                minimunDimension = true;
            } else if (currentComponentList instanceof MeasureDescriptor) {
                minimunMeasure = true;
            }
        }

        if (!minimunDimension) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.SRM_VALIDATION_CONSTRAINT_CARDINALITY_MIN, ServiceExceptionParameters.COMPONENT_LIST_DIMENSION_DESCRIPTOR));
        }

        if (!minimunMeasure) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.SRM_VALIDATION_CONSTRAINT_CARDINALITY_MIN, ServiceExceptionParameters.COMPONENT_LIST_MEASURE_DESCRIPTOR));
        }

        // The rest of constraints was checked in creation time.
    }

    /**************************************************************************
     * PRIVATE
     *************************************************************************/
    private static void checkDataAttribute(DataAttribute dataAttribute, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (dataAttribute == null) {
            return;
        }

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // AssignmentStatus
        ValidationUtils.checkMetadataRequired(dataAttribute.getUsageStatus(), ServiceExceptionParameters.DATA_ATTRIBUTE_USAGE_STATES, exceptions);

        // ConceptRole
        // <xs:element name="ConceptRole" type="common:ConceptReferenceType" minOccurs="0" maxOccurs="unbounded">
        for (ExternalItem externalItem : dataAttribute.getRole()) {
            if (!TypeExternalArtefactsEnum.CONCEPT.equals(externalItem.getType())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.DATA_ATTRIBUTE_ROLE));
            }
        }

        // AttributeRelationshipType: minOccurs="1"
        if (dataAttribute.getRelateTo() == null) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.DATA_ATTRIBUTE_RELATE_TO));
        } else {
            validateAttributeRelationshipType(dataAttribute.getRelateTo(), exceptions);
        }

        // LocalRepresentation: minOccurs="0"
        validateSimpleDataStructureRepresentationType(dataAttribute.getLocalRepresentation(), ServiceExceptionParameters.DATA_ATTRIBUTE, exceptions);
    }

    private static void checkReportingYearStartDay(ReportingYearStartDay reportingYearStartDay, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (reportingYearStartDay == null) {
            return;
        }

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        // AssignmentStatus
        ValidationUtils.checkMetadataRequired(reportingYearStartDay.getUsageStatus(), ServiceExceptionParameters.DATA_ATTRIBUTE_USAGE_STATES, exceptions);

        // AttributeRelationshipType: minOccurs="1"
        if (reportingYearStartDay.getRelateTo() == null) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.DATA_ATTRIBUTE_RELATE_TO));
        } else {
            validateAttributeRelationshipType(reportingYearStartDay.getRelateTo(), exceptions);
        }

        // LocalRepresentation: minOccurs="1"
        if (reportingYearStartDay.getLocalRepresentation() == null) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.DATA_ATTRIBUTE_RELATE_TO));
        } else {
            validateReportingYearStartDayRepresentationType(reportingYearStartDay.getLocalRepresentation(), exceptions);
        }
    }

    private static void checkDimension(Dimension dimension, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (dimension == null) {
            return;
        }

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // ConceptRole
        // <xs:element name="ConceptRole" type="common:ConceptReferenceType" minOccurs="0" maxOccurs="unbounded">
        for (ExternalItem externalItem : dimension.getRole()) {
            if (!TypeExternalArtefactsEnum.CONCEPT.equals(externalItem.getType())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.DIMENSION_ROLE));
            }
        }

        // LocalRepresentation: minOccurs="0"
        validateSimpleDataStructureRepresentationType(dimension.getLocalRepresentation(), ServiceExceptionParameters.DIMENSION, exceptions);
    }

    private static void checkMeasureDimension(MeasureDimension measureDimension, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (measureDimension == null) {
            return;
        }

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // ConceptRole
        // <xs:element name="ConceptRole" type="common:ConceptReferenceType" minOccurs="0" maxOccurs="unbounded">
        for (ExternalItem externalItem : measureDimension.getRole()) {
            if (!TypeExternalArtefactsEnum.CONCEPT.equals(externalItem.getType())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.MEASURE_DIMENSION_ROLE));
            }
        }

        // LocalRepresentation: minOccurs="1"
        if (measureDimension.getLocalRepresentation() == null) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.MEASURE_DIMENSION + ServiceExceptionParametersInternal.REPRESENTATION));
        } else {
            validateMeasureDimensionRepresentationType(measureDimension.getLocalRepresentation(), exceptions);
        }
    }

    private static void checkTimeDimension(TimeDimension timeDimension, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (timeDimension == null) {
            return;
        }

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // LocalRepresentation: minOccurs="1"
        if (timeDimension.getLocalRepresentation() == null) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.TIME_DIMENSION + ServiceExceptionParametersInternal.REPRESENTATION));
        } else {
            validateTimeDimensionRepresentationType(timeDimension.getLocalRepresentation(), exceptions);
        }
    }

    private static void checkPrimaryMeasure(PrimaryMeasure primaryMeasure, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (primaryMeasure == null) {
            return;
        }

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // LocalRepresentation: minOccurs="0"
        validateSimpleDataStructureRepresentationType(primaryMeasure.getLocalRepresentation(), ServiceExceptionParameters.PRIMARY_MEASURE, exceptions);
    }

    private static void validateAttributeRelationshipType(AttributeRelationship attributeRelationship, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (attributeRelationship == null) {
            return;
        }

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        if (attributeRelationship instanceof DimensionRelationship) {
            if (((DimensionRelationship) attributeRelationship).getDimension().isEmpty() && ((DimensionRelationship) attributeRelationship).getGroupKey().isEmpty()) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.DATA_ATTRIBUTE_RELATE_TO
                        + ServiceExceptionParametersInternal.ATTRIBUTE_RELATIONSHIP_TYPE_DIMENSION));
            }
        } else if (attributeRelationship instanceof GroupRelationship) {
            ValidationUtils.checkMetadataRequired(((GroupRelationship) attributeRelationship).getGroupKey(), ServiceExceptionParameters.DATA_ATTRIBUTE_RELATE_TO
                    + ServiceExceptionParametersInternal.ATTRIBUTE_RELATIONSHIP_TYPE_GROUP, exceptions);
        } else if (attributeRelationship instanceof NoSpecifiedRelationship) {
            ; // Nothing
        } else if (attributeRelationship instanceof PrimaryMeasureRelationship) {
            ; // Nothing
        }

    }

    private static void validateSimpleDataStructureRepresentationType(Representation representation, String metadataName, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (representation == null) {
            return;
        }

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // Representation: Choice between Enumerated(Enumeration + EnumerationFormat) and NonEnumerated (TextFormat)
        if (representation instanceof TextFormatRepresentation) {
            // minOccurs="1"
            if (((TextFormatRepresentation) representation).getNonEnumerated() == null) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, metadataName + ServiceExceptionParametersInternal.REPRESENTATION_NON_ENUMERATED));
            } else {
                checkSimpleComponentTextFormatType(((TextFormatRepresentation) representation).getNonEnumerated(), metadataName, exceptions);
            }
        } else if (representation instanceof EnumeratedRepresentation) {
            // minOccurs="1"
            // Enumeration:: common:CodelistReferenceType
            if (((EnumeratedRepresentation) representation).getEnumerated() == null) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, metadataName + ServiceExceptionParametersInternal.REPRESENTATION_ENUMERATED));
            } else {
                if (!TypeExternalArtefactsEnum.CODELIST.equals(((EnumeratedRepresentation) representation).getEnumerated().getType())) {
                    exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParametersInternal.REPRESENTATION_ENUMERATED));
                }
            }

            // minOccurs="0"
            // EnumerationFormat:: CodededTextFormatType
            // TODO: METAMAC NOT SUPPORT THIS --> checkCodededTextFormatType(representationDto);
        }
    }

    private static void validateReportingYearStartDayRepresentationType(Representation representation, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (representation == null) {
            return;
        }

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // Representation: Choice between Enumerated(Enumeration + EnumerationFormat) and NonEnumerated (TextFormat)
        if (representation instanceof TextFormatRepresentation) {
            // minOccurs="1"
            if (((TextFormatRepresentation) representation).getNonEnumerated() == null) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.TIME_DIMENSION
                        + ServiceExceptionParametersInternal.REPRESENTATION_NON_ENUMERATED));
            } else {
                checkNonFacetedTextFormatType(((TextFormatRepresentation) representation).getNonEnumerated(), ServiceExceptionParameters.TIME_DIMENSION
                        + ServiceExceptionParametersInternal.REPRESENTATION_NON_ENUMERATED, exceptions);
            }

            // FacetValueType must be fixed to "MonthDay"
            if (!FacetValueTypeEnum.MONTH_DAY_FVT.equals(((TextFormatRepresentation) representation).getNonEnumerated().getFacetValue())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_UNEXPECTED, ServiceExceptionParameters.REPORTING_YEAR_START_DAY
                        + ServiceExceptionParametersInternal.FACET_VALUE_TYPE_ENUM));
            }
        } else if (representation instanceof EnumeratedRepresentation) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.PARAMETER_INCORRECT, ServiceExceptionParameters.REPORTING_YEAR_START_DAY
                    + ServiceExceptionParametersInternal.REPRESENTATION_ENUMERATED));
        }
    }

    private static void validateMeasureDimensionRepresentationType(Representation representation, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (representation == null) {
            return;
        }

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // Representation: Choice between Enumerated(Enumeration + EnumerationFormat) and NonEnumerated (TextFormat)
        if (representation instanceof TextFormatRepresentation) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.PARAMETER_INCORRECT, ServiceExceptionParameters.MEASURE_DIMENSION
                    + ServiceExceptionParametersInternal.REPRESENTATION_NON_ENUMERATED));
        } else if (representation instanceof EnumeratedRepresentation) {
            // minOccurs="1"
            // Enumeration:: common:ConceptSchemeReferenceType
            if (((EnumeratedRepresentation) representation).getEnumerated() == null) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.MEASURE_DIMENSION
                        + ServiceExceptionParametersInternal.REPRESENTATION_ENUMERATED));
            } else {
                if (!TypeExternalArtefactsEnum.CONCEPT_SCHEME.equals(((EnumeratedRepresentation) representation).getEnumerated().getType())) {
                    exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.MEASURE_DIMENSION
                            + ServiceExceptionParametersInternal.REPRESENTATION_ENUMERATED));
                }
            }
        }
    }

    private static void validateTimeDimensionRepresentationType(Representation representation, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (representation == null) {
            return;
        }

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // Representation: Choice between Enumerated(Enumeration + EnumerationFormat) and NonEnumerated (TextFormat)
        if (representation instanceof TextFormatRepresentation) {
            // minOccurs="1"
            if (((TextFormatRepresentation) representation).getNonEnumerated() == null) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.TIME_DIMENSION
                        + ServiceExceptionParametersInternal.REPRESENTATION_NON_ENUMERATED));
            } else {
                checkTimeTextFormatType(((TextFormatRepresentation) representation).getNonEnumerated(), ServiceExceptionParameters.TIME_DIMENSION, exceptions);
            }
        } else if (representation instanceof EnumeratedRepresentation) {
            exceptions
                    .add(new MetamacExceptionItem(ServiceExceptionType.PARAMETER_INCORRECT, ServiceExceptionParameters.TIME_DIMENSION + ServiceExceptionParametersInternal.REPRESENTATION_ENUMERATED));
        }
    }

    private static void checkSimpleComponentTextFormatType(Facet facet, String metadataName, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (facet == null) {
            return;
        }

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // Check for restrictions
        // FacetValueType:
        if (facet.getFacetValue() == null) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, metadataName + ServiceExceptionParametersInternal.FACET_VALUE_TYPE_ENUM));
        } else {
            checkSimpleDataType(facet.getFacetValue(), metadataName);
        }

        // FacetType --

        // IsSequenceFT
        if (StringUtils.isNotEmpty(facet.getIsSequenceFT()) && !CoreCommonUtil.matchStringToPattern(facet.getIsSequenceFT(), CoreCommonUtil.PATTERN_XSD_BOOLEAN)) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, metadataName + ServiceExceptionParametersInternal.FACET_IS_SEQUENCE_FT));
        }

        // IntervalFT
        if (StringUtils.isNotEmpty(facet.getIntervalFT()) && !CoreCommonUtil.matchStringToPattern(facet.getIntervalFT(), CoreCommonUtil.PATTERN_XSD_INTEGER)) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, metadataName + ServiceExceptionParametersInternal.FACET_INTERVAL_FT));
        }

        // StartValueFT
        if (StringUtils.isNotEmpty(facet.getStartValueFT()) && !CoreCommonUtil.matchStringToPattern(facet.getStartValueFT(), CoreCommonUtil.PATTERN_XSD_INTEGER)) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, metadataName + ServiceExceptionParametersInternal.FACET_START_VALUE_FT));
        }

        // EndValueFT
        if (StringUtils.isNotEmpty(facet.getEndValueFT()) && !CoreCommonUtil.matchStringToPattern(facet.getEndValueFT(), CoreCommonUtil.PATTERN_XSD_INTEGER)) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, metadataName + ServiceExceptionParametersInternal.FACET_END_VALUE_FT));
        }

        // TimeInterval
        if (StringUtils.isNotEmpty(facet.getTimeIntervalFT()) && !CoreCommonUtil.matchStringToPattern(facet.getTimeIntervalFT(), CoreCommonUtil.PATTERN_XSD_DURATION)) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, metadataName + ServiceExceptionParametersInternal.FACET_TIME_INTERVAL_FT));
        }

        // startTime
        if (StringUtils.isNotEmpty(facet.getStartTimeFT()) && !CoreCommonUtil.matchStringToPattern(facet.getStartTimeFT(), CoreCommonUtil.PATTERN_SDMXL21_STANDARD_TIME_PERIOD)) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, metadataName + ServiceExceptionParametersInternal.FACET_START_TIME_FT));
        }

        // endTime
        if (StringUtils.isNotEmpty(facet.getEndTimeFT()) && !CoreCommonUtil.matchStringToPattern(facet.getEndTimeFT(), CoreCommonUtil.PATTERN_SDMXL21_STANDARD_TIME_PERIOD)) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, metadataName + ServiceExceptionParametersInternal.FACET_END_TIME_FT));
        }

        // minLength
        if (StringUtils.isNotEmpty(facet.getMinLengthFT()) && !CoreCommonUtil.matchStringToPattern(facet.getMinLengthFT(), CoreCommonUtil.PATTERN_XSD_POSITIVEINTEGER)) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, metadataName + ServiceExceptionParametersInternal.FACET_MIN_LENGTH_FT));
        }

        // maxLength
        if (StringUtils.isNotEmpty(facet.getMaxLengthFT()) && !CoreCommonUtil.matchStringToPattern(facet.getMaxLengthFT(), CoreCommonUtil.PATTERN_XSD_POSITIVEINTEGER)) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, metadataName + ServiceExceptionParametersInternal.FACET_MAX_LENGTH_FT));
        }

        // minValue
        if (StringUtils.isNotEmpty(facet.getMinValueFT()) && !CoreCommonUtil.matchStringToPattern(facet.getMinValueFT(), CoreCommonUtil.PATTERN_XSD_INTEGER)) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, metadataName + ServiceExceptionParametersInternal.FACET_MIN_VALUE_FT));
        }

        // maxValue
        if (StringUtils.isNotEmpty(facet.getMaxValueFT()) && !CoreCommonUtil.matchStringToPattern(facet.getMaxValueFT(), CoreCommonUtil.PATTERN_XSD_INTEGER)) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, metadataName + ServiceExceptionParametersInternal.FACET_MAX_VALUE_FT));
        }

        // decimals
        if (StringUtils.isNotEmpty(facet.getDecimalsFT()) && !CoreCommonUtil.matchStringToPattern(facet.getDecimalsFT(), CoreCommonUtil.PATTERN_XSD_POSITIVEINTEGER)) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, metadataName + ServiceExceptionParametersInternal.FACET_DECIMALS_FT));
        }

        // pattern
        // ... nothing ...

        // Prohibited
        if (StringUtils.isNotEmpty(facet.getIsMultiLingual()) && StringUtils.isNotEmpty(facet.getIsMultiLingual())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_UNEXPECTED, metadataName + ServiceExceptionParametersInternal.FACET_IS_MULTI_LINGUAL));
        }
        if (StringUtils.isNotEmpty(facet.getXhtmlEFT()) && StringUtils.isNotEmpty(facet.getXhtmlEFT())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_UNEXPECTED, metadataName + ServiceExceptionParametersInternal.FACET_XHTML_EFT));
        }
    }

    private static void checkNonFacetedTextFormatType(Facet facet, String metadataName, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (facet == null) {
            return;
        }

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // Check for restrictions
        if (facet != null) {
            // FacetValueType
            if (facet.getFacetValue() == null) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, metadataName + ServiceExceptionParametersInternal.FACET_VALUE_TYPE_ENUM));
            } else {
                checkSimpleDataType(facet.getFacetValue(), metadataName);
            }

            // FacetType --
            // NonFacetedTextFormatType is a restricted version of the SimpleComponentTextFormatType that does not allow for any facets.

            // IsSequenceFT
            if (StringUtils.isNotEmpty(facet.getIsSequenceFT())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_UNEXPECTED, metadataName + ServiceExceptionParametersInternal.FACET_IS_SEQUENCE_FT));
            }

            // IntervalFT
            if (StringUtils.isNotEmpty(facet.getIntervalFT())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, metadataName + ServiceExceptionParametersInternal.FACET_INTERVAL_FT));
            }

            // StartValueFT
            if (StringUtils.isNotEmpty(facet.getStartValueFT())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, metadataName + ServiceExceptionParametersInternal.FACET_START_VALUE_FT));
            }

            // EndValueFT
            if (StringUtils.isNotEmpty(facet.getEndValueFT())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, metadataName + ServiceExceptionParametersInternal.FACET_END_VALUE_FT));
            }

            // TimeInterval
            if (StringUtils.isNotEmpty(facet.getTimeIntervalFT())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, metadataName + ServiceExceptionParametersInternal.FACET_TIME_INTERVAL_FT));
            }

            // startTime
            if (StringUtils.isNotEmpty(facet.getStartTimeFT())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, metadataName + ServiceExceptionParametersInternal.FACET_START_TIME_FT));
            }

            // endTime
            if (StringUtils.isNotEmpty(facet.getEndTimeFT())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, metadataName + ServiceExceptionParametersInternal.FACET_END_TIME_FT));
            }

            // minLength
            if (StringUtils.isNotEmpty(facet.getMinLengthFT())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, metadataName + ServiceExceptionParametersInternal.FACET_MIN_LENGTH_FT));
            }

            // maxLength
            if (StringUtils.isNotEmpty(facet.getMaxLengthFT())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, metadataName + ServiceExceptionParametersInternal.FACET_MAX_LENGTH_FT));
            }

            // minValue
            if (StringUtils.isNotEmpty(facet.getMinValueFT())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, metadataName + ServiceExceptionParametersInternal.FACET_MIN_VALUE_FT));
            }

            // maxValue
            if (StringUtils.isNotEmpty(facet.getMaxValueFT())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, metadataName + ServiceExceptionParametersInternal.FACET_MAX_VALUE_FT));
            }

            // decimals
            if (StringUtils.isNotEmpty(facet.getDecimalsFT())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, metadataName + ServiceExceptionParametersInternal.FACET_DECIMALS_FT));
            }

            // pattern
            if (StringUtils.isNotEmpty(facet.getPatternFT())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, metadataName + ServiceExceptionParametersInternal.FACET_PATTERN_FT));
            }

            // Prohibited
            if (StringUtils.isNotEmpty(facet.getIsMultiLingual())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_UNEXPECTED, metadataName + ServiceExceptionParametersInternal.FACET_IS_MULTI_LINGUAL));
            }
            if (StringUtils.isNotEmpty(facet.getXhtmlEFT())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_UNEXPECTED, metadataName + ServiceExceptionParametersInternal.FACET_XHTML_EFT));
            }

        }
    }

    private static void checkTimeTextFormatType(Facet facet, String metadataName, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (facet == null) {
            return;
        }

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // Check for restrictions
        if (facet != null) {
            // FacetValueType
            if (facet.getFacetValue() == null) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, metadataName + ServiceExceptionParametersInternal.FACET_VALUE_TYPE_ENUM));
            } else {
                checkTimeDataType(facet.getFacetValue(), metadataName);
            }

            // FacetType --
            // NonFacetedTextFormatType is a restricted version of the SimpleComponentTextFormatType that does not allow for any facets.

            // IsSequenceFT
            if (StringUtils.isNotEmpty(facet.getIsSequenceFT())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_UNEXPECTED, metadataName + ServiceExceptionParametersInternal.FACET_IS_SEQUENCE_FT));
            }

            // IntervalFT
            if (StringUtils.isNotEmpty(facet.getIntervalFT())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, metadataName + ServiceExceptionParametersInternal.FACET_INTERVAL_FT));
            }

            // StartValueFT
            if (StringUtils.isNotEmpty(facet.getStartValueFT())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, metadataName + ServiceExceptionParametersInternal.FACET_START_VALUE_FT));
            }

            // EndValueFT
            if (StringUtils.isNotEmpty(facet.getEndValueFT())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, metadataName + ServiceExceptionParametersInternal.FACET_END_VALUE_FT));
            }

            // TimeInterval
            if (StringUtils.isNotEmpty(facet.getTimeIntervalFT())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, metadataName + ServiceExceptionParametersInternal.FACET_TIME_INTERVAL_FT));
            }

            // startTime
            if (StringUtils.isNotEmpty(facet.getStartTimeFT()) && !CoreCommonUtil.matchStringToPattern(facet.getStartTimeFT(), CoreCommonUtil.PATTERN_SDMXL21_STANDARD_TIME_PERIOD)) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, metadataName + ServiceExceptionParametersInternal.FACET_START_TIME_FT));
            }

            // endTime
            if (StringUtils.isNotEmpty(facet.getEndTimeFT()) && !CoreCommonUtil.matchStringToPattern(facet.getEndTimeFT(), CoreCommonUtil.PATTERN_SDMXL21_STANDARD_TIME_PERIOD)) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, metadataName + ServiceExceptionParametersInternal.FACET_END_TIME_FT));
            }

            // minLength
            if (StringUtils.isNotEmpty(facet.getMinLengthFT())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, metadataName + ServiceExceptionParametersInternal.FACET_MIN_LENGTH_FT));
            }

            // maxLength
            if (StringUtils.isNotEmpty(facet.getMaxLengthFT())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, metadataName + ServiceExceptionParametersInternal.FACET_MAX_LENGTH_FT));
            }

            // minValue
            if (StringUtils.isNotEmpty(facet.getMinValueFT())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, metadataName + ServiceExceptionParametersInternal.FACET_MIN_VALUE_FT));
            }

            // maxValue
            if (StringUtils.isNotEmpty(facet.getMaxValueFT())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, metadataName + ServiceExceptionParametersInternal.FACET_MAX_VALUE_FT));
            }

            // decimals
            if (StringUtils.isNotEmpty(facet.getDecimalsFT())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, metadataName + ServiceExceptionParametersInternal.FACET_DECIMALS_FT));
            }

            // pattern
            if (StringUtils.isNotEmpty(facet.getPatternFT())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, metadataName + ServiceExceptionParametersInternal.FACET_PATTERN_FT));
            }

            // Prohibited
            if (StringUtils.isNotEmpty(facet.getIsMultiLingual())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_UNEXPECTED, metadataName + ServiceExceptionParametersInternal.FACET_IS_MULTI_LINGUAL));
            }
            if (StringUtils.isNotEmpty(facet.getXhtmlEFT())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_UNEXPECTED, metadataName + ServiceExceptionParametersInternal.FACET_XHTML_EFT));
            }

        }
    }

    private static void checkSimpleDataType(FacetValueTypeEnum facetValueTypeEnum, String metadataName) throws MetamacException {
        if (facetValueTypeEnum == null) {
            return;
        }
        if (!DataStructureConstraintSharedValidator.isSimpleDataType(facetValueTypeEnum)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_INCORRECT).withLoggedLevel(ExceptionLevelEnum.DEBUG)
                    .withMessageParameters(metadataName + ServiceExceptionParametersInternal.FACET_VALUE_TYPE_ENUM).build();
        }
    }

    private static void checkTimeDataType(FacetValueTypeEnum facetValueTypeEnum, String metadataName) throws MetamacException {
        if (facetValueTypeEnum == null) {
            return;
        }
        if (!DataStructureConstraintSharedValidator.isTimeDataType(facetValueTypeEnum)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_INCORRECT).withLoggedLevel(ExceptionLevelEnum.DEBUG)
                    .withMessageParameters(metadataName + ServiceExceptionParametersInternal.FACET_VALUE_TYPE_ENUM).build();
        }
    }

}
