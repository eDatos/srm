package org.siemac.metamac.srm.web.category.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.List;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.BooleanUtils;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.category.model.ds.CategorySchemeDS;
import org.siemac.metamac.srm.web.category.model.record.CategorySchemeRecord;
import org.siemac.metamac.srm.web.category.presenter.CategorySchemePresenter;
import org.siemac.metamac.srm.web.category.utils.CategoriesClientSecurityUtils;
import org.siemac.metamac.srm.web.category.utils.CategoriesFormUtils;
import org.siemac.metamac.srm.web.category.view.handlers.CategorySchemeUiHandlers;
import org.siemac.metamac.srm.web.category.widgets.CategoriesTreeGrid;
import org.siemac.metamac.srm.web.category.widgets.CategorySchemeCategorisationsPanel;
import org.siemac.metamac.srm.web.category.widgets.CategorySchemeMainFormLayout;
import org.siemac.metamac.srm.web.category.widgets.CategorySchemeVersionsSectionStack;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.client.widgets.AnnotationsPanel;
import org.siemac.metamac.srm.web.client.widgets.VersionWindow;
import org.siemac.metamac.srm.web.shared.category.GetCategoriesResult;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemesResult;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.utils.DateUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.InformationWindow;
import org.siemac.metamac.web.common.client.widgets.TitleLabel;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextAreaItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class CategorySchemeViewImpl extends ViewWithUiHandlers<CategorySchemeUiHandlers> implements CategorySchemePresenter.CategorySchemeView {

    private VLayout                            panel;
    private CategorySchemeMainFormLayout       mainFormLayout;

    // View forms
    private GroupDynamicForm                   identifiersForm;
    private GroupDynamicForm                   contentDescriptorsForm;
    private GroupDynamicForm                   productionDescriptorsForm;
    private GroupDynamicForm                   diffusionDescriptorsForm;
    private GroupDynamicForm                   versionResponsibilityForm;
    private GroupDynamicForm                   commentsForm;
    private AnnotationsPanel                   annotationsPanel;

    // Edition forms
    private GroupDynamicForm                   identifiersEditionForm;
    private GroupDynamicForm                   contentDescriptorsEditionForm;
    private GroupDynamicForm                   productionDescriptorsEditionForm;
    private GroupDynamicForm                   diffusionDescriptorsEditionForm;
    private GroupDynamicForm                   versionResponsibilityEditionForm;
    private GroupDynamicForm                   commentsEditionForm;
    private AnnotationsPanel                   annotationsEditionPanel;

    // Versions
    private CategorySchemeVersionsSectionStack versionsSectionStack;

    // CategoriesTree
    private CategoriesTreeGrid                 categoriesTreeGrid;

    // Categorisations
    private CategorySchemeCategorisationsPanel categorisationsPanel;

    private CategorySchemeMetamacDto           categorySchemeDto;

    @Inject
    public CategorySchemeViewImpl() {
        super();
        panel = new VLayout();
        panel.setHeight100();

        //
        // CATEGORY SCHEME VERSIONS
        //

        versionsSectionStack = new CategorySchemeVersionsSectionStack(getConstants().categorySchemeVersions());
        versionsSectionStack.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                String urn = ((CategorySchemeRecord) event.getRecord()).getUrn();
                getUiHandlers().goToCategoryScheme(urn);
            }
        });

        //
        // CATEGORY SCHEME
        //

        mainFormLayout = new CategorySchemeMainFormLayout();
        bindMainFormLayoutEvents();
        createViewForm();
        createEditionForm();

        // CATEGORIES

        categoriesTreeGrid = new CategoriesTreeGrid();

        VLayout categoriesListGridLayout = new VLayout();
        categoriesListGridLayout.setMargin(15);
        categoriesListGridLayout.addMember(new TitleLabel(getConstants().categories()));
        categoriesListGridLayout.addMember(categoriesTreeGrid);

        categorisationsPanel = new CategorySchemeCategorisationsPanel();

        VLayout subPanel = new VLayout();
        subPanel.setOverflow(Overflow.SCROLL);
        subPanel.addMember(versionsSectionStack);
        subPanel.addMember(mainFormLayout);
        subPanel.addMember(categoriesListGridLayout);
        subPanel.addMember(categorisationsPanel);

        panel.addMember(subPanel);
    }

    @Override
    public void setUiHandlers(CategorySchemeUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        this.categorisationsPanel.setUiHandlers(uiHandlers);
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

                productionDescriptorsForm.setTranslationsShowed(translationsShowed);
                productionDescriptorsEditionForm.setTranslationsShowed(translationsShowed);

                diffusionDescriptorsForm.setTranslationsShowed(translationsShowed);
                diffusionDescriptorsEditionForm.setTranslationsShowed(translationsShowed);

                versionResponsibilityForm.setTranslationsShowed(translationsShowed);
                versionResponsibilityEditionForm.setTranslationsShowed(translationsShowed);

                commentsForm.setTranslationsShowed(translationsShowed);
                commentsEditionForm.setTranslationsShowed(translationsShowed);

                annotationsPanel.setTranslationsShowed(translationsShowed);
                annotationsEditionPanel.setTranslationsShowed(translationsShowed);
            }
        });

        // Edit: Add a custom handler to check scheme status before start editing
        mainFormLayout.getEditToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ProcStatusEnum status = categorySchemeDto.getLifeCycle().getProcStatus();
                if (ProcStatusEnum.INTERNALLY_PUBLISHED.equals(status) || ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(status)) {
                    // Create a new version
                    final InformationWindow window = new InformationWindow(getMessages().categorySchemeEditionInfo(), getMessages().categorySchemeEditionInfoDetailedMessage());
                    window.show();
                } else {
                    // Default behavior
                    setEditionMode();
                }
            }
        });

        // Save
        mainFormLayout.getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (identifiersEditionForm.validate(false) && contentDescriptorsEditionForm.validate(false) && productionDescriptorsEditionForm.validate(false)
                        && diffusionDescriptorsEditionForm.validate(false)) {
                    getUiHandlers().saveCategoryScheme(getCategorySchemeDto());
                }
            }
        });

        // Life cycle
        mainFormLayout.getSendToProductionValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().sendToProductionValidation(categorySchemeDto.getUrn(), categorySchemeDto.getLifeCycle().getProcStatus());
            }
        });
        mainFormLayout.getSendToDiffusionValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().sendToDiffusionValidation(categorySchemeDto.getUrn(), categorySchemeDto.getLifeCycle().getProcStatus());
            }
        });
        mainFormLayout.getRejectValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().rejectValidation(categorySchemeDto.getUrn(), categorySchemeDto.getLifeCycle().getProcStatus());
            }
        });
        mainFormLayout.getPublishInternally().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().publishInternally(categorySchemeDto.getUrn(), categorySchemeDto.getLifeCycle().getProcStatus());
            }
        });
        mainFormLayout.getPublishExternally().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().publishExternally(categorySchemeDto.getUrn(), categorySchemeDto.getLifeCycle().getProcStatus());
            }
        });
        mainFormLayout.getVersioning().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                final VersionWindow versionWindow = new VersionWindow(getConstants().lifeCycleVersioning());
                versionWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (versionWindow.validateForm()) {
                            getUiHandlers().versioning(categorySchemeDto.getUrn(), versionWindow.getSelectedVersion());
                            versionWindow.destroy();
                        }
                    }
                });
            }
        });
        mainFormLayout.getCancelValidity().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().cancelValidity(categorySchemeDto.getUrn());
            }
        });
    }

    private void setEditionMode() {
        mainFormLayout.setEditionMode();
    }

    private void createViewForm() {
        // Identifiers Form
        identifiersForm = new GroupDynamicForm(getConstants().formIdentifiers());
        ViewTextItem code = new ViewTextItem(CategorySchemeDS.CODE_VIEW, getConstants().identifiableArtefactCode());
        ViewMultiLanguageTextItem name = new ViewMultiLanguageTextItem(CategorySchemeDS.NAME_VIEW, getConstants().nameableArtefactName());
        ViewTextItem uri = new ViewTextItem(CategorySchemeDS.URI, getConstants().identifiableArtefactUri());
        ViewTextItem urn = new ViewTextItem(CategorySchemeDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(CategorySchemeDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        ViewTextItem version = new ViewTextItem(CategorySchemeDS.VERSION_LOGIC, getConstants().maintainableArtefactVersionLogic());
        identifiersForm.setFields(code, name, uri, urn, urnProvider, version);

        // Content descriptors
        contentDescriptorsForm = new GroupDynamicForm(getConstants().formContentDescriptors());
        ViewMultiLanguageTextItem description = new ViewMultiLanguageTextItem(CategorySchemeDS.DESCRIPTION_VIEW, getConstants().nameableArtefactDescription());
        ViewTextItem partial = new ViewTextItem(CategorySchemeDS.IS_PARTIAL, getConstants().itemSchemeIsPartial());
        ViewTextItem isExternalReference = new ViewTextItem(CategorySchemeDS.IS_EXTERNAL_REFERENCE, getConstants().maintainableArtefactIsExternalReference());
        ViewTextItem isFinal = new ViewTextItem(CategorySchemeDS.FINAL, getConstants().maintainableArtefactFinalLogic());
        contentDescriptorsForm.setFields(description, partial, isExternalReference, isFinal);

        // Production descriptors
        productionDescriptorsForm = new GroupDynamicForm(getConstants().formProductionDescriptors());
        ViewTextItem agency = new ViewTextItem(CategorySchemeDS.MAINTAINER, getConstants().maintainableArtefactMaintainer());
        ViewTextItem procStatus = new ViewTextItem(CategorySchemeDS.PROC_STATUS, getConstants().lifeCycleProcStatus());
        productionDescriptorsForm.setFields(agency, procStatus);

        // Diffusion descriptors
        diffusionDescriptorsForm = new GroupDynamicForm(getConstants().formDiffusionDescriptors());
        ViewTextItem replacedBy = new ViewTextItem(CategorySchemeDS.REPLACED_BY_VERSION, getConstants().maintainableArtefactReplacedByVersion());
        ViewTextItem replaceTo = new ViewTextItem(CategorySchemeDS.REPLACE_TO_VERSION, getConstants().maintainableArtefactReplaceToVersion());
        ViewTextItem validFrom = new ViewTextItem(CategorySchemeDS.VALID_FROM, getConstants().maintainableArtefactValidFrom());
        ViewTextItem validTo = new ViewTextItem(CategorySchemeDS.VALID_TO, getConstants().maintainableArtefactValidTo());
        ViewTextItem externalPublicationFailed = new ViewTextItem(CategorySchemeDS.IS_EXTERNAL_PUBLICATION_FAILED, getConstants().lifeCycleExternalPublicationFailed());
        ViewTextItem externalPublicationFailedDate = new ViewTextItem(CategorySchemeDS.EXTERNAL_PUBLICATION_FAILED_DATE, getConstants().lifeCycleExternalPublicationFailedDate());
        diffusionDescriptorsForm.setFields(replacedBy, replaceTo, validFrom, validTo, externalPublicationFailed, externalPublicationFailedDate);

        // Version responsibility
        versionResponsibilityForm = new GroupDynamicForm(getConstants().lifeCycleVersionResponsibility());
        ViewTextItem productionValidationUser = new ViewTextItem(CategorySchemeDS.PRODUCTION_VALIDATION_USER, getConstants().lifeCycleProductionValidationUser());
        ViewTextItem productionValidationDate = new ViewTextItem(CategorySchemeDS.PRODUCTION_VALIDATION_DATE, getConstants().lifeCycleProductionValidationDate());
        ViewTextItem diffusionValidationUser = new ViewTextItem(CategorySchemeDS.DIFFUSION_VALIDATION_USER, getConstants().lifeCycleDiffusionValidationUser());
        ViewTextItem diffusionValidationDate = new ViewTextItem(CategorySchemeDS.DIFFUSION_VALIDATION_DATE, getConstants().lifeCycleDiffusionValidationDate());
        ViewTextItem internalPublicationUser = new ViewTextItem(CategorySchemeDS.INTERNAL_PUBLICATION_USER, getConstants().lifeCycleInternalPublicationUser());
        ViewTextItem internalPublicationDate = new ViewTextItem(CategorySchemeDS.INTERNAL_PUBLICATION_DATE, getConstants().lifeCycleInternalPublicationDate());
        ViewTextItem externalPublicationUser = new ViewTextItem(CategorySchemeDS.EXTERNAL_PUBLICATION_USER, getConstants().lifeCycleExternalPublicationUser());
        ViewTextItem externalPublicationDate = new ViewTextItem(CategorySchemeDS.EXTERNAL_PUBLICATION_DATE, getConstants().lifeCycleExternalPublicationDate());
        versionResponsibilityForm.setFields(productionValidationUser, productionValidationDate, diffusionValidationUser, diffusionValidationDate, internalPublicationUser, internalPublicationDate,
                externalPublicationUser, externalPublicationDate);

        // Comments
        commentsForm = new GroupDynamicForm(getConstants().nameableArtefactComments());
        ViewMultiLanguageTextItem comments = new ViewMultiLanguageTextItem(CategorySchemeDS.COMMENTS, getConstants().nameableArtefactComments());
        commentsForm.setFields(comments);

        // Annotations
        annotationsPanel = new AnnotationsPanel(true);

        mainFormLayout.addViewCanvas(identifiersForm);
        mainFormLayout.addViewCanvas(contentDescriptorsForm);
        mainFormLayout.addViewCanvas(productionDescriptorsForm);
        mainFormLayout.addViewCanvas(diffusionDescriptorsForm);
        mainFormLayout.addViewCanvas(versionResponsibilityForm);
        mainFormLayout.addViewCanvas(commentsForm);
        mainFormLayout.addViewCanvas(annotationsPanel);
    }

    private void createEditionForm() {
        // Identifiers
        identifiersEditionForm = new GroupDynamicForm(getConstants().formIdentifiers());

        RequiredTextItem code = new RequiredTextItem(CategorySchemeDS.CODE, getConstants().identifiableArtefactCode());
        code.setValidators(SemanticIdentifiersUtils.getCategorySchemeIdentifierCustomValidator());
        code.setShowIfCondition(getCodeFormItemIfFunction());

        ViewTextItem staticCode = new ViewTextItem(CategorySchemeDS.CODE_VIEW, getConstants().identifiableArtefactCode());
        staticCode.setShowIfCondition(getStaticCodeFormItemIfFunction());

        MultiLanguageTextItem name = new MultiLanguageTextItem(CategorySchemeDS.NAME, getConstants().nameableArtefactName());
        name.setRequired(true);
        name.setShowIfCondition(getNameFormItemIfFunction());

        ViewMultiLanguageTextItem staticName = new ViewMultiLanguageTextItem(CategorySchemeDS.NAME_VIEW, getConstants().nameableArtefactName());
        staticName.setShowIfCondition(getStaticNameFormItemIfFunction());

        ViewTextItem uri = new ViewTextItem(CategorySchemeDS.URI, getConstants().identifiableArtefactUri());
        ViewTextItem urn = new ViewTextItem(CategorySchemeDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(CategorySchemeDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        ViewTextItem version = new ViewTextItem(CategorySchemeDS.VERSION_LOGIC, getConstants().maintainableArtefactVersionLogic());
        identifiersEditionForm.setFields(code, staticCode, name, staticName, uri, urn, urnProvider, version);

        // Content descriptors
        contentDescriptorsEditionForm = new GroupDynamicForm(getConstants().formContentDescriptors());

        MultiLanguageTextAreaItem description = new MultiLanguageTextAreaItem(CategorySchemeDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        description.setShowIfCondition(getDescriptionFormItemIfFunction());

        ViewMultiLanguageTextItem staticDescription = new ViewMultiLanguageTextItem(CategorySchemeDS.DESCRIPTION_VIEW, getConstants().nameableArtefactDescription());
        staticDescription.setShowIfCondition(getStaticDescriptionFormItemIfFunction());

        ViewTextItem partial = new ViewTextItem(CategorySchemeDS.IS_PARTIAL, getConstants().itemSchemeIsPartial());
        ViewTextItem isExternalReference = new ViewTextItem(CategorySchemeDS.IS_EXTERNAL_REFERENCE, getConstants().maintainableArtefactIsExternalReference());
        ViewTextItem isFinal = new ViewTextItem(CategorySchemeDS.FINAL, getConstants().maintainableArtefactFinalLogic());
        contentDescriptorsEditionForm.setFields(description, staticDescription, partial, isExternalReference, isFinal);

        // Production descriptors
        productionDescriptorsEditionForm = new GroupDynamicForm(getConstants().formProductionDescriptors());
        ViewTextItem agency = new ViewTextItem(CategorySchemeDS.MAINTAINER, getConstants().maintainableArtefactMaintainer());
        ViewTextItem procStatus = new ViewTextItem(CategorySchemeDS.PROC_STATUS, getConstants().lifeCycleProcStatus());
        productionDescriptorsEditionForm.setFields(agency, procStatus);

        // Diffusion descriptors
        diffusionDescriptorsEditionForm = new GroupDynamicForm(getConstants().formDiffusionDescriptors());
        ViewTextItem replacedBy = new ViewTextItem(CategorySchemeDS.REPLACED_BY_VERSION, getConstants().maintainableArtefactReplacedByVersion());
        ViewTextItem replaceTo = new ViewTextItem(CategorySchemeDS.REPLACE_TO_VERSION, getConstants().maintainableArtefactReplaceToVersion());
        ViewTextItem validFrom = new ViewTextItem(CategorySchemeDS.VALID_FROM, getConstants().maintainableArtefactValidFrom());
        ViewTextItem validTo = new ViewTextItem(CategorySchemeDS.VALID_TO, getConstants().maintainableArtefactValidTo());
        ViewTextItem externalPublicationFailed = new ViewTextItem(CategorySchemeDS.IS_EXTERNAL_PUBLICATION_FAILED, getConstants().lifeCycleExternalPublicationFailed());
        ViewTextItem externalPublicationFailedDate = new ViewTextItem(CategorySchemeDS.EXTERNAL_PUBLICATION_FAILED_DATE, getConstants().lifeCycleExternalPublicationFailedDate());
        diffusionDescriptorsEditionForm.setFields(replacedBy, replaceTo, validFrom, validTo, externalPublicationFailed, externalPublicationFailedDate);

        // Version responsibility
        versionResponsibilityEditionForm = new GroupDynamicForm(getConstants().lifeCycleVersionResponsibility());
        ViewTextItem productionValidationUser = new ViewTextItem(CategorySchemeDS.PRODUCTION_VALIDATION_USER, getConstants().lifeCycleProductionValidationUser());
        ViewTextItem productionValidationDate = new ViewTextItem(CategorySchemeDS.PRODUCTION_VALIDATION_DATE, getConstants().lifeCycleProductionValidationDate());
        ViewTextItem diffusionValidationUser = new ViewTextItem(CategorySchemeDS.DIFFUSION_VALIDATION_USER, getConstants().lifeCycleDiffusionValidationUser());
        ViewTextItem diffusionValidationDate = new ViewTextItem(CategorySchemeDS.DIFFUSION_VALIDATION_DATE, getConstants().lifeCycleDiffusionValidationDate());
        ViewTextItem internalPublicationUser = new ViewTextItem(CategorySchemeDS.INTERNAL_PUBLICATION_USER, getConstants().lifeCycleInternalPublicationUser());
        ViewTextItem internalPublicationDate = new ViewTextItem(CategorySchemeDS.INTERNAL_PUBLICATION_DATE, getConstants().lifeCycleInternalPublicationDate());
        ViewTextItem externalPublicationUser = new ViewTextItem(CategorySchemeDS.EXTERNAL_PUBLICATION_USER, getConstants().lifeCycleExternalPublicationUser());
        ViewTextItem externalPublicationDate = new ViewTextItem(CategorySchemeDS.EXTERNAL_PUBLICATION_DATE, getConstants().lifeCycleExternalPublicationDate());
        versionResponsibilityEditionForm.setFields(productionValidationUser, productionValidationDate, diffusionValidationUser, diffusionValidationDate, internalPublicationUser,
                internalPublicationDate, externalPublicationUser, externalPublicationDate);

        // Comments
        commentsEditionForm = new GroupDynamicForm(getConstants().nameableArtefactComments());
        MultiLanguageTextAreaItem comments = new MultiLanguageTextAreaItem(CategorySchemeDS.COMMENTS, getConstants().nameableArtefactComments());
        commentsEditionForm.setFields(comments);

        // Annotations
        annotationsEditionPanel = new AnnotationsPanel(false);

        mainFormLayout.addEditionCanvas(identifiersEditionForm);
        mainFormLayout.addEditionCanvas(contentDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(productionDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(diffusionDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(versionResponsibilityEditionForm);
        mainFormLayout.addEditionCanvas(commentsEditionForm);
        mainFormLayout.addEditionCanvas(annotationsEditionPanel);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == CategorySchemePresenter.TYPE_SetContextAreaContentCategoriesToolBar) {
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
    public void setCategoryScheme(CategorySchemeMetamacDto categorySchemeDto) {
        this.categorySchemeDto = categorySchemeDto;

        String defaultLocalisedName = InternationalStringUtils.getLocalisedString(categorySchemeDto.getName());
        String title = defaultLocalisedName != null ? defaultLocalisedName : StringUtils.EMPTY;
        mainFormLayout.setTitleLabelContents(title);

        // Security
        ProcStatusEnum procStatus = categorySchemeDto.getLifeCycle().getProcStatus();
        mainFormLayout.setCanEdit(CategoriesClientSecurityUtils.canUpdateCategoryScheme(procStatus));
        mainFormLayout.updatePublishSection(procStatus, categorySchemeDto.getValidTo());
        mainFormLayout.setViewMode();
        categorisationsPanel.updateVisibility(procStatus);

        setCategorySchemeViewMode(categorySchemeDto);
        setCategorySchemeEditionMode(categorySchemeDto);

        // Update category scheme in tree grid
        categoriesTreeGrid.updateItemScheme(categorySchemeDto);
    }

    @Override
    public void setCategorisations(List<CategorisationDto> categorisationDtos) {
        categorisationsPanel.setCategorisations(categorisationDtos);
    }

    @Override
    public void setCategorySchemesForCategorisations(GetCategorySchemesResult result) {
        categorisationsPanel.setCategorySchemes(result);
    }

    @Override
    public void setCategoriesForCategorisations(GetCategoriesResult result) {
        categorisationsPanel.setCategories(result);
    }

    public void setCategorySchemeViewMode(CategorySchemeMetamacDto categorySchemeDto) {
        // Identifiers
        identifiersForm.setValue(CategorySchemeDS.CODE_VIEW, categorySchemeDto.getCode());
        identifiersForm.setValue(CategorySchemeDS.URI, categorySchemeDto.getUriProvider());
        identifiersForm.setValue(CategorySchemeDS.URN, categorySchemeDto.getUrn());
        identifiersForm.setValue(CategorySchemeDS.URN_PROVIDER, categorySchemeDto.getUrnProvider());
        identifiersForm.setValue(CategorySchemeDS.VERSION_LOGIC, categorySchemeDto.getVersionLogic());
        identifiersForm.setValue(CategorySchemeDS.NAME_VIEW, RecordUtils.getInternationalStringRecord(categorySchemeDto.getName()));

        // Content descriptors
        contentDescriptorsForm.setValue(CategorySchemeDS.DESCRIPTION_VIEW, RecordUtils.getInternationalStringRecord(categorySchemeDto.getDescription()));
        contentDescriptorsForm.setValue(CategorySchemeDS.IS_PARTIAL, CommonUtils.getBooleanName(categorySchemeDto.getIsPartial()));
        contentDescriptorsForm.setValue(CategorySchemeDS.IS_EXTERNAL_REFERENCE, categorySchemeDto.getIsExternalReference() != null ? (categorySchemeDto.getIsExternalReference() ? MetamacWebCommon
                .getConstants().yes() : MetamacWebCommon.getConstants().no()) : StringUtils.EMPTY);
        contentDescriptorsForm.setValue(CategorySchemeDS.FINAL, categorySchemeDto.getFinalLogic() != null ? (categorySchemeDto.getFinalLogic()
                ? MetamacWebCommon.getConstants().yes()
                : MetamacWebCommon.getConstants().no()) : StringUtils.EMPTY);

        // Production descriptors
        productionDescriptorsForm.setValue(CategorySchemeDS.MAINTAINER, org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourceName(categorySchemeDto.getMaintainer()));
        productionDescriptorsForm.setValue(CategorySchemeDS.PROC_STATUS, org.siemac.metamac.srm.web.client.utils.CommonUtils.getProcStatusName(categorySchemeDto.getLifeCycle().getProcStatus()));

        // Diffusion descriptors
        diffusionDescriptorsForm.setValue(CategorySchemeDS.REPLACED_BY_VERSION, categorySchemeDto.getReplacedByVersion());
        diffusionDescriptorsForm.setValue(CategorySchemeDS.REPLACE_TO_VERSION, categorySchemeDto.getReplaceToVersion());
        diffusionDescriptorsForm.setValue(CategorySchemeDS.VALID_FROM, categorySchemeDto.getValidFrom());
        diffusionDescriptorsForm.setValue(CategorySchemeDS.VALID_TO, categorySchemeDto.getValidTo());
        diffusionDescriptorsForm.setValue(CategorySchemeDS.IS_EXTERNAL_PUBLICATION_FAILED, BooleanUtils.isTrue(categorySchemeDto.getLifeCycle().getIsExternalPublicationFailed()) ? MetamacWebCommon
                .getConstants().yes() : StringUtils.EMPTY);
        diffusionDescriptorsForm.setValue(CategorySchemeDS.EXTERNAL_PUBLICATION_FAILED_DATE, DateUtils.getFormattedDate(categorySchemeDto.getLifeCycle().getExternalPublicationFailedDate()));

        // Version responsibility
        versionResponsibilityForm.setValue(CategorySchemeDS.PRODUCTION_VALIDATION_USER, categorySchemeDto.getLifeCycle().getProductionValidationUser());
        versionResponsibilityForm.setValue(CategorySchemeDS.PRODUCTION_VALIDATION_DATE, categorySchemeDto.getLifeCycle().getProductionValidationDate());
        versionResponsibilityForm.setValue(CategorySchemeDS.DIFFUSION_VALIDATION_USER, categorySchemeDto.getLifeCycle().getDiffusionValidationUser());
        versionResponsibilityForm.setValue(CategorySchemeDS.DIFFUSION_VALIDATION_DATE, categorySchemeDto.getLifeCycle().getDiffusionValidationDate());
        versionResponsibilityForm.setValue(CategorySchemeDS.INTERNAL_PUBLICATION_USER, categorySchemeDto.getLifeCycle().getInternalPublicationUser());
        versionResponsibilityForm.setValue(CategorySchemeDS.INTERNAL_PUBLICATION_DATE, categorySchemeDto.getLifeCycle().getInternalPublicationDate());
        versionResponsibilityForm.setValue(CategorySchemeDS.EXTERNAL_PUBLICATION_USER, categorySchemeDto.getLifeCycle().getExternalPublicationUser());
        versionResponsibilityForm.setValue(CategorySchemeDS.EXTERNAL_PUBLICATION_DATE, categorySchemeDto.getLifeCycle().getExternalPublicationDate());

        // Comments
        commentsForm.setValue(CategorySchemeDS.COMMENTS, RecordUtils.getInternationalStringRecord(categorySchemeDto.getComment()));

        // Annotations
        annotationsPanel.setAnnotations(categorySchemeDto.getAnnotations());
    }

    public void setCategorySchemeEditionMode(CategorySchemeMetamacDto categorySchemeDto) {
        // Identifiers
        identifiersEditionForm.setValue(CategorySchemeDS.CODE, categorySchemeDto.getCode());
        identifiersEditionForm.setValue(CategorySchemeDS.CODE_VIEW, categorySchemeDto.getCode());
        identifiersEditionForm.setValue(CategorySchemeDS.URI, categorySchemeDto.getUriProvider());
        identifiersEditionForm.setValue(CategorySchemeDS.URN, categorySchemeDto.getUrn());
        identifiersEditionForm.setValue(CategorySchemeDS.URN_PROVIDER, categorySchemeDto.getUrnProvider());
        identifiersEditionForm.setValue(CategorySchemeDS.VERSION_LOGIC, categorySchemeDto.getVersionLogic());
        identifiersEditionForm.setValue(CategorySchemeDS.NAME, RecordUtils.getInternationalStringRecord(categorySchemeDto.getName()));
        identifiersEditionForm.setValue(CategorySchemeDS.NAME_VIEW, RecordUtils.getInternationalStringRecord(categorySchemeDto.getName()));
        identifiersEditionForm.markForRedraw();

        // Content descriptors
        contentDescriptorsEditionForm.setValue(CategorySchemeDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(categorySchemeDto.getDescription()));
        contentDescriptorsEditionForm.setValue(CategorySchemeDS.DESCRIPTION_VIEW, RecordUtils.getInternationalStringRecord(categorySchemeDto.getDescription()));
        contentDescriptorsEditionForm.setValue(CategorySchemeDS.IS_PARTIAL, CommonUtils.getBooleanName(categorySchemeDto.getIsPartial()));
        contentDescriptorsEditionForm.setValue(CategorySchemeDS.IS_EXTERNAL_REFERENCE, categorySchemeDto.getIsExternalReference() != null ? (categorySchemeDto.getIsExternalReference()
                ? MetamacWebCommon.getConstants().yes()
                : MetamacWebCommon.getConstants().no()) : StringUtils.EMPTY);
        contentDescriptorsEditionForm.setValue(CategorySchemeDS.FINAL, categorySchemeDto.getFinalLogic() != null ? (categorySchemeDto.getFinalLogic()
                ? MetamacWebCommon.getConstants().yes()
                : MetamacWebCommon.getConstants().no()) : StringUtils.EMPTY);
        contentDescriptorsEditionForm.markForRedraw();

        // Production descriptors
        productionDescriptorsEditionForm.setValue(CategorySchemeDS.MAINTAINER, org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourceName(categorySchemeDto.getMaintainer()));
        productionDescriptorsEditionForm
                .setValue(CategorySchemeDS.PROC_STATUS, org.siemac.metamac.srm.web.client.utils.CommonUtils.getProcStatusName(categorySchemeDto.getLifeCycle().getProcStatus()));

        // Diffusion descriptors
        diffusionDescriptorsEditionForm.setValue(CategorySchemeDS.REPLACED_BY_VERSION, categorySchemeDto.getReplacedByVersion());
        diffusionDescriptorsEditionForm.setValue(CategorySchemeDS.REPLACE_TO_VERSION, categorySchemeDto.getReplaceToVersion());
        diffusionDescriptorsEditionForm.setValue(CategorySchemeDS.VALID_FROM, DateUtils.getFormattedDate(categorySchemeDto.getValidFrom()));
        diffusionDescriptorsEditionForm.setValue(CategorySchemeDS.VALID_TO, DateUtils.getFormattedDate(categorySchemeDto.getValidTo()));
        diffusionDescriptorsEditionForm.setValue(CategorySchemeDS.IS_EXTERNAL_PUBLICATION_FAILED, BooleanUtils.isTrue(categorySchemeDto.getLifeCycle().getIsExternalPublicationFailed())
                ? MetamacWebCommon.getConstants().yes()
                : MetamacWebCommon.getConstants().no());
        diffusionDescriptorsEditionForm.setValue(CategorySchemeDS.EXTERNAL_PUBLICATION_FAILED_DATE, DateUtils.getFormattedDate(categorySchemeDto.getLifeCycle().getExternalPublicationFailedDate()));

        // Version responsibility
        versionResponsibilityEditionForm.setValue(CategorySchemeDS.PRODUCTION_VALIDATION_USER, categorySchemeDto.getLifeCycle().getProductionValidationUser());
        versionResponsibilityEditionForm.setValue(CategorySchemeDS.PRODUCTION_VALIDATION_DATE, categorySchemeDto.getLifeCycle().getProductionValidationDate());
        versionResponsibilityEditionForm.setValue(CategorySchemeDS.DIFFUSION_VALIDATION_USER, categorySchemeDto.getLifeCycle().getDiffusionValidationUser());
        versionResponsibilityEditionForm.setValue(CategorySchemeDS.DIFFUSION_VALIDATION_DATE, categorySchemeDto.getLifeCycle().getDiffusionValidationDate());
        versionResponsibilityEditionForm.setValue(CategorySchemeDS.INTERNAL_PUBLICATION_USER, categorySchemeDto.getLifeCycle().getInternalPublicationUser());
        versionResponsibilityEditionForm.setValue(CategorySchemeDS.INTERNAL_PUBLICATION_DATE, categorySchemeDto.getLifeCycle().getInternalPublicationDate());
        versionResponsibilityEditionForm.setValue(CategorySchemeDS.EXTERNAL_PUBLICATION_USER, categorySchemeDto.getLifeCycle().getExternalPublicationUser());
        versionResponsibilityEditionForm.setValue(CategorySchemeDS.EXTERNAL_PUBLICATION_DATE, categorySchemeDto.getLifeCycle().getExternalPublicationDate());

        // Comments
        commentsEditionForm.setValue(CategorySchemeDS.COMMENTS, RecordUtils.getInternationalStringRecord(categorySchemeDto.getComment()));

        // Annotations
        annotationsEditionPanel.setAnnotations(categorySchemeDto.getAnnotations());
    }

    @Override
    public void setCategorySchemeVersions(List<CategorySchemeMetamacDto> categorySchemeMetamacDtos) {
        versionsSectionStack.setCategorySchemes(categorySchemeMetamacDtos);
        versionsSectionStack.selectCategoryScheme(categorySchemeDto);
    }

    @Override
    public void setCategoryList(List<ItemHierarchyDto> categoryDtos) {
        // Category hierarchy
        categoriesTreeGrid.setUiHandlers(getUiHandlers()); // UiHandlers cannot be set in constructor because is still null
        categoriesTreeGrid.setItems(categorySchemeDto, categoryDtos);
    }

    public CategorySchemeMetamacDto getCategorySchemeDto() {

        // SDMX METADATA

        if (CommonUtils.isDefaultMaintainer(categorySchemeDto.getMaintainer())) {
            // Identifiers
            categorySchemeDto.setCode(identifiersEditionForm.getValueAsString(CategorySchemeDS.CODE));
            categorySchemeDto.setName((InternationalStringDto) identifiersEditionForm.getValue(CategorySchemeDS.NAME));
            // Content descriptors
            categorySchemeDto.setDescription((InternationalStringDto) contentDescriptorsEditionForm.getValue(CategorySchemeDS.DESCRIPTION));

            // Annotations
            categorySchemeDto.getAnnotations().clear();
            categorySchemeDto.getAnnotations().addAll(annotationsEditionPanel.getAnnotations());
        }

        // METAMAC METADATA

        // Comments
        categorySchemeDto.setComment((InternationalStringDto) commentsEditionForm.getValue(CategorySchemeDS.COMMENTS));

        return categorySchemeDto;
    }

    // ------------------------------------------------------------------------------------------------------------
    // FORM ITEM IF FUNCTIONS
    // ------------------------------------------------------------------------------------------------------------

    private FormItemIfFunction getCodeFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return CategoriesFormUtils.canCategorySchemeCodeBeEdited(categorySchemeDto);
            }
        };
    }

    private FormItemIfFunction getStaticCodeFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !CategoriesFormUtils.canCategorySchemeCodeBeEdited(categorySchemeDto);
            }
        };
    }

    private FormItemIfFunction getNameFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return CategoriesFormUtils.canCategorySchemeNameBeEdited(categorySchemeDto);
            }
        };
    }

    private FormItemIfFunction getStaticNameFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !CategoriesFormUtils.canCategorySchemeNameBeEdited(categorySchemeDto);
            }
        };
    }

    private FormItemIfFunction getDescriptionFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return CategoriesFormUtils.canCategorySchemeDescriptionBeEdited(categorySchemeDto);
            }
        };
    }

    private FormItemIfFunction getStaticDescriptionFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !CategoriesFormUtils.canCategorySchemeDescriptionBeEdited(categorySchemeDto);
            }
        };
    }
}
