package org.siemac.metamac.srm.web.concept.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.List;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.concept.domain.shared.ConceptMetamacVisualisationResult;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.web.client.utils.RequiredFieldUtils;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.client.widgets.AnnotationsPanel;
import org.siemac.metamac.srm.web.client.widgets.ConfirmationWindow;
import org.siemac.metamac.srm.web.client.widgets.CustomTabSet;
import org.siemac.metamac.srm.web.client.widgets.RelatedResourceLinkItem;
import org.siemac.metamac.srm.web.client.widgets.SearchStatisticalOperationLinkItem;
import org.siemac.metamac.srm.web.client.widgets.VersionWindow;
import org.siemac.metamac.srm.web.code.model.ds.CodelistDS;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeDS;
import org.siemac.metamac.srm.web.concept.model.record.ConceptSchemeRecord;
import org.siemac.metamac.srm.web.concept.presenter.ConceptSchemePresenter;
import org.siemac.metamac.srm.web.concept.utils.CommonUtils;
import org.siemac.metamac.srm.web.concept.utils.ConceptsFormUtils;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptSchemeUiHandlers;
import org.siemac.metamac.srm.web.concept.widgets.ConceptSchemeCategorisationsPanel;
import org.siemac.metamac.srm.web.concept.widgets.ConceptSchemeMainFormLayout;
import org.siemac.metamac.srm.web.concept.widgets.ConceptSchemeVersionsSectionStack;
import org.siemac.metamac.srm.web.concept.widgets.ConceptsTreeGrid;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemesResult;
import org.siemac.metamac.srm.web.shared.concept.GetStatisticalOperationsResult;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.utils.BooleanWebUtils;
import org.siemac.metamac.web.common.client.utils.DateUtils;
import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;
import org.siemac.metamac.web.common.client.widgets.InformationLabel;
import org.siemac.metamac.web.common.client.widgets.TitleLabel;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.ExternalItemLinkItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultilanguageRichTextEditorItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;
import org.siemac.metamac.web.common.client.widgets.handlers.CustomLinkItemNavigationClickHandler;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.validator.RequiredIfFunction;
import com.smartgwt.client.widgets.form.validator.RequiredIfValidator;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

public class ConceptSchemeViewImpl extends ViewWithUiHandlers<ConceptSchemeUiHandlers> implements ConceptSchemePresenter.ConceptSchemeView {

    private final TitleLabel                        titleLabel;
    private final VLayout                           panel;
    private final ConceptSchemeMainFormLayout       mainFormLayout;

    private final CustomTabSet                      tabSet;
    private final Tab                               conceptSchemeTab;

    // View forms
    private GroupDynamicForm                        identifiersForm;
    private GroupDynamicForm                        contentDescriptorsForm;
    private GroupDynamicForm                        classDescriptorsForm;
    private GroupDynamicForm                        productionDescriptorsForm;
    private GroupDynamicForm                        diffusionDescriptorsForm;
    private GroupDynamicForm                        versionResponsibilityForm;
    private GroupDynamicForm                        commentsForm;
    private AnnotationsPanel                        annotationsPanel;

    // Edition forms
    private GroupDynamicForm                        identifiersEditionForm;
    private GroupDynamicForm                        contentDescriptorsEditionForm;
    private GroupDynamicForm                        classDescriptorsEditionForm;
    private GroupDynamicForm                        productionDescriptorsEditionForm;
    private GroupDynamicForm                        diffusionDescriptorsEditionForm;
    private GroupDynamicForm                        versionResponsibilityEditionForm;
    private GroupDynamicForm                        commentsEditionForm;
    private AnnotationsPanel                        annotationsEditionPanel;

    private final InformationLabel                  conceptsNoVisibleInfoMessage;
    private final ConceptsTreeGrid                  conceptsTreeGrid;
    private final ConceptSchemeVersionsSectionStack versionsSectionStack;
    private final ConceptSchemeCategorisationsPanel categorisationsPanel;

    private ConceptSchemeMetamacDto                 conceptSchemeDto;
    private ConceptSchemeTypeEnum                   conceptSchemeType;               // this field stores the initial type of the concept scheme
    private SearchStatisticalOperationLinkItem      operationItem;

