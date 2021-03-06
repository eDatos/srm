package org.siemac.metamac.srm.web.code.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.BooleanUtils;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementBasicDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementOperationDto;
import org.siemac.metamac.srm.core.code.enume.domain.VariableTypeEnum;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.web.client.constants.SrmWebConstants;
import org.siemac.metamac.srm.web.client.resources.GlobalResources;
import org.siemac.metamac.srm.web.client.widgets.RelatedResourceListItem;
import org.siemac.metamac.srm.web.client.widgets.SearchRelatedResourcePaginatedWindow;
import org.siemac.metamac.srm.web.client.widgets.search.SearchMultiRelatedResourceSimpleItem;
import org.siemac.metamac.srm.web.client.widgets.search.SearchMultipleRelatedResourcePaginatedWindow;
import org.siemac.metamac.srm.web.code.model.ds.VariableDS;
import org.siemac.metamac.srm.web.code.model.ds.VariableElementDS;
import org.siemac.metamac.srm.web.code.model.record.VariableElementRecord;
import org.siemac.metamac.srm.web.code.presenter.VariablePresenter;
import org.siemac.metamac.srm.web.code.utils.CodesClientSecurityUtils;
import org.siemac.metamac.srm.web.code.utils.CodesFormUtils;
import org.siemac.metamac.srm.web.code.utils.CommonUtils;
import org.siemac.metamac.srm.web.code.view.handlers.VariableUiHandlers;
import org.siemac.metamac.srm.web.code.widgets.ImportVariableElementShapeWindow;
import org.siemac.metamac.srm.web.code.widgets.ImportVariableElementsWindow;
import org.siemac.metamac.srm.web.code.widgets.NewVariableElementWindow;
import org.siemac.metamac.srm.web.code.widgets.VariableElementOperationLayout;
import org.siemac.metamac.srm.web.code.widgets.VariableMainFormLayout;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementsResult;
import org.siemac.metamac.srm.web.shared.code.GetVariableFamiliesResult;
import org.siemac.metamac.srm.web.shared.code.GetVariablesResult;
import org.siemac.metamac.srm.web.shared.criteria.VariableWebCriteria;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.listener.UploadListener;
import org.siemac.metamac.web.common.client.utils.DateUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;
import org.siemac.metamac.web.common.client.widgets.CustomListGridField;
import org.siemac.metamac.web.common.client.widgets.CustomSectionStack;
import org.siemac.metamac.web.common.client.widgets.CustomToolStripButton;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.InformationWindow;
import org.siemac.metamac.web.common.client.widgets.PaginatedCheckListGrid;
import org.siemac.metamac.web.common.client.widgets.SearchSectionStack;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCheckboxItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomDateItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;
import org.siemac.metamac.web.common.client.widgets.handlers.ListRecordNavigationClickHandler;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.smartgwt.client.widgets.form.validator.CustomValidator;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

public class VariableViewImpl extends ViewWithUiHandlers<VariableUiHandlers> implements VariablePresenter.VariableView {

    private final VLayout panel;
    private final VariableMainFormLayout mainFormLayout;

    // View forms
    private GroupDynamicForm identifiersForm;
    private GroupDynamicForm contentDescriptorsForm;
    private GroupDynamicForm productionDescriptorsForm;
    private GroupDynamicForm diffusionDescriptorsForm;

    // Edition forms
    private GroupDynamicForm identifiersEditionForm;
    private GroupDynamicForm contentDescriptorsEditionForm;
    private GroupDynamicForm productionDescriptorsEditionForm;
    private GroupDynamicForm diffusionDescriptorsEditionForm;

    // Variable elements

    private final PaginatedCheckListGrid variableElementListGrid;
    private final CustomToolStripButton createVariableElementButton;
    private final CustomToolStripButton importVariableElementButton;
    private final CustomToolStripButton exportVariableElementButton;
    private final CustomToolStripButton deleteVariableElementButton;
    private final CustomToolStripButton fusionVariableElementButton;
    private final CustomToolStripButton segregateVariableElementButton;
    private NewVariableElementWindow newVariableElementWindow;
    private final DeleteConfirmationWindow deleteConfirmationWindow;
    private SearchSectionStack elementsSectionStack;

    private SearchRelatedResourcePaginatedWindow createFusionWindow;
    private SearchMultipleRelatedResourcePaginatedWindow createSegregationWindow;

    // Variable elements importation

    private final ImportVariableElementsWindow importVariableElementsWindow;

    // Variable element operations

    private final CustomSectionStack operationsSectionStack;
    private final VariableElementOperationLayout variableElementOperationsLayout;

    private final ImportVariableElementShapeWindow importVariableElementShapeWindow;

    private VariableDto variableDto;

