package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.code.view.handlers.VariableUiHandlers;
import org.siemac.metamac.srm.web.shared.ImportableResourceTypeEnum;
import org.siemac.metamac.srm.web.shared.utils.SrmSharedTokens;
import org.siemac.metamac.web.common.client.widgets.ImportResourceWindow;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCheckboxItem;

import com.smartgwt.client.widgets.form.fields.HiddenItem;

public class ImportVariableElementsWindow extends ImportResourceWindow {

    private VariableUiHandlers uiHandlers;

    public ImportVariableElementsWindow() {
        super(getConstants().actionImportVariableElements());

        UploadOrdersForm form = new UploadOrdersForm();
        setForm(form);
    }

    public void setVariable(VariableDto variableDto) {
        ((HiddenItem) form.getItem(SrmSharedTokens.UPLOAD_PARAM_VARIABLE_URN)).setDefaultValue(variableDto.getUrn());
    }

    private class UploadOrdersForm extends UploadForm {

        public UploadOrdersForm() {
            super(getConstants().variableElements());

            HiddenItem fileTypeItem = new HiddenItem(SrmSharedTokens.UPLOAD_PARAM_FILE_TYPE);
            fileTypeItem.setDefaultValue(ImportableResourceTypeEnum.VARIABLE_ELEMENTS.name());

            HiddenItem variableUrnItem = new HiddenItem(SrmSharedTokens.UPLOAD_PARAM_VARIABLE_URN);

            CustomCheckboxItem updateExistingElementsItem = new CustomCheckboxItem(SrmSharedTokens.UPLOAD_PARAM_UPDATE_EXISTING, getConstants().variableElementsUploadUpdateExisting());

            addFieldsInThePenultimePosition(fileTypeItem, variableUrnItem, updateExistingElementsItem);
        }
    }

    @Override
    public String getRelativeURL(String url) {
        return MetamacSrmWeb.getRelativeURL(url);
    }

    @Override
    public void showWaitPopup() {
        getUiHandlers().showWaitPopup();
    }

    public void setUiHandlers(VariableUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public VariableUiHandlers getUiHandlers() {
        return uiHandlers;
    }
}
