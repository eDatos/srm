package org.siemac.metamac.srm.web.concept.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptSchemeUiHandlers;
import org.siemac.metamac.srm.web.shared.ImportableResourceTypeEnum;
import org.siemac.metamac.srm.web.shared.utils.SrmSharedTokens;
import org.siemac.metamac.web.common.client.widgets.ImportResourceWindow;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCheckboxItem;

import com.smartgwt.client.widgets.form.fields.HiddenItem;

public class ImportConceptsWindow extends ImportResourceWindow {

    private ConceptSchemeUiHandlers uiHandlers;

    public ImportConceptsWindow() {
        super(getConstants().actionImportConcepts());

        UploadConceptsForm form = new UploadConceptsForm();
        setForm(form);
    }

    public void setConceptScheme(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        ((HiddenItem) form.getItem(SrmSharedTokens.UPLOAD_PARAM_CONCEPT_SCHEME_URN)).setDefaultValue(conceptSchemeMetamacDto.getUrn());
    }

    private class UploadConceptsForm extends UploadForm {

        public UploadConceptsForm() {
            super(getConstants().concepts());

            HiddenItem fileTypeItem = new HiddenItem(SrmSharedTokens.UPLOAD_PARAM_FILE_TYPE);
            fileTypeItem.setDefaultValue(ImportableResourceTypeEnum.CONCEPTS.name());

            HiddenItem conceptSchemeUrnItem = new HiddenItem(SrmSharedTokens.UPLOAD_PARAM_CONCEPT_SCHEME_URN);

            CustomCheckboxItem updateExistingConceptsItem = new CustomCheckboxItem(SrmSharedTokens.UPLOAD_PARAM_UPDATE_EXISTING, getConstants().conceptsUploadUpdateExisting());

            addFieldsInThePenultimePosition(fileTypeItem, conceptSchemeUrnItem, updateExistingConceptsItem);
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

    public void setUiHandlers(ConceptSchemeUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public ConceptSchemeUiHandlers getUiHandlers() {
        return uiHandlers;
    }
}
