package org.siemac.metamac.srm.web.category.widgets;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.web.category.utils.CategoriesClientSecurityUtils;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;

public class CategoryMainFormLayout extends InternationalMainFormLayout {

    public void setCategoryScheme(CategorySchemeMetamacDto categorySchemeMetamacDto) {
        setCanEdit(CategoriesClientSecurityUtils.canUpdateCategory(categorySchemeMetamacDto.getLifeCycle().getProcStatus()));
        setCanDelete(CategoriesClientSecurityUtils.canDeleteCategory(categorySchemeMetamacDto));
    }
}
