package org.siemac.metamac.srm.web.organisation.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.srm.web.organisation.model.ds.ContactDS;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.utils.UrlUtils;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.organisation.ContactDto;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.FormItem;

public class ContactMainFormLayout extends InternationalMainFormLayout {

    private GroupDynamicForm form;
    private GroupDynamicForm editionForm;

    // Stores TRUE when a new contact is going to be created. If cancel button is clicked, this mainFormLayout should be disappear.
    private boolean          isNewContact;

    private ContactDto       contactDto;

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

        getCancelToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (isNewContact) {
                    hide();
                }
            }
        });

        getTranslateToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                boolean translationsShowed = getTranslateToolStripButton().isSelected();
                form.setTranslationsShowed(translationsShowed);
                editionForm.setTranslationsShowed(translationsShowed);
            }
        });
    }

    private void createViewForm() {
        form = new GroupDynamicForm(getConstants().organisationContact());
        form.setNumCols(2);
        form.setFields(getViewFormFields());
        addViewCanvas(form);
    }

    private FormItem[] getViewFormFields() {
        ViewMultiLanguageTextItem name = new ViewMultiLanguageTextItem(ContactDS.NAME, getConstants().organisationContactName());
        ViewMultiLanguageTextItem organisationUnit = new ViewMultiLanguageTextItem(ContactDS.ORGANISATION_UNIT, getConstants().organisationContactOrganisationUnit());
        ViewMultiLanguageTextItem responsibily = new ViewMultiLanguageTextItem(ContactDS.RESPONSIBILITY, getConstants().organisationContactResponsibility());
        ViewTextItem url = new ViewTextItem(ContactDS.URL, getConstants().organisationContactUrl());
        ViewTextItem telephone = new ViewTextItem(ContactDS.TELEPHONE, getConstants().organisationContactTelephone());
        ViewTextItem email = new ViewTextItem(ContactDS.EMAIL, getConstants().organisationContactEmail());
        ViewTextItem fax = new ViewTextItem(ContactDS.FAX, getConstants().organisationContactFax());
        return new FormItem[]{name, organisationUnit, responsibily, url, telephone, email, fax};
    }

    private void createEditionForm() {
        editionForm = new GroupDynamicForm(getConstants().organisationContact());
        editionForm.setNumCols(2);
        editionForm.setFields(getEditionFormFields());
        addEditionCanvas(editionForm);
    }

    private FormItem[] getEditionFormFields() {
        MultiLanguageTextItem name = new MultiLanguageTextItem(ContactDS.NAME, getConstants().organisationContactName());
        name.setRequired(true);
        MultiLanguageTextItem organisationUnit = new MultiLanguageTextItem(ContactDS.ORGANISATION_UNIT, getConstants().organisationContactOrganisationUnit());
        MultiLanguageTextItem responsibility = new MultiLanguageTextItem(ContactDS.RESPONSIBILITY, getConstants().organisationContactResponsibility());
        CustomTextItem url = new CustomTextItem(ContactDS.URL, getConstants().organisationContactUrl());
        url.setValidators(UrlUtils.getUrlValidator());
        MultiTextItem telephone = new MultiTextItem(ContactDS.TELEPHONE, getConstants().organisationContactTelephone());
        MultiTextItem email = new MultiTextItem(ContactDS.EMAIL, getConstants().organisationContactEmail());
        MultiTextItem fax = new MultiTextItem(ContactDS.FAX, getConstants().organisationContactFax());
        return new FormItem[]{name, organisationUnit, responsibility, url, telephone, email, fax};
    }

    public void setContact(ContactDto contactDto, boolean isNewContact) {
        this.contactDto = contactDto;
        this.isNewContact = isNewContact;

        setViewMode();

        setContactViewMode(this.contactDto);
        setContactEditionMode(this.contactDto);
    }

    public ContactDto getContact() {
        contactDto.setName((InternationalStringDto) editionForm.getValue(ContactDS.NAME));
        contactDto.setOrganisationUnit((InternationalStringDto) editionForm.getValue(ContactDS.ORGANISATION_UNIT));
        contactDto.setResponsibility((InternationalStringDto) editionForm.getValue(ContactDS.RESPONSIBILITY));
        contactDto.setUrl(editionForm.getValueAsString(ContactDS.URL));
        // Telephones
        contactDto.getTelephones().clear();
        contactDto.getTelephones().addAll(((MultiTextItem) editionForm.getItem(ContactDS.TELEPHONE)).getValues());
        // Emails
        contactDto.getEmails().clear();
        contactDto.getEmails().addAll(((MultiTextItem) editionForm.getItem(ContactDS.EMAIL)).getValues());
        // Faxes
        contactDto.getFaxes().clear();
        contactDto.getFaxes().addAll(((MultiTextItem) editionForm.getItem(ContactDS.FAX)).getValues());
        return contactDto;
    }

    public boolean validate() {
        return editionForm.validate();
    }

    private void setContactViewMode(ContactDto contactDto) {
        // TODO Why it is necessary to destroy and rebuild the form each time?
        form.destroy();
        createViewForm();
        form.setValue(ContactDS.NAME, RecordUtils.getInternationalStringRecord(contactDto.getName()));
        form.setValue(ContactDS.ORGANISATION_UNIT, RecordUtils.getInternationalStringRecord(contactDto.getOrganisationUnit()));
        form.setValue(ContactDS.RESPONSIBILITY, RecordUtils.getInternationalStringRecord(contactDto.getResponsibility()));
        form.setValue(ContactDS.URL, contactDto.getUrl());
        form.setValue(ContactDS.TELEPHONE, CommonWebUtils.getStringListToString(contactDto.getTelephones()));
        form.setValue(ContactDS.EMAIL, CommonWebUtils.getStringListToString(contactDto.getEmails()));
        form.setValue(ContactDS.FAX, CommonWebUtils.getStringListToString(contactDto.getFaxes()));
        form.markForRedraw();
    }

    private void setContactEditionMode(ContactDto contactDto) {
        // TODO Why it is necessary to destroy and rebuild the form each time?
        editionForm.destroy();
        createEditionForm();
        editionForm.setValue(ContactDS.NAME, RecordUtils.getInternationalStringRecord(contactDto.getName()));
        editionForm.setValue(ContactDS.ORGANISATION_UNIT, RecordUtils.getInternationalStringRecord(contactDto.getOrganisationUnit()));
        editionForm.setValue(ContactDS.RESPONSIBILITY, RecordUtils.getInternationalStringRecord(contactDto.getResponsibility()));
        editionForm.setValue(ContactDS.URL, contactDto.getUrl());
        ((MultiTextItem) editionForm.getItem(ContactDS.TELEPHONE)).setValues(contactDto.getTelephones());
        ((MultiTextItem) editionForm.getItem(ContactDS.EMAIL)).setValues(contactDto.getEmails());
        ((MultiTextItem) editionForm.getItem(ContactDS.FAX)).setValues(contactDto.getFaxes());
        editionForm.markForRedraw();
    }

}
