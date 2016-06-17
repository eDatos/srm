package org.siemac.metamac.srm.core.code.domain;

import org.siemac.metamac.srm.core.common.domain.ItemMetamacResultSelection;

public class CategoryMetamacResultSelection extends ItemMetamacResultSelection {

    public static final CategoryMetamacResultSelection EXPORT = new CategoryMetamacResultSelection(true, true, true, true);

    public CategoryMetamacResultSelection(boolean descriptions, boolean comments, boolean annotations, boolean internationalStringsMetamac) {
        super(descriptions, comments, annotations, internationalStringsMetamac);
    }
}
