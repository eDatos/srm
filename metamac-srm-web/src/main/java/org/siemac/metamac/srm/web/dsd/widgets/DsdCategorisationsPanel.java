package org.siemac.metamac.srm.web.dsd.widgets;

import org.siemac.metamac.srm.web.client.widgets.CategorisationsPanel;

public class DsdCategorisationsPanel extends CategorisationsPanel {

    @Override
    public void showDeleteCategorisationButton() {
        // TODO Security
        deleteCategorisationButton.show();
    }
}
