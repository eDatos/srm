package org.siemac.metamac.srm.web.category.enums;

import com.smartgwt.client.types.ValueEnum;

public enum CategoriesToolStripButtonEnum implements ValueEnum {

    CATEGORY_SCHEMES("cat_scheme_button"), CATEGORIES("cat_button");

    private String value;

    CategoriesToolStripButtonEnum(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
