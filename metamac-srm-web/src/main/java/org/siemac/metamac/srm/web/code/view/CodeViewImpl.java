package org.siemac.metamac.srm.web.code.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.client.widgets.AnnotationsPanel;
import org.siemac.metamac.srm.web.client.widgets.CustomVLayout;
import org.siemac.metamac.srm.web.client.widgets.SearchRelatedResourcePaginatedWindow;
import org.siemac.metamac.srm.web.code.model.ds.CodeDS;
import org.siemac.metamac.srm.web.code.presenter.CodePresenter;
import org.siemac.metamac.srm.web.code.utils.CodesClientSecurityUtils;
import org.siemac.metamac.srm.web.code.view.handlers.CodeUiHandlers;
import org.siemac.metamac.srm.web.code.widgets.CodeMainFormLayout;
import org.siemac.metamac.srm.web.code.widgets.CodesTreeGrid;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.TitleLabel;
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
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.layout.VLayout;

public class CodeViewImpl extends ViewWithUiHandlers<CodeUiHandlers> implements CodePresenter.CodeView {

    private VLayout                              panel;
    private CodeMainFormLayout                   mainFormLayout;

    private CodesTreeGrid                        codesTreeGrid;

    // View forms
    private GroupDynamicForm                     identifiersForm;
    private GroupDynamicForm                     contentDescriptorsForm;
    private GroupDynamicForm                     commentsForm;
    private AnnotationsPanel                     annotationsPanel;

    // Edition forms
    private GroupDynamicForm                     identifiersEditionForm;
    private GroupDynamicForm                     contentDescriptorsEditionForm;
    private GroupDynamicForm                     commentsEditionForm;
    private AnnotationsPanel                     annotationsEditionPanel;

    private SearchRelatedResourcePaginatedWindow searchVariableElementWindow;

    private CodeMetamacDto                       codeDto;

