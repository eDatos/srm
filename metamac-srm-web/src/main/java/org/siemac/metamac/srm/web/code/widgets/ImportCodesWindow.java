package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.web.client.widgets.ImportResourceWindow;
import org.siemac.metamac.srm.web.shared.ImportableResourceTypeEnum;
import org.siemac.metamac.srm.web.shared.utils.SharedTokens;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCheckboxItem;

import com.smartgwt.client.widgets.form.fields.HiddenItem;

public class ImportCodesWindow extends ImportResourceWindow {

    public ImportCodesWindow() {
        super(getConstants().actionImportCodes());

        UploadCodesForm form = new UploadCodesForm();
        setForm(form);
    }

    public void setCodelist(CodelistMetamacDto codelistMetamacDto) {
        ((HiddenItem) form.getItem(SharedTokens.UPLOAD_PARAM_CODELIST_URN)).setDefaultValue(codelistMetamacDto.getUrn());
    }

    private class UploadCodesForm extends UploadForm {

        public UploadCodesForm() {
            super(getConstants().codes());

            HiddenItem fileTypeItem = new HiddenItem(SharedTokens.UPLOAD_PARAM_FILE_TYPE);
            fileTypeItem.setDefaultValue(ImportableResourceTypeEnum.CODES.name());

            HiddenItem codelistUrnItem = new HiddenItem(SharedTokens.UPLOAD_PARAM_CODELIST_URN);

            CustomCheckboxItem updateExistingCodesItem = new CustomCheckboxItem(SharedTokens.UPDATE_PARAM_UPDATE_EXISTING, getConstants().codesUploadUpdateExisting());

            addFieldsInThePenultimePosition(fileTypeItem, codelistUrnItem, updateExistingCodesItem);
        }
    }
}