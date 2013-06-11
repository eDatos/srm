package org.siemac.metamac.srm.core.concept.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.MetamacExceptionItemBuilder;
import org.siemac.metamac.core.common.util.shared.VersionUtil;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.common.LifeCycleImpl;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacRepository;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsMetamacService;
import org.siemac.metamac.srm.core.concept.serviceimpl.utils.ConceptsMetamacInvocationValidator;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.base.serviceapi.BaseService;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.ValidationUtils;
import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptRepository;
import com.arte.statistic.sdmx.srm.core.concept.serviceapi.ConceptsService;

@Service("conceptSchemeLifeCycle")
public class ConceptSchemeLifeCycleImpl extends LifeCycleImpl {

    @Autowired
    private ItemSchemeVersionRepository           itemSchemeVersionRepository;

    @Autowired
    private ItemRepository                        itemRepository;

    @Autowired
    private ConceptRepository                     conceptRepository;

    @Autowired
    private ConceptSchemeVersionMetamacRepository conceptSchemeVersionMetamacRepository;

    @Autowired
    private ConceptsService                       conceptsService;

    @Autowired
    private ConceptsMetamacService                conceptsMetamacService;

    @Autowired
    private BaseService                           baseService;

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
        public Object updateSrmResource(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptSchemeVersionMetamac(srmResourceVersion);
            // Update item scheme
            baseService.updateItemSchemeLastUpdated(ctx, conceptSchemeVersion);
            // Update item scheme version
            return itemSchemeVersionRepository.save(conceptSchemeVersion);
        }

        @Override
        public Object retrieveSrmResourceByProcStatus(String urn, ProcStatusEnum[] procStatus) throws MetamacException {
            return conceptSchemeVersionMetamacRepository.retrieveConceptSchemeVersionByProcStatus(urn, procStatus);
        }

        @Override
        public Object executeBeforeSendProductionValidation(ServiceContext ctx, Object srmResourceVersion) {
            ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptSchemeVersionMetamac(srmResourceVersion);
            // Assign variable automatically
            for (Item item : conceptSchemeVersion.getItems()) {
                ConceptMetamac concept = (ConceptMetamac) item;
                boolean assigned = SrmServiceUtils.assignToConceptSameVariableOfCodelist(conceptSchemeVersion, concept);
                if (assigned) {
                    itemRepository.save(concept);
                }
            }
            return srmResourceVersion;
        }

