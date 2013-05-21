package org.siemac.metamac.srm.web.category.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.web.category.model.ds.CategoryDS;
import org.siemac.metamac.srm.web.category.presenter.CategoryPresenter;
import org.siemac.metamac.srm.web.category.utils.CategoriesFormUtils;
import org.siemac.metamac.srm.web.category.view.handlers.CategoryUiHandlers;
import org.siemac.metamac.srm.web.category.widgets.CategoriesTreeGrid;
import org.siemac.metamac.srm.web.category.widgets.CategoryMainFormLayout;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.client.widgets.AnnotationsPanel;
import org.siemac.metamac.srm.web.client.widgets.CustomVLayout;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.CustomSectionStack;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultilanguageRichTextEditorItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.layout.VLayout;

public class CategoryViewImpl extends ViewWithUiHandlers<CategoryUiHandlers> implements CategoryPresenter.CategoryView {

    private VLayout                  panel;
    private CategoryMainFormLayout   mainFormLayout;

    private CategoriesTreeGrid       categoriesTreeGrid;

    // View forms
    private GroupDynamicForm         identifiersForm;
    private GroupDynamicForm         productionDescriptorsForm;
    private GroupDynamicForm         contentDescriptorsForm;
    private GroupDynamicForm         commentsForm;
    private AnnotationsPanel         annotationsPanel;

    // Edition forms
    private GroupDynamicForm         identifiersEditionForm;
    private GroupDynamicForm         productionDescriptorsEditionForm;
    private GroupDynamicForm         contentDescriptorsEditionForm;
    private GroupDynamicForm         commentsEditionForm;
    private AnnotationsPanel         annotationsEditionPanel;

    private CategorySchemeMetamacDto categorySchemeDto;
    private CategoryMetamacDto       categoryDto;

    public CategoryViewImpl() {
        super();
        panel = new VLayout();
        panel.setHeight100();

        //
        // CATEGORIES HIERARCHY
        //

        categoriesTreeGrid = new CategoriesTreeGrid();
        categoriesTreeGrid.setAutoFitMaxRecords(10);

        CustomVLayout categoriesListGridLayout = new CustomVLayout();
        CustomSectionStack sectionStack = new CustomSectionStack(getConstants().categorySchemeCategories());
        sectionStack.getDefaultSection().setItems(categoriesTreeGrid);
        categoriesListGridLayout.addMember(sectionStack);

        //
        // CATEGORY
        //

        mainFormLayout = new CategoryMainFormLayout();
        bindMainFormLayoutEvents();

        createViewForm();
        createEditionForm();

        VLayout subPanel = new VLayout();
        subPanel.setOverflow(Overflow.SCROLL);
        subPanel.addMember(categoriesListGridLayout);
        subPanel.addMember(mainFormLayout);

        panel.addMember(subPanel);
    }

