package org.siemac.metamac.srm.web.concept.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.core.common.util.shared.VersionUtil;
import org.siemac.metamac.srm.core.concept.dto.MetamacConceptDto;
import org.siemac.metamac.srm.core.concept.dto.MetamacConceptSchemeDto;
import org.siemac.metamac.srm.core.enume.domain.MaintainableArtefactProcStatusEnum;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeDS;
import org.siemac.metamac.srm.web.concept.model.record.ConceptRecord;
import org.siemac.metamac.srm.web.concept.model.record.ConceptSchemeRecord;
import org.siemac.metamac.srm.web.concept.presenter.ConceptSchemePresenter;
import org.siemac.metamac.srm.web.concept.utils.CommonUtils;
import org.siemac.metamac.srm.web.concept.utils.ConceptClientSecurityUtils;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptSchemeUiHandlers;
import org.siemac.metamac.srm.web.concept.widgets.ConceptSchemeMainFormLayout;
import org.siemac.metamac.srm.web.concept.widgets.HistorySectionStack;
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
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.BooleanItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
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
    private GroupDynamicForm            versionResponsibilityForm;

    // Edition forms
    private GroupDynamicForm            identifiersEditionForm;
    private GroupDynamicForm            contentDescriptorsEditionForm;
    private GroupDynamicForm            classDescriptorsEditionForm;
    private GroupDynamicForm            productionDescriptorsEditionForm;
    private GroupDynamicForm            diffusionDescriptorsEditionForm;
    private GroupDynamicForm            versionResponsibilityEditionForm;

    private ListGridToolStrip           conceptsToolStripListGrid;
    private CustomListGrid              conceptsListGrid;

    private HistorySectionStack         historySectionStack;

    private MetamacConceptSchemeDto     conceptSchemeDto;

    @Inject
    public ConceptSchemeViewImpl() {
        super();
        panel = new VLayout();
        panel.setHeight100();
        panel.setOverflow(Overflow.SCROLL);

        // Scheme

        mainFormLayout = new ConceptSchemeMainFormLayout(ConceptClientSecurityUtils.canUpdateConceptScheme());
        bindMainFormLayoutEvents();
        createViewForm();
        createEditionForm();

        // Scheme version list

        historySectionStack = new HistorySectionStack();
        historySectionStack.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                String urn = ((ConceptSchemeRecord) event.getRecord()).getUrn();
                uiHandlers.goToConceptScheme(urn);
            }
        });

        // Concept list

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
        conceptsToolStripListGrid.getNewButton().setVisibility(ConceptClientSecurityUtils.canCreateConcept() ? Visibility.VISIBLE : Visibility.HIDDEN);
        conceptsToolStripListGrid.getDeleteConfirmationWindow().getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.deleteConcepts(getSelectedConcepts());
            }
        });

        conceptsListGrid = new CustomListGrid();
        conceptsListGrid.setHeight(300);
        ListGridField codeField = new ListGridField(ConceptDS.CODE, getConstants().conceptCode());
        ListGridField nameField = new ListGridField(ConceptDS.NAME, getConstants().conceptName());
        conceptsListGrid.setFields(codeField, nameField);
        conceptsListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (conceptsListGrid.getSelectedRecords() != null && conceptsListGrid.getSelectedRecords().length == 1) {
                    ConceptRecord record = (ConceptRecord) conceptsListGrid.getSelectedRecord();
                    selectConcept(record.getId());
                } else {
                    // No record selected
                    hideConceptListGridDeleteButton();
                    if (conceptsListGrid.getSelectedRecords().length > 1) {
                        // Delete more than one concept with one click
                        showConceptListGridDeleteButton();
                    }
                }
            }
        });
        conceptsListGrid.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) {
                    ConceptRecord record = (ConceptRecord) event.getRecord();
                    uiHandlers.goToConcept(record.getCode());
                }
            }
        });

        VLayout conceptsListGridLayout = new VLayout();
        conceptsListGridLayout.setMargin(15);
        conceptsListGridLayout.addMember(new TitleLabel(getConstants().concepts()));
        conceptsListGridLayout.addMember(conceptsToolStripListGrid);
        conceptsListGridLayout.addMember(conceptsListGrid);

        panel.addMember(mainFormLayout);
        panel.addMember(historySectionStack);
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

                versionResponsibilityForm.setTranslationsShowed(translationsShowed);
                versionResponsibilityEditionForm.setTranslationsShowed(translationsShowed);
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
        mainFormLayout.getSendToProductionValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.sendToProductionValidation(conceptSchemeDto.getId());
            }
        });
        mainFormLayout.getSendToDiffusionValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.sendToDiffusionValidation(conceptSchemeDto.getId());
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
    public void setConceptScheme(MetamacConceptSchemeDto conceptScheme) {
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
    public void setConceptList(List<MetamacConceptDto> conceptDtos) {
        conceptsListGrid.removeAllData();
        if (conceptDtos != null) {
            for (MetamacConceptDto conceptDto : conceptDtos) {
                conceptsListGrid.addData(org.siemac.metamac.srm.web.concept.utils.RecordUtils.getConceptRecord(conceptDto));
            }
        }
    }

    @Override
    public void setConceptSchemeHistoryList(List<MetamacConceptSchemeDto> conceptSchemeDtos) {
        historySectionStack.setConceptSchemes(conceptSchemeDtos);
    }

    private void createViewForm() {
        // Identifiers Form
        identifiersForm = new GroupDynamicForm(getConstants().conceptSchemeIdentifiers());
        ViewTextItem code = new ViewTextItem(ConceptSchemeDS.CODE, getConstants().conceptSchemeCode());
        ViewMultiLanguageTextItem name = new ViewMultiLanguageTextItem(ConceptSchemeDS.NAME, getConstants().conceptSchemeName());
        ViewTextItem uri = new ViewTextItem(ConceptSchemeDS.URI, getConstants().conceptSchemeUri());
        ViewTextItem urn = new ViewTextItem(ConceptSchemeDS.URN, getConstants().conceptSchemeUrn());
        ViewTextItem version = new ViewTextItem(ConceptSchemeDS.VERSION_LOGIC, getConstants().conceptSchemeVersion());
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

        // Version responsibility
        versionResponsibilityForm = new GroupDynamicForm(getConstants().conceptSchemeVersionResponsibility());
        ViewTextItem productionEnvironment = new ViewTextItem("prod", getConstants().conceptSchemeVersionResponsibilityProduction()); // TODO
        ViewTextItem diffusionEnvironment = new ViewTextItem("diff", getConstants().conceptSchemeVersionResponsibilityDiffusion()); // TODO
        versionResponsibilityForm.setFields(productionEnvironment, diffusionEnvironment);

        mainFormLayout.addViewCanvas(identifiersForm);
        mainFormLayout.addViewCanvas(contentDescriptorsForm);
        mainFormLayout.addViewCanvas(classDescriptorsForm);
        mainFormLayout.addViewCanvas(productionDescriptorsForm);
        mainFormLayout.addViewCanvas(diffusionDescriptorsForm);
        mainFormLayout.addViewCanvas(versionResponsibilityForm);
    }

    private void createEditionForm() {
        // Identifiers
        identifiersEditionForm = new GroupDynamicForm(getConstants().conceptSchemeIdentifiers());
        RequiredTextItem code = new RequiredTextItem(ConceptSchemeDS.CODE, getConstants().conceptSchemeCode());
        code.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                // CODE cannot be modified if status is INTERNALLY_PUBLISHED, EXTERNALLY_PUBLISHED or EXTERNAL_PUBLICATION_FAILED or if version is greater then VERSION_INITIAL_VERSION (01.000)
                if ((MaintainableArtefactProcStatusEnum.INTERNALLY_PUBLISHED.equals(conceptSchemeDto.getProcStatus())
                        || MaintainableArtefactProcStatusEnum.EXTERNALLY_PUBLISHED.equals(conceptSchemeDto.getProcStatus()) || MaintainableArtefactProcStatusEnum.EXTERNAL_PUBLICATION_FAILED
                        .equals(conceptSchemeDto.getProcStatus()))
                        || (!VersionUtil.VERSION_INITIAL_VERSION.equals(conceptSchemeDto.getVersionLogic()) && !StringUtils.isBlank(conceptSchemeDto.getVersionLogic()))) {
                    return false;
                } else {
                    return true;
                }
            }
        });
        ViewTextItem staticCode = new ViewTextItem(ConceptSchemeDS.CODE, getConstants().conceptSchemeCode());
        staticCode.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !form.getItem(ConceptSchemeDS.CODE).isVisible();
            }
        });
        MultiLanguageTextItem name = new MultiLanguageTextItem(ConceptSchemeDS.NAME, getConstants().conceptSchemeName());
        name.setRequired(true);
        ViewTextItem uri = new ViewTextItem(ConceptSchemeDS.URI, getConstants().conceptSchemeUri());
        ViewTextItem urn = new ViewTextItem(ConceptSchemeDS.URN, getConstants().conceptSchemeUrn());
        ViewTextItem version = new ViewTextItem(ConceptSchemeDS.VERSION_LOGIC, getConstants().conceptSchemeVersion());
        identifiersEditionForm.setFields(code, staticCode, name, uri, urn, version);

        // Content descriptors
        contentDescriptorsEditionForm = new GroupDynamicForm(getConstants().conceptSchemeContentDescriptors());
        MultiLanguageTextItem description = new MultiLanguageTextItem(ConceptSchemeDS.DESCRIPTION, getConstants().conceptSchemeDescription());
        BooleanItem partial = new BooleanItem(ConceptSchemeDS.IS_PARTIAL, getConstants().conceptSchemeIsPartial());
        contentDescriptorsEditionForm.setFields(description, partial);

        // Class descriptors
        classDescriptorsEditionForm = new GroupDynamicForm(getConstants().conceptSchemeClassDescriptors());
        SelectItem type = new SelectItem(ConceptSchemeDS.TYPE, getConstants().conceptSchemeType());
        type.setValueMap(CommonUtils.getConceptSchemeTypeHashMap());
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

        // Version responsibility
        versionResponsibilityEditionForm = new GroupDynamicForm(getConstants().conceptSchemeVersionResponsibility());
        ViewTextItem productionEnvironment = new ViewTextItem("prod", getConstants().conceptSchemeVersionResponsibilityProduction()); // TODO
        ViewTextItem diffusionEnvironment = new ViewTextItem("diff", getConstants().conceptSchemeVersionResponsibilityDiffusion()); // TODO
        versionResponsibilityEditionForm.setFields(productionEnvironment, diffusionEnvironment);

        mainFormLayout.addEditionCanvas(identifiersEditionForm);
        mainFormLayout.addEditionCanvas(contentDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(classDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(productionDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(diffusionDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(versionResponsibilityEditionForm);
    }

    public void setEditionMode() {
        mainFormLayout.setEditionMode();
    }

    public void setConceptSchemeViewMode(MetamacConceptSchemeDto conceptSchemeDto) {
        // Identifiers
        identifiersForm.setValue(ConceptSchemeDS.CODE, conceptSchemeDto.getCode());
        identifiersForm.setValue(ConceptSchemeDS.URI, conceptSchemeDto.getUri());
        identifiersForm.setValue(ConceptSchemeDS.URN, conceptSchemeDto.getUrn());
        identifiersForm.setValue(ConceptSchemeDS.VERSION_LOGIC, conceptSchemeDto.getVersionLogic());
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

    public void setConceptSchemeEditionMode(MetamacConceptSchemeDto conceptSchemeDto) {
        // Identifiers
        identifiersEditionForm.setValue(ConceptSchemeDS.CODE, conceptSchemeDto.getCode());
        identifiersEditionForm.setValue(ConceptSchemeDS.URI, conceptSchemeDto.getUri());
        identifiersEditionForm.setValue(ConceptSchemeDS.URN, conceptSchemeDto.getUrn());
        identifiersEditionForm.setValue(ConceptSchemeDS.VERSION_LOGIC, conceptSchemeDto.getVersionLogic());
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

    public MetamacConceptSchemeDto getConceptSchemeDto() {
        // Identifiers
        conceptSchemeDto.setName((InternationalStringDto) identifiersEditionForm.getValue(ConceptSchemeDS.NAME));
        // Content descriptors
        conceptSchemeDto.setDescription((InternationalStringDto) contentDescriptorsEditionForm.getValue(ConceptSchemeDS.DESCRIPTION));
        conceptSchemeDto.setIsPartial((Boolean) contentDescriptorsEditionForm.getValue(ConceptSchemeDS.IS_PARTIAL));
        return conceptSchemeDto;
    }

    private void showConceptListGridDeleteButton() {
        if (ConceptClientSecurityUtils.canDeleteConcept()) {
            conceptsToolStripListGrid.getDeleteButton().show();
        }
    }

    private void hideConceptListGridDeleteButton() {
        conceptsToolStripListGrid.getDeleteButton().hide();
    }

    private void selectConcept(Long id) {
        if (id == null) {
            // New concept
            conceptsToolStripListGrid.getDeleteButton().hide();
            conceptsListGrid.deselectAllRecords();
        } else {
            showConceptListGridDeleteButton();
        }
    }

    public List<Long> getSelectedConcepts() {
        List<Long> selectedConcepts = new ArrayList<Long>();
        if (conceptsListGrid.getSelectedRecords() != null) {
            ListGridRecord[] records = conceptsListGrid.getSelectedRecords();
            for (int i = 0; i < records.length; i++) {
                ConceptRecord record = (ConceptRecord) records[i];
                selectedConcepts.add(record.getId());
            }
        }
        return selectedConcepts;
    }

}
