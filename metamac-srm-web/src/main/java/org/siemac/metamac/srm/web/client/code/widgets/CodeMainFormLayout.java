package org.siemac.metamac.srm.web.client.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;
import org.siemac.metamac.web.common.client.widgets.MainFormLayoutButton;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;

import com.smartgwt.client.widgets.events.HasClickHandlers;

public class CodeMainFormLayout extends InternationalMainFormLayout {

    protected MainFormLayoutButton updateVariableElementButton;

    public CodeMainFormLayout() {
        common();
    }

    public CodeMainFormLayout(boolean canEdit) {
        super(canEdit);
        common();
    }

    private void common() {
        // Add a button to update the variable element of the code
        updateVariableElementButton = new MainFormLayoutButton(getConstants().codeUpdateVariableElement(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.editListGrid()
                .getURL());
        toolStrip.addButton(updateVariableElementButton, 1);
    }

    public HasClickHandlers getUpdateVariableElement() {
        return updateVariableElementButton;
    }

    public void updateButtonsVisibility(ProcStatusEnum procStatusEnum) {
        if (CommonUtils.isItemSchemePublished(procStatusEnum)) {
            showUpdateVariableElementButton();
        } else {
            updateVariableElementButton.hide();
        }
    }

    protected void showUpdateVariableElementButton() {
        // TODO Security
        updateVariableElementButton.show();
    }
}
