package org.siemac.metamac.srm.web.organisation.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.client.widgets.AnnotationsPanel;
import org.siemac.metamac.srm.web.client.widgets.CustomVLayout;
import org.siemac.metamac.srm.web.organisation.model.ds.ContactDS;
import org.siemac.metamac.srm.web.organisation.model.ds.OrganisationDS;
import org.siemac.metamac.srm.web.organisation.model.record.ContactRecord;
import org.siemac.metamac.srm.web.organisation.presenter.OrganisationPresenter;
import org.siemac.metamac.srm.web.organisation.utils.CommonUtils;
import org.siemac.metamac.srm.web.organisation.utils.OrganisationsClientSecurityUtils;
import org.siemac.metamac.srm.web.organisation.view.handlers.OrganisationUiHandlers;
import org.siemac.metamac.srm.web.organisation.widgets.ContactMainFormLayout;
import org.siemac.metamac.srm.web.organisation.widgets.OrganisationsTreeGrid;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.CustomListGrid;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.TitleLabel;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextAreaItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.organisation.ContactDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class OrganisationViewImpl extends ViewWithUiHandlers<OrganisationUiHandlers> implements OrganisationPresenter.OrganisationView {

    private VLayout                      panel;
    private InternationalMainFormLayout  mainFormLayout;

    private CustomVLayout                organisationsTreeGridLayout;
    private OrganisationsTreeGrid        organisationsTreeGrid;

    // View forms
    private GroupDynamicForm             identifiersForm;
    private GroupDynamicForm             contentDescriptorsForm;
    private GroupDynamicForm             commentsForm;
    private AnnotationsPanel             annotationsPanel;

    // Edition forms
    private GroupDynamicForm             identifiersEditionForm;
    private GroupDynamicForm             contentDescriptorsEditionForm;
    private GroupDynamicForm             commentsEditionForm;
    private AnnotationsPanel             annotationsEditionPanel;

    // Contacts
    private CustomListGrid               contactListGrid;
    private ContactMainFormLayout        contactMainFormLayout;
    private ToolStripButton              contactNewButton;
    private ToolStripButton              contactDeleteButton;
    private DeleteConfirmationWindow     contactDeleteConfirmationWindow;

    private OrganisationSchemeMetamacDto organisationSchemeMetamacDto;
    private OrganisationMetamacDto       organisationDto;
    private List<ContactDto>             contactDtos;

    @Inject
    public OrganisationViewImpl() {
        super();
        panel = new VLayout();
        panel.setHeight100();
        panel.setOverflow(Overflow.SCROLL);

        //
        // ORGANISATIONS HIERARCHY
        //

        organisationsTreeGrid = new OrganisationsTreeGrid();

        organisationsTreeGridLayout = new CustomVLayout();
        organisationsTreeGridLayout.addMember(new TitleLabel(getConstants().organisationSchemeOrganisations()));
        organisationsTreeGridLayout.addMember(organisationsTreeGrid);

        //
        // ORGANISATION
        //

        mainFormLayout = new InternationalMainFormLayout();
        bindMainFormLayoutEvents();

        createViewForm();
        createEditionForm();

        // CONTACTS

        TitleLabel titleLabel = new TitleLabel(getConstants().organisationContacts());

        // ToolStrip

        ToolStrip contactsToolStrip = new ToolStrip();

        contactNewButton = new ToolStripButton(getConstants().actionNew(), RESOURCE.newListGrid().getURL());
        contactNewButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                contactListGrid.deselectAllRecords();
                contactMainFormLayout.setContact(new ContactDto(), true);
                contactMainFormLayout.setEditionMode();
                contactMainFormLayout.show();
            }
        });

        contactDeleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().organisationContactDeleteConfirmationTitle(), getConstants().organisationContactDeleteConfirmation());
        contactDeleteConfirmationWindow.setVisibility(Visibility.HIDDEN);
        contactDeleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                List<ContactDto> contactsToDelete = getSelectedContacts();
                removeContactsFromOrganisation(contactsToDelete);
                getUiHandlers().updateContacts(organisationDto.getContacts(), null);

                contactDeleteConfirmationWindow.hide();
            }
        });

        contactDeleteButton = new ToolStripButton(getConstants().actionDelete(), RESOURCE.deleteListGrid().getURL());
        contactDeleteButton.setVisibility(Visibility.HIDDEN);
        contactDeleteButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                contactDeleteConfirmationWindow.show();
            }
        });

        contactsToolStrip.addButton(contactNewButton);
        contactsToolStrip.addButton(contactDeleteButton);

        // Contact list

        contactListGrid = new CustomListGrid();
        contactListGrid.setAutoFitMaxRecords(20);
        contactListGrid.setAutoFitData(Autofit.VERTICAL);
        ListGridField nameField = new ListGridField(ContactDS.NAME, getConstants().organisationContactName());
        ListGridField organisationUnitField = new ListGridField(ContactDS.ORGANISATION_UNIT, getConstants().organisationContactOrganisationUnit());
        contactListGrid.setFields(nameField, organisationUnitField);
        contactListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (contactListGrid.getSelectedRecords().length > 0) {
                    // Show delete button
                    showContactListGridDeleteButton();
                    if (contactListGrid.getSelectedRecords().length > 1) {
                        contactMainFormLayout.hide();
                    }
                } else {
                    contactDeleteButton.hide();
                    contactMainFormLayout.hide();
                }
            }
        });
        contactListGrid.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // Clicking checkBox will be ignored
                    ContactDto selectedContact = ((ContactRecord) event.getRecord()).getContactDto();
                    contactMainFormLayout.setContact(selectedContact, false);
                    contactMainFormLayout.show();
                }
            }
        });

        // Contact form

        contactMainFormLayout = new ContactMainFormLayout();
        contactMainFormLayout.setVisibility(Visibility.HIDDEN);
        contactMainFormLayout.getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (contactMainFormLayout.validate()) {
                    ContactDto contactToSave = contactMainFormLayout.getContact();
                    List<ContactDto> contactsUpdated = updateContacts(OrganisationViewImpl.this.contactDtos, contactToSave);
                    organisationDto.getContacts().clear();
                    organisationDto.getContacts().addAll(contactsUpdated);

                    getUiHandlers().updateContacts(OrganisationViewImpl.this.organisationDto.getContacts(), contactToSave.getId());
                }
            }
        });

        VLayout contactListGridLayout = new VLayout();
        contactListGridLayout.addMember(contactsToolStrip);
        contactListGridLayout.addMember(contactListGrid);
        contactListGridLayout.setWidth("50%");

        VLayout contactMainFormLayoutVLayout = new VLayout();
        contactMainFormLayoutVLayout.addMember(contactMainFormLayout);
        contactMainFormLayoutVLayout.setWidth("50%");

        HLayout contactLayout = new HLayout();
        contactLayout.addMember(contactListGridLayout);
        contactLayout.addMember(contactMainFormLayoutVLayout);

        VLayout contactsSectionLayout = new VLayout();
        contactsSectionLayout.setMargin(15);
        contactsSectionLayout.addMember(titleLabel);
        contactsSectionLayout.addMember(contactLayout);

        panel.addMember(organisationsTreeGridLayout);
        panel.addMember(mainFormLayout);
        panel.addMember(contactsSectionLayout);
    }

    @Override
    public void setUiHandlers(OrganisationUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        organisationsTreeGrid.setUiHandlers(uiHandlers);
    }

    private void bindMainFormLayoutEvents() {
        mainFormLayout.getTranslateToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                boolean translationsShowed = mainFormLayout.getTranslateToolStripButton().isSelected();
                identifiersForm.setTranslationsShowed(translationsShowed);
                identifiersEditionForm.setTranslationsShowed(translationsShowed);

                contentDescriptorsForm.setTranslationsShowed(translationsShowed);
                contentDescriptorsEditionForm.setTranslationsShowed(translationsShowed);

                commentsForm.setTranslationsShowed(translationsShowed);
                commentsEditionForm.setTranslationsShowed(translationsShowed);

                annotationsPanel.setTranslationsShowed(translationsShowed);
                annotationsEditionPanel.setTranslationsShowed(translationsShowed);
            }
        });

        mainFormLayout.getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (validateEditionForms()) {
                    getUiHandlers().updateOrganisation(getOrganisationDto());
                }
            }
        });
    }

    private void createViewForm() {
        // Identifiers Form
        identifiersForm = new GroupDynamicForm(getConstants().formIdentifiers());
        ViewTextItem code = new ViewTextItem(OrganisationDS.CODE, getConstants().identifiableArtefactCode());
        ViewMultiLanguageTextItem name = new ViewMultiLanguageTextItem(OrganisationDS.NAME, getConstants().nameableArtefactName());
        ViewTextItem uri = new ViewTextItem(OrganisationDS.URI, getConstants().identifiableArtefactUri());
        ViewTextItem urn = new ViewTextItem(OrganisationDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(OrganisationDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        identifiersForm.setFields(code, name, uri, urn, urnProvider);

        // Content descriptors
        contentDescriptorsForm = new GroupDynamicForm(getConstants().formContentDescriptors());
        ViewTextItem type = new ViewTextItem(OrganisationDS.TYPE, getConstants().organisationType());
        ViewMultiLanguageTextItem description = new ViewMultiLanguageTextItem(OrganisationDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        contentDescriptorsForm.setFields(type, description);

        // Comments
        commentsForm = new GroupDynamicForm(getConstants().nameableArtefactComments());
        ViewMultiLanguageTextItem comments = new ViewMultiLanguageTextItem(OrganisationDS.COMMENTS, getConstants().nameableArtefactComments());
        commentsForm.setFields(comments);

        // Annotations
        annotationsPanel = new AnnotationsPanel(true);

        mainFormLayout.addViewCanvas(identifiersForm);
        mainFormLayout.addViewCanvas(contentDescriptorsForm);
        mainFormLayout.addViewCanvas(commentsForm);
        mainFormLayout.addViewCanvas(annotationsPanel);
    }

    private void createEditionForm() {
        // Identifiers Form
        identifiersEditionForm = new GroupDynamicForm(getConstants().formIdentifiers());

        ViewTextItem codeView = new ViewTextItem(OrganisationDS.CODE_VIEW, getConstants().identifiableArtefactCode());
        codeView.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !canOrganisationCodeBeEdited();
            }
        });

        RequiredTextItem code = new RequiredTextItem(OrganisationDS.CODE, getConstants().identifiableArtefactCode());
        code.setValidators(SemanticIdentifiersUtils.getOrganisationIdentifierCustomValidator());
        code.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return canOrganisationCodeBeEdited();
            }
        });

        MultiLanguageTextItem name = new MultiLanguageTextItem(OrganisationDS.NAME, getConstants().nameableArtefactName());
        name.setRequired(true);
        ViewTextItem uri = new ViewTextItem(OrganisationDS.URI, getConstants().identifiableArtefactUri());
        ViewTextItem urn = new ViewTextItem(OrganisationDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(OrganisationDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        identifiersEditionForm.setFields(codeView, code, name, uri, urn, urnProvider);

        // Content descriptors
        contentDescriptorsEditionForm = new GroupDynamicForm(getConstants().formContentDescriptors());
        ViewTextItem type = new ViewTextItem(OrganisationDS.TYPE, getConstants().organisationType());
        MultiLanguageTextAreaItem description = new MultiLanguageTextAreaItem(OrganisationDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        contentDescriptorsEditionForm.setFields(type, description);

        // Comments
        commentsEditionForm = new GroupDynamicForm(getConstants().nameableArtefactComments());
        MultiLanguageTextAreaItem comments = new MultiLanguageTextAreaItem(OrganisationDS.COMMENTS, getConstants().nameableArtefactComments());
        commentsEditionForm.setFields(comments);

        // Annotations
        annotationsEditionPanel = new AnnotationsPanel(false);

        mainFormLayout.addEditionCanvas(identifiersEditionForm);
        mainFormLayout.addEditionCanvas(contentDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(commentsEditionForm);
        mainFormLayout.addEditionCanvas(annotationsEditionPanel);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setOrganisation(OrganisationMetamacDto organisationDto, Long contactToShowId) {
        this.organisationDto = organisationDto;
        this.contactDtos = new ArrayList<ContactDto>();
        this.contactDtos.addAll(organisationDto.getContacts());

        getUiHandlers().retrieveOrganisationListByScheme(organisationDto.getItemSchemeVersionUrn());

        String defaultLocalisedName = InternationalStringUtils.getLocalisedString(organisationDto.getName());
        String title = defaultLocalisedName != null ? defaultLocalisedName : StringUtils.EMPTY;
        mainFormLayout.setTitleLabelContents(title);

        mainFormLayout.setViewMode();

        setOrganisationViewMode(organisationDto);
        setOrganisationEditionMode(organisationDto);

        // Contacts
        setContacts(organisationDto.getContacts(), contactToShowId);
    }

    @Override
    public void setOrganisationList(OrganisationSchemeMetamacDto organisationSchemeMetamacDto, List<ItemHierarchyDto> itemHierarchyDtos) {
        this.organisationSchemeMetamacDto = organisationSchemeMetamacDto;
        identifiersEditionForm.markForRedraw();

        if (OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME.equals(organisationSchemeMetamacDto.getType())) {
            organisationsTreeGrid.setItems(organisationSchemeMetamacDto, itemHierarchyDtos);
            organisationsTreeGrid.selectItem(organisationDto.getUrn());
            organisationsTreeGridLayout.show();
        } else {
            organisationsTreeGridLayout.hide();
        }

        // Security
        mainFormLayout.setCanEdit(OrganisationsClientSecurityUtils.canUpdateOrganisation(organisationSchemeMetamacDto.getLifeCycle().getProcStatus(), organisationSchemeMetamacDto.getType()));
        contactMainFormLayout.setCanEdit(OrganisationsClientSecurityUtils.canUpdateOrganisation(organisationSchemeMetamacDto.getLifeCycle().getProcStatus(), organisationSchemeMetamacDto.getType()));
        contactNewButton.setVisibility(OrganisationsClientSecurityUtils.canUpdateOrganisation(organisationSchemeMetamacDto.getLifeCycle().getProcStatus(), organisationSchemeMetamacDto.getType())
                ? Visibility.VISIBLE
                : Visibility.HIDDEN);
    }

    private void setOrganisationViewMode(OrganisationMetamacDto organisationDto) {
        // Identifiers Form
        identifiersForm.setValue(OrganisationDS.CODE, organisationDto.getCode());
        identifiersForm.setValue(OrganisationDS.NAME, RecordUtils.getInternationalStringRecord(organisationDto.getName()));
        identifiersForm.setValue(OrganisationDS.URI, organisationDto.getUriProvider());
        identifiersForm.setValue(OrganisationDS.URN, organisationDto.getUrn());
        identifiersForm.setValue(OrganisationDS.URN_PROVIDER, organisationDto.getUrnProvider());

        // Content descriptors
        contentDescriptorsForm.setValue(OrganisationDS.TYPE, CommonUtils.getOrganisationTypeName(organisationDto.getType()));
        contentDescriptorsForm.setValue(OrganisationDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(organisationDto.getDescription()));

        // Comments
        commentsForm.setValue(OrganisationDS.COMMENTS, RecordUtils.getInternationalStringRecord(organisationDto.getComment()));

        // Annotations
        annotationsPanel.setAnnotations(organisationDto.getAnnotations());
    }

    private void setOrganisationEditionMode(OrganisationMetamacDto organisationDto) {
        // Identifiers Form
        identifiersEditionForm.setValue(OrganisationDS.CODE, organisationDto.getCode());
        identifiersEditionForm.setValue(OrganisationDS.CODE_VIEW, organisationDto.getCode());
        identifiersEditionForm.setValue(OrganisationDS.NAME, RecordUtils.getInternationalStringRecord(organisationDto.getName()));
        identifiersEditionForm.setValue(OrganisationDS.URI, organisationDto.getUriProvider());
        identifiersEditionForm.setValue(OrganisationDS.URN, organisationDto.getUrn());
        identifiersEditionForm.setValue(OrganisationDS.URN_PROVIDER, organisationDto.getUrnProvider());
        identifiersEditionForm.markForRedraw();

        // Content descriptors
        contentDescriptorsEditionForm.setValue(OrganisationDS.TYPE, CommonUtils.getOrganisationTypeName(organisationDto.getType()));
        contentDescriptorsEditionForm.setValue(OrganisationDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(organisationDto.getDescription()));
        contentDescriptorsEditionForm.markForRedraw();

        // Comments
        commentsEditionForm.setValue(OrganisationDS.COMMENTS, RecordUtils.getInternationalStringRecord(organisationDto.getComment()));

        // Annotations
        annotationsEditionPanel.setAnnotations(organisationDto.getAnnotations());
    }

    private void setContacts(List<ContactDto> contactDtos, Long contactToShowId) {
        // Hide previous contact form
        contactMainFormLayout.hide();

        ContactRecord[] records = new ContactRecord[contactDtos.size()];
        for (int i = 0; i < records.length; i++) {
            records[i] = org.siemac.metamac.srm.web.organisation.utils.RecordUtils.getContactRecord(contactDtos.get(i));
        }
        contactListGrid.setData(records);

        // Select last contact updated (if exists)
        if (contactToShowId != null) {
            Record record = contactListGrid.getRecordList().find(ContactDS.ID, contactToShowId);
            if (record != null) {
                contactListGrid.selectRecord(record);
                if (record instanceof ContactRecord) {
                    contactMainFormLayout.setContact(((ContactRecord) record).getContactDto(), false);
                    contactMainFormLayout.show();
                }
            }
        }
    }

    private OrganisationMetamacDto getOrganisationDto() {
        // Identifiers Form
        organisationDto.setCode(identifiersEditionForm.getValueAsString(OrganisationDS.CODE));
        organisationDto.setName((InternationalStringDto) identifiersEditionForm.getValue(OrganisationDS.NAME));

        // Content descriptors
        organisationDto.setDescription((InternationalStringDto) contentDescriptorsEditionForm.getValue(OrganisationDS.DESCRIPTION));

        // Comments
        organisationDto.setComment((InternationalStringDto) commentsEditionForm.getValue(OrganisationDS.COMMENTS));

        // Annotations
        organisationDto.getAnnotations().clear();
        organisationDto.getAnnotations().addAll(annotationsEditionPanel.getAnnotations());

        return organisationDto;
    }

    private boolean validateEditionForms() {
        return identifiersEditionForm.validate(false) && contentDescriptorsEditionForm.validate(false);
    }

    private void showContactListGridDeleteButton() {
        if (OrganisationsClientSecurityUtils.canUpdateOrganisation(this.organisationSchemeMetamacDto.getLifeCycle().getProcStatus(), this.organisationSchemeMetamacDto.getType())) {
            contactDeleteButton.show();
        }
    }

    private List<ContactDto> updateContacts(List<ContactDto> contactDtos, ContactDto contactDto) {
        if (contactDto.getId() == null) {
            // If there is a non saved contact in the list, replace it
            boolean contactPrevouslyAdded = false;
            for (int i = 0; i < contactDtos.size(); i++) {
                if (contactDtos.get(i).getId() == null) {
                    contactPrevouslyAdded = true;
                    contactDtos.set(i, contactDto);

                }
            }
            // If not, add it to the organisation contact list
            if (!contactPrevouslyAdded) {
                contactDtos.add(contactDto);
            }
        } else {
            // If contact is being updated, replace it in the organisation
            for (int i = 0; i < contactDtos.size(); i++) {
                if (contactDto.getId().equals(contactDtos.get(i).getId())) {
                    contactDtos.set(i, contactDto);
                }
            }
        }
        return contactDtos;
    }

    private void removeContactsFromOrganisation(List<ContactDto> contactDtos) {
        for (ContactDto contactDto : contactDtos) {
            removeContactFromOrganisation(contactDto);
        }
    }

    private void removeContactFromOrganisation(ContactDto contactDto) {
        for (int i = 0; i < organisationDto.getContacts().size(); i++) {
            if (organisationDto.getContacts().get(i).getId().compareTo(contactDto.getId()) == 0) {
                organisationDto.getContacts().remove(i);
                return;
            }
        }
    }

    private List<ContactDto> getSelectedContacts() {
        List<ContactDto> contacts = new ArrayList<ContactDto>();
        for (ListGridRecord record : contactListGrid.getSelectedRecords()) {
            ContactRecord contactRecord = (ContactRecord) record;
            contacts.add(contactRecord.getContactDto());
        }
        return contacts;
    }

    private boolean canOrganisationCodeBeEdited() {
        // If organisation type is AGENCY, code can only be edited when organisation is not published
        if (OrganisationSchemeTypeEnum.AGENCY_SCHEME.equals(organisationSchemeMetamacDto.getType())) {
            return !org.siemac.metamac.srm.web.client.utils.CommonUtils.isItemSchemePublished(organisationSchemeMetamacDto.getLifeCycle().getProcStatus());
        } else {
            return true;
        }
    }

}
