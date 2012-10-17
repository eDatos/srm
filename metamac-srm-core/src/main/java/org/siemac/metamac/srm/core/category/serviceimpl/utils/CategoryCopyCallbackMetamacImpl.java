package org.siemac.metamac.srm.core.category.serviceimpl.utils;

import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.category.domain.Category;
import com.arte.statistic.sdmx.srm.core.category.domain.CategorySchemeVersion;
import com.arte.statistic.sdmx.srm.core.category.serviceimpl.utils.CategoriesDoCopyUtils.CategoryCopyCallback;

/**
 * Copy Metamac metadata
 */
@Component("categoryCopyCallbackMetamac")
public class CategoryCopyCallbackMetamacImpl implements CategoryCopyCallback {

    @Override
    public CategorySchemeVersion copyCategorySchemeVersion(CategorySchemeVersion source) {
        return copyCategorySchemeVersion((CategorySchemeVersionMetamac) source);
    }

    private CategorySchemeVersionMetamac copyCategorySchemeVersion(CategorySchemeVersionMetamac source) {
        CategorySchemeVersionMetamac target = new CategorySchemeVersionMetamac();
        target.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
        return target;
    }

    @Override
    public Category copyCategory(Category source) {
        return copyCategory((CategoryMetamac) source);
    }

    private CategoryMetamac copyCategory(CategoryMetamac source) {
        CategoryMetamac target = new CategoryMetamac();
        return target;
    }
}