package org.siemac.metamac.srm.web.concept.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.web.client.widgets.SearchItemItem;
import org.siemac.metamac.web.common.client.widgets.handlers.CustomLinkItemNavigationClickHandler;

public abstract class SearchConceptItem extends SearchItemItem {

    public SearchConceptItem(String name, String title, CustomLinkItemNavigationClickHandler navigationHandler) {
        super(name, title, getConstants().searchConcepts(), getConstants().filterConceptScheme(), getConstants().selectedConceptScheme(), getConstants().selectionConcept(), navigationHandler);
    }

}
