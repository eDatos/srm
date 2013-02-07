package org.siemac.metamac.srm.web.client.widgets;

import java.util.List;

import org.siemac.metamac.srm.web.client.view.handlers.CategorisationUiHandlers;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;

import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;

public class NewCategorisationWindow extends CustomWindow {

    public CategorisationUiHandlers uiHandlers;

    public NewCategorisationWindow(String title) {
        super(title);
        // TODO Auto-generated constructor stub
        show();
    }

    public List<String> getSelectedCategoryUrns() {
        return null; // TODO
    }

    public void setUiHandlers(CategorisationUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public CategorisationUiHandlers getUiHandlers() {
        return uiHandlers;
    }

    public boolean validateForm() {
        return false; // TODO
    }

    public HasClickHandlers getSave() {
        return null; // TODO
    }
}