    @Inject
    public VariableViewImpl() {
        super();

        //
        // VARIABLE
        //

        mainFormLayout = new VariableMainFormLayout();
        bindMainFormLayoutEvents();
        createViewForm();
        createEditionForm();

        // SHAPES IMPORTATION

        // Variable elements importation window

        importVariableElementShapeWindow = new ImportVariableElementShapeWindow();
        importVariableElementShapeWindow.setUploadListener(new UploadListener() {

            @Override
            public void uploadFailed(String errorMessage) {
                getUiHandlers().resourceImportationFailed(errorMessage);
            }

            @Override
            public void uploadComplete(String fileName) {
                getUiHandlers().resourceImportationSucceed(fileName);
            }
        });

        // VARIABLE ELEMENTS

        // Variable elements importation window

        importVariableElementsWindow = new ImportVariableElementsWindow();
        importVariableElementsWindow.setUploadListener(new UploadListener() {

            @Override
            public void uploadFailed(String errorMessage) {
                getUiHandlers().resourceImportationFailed(errorMessage);
            }

            @Override
            public void uploadComplete(String fileName) {
                getUiHandlers().resourceImportationSucceed(fileName);
            }
        });

        // ToolStrip

        ToolStrip toolStrip = new ToolStrip();
        toolStrip.setWidth100();
        createVariableElementButton = createCreateVariableElementButton();
        toolStrip.addButton(createVariableElementButton);

        importVariableElementButton = createImportVariableElementsButton();
        toolStrip.addButton(importVariableElementButton);

        exportVariableElementButton = createExportVariableElementsButton();
        toolStrip.addButton(exportVariableElementButton);

        deleteVariableElementButton = createDeleteVariableElementButton();
        toolStrip.addButton(deleteVariableElementButton);

        fusionVariableElementButton = createFusionButton();
        toolStrip.addButton(fusionVariableElementButton);

        segregateVariableElementButton = createSegregateButton();
        toolStrip.addButton(segregateVariableElementButton);

        // ListGrid

        variableElementListGrid = new PaginatedCheckListGrid(VariablePresenter.ELEMENT_LIST_MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveVariableElementsByVariable(firstResult, maxResults, elementsSectionStack.getSearchCriteria(), variableDto.getUrn());
            }
        });

        variableElementListGrid.getListGrid().setDataSource(new VariableElementDS());
        variableElementListGrid.getListGrid().setUseAllDataSourceFields(false);
        variableElementListGrid.getListGrid().addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                ListGridRecord[] selectedRecords = variableElementListGrid.getListGrid().getSelectedRecords();
                updateListGridDeleteButtonVisibility(selectedRecords);
                updateListGridFusionButtonVisibility(selectedRecords.length);
                updateListGridSegregateButtonVisibility(selectedRecords.length);

            }
        });
        variableElementListGrid.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // Clicking checkBox will be ignored
                    String code = ((VariableElementRecord) event.getRecord()).getAttribute(VariableElementDS.CODE);
                    getUiHandlers().goToVariableElement(code);
                }
            }
        });
        ListGridField fieldCode = new ListGridField(VariableElementDS.CODE, getConstants().identifiableArtefactCode());
        fieldCode.setAlign(Alignment.LEFT);
        ListGridField fieldShortName = new ListGridField(VariableElementDS.SHORT_NAME, getConstants().variableElementShortName());
        CustomListGridField hasShape = new CustomListGridField(VariableElementDS.SHAPE_WKT, getConstants().variableElementHasPolygonShape());
        ListGridField urn = new ListGridField(VariableElementDS.URN, getConstants().identifiableArtefactUrn());

        variableElementListGrid.getListGrid().setAutoFitData(Autofit.VERTICAL);
        variableElementListGrid.getListGrid().setFields(fieldCode, fieldShortName, hasShape, urn);

        // Remove window

        deleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().variableElementDeleteConfirmationTitle(), getConstants().variableElementDeleteConfirmation());
        deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers()
                        .deleteVariableElements(org.siemac.metamac.srm.web.code.utils.CommonUtils.getUrnsFromSelectedVariableElements(variableElementListGrid.getListGrid().getSelectedRecords()));
                deleteConfirmationWindow.hide();
            }
        });

        // VARIABLE ELEMENT OPERATIONS

        variableElementOperationsLayout = new VariableElementOperationLayout();

        // layout

        VLayout layout = new VLayout();
        layout.setMargin(15);
        layout.setMembersMargin(15);

        elementsSectionStack = new SearchSectionStack(getConstants().variableVariableElements());
        elementsSectionStack.getSection().addItem(toolStrip);
        elementsSectionStack.getSection().addItem(variableElementListGrid);
        elementsSectionStack.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                getUiHandlers().retrieveVariableElementsByVariable(VariablePresenter.ELEMENT_LIST_FIRST_RESULT, VariablePresenter.ELEMENT_LIST_MAX_RESULTS, elementsSectionStack.getSearchCriteria(),
                        variableDto.getUrn());
            }
        });
        elementsSectionStack.addSearchItemKeyPressHandler(new KeyPressHandler() {

            @Override
            public void onKeyPress(KeyPressEvent event) {
                if (StringUtils.equalsIgnoreCase(event.getKeyName(), SrmWebConstants.ENTER_KEY)) {
                    getUiHandlers().retrieveVariableElementsByVariable(VariablePresenter.ELEMENT_LIST_FIRST_RESULT, VariablePresenter.ELEMENT_LIST_MAX_RESULTS,
                            elementsSectionStack.getSearchCriteria(), variableDto.getUrn());
                }
            }
        });

        layout.addMember(elementsSectionStack);

        operationsSectionStack = new CustomSectionStack(getConstants().variableOperationsBetweenElements());
        operationsSectionStack.getDefaultSection().addItem(variableElementOperationsLayout);
        layout.addMember(operationsSectionStack);

        panel = new VLayout();
        panel.setOverflow(Overflow.SCROLL);
        panel.addMember(mainFormLayout);
        panel.addMember(layout);
    }

    @Override
    public void setUiHandlers(VariableUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        variableElementOperationsLayout.setUiHandlers(uiHandlers);
        importVariableElementShapeWindow.setUiHandlers(uiHandlers);
        importVariableElementsWindow.setUiHandlers(uiHandlers);
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

                diffusionDescriptorsForm.setTranslationsShowed(translationsShowed);
                diffusionDescriptorsEditionForm.setTranslationsShowed(translationsShowed);
            }
        });

        // Save
        mainFormLayout.getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (identifiersEditionForm.validate(false) && contentDescriptorsEditionForm.validate(false) && diffusionDescriptorsEditionForm.validate(false)) {
                    // See: METAMAC-2516
                    // Two invokes to getXXXDto() is needed for Chrome, please don't remove this two call fix.
                    getVariableDto();
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

                        @Override
                        public void execute() {
                            getUiHandlers().saveVariable(getVariableDto());
                        }
                    });
                }
            }
        });

        // Delete
        mainFormLayout.getDeleteConfirmationWindow().getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deleteVariable(variableDto.getUrn());
            }
        });

        // Importation
        mainFormLayout.getImportShape().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                importVariableElementShapeWindow.show();
            }
        });
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == VariablePresenter.TYPE_SetContextAreaContentCodesToolBar) {
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
    public void setVariable(VariableDto variableDto) {
        this.variableDto = variableDto;
        this.mainFormLayout.setVariable(variableDto);
        this.importVariableElementsWindow.setVariable(variableDto);
        this.importVariableElementShapeWindow.setVariable(variableDto);

        String defaultLocalized = InternationalStringUtils.getLocalisedString(variableDto.getName());
        String title = defaultLocalized != null ? defaultLocalized : StringUtils.EMPTY;
        mainFormLayout.setTitleLabelContents(title);

        mainFormLayout.setViewMode();

        setVariableViewMode(variableDto);
        setVariableEditionMode(variableDto);

        updateVariableElementCreationAndImportationButtons(variableDto);
    }

    @Override
    public void clearSearchSection() {
        elementsSectionStack.reset();
    }

    private void createViewForm() {
        // Identifiers Form
        identifiersForm = new GroupDynamicForm(getConstants().formIdentifiers());
        ViewTextItem code = new ViewTextItem(VariableDS.CODE, getConstants().identifiableArtefactCode());
        ViewMultiLanguageTextItem name = new ViewMultiLanguageTextItem(VariableDS.NAME, getConstants().nameableArtefactName());
        ViewMultiLanguageTextItem shortName = new ViewMultiLanguageTextItem(VariableDS.SHORT_NAME, getConstants().variableShortName());
        ViewTextItem urn = new ViewTextItem(VariableDS.URN, getConstants().identifiableArtefactUrn());
        identifiersForm.setFields(code, name, shortName, urn);

        // Content descriptors
        contentDescriptorsForm = new GroupDynamicForm(getConstants().formContentDescriptors());
        ViewTextItem isGeographicalVariable = new ViewTextItem(VariableDS.IS_GEOGRAPHICAL, getConstants().variableIsGeographical());
        RelatedResourceListItem families = new RelatedResourceListItem(VariableDS.FAMILIES, getConstants().variableFamilies(), false, getListRecordNavigationClickHandler());
        contentDescriptorsForm.setFields(isGeographicalVariable, families);

        // Production descriptors
        productionDescriptorsForm = new GroupDynamicForm(getConstants().formProductionDescriptors());
        ViewTextItem creationDate = new ViewTextItem(VariableDS.CREATION_DATE, getConstants().resourceCreationDate());
        ViewTextItem creationUser = new ViewTextItem(VariableDS.CREATION_USER, getConstants().resourceCreationUser());
        ViewTextItem lastUpdateDate = new ViewTextItem(VariableDS.LAST_UPDATE_DATE, getConstants().resourceLastUpdateDate());
        ViewTextItem lastUpdateUser = new ViewTextItem(VariableDS.LAST_UPDATE_USER, getConstants().resourceLastUpdateUser());
        productionDescriptorsForm.setFields(creationDate, creationUser, lastUpdateDate, lastUpdateUser);

        // Diffusion descriptors
        diffusionDescriptorsForm = new GroupDynamicForm(getConstants().formDiffusionDescriptors());
        ViewTextItem validFrom = new ViewTextItem(VariableDS.VALID_FROM, getConstants().variableValidFrom());
        ViewTextItem validTo = new ViewTextItem(VariableDS.VALID_TO, getConstants().variableValidTo());
        RelatedResourceListItem replaceToVariables = new RelatedResourceListItem(VariableDS.REPLACE_TO_VARIABLES, getConstants().variableReplaceToVariables(), false,
                getListRecordNavigationClickHandler());
        ViewTextItem replacedByVariable = new ViewTextItem(VariableDS.REPLACED_BY_VARIABLE, getConstants().variableReplacedByVariable());
        diffusionDescriptorsForm.setFields(validFrom, validTo, replaceToVariables, replacedByVariable);

        mainFormLayout.addViewCanvas(identifiersForm);
        mainFormLayout.addViewCanvas(contentDescriptorsForm);
        mainFormLayout.addViewCanvas(productionDescriptorsForm);
        mainFormLayout.addViewCanvas(diffusionDescriptorsForm);
    }

    private void createEditionForm() {
        // Identifiers
        identifiersEditionForm = new GroupDynamicForm(getConstants().formIdentifiers());
        ViewTextItem code = new ViewTextItem(VariableDS.CODE, getConstants().identifiableArtefactCode());
        MultiLanguageTextItem name = new MultiLanguageTextItem(VariableDS.NAME, getConstants().nameableArtefactName());
        name.setRequired(true);
        MultiLanguageTextItem shortName = new MultiLanguageTextItem(VariableDS.SHORT_NAME, getConstants().variableShortName(), SrmConstants.METADATA_SHORT_NAME_MAXIMUM_LENGTH);
        name.setRequired(true);
        ViewTextItem urn = new ViewTextItem(VariableDS.URN, getConstants().identifiableArtefactUrn());
        identifiersEditionForm.setFields(code, name, shortName, urn);

        // Content descriptors
        contentDescriptorsEditionForm = new GroupDynamicForm(getConstants().formContentDescriptors());

        ViewTextItem staticIsGeographical = new ViewTextItem(VariableDS.IS_GEOGRAPHICAL_VIEW, getConstants().variableIsGeographical());
        staticIsGeographical.setShowIfCondition(getStaticIsGeographicalFormItemIfFunction());

        CustomCheckboxItem isGeographical = new CustomCheckboxItem(VariableDS.IS_GEOGRAPHICAL, getConstants().variableIsGeographical());
        isGeographical.setShowIfCondition(getIsGeographicalFormItemIfFunction());
        FormItemIcon infoIcon = new FormItemIcon();
        infoIcon.setHeight(14);
        infoIcon.setWidth(14);
        infoIcon.setSrc(org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.info().getURL());
        infoIcon.setPrompt(getConstants().variableIsGeographicalChangeInfoMessage());
        isGeographical.setIcons(infoIcon);

        RelatedResourceListItem families = createFamiliesItem();

        contentDescriptorsEditionForm.setFields(staticIsGeographical, isGeographical, families);

        // Production descriptors
        productionDescriptorsEditionForm = new GroupDynamicForm(getConstants().formProductionDescriptors());
        ViewTextItem creationDate = new ViewTextItem(VariableDS.CREATION_DATE, getConstants().resourceCreationDate());
        ViewTextItem creationUser = new ViewTextItem(VariableDS.CREATION_USER, getConstants().resourceCreationUser());
        ViewTextItem lastUpdateDate = new ViewTextItem(VariableDS.LAST_UPDATE_DATE, getConstants().resourceLastUpdateDate());
        ViewTextItem lastUpdateUser = new ViewTextItem(VariableDS.LAST_UPDATE_USER, getConstants().resourceLastUpdateUser());
        productionDescriptorsEditionForm.setFields(creationDate, creationUser, lastUpdateDate, lastUpdateUser);

        // Diffusion descriptors
        diffusionDescriptorsEditionForm = new GroupDynamicForm(getConstants().formDiffusionDescriptors());
        CustomDateItem validFrom = new CustomDateItem(VariableDS.VALID_FROM, getConstants().variableValidFrom());
        CustomDateItem validTo = new CustomDateItem(VariableDS.VALID_TO, getConstants().variableValidTo());
        RelatedResourceListItem replaceToVariables = createReplaceToVariablesItem();
        ViewTextItem replacedByVariable = new ViewTextItem(VariableDS.REPLACED_BY_VARIABLE, getConstants().variableReplacedByVariable());
        diffusionDescriptorsEditionForm.setFields(validFrom, validTo, replaceToVariables, replacedByVariable);

        mainFormLayout.addEditionCanvas(identifiersEditionForm);
        mainFormLayout.addEditionCanvas(contentDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(productionDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(diffusionDescriptorsEditionForm);
    }

    public void setEditionMode() {
        mainFormLayout.setEditionMode();
    }

    public void setVariableViewMode(VariableDto variableDto) {
        // Identifiers
        identifiersForm.setValue(VariableDS.CODE, variableDto.getCode());
        identifiersForm.setValue(VariableDS.URN, variableDto.getUrn());
        identifiersForm.setValue(VariableDS.NAME, variableDto.getName());
        identifiersForm.setValue(VariableDS.SHORT_NAME, variableDto.getShortName());

        // Content descriptors
        contentDescriptorsForm.setValue(VariableDS.IS_GEOGRAPHICAL,
                VariableTypeEnum.GEOGRAPHICAL.equals(variableDto.getType()) ? MetamacWebCommon.getConstants().yes() : MetamacWebCommon.getConstants().no());
        ((RelatedResourceListItem) contentDescriptorsForm.getItem(VariableDS.FAMILIES)).setRelatedResources(variableDto.getFamilies());

        // Production descriptors
        productionDescriptorsForm.setValue(VariableDS.CREATION_DATE, variableDto.getCreatedDate());
        productionDescriptorsForm.setValue(VariableDS.CREATION_USER, variableDto.getCreatedBy());
        productionDescriptorsForm.setValue(VariableDS.LAST_UPDATE_DATE, variableDto.getLastUpdated());
        productionDescriptorsForm.setValue(VariableDS.LAST_UPDATE_USER, variableDto.getLastUpdatedBy());

        // Diffusion descriptors
        diffusionDescriptorsForm.setValue(VariableDS.VALID_FROM, DateUtils.getFormattedDate(variableDto.getValidFrom()));
        diffusionDescriptorsForm.setValue(VariableDS.VALID_TO, DateUtils.getFormattedDate(variableDto.getValidTo()));
        ((RelatedResourceListItem) diffusionDescriptorsForm.getItem(VariableDS.REPLACE_TO_VARIABLES)).setRelatedResources(variableDto.getReplaceToVariables());
        diffusionDescriptorsForm.setValue(VariableDS.REPLACED_BY_VARIABLE, RelatedResourceUtils.getRelatedResourceName(variableDto.getReplacedByVariable()));
    }

    public void setVariableEditionMode(VariableDto variableDto) {
        // Identifiers
        identifiersEditionForm.setValue(VariableDS.CODE, variableDto.getCode());
        identifiersEditionForm.setValue(VariableDS.URN, variableDto.getUrn());
        identifiersEditionForm.setValue(VariableDS.NAME, variableDto.getName());
        identifiersEditionForm.setValue(VariableDS.SHORT_NAME, variableDto.getShortName());

        // Content descriptors
        contentDescriptorsEditionForm.setValue(VariableDS.IS_GEOGRAPHICAL_VIEW,
                VariableTypeEnum.GEOGRAPHICAL.equals(variableDto.getType()) ? MetamacWebCommon.getConstants().yes() : MetamacWebCommon.getConstants().no());
        contentDescriptorsEditionForm.setValue(VariableDS.IS_GEOGRAPHICAL, VariableTypeEnum.GEOGRAPHICAL.equals(variableDto.getType()));
        ((RelatedResourceListItem) contentDescriptorsEditionForm.getItem(VariableDS.FAMILIES)).setRelatedResources(variableDto.getFamilies());

        // Production descriptors
        productionDescriptorsEditionForm.setValue(VariableDS.CREATION_DATE, variableDto.getCreatedDate());
        productionDescriptorsEditionForm.setValue(VariableDS.CREATION_USER, variableDto.getCreatedBy());
        productionDescriptorsEditionForm.setValue(VariableDS.LAST_UPDATE_DATE, variableDto.getLastUpdated());
        productionDescriptorsEditionForm.setValue(VariableDS.LAST_UPDATE_USER, variableDto.getLastUpdatedBy());

        // Diffusion descriptors
        diffusionDescriptorsEditionForm.setValue(VariableDS.VALID_FROM, variableDto.getValidFrom());
        diffusionDescriptorsEditionForm.setValue(VariableDS.VALID_TO, variableDto.getValidTo());
        ((RelatedResourceListItem) diffusionDescriptorsEditionForm.getItem(VariableDS.REPLACE_TO_VARIABLES)).setRelatedResources(variableDto.getReplaceToVariables());
        diffusionDescriptorsEditionForm.setValue(VariableDS.REPLACED_BY_VARIABLE, RelatedResourceUtils.getRelatedResourceName(variableDto.getReplacedByVariable()));
    }

    public VariableDto getVariableDto() {
        // Identifiers
        variableDto.setName(identifiersEditionForm.getValueAsInternationalStringDto(VariableDS.NAME));
        variableDto.setShortName(identifiersEditionForm.getValueAsInternationalStringDto(VariableDS.SHORT_NAME));

        // Content descriptors

        Boolean isGeographical = ((CustomCheckboxItem) contentDescriptorsEditionForm.getItem(VariableDS.IS_GEOGRAPHICAL)).getValueAsBoolean();
        variableDto.setType(BooleanUtils.isTrue(isGeographical) ? VariableTypeEnum.GEOGRAPHICAL : null);

        variableDto.getFamilies().clear();
        variableDto.getFamilies().addAll(((RelatedResourceListItem) contentDescriptorsEditionForm.getItem(VariableDS.FAMILIES)).getSelectedRelatedResources());

        // Diffusion descriptors
        variableDto.setValidFrom(((CustomDateItem) diffusionDescriptorsEditionForm.getItem(VariableDS.VALID_FROM)).getValueAsDate());
        variableDto.setValidTo(((CustomDateItem) diffusionDescriptorsEditionForm.getItem(VariableDS.VALID_TO)).getValueAsDate());
        variableDto.getReplaceToVariables().clear();
        variableDto.getReplaceToVariables().addAll(((RelatedResourceListItem) diffusionDescriptorsEditionForm.getItem(VariableDS.REPLACE_TO_VARIABLES)).getSelectedRelatedResources());

        return variableDto;
    }

    @Override
    public void setVariableFamilies(GetVariableFamiliesResult result) {
        ((SearchMultiRelatedResourceSimpleItem) contentDescriptorsEditionForm.getItem(VariableDS.FAMILIES))
                .setResources(RelatedResourceUtils.getVariableFamilyBasicDtosAsRelatedResourceDtos(result.getFamilies()), result.getFirstResultOut(), result.getTotalResults());
    }

    @Override
    public void setVariables(GetVariablesResult result) {
        ((SearchMultiRelatedResourceSimpleItem) diffusionDescriptorsEditionForm.getItem(VariableDS.REPLACE_TO_VARIABLES))
                .setResources(RelatedResourceUtils.getVariableBasicDtosAsRelatedResourceDtos(result.getVariables()), result.getFirstResultOut(), result.getTotalResults());
    }

    @Override
    public void setVariableElements(GetVariableElementsResult result) {
        setVariableElements(result.getVariableElements());
        variableElementListGrid.getListGrid().setAutoFitMaxRecords(result.getVariableElements().size());
        variableElementListGrid.refreshPaginationInfo(result.getFirstResultOut(), result.getVariableElements().size(), result.getTotalResults());
    }

    @Override
    public void setVariableElementsForFusion(GetVariableElementsResult result) {
        if (createFusionWindow != null) {
            createFusionWindow.setRelatedResources(RelatedResourceUtils.getVariableElementBasicDtosAsRelatedResourceDtos(result.getVariableElements()));
            createFusionWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getVariableElements().size(), result.getTotalResults());
        }
    }

    @Override
    public void setVariableElementsForSegregation(GetVariableElementsResult result) {
        if (createSegregationWindow != null) {
            createSegregationWindow.setResources(RelatedResourceUtils.getVariableElementBasicDtosAsRelatedResourceDtos(result.getVariableElements()));
            createSegregationWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getVariableElements().size(), result.getTotalResults());
        }
    }

    @Override
    public void setVariableElementOperations(List<VariableElementOperationDto> variableElementOperationDtos) {
        variableElementOperationsLayout.setVariableElementOperations(variableElementOperationDtos);

        // Do not show the operations is the list is empty
        if (variableElementOperationDtos.isEmpty()) {
            operationsSectionStack.hide();
        } else {
            operationsSectionStack.show();
        }
    }

    private void setVariableElements(List<VariableElementBasicDto> variableElementDtos) {
        VariableElementRecord[] records = new VariableElementRecord[variableElementDtos.size()];
        int index = 0;
        for (VariableElementBasicDto element : variableElementDtos) {
            records[index++] = org.siemac.metamac.srm.web.code.utils.CodesRecordUtils.getVariableElementRecord(element);
        }
        variableElementListGrid.getListGrid().setData(records);
    }

    //
    // ITEMS CREATION
    //

    private RelatedResourceListItem createFamiliesItem() {

        final SearchMultiRelatedResourceSimpleItem familiesItem = new SearchMultiRelatedResourceSimpleItem(VariableDS.FAMILIES, getConstants().variableFamilies(),
                SrmWebConstants.FORM_LIST_MAX_RESULTS, getListRecordNavigationClickHandler()) {

            @Override
            protected void retrieveResources(int firstResult, int maxResults, MetamacWebCriteria webCriteria) {
                getUiHandlers().retrieveVariableFamilies(firstResult, maxResults, webCriteria.getCriteria());
            }
        };

        // Set required with a customValidator
        CustomValidator customValidator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                return familiesItem.getSelectedRelatedResources() != null && !familiesItem.getSelectedRelatedResources().isEmpty();
            }
        };
        familiesItem.setValidators(customValidator);
        familiesItem.setTitleStyle("staticFormItemTitle");

        return familiesItem;
    }

    private RelatedResourceListItem createReplaceToVariablesItem() {
        return new SearchMultiRelatedResourceSimpleItem(VariableDS.REPLACE_TO_VARIABLES, getConstants().variableReplaceToVariables(), SrmWebConstants.FORM_LIST_MAX_RESULTS,
                getListRecordNavigationClickHandler()) {

            @Override
            protected void retrieveResources(int firstResult, int maxResults, MetamacWebCriteria webCriteria) {
                getUiHandlers().retrieveVariables(firstResult, maxResults, new VariableWebCriteria(webCriteria.getCriteria()));
            }
        };
    }

    private CustomToolStripButton createCreateVariableElementButton() {
        CustomToolStripButton createButton = new CustomToolStripButton(getConstants().actionNew(), RESOURCE.newListGrid().getURL());
        createButton.setVisible(CodesClientSecurityUtils.canCreateVariableElement());
        createButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (variableDto != null && variableDto.getType() == null && !BooleanUtils.isTrue(variableDto.getHasVariableElements())) {
                    // If it is not a geographical variable and the first variable element is going to be created, show an information message saying that once a variable had a variable element,
                    // cannot be turned into a geographical variable
                    final InformationWindow informationWindow = new InformationWindow(getConstants().variableElementCreate(),
                            getConstants().variableElementCreateInANonGeographicalVariableInfoMessage());
                    informationWindow.show();
                    informationWindow.getAcceptButtonHandlerRegistration().removeHandler();
                    informationWindow.getAcceptButton().addClickHandler(new ClickHandler() {

                        @Override
                        public void onClick(ClickEvent event) {
                            showNewVariableElementWindow();
                            informationWindow.markForDestroy();
                        }
                    });
                } else {
                    showNewVariableElementWindow();
                }
            }
        });
        return createButton;
    }

    private void showNewVariableElementWindow() {
        newVariableElementWindow = new NewVariableElementWindow(getConstants().variableElementCreate(), getUiHandlers(), variableDto);
        newVariableElementWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                if (newVariableElementWindow.validateForm()) {
                    VariableElementDto variableElementToCreate = newVariableElementWindow.getNewVariableElementDto();
                    getUiHandlers().createVariableElement(variableElementToCreate);
                    newVariableElementWindow.destroy();
                }
            }
        });
    }

    private CustomToolStripButton createImportVariableElementsButton() {
        CustomToolStripButton importButton = new CustomToolStripButton(getConstants().actionImportVariableElements(),
                org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.importResource().getURL());
        importButton.setVisible(CodesClientSecurityUtils.canImportVariableElements());
        importButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                importVariableElementsWindow.show();
            }
        });
        return importButton;
    }

    private CustomToolStripButton createExportVariableElementsButton() {
        CustomToolStripButton exportButton = new CustomToolStripButton(getConstants().actionExportVariableElements(),
                org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.exportResource().getURL());
        exportButton.setVisible(CodesClientSecurityUtils.canExportVariableElements());
        exportButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().exportVariableElements(variableDto.getUrn());
            }
        });
        return exportButton;
    }

    private CustomToolStripButton createDeleteVariableElementButton() {
        CustomToolStripButton deleteButton = new CustomToolStripButton(getConstants().actionDelete(), RESOURCE.deleteListGrid().getURL());
        deleteButton.setVisible(false);
        deleteButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });
        return deleteButton;
    }

    private CustomToolStripButton createFusionButton() {
        CustomToolStripButton fusionButton = new CustomToolStripButton(getConstants().actionFusion(), GlobalResources.RESOURCE.fusion().getURL());
        fusionButton.setVisible(false);
        fusionButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                createFusionWindow = new SearchRelatedResourcePaginatedWindow(getConstants().actionFusion(), SrmWebConstants.FORM_LIST_MAX_RESULTS, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        getUiHandlers().retrieveVariableElementsByVariableForFusionOperation(firstResult, maxResults, createFusionWindow.getRelatedResourceCriteria(), variableDto.getUrn());
                    }
                });

                // Load variable elements (to populate the selection window)
                getUiHandlers().retrieveVariableElementsByVariableForFusionOperation(0, SrmWebConstants.FORM_LIST_MAX_RESULTS, null, variableDto.getUrn());

                createFusionWindow.getListGridItem().getListGrid().setSelectionType(SelectionStyle.SINGLE);
                createFusionWindow.getListGridItem().setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                        getUiHandlers().retrieveVariableElementsByVariableForFusionOperation(firstResult, maxResults, criteria, variableDto.getUrn());
                    }
                });

                createFusionWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent arg0) {
                        RelatedResourceDto selectedVariableElement = createFusionWindow.getSelectedRelatedResource();
                        createFusionWindow.markForDestroy();
                        // Fusion the selected variable elements
                        List<String> sourceUrns = org.siemac.metamac.srm.web.code.utils.CommonUtils.getUrnsFromSelectedVariableElements(variableElementListGrid.getListGrid().getSelectedRecords());
                        String targetUrn = selectedVariableElement.getUrn();
                        getUiHandlers().createFusion(sourceUrns, targetUrn);
                    }
                });

            }
        });
        return fusionButton;
    }

    private CustomToolStripButton createSegregateButton() {
        CustomToolStripButton segregateButton = new CustomToolStripButton(getConstants().actionSegregate(), GlobalResources.RESOURCE.segregate().getURL());
        segregateButton.setValign(VerticalAlignment.CENTER);
        segregateButton.setVisible(false);
        segregateButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {

                createSegregationWindow = new SearchMultipleRelatedResourcePaginatedWindow(MetamacWebCommon.getConstants().resourceSelection(), SrmWebConstants.FORM_LIST_MAX_RESULTS,
                        new org.siemac.metamac.web.common.client.widgets.actions.search.SearchPaginatedAction<MetamacWebCriteria>() {

                            @Override
                            public void retrieveResultSet(int firstResult, int maxResults, MetamacWebCriteria webCriteria) {
                                getUiHandlers().retrieveVariableElementsByVariableForSegregationOperation(firstResult, maxResults, webCriteria.getCriteria(), variableDto.getUrn());
                            }
                        });

                createSegregationWindow.retrieveItems();

                createSegregationWindow.setSaveAction(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        List<RelatedResourceDto> selectedVariableElements = createSegregationWindow.getSelectedResources();
                        createSegregationWindow.markForDestroy();
                        // Segregate the selected variable elements
                        String sourceUrn = org.siemac.metamac.srm.web.code.utils.CommonUtils.getUrnsFromSelectedVariableElements(variableElementListGrid.getListGrid().getSelectedRecords()).get(0);
                        List<String> targetUrns = RelatedResourceUtils.getUrnsFromRelatedResourceDtos(selectedVariableElements);
                        getUiHandlers().createSegregation(sourceUrn, targetUrns);
                    }
                });
            }
        });
        return segregateButton;
    }

    //
    // VISIBILITY UPDATE
    //

    private void updateListGridDeleteButtonVisibility(ListGridRecord[] selectedRecords) {
        if (selectedRecords.length > 0) {
            showListGridDeleteButton(selectedRecords);
        } else {
            deleteVariableElementButton.hide();
        }
    }

    private void updateListGridFusionButtonVisibility(int selectedRecords) {
        if (selectedRecords > 1) {
            showListGridFusionButton();
        } else {
            fusionVariableElementButton.hide();
        }
    }

    private void updateListGridSegregateButtonVisibility(int selectedRecords) {
        if (selectedRecords == 1) {
            showListGridSegregateButton();
        } else {
            segregateVariableElementButton.hide();
        }
    }

    private void showListGridDeleteButton(ListGridRecord[] selectedRecords) {
        boolean allSelectedVariableElementsCanBeDeleted = true;
        for (ListGridRecord record : selectedRecords) {
            VariableElementBasicDto variableElement = ((VariableElementRecord) record).getVariableElementBasicDto();
            if (!CodesClientSecurityUtils.canDeleteVariableElement(variableElement)) {
                allSelectedVariableElementsCanBeDeleted = false;
                break;
            }
        }
        if (allSelectedVariableElementsCanBeDeleted) {
            deleteVariableElementButton.show();
        } else {
            deleteVariableElementButton.hide();
        }
    }

    private void showListGridFusionButton() {
        if (CodesClientSecurityUtils.canFusionVariableElements()) {
            fusionVariableElementButton.show();
        }
    }

    private void showListGridSegregateButton() {
        if (CodesClientSecurityUtils.canSegregateVariableElement()) {
            segregateVariableElementButton.show();
        }
    }

    private void updateVariableElementCreationAndImportationButtons(VariableDto variableDto) {
        if (CommonUtils.isVariableWorld(variableDto)) {
            createVariableElementButton.hide();
            importVariableElementButton.hide();
            exportVariableElementButton.hide();
        } else {
            if (CodesClientSecurityUtils.canCreateVariableElement()) {
                createVariableElementButton.show();
            }
            if (CodesClientSecurityUtils.canImportVariableElements()) {
                importVariableElementButton.show();
            }
            if (CodesClientSecurityUtils.canExportVariableElements()) {
                exportVariableElementButton.show();
            }
        }
    }

    //
    // RELATED RESOURCES
    //

    @Override
    public void setCodelistsForVariableElementGeographicalGranularity(GetRelatedResourcesResult result) {
        if (newVariableElementWindow != null) {
            newVariableElementWindow.setItemSchemes(result);
        }
    }

    @Override
    public void setCodesForVariableElementGeographicalGranularity(GetRelatedResourcesResult result) {
        if (newVariableElementWindow != null) {
            newVariableElementWindow.setItems(result);
        }
    }

    // ------------------------------------------------------------------------------------------------------------
    // FORM ITEM IF FUNCTIONS
    // ------------------------------------------------------------------------------------------------------------

    // IS GEOGRAPHICAL

    private FormItemIfFunction getIsGeographicalFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return CodesFormUtils.canVariableIsGeographicalBeEdited(variableDto);
            }
        };
    }

    private FormItemIfFunction getStaticIsGeographicalFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !CodesFormUtils.canVariableIsGeographicalBeEdited(variableDto);
            }
        };
    }

    // ------------------------------------------------------------------------------------------------------------
    // CLICK HANDLERS
    // ------------------------------------------------------------------------------------------------------------

    private ListRecordNavigationClickHandler getListRecordNavigationClickHandler() {
        return new ListRecordNavigationClickHandler() {

            @Override
            public BaseUiHandlers getBaseUiHandlers() {
                return getUiHandlers();
            }
        };
    }
}
