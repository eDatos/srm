package org.siemac.metamac.srm.core.code.domain;

import org.siemac.metamac.srm.core.common.domain.ItemMetamacResultSelection;

public class CodeMetamacResultSelection extends ItemMetamacResultSelection {

    public static final CodeMetamacResultSelection EXPORT          = new CodeMetamacResultSelection(true, true, true, true, true);

    private boolean                                variableElement = false;

    public CodeMetamacResultSelection(boolean descriptions, boolean comments, boolean annotations, boolean internationalStringsMetamac, boolean variableElement) {
        super(descriptions, comments, annotations, internationalStringsMetamac);
        this.variableElement = variableElement;
    }

    public boolean isVariableElement() {
        return variableElement;
    }

}
