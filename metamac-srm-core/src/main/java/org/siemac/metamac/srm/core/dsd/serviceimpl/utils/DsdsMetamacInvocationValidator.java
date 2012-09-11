package org.siemac.metamac.srm.core.dsd.serviceimpl.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;

import com.arte.statistic.sdmx.srm.core.structure.serviceimpl.utils.DataStructureInvocationValidator;

public class DsdsMetamacInvocationValidator extends DataStructureInvocationValidator {

    public static void checkCreateDataStructureDefinition(DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        ValidationUtils.checkParameterRequired(dataStructureDefinitionVersionMetamac, ServiceExceptionParameters.DATA_STRUCTURE_DEFINITION, exceptions);
        if (dataStructureDefinitionVersionMetamac != null) {
            if (dataStructureDefinitionVersionMetamac != null) {
                checkDataStructureDefinition(dataStructureDefinitionVersionMetamac, exceptions);
            }
        }

        ExceptionUtils.throwIfException(exceptions);
    }
    
    public static void checkUpdateDataStructureDefinition(DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        ValidationUtils.checkParameterRequired(dataStructureDefinitionVersionMetamac, ServiceExceptionParameters.DATA_STRUCTURE_DEFINITION, exceptions);
        if (dataStructureDefinitionVersionMetamac != null) {
            checkDataStructureDefinition(dataStructureDefinitionVersionMetamac, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }
    
    public static void checkFindDataStructureDefinitionsByCondition(List<ConditionalCriteria> conditions, PagingParameter pagingParameter, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ExceptionUtils.throwIfException(exceptions);
    }
    
    public static void cancelDataStructureDefinitionVersionMetamacValidity(String urn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        ValidationUtils.checkParameterRequired(urn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }
    
    /**************************************************************************
     * PRIVATES
     *************************************************************************/
    
    private static void checkDataStructureDefinition(DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac, List<MetamacExceptionItem> exceptions) {
        if (dataStructureDefinitionVersionMetamac.getMaintainableArtefact() != null && BooleanUtils.isTrue(dataStructureDefinitionVersionMetamac.getMaintainableArtefact().getIsExternalReference())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.MAINTAINABLE_ARTEFACT_IS_EXTERNAL_REFERENCE));
        }
    }

}
