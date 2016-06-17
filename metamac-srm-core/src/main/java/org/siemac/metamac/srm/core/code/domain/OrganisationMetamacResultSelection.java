package org.siemac.metamac.srm.core.code.domain;

import org.siemac.metamac.srm.core.common.domain.ItemMetamacResultSelection;

public class OrganisationMetamacResultSelection extends ItemMetamacResultSelection {

    public static final OrganisationMetamacResultSelection EXPORT = new OrganisationMetamacResultSelection(true, true, true, true);

    public OrganisationMetamacResultSelection(boolean descriptions, boolean comments, boolean annotations, boolean internationalStringsMetamac) {
        super(descriptions, comments, annotations, internationalStringsMetamac);
    }
}
