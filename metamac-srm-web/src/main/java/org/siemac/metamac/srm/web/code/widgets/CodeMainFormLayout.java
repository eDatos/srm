package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;
import org.siemac.metamac.srm.web.code.utils.CodesClientSecurityUtils;
import org.siemac.metamac.web.common.client.widgets.MainFormLayoutButton;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;

import com.smartgwt.client.widgets.events.HasClickHandlers;

public class CodeMainFormLayout extends InternationalMainFormLayout {

    protected MainFormLayoutButton updateVariableElementButton;

    public CodeMainFormLayout() {
        common();
    }

    public void setCodelist(CodelistMetamacDto codelistMetamacDto) {
        setCanEdit(CodesClientSecurityUtils.canUpdateCode(codelistMetamacDto));
        setCanDelete(CodesClientSecurityUtils.canDeleteCode(codelistMetamacDto));
        updateButtonsVisibility(codelistMetamacDto);
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

    public void updateButtonsVisibility(CodelistMetamacDto codelistMetamacDto) {
        if (CommonUtils.isMaintainableArtefactPublished(codelistMetamacDto.getLifeCycle().getProcStatus())) {
            showUpdateVariableElementButton(codelistMetamacDto);
        } else {
            updateVariableElementButton.hide();
        }
    }

    protected void showUpdateVariableElementButton(CodelistMetamacDto codelistMetamacDto) {
        if (CodesClientSecurityUtils.canUpdateCodeVariableElement(codelistMetamacDto)) {
            updateVariableElementButton.show();
        }
    }
}
