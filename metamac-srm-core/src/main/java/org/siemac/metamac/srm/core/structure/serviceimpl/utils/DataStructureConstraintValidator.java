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
import org.siemac.metamac.srm.core.base.domain.Representation;
import org.siemac.metamac.srm.core.base.domain.TextFormatRepresentation;
import org.siemac.metamac.srm.core.common.error.MetamacCoreExceptionType;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParametersInternal;
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
                    MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.MTM_CORE_VALIDATION_CONSTRAINT_CARDINALITY_MAX, "Max attribute descriptor is 1");
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
                    MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.MTM_CORE_VALIDATION_CONSTRAINT_CARDINALITY_MAX, "Max dimension descriptor is 1");
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
                    MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.MTM_CORE_VALIDATION_CONSTRAINT_CARDINALITY_MAX, "Max measure descriptor is 1");
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
            exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_REQUIRED, "COMPONENT_COCNEPTID")); //TODO CONCATENAR MSG
        }
        else {
            if (!TypeExternalArtefactsEnum.CONCEPT.equals(component.getCptIdRef().getType())) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, "CONCEPT_REFEREFENCE")); //TODO CONCATENAR MSG
            }
        }
        
        // LocalRepresentation
        if (component instanceof DataAttribute) {
            checkDataAttribute((DataAttribute)component, exceptions);
        }
        if (component instanceof ReportingYearStartDay) {
            checkReportingYearStartDay((ReportingYearStartDay)component, exceptions);
        }
        else if (component instanceof Dimension) {
            checkDimension((Dimension)component, exceptions);
        }
        else if (component instanceof MeasureDimension) {
            checkMeasureDimension((MeasureDimension)component, exceptions);
        }
        else if (component instanceof TimeDimension) {
            checkTimeDimension((TimeDimension)component, exceptions);
        }
        else if (component instanceof PrimaryMeasure) {
            checkPrimaryMeasure((PrimaryMeasure)component, exceptions);
        }
        
    }
    
    /**************************************************************************
     *                      PRIVATE
     *************************************************************************/
    private static void checkDataAttribute(DataAttribute dataAttribute, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        if (dataAttribute instanceof ReportingYearStartDay) {
            checkReportingYearStartDay((ReportingYearStartDay) dataAttribute, exceptions);
        }
        
        // AssignmentStatus
        ValidationUtils.checkMetadataRequired(dataAttribute.getUsageStatus(), ServiceExceptionParameters.DATAATTRIBUTE_USAGE_STATES, exceptions);
        
        // ConceptRole
        // <xs:element name="ConceptRole" type="common:ConceptReferenceType" minOccurs="0" maxOccurs="unbounded">
        for (ExternalItem externalItem: dataAttribute.getRole()) {
            if (!TypeExternalArtefactsEnum.CONCEPT.equals(externalItem.getType())) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, "DATAATTR_ROLE_CONCEPT_REFEREFENCE")); //TODO CONCATENAR MSG
            }
        }
        
        // AttributeRelationshipType
        validateAttributeRelationshipType(dataAttribute.getRelateTo(), exceptions);
        
        // LocalRepresentation
        validateSimpleDataStructureRepresentationType(dataAttribute.getLocalRepresentation(), exceptions);
    }
    
    private static void checkReportingYearStartDay(ReportingYearStartDay reportingYearStartDay, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        // AssignmentStatus
        ValidationUtils.checkMetadataRequired(reportingYearStartDay.getUsageStatus(), ServiceExceptionParameters.DATAATTRIBUTE_USAGE_STATES, exceptions);

        // AttributeRelationshipType
        validateAttributeRelationshipType(reportingYearStartDay.getRelateTo(), exceptions);
        
        // LocalRepresentation
        validateReportingYearStartDayRepresentationType(reportingYearStartDay.getLocalRepresentation(), exceptions);
    }
    
    private static void checkDimension(Dimension dimension, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        // ConceptRole
        // <xs:element name="ConceptRole" type="common:ConceptReferenceType" minOccurs="0" maxOccurs="unbounded">
        for (ExternalItem externalItem: dimension.getRole()) {
            if (!TypeExternalArtefactsEnum.CONCEPT.equals(externalItem.getType())) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, "DIMENSION_ROLE_CONCEPT_REFEREFENCE")); //TODO CONCATENAR MSG
            }
        }
        
        // LocalRepresentation
        validateSimpleDataStructureRepresentationType(dimension.getLocalRepresentation(), exceptions);
    }

    private static void checkMeasureDimension(MeasureDimension measureDimension, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        // ConceptRole
        // <xs:element name="ConceptRole" type="common:ConceptReferenceType" minOccurs="0" maxOccurs="unbounded">
        for (ExternalItem externalItem: measureDimension.getRole()) {
            if (!TypeExternalArtefactsEnum.CONCEPT.equals(externalItem.getType())) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, "MEASUREDIMENSION_ROLE_CONCEPT_REFEREFENCE")); //TODO CONCATENAR MSG
            }
        }
        
        // LocalRepresentation
        validateMeasureDimensionRepresentationType(measureDimension.getLocalRepresentation(), exceptions);
    }
    
    private static void checkTimeDimension(TimeDimension timeDimension, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        // LocalRepresentation
        validateTimeDimensionRepresentationType(timeDimension.getLocalRepresentation(), exceptions);
    }
    
    private static void checkPrimaryMeasure(PrimaryMeasure primaryMeasure, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        // LocalRepresentation
        validateSimpleDataStructureRepresentationType(primaryMeasure.getLocalRepresentation(), exceptions);
    }

    private static void validateAttributeRelationshipType(AttributeRelationship attributeRelationship, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (attributeRelationship == null) {
            exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_REQUIRED, "attributeRelationship"));
            return;
        }
        
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        if (attributeRelationship instanceof DimensionRelationship) {
            if (((DimensionRelationship) attributeRelationship).getDimension().isEmpty() &&((DimensionRelationship) attributeRelationship).getGroupKey().isEmpty()) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_REQUIRED, "Dimensionrelationship")); // TODOD arg
            }
        }
        else if (attributeRelationship instanceof GroupRelationship) {
            ValidationUtils.checkMetadataRequired(((GroupRelationship) attributeRelationship).getGroupKey(), "GroupRelationship", exceptions); // TODOD arg
        }
        else if (attributeRelationship instanceof NoSpecifiedRelationship) {
            ; // Nothing
        }
        else if (attributeRelationship instanceof PrimaryMeasureRelationship) {
            ; // Nothing
        }
    
    }
    
    private static void validateSimpleDataStructureRepresentationType(Representation representation, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (representation == null) {
            return;
        }
        
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        // Representation: Choice between Enumerated(Enumeration + EnumerationFormat) and NonEnumerated (TextFormat)
        if (representation instanceof TextFormatRepresentation) {
            // minOccurs="1"
            checkSimpleComponentTextFormatType((TextFormatRepresentation) representation, exceptions);
        }
        else if (representation instanceof EnumeratedRepresentation) {
            // minOccurs="1"
            // Enumeration:: common:CodelistReferenceType
            if (((EnumeratedRepresentation) representation).getEnumerated() == null) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_REQUIRED, "ENUMERATED")); //TODO CONCATENAR MSG
            }
            else {
                if (!TypeExternalArtefactsEnum.CODELIST.equals(((EnumeratedRepresentation) representation).getEnumerated().getType())) {
                    exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, "ENUMERATED")); //TODO CONCATENAR MSG
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
            checkNonFacetedTextFormatType((TextFormatRepresentation) representation, exceptions);
            
            // FacetValueType must be fixed to "MonthDay"
            if (!FacetValueTypeEnum.MONTH_DAY_FVT.equals(((TextFormatRepresentation) representation).getNonEnumerated().getFacetValue())) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_UNEXPECTED, ServiceExceptionParametersInternal.FACET_FACETVALUETYPEENUM)); //TODO CONCATENAR MSG
            }
        }
        else if (representation instanceof EnumeratedRepresentation) {
           exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.PARAMETER_INCORRECT, "ENUMERATED")); //TODO CONCATENAR MSG
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
            exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.PARAMETER_INCORRECT, "NONENUMERATED")); //TODO CONCATENAR MSG
        }
        else if (representation instanceof EnumeratedRepresentation) {
            // minOccurs="1"
            // Enumeration:: common:ConceptSchemeReferenceType
            if (((EnumeratedRepresentation) representation).getEnumerated() == null) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_REQUIRED, "ENUMERATED")); //TODO CONCATENAR MSG
            }
            else {
                if (!TypeExternalArtefactsEnum.CONCEPT_SCHEME.equals(((EnumeratedRepresentation) representation).getEnumerated().getType())) {
                    exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, "ENUMERATED")); //TODO CONCATENAR MSG
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
            checkNonFacetedTextFormatType((TextFormatRepresentation) representation, exceptions);
            
            // FacetValueType must be fixed to "MonthDay"
            if (!FacetValueTypeEnum.MONTH_DAY_FVT.equals(((TextFormatRepresentation) representation).getNonEnumerated().getFacetValue())) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_UNEXPECTED, ServiceExceptionParametersInternal.FACET_FACETVALUETYPEENUM)); //TODO CONCATENAR MSG
            }
        }
        else if (representation instanceof EnumeratedRepresentation) {
           exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.PARAMETER_INCORRECT, "ENUMERATED")); //TODO CONCATENAR MSG
        }
    }
    
    private static void checkSimpleComponentTextFormatType(TextFormatRepresentation textFormatRepresentation, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        // Check for restrictions
        if (textFormatRepresentation.getNonEnumerated() != null) {
            // FacetValueType
            checkSimpleDataType(textFormatRepresentation.getNonEnumerated().getFacetValue());
            
            // FacetType --
            
            // IsSequenceFT
            if (!CoreCommonUtil.matchStringToPattern(textFormatRepresentation.getNonEnumerated().getIsSequenceFT(), CoreCommonUtil.PATTERN_XSD_BOOLEAN)) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, ServiceExceptionParametersInternal.FACET_ISSEQUENCEFT)); //TODO CONCATENAR MSG
            }
            
            // IntervalFT
            if (!CoreCommonUtil.matchStringToPattern(textFormatRepresentation.getNonEnumerated().getIntervalFT(), CoreCommonUtil.PATTERN_XSD_INTEGER)) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, ServiceExceptionParametersInternal.FACET_INTERVALFT)); //TODO CONCATENAR MSG
            }
            
            // StartValueFT
            if (!CoreCommonUtil.matchStringToPattern(textFormatRepresentation.getNonEnumerated().getStartValueFT(), CoreCommonUtil.PATTERN_XSD_INTEGER)) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, ServiceExceptionParametersInternal.FACET_STARTVALUEFT)); //TODO CONCATENAR MSG
            }
            
            // EndValueFT
            if (!CoreCommonUtil.matchStringToPattern(textFormatRepresentation.getNonEnumerated().getEndValueFT(), CoreCommonUtil.PATTERN_XSD_INTEGER)) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, ServiceExceptionParametersInternal.FACET_ENDVALUEFT)); //TODO CONCATENAR MSG
            }
            
            // TimeInterval
            if (!CoreCommonUtil.matchStringToPattern(textFormatRepresentation.getNonEnumerated().getTimeIntervalFT(), CoreCommonUtil.PATTERN_XSD_DURATION)) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, ServiceExceptionParametersInternal.FACET_TIMEINTERVALFT)); //TODO CONCATENAR MSG
            }
            
            // startTime
            if (!CoreCommonUtil.matchStringToPattern(textFormatRepresentation.getNonEnumerated().getStartTimeFT(), CoreCommonUtil.PATTERN_SDMXL21_STANDARD_TIME_PERIOD)) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, ServiceExceptionParametersInternal.FACET_STARTTIMEFT)); //TODO CONCATENAR MSG
            }
            
            // endTime
            if (!CoreCommonUtil.matchStringToPattern(textFormatRepresentation.getNonEnumerated().getEndTimeFT(), CoreCommonUtil.PATTERN_SDMXL21_STANDARD_TIME_PERIOD)) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, ServiceExceptionParametersInternal.FACET_ENDTIMEFT)); //TODO CONCATENAR MSG
            }

            // minLength
            if (!CoreCommonUtil.matchStringToPattern(textFormatRepresentation.getNonEnumerated().getMinLengthFT(), CoreCommonUtil.PATTERN_XSD_POSITIVEINTEGER)) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, ServiceExceptionParametersInternal.FACET_MINLENGTHFT)); //TODO CONCATENAR MSG
            }
            
            // maxLength
            if (!CoreCommonUtil.matchStringToPattern(textFormatRepresentation.getNonEnumerated().getMaxLengthFT(), CoreCommonUtil.PATTERN_XSD_POSITIVEINTEGER)) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, ServiceExceptionParametersInternal.FACET_MAXLENGTHFT)); //TODO CONCATENAR MSG
            }
            
            // minValue
            if (!CoreCommonUtil.matchStringToPattern(textFormatRepresentation.getNonEnumerated().getMinValueFT(), CoreCommonUtil.PATTERN_XSD_INTEGER)) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, ServiceExceptionParametersInternal.FACET_MINVALUEFT)); //TODO CONCATENAR MSG
            }
            
            // maxValue
            if (!CoreCommonUtil.matchStringToPattern(textFormatRepresentation.getNonEnumerated().getMaxValueFT(), CoreCommonUtil.PATTERN_XSD_INTEGER)) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, ServiceExceptionParametersInternal.FACET_MAXVALUEFT)); //TODO CONCATENAR MSG
            }
            
            // decimals
            if (!CoreCommonUtil.matchStringToPattern(textFormatRepresentation.getNonEnumerated().getDecimalsFT(), CoreCommonUtil.PATTERN_XSD_POSITIVEINTEGER)) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, ServiceExceptionParametersInternal.FACET_DECIMALSFT)); //TODO CONCATENAR MSG
            }
            
            // pattern
            // ... nothing ...
            
            // Prohibited
            if (StringUtils.isNotEmpty(textFormatRepresentation.getNonEnumerated().getIsMultiLingual())) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_UNEXPECTED, ServiceExceptionParametersInternal.FACET_ISMULTILINGUAL)); //TODO CONCATENAR MSG
            }
            if (StringUtils.isNotEmpty(textFormatRepresentation.getNonEnumerated().getXhtmlEFT())) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_UNEXPECTED, ServiceExceptionParametersInternal.FACET_XHTMLEFT)); //TODO CONCATENAR MSG
            }
            
        }
    }
    
    private static void checkNonFacetedTextFormatType(TextFormatRepresentation textFormatRepresentation, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        // Check for restrictions
        if (textFormatRepresentation.getNonEnumerated() != null) {
            // FacetValueType
            checkSimpleDataType(textFormatRepresentation.getNonEnumerated().getFacetValue());
            
            // FacetType --
            // NonFacetedTextFormatType is a restricted version of the SimpleComponentTextFormatType that does not allow for any facets.
            
            // IsSequenceFT
            if (StringUtils.isNotEmpty(textFormatRepresentation.getNonEnumerated().getIsSequenceFT())) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_UNEXPECTED, ServiceExceptionParametersInternal.FACET_ISSEQUENCEFT)); //TODO CONCATENAR MSG
            }
            
            // IntervalFT
            if (StringUtils.isNotEmpty(textFormatRepresentation.getNonEnumerated().getIntervalFT())) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, ServiceExceptionParametersInternal.FACET_INTERVALFT)); //TODO CONCATENAR MSG
            }
            
            // StartValueFT
            if (StringUtils.isNotEmpty(textFormatRepresentation.getNonEnumerated().getStartValueFT())) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, ServiceExceptionParametersInternal.FACET_STARTVALUEFT)); //TODO CONCATENAR MSG
            }
            
            // EndValueFT
            if (StringUtils.isNotEmpty(textFormatRepresentation.getNonEnumerated().getEndValueFT())) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, ServiceExceptionParametersInternal.FACET_ENDVALUEFT)); //TODO CONCATENAR MSG
            }
            
            // TimeInterval
            if (StringUtils.isNotEmpty(textFormatRepresentation.getNonEnumerated().getTimeIntervalFT())) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, ServiceExceptionParametersInternal.FACET_TIMEINTERVALFT)); //TODO CONCATENAR MSG
            }
            
            // startTime
            if (StringUtils.isNotEmpty(textFormatRepresentation.getNonEnumerated().getStartTimeFT())) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, ServiceExceptionParametersInternal.FACET_STARTTIMEFT)); //TODO CONCATENAR MSG
            }
            
            // endTime
            if (StringUtils.isNotEmpty(textFormatRepresentation.getNonEnumerated().getEndTimeFT())) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, ServiceExceptionParametersInternal.FACET_ENDTIMEFT)); //TODO CONCATENAR MSG
            }

            // minLength
            if (StringUtils.isNotEmpty(textFormatRepresentation.getNonEnumerated().getMinLengthFT())) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, ServiceExceptionParametersInternal.FACET_MINLENGTHFT)); //TODO CONCATENAR MSG
            }
            
            // maxLength
            if (StringUtils.isNotEmpty(textFormatRepresentation.getNonEnumerated().getMaxLengthFT())) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, ServiceExceptionParametersInternal.FACET_MAXLENGTHFT)); //TODO CONCATENAR MSG
            }
            
            // minValue
            if (StringUtils.isNotEmpty(textFormatRepresentation.getNonEnumerated().getMinValueFT())) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, ServiceExceptionParametersInternal.FACET_MINVALUEFT)); //TODO CONCATENAR MSG
            }
            
            // maxValue
            if (StringUtils.isNotEmpty(textFormatRepresentation.getNonEnumerated().getMaxValueFT())) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, ServiceExceptionParametersInternal.FACET_MAXVALUEFT)); //TODO CONCATENAR MSG
            }
            
            // decimals
            if (StringUtils.isNotEmpty(textFormatRepresentation.getNonEnumerated().getDecimalsFT())) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, ServiceExceptionParametersInternal.FACET_DECIMALSFT)); //TODO CONCATENAR MSG
            }
            
            // pattern
            if (StringUtils.isNotEmpty(textFormatRepresentation.getNonEnumerated().getPatternFT())) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, ServiceExceptionParametersInternal.FACET_PATTERNFT)); //TODO CONCATENAR MSG
            }
            
            // Prohibited
            if (StringUtils.isNotEmpty(textFormatRepresentation.getNonEnumerated().getIsMultiLingual())) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_UNEXPECTED, ServiceExceptionParametersInternal.FACET_ISMULTILINGUAL)); //TODO CONCATENAR MSG
            }
            if (StringUtils.isNotEmpty(textFormatRepresentation.getNonEnumerated().getXhtmlEFT())) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_UNEXPECTED, ServiceExceptionParametersInternal.FACET_XHTMLEFT)); //TODO CONCATENAR MSG
            }
            
        }
    }
    
    private static void checkTimeTextFormatType(TextFormatRepresentation textFormatRepresentation, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        // Check for restrictions
        if (textFormatRepresentation.getNonEnumerated() != null) {
            // FacetValueType
            checkTimeDataType(textFormatRepresentation.getNonEnumerated().getFacetValue());
            
            // FacetType --
            // NonFacetedTextFormatType is a restricted version of the SimpleComponentTextFormatType that does not allow for any facets.
            
            // IsSequenceFT
            if (StringUtils.isNotEmpty(textFormatRepresentation.getNonEnumerated().getIsSequenceFT())) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_UNEXPECTED, ServiceExceptionParametersInternal.FACET_ISSEQUENCEFT)); //TODO CONCATENAR MSG
            }
            
            // IntervalFT
            if (StringUtils.isNotEmpty(textFormatRepresentation.getNonEnumerated().getIntervalFT())) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, ServiceExceptionParametersInternal.FACET_INTERVALFT)); //TODO CONCATENAR MSG
            }
            
            // StartValueFT
            if (StringUtils.isNotEmpty(textFormatRepresentation.getNonEnumerated().getStartValueFT())) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, ServiceExceptionParametersInternal.FACET_STARTVALUEFT)); //TODO CONCATENAR MSG
            }
            
            // EndValueFT
            if (StringUtils.isNotEmpty(textFormatRepresentation.getNonEnumerated().getEndValueFT())) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, ServiceExceptionParametersInternal.FACET_ENDVALUEFT)); //TODO CONCATENAR MSG
            }
            
            // TimeInterval
            if (StringUtils.isNotEmpty(textFormatRepresentation.getNonEnumerated().getTimeIntervalFT())) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, ServiceExceptionParametersInternal.FACET_TIMEINTERVALFT)); //TODO CONCATENAR MSG
            }
            
            // startTime
            if (!CoreCommonUtil.matchStringToPattern(textFormatRepresentation.getNonEnumerated().getStartTimeFT(), CoreCommonUtil.PATTERN_SDMXL21_STANDARD_TIME_PERIOD)) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, ServiceExceptionParametersInternal.FACET_STARTTIMEFT)); //TODO CONCATENAR MSG
            }
            
            // endTime
            if (!CoreCommonUtil.matchStringToPattern(textFormatRepresentation.getNonEnumerated().getEndTimeFT(), CoreCommonUtil.PATTERN_SDMXL21_STANDARD_TIME_PERIOD)) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, ServiceExceptionParametersInternal.FACET_ENDTIMEFT)); //TODO CONCATENAR MSG
            }

            // minLength
            if (StringUtils.isNotEmpty(textFormatRepresentation.getNonEnumerated().getMinLengthFT())) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, ServiceExceptionParametersInternal.FACET_MINLENGTHFT)); //TODO CONCATENAR MSG
            }
            
            // maxLength
            if (StringUtils.isNotEmpty(textFormatRepresentation.getNonEnumerated().getMaxLengthFT())) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, ServiceExceptionParametersInternal.FACET_MAXLENGTHFT)); //TODO CONCATENAR MSG
            }
            
            // minValue
            if (StringUtils.isNotEmpty(textFormatRepresentation.getNonEnumerated().getMinValueFT())) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, ServiceExceptionParametersInternal.FACET_MINVALUEFT)); //TODO CONCATENAR MSG
            }
            
            // maxValue
            if (StringUtils.isNotEmpty(textFormatRepresentation.getNonEnumerated().getMaxValueFT())) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, ServiceExceptionParametersInternal.FACET_MAXVALUEFT)); //TODO CONCATENAR MSG
            }
            
            // decimals
            if (StringUtils.isNotEmpty(textFormatRepresentation.getNonEnumerated().getDecimalsFT())) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, ServiceExceptionParametersInternal.FACET_DECIMALSFT)); //TODO CONCATENAR MSG
            }
            
            // pattern
            if (StringUtils.isNotEmpty(textFormatRepresentation.getNonEnumerated().getPatternFT())) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_INCORRECT, ServiceExceptionParametersInternal.FACET_PATTERNFT)); //TODO CONCATENAR MSG
            }
            
            // Prohibited
            if (StringUtils.isNotEmpty(textFormatRepresentation.getNonEnumerated().getIsMultiLingual())) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_UNEXPECTED, ServiceExceptionParametersInternal.FACET_ISMULTILINGUAL)); //TODO CONCATENAR MSG
            }
            if (StringUtils.isNotEmpty(textFormatRepresentation.getNonEnumerated().getXhtmlEFT())) {
                exceptions.add(new MetamacExceptionItem(MetamacCoreExceptionType.METADATA_UNEXPECTED, ServiceExceptionParametersInternal.FACET_XHTMLEFT)); //TODO CONCATENAR MSG
            }
            
        }
    }
    
    private static void checkSimpleDataType(FacetValueTypeEnum facetValueTypeEnum) throws MetamacException {
        // Check for restrictions
        switch (facetValueTypeEnum) {
            case STRING_FVT:
                break;
            case ALPHA_FVT:
                break;
            case ALPHA_NUMERIC_FVT:
                break;
            case NUMERIC_FVT:
                break;
            case BIG_INTEGER_FVT:
                break;
            case INTEGER_FVT:
                break;
            case LONG_FVT:
                break;
            case SHORT_FVT:
                break;
            case DECIMAL_FVT:
                break;
            case FLOAT_FVT:
                break;
            case DOUBLE_FVT:
                break;
            case BOOLEAN_FVT:
                break;
            case URI_FVT:
                break;
            case COUNT_FVT:
                break;
            case INCLUSIVE_VALUE_RANGE_FVT:
                break;
            case EXCLUSIVE_VALUE_RANGE_FVT:
                break;
            case INCREMENTAL_FVT:
                break;
            case OBSERVATIONAL_TIME_PERIOD_FVT:
                break;
            case STANDARD_TIME_PERIOD_FVT:
                break;
            case BASIC_TIME_PERIOD_FVT:
                break;
            case GREGORIAN_TIME_PERIOD_FVT:
                break;
            case GREGORIAN_YEAR_FVT:
                break;
            case GREGORIAN_YEAR_MONTH_FVT:
                break;
            case GREGORIAN_DAY_FVT:
                break;
            case REPORTING_TIME_PERIOD_FVT:
                break;
            case REPORTING_YEAR_FVT:
                break;
            case REPORTING_SEMESTER_FVT:
                break;
            case REPORTING_TRIMESTER_FVT:
                break;
            case REPORTING_QUARTER_FVT:
                break;
            case REPORTING_MONTH_FVT:
                break;
            case REPORTING_WEEK_FVT:
                break;
            case REPORTING_DAY_FVT:
                break;
            case DATE_TIME_FVT:
                break;
            case TIMES_RANGE_FVT:
                break;
            case MONTH_FVT:
                break;
            case MONTH_DAY_FVT:
                break;
            case DAY_FVT:
                break;
            case TIME_FVT:
                break;
            case DURATION_FVT:
                break;
            default:
                // TODO localizar el parametro del mensaje.
                throw MetamacExceptionBuilder.builder().withExceptionItems(MetamacCoreExceptionType.METADATA_INCORRECT).withLoggedLevel(ExceptionLevelEnum.DEBUG).withMessageParameters("FacetValueTypeEnum").build();
        }
    }
    
    private static void checkTimeDataType(FacetValueTypeEnum facetValueTypeEnum) throws MetamacException {
        // Check for restrictions
        switch (facetValueTypeEnum) {
            case OBSERVATIONAL_TIME_PERIOD_FVT:
                break;
            case STANDARD_TIME_PERIOD_FVT:
                break;
            case BASIC_TIME_PERIOD_FVT:
                break;
            case GREGORIAN_TIME_PERIOD_FVT:
                break;
            case GREGORIAN_YEAR_FVT:
                break;
            case GREGORIAN_YEAR_MONTH_FVT:
                break;
            case GREGORIAN_DAY_FVT:
                break;
            case REPORTING_TIME_PERIOD_FVT:
                break;
            case REPORTING_YEAR_FVT:
                break;
            case REPORTING_SEMESTER_FVT:
                break;
            case REPORTING_TRIMESTER_FVT:
                break;
            case REPORTING_QUARTER_FVT:
                break;
            case REPORTING_MONTH_FVT:
                break;
            case REPORTING_WEEK_FVT:
                break;
            case REPORTING_DAY_FVT:
                break;
            case DATE_TIME_FVT:
                break;
            case TIMES_RANGE_FVT:
                break;
            default:
                // TODO localizar el parametro del mensaje.
                throw MetamacExceptionBuilder.builder().withExceptionItems(MetamacCoreExceptionType.METADATA_INCORRECT).withLoggedLevel(ExceptionLevelEnum.DEBUG).withMessageParameters("FacetValueTypeEnum").build();
        }
    }

}
