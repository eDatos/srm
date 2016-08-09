package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.code.view.handlers.CodelistUiHandlers;
import org.siemac.metamac.srm.web.shared.ImportableResourceTypeEnum;
import org.siemac.metamac.srm.web.shared.utils.SrmSharedTokens;
import org.siemac.metamac.web.common.client.widgets.ImportResourceWindow;
import org.siemac.metamac.web.common.client.widgets.InformationLabel;
import org.siemac.metamac.web.common.client.widgets.WarningLabel;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCheckboxItem;

import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

public class ImportCodesWindow extends ImportResourceWindow {

    private CodelistUiHandlers uiHandlers;
    protected WarningLabel     warningLabel;

    public ImportCodesWindow() {
        super(getConstants().actionImportCodesTsv());

        UploadCodesForm form = new UploadCodesForm();
        setForm(form);

        buildInformationLabel();
        buildWarningLabel();
    }

    private void buildInformationLabel() {
        InformationLabel informationLabel = new InformationLabel(getConstants().itemImportationNotDeletingElementsInfoMessage());
        informationLabel.setWidth(getWidth());
        informationLabel.setMargin(10);
        body.addMember(informationLabel, 1);
    }

    private void buildWarningLabel() {
        warningLabel = new WarningLabel(getMessages().errorFileRequired());
        warningLabel.setWidth(getWidth());
        warningLabel.setMargin(5);
        warningLabel.hide();
        body.addMember(warningLabel, 0);
    }

    public void setCodelist(CodelistMetamacDto codelistMetamacDto) {
        ((HiddenItem) form.getItem(SrmSharedTokens.UPLOAD_PARAM_CODELIST_URN)).setDefaultValue(codelistMetamacDto.getUrn());
    }

    private class UploadCodesForm extends UploadForm {

        public UploadCodesForm() {
            super(getConstants().codes());

            HiddenItem fileTypeItem = new HiddenItem(SrmSharedTokens.UPLOAD_PARAM_FILE_TYPE);
            fileTypeItem.setDefaultValue(ImportableResourceTypeEnum.CODES.name());

            HiddenItem codelistUrnItem = new HiddenItem(SrmSharedTokens.UPLOAD_PARAM_CODELIST_URN);

            CustomCheckboxItem updateExistingCodesItem = new CustomCheckboxItem(SrmSharedTokens.UPLOAD_PARAM_UPDATE_EXISTING, getConstants().codesUploadUpdateExisting());

            addFieldsInThePenultimePosition(fileTypeItem, codelistUrnItem, updateExistingCodesItem);

            prepareRequiredFileCheck();
        }

        private void prepareRequiredFileCheck() {
            getUploadItem().addChangeHandler(new ChangeHandler() {

                @Override
                public void onChange(ChangeEvent event) {
                    warningLabel.hide();
                }
            });

            getUploadButton().addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    String displayValue = ImportCodesWindow.this.form.getUploadItem().getDisplayValue();
                    if (StringUtils.isBlank(displayValue)) {
                        warningLabel.show();
                    }
                }
            });
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
