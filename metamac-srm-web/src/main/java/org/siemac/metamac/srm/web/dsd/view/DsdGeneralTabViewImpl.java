package org.siemac.metamac.srm.web.dsd.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.concept.domain.shared.ConceptMetamacVisualisationResult;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacBasicDto;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.client.widgets.AnnotationsPanel;
import org.siemac.metamac.srm.web.client.widgets.BooleanSelectItem;
import org.siemac.metamac.srm.web.client.widgets.ConfirmationWindow;
import org.siemac.metamac.srm.web.client.widgets.VersionWindow;
import org.siemac.metamac.srm.web.dsd.model.ds.DataStructureDefinitionDS;
import org.siemac.metamac.srm.web.dsd.presenter.DsdGeneralTabPresenter;
import org.siemac.metamac.srm.web.dsd.utils.DsdsFormUtils;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdGeneralTabUiHandlers;
import org.siemac.metamac.srm.web.dsd.widgets.DimensionsVisualisationItem;
import org.siemac.metamac.srm.web.dsd.widgets.DsdDimensionCodesVisualisationItem;
import org.siemac.metamac.srm.web.dsd.widgets.DsdMainFormLayout;
import org.siemac.metamac.srm.web.dsd.widgets.ShowDecimalsPrecisionItem;
import org.siemac.metamac.srm.web.shared.concept.GetStatisticalOperationsResult;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdsResult;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.utils.ExternalItemUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
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

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DimensionComponentDto;
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
import com.smartgwt.client.widgets.layout.VLayout;

public class DsdGeneralTabViewImpl extends ViewWithUiHandlers<DsdGeneralTabUiHandlers> implements DsdGeneralTabPresenter.DsdGeneralTabView {

    private VLayout                           panel;

    private DsdMainFormLayout                 mainFormLayout;

    // VIEW FORM

    private GroupDynamicForm                  identifiersForm;
    private GroupDynamicForm                  contentDescriptorsForm;
    private GroupDynamicForm                  classDescriptorsForm;
    private GroupDynamicForm                  productionDescriptorsForm;
    private GroupDynamicForm                  diffusionDescriptorsForm;
    private GroupDynamicForm                  versionResponsibilityForm;
    private GroupDynamicForm                  visualisationMetadataForm;
    private GroupDynamicForm                  commentsForm;
    private AnnotationsPanel                  annotationsPanel;

    // EDITION FORM

    private GroupDynamicForm                  identifiersEditionForm;
    private GroupDynamicForm                  contentDescriptorsEditionForm;
    private GroupDynamicForm                  classDescriptorsEditionForm;
    private GroupDynamicForm                  productionDescriptorsEditionForm;
    private GroupDynamicForm                  diffusionDescriptorsEditionForm;
    private GroupDynamicForm                  versionResponsibilityEditionForm;
    private GroupDynamicForm                  visualisationMetadataEditionForm;
    private GroupDynamicForm                  commentsEditionForm;
    private AnnotationsPanel                  annotationsEditionPanel;

    private SearchExternalItemWindow          searchOperationWindow;

    private DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto;
    private ExternalItemDto                   statisticalOperation;

