package org.siemac.metamac.srm.core.structure.serviceimpl.utils;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.exception.ExceptionLevelEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils;
import org.siemac.metamac.srm.core.base.domain.Component;
import org.siemac.metamac.srm.core.base.domain.ComponentList;
import org.siemac.metamac.srm.core.common.error.MetamacCoreExceptionType;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.structure.domain.AttributeDescriptor;
import org.siemac.metamac.srm.core.structure.domain.DataAttribute;
import org.siemac.metamac.srm.core.structure.domain.DataStructureDefinition;
import org.siemac.metamac.srm.core.structure.domain.Dimension;
import org.siemac.metamac.srm.core.structure.domain.DimensionDescriptor;
import org.siemac.metamac.srm.core.structure.domain.GroupDimensionDescriptor;
import org.siemac.metamac.srm.core.structure.domain.GroupRelationship;
import org.siemac.metamac.srm.core.structure.domain.MeasureDescriptor;
import org.siemac.metamac.srm.core.structure.domain.PrimaryMeasure;
import org.siemac.metamac.srm.core.structure.domain.ReportingYearStartDay;
import org.siemac.metamac.srm.core.structure.domain.TimeDimension;


public class DataStructureConstraintValidator {

    public static void checkConstraintDsdGrouping(DataStructureDefinition dataStructureDefinition, ComponentList componentList) throws MetamacException {
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
    
    public static void checkDataAttribute(DataAttribute dataAttribute, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        if (dataAttribute instanceof ReportingYearStartDay) {
            checkReportingYearStartDay((ReportingYearStartDay) dataAttribute, exceptions);
        }
        
        // Metadata Required
        ValidationUtils.checkMetadataRequired(dataAttribute.getUsageStatus(), ServiceExceptionParameters.DATAATTRIBUTE_USAGE_STATES, exceptions);
        ValidationUtils.checkMetadataRequired(dataAttribute.getRelateTo(), ServiceExceptionParameters.DATAATTRIBUTE_RELATETO, exceptions);
        
        // LocalRepresentation
        // TODO remove validaterepresentation in DTO
//           ValidationUtil.validateRepresentationForDataAttribute(ctx, source.getLocalRepresentation());
        
        
        // TODO ConceptIdentity in component???
//        if (dataAttribute.getCptIdRef() == null) {
//            exceptions.add(new MetamacExceptionItem(CommonServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.da));
//            MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.PARAMETER_INCORRECT, "conceptIdentity");
//            metamacException.setLoggedLevel(ExceptionLevelEnum.INFO);
//            throw metamacException;
//        }
        
        ExceptionUtils.throwIfException(exceptions);
    }
    
    public static void checkReportingYearStartDay(ReportingYearStartDay reportingYearStartDay, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        // Metadata Required
        
        //TODO ValidationUtil.validateRepresentationForReportingYearStartDayType(ctx, source.getLocalRepresentation());
        
        ExceptionUtils.throwIfException(exceptions);
    }
    
    public static void checkDimension(Dimension dimension, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        // Metadata Required
        
        //TODO ValidationUtil.validateRepresentationForReportingYearStartDayType(ctx, source.getLocalRepresentation());
        
        
        // LocalRepresentation
//        ValidationUtil.validateRepresentationForDimension(ctx, source.getLocalRepresentation());
        
        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkMeasureDimension(TimeDimension timeDimension, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        // Metadata Required
        
        // LocalRepresentation
// TODO       ValidationUtil.validateRepresentationForMeasureDimension(ctx, source.getLocalRepresentation());
        
        ExceptionUtils.throwIfException(exceptions);
    }
    
    public static void checkTimeDimension(TimeDimension timeDimension, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        // Metadata Required
        
        
        // LocalRepresentation
        // TODO ValidationUtil.validateRepresentationForTimeDimension(ctx, source.getLocalRepresentation());
        
        
        // TODO role debe ser vacío
//        if (!((DimensionComponentDto) source).getRole().isEmpty()) {
//            MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.PARAMETER_INCORRECT, "role");
//            metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
//            throw metamacException;
//        }
        
        ExceptionUtils.throwIfException(exceptions);
    }
    
    
    public static void checkPrimaryMeasure(PrimaryMeasure primaryMeasure, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        // Metadata Required
        
//        ValidationUtil.validateRepresentationForPrimaryMeasure(ctx, source.getLocalRepresentation());
        
        ExceptionUtils.throwIfException(exceptions);
    }
    
    public static void checkComponent(Component component, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        // Metadata Required
        
        //TODO ValidationUtil.validateRepresentationForReportingYearStartDayType(ctx, source.getLocalRepresentation());
        
        
        // ConceptIdentity
//        if (((DimensionComponentDto) source).getCptIdRef() == null) {
//            MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.PARAMETER_INCORRECT, "conceptIdentity");
//            metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
//            throw metamacException;
//        }
        
        ExceptionUtils.throwIfException(exceptions);
    }
    
    public static void checkGroupDimensionDescriptor(GroupDimensionDescriptor groupDimensionDescriptor, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        //TODO Validate.notNull(((GroupDimensionDescriptor)result).getIsAttachmentConstraint(), "GroupDimensionDescriptor.isAttachmentConstraint must not be null"); // By Hibernate strategy
        // inheritance, this is defined here and not in the relational model
        
        ExceptionUtils.throwIfException(exceptions);
    }

    
    public static void checkGrouprelationship(GroupRelationship groupRelationship, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // TODO Validate.notNull(result.getGroupKey(), "GroupRelationship.groupKey must not be null"); // By Hibernate strategy inheritance, this is defined here
        
        ExceptionUtils.throwIfException(exceptions);
    }

    
    

}
