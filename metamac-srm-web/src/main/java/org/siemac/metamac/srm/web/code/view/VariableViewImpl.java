package org.siemac.metamac.srm.web.code.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.List;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementOperationDto;
import org.siemac.metamac.srm.web.client.resources.GlobalResources;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.client.widgets.RelatedResourceListItem;
import org.siemac.metamac.srm.web.client.widgets.SearchMultipleRelatedResourcePaginatedWindow;
import org.siemac.metamac.srm.web.client.widgets.SearchRelatedResourcePaginatedWindow;
import org.siemac.metamac.srm.web.code.model.ds.VariableDS;
import org.siemac.metamac.srm.web.code.model.ds.VariableElementDS;
import org.siemac.metamac.srm.web.code.model.record.VariableElementRecord;
import org.siemac.metamac.srm.web.code.presenter.VariablePresenter;
import org.siemac.metamac.srm.web.code.view.handlers.VariableUiHandlers;
import org.siemac.metamac.srm.web.code.widgets.NewVariableElementWindow;
import org.siemac.metamac.srm.web.code.widgets.VariableElementOperationLayout;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementsResult;
import org.siemac.metamac.srm.web.shared.code.GetVariableFamiliesResult;
import org.siemac.metamac.srm.web.shared.code.GetVariablesResult;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.utils.DateUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.CustomToolStripButton;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.PaginatedCheckListGrid;
import org.siemac.metamac.web.common.client.widgets.TitleLabel;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomDateItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.form.validator.CustomValidator;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

public class VariableViewImpl extends ViewWithUiHandlers<VariableUiHandlers> implements VariablePresenter.VariableView {

    private VLayout                                      panel;
    private InternationalMainFormLayout                  mainFormLayout;

    // View forms
    private GroupDynamicForm                             identifiersForm;
    private GroupDynamicForm                             contentDescriptorsForm;
    private GroupDynamicForm                             diffusionDescriptorsForm;

    // Edition forms
    private GroupDynamicForm                             identifiersEditionForm;
    private GroupDynamicForm                             contentDescriptorsEditionForm;
    private GroupDynamicForm                             diffusionDescriptorsEditionForm;

    private SearchMultipleRelatedResourcePaginatedWindow searchFamiliesWindow;
    private SearchMultipleRelatedResourcePaginatedWindow searchReplaceToVariablesWindow;

    // Variable elements

    private PaginatedCheckListGrid                       variableElementListGrid;
    private CustomToolStripButton                        createVariableElementButton;
    private CustomToolStripButton                        deleteVariableElementButton;
    private CustomToolStripButton                        fusionVariableElementButton;
    private CustomToolStripButton                        segregateVariableElementButton;
    private NewVariableElementWindow                     newVariableElementWindow;
    private DeleteConfirmationWindow                     deleteConfirmationWindow;

    private SearchRelatedResourcePaginatedWindow         createFusionWindow;
    private SearchMultipleRelatedResourcePaginatedWindow createSegregationWindow;

    // Variable element operations

    private VariableElementOperationLayout               variableElementOperationsLayout;

    private VariableDto                                  variableDto;

