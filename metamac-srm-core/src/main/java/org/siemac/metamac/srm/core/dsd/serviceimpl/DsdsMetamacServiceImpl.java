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
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.common.LifeCycle;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamacProperties;
import org.siemac.metamac.srm.core.dsd.serviceimpl.utils.DsdsMetamacInvocationValidator;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.serviceimpl.SrmServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.domain.Component;
import com.arte.statistic.sdmx.srm.core.base.domain.ComponentList;
import com.arte.statistic.sdmx.srm.core.structure.domain.DataStructureDefinitionVersion;
import com.arte.statistic.sdmx.srm.core.structure.serviceapi.DataStructureDefinitionService;
import com.arte.statistic.sdmx.srm.core.structure.serviceimpl.utils.StructureDoCopyUtils.StructureCopyCallback;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeComponentList;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.VersionTypeEnum;

/**
 * Implementation of DsdsMetamacService.
 */
@Service("dsdsMetamacService")
public class DsdsMetamacServiceImpl extends DsdsMetamacServiceImplBase {

    @Autowired
    private DataStructureDefinitionService dataStructureDefinitionService;

    @Autowired
    @Qualifier("dsdLifeCycle")
    private LifeCycle                      dsdLifeCycle;

    @Autowired
    @Qualifier("structureCopyCallbackMetamac")
    private StructureCopyCallback          structureCopyCallback;

    public DsdsMetamacServiceImpl() {
    }

