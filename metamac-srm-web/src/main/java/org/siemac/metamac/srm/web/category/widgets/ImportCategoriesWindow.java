package org.siemac.metamac.srm.web.category.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.web.category.view.handlers.CategorySchemeUiHandlers;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.shared.ImportableResourceTypeEnum;
import org.siemac.metamac.srm.web.shared.utils.SrmSharedTokens;
import org.siemac.metamac.web.common.client.widgets.ImportResourceWindow;
import org.siemac.metamac.web.common.client.widgets.InformationLabel;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCheckboxItem;

import com.smartgwt.client.widgets.form.fields.HiddenItem;

public class ImportCategoriesWindow extends ImportResourceWindow {

    private CategorySchemeUiHandlers uiHandlers;

    public ImportCategoriesWindow() {
        super(getConstants().actionImportCategoriesTsv());

        UploadCategoriesForm form = new UploadCategoriesForm();
        setForm(form);

        InformationLabel informationLabel = new InformationLabel(getConstants().itemImportationNotDeletingElementsInfoMessage());
        informationLabel.setWidth(getWidth());
        informationLabel.setMargin(10);
        body.addMember(informationLabel, 1);
    }

    public void setCategorycheme(CategorySchemeMetamacDto categorySchemeMetamacDto) {
        ((HiddenItem) form.getItem(SrmSharedTokens.UPLOAD_PARAM_CATEGORY_SCHEME_URN)).setDefaultValue(categorySchemeMetamacDto.getUrn());
    }

    private class UploadCategoriesForm extends UploadForm {

        public UploadCategoriesForm() {
            super(getConstants().categories());

            HiddenItem fileTypeItem = new HiddenItem(SrmSharedTokens.UPLOAD_PARAM_FILE_TYPE);
            fileTypeItem.setDefaultValue(ImportableResourceTypeEnum.CATEGORIES.name());

            HiddenItem categorySchemeUrnItem = new HiddenItem(SrmSharedTokens.UPLOAD_PARAM_CATEGORY_SCHEME_URN);

            CustomCheckboxItem updateExistingCategoriesItem = new CustomCheckboxItem(SrmSharedTokens.UPLOAD_PARAM_UPDATE_EXISTING, getConstants().categoriesUploadUpdateExisting());

            addFieldsInThePenultimePosition(fileTypeItem, categorySchemeUrnItem, updateExistingCategoriesItem);
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

    public void setUiHandlers(CategorySchemeUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public CategorySchemeUiHandlers getUiHandlers() {
        return uiHandlers;
    }
}
