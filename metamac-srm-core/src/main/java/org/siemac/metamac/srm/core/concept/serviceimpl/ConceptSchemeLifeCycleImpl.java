package org.siemac.metamac.srm.core.concept.serviceimpl;

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
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacRepository;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacRepository;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.ValidationUtils;
import com.arte.statistic.sdmx.srm.core.concept.serviceapi.ConceptsService;

@Service("conceptSchemeLifeCycle")
public class ConceptSchemeLifeCycleImpl extends LifeCycleImpl {

    @Autowired
    private ItemSchemeVersionRepository           itemSchemeVersionRepository;

    @Autowired
    private ConceptSchemeVersionMetamacRepository conceptSchemeVersionMetamacRepository;

    @Autowired
    private ConceptsService                       conceptsService;

    @Autowired
    private ConceptMetamacRepository              conceptMetamacRepository;

    public ConceptSchemeLifeCycleImpl() {
        this.callback = new ConceptSchemeLifeCycleCallback();
    }

    private class ConceptSchemeLifeCycleCallback implements LifeCycleCallback {

        @Override
        public SrmLifeCycleMetadata getLifeCycleMetadata(Object srmResourceVersion) {
            return getConceptSchemeVersionMetamac(srmResourceVersion).getLifeCycleMetadata();
        }

        @Override
        public MaintainableArtefact getMaintainableArtefact(Object srmResourceVersion) {
            return getConceptSchemeVersionMetamac(srmResourceVersion).getMaintainableArtefact();
        }

        @Override
        public Object updateSrmResource(Object srmResourceVersion) {
            return itemSchemeVersionRepository.save(getConceptSchemeVersionMetamac(srmResourceVersion));
        }

        @Override
        public Object retrieveSrmResourceByProcStatus(String urn, ProcStatusEnum[] procStatus) throws MetamacException {
            return conceptSchemeVersionMetamacRepository.retrieveConceptSchemeVersionByProcStatus(urn, procStatus);
        }

        @Override
        public void checkConcreteResourceInProductionValidation(Object srmResourceVersion, List<MetamacExceptionItem> exceptions) {

            ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptSchemeVersionMetamac(srmResourceVersion);

            // Metadata required
            ValidationUtils.checkMetadataRequired(conceptSchemeVersion.getIsPartial(), ServiceExceptionParameters.ITEM_SCHEME_IS_PARTIAL, exceptions);

            // One concept at least
            if (conceptSchemeVersion.getItems().size() == 0) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.ITEM_SCHEME_WITHOUT_ITEMS, conceptSchemeVersion.getMaintainableArtefact().getUrn()));
            }
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
            // note: role and extends concepts are already externally published when it is added
        }

        @Override
        public void checkConcreteResourceInExternallyPublished(Object srmResourceVersion, List<MetamacExceptionItem> exceptions) {
            // nothing
            // note: role and extends concepts are already externally published when it is added
        }

        @Override
        public Object markSrmResourceAsFinal(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            return conceptsService.markConceptSchemeAsFinal(ctx, getConceptSchemeVersionMetamac(srmResourceVersion).getMaintainableArtefact().getUrn());
        }

        @Override
        public Object startSrmResourceValidity(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            return conceptsService.startConceptSchemeValidity(ctx, getConceptSchemeVersionMetamac(srmResourceVersion).getMaintainableArtefact().getUrn(), null);
        }

        @Override
        public Object endSrmResourceValidity(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            return conceptsService.endConceptSchemeValidity(ctx, getConceptSchemeVersionMetamac(srmResourceVersion).getMaintainableArtefact().getUrn(), null);
        }

        @Override
        public List<Object> findSrmResourceVersionsOfSrmResourceInProcStatus(ServiceContext ctx, Object srmResourceVersion, ProcStatusEnum... procStatus) {

            ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptSchemeVersionMetamac(srmResourceVersion);

            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).withProperty(ConceptSchemeVersionMetamacProperties.itemScheme().id())
                    .eq(conceptSchemeVersion.getItemScheme().getId()).withProperty(ConceptSchemeVersionMetamacProperties.lifeCycleMetadata().procStatus()).in((Object[]) procStatus).distinctRoot()
                    .build();
            PagingParameter pagingParameter = PagingParameter.noLimits();
            PagedResult<ConceptSchemeVersionMetamac> conceptSchemeVersionPagedResult = conceptSchemeVersionMetamacRepository.findByCondition(conditions, pagingParameter);
            return conceptSchemeMetamacToObject(conceptSchemeVersionPagedResult.getValues());
        }

        @Override
        public MetamacExceptionItem buildExceptionItemWrongProcStatus(Object srmResourceVersion, String[] procStatusExpecteds) {
            ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptSchemeVersionMetamac(srmResourceVersion);
            return MetamacExceptionItemBuilder.metamacExceptionItem().withCommonServiceExceptionType(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS)
                    .withMessageParameters(conceptSchemeVersion.getMaintainableArtefact().getUrn(), procStatusExpecteds).build();
        }

        @Override
        public Boolean canHaveCategorisations() {
            return Boolean.TRUE;
        }

        private ConceptSchemeVersionMetamac getConceptSchemeVersionMetamac(Object srmResource) {
            return (ConceptSchemeVersionMetamac) srmResource;
        }

        private List<Object> conceptSchemeMetamacToObject(List<ConceptSchemeVersionMetamac> conceptSchemeVersions) {
            List<Object> objects = new ArrayList<Object>();
            for (ConceptSchemeVersionMetamac conceptSchemeVersion : conceptSchemeVersions) {
                objects.add((ItemSchemeVersion) conceptSchemeVersion);
            }
            return objects;
        }
    }
}
