package org.siemac.metamac.srm.web.code.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.dto.VariableBasicDto;
import org.siemac.metamac.srm.core.code.dto.VariableFamilyDto;
import org.siemac.metamac.srm.web.client.constants.SrmWebConstants;
import org.siemac.metamac.srm.web.client.widgets.search.SearchMultipleRelatedResourcePaginatedWindow;
import org.siemac.metamac.srm.web.code.model.ds.VariableDS;
import org.siemac.metamac.srm.web.code.model.ds.VariableFamilyDS;
import org.siemac.metamac.srm.web.code.model.record.VariableRecord;
import org.siemac.metamac.srm.web.code.presenter.VariableFamilyPresenter;
import org.siemac.metamac.srm.web.code.utils.CodesClientSecurityUtils;
import org.siemac.metamac.srm.web.code.utils.CommonUtils;
import org.siemac.metamac.srm.web.code.view.handlers.VariableFamilyUiHandlers;
import org.siemac.metamac.srm.web.shared.code.GetVariablesResult;
import org.siemac.metamac.srm.web.shared.criteria.VariableWebCriteria;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.PaginatedCheckListGrid;
import org.siemac.metamac.web.common.client.widgets.SearchSectionStack;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class VariableFamilyViewImpl extends ViewWithUiHandlers<VariableFamilyUiHandlers> implements VariableFamilyPresenter.VariableFamilyView {

    private VLayout                                      panel;
    private InternationalMainFormLayout                  mainFormLayout;

    // View forms
    private GroupDynamicForm                             identifiersForm;
    private GroupDynamicForm                             productionDescriptorsForm;

    // Edition forms
    private GroupDynamicForm                             identifiersEditionForm;
    private GroupDynamicForm                             productionDescriptorsEditionForm;

    private PaginatedCheckListGrid                       variableListGrid;
    private ToolStripButton                              addVariableToFamilyButton;
    private ToolStripButton                              removeVariableToFamilyButton;
    private SearchMultipleRelatedResourcePaginatedWindow variablesWindow;
    private DeleteConfirmationWindow                     removeConfirmationWindow;
    private SearchSectionStack                           variablesSectionStack;

    private VariableFamilyDto                            variableFamilyDto;

    @Inject
    public VariableFamilyViewImpl() {
        super();

        //
        // VARIABLE FAMILY
        //

        mainFormLayout = new InternationalMainFormLayout(CodesClientSecurityUtils.canUpdateVariableFamily(), CodesClientSecurityUtils.canDeleteVariableFamily());
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
                getUiHandlers().retrieveVariablesByFamily(firstResult, maxResults, variablesSectionStack.getSearchCriteria(), variableFamilyDto.getUrn());
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
                    String code = ((VariableRecord) event.getRecord()).getAttribute(VariableDS.CODE);
                    getUiHandlers().goToVariable(code);
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

        variablesSectionStack = new SearchSectionStack(getConstants().variableFamilyVariables());
        variablesSectionStack.getSection().addItem(toolStrip);
        variablesSectionStack.getSection().addItem(variableListGrid);
        variablesSectionStack.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                getUiHandlers().retrieveVariablesByFamily(VariableFamilyPresenter.VARIABLE_LIST_FIRST_RESULT, VariableFamilyPresenter.VARIABLE_LIST_MAX_RESULTS,
                        variablesSectionStack.getSearchCriteria(), variableFamilyDto.getUrn());
            }
        });
        variablesSectionStack.addSearchItemKeyPressHandler(new KeyPressHandler() {

            @Override
            public void onKeyPress(KeyPressEvent event) {
                if (StringUtils.equalsIgnoreCase(event.getKeyName(), SrmWebConstants.ENTER_KEY)) {
                    getUiHandlers().retrieveVariablesByFamily(VariableFamilyPresenter.VARIABLE_LIST_FIRST_RESULT, VariableFamilyPresenter.VARIABLE_LIST_MAX_RESULTS,
                            variablesSectionStack.getSearchCriteria(), variableFamilyDto.getUrn());
                }
            }
        });

        variablesLayout.addMember(variablesSectionStack);

        panel = new VLayout();
        panel.setOverflow(Overflow.SCROLL);
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
                    // See: METAMAC-2516
                    // Two invokes to getXXXDto() is needed for Chrome, please don't remove this two call fix.
                    getVariableFamilyDto();
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

                        @Override
                        public void execute() {
                            getUiHandlers().saveVariableFamily(getVariableFamilyDto());
                        }
                    });
                }
            }
        });

        // Delete
        mainFormLayout.getDeleteConfirmationWindow().getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deleteVariableFamily(variableFamilyDto.getUrn());
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
        variableFamilyDto = family;

        String defaultLocalized = InternationalStringUtils.getLocalisedString(family.getName());
        String title = defaultLocalized != null ? defaultLocalized : StringUtils.EMPTY;
        mainFormLayout.setTitleLabelContents(title);

        mainFormLayout.setViewMode();

        setVariableFamilyViewMode(family);
        setVariableFamilyEditionMode(family);
    }

    @Override
    public void clearSearchSection() {
        variablesSectionStack.reset();
    }

    private void createViewForm() {
        // Identifiers Form
        identifiersForm = new GroupDynamicForm(getConstants().formIdentifiers());
        ViewTextItem code = new ViewTextItem(VariableFamilyDS.CODE, getConstants().identifiableArtefactCode());
        ViewMultiLanguageTextItem name = new ViewMultiLanguageTextItem(VariableFamilyDS.NAME, getConstants().nameableArtefactName());
        ViewTextItem urn = new ViewTextItem(VariableFamilyDS.URN, getConstants().identifiableArtefactUrn());
        identifiersForm.setFields(code, name, urn);

        // Production descriptors
        productionDescriptorsForm = new GroupDynamicForm(getConstants().formProductionDescriptors());
        ViewTextItem creationDate = new ViewTextItem(VariableFamilyDS.CREATION_DATE, getConstants().resourceCreationDate());
        ViewTextItem creationUser = new ViewTextItem(VariableFamilyDS.CREATION_USER, getConstants().resourceCreationUser());
        ViewTextItem lastUpdateDate = new ViewTextItem(VariableFamilyDS.LAST_UPDATE_DATE, getConstants().resourceLastUpdateDate());
        ViewTextItem lastUpdateUser = new ViewTextItem(VariableFamilyDS.LAST_UPDATE_USER, getConstants().resourceLastUpdateUser());
        productionDescriptorsForm.setFields(creationDate, creationUser, lastUpdateDate, lastUpdateUser);

        mainFormLayout.addViewCanvas(identifiersForm);
        mainFormLayout.addViewCanvas(productionDescriptorsForm);
    }

    private void createEditionForm() {
        // Identifiers
        identifiersEditionForm = new GroupDynamicForm(getConstants().formIdentifiers());
        ViewTextItem code = new ViewTextItem(VariableFamilyDS.CODE, getConstants().identifiableArtefactCode());
        MultiLanguageTextItem name = new MultiLanguageTextItem(VariableFamilyDS.NAME, getConstants().nameableArtefactName());
        name.setRequired(true);
        ViewTextItem urn = new ViewTextItem(VariableFamilyDS.URN, getConstants().identifiableArtefactUrn());
        identifiersEditionForm.setFields(code, name, urn);

        // Production descriptors
        productionDescriptorsEditionForm = new GroupDynamicForm(getConstants().formProductionDescriptors());
        ViewTextItem creationDate = new ViewTextItem(VariableFamilyDS.CREATION_DATE, getConstants().resourceCreationDate());
        ViewTextItem creationUser = new ViewTextItem(VariableFamilyDS.CREATION_USER, getConstants().resourceCreationUser());
        ViewTextItem lastUpdateDate = new ViewTextItem(VariableFamilyDS.LAST_UPDATE_DATE, getConstants().resourceLastUpdateDate());
        ViewTextItem lastUpdateUser = new ViewTextItem(VariableFamilyDS.LAST_UPDATE_USER, getConstants().resourceLastUpdateUser());
        productionDescriptorsEditionForm.setFields(creationDate, creationUser, lastUpdateDate, lastUpdateUser);

        mainFormLayout.addEditionCanvas(identifiersEditionForm);
        mainFormLayout.addEditionCanvas(productionDescriptorsEditionForm);
    }

    public void setEditionMode() {
        mainFormLayout.setEditionMode();
    }

    public void setVariableFamilyViewMode(VariableFamilyDto variableFamilyDto) {
        // Identifiers
        identifiersForm.setValue(VariableFamilyDS.CODE, variableFamilyDto.getCode());
        identifiersForm.setValue(VariableFamilyDS.URN, variableFamilyDto.getUrn());
        identifiersForm.setValue(VariableFamilyDS.NAME, variableFamilyDto.getName());

        // Production descriptors
        productionDescriptorsForm.setValue(VariableFamilyDS.CREATION_DATE, variableFamilyDto.getCreatedDate());
        productionDescriptorsForm.setValue(VariableFamilyDS.CREATION_USER, variableFamilyDto.getCreatedBy());
        productionDescriptorsForm.setValue(VariableFamilyDS.LAST_UPDATE_DATE, variableFamilyDto.getLastUpdated());
        productionDescriptorsForm.setValue(VariableFamilyDS.LAST_UPDATE_USER, variableFamilyDto.getLastUpdatedBy());
    }

    public void setVariableFamilyEditionMode(VariableFamilyDto variableFamilyDto) {
        // Identifiers
        identifiersEditionForm.setValue(VariableFamilyDS.CODE, variableFamilyDto.getCode());
        identifiersEditionForm.setValue(VariableFamilyDS.URN, variableFamilyDto.getUrn());
        identifiersEditionForm.setValue(VariableFamilyDS.NAME, variableFamilyDto.getName());

        // Production descriptors
        productionDescriptorsEditionForm.setValue(VariableFamilyDS.CREATION_DATE, variableFamilyDto.getCreatedDate());
        productionDescriptorsEditionForm.setValue(VariableFamilyDS.CREATION_USER, variableFamilyDto.getCreatedBy());
        productionDescriptorsEditionForm.setValue(VariableFamilyDS.LAST_UPDATE_DATE, variableFamilyDto.getLastUpdated());
        productionDescriptorsEditionForm.setValue(VariableFamilyDS.LAST_UPDATE_USER, variableFamilyDto.getLastUpdatedBy());
    }

    public VariableFamilyDto getVariableFamilyDto() {
        // Identifiers
        variableFamilyDto.setName(identifiersEditionForm.getValueAsInternationalStringDto(VariableFamilyDS.NAME));

        return variableFamilyDto;
    }

    @Override
    public void setVariables(GetVariablesResult result) {
        if (variablesWindow != null) {
            variablesWindow.setResources(RelatedResourceUtils.getVariableBasicDtosAsRelatedResourceDtos(result.getVariables()));
            variablesWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getVariables().size(), result.getTotalResults());
        }
    }

    @Override
    public void setVariablesOfFamily(GetVariablesResult result) {
        setVariablesOfFamily(result.getVariables());
        variableListGrid.getListGrid().setAutoFitMaxRecords(result.getVariables().size());
        variableListGrid.refreshPaginationInfo(result.getFirstResultOut(), result.getVariables().size(), result.getTotalResults());
    }

    private void setVariablesOfFamily(List<VariableBasicDto> variableDtos) {
        VariableRecord[] records = new VariableRecord[variableDtos.size()];
        int index = 0;
        for (VariableBasicDto scheme : variableDtos) {
            records[index++] = org.siemac.metamac.srm.web.code.utils.CodesRecordUtils.getVariableRecord(scheme);
        }
        variableListGrid.getListGrid().setData(records);
    }

    private ToolStripButton createAddVariableToFamilyButton() {

        ToolStripButton button = new ToolStripButton(MetamacWebCommon.getConstants().add(), RESOURCE.newListGrid().getURL());
        button.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent arg0) {

                variablesWindow = new SearchMultipleRelatedResourcePaginatedWindow(getConstants().variablesSelection(), SrmWebConstants.FORM_LIST_MAX_RESULTS,
                        new org.siemac.metamac.web.common.client.widgets.actions.search.SearchPaginatedAction<MetamacWebCriteria>() {

                            @Override
                            public void retrieveResultSet(int firstResult, int maxResults, MetamacWebCriteria webCriteria) {
                                getUiHandlers().retrieveVariables(firstResult, maxResults, new VariableWebCriteria(webCriteria.getCriteria()));
                            }
                        });

                variablesWindow.retrieveItems();

                variablesWindow.setSaveAction(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        List<RelatedResourceDto> selectedVariables = variablesWindow.getSelectedResources();
                        variablesWindow.markForDestroy();
                        if (selectedVariables != null && !selectedVariables.isEmpty()) {
                            getUiHandlers().addVariablesToFamily(RelatedResourceUtils.getUrnsFromRelatedResourceDtos(selectedVariables), variableFamilyDto.getUrn());
                        }
                    }
                });

            }
        });
        button.setVisible(CodesClientSecurityUtils.canAddVariablesToVariableFamily());
        return button;
    }

    private ToolStripButton createRemoveVariableToFamilyButton() {
        ToolStripButton button = new ToolStripButton(getConstants().variableRemoveFromFamily(), RESOURCE.deleteListGrid().getURL());
        button.setVisible(false);
        button.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent arg0) {
                removeConfirmationWindow.show();
            }
        });
        return button;
    }

    private void showListGridRemoveButton() {
        if (CodesClientSecurityUtils.canRemoveVariablesFromVariableFamily()) {
            removeVariableToFamilyButton.show();
        }
    }
}
