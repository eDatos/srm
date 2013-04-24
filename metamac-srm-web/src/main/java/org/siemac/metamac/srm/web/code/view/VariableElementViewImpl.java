package org.siemac.metamac.srm.web.code.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementOperationDto;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.client.widgets.RelatedResourceListItem;
import org.siemac.metamac.srm.web.client.widgets.SearchMultipleRelatedResourcePaginatedWindow;
import org.siemac.metamac.srm.web.code.model.ds.VariableElementDS;
import org.siemac.metamac.srm.web.code.presenter.VariableElementPresenter;
import org.siemac.metamac.srm.web.code.utils.CodesClientSecurityUtils;
import org.siemac.metamac.srm.web.code.view.handlers.VariableElementUiHandlers;
import org.siemac.metamac.srm.web.code.widgets.VariableElementMainFormLayout;
import org.siemac.metamac.srm.web.code.widgets.VariableElementOperationLayout;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementsResult;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.utils.DateUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomDateItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.layout.VLayout;

public class VariableElementViewImpl extends ViewWithUiHandlers<VariableElementUiHandlers> implements VariableElementPresenter.VariableElementView {

    private VLayout                                      panel;
    private VariableElementMainFormLayout                mainFormLayout;

    // View forms
    private GroupDynamicForm                             identifiersForm;
    private GroupDynamicForm                             contentDescriptorsForm;
    private GroupDynamicForm                             diffusionDescriptorsForm;

    // Edition forms
    private GroupDynamicForm                             identifiersEditionForm;
    private GroupDynamicForm                             contentDescriptorsEditionForm;
    private GroupDynamicForm                             diffusionDescriptorsEditionForm;

    private SearchMultipleRelatedResourcePaginatedWindow searchReplaceToElementsWindow;
    private SearchMultipleRelatedResourcePaginatedWindow createSegregationWindow;

    // Variable element operations
    private VariableElementOperationLayout               variableElementOperationsLayout;

    private VariableElementDto                           variableElementDto;

