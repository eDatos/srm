package org.siemac.metamac.srm.core.organisation.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
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
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamacRepository;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamacRepository;
import org.siemac.metamac.srm.core.organisation.serviceapi.OrganisationsMetamacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.base.serviceapi.BaseService;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.ValidationUtils;
import com.arte.statistic.sdmx.srm.core.common.service.utils.SdmxSrmValidationUtils;
import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationRepository;
import com.arte.statistic.sdmx.srm.core.organisation.serviceapi.OrganisationsService;

@Service("organisationSchemeLifeCycle")
public class OrganisationSchemeLifeCycleImpl extends LifeCycleImpl {

    @Autowired
    private ItemSchemeVersionRepository                itemSchemeVersionRepository;

    @Autowired
    private OrganisationRepository                     organisationRepository;

    @Autowired
    private OrganisationMetamacRepository              organisationMetamacRepository;

    @Autowired
    private OrganisationSchemeVersionMetamacRepository organisationSchemeVersionMetamacRepository;

    @Autowired
    private OrganisationsService                       organisationsService;

    @Autowired
    private OrganisationsMetamacService                organisationsMetamacService;

    @Autowired
    private BaseService                                baseService;

    public OrganisationSchemeLifeCycleImpl() {
        this.callback = new OrganisationSchemeLifeCycleCallback();
    }

    private class OrganisationSchemeLifeCycleCallback implements LifeCycleCallback {

        @Override
        public SrmLifeCycleMetadata getLifeCycleMetadata(Object srmResourceVersion) {
            return getOrganisationSchemeVersionMetamac(srmResourceVersion).getLifeCycleMetadata();
        }

        @Override
        public MaintainableArtefact getMaintainableArtefact(Object srmResourceVersion) {
            return getOrganisationSchemeVersionMetamac(srmResourceVersion).getMaintainableArtefact();
        }

        @Override
        public Object updateSrmResource(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            OrganisationSchemeVersionMetamac organisationSchemeVersion = getOrganisationSchemeVersionMetamac(srmResourceVersion);
            // Update item scheme
            baseService.updateItemSchemeLastUpdated(ctx, organisationSchemeVersion);
            // Update item scheme version
            return itemSchemeVersionRepository.save(organisationSchemeVersion);
        }

        @Override
        public Object retrieveSrmResourceByProcStatus(String urn, ProcStatusEnum[] procStatus) throws MetamacException {
            return organisationSchemeVersionMetamacRepository.retrieveOrganisationSchemeVersionByProcStatus(urn, procStatus);
        }

        @Override
        public Object executeBeforeSendProductionValidation(ServiceContext ctx, Object srmResourceVersion) {
            // nothing
            return srmResourceVersion;
        }

        @Override
        public void checkConcreteResourceInProductionValidation(ServiceContext ctx, Object srmResourceVersion, ProcStatusEnum targetStatus) throws MetamacException {

            OrganisationSchemeVersionMetamac organisationSchemeVersion = getOrganisationSchemeVersionMetamac(srmResourceVersion);
            String organisationSchemeUrn = organisationSchemeVersion.getMaintainableArtefact().getUrn();
            Map<String, MetamacExceptionItem> exceptionsByResourceUrn = new HashMap<String, MetamacExceptionItem>();

            // Check organisationScheme
            {
                List<MetamacExceptionItem> exceptionsOrganisationScheme = new ArrayList<MetamacExceptionItem>();
                // Metadata required
                ValidationUtils.checkMetadataRequired(organisationSchemeVersion.getIsPartial(), ServiceExceptionParameters.ITEM_SCHEME_IS_PARTIAL, exceptionsOrganisationScheme);
                // One organisation at least
                Long itemsCount = organisationRepository.countItems(organisationSchemeVersion.getId());
                if (itemsCount == 0) {
                    exceptionsOrganisationScheme.add(new MetamacExceptionItem(ServiceExceptionType.ITEM_SCHEME_WITHOUT_ITEMS));
                }
                addOrUpdateExceptionItemByResourceUrnWhenExceptionsNonZero(exceptionsByResourceUrn, organisationSchemeUrn, exceptionsOrganisationScheme);
            }
            // Check organisations
            {
                // nothing
            }
            // Check translations
            {
                organisationsMetamacService.checkOrganisationSchemeVersionTranslations(ctx, organisationSchemeVersion.getId(), getLanguageDefault(), exceptionsByResourceUrn);
            }
            // Throw exception if there is any exception
            throwExceptionsInExceptionsMap(exceptionsByResourceUrn, organisationSchemeUrn);
        }

        @Override
        public void checkConcreteResourceInDiffusionValidation(Object srmResourceVersion, ProcStatusEnum targetStatus) {
            // nothing
        }

        @Override
        public void checkConcreteResourceInRejectProductionValidation(Object srmResourceVersion, ProcStatusEnum targetStatus) {
            // nothing
        }

        @Override
        public void checkConcreteResourceInRejectDiffusionValidation(Object srmResourceVersion, ProcStatusEnum targetStatus) {
            // nothing
        }

        @Override
        public void checkConcreteResourceInInternallyPublished(ServiceContext ctx, Object srmResourceVersion, ProcStatusEnum targetStatus) {
            // nothing
        }

        @Override
        public Object publishInternallyConcreteResource(ServiceContext ctx, Object srmResourceVersion) {
            OrganisationSchemeVersionMetamac organisationSchemeVersion = getOrganisationSchemeVersionMetamac(srmResourceVersion);
            if (SdmxSrmValidationUtils.isOrganisationSchemeWithSpecialTreatment(organisationSchemeVersion)) {
                organisationMetamacRepository.updateHasBeenPublishedEfficiently(organisationSchemeVersion.getId());
            }
            return srmResourceVersion;
        }

