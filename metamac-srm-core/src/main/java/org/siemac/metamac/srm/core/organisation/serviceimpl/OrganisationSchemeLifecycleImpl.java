package org.siemac.metamac.srm.core.organisation.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.itemscheme.ItemSchemeLifecycle;
import org.siemac.metamac.srm.core.itemscheme.ItemSchemeLifecycle.ItemSchemeLifecycleCallback;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamacRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemScheme;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.organisation.serviceapi.OrganisationsService;

@Service("organisationSchemeLifecycle")
public class OrganisationSchemeLifecycleImpl implements OrganisationSchemeLifecycle {

    @Autowired
    private ItemSchemeVersionRepository                itemSchemeVersionRepository;

    @Autowired
    private OrganisationSchemeVersionMetamacRepository organisationSchemeVersionMetamacRepository;

    @Autowired
    private OrganisationsService                       organisationsService;

    private ItemSchemeLifecycle                        itemSchemeLifecycle = null;

    public OrganisationSchemeLifecycleImpl() {
        itemSchemeLifecycle = new ItemSchemeLifecycle(new OrganisationSchemeLifecycleCallback());
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

    private class OrganisationSchemeLifecycleCallback implements ItemSchemeLifecycleCallback {

        @Override
        public SrmLifeCycleMetadata getSrmLifeCycleMetadata(ItemSchemeVersion itemSchemeVersion) {
            OrganisationSchemeVersionMetamac organisationSchemeVersion = getOrganisationSchemeVersionMetamac(itemSchemeVersion);
            return organisationSchemeVersion.getLifecycleMetadata();
        }

        @Override
        public ItemSchemeVersion updateItemScheme(ItemSchemeVersion itemSchemeVersion) {
            return itemSchemeVersionRepository.save(itemSchemeVersion);
        }

        @Override
        public ItemSchemeVersion retrieveItemSchemeByProcStatus(String urn, ProcStatusEnum[] procStatus) throws MetamacException {
            return organisationSchemeVersionMetamacRepository.retrieveOrganisationSchemeVersionByProcStatus(urn, procStatus);
        }

        @Override
        public void checkAdditionalConditionsSinceSendToProductionValidation(ItemSchemeVersion itemSchemeVersion, List<MetamacExceptionItem> exceptions) {

            OrganisationSchemeVersionMetamac organisationSchemeVersion = getOrganisationSchemeVersionMetamac(itemSchemeVersion);

            // TODO One organisation at least
            // if (organisationSchemeVersion.getItems().size() == 0) {
            // exceptions.add(new MetamacExceptionItem(ServiceExceptionType.ORGANISATION_SCHEME_WITHOUT_ORGANISATIONS, organisationSchemeVersion.getMaintainableArtefact().getUrn()));
            // }
        }

        @Override
        public void checkAdditionalConditionsToPublishInternally(ItemSchemeVersion itemSchemeVersion, List<MetamacExceptionItem> exceptions) {

            // No conditions

        }

        @Override
        public void checkAdditionalConditionsToPublishExternally(ItemSchemeVersion itemSchemeVersion, List<MetamacExceptionItem> exceptions) {

            // No conditions
        }

        @Override
        public ItemSchemeVersion markItemSchemeAsFinal(ServiceContext ctx, ItemSchemeVersion itemSchemeVersion) throws MetamacException {
            return organisationsService.markOrganisationSchemeAsFinal(ctx, itemSchemeVersion.getMaintainableArtefact().getUrn());
        }

        @Override
        public ItemSchemeVersion startItemSchemeValidity(ServiceContext ctx, ItemSchemeVersion itemSchemeVersion) throws MetamacException {
            return organisationsService.startOrganisationSchemeValidity(ctx, itemSchemeVersion.getMaintainableArtefact().getUrn());
        }

        @Override
        public List<ItemSchemeVersion> findItemSchemeVersionsOfItemSchemeInProcStatus(ServiceContext ctx, ItemScheme itemScheme, ProcStatusEnum... procStatus) {

            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(OrganisationSchemeVersionMetamac.class)
                    .withProperty(OrganisationSchemeVersionMetamacProperties.itemScheme().id()).eq(itemScheme.getId())
                    .withProperty(OrganisationSchemeVersionMetamacProperties.lifecycleMetadata().procStatus()).in((Object[]) procStatus).distinctRoot().build();
            PagingParameter pagingParameter = PagingParameter.noLimits();
            PagedResult<OrganisationSchemeVersionMetamac> organisationSchemeVersionPagedResult = organisationSchemeVersionMetamacRepository.findByCondition(conditions, pagingParameter);
            return organisationSchemeMetamacToItemScheme(organisationSchemeVersionPagedResult.getValues());
        }

        private OrganisationSchemeVersionMetamac getOrganisationSchemeVersionMetamac(ItemSchemeVersion itemSchemeVersion) {
            return (OrganisationSchemeVersionMetamac) itemSchemeVersion;
        }

        private List<ItemSchemeVersion> organisationSchemeMetamacToItemScheme(List<OrganisationSchemeVersionMetamac> organisationSchemeVersions) {
            List<ItemSchemeVersion> itemSchemeVersions = new ArrayList<ItemSchemeVersion>();
            for (OrganisationSchemeVersionMetamac organisationSchemeVersion : organisationSchemeVersions) {
                itemSchemeVersions.add((ItemSchemeVersion) organisationSchemeVersion);
            }
            return itemSchemeVersions;
        }
    }
}
