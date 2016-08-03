package org.siemac.metamac.srm.web.organisation.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.organisation.view.handlers.OrganisationSchemeUiHandlers;
import org.siemac.metamac.srm.web.shared.ImportableResourceTypeEnum;
import org.siemac.metamac.srm.web.shared.utils.SrmSharedTokens;
import org.siemac.metamac.web.common.client.widgets.ImportResourceWindow;
import org.siemac.metamac.web.common.client.widgets.InformationLabel;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCheckboxItem;

import com.smartgwt.client.widgets.form.fields.HiddenItem;

public class ImportOrganisationsWindow extends ImportResourceWindow {

    private OrganisationSchemeUiHandlers uiHandlers;

    public ImportOrganisationsWindow() {
        super(getConstants().actionImportOrganisationsTsv());

        UploadOrganisationsForm form = new UploadOrganisationsForm();
        setForm(form);

        InformationLabel informationLabel = new InformationLabel(getConstants().itemImportationNotDeletingElementsInfoMessage());
        informationLabel.setWidth(getWidth());
        informationLabel.setMargin(10);
        body.addMember(informationLabel, 1);
    }

    public void setOrganisationScheme(OrganisationSchemeMetamacDto organisationSchemeMetamacDto) {
        ((HiddenItem) form.getItem(SrmSharedTokens.UPLOAD_PARAM_ORGANISATION_SCHEME_URN)).setDefaultValue(organisationSchemeMetamacDto.getUrn());
    }

    private class UploadOrganisationsForm extends UploadForm {

        public UploadOrganisationsForm() {
            super(getConstants().organisations());

            HiddenItem fileTypeItem = new HiddenItem(SrmSharedTokens.UPLOAD_PARAM_FILE_TYPE);
            fileTypeItem.setDefaultValue(ImportableResourceTypeEnum.ORGANISATIONS.name());

            HiddenItem organisationSchemeUrnItem = new HiddenItem(SrmSharedTokens.UPLOAD_PARAM_ORGANISATION_SCHEME_URN);

            CustomCheckboxItem updateExistingOrganisationsItem = new CustomCheckboxItem(SrmSharedTokens.UPLOAD_PARAM_UPDATE_EXISTING, getConstants().organisationsUploadUpdateExisting());

            addFieldsInThePenultimePosition(fileTypeItem, organisationSchemeUrnItem, updateExistingOrganisationsItem);
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

    public void setUiHandlers(OrganisationSchemeUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public OrganisationSchemeUiHandlers getUiHandlers() {
        return uiHandlers;
    }
}
