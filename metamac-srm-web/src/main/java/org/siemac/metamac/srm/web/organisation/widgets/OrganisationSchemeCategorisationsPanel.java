package org.siemac.metamac.srm.web.organisation.widgets;

import org.siemac.metamac.srm.web.client.widgets.CategorisationsPanel;

public class OrganisationSchemeCategorisationsPanel extends CategorisationsPanel {

    @Override
    public void showDeleteCategorisationButton() {
        // TODO Security
        deleteCategorisationButton.show();
    }
}
