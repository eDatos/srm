package org.siemac.metamac.srm.core.concept.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacRepository;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacRepository;
import org.siemac.metamac.srm.core.enume.domain.ItemSchemeMetamacProcStatusEnum;
import org.siemac.metamac.srm.core.itemscheme.ItemSchemeLifecycle;
import org.siemac.metamac.srm.core.itemscheme.ItemSchemeLifecycle.ItemSchemeLifecycleCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemScheme;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.concept.serviceapi.ConceptsService;

@Service("conceptSchemeLifecycle")
public class ConceptSchemeLifecycleImpl implements ConceptSchemeLifecycle {

    @Autowired
    private ItemSchemeVersionRepository           itemSchemeVersionRepository;

    @Autowired
    private ConceptSchemeVersionMetamacRepository conceptSchemeVersionMetamacRepository;

    @Autowired
    private ConceptsService                       conceptsService;

    @Autowired
    private ConceptMetamacRepository              conceptMetamacRepository;

    private ItemSchemeLifecycle                   itemSchemeLifecycle = null;

    public ConceptSchemeLifecycleImpl() {
        itemSchemeLifecycle = new ItemSchemeLifecycle(new ConceptSchemeLifecycleCallback());
    }

    @Override
    public ItemSchemeVersion sendToProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return itemSchemeLifecycle.sendToProductionValidation(ctx, urn);
    }

    @Override
    public ItemSchemeVersion sendToDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return itemSchemeLifecycle.sendToDiffusionValidation(ctx, urn);
    }

    @Override
    public ItemSchemeVersion rejectProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return itemSchemeLifecycle.rejectProductionValidation(ctx, urn);
    }

    @Override
    public ItemSchemeVersion rejectDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return itemSchemeLifecycle.rejectDiffusionValidation(ctx, urn);
    }

    @Override
    public ItemSchemeVersion publishInternally(ServiceContext ctx, String urn) throws MetamacException {
        return itemSchemeLifecycle.publishInternally(ctx, urn);
    }
    
    @Override
    public ItemSchemeVersion publishExternally(ServiceContext ctx, String urn) throws MetamacException {
        return itemSchemeLifecycle.publishExternally(ctx, urn);
    }

    private class ConceptSchemeLifecycleCallback implements ItemSchemeLifecycleCallback {

        @Override
        public ItemSchemeVersion updateItemScheme(ItemSchemeVersion itemSchemeVersion) {
            return itemSchemeVersionRepository.save(itemSchemeVersion);
        }

        @Override
        public ItemSchemeVersion retrieveItemSchemeByProcStatus(String urn, ItemSchemeMetamacProcStatusEnum[] procStatus) throws MetamacException {
            return conceptSchemeVersionMetamacRepository.retrieveConceptSchemeVersionByProcStatus(urn, procStatus);
        }

        @Override
        public void setItemSchemeProcStatusAndInformationStatus(ItemSchemeVersion itemSchemeVersion, ItemSchemeMetamacProcStatusEnum newProcStatus, String user) {

            ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptSchemeVersionMetamac(itemSchemeVersion);

            // Proc status
            conceptSchemeVersion.setProcStatus(newProcStatus);

            // User and date
            switch (newProcStatus) {
                case PRODUCTION_VALIDATION:
                    conceptSchemeVersion.setProductionValidationDate(new DateTime());
                    conceptSchemeVersion.setProductionValidationUser(user);
                    break;
                case DIFFUSION_VALIDATION:
                    conceptSchemeVersion.setDiffusionValidationDate(new DateTime());
                    conceptSchemeVersion.setDiffusionValidationUser(user);
                    break;
                case VALIDATION_REJECTED:
                    conceptSchemeVersion.setProductionValidationDate(null);
                    conceptSchemeVersion.setProductionValidationUser(null);
                    conceptSchemeVersion.setDiffusionValidationDate(null);
                    conceptSchemeVersion.setDiffusionValidationUser(null);
                    break;
                case INTERNALLY_PUBLISHED:
                    conceptSchemeVersion.setInternalPublicationDate(new DateTime());
                    conceptSchemeVersion.setInternalPublicationUser(user);
                    break;
                case EXTERNALLY_PUBLISHED:
                    conceptSchemeVersion.setExternalPublicationDate(new DateTime());
                    conceptSchemeVersion.setExternalPublicationUser(user);
                    break;
                default:
                    throw new IllegalArgumentException("unsupported: " + newProcStatus);
            }
        }
        
        @Override
        public ItemSchemeMetamacProcStatusEnum getProcStatusMetadataValue(ItemSchemeVersion itemSchemeVersion) {
            ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptSchemeVersionMetamac(itemSchemeVersion);
            return conceptSchemeVersion.getProcStatus();
        }

        @Override
        public DateTime getExternalPublicationDateMetadataValue(ItemSchemeVersion itemSchemeVersion) {
            ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptSchemeVersionMetamac(itemSchemeVersion);
            return conceptSchemeVersion.getExternalPublicationDate();
        }
        

        @Override
        public void checkAdditionalConditionsSinceSendToProductionValidation(ItemSchemeVersion itemSchemeVersion, List<MetamacExceptionItem> exceptions) {

            ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptSchemeVersionMetamac(itemSchemeVersion);

            // One concept at least
            if (conceptSchemeVersion.getItems().size() == 0) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.CONCEPT_SCHEME_WITHOUT_CONCEPTS, conceptSchemeVersion.getMaintainableArtefact().getUrn()));
            }
        }

        @Override
        public void checkAdditionalConditionsToPublishInternally(ItemSchemeVersion itemSchemeVersion, List<MetamacExceptionItem> exceptions) {

            // Check there is not any concept in 'extends' metadata in concepts schemes were not marked as "final" ( = publish internally)
            ConceptMetamac conceptWithExtends = conceptMetamacRepository.findOneConceptExtendsConceptInConceptSchemeNotFinal(itemSchemeVersion.getMaintainableArtefact().getUrn());
            if (conceptWithExtends != null) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.CONCEPT_SCHEME_WITH_RELATED_CONCEPTS_NOT_FINAL, itemSchemeVersion.getMaintainableArtefact().getUrn(), conceptWithExtends
                        .getConceptExtends().getNameableArtefact().getUrn()));
            }

            // Note: in SDMX module check related concept with 'role' belong to concepts schemes were marked as "final" ( = publish internally)
        }

        @Override
        public void checkAdditionalConditionsToPublishExternally(ItemSchemeVersion itemSchemeVersion, List<MetamacExceptionItem> exceptions) {

            // Check there is not any concept in 'extends' metadata in concepts schemes were not marked as "valid" ( = publish externally)
            ConceptMetamac conceptWithExtends = conceptMetamacRepository.findOneConceptExtendsConceptInConceptSchemeNotValid(itemSchemeVersion.getMaintainableArtefact().getUrn());
            if (conceptWithExtends != null) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.CONCEPT_SCHEME_WITH_RELATED_CONCEPTS_VALIDITY_NOT_STARTED, itemSchemeVersion.getMaintainableArtefact().getUrn(),
                        conceptWithExtends.getConceptExtends().getNameableArtefact().getUrn()));
            }

            // Note: in SDMX module check related concept with 'role' belong to concepts schemes with filled "validFrom" ( = publish externally)
        }

        @Override
        public ItemSchemeVersion markItemSchemeAsFinal(ServiceContext ctx, ItemSchemeVersion itemSchemeVersion) throws MetamacException {
            return conceptsService.markConceptSchemeAsFinal(ctx, itemSchemeVersion.getMaintainableArtefact().getUrn());
        }

        @Override
        public ItemSchemeVersion startItemSchemeValidity(ServiceContext ctx, ItemSchemeVersion itemSchemeVersion) throws MetamacException {
            return conceptsService.startConceptSchemeValidity(ctx, itemSchemeVersion.getMaintainableArtefact().getUrn());
        }

        @Override
        public List<ItemSchemeVersion> findItemSchemeVersionsOfItemSchemeInProcStatus(ServiceContext ctx, ItemScheme itemScheme, ItemSchemeMetamacProcStatusEnum... procStatus) {

            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).withProperty(ConceptSchemeVersionMetamacProperties.itemScheme().id())
                    .eq(itemScheme.getId()).withProperty(ConceptSchemeVersionMetamacProperties.procStatus()).in((Object[]) procStatus).distinctRoot().build();
            PagingParameter pagingParameter = PagingParameter.noLimits();
            PagedResult<ConceptSchemeVersionMetamac> conceptSchemeVersionPagedResult = conceptSchemeVersionMetamacRepository.findByCondition(conditions, pagingParameter);
            return conceptSchemeMetamacToItemScheme(conceptSchemeVersionPagedResult.getValues());
        }
        
        private ConceptSchemeVersionMetamac getConceptSchemeVersionMetamac(ItemSchemeVersion itemSchemeVersion) {
            return (ConceptSchemeVersionMetamac) itemSchemeVersion;
        }

        private List<ItemSchemeVersion> conceptSchemeMetamacToItemScheme(List<ConceptSchemeVersionMetamac> conceptSchemeVersions) {
            List<ItemSchemeVersion> itemSchemeVersions = new ArrayList<ItemSchemeVersion>();
            for (ConceptSchemeVersionMetamac conceptSchemeVersion : conceptSchemeVersions) {
                itemSchemeVersions.add((ItemSchemeVersion) conceptSchemeVersion);
            }
            return itemSchemeVersions;
        }
    }
}