        @Override
        public void checkConcreteResourceInExternallyPublished(ServiceContext ctx, Object srmResourceVersion, ProcStatusEnum targetStatus) throws MetamacException {
            organisationsMetamacService.checkOrganisationSchemeWithRelatedResourcesExternallyPublished(ctx, getOrganisationSchemeVersionMetamac(srmResourceVersion));
        }

        @Override
        public Object markSrmResourceAsFinal(ServiceContext ctx, Object srmResourceVersion, Boolean forceLastestFinal) throws MetamacException {
            OrganisationSchemeVersionMetamac organisationSchemeVersion = getOrganisationSchemeVersionMetamac(srmResourceVersion);
            if (SdmxSrmValidationUtils.isOrganisationSchemeWithSpecialTreatment(organisationSchemeVersion)) {
                // Do not mark as final in SDMX metadata. Only mark it is LatestFinal
                organisationSchemeVersion.getMaintainableArtefact().setLatestFinal(Boolean.TRUE);
                updateSrmResource(ctx, organisationSchemeVersion);
            } else {
                // Mark as final
                return organisationsService.markOrganisationSchemeAsFinal(ctx, organisationSchemeVersion.getMaintainableArtefact().getUrn(), forceLastestFinal);
            }
            return srmResourceVersion;
        }

        @Override
        public Object markSrmResourceAsPublic(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            return organisationsService.markOrganisationSchemeAsPublic(ctx, getOrganisationSchemeVersionMetamac(srmResourceVersion).getMaintainableArtefact().getUrn());
        }

        @Override
        public Object startSrmResourceValidity(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            if (!SdmxSrmValidationUtils.isOrganisationSchemeWithSpecialTreatment(getOrganisationSchemeVersionMetamac(srmResourceVersion))) {
                return organisationsMetamacService.startOrganisationSchemeValidity(ctx, getOrganisationSchemeVersionMetamac(srmResourceVersion).getMaintainableArtefact().getUrn());
            }
            return srmResourceVersion;
        }

        @Override
        public Object endSrmResourceValidity(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            if (!SdmxSrmValidationUtils.isOrganisationSchemeWithSpecialTreatment(getOrganisationSchemeVersionMetamac(srmResourceVersion))) {
                return organisationsMetamacService.endOrganisationSchemeValidity(ctx, getOrganisationSchemeVersionMetamac(srmResourceVersion).getMaintainableArtefact().getUrn());
            }
            return srmResourceVersion;
        }

        @Override
        public List<Object> findSrmResourceVersionsOfSrmResourceInProcStatus(ServiceContext ctx, Object srmResourceVersion, ProcStatusEnum... procStatus) {

            OrganisationSchemeVersionMetamac organisationSchemeVersion = getOrganisationSchemeVersionMetamac(srmResourceVersion);

            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(OrganisationSchemeVersionMetamac.class)
                    .withProperty(OrganisationSchemeVersionMetamacProperties.itemScheme().id()).eq(organisationSchemeVersion.getItemScheme().getId())
                    .withProperty(OrganisationSchemeVersionMetamacProperties.lifeCycleMetadata().procStatus()).in((Object[]) procStatus).distinctRoot().build();
            PagingParameter pagingParameter = PagingParameter.noLimits();
            PagedResult<OrganisationSchemeVersionMetamac> organisationSchemeVersionPagedResult = organisationSchemeVersionMetamacRepository.findByCondition(conditions, pagingParameter);
            return organisationSchemeMetamacToObject(organisationSchemeVersionPagedResult.getValues());
        }

        @Override
        public MetamacExceptionItem buildExceptionItemWrongProcStatus(Object srmResourceVersion, String[] procStatusExpecteds) {
            OrganisationSchemeVersionMetamac organisationSchemeVersion = getOrganisationSchemeVersionMetamac(srmResourceVersion);
            return MetamacExceptionItemBuilder.metamacExceptionItem().withCommonServiceExceptionType(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS)
                    .withMessageParameters(organisationSchemeVersion.getMaintainableArtefact().getUrn(), procStatusExpecteds).build();
        }

        @Override
        public Boolean canHaveCategorisations() {
            return Boolean.TRUE;
        }

        @Override
        public Object mergeTemporal(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            OrganisationSchemeVersionMetamac organisationSchemeVersionMetamac = (OrganisationSchemeVersionMetamac) srmResourceVersion;
            if (VersionUtil.isTemporalVersion(organisationSchemeVersionMetamac.getMaintainableArtefact().getVersionLogic())) {
                return organisationsMetamacService.mergeTemporalVersion(ctx, organisationSchemeVersionMetamac);
            }
            return srmResourceVersion;
        }

        @Override
        public Boolean isTemporalToPublishExternally(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            OrganisationSchemeVersionMetamac organisationSchemeVersionMetamac = (OrganisationSchemeVersionMetamac) srmResourceVersion;
            if (ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(organisationSchemeVersionMetamac.getLifeCycleMetadata().getProcStatus())) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }

        private OrganisationSchemeVersionMetamac getOrganisationSchemeVersionMetamac(Object srmResource) {
            return (OrganisationSchemeVersionMetamac) srmResource;
        }

        private List<Object> organisationSchemeMetamacToObject(List<OrganisationSchemeVersionMetamac> organisationSchemeVersions) {
            List<Object> objects = new ArrayList<Object>(organisationSchemeVersions.size());
            for (OrganisationSchemeVersionMetamac organisationSchemeVersion : organisationSchemeVersions) {
                objects.add(organisationSchemeVersion);
            }
            return objects;
        }
    }
}
