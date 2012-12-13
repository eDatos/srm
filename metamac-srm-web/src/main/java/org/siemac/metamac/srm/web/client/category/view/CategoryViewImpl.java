package org.siemac.metamac.srm.web.client.category.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.web.client.category.model.ds.CategoryDS;
import org.siemac.metamac.srm.web.client.category.presenter.CategoryPresenter;
import org.siemac.metamac.srm.web.client.category.utils.CategoriesClientSecurityUtils;
import org.siemac.metamac.srm.web.client.category.view.handlers.CategoryUiHandlers;
import org.siemac.metamac.srm.web.client.category.widgets.CategoriesTreeGrid;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.widgets.AnnotationsPanel;
import org.siemac.metamac.srm.web.client.widgets.CustomVLayout;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.TitleLabel;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextAreaItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class CategoryViewImpl extends ViewWithUiHandlers<CategoryUiHandlers> implements CategoryPresenter.CategoryView {

    private VLayout                     panel;
    private InternationalMainFormLayout mainFormLayout;

    private CategoriesTreeGrid          categoriesTreeGrid;

    // View forms
    private GroupDynamicForm            identifiersForm;
    private GroupDynamicForm            contentDescriptorsForm;
    private AnnotationsPanel            annotationsPanel;

    // Edition forms
    private GroupDynamicForm            identifiersEditionForm;
    private GroupDynamicForm            contentDescriptorsEditionForm;
    private AnnotationsPanel            annotationsEditionPanel;

    private CategoryMetamacDto          categoryDto;

    public CategoryViewImpl() {
        super();
        panel = new VLayout();
        panel.setHeight100();
        panel.setOverflow(Overflow.SCROLL);

        //
        // CATEGORIES HIERARCHY
        //

        categoriesTreeGrid = new CategoriesTreeGrid();

        CustomVLayout categoriesListGridLayout = new CustomVLayout();
        categoriesListGridLayout.addMember(new TitleLabel(getConstants().categorySchemeCategories()));
        categoriesListGridLayout.addMember(categoriesTreeGrid);

        //
        // CATEGORY
        //

        mainFormLayout = new InternationalMainFormLayout();
        bindMainFormLayoutEvents();

        createViewForm();
        createEditionForm();

        panel.addMember(categoriesListGridLayout);
        panel.addMember(mainFormLayout);
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
        if (slot == CategoryPresenter.TYPE_SetContextAreaContentCategoryToolBar) {
            if (content != null) {
                Canvas[] canvas = ((ToolStrip) content).getMembers();
                for (int i = 0; i < canvas.length; i++) {
                    if (canvas[i] instanceof ToolStripButton) {
                        if (ToolStripButtonEnum.CATEGORIES.getValue().equals(((ToolStripButton) canvas[i]).getID())) {
                            ((ToolStripButton) canvas[i]).select();
                        }
                    }
                }
                panel.addMember(content, 0);
            }
        } else {
            // To support inheritance in your views it is good practice to call super.setInSlot when you can't handle the call.
            // Who knows, maybe the parent class knows what to do with this slot.
            super.setInSlot(slot, content);
        }
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
    }

    @Override
    public void setCategoryList(CategorySchemeMetamacDto categorySchemeMetamacDto, List<ItemHierarchyDto> itemHierarchyDtos) {
        categoriesTreeGrid.setItems(categorySchemeMetamacDto, itemHierarchyDtos);
        categoriesTreeGrid.selectItem(categoryDto);

        // Security
        mainFormLayout.setCanEdit(CategoriesClientSecurityUtils.canUpdateCategory(categorySchemeMetamacDto.getLifeCycle().getProcStatus()));

    }

    private void createViewForm() {
        // Identifiers Form
        identifiersForm = new GroupDynamicForm(getConstants().categoryIdentifiers());
        ViewTextItem code = new ViewTextItem(CategoryDS.CODE, getConstants().identifiableArtefactCode());
        ViewMultiLanguageTextItem name = new ViewMultiLanguageTextItem(CategoryDS.NAME, getConstants().nameableArtefactName());
        ViewTextItem uri = new ViewTextItem(CategoryDS.URI, getConstants().identifiableArtefactUri());
        ViewTextItem urn = new ViewTextItem(CategoryDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(CategoryDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        identifiersForm.setFields(code, name, uri, urn, urnProvider);

        // Content descriptors
        contentDescriptorsForm = new GroupDynamicForm(getConstants().categoryContentDescriptors());
        ViewMultiLanguageTextItem description = new ViewMultiLanguageTextItem(CategoryDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        contentDescriptorsForm.setFields(description);

        // Annotations
        annotationsPanel = new AnnotationsPanel(true);

        mainFormLayout.addViewCanvas(identifiersForm);
        mainFormLayout.addViewCanvas(contentDescriptorsForm);
        mainFormLayout.addViewCanvas(annotationsPanel);
    }

    private void createEditionForm() {
        // Identifiers Form
        identifiersEditionForm = new GroupDynamicForm(getConstants().categoryIdentifiers());
        RequiredTextItem code = new RequiredTextItem(CategoryDS.CODE, getConstants().identifiableArtefactCode());
        code.setValidators(CommonWebUtils.getSemanticIdentifierCustomValidator());
        MultiLanguageTextItem name = new MultiLanguageTextItem(CategoryDS.NAME, getConstants().nameableArtefactName());
        name.setRequired(true);
        ViewTextItem uri = new ViewTextItem(CategoryDS.URI, getConstants().identifiableArtefactUri());
        ViewTextItem urn = new ViewTextItem(CategoryDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(CategoryDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        identifiersEditionForm.setFields(code, name, uri, urn, urnProvider);

        // Content descriptors
        contentDescriptorsEditionForm = new GroupDynamicForm(getConstants().categoryContentDescriptors());
        MultiLanguageTextAreaItem description = new MultiLanguageTextAreaItem(CategoryDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        contentDescriptorsEditionForm.setFields(description);

        // Annotations
        annotationsEditionPanel = new AnnotationsPanel(false);

        mainFormLayout.addEditionCanvas(identifiersEditionForm);
        mainFormLayout.addEditionCanvas(contentDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(annotationsEditionPanel);
    }

    private void setCategoryViewMode(CategoryMetamacDto categoryDto) {
        // Identifiers Form
        identifiersForm.setValue(CategoryDS.CODE, categoryDto.getCode());
        identifiersForm.setValue(CategoryDS.NAME, RecordUtils.getInternationalStringRecord(categoryDto.getName()));
        identifiersForm.setValue(CategoryDS.URI, categoryDto.getUriProvider());
        identifiersForm.setValue(CategoryDS.URN, categoryDto.getUrn());
        identifiersForm.setValue(CategoryDS.URN_PROVIDER, categoryDto.getUrnProvider());

        // Content descriptors
        contentDescriptorsForm.setValue(CategoryDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(categoryDto.getDescription()));

        // Annotations
        annotationsPanel.setAnnotations(categoryDto.getAnnotations());
    }

    private void setCategoryEditionMode(CategoryMetamacDto categoryDto) {
        // Identifiers Form
        identifiersEditionForm.setValue(CategoryDS.CODE, categoryDto.getCode());
        identifiersEditionForm.setValue(CategoryDS.NAME, RecordUtils.getInternationalStringRecord(categoryDto.getName()));
        identifiersEditionForm.setValue(CategoryDS.URI, categoryDto.getUriProvider());
        identifiersEditionForm.setValue(CategoryDS.URN, categoryDto.getUrn());
        identifiersEditionForm.setValue(CategoryDS.URN_PROVIDER, categoryDto.getUrnProvider());

        // Content descriptors
        contentDescriptorsEditionForm.setValue(CategoryDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(categoryDto.getDescription()));

        // Annotations
        annotationsEditionPanel.setAnnotations(categoryDto.getAnnotations());
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

                annotationsPanel.setTranslationsShowed(translationsShowed);
                annotationsEditionPanel.setTranslationsShowed(translationsShowed);
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

        // Annotations
        categoryDto.getAnnotations().clear();
        categoryDto.getAnnotations().addAll(annotationsEditionPanel.getAnnotations());

        return categoryDto;
    }

}
