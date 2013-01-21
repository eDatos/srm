package org.siemac.metamac.srm.core.category.serviceimpl.utils;

import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.category.domain.Category;
import com.arte.statistic.sdmx.srm.core.category.domain.CategorySchemeVersion;
import com.arte.statistic.sdmx.srm.core.category.serviceimpl.utils.CategoriesVersioningCopyUtils.CategoryVersioningCopyCallback;

/**
 * Copy Metamac metadata
 */
@Component("categoryVersioningCopyCallbackMetamac")
public class CategoryVersioningCopyCallbackMetamacImpl implements CategoryVersioningCopyCallback {

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
    public void copyCategory(Category source, Category target) {
        // nothing more

    }
}