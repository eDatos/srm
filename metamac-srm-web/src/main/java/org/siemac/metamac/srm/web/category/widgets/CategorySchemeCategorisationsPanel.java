package org.siemac.metamac.srm.web.category.widgets;

import org.siemac.metamac.srm.web.client.widgets.CategorisationsPanel;

public class CategorySchemeCategorisationsPanel extends CategorisationsPanel {

    @Override
    public void showDeleteCategorisationButton() {
        // TODO Security
        deleteCategorisationButton.show();
    }
}
