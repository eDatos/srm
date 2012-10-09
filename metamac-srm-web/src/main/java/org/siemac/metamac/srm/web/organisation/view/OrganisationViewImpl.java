package org.siemac.metamac.srm.web.organisation.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.List;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.widgets.AnnotationsPanel;
import org.siemac.metamac.srm.web.organisation.model.ds.ContactDS;
import org.siemac.metamac.srm.web.organisation.model.ds.OrganisationDS;
import org.siemac.metamac.srm.web.organisation.model.record.ContactRecord;
import org.siemac.metamac.srm.web.organisation.presenter.OrganisationPresenter;
import org.siemac.metamac.srm.web.organisation.utils.CommonUtils;
import org.siemac.metamac.srm.web.organisation.view.handlers.OrganisationUiHandlers;
import org.siemac.metamac.srm.web.organisation.widgets.ContactMainFormLayout;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
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
import com.arte.statistic.sdmx.v2_1.domain.dto.organisation.OrganisationDto;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class OrganisationViewImpl extends ViewWithUiHandlers<OrganisationUiHandlers> implements OrganisationPresenter.OrganisationView {

    private VLayout                     panel;
    private InternationalMainFormLayout mainFormLayout;

    // View forms
    private GroupDynamicForm            identifiersForm;
    private GroupDynamicForm            contentDescriptorsForm;
    private AnnotationsPanel            annotationsPanel;

    // Edition forms
    private GroupDynamicForm            identifiersEditionForm;
    private GroupDynamicForm            contentDescriptorsEditionForm;
    private AnnotationsPanel            annotationsEditionPanel;

    // Contacts
    private CustomListGrid              contactListGrid;
    private ContactMainFormLayout       contactMainFormLayout;
    private ToolStripButton             contactDeleteButton;
    private DeleteConfirmationWindow    contactDeleteConfirmationWindow;

    private OrganisationDto             organisationDto;

    @Inject
    public OrganisationViewImpl() {
        super();
        panel = new VLayout();
        panel.setHeight100();
        panel.setOverflow(Overflow.SCROLL);

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

        ToolStripButton contactNewButton = new ToolStripButton(getConstants().actionNew(), RESOURCE.newListGrid().getURL());
        contactNewButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                contactMainFormLayout.setContact(new ContactDto());
                contactMainFormLayout.show();
            }
        });

        contactDeleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().organisationContactDeleteConfirmationTitle(), getConstants().organisationContactDeleteConfirmation());
        contactDeleteConfirmationWindow.setVisibility(Visibility.HIDDEN);
        contactDeleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                // TODO
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
                } else {
                    contactDeleteButton.hide();
                }
            }
        });
        contactListGrid.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                ContactRecord record = (ContactRecord) event.getRecord();
                contactMainFormLayout.setContact(record.getContactDto());
                contactMainFormLayout.show();
            }
        });

        // Contact form

        contactMainFormLayout = new ContactMainFormLayout();
        contactMainFormLayout.setVisibility(Visibility.HIDDEN);

        VLayout contactListGridLayout = new VLayout();
        contactListGridLayout.addMember(contactsToolStrip);
        contactListGridLayout.addMember(contactListGrid);

        HLayout contactLayout = new HLayout();
        contactLayout.addMember(contactListGridLayout);
        contactLayout.addMember(contactMainFormLayout);

        VLayout contactsSectionLayout = new VLayout();
        contactsSectionLayout.setMargin(15);
        contactsSectionLayout.addMember(titleLabel);
        contactsSectionLayout.addMember(contactLayout);

        panel.addMember(mainFormLayout);
        panel.addMember(contactsSectionLayout);
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

                annotationsPanel.setTranslationsShowed(translationsShowed);
                annotationsEditionPanel.setTranslationsShowed(translationsShowed);
            }
        });

        mainFormLayout.getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (validateEditionForms()) {
                    getUiHandlers().saveOrganisation(getOrganisationDto());
                }
            }
        });
    }

    private void createViewForm() {
        // Identifiers Form
        identifiersForm = new GroupDynamicForm(getConstants().organisationIdentifiers());
        ViewTextItem code = new ViewTextItem(OrganisationDS.CODE, getConstants().identifiableArtefactCode());
        ViewMultiLanguageTextItem name = new ViewMultiLanguageTextItem(OrganisationDS.NAME, getConstants().nameableArtefactName());
        ViewTextItem uri = new ViewTextItem(OrganisationDS.URI, getConstants().identifiableArtefactUri());
        ViewTextItem urn = new ViewTextItem(OrganisationDS.URN, getConstants().identifiableArtefactUrn());
        identifiersForm.setFields(code, name, uri, urn);

        // Content descriptors
        contentDescriptorsForm = new GroupDynamicForm(getConstants().organisationContentDescriptors());
        ViewTextItem type = new ViewTextItem(OrganisationDS.TYPE, getConstants().organisationType());
        ViewMultiLanguageTextItem description = new ViewMultiLanguageTextItem(OrganisationDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        contentDescriptorsForm.setFields(type, description);

        // Annotations
        annotationsPanel = new AnnotationsPanel(true);

        mainFormLayout.addViewCanvas(identifiersForm);
        mainFormLayout.addViewCanvas(contentDescriptorsForm);
        mainFormLayout.addViewCanvas(annotationsPanel);
    }

    private void createEditionForm() {
        // Identifiers Form
        identifiersEditionForm = new GroupDynamicForm(getConstants().organisationIdentifiers());
        RequiredTextItem code = new RequiredTextItem(OrganisationDS.CODE, getConstants().identifiableArtefactCode());
        code.setValidators(CommonWebUtils.getSemanticIdentifierCustomValidator());
        MultiLanguageTextItem name = new MultiLanguageTextItem(OrganisationDS.NAME, getConstants().nameableArtefactName());
        name.setRequired(true);
        ViewTextItem uri = new ViewTextItem(OrganisationDS.URI, getConstants().identifiableArtefactUri());
        ViewTextItem urn = new ViewTextItem(OrganisationDS.URN, getConstants().identifiableArtefactUrn());
        identifiersEditionForm.setFields(code, name, uri, urn);

        // Content descriptors
        contentDescriptorsEditionForm = new GroupDynamicForm(getConstants().organisationContentDescriptors());
        ViewTextItem type = new ViewTextItem(OrganisationDS.TYPE, getConstants().organisationType());
        MultiLanguageTextAreaItem description = new MultiLanguageTextAreaItem(OrganisationDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        contentDescriptorsEditionForm.setFields(type, description);

        // Annotations
        annotationsEditionPanel = new AnnotationsPanel(false);

        mainFormLayout.addEditionCanvas(identifiersEditionForm);
        mainFormLayout.addEditionCanvas(contentDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(annotationsEditionPanel);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == OrganisationPresenter.TYPE_SetContextAreaContentOrganisationToolBar) {
            if (content != null) {
                Canvas[] canvas = ((ToolStrip) content).getMembers();
                for (int i = 0; i < canvas.length; i++) {
                    if (canvas[i] instanceof ToolStripButton) {
                        if (ToolStripButtonEnum.ORGANISATIONS.getValue().equals(((ToolStripButton) canvas[i]).getID())) {
                            ((ToolStripButton) canvas[i]).select();
                        }
                    }
                }
                panel.addMember(content, 0);
            }
        } else {
            // To support inheritance in your views it is good practice to call super.setInSlot when you can't handle the call.
            // Who knows, maybe the parent class knows what to do with this slot.
            super.setInSlot(slot, content);
        }
    }

    @Override
    public void setOrganisation(OrganisationDto organisationDto) {
        this.organisationDto = organisationDto;

        String defaultLocalized = InternationalStringUtils.getLocalisedString(organisationDto.getName());
        String title = defaultLocalized != null ? defaultLocalized : StringUtils.EMPTY;
        mainFormLayout.setTitleLabelContents(title);

        mainFormLayout.setViewMode();

        setOrganisationViewMode(organisationDto);
        setOrganisationEditionMode(organisationDto);

        // Contacts
        setContacts(organisationDto.getContacts());
    }

    private void setOrganisationViewMode(OrganisationDto organisationDto) {
        // Identifiers Form
        identifiersForm.setValue(OrganisationDS.CODE, organisationDto.getCode());
        identifiersForm.setValue(OrganisationDS.NAME, RecordUtils.getInternationalStringRecord(organisationDto.getName()));
        identifiersForm.setValue(OrganisationDS.URI, organisationDto.getUri());
        identifiersForm.setValue(OrganisationDS.URN, organisationDto.getUrn());

        // Content descriptors
        contentDescriptorsForm.setValue(OrganisationDS.TYPE, CommonUtils.getOrganisationTypeName(organisationDto.getType()));
        contentDescriptorsForm.setValue(OrganisationDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(organisationDto.getDescription()));

        // Annotations
        annotationsPanel.setAnnotations(organisationDto.getAnnotations());
    }

    private void setOrganisationEditionMode(OrganisationDto organisationDto) {
        // Identifiers Form
        identifiersEditionForm.setValue(OrganisationDS.CODE, organisationDto.getCode());
        identifiersEditionForm.setValue(OrganisationDS.NAME, RecordUtils.getInternationalStringRecord(organisationDto.getName()));
        identifiersEditionForm.setValue(OrganisationDS.URI, organisationDto.getUri());
        identifiersEditionForm.setValue(OrganisationDS.URN, organisationDto.getUrn());

        // Content descriptors
        contentDescriptorsEditionForm.setValue(OrganisationDS.TYPE, CommonUtils.getOrganisationTypeName(organisationDto.getType()));
        contentDescriptorsEditionForm.setValue(OrganisationDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(organisationDto.getDescription()));

        // Annotations
        annotationsEditionPanel.setAnnotations(organisationDto.getAnnotations());
    }

    private void setContacts(List<ContactDto> contactDtos) {
        // Hide previous contact form
        contactMainFormLayout.hide();

        ContactRecord[] records = new ContactRecord[contactDtos.size()];
        for (int i = 0; i < records.length; i++) {
            records[i] = org.siemac.metamac.srm.web.organisation.utils.RecordUtils.getContactRecord(contactDtos.get(i));
        }
        contactListGrid.setData(records);
    }

    private OrganisationDto getOrganisationDto() {
        // Identifiers Form
        organisationDto.setCode(identifiersEditionForm.getValueAsString(OrganisationDS.CODE));
        organisationDto.setName((InternationalStringDto) identifiersEditionForm.getValue(OrganisationDS.NAME));

        // Content descriptors
        organisationDto.setDescription((InternationalStringDto) contentDescriptorsEditionForm.getValue(OrganisationDS.DESCRIPTION));

        // Annotations
        organisationDto.getAnnotations().clear();
        organisationDto.getAnnotations().addAll(annotationsEditionPanel.getAnnotations());

        return organisationDto;
    }

    private boolean validateEditionForms() {
        return identifiersEditionForm.validate(false) && contentDescriptorsEditionForm.validate(false);
    }

    private void showContactListGridDeleteButton() {
        contactDeleteButton.show();
    }

}
