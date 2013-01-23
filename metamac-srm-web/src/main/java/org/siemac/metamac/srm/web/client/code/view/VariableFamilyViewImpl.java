package org.siemac.metamac.srm.web.client.code.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.List;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.core.code.dto.VariableFamilyDto;
import org.siemac.metamac.srm.web.client.code.model.ds.VariableDS;
import org.siemac.metamac.srm.web.client.code.model.ds.VariableFamilyDS;
import org.siemac.metamac.srm.web.client.code.model.record.VariableRecord;
import org.siemac.metamac.srm.web.client.code.presenter.VariableFamilyPresenter;
import org.siemac.metamac.srm.web.client.code.utils.CommonUtils;
import org.siemac.metamac.srm.web.client.code.view.handlers.VariableFamilyUiHandlers;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.client.widgets.SearchMultipleRelatedResourcePaginatedWindow;
import org.siemac.metamac.srm.web.shared.code.GetVariablesResult;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
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
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class VariableFamilyViewImpl extends ViewWithUiHandlers<VariableFamilyUiHandlers> implements VariableFamilyPresenter.VariableFamilyView {

    private VLayout                             panel;
    private InternationalMainFormLayout         mainFormLayout;

    // View forms
    private GroupDynamicForm                    identifiersForm;

    // Edition forms
    private GroupDynamicForm                    identifiersEditionForm;

    private PaginatedCheckListGrid              variableListGrid;
    private ToolStripButton                     addVariableToFamilyButton;
    private ToolStripButton                     removeVariableToFamilyButton;
    private SearchMultipleRelatedResourcePaginatedWindow variablesWindow;
    private DeleteConfirmationWindow            removeConfirmationWindow;

    private VariableFamilyDto                   variableFamilyDto;

    @Inject
    public VariableFamilyViewImpl() {
        super();
        panel = new VLayout();
        panel.setHeight100();
        panel.setOverflow(Overflow.SCROLL);

        //
        // VARIABLE FAMILY
        //

        mainFormLayout = new InternationalMainFormLayout(); // TODO Security
        bindMainFormLayoutEvents();
        createViewForm();
        createEditionForm();

        // VARIABLES

        // ToolStrip

        ToolStrip toolStrip = new ToolStrip();
        toolStrip.setWidth100();
        addVariableToFamilyButton = createAddVariableToFamilyButton();
        removeVariableToFamilyButton = createRemoveVariableToFamilyButton();
        toolStrip.addButton(addVariableToFamilyButton);
        toolStrip.addButton(removeVariableToFamilyButton);

        // ListGrid

        variableListGrid = new PaginatedCheckListGrid(VariableFamilyPresenter.VARIABLE_LIST_MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveVariablesByFamily(firstResult, maxResults, null, variableFamilyDto.getUrn());
            }
        });
        variableListGrid.getListGrid().setAutoFitData(Autofit.VERTICAL);
        variableListGrid.getListGrid().setDataSource(new VariableDS());
        variableListGrid.getListGrid().setUseAllDataSourceFields(false);
        variableListGrid.getListGrid().addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (variableListGrid.getListGrid().getSelectedRecords().length > 0) {
                    // Show delete button
                    showListGridRemoveButton();
                } else {
                    removeVariableToFamilyButton.hide();
                }
            }
        });
        variableListGrid.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // Clicking checkBox will be ignored
                    String urn = ((VariableRecord) event.getRecord()).getAttribute(VariableDS.URN);
                    getUiHandlers().goToVariable(urn);
                }
            }
        });
        ListGridField fieldCode = new ListGridField(VariableDS.CODE, getConstants().identifiableArtefactCode());
        fieldCode.setAlign(Alignment.LEFT);
        ListGridField fieldName = new ListGridField(VariableDS.NAME, getConstants().nameableArtefactName());
        ListGridField urn = new ListGridField(VariableDS.URN, getConstants().identifiableArtefactUrn());
        variableListGrid.getListGrid().setFields(fieldCode, fieldName, urn);

        // Remove window

        removeConfirmationWindow = new DeleteConfirmationWindow(getConstants().variableRemoveFromFamilyConfirmationTitle(), getConstants().variableRemoveFromFamilyConfirmation());
        removeConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().removeVariablesFromFamily(CommonUtils.getUrnsFromSelectedVariables(variableListGrid.getListGrid().getSelectedRecords()), variableFamilyDto.getUrn());
                removeConfirmationWindow.hide();
            }
        });

        VLayout variablesLayout = new VLayout();
        variablesLayout.setMargin(15);
        variablesLayout.addMember(new TitleLabel(getConstants().variableFamilyVariables()));
        variablesLayout.addMember(toolStrip);
        variablesLayout.addMember(variableListGrid);

        panel.addMember(mainFormLayout);
        panel.addMember(variablesLayout);

    }

    private void bindMainFormLayoutEvents() {
        mainFormLayout.getTranslateToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                boolean translationsShowed = mainFormLayout.getTranslateToolStripButton().isSelected();
                identifiersForm.setTranslationsShowed(translationsShowed);
                identifiersEditionForm.setTranslationsShowed(translationsShowed);
            }
        });

        // Save
        mainFormLayout.getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (identifiersEditionForm.validate(false)) {
                    getUiHandlers().saveVariableFamily(getVariableFamilyDto());
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
        if (slot == VariableFamilyPresenter.TYPE_SetContextAreaContentCodesToolBar) {
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
    public void setVariableFamily(VariableFamilyDto family) {
        this.variableFamilyDto = family;

        String defaultLocalized = InternationalStringUtils.getLocalisedString(family.getName());
        String title = defaultLocalized != null ? defaultLocalized : StringUtils.EMPTY;
        mainFormLayout.setTitleLabelContents(title);

        mainFormLayout.setViewMode();

        setVariableFamilyViewMode(family);
        setVariableFamilyEditionMode(family);
    }

    private void createViewForm() {
        // Identifiers Form
        identifiersForm = new GroupDynamicForm(getConstants().formIdentifiers());
        ViewTextItem code = new ViewTextItem(VariableFamilyDS.CODE, getConstants().identifiableArtefactCode());
        ViewMultiLanguageTextItem name = new ViewMultiLanguageTextItem(VariableFamilyDS.NAME, getConstants().nameableArtefactName());
        ViewTextItem urn = new ViewTextItem(VariableFamilyDS.URN, getConstants().identifiableArtefactUrn());
        identifiersForm.setFields(code, name, urn);

        mainFormLayout.addViewCanvas(identifiersForm);
    }

    private void createEditionForm() {
        // Identifiers
        identifiersEditionForm = new GroupDynamicForm(getConstants().formIdentifiers());
        RequiredTextItem code = new RequiredTextItem(VariableFamilyDS.CODE, getConstants().identifiableArtefactCode());
        code.setValidators(SemanticIdentifiersUtils.getVariableFamilyIdentifierCustomValidator());
        MultiLanguageTextItem name = new MultiLanguageTextItem(VariableFamilyDS.NAME, getConstants().nameableArtefactName());
        name.setRequired(true);
        ViewTextItem urn = new ViewTextItem(VariableFamilyDS.URN, getConstants().identifiableArtefactUrn());
        identifiersEditionForm.setFields(code, name, urn);

        mainFormLayout.addEditionCanvas(identifiersEditionForm);
    }

    public void setEditionMode() {
        mainFormLayout.setEditionMode();
    }

    public void setVariableFamilyViewMode(VariableFamilyDto variableFamilyDto) {
        // Identifiers
        identifiersForm.setValue(VariableFamilyDS.CODE, variableFamilyDto.getCode());
        identifiersForm.setValue(VariableFamilyDS.URN, variableFamilyDto.getUrn());
        identifiersForm.setValue(VariableFamilyDS.NAME, RecordUtils.getInternationalStringRecord(variableFamilyDto.getName()));
    }

    public void setVariableFamilyEditionMode(VariableFamilyDto variableFamilyDto) {
        // Identifiers
        identifiersEditionForm.setValue(VariableFamilyDS.CODE, variableFamilyDto.getCode());
        identifiersEditionForm.setValue(VariableFamilyDS.URN, variableFamilyDto.getUrn());
        identifiersEditionForm.setValue(VariableFamilyDS.NAME, RecordUtils.getInternationalStringRecord(variableFamilyDto.getName()));
    }

    public VariableFamilyDto getVariableFamilyDto() {
        // Identifiers
        variableFamilyDto.setCode(identifiersEditionForm.getValueAsString(VariableFamilyDS.CODE));
        variableFamilyDto.setName((InternationalStringDto) identifiersEditionForm.getValue(VariableFamilyDS.NAME));

        return variableFamilyDto;
    }

    @Override
    public void setVariables(GetVariablesResult result) {
        if (variablesWindow != null) {
            variablesWindow.setSourceRelatedResources(RelatedResourceUtils.getRelatedResourceDtosFromVariableDtos(result.getVariables()));
            variablesWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getVariables().size(), result.getTotalResults());
        }
    }

    @Override
    public void setVariablesOfFamily(GetVariablesResult result) {
        setVariablesOfFamily(result.getVariables());
        variableListGrid.refreshPaginationInfo(result.getFirstResultOut(), result.getVariables().size(), result.getTotalResults());
    }

    private void setVariablesOfFamily(List<VariableDto> variableDtos) {
        VariableRecord[] records = new VariableRecord[variableDtos.size()];
        int index = 0;
        for (VariableDto scheme : variableDtos) {
            records[index++] = org.siemac.metamac.srm.web.client.code.utils.RecordUtils.getVariableRecord(scheme);
        }
        variableListGrid.getListGrid().setData(records);
    }

    private ToolStripButton createAddVariableToFamilyButton() {
        final int FIRST_RESULT = 0;
        final int MAX_RESULTS = 8;

        ToolStripButton button = new ToolStripButton(MetamacWebCommon.getConstants().add(), RESOURCE.newListGrid().getURL());
        button.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent arg0) {
                variablesWindow = new SearchMultipleRelatedResourcePaginatedWindow(getConstants().variablesSelection(), MAX_RESULTS, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        getUiHandlers().retrieveVariables(firstResult, maxResults, variablesWindow.getRelatedResourceCriteria());
                    }
                });

                // Load the list variables to be included in this family
                getUiHandlers().retrieveVariables(FIRST_RESULT, MAX_RESULTS, null);

                variablesWindow.setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                        getUiHandlers().retrieveVariables(firstResult, maxResults, criteria);
                    }
                });
                variablesWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent arg0) {
                        List<RelatedResourceDto> selectedVariables = variablesWindow.getSelectedRelatedResources();
                        variablesWindow.markForDestroy();
                        if (selectedVariables != null && !selectedVariables.isEmpty()) {
                            getUiHandlers().addVariablesToFamily(RelatedResourceUtils.getUrnsFromRelatedResourceDtos(selectedVariables), variableFamilyDto.getUrn());
                        }
                    }
                });
            }
        });
        return button;
    }

    private ToolStripButton createRemoveVariableToFamilyButton() {
        ToolStripButton button = new ToolStripButton(getConstants().variableRemoveFromFamily(), RESOURCE.deleteListGrid().getURL());
        button.setVisibility(Visibility.HIDDEN);
        button.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent arg0) {
                removeConfirmationWindow.show();
            }
        });
        return button;
    }

    private void showListGridRemoveButton() {
        removeVariableToFamilyButton.show();
    }
}
