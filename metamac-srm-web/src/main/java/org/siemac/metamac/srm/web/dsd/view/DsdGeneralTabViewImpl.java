package org.siemac.metamac.srm.web.dsd.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.model.record.DsdRecord;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.client.widgets.AnnotationsPanel;
import org.siemac.metamac.srm.web.client.widgets.BooleanSelectItem;
import org.siemac.metamac.srm.web.client.widgets.DimensionsVisualisationItem;
import org.siemac.metamac.srm.web.client.widgets.VersionWindow;
import org.siemac.metamac.srm.web.dsd.model.ds.DataStructureDefinitionDS;
import org.siemac.metamac.srm.web.dsd.presenter.DsdGeneralTabPresenter;
import org.siemac.metamac.srm.web.dsd.utils.DsdClientSecurityUtils;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdGeneralTabUiHandlers;
import org.siemac.metamac.srm.web.dsd.widgets.DsdCategorisationsPanel;
import org.siemac.metamac.srm.web.dsd.widgets.DsdMainFormLayout;
import org.siemac.metamac.srm.web.dsd.widgets.DsdVersionsSectionStack;
import org.siemac.metamac.srm.web.dsd.widgets.ShowDecimalsPrecisionItem;
import org.siemac.metamac.srm.web.shared.category.GetCategoriesResult;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemesResult;
import org.siemac.metamac.srm.web.shared.concept.GetStatisticalOperationsResult;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.utils.ExternalItemUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.InformationWindow;
import org.siemac.metamac.web.common.client.widgets.SearchExternalItemWindow;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextAreaItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.SearchViewTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DimensionComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.HasClickHandlers;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class DsdGeneralTabViewImpl extends ViewWithUiHandlers<DsdGeneralTabUiHandlers> implements DsdGeneralTabPresenter.DsdGeneralTabView {

    private VLayout                           panel;

    private DsdMainFormLayout                 mainFormLayout;

    private DsdVersionsSectionStack           versionsSectionStack;

    // VIEW FORM

    private GroupDynamicForm                  identifiersForm;
    private GroupDynamicForm                  contentDescriptorsForm;
    private GroupDynamicForm                  classDescriptorsForm;
    private GroupDynamicForm                  statusForm;
    private GroupDynamicForm                  versionResponsibilityForm;
    private GroupDynamicForm                  visualisationMetadataForm;
    private GroupDynamicForm                  commentsForm;
    private AnnotationsPanel                  annotationsPanel;

    // EDITION FORM

    private GroupDynamicForm                  identifiersEditionForm;
    private GroupDynamicForm                  contentDescriptorsEditionForm;
    private GroupDynamicForm                  classDescriptorsEditionForm;
    private GroupDynamicForm                  statusEditionForm;
    private GroupDynamicForm                  versionResponsibilityEditionForm;
    private GroupDynamicForm                  visualisationMetadataEditionForm;
    private GroupDynamicForm                  commentsEditionForm;
    private AnnotationsPanel                  annotationsEditionPanel;

    private DsdCategorisationsPanel           categorisationsPanel;
    private SearchExternalItemWindow          searchOperationWindow;

    private DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto;
    private ExternalItemDto                   statisticalOperation;

    @Inject
    public DsdGeneralTabViewImpl() {
        super();
        panel = new VLayout();

        // Versions

        versionsSectionStack = new DsdVersionsSectionStack(getConstants().dsdVersions());
        versionsSectionStack.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                String urn = ((DsdRecord) event.getRecord()).getUrn();
                getUiHandlers().goToDsd(urn);
            }
        });

        // DSD

        mainFormLayout = new DsdMainFormLayout();
        bindMainFormLayoutEvents();
        createViewForm();
        createEditionForm();

        categorisationsPanel = new DsdCategorisationsPanel();

        panel.addMember(versionsSectionStack);
        panel.addMember(mainFormLayout);
        panel.addMember(categorisationsPanel);
    }

    @Override
    public void setUiHandlers(DsdGeneralTabUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        this.categorisationsPanel.setUiHandlers(uiHandlers);
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

                statusForm.setTranslationsShowed(translationsShowed);
                statusEditionForm.setTranslationsShowed(translationsShowed);

                versionResponsibilityForm.setTranslationsShowed(translationsShowed);
                versionResponsibilityEditionForm.setTranslationsShowed(translationsShowed);

                visualisationMetadataForm.setTranslationsShowed(translationsShowed);
                visualisationMetadataEditionForm.setTranslationsShowed(translationsShowed);

                commentsForm.setTranslationsShowed(translationsShowed);
                commentsEditionForm.setTranslationsShowed(translationsShowed);

                annotationsPanel.setTranslationsShowed(translationsShowed);
                annotationsEditionPanel.setTranslationsShowed(translationsShowed);
            }
        });

        // Edit: Add a custom handler to check scheme status before start editing
        mainFormLayout.getEditToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ProcStatusEnum status = dataStructureDefinitionMetamacDto.getLifeCycle().getProcStatus();
                if (ProcStatusEnum.INTERNALLY_PUBLISHED.equals(status) || ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(status)) {
                    // Create a new version
                    final InformationWindow window = new InformationWindow(getMessages().dsdEditionInfo(), getMessages().dsdEditionInfoDetailedMessage());
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
                if (identifiersEditionForm.validate(false) && contentDescriptorsEditionForm.validate(false) && classDescriptorsEditionForm.validate(false) && statusEditionForm.validate(false)
                        && visualisationMetadataEditionForm.validate(false)) {
                    getUiHandlers().saveDsd(getDataStructureDefinitionDto());
                }
            }
        });

        // Life cycle
        mainFormLayout.getSendToProductionValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().sendToProductionValidation(dataStructureDefinitionMetamacDto);
            }
        });
        mainFormLayout.getSendToDiffusionValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().sendToDiffusionValidation(dataStructureDefinitionMetamacDto);
            }
        });
        mainFormLayout.getRejectValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().rejectValidation(dataStructureDefinitionMetamacDto);
            }
        });
        mainFormLayout.getPublishInternally().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().publishInternally(dataStructureDefinitionMetamacDto);
            }
        });
        mainFormLayout.getPublishExternally().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().publishExternally(dataStructureDefinitionMetamacDto);
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
                            getUiHandlers().versioning(dataStructureDefinitionMetamacDto.getUrn(), versionWindow.getSelectedVersion());
                            versionWindow.destroy();
                        }
                    }
                });
            }
        });
        mainFormLayout.getCancelValidity().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().cancelValidity(dataStructureDefinitionMetamacDto.getUrn());
            }
        });
    }

    /**
     * Creates and returns the view layout
     * 
     * @return
     */
    private void createViewForm() {
        // Identifiers Form
        identifiersForm = new GroupDynamicForm(MetamacSrmWeb.getConstants().formIdentifiers());
        ViewTextItem staticIdLogic = new ViewTextItem(DataStructureDefinitionDS.CODE, getConstants().dsdIdentifier());
        ViewMultiLanguageTextItem staticNameItem = new ViewMultiLanguageTextItem(DataStructureDefinitionDS.NAME, getConstants().nameableArtefactName());
        ViewTextItem staticUriItem = new ViewTextItem(DataStructureDefinitionDS.URI, getConstants().identifiableArtefactUri());
        ViewTextItem staticUrnItem = new ViewTextItem(DataStructureDefinitionDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem staticUrnProviderItem = new ViewTextItem(DataStructureDefinitionDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        ViewTextItem staticVersion = new ViewTextItem(DataStructureDefinitionDS.VERSION_LOGIC, getConstants().maintainableArtefactVersionLogic());
        identifiersForm.setFields(staticIdLogic, staticNameItem, staticUriItem, staticUrnItem, staticUrnProviderItem, staticVersion);

        // Content Descriptors Form
        contentDescriptorsForm = new GroupDynamicForm(getConstants().formContentDescriptors());
        ViewMultiLanguageTextItem staticDescriptionItem = new ViewMultiLanguageTextItem(DataStructureDefinitionDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        ViewTextItem staticFinalItem = new ViewTextItem(DataStructureDefinitionDS.FINAL, getConstants().maintainableArtefactFinalLogic());
        ViewTextItem staticAgency = new ViewTextItem(DataStructureDefinitionDS.MAINTAINER, getConstants().maintainableArtefactMaintainer());
        ViewTextItem relatedOperation = new ViewTextItem(DataStructureDefinitionDS.STATISTICAL_OPERATION, getConstants().dsdOperation());
        contentDescriptorsForm.setFields(staticDescriptionItem, staticFinalItem, staticAgency, relatedOperation);

        // Class Descriptors Form
        classDescriptorsForm = new GroupDynamicForm(getConstants().formClassDescriptors());

        // Status Form
        statusForm = new GroupDynamicForm(getConstants().dsdStatus());
        ViewTextItem replacedBy = new ViewTextItem(DataStructureDefinitionDS.REPLACED_BY_VERSION, getConstants().maintainableArtefactReplacedByVersion());
        ViewTextItem replaceTo = new ViewTextItem(DataStructureDefinitionDS.REPLACE_TO_VERSION, getConstants().maintainableArtefactReplaceToVersion());
        ViewTextItem validFrom = new ViewTextItem(DataStructureDefinitionDS.VALID_FROM, getConstants().maintainableArtefactValidFrom());
        ViewTextItem validTo = new ViewTextItem(DataStructureDefinitionDS.VALID_TO, getConstants().maintainableArtefactValidTo());

        ViewTextItem isExternalReference = new ViewTextItem(DataStructureDefinitionDS.IS_EXTERNAL_REFERENCE, getConstants().maintainableArtefactIsExternalReference());
        statusForm.setFields(replacedBy, replaceTo, validFrom, validTo, isExternalReference);

        // Version responsibility
        versionResponsibilityForm = new GroupDynamicForm(getConstants().lifeCycleVersionResponsibility());
        ViewTextItem productionValidationUser = new ViewTextItem(DataStructureDefinitionDS.PRODUCTION_VALIDATION_USER, getConstants().lifeCycleProductionValidationUser());
        ViewTextItem productionValidationDate = new ViewTextItem(DataStructureDefinitionDS.PRODUCTION_VALIDATION_DATE, getConstants().lifeCycleProductionValidationDate());
        ViewTextItem diffusionValidationUser = new ViewTextItem(DataStructureDefinitionDS.DIFFUSION_VALIDATION_USER, getConstants().lifeCycleDiffusionValidationUser());
        ViewTextItem diffusionValidationDate = new ViewTextItem(DataStructureDefinitionDS.DIFFUSION_VALIDATION_DATE, getConstants().lifeCycleDiffusionValidationDate());
        ViewTextItem internalPublicationUser = new ViewTextItem(DataStructureDefinitionDS.INTERNAL_PUBLICATION_USER, getConstants().lifeCycleInternalPublicationUser());
        ViewTextItem internalPublicationDate = new ViewTextItem(DataStructureDefinitionDS.INTERNAL_PUBLICATION_DATE, getConstants().lifeCycleInternalPublicationDate());
        ViewTextItem externalPublicationUser = new ViewTextItem(DataStructureDefinitionDS.EXTERNAL_PUBLICATION_USER, getConstants().lifeCycleExternalPublicationUser());
        ViewTextItem externalPublicationDate = new ViewTextItem(DataStructureDefinitionDS.EXTERNAL_PUBLICATION_DATE, getConstants().lifeCycleExternalPublicationDate());
        versionResponsibilityForm.setFields(productionValidationUser, productionValidationDate, diffusionValidationUser, diffusionValidationDate, internalPublicationUser, internalPublicationDate,
                externalPublicationUser, externalPublicationDate);

        // Visualisation metadata
        visualisationMetadataForm = new GroupDynamicForm(getConstants().dsdVisualisationMetadata());
        ViewTextItem autoOpen = new ViewTextItem(DataStructureDefinitionDS.AUTO_OPEN, getConstants().dsdAutoOpen());
        ViewTextItem showDecimals = new ViewTextItem(DataStructureDefinitionDS.SHOW_DECIMALS, getConstants().dsdShowDecimals());
        DimensionsVisualisationItem dimensionsVisualisationItem = new DimensionsVisualisationItem(DataStructureDefinitionDS.DIMENSIONS_VISUALISATIONS, getConstants().dsdDimensionsVisualisation(),
                false);
        ShowDecimalsPrecisionItem showDecimalsPrecision = new ShowDecimalsPrecisionItem(DataStructureDefinitionDS.SHOW_DECIMALS_PRECISION, getConstants().dsdShowDecimalsPrecision(), false);
        visualisationMetadataForm.setFields(autoOpen, showDecimals, dimensionsVisualisationItem, showDecimalsPrecision);

        // Comments
        commentsForm = new GroupDynamicForm(getConstants().nameableArtefactComments());
        ViewMultiLanguageTextItem comments = new ViewMultiLanguageTextItem(DataStructureDefinitionDS.COMMENTS, getConstants().nameableArtefactComments());
        commentsForm.setFields(comments);

        // Annotations
        annotationsPanel = new AnnotationsPanel(true);

        mainFormLayout.addViewCanvas(identifiersForm);
        mainFormLayout.addViewCanvas(contentDescriptorsForm);
        mainFormLayout.addViewCanvas(classDescriptorsForm);
        mainFormLayout.addViewCanvas(statusForm);
        mainFormLayout.addViewCanvas(versionResponsibilityForm);
        mainFormLayout.addViewCanvas(visualisationMetadataForm);
        mainFormLayout.addViewCanvas(commentsForm);
        mainFormLayout.addViewCanvas(annotationsPanel);
    }

    /**
     * Creates and returns the edition layout
     * 
     * @return
     */
    private void createEditionForm() {
        // Identifiers Form
        identifiersEditionForm = new GroupDynamicForm(getConstants().formIdentifiers());

        RequiredTextItem code = new RequiredTextItem(DataStructureDefinitionDS.CODE, getConstants().identifiableArtefactCode());
        code.setValidators(SemanticIdentifiersUtils.getDsdIdentifierCustomValidator());
        code.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                // CODE cannot be modified if status is INTERNALLY_PUBLISHED or EXTERNALLY_PUBLISHED, or if version is greater than VERSION_INITIAL_VERSION (01.000)
                return org.siemac.metamac.srm.web.client.utils.CommonUtils.canCodeBeEdited(dataStructureDefinitionMetamacDto.getLifeCycle().getProcStatus(),
                        dataStructureDefinitionMetamacDto.getVersionLogic());
            }
        });
        ViewTextItem staticCode = new ViewTextItem(DataStructureDefinitionDS.CODE_VIEW, getConstants().identifiableArtefactCode());
        staticCode.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !org.siemac.metamac.srm.web.client.utils.CommonUtils.canCodeBeEdited(dataStructureDefinitionMetamacDto.getLifeCycle().getProcStatus(),
                        dataStructureDefinitionMetamacDto.getVersionLogic());
            }
        });
        MultiLanguageTextItem nameItem = new MultiLanguageTextItem(DataStructureDefinitionDS.NAME, getConstants().nameableArtefactName());
        nameItem.setRequired(true);
        ViewTextItem staticUriItemEdit = new ViewTextItem(DataStructureDefinitionDS.URI, getConstants().identifiableArtefactUri());
        ViewTextItem staticUrnItemEdit = new ViewTextItem(DataStructureDefinitionDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem staticUrnProviderEditItem = new ViewTextItem(DataStructureDefinitionDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        ViewTextItem staticVersionEdit = new ViewTextItem(DataStructureDefinitionDS.VERSION_LOGIC, getConstants().maintainableArtefactVersionLogic());
        identifiersEditionForm.setFields(code, staticCode, nameItem, staticUriItemEdit, staticUrnItemEdit, staticUrnProviderEditItem, staticVersionEdit);

        // Content Descriptors Form
        contentDescriptorsEditionForm = new GroupDynamicForm(getConstants().formContentDescriptors());
        MultiLanguageTextAreaItem descriptionItem = new MultiLanguageTextAreaItem(DataStructureDefinitionDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        ViewTextItem staticFinalItemEdit = new ViewTextItem(DataStructureDefinitionDS.FINAL, getConstants().maintainableArtefactFinalLogic());
        ViewTextItem staticAgencyEdit = new ViewTextItem(DataStructureDefinitionDS.MAINTAINER, getConstants().maintainableArtefactMaintainer());
        SearchViewTextItem operationItem = createStatisticalOperationItem(DataStructureDefinitionDS.STATISTICAL_OPERATION, getConstants().dsdOperation());
        contentDescriptorsEditionForm.setFields(descriptionItem, staticFinalItemEdit, staticAgencyEdit, operationItem);

        // Class descriptors form
        classDescriptorsEditionForm = new GroupDynamicForm(getConstants().formClassDescriptors());

        // Status Form
        statusEditionForm = new GroupDynamicForm(getConstants().dsdStatus());
        ViewTextItem replacedBy = new ViewTextItem(DataStructureDefinitionDS.REPLACED_BY_VERSION, getConstants().maintainableArtefactReplacedByVersion());
        ViewTextItem replaceTo = new ViewTextItem(DataStructureDefinitionDS.REPLACE_TO_VERSION, getConstants().maintainableArtefactReplaceToVersion());
        ViewTextItem validFrom = new ViewTextItem(DataStructureDefinitionDS.VALID_FROM, getConstants().maintainableArtefactValidFrom());
        ViewTextItem validTo = new ViewTextItem(DataStructureDefinitionDS.VALID_TO, getConstants().maintainableArtefactValidTo());

        ViewTextItem isExternalReference = new ViewTextItem(DataStructureDefinitionDS.IS_EXTERNAL_REFERENCE, getConstants().maintainableArtefactIsExternalReference());
        statusEditionForm.setFields(replacedBy, replaceTo, validFrom, validTo, isExternalReference);

        // Version responsibility
        versionResponsibilityEditionForm = new GroupDynamicForm(getConstants().lifeCycleVersionResponsibility());
        ViewTextItem productionValidationUser = new ViewTextItem(DataStructureDefinitionDS.PRODUCTION_VALIDATION_USER, getConstants().lifeCycleProductionValidationUser());
        ViewTextItem productionValidationDate = new ViewTextItem(DataStructureDefinitionDS.PRODUCTION_VALIDATION_DATE, getConstants().lifeCycleProductionValidationDate());
        ViewTextItem diffusionValidationUser = new ViewTextItem(DataStructureDefinitionDS.DIFFUSION_VALIDATION_USER, getConstants().lifeCycleDiffusionValidationUser());
        ViewTextItem diffusionValidationDate = new ViewTextItem(DataStructureDefinitionDS.DIFFUSION_VALIDATION_DATE, getConstants().lifeCycleDiffusionValidationDate());
        ViewTextItem internalPublicationUser = new ViewTextItem(DataStructureDefinitionDS.INTERNAL_PUBLICATION_USER, getConstants().lifeCycleInternalPublicationUser());
        ViewTextItem internalPublicationDate = new ViewTextItem(DataStructureDefinitionDS.INTERNAL_PUBLICATION_DATE, getConstants().lifeCycleInternalPublicationDate());
        ViewTextItem externalPublicationUser = new ViewTextItem(DataStructureDefinitionDS.EXTERNAL_PUBLICATION_USER, getConstants().lifeCycleExternalPublicationUser());
        ViewTextItem externalPublicationDate = new ViewTextItem(DataStructureDefinitionDS.EXTERNAL_PUBLICATION_DATE, getConstants().lifeCycleExternalPublicationDate());
        versionResponsibilityEditionForm.setFields(productionValidationUser, productionValidationDate, diffusionValidationUser, diffusionValidationDate, internalPublicationUser,
                internalPublicationDate, externalPublicationUser, externalPublicationDate);

        // Visualisation metadata
        visualisationMetadataEditionForm = new GroupDynamicForm(getConstants().dsdVisualisationMetadata());
        BooleanSelectItem autoOpen = new BooleanSelectItem(DataStructureDefinitionDS.AUTO_OPEN, getConstants().dsdAutoOpen());
        SelectItem showDecimals = new SelectItem(DataStructureDefinitionDS.SHOW_DECIMALS, getConstants().dsdShowDecimals());
        showDecimals.setValueMap(org.siemac.metamac.srm.web.dsd.utils.CommonUtils.getDsdShowDecimalsHashMap());
        DimensionsVisualisationItem dimensionsVisualisationItem = new DimensionsVisualisationItem(DataStructureDefinitionDS.DIMENSIONS_VISUALISATIONS, getConstants().dsdDimensionsVisualisation(),
                true);
        ShowDecimalsPrecisionItem showDecimalsPrecision = new ShowDecimalsPrecisionItem(DataStructureDefinitionDS.SHOW_DECIMALS_PRECISION, getConstants().dsdShowDecimalsPrecision(), true);
        visualisationMetadataEditionForm.setFields(autoOpen, showDecimals, dimensionsVisualisationItem, showDecimalsPrecision);

        // Comments
        commentsEditionForm = new GroupDynamicForm(getConstants().nameableArtefactComments());
        MultiLanguageTextAreaItem comments = new MultiLanguageTextAreaItem(DataStructureDefinitionDS.COMMENTS, getConstants().nameableArtefactComments());
        commentsEditionForm.setFields(comments);

        // Annotations
        annotationsEditionPanel = new AnnotationsPanel(false);

        mainFormLayout.addEditionCanvas(identifiersEditionForm);
        mainFormLayout.addEditionCanvas(contentDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(classDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(statusEditionForm);
        mainFormLayout.addEditionCanvas(versionResponsibilityEditionForm);
        mainFormLayout.addEditionCanvas(visualisationMetadataEditionForm);
        mainFormLayout.addEditionCanvas(commentsEditionForm);
        mainFormLayout.addEditionCanvas(annotationsEditionPanel);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setDsd(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        this.dataStructureDefinitionMetamacDto = dataStructureDefinitionMetamacDto;
        this.statisticalOperation = dataStructureDefinitionMetamacDto.getStatisticalOperation();

        // Security
        ProcStatusEnum procStatus = dataStructureDefinitionMetamacDto.getLifeCycle().getProcStatus();
        String operationCode = org.siemac.metamac.srm.web.dsd.utils.CommonUtils.getStatisticalOperationCodeFromDsd(dataStructureDefinitionMetamacDto);
        mainFormLayout.setCanEdit(DsdClientSecurityUtils.canUpdateDsd(procStatus, operationCode));
        mainFormLayout.updatePublishSection(dataStructureDefinitionMetamacDto.getLifeCycle().getProcStatus(), dataStructureDefinitionMetamacDto.getValidTo(), operationCode);
        mainFormLayout.setViewMode();
        categorisationsPanel.updateVisibility(dataStructureDefinitionMetamacDto);

        setDsdViewMode(dataStructureDefinitionMetamacDto);
        setDsdEditionMode(dataStructureDefinitionMetamacDto);

        // Clear errors
        identifiersEditionForm.clearErrors(true);
        contentDescriptorsEditionForm.clearErrors(true);
    }

    @Override
    public void setOperations(GetStatisticalOperationsResult result) {
        if (searchOperationWindow != null) {
            searchOperationWindow.setExternalItems(result.getOperations());
            searchOperationWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getOperations().size(), result.getTotalResults());
        }
    }

    @Override
    public void setCategorisations(List<CategorisationDto> categorisationDtos) {
        categorisationsPanel.setCategorisations(categorisationDtos);
    }

    @Override
    public void setCategorySchemesForCategorisations(GetCategorySchemesResult result) {
        categorisationsPanel.setCategorySchemes(result);
    }

    @Override
    public void setCategoriesForCategorisations(GetCategoriesResult result) {
        categorisationsPanel.setCategories(result);
    }

    private void setDsdViewMode(DataStructureDefinitionMetamacDto dsd) {
        // Identifiers form
        identifiersForm.setValue(DataStructureDefinitionDS.CODE, dsd.getCode());
        identifiersForm.setValue(DataStructureDefinitionDS.NAME, RecordUtils.getInternationalStringRecord(dsd.getName()));
        identifiersForm.setValue(DataStructureDefinitionDS.URI, dsd.getUriProvider());
        identifiersForm.setValue(DataStructureDefinitionDS.URN, dsd.getUrn());
        identifiersForm.setValue(DataStructureDefinitionDS.URN_PROVIDER, dsd.getUrnProvider());
        identifiersForm.setValue(DataStructureDefinitionDS.VERSION_LOGIC, dsd.getVersionLogic());

        // Content Descriptors form
        contentDescriptorsForm.setValue(DataStructureDefinitionDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(dsd.getDescription()));
        contentDescriptorsForm.setValue(DataStructureDefinitionDS.FINAL, dsd.getFinalLogic() != null ? (dsd.getFinalLogic() ? MetamacWebCommon.getConstants().yes() : MetamacWebCommon.getConstants()
                .no()) : StringUtils.EMPTY);
        contentDescriptorsForm.setValue(DataStructureDefinitionDS.MAINTAINER, RelatedResourceUtils.getRelatedResourceName(dsd.getMaintainer()));
        contentDescriptorsForm.setValue(DataStructureDefinitionDS.STATISTICAL_OPERATION, ExternalItemUtils.getExternalItemName(dsd.getStatisticalOperation()));

        // Class descriptors form

        // Status form
        statusForm.setValue(DataStructureDefinitionDS.REPLACED_BY_VERSION, dsd.getReplacedByVersion());
        statusForm.setValue(DataStructureDefinitionDS.REPLACE_TO_VERSION, dsd.getReplaceToVersion());
        statusForm.setValue(DataStructureDefinitionDS.VALID_FROM, dsd.getValidFrom());
        statusForm.setValue(DataStructureDefinitionDS.VALID_TO, dsd.getValidTo());
        statusForm.setValue(DataStructureDefinitionDS.IS_EXTERNAL_REFERENCE, dsd.getIsExternalReference() != null ? (dsd.getIsExternalReference()
                ? MetamacWebCommon.getConstants().yes()
                : MetamacWebCommon.getConstants().no()) : StringUtils.EMPTY);

        // Version responsibility
        versionResponsibilityForm.setValue(DataStructureDefinitionDS.PRODUCTION_VALIDATION_USER, dsd.getLifeCycle().getProductionValidationUser());
        versionResponsibilityForm.setValue(DataStructureDefinitionDS.PRODUCTION_VALIDATION_DATE, dsd.getLifeCycle().getProductionValidationDate());
        versionResponsibilityForm.setValue(DataStructureDefinitionDS.DIFFUSION_VALIDATION_USER, dsd.getLifeCycle().getDiffusionValidationUser());
        versionResponsibilityForm.setValue(DataStructureDefinitionDS.DIFFUSION_VALIDATION_DATE, dsd.getLifeCycle().getDiffusionValidationDate());
        versionResponsibilityForm.setValue(DataStructureDefinitionDS.INTERNAL_PUBLICATION_USER, dsd.getLifeCycle().getInternalPublicationUser());
        versionResponsibilityForm.setValue(DataStructureDefinitionDS.INTERNAL_PUBLICATION_DATE, dsd.getLifeCycle().getInternalPublicationDate());
        versionResponsibilityForm.setValue(DataStructureDefinitionDS.EXTERNAL_PUBLICATION_USER, dsd.getLifeCycle().getExternalPublicationUser());
        versionResponsibilityForm.setValue(DataStructureDefinitionDS.EXTERNAL_PUBLICATION_DATE, dsd.getLifeCycle().getExternalPublicationDate());

        // Visualisation metadata
        visualisationMetadataForm.setValue(DataStructureDefinitionDS.AUTO_OPEN, CommonUtils.getBooleanName(dsd.getAutoOpen()));
        visualisationMetadataForm.setValue(DataStructureDefinitionDS.SHOW_DECIMALS, dsd.getShowDecimals() != null ? dsd.getShowDecimals().toString() : StringUtils.EMPTY);
        ((DimensionsVisualisationItem) visualisationMetadataForm.getItem(DataStructureDefinitionDS.DIMENSIONS_VISUALISATIONS)).setVisualisationDimensions(dsd.getHeadingDimensions(),
                dsd.getStubDimensions());
        ((ShowDecimalsPrecisionItem) visualisationMetadataForm.getItem(DataStructureDefinitionDS.SHOW_DECIMALS_PRECISION)).setMeasureDimensionPrecisions(dsd.getShowDecimalsPrecisions());

        // Comments
        commentsForm.setValue(DataStructureDefinitionDS.COMMENTS, RecordUtils.getInternationalStringRecord(dsd.getComment()));

        // Annotations
        annotationsPanel.setAnnotations(dsd.getAnnotations());
    }

    private void setDsdEditionMode(DataStructureDefinitionMetamacDto dsd) {
        // Identifiers form
        identifiersEditionForm.setValue(DataStructureDefinitionDS.CODE, dsd.getCode());
        identifiersEditionForm.setValue(DataStructureDefinitionDS.CODE_VIEW, dsd.getCode());
        identifiersEditionForm.setValue(DataStructureDefinitionDS.NAME, RecordUtils.getInternationalStringRecord(dsd.getName()));
        identifiersEditionForm.setValue(DataStructureDefinitionDS.URI, dsd.getUriProvider());
        identifiersEditionForm.setValue(DataStructureDefinitionDS.URN, dsd.getUrn());
        identifiersEditionForm.setValue(DataStructureDefinitionDS.URN_PROVIDER, dsd.getUrnProvider());
        identifiersEditionForm.setValue(DataStructureDefinitionDS.VERSION_LOGIC, dsd.getVersionLogic());

        // Content Descriptors form
        contentDescriptorsEditionForm.setValue(DataStructureDefinitionDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(dsd.getDescription()));
        contentDescriptorsEditionForm.setValue(DataStructureDefinitionDS.FINAL, dsd.getFinalLogic() != null ? (dsd.getFinalLogic() ? MetamacWebCommon.getConstants().yes() : MetamacWebCommon
                .getConstants().no()) : StringUtils.EMPTY);
        contentDescriptorsEditionForm.setValue(DataStructureDefinitionDS.MAINTAINER, RelatedResourceUtils.getRelatedResourceName(dsd.getMaintainer()));
        contentDescriptorsEditionForm.setValue(DataStructureDefinitionDS.STATISTICAL_OPERATION, ExternalItemUtils.getExternalItemName(dsd.getStatisticalOperation()));

        // Class descriptors form

        // Status form
        statusEditionForm.setValue(DataStructureDefinitionDS.VALID_FROM, dsd.getValidFrom());
        statusEditionForm.setValue(DataStructureDefinitionDS.VALID_TO, dsd.getValidTo());
        statusEditionForm.setValue(DataStructureDefinitionDS.IS_EXTERNAL_REFERENCE, dsd.getIsExternalReference() != null ? (dsd.getIsExternalReference()
                ? MetamacWebCommon.getConstants().yes()
                : MetamacWebCommon.getConstants().no()) : StringUtils.EMPTY);

        // Version responsibility
        versionResponsibilityEditionForm.setValue(DataStructureDefinitionDS.PRODUCTION_VALIDATION_USER, dsd.getLifeCycle().getProductionValidationUser());
        versionResponsibilityEditionForm.setValue(DataStructureDefinitionDS.PRODUCTION_VALIDATION_DATE, dsd.getLifeCycle().getProductionValidationDate());
        versionResponsibilityEditionForm.setValue(DataStructureDefinitionDS.DIFFUSION_VALIDATION_USER, dsd.getLifeCycle().getDiffusionValidationUser());
        versionResponsibilityEditionForm.setValue(DataStructureDefinitionDS.DIFFUSION_VALIDATION_DATE, dsd.getLifeCycle().getDiffusionValidationDate());
        versionResponsibilityEditionForm.setValue(DataStructureDefinitionDS.INTERNAL_PUBLICATION_USER, dsd.getLifeCycle().getInternalPublicationUser());
        versionResponsibilityEditionForm.setValue(DataStructureDefinitionDS.INTERNAL_PUBLICATION_DATE, dsd.getLifeCycle().getInternalPublicationDate());
        versionResponsibilityEditionForm.setValue(DataStructureDefinitionDS.EXTERNAL_PUBLICATION_USER, dsd.getLifeCycle().getExternalPublicationUser());
        versionResponsibilityEditionForm.setValue(DataStructureDefinitionDS.EXTERNAL_PUBLICATION_DATE, dsd.getLifeCycle().getExternalPublicationDate());

        // Visualisation metadata
        ((BooleanSelectItem) visualisationMetadataEditionForm.getItem(DataStructureDefinitionDS.AUTO_OPEN)).setBooleanValue(dsd.getAutoOpen());
        visualisationMetadataEditionForm.setValue(DataStructureDefinitionDS.SHOW_DECIMALS, dsd.getShowDecimals() != null ? dsd.getShowDecimals().toString() : StringUtils.EMPTY);
        ((DimensionsVisualisationItem) visualisationMetadataEditionForm.getItem(DataStructureDefinitionDS.DIMENSIONS_VISUALISATIONS)).setVisualisationDimensions(dsd.getHeadingDimensions(),
                dsd.getStubDimensions());
        ((ShowDecimalsPrecisionItem) visualisationMetadataEditionForm.getItem(DataStructureDefinitionDS.SHOW_DECIMALS_PRECISION)).setMeasureDimensionPrecisions(dsd.getShowDecimalsPrecisions());

        // Comments
        commentsEditionForm.setValue(DataStructureDefinitionDS.COMMENTS, RecordUtils.getInternationalStringRecord(dsd.getComment()));

        // Annotations
        annotationsEditionPanel.setAnnotations(dsd.getAnnotations());
    }

    @Override
    public HasClickHandlers getSave() {
        return mainFormLayout.getSave();
    }

    @Override
    public DataStructureDefinitionMetamacDto getDataStructureDefinitionDto() {
        // Identifiers form
        dataStructureDefinitionMetamacDto.setCode(identifiersEditionForm.getValueAsString(DataStructureDefinitionDS.CODE));
        dataStructureDefinitionMetamacDto.setName((InternationalStringDto) identifiersEditionForm.getValue(DataStructureDefinitionDS.NAME));

        // Content Descriptors form
        dataStructureDefinitionMetamacDto.setDescription((InternationalStringDto) contentDescriptorsEditionForm.getValue(DataStructureDefinitionDS.DESCRIPTION));
        dataStructureDefinitionMetamacDto.setStatisticalOperation(ExternalItemUtils.removeTitle(statisticalOperation));

        // Class descriptors form

        // Visualisation metadata
        dataStructureDefinitionMetamacDto.setAutoOpen(((BooleanSelectItem) visualisationMetadataEditionForm.getItem(DataStructureDefinitionDS.AUTO_OPEN)).getBooleanValue());
        dataStructureDefinitionMetamacDto.setShowDecimals(!StringUtils.isBlank(visualisationMetadataEditionForm.getValueAsString(DataStructureDefinitionDS.SHOW_DECIMALS)) ? Integer
                .valueOf(visualisationMetadataEditionForm.getValueAsString(DataStructureDefinitionDS.SHOW_DECIMALS)) : null);
        dataStructureDefinitionMetamacDto.getHeadingDimensions().clear();
        dataStructureDefinitionMetamacDto.getHeadingDimensions().addAll(
                ((DimensionsVisualisationItem) visualisationMetadataEditionForm.getItem(DataStructureDefinitionDS.DIMENSIONS_VISUALISATIONS)).getHeadingDimensions());
        dataStructureDefinitionMetamacDto.getStubDimensions().clear();
        dataStructureDefinitionMetamacDto.getStubDimensions().addAll(
                ((DimensionsVisualisationItem) visualisationMetadataEditionForm.getItem(DataStructureDefinitionDS.DIMENSIONS_VISUALISATIONS)).getStubDimensions());
        dataStructureDefinitionMetamacDto.getShowDecimalsPrecisions().clear();
        dataStructureDefinitionMetamacDto.getShowDecimalsPrecisions().addAll(
                ((ShowDecimalsPrecisionItem) visualisationMetadataEditionForm.getItem(DataStructureDefinitionDS.SHOW_DECIMALS_PRECISION)).getMeasureDimensionPrecisionDtos());

        // Comments
        dataStructureDefinitionMetamacDto.setComment((InternationalStringDto) commentsEditionForm.getValue(DataStructureDefinitionDS.COMMENTS));

        // Annotations
        dataStructureDefinitionMetamacDto.getAnnotations().clear();
        dataStructureDefinitionMetamacDto.getAnnotations().addAll(annotationsEditionPanel.getAnnotations());
        return dataStructureDefinitionMetamacDto;
    }

    @Override
    public void onDsdSaved(DataStructureDefinitionMetamacDto dsd) {
        setDsd(dsd);
        mainFormLayout.setViewMode();
    }

    @Override
    public void setDsdVersions(List<DataStructureDefinitionMetamacDto> dataStructureDefinitionMetamacDtos) {
        versionsSectionStack.setDataStructureDefinitions(dataStructureDefinitionMetamacDtos);
        versionsSectionStack.selectDataStructureDefinition(dataStructureDefinitionMetamacDto);
    }

    // Visualisation metadata

    @Override
    public void setDimensionsForStubAndHeading(List<DimensionComponentDto> dimensionComponentDtos) {
        List<RelatedResourceDto> dimensions = RelatedResourceUtils.getDimensionComponentDtosAsRelatedResourceDtos(dimensionComponentDtos);
        ((DimensionsVisualisationItem) visualisationMetadataEditionForm.getItem(DataStructureDefinitionDS.DIMENSIONS_VISUALISATIONS)).setDimensions(dimensions);
    }

    @Override
    public void setConceptsForShowDecimalsPrecision(ConceptSchemeMetamacDto conceptSchemeMetamacDto, List<ItemHierarchyDto> concepts) {
        ((ShowDecimalsPrecisionItem) visualisationMetadataForm.getItem(DataStructureDefinitionDS.SHOW_DECIMALS_PRECISION)).setConcepts(conceptSchemeMetamacDto, concepts);
        ((ShowDecimalsPrecisionItem) visualisationMetadataEditionForm.getItem(DataStructureDefinitionDS.SHOW_DECIMALS_PRECISION)).setConcepts(conceptSchemeMetamacDto, concepts);
    }

    private void setEditionMode() {
        mainFormLayout.setEditionMode();
    }

    private SearchViewTextItem createStatisticalOperationItem(String name, String title) {
        SearchViewTextItem operationItem = new SearchViewTextItem(name, title);
        operationItem.setRequired(true);
        operationItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                final int OPERATION_FIRST_RESULT = 0;
                final int OPERATION_MAX_RESULTS = 16;
                searchOperationWindow = new SearchExternalItemWindow(getConstants().dsdSearchOperations(), OPERATION_MAX_RESULTS, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        getUiHandlers().retrieveStatisticalOperations(firstResult, maxResults, searchOperationWindow.getSearchCriteria());
                    }
                });
                getUiHandlers().retrieveStatisticalOperations(OPERATION_FIRST_RESULT, OPERATION_MAX_RESULTS, null);
                searchOperationWindow.getListGrid().setSelectionType(SelectionStyle.SINGLE); // Only one statistical operation can be selected
                searchOperationWindow.getExternalListGridItem().setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String code) {
                        getUiHandlers().retrieveStatisticalOperations(firstResult, maxResults, code);
                    }
                });
                searchOperationWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        statisticalOperation = searchOperationWindow.getSelectedExternalItem();
                        searchOperationWindow.destroy();
                        contentDescriptorsEditionForm.setValue(DataStructureDefinitionDS.STATISTICAL_OPERATION, ExternalItemUtils.getExternalItemName(statisticalOperation));
                        contentDescriptorsEditionForm.validate(false);
                    }
                });
            }
        });
        return operationItem;
    }
}