    @Override
    public DataStructureDefinitionVersionMetamac createDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersion) throws MetamacException {
        // Validation
        DsdsMetamacInvocationValidator.checkCreateDataStructureDefinition(dataStructureDefinitionVersion, null);

        // Fill metadata
        dataStructureDefinitionVersion.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
        dataStructureDefinitionVersion.getMaintainableArtefact().setIsExternalReference(Boolean.FALSE);

        // Save
        return (DataStructureDefinitionVersionMetamac) dataStructureDefinitionService.createDataStructureDefinition(ctx, dataStructureDefinitionVersion, SrmConstants.VERSION_PATTERN_METAMAC);
    }

    @Override
    public DataStructureDefinitionVersionMetamac updateDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersion) throws MetamacException {
        // Validation
        DsdsMetamacInvocationValidator.checkUpdateDataStructureDefinition(dataStructureDefinitionVersion, null);
        // dataStructureDefinitionService checks data structure definition isn't final (Schemes cannot be updated when procStatus is INTERNALLY_PUBLISHED or EXTERNALLY_PUBLISHED)

        // Save
        return (DataStructureDefinitionVersionMetamac) dataStructureDefinitionService.updateDataStructureDefinition(ctx, dataStructureDefinitionVersion);
    }

    @Override
    public DataStructureDefinitionVersionMetamac retrieveDataStructureDefinitionByUrn(ServiceContext ctx, String urn) throws MetamacException {
        return (DataStructureDefinitionVersionMetamac) dataStructureDefinitionService.retrieveDataStructureDefinitionByUrn(ctx, urn);
    }

    @Override
    public List<DataStructureDefinitionVersionMetamac> retrieveDataStructureDefinitionVersions(ServiceContext ctx, String urn) throws MetamacException {
        // Retrieve dataStructureDefinitionVersions
        List<DataStructureDefinitionVersion> dataStructureDefinitionVersions = dataStructureDefinitionService.retrieveDataStructureDefinitionVersions(ctx, urn);

        // Typecast to DataStructureDefinitionVersionMetamac
        return dataStructureDefinitionVersionsToMetamac(dataStructureDefinitionVersions);
    }

    @Override
    public PagedResult<DataStructureDefinitionVersionMetamac> findDataStructureDefinitionsByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter)
            throws MetamacException {

        PagedResult<DataStructureDefinitionVersion> dataStructureDefinitionVersionPagedResult = dataStructureDefinitionService.findDataStructureDefinitionByCondition(ctx, conditions, pagingParameter);
        return pagedResultDataStructureDefinitionVersionToMetamac(dataStructureDefinitionVersionPagedResult);
    }

    @Override
    public ComponentList saveDescriptorForDataStructureDefinition(ServiceContext ctx, String dataStructureDefinitionVersionUrn, ComponentList componentList) throws MetamacException {
        return dataStructureDefinitionService.saveDescriptorForDataStructureDefinition(ctx, dataStructureDefinitionVersionUrn, componentList);
    }

    @Override
    public void deleteDescriptorForDataStructureDefinition(ServiceContext ctx, String dataStructureDefinitionVersionUrn, ComponentList componentList) throws MetamacException {
        dataStructureDefinitionService.deleteDescriptorForDataStructureDefinition(ctx, dataStructureDefinitionVersionUrn, componentList);
    }

    @Override
    public Component saveComponentForDataStructureDefinition(ServiceContext ctx, String dataStructureDefinitionVersionUrn, Component component, TypeComponentList typeComponentList)
            throws MetamacException {
        return dataStructureDefinitionService.saveComponentForDataStructureDefinition(ctx, dataStructureDefinitionVersionUrn, component, typeComponentList);
    }

    @Override
    public void deleteComponentForDataStructureDefinition(ServiceContext ctx, String dataStructureDefinitionVersionUrn, Component component, TypeComponentList typeComponentList)
            throws MetamacException {
        dataStructureDefinitionService.deleteComponentForDataStructureDefinition(ctx, dataStructureDefinitionVersionUrn, component, typeComponentList);
    }

    @Override
    public DataStructureDefinitionVersionMetamac sendDataStructureDefinitionToProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (DataStructureDefinitionVersionMetamac) dsdLifeCycle.sendToProductionValidation(ctx, urn);
    }

    @Override
    public DataStructureDefinitionVersionMetamac sendDataStructureDefinitionToDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (DataStructureDefinitionVersionMetamac) dsdLifeCycle.sendToDiffusionValidation(ctx, urn);
    }

    @Override
    public DataStructureDefinitionVersionMetamac rejectDataStructureDefinitionProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (DataStructureDefinitionVersionMetamac) dsdLifeCycle.rejectProductionValidation(ctx, urn);
    }

    @Override
    public DataStructureDefinitionVersionMetamac rejectDataStructureDefinitionDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (DataStructureDefinitionVersionMetamac) dsdLifeCycle.rejectDiffusionValidation(ctx, urn);
    }

    @Override
    public DataStructureDefinitionVersionMetamac publishInternallyDataStructureDefinition(ServiceContext ctx, String urn) throws MetamacException {
        return (DataStructureDefinitionVersionMetamac) dsdLifeCycle.publishInternally(ctx, urn);
    }

    // TODO validTo, validFrom: ¿rellenar cuando el artefacto no sea del ISTAC? Pendiente decisión del ISTAC.
    @Override
    public DataStructureDefinitionVersionMetamac publishExternallyDataStructureDefinition(ServiceContext ctx, String urn) throws MetamacException {
        return (DataStructureDefinitionVersionMetamac) dsdLifeCycle.publishExternally(ctx, urn);
    }

    @Override
    public void deleteDataStructureDefinition(ServiceContext ctx, String urn) throws MetamacException {
        dataStructureDefinitionService.deleteDataStructureDefinition(ctx, urn);
    }

    @Override
    public DataStructureDefinitionVersionMetamac versioningDataStructureDefinition(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionType) throws MetamacException {
        // Validation
        DsdsMetamacInvocationValidator.checkVersioningDataStructureDefinition(urnToCopy, versionType, null, null);
        // Validations of scheme final, ... are done in sdmx module

        // Versioning
        return (DataStructureDefinitionVersionMetamac) dataStructureDefinitionService.versioningDataStructureDefinition(ctx, urnToCopy, versionType, structureCopyCallback);
    }

    @Override
    public DataStructureDefinitionVersionMetamac importDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersion) throws MetamacException {
        // Validation
        DsdsMetamacInvocationValidator.checkImportDataStructureDefinition(dataStructureDefinitionVersion, true, null);

        // Fill metadata. The Metamac Metadata not copied through imported versions.
        dataStructureDefinitionVersion.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
        dataStructureDefinitionVersion.getMaintainableArtefact().setIsExternalReference(Boolean.FALSE);

        // Import
        return (DataStructureDefinitionVersionMetamac) dataStructureDefinitionService.importDataStructureDefinition(ctx, dataStructureDefinitionVersion, structureCopyCallback);
    }

    @Override
    public DataStructureDefinitionVersionMetamac endDataStructureDefinitionValidity(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        DsdsMetamacInvocationValidator.checkEndDataStructureDefinitionValidity(urn, null);

        // Retrieve version in specific procStatus
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = retrieveDataStructureDefinitionVersionByProcStatus(ctx, urn, ProcStatusEnum.EXTERNALLY_PUBLISHED);

        // End validity
        dataStructureDefinitionVersionMetamac = (DataStructureDefinitionVersionMetamac) dataStructureDefinitionService.endDataStructureDefinitionValidity(ctx, urn);

        return dataStructureDefinitionVersionMetamac;
    }

    /**************************************************************************
     * PRIVATE
     *************************************************************************/
    /**
     * Retrieves version of a data structure definition in specific procStatus
     */
    private DataStructureDefinitionVersionMetamac retrieveDataStructureDefinitionVersionByProcStatus(ServiceContext ctx, String urn, ProcStatusEnum... procStatus) throws MetamacException {

        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(DataStructureDefinitionVersionMetamac.class)
                .withProperty(DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().urn()).eq(urn)
                .withProperty(DataStructureDefinitionVersionMetamacProperties.lifeCycleMetadata().procStatus()).in((Object[]) procStatus).distinctRoot().build();
        PagingParameter pagingParameter = PagingParameter.pageAccess(1);
        PagedResult<DataStructureDefinitionVersionMetamac> dataStructureDefinitionVersionMetamacPagedResult = getDataStructureDefinitionVersionMetamacRepository().findByCondition(conditions,
                pagingParameter);

        if (dataStructureDefinitionVersionMetamacPagedResult.getValues().size() != 1) {
            // check data structure definition exists to throws specific exception
            retrieveDataStructureDefinitionByUrn(ctx, urn);

            // if exists, throw exception about wrong proc status
            String[] procStatusString = SrmServiceUtils.procStatusEnumToString(procStatus);
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS).withMessageParameters(urn, procStatusString).build();
        }
        return dataStructureDefinitionVersionMetamacPagedResult.getValues().get(0);
    }

    /**
     * Typecast to Metamac type
     */
    private List<DataStructureDefinitionVersionMetamac> dataStructureDefinitionVersionsToMetamac(List<DataStructureDefinitionVersion> sources) {
        List<DataStructureDefinitionVersionMetamac> targets = new ArrayList<DataStructureDefinitionVersionMetamac>();
        for (DataStructureDefinitionVersion source : sources) {
            targets.add((DataStructureDefinitionVersionMetamac) source);
        }
        return targets;
    }

    /**
     * Typecast to Metamac type
     */
    private PagedResult<DataStructureDefinitionVersionMetamac> pagedResultDataStructureDefinitionVersionToMetamac(PagedResult<DataStructureDefinitionVersion> source) {
        List<DataStructureDefinitionVersionMetamac> dataStructureDefinitionVersionsMetamac = dataStructureDefinitionVersionsToMetamac(source.getValues());
        return new PagedResult<DataStructureDefinitionVersionMetamac>(dataStructureDefinitionVersionsMetamac, source.getStartRow(), source.getRowCount(), source.getPageSize(), source.getTotalRows(),
                source.getAdditionalResultRows());
    }
}