package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.web.client.widgets.SearchItemItem;
import org.siemac.metamac.web.common.client.widgets.handlers.CustomLinkItemNavigationClickHandler;

public abstract class SearchCodeItem extends SearchItemItem {

    public SearchCodeItem(String name, String title, CustomLinkItemNavigationClickHandler navigationHandler) {
        super(name, title, getConstants().searchCodes(), getConstants().filterCodelist(), getConstants().selectedCodelist(), getConstants().selectionCode(), navigationHandler);
    }
}
