package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.web.client.widgets.ImportResourceWindow;
import org.siemac.metamac.srm.web.shared.ImportableResourceTypeEnum;
import org.siemac.metamac.srm.web.shared.utils.SrmSharedTokens;

import com.smartgwt.client.widgets.form.fields.HiddenItem;

public class ImportCodeOrdersWindow extends ImportResourceWindow {

    public ImportCodeOrdersWindow() {
        super(getConstants().actionImportCodeOrders());

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
}
