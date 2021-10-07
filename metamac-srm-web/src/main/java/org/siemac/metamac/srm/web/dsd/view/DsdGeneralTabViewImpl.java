package org.siemac.metamac.srm.web.dsd.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.concept.domain.shared.ConceptMetamacVisualisationResult;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacBasicDto;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.StreamMessageStatusEnum;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.enums.ExportDetailEnum;
import org.siemac.metamac.srm.web.client.enums.ExportReferencesEnum;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;
import org.siemac.metamac.srm.web.client.utils.RequiredFieldUtils;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.client.widgets.AnnotationsPanel;
import org.siemac.metamac.srm.web.client.widgets.ConfirmationWindow;
import org.siemac.metamac.srm.web.client.widgets.CopyResourceWindow;
import org.siemac.metamac.srm.web.client.widgets.RelatedResourceLinkItem;
import org.siemac.metamac.srm.web.client.widgets.SearchStatisticalOperationLinkItem;
import org.siemac.metamac.srm.web.client.widgets.VersionWindow;
import org.siemac.metamac.srm.web.dsd.model.ds.DataStructureDefinitionDS;
import org.siemac.metamac.srm.web.dsd.presenter.DsdGeneralTabPresenter;
import org.siemac.metamac.srm.web.dsd.utils.DsdsFormUtils;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdGeneralTabUiHandlers;
import org.siemac.metamac.srm.web.dsd.widgets.DimensionsVisualisationItem;
import org.siemac.metamac.srm.web.dsd.widgets.DsdDimensionCodesVisualisationItem;
import org.siemac.metamac.srm.web.dsd.widgets.DsdMainFormLayout;
import org.siemac.metamac.srm.web.dsd.widgets.ExportSdmxResourceWindow;
import org.siemac.metamac.srm.web.dsd.widgets.ShowDecimalsPrecisionItem;
import org.siemac.metamac.srm.web.shared.CommonSharedUtils;
import org.siemac.metamac.srm.web.shared.concept.GetStatisticalOperationsResult;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdsResult;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.utils.BooleanWebUtils;
import org.siemac.metamac.web.common.client.utils.CustomRequiredValidator;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.BooleanSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ExternalItemLinkItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageRichTextEditorItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;
import org.siemac.metamac.web.common.client.widgets.handlers.CustomLinkItemNavigationClickHandler;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DimensionComponentDto;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.HasClickHandlers;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.layout.VLayout;

public class DsdGeneralTabViewImpl extends ViewWithUiHandlers<DsdGeneralTabUiHandlers> implements DsdGeneralTabPresenter.DsdGeneralTabView {

    private final VLayout panel;

    private final DsdMainFormLayout mainFormLayout;

    // VIEW FORM

    private GroupDynamicForm identifiersForm;
    private GroupDynamicForm contentDescriptorsForm;
    private GroupDynamicForm classDescriptorsForm;
    private GroupDynamicForm productionDescriptorsForm;
    private GroupDynamicForm diffusionDescriptorsForm;
    private GroupDynamicForm versionResponsibilityForm;
    private GroupDynamicForm visualisationMetadataForm;
    private GroupDynamicForm commentsForm;
    private AnnotationsPanel annotationsPanel;

    // EDITION FORM

    private GroupDynamicForm identifiersEditionForm;
    private GroupDynamicForm contentDescriptorsEditionForm;
    private GroupDynamicForm classDescriptorsEditionForm;
    private GroupDynamicForm productionDescriptorsEditionForm;
    private GroupDynamicForm diffusionDescriptorsEditionForm;
    private GroupDynamicForm versionResponsibilityEditionForm;
    private GroupDynamicForm visualisationMetadataEditionForm;
    private GroupDynamicForm commentsEditionForm;
    private AnnotationsPanel annotationsEditionPanel;

    private DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto;

    private SearchStatisticalOperationLinkItem operationItem;

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

