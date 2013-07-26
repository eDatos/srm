package org.siemac.metamac.srm.core.common.domain;

import com.arte.statistic.sdmx.srm.core.common.domain.ItemResultSelection;

public class ItemMetamacResultSelection extends ItemResultSelection {

    public static final ItemMetamacResultSelection ALL                         = new ItemMetamacResultSelection(true, true, true, true);
    public static final ItemMetamacResultSelection MERGE                       = new ItemMetamacResultSelection(true, true, true, true);
    public static final ItemMetamacResultSelection COPY                        = new ItemMetamacResultSelection(true, false, true, true);
    public static final ItemMetamacResultSelection VERSIONING                  = new ItemMetamacResultSelection(true, false, true, true);
    public static final ItemMetamacResultSelection VERSIONING_DUMMY            = ALL;
    public static final ItemMetamacResultSelection API                         = new ItemMetamacResultSelection(false, false, false, false);
    public static final ItemMetamacResultSelection RETRIEVE                    = new ItemMetamacResultSelection(true, false, true, true);

    private boolean                                internationalStringsMetamac = false;

    public ItemMetamacResultSelection(boolean descriptions, boolean comments, boolean annotations, boolean internationalStringsMetamac) {
        super(descriptions, comments, annotations);
        this.internationalStringsMetamac = internationalStringsMetamac;
    }

    public boolean isInternationalStringsMetamac() {
        return internationalStringsMetamac;
    }

}
