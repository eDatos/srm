package org.siemac.metamac.srm.web.organisation.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.web.organisation.model.ds.ContactDS;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.utils.UrlUtils;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.organisation.ContactDto;

public class ContactMainFormLayout extends InternationalMainFormLayout {

    private GroupDynamicForm form;
    private GroupDynamicForm editionForm;

    public ContactMainFormLayout() {
        super();
        common();
    }

    public ContactMainFormLayout(boolean canEdit) {
        super(canEdit);
        common();
    }

    private void common() {
        createViewForm();
        createEditionForm();
    }

    private void createViewForm() {
        form = new GroupDynamicForm(getConstants().organisationContact());
        form.setNumCols(2);
        ViewMultiLanguageTextItem name = new ViewMultiLanguageTextItem(ContactDS.NAME, getConstants().organisationContactName());
        ViewMultiLanguageTextItem organisationUnit = new ViewMultiLanguageTextItem(ContactDS.ORGANISATION_UNIT, getConstants().organisationContactOrganisationUnit());
        ViewMultiLanguageTextItem responsibily = new ViewMultiLanguageTextItem(ContactDS.RESPONSIBILITY, getConstants().organisationContactResponsibility());
        ViewTextItem url = new ViewTextItem(ContactDS.URL, getConstants().organisationContactUrl());
        form.setFields(name, organisationUnit, responsibily, url);
        addViewCanvas(form);
    }

    private void createEditionForm() {
        editionForm = new GroupDynamicForm(getConstants().organisationContact());
        editionForm.setNumCols(2);
        MultiLanguageTextItem name = new MultiLanguageTextItem(ContactDS.NAME, getConstants().organisationContactName());
        MultiLanguageTextItem organisationUnit = new MultiLanguageTextItem(ContactDS.ORGANISATION_UNIT, getConstants().organisationContactOrganisationUnit());
        MultiLanguageTextItem responsibility = new MultiLanguageTextItem(ContactDS.RESPONSIBILITY, getConstants().organisationContactResponsibility());
        CustomTextItem url = new CustomTextItem(ContactDS.URL, getConstants().organisationContactUrl());
        url.setValidators(UrlUtils.getUrlValidator());
        editionForm.setFields(name, organisationUnit, responsibility, url);
        addEditionCanvas(editionForm);
    }

    public void setContact(ContactDto contactDto) {
        setContactViewMode(contactDto);
        setContactEditionMode(contactDto);
    }

    private void setContactViewMode(ContactDto contactDto) {
        form.setValue(ContactDS.NAME, RecordUtils.getInternationalStringRecord(contactDto.getName()));
        form.setValue(ContactDS.ORGANISATION_UNIT, RecordUtils.getInternationalStringRecord(contactDto.getOrganisationUnit()));
        form.setValue(ContactDS.RESPONSIBILITY, RecordUtils.getInternationalStringRecord(contactDto.getResponsibility()));
        form.setValue(ContactDS.URL, contactDto.getUrl());
    }

    private void setContactEditionMode(ContactDto contactDto) {
        editionForm.setValue(ContactDS.NAME, RecordUtils.getInternationalStringRecord(contactDto.getName()));
        editionForm.setValue(ContactDS.ORGANISATION_UNIT, RecordUtils.getInternationalStringRecord(contactDto.getOrganisationUnit()));
        editionForm.setValue(ContactDS.RESPONSIBILITY, RecordUtils.getInternationalStringRecord(contactDto.getResponsibility()));
        editionForm.setValue(ContactDS.URL, contactDto.getUrl());
    }

}