    @Inject
    public DsdGeneralTabViewImpl() {
        super();
        panel = new VLayout();

        // DSD

        mainFormLayout = new DsdMainFormLayout();
        bindMainFormLayoutEvents();
        createViewForm();
        createEditionForm();

        panel.addMember(mainFormLayout);
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
                if (org.siemac.metamac.srm.web.client.utils.CommonUtils.isItemSchemePublished(status)) {
                    // If the scheme is published, create a temporal version
                    getUiHandlers().createTemporalVersion(dataStructureDefinitionMetamacDto.getUrn());
                } else {
                    // Default behavior
                    startDsdEdition();
                }
            }
        });

        // Save
        mainFormLayout.getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (identifiersEditionForm.validate(false) && contentDescriptorsEditionForm.validate(false) && classDescriptorsEditionForm.validate(false)
                        && diffusionDescriptorsEditionForm.validate(false) && visualisationMetadataEditionForm.validate(false)) {
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
                publishDsdInternally();
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
                versionDsd();
            }
        });
        mainFormLayout.getCancelValidity().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().cancelValidity(dataStructureDefinitionMetamacDto.getUrn());
            }
        });
        mainFormLayout.getVersionSdmxResource().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                versionDsd();
            }
        });
        mainFormLayout.getExport().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().exportDsd(dataStructureDefinitionMetamacDto.getUrn());
            }
        });
        mainFormLayout.getCopy().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().copyDsd(dataStructureDefinitionMetamacDto.getUrn());
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
        contentDescriptorsForm.setFields(staticDescriptionItem, staticFinalItem);

        // Class Descriptors Form
        classDescriptorsForm = new GroupDynamicForm(getConstants().formClassDescriptors());
        ViewTextItem relatedOperation = new ViewTextItem(DataStructureDefinitionDS.STATISTICAL_OPERATION, getConstants().dsdOperation());
        classDescriptorsForm.setFields(relatedOperation);

        // Production descriptors form
        productionDescriptorsForm = new GroupDynamicForm(getConstants().formProductionDescriptors());
        ViewTextItem maintainer = new ViewTextItem(DataStructureDefinitionDS.MAINTAINER, getConstants().maintainableArtefactMaintainer());
        ViewTextItem procStatus = new ViewTextItem(DataStructureDefinitionDS.PROC_STATUS, getConstants().lifeCycleProcStatus());
        productionDescriptorsForm.setFields(maintainer, procStatus);

        // Diffusion descriptors Form
        diffusionDescriptorsForm = new GroupDynamicForm(getConstants().formDiffusionDescriptors());
        ViewTextItem replacedBy = new ViewTextItem(DataStructureDefinitionDS.REPLACED_BY_VERSION, getConstants().maintainableArtefactReplacedByVersion());
        ViewTextItem replaceTo = new ViewTextItem(DataStructureDefinitionDS.REPLACE_TO_VERSION, getConstants().maintainableArtefactReplaceToVersion());
        ViewTextItem validFrom = new ViewTextItem(DataStructureDefinitionDS.VALID_FROM, getConstants().maintainableArtefactValidFrom());
        ViewTextItem validTo = new ViewTextItem(DataStructureDefinitionDS.VALID_TO, getConstants().maintainableArtefactValidTo());
        ViewTextItem isExternalReference = new ViewTextItem(DataStructureDefinitionDS.IS_EXTERNAL_REFERENCE, getConstants().maintainableArtefactIsExternalReference());
        diffusionDescriptorsForm.setFields(replacedBy, replaceTo, validFrom, validTo, isExternalReference);

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
        DsdDimensionCodesVisualisationItem dimensionCodesVisualisationItem = new DsdDimensionCodesVisualisationItem(DataStructureDefinitionDS.DIMENSION_CODES_VISUALISATION, getConstants()
                .dsdDimensionCodesVisualisation(), false);
        visualisationMetadataForm.setFields(autoOpen, showDecimals, dimensionsVisualisationItem, showDecimalsPrecision, dimensionCodesVisualisationItem);

        // Comments
        commentsForm = new GroupDynamicForm(getConstants().nameableArtefactComments());
        ViewMultiLanguageTextItem comments = new ViewMultiLanguageTextItem(DataStructureDefinitionDS.COMMENTS, getConstants().nameableArtefactComments());
        commentsForm.setFields(comments);

        // Annotations
        annotationsPanel = new AnnotationsPanel(true);

        mainFormLayout.addViewCanvas(identifiersForm);
        mainFormLayout.addViewCanvas(contentDescriptorsForm);
        mainFormLayout.addViewCanvas(classDescriptorsForm);
        mainFormLayout.addViewCanvas(productionDescriptorsForm);
        mainFormLayout.addViewCanvas(diffusionDescriptorsForm);
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
        code.setShowIfCondition(getCodeFormItemIfFunction());

        ViewTextItem staticCode = new ViewTextItem(DataStructureDefinitionDS.CODE_VIEW, getConstants().identifiableArtefactCode());
        staticCode.setShowIfCondition(getStaticCodeFormItemIfFunction());

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
        contentDescriptorsEditionForm.setFields(descriptionItem, staticFinalItemEdit);

        // Class descriptors form
        classDescriptorsEditionForm = new GroupDynamicForm(getConstants().formClassDescriptors());
        SearchViewTextItem operationItem = createStatisticalOperationItem(DataStructureDefinitionDS.STATISTICAL_OPERATION, getConstants().dsdOperation());
        classDescriptorsEditionForm.setFields(operationItem);

        // Production descriptors form
        productionDescriptorsEditionForm = new GroupDynamicForm(getConstants().formProductionDescriptors());
        ViewTextItem maintainer = new ViewTextItem(DataStructureDefinitionDS.MAINTAINER, getConstants().maintainableArtefactMaintainer());
        ViewTextItem procStatus = new ViewTextItem(DataStructureDefinitionDS.PROC_STATUS, getConstants().lifeCycleProcStatus());
        productionDescriptorsEditionForm.setFields(maintainer, procStatus);

        // Diffusion descriptors Form
        diffusionDescriptorsEditionForm = new GroupDynamicForm(getConstants().formDiffusionDescriptors());
        ViewTextItem replacedBy = new ViewTextItem(DataStructureDefinitionDS.REPLACED_BY_VERSION, getConstants().maintainableArtefactReplacedByVersion());
        ViewTextItem replaceTo = new ViewTextItem(DataStructureDefinitionDS.REPLACE_TO_VERSION, getConstants().maintainableArtefactReplaceToVersion());
        ViewTextItem validFrom = new ViewTextItem(DataStructureDefinitionDS.VALID_FROM, getConstants().maintainableArtefactValidFrom());
        ViewTextItem validTo = new ViewTextItem(DataStructureDefinitionDS.VALID_TO, getConstants().maintainableArtefactValidTo());
        ViewTextItem isExternalReference = new ViewTextItem(DataStructureDefinitionDS.IS_EXTERNAL_REFERENCE, getConstants().maintainableArtefactIsExternalReference());
        diffusionDescriptorsEditionForm.setFields(replacedBy, replaceTo, validFrom, validTo, isExternalReference);

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
        DsdDimensionCodesVisualisationItem dimensionCodesVisualisationItem = new DsdDimensionCodesVisualisationItem(DataStructureDefinitionDS.DIMENSION_CODES_VISUALISATION, getConstants()
                .dsdDimensionCodesVisualisation(), true);
        visualisationMetadataEditionForm.setFields(autoOpen, showDecimals, dimensionsVisualisationItem, showDecimalsPrecision, dimensionCodesVisualisationItem);

        // Comments
        commentsEditionForm = new GroupDynamicForm(getConstants().nameableArtefactComments());
        MultiLanguageTextAreaItem comments = new MultiLanguageTextAreaItem(DataStructureDefinitionDS.COMMENTS, getConstants().nameableArtefactComments());
        commentsEditionForm.setFields(comments);

        // Annotations
        annotationsEditionPanel = new AnnotationsPanel(false);

        mainFormLayout.addEditionCanvas(identifiersEditionForm);
        mainFormLayout.addEditionCanvas(contentDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(classDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(productionDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(diffusionDescriptorsEditionForm);
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
    public void setDsd(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto, List<DimensionComponentDto> dimensionComponentDtos) {
        this.dataStructureDefinitionMetamacDto = dataStructureDefinitionMetamacDto;
        this.statisticalOperation = dataStructureDefinitionMetamacDto.getStatisticalOperation();

        // Security
        mainFormLayout.setDsd(dataStructureDefinitionMetamacDto);
        mainFormLayout.setViewMode();

        setDsdViewMode(dataStructureDefinitionMetamacDto, dimensionComponentDtos);
        setDsdEditionMode(dataStructureDefinitionMetamacDto, dimensionComponentDtos);

        // Clear errors
        identifiersEditionForm.clearErrors(true);
        contentDescriptorsEditionForm.clearErrors(true);
    }

    @Override
    public void startDsdEdition() {
        mainFormLayout.setEditionMode();
    }

    @Override
    public void setOperations(GetStatisticalOperationsResult result) {
        if (searchOperationWindow != null) {
            searchOperationWindow.setExternalItems(result.getOperations());
            searchOperationWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getOperations().size(), result.getTotalResults());
        }
    }

    private void setDsdViewMode(DataStructureDefinitionMetamacDto dsd, List<DimensionComponentDto> dimensionComponentDtos) {
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

        // Class descriptors form
        classDescriptorsForm.setValue(DataStructureDefinitionDS.STATISTICAL_OPERATION, ExternalItemUtils.getExternalItemName(dsd.getStatisticalOperation()));

        // Production descriptors form
        productionDescriptorsForm.setValue(DataStructureDefinitionDS.MAINTAINER, RelatedResourceUtils.getRelatedResourceName(dsd.getMaintainer()));
        productionDescriptorsForm.setValue(DataStructureDefinitionDS.PROC_STATUS, CommonUtils.getProcStatusName(dsd.getLifeCycle().getProcStatus()));

        // Status form
        diffusionDescriptorsForm.setValue(DataStructureDefinitionDS.REPLACED_BY_VERSION, dsd.getReplacedByVersion());
        diffusionDescriptorsForm.setValue(DataStructureDefinitionDS.REPLACE_TO_VERSION, dsd.getReplaceToVersion());
        diffusionDescriptorsForm.setValue(DataStructureDefinitionDS.VALID_FROM, dsd.getValidFrom());
        diffusionDescriptorsForm.setValue(DataStructureDefinitionDS.VALID_TO, dsd.getValidTo());
        diffusionDescriptorsForm.setValue(DataStructureDefinitionDS.IS_EXTERNAL_REFERENCE, dsd.getIsExternalReference() != null ? (dsd.getIsExternalReference()
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
        // DIMENSION_CODES_VISUALISATION is set in set setDimensionsAndCandidateVisualisations

        // Comments
        commentsForm.setValue(DataStructureDefinitionDS.COMMENTS, RecordUtils.getInternationalStringRecord(dsd.getComment()));

        // Annotations
        annotationsPanel.setAnnotations(dsd.getAnnotations(), dataStructureDefinitionMetamacDto);
    }

    private void setDsdEditionMode(DataStructureDefinitionMetamacDto dsd, List<DimensionComponentDto> dimensionComponentDtos) {
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

        // Class descriptors form
        classDescriptorsEditionForm.setValue(DataStructureDefinitionDS.STATISTICAL_OPERATION, ExternalItemUtils.getExternalItemName(dsd.getStatisticalOperation()));

        // Production descriptors form
        productionDescriptorsEditionForm.setValue(DataStructureDefinitionDS.MAINTAINER, RelatedResourceUtils.getRelatedResourceName(dsd.getMaintainer()));
        productionDescriptorsEditionForm.setValue(DataStructureDefinitionDS.PROC_STATUS, CommonUtils.getProcStatusName(dsd.getLifeCycle().getProcStatus()));

        // Status form
        diffusionDescriptorsEditionForm.setValue(DataStructureDefinitionDS.VALID_FROM, dsd.getValidFrom());
        diffusionDescriptorsEditionForm.setValue(DataStructureDefinitionDS.VALID_TO, dsd.getValidTo());
        diffusionDescriptorsEditionForm.setValue(DataStructureDefinitionDS.IS_EXTERNAL_REFERENCE, dsd.getIsExternalReference() != null ? (dsd.getIsExternalReference() ? MetamacWebCommon
                .getConstants().yes() : MetamacWebCommon.getConstants().no()) : StringUtils.EMPTY);

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
        setDimensionsForStubAndHeading(dimensionComponentDtos);

        ((ShowDecimalsPrecisionItem) visualisationMetadataEditionForm.getItem(DataStructureDefinitionDS.SHOW_DECIMALS_PRECISION)).setMeasureDimensionPrecisions(dsd.getShowDecimalsPrecisions());

        // DIMENSION_CODES_VISUALISATION is set in set setDimensionsAndCandidateVisualisations

        // Comments
        commentsEditionForm.setValue(DataStructureDefinitionDS.COMMENTS, RecordUtils.getInternationalStringRecord(dsd.getComment()));

        // Annotations
        annotationsEditionPanel.setAnnotations(dsd.getAnnotations(), dataStructureDefinitionMetamacDto);
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

        // Class descriptors form
        dataStructureDefinitionMetamacDto.setStatisticalOperation(ExternalItemUtils.removeTitle(statisticalOperation));

        // Production descriptors form

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
        dataStructureDefinitionMetamacDto.getDimensionVisualisationInfos().clear();
        dataStructureDefinitionMetamacDto.getDimensionVisualisationInfos().addAll(
                ((DsdDimensionCodesVisualisationItem) visualisationMetadataEditionForm.getItem(DataStructureDefinitionDS.DIMENSION_CODES_VISUALISATION)).getDimensionVisualisationInfoDtos());

        // Comments
        dataStructureDefinitionMetamacDto.setComment((InternationalStringDto) commentsEditionForm.getValue(DataStructureDefinitionDS.COMMENTS));

        // Annotations
        dataStructureDefinitionMetamacDto.getAnnotations().clear();
        dataStructureDefinitionMetamacDto.getAnnotations().addAll(annotationsEditionPanel.getAnnotations());
        return dataStructureDefinitionMetamacDto;
    }

    private void publishDsdInternally() {
        if (org.siemac.metamac.srm.web.client.utils.CommonUtils.isDefaultMaintainer(dataStructureDefinitionMetamacDto.getMaintainer())) {
            getUiHandlers().publishInternally(dataStructureDefinitionMetamacDto, null);
        } else {
            // If the DSD is imported, ask the user if this resource should be the latest one.
            // If there were another DSD marked as final, find it, and inform the user that the DSD to publish will replace the latest one.
            getUiHandlers().retrieveLatestDsd(dataStructureDefinitionMetamacDto); // Publication will be done in setLatestDsdForInternalPublication method
        }
    }

    @Override
    public void setLatestDsdForInternalPublication(GetDsdsResult result) {
        if (result.getDsdDtos().isEmpty()) {
            getUiHandlers().publishInternally(dataStructureDefinitionMetamacDto, null);
        } else {
            // If there were other version marked as the latest, ask the user what to do
            DataStructureDefinitionMetamacBasicDto latest = result.getDsdDtos().get(0);
            ConfirmationWindow confirmationWindow = new ConfirmationWindow(getConstants().lifeCyclePublishInternally(), getMessages().dsdShouldBeMarkAsTheLatest(latest.getVersionLogic()));
            confirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    // DSD will be the latest
                    getUiHandlers().publishInternally(dataStructureDefinitionMetamacDto, true);
                }
            });
            confirmationWindow.getNoButton().addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    // DSD WON'T be the latest
                    getUiHandlers().publishInternally(dataStructureDefinitionMetamacDto, false);
                }
            });
        }
    }

    private void versionDsd() {
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

    // Visualisation metadata

    @Override
    public void setDimensionsAndCandidateVisualisations(List<DimensionComponentDto> dimensionComponentDtos, Map<String, List<RelatedResourceDto>> candidateOrderVisualisations,
            Map<String, List<RelatedResourceDto>> candidateOpennessLevelVisualisations) {
        ((DsdDimensionCodesVisualisationItem) visualisationMetadataForm.getItem(DataStructureDefinitionDS.DIMENSION_CODES_VISUALISATION)).setDimensionsAndCandidateVisualisations(
                dataStructureDefinitionMetamacDto, dimensionComponentDtos, candidateOrderVisualisations, candidateOpennessLevelVisualisations);
        ((DsdDimensionCodesVisualisationItem) visualisationMetadataEditionForm.getItem(DataStructureDefinitionDS.DIMENSION_CODES_VISUALISATION)).setDimensionsAndCandidateVisualisations(
                dataStructureDefinitionMetamacDto, dimensionComponentDtos, candidateOrderVisualisations, candidateOpennessLevelVisualisations);
    }

    private void setDimensionsForStubAndHeading(List<DimensionComponentDto> dimensionComponentDtos) {
        List<RelatedResourceDto> dimensions = RelatedResourceUtils.getDimensionComponentDtosAsRelatedResourceDtos(dimensionComponentDtos);
        ((DimensionsVisualisationItem) visualisationMetadataEditionForm.getItem(DataStructureDefinitionDS.DIMENSIONS_VISUALISATIONS)).setDimensions(dimensions);
        visualisationMetadataEditionForm.markForRedraw();
    }

    @Override
    public void setConceptsForShowDecimalsPrecision(ConceptSchemeMetamacDto conceptSchemeMetamacDto, List<ConceptMetamacVisualisationResult> concepts) {
        // FIXME ((ShowDecimalsPrecisionItem) visualisationMetadataForm.getItem(DataStructureDefinitionDS.SHOW_DECIMALS_PRECISION)).setConcepts(conceptSchemeMetamacDto, concepts);
        // FIXME ((ShowDecimalsPrecisionItem) visualisationMetadataEditionForm.getItem(DataStructureDefinitionDS.SHOW_DECIMALS_PRECISION)).setConcepts(conceptSchemeMetamacDto, concepts);
        visualisationMetadataForm.markForRedraw();
        visualisationMetadataEditionForm.markForRedraw();
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
                        classDescriptorsEditionForm.setValue(DataStructureDefinitionDS.STATISTICAL_OPERATION, ExternalItemUtils.getExternalItemName(statisticalOperation));
                        classDescriptorsEditionForm.validate(false);
                    }
                });
            }
        });
        return operationItem;
    }

    // ------------------------------------------------------------------------------------------------------------
    // FORM ITEM IF FUNCTIONS
    // ------------------------------------------------------------------------------------------------------------

    // CODE

    private FormItemIfFunction getCodeFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return DsdsFormUtils.canDsdCodeBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }

    private FormItemIfFunction getStaticCodeFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !DsdsFormUtils.canDsdCodeBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }
}
