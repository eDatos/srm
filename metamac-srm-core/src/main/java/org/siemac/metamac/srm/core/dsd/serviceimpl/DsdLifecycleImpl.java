package org.siemac.metamac.srm.core.dsd.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.base.domain.SrmLifecycleMetadata;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamacProperties;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamacRepository;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.structure.StructureLifecycle;
import org.siemac.metamac.srm.core.structure.StructureLifecycle.StructureLifecycleCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.domain.Structure;
import com.arte.statistic.sdmx.srm.core.base.domain.StructureVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.StructureVersionRepository;
import com.arte.statistic.sdmx.srm.core.structure.serviceapi.DataStructureDefinitionService;

@Service("dsdLifecycle")
public class DsdLifecycleImpl implements DsdLifecycle {

    @Autowired
    private StructureVersionRepository                      structureVersionRepository;

    @Autowired
    private DataStructureDefinitionVersionMetamacRepository dataStructureDefinitionVersionMetamacRepository;

    @Autowired
    private DataStructureDefinitionService                  dataStructureDefinitionService;

    private StructureLifecycle                              structureLifecycle = null;

    public DsdLifecycleImpl() {
        structureLifecycle = new StructureLifecycle(new DataStructureDefinitionLifecycleCallback());
    }

    @Override
    public StructureVersion sendToProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return structureLifecycle.sendToProductionValidation(ctx, urn);
    }

    @Override
    public StructureVersion sendToDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return structureLifecycle.sendToDiffusionValidation(ctx, urn);
    }

    @Override
    public StructureVersion rejectProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return structureLifecycle.rejectProductionValidation(ctx, urn);
    }

    @Override
    public StructureVersion rejectDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return structureLifecycle.rejectDiffusionValidation(ctx, urn);
    }

    @Override
    public StructureVersion publishInternally(ServiceContext ctx, String urn) throws MetamacException {
        return structureLifecycle.publishInternally(ctx, urn);
    }

    @Override
    public StructureVersion publishExternally(ServiceContext ctx, String urn) throws MetamacException {
        return structureLifecycle.publishExternally(ctx, urn);
    }

    private class DataStructureDefinitionLifecycleCallback implements StructureLifecycleCallback {

        @Override
        public SrmLifecycleMetadata getSrmLifecycleMetadata(StructureVersion structureVersion) {
            DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = getDataStructureDefinitionVersionMetamac(structureVersion);
            return dataStructureDefinitionVersionMetamac.getLifecycleMetadata();
        }

        @Override
        public StructureVersion retrieveStructureByProcStatus(String urn, ProcStatusEnum[] procStatus) throws MetamacException {
            return dataStructureDefinitionVersionMetamacRepository.retrieveDataStructureDefinitionVersionByProcStatus(urn, procStatus);
        }

        @Override
        public StructureVersion updateStructure(StructureVersion structureVersion) {
            return structureVersionRepository.save(structureVersion);
        }

        @Override
        public void checkAdditionalConditionsSinceSendToProductionValidation(StructureVersion structureVersion, List<MetamacExceptionItem> exceptions) {
            // TODO grouping validation tiene que hacerse aquí???
        }

        @Override
        public void checkAdditionalConditionsToPublishInternally(StructureVersion structureVersion, List<MetamacExceptionItem> exceptions) {

        }

        @Override
        public void checkAdditionalConditionsToPublishExternally(StructureVersion structureVersion, List<MetamacExceptionItem> exceptions) {

        }

        @Override
        public StructureVersion markStructureAsFinal(ServiceContext ctx, StructureVersion structureVersion) throws MetamacException {
            return dataStructureDefinitionService.markDataStructureAsFinal(ctx, structureVersion.getMaintainableArtefact().getUrn());
        }

        @Override
        public StructureVersion startStructureValidity(ServiceContext ctx, StructureVersion structureVersion) throws MetamacException {
            return dataStructureDefinitionService.startDataStructureDefinitionValidity(ctx, structureVersion.getMaintainableArtefact().getUrn());
        }

        @Override
        public List<StructureVersion> findStructureVersionsOfItemSchemeInProcStatus(ServiceContext ctx, Structure structure, ProcStatusEnum... procStatus) {

            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(DataStructureDefinitionVersionMetamac.class)
                    .withProperty(DataStructureDefinitionVersionMetamacProperties.structure().id()).eq(structure.getId())
                    .withProperty(DataStructureDefinitionVersionMetamacProperties.lifecycleMetadata().procStatus()).in((Object[]) procStatus).distinctRoot().build();
            PagingParameter pagingParameter = PagingParameter.noLimits();
            PagedResult<DataStructureDefinitionVersionMetamac> dataStructureDefinitionVersionMetamac = dataStructureDefinitionVersionMetamacRepository.findByCondition(conditions, pagingParameter);
            return dataStructureDefinitionMetamacVersionsToStructureVersions(dataStructureDefinitionVersionMetamac.getValues());
        }

        /**********************************************************************
         * PRIVATES
         **********************************************************************/
        private DataStructureDefinitionVersionMetamac getDataStructureDefinitionVersionMetamac(StructureVersion structureVersion) {
            return (DataStructureDefinitionVersionMetamac) structureVersion;
        }

        private List<StructureVersion> dataStructureDefinitionMetamacVersionsToStructureVersions(List<DataStructureDefinitionVersionMetamac> dataStructureDefinitionVersions) {
            List<StructureVersion> structureVersions = new ArrayList<StructureVersion>();
            for (DataStructureDefinitionVersionMetamac dataStructureVersion : dataStructureDefinitionVersions) {
                structureVersions.add((StructureVersion) dataStructureVersion);
            }
            return structureVersions;
        }
    }
}