    @Inject
    public CodeViewImpl() {
        super();
        panel = new VLayout();
        panel.setHeight100();
        panel.setOverflow(Overflow.SCROLL);

        //
        // CODES HIERARCHY
        //

        codesTreeGrid = new CodesTreeGrid();

        CustomVLayout codesListGridLayout = new CustomVLayout();
        codesListGridLayout.addMember(new TitleLabel(getConstants().codelistCodes()));
        codesListGridLayout.addMember(codesTreeGrid);

        //
        // CODE
        //

        mainFormLayout = new CodeMainFormLayout();

        // Translations
        mainFormLayout.getTranslateToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                boolean translationsShowed = mainFormLayout.getTranslateToolStripButton().isSelected();
                identifiersForm.setTranslationsShowed(translationsShowed);
                identifiersEditionForm.setTranslationsShowed(translationsShowed);

                contentDescriptorsForm.setTranslationsShowed(translationsShowed);
                contentDescriptorsEditionForm.setTranslationsShowed(translationsShowed);

                commentsForm.setTranslationsShowed(translationsShowed);
                commentsEditionForm.setTranslationsShowed(translationsShowed);

                annotationsPanel.setTranslationsShowed(translationsShowed);
                annotationsEditionPanel.setTranslationsShowed(translationsShowed);
            }
        });

        // Save
        mainFormLayout.getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (validateEditionForms()) {
                    getUiHandlers().saveCode(getCodeDto());
                }
            }
        });

        // Update variable element
        mainFormLayout.getUpdateVariableElement().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                showSearchVariableElementWindow(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        RelatedResourceDto selectedVariableElement = searchVariableElementWindow.getSelectedRelatedResource();
                        searchVariableElementWindow.markForDestroy();
                        getUiHandlers().updateVariableElement(codeDto.getUrn(), selectedVariableElement.getUrn());
                    }
                });
            }
        });

        createViewForm();
        createEditionForm();

        panel.addMember(codesListGridLayout);
        panel.addMember(mainFormLayout);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == CodePresenter.TYPE_SetContextAreaContentCodesToolBar) {
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
    public void setUiHandlers(CodeUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        codesTreeGrid.setUiHandlers(uiHandlers);
    }

    private void createViewForm() {
        // Identifiers Form
        identifiersForm = new GroupDynamicForm(getConstants().formIdentifiers());
        ViewTextItem code = new ViewTextItem(CodeDS.CODE, getConstants().identifiableArtefactCode());
        ViewMultiLanguageTextItem name = new ViewMultiLanguageTextItem(CodeDS.NAME, getConstants().nameableArtefactName());
        ViewMultiLanguageTextItem shortName = new ViewMultiLanguageTextItem(CodeDS.SHORT_NAME, getConstants().codeShortName());
        ViewTextItem uri = new ViewTextItem(CodeDS.URI, getConstants().identifiableArtefactUri());
        ViewTextItem urn = new ViewTextItem(CodeDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(CodeDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        identifiersForm.setFields(code, name, shortName, uri, urn, urnProvider);

        // Content descriptors
        contentDescriptorsForm = new GroupDynamicForm(getConstants().formContentDescriptors());
        ViewMultiLanguageTextItem description = new ViewMultiLanguageTextItem(CodeDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        ViewTextItem variableElement = new ViewTextItem(CodeDS.VARIABLE_ELEMENT_VIEW, getConstants().variableElement());
        contentDescriptorsForm.setFields(description, variableElement);

        // Comments
        commentsForm = new GroupDynamicForm(getConstants().nameableArtefactComments());
        ViewMultiLanguageTextItem comments = new ViewMultiLanguageTextItem(CodeDS.COMMENTS, getConstants().nameableArtefactComments());
        commentsForm.setFields(comments);

        // Annotations
        annotationsPanel = new AnnotationsPanel(true);

        mainFormLayout.addViewCanvas(identifiersForm);
        mainFormLayout.addViewCanvas(contentDescriptorsForm);
        mainFormLayout.addViewCanvas(commentsForm);
        mainFormLayout.addViewCanvas(annotationsPanel);
    }

    private void createEditionForm() {
        // Identifiers Form
        identifiersEditionForm = new GroupDynamicForm(getConstants().formIdentifiers());
        RequiredTextItem code = new RequiredTextItem(CodeDS.CODE, getConstants().identifiableArtefactCode());
        code.setValidators(SemanticIdentifiersUtils.getCodeIdentifierCustomValidator());
        MultiLanguageTextItem name = new MultiLanguageTextItem(CodeDS.NAME, getConstants().nameableArtefactName());
        name.setRequired(true);
        MultiLanguageTextItem shortName = new MultiLanguageTextItem(CodeDS.SHORT_NAME, getConstants().codeShortName());
        ViewTextItem uri = new ViewTextItem(CodeDS.URI, getConstants().identifiableArtefactUri());
        ViewTextItem urn = new ViewTextItem(CodeDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(CodeDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        identifiersEditionForm.setFields(code, name, shortName, uri, urn, urnProvider);

        // Content descriptors
        contentDescriptorsEditionForm = new GroupDynamicForm(getConstants().formContentDescriptors());
        MultiLanguageTextAreaItem description = new MultiLanguageTextAreaItem(CodeDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        ViewTextItem variableElement = new ViewTextItem(CodeDS.VARIABLE_ELEMENT, getConstants().variableElement());
        variableElement.setShowIfCondition(FormItemUtils.getFalseFormItemIfFunction());
        SearchViewTextItem variableElementView = createVariableElementItem(CodeDS.VARIABLE_ELEMENT_VIEW, getConstants().variableElement());
        contentDescriptorsEditionForm.setFields(description, variableElement, variableElementView);

        // Comments
        commentsEditionForm = new GroupDynamicForm(getConstants().nameableArtefactComments());
        MultiLanguageTextAreaItem comments = new MultiLanguageTextAreaItem(CodeDS.COMMENTS, getConstants().nameableArtefactComments());
        commentsEditionForm.setFields(comments);

        // Annotations
        annotationsEditionPanel = new AnnotationsPanel(false);

        mainFormLayout.addEditionCanvas(identifiersEditionForm);
        mainFormLayout.addEditionCanvas(contentDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(commentsEditionForm);
        mainFormLayout.addEditionCanvas(annotationsEditionPanel);
    }

    @Override
    public void setCodes(CodelistMetamacDto codelistMetamacDto, List<ItemHierarchyDto> itemHierarchyDtos) {
        codesTreeGrid.setItems(codelistMetamacDto, itemHierarchyDtos);
        codesTreeGrid.selectItem(codeDto);

        // Security
        mainFormLayout.setCanEdit(CodesClientSecurityUtils.canUpdateCode(codelistMetamacDto.getLifeCycle().getProcStatus()));
        mainFormLayout.updateButtonsVisibility(codelistMetamacDto.getLifeCycle().getProcStatus());
    }

    @Override
    public void setVariableElements(GetRelatedResourcesResult result) {
        if (searchVariableElementWindow != null) {
            searchVariableElementWindow.setRelatedResources(result.getRelatedResourceDtos());
            searchVariableElementWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getRelatedResourceDtos().size(), result.getTotalResults());
        }
    }

    @Override
    public void setCode(CodeMetamacDto codeDto) {
        this.codeDto = codeDto;

        getUiHandlers().retrieveCodesByCodelist(codeDto.getItemSchemeVersionUrn());

        // Set title
        String defaultLocalized = InternationalStringUtils.getLocalisedString(codeDto.getName());
        String title = defaultLocalized != null ? defaultLocalized : StringUtils.EMPTY;
        mainFormLayout.setTitleLabelContents(title);

        mainFormLayout.setViewMode();

        setCodeViewMode(codeDto);
        setCodeEditionMode(codeDto);
    }

    private void setCodeViewMode(CodeMetamacDto codeDto) {
        // Identifiers Form
        identifiersForm.setValue(CodeDS.CODE, codeDto.getCode());
        identifiersForm.setValue(CodeDS.NAME, RecordUtils.getInternationalStringRecord(codeDto.getName()));
        identifiersForm.setValue(CodeDS.SHORT_NAME, RecordUtils.getInternationalStringRecord(codeDto.getShortName()));
        identifiersForm.setValue(CodeDS.URI, codeDto.getUriProvider());
        identifiersForm.setValue(CodeDS.URN, codeDto.getUrn());
        identifiersForm.setValue(CodeDS.URN_PROVIDER, codeDto.getUrnProvider());

        // Content descriptors
        contentDescriptorsForm.setValue(CodeDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(codeDto.getDescription()));
        contentDescriptorsForm.setValue(CodeDS.VARIABLE_ELEMENT_VIEW,
                codeDto.getVariableElement() != null ? org.siemac.metamac.srm.web.client.utils.CommonUtils.getRelatedResourceName(codeDto.getVariableElement()) : StringUtils.EMPTY);
        contentDescriptorsForm.markForRedraw();

        // Comments
        commentsForm.setValue(CodeDS.COMMENTS, RecordUtils.getInternationalStringRecord(codeDto.getComment()));

        // Annotations
        annotationsPanel.setAnnotations(codeDto.getAnnotations());
    }

    private void setCodeEditionMode(CodeMetamacDto codeDto) {
        // Identifiers Form
        identifiersEditionForm.setValue(CodeDS.CODE, codeDto.getCode());
        identifiersEditionForm.setValue(CodeDS.NAME, RecordUtils.getInternationalStringRecord(codeDto.getName()));
        identifiersEditionForm.setValue(CodeDS.SHORT_NAME, RecordUtils.getInternationalStringRecord(codeDto.getShortName()));
        identifiersEditionForm.setValue(CodeDS.URI, codeDto.getUriProvider());
        identifiersEditionForm.setValue(CodeDS.URN, codeDto.getUrn());
        identifiersEditionForm.setValue(CodeDS.URN_PROVIDER, codeDto.getUrnProvider());

        // Content descriptors
        contentDescriptorsEditionForm.setValue(CodeDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(codeDto.getDescription()));
        contentDescriptorsEditionForm.setValue(CodeDS.VARIABLE_ELEMENT_VIEW,
                codeDto.getVariableElement() != null ? org.siemac.metamac.srm.web.client.utils.CommonUtils.getRelatedResourceName(codeDto.getVariableElement()) : StringUtils.EMPTY);
        contentDescriptorsEditionForm.setValue(CodeDS.VARIABLE_ELEMENT, codeDto.getVariableElement() != null ? codeDto.getVariableElement().getUrn() : StringUtils.EMPTY);

        // Comments
        commentsEditionForm.setValue(CodeDS.COMMENTS, RecordUtils.getInternationalStringRecord(codeDto.getComment()));

        // Annotations
        annotationsEditionPanel.setAnnotations(codeDto.getAnnotations());
    }

    private CodeMetamacDto getCodeDto() {
        // Identifiers Form
        codeDto.setCode(identifiersEditionForm.getValueAsString(CodeDS.CODE));
        codeDto.setName((InternationalStringDto) identifiersEditionForm.getValue(CodeDS.NAME));
        codeDto.setShortName((InternationalStringDto) identifiersEditionForm.getValue(CodeDS.SHORT_NAME));

        // Content descriptors
        codeDto.setDescription((InternationalStringDto) contentDescriptorsEditionForm.getValue(CodeDS.DESCRIPTION));
        codeDto.setVariableElement(!StringUtils.isBlank(contentDescriptorsEditionForm.getValueAsString(CodeDS.VARIABLE_ELEMENT)) ? RelatedResourceUtils
                .createRelatedResourceDto(contentDescriptorsEditionForm.getValueAsString(CodeDS.VARIABLE_ELEMENT)) : null);

        // Comments
        codeDto.setComment((InternationalStringDto) commentsEditionForm.getValue(CodeDS.COMMENTS));

        // Annotations
        codeDto.getAnnotations().clear();
        codeDto.getAnnotations().addAll(annotationsEditionPanel.getAnnotations());

        return codeDto;
    }

    private boolean validateEditionForms() {
        return identifiersEditionForm.validate(false) && contentDescriptorsEditionForm.validate(false);
    }

    private SearchViewTextItem createVariableElementItem(String name, String title) {
        SearchViewTextItem variableElementItem = new SearchViewTextItem(name, title);
        variableElementItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                showSearchVariableElementWindow(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent arg0) {
                        RelatedResourceDto selectedVariableElement = searchVariableElementWindow.getSelectedRelatedResource();
                        searchVariableElementWindow.markForDestroy();
                        // Set selected variable element in form
                        contentDescriptorsEditionForm.setValue(CodeDS.VARIABLE_ELEMENT, selectedVariableElement != null ? selectedVariableElement.getUrn() : null);
                        contentDescriptorsEditionForm.setValue(CodeDS.VARIABLE_ELEMENT_VIEW,
                                selectedVariableElement != null ? org.siemac.metamac.srm.web.client.utils.CommonUtils.getRelatedResourceName(selectedVariableElement) : null);
                    }
                });
            }
        });
        return variableElementItem;
    }

    private void showSearchVariableElementWindow(com.smartgwt.client.widgets.form.fields.events.ClickHandler acceptButtonClickHandler) {
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;

        final String codelistUrn = codeDto.getItemSchemeVersionUrn();

        searchVariableElementWindow = new SearchRelatedResourcePaginatedWindow(getConstants().variableElementSelection(), MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveVariableElements(firstResult, maxResults, searchVariableElementWindow.getRelatedResourceCriteria(), codelistUrn);
            }
        });

        // Load variables (to populate the selection window)
        getUiHandlers().retrieveVariableElements(FIRST_RESULST, MAX_RESULTS, null, codelistUrn);

        searchVariableElementWindow.getListGridItem().getListGrid().setSelectionType(SelectionStyle.SINGLE);
        searchVariableElementWindow.getListGridItem().setSearchAction(new SearchPaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                getUiHandlers().retrieveVariableElements(firstResult, maxResults, criteria, codelistUrn);
            }
        });
        searchVariableElementWindow.getSave().addClickHandler(acceptButtonClickHandler);
    }
}
