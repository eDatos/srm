package org.siemac.metamac.srm.web.organisation.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;
import org.siemac.metamac.srm.web.organisation.model.ds.ContactDS;
import org.siemac.metamac.srm.web.organisation.utils.OrganisationsFormUtils;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.utils.UrlUtils;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.organisation.ContactDto;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;

public class ContactMainFormLayout extends InternationalMainFormLayout {

    private GroupDynamicForm             form;
    private GroupDynamicForm             editionForm;

    // Stores TRUE when a new contact is going to be created. If cancel button is clicked, this mainFormLayout should be disappear.
    private boolean                      isNewContact;

    private OrganisationSchemeMetamacDto organisationSchemeMetamacDto;
    private ContactDto                   contactDto;

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

        MultiTextItem url = new MultiTextItem(ContactDS.URL, getConstants().organisationContactUrl(), UrlUtils.getUrlValidator());
        url.setShowIfCondition(getUrlFormItemIfFunction());

        ViewTextItem staticUrl = new ViewTextItem(ContactDS.URL_VIEW, getConstants().organisationContactUrl());
        staticUrl.setShowIfCondition(getStaticUrlFormItemIfFunction());

        MultiTextItem telephone = new MultiTextItem(ContactDS.TELEPHONE, getConstants().organisationContactTelephone());
        telephone.setShowIfCondition(getTelephoneFormItemIfFunction());

        ViewTextItem staticTelephone = new ViewTextItem(ContactDS.TELEPHONE_VIEW, getConstants().organisationContactTelephone());
        staticTelephone.setShowIfCondition(getStaticTelephoneFormItemIfFunction());

        MultiTextItem email = new MultiTextItem(ContactDS.EMAIL, getConstants().organisationContactEmail());
        email.setShowIfCondition(getEmailFormItemIfFunction());

        ViewTextItem staticEmail = new ViewTextItem(ContactDS.EMAIL_VIEW, getConstants().organisationContactEmail());
        staticEmail.setShowIfCondition(getStaticEmailFormItemIfFunction());

        MultiTextItem fax = new MultiTextItem(ContactDS.FAX, getConstants().organisationContactFax());
        fax.setShowIfCondition(getFaxFormItemIfFunction());

        ViewTextItem staticFax = new ViewTextItem(ContactDS.FAX_VIEW, getConstants().organisationContactFax());
        staticFax.setShowIfCondition(getStaticFaxFormItemIfFunction());

        return new FormItem[]{name, organisationUnit, responsibility, url, staticUrl, telephone, staticTelephone, email, staticEmail, fax, staticFax};
    }

    public void setContact(OrganisationSchemeMetamacDto organisationSchemeMetamacDto, ContactDto contactDto, boolean isNewContact) {
        this.organisationSchemeMetamacDto = organisationSchemeMetamacDto;
        this.contactDto = contactDto;
        this.isNewContact = isNewContact;

        setViewMode();

        setContactViewMode(this.contactDto);
        setContactEditionMode(this.contactDto);

        markFormsForRedraw();
    }

    public ContactDto getContact() {
        contactDto.setName((InternationalStringDto) editionForm.getValue(ContactDS.NAME));
        contactDto.setOrganisationUnit((InternationalStringDto) editionForm.getValue(ContactDS.ORGANISATION_UNIT));
        contactDto.setResponsibility((InternationalStringDto) editionForm.getValue(ContactDS.RESPONSIBILITY));
        if (CommonUtils.isDefaultMaintainer(organisationSchemeMetamacDto.getMaintainer())) {
            // URLs
            contactDto.getUrls().clear();
            contactDto.getUrls().addAll(((MultiTextItem) editionForm.getItem(ContactDS.URL)).getValues());
            // Telephones
            contactDto.getTelephones().clear();
            contactDto.getTelephones().addAll(((MultiTextItem) editionForm.getItem(ContactDS.TELEPHONE)).getValues());
            // Emails
            contactDto.getEmails().clear();
            contactDto.getEmails().addAll(((MultiTextItem) editionForm.getItem(ContactDS.EMAIL)).getValues());
            // Faxes
            contactDto.getFaxes().clear();
            contactDto.getFaxes().addAll(((MultiTextItem) editionForm.getItem(ContactDS.FAX)).getValues());
        }
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
        form.setValue(ContactDS.URL, CommonWebUtils.getStringListToString(contactDto.getUrls()));
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
        ((MultiTextItem) editionForm.getItem(ContactDS.URL)).setValues(contactDto.getUrls());
        editionForm.setValue(ContactDS.URL_VIEW, CommonWebUtils.getStringListToString(contactDto.getUrls()));
        ((MultiTextItem) editionForm.getItem(ContactDS.TELEPHONE)).setValues(contactDto.getTelephones());
        editionForm.setValue(ContactDS.TELEPHONE_VIEW, CommonWebUtils.getStringListToString(contactDto.getTelephones()));
        ((MultiTextItem) editionForm.getItem(ContactDS.EMAIL)).setValues(contactDto.getEmails());
        editionForm.setValue(ContactDS.EMAIL_VIEW, CommonWebUtils.getStringListToString(contactDto.getEmails()));
        ((MultiTextItem) editionForm.getItem(ContactDS.FAX)).setValues(contactDto.getFaxes());
        editionForm.setValue(ContactDS.FAX_VIEW, CommonWebUtils.getStringListToString(contactDto.getFaxes()));
        editionForm.markForRedraw();
    }

    private void markFormsForRedraw() {
        form.markForRedraw();
        editionForm.markForRedraw();
    }

    // ------------------------------------------------------------------------------------------------------------
    // FORM ITEM IF FUNCTIONS
    // ------------------------------------------------------------------------------------------------------------

    // URL

    private FormItemIfFunction getUrlFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return OrganisationsFormUtils.canContactUrlBeEdited(organisationSchemeMetamacDto);
            }
        };
    }

    private FormItemIfFunction getStaticUrlFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !OrganisationsFormUtils.canContactUrlBeEdited(organisationSchemeMetamacDto);
            }
        };
    }

    // TELEPHONE

    private FormItemIfFunction getTelephoneFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return OrganisationsFormUtils.canContactTelephoneBeEdited(organisationSchemeMetamacDto);
            }
        };
    }

    private FormItemIfFunction getStaticTelephoneFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !OrganisationsFormUtils.canContactTelephoneBeEdited(organisationSchemeMetamacDto);
            }
        };
    }

    // EMAIL

    private FormItemIfFunction getEmailFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return OrganisationsFormUtils.canContactEmailBeEdited(organisationSchemeMetamacDto);
            }
        };
    }

    private FormItemIfFunction getStaticEmailFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !OrganisationsFormUtils.canContactEmailBeEdited(organisationSchemeMetamacDto);
            }
        };
    }

    // FAX

    private FormItemIfFunction getFaxFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return OrganisationsFormUtils.canContactFaxBeEdited(organisationSchemeMetamacDto);
            }
        };
    }

    private FormItemIfFunction getStaticFaxFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !OrganisationsFormUtils.canContactFaxBeEdited(organisationSchemeMetamacDto);
            }
        };
    }
}
