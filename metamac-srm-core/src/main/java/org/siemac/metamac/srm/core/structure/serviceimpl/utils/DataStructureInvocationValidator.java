package org.siemac.metamac.srm.core.structure.serviceimpl.utils;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.ent.domain.ExternalItem;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils;
import org.siemac.metamac.srm.core.base.domain.Component;
import org.siemac.metamac.srm.core.base.domain.ComponentList;
import org.siemac.metamac.srm.core.common.error.MetamacCoreExceptionType;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParametersInternal;
import org.siemac.metamac.srm.core.structure.domain.AttributeDescriptor;
import org.siemac.metamac.srm.core.structure.domain.DataStructureDefinition;
import org.siemac.metamac.srm.core.structure.domain.DimensionDescriptor;
import org.siemac.metamac.srm.core.structure.domain.GroupDimensionDescriptor;
import org.siemac.metamac.srm.core.structure.domain.MeasureDescriptor;


public class DataStructureInvocationValidator {

    public static void checkSaveDataStructureDefinition(DataStructureDefinition dataStructureDefinition, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        // Metadata Required
        ValidationUtils.checkMetadataRequired(dataStructureDefinition.getName(), ServiceExceptionParameters.DATASTRUCTUREDEFINITION_NAME, exceptions);
        checkExternalItem(dataStructureDefinition.getMaintainer(), exceptions, ServiceExceptionParameters.DATASTRUCTUREDEFINITION_MAINTAINER);

        ExceptionUtils.throwIfException(exceptions);
    }
    
    public static void checkDeleteDataStructureDefinition(DataStructureDefinition dataStructureDefinition, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkSaveDescriptorForDataStructureDefinition(DataStructureDefinition dataStructureDefinition, ComponentList componentList, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // Check type Component list
        if (!(componentList instanceof AttributeDescriptor) && !(componentList instanceof DimensionDescriptor) && !(componentList instanceof GroupDimensionDescriptor)
                && !(componentList instanceof MeasureDescriptor)) {
            MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.PARAMETER_INCORRECT, ServiceExceptionParameters.COMPONENT_LIST);
            throw metamacException;
        }

        // Other Constraints
        DataStructureConstraintValidator.checkConstraintDsdGrouping(dataStructureDefinition, componentList, exceptions);
        
        ExceptionUtils.throwIfException(exceptions);
    }
    
    public static void checkDeleteDescriptorForDataStructureDefinition(DataStructureDefinition dataStructureDefinition, ComponentList componentList, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ExceptionUtils.throwIfException(exceptions);
    }
    
    public static void checkSaveComponentForDataStructureDefinition(DataStructureDefinition dataStructureDefinition, Component component, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        // Other Constraints
        DataStructureConstraintValidator.checkComponent(component, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }
    
    public static void checkDeleteComponentForDataStructureDefinition(DataStructureDefinition dataStructureDefinition, Component component, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    //TODO invoke this when publish DSD
    public static void checkPublishDataStructureDefinition(DataStructureDefinition dataStructureDefinition, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        // Other Constraints
        DataStructureConstraintValidator.checkDsdConstraintsToPublish(dataStructureDefinition, exceptions);
        
        ExceptionUtils.throwIfException(exceptions);
    }
    
    /**************************************************************************
     *  PRIVATES
     *************************************************************************/
    private static List<MetamacExceptionItem> checkExternalItem(ExternalItem externalItem, List<MetamacExceptionItem> exceptions, String metadataName) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        // Parameter Required
        
        // Metadata Empty
        
        // Metadata Required
        ValidationUtils.checkMetadataRequired(externalItem.getUri(), metadataName + ServiceExceptionParametersInternal.EXTERNALITEM_URI, exceptions);
        ValidationUtils.checkMetadataRequired(externalItem.getCode(), metadataName + ServiceExceptionParametersInternal.EXTERNALITEM_CODE, exceptions);
        ValidationUtils.checkMetadataRequired(externalItem.getUrn(), metadataName + ServiceExceptionParametersInternal.EXTERNALITEM_URN, exceptions);
        ValidationUtils.checkMetadataRequired(externalItem.getType(), metadataName + ServiceExceptionParametersInternal.EXTERNALITEM_TYPE, exceptions);
        
        return exceptions;
    }
    
}
