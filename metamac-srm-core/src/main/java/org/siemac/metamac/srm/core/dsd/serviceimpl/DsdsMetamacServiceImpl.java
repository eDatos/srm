package org.siemac.metamac.srm.core.dsd.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.serviceimpl.utils.ConceptsMetamacInvocationValidator;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamacProperties;
import org.siemac.metamac.srm.core.dsd.serviceimpl.utils.DsdsMetamacInvocationValidator;
import org.siemac.metamac.srm.core.enume.domain.ItemSchemeMetamacProcStatusEnum;
import org.siemac.metamac.srm.core.serviceimpl.SrmServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.structure.domain.DataStructureDefinitionVersion;
import com.arte.statistic.sdmx.srm.core.structure.serviceapi.DataStructureDefinitionService;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.VersionTypeEnum;

/**
 * Implementation of DsdsMetamacService.
 */
@Service("dsdsMetamacService")
public class DsdsMetamacServiceImpl extends DsdsMetamacServiceImplBase {

    @Autowired
    private DataStructureDefinitionService dataStructureDefinitionService;

    @Autowired
    private DsdLifecycle                   dsdLifecycle;

    public DsdsMetamacServiceImpl() {
    }

    @Override
    public DataStructureDefinitionVersionMetamac createDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersion) throws MetamacException {
        // Validation
        DsdsMetamacInvocationValidator.checkCreateDataStructureDefinition(dataStructureDefinitionVersion, null);

        // Fill metadata
        dataStructureDefinitionVersion.setProcStatus(ItemSchemeMetamacProcStatusEnum.DRAFT);
        dataStructureDefinitionVersion.getMaintainableArtefact().setIsExternalReference(Boolean.FALSE);

        // Save conceptScheme
        return (DataStructureDefinitionVersionMetamac) dataStructureDefinitionService.createDataStructureDefinition(ctx, dataStructureDefinitionVersion);

    }

    @Override
    public DataStructureDefinitionVersionMetamac updateDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersion) throws MetamacException {
        // Validation
        DsdsMetamacInvocationValidator.checkUpdateDataStructureDefinition(dataStructureDefinitionVersion, null);
        // ConceptsService checks data structure definition isn't final (Schemes cannot be updated when procStatus is INTERNALLY_PUBLISHED or EXTERNALLY_PUBLISHED)

        // Save conceptScheme
        return (DataStructureDefinitionVersionMetamac) dataStructureDefinitionService.updateDataStructureDefinition(ctx, dataStructureDefinitionVersion);
    }
    
    @Override
    public DataStructureDefinitionVersionMetamac retrieveDataStructureDefinitionByUrn(ServiceContext ctx, String urn) throws MetamacException {
        return (DataStructureDefinitionVersionMetamac) dataStructureDefinitionService.retrieveDataStructureDefinitionByUrn(ctx, urn);
    }

    @Override
    public List<DataStructureDefinitionVersionMetamac> retrieveDataStructureDefinitionVersions(ServiceContext ctx, String urn) throws MetamacException {
        // Retrieve dataStructureDefinitionVersions
        List<DataStructureDefinitionVersion> dataStructureDefinitionVersionVersions = dataStructureDefinitionService.retrieveDataStructureDefinitionVersions(ctx, urn);

        // Type cast to DataStructureDefinitionVersionMetamac
        List<DataStructureDefinitionVersionMetamac> dataStructureVersionMetamacs = new ArrayList<DataStructureDefinitionVersionMetamac>();
        for (DataStructureDefinitionVersion dataStructureDefinitionVersion : dataStructureDefinitionVersionVersions) {
            dataStructureVersionMetamacs.add((DataStructureDefinitionVersionMetamac) dataStructureDefinitionVersion);
        }

        return dataStructureVersionMetamacs;
    }

    @Override
    public PagedResult<DataStructureDefinitionVersionMetamac> findDataStructureDefinitionsByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter)
            throws MetamacException {

        // Validation
        DsdsMetamacInvocationValidator.checkFindDataStructureDefinitionsByCondition(conditions, pagingParameter, null);

        // Find (do not call sdmx module to avoid typecast)
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(DataStructureDefinitionVersionMetamac.class).distinctRoot().build();
        }
        PagedResult<DataStructureDefinitionVersionMetamac> dataStructureVersionPagedResult = getDataStructureDefinitionVersionMetamacRepository().findByCondition(conditions, pagingParameter);
        return dataStructureVersionPagedResult;
    }

    @Override
    public DataStructureDefinitionVersionMetamac sendDataStructureDefinitionToProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (DataStructureDefinitionVersionMetamac) dsdLifecycle.sendToProductionValidation(ctx, urn);
    }

    @Override
    public DataStructureDefinitionVersionMetamac sendDataStructureDefinitionToDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (DataStructureDefinitionVersionMetamac) dsdLifecycle.sendToDiffusionValidation(ctx, urn);
    }

    public DataStructureDefinitionVersionMetamac rejectDataStructureDefinitionProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (DataStructureDefinitionVersionMetamac) dsdLifecycle.rejectProductionValidation(ctx, urn);
    }

    public DataStructureDefinitionVersionMetamac rejectDataStructureDefinitionDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (DataStructureDefinitionVersionMetamac) dsdLifecycle.rejectDiffusionValidation(ctx, urn);
    }

    public DataStructureDefinitionVersionMetamac publishInternallyDataStructureDefinition(ServiceContext ctx, String urn) throws MetamacException {
        return (DataStructureDefinitionVersionMetamac) dsdLifecycle.publishInternally(ctx, urn);
    }

    // TODO validTo, validFrom: ¿rellenar cuando el artefacto no sea del ISTAC? Pendiente decisión del ISTAC.
    public DataStructureDefinitionVersionMetamac publishExternallyDataStructureDefinition(ServiceContext ctx, String urn) throws MetamacException {
        return (DataStructureDefinitionVersionMetamac) dsdLifecycle.publishExternally(ctx, urn);
    }

    public void deleteDataStructureDefinitionVersionMetamac(ServiceContext ctx, String urn) throws MetamacException {
        dataStructureDefinitionService.deleteDataStructureDefinition(ctx, urn);
    }

    public DataStructureDefinitionVersionMetamac versioningDataStructureDefinition(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionType) throws MetamacException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("versioningDataStructureDefinition not implemented");

    }

    public DataStructureDefinitionVersionMetamac cancelDataStructureDefinitionVersionMetamacValidity(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        DsdsMetamacInvocationValidator.cancelDataStructureDefinitionVersionMetamacValidity(urn, null);

        // Retrieve version in specific procStatus
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = retrieveDataStructureDefinitionVersionByProcStatus(ctx, urn, ItemSchemeMetamacProcStatusEnum.EXTERNALLY_PUBLISHED);

        // Cancel validity
        dataStructureDefinitionVersionMetamac = (DataStructureDefinitionVersionMetamac) dataStructureDefinitionService.endDataStructureDefinitionValidity(ctx, urn);

        return dataStructureDefinitionVersionMetamac;
    }

    /**************************************************************************
     * PRIVATE
     *************************************************************************/
    /**
     * Retrieves version of a data structure definition in specific procStatus
     */
    private DataStructureDefinitionVersionMetamac retrieveDataStructureDefinitionVersionByProcStatus(ServiceContext ctx, String urn, ItemSchemeMetamacProcStatusEnum... procStatus)
            throws MetamacException {

        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(DataStructureDefinitionVersionMetamac.class)
                .withProperty(DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().urn()).eq(urn).withProperty(DataStructureDefinitionVersionMetamacProperties.procStatus())
                .in((Object[]) procStatus).distinctRoot().build();
        PagingParameter pagingParameter = PagingParameter.pageAccess(1);
        PagedResult<DataStructureDefinitionVersionMetamac> dataStructureDefinitionVersionMetamacPagedResult = getDataStructureDefinitionVersionMetamacRepository().findByCondition(conditions,
                pagingParameter);

        if (dataStructureDefinitionVersionMetamacPagedResult.getValues().size() != 1) {
            // check data structure definition exists to throws specific exception
            retrieveDataStructureDefinitionByUrn(ctx, urn);

            // if exists, throw exception about wrong proc status
            String[] procStatusString = SrmServiceUtils.procStatusEnumToString(procStatus);
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.DATA_STRUCTURE_DEFINITION_WRONG_PROC_STATUS).withMessageParameters(urn, procStatusString).build();
        }
        return dataStructureDefinitionVersionMetamacPagedResult.getValues().get(0);
    }

}
