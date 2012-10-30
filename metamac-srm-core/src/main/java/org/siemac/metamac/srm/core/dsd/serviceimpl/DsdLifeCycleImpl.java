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
import org.siemac.metamac.core.common.exception.MetamacExceptionItemBuilder;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.common.LifeCycleImpl;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamacProperties;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamacRepository;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.StructureVersionRepository;
import com.arte.statistic.sdmx.srm.core.structure.serviceapi.DataStructureDefinitionService;

@Service("dsdLifeCycle")
public class DsdLifeCycleImpl extends LifeCycleImpl {

    @Autowired
    private StructureVersionRepository                      structureVersionRepository;

    @Autowired
    private DataStructureDefinitionVersionMetamacRepository dataStructureDefinitionVersionMetamacRepository;

    @Autowired
    private DataStructureDefinitionService                  dataStructureDefinitionService;

    public DsdLifeCycleImpl() {
        this.callback = new DataStructureDefinitionLifeCycleCallback();
    }

    private class DataStructureDefinitionLifeCycleCallback implements LifeCycleCallback {

        @Override
        public SrmLifeCycleMetadata getLifeCycleMetadata(Object srmResourceVersion) {
            return getDataStructureDefinitionVersionMetamac(srmResourceVersion).getLifeCycleMetadata();
        }

        @Override
        public MaintainableArtefact getMaintainableArtefact(Object srmResourceVersion) {
            return getDataStructureDefinitionVersionMetamac(srmResourceVersion).getMaintainableArtefact();
        }

        @Override
        public Object updateSrmResource(Object srmResourceVersion) {
            return structureVersionRepository.save(getDataStructureDefinitionVersionMetamac(srmResourceVersion));
        }

        @Override
        public Object retrieveSrmResourceByProcStatus(String urn, ProcStatusEnum[] procStatus) throws MetamacException {
            return dataStructureDefinitionVersionMetamacRepository.retrieveDataStructureDefinitionVersionByProcStatus(urn, procStatus);
        }

        @Override
        public void checkConcreteResourceInProductionValidation(Object srmResourceVersion, List<MetamacExceptionItem> exceptions) {
            // TODO grouping validation tiene que hacerse aquí???
        }

        @Override
        public void checkConcreteResourceInDiffusionValidation(Object srmResourceVersion, List<MetamacExceptionItem> exceptions) {
            // nothing
        }

        @Override
        public void checkConcreteResourceInRejectProductionValidation(Object srmResourceVersion, List<MetamacExceptionItem> exceptions) {
            // nothing
        }

        @Override
        public void checkConcreteResourceInRejectDiffusionValidation(Object srmResourceVersion, List<MetamacExceptionItem> exceptions) {
            // nothing
        }

        @Override
        public void checkConcreteResourceInInternallyPublished(Object srmResourceVersion, List<MetamacExceptionItem> exceptions) {
            // nothing
        }

        @Override
        public void checkConcreteResourceInExternallyPublished(Object srmResourceVersion, List<MetamacExceptionItem> exceptions) {
            // nothing
        }

        @Override
        public Object markSrmResourceAsFinal(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            return dataStructureDefinitionService.markDataStructureAsFinal(ctx, getDataStructureDefinitionVersionMetamac(srmResourceVersion).getMaintainableArtefact().getUrn());
        }

        @Override
        public Object startSrmResourceValidity(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            return dataStructureDefinitionService.startDataStructureDefinitionValidity(ctx, getDataStructureDefinitionVersionMetamac(srmResourceVersion).getMaintainableArtefact().getUrn());
        }

        public Object endSrmResourceValidity(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            return dataStructureDefinitionService.endDataStructureDefinitionValidity(ctx, getDataStructureDefinitionVersionMetamac(srmResourceVersion).getMaintainableArtefact().getUrn());
        }

        @Override
        public List<Object> findSrmResourceVersionsOfSrmResourceInProcStatus(ServiceContext ctx, Object srmResourceVersion, ProcStatusEnum... procStatus) {

            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(DataStructureDefinitionVersionMetamac.class)
                    .withProperty(DataStructureDefinitionVersionMetamacProperties.structure().id()).eq(getDataStructureDefinitionVersionMetamac(srmResourceVersion).getStructure().getId())
                    .withProperty(DataStructureDefinitionVersionMetamacProperties.lifeCycleMetadata().procStatus()).in((Object[]) procStatus).distinctRoot().build();
            PagingParameter pagingParameter = PagingParameter.noLimits();
            PagedResult<DataStructureDefinitionVersionMetamac> dataStructureDefinitionVersionMetamac = dataStructureDefinitionVersionMetamacRepository.findByCondition(conditions, pagingParameter);
            return dataStructureDefinitionMetamacVersionsToObject(dataStructureDefinitionVersionMetamac.getValues());
        }

        @Override
        public MetamacExceptionItem buildExceptionItemWrongProcStatus(Object srmResourceVersion, String[] procStatusExpecteds) {
            return MetamacExceptionItemBuilder.metamacExceptionItem().withCommonServiceExceptionType(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS)
                    .withMessageParameters(getDataStructureDefinitionVersionMetamac(srmResourceVersion).getMaintainableArtefact().getUrn(), procStatusExpecteds).build();
        }

        @Override
        public Boolean canHaveCategorisations() {
            return Boolean.TRUE;
        }

        /**********************************************************************
         * PRIVATES
         **********************************************************************/
        private DataStructureDefinitionVersionMetamac getDataStructureDefinitionVersionMetamac(Object srmResourceVersion) {
            return (DataStructureDefinitionVersionMetamac) srmResourceVersion;
        }

        private List<Object> dataStructureDefinitionMetamacVersionsToObject(List<DataStructureDefinitionVersionMetamac> dataStructureDefinitionVersions) {
            List<Object> structureVersions = new ArrayList<Object>();
            for (DataStructureDefinitionVersionMetamac dataStructureVersion : dataStructureDefinitionVersions) {
                structureVersions.add((Object) dataStructureVersion);
            }
            return structureVersions;
        }
    }
}
