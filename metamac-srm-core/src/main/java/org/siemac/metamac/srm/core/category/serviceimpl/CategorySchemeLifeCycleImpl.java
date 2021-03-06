package org.siemac.metamac.srm.core.category.serviceimpl;

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
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamacRepository;
import org.siemac.metamac.srm.core.category.serviceapi.CategoriesMetamacService;
import org.siemac.metamac.srm.core.common.LifeCycleImpl;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.base.serviceapi.BaseService;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.ValidationUtils;
import com.arte.statistic.sdmx.srm.core.category.domain.CategoryRepository;
import com.arte.statistic.sdmx.srm.core.category.serviceapi.CategoriesService;

@Service("categorySchemeLifeCycle")
public class CategorySchemeLifeCycleImpl extends LifeCycleImpl {

    @Autowired
    private ItemSchemeVersionRepository            itemSchemeVersionRepository;

    @Autowired
    private CategorySchemeVersionMetamacRepository categorySchemeVersionMetamacRepository;

    @Autowired
    private CategoryRepository                     categoryRepository;

    @Autowired
    private CategoriesService                      categoriesService;

    @Autowired
    private CategoriesMetamacService               categoriesMetamacService;

    @Autowired
    private BaseService                            baseService;

    public CategorySchemeLifeCycleImpl() {
        this.callback = new CategorySchemeLifeCycleCallback();
    }

    private class CategorySchemeLifeCycleCallback implements LifeCycleCallback {

        @Override
        public SrmLifeCycleMetadata getLifeCycleMetadata(Object srmResourceVersion) {
            return getCategorySchemeVersionMetamac(srmResourceVersion).getLifeCycleMetadata();
        }

        @Override
        public MaintainableArtefact getMaintainableArtefact(Object srmResourceVersion) {
            return getCategorySchemeVersionMetamac(srmResourceVersion).getMaintainableArtefact();
        }

        @Override
        public Object updateSrmResource(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            CategorySchemeVersionMetamac categorySchemeVersion = getCategorySchemeVersionMetamac(srmResourceVersion);
            // Update item scheme
            baseService.updateItemSchemeLastUpdated(ctx, categorySchemeVersion);
            // Update item scheme version
            return itemSchemeVersionRepository.save(categorySchemeVersion);
        }

        @Override
        public Object retrieveSrmResourceByProcStatus(String urn, ProcStatusEnum[] procStatus) throws MetamacException {
            return categorySchemeVersionMetamacRepository.retrieveCategorySchemeVersionByProcStatus(urn, procStatus);
        }

        @Override
        public Object executeBeforeSendProductionValidation(ServiceContext ctx, Object srmResourceVersion) {
            // nothing
            return srmResourceVersion;
        }

        @Override
        public void checkConcreteResourceInProductionValidation(ServiceContext ctx, Object srmResourceVersion, ProcStatusEnum targetStatus) throws MetamacException {
            CategorySchemeVersionMetamac categorySchemeVersion = getCategorySchemeVersionMetamac(srmResourceVersion);
            String categorySchemeUrn = categorySchemeVersion.getMaintainableArtefact().getUrn();
            Map<String, MetamacExceptionItem> exceptionsByResourceUrn = new HashMap<String, MetamacExceptionItem>();

            // Check categoryScheme
            {
                List<MetamacExceptionItem> exceptionsCategoryScheme = new ArrayList<MetamacExceptionItem>();
                // Metadata required
                ValidationUtils.checkMetadataRequired(categorySchemeVersion.getIsPartial(), ServiceExceptionParameters.ITEM_SCHEME_IS_PARTIAL, exceptionsCategoryScheme);
                // One category at least
                Long itemsCount = categoryRepository.countItems(categorySchemeVersion.getId());
                if (itemsCount == 0) {
                    exceptionsCategoryScheme.add(new MetamacExceptionItem(ServiceExceptionType.ITEM_SCHEME_WITHOUT_ITEMS));
                }
                addOrUpdateExceptionItemByResourceUrnWhenExceptionsNonZero(exceptionsByResourceUrn, categorySchemeUrn, exceptionsCategoryScheme);
            }
            // Check categories
            {
                // nothing
            }
            // Check translations
            {
                categoriesMetamacService.checkCategorySchemeVersionTranslations(ctx, categorySchemeVersion.getId(), getLanguageDefault(), exceptionsByResourceUrn);
            }
            // Throw exception if there is any exception
            throwExceptionsInExceptionsMap(exceptionsByResourceUrn, categorySchemeUrn);
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
            // nothing
            return srmResourceVersion;
        }

