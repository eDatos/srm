package org.siemac.metamac.srm.core.structure.serviceimpl.utils;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.srm.core.structure.domain.DataStructureDefinition;
import org.siemac.metamac.srm.core.structure.domain.GroupDimensionDescriptor;


public class DataStructureInvocationValidator {

    public static void checkSaveDataStructureDefinition(DataStructureDefinition dataStructureDefinition, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // Parameter Required
//        ValidationUtils.checkParameterRequired(publication, ServiceExceptionParameters.PUBLICATION, exceptions);
        
        // Metadata Empty
//        ValidationUtils.checkMetadataEmpty(publication.getId(), ServiceExceptionParameters.PUBLICATION_ID, exceptions);
//        ValidationUtils.checkMetadataEmpty(publication.getVersion(), ServiceExceptionParameters.PUBLICATION_VERSION, exceptions);
        
        // Metadata Required
//        ValidationUtils.checkMetadataRequired(publication.getUrn(), ServiceExceptionParameters.PUBLICATION_URI, exceptions);
//        ValidationUtils.checkMetadataRequired(publication.getRestpath(), ServiceExceptionParameters.PUBLICATION_REST_PATH, exceptions);
//        ValidationUtils.checkMetadataRequired(publication.getVersionLogic(), ServiceExceptionParameters.PUBLICATION_VERSION_LOGIC, exceptions);
//        ValidationUtils.checkMetadataRequired(publication.getPublishDate(), ServiceExceptionParameters.PUBLICATION_PUBLISH_DATE, exceptions);
//        ValidationUtils.checkMetadataRequired(publication.getTypePublishResource(), ServiceExceptionParameters.PUBLICATION_TYPE_PUBLISH_RESOURCE, exceptions);
//        ValidationUtils.checkMetadataRequired(publication.getTypePublishState(), ServiceExceptionParameters.PUBLICATION_TYPE_PUBLISH_STATE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }
    
    public static void checkDeleteDataStructureDefinition(DataStructureDefinition dataStructureDefinition, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkSaveDescriptorForDataStructureDefinition(DataStructureDefinition dataStructureDefinition, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ExceptionUtils.throwIfException(exceptions);
    }
    
    public static void checkDeleteDescriptorForDataStructureDefinition(DataStructureDefinition dataStructureDefinition, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ExceptionUtils.throwIfException(exceptions);
    }
    
    public static void checkSaveComponentForDataStructureDefinition(DataStructureDefinition dataStructureDefinition, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ExceptionUtils.throwIfException(exceptions);
    }
    
    public static void checkDeleteComponentForDataStructureDefinition(DataStructureDefinition dataStructureDefinition, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    
}
