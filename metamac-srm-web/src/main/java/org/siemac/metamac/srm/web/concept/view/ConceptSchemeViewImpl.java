package org.siemac.metamac.srm.web.concept.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.List;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.domain.concept.dto.ConceptDto;
import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;
import org.siemac.metamac.domain.srm.enume.domain.MaintainableArtefactProcStatusEnum;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.utils.ClientSecurityUtils;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeDS;
import org.siemac.metamac.srm.web.concept.presenter.ConceptSchemePresenter;
import org.siemac.metamac.srm.web.concept.utils.CommonUtils;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptSchemeUiHandlers;
import org.siemac.metamac.srm.web.concept.widgets.ConceptSchemeMainFormLayout;
import org.siemac.metamac.srm.web.concept.widgets.NewConceptWindow;
import org.siemac.metamac.web.common.client.utils.DateUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.CustomListGrid;
import org.siemac.metamac.web.common.client.widgets.InformationWindow;
import org.siemac.metamac.web.common.client.widgets.ListGridToolStrip;
import org.siemac.metamac.web.common.client.widgets.TitleLabel;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.BooleanItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class ConceptSchemeViewImpl extends ViewImpl implements ConceptSchemePresenter.ConceptSchemeView {

    private ConceptSchemeUiHandlers     uiHandlers;

    private VLayout                     panel;
    private ConceptSchemeMainFormLayout mainFormLayout;

    // View forms
    private GroupDynamicForm            identifiersForm;
    private GroupDynamicForm            contentDescriptorsForm;
    private GroupDynamicForm            classDescriptorsForm;
    private GroupDynamicForm            productionDescriptorsForm;
    private GroupDynamicForm            diffusionDescriptorsForm;

    // Edition forms
    private GroupDynamicForm            identifiersEditionForm;
    private GroupDynamicForm            contentDescriptorsEditionForm;
    private GroupDynamicForm            classDescriptorsEditionForm;
    private GroupDynamicForm            productionDescriptorsEditionForm;
    private GroupDynamicForm            diffusionDescriptorsEditionForm;

    private ListGridToolStrip           conceptsToolStripListGrid;
    private CustomListGrid              conceptsListGrid;

    private ConceptSchemeDto            conceptSchemeDto;

    @Inject
    public ConceptSchemeViewImpl() {
        super();
        panel = new VLayout();
        mainFormLayout = new ConceptSchemeMainFormLayout(ClientSecurityUtils.canEditConceptScheme());
        bindMainFormLayoutEvents();

        createViewForm();
        createEditionForm();

        conceptsToolStripListGrid = new ListGridToolStrip(getConstants().conceptDeleteTitle(), getConstants().conceptDeleteConfirmation());
        conceptsToolStripListGrid.getNewButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                // Clear new concept form
                final NewConceptWindow window = new NewConceptWindow(MetamacSrmWeb.getConstants().conceptCreate());
                window.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (window.validateForm()) {
                            uiHandlers.createConcept(window.getNewConceptDto());
                            window.destroy();
                        }
                    }
                });
            }
        });

        conceptsListGrid = new CustomListGrid();
        conceptsListGrid.setHeight(300);

        VLayout conceptsListGridLayout = new VLayout();
        conceptsListGridLayout.setMargin(15);
        conceptsListGridLayout.addMember(new TitleLabel(getConstants().concepts()));
        conceptsListGridLayout.addMember(conceptsToolStripListGrid);
        conceptsListGridLayout.addMember(conceptsListGrid);

        panel.addMember(mainFormLayout);
        panel.addMember(conceptsListGridLayout);
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

                classDescriptorsForm.setTranslationsShowed(translationsShowed);
                classDescriptorsEditionForm.setTranslationsShowed(translationsShowed);

                productionDescriptorsForm.setTranslationsShowed(translationsShowed);
                productionDescriptorsEditionForm.setTranslationsShowed(translationsShowed);

                diffusionDescriptorsForm.setTranslationsShowed(translationsShowed);
                diffusionDescriptorsEditionForm.setTranslationsShowed(translationsShowed);
            }
        });

        // Edit: Add a custom handler to check scheme status before start editing
        mainFormLayout.getEditToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                MaintainableArtefactProcStatusEnum status = conceptSchemeDto.getProcStatus();
                if (MaintainableArtefactProcStatusEnum.INTERNALLY_PUBLISHED.equals(status) || MaintainableArtefactProcStatusEnum.EXTERNALLY_PUBLISHED.equals(status)) {
                    // Create a new version
                    final InformationWindow window = new InformationWindow(getMessages().conceptSchemeEditionInfo(), getMessages().conceptSchemeEditionInfoDetailedMessage());
                    window.show();
                } else {
                    // Default behavior
                    setEditionMode();
                }
            }
        });

        // Save
        mainFormLayout.getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (identifiersEditionForm.validate(false) && contentDescriptorsEditionForm.validate(false) && classDescriptorsEditionForm.validate(false)
                        && productionDescriptorsEditionForm.validate(false) && diffusionDescriptorsEditionForm.validate(false)) {
                    uiHandlers.saveConceptScheme(getConceptSchemeDto());
                }
            }
        });

        // Life cycle
        mainFormLayout.getSendToPendingPublication().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.sendToPendingPublication(conceptSchemeDto.getId());
            }
        });
        mainFormLayout.getRejectValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.rejectValidation(conceptSchemeDto.getId());
            }
        });
        mainFormLayout.getPublishInternally().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.publishInternally(conceptSchemeDto.getId());
            }
        });
        mainFormLayout.getPublishExternally().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.publishExternally(conceptSchemeDto.getId());
            }
        });
        mainFormLayout.getVersioning().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.versioning(conceptSchemeDto.getId());
            }
        });
    }

    /*
     * GWTP will call setInSlot when a child presenter asks to be added under this view
     */
    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == ConceptSchemePresenter.TYPE_SetContextAreaContentConceptSchemeToolBar) {
            if (content != null) {
                Canvas[] canvas = ((ToolStrip) content).getMembers();
                for (int i = 0; i < canvas.length; i++) {
                    if (canvas[i] instanceof ToolStripButton) {
                        if (ToolStripButtonEnum.CONCEPTS.getValue().equals(((ToolStripButton) canvas[i]).getID())) {
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
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setUiHandlers(ConceptSchemeUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    @Override
    public void setConceptScheme(ConceptSchemeDto conceptScheme) {
        this.conceptSchemeDto = conceptScheme;

        String defaultLocalized = InternationalStringUtils.getLocalisedString(conceptScheme.getName());
        String title = defaultLocalized != null ? defaultLocalized : StringUtils.EMPTY;
        mainFormLayout.setTitleLabelContents(title);

        mainFormLayout.updatePublishSection(conceptScheme.getProcStatus());
        mainFormLayout.setViewMode();

        setConceptSchemeViewMode(conceptScheme);
        setConceptSchemeEditionMode(conceptScheme);
    }

    @Override
    public void setConceptList(List<ConceptDto> conceptDtos) {
        conceptsListGrid.removeAllData();
        if (conceptDtos != null) {
            for (ConceptDto conceptDto : conceptDtos) {
                // TODO conceptsListGrid.addData(org.siemac.metamac.srm.web.concept.utils.RecordUtils.getConceptSchemeRecord(conceptDto))
            }
        }
    }

    private void createViewForm() {
        // Identifiers Form
        identifiersForm = new GroupDynamicForm(getConstants().conceptSchemeIdentifiers());
        ViewTextItem code = new ViewTextItem(ConceptSchemeDS.CODE, getConstants().conceptSchemeCode());
        ViewMultiLanguageTextItem name = new ViewMultiLanguageTextItem(ConceptSchemeDS.NAME, getConstants().conceptSchemeName());
        ViewTextItem uri = new ViewTextItem(ConceptSchemeDS.URI, getConstants().conceptSchemeUri());
        ViewTextItem urn = new ViewTextItem(ConceptSchemeDS.URN, getConstants().conceptSchemeUrn());
        ViewTextItem version = new ViewTextItem(ConceptSchemeDS.VERSION, getConstants().conceptSchemeVersion());
        identifiersForm.setFields(code, name, uri, urn, version);

        // Content descriptors
        contentDescriptorsForm = new GroupDynamicForm(getConstants().conceptSchemeContentDescriptors());
        ViewMultiLanguageTextItem description = new ViewMultiLanguageTextItem(ConceptSchemeDS.DESCRIPTION, getConstants().conceptSchemeDescription());
        ViewTextItem partial = new ViewTextItem(ConceptSchemeDS.IS_PARTIAL, getConstants().conceptSchemeIsPartial());
        contentDescriptorsForm.setFields(description, partial);

        // Class descriptors
        classDescriptorsForm = new GroupDynamicForm(getConstants().conceptSchemeClassDescriptors());
        ViewTextItem agency = new ViewTextItem(ConceptSchemeDS.AGENCY, getConstants().conceptSchemeAgency());
        ViewTextItem type = new ViewTextItem(ConceptSchemeDS.TYPE, getConstants().conceptSchemeType());
        ViewTextItem operation = new ViewTextItem(ConceptSchemeDS.RELATED_OPERATION, getConstants().conceptSchemeOperation());
        classDescriptorsForm.setFields(type, operation, agency);

        // Production descriptors
        productionDescriptorsForm = new GroupDynamicForm(getConstants().conceptSchemeProductionDescriptors());
        ViewTextItem procStatus = new ViewTextItem(ConceptSchemeDS.PROC_STATUS, getConstants().conceptSchemeProcStatus());
        productionDescriptorsForm.setFields(procStatus);

        // Diffusion descriptors
        diffusionDescriptorsForm = new GroupDynamicForm(getConstants().conceptSchemeDiffusionDescriptors());
        ViewTextItem startDate = new ViewTextItem(ConceptSchemeDS.VALID_FROM, getConstants().conceptSchemeStartDate());
        ViewTextItem endDate = new ViewTextItem(ConceptSchemeDS.VALID_TO, getConstants().conceptSchemeEndDate());
        diffusionDescriptorsForm.setFields(startDate, endDate);

        mainFormLayout.addViewCanvas(identifiersForm);
        mainFormLayout.addViewCanvas(contentDescriptorsForm);
        mainFormLayout.addViewCanvas(classDescriptorsForm);
        mainFormLayout.addViewCanvas(productionDescriptorsForm);
        mainFormLayout.addViewCanvas(diffusionDescriptorsForm);
    }

    private void createEditionForm() {
        // Identifiers
        identifiersEditionForm = new GroupDynamicForm(getConstants().conceptSchemeIdentifiers());
        ViewTextItem code = new ViewTextItem(ConceptSchemeDS.CODE, getConstants().conceptSchemeCode());
        MultiLanguageTextItem name = new MultiLanguageTextItem(ConceptSchemeDS.NAME, getConstants().conceptSchemeName());
        name.setRequired(true);
        ViewTextItem uri = new ViewTextItem(ConceptSchemeDS.URI, getConstants().conceptSchemeUri());
        ViewTextItem urn = new ViewTextItem(ConceptSchemeDS.URN, getConstants().conceptSchemeUrn());
        ViewTextItem version = new ViewTextItem(ConceptSchemeDS.VERSION, getConstants().conceptSchemeVersion());
        identifiersEditionForm.setFields(code, name, uri, urn, version);

        // Content descriptors
        contentDescriptorsEditionForm = new GroupDynamicForm(getConstants().conceptSchemeContentDescriptors());
        MultiLanguageTextItem description = new MultiLanguageTextItem(ConceptSchemeDS.DESCRIPTION, getConstants().conceptSchemeDescription());
        BooleanItem partial = new BooleanItem(ConceptSchemeDS.IS_PARTIAL, getConstants().conceptSchemeIsPartial());
        contentDescriptorsEditionForm.setFields(description, partial);

        // Class descriptors
        classDescriptorsEditionForm = new GroupDynamicForm(getConstants().conceptSchemeClassDescriptors());
        SelectItem type = new SelectItem(ConceptSchemeDS.TYPE, getConstants().conceptSchemeType());
        SelectItem operation = new SelectItem(ConceptSchemeDS.RELATED_OPERATION, getConstants().conceptSchemeOperation());
        ViewTextItem agency = new ViewTextItem(ConceptSchemeDS.AGENCY, getConstants().conceptSchemeAgency());
        classDescriptorsEditionForm.setFields(type, operation, agency);

        // Production descriptors
        productionDescriptorsEditionForm = new GroupDynamicForm(getConstants().conceptSchemeProductionDescriptors());
        ViewTextItem procStatus = new ViewTextItem(ConceptSchemeDS.PROC_STATUS, getConstants().conceptSchemeProcStatus());
        productionDescriptorsEditionForm.setFields(procStatus);

        // Diffusion descriptors
        diffusionDescriptorsEditionForm = new GroupDynamicForm(getConstants().conceptSchemeDiffusionDescriptors());
        ViewTextItem startDate = new ViewTextItem(ConceptSchemeDS.VALID_FROM, getConstants().conceptSchemeStartDate());
        ViewTextItem endDate = new ViewTextItem(ConceptSchemeDS.VALID_TO, getConstants().conceptSchemeEndDate());
        diffusionDescriptorsEditionForm.setFields(startDate, endDate);

        mainFormLayout.addEditionCanvas(identifiersEditionForm);
        mainFormLayout.addEditionCanvas(contentDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(classDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(productionDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(diffusionDescriptorsEditionForm);
    }

    public void setEditionMode() {
        mainFormLayout.setEditionMode();
    }

    public void setConceptSchemeViewMode(ConceptSchemeDto conceptSchemeDto) {
        // Identifiers
        identifiersForm.setValue(ConceptSchemeDS.CODE, conceptSchemeDto.getCode());
        identifiersForm.setValue(ConceptSchemeDS.URI, conceptSchemeDto.getUri());
        identifiersForm.setValue(ConceptSchemeDS.URN, conceptSchemeDto.getUrn());
        identifiersForm.setValue(ConceptSchemeDS.VERSION, conceptSchemeDto.getVersionLogic());
        identifiersForm.setValue(ConceptSchemeDS.NAME, RecordUtils.getInternationalStringRecord(conceptSchemeDto.getName()));

        // Content descriptors
        contentDescriptorsForm.setValue(ConceptSchemeDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(conceptSchemeDto.getDescription()));
        contentDescriptorsForm.setValue(ConceptSchemeDS.IS_PARTIAL, (conceptSchemeDto.getIsPartial() != null && conceptSchemeDto.getIsPartial()) ? MetamacSrmWeb.getConstants().yes() : MetamacSrmWeb
                .getConstants().no());

        // Class descriptors
        classDescriptorsForm.setValue(ConceptSchemeDS.AGENCY, conceptSchemeDto.getMaintainer() != null ? conceptSchemeDto.getMaintainer().getCode() : StringUtils.EMPTY);

        // Production descriptors
        productionDescriptorsForm.setValue(ConceptSchemeDS.PROC_STATUS, CommonUtils.getConceptSchemeProcStatus(conceptSchemeDto));

        // Diffusion descriptors
        diffusionDescriptorsForm.setValue(ConceptSchemeDS.VALID_FROM, DateUtils.getFormattedDate(conceptSchemeDto.getValidFrom()));
        diffusionDescriptorsForm.setValue(ConceptSchemeDS.VALID_TO, DateUtils.getFormattedDate(conceptSchemeDto.getValidTo()));
    }

    public void setConceptSchemeEditionMode(ConceptSchemeDto conceptSchemeDto) {
        // Identifiers
        identifiersEditionForm.setValue(ConceptSchemeDS.CODE, conceptSchemeDto.getCode());
        identifiersEditionForm.setValue(ConceptSchemeDS.URI, conceptSchemeDto.getUri());
        identifiersEditionForm.setValue(ConceptSchemeDS.URN, conceptSchemeDto.getUrn());
        identifiersEditionForm.setValue(ConceptSchemeDS.VERSION, conceptSchemeDto.getVersionLogic());
        identifiersEditionForm.setValue(ConceptSchemeDS.NAME, RecordUtils.getInternationalStringRecord(conceptSchemeDto.getName()));

        // Content descriptors
        contentDescriptorsEditionForm.setValue(ConceptSchemeDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(conceptSchemeDto.getDescription()));
        contentDescriptorsEditionForm.setValue(ConceptSchemeDS.IS_PARTIAL, conceptSchemeDto.getIsPartial() != null ? conceptSchemeDto.getIsPartial() : false);

        // Class descriptors
        classDescriptorsEditionForm.setValue(ConceptSchemeDS.AGENCY, conceptSchemeDto.getMaintainer() != null ? conceptSchemeDto.getMaintainer().getCode() : StringUtils.EMPTY);

        // Production descriptors
        productionDescriptorsEditionForm.setValue(ConceptSchemeDS.PROC_STATUS, CommonUtils.getConceptSchemeProcStatus(conceptSchemeDto));

        // Diffusion descriptors
        diffusionDescriptorsEditionForm.setValue(ConceptSchemeDS.VALID_FROM, DateUtils.getFormattedDate(conceptSchemeDto.getValidFrom()));
        diffusionDescriptorsEditionForm.setValue(ConceptSchemeDS.VALID_TO, DateUtils.getFormattedDate(conceptSchemeDto.getValidTo()));
    }

    public ConceptSchemeDto getConceptSchemeDto() {
        // Identifiers
        conceptSchemeDto.setName((InternationalStringDto) identifiersEditionForm.getValue(ConceptSchemeDS.NAME));
        // Content descriptors
        conceptSchemeDto.setDescription((InternationalStringDto) contentDescriptorsEditionForm.getValue(ConceptSchemeDS.DESCRIPTION));
        conceptSchemeDto.setIsPartial((Boolean) contentDescriptorsEditionForm.getValue(ConceptSchemeDS.IS_PARTIAL));
        return conceptSchemeDto;
    }

}
