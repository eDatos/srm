package org.siemac.metamac.srm.web.client.code.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.List;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.BooleanUtils;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.code.model.ds.CodelistDS;
import org.siemac.metamac.srm.web.client.code.model.record.CodelistRecord;
import org.siemac.metamac.srm.web.client.code.presenter.CodelistPresenter;
import org.siemac.metamac.srm.web.client.code.utils.CommonUtils;
import org.siemac.metamac.srm.web.client.code.view.handlers.CodelistUiHandlers;
import org.siemac.metamac.srm.web.client.code.widgets.CodelistMainFormLayout;
import org.siemac.metamac.srm.web.client.code.widgets.CodelistVersionsSectionStack;
import org.siemac.metamac.srm.web.client.code.widgets.CodesTreeGrid;
import org.siemac.metamac.srm.web.client.widgets.AnnotationsPanel;
import org.siemac.metamac.srm.web.client.widgets.RelatedResourceListItem;
import org.siemac.metamac.srm.web.client.widgets.SearchMultipleRelatedResourceWindow;
import org.siemac.metamac.srm.web.client.widgets.SearchRelatedResourceWindow;
import org.siemac.metamac.srm.web.client.widgets.VersionWindow;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.utils.DateUtils;
import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.InformationWindow;
import org.siemac.metamac.web.common.client.widgets.TitleLabel;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.BooleanSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomSelectItem;
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
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class CodelistViewImpl extends ViewWithUiHandlers<CodelistUiHandlers> implements CodelistPresenter.CodelistView {

    private VLayout                             panel;
    private CodelistMainFormLayout              mainFormLayout;

    // View forms
    private GroupDynamicForm                    identifiersForm;
    private GroupDynamicForm                    contentDescriptorsForm;
    private GroupDynamicForm                    productionDescriptorsForm;
    private GroupDynamicForm                    diffusionDescriptorsForm;
    private GroupDynamicForm                    versionResponsibilityForm;
    private GroupDynamicForm                    commentsForm;
    private AnnotationsPanel                    annotationsPanel;

    // Edition forms
    private GroupDynamicForm                    identifiersEditionForm;
    private GroupDynamicForm                    contentDescriptorsEditionForm;
    private GroupDynamicForm                    productionDescriptorsEditionForm;
    private GroupDynamicForm                    diffusionDescriptorsEditionForm;
    private GroupDynamicForm                    versionResponsibilityEditionForm;
    private GroupDynamicForm                    commentsEditionForm;
    private AnnotationsPanel                    annotationsEditionPanel;

    private CodesTreeGrid                       codesTreeGrid;

    private CodelistVersionsSectionStack        versionsSectionStack;

    private SearchRelatedResourceWindow         searchFamilyWindow;
    private SearchMultipleRelatedResourceWindow searchReplaceToCodelistsWindow;

    private CodelistMetamacDto                  codelistDto;

    @Inject
    public CodelistViewImpl() {
        super();
        panel = new VLayout();
        panel.setHeight100();
        panel.setOverflow(Overflow.SCROLL);

        //
        // CODELIST
        //

        mainFormLayout = new CodelistMainFormLayout(); // TODO Security
        bindMainFormLayoutEvents();
        createViewForm();
        createEditionForm();

        //
        // CODELIST VERSIONS
        //

        versionsSectionStack = new CodelistVersionsSectionStack(getConstants().codelistVersions());
        versionsSectionStack.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                String urn = ((CodelistRecord) event.getRecord()).getUrn();
                getUiHandlers().goToCodelist(urn);
            }
        });

        //
        // CODES
        //

        codesTreeGrid = new CodesTreeGrid();

        VLayout codesListGridLayout = new VLayout();
        codesListGridLayout.setMargin(15);
        codesListGridLayout.addMember(new TitleLabel(getConstants().codes()));
        codesListGridLayout.addMember(codesTreeGrid);

        panel.addMember(mainFormLayout);
        panel.addMember(versionsSectionStack);
        panel.addMember(codesListGridLayout);
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
                ProcStatusEnum status = codelistDto.getLifeCycle().getProcStatus();
                if (ProcStatusEnum.INTERNALLY_PUBLISHED.equals(status) || ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(status)) {
                    // Create a new version
                    final InformationWindow window = new InformationWindow(getMessages().codelistEditionInfo(), getMessages().codelistEditionInfoDetailedMessage());
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
                    getUiHandlers().saveCodelist(getCodelistDto());
                }
            }
        });

        // Life cycle
        mainFormLayout.getSendToProductionValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().sendToProductionValidation(codelistDto.getUrn(), codelistDto.getLifeCycle().getProcStatus());
            }
        });
        mainFormLayout.getSendToDiffusionValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().sendToDiffusionValidation(codelistDto.getUrn(), codelistDto.getLifeCycle().getProcStatus());
            }
        });
        mainFormLayout.getRejectValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().rejectValidation(codelistDto.getUrn(), codelistDto.getLifeCycle().getProcStatus());
            }
        });
        mainFormLayout.getPublishInternally().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().publishInternally(codelistDto.getUrn(), codelistDto.getLifeCycle().getProcStatus());
            }
        });
        mainFormLayout.getPublishExternally().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().publishExternally(codelistDto.getUrn(), codelistDto.getLifeCycle().getProcStatus());
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
                            getUiHandlers().versioning(codelistDto.getUrn(), versionWindow.getSelectedVersion());
                            versionWindow.destroy();
                        }
                    }
                });
            }
        });
        mainFormLayout.getCancelValidity().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().cancelValidity(codelistDto.getUrn());
            }
        });
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == CodelistPresenter.TYPE_SetContextAreaContentCodesToolBar) {
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
    public void setUiHandlers(CodelistUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        this.codesTreeGrid.setUiHandlers(uiHandlers);
    }

    @Override
    public void setCodelist(CodelistMetamacDto codelist) {
        this.codelistDto = codelist;

        String defaultLocalized = InternationalStringUtils.getLocalisedString(codelist.getName());
        String title = defaultLocalized != null ? defaultLocalized : StringUtils.EMPTY;
        mainFormLayout.setTitleLabelContents(title);

        mainFormLayout.updatePublishSection(codelist.getLifeCycle().getProcStatus(), codelist.getValidTo());
        mainFormLayout.setViewMode();

        setCodelistViewMode(codelist);
        setCodelistEditionMode(codelist);

        // Update codelist in tree grid
        codesTreeGrid.updateItemScheme(codelist);
    }

    @Override
    public void setCodes(List<ItemHierarchyDto> itemHierarchyDtos) {
        codesTreeGrid.setItems(codelistDto, itemHierarchyDtos);
    }

    @Override
    public void setCodelistVersions(List<CodelistMetamacDto> codelistDtos) {
        versionsSectionStack.setCodelists(codelistDtos);
        versionsSectionStack.selectCodelist(codelistDto);
    }

    @Override
    public void setFamilies(List<RelatedResourceDto> families, int firstResult, int totalResults) {
        if (searchFamilyWindow != null) {
            searchFamilyWindow.setRelatedResources(families);
            searchFamilyWindow.refreshSourcePaginationInfo(firstResult, families.size(), totalResults);
        }
    }

    @Override
    public void setCodelistsToReplace(List<RelatedResourceDto> codelists, int firstResult, int totalResults) {
        if (searchReplaceToCodelistsWindow != null) {
            searchReplaceToCodelistsWindow.setSourceRelatedResources(codelists);
            searchReplaceToCodelistsWindow.refreshSourcePaginationInfo(firstResult, codelists.size(), totalResults);
        }
    }

    private void createViewForm() {
        // Identifiers Form
        identifiersForm = new GroupDynamicForm(getConstants().codelistIdentifiers());
        ViewTextItem code = new ViewTextItem(CodelistDS.CODE, getConstants().identifiableArtefactCode());
        ViewMultiLanguageTextItem name = new ViewMultiLanguageTextItem(CodelistDS.NAME, getConstants().nameableArtefactName());
        ViewMultiLanguageTextItem shortName = new ViewMultiLanguageTextItem(CodelistDS.SHORT_NAME, getConstants().codelistShortName());
        ViewTextItem uri = new ViewTextItem(CodelistDS.URI, getConstants().identifiableArtefactUri());
        ViewTextItem urn = new ViewTextItem(CodelistDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(CodelistDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        ViewTextItem version = new ViewTextItem(CodelistDS.VERSION_LOGIC, getConstants().maintainableArtefactVersionLogic());
        identifiersForm.setFields(code, name, shortName, uri, urn, urnProvider, version);

        // Content descriptors
        contentDescriptorsForm = new GroupDynamicForm(getConstants().codelistContentDescriptors());
        ViewMultiLanguageTextItem description = new ViewMultiLanguageTextItem(CodelistDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        ViewTextItem partial = new ViewTextItem(CodelistDS.IS_PARTIAL, getConstants().itemSchemeIsPartial());
        ViewTextItem isExternalReference = new ViewTextItem(CodelistDS.IS_EXTERNAL_REFERENCE, getConstants().maintainableArtefactIsExternalReference());
        ViewTextItem isFinal = new ViewTextItem(CodelistDS.FINAL, getConstants().maintainableArtefactFinalLogic());
        ViewTextItem isRecommended = new ViewTextItem(CodelistDS.IS_RECOMMENDED, getConstants().codelistIsRecommended());
        ViewTextItem family = new ViewTextItem(CodelistDS.FAMILY_VIEW, getConstants().codelistFamily());
        contentDescriptorsForm.setFields(description, partial, isExternalReference, isFinal, isRecommended, family);

        // Production descriptors
        productionDescriptorsForm = new GroupDynamicForm(getConstants().codelistProductionDescriptors());
        ViewTextItem agency = new ViewTextItem(CodelistDS.MAINTAINER, getConstants().maintainableArtefactMaintainer());
        ViewTextItem procStatus = new ViewTextItem(CodelistDS.PROC_STATUS, getConstants().lifeCycleProcStatus());
        productionDescriptorsForm.setFields(agency, procStatus);

        // Diffusion descriptors
        diffusionDescriptorsForm = new GroupDynamicForm(getConstants().codelistDiffusionDescriptors());
        RelatedResourceListItem replaceToCodelists = new RelatedResourceListItem(CodelistDS.REPLACE_TO_CODELISTS, getConstants().codelistReplaceToCodelists(), false);
        ViewTextItem replacedByCodelist = new ViewTextItem(CodelistDS.REPLACED_BY_CODELIST, getConstants().codelistReplacedByCodelist());
        ViewTextItem accessType = new ViewTextItem(CodelistDS.ACCESS_TYPE, getConstants().codelistAccessType());
        ViewTextItem replacedByVersion = new ViewTextItem(CodelistDS.REPLACED_BY_VERSION, getConstants().maintainableArtefactReplacedByVersion());
        ViewTextItem replaceTo = new ViewTextItem(CodelistDS.REPLACE_TO_VERSION, getConstants().maintainableArtefactReplaceToVersion());
        ViewTextItem validFrom = new ViewTextItem(CodelistDS.VALID_FROM, getConstants().maintainableArtefactValidFrom());
        ViewTextItem validTo = new ViewTextItem(CodelistDS.VALID_TO, getConstants().maintainableArtefactValidTo());
        ViewTextItem externalPublicationFailed = new ViewTextItem(CodelistDS.IS_EXTERNAL_PUBLICATION_FAILED, getConstants().lifeCycleExternalPublicationFailed());
        ViewTextItem externalPublicationFailedDate = new ViewTextItem(CodelistDS.EXTERNAL_PUBLICATION_FAILED_DATE, getConstants().lifeCycleExternalPublicationFailedDate());
        diffusionDescriptorsForm.setFields(replaceToCodelists, replacedByCodelist, accessType, replacedByVersion, replaceTo, validFrom, validTo, externalPublicationFailed,
                externalPublicationFailedDate);

        // Version responsibility
        versionResponsibilityForm = new GroupDynamicForm(getConstants().lifeCycleVersionResponsibility());
        ViewTextItem productionValidationUser = new ViewTextItem(CodelistDS.PRODUCTION_VALIDATION_USER, getConstants().lifeCycleProductionValidationUser());
        ViewTextItem productionValidationDate = new ViewTextItem(CodelistDS.PRODUCTION_VALIDATION_DATE, getConstants().lifeCycleProductionValidationDate());
        ViewTextItem diffusionValidationUser = new ViewTextItem(CodelistDS.DIFFUSION_VALIDATION_USER, getConstants().lifeCycleDiffusionValidationUser());
        ViewTextItem diffusionValidationDate = new ViewTextItem(CodelistDS.DIFFUSION_VALIDATION_DATE, getConstants().lifeCycleDiffusionValidationDate());
        ViewTextItem internalPublicationUser = new ViewTextItem(CodelistDS.INTERNAL_PUBLICATION_USER, getConstants().lifeCycleInternalPublicationUser());
        ViewTextItem internalPublicationDate = new ViewTextItem(CodelistDS.INTERNAL_PUBLICATION_DATE, getConstants().lifeCycleInternalPublicationDate());
        ViewTextItem externalPublicationUser = new ViewTextItem(CodelistDS.EXTERNAL_PUBLICATION_USER, getConstants().lifeCycleExternalPublicationUser());
        ViewTextItem externalPublicationDate = new ViewTextItem(CodelistDS.EXTERNAL_PUBLICATION_DATE, getConstants().lifeCycleExternalPublicationDate());
        versionResponsibilityForm.setFields(productionValidationUser, productionValidationDate, diffusionValidationUser, diffusionValidationDate, internalPublicationUser, internalPublicationDate,
                externalPublicationUser, externalPublicationDate);

        // Comments
        commentsForm = new GroupDynamicForm(getConstants().nameableArtefactComments());
        ViewMultiLanguageTextItem comments = new ViewMultiLanguageTextItem(CodelistDS.COMMENTS, getConstants().nameableArtefactComments());
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
        identifiersEditionForm = new GroupDynamicForm(getConstants().codelistIdentifiers());
        RequiredTextItem code = new RequiredTextItem(CodelistDS.CODE, getConstants().identifiableArtefactCode());
        code.setValidators(CommonWebUtils.getSemanticIdentifierCustomValidator());
        code.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                // CODE cannot be modified if status is INTERNALLY_PUBLISHED or EXTERNALLY_PUBLISHED, or if version is greater than VERSION_INITIAL_VERSION (01.000)
                return org.siemac.metamac.srm.web.client.utils.CommonUtils.canCodeBeEdited(codelistDto.getLifeCycle().getProcStatus(), codelistDto.getVersionLogic());
            }
        });
        ViewTextItem staticCode = new ViewTextItem(CodelistDS.CODE_VIEW, getConstants().identifiableArtefactCode());
        staticCode.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !org.siemac.metamac.srm.web.client.utils.CommonUtils.canCodeBeEdited(codelistDto.getLifeCycle().getProcStatus(), codelistDto.getVersionLogic());
            }
        });
        MultiLanguageTextItem name = new MultiLanguageTextItem(CodelistDS.NAME, getConstants().nameableArtefactName());
        name.setRequired(true);
        MultiLanguageTextItem shortName = new MultiLanguageTextItem(CodelistDS.SHORT_NAME, getConstants().codelistShortName());
        ViewTextItem uri = new ViewTextItem(CodelistDS.URI, getConstants().identifiableArtefactUri());
        ViewTextItem urn = new ViewTextItem(CodelistDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(CodelistDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        ViewTextItem version = new ViewTextItem(CodelistDS.VERSION_LOGIC, getConstants().maintainableArtefactVersionLogic());
        identifiersEditionForm.setFields(code, staticCode, name, shortName, uri, urn, urnProvider, version);

        // Content descriptors
        contentDescriptorsEditionForm = new GroupDynamicForm(getConstants().codelistContentDescriptors());
        MultiLanguageTextAreaItem description = new MultiLanguageTextAreaItem(CodelistDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        BooleanSelectItem partial = new BooleanSelectItem(CodelistDS.IS_PARTIAL, getConstants().itemSchemeIsPartial());
        ViewTextItem isExternalReference = new ViewTextItem(CodelistDS.IS_EXTERNAL_REFERENCE, getConstants().maintainableArtefactIsExternalReference());
        ViewTextItem isFinal = new ViewTextItem(CodelistDS.FINAL, getConstants().maintainableArtefactFinalLogic());
        BooleanSelectItem isRecommended = new BooleanSelectItem(CodelistDS.IS_RECOMMENDED, getConstants().codelistIsRecommended());
        ViewTextItem family = new ViewTextItem(CodelistDS.FAMILY, getConstants().codelistFamily());
        family.setShowIfCondition(FormItemUtils.getFalseFormItemIfFunction());
        SearchViewTextItem familyView = createFamilyItem(CodelistDS.FAMILY_VIEW, getConstants().codelistFamily());
        contentDescriptorsEditionForm.setFields(description, partial, isExternalReference, isFinal, isRecommended, family, familyView);

        // Production descriptors
        productionDescriptorsEditionForm = new GroupDynamicForm(getConstants().codelistProductionDescriptors());
        ViewTextItem agency = new ViewTextItem(CodelistDS.MAINTAINER, getConstants().maintainableArtefactMaintainer());
        ViewTextItem procStatus = new ViewTextItem(CodelistDS.PROC_STATUS, getConstants().lifeCycleProcStatus());
        productionDescriptorsEditionForm.setFields(agency, procStatus);

        // Diffusion descriptors
        diffusionDescriptorsEditionForm = new GroupDynamicForm(getConstants().codelistDiffusionDescriptors());
        RelatedResourceListItem replaceToCodelists = createReplaceToCodelistsItem(CodelistDS.REPLACE_TO_CODELISTS, getConstants().codelistReplaceToCodelists());
        ViewTextItem replacedByCodelist = new ViewTextItem(CodelistDS.REPLACED_BY_CODELIST, getConstants().codelistReplacedByCodelist());
        CustomSelectItem accessType = new CustomSelectItem(CodelistDS.ACCESS_TYPE, getConstants().codelistAccessType());
        accessType.setValueMap(CommonUtils.getAccessTypeHashMap());
        ViewTextItem replacedByVersion = new ViewTextItem(CodelistDS.REPLACED_BY_VERSION, getConstants().maintainableArtefactReplacedByVersion());
        ViewTextItem replaceTo = new ViewTextItem(CodelistDS.REPLACE_TO_VERSION, getConstants().maintainableArtefactReplaceToVersion());
        ViewTextItem validFrom = new ViewTextItem(CodelistDS.VALID_FROM, getConstants().maintainableArtefactValidFrom());
        ViewTextItem validTo = new ViewTextItem(CodelistDS.VALID_TO, getConstants().maintainableArtefactValidTo());
        ViewTextItem externalPublicationFailed = new ViewTextItem(CodelistDS.IS_EXTERNAL_PUBLICATION_FAILED, getConstants().lifeCycleExternalPublicationFailed());
        ViewTextItem externalPublicationFailedDate = new ViewTextItem(CodelistDS.EXTERNAL_PUBLICATION_FAILED_DATE, getConstants().lifeCycleExternalPublicationFailedDate());
        diffusionDescriptorsEditionForm.setFields(replaceToCodelists, replacedByCodelist, accessType, replacedByVersion, replaceTo, validFrom, validTo, externalPublicationFailed,
                externalPublicationFailedDate);

        // Version responsibility
        versionResponsibilityEditionForm = new GroupDynamicForm(getConstants().lifeCycleVersionResponsibility());
        ViewTextItem productionValidationUser = new ViewTextItem(CodelistDS.PRODUCTION_VALIDATION_USER, getConstants().lifeCycleProductionValidationUser());
        ViewTextItem productionValidationDate = new ViewTextItem(CodelistDS.PRODUCTION_VALIDATION_DATE, getConstants().lifeCycleProductionValidationDate());
        ViewTextItem diffusionValidationUser = new ViewTextItem(CodelistDS.DIFFUSION_VALIDATION_USER, getConstants().lifeCycleDiffusionValidationUser());
        ViewTextItem diffusionValidationDate = new ViewTextItem(CodelistDS.DIFFUSION_VALIDATION_DATE, getConstants().lifeCycleDiffusionValidationDate());
        ViewTextItem internalPublicationUser = new ViewTextItem(CodelistDS.INTERNAL_PUBLICATION_USER, getConstants().lifeCycleInternalPublicationUser());
        ViewTextItem internalPublicationDate = new ViewTextItem(CodelistDS.INTERNAL_PUBLICATION_DATE, getConstants().lifeCycleInternalPublicationDate());
        ViewTextItem externalPublicationUser = new ViewTextItem(CodelistDS.EXTERNAL_PUBLICATION_USER, getConstants().lifeCycleExternalPublicationUser());
        ViewTextItem externalPublicationDate = new ViewTextItem(CodelistDS.EXTERNAL_PUBLICATION_DATE, getConstants().lifeCycleExternalPublicationDate());
        versionResponsibilityEditionForm.setFields(productionValidationUser, productionValidationDate, diffusionValidationUser, diffusionValidationDate, internalPublicationUser,
                internalPublicationDate, externalPublicationUser, externalPublicationDate);

        // Comments
        commentsEditionForm = new GroupDynamicForm(getConstants().nameableArtefactComments());
        MultiLanguageTextAreaItem comments = new MultiLanguageTextAreaItem(CodelistDS.COMMENTS, getConstants().nameableArtefactComments());
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

    public void setEditionMode() {
        mainFormLayout.setEditionMode();
    }

    public void setCodelistViewMode(CodelistMetamacDto codelistDto) {
        // Identifiers
        identifiersForm.setValue(CodelistDS.CODE, codelistDto.getCode());
        identifiersForm.setValue(CodelistDS.URI, codelistDto.getUriProvider());
        identifiersForm.setValue(CodelistDS.URN, codelistDto.getUrn());
        identifiersForm.setValue(CodelistDS.URN_PROVIDER, codelistDto.getUrnProvider());
        identifiersForm.setValue(CodelistDS.VERSION_LOGIC, codelistDto.getVersionLogic());
        identifiersForm.setValue(CodelistDS.NAME, RecordUtils.getInternationalStringRecord(codelistDto.getName()));
        identifiersForm.setValue(CodelistDS.SHORT_NAME, RecordUtils.getInternationalStringRecord(codelistDto.getShortName()));

        // Content descriptors
        contentDescriptorsForm.setValue(CodelistDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(codelistDto.getDescription()));
        contentDescriptorsForm.setValue(CodelistDS.IS_PARTIAL, codelistDto.getIsPartial() != null ? (codelistDto.getIsPartial() ? MetamacWebCommon.getConstants().yes() : MetamacWebCommon
                .getConstants().no()) : StringUtils.EMPTY);
        contentDescriptorsForm.setValue(CodelistDS.IS_EXTERNAL_REFERENCE, codelistDto.getIsExternalReference() != null ? (codelistDto.getIsExternalReference()
                ? MetamacWebCommon.getConstants().yes()
                : MetamacWebCommon.getConstants().no()) : StringUtils.EMPTY);
        contentDescriptorsForm.setValue(CodelistDS.FINAL, codelistDto.getFinalLogic() != null ? (codelistDto.getFinalLogic() ? MetamacWebCommon.getConstants().yes() : MetamacWebCommon.getConstants()
                .no()) : StringUtils.EMPTY);
        contentDescriptorsForm.setValue(CodelistDS.IS_RECOMMENDED, codelistDto.getIsRecommended() != null ? (codelistDto.getIsRecommended() ? MetamacWebCommon.getConstants().yes() : MetamacWebCommon
                .getConstants().no()) : StringUtils.EMPTY);
        contentDescriptorsForm.setValue(CodelistDS.FAMILY_VIEW, codelistDto.getFamily() != null
                ? org.siemac.metamac.srm.web.client.utils.CommonUtils.getRelatedResourceName(codelistDto.getFamily())
                : StringUtils.EMPTY);

        // Production descriptors
        productionDescriptorsForm.setValue(CodelistDS.MAINTAINER, codelistDto.getMaintainer() != null ? codelistDto.getMaintainer().getCode() : StringUtils.EMPTY);
        productionDescriptorsForm.setValue(CodelistDS.PROC_STATUS, org.siemac.metamac.srm.web.client.utils.CommonUtils.getProcStatusName(codelistDto.getLifeCycle().getProcStatus()));

        // Diffusion descriptors
        diffusionDescriptorsForm.setValue(CodelistDS.REPLACED_BY_CODELIST, org.siemac.metamac.srm.web.client.utils.CommonUtils.getRelatedResourceName(codelistDto.getReplacedByCodelist()));
        ((RelatedResourceListItem) diffusionDescriptorsForm.getItem(CodelistDS.REPLACE_TO_CODELISTS)).setRelatedResources(codelistDto.getReplaceToCodelists());
        diffusionDescriptorsForm.setValue(CodelistDS.ACCESS_TYPE, CommonUtils.getAccessTypeName(codelistDto.getAccessType()));
        diffusionDescriptorsForm.setValue(CodelistDS.REPLACED_BY_VERSION, codelistDto.getReplacedByVersion());
        diffusionDescriptorsForm.setValue(CodelistDS.REPLACE_TO_VERSION, codelistDto.getReplaceToVersion());
        diffusionDescriptorsForm.setValue(CodelistDS.VALID_FROM, codelistDto.getValidFrom());
        diffusionDescriptorsForm.setValue(CodelistDS.VALID_TO, codelistDto.getValidTo());
        diffusionDescriptorsForm.setValue(CodelistDS.IS_EXTERNAL_PUBLICATION_FAILED, BooleanUtils.isTrue(codelistDto.getLifeCycle().getIsExternalPublicationFailed()) ? MetamacWebCommon.getConstants()
                .yes() : StringUtils.EMPTY);
        diffusionDescriptorsForm.setValue(CodelistDS.EXTERNAL_PUBLICATION_FAILED_DATE, DateUtils.getFormattedDate(codelistDto.getLifeCycle().getExternalPublicationFailedDate()));

        // Version responsibility
        versionResponsibilityForm.setValue(CodelistDS.PRODUCTION_VALIDATION_USER, codelistDto.getLifeCycle().getProductionValidationUser());
        versionResponsibilityForm.setValue(CodelistDS.PRODUCTION_VALIDATION_DATE, codelistDto.getLifeCycle().getProductionValidationDate());
        versionResponsibilityForm.setValue(CodelistDS.DIFFUSION_VALIDATION_USER, codelistDto.getLifeCycle().getDiffusionValidationUser());
        versionResponsibilityForm.setValue(CodelistDS.DIFFUSION_VALIDATION_DATE, codelistDto.getLifeCycle().getDiffusionValidationDate());
        versionResponsibilityForm.setValue(CodelistDS.INTERNAL_PUBLICATION_USER, codelistDto.getLifeCycle().getInternalPublicationUser());
        versionResponsibilityForm.setValue(CodelistDS.INTERNAL_PUBLICATION_DATE, codelistDto.getLifeCycle().getInternalPublicationDate());
        versionResponsibilityForm.setValue(CodelistDS.EXTERNAL_PUBLICATION_USER, codelistDto.getLifeCycle().getExternalPublicationUser());
        versionResponsibilityForm.setValue(CodelistDS.EXTERNAL_PUBLICATION_DATE, codelistDto.getLifeCycle().getExternalPublicationDate());

        // Comments
        commentsForm.setValue(CodelistDS.COMMENTS, RecordUtils.getInternationalStringRecord(codelistDto.getComment()));

        // Annotations
        annotationsPanel.setAnnotations(codelistDto.getAnnotations());
    }

    public void setCodelistEditionMode(CodelistMetamacDto codelistDto) {
        // Identifiers
        identifiersEditionForm.setValue(CodelistDS.CODE, codelistDto.getCode());
        identifiersEditionForm.setValue(CodelistDS.CODE_VIEW, codelistDto.getCode());
        identifiersEditionForm.setValue(CodelistDS.URI, codelistDto.getUriProvider());
        identifiersEditionForm.setValue(CodelistDS.URN, codelistDto.getUrn());
        identifiersEditionForm.setValue(CodelistDS.URN_PROVIDER, codelistDto.getUrnProvider());
        identifiersEditionForm.setValue(CodelistDS.VERSION_LOGIC, codelistDto.getVersionLogic());
        identifiersEditionForm.setValue(CodelistDS.NAME, RecordUtils.getInternationalStringRecord(codelistDto.getName()));
        identifiersEditionForm.setValue(CodelistDS.SHORT_NAME, RecordUtils.getInternationalStringRecord(codelistDto.getShortName()));
        identifiersEditionForm.markForRedraw();

        // Content descriptors
        contentDescriptorsEditionForm.setValue(CodelistDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(codelistDto.getDescription()));
        contentDescriptorsEditionForm.setValue(CodelistDS.IS_PARTIAL, codelistDto.getIsPartial() != null ? codelistDto.getIsPartial() : false);
        contentDescriptorsEditionForm.setValue(CodelistDS.IS_EXTERNAL_REFERENCE, codelistDto.getIsExternalReference() != null ? (codelistDto.getIsExternalReference() ? MetamacWebCommon.getConstants()
                .yes() : MetamacWebCommon.getConstants().no()) : StringUtils.EMPTY);
        contentDescriptorsEditionForm.setValue(CodelistDS.FINAL, codelistDto.getFinalLogic() != null ? (codelistDto.getFinalLogic() ? MetamacWebCommon.getConstants().yes() : MetamacWebCommon
                .getConstants().no()) : StringUtils.EMPTY);
        contentDescriptorsEditionForm.setValue(CodelistDS.IS_RECOMMENDED, codelistDto.getIsRecommended() != null ? codelistDto.getIsRecommended() : false);
        contentDescriptorsEditionForm.setValue(CodelistDS.FAMILY_VIEW,
                codelistDto.getFamily() != null ? org.siemac.metamac.srm.web.client.utils.CommonUtils.getRelatedResourceName(codelistDto.getFamily()) : StringUtils.EMPTY);
        contentDescriptorsEditionForm.setValue(CodelistDS.FAMILY, codelistDto.getFamily() != null ? codelistDto.getFamily().getUrn() : StringUtils.EMPTY);

        // Production descriptors
        productionDescriptorsEditionForm.setValue(CodelistDS.MAINTAINER, codelistDto.getMaintainer() != null ? codelistDto.getMaintainer().getCode() : StringUtils.EMPTY);
        productionDescriptorsEditionForm.setValue(CodelistDS.PROC_STATUS, org.siemac.metamac.srm.web.client.utils.CommonUtils.getProcStatusName(codelistDto.getLifeCycle().getProcStatus()));
        productionDescriptorsEditionForm.markForRedraw();

        // Diffusion descriptors
        diffusionDescriptorsEditionForm.setValue(CodelistDS.REPLACED_BY_CODELIST, org.siemac.metamac.srm.web.client.utils.CommonUtils.getRelatedResourceName(codelistDto.getReplacedByCodelist()));
        ((RelatedResourceListItem) diffusionDescriptorsEditionForm.getItem(CodelistDS.REPLACE_TO_CODELISTS)).setRelatedResources(codelistDto.getReplaceToCodelists());
        diffusionDescriptorsEditionForm.setValue(CodelistDS.ACCESS_TYPE, codelistDto.getAccessType() != null ? codelistDto.getAccessType().name() : StringUtils.EMPTY);
        diffusionDescriptorsEditionForm.setValue(CodelistDS.REPLACED_BY_VERSION, codelistDto.getReplacedByVersion());
        diffusionDescriptorsEditionForm.setValue(CodelistDS.REPLACE_TO_VERSION, codelistDto.getReplaceToVersion());
        diffusionDescriptorsEditionForm.setValue(CodelistDS.VALID_FROM, DateUtils.getFormattedDate(codelistDto.getValidFrom()));
        diffusionDescriptorsEditionForm.setValue(CodelistDS.VALID_TO, DateUtils.getFormattedDate(codelistDto.getValidTo()));
        diffusionDescriptorsEditionForm.setValue(CodelistDS.IS_EXTERNAL_PUBLICATION_FAILED, BooleanUtils.isTrue(codelistDto.getLifeCycle().getIsExternalPublicationFailed()) ? MetamacWebCommon
                .getConstants().yes() : MetamacWebCommon.getConstants().no());
        diffusionDescriptorsEditionForm.setValue(CodelistDS.EXTERNAL_PUBLICATION_FAILED_DATE, DateUtils.getFormattedDate(codelistDto.getLifeCycle().getExternalPublicationFailedDate()));

        // Version responsibility
        versionResponsibilityEditionForm.setValue(CodelistDS.PRODUCTION_VALIDATION_USER, codelistDto.getLifeCycle().getProductionValidationUser());
        versionResponsibilityEditionForm.setValue(CodelistDS.PRODUCTION_VALIDATION_DATE, codelistDto.getLifeCycle().getProductionValidationDate());
        versionResponsibilityEditionForm.setValue(CodelistDS.DIFFUSION_VALIDATION_USER, codelistDto.getLifeCycle().getDiffusionValidationUser());
        versionResponsibilityEditionForm.setValue(CodelistDS.DIFFUSION_VALIDATION_DATE, codelistDto.getLifeCycle().getDiffusionValidationDate());
        versionResponsibilityEditionForm.setValue(CodelistDS.INTERNAL_PUBLICATION_USER, codelistDto.getLifeCycle().getInternalPublicationUser());
        versionResponsibilityEditionForm.setValue(CodelistDS.INTERNAL_PUBLICATION_DATE, codelistDto.getLifeCycle().getInternalPublicationDate());
        versionResponsibilityEditionForm.setValue(CodelistDS.EXTERNAL_PUBLICATION_USER, codelistDto.getLifeCycle().getExternalPublicationUser());
        versionResponsibilityEditionForm.setValue(CodelistDS.EXTERNAL_PUBLICATION_DATE, codelistDto.getLifeCycle().getExternalPublicationDate());

        // Comments
        commentsEditionForm.setValue(CodelistDS.COMMENTS, RecordUtils.getInternationalStringRecord(codelistDto.getComment()));

        // Annotations
        annotationsEditionPanel.setAnnotations(codelistDto.getAnnotations());
    }

    public CodelistMetamacDto getCodelistDto() {
        // Identifiers
        codelistDto.setCode(identifiersEditionForm.getValueAsString(CodelistDS.CODE));
        codelistDto.setName((InternationalStringDto) identifiersEditionForm.getValue(CodelistDS.NAME));
        codelistDto.setShortName((InternationalStringDto) identifiersEditionForm.getValue(CodelistDS.SHORT_NAME));

        // Content descriptors
        codelistDto.setDescription((InternationalStringDto) contentDescriptorsEditionForm.getValue(CodelistDS.DESCRIPTION));
        codelistDto.setIsPartial((contentDescriptorsEditionForm.getValue(CodelistDS.IS_PARTIAL) != null && !StringUtils.isEmpty(contentDescriptorsEditionForm.getValueAsString(CodelistDS.IS_PARTIAL)))
                ? Boolean.valueOf(contentDescriptorsEditionForm.getValueAsString(CodelistDS.IS_PARTIAL))
                : false);
        codelistDto.setIsRecommended((contentDescriptorsEditionForm.getValue(CodelistDS.IS_RECOMMENDED) != null && !StringUtils.isEmpty(contentDescriptorsEditionForm
                .getValueAsString(CodelistDS.IS_RECOMMENDED))) ? Boolean.valueOf(contentDescriptorsEditionForm.getValueAsString(CodelistDS.IS_RECOMMENDED)) : false);
        codelistDto.setFamily(!StringUtils.isBlank(contentDescriptorsEditionForm.getValueAsString(CodelistDS.FAMILY)) ? RelatedResourceUtils.createRelatedResourceDto(contentDescriptorsEditionForm
                .getValueAsString(CodelistDS.FAMILY)) : null);

        // Diffusion descriptors
        codelistDto.setReplaceToCodelists(((RelatedResourceListItem) diffusionDescriptorsEditionForm.getItem(CodelistDS.REPLACE_TO_CODELISTS)).getSelectedRelatedResources());
        codelistDto.setAccessType(!StringUtils.isBlank(diffusionDescriptorsEditionForm.getValueAsString(CodelistDS.ACCESS_TYPE)) ? AccessTypeEnum.valueOf(diffusionDescriptorsEditionForm
                .getValueAsString(CodelistDS.ACCESS_TYPE)) : null);

        // Comments
        codelistDto.setComment((InternationalStringDto) commentsEditionForm.getValue(CodelistDS.COMMENTS));

        // Annotations
        codelistDto.getAnnotations().clear();
        codelistDto.getAnnotations().addAll(annotationsEditionPanel.getAnnotations());

        return codelistDto;
    }

    private SearchViewTextItem createFamilyItem(String name, String title) {
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;
        SearchViewTextItem familyItem = new SearchViewTextItem(name, title);
        familyItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {

                searchFamilyWindow = new SearchRelatedResourceWindow(getConstants().codelistFamilySelection(), MAX_RESULTS, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        getUiHandlers().retrieveFamilies(firstResult, maxResults, searchFamilyWindow.getRelatedResourceCriteria());
                    }
                });

                // Load families (to populate the selection window)
                getUiHandlers().retrieveFamilies(FIRST_RESULST, MAX_RESULTS, null);

                searchFamilyWindow.getListGridItem().getListGrid().setSelectionType(SelectionStyle.SINGLE);
                searchFamilyWindow.getListGridItem().setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                        getUiHandlers().retrieveFamilies(firstResult, maxResults, criteria);
                    }
                });
                searchFamilyWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent arg0) {
                        RelatedResourceDto selectedFamily = searchFamilyWindow.getSelectedRelatedResource();
                        searchFamilyWindow.markForDestroy();
                        // Set selected family in form
                        contentDescriptorsEditionForm.setValue(CodelistDS.FAMILY, selectedFamily != null ? selectedFamily.getUrn() : null);
                        contentDescriptorsEditionForm.setValue(CodelistDS.FAMILY_VIEW,
                                selectedFamily != null ? org.siemac.metamac.srm.web.client.utils.CommonUtils.getRelatedResourceName(selectedFamily) : null);
                    }
                });
            }
        });
        return familyItem;
    }

    private RelatedResourceListItem createReplaceToCodelistsItem(String name, String title) {
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;

        RelatedResourceListItem replaceToItem = new RelatedResourceListItem(name, title, true);
        replaceToItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent arg0) {
                searchReplaceToCodelistsWindow = new SearchMultipleRelatedResourceWindow(getConstants().codelistsSelection(), MAX_RESULTS, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        getUiHandlers().retrieveCodelistsThatCanBeReplaced(firstResult, maxResults, searchReplaceToCodelistsWindow.getRelatedResourceCriteria());
                    }
                });

                // Load the list codelists that can be replaced
                getUiHandlers().retrieveCodelistsThatCanBeReplaced(FIRST_RESULST, MAX_RESULTS, null);

                // Set the selected codelists
                List<RelatedResourceDto> selectedCodelists = ((RelatedResourceListItem) diffusionDescriptorsEditionForm.getItem(CodelistDS.REPLACE_TO_CODELISTS)).getRelatedResourceDtos();
                searchReplaceToCodelistsWindow.setTargetRelatedResources(selectedCodelists);

                searchReplaceToCodelistsWindow.setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                        getUiHandlers().retrieveCodelistsThatCanBeReplaced(firstResult, maxResults, criteria);
                    }
                });
                searchReplaceToCodelistsWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent arg0) {
                        List<RelatedResourceDto> selectedCodelists = searchReplaceToCodelistsWindow.getSelectedRelatedResources();
                        searchReplaceToCodelistsWindow.markForDestroy();
                        // Set selected codelists in form
                        ((RelatedResourceListItem) diffusionDescriptorsEditionForm.getItem(CodelistDS.REPLACE_TO_CODELISTS)).setRelatedResources(selectedCodelists);
                    }
                });
            }
        });
        return replaceToItem;
    }
}