    @Inject
    public ConceptSchemeViewImpl() {
        super();
        panel = new VLayout();
        panel.setHeight100();

        //
        // CONCEPT SCHEME VERSIONS
        //

        versionsSectionStack = new ConceptSchemeVersionsSectionStack(getConstants().conceptSchemeVersions());
        versionsSectionStack.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                String urn = ((ConceptSchemeRecord) event.getRecord()).getUrn();
                getUiHandlers().goToConceptScheme(urn);
            }
        });

        //
        // CONCEPT SCHEME
        //

        mainFormLayout = new ConceptSchemeMainFormLayout();
        bindMainFormLayoutEvents();
        createViewForm();
        createEditionForm();

        //
        // CONCEPTS
        //

        conceptsNoVisibleInfoMessage = new InformationLabel(getConstants().conceptSchemeConceptsNoVisibleInfoMessage());
        conceptsNoVisibleInfoMessage.setMargin(10);

        conceptsTreeGrid = new ConceptsTreeGrid();

        VLayout conceptsLayout = new VLayout();
        conceptsLayout.setMargin(15);
        conceptsLayout.addMember(conceptsNoVisibleInfoMessage);
        conceptsLayout.addMember(conceptsTreeGrid);

        //
        // CATEGORISATIONS
        //

        categorisationsPanel = new ConceptSchemeCategorisationsPanel();

        //
        // PANEL LAYOUT
        //

        VLayout subPanel = new VLayout();
        subPanel.setOverflow(Overflow.SCROLL);
        subPanel.setMembersMargin(5);
        subPanel.addMember(versionsSectionStack);

        titleLabel = new TitleLabel();

        tabSet = new CustomTabSet();

        // ConceptScheme tab
        conceptSchemeTab = new Tab(getConstants().conceptScheme());
        conceptSchemeTab.setPane(mainFormLayout);
        tabSet.addTab(conceptSchemeTab);

        // Concepts tab
        Tab conceptsTab = new Tab(getConstants().concepts());
        conceptsTab.setPane(conceptsLayout);
        tabSet.addTab(conceptsTab);

        // Categorisations tab
        Tab categorisationsTab = new Tab(getConstants().categorisations());
        categorisationsTab.setPane(categorisationsPanel);
        tabSet.addTab(categorisationsTab);

        VLayout tabSubPanel = new VLayout();
        tabSubPanel.addMember(titleLabel);
        tabSubPanel.addMember(tabSet);
        tabSubPanel.setMargin(15);
        subPanel.addMember(tabSubPanel);

        panel.addMember(subPanel);
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

                commentsForm.setTranslationsShowed(translationsShowed);
                commentsEditionForm.setTranslationsShowed(translationsShowed);

                annotationsPanel.setTranslationsShowed(translationsShowed);
                annotationsEditionPanel.setTranslationsShowed(translationsShowed);
            }
        });

        // Edit
        mainFormLayout.getEditToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                startConceptSchemeEdition();
            }
        });

        // Delete
        mainFormLayout.getDeleteConfirmationWindow().getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deleteConceptScheme(conceptSchemeDto.getUrn());
            }
        });

        // Save
        mainFormLayout.getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (identifiersEditionForm.validate(false) && contentDescriptorsEditionForm.validate(false) && classDescriptorsEditionForm.validate(false)
                        && productionDescriptorsEditionForm.validate(false) && diffusionDescriptorsEditionForm.validate(false)) {
                    getUiHandlers().saveConceptScheme(getConceptSchemeDto());
                }
            }
        });

        // Life cycle
        mainFormLayout.getSendToProductionValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().sendToProductionValidation(conceptSchemeDto);
            }
        });
        mainFormLayout.getSendToDiffusionValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().sendToDiffusionValidation(conceptSchemeDto);
            }
        });
        mainFormLayout.getRejectValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().rejectValidation(conceptSchemeDto);
            }
        });
        mainFormLayout.getPublishInternally().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                publishConceptSchemeInternally();
            }
        });
        mainFormLayout.getPublishExternally().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().publishExternally(conceptSchemeDto);
            }
        });
        mainFormLayout.getCreateTemporalVersion().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().createTemporalVersion(conceptSchemeDto.getUrn());
            }
        });
        mainFormLayout.getConsolidateVersion().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                versionConceptScheme();
            }
        });
        mainFormLayout.getCancelValidity().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().cancelValidity(conceptSchemeDto.getUrn());
            }
        });
        mainFormLayout.getExport().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().exportConceptScheme(conceptSchemeDto.getUrn());
            }
        });
        mainFormLayout.getCopy().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().copyConceptScheme(conceptSchemeDto.getUrn());
            }
        });
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == ConceptSchemePresenter.TYPE_SetContextAreaContentConceptsToolBar) {
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
    public void setUiHandlers(ConceptSchemeUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        this.conceptsTreeGrid.setUiHandlers(uiHandlers);
        this.categorisationsPanel.setUiHandlers(uiHandlers);
    }

    @Override
    public void setConceptSchemeVersions(List<ConceptSchemeMetamacBasicDto> conceptSchemeDtos) {
        versionsSectionStack.setConceptSchemes(conceptSchemeDtos);
        versionsSectionStack.selectConceptScheme(conceptSchemeDto);
    }

    @Override
    public void selectConceptSchemeTab() {
        tabSet.selectTab(conceptSchemeTab);
    }

    @Override
    public void setConceptScheme(ConceptSchemeMetamacDto conceptScheme) {
        this.conceptSchemeDto = conceptScheme;
        this.conceptSchemeType = conceptScheme.getType();

        // Set title
        titleLabel.setContents(org.siemac.metamac.srm.web.client.utils.CommonUtils.getResourceTitle(conceptScheme));

        // Security
        mainFormLayout.setConceptScheme(conceptScheme);
        mainFormLayout.setViewMode();
        categorisationsPanel.updateVisibility(conceptScheme);

        setConceptSchemeViewMode(conceptScheme);
        setConceptSchemeEditionMode(conceptScheme);

        // Update concept scheme in tree grid
        conceptsTreeGrid.updateItemScheme(conceptScheme);
        // The concepts scheme type can be null (if have been imported). Do not show concepts until the concept scheme type is specified.
        if (conceptScheme.getType() == null) {
            conceptsNoVisibleInfoMessage.show();
            conceptsTreeGrid.hide();
        } else {
            conceptsNoVisibleInfoMessage.hide();
            conceptsTreeGrid.show();
        }
    }

    @Override
    public void startConceptSchemeEdition() {
        mainFormLayout.setEditionMode();
    }

    @Override
    public void setConcepts(List<ConceptMetamacVisualisationResult> itemHierarchyDtos) {
        conceptsTreeGrid.setConcepts(conceptSchemeDto, itemHierarchyDtos);
        // Set the max records to the size of the items list (plus the item scheme node)
        conceptsTreeGrid.setAutoFitMaxRecords(itemHierarchyDtos.size() + 1);
    }

    @Override
    public void setOperations(GetStatisticalOperationsResult result) {
        operationItem.setOperations(result.getOperations(), result.getFirstResultOut(), result.getTotalResults());
    }

    @Override
    public void setCategorisations(List<CategorisationDto> categorisationDtos) {
        categorisationsPanel.setCategorisations(categorisationDtos);
    }

    @Override
    public void setCategorySchemesForCategorisations(GetRelatedResourcesResult result) {
        categorisationsPanel.setCategorySchemes(result);
    }

    @Override
    public void setCategoriesForCategorisations(GetRelatedResourcesResult result) {
        categorisationsPanel.setCategories(result);
    }

    private void createViewForm() {
        // Identifiers Form
        identifiersForm = new GroupDynamicForm(getConstants().formIdentifiers());
        ViewTextItem code = new ViewTextItem(ConceptSchemeDS.CODE, getConstants().identifiableArtefactCode());
        ViewMultiLanguageTextItem name = new ViewMultiLanguageTextItem(ConceptSchemeDS.NAME, getConstants().nameableArtefactName());
        ViewTextItem uri = new ViewTextItem(ConceptSchemeDS.URI, getConstants().identifiableArtefactUriProvider());
        ViewTextItem urn = new ViewTextItem(ConceptSchemeDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(ConceptSchemeDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        ViewTextItem version = new ViewTextItem(ConceptSchemeDS.VERSION_LOGIC, getConstants().maintainableArtefactVersionLogic());
        identifiersForm.setFields(code, name, uri, urn, urnProvider, version);

        // Content descriptors
        contentDescriptorsForm = new GroupDynamicForm(getConstants().formContentDescriptors());
        ViewMultiLanguageTextItem description = new ViewMultiLanguageTextItem(ConceptSchemeDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        ViewTextItem partial = new ViewTextItem(ConceptSchemeDS.IS_PARTIAL, getConstants().itemSchemeIsPartial());
        ViewTextItem isExternalReference = new ViewTextItem(ConceptSchemeDS.IS_EXTERNAL_REFERENCE, getConstants().maintainableArtefactIsExternalReference());
        ViewTextItem isFinal = new ViewTextItem(ConceptSchemeDS.FINAL, getConstants().maintainableArtefactFinalLogic());
        contentDescriptorsForm.setFields(description, partial, isExternalReference, isFinal);

        // Class descriptors
        classDescriptorsForm = new GroupDynamicForm(getConstants().formClassDescriptors());
        ViewTextItem type = new ViewTextItem(ConceptSchemeDS.TYPE, getConstants().conceptSchemeType());
        ViewTextItem typeView = new ViewTextItem(ConceptSchemeDS.TYPE_VIEW, getConstants().conceptSchemeType());
        typeView.setShowIfCondition(FormItemUtils.getFalseFormItemIfFunction());
        ExternalItemLinkItem operation = new ExternalItemLinkItem(ConceptSchemeDS.RELATED_OPERATION, getConstants().conceptSchemeOperation());
        operation.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return ConceptSchemeTypeEnum.OPERATION.name().equals(form.getValueAsString(ConceptSchemeDS.TYPE_VIEW))
                        || ConceptSchemeTypeEnum.MEASURE.name().equals(form.getValueAsString(ConceptSchemeDS.TYPE_VIEW));
            }
        });
        classDescriptorsForm.setFields(type, typeView, operation);

        // Production descriptors
        productionDescriptorsForm = new GroupDynamicForm(getConstants().formProductionDescriptors());
        RelatedResourceLinkItem agency = new RelatedResourceLinkItem(ConceptSchemeDS.MAINTAINER, getConstants().maintainableArtefactMaintainer(), getCustomLinkItemNavigationClickHandler());
        ViewTextItem procStatus = new ViewTextItem(ConceptSchemeDS.PROC_STATUS, getConstants().lifeCycleProcStatus());
        ViewTextItem creationDate = new ViewTextItem(ConceptSchemeDS.VERSION_CREATION_DATE, getConstants().maintainableArtefactVersionCreationDate());
        ViewTextItem resourceCreationDate = new ViewTextItem(ConceptSchemeDS.RESOURCE_CREATION_DATE, getConstants().maintainableArtefactResourceCreationDate());
        productionDescriptorsForm.setFields(agency, procStatus, creationDate, resourceCreationDate);

        // Diffusion descriptors
        diffusionDescriptorsForm = new GroupDynamicForm(getConstants().formDiffusionDescriptors());
        ViewTextItem replacedBy = new ViewTextItem(ConceptSchemeDS.REPLACED_BY_VERSION, getConstants().maintainableArtefactReplacedByVersion());
        ViewTextItem replaceTo = new ViewTextItem(ConceptSchemeDS.REPLACE_TO_VERSION, getConstants().maintainableArtefactReplaceToVersion());
        ViewTextItem validFrom = new ViewTextItem(ConceptSchemeDS.VALID_FROM, getConstants().maintainableArtefactValidFrom());
        ViewTextItem validTo = new ViewTextItem(ConceptSchemeDS.VALID_TO, getConstants().maintainableArtefactValidTo());
        diffusionDescriptorsForm.setFields(replacedBy, replaceTo, validFrom, validTo);

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

        // Comments
        commentsForm = new GroupDynamicForm(getConstants().nameableArtefactComments());
        ViewMultiLanguageTextItem comments = new ViewMultiLanguageTextItem(ConceptSchemeDS.COMMENTS, getConstants().nameableArtefactComments());
        commentsForm.setFields(comments);

        // Annotations
        annotationsPanel = new AnnotationsPanel(true);

        mainFormLayout.addViewCanvas(identifiersForm);
        mainFormLayout.addViewCanvas(contentDescriptorsForm);
        mainFormLayout.addViewCanvas(classDescriptorsForm);
        mainFormLayout.addViewCanvas(productionDescriptorsForm);
        mainFormLayout.addViewCanvas(diffusionDescriptorsForm);
        mainFormLayout.addViewCanvas(versionResponsibilityForm);
        mainFormLayout.addViewCanvas(commentsForm);
        mainFormLayout.addViewCanvas(annotationsPanel);
    }

    private void createEditionForm() {
        // Identifiers
        identifiersEditionForm = new GroupDynamicForm(getConstants().formIdentifiers());
        RequiredTextItem code = new RequiredTextItem(ConceptSchemeDS.CODE, getConstants().identifiableArtefactCode());
        code.setValidators(SemanticIdentifiersUtils.getConceptSchemeIdentifierCustomValidator());
        code.setShowIfCondition(getCodeFormItemIfFunction());

        ViewTextItem staticCode = new ViewTextItem(ConceptSchemeDS.CODE_VIEW, getConstants().identifiableArtefactCode());
        staticCode.setShowIfCondition(getStaticCodeFormItemIfFunction());

        MultiLanguageTextItem name = new MultiLanguageTextItem(ConceptSchemeDS.NAME, getConstants().nameableArtefactName());
        name.setRequired(true);
        ViewTextItem uriProvider = new ViewTextItem(ConceptSchemeDS.URI, getConstants().identifiableArtefactUriProvider());
        ViewTextItem urn = new ViewTextItem(ConceptSchemeDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(ConceptSchemeDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        ViewTextItem version = new ViewTextItem(ConceptSchemeDS.VERSION_LOGIC, getConstants().maintainableArtefactVersionLogic());
        identifiersEditionForm.setFields(code, staticCode, name, uriProvider, urn, urnProvider, version);

        // Content descriptors
        contentDescriptorsEditionForm = new GroupDynamicForm(getConstants().formContentDescriptors());
        MultilanguageRichTextEditorItem description = new MultilanguageRichTextEditorItem(ConceptSchemeDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        ViewTextItem partial = new ViewTextItem(ConceptSchemeDS.IS_PARTIAL, getConstants().itemSchemeIsPartial());
        ViewTextItem isExternalReference = new ViewTextItem(ConceptSchemeDS.IS_EXTERNAL_REFERENCE, getConstants().maintainableArtefactIsExternalReference());
        ViewTextItem isFinal = new ViewTextItem(ConceptSchemeDS.FINAL, getConstants().maintainableArtefactFinalLogic());
        contentDescriptorsEditionForm.setFields(description, partial, isExternalReference, isFinal);

        // CLASS DESCRIPTORS

        classDescriptorsEditionForm = new GroupDynamicForm(getConstants().formClassDescriptors());

        // Type

        final RequiredSelectItem type = new RequiredSelectItem(ConceptSchemeDS.TYPE, getConstants().conceptSchemeType());
        type.setValueMap(CommonUtils.getConceptSchemeTypeHashMap());
        type.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                classDescriptorsEditionForm.markForRedraw();
                if (!ConceptSchemeTypeEnum.OPERATION.name().equals(type.getValueAsString()) && !ConceptSchemeTypeEnum.MEASURE.name().equals(type.getValueAsString())) {
                    // if the concept scheme is not an operation type anymore, remove the related operation
                    classDescriptorsEditionForm.clearValue(ConceptSchemeDS.RELATED_OPERATION);
                }
            }
        });
        type.setShowIfCondition(getTypeFormItemIfFunction());
        type.setIcons(CommonUtils.getConceptSchemeTypeInfoIcon());

        ViewTextItem typeView = new ViewTextItem(ConceptSchemeDS.TYPE_VIEW, getConstants().conceptSchemeType());
        typeView.setShowIfCondition(getStaticTypeFormItemIfFunction());

        // Operation

        operationItem = createRelatedOperationItem(ConceptSchemeDS.RELATED_OPERATION, getConstants().conceptSchemeOperation());
        operationItem.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return ConceptSchemeTypeEnum.OPERATION.name().equals(form.getValueAsString(ConceptSchemeDS.TYPE))
                        || ConceptSchemeTypeEnum.MEASURE.name().equals(form.getValueAsString(ConceptSchemeDS.TYPE));
            }
        });

        classDescriptorsEditionForm.setFields(type, typeView, operationItem);

        // Production descriptors
        productionDescriptorsEditionForm = new GroupDynamicForm(getConstants().formProductionDescriptors());
        RelatedResourceLinkItem agency = new RelatedResourceLinkItem(ConceptSchemeDS.MAINTAINER, getConstants().maintainableArtefactMaintainer(), getCustomLinkItemNavigationClickHandler());
        ViewTextItem procStatus = new ViewTextItem(ConceptSchemeDS.PROC_STATUS, getConstants().lifeCycleProcStatus());
        ViewTextItem creationDate = new ViewTextItem(ConceptSchemeDS.VERSION_CREATION_DATE, getConstants().maintainableArtefactVersionCreationDate());
        ViewTextItem resourceCreationDate = new ViewTextItem(ConceptSchemeDS.RESOURCE_CREATION_DATE, getConstants().maintainableArtefactResourceCreationDate());
        productionDescriptorsEditionForm.setFields(agency, procStatus, creationDate, resourceCreationDate);

        // Diffusion descriptors
        diffusionDescriptorsEditionForm = new GroupDynamicForm(getConstants().formDiffusionDescriptors());
        ViewTextItem replacedBy = new ViewTextItem(ConceptSchemeDS.REPLACED_BY_VERSION, getConstants().maintainableArtefactReplacedByVersion());
        ViewTextItem replaceTo = new ViewTextItem(ConceptSchemeDS.REPLACE_TO_VERSION, getConstants().maintainableArtefactReplaceToVersion());
        ViewTextItem validFrom = new ViewTextItem(ConceptSchemeDS.VALID_FROM, getConstants().maintainableArtefactValidFrom());
        ViewTextItem validTo = new ViewTextItem(ConceptSchemeDS.VALID_TO, getConstants().maintainableArtefactValidTo());
        diffusionDescriptorsEditionForm.setFields(replacedBy, replaceTo, validFrom, validTo);

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

        // Comments
        commentsEditionForm = new GroupDynamicForm(getConstants().nameableArtefactComments());
        MultilanguageRichTextEditorItem comments = new MultilanguageRichTextEditorItem(ConceptSchemeDS.COMMENTS, getConstants().nameableArtefactComments());
        commentsEditionForm.setFields(comments);

        // Annotations
        annotationsEditionPanel = new AnnotationsPanel(false);

        mainFormLayout.addEditionCanvas(identifiersEditionForm);
        mainFormLayout.addEditionCanvas(contentDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(classDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(productionDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(diffusionDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(versionResponsibilityEditionForm);
        mainFormLayout.addEditionCanvas(commentsEditionForm);
        mainFormLayout.addEditionCanvas(annotationsEditionPanel);
    }
    public void setConceptSchemeViewMode(ConceptSchemeMetamacDto conceptSchemeDto) {
        // Identifiers
        identifiersForm.setValue(ConceptSchemeDS.CODE, conceptSchemeDto.getCode());
        identifiersForm.setValue(ConceptSchemeDS.URI, conceptSchemeDto.getUriProvider());
        identifiersForm.setValue(ConceptSchemeDS.URN, conceptSchemeDto.getUrn());
        identifiersForm.setValue(ConceptSchemeDS.URN_PROVIDER, conceptSchemeDto.getUrnProvider());
        identifiersForm.setValue(ConceptSchemeDS.VERSION_LOGIC, conceptSchemeDto.getVersionLogic());
        identifiersForm.setValue(ConceptSchemeDS.NAME, RecordUtils.getInternationalStringRecord(conceptSchemeDto.getName()));

        // Content descriptors
        contentDescriptorsForm.setValue(ConceptSchemeDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(conceptSchemeDto.getDescription()));
        contentDescriptorsForm.setValue(ConceptSchemeDS.IS_PARTIAL, BooleanWebUtils.getBooleanLabel(conceptSchemeDto.getIsPartial()));
        contentDescriptorsForm.setValue(ConceptSchemeDS.IS_EXTERNAL_REFERENCE, conceptSchemeDto.getIsExternalReference() != null ? (conceptSchemeDto.getIsExternalReference() ? MetamacWebCommon
                .getConstants().yes() : MetamacWebCommon.getConstants().no()) : StringUtils.EMPTY);
        contentDescriptorsForm.setValue(ConceptSchemeDS.FINAL, conceptSchemeDto.getFinalLogic() != null ? (conceptSchemeDto.getFinalLogic() ? MetamacWebCommon.getConstants().yes() : MetamacWebCommon
                .getConstants().no()) : StringUtils.EMPTY);

        // Class descriptors
        classDescriptorsForm.setValue(ConceptSchemeDS.TYPE_VIEW, conceptSchemeDto.getType() != null ? conceptSchemeDto.getType().name() : null);
        classDescriptorsForm.setValue(ConceptSchemeDS.TYPE, CommonUtils.getConceptSchemeTypeName(conceptSchemeDto.getType()));
        if (ConceptSchemeTypeEnum.OPERATION.equals(conceptSchemeDto.getType()) || ConceptSchemeTypeEnum.MEASURE.equals(conceptSchemeDto.getType())) {
            classDescriptorsForm.getItem(ConceptSchemeDS.RELATED_OPERATION).show();
        } else {
            classDescriptorsForm.getItem(ConceptSchemeDS.RELATED_OPERATION).hide();
        }
        classDescriptorsForm.setValue(ConceptSchemeDS.RELATED_OPERATION, conceptSchemeDto.getRelatedOperation());
        classDescriptorsForm.markForRedraw();

        // Production descriptors

        ((RelatedResourceLinkItem) productionDescriptorsForm.getItem(ConceptSchemeDS.MAINTAINER)).setRelatedResource(conceptSchemeDto.getMaintainer());
        productionDescriptorsForm.setValue(ConceptSchemeDS.PROC_STATUS, org.siemac.metamac.srm.web.client.utils.CommonUtils.getProcStatusName(conceptSchemeDto.getLifeCycle().getProcStatus()));
        productionDescriptorsForm.setValue(CodelistDS.VERSION_CREATION_DATE, conceptSchemeDto.getCreatedDate());
        productionDescriptorsForm.setValue(CodelistDS.RESOURCE_CREATION_DATE, conceptSchemeDto.getResourceCreatedDate());

        // Diffusion descriptors
        diffusionDescriptorsForm.setValue(ConceptSchemeDS.REPLACED_BY_VERSION, conceptSchemeDto.getReplacedByVersion());
        diffusionDescriptorsForm.setValue(ConceptSchemeDS.REPLACE_TO_VERSION, conceptSchemeDto.getReplaceToVersion());
        diffusionDescriptorsForm.setValue(ConceptSchemeDS.VALID_FROM, conceptSchemeDto.getValidFrom());
        diffusionDescriptorsForm.setValue(ConceptSchemeDS.VALID_TO, conceptSchemeDto.getValidTo());

        // Version responsibility
        versionResponsibilityForm.setValue(ConceptSchemeDS.PRODUCTION_VALIDATION_USER, conceptSchemeDto.getLifeCycle().getProductionValidationUser());
        versionResponsibilityForm.setValue(ConceptSchemeDS.PRODUCTION_VALIDATION_DATE, conceptSchemeDto.getLifeCycle().getProductionValidationDate());
        versionResponsibilityForm.setValue(ConceptSchemeDS.DIFFUSION_VALIDATION_USER, conceptSchemeDto.getLifeCycle().getDiffusionValidationUser());
        versionResponsibilityForm.setValue(ConceptSchemeDS.DIFFUSION_VALIDATION_DATE, conceptSchemeDto.getLifeCycle().getDiffusionValidationDate());
        versionResponsibilityForm.setValue(ConceptSchemeDS.INTERNAL_PUBLICATION_USER, conceptSchemeDto.getLifeCycle().getInternalPublicationUser());
        versionResponsibilityForm.setValue(ConceptSchemeDS.INTERNAL_PUBLICATION_DATE, conceptSchemeDto.getLifeCycle().getInternalPublicationDate());
        versionResponsibilityForm.setValue(ConceptSchemeDS.EXTERNAL_PUBLICATION_USER, conceptSchemeDto.getLifeCycle().getExternalPublicationUser());
        versionResponsibilityForm.setValue(ConceptSchemeDS.EXTERNAL_PUBLICATION_DATE, conceptSchemeDto.getLifeCycle().getExternalPublicationDate());

        // Comments
        commentsForm.setValue(ConceptSchemeDS.COMMENTS, RecordUtils.getInternationalStringRecord(conceptSchemeDto.getComment()));

        // Annotations
        annotationsPanel.setAnnotations(conceptSchemeDto.getAnnotations(), conceptSchemeDto);
    }

    public void setConceptSchemeEditionMode(ConceptSchemeMetamacDto conceptSchemeDto) {

        String[] requiredFieldsToNextProcStatus = RequiredFieldUtils.getConceptSchemeRequiredFieldsToNextProcStatus(conceptSchemeDto.getLifeCycle().getProcStatus());

        // IDENTIFIERS

        identifiersEditionForm.setValue(ConceptSchemeDS.CODE, conceptSchemeDto.getCode());
        identifiersEditionForm.setValue(ConceptSchemeDS.CODE_VIEW, conceptSchemeDto.getCode());
        identifiersEditionForm.setValue(ConceptSchemeDS.URI, conceptSchemeDto.getUriProvider());
        identifiersEditionForm.setValue(ConceptSchemeDS.URN, conceptSchemeDto.getUrn());
        identifiersEditionForm.setValue(ConceptSchemeDS.URN_PROVIDER, conceptSchemeDto.getUrnProvider());
        identifiersEditionForm.setValue(ConceptSchemeDS.VERSION_LOGIC, conceptSchemeDto.getVersionLogic());
        identifiersEditionForm.setValue(ConceptSchemeDS.NAME, RecordUtils.getInternationalStringRecord(conceptSchemeDto.getName()));
        identifiersEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);
        identifiersEditionForm.markForRedraw();

        // CONTENT DESCRIPTORS

        contentDescriptorsEditionForm.setValue(ConceptSchemeDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(conceptSchemeDto.getDescription()));
        contentDescriptorsEditionForm.setValue(ConceptSchemeDS.IS_PARTIAL, BooleanWebUtils.getBooleanLabel(conceptSchemeDto.getIsPartial()));
        contentDescriptorsEditionForm.setValue(ConceptSchemeDS.IS_EXTERNAL_REFERENCE, conceptSchemeDto.getIsExternalReference() != null ? (conceptSchemeDto.getIsExternalReference() ? MetamacWebCommon
                .getConstants().yes() : MetamacWebCommon.getConstants().no()) : StringUtils.EMPTY);
        contentDescriptorsEditionForm.setValue(ConceptSchemeDS.FINAL, conceptSchemeDto.getFinalLogic() != null ? (conceptSchemeDto.getFinalLogic()
                ? MetamacWebCommon.getConstants().yes()
                : MetamacWebCommon.getConstants().no()) : StringUtils.EMPTY);
        contentDescriptorsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // CLASS DESCRIPTORS

        classDescriptorsEditionForm.setValue(ConceptSchemeDS.TYPE, conceptSchemeDto.getType() != null ? conceptSchemeDto.getType().name() : null);
        classDescriptorsEditionForm.setValue(ConceptSchemeDS.TYPE_VIEW, CommonUtils.getConceptSchemeTypeName(conceptSchemeDto.getType()));
        classDescriptorsEditionForm.setValue(ConceptSchemeDS.RELATED_OPERATION, conceptSchemeDto.getRelatedOperation());
        classDescriptorsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);
        classDescriptorsEditionForm.markForRedraw();

        // PRODUCTION DESCRIPTORS

        ((RelatedResourceLinkItem) productionDescriptorsEditionForm.getItem(ConceptSchemeDS.MAINTAINER)).setRelatedResource(conceptSchemeDto.getMaintainer());
        productionDescriptorsEditionForm.setValue(ConceptSchemeDS.PROC_STATUS, org.siemac.metamac.srm.web.client.utils.CommonUtils.getProcStatusName(conceptSchemeDto.getLifeCycle().getProcStatus()));
        productionDescriptorsEditionForm.setValue(CodelistDS.VERSION_CREATION_DATE, conceptSchemeDto.getCreatedDate());
        productionDescriptorsEditionForm.setValue(CodelistDS.RESOURCE_CREATION_DATE, conceptSchemeDto.getResourceCreatedDate());
        productionDescriptorsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // DIFFUSION DESCRIPTORS

        diffusionDescriptorsEditionForm.setValue(ConceptSchemeDS.REPLACED_BY_VERSION, conceptSchemeDto.getReplacedByVersion());
        diffusionDescriptorsEditionForm.setValue(ConceptSchemeDS.REPLACE_TO_VERSION, conceptSchemeDto.getReplaceToVersion());
        diffusionDescriptorsEditionForm.setValue(ConceptSchemeDS.VALID_FROM, DateUtils.getFormattedDate(conceptSchemeDto.getValidFrom()));
        diffusionDescriptorsEditionForm.setValue(ConceptSchemeDS.VALID_TO, DateUtils.getFormattedDate(conceptSchemeDto.getValidTo()));
        diffusionDescriptorsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // VERSION RESPONSIBILITY

        versionResponsibilityEditionForm.setValue(ConceptSchemeDS.PRODUCTION_VALIDATION_USER, conceptSchemeDto.getLifeCycle().getProductionValidationUser());
        versionResponsibilityEditionForm.setValue(ConceptSchemeDS.PRODUCTION_VALIDATION_DATE, conceptSchemeDto.getLifeCycle().getProductionValidationDate());
        versionResponsibilityEditionForm.setValue(ConceptSchemeDS.DIFFUSION_VALIDATION_USER, conceptSchemeDto.getLifeCycle().getDiffusionValidationUser());
        versionResponsibilityEditionForm.setValue(ConceptSchemeDS.DIFFUSION_VALIDATION_DATE, conceptSchemeDto.getLifeCycle().getDiffusionValidationDate());
        versionResponsibilityEditionForm.setValue(ConceptSchemeDS.INTERNAL_PUBLICATION_USER, conceptSchemeDto.getLifeCycle().getInternalPublicationUser());
        versionResponsibilityEditionForm.setValue(ConceptSchemeDS.INTERNAL_PUBLICATION_DATE, conceptSchemeDto.getLifeCycle().getInternalPublicationDate());
        versionResponsibilityEditionForm.setValue(ConceptSchemeDS.EXTERNAL_PUBLICATION_USER, conceptSchemeDto.getLifeCycle().getExternalPublicationUser());
        versionResponsibilityEditionForm.setValue(ConceptSchemeDS.EXTERNAL_PUBLICATION_DATE, conceptSchemeDto.getLifeCycle().getExternalPublicationDate());
        versionResponsibilityEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // COMMENTS

        commentsEditionForm.setValue(ConceptSchemeDS.COMMENTS, RecordUtils.getInternationalStringRecord(conceptSchemeDto.getComment()));
        commentsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // ANNOTATIONS

        annotationsEditionPanel.setAnnotations(conceptSchemeDto.getAnnotations(), conceptSchemeDto);
    }

    public ConceptSchemeMetamacDto getConceptSchemeDto() {
        // Identifiers
        conceptSchemeDto.setCode(identifiersEditionForm.getValueAsString(ConceptSchemeDS.CODE));
        conceptSchemeDto.setName((InternationalStringDto) identifiersEditionForm.getValue(ConceptSchemeDS.NAME));
        // Content descriptors
        conceptSchemeDto.setDescription((InternationalStringDto) contentDescriptorsEditionForm.getValue(ConceptSchemeDS.DESCRIPTION));
        // Class descriptors
        conceptSchemeDto.setType(ConceptSchemeTypeEnum.valueOf(classDescriptorsEditionForm.getValueAsString(ConceptSchemeDS.TYPE)));
        conceptSchemeDto.setRelatedOperation(classDescriptorsEditionForm.getValueAsExternalItemDto(ConceptSchemeDS.RELATED_OPERATION));

        // Comments
        conceptSchemeDto.setComment((InternationalStringDto) commentsEditionForm.getValue(ConceptSchemeDS.COMMENTS));

        // Annotations
        conceptSchemeDto.getAnnotations().clear();
        conceptSchemeDto.getAnnotations().addAll(annotationsEditionPanel.getAnnotations());

        return conceptSchemeDto;
    }

    private SearchStatisticalOperationLinkItem createRelatedOperationItem(String name, String title) {
        SearchStatisticalOperationLinkItem operation = new SearchStatisticalOperationLinkItem(name, title) {

            @Override
            protected void retrieveStatisticalOperations(int firstResult, int maxResults, MetamacWebCriteria webCriteria) {
                getUiHandlers().retrieveStatisticalOperations(firstResult, maxResults, webCriteria != null ? webCriteria.getCriteria() : null);
            }
        };

        RequiredIfValidator requiredIfValidator = new RequiredIfValidator(new RequiredIfFunction() {

            @Override
            public boolean execute(FormItem formItem, Object value) {
                return ConceptSchemeTypeEnum.OPERATION.equals(conceptSchemeDto.getType());
            }
        });
        operation.setValidators(requiredIfValidator);
        return operation;
    }

    private void publishConceptSchemeInternally() {
        if (org.siemac.metamac.srm.web.client.utils.CommonUtils.isDefaultMaintainer(conceptSchemeDto.getMaintainer())) {
            getUiHandlers().publishInternally(conceptSchemeDto, null);
        } else {
            // If the concept scheme is imported, ask the user if this resource should be the latest one.
            // If there were another concept scheme marked as final, find it, and inform the user that the concept scheme to publish will replace the latest one.
            getUiHandlers().retrieveLatestConceptScheme(conceptSchemeDto); // Publication will be done in setLatestConceptSchemeForInternalPublication method
        }
    }

    @Override
    public void setLatestConceptSchemeForInternalPublication(GetConceptSchemesResult result) {
        if (result.getConceptSchemeList().isEmpty()) {
            getUiHandlers().publishInternally(conceptSchemeDto, null);
        } else {
            // If there were other version marked as the latest, ask the user what to do
            ConceptSchemeMetamacBasicDto latest = result.getConceptSchemeList().get(0);
            ConfirmationWindow confirmationWindow = new ConfirmationWindow(getConstants().lifeCyclePublishInternally(), getMessages().conceptSchemeShouldBeMarkAsTheLatest(latest.getVersionLogic()));
            confirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    // Concept scheme will be the latest
                    getUiHandlers().publishInternally(conceptSchemeDto, true);
                }
            });
            confirmationWindow.getNoButton().addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    // Concept scheme WON'T be the latest
                    getUiHandlers().publishInternally(conceptSchemeDto, false);
                }
            });
        }
    }

    private void versionConceptScheme() {
        final VersionWindow versionWindow = new VersionWindow(getConstants().lifeCycleVersioning());
        versionWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                if (versionWindow.validateForm()) {
                    getUiHandlers().versioning(conceptSchemeDto.getUrn(), versionWindow.getSelectedVersion());
                    versionWindow.destroy();
                }
            }
        });
    }

    // ------------------------------------------------------------------------------------------------------------
    // FORM ITEM IF FUNCTIONS
    // ------------------------------------------------------------------------------------------------------------

    // CODE

    private FormItemIfFunction getCodeFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return ConceptsFormUtils.canConceptSchemeCodeBeEdited(conceptSchemeDto);
            }
        };
    }

    private FormItemIfFunction getStaticCodeFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !ConceptsFormUtils.canConceptSchemeCodeBeEdited(conceptSchemeDto);
            }
        };
    }

    // TYPE

    private FormItemIfFunction getTypeFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return ConceptsFormUtils.canConceptSchemeTypeBeEdited(conceptSchemeType); // Do not call with conceptSchemeDto (may have been modified by the user)
            }
        };
    }

    private FormItemIfFunction getStaticTypeFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !ConceptsFormUtils.canConceptSchemeTypeBeEdited(conceptSchemeType); // Do not call with conceptSchemeDto (may have been modified by the user)
            }
        };
    }

    // ------------------------------------------------------------------------------------------------------------
    // CLICK HANDLERS
    // ------------------------------------------------------------------------------------------------------------

    private CustomLinkItemNavigationClickHandler getCustomLinkItemNavigationClickHandler() {
        return new CustomLinkItemNavigationClickHandler() {

            @Override
            public BaseUiHandlers getBaseUiHandlers() {
                return getUiHandlers();
            }
        };
    }
}
