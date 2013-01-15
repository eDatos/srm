package org.siemac.metamac.srm.web.client.code.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.web.client.code.model.ds.VariableElementDS;
import org.siemac.metamac.srm.web.client.code.presenter.VariableElementPresenter;
import org.siemac.metamac.srm.web.client.code.view.handlers.VariableElementUiHandlers;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;
import org.siemac.metamac.srm.web.client.widgets.SearchRelatedResourceWindow;
import org.siemac.metamac.srm.web.shared.code.GetVariablesResult;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.SearchViewTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.form.validator.CustomValidator;
import com.smartgwt.client.widgets.layout.VLayout;

public class VariableElementViewImpl extends ViewWithUiHandlers<VariableElementUiHandlers> implements VariableElementPresenter.VariableElementView {

    private VLayout                     panel;
    private InternationalMainFormLayout mainFormLayout;

    // View forms
    private GroupDynamicForm            identifiersForm;
    private GroupDynamicForm            contentDescriptorsForm;

    // Edition forms
    private GroupDynamicForm            identifiersEditionForm;
    private GroupDynamicForm            contentDescriptorsEditionForm;

    private SearchRelatedResourceWindow searchVariableWindow;

    private VariableElementDto          variableElementDto;

    @Inject
    public VariableElementViewImpl() {
        super();
        panel = new VLayout();
        panel.setHeight100();
        panel.setOverflow(Overflow.SCROLL);

        //
        // VARIABLE ELEMENT
        //

        mainFormLayout = new InternationalMainFormLayout(); // TODO Security
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
            }
        });

        // Save
        mainFormLayout.getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (identifiersEditionForm.validate(false) && contentDescriptorsEditionForm.validate(false)) {
                    saveVariableElement();
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
        if (slot == VariableElementPresenter.TYPE_SetContextAreaContentCodesToolBar) {
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
    public void setVariableElement(VariableElementDto variableElementDto) {
        this.variableElementDto = variableElementDto;

        String defaultLocalized = InternationalStringUtils.getLocalisedString(variableElementDto.getName());
        String title = defaultLocalized != null ? defaultLocalized : StringUtils.EMPTY;
        mainFormLayout.setTitleLabelContents(title);

        mainFormLayout.setViewMode();

        setVariableElementViewMode(variableElementDto);
        setVariableElementEditionMode(variableElementDto);
    }

    @Override
    public void setVariables(GetVariablesResult result) {
        if (searchVariableWindow != null) {
            searchVariableWindow.setRelatedResources(RelatedResourceUtils.getRelatedResourceDtosFromVariableDtos(result.getVariables()));
            searchVariableWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getVariables().size(), result.getTotalResults());
        }
    }

    private void createViewForm() {
        // Identifiers Form
        identifiersForm = new GroupDynamicForm(getConstants().formIdentifiers());
        ViewTextItem code = new ViewTextItem(VariableElementDS.CODE, getConstants().identifiableArtefactCode());
        ViewMultiLanguageTextItem name = new ViewMultiLanguageTextItem(VariableElementDS.NAME, getConstants().nameableArtefactName());
        ViewMultiLanguageTextItem shortName = new ViewMultiLanguageTextItem(VariableElementDS.SHORT_NAME, getConstants().variableElementShortName());
        ViewTextItem urn = new ViewTextItem(VariableElementDS.URN, getConstants().identifiableArtefactUrn());
        identifiersForm.setFields(code, name, shortName, urn);

        // Content descriptors
        contentDescriptorsForm = new GroupDynamicForm(getConstants().formContentDescriptors());
        ViewTextItem variable = new ViewTextItem(VariableElementDS.VARIABLE_VIEW, getConstants().variable());
        contentDescriptorsForm.setFields(variable);

        mainFormLayout.addViewCanvas(identifiersForm);
        mainFormLayout.addViewCanvas(contentDescriptorsForm);
    }

    private void createEditionForm() {
        // Identifiers
        identifiersEditionForm = new GroupDynamicForm(getConstants().formIdentifiers());
        RequiredTextItem code = new RequiredTextItem(VariableElementDS.CODE, getConstants().identifiableArtefactCode());
        code.setValidators(CommonWebUtils.getSemanticIdentifierCustomValidator());
        MultiLanguageTextItem name = new MultiLanguageTextItem(VariableElementDS.NAME, getConstants().nameableArtefactName());
        name.setRequired(true);
        MultiLanguageTextItem shortName = new MultiLanguageTextItem(VariableElementDS.SHORT_NAME, getConstants().variableElementShortName());
        shortName.setRequired(true);
        ViewTextItem urn = new ViewTextItem(VariableElementDS.URN, getConstants().identifiableArtefactUrn());
        identifiersEditionForm.setFields(code, name, shortName, urn);

        // Content descriptors
        contentDescriptorsEditionForm = new GroupDynamicForm(getConstants().formContentDescriptors());
        ViewTextItem variable = new ViewTextItem(VariableElementDS.VARIABLE, getConstants().variable());
        variable.setShowIfCondition(FormItemUtils.getFalseFormItemIfFunction());
        SearchViewTextItem variableView = createVariableItem();
        contentDescriptorsEditionForm.setFields(variable, variableView);

        mainFormLayout.addEditionCanvas(identifiersEditionForm);
        mainFormLayout.addEditionCanvas(contentDescriptorsEditionForm);
    }

    public void setEditionMode() {
        mainFormLayout.setEditionMode();
    }

    public void setVariableElementViewMode(VariableElementDto variableElementDto) {
        // Identifiers
        identifiersForm.setValue(VariableElementDS.CODE, variableElementDto.getCode());
        identifiersForm.setValue(VariableElementDS.URN, variableElementDto.getUrn());
        identifiersForm.setValue(VariableElementDS.NAME, RecordUtils.getInternationalStringRecord(variableElementDto.getName()));
        identifiersForm.setValue(VariableElementDS.SHORT_NAME, RecordUtils.getInternationalStringRecord(variableElementDto.getShortName()));

        // Content descriptors
        contentDescriptorsForm.setValue(VariableElementDS.VARIABLE_VIEW, CommonUtils.getRelatedResourceName(variableElementDto.getVariable()));
    }

    public void setVariableElementEditionMode(VariableElementDto variableElementDto) {
        // Identifiers
        identifiersEditionForm.setValue(VariableElementDS.CODE, variableElementDto.getCode());
        identifiersEditionForm.setValue(VariableElementDS.URN, variableElementDto.getUrn());
        identifiersEditionForm.setValue(VariableElementDS.NAME, RecordUtils.getInternationalStringRecord(variableElementDto.getName()));
        identifiersEditionForm.setValue(VariableElementDS.SHORT_NAME, RecordUtils.getInternationalStringRecord(variableElementDto.getShortName()));

        // Content descriptors
        contentDescriptorsEditionForm.setValue(VariableElementDS.VARIABLE_VIEW, CommonUtils.getRelatedResourceName(variableElementDto.getVariable()));
        contentDescriptorsEditionForm.setValue(VariableElementDS.VARIABLE, variableElementDto.getVariable() != null ? variableElementDto.getVariable().getUrn() : StringUtils.EMPTY);
    }

    public void saveVariableElement() {
        // Identifiers
        variableElementDto.setCode(identifiersEditionForm.getValueAsString(VariableElementDS.CODE));
        variableElementDto.setName((InternationalStringDto) identifiersEditionForm.getValue(VariableElementDS.NAME));
        variableElementDto.setShortName((InternationalStringDto) identifiersEditionForm.getValue(VariableElementDS.SHORT_NAME));

        // Content descriptors
        // It is necessary to update the place request hierarchy if the variable has been changed! If not, the URL would be incorrect.
        String oldVariableUrn = variableElementDto.getVariable().getUrn();
        String newVariableUrn = contentDescriptorsEditionForm.getValueAsString(VariableElementDS.VARIABLE);
        boolean updatePlaceRequestHierarchy = !StringUtils.equals(oldVariableUrn, newVariableUrn);
        variableElementDto.setVariable(RelatedResourceUtils.createRelatedResourceDto(newVariableUrn));

        getUiHandlers().saveVariableElement(variableElementDto, updatePlaceRequestHierarchy);
    }

    private SearchViewTextItem createVariableItem() {
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;

        SearchViewTextItem variableItem = new SearchViewTextItem(VariableElementDS.VARIABLE_VIEW, getConstants().variable());
        variableItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                searchVariableWindow = new SearchRelatedResourceWindow(getConstants().variableSelection(), MAX_RESULTS, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        getUiHandlers().retrieveVariables(FIRST_RESULST, MAX_RESULTS, searchVariableWindow.getRelatedResourceCriteria());
                    }
                });

                // Load variables (to populate the selection window)
                getUiHandlers().retrieveVariables(FIRST_RESULST, MAX_RESULTS, null);

                searchVariableWindow.getListGridItem().getListGrid().setSelectionType(SelectionStyle.SINGLE);
                searchVariableWindow.getListGridItem().setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                        getUiHandlers().retrieveVariables(firstResult, maxResults, criteria);
                    }
                });

                searchVariableWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent arg0) {
                        RelatedResourceDto selectedVariable = searchVariableWindow.getSelectedRelatedResource();
                        searchVariableWindow.markForDestroy();
                        // Set selected family in form
                        contentDescriptorsEditionForm.setValue(VariableElementDS.VARIABLE, selectedVariable != null ? selectedVariable.getUrn() : null);
                        contentDescriptorsEditionForm.setValue(VariableElementDS.VARIABLE_VIEW,
                                selectedVariable != null ? org.siemac.metamac.srm.web.client.utils.CommonUtils.getRelatedResourceName(selectedVariable) : null);
                    }
                });
            }
        });
        // Set required with a customValidator
        CustomValidator customValidator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                return !StringUtils.isBlank(contentDescriptorsEditionForm.getValueAsString(VariableElementDS.VARIABLE));
            }
        };
        variableItem.setValidators(customValidator);
        variableItem.setTitleStyle("staticFormItemTitle");
        return variableItem;
    };
}