        @Override
        public void checkConcreteResourceInExternallyPublished(ServiceContext ctx, Object srmResourceVersion, ProcStatusEnum targetStatus) throws MetamacException {
            categoriesMetamacService.checkCategorySchemeWithRelatedResourcesExternallyPublished(ctx, getCategorySchemeVersionMetamac(srmResourceVersion));
        }

        @Override
        public Object markSrmResourceAsFinal(ServiceContext ctx, Object srmResourceVersion, Boolean forceLastestFinal) throws MetamacException {
            return categoriesService.markCategorySchemeAsFinal(ctx, getCategorySchemeVersionMetamac(srmResourceVersion).getMaintainableArtefact().getUrn(), forceLastestFinal);
        }

        @Override
        public Object markSrmResourceAsPublic(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            return categoriesService.markCategorySchemeAsPublic(ctx, getCategorySchemeVersionMetamac(srmResourceVersion).getMaintainableArtefact().getUrn());
        }

        @Override
        public Object startSrmResourceValidity(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            return categoriesMetamacService.startCategorySchemeValidity(ctx, getCategorySchemeVersionMetamac(srmResourceVersion).getMaintainableArtefact().getUrn());
        }

        @Override
        public Object endSrmResourceValidity(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            return categoriesMetamacService.endCategorySchemeValidity(ctx, getCategorySchemeVersionMetamac(srmResourceVersion).getMaintainableArtefact().getUrn());
        }

        @Override
        public List<Object> findSrmResourceVersionsOfSrmResourceInProcStatus(ServiceContext ctx, Object srmResourceVersion, ProcStatusEnum... procStatus) {

            CategorySchemeVersionMetamac categorySchemeVersion = getCategorySchemeVersionMetamac(srmResourceVersion);

            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CategorySchemeVersionMetamac.class).withProperty(CategorySchemeVersionMetamacProperties.itemScheme().id())
                    .eq(categorySchemeVersion.getItemScheme().getId()).withProperty(CategorySchemeVersionMetamacProperties.lifeCycleMetadata().procStatus()).in((Object[]) procStatus).distinctRoot()
                    .build();
            PagingParameter pagingParameter = PagingParameter.noLimits();
            PagedResult<CategorySchemeVersionMetamac> categorySchemeVersionPagedResult = categorySchemeVersionMetamacRepository.findByCondition(conditions, pagingParameter);
            return categorySchemeMetamacToObject(categorySchemeVersionPagedResult.getValues());
        }

        @Override
        public MetamacExceptionItem buildExceptionItemWrongProcStatus(Object srmResourceVersion, String[] procStatusExpecteds) {
            CategorySchemeVersionMetamac categorySchemeVersion = getCategorySchemeVersionMetamac(srmResourceVersion);
            return MetamacExceptionItemBuilder.metamacExceptionItem().withCommonServiceExceptionType(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS)
                    .withMessageParameters(categorySchemeVersion.getMaintainableArtefact().getUrn(), procStatusExpecteds).build();
        }

        @Override
        public Boolean canHaveCategorisations() {
            return Boolean.FALSE;
        }

        @Override
        public Object mergeTemporal(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            CategorySchemeVersionMetamac categorySchemeVersionMetamac = (CategorySchemeVersionMetamac) srmResourceVersion;
            if (VersionUtil.isTemporalVersion(categorySchemeVersionMetamac.getMaintainableArtefact().getVersionLogic())) {
                return categoriesMetamacService.mergeTemporalVersion(ctx, categorySchemeVersionMetamac);
            }
            return srmResourceVersion;
        }

        @Override
        public Boolean isTemporalToPublishExternally(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            CategorySchemeVersionMetamac categorySchemeVersionMetamac = (CategorySchemeVersionMetamac) srmResourceVersion;
            if (ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(categorySchemeVersionMetamac.getLifeCycleMetadata().getProcStatus())) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }

        private CategorySchemeVersionMetamac getCategorySchemeVersionMetamac(Object srmResource) {
            return (CategorySchemeVersionMetamac) srmResource;
        }

        private List<Object> categorySchemeMetamacToObject(List<CategorySchemeVersionMetamac> categorySchemeVersions) {
            List<Object> objects = new ArrayList<Object>(categorySchemeVersions.size());
            for (CategorySchemeVersionMetamac categorySchemeVersion : categorySchemeVersions) {
                objects.add(categorySchemeVersion);
            }
            return objects;
        }

    }
}