    @Inject
    public VariableViewImpl() {
        super();
        panel = new VLayout();
        panel.setHeight100();
        panel.setOverflow(Overflow.SCROLL);

        //
        // VARIABLE
        //

        mainFormLayout = new InternationalMainFormLayout(); // TODO Security
        bindMainFormLayoutEvents();
        createViewForm();
        createEditionForm();

        // VARIABLE ELEMENTS

        // ToolStrip

        ToolStrip toolStrip = new ToolStrip();
        toolStrip.setWidth100();
        createVariableElementButton = createCreateVariableElementButton();
        deleteVariableElementButton = createDeleteVariableElementButton();
        fusionVariableElementButton = createFusionButton();
        segregateVariableElementButton = createSegregateButton();
        toolStrip.addButton(createVariableElementButton);
        toolStrip.addButton(deleteVariableElementButton);
        toolStrip.addButton(fusionVariableElementButton);
        toolStrip.addButton(segregateVariableElementButton);

        // ListGrid

        variableElementListGrid = new PaginatedCheckListGrid(VariablePresenter.ELEMENT_LIST_MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveVariableElementsByVariable(firstResult, maxResults, null, variableDto.getUrn());
            }
        });
        variableElementListGrid.getListGrid().setAutoFitData(Autofit.VERTICAL);
        variableElementListGrid.getListGrid().setDataSource(new VariableElementDS());
        variableElementListGrid.getListGrid().setUseAllDataSourceFields(false);
        variableElementListGrid.getListGrid().addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                int selectedRecords = variableElementListGrid.getListGrid().getSelectedRecords().length;
                updateListGridDeleteButtonVisibility(selectedRecords);
                updateListGridFusionButtonVisibility(selectedRecords);
                updateListGridSegregateButtonVisibility(selectedRecords);

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
        ListGridField fieldName = new ListGridField(VariableElementDS.NAME, getConstants().nameableArtefactName());
        ListGridField urn = new ListGridField(VariableElementDS.URN, getConstants().identifiableArtefactUrn());
        variableElementListGrid.getListGrid().setFields(fieldCode, fieldName, urn);

        // Remove window

        deleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().variableElementDeleteConfirmationTitle(), getConstants().variableElementDeleteConfirmation());
        deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deleteVariableElements(
                        org.siemac.metamac.srm.web.code.utils.CommonUtils.getUrnsFromSelectedVariableElements(variableElementListGrid.getListGrid().getSelectedRecords()));
                deleteConfirmationWindow.hide();
            }
        });

        // VARIABLE ELEMENT OPERATIONS

        variableElementOperationsLayout = new VariableElementOperationLayout(getConstants().variableOperationsBetweenElements());
        variableElementOperationsLayout.setStyleName("marginTop15");

        VLayout layout = new VLayout();
        layout.setMargin(15);
        layout.addMember(new TitleLabel(getConstants().variableVariableElements()));
        layout.addMember(toolStrip);
        layout.addMember(variableElementListGrid);
        layout.addMember(variableElementOperationsLayout);

        panel.addMember(mainFormLayout);
        panel.addMember(layout);
    }

    @Override
    public void setUiHandlers(VariableUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        variableElementOperationsLayout.setUiHandlers(uiHandlers);
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
                    getUiHandlers().saveVariable(getVariableDto());
                }
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

        String defaultLocalized = InternationalStringUtils.getLocalisedString(variableDto.getName());
        String title = defaultLocalized != null ? defaultLocalized : StringUtils.EMPTY;
        mainFormLayout.setTitleLabelContents(title);

        mainFormLayout.setViewMode();

        setVariableViewMode(variableDto);
        setVariableEditionMode(variableDto);
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
        RelatedResourceListItem families = new RelatedResourceListItem(VariableDS.FAMILIES, getConstants().variableFamilies(), false);
        contentDescriptorsForm.setFields(families);

        // Diffusion descriptors
        diffusionDescriptorsForm = new GroupDynamicForm(getConstants().formDiffusionDescriptors());
        ViewTextItem validFrom = new ViewTextItem(VariableDS.VALID_FROM, getConstants().variableValidFrom());
        ViewTextItem validTo = new ViewTextItem(VariableDS.VALID_TO, getConstants().variableValidTo());
        RelatedResourceListItem replaceToVariables = new RelatedResourceListItem(VariableDS.REPLACE_TO_VARIABLES, getConstants().variableReplaceToVariables(), false);
        ViewTextItem replacedByVariable = new ViewTextItem(VariableDS.REPLACED_BY_VARIABLE, getConstants().variableReplacedByVariable());
        diffusionDescriptorsForm.setFields(validFrom, validTo, replaceToVariables, replacedByVariable);

        mainFormLayout.addViewCanvas(identifiersForm);
        mainFormLayout.addViewCanvas(contentDescriptorsForm);
        mainFormLayout.addViewCanvas(diffusionDescriptorsForm);
    }

    private void createEditionForm() {
        // Identifiers
        identifiersEditionForm = new GroupDynamicForm(getConstants().formIdentifiers());
        RequiredTextItem code = new RequiredTextItem(VariableDS.CODE, getConstants().identifiableArtefactCode());
        code.setValidators(SemanticIdentifiersUtils.getVariableIdentifierCustomValidator());
        MultiLanguageTextItem name = new MultiLanguageTextItem(VariableDS.NAME, getConstants().nameableArtefactName());
        name.setRequired(true);
        MultiLanguageTextItem shortName = new MultiLanguageTextItem(VariableDS.SHORT_NAME, getConstants().variableShortName());
        name.setRequired(true);
        ViewTextItem urn = new ViewTextItem(VariableDS.URN, getConstants().identifiableArtefactUrn());
        identifiersEditionForm.setFields(code, name, shortName, urn);

        // Content descriptors
        contentDescriptorsEditionForm = new GroupDynamicForm(getConstants().formContentDescriptors());
        RelatedResourceListItem families = createFamiliesItem();
        contentDescriptorsEditionForm.setFields(families);

        // Diffusion descriptors
        diffusionDescriptorsEditionForm = new GroupDynamicForm(getConstants().formDiffusionDescriptors());
        CustomDateItem validFrom = new CustomDateItem(VariableDS.VALID_FROM, getConstants().variableValidFrom());
        CustomDateItem validTo = new CustomDateItem(VariableDS.VALID_TO, getConstants().variableValidTo());
        RelatedResourceListItem replaceToVariables = createReplaceToVariablesItem();
        ViewTextItem replacedByVariable = new ViewTextItem(VariableDS.REPLACED_BY_VARIABLE, getConstants().variableReplacedByVariable());
        diffusionDescriptorsEditionForm.setFields(validFrom, validTo, replaceToVariables, replacedByVariable);

        mainFormLayout.addEditionCanvas(identifiersEditionForm);
        mainFormLayout.addEditionCanvas(contentDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(diffusionDescriptorsEditionForm);
    }

    public void setEditionMode() {
        mainFormLayout.setEditionMode();
    }

    public void setVariableViewMode(VariableDto variableDto) {
        // Identifiers
        identifiersForm.setValue(VariableDS.CODE, variableDto.getCode());
        identifiersForm.setValue(VariableDS.URN, variableDto.getUrn());
        identifiersForm.setValue(VariableDS.NAME, RecordUtils.getInternationalStringRecord(variableDto.getName()));
        identifiersForm.setValue(VariableDS.SHORT_NAME, RecordUtils.getInternationalStringRecord(variableDto.getShortName()));

        // Content descriptors
        ((RelatedResourceListItem) contentDescriptorsForm.getItem(VariableDS.FAMILIES)).setRelatedResources(variableDto.getFamilies());

        // Diffusion descriptors
        diffusionDescriptorsForm.setValue(VariableDS.VALID_FROM, DateUtils.getFormattedDate(variableDto.getValidFrom()));
        diffusionDescriptorsForm.setValue(VariableDS.VALID_TO, DateUtils.getFormattedDate(variableDto.getValidTo()));
        ((RelatedResourceListItem) diffusionDescriptorsForm.getItem(VariableDS.REPLACE_TO_VARIABLES)).setRelatedResources(variableDto.getReplaceToVariables());
        diffusionDescriptorsForm.setValue(VariableDS.REPLACED_BY_VARIABLE, CommonUtils.getRelatedResourceName(variableDto.getReplacedByVariable()));
    }

    public void setVariableEditionMode(VariableDto variableDto) {
        // Identifiers
        identifiersEditionForm.setValue(VariableDS.CODE, variableDto.getCode());
        identifiersEditionForm.setValue(VariableDS.URN, variableDto.getUrn());
        identifiersEditionForm.setValue(VariableDS.NAME, RecordUtils.getInternationalStringRecord(variableDto.getName()));
        identifiersEditionForm.setValue(VariableDS.SHORT_NAME, RecordUtils.getInternationalStringRecord(variableDto.getShortName()));

        // Content descriptors
        ((RelatedResourceListItem) contentDescriptorsEditionForm.getItem(VariableDS.FAMILIES)).setRelatedResources(variableDto.getFamilies());

        // Diffusion descriptors
        diffusionDescriptorsEditionForm.setValue(VariableDS.VALID_FROM, variableDto.getValidFrom());
        diffusionDescriptorsEditionForm.setValue(VariableDS.VALID_TO, variableDto.getValidTo());
        ((RelatedResourceListItem) diffusionDescriptorsEditionForm.getItem(VariableDS.REPLACE_TO_VARIABLES)).setRelatedResources(variableDto.getReplaceToVariables());
        diffusionDescriptorsEditionForm.setValue(VariableDS.REPLACED_BY_VARIABLE, CommonUtils.getRelatedResourceName(variableDto.getReplacedByVariable()));
    }

    public VariableDto getVariableDto() {
        // Identifiers
        variableDto.setCode(identifiersEditionForm.getValueAsString(VariableDS.CODE));
        variableDto.setName((InternationalStringDto) identifiersEditionForm.getValue(VariableDS.NAME));
        variableDto.setShortName((InternationalStringDto) identifiersEditionForm.getValue(VariableDS.SHORT_NAME));

        // Content descriptors
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
        if (searchFamiliesWindow != null) {
            searchFamiliesWindow.setSourceRelatedResources(RelatedResourceUtils.getRelatedResourceDtosFromVariableFamilyDtos(result.getFamilies()));
            searchFamiliesWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getFamilies().size(), result.getTotalResults());
        }
    }

    @Override
    public void setVariables(GetVariablesResult result) {
        if (searchReplaceToVariablesWindow != null) {
            searchReplaceToVariablesWindow.setSourceRelatedResources(RelatedResourceUtils.getRelatedResourceDtosFromVariableDtos(result.getVariables()));
            searchReplaceToVariablesWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getVariables().size(), result.getTotalResults());
        }
    }

    @Override
    public void setVariableElements(GetVariableElementsResult result) {
        setVariableElements(result.getVariableElements());
        variableElementListGrid.refreshPaginationInfo(result.getFirstResultOut(), result.getVariableElements().size(), result.getTotalResults());
    }

    @Override
    public void setVariableElementsForFusion(GetVariableElementsResult result) {
        if (createFusionWindow != null) {
            createFusionWindow.setRelatedResources(RelatedResourceUtils.getRelatedResourceDtosFromVariableElementDtos(result.getVariableElements()));
            createFusionWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getVariableElements().size(), result.getTotalResults());
        }
    }

    @Override
    public void setVariableElementsForSegregation(GetVariableElementsResult result) {
        if (createSegregationWindow != null) {
            createSegregationWindow.setSourceRelatedResources(RelatedResourceUtils.getRelatedResourceDtosFromVariableElementDtos(result.getVariableElements()));
            createSegregationWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getVariableElements().size(), result.getTotalResults());
        }
    }

    @Override
    public void setVariableElementOperations(List<VariableElementOperationDto> variableElementOperationDtos) {
        variableElementOperationsLayout.setVariableElementOperations(variableElementOperationDtos);
    }

    private void setVariableElements(List<VariableElementDto> variableElementDtos) {
        VariableElementRecord[] records = new VariableElementRecord[variableElementDtos.size()];
        int index = 0;
        for (VariableElementDto element : variableElementDtos) {
            records[index++] = org.siemac.metamac.srm.web.code.utils.RecordUtils.getVariableElementRecord(element);
        }
        variableElementListGrid.getListGrid().setData(records);
    }

    private RelatedResourceListItem createFamiliesItem() {
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;

        final RelatedResourceListItem familiesItem = new RelatedResourceListItem(VariableDS.FAMILIES, getConstants().variableFamilies(), true);
        familiesItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent arg0) {
                searchFamiliesWindow = new SearchMultipleRelatedResourcePaginatedWindow(getConstants().familiesSelection(), MAX_RESULTS, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        getUiHandlers().retrieveVariableFamilies(firstResult, maxResults, searchFamiliesWindow.getRelatedResourceCriteria());
                    }
                });

                // Load the list of families
                getUiHandlers().retrieveVariableFamilies(FIRST_RESULST, MAX_RESULTS, null);

                // Set the selected families
                List<RelatedResourceDto> selectedFamilies = ((RelatedResourceListItem) contentDescriptorsEditionForm.getItem(VariableDS.FAMILIES)).getRelatedResourceDtos();
                searchFamiliesWindow.setTargetRelatedResources(selectedFamilies);

                searchFamiliesWindow.setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                        getUiHandlers().retrieveVariableFamilies(firstResult, maxResults, criteria);
                    }
                });
                searchFamiliesWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent arg0) {
                        List<RelatedResourceDto> selectedFamilies = searchFamiliesWindow.getSelectedRelatedResources();
                        searchFamiliesWindow.markForDestroy();
                        // Set selected families in form
                        ((RelatedResourceListItem) contentDescriptorsEditionForm.getItem(VariableDS.FAMILIES)).setRelatedResources(selectedFamilies);
                    }
                });
            }
        });

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
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;

        RelatedResourceListItem replaceToItem = new RelatedResourceListItem(VariableDS.REPLACE_TO_VARIABLES, getConstants().variableReplaceToVariables(), true);
        replaceToItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                searchReplaceToVariablesWindow = new SearchMultipleRelatedResourcePaginatedWindow(getConstants().variablesSelection(), MAX_RESULTS, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        getUiHandlers().retrieveVariables(firstResult, maxResults, searchReplaceToVariablesWindow.getRelatedResourceCriteria());
                    }
                });

                // Load variables
                getUiHandlers().retrieveVariables(FIRST_RESULST, MAX_RESULTS, null);

                // Set the selected variables
                List<RelatedResourceDto> selectedVariables = ((RelatedResourceListItem) diffusionDescriptorsEditionForm.getItem(VariableDS.REPLACE_TO_VARIABLES)).getRelatedResourceDtos();
                searchReplaceToVariablesWindow.setTargetRelatedResources(selectedVariables);

                searchReplaceToVariablesWindow.setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                        getUiHandlers().retrieveVariables(firstResult, maxResults, criteria);
                    }
                });

                searchReplaceToVariablesWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        List<RelatedResourceDto> selectedVariables = searchReplaceToVariablesWindow.getSelectedRelatedResources();
                        searchReplaceToVariablesWindow.markForDestroy();
                        // Set selected variables in form
                        ((RelatedResourceListItem) diffusionDescriptorsEditionForm.getItem(VariableDS.REPLACE_TO_VARIABLES)).setRelatedResources(selectedVariables);
                    }
                });
            }
        });
        return replaceToItem;
    }

    private CustomToolStripButton createCreateVariableElementButton() {
        CustomToolStripButton createButton = new CustomToolStripButton(getConstants().actionNew(), RESOURCE.newListGrid().getURL());
        createButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                newVariableElementWindow = new NewVariableElementWindow(getConstants().variableElementCreate());
                newVariableElementWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (newVariableElementWindow.validateForm()) {
                            VariableElementDto variableElementToCreate = newVariableElementWindow.getNewVariableElementDto();
                            variableElementToCreate.setVariable(RelatedResourceUtils.createRelatedResourceDto(variableDto.getUrn()));
                            getUiHandlers().createVariableElement(variableElementToCreate);
                            newVariableElementWindow.destroy();
                        }
                    }
                });
            }
        });
        return createButton;
    }

    private CustomToolStripButton createDeleteVariableElementButton() {
        CustomToolStripButton deleteButton = new CustomToolStripButton(getConstants().actionDelete(), RESOURCE.deleteListGrid().getURL());
        deleteButton.setVisibility(Visibility.HIDDEN);
        deleteButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });
        return deleteButton;
    }

    private CustomToolStripButton createFusionButton() {
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;
        CustomToolStripButton fusionButton = new CustomToolStripButton(getConstants().actionFusion(), GlobalResources.RESOURCE.fusion().getURL());
        fusionButton.setVisibility(Visibility.HIDDEN);
        fusionButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                createFusionWindow = new SearchRelatedResourcePaginatedWindow(getConstants().actionFusion(), MAX_RESULTS, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        getUiHandlers().retrieveVariableElementsByVariableForFusionOperation(firstResult, maxResults, createFusionWindow.getRelatedResourceCriteria(), variableDto.getUrn());
                    }
                });

                // Load variable elements (to populate the selection window)
                getUiHandlers().retrieveVariableElementsByVariableForFusionOperation(FIRST_RESULST, MAX_RESULTS, null, variableDto.getUrn());

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
                        List<String> sourceUrns = org.siemac.metamac.srm.web.code.utils.CommonUtils.getUrnsFromSelectedVariableElements(variableElementListGrid.getListGrid()
                                .getSelectedRecords());
                        String targetUrn = selectedVariableElement.getUrn();
                        getUiHandlers().createFusion(sourceUrns, targetUrn);
                    }
                });

            }
        });
        return fusionButton;
    }

    private CustomToolStripButton createSegregateButton() {
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;
        CustomToolStripButton segregateButton = new CustomToolStripButton(getConstants().actionSegregate(), GlobalResources.RESOURCE.segregate().getURL());
        segregateButton.setValign(VerticalAlignment.CENTER);
        segregateButton.setVisibility(Visibility.HIDDEN);
        segregateButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                createSegregationWindow = new SearchMultipleRelatedResourcePaginatedWindow(getConstants().actionSegregate(), MAX_RESULTS, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        getUiHandlers().retrieveVariableElementsByVariableForSegregationOperation(firstResult, maxResults, createSegregationWindow.getRelatedResourceCriteria(), variableDto.getUrn());
                    }
                });

                // Load variable elements (to populate the selection window)
                getUiHandlers().retrieveVariableElementsByVariableForSegregationOperation(FIRST_RESULST, MAX_RESULTS, null, variableDto.getUrn());

                createSegregationWindow.setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                        getUiHandlers().retrieveVariableElementsByVariableForSegregationOperation(firstResult, maxResults, criteria, variableDto.getUrn());
                    }
                });
                createSegregationWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent arg0) {
                        List<RelatedResourceDto> selectedVariableElements = createSegregationWindow.getSelectedRelatedResources();
                        createSegregationWindow.markForDestroy();
                        // Segregate the selected variable elements
                        String sourceUrn = org.siemac.metamac.srm.web.code.utils.CommonUtils.getUrnsFromSelectedVariableElements(variableElementListGrid.getListGrid().getSelectedRecords())
                                .get(0);
                        List<String> targetUrns = RelatedResourceUtils.getUrnsFromRelatedResourceDtos(selectedVariableElements);
                        getUiHandlers().createSegregation(sourceUrn, targetUrns);
                    }
                });
            }
        });
        return segregateButton;
    }

    private void updateListGridDeleteButtonVisibility(int selectedRecords) {
        if (selectedRecords > 0) {
            deleteVariableElementButton.show();
        } else {
            deleteVariableElementButton.hide();
        }
    }

    private void updateListGridFusionButtonVisibility(int selectedRecords) {
        if (selectedRecords > 1) {
            fusionVariableElementButton.show();
        } else {
            fusionVariableElementButton.hide();
        }
    }

    private void updateListGridSegregateButtonVisibility(int selectedRecords) {
        if (selectedRecords == 1) {
            segregateVariableElementButton.show();
        } else {
            segregateVariableElementButton.hide();
        }
    }
}