    @Override
    public void setUiHandlers(CategoryUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        categoriesTreeGrid.setUiHandlers(uiHandlers);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == CategoryPresenter.TYPE_SetContextAreaContentCategoriesToolBar) {
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
    public void setCategory(CategoryMetamacDto categoryDto, CategorySchemeMetamacDto categorySchemeMetamacDto) {
        setCategoryScheme(categorySchemeMetamacDto);
        setCategory(categoryDto);
    }

    @Override
    public void setCategory(CategoryMetamacDto categoryDto) {
        this.categoryDto = categoryDto;

        getUiHandlers().retrieveCategoryListByScheme(categoryDto.getItemSchemeVersionUrn());

        String defaultLocalisedName = InternationalStringUtils.getLocalisedString(categoryDto.getName());
        String title = defaultLocalisedName != null ? defaultLocalisedName : StringUtils.EMPTY;
        mainFormLayout.setTitleLabelContents(title);

        mainFormLayout.setViewMode();

        setCategoryViewMode(categoryDto);
        setCategoryEditionMode(categoryDto);

        markFormsForRedraw();
    }

    private void setCategoryScheme(CategorySchemeMetamacDto categorySchemeMetamacDto) {
        this.categorySchemeDto = categorySchemeMetamacDto;

        // Security
        mainFormLayout.setCategoryScheme(categorySchemeDto);

        markFormsForRedraw();
    }

    @Override
    public void setCategoryList(CategorySchemeMetamacDto categorySchemeDto, List<ItemVisualisationResult> itemVisualisationResults) {
        categoriesTreeGrid.setCategories(categorySchemeDto, itemVisualisationResults);
        categoriesTreeGrid.selectItem(categoryDto.getUrn());
    }

    private void createViewForm() {
        // Identifiers Form
        identifiersForm = new GroupDynamicForm(getConstants().formIdentifiers());
        ViewTextItem code = new ViewTextItem(CategoryDS.CODE, getConstants().identifiableArtefactCode());
        ViewMultiLanguageTextItem name = new ViewMultiLanguageTextItem(CategoryDS.NAME, getConstants().nameableArtefactName());
        ViewTextItem uri = new ViewTextItem(CategoryDS.URI, getConstants().identifiableArtefactUri());
        ViewTextItem urn = new ViewTextItem(CategoryDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(CategoryDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        identifiersForm.setFields(code, name, uri, urn, urnProvider);

        // Production descriptors
        productionDescriptorsForm = new GroupDynamicForm(getConstants().formProductionDescriptors());
        ViewTextItem creationDate = new ViewTextItem(CategoryDS.CREATION_DATE, getConstants().identifiableArtefactCreationDate());
        productionDescriptorsForm.setFields(creationDate);

        // Content descriptors
        contentDescriptorsForm = new GroupDynamicForm(getConstants().formContentDescriptors());
        ViewMultiLanguageTextItem description = new ViewMultiLanguageTextItem(CategoryDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        contentDescriptorsForm.setFields(description);

        // Comments
        commentsForm = new GroupDynamicForm(getConstants().nameableArtefactComments());
        ViewMultiLanguageTextItem comments = new ViewMultiLanguageTextItem(CategoryDS.COMMENTS, getConstants().nameableArtefactComments());
        commentsForm.setFields(comments);

        // Annotations
        annotationsPanel = new AnnotationsPanel(true);

        mainFormLayout.addViewCanvas(identifiersForm);
        mainFormLayout.addViewCanvas(productionDescriptorsForm);
        mainFormLayout.addViewCanvas(contentDescriptorsForm);
        mainFormLayout.addViewCanvas(commentsForm);
        mainFormLayout.addViewCanvas(annotationsPanel);
    }

    private void createEditionForm() {
        // Identifiers Form
        identifiersEditionForm = new GroupDynamicForm(getConstants().formIdentifiers());

        RequiredTextItem code = new RequiredTextItem(CategoryDS.CODE, getConstants().identifiableArtefactCode());
        code.setValidators(SemanticIdentifiersUtils.getCategoryIdentifierCustomValidator());
        code.setShowIfCondition(getCodeFormItemIfFunction());

        ViewTextItem staticCode = new ViewTextItem(CategoryDS.CODE_VIEW, getConstants().identifiableArtefactCode());
        staticCode.setShowIfCondition(getStaticCodeFormItemIfFunction());

        MultiLanguageTextItem name = new MultiLanguageTextItem(CategoryDS.NAME, getConstants().nameableArtefactName());
        name.setRequired(true);
        ViewTextItem uri = new ViewTextItem(CategoryDS.URI, getConstants().identifiableArtefactUri());
        ViewTextItem urn = new ViewTextItem(CategoryDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(CategoryDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        identifiersEditionForm.setFields(code, staticCode, name, uri, urn, urnProvider);

        // Production descriptors
        productionDescriptorsEditionForm = new GroupDynamicForm(getConstants().formProductionDescriptors());
        ViewTextItem creationDate = new ViewTextItem(CategoryDS.CREATION_DATE, getConstants().identifiableArtefactCreationDate());
        productionDescriptorsEditionForm.setFields(creationDate);

        // Content descriptors
        contentDescriptorsEditionForm = new GroupDynamicForm(getConstants().formContentDescriptors());
        MultilanguageRichTextEditorItem description = new MultilanguageRichTextEditorItem(CategoryDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        contentDescriptorsEditionForm.setFields(description);

        // Comments
        commentsEditionForm = new GroupDynamicForm(getConstants().nameableArtefactComments());
        MultilanguageRichTextEditorItem comments = new MultilanguageRichTextEditorItem(CategoryDS.COMMENTS, getConstants().nameableArtefactComments());
        commentsEditionForm.setFields(comments);

        // Annotations
        annotationsEditionPanel = new AnnotationsPanel(false);

        mainFormLayout.addEditionCanvas(identifiersEditionForm);
        mainFormLayout.addEditionCanvas(productionDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(contentDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(commentsEditionForm);
        mainFormLayout.addEditionCanvas(annotationsEditionPanel);
    }

    private void setCategoryViewMode(CategoryMetamacDto categoryDto) {
        // Identifiers Form
        identifiersForm.setValue(CategoryDS.CODE, categoryDto.getCode());
        identifiersForm.setValue(CategoryDS.NAME, RecordUtils.getInternationalStringRecord(categoryDto.getName()));
        identifiersForm.setValue(CategoryDS.URI, categoryDto.getUriProvider());
        identifiersForm.setValue(CategoryDS.URN, categoryDto.getUrn());
        identifiersForm.setValue(CategoryDS.URN_PROVIDER, categoryDto.getUrnProvider());

        // Production descriptors form
        productionDescriptorsForm.setValue(CategoryDS.CREATION_DATE, categoryDto.getCreatedDate());

        // Content descriptors
        contentDescriptorsForm.setValue(CategoryDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(categoryDto.getDescription()));

        // Comments
        commentsForm.setValue(CategoryDS.COMMENTS, RecordUtils.getInternationalStringRecord(categoryDto.getComment()));

        // Annotations
        annotationsPanel.setAnnotations(categoryDto.getAnnotations(), categorySchemeDto);
    }

    private void setCategoryEditionMode(CategoryMetamacDto categoryDto) {
        // Identifiers Form
        identifiersEditionForm.setValue(CategoryDS.CODE, categoryDto.getCode());
        identifiersEditionForm.setValue(CategoryDS.CODE_VIEW, categoryDto.getCode());
        identifiersEditionForm.setValue(CategoryDS.NAME, RecordUtils.getInternationalStringRecord(categoryDto.getName()));
        identifiersEditionForm.setValue(CategoryDS.URI, categoryDto.getUriProvider());
        identifiersEditionForm.setValue(CategoryDS.URN, categoryDto.getUrn());
        identifiersEditionForm.setValue(CategoryDS.URN_PROVIDER, categoryDto.getUrnProvider());

        // Production descriptors form
        productionDescriptorsEditionForm.setValue(CategoryDS.CREATION_DATE, categoryDto.getCreatedDate());

        // Content descriptors
        contentDescriptorsEditionForm.setValue(CategoryDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(categoryDto.getDescription()));

        // Comments
        commentsEditionForm.setValue(CategoryDS.COMMENTS, RecordUtils.getInternationalStringRecord(categoryDto.getComment()));

        // Annotations
        annotationsEditionPanel.setAnnotations(categoryDto.getAnnotations(), categorySchemeDto);
    }

    private void bindMainFormLayoutEvents() {
        mainFormLayout.getTranslateToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                boolean translationsShowed = mainFormLayout.getTranslateToolStripButton().isSelected();
                identifiersForm.setTranslationsShowed(translationsShowed);
                identifiersEditionForm.setTranslationsShowed(translationsShowed);

                productionDescriptorsForm.setTranslationsShowed(translationsShowed);
                productionDescriptorsEditionForm.setTranslationsShowed(translationsShowed);

                contentDescriptorsForm.setTranslationsShowed(translationsShowed);
                contentDescriptorsEditionForm.setTranslationsShowed(translationsShowed);

                commentsForm.setTranslationsShowed(translationsShowed);
                commentsEditionForm.setTranslationsShowed(translationsShowed);

                annotationsPanel.setTranslationsShowed(translationsShowed);
                annotationsEditionPanel.setTranslationsShowed(translationsShowed);
            }
        });

        mainFormLayout.getDeleteConfirmationWindow().getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deleteCategory(categoryDto);
            }
        });

        mainFormLayout.getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (validateEditionForms()) {
                    getUiHandlers().saveCategory(getCategoryDto());
                }
            }
        });
    }

