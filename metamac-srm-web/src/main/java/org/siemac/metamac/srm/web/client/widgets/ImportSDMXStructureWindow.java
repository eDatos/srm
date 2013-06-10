package org.siemac.metamac.srm.web.client.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.web.shared.ImportableResourceTypeEnum;
import org.siemac.metamac.srm.web.shared.utils.SrmSharedTokens;

import com.smartgwt.client.widgets.form.fields.HiddenItem;

public class ImportSDMXStructureWindow extends ImportResourceWindow {

    public ImportSDMXStructureWindow() {
        super(getConstants().actionImportSDMXResource());

        UploadSDMXResourceForm form = new UploadSDMXResourceForm();
        setForm(form);
    }

    private class UploadSDMXResourceForm extends UploadForm {

        public UploadSDMXResourceForm() {
            super(getConstants().sDMXResource());

            HiddenItem fileTypeItem = new HiddenItem(SrmSharedTokens.UPLOAD_PARAM_FILE_TYPE);
            fileTypeItem.setDefaultValue(ImportableResourceTypeEnum.SDMX_STRUCTURE.name());

            addFieldsInThePenultimePosition(fileTypeItem);
        }
    }
}
