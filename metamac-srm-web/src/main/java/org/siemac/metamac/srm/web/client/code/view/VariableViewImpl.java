package org.siemac.metamac.srm.web.client.code.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.List;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.web.client.code.model.ds.VariableDS;
import org.siemac.metamac.srm.web.client.code.model.ds.VariableElementDS;
import org.siemac.metamac.srm.web.client.code.model.record.VariableElementRecord;
import org.siemac.metamac.srm.web.client.code.presenter.VariablePresenter;
import org.siemac.metamac.srm.web.client.code.view.handlers.VariableUiHandlers;
import org.siemac.metamac.srm.web.client.code.widgets.NewVariableElementWindow;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;
import org.siemac.metamac.srm.web.client.widgets.RelatedResourceListItem;
import org.siemac.metamac.srm.web.client.widgets.SearchMultipleRelatedResourceWindow;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementsResult;
import org.siemac.metamac.srm.web.shared.code.GetVariableFamiliesResult;
import org.siemac.metamac.srm.web.shared.code.GetVariablesResult;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.PaginatedCheckListGrid;
import org.siemac.metamac.web.common.client.widgets.TitleLabel;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
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
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class VariableViewImpl extends ViewWithUiHandlers<VariableUiHandlers> implements VariablePresenter.VariableView {

    private VLayout                             panel;
    private InternationalMainFormLayout         mainFormLayout;

    // View forms
    private GroupDynamicForm                    identifiersForm;
    private GroupDynamicForm                    contentDescriptorsForm;
    private GroupDynamicForm                    diffusionDescriptorsForm;

    // Edition forms
    private GroupDynamicForm                    identifiersEditionForm;
    private GroupDynamicForm                    contentDescriptorsEditionForm;
    private GroupDynamicForm                    diffusionDescriptorsEditionForm;

    private SearchMultipleRelatedResourceWindow searchFamiliesWindow;
    private SearchMultipleRelatedResourceWindow searchReplaceToVariablesWindow;

    private PaginatedCheckListGrid              variableElementListGrid;
    private ToolStripButton                     createVariableElementButton;
    private ToolStripButton                     deleteVariableElementButton;
    private NewVariableElementWindow            newVariableElementWindow;
    private DeleteConfirmationWindow            deleteConfirmationWindow;

    private VariableDto                         variableDto;

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
        toolStrip.addButton(createVariableElementButton);
        toolStrip.addButton(deleteVariableElementButton);

        // ListGrid

        variableElementListGrid = new PaginatedCheckListGrid(VariablePresenter.VARIABLE_LIST_MAX_RESULTS, new PaginatedAction() {

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
                if (variableElementListGrid.getListGrid().getSelectedRecords().length > 0) {
                    // Show delete button
                    showListGridDeleteButton();
                } else {
                    deleteVariableElementButton.hide();
                }
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
                        org.siemac.metamac.srm.web.client.code.utils.CommonUtils.getUrnsFromSelectedVariableElements(variableElementListGrid.getListGrid().getSelectedRecords()));
                deleteConfirmationWindow.hide();
            }
        });

        VLayout codelistsLayout = new VLayout();
        codelistsLayout.setMargin(15);
        codelistsLayout.addMember(new TitleLabel(getConstants().variableVariableElements()));
        codelistsLayout.addMember(toolStrip);
        codelistsLayout.addMember(variableElementListGrid);

        panel.addMember(mainFormLayout);
        panel.addMember(codelistsLayout);
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
        RelatedResourceListItem replaceToVariables = new RelatedResourceListItem(VariableDS.REPLACE_TO_VARIABLES, getConstants().variableReplaceToVariables(), false);
        ViewTextItem replacedByVariable = new ViewTextItem(VariableDS.REPLACED_BY, getConstants().variableReplacedByVariable());
        diffusionDescriptorsForm.setFields(replaceToVariables, replacedByVariable);

        mainFormLayout.addViewCanvas(identifiersForm);
        mainFormLayout.addViewCanvas(contentDescriptorsForm);
        mainFormLayout.addViewCanvas(diffusionDescriptorsForm);
    }

    private void createEditionForm() {
        // Identifiers
        identifiersEditionForm = new GroupDynamicForm(getConstants().formIdentifiers());
        RequiredTextItem code = new RequiredTextItem(VariableDS.CODE, getConstants().identifiableArtefactCode());
        code.setValidators(CommonWebUtils.getSemanticIdentifierCustomValidator());
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
        RelatedResourceListItem replaceToVariables = createReplaceToVariablesItem();
        ViewTextItem replacedByVariable = new ViewTextItem(VariableDS.REPLACED_BY, getConstants().variableReplacedByVariable());
        diffusionDescriptorsEditionForm.setFields(replaceToVariables, replacedByVariable);

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
        ((RelatedResourceListItem) diffusionDescriptorsForm.getItem(VariableDS.REPLACE_TO_VARIABLES)).setRelatedResources(variableDto.getReplaceToVariables());
        diffusionDescriptorsForm.setValue(VariableDS.REPLACED_BY, CommonUtils.getRelatedResourceName(variableDto.getReplacedByVariable()));
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
        ((RelatedResourceListItem) diffusionDescriptorsEditionForm.getItem(VariableDS.REPLACE_TO_VARIABLES)).setRelatedResources(variableDto.getReplaceToVariables());
        diffusionDescriptorsEditionForm.setValue(VariableDS.REPLACED_BY, CommonUtils.getRelatedResourceName(variableDto.getReplacedByVariable()));
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

    private void setVariableElements(List<VariableElementDto> variableElementDtos) {
        VariableElementRecord[] records = new VariableElementRecord[variableElementDtos.size()];
        int index = 0;
        for (VariableElementDto element : variableElementDtos) {
            records[index++] = org.siemac.metamac.srm.web.client.code.utils.RecordUtils.getVariableElementRecord(element);
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
                searchFamiliesWindow = new SearchMultipleRelatedResourceWindow(getConstants().familiesSelection(), MAX_RESULTS, new PaginatedAction() {

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
                searchReplaceToVariablesWindow = new SearchMultipleRelatedResourceWindow(getConstants().variablesSelection(), MAX_RESULTS, new PaginatedAction() {

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

    private void showListGridDeleteButton() {
        deleteVariableElementButton.show();
    }

    private ToolStripButton createCreateVariableElementButton() {
        ToolStripButton createButton = new ToolStripButton(getConstants().actionNew(), RESOURCE.newListGrid().getURL());
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

    private ToolStripButton createDeleteVariableElementButton() {
        ToolStripButton deleteButton = new ToolStripButton(getConstants().actionDelete(), RESOURCE.deleteListGrid().getURL());
        deleteButton.setVisibility(Visibility.HIDDEN);
        deleteButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });
        return deleteButton;
    }
}