    private boolean validateEditionForms() {
        return identifiersEditionForm.validate(false) && contentDescriptorsEditionForm.validate(false);
    }

    private CategoryMetamacDto getCategoryDto() {

        // Identifiers Form
        categoryDto.setCode(identifiersEditionForm.getValueAsString(CategoryDS.CODE));
        categoryDto.setName((InternationalStringDto) identifiersEditionForm.getValue(CategoryDS.NAME));

        // Content descriptors
        categoryDto.setDescription((InternationalStringDto) contentDescriptorsEditionForm.getValue(CategoryDS.DESCRIPTION));

        // Comments
        categoryDto.setComment((InternationalStringDto) commentsEditionForm.getValue(CategoryDS.COMMENTS));

        // Annotations
        categoryDto.getAnnotations().clear();
        categoryDto.getAnnotations().addAll(annotationsEditionPanel.getAnnotations());

        return categoryDto;
    }

    private void markFormsForRedraw() {
        identifiersForm.markForRedraw();
        identifiersEditionForm.markForRedraw();

        contentDescriptorsForm.markForRedraw();
        contentDescriptorsEditionForm.markForRedraw();

        commentsForm.markForRedraw();
        commentsEditionForm.markForRedraw();

        annotationsPanel.markForRedraw();
        annotationsEditionPanel.markForRedraw();
    }

    // ------------------------------------------------------------------------------------------------------------
    // FORM ITEM IF FUNCTIONS
    // ------------------------------------------------------------------------------------------------------------

    // CODE

    private FormItemIfFunction getCodeFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return CategoriesFormUtils.canCategoryCodeBeEdited(categorySchemeDto);
            }
        };
    }

    private FormItemIfFunction getStaticCodeFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !CategoriesFormUtils.canCategoryCodeBeEdited(categorySchemeDto);
            }
        };
    }
}
