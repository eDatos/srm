package org.siemac.metamac.srm.core.dsd.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.common.LifeCycle;
import org.siemac.metamac.srm.core.common.SrmValidation;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils;
import org.siemac.metamac.srm.core.common.service.utils.SrmValidationUtils;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamacProperties;
import org.siemac.metamac.srm.core.dsd.domain.DimensionOrder;
import org.siemac.metamac.srm.core.dsd.serviceimpl.utils.DsdsMetamacInvocationValidator;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.domain.Component;
import com.arte.statistic.sdmx.srm.core.base.domain.ComponentList;
import com.arte.statistic.sdmx.srm.core.base.domain.StructureVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.StructureVersionRepository;
import com.arte.statistic.sdmx.srm.core.structure.domain.DataStructureDefinitionVersion;
import com.arte.statistic.sdmx.srm.core.structure.domain.DimensionComponent;
import com.arte.statistic.sdmx.srm.core.structure.domain.DimensionDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.serviceapi.DataStructureDefinitionService;
import com.arte.statistic.sdmx.srm.core.structure.serviceimpl.utils.StructureVersioningCopyUtils.StructureVersioningCopyCallback;

/**
 * Implementation of DsdsMetamacService.
 */
@Service("dsdsMetamacService")
public class DsdsMetamacServiceImpl extends DsdsMetamacServiceImplBase {

    @Autowired
    private DataStructureDefinitionService  dataStructureDefinitionService;

    @Autowired
    @Qualifier("dsdLifeCycle")
    private LifeCycle                       dsdLifeCycle;

    @Autowired
    private SrmValidation                   srmValidation;

    @Autowired
    @Qualifier("structureVersioningCopyCallbackMetamac")
    private StructureVersioningCopyCallback structureVersioningCopyCallback;

    @Autowired
    private StructureVersionRepository      structureVersionRepository;

    public DsdsMetamacServiceImpl() {
    }

