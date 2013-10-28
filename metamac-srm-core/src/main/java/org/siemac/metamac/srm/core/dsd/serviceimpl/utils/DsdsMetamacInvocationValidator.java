package org.siemac.metamac.srm.core.dsd.serviceimpl.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.srm.core.common.SrmConstantsTest;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;

import com.arte.statistic.sdmx.srm.core.base.domain.Component;
import com.arte.statistic.sdmx.srm.core.base.domain.ComponentList;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.ValidationUtils;
import com.arte.statistic.sdmx.srm.core.structure.domain.DimensionComponent;
import com.arte.statistic.sdmx.srm.core.structure.serviceimpl.utils.DataStructureInvocationValidator;

public class DsdsMetamacInvocationValidator extends DataStructureInvocationValidator {

    public static void checkCreateDataStructureDefinition(DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(dataStructureDefinitionVersionMetamac.getStatisticalOperation(), ServiceExceptionParameters.DATA_STRUCTURE_DEFINITION_STATISTICAL_OPERATION, exceptions);
        if (dataStructureDefinitionVersionMetamac.getStatisticalOperation() != null) {
            // urn in ExternalItems is optional, but it is required for statistical operation
            ValidationUtils.checkMetadataRequired(dataStructureDefinitionVersionMetamac.getStatisticalOperation().getUrn(), ServiceExceptionParameters.DATA_STRUCTURE_DEFINITION_STATISTICAL_OPERATION,
                    exceptions);
            ValidationUtils.checkMetadataEmpty(dataStructureDefinitionVersionMetamac.getStatisticalOperation().getUrnProvider(),
                    ServiceExceptionParameters.DATA_STRUCTURE_DEFINITION_STATISTICAL_OPERATION, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkPreCreateDataStructureDefinition(DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac, List<MetamacExceptionItem> exceptions)
            throws MetamacException {
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

    public static void checkFindDataStructureDefinitionsByCondition(List<ConditionalCriteria> conditions, PagingParameter pagingParameter, List<MetamacExceptionItem> exceptions)
            throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkDescriptorToCreateOrUpdate(ComponentList descriptor, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(descriptor, ServiceExceptionParameters.COMPONENT_LIST, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkComponentToCreateOrUpdate(Component component, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(component, ServiceExceptionParameters.COMPONENT, exceptions);

        if (component instanceof DimensionComponent) {
            ValidationUtils.checkMetadataRequired(((DimensionComponent) component).getIsEnumeratedRepresentationUpdated(),
                    ServiceExceptionParameters.DATA_STRUCTURE_DEFINITION_DIMENSION_REPRESENTATION_UPDATED, exceptions);
            ValidationUtils.checkMetadataRequired(((DimensionComponent) component).getIsConceptIdUpdated(), ServiceExceptionParameters.DATA_STRUCTURE_DEFINITION_DIMENSION_CONCEPTID_UPDATED,
                    exceptions);
        }

        // In metamac there are reserved keywords
        if (StringUtils.equalsIgnoreCase(SrmConstantsTest.ATTRIBUTE_DATA_SOURCE_ID, component.getCode())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METAMAC_RESERVED_KEYWORD, component.getCode()));
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindConceptsCanBeDsdPrimaryMeasureByCondition(List<ConditionalCriteria> conditions, PagingParameter pagingParameter, String dsdUrn, List<MetamacExceptionItem> exceptions)
            throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(dsdUrn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindConceptsCanBeDsdRoleByCondition(List<ConditionalCriteria> conditions, PagingParameter pagingParameter, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindCodelistsCanBeEnumeratedRepresentationForDsd(List<ConditionalCriteria> conditions, PagingParameter pagingParameter, String conceptUrn,
            List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(conceptUrn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindConceptSchemesCanBeEnumeratedRepresentationForDsdMeasureDimension(List<ConditionalCriteria> conditions, PagingParameter pagingParametern,
            List<MetamacExceptionItem> exceptions) throws MetamacException {
        // nothing
    }
    public static void checkFindCodelistsCanBeEnumeratedRepresentationForDsdPrimaryMeasureByCondition(List<ConditionalCriteria> conditions, PagingParameter pagingParameter, Object object) {
        // nothing
    }

    public static void findOrderVisualisationCanBeDisplayOrderForDsdDimensionByCondition(List<ConditionalCriteria> conditions, PagingParameter pagingParameter, String dimensionUrn,
            List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(dimensionUrn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void findOpennessVisualisationCanBeHierarchylevelopenForDsdDimensionByCondition(List<ConditionalCriteria> conditions, PagingParameter pagingParameter, String dimensionUrn,
            List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(dimensionUrn, ServiceExceptionParameters.URN, exceptions);

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
