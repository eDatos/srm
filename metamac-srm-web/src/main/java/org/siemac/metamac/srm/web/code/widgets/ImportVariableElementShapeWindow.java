package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.web.client.widgets.ImportResourceWindow;
import org.siemac.metamac.srm.web.code.utils.CommonUtils;
import org.siemac.metamac.srm.web.shared.ImportableResourceTypeEnum;
import org.siemac.metamac.srm.web.shared.utils.SrmSharedTokens;
import org.siemac.metamac.web.common.client.widgets.InformationLabel;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomSelectItem;

import com.smartgwt.client.widgets.form.fields.HiddenItem;

public class ImportVariableElementShapeWindow extends ImportResourceWindow {

    public ImportVariableElementShapeWindow() {
        super(getConstants().actionImportVariableElementShape());

        InformationLabel informationLabel = new InformationLabel(getConstants().variableElementShapeUploadFileTypeInfoMessage());
        informationLabel.setWidth(300);
        informationLabel.setMargin(10);
        body.addMember(informationLabel);

        UploadShapeForm form = new UploadShapeForm();
        setForm(form);
    }

    public void setVariable(VariableDto variableDto) {
        ((HiddenItem) form.getItem(SrmSharedTokens.UPLOAD_PARAM_VARIABLE_URN)).setDefaultValue(variableDto.getUrn());
    }

    private class UploadShapeForm extends UploadForm {

        public UploadShapeForm() {
            super(getConstants().variableElementShape());

            CustomSelectItem shapeType = new CustomSelectItem(SrmSharedTokens.UPLOAD_PARAM_VARIABLE_ELEMENT_SHAPE_TYPE, getConstants().variableElementShapeType());
            shapeType.setRequired(true);
            shapeType.setValueMap(CommonUtils.getVariableElementShapeTypeHashMap());
            shapeType.setWidth(100);

            HiddenItem fileTypeItem = new HiddenItem(SrmSharedTokens.UPLOAD_PARAM_FILE_TYPE);
            fileTypeItem.setDefaultValue(ImportableResourceTypeEnum.VARIABLE_ELEMENT_SHAPE.name());

            HiddenItem variableUrnItem = new HiddenItem(SrmSharedTokens.UPLOAD_PARAM_VARIABLE_URN);

            addFieldsInThePenultimePosition(shapeType, fileTypeItem, variableUrnItem);
        }
    }
}
