package org.siemac.metamac.srm.web.organisation.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.BooleanUtils;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.client.widgets.AnnotationsPanel;
import org.siemac.metamac.srm.web.client.widgets.VersionWindow;
import org.siemac.metamac.srm.web.organisation.model.ds.OrganisationDS;
import org.siemac.metamac.srm.web.organisation.model.ds.OrganisationSchemeDS;
import org.siemac.metamac.srm.web.organisation.model.record.OrganisationRecord;
import org.siemac.metamac.srm.web.organisation.model.record.OrganisationSchemeRecord;
import org.siemac.metamac.srm.web.organisation.presenter.OrganisationSchemePresenter;
import org.siemac.metamac.srm.web.organisation.utils.CommonUtils;
import org.siemac.metamac.srm.web.organisation.utils.OrganisationsClientSecurityUtils;
import org.siemac.metamac.srm.web.organisation.view.handlers.OrganisationSchemeUiHandlers;
import org.siemac.metamac.srm.web.organisation.widgets.NewOrganisationWindow;
import org.siemac.metamac.srm.web.organisation.widgets.OrganisationSchemeCategorisationsPanel;
import org.siemac.metamac.srm.web.organisation.widgets.OrganisationSchemeMainFormLayout;
import org.siemac.metamac.srm.web.organisation.widgets.OrganisationSchemeVersionsSectionStack;
import org.siemac.metamac.srm.web.organisation.widgets.OrganisationsTreeGrid;
import org.siemac.metamac.srm.web.shared.category.GetCategoriesResult;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemesResult;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.utils.DateUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.CustomListGrid;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.InformationWindow;
import org.siemac.metamac.web.common.client.widgets.TitleLabel;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextAreaItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class OrganisationSchemeViewImpl extends ViewWithUiHandlers<OrganisationSchemeUiHandlers> implements OrganisationSchemePresenter.OrganisationSchemeView {

    private VLayout                                panel;
    private OrganisationSchemeMainFormLayout       mainFormLayout;

    // View forms
    private GroupDynamicForm                       identifiersForm;
    private GroupDynamicForm                       contentDescriptorsForm;
    private GroupDynamicForm                       productionDescriptorsForm;
    private GroupDynamicForm                       diffusionDescriptorsForm;
    private GroupDynamicForm                       versionResponsibilityForm;
    private GroupDynamicForm                       commentsForm;
    private AnnotationsPanel                       annotationsPanel;

    // Edition forms
    private GroupDynamicForm                       identifiersEditionForm;
    private GroupDynamicForm                       contentDescriptorsEditionForm;
    private GroupDynamicForm                       productionDescriptorsEditionForm;
    private GroupDynamicForm                       diffusionDescriptorsEditionForm;
    private GroupDynamicForm                       versionResponsibilityEditionForm;
    private GroupDynamicForm                       commentsEditionForm;
    private AnnotationsPanel                       annotationsEditionPanel;

    // Versions
    private OrganisationSchemeVersionsSectionStack versionsSectionStack;

    // Organisation list
    private ToolStrip                              toolStrip;
    private ToolStripButton                        newButton;
    private ToolStripButton                        deleteButton;
    private NewOrganisationWindow                  newOrganisationWindow;
    private DeleteConfirmationWindow               deleteConfirmationWindow;
    private CustomListGrid                         organisationListGrid;
    private OrganisationSchemeMetamacDto           organisationSchemeDto;

    // OrganisationTree
    private OrganisationsTreeGrid                  organisationsTreeGrid;

    // Categorisations
    private OrganisationSchemeCategorisationsPanel categorisationsPanel;

    @Inject
    public OrganisationSchemeViewImpl() {
        super();
        panel = new VLayout();
        panel.setHeight100();
        panel.setOverflow(Overflow.SCROLL);

        //
        // ORGANISATION SCHEME VERSIONS
        //

        versionsSectionStack = new OrganisationSchemeVersionsSectionStack(getConstants().organisationSchemeVersions());
        versionsSectionStack.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                String urn = ((OrganisationSchemeRecord) event.getRecord()).getUrn();
                OrganisationSchemeTypeEnum type = ((OrganisationSchemeRecord) event.getRecord()).getOrganisationSchemeDto().getType();
                getUiHandlers().goToOrganisationScheme(urn, type);
            }
        });

        //
        // ORGANISATION SCHEME
        //

        mainFormLayout = new OrganisationSchemeMainFormLayout();
        bindMainFormLayoutEvents();
        createViewForm();
        createEditionForm();

        //
        // ORGANISATIONS
        //

        // ToolStrip

        toolStrip = new ToolStrip();
        toolStrip.setWidth100();

        newButton = new ToolStripButton(getConstants().actionNew(), RESOURCE.newListGrid().getURL());
        newButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                newOrganisationWindow = new NewOrganisationWindow(getConstants().organisationCreate());
                newOrganisationWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (newOrganisationWindow.validateForm()) {
                            OrganisationMetamacDto organisationToCreate = newOrganisationWindow.getNewOrganisationDto(CommonUtils.getOrganisationTypeEnum(organisationSchemeDto.getType()));
                            organisationToCreate.setItemSchemeVersionUrn(organisationSchemeDto.getUrn());
                            getUiHandlers().createOrganisation(organisationToCreate);
                            newOrganisationWindow.destroy();
                        }
                    }
                });
            }
        });

        deleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().organisationDeleteConfirmationTitle(), getConstants().organisationDeleteConfirmation());
        deleteConfirmationWindow.setVisibility(Visibility.HIDDEN);
        deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deleteOrganisations(getSelectedOrganisationUrns());
                deleteConfirmationWindow.hide();
            }
        });

        deleteButton = new ToolStripButton(getConstants().actionDelete(), RESOURCE.deleteListGrid().getURL());
        deleteButton.setVisibility(Visibility.HIDDEN);
        deleteButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });

        toolStrip.addButton(newButton);
        toolStrip.addButton(deleteButton);

        // ListGrid

        organisationListGrid = new CustomListGrid();
        organisationListGrid.setAutoFitMaxRecords(20);
        organisationListGrid.setAutoFitData(Autofit.VERTICAL);
        ListGridField codeField = new ListGridField(OrganisationDS.CODE, getConstants().identifiableArtefactCode());
        ListGridField nameField = new ListGridField(OrganisationDS.NAME, getConstants().nameableArtefactName());
        organisationListGrid.setFields(codeField, nameField);
        organisationListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (organisationListGrid.getSelectedRecords().length > 0) {
                    // Show delete button
                    showListGridDeleteButton();
                } else {
                    deleteButton.hide();
                }
            }
        });

        organisationListGrid.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // Clicking checkBox will be ignored
                    String urn = ((OrganisationRecord) event.getRecord()).getAttribute(OrganisationDS.URN);
                    getUiHandlers().goToOrganisation(urn);
                }
            }
        });

        // TreeGrid

        organisationsTreeGrid = new OrganisationsTreeGrid();

        VLayout organisationsLayout = new VLayout();
        organisationsLayout.setMargin(15);
        organisationsLayout.addMember(new TitleLabel(getConstants().organisations()));
        organisationsLayout.addMember(toolStrip);
        organisationsLayout.addMember(organisationListGrid);
        organisationsLayout.addMember(organisationsTreeGrid);

        categorisationsPanel = new OrganisationSchemeCategorisationsPanel();

        panel.addMember(versionsSectionStack);
        panel.addMember(mainFormLayout);
        panel.addMember(organisationsLayout);
        panel.addMember(categorisationsPanel);
    }

    @Override
    public void setUiHandlers(OrganisationSchemeUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        organisationsTreeGrid.setUiHandlers(getUiHandlers());
        categorisationsPanel.setUiHandlers(uiHandlers);
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
                OrganisationSchemeTypeEnum type = organisationSchemeDto.getType();
                ProcStatusEnum status = organisationSchemeDto.getLifeCycle().getProcStatus();
                // If the organisation selected is and OrganisationUnit (and its status is INTERNALLY_PUBLISHED or EXTERNALLY_PUBLISHED)
                if ((!CommonUtils.isAgencyScheme(type) && !CommonUtils.isDataConsumerScheme(type) && !CommonUtils.isDataProviderScheme(type))
                        && ((ProcStatusEnum.INTERNALLY_PUBLISHED.equals(status) || ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(status)))) {
                    // Create a new version
                    final InformationWindow window = new InformationWindow(getMessages().organisationSchemeEditionInfo(), getMessages().organisationSchemeEditionInfoDetailedMessage());
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
                    getUiHandlers().saveOrganisationScheme(getOrganisationSchemeDto());
                }
            }
        });

        // Life cycle
        mainFormLayout.getSendToProductionValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().sendToProductionValidation(organisationSchemeDto.getUrn(), organisationSchemeDto.getLifeCycle().getProcStatus());
            }
        });
        mainFormLayout.getSendToDiffusionValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().sendToDiffusionValidation(organisationSchemeDto.getUrn(), organisationSchemeDto.getLifeCycle().getProcStatus());
            }
        });
        mainFormLayout.getRejectValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().rejectValidation(organisationSchemeDto.getUrn(), organisationSchemeDto.getLifeCycle().getProcStatus());
            }
        });
        mainFormLayout.getPublishInternally().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().publishInternally(organisationSchemeDto.getUrn(), organisationSchemeDto.getLifeCycle().getProcStatus());
            }
        });
        mainFormLayout.getPublishExternally().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().publishExternally(organisationSchemeDto.getUrn(), organisationSchemeDto.getLifeCycle().getProcStatus());
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
                            getUiHandlers().versioning(organisationSchemeDto.getUrn(), versionWindow.getSelectedVersion());
                            versionWindow.destroy();
                        }
                    }
                });
            }
        });
        mainFormLayout.getCancelValidity().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().cancelValidity(organisationSchemeDto.getUrn());
            }
        });
    }

    private void createViewForm() {
        // Identifiers Form
        identifiersForm = new GroupDynamicForm(getConstants().formIdentifiers());
        ViewTextItem code = new ViewTextItem(OrganisationSchemeDS.CODE, getConstants().identifiableArtefactCode());
        ViewMultiLanguageTextItem name = new ViewMultiLanguageTextItem(OrganisationSchemeDS.NAME, getConstants().nameableArtefactName());
        ViewTextItem uri = new ViewTextItem(OrganisationSchemeDS.URI, getConstants().identifiableArtefactUri());
        ViewTextItem urn = new ViewTextItem(OrganisationSchemeDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(OrganisationSchemeDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        ViewTextItem version = new ViewTextItem(OrganisationSchemeDS.VERSION_LOGIC, getConstants().maintainableArtefactVersionLogic());
        identifiersForm.setFields(code, name, uri, urn, urnProvider, version);

        // Content descriptors
        contentDescriptorsForm = new GroupDynamicForm(getConstants().formContentDescriptors());
        ViewTextItem type = new ViewTextItem(OrganisationSchemeDS.TYPE, getConstants().organisationSchemeType());
        ViewMultiLanguageTextItem description = new ViewMultiLanguageTextItem(OrganisationSchemeDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        ViewTextItem partial = new ViewTextItem(OrganisationSchemeDS.IS_PARTIAL, getConstants().itemSchemeIsPartial());
        ViewTextItem isExternalReference = new ViewTextItem(OrganisationSchemeDS.IS_EXTERNAL_REFERENCE, getConstants().maintainableArtefactIsExternalReference());
        ViewTextItem isFinal = new ViewTextItem(OrganisationSchemeDS.FINAL, getConstants().maintainableArtefactFinalLogic());
        contentDescriptorsForm.setFields(type, description, partial, isExternalReference, isFinal);

        // Production descriptors
        productionDescriptorsForm = new GroupDynamicForm(getConstants().formProductionDescriptors());
        ViewTextItem agency = new ViewTextItem(OrganisationSchemeDS.MAINTAINER, getConstants().maintainableArtefactMaintainer());
        ViewTextItem procStatus = new ViewTextItem(OrganisationSchemeDS.PROC_STATUS, getConstants().lifeCycleProcStatus());
        productionDescriptorsForm.setFields(agency, procStatus);

        // Diffusion descriptors
        diffusionDescriptorsForm = new GroupDynamicForm(getConstants().formDiffusionDescriptors());
        ViewTextItem replacedBy = new ViewTextItem(OrganisationSchemeDS.REPLACED_BY_VERSION, getConstants().maintainableArtefactReplacedByVersion());
        ViewTextItem replaceTo = new ViewTextItem(OrganisationSchemeDS.REPLACE_TO_VERSION, getConstants().maintainableArtefactReplaceToVersion());
        ViewTextItem validFrom = new ViewTextItem(OrganisationSchemeDS.VALID_FROM, getConstants().maintainableArtefactValidFrom());
        ViewTextItem validTo = new ViewTextItem(OrganisationSchemeDS.VALID_TO, getConstants().maintainableArtefactValidTo());
        ViewTextItem externalPublicationFailed = new ViewTextItem(OrganisationSchemeDS.IS_EXTERNAL_PUBLICATION_FAILED, getConstants().lifeCycleExternalPublicationFailed());
        ViewTextItem externalPublicationFailedDate = new ViewTextItem(OrganisationSchemeDS.EXTERNAL_PUBLICATION_FAILED_DATE, getConstants().lifeCycleExternalPublicationFailedDate());
        diffusionDescriptorsForm.setFields(replacedBy, replaceTo, validFrom, validTo, externalPublicationFailed, externalPublicationFailedDate);

        // Version responsibility
        versionResponsibilityForm = new GroupDynamicForm(getConstants().lifeCycleVersionResponsibility());
        ViewTextItem productionValidationUser = new ViewTextItem(OrganisationSchemeDS.PRODUCTION_VALIDATION_USER, getConstants().lifeCycleProductionValidationUser());
        ViewTextItem productionValidationDate = new ViewTextItem(OrganisationSchemeDS.PRODUCTION_VALIDATION_DATE, getConstants().lifeCycleProductionValidationDate());
        ViewTextItem diffusionValidationUser = new ViewTextItem(OrganisationSchemeDS.DIFFUSION_VALIDATION_USER, getConstants().lifeCycleDiffusionValidationUser());
        ViewTextItem diffusionValidationDate = new ViewTextItem(OrganisationSchemeDS.DIFFUSION_VALIDATION_DATE, getConstants().lifeCycleDiffusionValidationDate());
        ViewTextItem internalPublicationUser = new ViewTextItem(OrganisationSchemeDS.INTERNAL_PUBLICATION_USER, getConstants().lifeCycleInternalPublicationUser());
        ViewTextItem internalPublicationDate = new ViewTextItem(OrganisationSchemeDS.INTERNAL_PUBLICATION_DATE, getConstants().lifeCycleInternalPublicationDate());
        ViewTextItem externalPublicationUser = new ViewTextItem(OrganisationSchemeDS.EXTERNAL_PUBLICATION_USER, getConstants().lifeCycleExternalPublicationUser());
        ViewTextItem externalPublicationDate = new ViewTextItem(OrganisationSchemeDS.EXTERNAL_PUBLICATION_DATE, getConstants().lifeCycleExternalPublicationDate());
        versionResponsibilityForm.setFields(productionValidationUser, productionValidationDate, diffusionValidationUser, diffusionValidationDate, internalPublicationUser, internalPublicationDate,
                externalPublicationUser, externalPublicationDate);

        // Comments
        commentsForm = new GroupDynamicForm(getConstants().nameableArtefactComments());
        ViewMultiLanguageTextItem comments = new ViewMultiLanguageTextItem(OrganisationSchemeDS.COMMENTS, getConstants().nameableArtefactComments());
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
        RequiredTextItem code = new RequiredTextItem(OrganisationSchemeDS.CODE, getConstants().identifiableArtefactCode());
        code.setValidators(SemanticIdentifiersUtils.getOrganisationSchemeIdentifierCustomValidator());
        code.setShowIfCondition(getCodeFormItemIfFunction());
        ViewTextItem staticCode = new ViewTextItem(OrganisationSchemeDS.CODE_VIEW, getConstants().identifiableArtefactCode());
        staticCode.setShowIfCondition(getStaticCodeFormItemIfFunction());
        MultiLanguageTextItem name = new MultiLanguageTextItem(OrganisationSchemeDS.NAME, getConstants().nameableArtefactName());
        name.setRequired(true);
        ViewTextItem uri = new ViewTextItem(OrganisationSchemeDS.URI, getConstants().identifiableArtefactUri());
        ViewTextItem urn = new ViewTextItem(OrganisationSchemeDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(OrganisationSchemeDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        ViewTextItem version = new ViewTextItem(OrganisationSchemeDS.VERSION_LOGIC, getConstants().maintainableArtefactVersionLogic());
        identifiersEditionForm.setFields(code, staticCode, name, uri, urn, urnProvider, version);

        // Content descriptors
        contentDescriptorsEditionForm = new GroupDynamicForm(getConstants().formContentDescriptors());

        RequiredSelectItem type = new RequiredSelectItem(OrganisationSchemeDS.TYPE, getConstants().organisationSchemeType());
        type.setValueMap(CommonUtils.getOrganisationSchemeTypeHashMap());
        type.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                // TYPE cannot be modified if:
                // · status is INTERNALLY_PUBLISHED or EXTERNALLY_PUBLISHED
                // · version is greater than SdmxVersionUtil.PATTERN_XX_YYY_INITIAL_VERSION (01.000)
                // · scheme has children (not validated here, it is validated in core service)
                return !org.siemac.metamac.srm.web.client.utils.CommonUtils.isItemSchemePublished(organisationSchemeDto.getLifeCycle().getProcStatus())
                        && org.siemac.metamac.srm.web.client.utils.CommonUtils.isInitialVersion(organisationSchemeDto.getVersionLogic());
            }
        });
        ViewTextItem staticType = new ViewTextItem(OrganisationSchemeDS.TYPE_VIEW, getConstants().organisationSchemeType());
        staticType.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !form.getItem(OrganisationSchemeDS.TYPE).isVisible();
            }
        });

        MultiLanguageTextAreaItem description = new MultiLanguageTextAreaItem(OrganisationSchemeDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        ViewTextItem partial = new ViewTextItem(OrganisationSchemeDS.IS_PARTIAL, getConstants().itemSchemeIsPartial());
        ViewTextItem isExternalReference = new ViewTextItem(OrganisationSchemeDS.IS_EXTERNAL_REFERENCE, getConstants().maintainableArtefactIsExternalReference());
        ViewTextItem isFinal = new ViewTextItem(OrganisationSchemeDS.FINAL, getConstants().maintainableArtefactFinalLogic());
        contentDescriptorsEditionForm.setFields(type, staticType, description, partial, isExternalReference, isFinal);

        // Production descriptors
        productionDescriptorsEditionForm = new GroupDynamicForm(getConstants().formProductionDescriptors());
        ViewTextItem agency = new ViewTextItem(OrganisationSchemeDS.MAINTAINER, getConstants().maintainableArtefactMaintainer());
        ViewTextItem procStatus = new ViewTextItem(OrganisationSchemeDS.PROC_STATUS, getConstants().lifeCycleProcStatus());
        productionDescriptorsEditionForm.setFields(agency, procStatus);

        // Diffusion descriptors
        diffusionDescriptorsEditionForm = new GroupDynamicForm(getConstants().formDiffusionDescriptors());
        ViewTextItem replacedBy = new ViewTextItem(OrganisationSchemeDS.REPLACED_BY_VERSION, getConstants().maintainableArtefactReplacedByVersion());
        ViewTextItem replaceTo = new ViewTextItem(OrganisationSchemeDS.REPLACE_TO_VERSION, getConstants().maintainableArtefactReplaceToVersion());
        ViewTextItem validFrom = new ViewTextItem(OrganisationSchemeDS.VALID_FROM, getConstants().maintainableArtefactValidFrom());
        ViewTextItem validTo = new ViewTextItem(OrganisationSchemeDS.VALID_TO, getConstants().maintainableArtefactValidTo());
        ViewTextItem externalPublicationFailed = new ViewTextItem(OrganisationSchemeDS.IS_EXTERNAL_PUBLICATION_FAILED, getConstants().lifeCycleExternalPublicationFailed());
        ViewTextItem externalPublicationFailedDate = new ViewTextItem(OrganisationSchemeDS.EXTERNAL_PUBLICATION_FAILED_DATE, getConstants().lifeCycleExternalPublicationFailedDate());
        diffusionDescriptorsEditionForm.setFields(replacedBy, replaceTo, validFrom, validTo, externalPublicationFailed, externalPublicationFailedDate);

        // Version responsibility
        versionResponsibilityEditionForm = new GroupDynamicForm(getConstants().lifeCycleVersionResponsibility());
        ViewTextItem productionValidationUser = new ViewTextItem(OrganisationSchemeDS.PRODUCTION_VALIDATION_USER, getConstants().lifeCycleProductionValidationUser());
        ViewTextItem productionValidationDate = new ViewTextItem(OrganisationSchemeDS.PRODUCTION_VALIDATION_DATE, getConstants().lifeCycleProductionValidationDate());
        ViewTextItem diffusionValidationUser = new ViewTextItem(OrganisationSchemeDS.DIFFUSION_VALIDATION_USER, getConstants().lifeCycleDiffusionValidationUser());
        ViewTextItem diffusionValidationDate = new ViewTextItem(OrganisationSchemeDS.DIFFUSION_VALIDATION_DATE, getConstants().lifeCycleDiffusionValidationDate());
        ViewTextItem internalPublicationUser = new ViewTextItem(OrganisationSchemeDS.INTERNAL_PUBLICATION_USER, getConstants().lifeCycleInternalPublicationUser());
        ViewTextItem internalPublicationDate = new ViewTextItem(OrganisationSchemeDS.INTERNAL_PUBLICATION_DATE, getConstants().lifeCycleInternalPublicationDate());
        ViewTextItem externalPublicationUser = new ViewTextItem(OrganisationSchemeDS.EXTERNAL_PUBLICATION_USER, getConstants().lifeCycleExternalPublicationUser());
        ViewTextItem externalPublicationDate = new ViewTextItem(OrganisationSchemeDS.EXTERNAL_PUBLICATION_DATE, getConstants().lifeCycleExternalPublicationDate());
        versionResponsibilityEditionForm.setFields(productionValidationUser, productionValidationDate, diffusionValidationUser, diffusionValidationDate, internalPublicationUser,
                internalPublicationDate, externalPublicationUser, externalPublicationDate);

        // Comments
        commentsEditionForm = new GroupDynamicForm(getConstants().nameableArtefactComments());
        MultiLanguageTextAreaItem comments = new MultiLanguageTextAreaItem(OrganisationSchemeDS.COMMENTS, getConstants().nameableArtefactComments());
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
        if (slot == OrganisationSchemePresenter.TYPE_SetContextAreaContentOrganisationsToolBar) {
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
    public void setOrganisationScheme(OrganisationSchemeMetamacDto organisationSchemeMetamacDto) {
        this.organisationSchemeDto = organisationSchemeMetamacDto;

        String defaultLocalisedName = InternationalStringUtils.getLocalisedString(organisationSchemeMetamacDto.getName());
        String title = defaultLocalisedName != null ? defaultLocalisedName : StringUtils.EMPTY;
        mainFormLayout.setTitleLabelContents(title);

        // Security
        ProcStatusEnum procStatus = organisationSchemeMetamacDto.getLifeCycle().getProcStatus();
        mainFormLayout.setCanEdit(OrganisationsClientSecurityUtils.canUpdateOrganisationScheme(procStatus, organisationSchemeMetamacDto.getType()));
        mainFormLayout.updatePublishSection(procStatus, organisationSchemeMetamacDto.getValidTo(), organisationSchemeMetamacDto.getType());
        mainFormLayout.setViewMode();
        categorisationsPanel.updateVisibility(organisationSchemeMetamacDto);

        setOrganisationSchemeViewMode(organisationSchemeMetamacDto);
        setOrganisationSchemeEditionMode(organisationSchemeMetamacDto);

        // Show/hide organisation list and tree, depending on the organisation scheme type
        if (OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME.equals(organisationSchemeDto.getType())) {
            showOrganisationTree();
        } else {
            showOrganisationList();
            // Security to create organisations
            newButton.setVisibility(OrganisationsClientSecurityUtils.canCreateOrganisation(organisationSchemeMetamacDto.getLifeCycle().getProcStatus(), organisationSchemeMetamacDto.getType())
                    ? Visibility.VISIBLE
                    : Visibility.HIDDEN);
            toolStrip.markForRedraw();
        }

        // Update organisation scheme in tree grid
        organisationsTreeGrid.updateItemScheme(organisationSchemeMetamacDto);
    }

    @Override
    public void setOrganisationSchemeVersions(List<OrganisationSchemeMetamacDto> organisationSchemeMetamacDtos) {
        versionsSectionStack.setOrganisationSchemes(organisationSchemeMetamacDtos);
        versionsSectionStack.selectOrganisationScheme(organisationSchemeDto);
    }

    @Override
    public void setOrganisationList(List<ItemHierarchyDto> organisations) {
        if (OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME.equals(organisationSchemeDto.getType())) {
            // Organisation hierarchy
            organisationsTreeGrid.setItems(organisationSchemeDto, organisations);
        } else {
            // Organisation list
            OrganisationTypeEnum organisationTypeEnum = CommonUtils.getOrganisationTypeEnum(organisationSchemeDto.getType());
            OrganisationRecord[] records = new OrganisationRecord[organisations.size()];
            for (int i = 0; i < organisations.size(); i++) {
                records[i] = org.siemac.metamac.srm.web.organisation.utils.RecordUtils.getOrganisationRecord(organisations.get(i), organisationTypeEnum);
            }
            organisationListGrid.setData(records);
        }
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

    public void setOrganisationSchemeViewMode(OrganisationSchemeMetamacDto organisationSchemeDto) {
        // Identifiers
        identifiersForm.setValue(OrganisationSchemeDS.CODE, organisationSchemeDto.getCode());
        identifiersForm.setValue(OrganisationSchemeDS.URI, organisationSchemeDto.getUriProvider());
        identifiersForm.setValue(OrganisationSchemeDS.URN, organisationSchemeDto.getUrn());
        identifiersForm.setValue(OrganisationSchemeDS.URN_PROVIDER, organisationSchemeDto.getUrnProvider());
        identifiersForm.setValue(OrganisationSchemeDS.VERSION_LOGIC, organisationSchemeDto.getVersionLogic());
        identifiersForm.setValue(OrganisationSchemeDS.NAME, RecordUtils.getInternationalStringRecord(organisationSchemeDto.getName()));

        // Content descriptors
        contentDescriptorsForm.setValue(OrganisationSchemeDS.TYPE, CommonUtils.getOrganisationSchemeTypeName(organisationSchemeDto.getType()));
        contentDescriptorsForm.setValue(OrganisationSchemeDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(organisationSchemeDto.getDescription()));
        contentDescriptorsForm.setValue(OrganisationSchemeDS.IS_PARTIAL, org.siemac.metamac.srm.web.client.utils.CommonUtils.getBooleanName(organisationSchemeDto.getIsPartial()));
        contentDescriptorsForm.setValue(OrganisationSchemeDS.IS_EXTERNAL_REFERENCE, organisationSchemeDto.getIsExternalReference() != null ? (organisationSchemeDto.getIsExternalReference()
                ? MetamacWebCommon.getConstants().yes()
                : MetamacWebCommon.getConstants().no()) : StringUtils.EMPTY);
        contentDescriptorsForm.setValue(OrganisationSchemeDS.FINAL, organisationSchemeDto.getFinalLogic() != null ? (organisationSchemeDto.getFinalLogic()
                ? MetamacWebCommon.getConstants().yes()
                : MetamacWebCommon.getConstants().no()) : StringUtils.EMPTY);

        // Production descriptors
        productionDescriptorsForm.setValue(OrganisationSchemeDS.MAINTAINER, org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourceName(organisationSchemeDto.getMaintainer()));
        productionDescriptorsForm.setValue(OrganisationSchemeDS.PROC_STATUS,
                org.siemac.metamac.srm.web.client.utils.CommonUtils.getProcStatusName(organisationSchemeDto.getLifeCycle().getProcStatus()));

        // Diffusion descriptors
        diffusionDescriptorsForm.setValue(OrganisationSchemeDS.REPLACED_BY_VERSION, organisationSchemeDto.getReplacedByVersion());
        diffusionDescriptorsForm.setValue(OrganisationSchemeDS.REPLACE_TO_VERSION, organisationSchemeDto.getReplaceToVersion());
        diffusionDescriptorsForm.setValue(OrganisationSchemeDS.VALID_FROM, organisationSchemeDto.getValidFrom());
        diffusionDescriptorsForm.setValue(OrganisationSchemeDS.VALID_TO, organisationSchemeDto.getValidTo());
        diffusionDescriptorsForm.setValue(OrganisationSchemeDS.IS_EXTERNAL_PUBLICATION_FAILED, BooleanUtils.isTrue(organisationSchemeDto.getLifeCycle().getIsExternalPublicationFailed())
                ? MetamacWebCommon.getConstants().yes()
                : StringUtils.EMPTY);
        diffusionDescriptorsForm.setValue(OrganisationSchemeDS.EXTERNAL_PUBLICATION_FAILED_DATE, DateUtils.getFormattedDate(organisationSchemeDto.getLifeCycle().getExternalPublicationFailedDate()));

        // Version responsibility
        versionResponsibilityForm.setValue(OrganisationSchemeDS.PRODUCTION_VALIDATION_USER, organisationSchemeDto.getLifeCycle().getProductionValidationUser());
        versionResponsibilityForm.setValue(OrganisationSchemeDS.PRODUCTION_VALIDATION_DATE, organisationSchemeDto.getLifeCycle().getProductionValidationDate());
        versionResponsibilityForm.setValue(OrganisationSchemeDS.DIFFUSION_VALIDATION_USER, organisationSchemeDto.getLifeCycle().getDiffusionValidationUser());
        versionResponsibilityForm.setValue(OrganisationSchemeDS.DIFFUSION_VALIDATION_DATE, organisationSchemeDto.getLifeCycle().getDiffusionValidationDate());
        versionResponsibilityForm.setValue(OrganisationSchemeDS.INTERNAL_PUBLICATION_USER, organisationSchemeDto.getLifeCycle().getInternalPublicationUser());
        versionResponsibilityForm.setValue(OrganisationSchemeDS.INTERNAL_PUBLICATION_DATE, organisationSchemeDto.getLifeCycle().getInternalPublicationDate());
        versionResponsibilityForm.setValue(OrganisationSchemeDS.EXTERNAL_PUBLICATION_USER, organisationSchemeDto.getLifeCycle().getExternalPublicationUser());
        versionResponsibilityForm.setValue(OrganisationSchemeDS.EXTERNAL_PUBLICATION_DATE, organisationSchemeDto.getLifeCycle().getExternalPublicationDate());

        // Comments
        commentsForm.setValue(OrganisationSchemeDS.COMMENTS, RecordUtils.getInternationalStringRecord(organisationSchemeDto.getComment()));

        // Annotations
        annotationsPanel.setAnnotations(organisationSchemeDto.getAnnotations());
    }

    public void setOrganisationSchemeEditionMode(OrganisationSchemeMetamacDto organisationSchemeDto) {
        // Identifiers
        identifiersEditionForm.setValue(OrganisationSchemeDS.CODE, organisationSchemeDto.getCode());
        identifiersEditionForm.setValue(OrganisationSchemeDS.CODE_VIEW, organisationSchemeDto.getCode());
        identifiersEditionForm.setValue(OrganisationSchemeDS.URI, organisationSchemeDto.getUriProvider());
        identifiersEditionForm.setValue(OrganisationSchemeDS.URN, organisationSchemeDto.getUrn());
        identifiersEditionForm.setValue(OrganisationSchemeDS.URN_PROVIDER, organisationSchemeDto.getUrnProvider());
        identifiersEditionForm.setValue(OrganisationSchemeDS.VERSION_LOGIC, organisationSchemeDto.getVersionLogic());
        identifiersEditionForm.setValue(OrganisationSchemeDS.NAME, RecordUtils.getInternationalStringRecord(organisationSchemeDto.getName()));
        identifiersEditionForm.markForRedraw();

        // Content descriptors
        contentDescriptorsEditionForm.setValue(OrganisationSchemeDS.TYPE, organisationSchemeDto.getType().name());
        contentDescriptorsEditionForm.setValue(OrganisationSchemeDS.TYPE_VIEW, CommonUtils.getOrganisationSchemeTypeName(organisationSchemeDto.getType()));
        contentDescriptorsEditionForm.setValue(OrganisationSchemeDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(organisationSchemeDto.getDescription()));
        contentDescriptorsEditionForm.setValue(OrganisationSchemeDS.IS_PARTIAL, org.siemac.metamac.srm.web.client.utils.CommonUtils.getBooleanName(organisationSchemeDto.getIsPartial()));
        contentDescriptorsEditionForm.setValue(OrganisationSchemeDS.IS_EXTERNAL_REFERENCE, organisationSchemeDto.getIsExternalReference() != null ? (organisationSchemeDto.getIsExternalReference()
                ? MetamacWebCommon.getConstants().yes()
                : MetamacWebCommon.getConstants().no()) : StringUtils.EMPTY);
        contentDescriptorsEditionForm.setValue(OrganisationSchemeDS.FINAL, organisationSchemeDto.getFinalLogic() != null ? (organisationSchemeDto.getFinalLogic() ? MetamacWebCommon.getConstants()
                .yes() : MetamacWebCommon.getConstants().no()) : StringUtils.EMPTY);
        contentDescriptorsEditionForm.markForRedraw();

        // Production descriptors
        productionDescriptorsEditionForm.setValue(OrganisationSchemeDS.MAINTAINER,
                org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourceName(organisationSchemeDto.getMaintainer()));
        productionDescriptorsEditionForm.setValue(OrganisationSchemeDS.PROC_STATUS,
                org.siemac.metamac.srm.web.client.utils.CommonUtils.getProcStatusName(organisationSchemeDto.getLifeCycle().getProcStatus()));

        // Diffusion descriptors
        diffusionDescriptorsEditionForm.setValue(OrganisationSchemeDS.REPLACED_BY_VERSION, organisationSchemeDto.getReplacedByVersion());
        diffusionDescriptorsEditionForm.setValue(OrganisationSchemeDS.REPLACE_TO_VERSION, organisationSchemeDto.getReplaceToVersion());
        diffusionDescriptorsEditionForm.setValue(OrganisationSchemeDS.VALID_FROM, DateUtils.getFormattedDate(organisationSchemeDto.getValidFrom()));
        diffusionDescriptorsEditionForm.setValue(OrganisationSchemeDS.VALID_TO, DateUtils.getFormattedDate(organisationSchemeDto.getValidTo()));
        diffusionDescriptorsEditionForm.setValue(OrganisationSchemeDS.IS_EXTERNAL_PUBLICATION_FAILED, BooleanUtils.isTrue(organisationSchemeDto.getLifeCycle().getIsExternalPublicationFailed())
                ? MetamacWebCommon.getConstants().yes()
                : MetamacWebCommon.getConstants().no());
        diffusionDescriptorsEditionForm.setValue(OrganisationSchemeDS.EXTERNAL_PUBLICATION_FAILED_DATE,
                DateUtils.getFormattedDate(organisationSchemeDto.getLifeCycle().getExternalPublicationFailedDate()));

        // Version responsibility
        versionResponsibilityEditionForm.setValue(OrganisationSchemeDS.PRODUCTION_VALIDATION_USER, organisationSchemeDto.getLifeCycle().getProductionValidationUser());
        versionResponsibilityEditionForm.setValue(OrganisationSchemeDS.PRODUCTION_VALIDATION_DATE, organisationSchemeDto.getLifeCycle().getProductionValidationDate());
        versionResponsibilityEditionForm.setValue(OrganisationSchemeDS.DIFFUSION_VALIDATION_USER, organisationSchemeDto.getLifeCycle().getDiffusionValidationUser());
        versionResponsibilityEditionForm.setValue(OrganisationSchemeDS.DIFFUSION_VALIDATION_DATE, organisationSchemeDto.getLifeCycle().getDiffusionValidationDate());
        versionResponsibilityEditionForm.setValue(OrganisationSchemeDS.INTERNAL_PUBLICATION_USER, organisationSchemeDto.getLifeCycle().getInternalPublicationUser());
        versionResponsibilityEditionForm.setValue(OrganisationSchemeDS.INTERNAL_PUBLICATION_DATE, organisationSchemeDto.getLifeCycle().getInternalPublicationDate());
        versionResponsibilityEditionForm.setValue(OrganisationSchemeDS.EXTERNAL_PUBLICATION_USER, organisationSchemeDto.getLifeCycle().getExternalPublicationUser());
        versionResponsibilityEditionForm.setValue(OrganisationSchemeDS.EXTERNAL_PUBLICATION_DATE, organisationSchemeDto.getLifeCycle().getExternalPublicationDate());

        // Comments
        commentsEditionForm.setValue(OrganisationSchemeDS.COMMENTS, RecordUtils.getInternationalStringRecord(organisationSchemeDto.getComment()));

        // Annotations
        annotationsEditionPanel.setAnnotations(organisationSchemeDto.getAnnotations());
    }

    public OrganisationSchemeMetamacDto getOrganisationSchemeDto() {
        // Identifiers
        organisationSchemeDto.setCode(identifiersEditionForm.getValueAsString(OrganisationSchemeDS.CODE));
        organisationSchemeDto.setName((InternationalStringDto) identifiersEditionForm.getValue(OrganisationSchemeDS.NAME));
        // Content descriptors
        organisationSchemeDto.setType(contentDescriptorsEditionForm.getValueAsString(OrganisationSchemeDS.TYPE) != null ? OrganisationSchemeTypeEnum.valueOf(contentDescriptorsEditionForm
                .getValueAsString(OrganisationSchemeDS.TYPE)) : null);
        organisationSchemeDto.setDescription((InternationalStringDto) contentDescriptorsEditionForm.getValue(OrganisationSchemeDS.DESCRIPTION));

        // Comments
        organisationSchemeDto.setComment((InternationalStringDto) commentsEditionForm.getValue(OrganisationSchemeDS.COMMENTS));

        // Annotations
        organisationSchemeDto.getAnnotations().clear();
        organisationSchemeDto.getAnnotations().addAll(annotationsEditionPanel.getAnnotations());

        return organisationSchemeDto;
    }

    private void setEditionMode() {
        mainFormLayout.setEditionMode();
    }

    private List<String> getSelectedOrganisationUrns() {
        List<String> urns = new ArrayList<String>();
        for (ListGridRecord record : organisationListGrid.getSelectedRecords()) {
            OrganisationRecord organisationRecord = (OrganisationRecord) record;
            urns.add(organisationRecord.getUrn());
        }
        return urns;
    }

    private void showOrganisationList() {
        toolStrip.show();
        organisationListGrid.show();
        organisationsTreeGrid.hide();
    }

    private void showOrganisationTree() {
        toolStrip.hide();
        organisationListGrid.hide();
        organisationsTreeGrid.show();
    }

    private void showListGridDeleteButton() {
        if (!ProcStatusEnum.INTERNALLY_PUBLISHED.equals(organisationSchemeDto.getLifeCycle().getProcStatus())
                && !ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(organisationSchemeDto.getLifeCycle().getProcStatus())
                && OrganisationsClientSecurityUtils.canDeleteOrganisation(organisationSchemeDto.getLifeCycle().getProcStatus(), organisationSchemeDto.getType())) {
            deleteButton.show();
        } else {
            deleteButton.hide();
        }
    }

    private FormItemIfFunction getCodeFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return canOrganisationSchemeCodeBeEdited();
            }
        };
    }

    private FormItemIfFunction getStaticCodeFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !canOrganisationSchemeCodeBeEdited();
            }
        };
    }

    private boolean canOrganisationSchemeCodeBeEdited() {
        // CODE cannot be modified if status is INTERNALLY_PUBLISHED or EXTERNALLY_PUBLISHED, or if version is greater than VERSION_INITIAL_VERSION (01.000)
        // and if the OrganisationType is not AGENCY_SCHEME, DATA_PROVIDER_SCHEME or DATA_CONSUMER_SCHEME
        // TODO check maintainer
        return org.siemac.metamac.srm.web.client.utils.CommonUtils.canCodeBeEdited(organisationSchemeDto.getLifeCycle().getProcStatus(), organisationSchemeDto.getVersionLogic())
                && (!CommonUtils.isAgencyScheme(organisationSchemeDto.getType()) && !CommonUtils.isDataProviderScheme(organisationSchemeDto.getType()) && !CommonUtils
                        .isDataConsumerScheme(organisationSchemeDto.getType()));
    }
}
