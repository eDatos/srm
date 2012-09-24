package org.siemac.metamac.srm.web.concept.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.BooleanUtils;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.core.common.util.shared.VersionUtil;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.widgets.AnnotationsPanel;
import org.siemac.metamac.srm.web.client.widgets.VersionWindow;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeDS;
import org.siemac.metamac.srm.web.concept.model.record.ConceptSchemeRecord;
import org.siemac.metamac.srm.web.concept.presenter.ConceptSchemePresenter;
import org.siemac.metamac.srm.web.concept.utils.CommonUtils;
import org.siemac.metamac.srm.web.concept.utils.ConceptsClientSecurityUtils;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptSchemeUiHandlers;
import org.siemac.metamac.srm.web.concept.widgets.ConceptSchemeMainFormLayout;
import org.siemac.metamac.srm.web.concept.widgets.ConceptSchemeVersionsSectionStack;
import org.siemac.metamac.srm.web.concept.widgets.ConceptsTreeGrid;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.utils.DateUtils;
import org.siemac.metamac.web.common.client.utils.ExternalItemUtils;
import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.InformationWindow;
import org.siemac.metamac.web.common.client.widgets.SearchExternalItemWindow;
import org.siemac.metamac.web.common.client.widgets.TitleLabel;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.BooleanSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextAreaItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.SearchViewTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.form.validator.CustomValidator;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class ConceptSchemeViewImpl extends ViewImpl implements ConceptSchemePresenter.ConceptSchemeView {

    private ConceptSchemeUiHandlers           uiHandlers;

    private VLayout                           panel;
    private ConceptSchemeMainFormLayout       mainFormLayout;

    // View forms
    private GroupDynamicForm                  identifiersForm;
    private GroupDynamicForm                  contentDescriptorsForm;
    private GroupDynamicForm                  classDescriptorsForm;
    private GroupDynamicForm                  productionDescriptorsForm;
    private GroupDynamicForm                  diffusionDescriptorsForm;
    private GroupDynamicForm                  versionResponsibilityForm;
    private AnnotationsPanel                  annotationsPanel;

    // Edition forms
    private GroupDynamicForm                  identifiersEditionForm;
    private GroupDynamicForm                  contentDescriptorsEditionForm;
    private GroupDynamicForm                  classDescriptorsEditionForm;
    private GroupDynamicForm                  productionDescriptorsEditionForm;
    private GroupDynamicForm                  diffusionDescriptorsEditionForm;
    private GroupDynamicForm                  versionResponsibilityEditionForm;
    private AnnotationsPanel                  annotationsEditionPanel;

    private ConceptsTreeGrid                  conceptsTreeGrid;

    private ConceptSchemeVersionsSectionStack versionsSectionStack;

    private ConceptSchemeMetamacDto           conceptSchemeDto;
    private ExternalItemDto                   relatedOperation;

    private SearchExternalItemWindow          searchOperationsWindow;

    @Inject
    public ConceptSchemeViewImpl() {
        super();
        panel = new VLayout();
        panel.setHeight100();
        panel.setOverflow(Overflow.SCROLL);

        //
        // CONCEPT SCHEME
        //

        mainFormLayout = new ConceptSchemeMainFormLayout(ConceptsClientSecurityUtils.canUpdateConceptScheme());
        bindMainFormLayoutEvents();
        createViewForm();
        createEditionForm();

        //
        // CONCEPT SCHEME VERSIONS
        //

        versionsSectionStack = new ConceptSchemeVersionsSectionStack(getConstants().conceptSchemeVersions());
        versionsSectionStack.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                String urn = ((ConceptSchemeRecord) event.getRecord()).getUrn();
                uiHandlers.goToConceptScheme(urn);
            }
        });

        //
        // CONCEPTS
        //

        conceptsTreeGrid = new ConceptsTreeGrid();

        VLayout conceptsListGridLayout = new VLayout();
        conceptsListGridLayout.setMargin(15);
        conceptsListGridLayout.addMember(new TitleLabel(getConstants().concepts()));
        conceptsListGridLayout.addMember(conceptsTreeGrid);

        panel.addMember(mainFormLayout);
        panel.addMember(versionsSectionStack);
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

                annotationsPanel.setTranslationsShowed(translationsShowed);
                annotationsEditionPanel.setTranslationsShowed(translationsShowed);
            }
        });

        // Edit: Add a custom handler to check scheme status before start editing
        mainFormLayout.getEditToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ProcStatusEnum status = conceptSchemeDto.getProcStatus();
                if (ProcStatusEnum.INTERNALLY_PUBLISHED.equals(status) || ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(status)) {
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
                uiHandlers.sendToProductionValidation(conceptSchemeDto.getUrn(), conceptSchemeDto.getProcStatus());
            }
        });
        mainFormLayout.getSendToDiffusionValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.sendToDiffusionValidation(conceptSchemeDto.getUrn(), conceptSchemeDto.getProcStatus());
            }
        });
        mainFormLayout.getRejectValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.rejectValidation(conceptSchemeDto.getUrn(), conceptSchemeDto.getProcStatus());
            }
        });
        mainFormLayout.getPublishInternally().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.publishInternally(conceptSchemeDto.getUrn(), conceptSchemeDto.getProcStatus());
            }
        });
        mainFormLayout.getPublishExternally().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.publishExternally(conceptSchemeDto.getUrn(), conceptSchemeDto.getProcStatus());
            }
        });
        mainFormLayout.getVersioning().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                final VersionWindow versionWindow = new VersionWindow(getConstants().lifeCycleVersioning());
                versionWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (versionWindow.validateForm()) {
                            uiHandlers.versioning(conceptSchemeDto.getUrn(), versionWindow.getSelectedVersion());
                            versionWindow.destroy();
                        }
                    }
                });
            }
        });
        mainFormLayout.getCancelValidity().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.cancelValidity(conceptSchemeDto.getUrn());
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
        this.conceptsTreeGrid.setUiHandlers(uiHandlers);
    }

    @Override
    public void setConceptScheme(ConceptSchemeMetamacDto conceptScheme) {
        this.conceptSchemeDto = conceptScheme;
        this.relatedOperation = conceptScheme.getRelatedOperation();

        String defaultLocalized = InternationalStringUtils.getLocalisedString(conceptScheme.getName());
        String title = defaultLocalized != null ? defaultLocalized : StringUtils.EMPTY;
        mainFormLayout.setTitleLabelContents(title);

        mainFormLayout.updatePublishSection(conceptScheme);
        mainFormLayout.setViewMode();

        setConceptSchemeViewMode(conceptScheme);
        setConceptSchemeEditionMode(conceptScheme);
    }

    @Override
    public void setConceptList(List<ItemHierarchyDto> itemHierarchyDtos) {
        conceptsTreeGrid.setConcepts(conceptSchemeDto, itemHierarchyDtos);
    }

    @Override
    public void setConceptSchemeVersions(List<ConceptSchemeMetamacDto> conceptSchemeDtos) {
        versionsSectionStack.setConceptSchemes(conceptSchemeDtos);
        versionsSectionStack.selectConceptScheme(conceptSchemeDto);
    }

    private void createViewForm() {
        // Identifiers Form
        identifiersForm = new GroupDynamicForm(getConstants().conceptSchemeIdentifiers());
        ViewTextItem code = new ViewTextItem(ConceptSchemeDS.CODE, getConstants().conceptSchemeCode());
        ViewMultiLanguageTextItem name = new ViewMultiLanguageTextItem(ConceptSchemeDS.NAME, getConstants().conceptSchemeName());
        ViewTextItem uri = new ViewTextItem(ConceptSchemeDS.URI, getConstants().conceptSchemeUri());
        ViewTextItem urn = new ViewTextItem(ConceptSchemeDS.URN, getConstants().conceptSchemeUrn());
        ViewTextItem version = new ViewTextItem(ConceptSchemeDS.VERSION_LOGIC, getConstants().maintainableArtefactVersionLogic());
        identifiersForm.setFields(code, name, uri, urn, version);

        // Content descriptors
        contentDescriptorsForm = new GroupDynamicForm(getConstants().conceptSchemeContentDescriptors());
        ViewMultiLanguageTextItem description = new ViewMultiLanguageTextItem(ConceptSchemeDS.DESCRIPTION, getConstants().conceptSchemeDescription());
        ViewTextItem partial = new ViewTextItem(ConceptSchemeDS.IS_PARTIAL, getConstants().conceptSchemeIsPartial());
        ViewTextItem isExternalReference = new ViewTextItem(ConceptSchemeDS.IS_EXTERNAL_REFERENCE, getConstants().maintainableArtefactIsExternalReference());
        ViewTextItem isFinal = new ViewTextItem(ConceptSchemeDS.FINAL, getConstants().maintainableArtefactFinalLogic());
        contentDescriptorsForm.setFields(description, partial, isExternalReference, isFinal);

        // Class descriptors
        classDescriptorsForm = new GroupDynamicForm(getConstants().conceptSchemeClassDescriptors());
        ViewTextItem agency = new ViewTextItem(ConceptSchemeDS.MAINTAINER, getConstants().maintainableArtefactMaintainer());
        ViewTextItem type = new ViewTextItem(ConceptSchemeDS.TYPE, getConstants().conceptSchemeType());
        ViewTextItem typeView = new ViewTextItem(ConceptSchemeDS.TYPE_VIEW, getConstants().conceptSchemeType());
        typeView.setShowIfCondition(FormItemUtils.getFalseFormItemIfFunction());
        ViewTextItem operation = new ViewTextItem(ConceptSchemeDS.RELATED_OPERATION, getConstants().conceptSchemeOperation());
        operation.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return ConceptSchemeTypeEnum.OPERATION.name().equals(form.getValueAsString(ConceptSchemeDS.TYPE_VIEW));
            }
        });
        classDescriptorsForm.setFields(type, typeView, operation, agency);

        // Production descriptors
        productionDescriptorsForm = new GroupDynamicForm(getConstants().conceptSchemeProductionDescriptors());
        ViewTextItem procStatus = new ViewTextItem(ConceptSchemeDS.PROC_STATUS, getConstants().lifeCycleProcStatus());
        productionDescriptorsForm.setFields(procStatus);

        // Diffusion descriptors
        diffusionDescriptorsForm = new GroupDynamicForm(getConstants().conceptSchemeDiffusionDescriptors());
        ViewTextItem replacedBy = new ViewTextItem(ConceptSchemeDS.REPLACED_BY, getConstants().maintainableArtefactReplacedBy());
        ViewTextItem replaceTo = new ViewTextItem(ConceptSchemeDS.REPLACE_TO, getConstants().maintainableArtefactReplaceTo());
        ViewTextItem validFrom = new ViewTextItem(ConceptSchemeDS.VALID_FROM, getConstants().maintainableArtefactValidFrom());
        ViewTextItem validTo = new ViewTextItem(ConceptSchemeDS.VALID_TO, getConstants().maintainableArtefactValidTo());
        ViewTextItem externalPublicationFailed = new ViewTextItem(ConceptSchemeDS.IS_EXTERNAL_PUBLICATION_FAILED, getConstants().lifeCycleExternalPublicationFailed());
        ViewTextItem externalPublicationFailedDate = new ViewTextItem(ConceptSchemeDS.EXTERNAL_PUBLICATION_FAILED_DATE, getConstants().lifeCycleExternalPublicationFailedDate());
        diffusionDescriptorsForm.setFields(replacedBy, replaceTo, validFrom, validTo, externalPublicationFailed, externalPublicationFailedDate);

        // Version responsibility
        versionResponsibilityForm = new GroupDynamicForm(getConstants().lifeCycleVersionResponsibility());
        ViewTextItem productionValidationUser = new ViewTextItem(ConceptSchemeDS.PRODUCTION_VALIDATION_USER, getConstants().lifeCycleProductionValidationUser());
        ViewTextItem productionValidationDate = new ViewTextItem(ConceptSchemeDS.PRODUCTION_VALIDATION_DATE, getConstants().lifeCycleProductionValidationDate());
        ViewTextItem diffusionValidationUser = new ViewTextItem(ConceptSchemeDS.DIFFUSION_VALIDATION_USER, getConstants().lifeCycleDiffusionValidationUser());
        ViewTextItem diffusionValidationDate = new ViewTextItem(ConceptSchemeDS.DIFFUSION_VALIDATION_DATE, getConstants().lifeCycleDiffusionValidationDate());
        ViewTextItem internalPublicationUser = new ViewTextItem(ConceptSchemeDS.INTERNAL_PUBLICATION_USER, getConstants().lifeCycleInternalPublicationUser());
        ViewTextItem internalPublicationDate = new ViewTextItem(ConceptSchemeDS.INTERNAL_PUBLICATION_DATE, getConstants().lifeCycleInternalPublicationDate());
        ViewTextItem externalPublicationUser = new ViewTextItem(ConceptSchemeDS.EXTERNAL_PUBLICATION_USER, getConstants().lifeCycleExternalPublicationUser());
        ViewTextItem externalPublicationDate = new ViewTextItem(ConceptSchemeDS.EXTERNAL_PUBLICATION_DATE, getConstants().lifeCycleExternalPublicationDate());
        versionResponsibilityForm.setFields(productionValidationUser, productionValidationDate, diffusionValidationUser, diffusionValidationDate, internalPublicationUser, internalPublicationDate,
                externalPublicationUser, externalPublicationDate);

        // Annotations
        annotationsPanel = new AnnotationsPanel(true);

        mainFormLayout.addViewCanvas(identifiersForm);
        mainFormLayout.addViewCanvas(contentDescriptorsForm);
        mainFormLayout.addViewCanvas(classDescriptorsForm);
        mainFormLayout.addViewCanvas(productionDescriptorsForm);
        mainFormLayout.addViewCanvas(diffusionDescriptorsForm);
        mainFormLayout.addViewCanvas(versionResponsibilityForm);
        mainFormLayout.addViewCanvas(annotationsPanel);
    }

    private void createEditionForm() {
        // Identifiers
        identifiersEditionForm = new GroupDynamicForm(getConstants().conceptSchemeIdentifiers());
        RequiredTextItem code = new RequiredTextItem(ConceptSchemeDS.CODE, getConstants().conceptSchemeCode());
        code.setValidators(CommonWebUtils.getSemanticIdentifierCustomValidator());
        code.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                // CODE cannot be modified if status is INTERNALLY_PUBLISHED or EXTERNALLY_PUBLISHED, or if version is greater than VERSION_INITIAL_VERSION (01.000)
                return !((ProcStatusEnum.INTERNALLY_PUBLISHED.equals(conceptSchemeDto.getProcStatus()) || ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(conceptSchemeDto.getProcStatus())) || (!VersionUtil.VERSION_INITIAL_VERSION
                        .equals(conceptSchemeDto.getVersionLogic()) && !StringUtils.isBlank(conceptSchemeDto.getVersionLogic())));
            }
        });
        ViewTextItem staticCode = new ViewTextItem(ConceptSchemeDS.CODE_VIEW, getConstants().conceptSchemeCode());
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
        ViewTextItem version = new ViewTextItem(ConceptSchemeDS.VERSION_LOGIC, getConstants().maintainableArtefactVersionLogic());
        identifiersEditionForm.setFields(code, staticCode, name, uri, urn, version);

        // Content descriptors
        contentDescriptorsEditionForm = new GroupDynamicForm(getConstants().conceptSchemeContentDescriptors());
        MultiLanguageTextAreaItem description = new MultiLanguageTextAreaItem(ConceptSchemeDS.DESCRIPTION, getConstants().conceptSchemeDescription());
        BooleanSelectItem partial = new BooleanSelectItem(ConceptSchemeDS.IS_PARTIAL, getConstants().conceptSchemeIsPartial());
        ViewTextItem isExternalReference = new ViewTextItem(ConceptSchemeDS.IS_EXTERNAL_REFERENCE, getConstants().maintainableArtefactIsExternalReference());
        ViewTextItem isFinal = new ViewTextItem(ConceptSchemeDS.FINAL, getConstants().maintainableArtefactFinalLogic());
        contentDescriptorsEditionForm.setFields(description, partial, isExternalReference, isFinal);

        // Class descriptors
        classDescriptorsEditionForm = new GroupDynamicForm(getConstants().conceptSchemeClassDescriptors());
        final RequiredSelectItem type = new RequiredSelectItem(ConceptSchemeDS.TYPE, getConstants().conceptSchemeType());
        type.setValueMap(CommonUtils.getConceptSchemeTypeHashMap());
        type.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                classDescriptorsEditionForm.markForRedraw();
                if (!ConceptSchemeTypeEnum.OPERATION.name().equals(type.getValueAsString())) {
                    classDescriptorsEditionForm.setValue(ConceptSchemeDS.RELATED_OPERATION, StringUtils.EMPTY);
                    relatedOperation = null;
                }
            }
        });
        type.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                // TYPE cannot be modified if status is INTERNALLY_PUBLISHED or EXTERNALLY_PUBLISHED, or if version is greater than VERSION_INITIAL_VERSION (01.000)
                return !((ProcStatusEnum.INTERNALLY_PUBLISHED.equals(conceptSchemeDto.getProcStatus()) || ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(conceptSchemeDto.getProcStatus())) || (!VersionUtil.VERSION_INITIAL_VERSION
                        .equals(conceptSchemeDto.getVersionLogic()) && !StringUtils.isBlank(conceptSchemeDto.getVersionLogic())));
            }
        });
        ViewTextItem typeView = new ViewTextItem(ConceptSchemeDS.TYPE_VIEW, getConstants().conceptSchemeType());
        typeView.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !form.getItem(ConceptSchemeDS.TYPE).isVisible();
            }
        });
        final SearchViewTextItem operation = createRelatedOperationItem(ConceptSchemeDS.RELATED_OPERATION, getConstants().conceptSchemeOperation());
        operation.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return ConceptSchemeTypeEnum.OPERATION.name().equals(form.getValueAsString(ConceptSchemeDS.TYPE));
            }
        });
        CustomValidator customValidator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                if (operation.getValue() != null) {
                    String operationValue = String.valueOf(operation.getValue());
                    return !StringUtils.isEmpty(operationValue);
                }
                return true;
            }
        };
        operation.setValidators(customValidator);
        ViewTextItem agency = new ViewTextItem(ConceptSchemeDS.MAINTAINER, getConstants().maintainableArtefactMaintainer());
        classDescriptorsEditionForm.setFields(type, typeView, operation, agency);

        // Production descriptors
        productionDescriptorsEditionForm = new GroupDynamicForm(getConstants().conceptSchemeProductionDescriptors());
        ViewTextItem procStatus = new ViewTextItem(ConceptSchemeDS.PROC_STATUS, getConstants().lifeCycleProcStatus());
        productionDescriptorsEditionForm.setFields(procStatus);

        // Diffusion descriptors
        diffusionDescriptorsEditionForm = new GroupDynamicForm(getConstants().conceptSchemeDiffusionDescriptors());
        ViewTextItem replacedBy = new ViewTextItem(ConceptSchemeDS.REPLACED_BY, getConstants().maintainableArtefactReplacedBy());
        ViewTextItem replaceTo = new ViewTextItem(ConceptSchemeDS.REPLACE_TO, getConstants().maintainableArtefactReplaceTo());
        ViewTextItem validFrom = new ViewTextItem(ConceptSchemeDS.VALID_FROM, getConstants().maintainableArtefactValidFrom());
        ViewTextItem validTo = new ViewTextItem(ConceptSchemeDS.VALID_TO, getConstants().maintainableArtefactValidTo());
        ViewTextItem externalPublicationFailed = new ViewTextItem(ConceptSchemeDS.IS_EXTERNAL_PUBLICATION_FAILED, getConstants().lifeCycleExternalPublicationFailed());
        ViewTextItem externalPublicationFailedDate = new ViewTextItem(ConceptSchemeDS.EXTERNAL_PUBLICATION_FAILED_DATE, getConstants().lifeCycleExternalPublicationFailedDate());
        diffusionDescriptorsEditionForm.setFields(replacedBy, replaceTo, validFrom, validTo, externalPublicationFailed, externalPublicationFailedDate);

        // Version responsibility
        versionResponsibilityEditionForm = new GroupDynamicForm(getConstants().lifeCycleVersionResponsibility());
        ViewTextItem productionValidationUser = new ViewTextItem(ConceptSchemeDS.PRODUCTION_VALIDATION_USER, getConstants().lifeCycleProductionValidationUser());
        ViewTextItem productionValidationDate = new ViewTextItem(ConceptSchemeDS.PRODUCTION_VALIDATION_DATE, getConstants().lifeCycleProductionValidationDate());
        ViewTextItem diffusionValidationUser = new ViewTextItem(ConceptSchemeDS.DIFFUSION_VALIDATION_USER, getConstants().lifeCycleDiffusionValidationUser());
        ViewTextItem diffusionValidationDate = new ViewTextItem(ConceptSchemeDS.DIFFUSION_VALIDATION_DATE, getConstants().lifeCycleDiffusionValidationDate());
        ViewTextItem internalPublicationUser = new ViewTextItem(ConceptSchemeDS.INTERNAL_PUBLICATION_USER, getConstants().lifeCycleInternalPublicationUser());
        ViewTextItem internalPublicationDate = new ViewTextItem(ConceptSchemeDS.INTERNAL_PUBLICATION_DATE, getConstants().lifeCycleInternalPublicationDate());
        ViewTextItem externalPublicationUser = new ViewTextItem(ConceptSchemeDS.EXTERNAL_PUBLICATION_USER, getConstants().lifeCycleExternalPublicationUser());
        ViewTextItem externalPublicationDate = new ViewTextItem(ConceptSchemeDS.EXTERNAL_PUBLICATION_DATE, getConstants().lifeCycleExternalPublicationDate());
        versionResponsibilityEditionForm.setFields(productionValidationUser, productionValidationDate, diffusionValidationUser, diffusionValidationDate, internalPublicationUser,
                internalPublicationDate, externalPublicationUser, externalPublicationDate);

        // Annotations
        annotationsEditionPanel = new AnnotationsPanel(false);

        mainFormLayout.addEditionCanvas(identifiersEditionForm);
        mainFormLayout.addEditionCanvas(contentDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(classDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(productionDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(diffusionDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(versionResponsibilityEditionForm);
        mainFormLayout.addEditionCanvas(annotationsEditionPanel);
    }

    public void setEditionMode() {
        mainFormLayout.setEditionMode();
    }

    public void setConceptSchemeViewMode(ConceptSchemeMetamacDto conceptSchemeDto) {
        // Identifiers
        identifiersForm.setValue(ConceptSchemeDS.CODE, conceptSchemeDto.getCode());
        identifiersForm.setValue(ConceptSchemeDS.URI, conceptSchemeDto.getUri());
        identifiersForm.setValue(ConceptSchemeDS.URN, conceptSchemeDto.getUrn());
        identifiersForm.setValue(ConceptSchemeDS.VERSION_LOGIC, conceptSchemeDto.getVersionLogic());
        identifiersForm.setValue(ConceptSchemeDS.NAME, RecordUtils.getInternationalStringRecord(conceptSchemeDto.getName()));

        // Content descriptors
        contentDescriptorsForm.setValue(ConceptSchemeDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(conceptSchemeDto.getDescription()));
        contentDescriptorsForm.setValue(ConceptSchemeDS.IS_PARTIAL, conceptSchemeDto.getIsPartial() != null ? (conceptSchemeDto.getIsPartial()
                ? MetamacWebCommon.getConstants().yes()
                : MetamacWebCommon.getConstants().no()) : StringUtils.EMPTY);
        contentDescriptorsForm.setValue(ConceptSchemeDS.IS_EXTERNAL_REFERENCE, conceptSchemeDto.getIsExternalReference() != null ? (conceptSchemeDto.getIsExternalReference() ? MetamacWebCommon
                .getConstants().yes() : MetamacWebCommon.getConstants().no()) : StringUtils.EMPTY);
        contentDescriptorsForm.setValue(ConceptSchemeDS.FINAL, conceptSchemeDto.getFinalLogic() != null ? (conceptSchemeDto.getFinalLogic() ? MetamacWebCommon.getConstants().yes() : MetamacWebCommon
                .getConstants().no()) : StringUtils.EMPTY);

        // Class descriptors
        classDescriptorsForm.setValue(ConceptSchemeDS.TYPE_VIEW, conceptSchemeDto.getType() != null ? conceptSchemeDto.getType().name() : null);
        classDescriptorsForm.setValue(ConceptSchemeDS.TYPE, MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().conceptSchemeTypeEnum() + conceptSchemeDto.getType().name()));
        if (ConceptSchemeTypeEnum.OPERATION.equals(conceptSchemeDto.getType())) {
            classDescriptorsForm.getItem(ConceptSchemeDS.RELATED_OPERATION).show();
        } else {
            classDescriptorsForm.getItem(ConceptSchemeDS.RELATED_OPERATION).hide();
        }
        classDescriptorsForm.setValue(ConceptSchemeDS.RELATED_OPERATION, ExternalItemUtils.getExternalItemName(conceptSchemeDto.getRelatedOperation()));
        classDescriptorsForm.setValue(ConceptSchemeDS.MAINTAINER, conceptSchemeDto.getMaintainer() != null ? conceptSchemeDto.getMaintainer().getCode() : StringUtils.EMPTY);

        // Production descriptors
        productionDescriptorsForm.setValue(ConceptSchemeDS.PROC_STATUS, org.siemac.metamac.srm.web.client.utils.CommonUtils.getProcStatusName(conceptSchemeDto.getProcStatus()));

        // Diffusion descriptors
        diffusionDescriptorsForm.setValue(ConceptSchemeDS.REPLACED_BY, conceptSchemeDto.getReplacedBy());
        diffusionDescriptorsForm.setValue(ConceptSchemeDS.REPLACE_TO, conceptSchemeDto.getReplaceTo());
        diffusionDescriptorsForm.setValue(ConceptSchemeDS.VALID_FROM, conceptSchemeDto.getValidFrom());
        diffusionDescriptorsForm.setValue(ConceptSchemeDS.VALID_TO, conceptSchemeDto.getValidTo());
        diffusionDescriptorsForm.setValue(ConceptSchemeDS.IS_EXTERNAL_PUBLICATION_FAILED, BooleanUtils.isTrue(conceptSchemeDto.getIsExternalPublicationFailed()) ? MetamacWebCommon.getConstants()
                .yes() : StringUtils.EMPTY);
        diffusionDescriptorsForm.setValue(ConceptSchemeDS.EXTERNAL_PUBLICATION_FAILED_DATE, DateUtils.getFormattedDate(conceptSchemeDto.getExternalPublicationFailedDate()));

        // Version responsibility
        versionResponsibilityForm.setValue(ConceptSchemeDS.PRODUCTION_VALIDATION_USER, conceptSchemeDto.getProductionValidationUser());
        versionResponsibilityForm.setValue(ConceptSchemeDS.PRODUCTION_VALIDATION_DATE, conceptSchemeDto.getProductionValidationDate());
        versionResponsibilityForm.setValue(ConceptSchemeDS.DIFFUSION_VALIDATION_USER, conceptSchemeDto.getDiffusionValidationUser());
        versionResponsibilityForm.setValue(ConceptSchemeDS.DIFFUSION_VALIDATION_DATE, conceptSchemeDto.getDiffusionValidationDate());
        versionResponsibilityForm.setValue(ConceptSchemeDS.INTERNAL_PUBLICATION_USER, conceptSchemeDto.getInternalPublicationUser());
        versionResponsibilityForm.setValue(ConceptSchemeDS.INTERNAL_PUBLICATION_DATE, conceptSchemeDto.getInternalPublicationDate());
        versionResponsibilityForm.setValue(ConceptSchemeDS.EXTERNAL_PUBLICATION_USER, conceptSchemeDto.getExternalPublicationUser());
        versionResponsibilityForm.setValue(ConceptSchemeDS.EXTERNAL_PUBLICATION_DATE, conceptSchemeDto.getExternalPublicationDate());

        // Annotations
        annotationsPanel.setAnnotations(conceptSchemeDto.getAnnotations());
    }

    public void setConceptSchemeEditionMode(ConceptSchemeMetamacDto conceptSchemeDto) {
        // Identifiers
        identifiersEditionForm.setValue(ConceptSchemeDS.CODE, conceptSchemeDto.getCode());
        identifiersEditionForm.setValue(ConceptSchemeDS.CODE_VIEW, conceptSchemeDto.getCode());
        identifiersEditionForm.setValue(ConceptSchemeDS.URI, conceptSchemeDto.getUri());
        identifiersEditionForm.setValue(ConceptSchemeDS.URN, conceptSchemeDto.getUrn());
        identifiersEditionForm.setValue(ConceptSchemeDS.VERSION_LOGIC, conceptSchemeDto.getVersionLogic());
        identifiersEditionForm.setValue(ConceptSchemeDS.NAME, RecordUtils.getInternationalStringRecord(conceptSchemeDto.getName()));

        // Content descriptors
        contentDescriptorsEditionForm.setValue(ConceptSchemeDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(conceptSchemeDto.getDescription()));
        contentDescriptorsEditionForm.setValue(ConceptSchemeDS.IS_PARTIAL, conceptSchemeDto.getIsPartial() != null ? conceptSchemeDto.getIsPartial() : false);
        contentDescriptorsEditionForm.setValue(ConceptSchemeDS.IS_EXTERNAL_REFERENCE, conceptSchemeDto.getIsExternalReference() != null ? (conceptSchemeDto.getIsExternalReference() ? MetamacWebCommon
                .getConstants().yes() : MetamacWebCommon.getConstants().no()) : StringUtils.EMPTY);
        contentDescriptorsEditionForm.setValue(ConceptSchemeDS.FINAL, conceptSchemeDto.getFinalLogic() != null ? (conceptSchemeDto.getFinalLogic()
                ? MetamacWebCommon.getConstants().yes()
                : MetamacWebCommon.getConstants().no()) : StringUtils.EMPTY);

        // Class descriptors
        classDescriptorsEditionForm.setValue(ConceptSchemeDS.TYPE, conceptSchemeDto.getType().name());
        classDescriptorsEditionForm.setValue(ConceptSchemeDS.TYPE_VIEW,
                MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().conceptSchemeTypeEnum() + conceptSchemeDto.getType().name()));
        classDescriptorsEditionForm.setValue(ConceptSchemeDS.RELATED_OPERATION, ExternalItemUtils.getExternalItemName(conceptSchemeDto.getRelatedOperation()));
        classDescriptorsEditionForm.setValue(ConceptSchemeDS.MAINTAINER, conceptSchemeDto.getMaintainer() != null ? conceptSchemeDto.getMaintainer().getCode() : StringUtils.EMPTY);

        // Production descriptors
        productionDescriptorsEditionForm.setValue(ConceptSchemeDS.PROC_STATUS, org.siemac.metamac.srm.web.client.utils.CommonUtils.getProcStatusName(conceptSchemeDto.getProcStatus()));

        // Diffusion descriptors
        diffusionDescriptorsEditionForm.setValue(ConceptSchemeDS.REPLACED_BY, conceptSchemeDto.getReplacedBy());
        diffusionDescriptorsEditionForm.setValue(ConceptSchemeDS.REPLACE_TO, conceptSchemeDto.getReplaceTo());
        diffusionDescriptorsEditionForm.setValue(ConceptSchemeDS.VALID_FROM, DateUtils.getFormattedDate(conceptSchemeDto.getValidFrom()));
        diffusionDescriptorsEditionForm.setValue(ConceptSchemeDS.VALID_TO, DateUtils.getFormattedDate(conceptSchemeDto.getValidTo()));
        diffusionDescriptorsEditionForm.setValue(ConceptSchemeDS.IS_EXTERNAL_PUBLICATION_FAILED, BooleanUtils.isTrue(conceptSchemeDto.getIsExternalPublicationFailed()) ? MetamacWebCommon
                .getConstants().yes() : MetamacWebCommon.getConstants().no());
        diffusionDescriptorsEditionForm.setValue(ConceptSchemeDS.EXTERNAL_PUBLICATION_FAILED_DATE, DateUtils.getFormattedDate(conceptSchemeDto.getExternalPublicationFailedDate()));

        // Version responsibility
        versionResponsibilityEditionForm.setValue(ConceptSchemeDS.PRODUCTION_VALIDATION_USER, conceptSchemeDto.getProductionValidationUser());
        versionResponsibilityEditionForm.setValue(ConceptSchemeDS.PRODUCTION_VALIDATION_DATE, conceptSchemeDto.getProductionValidationDate());
        versionResponsibilityEditionForm.setValue(ConceptSchemeDS.DIFFUSION_VALIDATION_USER, conceptSchemeDto.getDiffusionValidationUser());
        versionResponsibilityEditionForm.setValue(ConceptSchemeDS.DIFFUSION_VALIDATION_DATE, conceptSchemeDto.getDiffusionValidationDate());
        versionResponsibilityEditionForm.setValue(ConceptSchemeDS.INTERNAL_PUBLICATION_USER, conceptSchemeDto.getInternalPublicationUser());
        versionResponsibilityEditionForm.setValue(ConceptSchemeDS.INTERNAL_PUBLICATION_DATE, conceptSchemeDto.getInternalPublicationDate());
        versionResponsibilityEditionForm.setValue(ConceptSchemeDS.EXTERNAL_PUBLICATION_USER, conceptSchemeDto.getExternalPublicationUser());
        versionResponsibilityEditionForm.setValue(ConceptSchemeDS.EXTERNAL_PUBLICATION_DATE, conceptSchemeDto.getExternalPublicationDate());

        // Annotations
        annotationsEditionPanel.setAnnotations(conceptSchemeDto.getAnnotations());
    }

    public ConceptSchemeMetamacDto getConceptSchemeDto() {
        // Identifiers
        conceptSchemeDto.setCode(identifiersEditionForm.getValueAsString(ConceptSchemeDS.CODE));
        conceptSchemeDto.setName((InternationalStringDto) identifiersEditionForm.getValue(ConceptSchemeDS.NAME));
        // Content descriptors
        conceptSchemeDto.setDescription((InternationalStringDto) contentDescriptorsEditionForm.getValue(ConceptSchemeDS.DESCRIPTION));
        conceptSchemeDto.setIsPartial((contentDescriptorsEditionForm.getValue(ConceptSchemeDS.IS_PARTIAL) != null && !StringUtils.isEmpty(contentDescriptorsEditionForm
                .getValueAsString(ConceptSchemeDS.IS_PARTIAL))) ? Boolean.valueOf(contentDescriptorsEditionForm.getValueAsString(ConceptSchemeDS.IS_PARTIAL)) : false);
        // Class descriptors
        conceptSchemeDto.setType(ConceptSchemeTypeEnum.valueOf(classDescriptorsEditionForm.getValueAsString(ConceptSchemeDS.TYPE)));
        conceptSchemeDto.setRelatedOperation(ExternalItemUtils.removeTitle(relatedOperation));

        // Annotations
        conceptSchemeDto.getAnnotations().clear();
        conceptSchemeDto.getAnnotations().addAll(annotationsEditionPanel.getAnnotations());

        return conceptSchemeDto;
    }

    private SearchViewTextItem createRelatedOperationItem(String name, String title) {
        SearchViewTextItem operation = new SearchViewTextItem(name, title);
        operation.setRequired(true);
        operation.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                final int OPERATION_FIRST_RESULT = 0;
                final int OPERATION_MAX_RESULTS = 16;
                searchOperationsWindow = new SearchExternalItemWindow(getConstants().conceptSchemeSearchOperations(), OPERATION_MAX_RESULTS, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        uiHandlers.retrieveStatisticalOperations(firstResult, maxResults, null);
                    }
                });
                uiHandlers.retrieveStatisticalOperations(OPERATION_FIRST_RESULT, OPERATION_MAX_RESULTS, null);
                searchOperationsWindow.getListGrid().setSelectionType(SelectionStyle.SINGLE); // Only one statistical operation can be selected
                searchOperationsWindow.getExternalListGridItem().setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String code) {
                        uiHandlers.retrieveStatisticalOperations(firstResult, maxResults, code);
                    }
                });
                searchOperationsWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        relatedOperation = searchOperationsWindow.getSelectedExternalItem();
                        searchOperationsWindow.destroy();
                        classDescriptorsEditionForm.setValue(ConceptSchemeDS.RELATED_OPERATION, ExternalItemUtils.getExternalItemName(relatedOperation));
                        classDescriptorsEditionForm.validate(false);
                    }
                });
            }
        });
        return operation;
    }

    @Override
    public void setOperations(List<ExternalItemDto> operations, int firstResult, int totalResults) {
        if (searchOperationsWindow != null) {
            searchOperationsWindow.setExternalItems(operations);
            searchOperationsWindow.refreshSourcePaginationInfo(firstResult, operations.size(), totalResults);
        }
    }

}