        // Edit
        mainFormLayout.getEditToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                startDsdEdition();
            }
        });

        // Delete
        mainFormLayout.getDeleteConfirmationWindow().getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deleteDsd(dataStructureDefinitionMetamacDto.getUrn());
            }
        });

        // Save
        mainFormLayout.getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (identifiersEditionForm.validate(false) && contentDescriptorsEditionForm.validate(false) && classDescriptorsEditionForm.validate(false)
                        && diffusionDescriptorsEditionForm.validate(false) && visualisationMetadataEditionForm.validate(false)) {
                    // See: METAMAC-2516
                    // Two invokes to getXXXDto() is needed for Chrome, please don't remove this two call fix.
                    getDataStructureDefinitionDto();
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

                        @Override
                        public void execute() {
                            getUiHandlers().saveDsd(getDataStructureDefinitionDto());
                        }
                    });
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
        mainFormLayout.getCreateTemporalVersion().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().createTemporalVersion(dataStructureDefinitionMetamacDto.getUrn());
            }
        });
        mainFormLayout.getConsolidateVersion().addClickHandler(new ClickHandler() {

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
        mainFormLayout.getExport().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                showExportationWindow(dataStructureDefinitionMetamacDto.getUrn());
            }

            protected void showExportationWindow(final String urn) {
                new ExportSdmxResourceWindow() {

                    @Override
                    protected void startExportation(ExportDetailEnum infoAmount, ExportReferencesEnum references) {
                        getUiHandlers().exportDsd(urn, infoAmount, references);
                    }
                };
            }
        });
        mainFormLayout.getCopy().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().copyDsd(dataStructureDefinitionMetamacDto.getUrn());
            }
        });

        mainFormLayout.getCopyKeepingMaintainer().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                copyDsdAskingCode(dataStructureDefinitionMetamacDto.getUrn());
            }
        });
        mainFormLayout.getLifeCycleReSendStreamMessage().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().reSendStreamMessageDsd(dataStructureDefinitionMetamacDto.getUrn());
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
        ViewTextItem staticUriItem = new ViewTextItem(DataStructureDefinitionDS.URI, getConstants().identifiableArtefactUriProvider());
        ViewTextItem staticUrnItem = new ViewTextItem(DataStructureDefinitionDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem staticUrnProviderItem = new ViewTextItem(DataStructureDefinitionDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        ViewTextItem staticVersion = new ViewTextItem(DataStructureDefinitionDS.VERSION_LOGIC, getConstants().maintainableArtefactVersionLogic());
        ViewTextItem publicationStreamStatus = new ViewTextItem(DataStructureDefinitionDS.PUBLICATION_STREAM_STATUS, getConstants().lifeCycleStructuralResourceStreamMsgStatus());
        publicationStreamStatus.setWidth(20);

        identifiersForm.setFields(staticIdLogic, staticNameItem, staticUriItem, staticUrnItem, staticUrnProviderItem, staticVersion, publicationStreamStatus);

        // Content Descriptors Form
        contentDescriptorsForm = new GroupDynamicForm(getConstants().formContentDescriptors());
        ViewMultiLanguageTextItem staticDescriptionItem = new ViewMultiLanguageTextItem(DataStructureDefinitionDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        ViewTextItem staticFinalItem = new ViewTextItem(DataStructureDefinitionDS.FINAL, getConstants().maintainableArtefactFinalLogic());
        contentDescriptorsForm.setFields(staticDescriptionItem, staticFinalItem);

        // Class Descriptors Form
        classDescriptorsForm = new GroupDynamicForm(getConstants().formClassDescriptors());
        ExternalItemLinkItem relatedOperation = new ExternalItemLinkItem(DataStructureDefinitionDS.STATISTICAL_OPERATION, getConstants().dsdOperation());
        classDescriptorsForm.setFields(relatedOperation);

        // Production descriptors form
        productionDescriptorsForm = new GroupDynamicForm(getConstants().formProductionDescriptors());
        RelatedResourceLinkItem maintainer = new RelatedResourceLinkItem(DataStructureDefinitionDS.MAINTAINER, getConstants().maintainableArtefactMaintainer(),
                getCustomLinkItemNavigationClickHandler());
        ViewTextItem procStatus = new ViewTextItem(DataStructureDefinitionDS.PROC_STATUS, getConstants().lifeCycleProcStatus());
        ViewTextItem creationDate = new ViewTextItem(DataStructureDefinitionDS.VERSION_CREATION_DATE, getConstants().maintainableArtefactVersionCreationDate());
        ViewTextItem creationUser = new ViewTextItem(DataStructureDefinitionDS.VERSION_CREATION_USER, getConstants().maintainableArtefactVersionCreationUser());
        ViewTextItem lastUpdateDate = new ViewTextItem(DataStructureDefinitionDS.VERSION_LAST_UPDATE_DATE, getConstants().maintainableArtefactVersionLastUpdateDate());
        ViewTextItem lastUpdateUser = new ViewTextItem(DataStructureDefinitionDS.VERSION_LAST_UPDATE_USER, getConstants().maintainableArtefactVersionLastUpdateUser());
        ViewTextItem resourceCreationDate = new ViewTextItem(DataStructureDefinitionDS.RESOURCE_CREATION_DATE, getConstants().maintainableArtefactResourceCreationDate());
        productionDescriptorsForm.setFields(maintainer, procStatus, creationDate, creationUser, lastUpdateDate, lastUpdateUser, resourceCreationDate);

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
        DsdDimensionCodesVisualisationItem dimensionCodesVisualisationItem = new DsdDimensionCodesVisualisationItem(DataStructureDefinitionDS.DIMENSION_CODES_VISUALISATION,
                getConstants().dsdDimensionCodesVisualisation(), false);
        visualisationMetadataForm.setFields(autoOpen, showDecimals, dimensionCodesVisualisationItem, showDecimalsPrecision, dimensionsVisualisationItem);

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
        ViewTextItem staticUriItemEdit = new ViewTextItem(DataStructureDefinitionDS.URI, getConstants().identifiableArtefactUriProvider());
        ViewTextItem staticUrnItemEdit = new ViewTextItem(DataStructureDefinitionDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem staticUrnProviderEditItem = new ViewTextItem(DataStructureDefinitionDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        ViewTextItem staticVersionEdit = new ViewTextItem(DataStructureDefinitionDS.VERSION_LOGIC, getConstants().maintainableArtefactVersionLogic());
        identifiersEditionForm.setFields(code, staticCode, nameItem, staticUriItemEdit, staticUrnItemEdit, staticUrnProviderEditItem, staticVersionEdit);

        // Content Descriptors Form
        contentDescriptorsEditionForm = new GroupDynamicForm(getConstants().formContentDescriptors());
        MultiLanguageRichTextEditorItem descriptionItem = new MultiLanguageRichTextEditorItem(DataStructureDefinitionDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        ViewTextItem staticFinalItemEdit = new ViewTextItem(DataStructureDefinitionDS.FINAL, getConstants().maintainableArtefactFinalLogic());
        contentDescriptorsEditionForm.setFields(descriptionItem, staticFinalItemEdit);

        // Class descriptors form
        classDescriptorsEditionForm = new GroupDynamicForm(getConstants().formClassDescriptors());
        operationItem = createStatisticalOperationItem(DataStructureDefinitionDS.STATISTICAL_OPERATION, getConstants().dsdOperation());
        classDescriptorsEditionForm.setFields(operationItem);

        // Production descriptors form
        productionDescriptorsEditionForm = new GroupDynamicForm(getConstants().formProductionDescriptors());
        RelatedResourceLinkItem maintainer = new RelatedResourceLinkItem(DataStructureDefinitionDS.MAINTAINER, getConstants().maintainableArtefactMaintainer(),
                getCustomLinkItemNavigationClickHandler());
        ViewTextItem procStatus = new ViewTextItem(DataStructureDefinitionDS.PROC_STATUS, getConstants().lifeCycleProcStatus());
        ViewTextItem creationDate = new ViewTextItem(DataStructureDefinitionDS.VERSION_CREATION_DATE, getConstants().maintainableArtefactVersionCreationDate());
        ViewTextItem creationUser = new ViewTextItem(DataStructureDefinitionDS.VERSION_CREATION_USER, getConstants().maintainableArtefactVersionCreationUser());
        ViewTextItem lastUpdateDate = new ViewTextItem(DataStructureDefinitionDS.VERSION_LAST_UPDATE_DATE, getConstants().maintainableArtefactVersionLastUpdateDate());
        ViewTextItem lastUpdateUser = new ViewTextItem(DataStructureDefinitionDS.VERSION_LAST_UPDATE_USER, getConstants().maintainableArtefactVersionLastUpdateUser());
        ViewTextItem resourceCreationDate = new ViewTextItem(DataStructureDefinitionDS.RESOURCE_CREATION_DATE, getConstants().maintainableArtefactResourceCreationDate());
        productionDescriptorsEditionForm.setFields(maintainer, procStatus, creationDate, creationUser, lastUpdateDate, lastUpdateUser, resourceCreationDate);

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

        final BooleanSelectItem autoOpen = new BooleanSelectItem(DataStructureDefinitionDS.AUTO_OPEN, getConstants().dsdAutoOpen());
        autoOpen.setValidators(new CustomRequiredValidator() {

            @Override
            protected boolean condition(Object value) {
                return CommonUtils.isMaintainableArtefactInProductionValidationOrGreaterProcStatus(dataStructureDefinitionMetamacDto.getLifeCycle().getProcStatus())
                        ? autoOpen.getBooleanValue() != null
                        : true;
            }
        });

        SelectItem showDecimals = new SelectItem(DataStructureDefinitionDS.SHOW_DECIMALS, getConstants().dsdShowDecimals());
        showDecimals.setValueMap(org.siemac.metamac.srm.web.dsd.utils.CommonUtils.getDsdShowDecimalsHashMap());
        DimensionsVisualisationItem dimensionsVisualisationItem = new DimensionsVisualisationItem(DataStructureDefinitionDS.DIMENSIONS_VISUALISATIONS, getConstants().dsdDimensionsVisualisation(),
                true);
        ShowDecimalsPrecisionItem showDecimalsPrecision = new ShowDecimalsPrecisionItem(DataStructureDefinitionDS.SHOW_DECIMALS_PRECISION, getConstants().dsdShowDecimalsPrecision(), true);
        DsdDimensionCodesVisualisationItem dimensionCodesVisualisationItem = new DsdDimensionCodesVisualisationItem(DataStructureDefinitionDS.DIMENSION_CODES_VISUALISATION,
                getConstants().dsdDimensionCodesVisualisation(), true);
        visualisationMetadataEditionForm.setFields(autoOpen, showDecimals, dimensionCodesVisualisationItem, showDecimalsPrecision, dimensionsVisualisationItem);

        // Comments
        commentsEditionForm = new GroupDynamicForm(getConstants().nameableArtefactComments());
        MultiLanguageRichTextEditorItem comments = new MultiLanguageRichTextEditorItem(DataStructureDefinitionDS.COMMENTS, getConstants().nameableArtefactComments());
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
    public void setUiHandlers(DsdGeneralTabUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        ((ShowDecimalsPrecisionItem) visualisationMetadataForm.getItem(DataStructureDefinitionDS.SHOW_DECIMALS_PRECISION)).setUiHandlers(uiHandlers);
        ((ShowDecimalsPrecisionItem) visualisationMetadataEditionForm.getItem(DataStructureDefinitionDS.SHOW_DECIMALS_PRECISION)).setUiHandlers(uiHandlers);
    }

    @Override
    public void setDsd(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto, List<DimensionComponentDto> dimensionComponentDtos) {
        this.dataStructureDefinitionMetamacDto = dataStructureDefinitionMetamacDto;

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

    private void setDsdViewMode(DataStructureDefinitionMetamacDto dsd, List<DimensionComponentDto> dimensionComponentDtos) {
        // Identifiers form
        identifiersForm.setValue(DataStructureDefinitionDS.CODE, dsd.getCode());
        identifiersForm.setValue(DataStructureDefinitionDS.NAME, dsd.getName());
        identifiersForm.setValue(DataStructureDefinitionDS.URI, dsd.getUriProvider());
        identifiersForm.setValue(DataStructureDefinitionDS.URN, dsd.getUrn());
        identifiersForm.setValue(DataStructureDefinitionDS.URN_PROVIDER, dsd.getUrnProvider());
        identifiersForm.setValue(DataStructureDefinitionDS.VERSION_LOGIC, dsd.getVersionLogic());
        identifiersForm.getItem(DataStructureDefinitionDS.PUBLICATION_STREAM_STATUS)
                .setIcons(StreamMessageStatusEnum.PENDING.equals(dsd.getStreamMessageStatus()) ? null : CommonSharedUtils.getPublicationStreamStatusIcon(dsd.getStreamMessageStatus()));

        // Content Descriptors form
        contentDescriptorsForm.setValue(DataStructureDefinitionDS.DESCRIPTION, dsd.getDescription());
        contentDescriptorsForm.setValue(DataStructureDefinitionDS.FINAL,
                dsd.getFinalLogic() != null ? (dsd.getFinalLogic() ? MetamacWebCommon.getConstants().yes() : MetamacWebCommon.getConstants().no()) : StringUtils.EMPTY);

        // Class descriptors form
        classDescriptorsForm.setValue(DataStructureDefinitionDS.STATISTICAL_OPERATION, dsd.getStatisticalOperation());

        // Production descriptors form
        ((RelatedResourceLinkItem) productionDescriptorsForm.getItem(DataStructureDefinitionDS.MAINTAINER)).setRelatedResource(dsd.getMaintainer());
        productionDescriptorsForm.setValue(DataStructureDefinitionDS.PROC_STATUS, CommonUtils.getProcStatusName(dsd.getLifeCycle().getProcStatus()));
        productionDescriptorsForm.setValue(DataStructureDefinitionDS.VERSION_CREATION_DATE, dsd.getCreatedDate());
        productionDescriptorsForm.setValue(DataStructureDefinitionDS.VERSION_CREATION_USER, dsd.getCreatedBy());
        productionDescriptorsForm.setValue(DataStructureDefinitionDS.VERSION_LAST_UPDATE_DATE, dsd.getLastUpdated());
        productionDescriptorsForm.setValue(DataStructureDefinitionDS.VERSION_LAST_UPDATE_USER, dsd.getLastUpdatedBy());
        productionDescriptorsForm.setValue(DataStructureDefinitionDS.RESOURCE_CREATION_DATE, dsd.getResourceCreatedDate());

        // Status form
        diffusionDescriptorsForm.setValue(DataStructureDefinitionDS.REPLACED_BY_VERSION, dsd.getReplacedByVersion());
        diffusionDescriptorsForm.setValue(DataStructureDefinitionDS.REPLACE_TO_VERSION, dsd.getReplaceToVersion());
        diffusionDescriptorsForm.setValue(DataStructureDefinitionDS.VALID_FROM, dsd.getValidFrom());
        diffusionDescriptorsForm.setValue(DataStructureDefinitionDS.VALID_TO, dsd.getValidTo());
        diffusionDescriptorsForm.setValue(DataStructureDefinitionDS.IS_EXTERNAL_REFERENCE,
                dsd.getIsExternalReference() != null ? (dsd.getIsExternalReference() ? MetamacWebCommon.getConstants().yes() : MetamacWebCommon.getConstants().no()) : StringUtils.EMPTY);

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
        visualisationMetadataForm.setValue(DataStructureDefinitionDS.AUTO_OPEN, BooleanWebUtils.getBooleanLabel(dsd.getAutoOpen()));
        visualisationMetadataForm.setValue(DataStructureDefinitionDS.SHOW_DECIMALS, dsd.getShowDecimals() != null ? dsd.getShowDecimals().toString() : StringUtils.EMPTY);
        ((DimensionsVisualisationItem) visualisationMetadataForm.getItem(DataStructureDefinitionDS.DIMENSIONS_VISUALISATIONS)).setVisualisationDimensions(dsd.getHeadingDimensions(),
                dsd.getStubDimensions());
        ((ShowDecimalsPrecisionItem) visualisationMetadataForm.getItem(DataStructureDefinitionDS.SHOW_DECIMALS_PRECISION)).setMeasureDimensionPrecisions(dsd.getShowDecimalsPrecisions());
        // DIMENSION_CODES_VISUALISATION is set in set setDimensionsAndCandidateVisualisations

        // Comments
        commentsForm.setValue(DataStructureDefinitionDS.COMMENTS, dsd.getComment());

        // Annotations
        annotationsPanel.setAnnotations(dsd.getAnnotations(), dataStructureDefinitionMetamacDto);
    }

    private void setDsdEditionMode(DataStructureDefinitionMetamacDto dsd, List<DimensionComponentDto> dimensionComponentDtos) {

        String[] requiredFieldsToNextProcStatus = RequiredFieldUtils.getDsdRequiredFieldsToNextProcStatus(dsd.getLifeCycle().getProcStatus());

        // IDENTIFIERS FORM

        identifiersEditionForm.setValue(DataStructureDefinitionDS.CODE, dsd.getCode());
        identifiersEditionForm.setValue(DataStructureDefinitionDS.CODE_VIEW, dsd.getCode());
        identifiersEditionForm.setValue(DataStructureDefinitionDS.NAME, dsd.getName());
        identifiersEditionForm.setValue(DataStructureDefinitionDS.URI, dsd.getUriProvider());
        identifiersEditionForm.setValue(DataStructureDefinitionDS.URN, dsd.getUrn());
        identifiersEditionForm.setValue(DataStructureDefinitionDS.URN_PROVIDER, dsd.getUrnProvider());
        identifiersEditionForm.setValue(DataStructureDefinitionDS.VERSION_LOGIC, dsd.getVersionLogic());
        identifiersEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // CONTENT DESCRIPTORS FORM

        contentDescriptorsEditionForm.setValue(DataStructureDefinitionDS.DESCRIPTION, dsd.getDescription());
        contentDescriptorsEditionForm.setValue(DataStructureDefinitionDS.FINAL,
                dsd.getFinalLogic() != null ? (dsd.getFinalLogic() ? MetamacWebCommon.getConstants().yes() : MetamacWebCommon.getConstants().no()) : StringUtils.EMPTY);
        contentDescriptorsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // CLASS DESCRIPTORS FORM

        classDescriptorsEditionForm.setValue(DataStructureDefinitionDS.STATISTICAL_OPERATION, dsd.getStatisticalOperation());
        classDescriptorsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // PRODUCTION DESCRIPTORS FORM

        ((RelatedResourceLinkItem) productionDescriptorsEditionForm.getItem(DataStructureDefinitionDS.MAINTAINER)).setRelatedResource(dsd.getMaintainer());
        productionDescriptorsEditionForm.setValue(DataStructureDefinitionDS.PROC_STATUS, CommonUtils.getProcStatusName(dsd.getLifeCycle().getProcStatus()));
        productionDescriptorsEditionForm.setValue(DataStructureDefinitionDS.VERSION_CREATION_DATE, dsd.getCreatedDate());
        productionDescriptorsEditionForm.setValue(DataStructureDefinitionDS.VERSION_CREATION_USER, dsd.getCreatedBy());
        productionDescriptorsEditionForm.setValue(DataStructureDefinitionDS.VERSION_LAST_UPDATE_DATE, dsd.getLastUpdated());
        productionDescriptorsEditionForm.setValue(DataStructureDefinitionDS.VERSION_LAST_UPDATE_USER, dsd.getLastUpdatedBy());
        productionDescriptorsEditionForm.setValue(DataStructureDefinitionDS.RESOURCE_CREATION_DATE, dsd.getResourceCreatedDate());
        productionDescriptorsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // STATUS FORM

        diffusionDescriptorsEditionForm.setValue(DataStructureDefinitionDS.VALID_FROM, dsd.getValidFrom());
        diffusionDescriptorsEditionForm.setValue(DataStructureDefinitionDS.VALID_TO, dsd.getValidTo());
        diffusionDescriptorsEditionForm.setValue(DataStructureDefinitionDS.IS_EXTERNAL_REFERENCE,
                dsd.getIsExternalReference() != null ? (dsd.getIsExternalReference() ? MetamacWebCommon.getConstants().yes() : MetamacWebCommon.getConstants().no()) : StringUtils.EMPTY);
        diffusionDescriptorsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // VERSION RESPONSIBILITY

        versionResponsibilityEditionForm.setValue(DataStructureDefinitionDS.PRODUCTION_VALIDATION_USER, dsd.getLifeCycle().getProductionValidationUser());
        versionResponsibilityEditionForm.setValue(DataStructureDefinitionDS.PRODUCTION_VALIDATION_DATE, dsd.getLifeCycle().getProductionValidationDate());
        versionResponsibilityEditionForm.setValue(DataStructureDefinitionDS.DIFFUSION_VALIDATION_USER, dsd.getLifeCycle().getDiffusionValidationUser());
        versionResponsibilityEditionForm.setValue(DataStructureDefinitionDS.DIFFUSION_VALIDATION_DATE, dsd.getLifeCycle().getDiffusionValidationDate());
        versionResponsibilityEditionForm.setValue(DataStructureDefinitionDS.INTERNAL_PUBLICATION_USER, dsd.getLifeCycle().getInternalPublicationUser());
        versionResponsibilityEditionForm.setValue(DataStructureDefinitionDS.INTERNAL_PUBLICATION_DATE, dsd.getLifeCycle().getInternalPublicationDate());
        versionResponsibilityEditionForm.setValue(DataStructureDefinitionDS.EXTERNAL_PUBLICATION_USER, dsd.getLifeCycle().getExternalPublicationUser());
        versionResponsibilityEditionForm.setValue(DataStructureDefinitionDS.EXTERNAL_PUBLICATION_DATE, dsd.getLifeCycle().getExternalPublicationDate());
        versionResponsibilityEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // VISUALISATION METADATA

        ((BooleanSelectItem) visualisationMetadataEditionForm.getItem(DataStructureDefinitionDS.AUTO_OPEN)).setBooleanValue(dsd.getAutoOpen());

        visualisationMetadataEditionForm.setValue(DataStructureDefinitionDS.SHOW_DECIMALS, dsd.getShowDecimals() != null ? dsd.getShowDecimals().toString() : StringUtils.EMPTY);

        ((DimensionsVisualisationItem) visualisationMetadataEditionForm.getItem(DataStructureDefinitionDS.DIMENSIONS_VISUALISATIONS)).setVisualisationDimensions(dsd.getHeadingDimensions(),
                dsd.getStubDimensions());
        setDimensionsForStubAndHeading(dimensionComponentDtos);

        ((ShowDecimalsPrecisionItem) visualisationMetadataEditionForm.getItem(DataStructureDefinitionDS.SHOW_DECIMALS_PRECISION)).setMeasureDimensionPrecisions(dsd.getShowDecimalsPrecisions());

        // DIMENSION_CODES_VISUALISATION is set in set setDimensionsAndCandidateVisualisations
        visualisationMetadataEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // COMMENTS

        commentsEditionForm.setValue(DataStructureDefinitionDS.COMMENTS, dsd.getComment());
        commentsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // ANNOTATIONS

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
        dataStructureDefinitionMetamacDto.setName(identifiersEditionForm.getValueAsInternationalStringDto(DataStructureDefinitionDS.NAME));

        // Content Descriptors form
        dataStructureDefinitionMetamacDto.setDescription(contentDescriptorsEditionForm.getValueAsInternationalStringDto(DataStructureDefinitionDS.DESCRIPTION));

        // Class descriptors form
        dataStructureDefinitionMetamacDto.setStatisticalOperation(classDescriptorsEditionForm.getValueAsExternalItemDto(DataStructureDefinitionDS.STATISTICAL_OPERATION));

        // Production descriptors form

        // Visualisation metadata
        dataStructureDefinitionMetamacDto.setAutoOpen(((BooleanSelectItem) visualisationMetadataEditionForm.getItem(DataStructureDefinitionDS.AUTO_OPEN)).getBooleanValue());
        dataStructureDefinitionMetamacDto.setShowDecimals(!StringUtils.isBlank(visualisationMetadataEditionForm.getValueAsString(DataStructureDefinitionDS.SHOW_DECIMALS))
                ? Integer.valueOf(visualisationMetadataEditionForm.getValueAsString(DataStructureDefinitionDS.SHOW_DECIMALS))
                : null);
        dataStructureDefinitionMetamacDto.getHeadingDimensions().clear();
        dataStructureDefinitionMetamacDto.getHeadingDimensions()
                .addAll(((DimensionsVisualisationItem) visualisationMetadataEditionForm.getItem(DataStructureDefinitionDS.DIMENSIONS_VISUALISATIONS)).getHeadingDimensions());
        dataStructureDefinitionMetamacDto.getStubDimensions().clear();
        dataStructureDefinitionMetamacDto.getStubDimensions()
                .addAll(((DimensionsVisualisationItem) visualisationMetadataEditionForm.getItem(DataStructureDefinitionDS.DIMENSIONS_VISUALISATIONS)).getStubDimensions());
        dataStructureDefinitionMetamacDto.getShowDecimalsPrecisions().clear();
        dataStructureDefinitionMetamacDto.getShowDecimalsPrecisions()
                .addAll(((ShowDecimalsPrecisionItem) visualisationMetadataEditionForm.getItem(DataStructureDefinitionDS.SHOW_DECIMALS_PRECISION)).getMeasureDimensionPrecisionDtos());
        dataStructureDefinitionMetamacDto.getDimensionVisualisationInfos().clear();
        dataStructureDefinitionMetamacDto.getDimensionVisualisationInfos()
                .addAll(((DsdDimensionCodesVisualisationItem) visualisationMetadataEditionForm.getItem(DataStructureDefinitionDS.DIMENSION_CODES_VISUALISATION)).getDimensionVisualisationInfoDtos());

        // Comments
        dataStructureDefinitionMetamacDto.setComment(commentsEditionForm.getValueAsInternationalStringDto(DataStructureDefinitionDS.COMMENTS));

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

    private void copyDsdAskingCode(final String urn) {
        final CopyResourceWindow copyResourceWindow = new CopyResourceWindow(getConstants().copyResource());
        copyResourceWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                if (copyResourceWindow.validateForm()) {
                    getUiHandlers().copyDsd(urn, copyResourceWindow.getSelectedCode());
                    copyResourceWindow.destroy();
                }
            }
        });
    }

    // Visualisation metadata

    @Override
    public void setDimensionsAndCandidateVisualisations(List<DimensionComponentDto> dimensionComponentDtos, Map<String, List<RelatedResourceDto>> candidateOrderVisualisations,
            Map<String, List<RelatedResourceDto>> candidateOpennessLevelVisualisations) {
        ((DsdDimensionCodesVisualisationItem) visualisationMetadataForm.getItem(DataStructureDefinitionDS.DIMENSION_CODES_VISUALISATION))
                .setDimensionsAndCandidateVisualisations(dataStructureDefinitionMetamacDto, dimensionComponentDtos, candidateOrderVisualisations, candidateOpennessLevelVisualisations);
        ((DsdDimensionCodesVisualisationItem) visualisationMetadataEditionForm.getItem(DataStructureDefinitionDS.DIMENSION_CODES_VISUALISATION))
                .setDimensionsAndCandidateVisualisations(dataStructureDefinitionMetamacDto, dimensionComponentDtos, candidateOrderVisualisations, candidateOpennessLevelVisualisations);
    }

    private void setDimensionsForStubAndHeading(List<DimensionComponentDto> dimensionComponentDtos) {
        List<RelatedResourceDto> dimensions = RelatedResourceUtils.getDimensionComponentDtosAsRelatedResourceDtos(dimensionComponentDtos);
        ((DimensionsVisualisationItem) visualisationMetadataEditionForm.getItem(DataStructureDefinitionDS.DIMENSIONS_VISUALISATIONS)).setDimensions(dimensions);
        visualisationMetadataEditionForm.markForRedraw();
    }

    @Override
    public void setConceptsForShowDecimalsPrecision(ConceptSchemeMetamacDto conceptSchemeMetamacDto, List<ConceptMetamacVisualisationResult> concepts) {
        ((ShowDecimalsPrecisionItem) visualisationMetadataForm.getItem(DataStructureDefinitionDS.SHOW_DECIMALS_PRECISION)).setConcepts(conceptSchemeMetamacDto, concepts);
        ((ShowDecimalsPrecisionItem) visualisationMetadataEditionForm.getItem(DataStructureDefinitionDS.SHOW_DECIMALS_PRECISION)).setConcepts(conceptSchemeMetamacDto, concepts);
        visualisationMetadataForm.markForRedraw();
        visualisationMetadataEditionForm.markForRedraw();
    }

    @Override
    public void resetMeasureDimensionConcepts() {
        ((ShowDecimalsPrecisionItem) visualisationMetadataForm.getItem(DataStructureDefinitionDS.SHOW_DECIMALS_PRECISION)).resetMeasureDimensionConceptsTreeGrid(false);
        ((ShowDecimalsPrecisionItem) visualisationMetadataEditionForm.getItem(DataStructureDefinitionDS.SHOW_DECIMALS_PRECISION)).resetMeasureDimensionConceptsTreeGrid(true);
        visualisationMetadataForm.markForRedraw();
        visualisationMetadataEditionForm.markForRedraw();
    }

    @Override
    public void setOperations(GetStatisticalOperationsResult result) {
        operationItem.setOperations(result.getOperations(), result.getFirstResultOut(), result.getTotalResults());
    }

    private SearchStatisticalOperationLinkItem createStatisticalOperationItem(String name, String title) {
        SearchStatisticalOperationLinkItem operationItem = new SearchStatisticalOperationLinkItem(name, title) {

            @Override
            protected void retrieveStatisticalOperations(int firstResult, int maxResults, MetamacWebCriteria webCriteria) {
                getUiHandlers().retrieveStatisticalOperations(firstResult, maxResults, webCriteria != null ? webCriteria.getCriteria() : null);
            }
        };
        operationItem.setRequired(true);
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
