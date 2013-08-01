package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.web.code.utils.CodesClientSecurityUtils;
import org.siemac.metamac.web.common.client.widgets.MainFormLayoutButton;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;

import com.smartgwt.client.widgets.events.HasClickHandlers;

public class VariableMainFormLayout extends InternationalMainFormLayout {

    protected VariableDto          variableDto;
    protected MainFormLayoutButton importShape;

    public VariableMainFormLayout() {
        super();
        common();
    }

    public VariableMainFormLayout(boolean canEdit, boolean canDelete) {
        super(canEdit, canDelete);
        common();
    }

    public void setVariable(VariableDto variableDto) {
        this.variableDto = variableDto;
    }

    private void common() {
        importShape = new MainFormLayoutButton(getConstants().actionImportVariableElementShape(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.importResource().getURL());
        toolStrip.addButton(importShape);
    }

    @Override
    public void setViewMode() {
        super.setViewMode();
        showImportShapeButton();
    }

    @Override
    public void setEditionMode() {
        super.setEditionMode();
        importShape.hide();
    }

    public HasClickHandlers getImportShape() {
        return importShape;
    }

    private void showImportShapeButton() {
        if (CodesClientSecurityUtils.canImportVariableElementShape(variableDto)) {
            importShape.show();
        }
    }
}
