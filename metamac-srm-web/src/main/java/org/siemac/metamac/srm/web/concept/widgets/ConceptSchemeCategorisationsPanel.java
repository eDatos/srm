package org.siemac.metamac.srm.web.concept.widgets;

import org.siemac.metamac.srm.web.client.widgets.CategorisationsPanel;

public class ConceptSchemeCategorisationsPanel extends CategorisationsPanel {

    @Override
    public void showDeleteCategorisationButton() {
        // TODO Security
        deleteCategorisationButton.show();
    }
}
