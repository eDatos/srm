package org.siemac.metamac.srm.core.code.domain;

import org.siemac.metamac.srm.core.common.domain.ItemMetamacResultSelection;

public class CodeMetamacResultSelection extends ItemMetamacResultSelection {

    public static final CodeMetamacResultSelection API                 = new CodeMetamacResultSelection(false, false, false, false, false, true);
    public static final CodeMetamacResultSelection EXPORT              = new CodeMetamacResultSelection(true, true, true, true, true, false);
    public static final CodeMetamacResultSelection EXPORT_ORDERS       = new CodeMetamacResultSelection(true, true, true, true, false, false);

    private boolean                                variableElementFull = false;
    private boolean                                variableElementCode = false;

    public CodeMetamacResultSelection(boolean descriptions, boolean comments, boolean annotations, boolean internationalStringsMetamac, boolean variableElementCode, boolean variableElementFull) {
        super(descriptions, comments, annotations, internationalStringsMetamac);
        this.variableElementCode = variableElementCode;
        this.variableElementFull = variableElementFull;
    }

    public boolean isVariableElementCode() {
        return variableElementCode;
    }

    public boolean isVariableElementFull() {
        return variableElementFull;
    }
}
