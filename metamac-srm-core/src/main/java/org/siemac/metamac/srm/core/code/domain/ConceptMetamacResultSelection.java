package org.siemac.metamac.srm.core.code.domain;

import org.siemac.metamac.srm.core.common.domain.ItemMetamacResultSelection;

public class ConceptMetamacResultSelection extends ItemMetamacResultSelection {

    public static final ConceptMetamacResultSelection EXPORT = new ConceptMetamacResultSelection(true, true, true, true, true, false);

    public ConceptMetamacResultSelection(boolean descriptions, boolean comments, boolean annotations, boolean internationalStringsMetamac, boolean variableElementCode, boolean variableElementFull) {
        super(descriptions, comments, annotations, internationalStringsMetamac);
    }
}