        @Override
        public void checkConcreteResourceInProductionValidation(ServiceContext ctx, Object srmResourceVersion, ProcStatusEnum targetStatus, List<MetamacExceptionItem> exceptions)
                throws MetamacException {

            ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptSchemeVersionMetamac(srmResourceVersion);

            // Metadata required
            ValidationUtils.checkMetadataRequired(conceptSchemeVersion.getIsPartial(), ServiceExceptionParameters.ITEM_SCHEME_IS_PARTIAL, exceptions);

            // One concept at least
            Long itemsCount = conceptRepository.countItems(conceptSchemeVersion.getId());
            if (itemsCount == 0) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.ITEM_SCHEME_WITHOUT_ITEMS, conceptSchemeVersion.getMaintainableArtefact().getUrn()));
            }

            // Check some metadata that can be empty or incorrect when importing. In not imported, metadata is checked in create/update operations
            // Note: this is a unefficient operation, so only check when send to production and not in next status.
            // In another status, if metadata is already changed to wrong value, they will be checked in create/update concept operations
            if (conceptSchemeVersion.getMaintainableArtefact().getIsImported() && ProcStatusEnum.PRODUCTION_VALIDATION.equals(targetStatus)) {
                List<MetamacExceptionItem> exceptionsConceptScheme = new ArrayList<MetamacExceptionItem>();
                conceptSchemeVersion.setIsTypeUpdated(Boolean.FALSE);
                ConceptsMetamacInvocationValidator.checkConceptScheme(conceptSchemeVersion, false, exceptionsConceptScheme);
                if (exceptionsConceptScheme.size() != 0) {
                    exceptions.addAll(exceptionsConceptScheme);
                } else {
                    // only check concepts if concept scheme is complete
                    for (Item item : conceptSchemeVersion.getItems()) {
                        ConceptMetamac concept = (ConceptMetamac) item;
                        List<MetamacExceptionItem> exceptionsConcepts = new ArrayList<MetamacExceptionItem>();
                        // Check misc metadata
                        ConceptsMetamacInvocationValidator.checkConcept(conceptSchemeVersion, concept, false, false, false, exceptionsConcepts);
                        // Check core representation
                        MetamacExceptionItem exceptionItem = conceptsMetamacService.checkConceptEnumeratedRepresentation(ctx, concept, false, conceptSchemeVersion.getMaintainableArtefact()
                                .getIsImported());
                        if (exceptionItem != null) {
                            exceptionsConcepts.add(exceptionItem);
                        }
                        // Group exceptions by concept
                        if (exceptionsConcepts.size() != 0) {
                            MetamacExceptionItem exceptionItemConcept = MetamacExceptionItemBuilder.metamacExceptionItem()
                                    .withCommonServiceExceptionType(ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA).withMessageParameters(item.getNameableArtefact().getUrn())
                                    .withExceptionItems(exceptionsConcepts).build();
                            exceptions.add(exceptionItemConcept);
                        }
                    }
                }
            }
        }

        @Override
        public void checkConcreteResourceInDiffusionValidation(Object srmResourceVersion, ProcStatusEnum targetStatus, List<MetamacExceptionItem> exceptions) {
            // nothing
        }

        @Override
        public void checkConcreteResourceInRejectProductionValidation(Object srmResourceVersion, ProcStatusEnum targetStatus, List<MetamacExceptionItem> exceptions) {
            // nothing
        }

        @Override
        public void checkConcreteResourceInRejectDiffusionValidation(Object srmResourceVersion, ProcStatusEnum targetStatus, List<MetamacExceptionItem> exceptions) {
            // nothing
        }

        @Override
        public void checkConcreteResourceInInternallyPublished(ServiceContext ctx, Object srmResourceVersion, ProcStatusEnum targetStatus, List<MetamacExceptionItem> exceptions) {
            // nothing
            // note: role and extends concepts are already externally published when they are added
            // note: enumerated representation is checked in statistic module, in markAsFinal operation
        }

        @Override
        public Map<String, MetamacExceptionItem> checkConcreteResourceTranslations(ServiceContext ctx, Object srmResourceVersion, String locale) throws MetamacException {
            return conceptsMetamacService.checkConceptSchemeVersionTranslations(ctx, getConceptSchemeVersionMetamac(srmResourceVersion).getId(), locale);
        }

        @Override
        public Object publishInternallyConcreteResource(ServiceContext ctx, Object srmResourceVersion) {
            // nothing
            return srmResourceVersion;
        }

        @Override
        public void checkConcreteResourceInExternallyPublished(Object srmResourceVersion, ProcStatusEnum targetStatus, List<MetamacExceptionItem> exceptions) {
            // nothing
            // note: role and extends concepts are already externally published when they are added
        }

        @Override
        public Object markSrmResourceAsFinal(ServiceContext ctx, Object srmResourceVersion, Boolean forceLastestFinal) throws MetamacException {
            return conceptsService.markConceptSchemeAsFinal(ctx, getConceptSchemeVersionMetamac(srmResourceVersion).getMaintainableArtefact().getUrn(), forceLastestFinal);
        }

        @Override
        public Object markSrmResourceAsPublic(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            return conceptsService.markConceptSchemeAsPublic(ctx, getConceptSchemeVersionMetamac(srmResourceVersion).getMaintainableArtefact().getUrn());
        }

        @Override
        public Object startSrmResourceValidity(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            return conceptsMetamacService.startConceptSchemeValidity(ctx, getConceptSchemeVersionMetamac(srmResourceVersion).getMaintainableArtefact().getUrn());
        }

        @Override
        public Object endSrmResourceValidity(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            return conceptsMetamacService.endConceptSchemeValidity(ctx, getConceptSchemeVersionMetamac(srmResourceVersion).getMaintainableArtefact().getUrn());
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

        @Override
        public Object mergeTemporal(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            ConceptSchemeVersionMetamac conceptSchemeVersionMetamac = (ConceptSchemeVersionMetamac) srmResourceVersion;
            if (VersionUtil.isTemporalVersion(conceptSchemeVersionMetamac.getMaintainableArtefact().getVersionLogic())) {
                return conceptsMetamacService.mergeTemporalVersion(ctx, conceptSchemeVersionMetamac);
            }
            return srmResourceVersion;
        }

        @Override
        public Boolean isTemporalToPublishExternally(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            ConceptSchemeVersionMetamac conceptSchemeVersionMetamac = (ConceptSchemeVersionMetamac) srmResourceVersion;
            if (ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(conceptSchemeVersionMetamac.getLifeCycleMetadata().getProcStatus())) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }

        private ConceptSchemeVersionMetamac getConceptSchemeVersionMetamac(Object srmResource) {
            return (ConceptSchemeVersionMetamac) srmResource;
        }

        private List<Object> conceptSchemeMetamacToObject(List<ConceptSchemeVersionMetamac> conceptSchemeVersions) {
            List<Object> objects = new ArrayList<Object>(conceptSchemeVersions.size());
            for (ConceptSchemeVersionMetamac conceptSchemeVersion : conceptSchemeVersions) {
                objects.add(conceptSchemeVersion);
            }
            return objects;
        }
    }
}
