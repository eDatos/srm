package org.siemac.metamac.srm.core.category.serviceimpl;

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
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.ValidationUtils;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.common.LifeCycleImpl;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamacRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.category.serviceapi.CategoriesService;

@Service("categorySchemeLifeCycle")
public class CategorySchemeLifeCycleImpl extends LifeCycleImpl {

    @Autowired
    private ItemSchemeVersionRepository            itemSchemeVersionRepository;

    @Autowired
    private CategorySchemeVersionMetamacRepository categorySchemeVersionMetamacRepository;

    @Autowired
    private CategoriesService                      categoriesService;

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
        public Object updateSrmResource(Object srmResourceVersion) {
            return itemSchemeVersionRepository.save(getCategorySchemeVersionMetamac(srmResourceVersion));
        }

        @Override
        public Object retrieveSrmResourceByProcStatus(String urn, ProcStatusEnum[] procStatus) throws MetamacException {
            return categorySchemeVersionMetamacRepository.retrieveCategorySchemeVersionByProcStatus(urn, procStatus);
        }

        @Override
        public void checkConcreteResourceInProductionValidation(Object srmResourceVersion, List<MetamacExceptionItem> exceptions) {

            CategorySchemeVersionMetamac categorySchemeVersion = getCategorySchemeVersionMetamac(srmResourceVersion);

            // Metadata required
            ValidationUtils.checkMetadataRequired(categorySchemeVersion.getIsPartial(), ServiceExceptionParameters.ITEM_SCHEME_IS_PARTIAL, exceptions);

            // One category at least
            if (categorySchemeVersion.getItems().size() == 0) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.ITEM_SCHEME_WITHOUT_ITEMS, categorySchemeVersion.getMaintainableArtefact().getUrn()));
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
        }

        @Override
        public void checkConcreteResourceInExternallyPublished(Object srmResourceVersion, List<MetamacExceptionItem> exceptions) {
            // nothing
        }

        @Override
        public Object markSrmResourceAsFinal(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            return categoriesService.markCategorySchemeAsFinal(ctx, getCategorySchemeVersionMetamac(srmResourceVersion).getMaintainableArtefact().getUrn());
        }

        @Override
        public Object startSrmResourceValidity(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            return categoriesService.startCategorySchemeValidity(ctx, getCategorySchemeVersionMetamac(srmResourceVersion).getMaintainableArtefact().getUrn());
        }

        @Override
        public Object endSrmResourceValidity(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            return categoriesService.endCategorySchemeValidity(ctx, getCategorySchemeVersionMetamac(srmResourceVersion).getMaintainableArtefact().getUrn());
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

        private CategorySchemeVersionMetamac getCategorySchemeVersionMetamac(Object srmResource) {
            return (CategorySchemeVersionMetamac) srmResource;
        }

        private List<Object> categorySchemeMetamacToObject(List<CategorySchemeVersionMetamac> categorySchemeVersions) {
            List<Object> objects = new ArrayList<Object>();
            for (CategorySchemeVersionMetamac categorySchemeVersion : categorySchemeVersions) {
                objects.add((ItemSchemeVersion) categorySchemeVersion);
            }
            return objects;
        }
    }
}