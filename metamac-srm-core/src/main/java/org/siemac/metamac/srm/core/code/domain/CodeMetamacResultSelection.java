package org.siemac.metamac.srm.core.code.domain;

import com.arte.statistic.sdmx.srm.core.common.domain.ItemResultSelection;

public class CodeMetamacResultSelection extends ItemResultSelection {

    public static final CodeMetamacResultSelection ALL        = new CodeMetamacResultSelection(true, true, true, true);
    public static final CodeMetamacResultSelection MERGE      = new CodeMetamacResultSelection(true, true, true, true);
    public static final CodeMetamacResultSelection COPY       = new CodeMetamacResultSelection(true, false, true, true);
    public static final CodeMetamacResultSelection VERSIONING = new CodeMetamacResultSelection(true, false, true, true);

    private boolean                                shortName  = false;

    public CodeMetamacResultSelection(boolean descriptions, boolean comments, boolean annotations, boolean shortName) {
        super(descriptions, comments, annotations);
        this.shortName = shortName;
    }
    public boolean isShortName() {
        return shortName;
    }
}
