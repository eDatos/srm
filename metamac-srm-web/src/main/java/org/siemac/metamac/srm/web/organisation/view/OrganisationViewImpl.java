package org.siemac.metamac.srm.web.organisation.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.organisation.domain.shared.OrganisationMetamacVisualisationResult;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.client.widgets.AnnotationsPanel;
import org.siemac.metamac.srm.web.client.widgets.CustomTabSet;
import org.siemac.metamac.srm.web.client.widgets.CustomVLayout;
import org.siemac.metamac.srm.web.organisation.model.ds.ContactDS;
import org.siemac.metamac.srm.web.organisation.model.ds.OrganisationDS;
import org.siemac.metamac.srm.web.organisation.model.record.ContactRecord;
import org.siemac.metamac.srm.web.organisation.presenter.OrganisationPresenter;
import org.siemac.metamac.srm.web.organisation.utils.CommonUtils;
import org.siemac.metamac.srm.web.organisation.utils.OrganisationsClientSecurityUtils;
import org.siemac.metamac.srm.web.organisation.utils.OrganisationsFormUtils;
import org.siemac.metamac.srm.web.organisation.view.handlers.OrganisationUiHandlers;
import org.siemac.metamac.srm.web.organisation.widgets.ContactMainFormLayout;
import org.siemac.metamac.srm.web.organisation.widgets.OrganisationContactSearchSectionStack;
import org.siemac.metamac.srm.web.organisation.widgets.OrganisationMainFormLayout;
import org.siemac.metamac.srm.web.organisation.widgets.OrganisationsTreeGrid;
import org.siemac.metamac.web.common.client.widgets.CustomListGrid;
import org.siemac.metamac.web.common.client.widgets.CustomSectionStack;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.TitleLabel;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageRichTextEditorItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.organisation.ContactDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.Overflow;
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
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class OrganisationViewImpl extends ViewWithUiHandlers<OrganisationUiHandlers> implements OrganisationPresenter.OrganisationView {

    private TitleLabel titleLabel;
    private VLayout panel;
    private OrganisationMainFormLayout mainFormLayout;

    private CustomTabSet tabSet;
    private Tab organisationTab;

    private CustomVLayout organisationsTreeGridLayout;
    private OrganisationsTreeGrid organisationsTreeGrid;

    // View forms
    private GroupDynamicForm identifiersForm;
    private GroupDynamicForm productionDescriptorsForm;
    private GroupDynamicForm contentDescriptorsForm;
    private GroupDynamicForm commentsForm;
    private AnnotationsPanel annotationsPanel;

    // Edition forms
    private GroupDynamicForm identifiersEditionForm;
    private GroupDynamicForm productionDescriptorsEditionForm;
    private GroupDynamicForm contentDescriptorsEditionForm;
    private GroupDynamicForm commentsEditionForm;
    private AnnotationsPanel annotationsEditionPanel;

    // Contacts
    private OrganisationContactSearchSectionStack searchSectionStack;
    private CustomListGrid contactListGrid;
    private ContactMainFormLayout contactMainFormLayout;
    private ToolStripButton contactNewButton;
    private ToolStripButton contactDeleteButton;
    private DeleteConfirmationWindow contactDeleteConfirmationWindow;

    private OrganisationSchemeMetamacDto organisationSchemeMetamacDto;
    private OrganisationMetamacDto organisationDto;
    private List<ContactDto> contactDtos;

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
        organisationsTreeGrid.setAutoFitMaxRecords(10);

        organisationsTreeGridLayout = new CustomVLayout();
        CustomSectionStack sectionStack = new CustomSectionStack(getConstants().organisationSchemeOrganisations());
        sectionStack.getDefaultSection().setItems(organisationsTreeGrid);
        organisationsTreeGridLayout.addMember(sectionStack);

        //
        // ORGANISATION
        //

        mainFormLayout = new OrganisationMainFormLayout();
        bindMainFormLayoutEvents();

        createViewForm();
        createEditionForm();

        // CONTACTS

        // Search section

        searchSectionStack = new OrganisationContactSearchSectionStack();
        searchSectionStack.getAdvancedSearchForm().setNumCols(2);

        // ToolStrip

        ToolStrip contactsToolStrip = new ToolStrip();

        contactNewButton = new ToolStripButton(getConstants().actionNew(), RESOURCE.newListGrid().getURL());
        contactNewButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                contactListGrid.deselectAllRecords();
                contactMainFormLayout.setContact(organisationSchemeMetamacDto, new ContactDto(), true);
                contactMainFormLayout.setEditionMode();
                contactMainFormLayout.show();
            }
        });

        contactDeleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().organisationContactDeleteConfirmationTitle(), getConstants().organisationContactDeleteConfirmation());
        contactDeleteConfirmationWindow.setVisible(false);
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
        contactDeleteButton.setVisible(false);
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
                    showContactListGridDeleteButton(contactListGrid.getSelectedRecords());
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
                    contactMainFormLayout.setContact(organisationSchemeMetamacDto, selectedContact, false);
                    contactMainFormLayout.show();
                }
            }
        });

        // Contact form

        contactMainFormLayout = new ContactMainFormLayout();
        contactMainFormLayout.setVisible(false);
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
        contactListGridLayout.addMember(searchSectionStack);
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
        contactsSectionLayout.addMember(contactLayout);

        //
        // PANEL LAYOUT
        //

        VLayout subPanel = new VLayout();
        subPanel.setOverflow(Overflow.SCROLL);
        subPanel.setMembersMargin(5);
        subPanel.addMember(organisationsTreeGridLayout);
        titleLabel = new TitleLabel();

        tabSet = new CustomTabSet();
        tabSet.setStyleName("marginTop15");

        // Organisation tab
        organisationTab = new Tab(getConstants().organisation());
        organisationTab.setPane(mainFormLayout);
        tabSet.addTab(organisationTab);

        // Contacts tab
        Tab contactsTab = new Tab(getConstants().organisationContacts());
        contactsTab.setPane(contactsSectionLayout);
        tabSet.addTab(contactsTab);

        VLayout tabSubPanel = new VLayout();
        tabSubPanel.addMember(titleLabel);
        tabSubPanel.addMember(tabSet);
        tabSubPanel.setMargin(15);
        subPanel.addMember(tabSubPanel);
        panel.addMember(subPanel);
    }

    @Override
    public void setUiHandlers(OrganisationUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        organisationsTreeGrid.setUiHandlers(uiHandlers);
        searchSectionStack.setUiHandlers(uiHandlers);
    }

    private void bindMainFormLayoutEvents() {
        mainFormLayout.getTranslateToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                boolean translationsShowed = mainFormLayout.getTranslateToolStripButton().isSelected();
                identifiersForm.setTranslationsShowed(translationsShowed);
                identifiersEditionForm.setTranslationsShowed(translationsShowed);

                productionDescriptorsForm.setTranslationsShowed(translationsShowed);
                productionDescriptorsEditionForm.setTranslationsShowed(translationsShowed);

                contentDescriptorsForm.setTranslationsShowed(translationsShowed);
                contentDescriptorsEditionForm.setTranslationsShowed(translationsShowed);

                commentsForm.setTranslationsShowed(translationsShowed);
                commentsEditionForm.setTranslationsShowed(translationsShowed);

                annotationsPanel.setTranslationsShowed(translationsShowed);
                annotationsEditionPanel.setTranslationsShowed(translationsShowed);
            }
        });

        mainFormLayout.getDeleteConfirmationWindow().getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deleteOrganisation(organisationDto);
            }
        });

        mainFormLayout.getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (validateEditionForms()) {
                    // See: METAMAC-2516
                    // Two invokes to getXXXDto() is needed for Chrome, please don't remove this two call fix.
                    getOrganisationDto();
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

                        @Override
                        public void execute() {
                            getUiHandlers().updateOrganisation(getOrganisationDto());
                        }
                    });
                }
            }
        });
    }

    private void createViewForm() {
        // Identifiers Form
        identifiersForm = new GroupDynamicForm(getConstants().formIdentifiers());
        ViewTextItem code = new ViewTextItem(OrganisationDS.CODE, getConstants().identifiableArtefactCode());
        ViewMultiLanguageTextItem name = new ViewMultiLanguageTextItem(OrganisationDS.NAME, getConstants().nameableArtefactName());
        ViewTextItem uriProvider = new ViewTextItem(OrganisationDS.URI, getConstants().identifiableArtefactUriProvider());
        ViewTextItem urn = new ViewTextItem(OrganisationDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(OrganisationDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        identifiersForm.setFields(code, name, uriProvider, urn, urnProvider);

        // Production descriptors
        productionDescriptorsForm = new GroupDynamicForm(getConstants().formProductionDescriptors());
        ViewTextItem creationDate = new ViewTextItem(OrganisationDS.CREATION_DATE, getConstants().identifiableArtefactCreationDate());
        productionDescriptorsForm.setFields(creationDate);

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
        mainFormLayout.addViewCanvas(productionDescriptorsForm);
        mainFormLayout.addViewCanvas(contentDescriptorsForm);
        mainFormLayout.addViewCanvas(commentsForm);
        mainFormLayout.addViewCanvas(annotationsPanel);
    }

    private void createEditionForm() {
        // Identifiers Form
        identifiersEditionForm = new GroupDynamicForm(getConstants().formIdentifiers());

        ViewTextItem codeView = new ViewTextItem(OrganisationDS.CODE_VIEW, getConstants().identifiableArtefactCode());
        codeView.setShowIfCondition(getStaticCodeFormItemIfFunction());

        RequiredTextItem code = new RequiredTextItem(OrganisationDS.CODE, getConstants().identifiableArtefactCode());
        code.setValidators(SemanticIdentifiersUtils.getOrganisationIdentifierCustomValidator());
        code.setShowIfCondition(getCodeFormItemIfFunction());

        MultiLanguageTextItem name = new MultiLanguageTextItem(OrganisationDS.NAME, getConstants().nameableArtefactName());
        name.setRequired(true);
        ViewTextItem uriProvider = new ViewTextItem(OrganisationDS.URI, getConstants().identifiableArtefactUriProvider());
        ViewTextItem urn = new ViewTextItem(OrganisationDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(OrganisationDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        identifiersEditionForm.setFields(codeView, code, name, uriProvider, urn, urnProvider);

        // Production descriptors
        productionDescriptorsEditionForm = new GroupDynamicForm(getConstants().formProductionDescriptors());
        ViewTextItem creationDate = new ViewTextItem(OrganisationDS.CREATION_DATE, getConstants().identifiableArtefactCreationDate());
        productionDescriptorsEditionForm.setFields(creationDate);

        // Content descriptors
        contentDescriptorsEditionForm = new GroupDynamicForm(getConstants().formContentDescriptors());
        ViewTextItem type = new ViewTextItem(OrganisationDS.TYPE, getConstants().organisationType());
        MultiLanguageRichTextEditorItem description = new MultiLanguageRichTextEditorItem(OrganisationDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        contentDescriptorsEditionForm.setFields(type, description);

        // Comments
        commentsEditionForm = new GroupDynamicForm(getConstants().nameableArtefactComments());
        MultiLanguageRichTextEditorItem comments = new MultiLanguageRichTextEditorItem(OrganisationDS.COMMENTS, getConstants().nameableArtefactComments());
        commentsEditionForm.setFields(comments);

        // Annotations
        annotationsEditionPanel = new AnnotationsPanel(false);

        mainFormLayout.addEditionCanvas(identifiersEditionForm);
        mainFormLayout.addEditionCanvas(productionDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(contentDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(commentsEditionForm);
        mainFormLayout.addEditionCanvas(annotationsEditionPanel);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == OrganisationPresenter.TYPE_SetContextAreaContentOrganisationsToolBar) {
            if (content != null) {
                panel.addMember(content, 0);
            }
        } else {
            // To support inheritance in your views it is good practice to call super.setInSlot when you can't handle the call.
            // Who knows, maybe the parent class knows what to do with this slot.
            super.setInSlot(slot, content);
        }
    }

    @Override
    public void selectOrganisationTab() {
        tabSet.selectTab(organisationTab);
    }

    @Override
    public void setOrganisation(OrganisationMetamacDto organisationDto, OrganisationSchemeMetamacDto organisationSchemeMetamacDto, Long contactToShowId) {
        setOrganisationScheme(organisationSchemeMetamacDto, organisationDto);
        setOrganisation(organisationDto, contactToShowId);
        searchSectionStack.setOrganisationMetamacDto(organisationDto);
    }

    @Override
    public void setOrganisation(OrganisationMetamacDto organisationDto, Long contactToShowId) {
        this.organisationDto = organisationDto;

        getUiHandlers().retrieveOrganisationListByScheme(organisationDto.getItemSchemeVersionUrn());

        // Set title
        titleLabel.setContents(org.siemac.metamac.srm.web.client.utils.CommonUtils.getResourceTitle(organisationDto));

        mainFormLayout.setViewMode();

        setOrganisationViewMode(organisationDto);
        setOrganisationEditionMode(organisationDto);

        // Contacts
        setContacts(organisationDto.getContacts(), contactToShowId);

        markFormsForRedraw();
    }

    @Override
    public void setContacts(List<ContactDto> contactDtos, Long contactToShowId) {
        this.contactDtos = new ArrayList<ContactDto>();
        this.contactDtos.addAll(organisationDto.getContacts());

        // Hide previous contact form
        contactMainFormLayout.hide();

        ContactRecord[] records = new ContactRecord[contactDtos.size()];
        for (int i = 0; i < records.length; i++) {
            records[i] = org.siemac.metamac.srm.web.organisation.utils.OrganisationsRecordUtils.getContactRecord(contactDtos.get(i));
        }
        contactListGrid.setData(records);

        // Select last contact updated (if exists)
        if (contactToShowId != null) {
            Record record = contactListGrid.getRecordList().find(ContactDS.ID, contactToShowId);
            if (record != null) {
                contactListGrid.selectRecord(record);
                if (record instanceof ContactRecord) {
                    contactMainFormLayout.setContact(organisationSchemeMetamacDto, ((ContactRecord) record).getContactDto(), false);
                    contactMainFormLayout.show();
                }
            }
        }
    }

    private void setOrganisationScheme(OrganisationSchemeMetamacDto organisationSchemeMetamacDto, OrganisationMetamacDto organisationDto) {
        this.organisationSchemeMetamacDto = organisationSchemeMetamacDto;

        // Security
        mainFormLayout.setOrganisationScheme(organisationSchemeMetamacDto, organisationDto);
        contactMainFormLayout.setCanEdit(OrganisationsClientSecurityUtils.canUpdateContact(organisationSchemeMetamacDto));
        contactNewButton.setVisible(OrganisationsClientSecurityUtils.canCreateContact(organisationSchemeMetamacDto));

        markFormsForRedraw();
    }

    @Override
    public void setOrganisationList(OrganisationSchemeMetamacDto organisationSchemeMetamacDto, List<OrganisationMetamacVisualisationResult> itemVisualisationResults) {
        if (OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME.equals(organisationSchemeMetamacDto.getType())) {
            organisationsTreeGrid.setOrganisations(organisationSchemeMetamacDto, itemVisualisationResults);
            organisationsTreeGrid.selectItem(organisationDto.getUrn());
            organisationsTreeGridLayout.show();
        } else {
            organisationsTreeGridLayout.hide();
        }
    }

    private void setOrganisationViewMode(OrganisationMetamacDto organisationDto) {
        // Identifiers Form
        identifiersForm.setValue(OrganisationDS.CODE, organisationDto.getCode());
        identifiersForm.setValue(OrganisationDS.NAME, organisationDto.getName());
        identifiersForm.setValue(OrganisationDS.URI, organisationDto.getUriProvider());
        identifiersForm.setValue(OrganisationDS.URN, organisationDto.getUrn());
        identifiersForm.setValue(OrganisationDS.URN_PROVIDER, organisationDto.getUrnProvider());

        // Production descriptors form
        productionDescriptorsForm.setValue(OrganisationDS.CREATION_DATE, organisationDto.getCreatedDate());

        // Content descriptors
        contentDescriptorsForm.setValue(OrganisationDS.TYPE, CommonUtils.getOrganisationTypeName(organisationDto.getType()));
        contentDescriptorsForm.setValue(OrganisationDS.DESCRIPTION, organisationDto.getDescription());

        // Comments
        commentsForm.setValue(OrganisationDS.COMMENTS, organisationDto.getComment());

        // Annotations
        annotationsPanel.setAnnotations(organisationDto.getAnnotations(), organisationSchemeMetamacDto);
    }

    private void setOrganisationEditionMode(OrganisationMetamacDto organisationDto) {
        // Identifiers Form
        identifiersEditionForm.setValue(OrganisationDS.CODE, organisationDto.getCode());
        identifiersEditionForm.setValue(OrganisationDS.CODE_VIEW, organisationDto.getCode());
        identifiersEditionForm.setValue(OrganisationDS.NAME, organisationDto.getName());
        identifiersEditionForm.setValue(OrganisationDS.URI, organisationDto.getUriProvider());
        identifiersEditionForm.setValue(OrganisationDS.URN, organisationDto.getUrn());
        identifiersEditionForm.setValue(OrganisationDS.URN_PROVIDER, organisationDto.getUrnProvider());
        identifiersEditionForm.markForRedraw();

        // Production descriptors form
        productionDescriptorsEditionForm.setValue(OrganisationDS.CREATION_DATE, organisationDto.getCreatedDate());

        // Content descriptors
        contentDescriptorsEditionForm.setValue(OrganisationDS.TYPE, CommonUtils.getOrganisationTypeName(organisationDto.getType()));
        contentDescriptorsEditionForm.setValue(OrganisationDS.DESCRIPTION, organisationDto.getDescription());
        contentDescriptorsEditionForm.markForRedraw();

        // Comments
        commentsEditionForm.setValue(OrganisationDS.COMMENTS, organisationDto.getComment());

        // Annotations
        annotationsEditionPanel.setAnnotations(organisationDto.getAnnotations(), organisationSchemeMetamacDto);
    }

    private OrganisationMetamacDto getOrganisationDto() {

        // Identifiers Form
        organisationDto.setCode(identifiersEditionForm.getValueAsString(OrganisationDS.CODE));
        organisationDto.setName(identifiersEditionForm.getValueAsInternationalStringDto(OrganisationDS.NAME));

        // Content descriptors
        organisationDto.setDescription(contentDescriptorsEditionForm.getValueAsInternationalStringDto(OrganisationDS.DESCRIPTION));

        // Comments
        organisationDto.setComment(commentsEditionForm.getValueAsInternationalStringDto(OrganisationDS.COMMENTS));

        // Annotations
        organisationDto.getAnnotations().clear();
        organisationDto.getAnnotations().addAll(annotationsEditionPanel.getAnnotations());

        return organisationDto;
    }

    private boolean validateEditionForms() {
        return identifiersEditionForm.validate(false) && contentDescriptorsEditionForm.validate(false);
    }

    private void showContactListGridDeleteButton(ListGridRecord[] records) {
        boolean canAllContactsBeDeleted = true;

        for (ListGridRecord record : records) {
            ContactDto contactDto = ((ContactRecord) record).getContactDto();
            if (!OrganisationsClientSecurityUtils.canDeleteContact(organisationSchemeMetamacDto, contactDto)) {
                canAllContactsBeDeleted = false;
                break;
            }
        }

        if (canAllContactsBeDeleted) {
            contactDeleteButton.show();
        } else {
            contactDeleteButton.hide();
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

    private void markFormsForRedraw() {
        identifiersForm.markForRedraw();
        identifiersEditionForm.markForRedraw();

        contentDescriptorsForm.markForRedraw();
        contentDescriptorsEditionForm.markForRedraw();

        commentsForm.markForRedraw();
        commentsEditionForm.markForRedraw();

        annotationsPanel.markForRedraw();
        annotationsEditionPanel.markForRedraw();
    }

    // ------------------------------------------------------------------------------------------------------------
    // FORM ITEM IF FUNCTIONS
    // ------------------------------------------------------------------------------------------------------------

    // CODE

    private FormItemIfFunction getCodeFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return OrganisationsFormUtils.canOrganisationCodeBeEdited(organisationSchemeMetamacDto, organisationDto);
            }
        };
    }

    private FormItemIfFunction getStaticCodeFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !OrganisationsFormUtils.canOrganisationCodeBeEdited(organisationSchemeMetamacDto, organisationDto);
            }
        };
    }
}
