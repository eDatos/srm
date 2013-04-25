package org.siemac.metamac.srm.core.category.serviceimpl.utils;

import java.util.List;

import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamacRepository;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.common.domain.ItemMetamacResultSelection;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.category.domain.Category;
import com.arte.statistic.sdmx.srm.core.category.domain.CategorySchemeVersion;
import com.arte.statistic.sdmx.srm.core.category.serviceimpl.utils.CategoriesVersioningCopyUtils.CategoryVersioningCopyCallback;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

/**
 * Copy Metamac metadata
 */
@Component("categoryVersioningCopyCallbackMetamac")
public class CategoryVersioningCopyCallbackMetamacImpl implements CategoryVersioningCopyCallback {

    @Autowired
    private CategoryMetamacRepository categoryMetamacRepository;

    @Override
    public CategorySchemeVersion createCategorySchemeVersion() {
        return new CategorySchemeVersionMetamac();
    }

    @Override
    public void copyCategorySchemeVersion(CategorySchemeVersion source, CategorySchemeVersion targetSdmx) {
        CategorySchemeVersionMetamac target = (CategorySchemeVersionMetamac) targetSdmx;
        target.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
        target.getMaintainableArtefact().setFinalLogicClient(Boolean.FALSE);
    }

    @Override
    public Category createCategory() {
        return new CategoryMetamac();
    }

    @Override
    public void copyCategory(Category source, ItemResult sourceItemResult, Category target) {
        // nothing more

        // IMPORTANT! If any InternationalString is added, do an efficient query and retrieve from sourceItemResult
    }

    @Override
    public Boolean isOverridedFindItemsEfficiently() {
        return Boolean.TRUE;
    }

    @Override
    public List<ItemResult> findItemsEfficiently(Long itemSchemeId) {
        return categoryMetamacRepository.findCategoriesByCategorySchemeUnordered(itemSchemeId, ItemMetamacResultSelection.VERSIONING);
    }
}