    @Override
    public DataStructureDefinitionVersionMetamac createDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersion) throws MetamacException {
        // Fill and validate Data Structure Definition
        preCreateDataStructureDefinition(ctx, dataStructureDefinitionVersion);

        // Save
        return (DataStructureDefinitionVersionMetamac) dataStructureDefinitionService.createDataStructureDefinition(ctx, dataStructureDefinitionVersion, SrmConstants.VERSION_PATTERN_METAMAC);
    }

    @Override
    public DataStructureDefinitionVersionMetamac preCreateDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersion) throws MetamacException {
        // Validation
        DsdsMetamacInvocationValidator.checkCreateDataStructureDefinition(dataStructureDefinitionVersion, null);
        checkDataStructureDefinitionToCreateOrUpdate(ctx, dataStructureDefinitionVersion);

        // Fill metadata
        dataStructureDefinitionVersion.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
        dataStructureDefinitionVersion.getMaintainableArtefact().setIsExternalReference(Boolean.FALSE);
        dataStructureDefinitionVersion.getMaintainableArtefact().setFinalLogicClient(Boolean.FALSE);

        return dataStructureDefinitionVersion;
    }

    @Override
    public DataStructureDefinitionVersionMetamac updateDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersion) throws MetamacException {
        // Validation
        DsdsMetamacInvocationValidator.checkUpdateDataStructureDefinition(dataStructureDefinitionVersion, null);
        checkDataStructureDefinitionToCreateOrUpdate(ctx, dataStructureDefinitionVersion);

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
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = (DataStructureDefinitionVersionMetamac) dataStructureDefinitionService.retrieveDataStructureDefinitionByUrn(ctx,
                dataStructureDefinitionVersionUrn);

        // Validation
        DsdsMetamacInvocationValidator.checkDescriptorToCreateOrUpdate(componentList, null);
        checkDescriptorToCreateOrUpdate(ctx, dataStructureDefinitionVersionMetamac, componentList);

        return dataStructureDefinitionService.saveDescriptorForDataStructureDefinition(ctx, dataStructureDefinitionVersionUrn, componentList);
    }

    @Override
    public void deleteDescriptorForDataStructureDefinition(ServiceContext ctx, String dataStructureDefinitionVersionUrn, ComponentList componentList) throws MetamacException {

        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = (DataStructureDefinitionVersionMetamac) dataStructureDefinitionService.retrieveDataStructureDefinitionByUrn(ctx,
                dataStructureDefinitionVersionUrn);

        checkDataStructureDefinitionCanBeModified(dataStructureDefinitionVersionMetamac);

        dataStructureDefinitionService.deleteDescriptorForDataStructureDefinition(ctx, dataStructureDefinitionVersionUrn, componentList);
    }

    @Override
    public Component saveComponentForDataStructureDefinition(ServiceContext ctx, String dataStructureDefinitionVersionUrn, Component component) throws MetamacException {
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = (DataStructureDefinitionVersionMetamac) dataStructureDefinitionService.retrieveDataStructureDefinitionByUrn(ctx,
                dataStructureDefinitionVersionUrn);

        // Validation
        DsdsMetamacInvocationValidator.checkDescriptorToCreateOrUpdate(component, null);
        checkComponentToCreateOrUpdate(ctx, dataStructureDefinitionVersionMetamac, component);

        return dataStructureDefinitionService.saveComponentForDataStructureDefinition(ctx, dataStructureDefinitionVersionUrn, component);
    }

    @Override
    public void deleteComponentForDataStructureDefinition(ServiceContext ctx, String dataStructureDefinitionVersionUrn, Component component) throws MetamacException {
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = (DataStructureDefinitionVersionMetamac) dataStructureDefinitionService.retrieveDataStructureDefinitionByUrn(ctx,
                dataStructureDefinitionVersionUrn);

        checkDataStructureDefinitionCanBeModified(dataStructureDefinitionVersionMetamac);

        dataStructureDefinitionService.deleteComponentForDataStructureDefinition(ctx, dataStructureDefinitionVersionUrn, component);
    }

    @Override
    public void deleteDataStructureDefinition(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersion = retrieveDataStructureDefinitionByUrn(ctx, urn);
        checkDataStructureDefinitionCanBeModified(dataStructureDefinitionVersion);
        dataStructureDefinitionVersion.removeAllHeadingDimensions();
        dataStructureDefinitionVersion.removeAllStubDimensions();
        dataStructureDefinitionVersion.removeAllShowDecimalsPrecisions();
        // dataStructureDefinitionVersion.getMaintainableArtefact().setIsCodeUpdated(false);
        // dataStructureDefinitionService.updateDataStructureDefinition(ctx, dataStructureDefinitionVersion);
        // Delete
        dataStructureDefinitionService.deleteDataStructureDefinition(ctx, urn);
    }

    @Override
    public DataStructureDefinitionVersionMetamac versioningDataStructureDefinition(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionType) throws MetamacException {
        // Validation
        DsdsMetamacInvocationValidator.checkVersioningDataStructureDefinition(urnToCopy, versionType, null, null);
        checkDataStructureDefinitionVersioning(ctx, urnToCopy);

        // ConceptSchemeVersionMetamac conceptSchemeVersionToCopy = retrieveConceptSchemeByUrn(ctx, urnToCopy);
        // ConceptSchemeVersionMetamac conceptSchemeNewVersion = (ConceptSchemeVersionMetamac) conceptsService.versioningConceptScheme(ctx, urnToCopy, versionType, conceptCopyCallback);

        // Versioning
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacToCopy = retrieveDataStructureDefinitionByUrn(ctx, urnToCopy);
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacNewVersion = (DataStructureDefinitionVersionMetamac) dataStructureDefinitionService
                .versioningDataStructureDefinition(ctx, urnToCopy, versionType, structureVersioningCopyCallback);

        // Versioning heading and stub (metadata of Metamac). Note: other relations are copied in copy callback
        // Map of new dimension
        Map<String, Component> dimensionOrderMap = new HashMap<String, Component>();
        for (ComponentList componentList : dataStructureDefinitionVersionMetamacNewVersion.getGrouping()) {
            if (componentList instanceof DimensionDescriptor) {
                for (Component component : componentList.getComponents()) {
                    dimensionOrderMap.put(component.getCode(), component);
                }
                break;
            }
        }
        // Heading
        for (DimensionOrder dimensionOrder : dataStructureDefinitionVersionMetamacToCopy.getHeadingDimensions()) {
            DimensionComponent targetDimension = (DimensionComponent) dimensionOrderMap.get(dimensionOrder.getDimension().getCode());
            DimensionOrder targetDimensionOrder = new DimensionOrder();
            targetDimensionOrder.setDimension(targetDimension);
            targetDimensionOrder.setDimOrder(dimensionOrder.getDimOrder());
            dataStructureDefinitionVersionMetamacNewVersion.addHeadingDimension(targetDimensionOrder);
        }
        // Stub
        for (DimensionOrder dimensionOrder : dataStructureDefinitionVersionMetamacToCopy.getStubDimensions()) {
            DimensionComponent targetDimension = (DimensionComponent) dimensionOrderMap.get(dimensionOrder.getDimension().getCode());
            DimensionOrder targetDimensionOrder = new DimensionOrder();
            targetDimensionOrder.setDimension(targetDimension);
            targetDimensionOrder.setDimOrder(dimensionOrder.getDimOrder());
            dataStructureDefinitionVersionMetamacNewVersion.addStubDimension(targetDimensionOrder);
        }

        return dataStructureDefinitionVersionMetamacNewVersion;
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

    @Override
    public DataStructureDefinitionVersionMetamac publishExternallyDataStructureDefinition(ServiceContext ctx, String urn) throws MetamacException {
        return (DataStructureDefinitionVersionMetamac) dsdLifeCycle.publishExternally(ctx, urn);
    }

    @Override
    public DataStructureDefinitionVersionMetamac endDataStructureDefinitionValidity(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        DsdsMetamacInvocationValidator.checkEndValidity(urn, null);

        // Retrieve version in specific procStatus
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = retrieveDataStructureDefinitionVersionByProcStatus(ctx, urn, ProcStatusEnum.EXTERNALLY_PUBLISHED);

        // End validity
        dataStructureDefinitionVersionMetamac = (DataStructureDefinitionVersionMetamac) dataStructureDefinitionService.endDataStructureDefinitionValidity(ctx, urn, null);

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

    /**
     * Common validations to create or update a category scheme
     */
    private void checkDataStructureDefinitionToCreateOrUpdate(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersion) throws MetamacException {

        // When updating
        if (dataStructureDefinitionVersion.getId() != null) {
            // Proc status
            checkDataStructureDefinitionCanBeModified(dataStructureDefinitionVersion);

            // Code
            SrmValidationUtils.checkMaintainableArtefactCanChangeCodeIfChanged(dataStructureDefinitionVersion.getMaintainableArtefact());
        }

        // Maintainer
        srmValidation.checkMaintainer(ctx, dataStructureDefinitionVersion.getMaintainableArtefact(), dataStructureDefinitionVersion.getMaintainableArtefact().getIsImported());
    }

    /**
     * Common validations to create or update a componentList
     */
    private void checkDescriptorToCreateOrUpdate(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersion, ComponentList componentList) throws MetamacException {
        checkDataStructureDefinitionCanBeModified(dataStructureDefinitionVersion);
    }

    /**
     * Common validations to create or update a componentList
     */
    private void checkComponentToCreateOrUpdate(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersion, Component component) throws MetamacException {
        checkDataStructureDefinitionCanBeModified(dataStructureDefinitionVersion);
    }

    private void checkDataStructureDefinitionVersioning(ServiceContext ctx, String urnToCopy) throws MetamacException {

        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionToCopy = retrieveDataStructureDefinitionByUrn(ctx, urnToCopy);
        // Check version to copy is published
        SrmValidationUtils.checkArtefactCanBeVersioned(dataStructureDefinitionVersionToCopy.getLifeCycleMetadata(), urnToCopy);

        // Check does not exist any version 'no final'
        StructureVersion dataStructureVersionNoFinal = structureVersionRepository.findStructureVersionFinal(dataStructureDefinitionVersionToCopy.getStructure().getId());
        if (dataStructureVersionNoFinal != null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.MAINTAINABLE_ARTEFACT_VERSIONING_NOT_SUPPORTED)
                    .withMessageParameters(dataStructureVersionNoFinal.getMaintainableArtefact().getUrn()).build();
        }
    }

    private void checkDataStructureDefinitionCanBeModified(DataStructureDefinitionVersionMetamac dataStructureDefinitionVersion) throws MetamacException {
        SrmValidationUtils.checkArtefactCanBeModified(dataStructureDefinitionVersion.getLifeCycleMetadata(), dataStructureDefinitionVersion.getMaintainableArtefact().getUrn());
    }
}