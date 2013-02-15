package org.siemac.metamac.srm.web.code.widgets;

import org.siemac.metamac.srm.web.client.widgets.CategorisationsPanel;

public class CodelistCategorisationsPanel extends CategorisationsPanel {

    @Override
    public void showDeleteCategorisationButton() {
        // TODO Security
        deleteCategorisationButton.show();
    }
}