    @Inject
    public VariableElementViewImpl() {
        super();
        panel = new VLayout();
        panel.setHeight100();

        //
        // VARIABLE ELEMENT
        //

        mainFormLayout = new VariableElementMainFormLayout(CodesClientSecurityUtils.canUpdateVariableElement());
        mainFormLayout.getSegregate().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                showCreateSegregationWindow();
            }
        });
        bindMainFormLayoutEvents();
        createViewForm();
        createEditionForm();

        // VARIABLE ELEMENT OPERATIONS

        variableElementOperationsLayout = new VariableElementOperationLayout(getConstants().variableElementOperations());
        variableElementOperationsLayout.setMargin(15);

        VLayout subPanel = new VLayout();
        subPanel.setOverflow(Overflow.SCROLL);
        subPanel.addMember(mainFormLayout);
        subPanel.addMember(variableElementOperationsLayout);

        panel.addMember(subPanel);
    }

    @Override
    public void setUiHandlers(VariableElementUiHandlers uiHandlers) {
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

        String defaultLocalized = InternationalStringUtils.getLocalisedString(variableElementDto.getShortName());
        String title = defaultLocalized != null ? defaultLocalized : StringUtils.EMPTY;
        mainFormLayout.setTitleLabelContents(title);

        mainFormLayout.setViewMode();

        setVariableElementViewMode(variableElementDto);
        setVariableElementEditionMode(variableElementDto);
    }

    @Override
    public void setVariableElementOperations(List<VariableElementOperationDto> variableElementOperationDtos) {
        variableElementOperationsLayout.setVariableElementOperations(variableElementOperationDtos);
    }

    @Override
    public void setVariableElementsForReplaceTo(GetVariableElementsResult result) {
        if (searchReplaceToElementsWindow != null) {
            searchReplaceToElementsWindow.setSourceRelatedResources(RelatedResourceUtils.getVariableElementBasicDtosAsRelatedResourceDtos(result.getVariableElements()));
            searchReplaceToElementsWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getVariableElements().size(), result.getTotalResults());
        }
    }

    @Override
    public void setVariableElementsForSegregation(GetVariableElementsResult result) {
        if (createSegregationWindow != null) {
            createSegregationWindow.setSourceRelatedResources(RelatedResourceUtils.getVariableElementBasicDtosAsRelatedResourceDtos(result.getVariableElements()));
            createSegregationWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getVariableElements().size(), result.getTotalResults());
        }
    }

    private void createViewForm() {
        // Identifiers Form
        identifiersForm = new GroupDynamicForm(getConstants().formIdentifiers());
        ViewTextItem code = new ViewTextItem(VariableElementDS.CODE, getConstants().identifiableArtefactCode());
        ViewMultiLanguageTextItem shortName = new ViewMultiLanguageTextItem(VariableElementDS.SHORT_NAME, getConstants().variableElementShortName());
        ViewTextItem urn = new ViewTextItem(VariableElementDS.URN, getConstants().identifiableArtefactUrn());
        identifiersForm.setFields(code, shortName, urn);

        // Content descriptors
        contentDescriptorsForm = new GroupDynamicForm(getConstants().formContentDescriptors());
        ViewTextItem variable = new ViewTextItem(VariableElementDS.VARIABLE, getConstants().variable());
        contentDescriptorsForm.setFields(variable);

        // Diffusion descriptors
        diffusionDescriptorsForm = new GroupDynamicForm(getConstants().formDiffusionDescriptors());
        ViewTextItem validFrom = new ViewTextItem(VariableElementDS.VALID_FROM, getConstants().variableElementValidFrom());
        ViewTextItem validTo = new ViewTextItem(VariableElementDS.VALID_TO, getConstants().variableElementValidTo());
        RelatedResourceListItem replaceToElements = new RelatedResourceListItem(VariableElementDS.REPLACE_TO_ELEMENTS, getConstants().variableElementReplaceToVariableElements(), false);
        ViewTextItem replacedByElement = new ViewTextItem(VariableElementDS.REPLACED_BY_ELEMENT, getConstants().variableElementReplacedByVariableElement());
        diffusionDescriptorsForm.setFields(validFrom, validTo, replaceToElements, replacedByElement);

        mainFormLayout.addViewCanvas(identifiersForm);
        mainFormLayout.addViewCanvas(contentDescriptorsForm);
        mainFormLayout.addViewCanvas(diffusionDescriptorsForm);
    }

    private void createEditionForm() {
        // Identifiers
        identifiersEditionForm = new GroupDynamicForm(getConstants().formIdentifiers());
        RequiredTextItem code = new RequiredTextItem(VariableElementDS.CODE, getConstants().identifiableArtefactCode());
        code.setValidators(SemanticIdentifiersUtils.getVariableElementIdentifierCustomValidator());
        MultiLanguageTextItem shortName = new MultiLanguageTextItem(VariableElementDS.SHORT_NAME, getConstants().variableElementShortName(), SrmConstants.METADATA_SHORT_NAME_MAXIMUM_LENGTH);
        shortName.setRequired(true);
        ViewTextItem urn = new ViewTextItem(VariableElementDS.URN, getConstants().identifiableArtefactUrn());
        identifiersEditionForm.setFields(code, shortName, urn);

        // Content descriptors
        contentDescriptorsEditionForm = new GroupDynamicForm(getConstants().formContentDescriptors());
        ViewTextItem variable = new ViewTextItem(VariableElementDS.VARIABLE, getConstants().variable());
        contentDescriptorsEditionForm.setFields(variable);

        // Diffusion descriptors
        diffusionDescriptorsEditionForm = new GroupDynamicForm(getConstants().formDiffusionDescriptors());
        CustomDateItem validFrom = new CustomDateItem(VariableElementDS.VALID_FROM, getConstants().variableElementValidFrom());
        CustomDateItem validTo = new CustomDateItem(VariableElementDS.VALID_TO, getConstants().variableElementValidTo());
        RelatedResourceListItem replaceToElements = createReplaceToElementsItem();
        ViewTextItem replacedByElement = new ViewTextItem(VariableElementDS.REPLACED_BY_ELEMENT, getConstants().variableElementReplacedByVariableElement());
        diffusionDescriptorsEditionForm.setFields(validFrom, validTo, replaceToElements, replacedByElement);

        mainFormLayout.addEditionCanvas(identifiersEditionForm);
        mainFormLayout.addEditionCanvas(contentDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(diffusionDescriptorsEditionForm);
    }

    public void setEditionMode() {
        mainFormLayout.setEditionMode();
    }

    public void setVariableElementViewMode(VariableElementDto variableElementDto) {
        // Identifiers
        identifiersForm.setValue(VariableElementDS.CODE, variableElementDto.getCode());
        identifiersForm.setValue(VariableElementDS.URN, variableElementDto.getUrn());
        identifiersForm.setValue(VariableElementDS.SHORT_NAME, RecordUtils.getInternationalStringRecord(variableElementDto.getShortName()));

        // Content descriptors
        contentDescriptorsForm.setValue(VariableElementDS.VARIABLE, RelatedResourceUtils.getRelatedResourceName(variableElementDto.getVariable()));

        // Diffusion descriptors
        diffusionDescriptorsForm.setValue(VariableElementDS.VALID_FROM, DateUtils.getFormattedDate(variableElementDto.getValidFrom()));
        diffusionDescriptorsForm.setValue(VariableElementDS.VALID_TO, DateUtils.getFormattedDate(variableElementDto.getValidTo()));
        ((RelatedResourceListItem) diffusionDescriptorsForm.getItem(VariableElementDS.REPLACE_TO_ELEMENTS)).setRelatedResources(variableElementDto.getReplaceToVariableElements());
        diffusionDescriptorsForm.setValue(VariableElementDS.REPLACED_BY_ELEMENT, RelatedResourceUtils.getRelatedResourceName(variableElementDto.getReplacedByVariableElement()));
    }

    public void setVariableElementEditionMode(VariableElementDto variableElementDto) {
        // Identifiers
        identifiersEditionForm.setValue(VariableElementDS.CODE, variableElementDto.getCode());
        identifiersEditionForm.setValue(VariableElementDS.URN, variableElementDto.getUrn());
        identifiersEditionForm.setValue(VariableElementDS.SHORT_NAME, RecordUtils.getInternationalStringRecord(variableElementDto.getShortName()));

        // Content descriptors
        contentDescriptorsEditionForm.setValue(VariableElementDS.VARIABLE, RelatedResourceUtils.getRelatedResourceName(variableElementDto.getVariable()));

        // Diffusion descriptors
        diffusionDescriptorsEditionForm.setValue(VariableElementDS.VALID_FROM, variableElementDto.getValidFrom());
        diffusionDescriptorsEditionForm.setValue(VariableElementDS.VALID_TO, variableElementDto.getValidTo());
        ((RelatedResourceListItem) diffusionDescriptorsEditionForm.getItem(VariableElementDS.REPLACE_TO_ELEMENTS)).setRelatedResources(variableElementDto.getReplaceToVariableElements());
        diffusionDescriptorsEditionForm.setValue(VariableElementDS.REPLACED_BY_ELEMENT, RelatedResourceUtils.getRelatedResourceName(variableElementDto.getReplacedByVariableElement()));
    }

    public void saveVariableElement() {
        // Identifiers
        variableElementDto.setCode(identifiersEditionForm.getValueAsString(VariableElementDS.CODE));
        variableElementDto.setShortName((InternationalStringDto) identifiersEditionForm.getValue(VariableElementDS.SHORT_NAME));

        // Content descriptors

        // Diffusion descriptors
        variableElementDto.setValidFrom(((CustomDateItem) diffusionDescriptorsEditionForm.getItem(VariableElementDS.VALID_FROM)).getValueAsDate());
        variableElementDto.setValidTo(((CustomDateItem) diffusionDescriptorsEditionForm.getItem(VariableElementDS.VALID_TO)).getValueAsDate());
        variableElementDto.getReplaceToVariableElements().clear();
        variableElementDto.getReplaceToVariableElements().addAll(
                ((RelatedResourceListItem) diffusionDescriptorsEditionForm.getItem(VariableElementDS.REPLACE_TO_ELEMENTS)).getSelectedRelatedResources());

        getUiHandlers().saveVariableElement(variableElementDto);
    }

    private RelatedResourceListItem createReplaceToElementsItem() {
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;

        RelatedResourceListItem replaceToItem = new RelatedResourceListItem(VariableElementDS.REPLACE_TO_ELEMENTS, getConstants().variableElementReplaceToVariableElements(), true);
        replaceToItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                searchReplaceToElementsWindow = new SearchMultipleRelatedResourcePaginatedWindow(getConstants().variableElementsSelection(), MAX_RESULTS, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        getUiHandlers().retrieveVariableElementsByVariableForReplaceTo(firstResult, maxResults, searchReplaceToElementsWindow.getRelatedResourceCriteria(),
                                variableElementDto.getVariable().getUrn());
                    }
                });

                // Load variables
                getUiHandlers().retrieveVariableElementsByVariableForReplaceTo(FIRST_RESULST, MAX_RESULTS, null, variableElementDto.getVariable().getUrn());

                // Set the selected variables
                List<RelatedResourceDto> selectedElements = ((RelatedResourceListItem) diffusionDescriptorsEditionForm.getItem(VariableElementDS.REPLACE_TO_ELEMENTS)).getRelatedResourceDtos();
                searchReplaceToElementsWindow.setTargetRelatedResources(selectedElements);

                searchReplaceToElementsWindow.setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                        getUiHandlers().retrieveVariableElementsByVariableForReplaceTo(firstResult, maxResults, criteria, variableElementDto.getVariable().getUrn());
                    }
                });

                searchReplaceToElementsWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        List<RelatedResourceDto> selectedVariables = searchReplaceToElementsWindow.getSelectedRelatedResources();
                        searchReplaceToElementsWindow.markForDestroy();
                        // Set selected variables in form
                        ((RelatedResourceListItem) diffusionDescriptorsEditionForm.getItem(VariableElementDS.REPLACE_TO_ELEMENTS)).setRelatedResources(selectedVariables);
                    }
                });
            }
        });
        return replaceToItem;
    }

    private void showCreateSegregationWindow() {
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;
        createSegregationWindow = new SearchMultipleRelatedResourcePaginatedWindow(getConstants().actionSegregate(), MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveVariableElementsByVariableForSegregationOperation(firstResult, maxResults, createSegregationWindow.getRelatedResourceCriteria(),
                        variableElementDto.getVariable().getUrn());
            }
        });

        // Load variable elements (to populate the selection window)
        getUiHandlers().retrieveVariableElementsByVariableForSegregationOperation(FIRST_RESULST, MAX_RESULTS, null, variableElementDto.getVariable().getUrn());

        createSegregationWindow.setSearchAction(new SearchPaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                getUiHandlers().retrieveVariableElementsByVariableForSegregationOperation(firstResult, maxResults, criteria, variableElementDto.getVariable().getUrn());
            }
        });
        createSegregationWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent arg0) {
                List<RelatedResourceDto> selectedVariableElements = createSegregationWindow.getSelectedRelatedResources();
                createSegregationWindow.markForDestroy();
                // Segregate the selected variable elements
                String sourceUrn = variableElementDto.getUrn();
                List<String> targetUrns = RelatedResourceUtils.getUrnsFromRelatedResourceDtos(selectedVariableElements);
                getUiHandlers().createSegregation(sourceUrn, targetUrns);
            }
        });
    }
}
