package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.web.client.resources.GlobalResources;
import org.siemac.metamac.srm.web.code.utils.CodesClientSecurityUtils;
import org.siemac.metamac.web.common.client.widgets.MainFormLayoutButton;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;

import com.smartgwt.client.widgets.events.HasClickHandlers;

public class VariableElementMainFormLayout extends InternationalMainFormLayout {

    protected MainFormLayoutButton segregateButton;

    public VariableElementMainFormLayout() {
        super();
        common();
    }

    public VariableElementMainFormLayout(boolean canEdit, boolean canDelete) {
        super(canEdit, canDelete);
        common();
    }

    private void common() {
        segregateButton = new MainFormLayoutButton(getConstants().actionSegregate(), GlobalResources.RESOURCE.segregate().getURL());
        toolStrip.addButton(segregateButton);
    }

    @Override
    public void setViewMode() {
        super.setViewMode();
        showSegregateButton();
    }

    @Override
    public void setEditionMode() {
        super.setEditionMode();
        segregateButton.hide();
    }

    public HasClickHandlers getSegregate() {
        return segregateButton;
    }

    private void showSegregateButton() {
        if (CodesClientSecurityUtils.canSegregateVariableElement()) {
            segregateButton.show();
        }
    }
}
