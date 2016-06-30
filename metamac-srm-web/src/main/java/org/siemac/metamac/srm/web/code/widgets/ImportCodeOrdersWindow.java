package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.code.view.handlers.CodelistUiHandlers;
import org.siemac.metamac.srm.web.shared.ImportableResourceTypeEnum;
import org.siemac.metamac.srm.web.shared.utils.SrmSharedTokens;
import org.siemac.metamac.web.common.client.widgets.ImportResourceWindow;

import com.smartgwt.client.widgets.form.fields.HiddenItem;

public class ImportCodeOrdersWindow extends ImportResourceWindow {

    private CodelistUiHandlers uiHandlers;

    public ImportCodeOrdersWindow() {
        super(getConstants().actionImportCodeOrdersTsv());

        UploadOrdersForm form = new UploadOrdersForm();
        setForm(form);
    }

    public void setCodelist(CodelistMetamacDto codelistMetamacDto) {
        ((HiddenItem) form.getItem(SrmSharedTokens.UPLOAD_PARAM_CODELIST_URN)).setDefaultValue(codelistMetamacDto.getUrn());
    }

    private class UploadOrdersForm extends UploadForm {

        public UploadOrdersForm() {
            super(getConstants().codelistOrders());

            HiddenItem fileTypeItem = new HiddenItem(SrmSharedTokens.UPLOAD_PARAM_FILE_TYPE);
            fileTypeItem.setDefaultValue(ImportableResourceTypeEnum.CODES_ORDER.name());

            HiddenItem codelistUrnItem = new HiddenItem(SrmSharedTokens.UPLOAD_PARAM_CODELIST_URN);

            addFieldsInThePenultimePosition(fileTypeItem, codelistUrnItem);
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

    public void setUiHandlers(CodelistUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public CodelistUiHandlers getUiHandlers() {
        return uiHandlers;
    }
}
