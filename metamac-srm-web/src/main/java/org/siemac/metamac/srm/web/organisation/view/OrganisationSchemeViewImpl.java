package org.siemac.metamac.srm.web.organisation.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.BooleanUtils;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.core.common.util.shared.VersionUtil;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.widgets.AnnotationsPanel;
import org.siemac.metamac.srm.web.client.widgets.VersionWindow;
import org.siemac.metamac.srm.web.organisation.model.ds.OrganisationSchemeDS;
import org.siemac.metamac.srm.web.organisation.presenter.OrganisationSchemePresenter;
import org.siemac.metamac.srm.web.organisation.utils.OrganisationsClientSecurityUtils;
import org.siemac.metamac.srm.web.organisation.view.handlers.OrganisationSchemeUiHandlers;
import org.siemac.metamac.srm.web.organisation.widgets.OrganisationSchemeMainFormLayout;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.utils.DateUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.InformationWindow;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.BooleanSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextAreaItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class OrganisationSchemeViewImpl extends ViewWithUiHandlers<OrganisationSchemeUiHandlers> implements OrganisationSchemePresenter.OrganisationSchemeView {

    private VLayout                          panel;
    private OrganisationSchemeMainFormLayout mainFormLayout;

    // View forms
    private GroupDynamicForm                 identifiersForm;
    private GroupDynamicForm                 contentDescriptorsForm;
    private GroupDynamicForm                 classDescriptorsForm;
    private GroupDynamicForm                 productionDescriptorsForm;
    private GroupDynamicForm                 diffusionDescriptorsForm;
    private GroupDynamicForm                 versionResponsibilityForm;
    private AnnotationsPanel                 annotationsPanel;

    // Edition forms
    private GroupDynamicForm                 identifiersEditionForm;
    private GroupDynamicForm                 contentDescriptorsEditionForm;
    private GroupDynamicForm                 classDescriptorsEditionForm;
    private GroupDynamicForm                 productionDescriptorsEditionForm;
    private GroupDynamicForm                 diffusionDescriptorsEditionForm;
    private GroupDynamicForm                 versionResponsibilityEditionForm;
    private AnnotationsPanel                 annotationsEditionPanel;

    private OrganisationSchemeMetamacDto     organisationSchemeDto;

    @Inject
    public OrganisationSchemeViewImpl() {
        super();
        panel = new VLayout();
        panel.setHeight100();
        panel.setOverflow(Overflow.SCROLL);

        //
        // ORGANISATION SCHEME
        //

        mainFormLayout = new OrganisationSchemeMainFormLayout(OrganisationsClientSecurityUtils.canUpdateOrganisationScheme());
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

                classDescriptorsForm.setTranslationsShowed(translationsShowed);
                classDescriptorsEditionForm.setTranslationsShowed(translationsShowed);

                productionDescriptorsForm.setTranslationsShowed(translationsShowed);
                productionDescriptorsEditionForm.setTranslationsShowed(translationsShowed);

                diffusionDescriptorsForm.setTranslationsShowed(translationsShowed);
                diffusionDescriptorsEditionForm.setTranslationsShowed(translationsShowed);

                versionResponsibilityForm.setTranslationsShowed(translationsShowed);
                versionResponsibilityEditionForm.setTranslationsShowed(translationsShowed);

                annotationsPanel.setTranslationsShowed(translationsShowed);
                annotationsEditionPanel.setTranslationsShowed(translationsShowed);
            }
        });

        // Edit: Add a custom handler to check scheme status before start editing
        mainFormLayout.getEditToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ProcStatusEnum status = organisationSchemeDto.getProcStatus();
                if (ProcStatusEnum.INTERNALLY_PUBLISHED.equals(status) || ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(status)) {
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
                if (identifiersEditionForm.validate(false) && contentDescriptorsEditionForm.validate(false) && classDescriptorsEditionForm.validate(false)
                        && productionDescriptorsEditionForm.validate(false) && diffusionDescriptorsEditionForm.validate(false)) {
                    getUiHandlers().saveOrganisationScheme(getOrganisationSchemeDto());
                }
            }
        });

        // Life cycle
        mainFormLayout.getSendToProductionValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().sendToProductionValidation(organisationSchemeDto.getUrn(), organisationSchemeDto.getProcStatus());
            }
        });
        mainFormLayout.getSendToDiffusionValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().sendToDiffusionValidation(organisationSchemeDto.getUrn(), organisationSchemeDto.getProcStatus());
            }
        });
        mainFormLayout.getRejectValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().rejectValidation(organisationSchemeDto.getUrn(), organisationSchemeDto.getProcStatus());
            }
        });
        mainFormLayout.getPublishInternally().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().publishInternally(organisationSchemeDto.getUrn(), organisationSchemeDto.getProcStatus());
            }
        });
        mainFormLayout.getPublishExternally().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().publishExternally(organisationSchemeDto.getUrn(), organisationSchemeDto.getProcStatus());
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
        identifiersForm = new GroupDynamicForm(getConstants().organisationSchemeIdentifiers());
        ViewTextItem code = new ViewTextItem(OrganisationSchemeDS.CODE, getConstants().maintainableArtefactCode());
        ViewMultiLanguageTextItem name = new ViewMultiLanguageTextItem(OrganisationSchemeDS.NAME, getConstants().maintainableArtefactName());
        ViewTextItem uri = new ViewTextItem(OrganisationSchemeDS.URI, getConstants().maintainableArtefactUri());
        ViewTextItem urn = new ViewTextItem(OrganisationSchemeDS.URN, getConstants().maintainableArtefactUrn());
        ViewTextItem version = new ViewTextItem(OrganisationSchemeDS.VERSION_LOGIC, getConstants().maintainableArtefactVersionLogic());
        identifiersForm.setFields(code, name, uri, urn, version);

        // Content descriptors
        contentDescriptorsForm = new GroupDynamicForm(getConstants().organisationSchemeContentDescriptors());
        ViewMultiLanguageTextItem description = new ViewMultiLanguageTextItem(OrganisationSchemeDS.DESCRIPTION, getConstants().maintainableArtefactDescription());
        ViewTextItem partial = new ViewTextItem(OrganisationSchemeDS.IS_PARTIAL, getConstants().itemSchemeIsPartial());
        ViewTextItem isExternalReference = new ViewTextItem(OrganisationSchemeDS.IS_EXTERNAL_REFERENCE, getConstants().maintainableArtefactIsExternalReference());
        ViewTextItem isFinal = new ViewTextItem(OrganisationSchemeDS.FINAL, getConstants().maintainableArtefactFinalLogic());
        contentDescriptorsForm.setFields(description, partial, isExternalReference, isFinal);

        // Class descriptors
        classDescriptorsForm = new GroupDynamicForm(getConstants().organisationSchemeClassDescriptors());
        ViewTextItem agency = new ViewTextItem(OrganisationSchemeDS.MAINTAINER, getConstants().maintainableArtefactMaintainer());
        classDescriptorsForm.setFields(agency);

        // Production descriptors
        productionDescriptorsForm = new GroupDynamicForm(getConstants().organisationSchemeProductionDescriptors());
        ViewTextItem procStatus = new ViewTextItem(OrganisationSchemeDS.PROC_STATUS, getConstants().lifeCycleProcStatus());
        productionDescriptorsForm.setFields(procStatus);

        // Diffusion descriptors
        diffusionDescriptorsForm = new GroupDynamicForm(getConstants().organisationSchemeDiffusionDescriptors());
        ViewTextItem replacedBy = new ViewTextItem(OrganisationSchemeDS.REPLACED_BY, getConstants().maintainableArtefactReplacedBy());
        ViewTextItem replaceTo = new ViewTextItem(OrganisationSchemeDS.REPLACE_TO, getConstants().maintainableArtefactReplaceTo());
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

        // Annotations
        annotationsPanel = new AnnotationsPanel(true);

        mainFormLayout.addViewCanvas(identifiersForm);
        mainFormLayout.addViewCanvas(contentDescriptorsForm);
        mainFormLayout.addViewCanvas(classDescriptorsForm);
        mainFormLayout.addViewCanvas(productionDescriptorsForm);
        mainFormLayout.addViewCanvas(diffusionDescriptorsForm);
        mainFormLayout.addViewCanvas(versionResponsibilityForm);
        mainFormLayout.addViewCanvas(annotationsPanel);
    }

    private void createEditionForm() {
        // Identifiers
        identifiersEditionForm = new GroupDynamicForm(getConstants().organisationSchemeIdentifiers());
        RequiredTextItem code = new RequiredTextItem(OrganisationSchemeDS.CODE, getConstants().maintainableArtefactCode());
        code.setValidators(CommonWebUtils.getSemanticIdentifierCustomValidator());
        code.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                // CODE cannot be modified if status is INTERNALLY_PUBLISHED or EXTERNALLY_PUBLISHED, or if version is greater than VERSION_INITIAL_VERSION (01.000)
                return !((ProcStatusEnum.INTERNALLY_PUBLISHED.equals(organisationSchemeDto.getProcStatus()) || ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(organisationSchemeDto.getProcStatus())) || (!VersionUtil.VERSION_INITIAL_VERSION
                        .equals(organisationSchemeDto.getVersionLogic()) && !StringUtils.isBlank(organisationSchemeDto.getVersionLogic())));
            }
        });
        ViewTextItem staticCode = new ViewTextItem(OrganisationSchemeDS.CODE_VIEW, getConstants().maintainableArtefactCode());
        staticCode.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !form.getItem(OrganisationSchemeDS.CODE).isVisible();
            }
        });
        MultiLanguageTextItem name = new MultiLanguageTextItem(OrganisationSchemeDS.NAME, getConstants().maintainableArtefactName());
        name.setRequired(true);
        ViewTextItem uri = new ViewTextItem(OrganisationSchemeDS.URI, getConstants().maintainableArtefactUri());
        ViewTextItem urn = new ViewTextItem(OrganisationSchemeDS.URN, getConstants().maintainableArtefactUrn());
        ViewTextItem version = new ViewTextItem(OrganisationSchemeDS.VERSION_LOGIC, getConstants().maintainableArtefactVersionLogic());
        identifiersEditionForm.setFields(code, staticCode, name, uri, urn, version);

        // Content descriptors
        contentDescriptorsEditionForm = new GroupDynamicForm(getConstants().organisationSchemeContentDescriptors());
        MultiLanguageTextAreaItem description = new MultiLanguageTextAreaItem(OrganisationSchemeDS.DESCRIPTION, getConstants().maintainableArtefactDescription());
        BooleanSelectItem partial = new BooleanSelectItem(OrganisationSchemeDS.IS_PARTIAL, getConstants().itemSchemeIsPartial());
        ViewTextItem isExternalReference = new ViewTextItem(OrganisationSchemeDS.IS_EXTERNAL_REFERENCE, getConstants().maintainableArtefactIsExternalReference());
        ViewTextItem isFinal = new ViewTextItem(OrganisationSchemeDS.FINAL, getConstants().maintainableArtefactFinalLogic());
        contentDescriptorsEditionForm.setFields(description, partial, isExternalReference, isFinal);

        // Class descriptors
        classDescriptorsEditionForm = new GroupDynamicForm(getConstants().organisationSchemeClassDescriptors());
        ViewTextItem agency = new ViewTextItem(OrganisationSchemeDS.MAINTAINER, getConstants().maintainableArtefactMaintainer());
        classDescriptorsEditionForm.setFields(agency);

        // Production descriptors
        productionDescriptorsEditionForm = new GroupDynamicForm(getConstants().organisationSchemeProductionDescriptors());
        ViewTextItem procStatus = new ViewTextItem(OrganisationSchemeDS.PROC_STATUS, getConstants().lifeCycleProcStatus());
        productionDescriptorsEditionForm.setFields(procStatus);

        // Diffusion descriptors
        diffusionDescriptorsEditionForm = new GroupDynamicForm(getConstants().organisationSchemeDiffusionDescriptors());
        ViewTextItem replacedBy = new ViewTextItem(OrganisationSchemeDS.REPLACED_BY, getConstants().maintainableArtefactReplacedBy());
        ViewTextItem replaceTo = new ViewTextItem(OrganisationSchemeDS.REPLACE_TO, getConstants().maintainableArtefactReplaceTo());
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

        // Annotations
        annotationsEditionPanel = new AnnotationsPanel(false);

        mainFormLayout.addEditionCanvas(identifiersEditionForm);
        mainFormLayout.addEditionCanvas(contentDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(classDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(productionDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(diffusionDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(versionResponsibilityEditionForm);
        mainFormLayout.addEditionCanvas(annotationsEditionPanel);
    }

    /*
     * GWTP will call setInSlot when a child presenter asks to be added under this view
     */
    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == OrganisationSchemePresenter.TYPE_SetContextAreaContentOrganisationSchemeToolBar) {
            if (content != null) {
                Canvas[] canvas = ((ToolStrip) content).getMembers();
                for (int i = 0; i < canvas.length; i++) {
                    if (canvas[i] instanceof ToolStripButton) {
                        if (ToolStripButtonEnum.ORGANISATIONS.getValue().equals(((ToolStripButton) canvas[i]).getID())) {
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
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setOrganisationScheme(OrganisationSchemeMetamacDto organisationSchemeMetamacDto) {
        this.organisationSchemeDto = organisationSchemeMetamacDto;

        String defaultLocalized = InternationalStringUtils.getLocalisedString(organisationSchemeMetamacDto.getName());
        String title = defaultLocalized != null ? defaultLocalized : StringUtils.EMPTY;
        mainFormLayout.setTitleLabelContents(title);

        mainFormLayout.updatePublishSection(organisationSchemeMetamacDto.getProcStatus(), organisationSchemeMetamacDto.getValidTo());
        mainFormLayout.setViewMode();

        setOrganisationSchemeViewMode(organisationSchemeMetamacDto);
        setOrganisationSchemeEditionMode(organisationSchemeMetamacDto);
    }

    public void setOrganisationSchemeViewMode(OrganisationSchemeMetamacDto organisationSchemeMetamacDto) {
        // Identifiers
        identifiersForm.setValue(OrganisationSchemeDS.CODE, organisationSchemeMetamacDto.getCode());
        identifiersForm.setValue(OrganisationSchemeDS.URI, organisationSchemeMetamacDto.getUri());
        identifiersForm.setValue(OrganisationSchemeDS.URN, organisationSchemeMetamacDto.getUrn());
        identifiersForm.setValue(OrganisationSchemeDS.VERSION_LOGIC, organisationSchemeMetamacDto.getVersionLogic());
        identifiersForm.setValue(OrganisationSchemeDS.NAME, RecordUtils.getInternationalStringRecord(organisationSchemeMetamacDto.getName()));

        // Content descriptors
        contentDescriptorsForm.setValue(OrganisationSchemeDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(organisationSchemeMetamacDto.getDescription()));
        contentDescriptorsForm.setValue(OrganisationSchemeDS.IS_PARTIAL, organisationSchemeMetamacDto.getIsPartial() != null ? (organisationSchemeMetamacDto.getIsPartial() ? MetamacWebCommon
                .getConstants().yes() : MetamacWebCommon.getConstants().no()) : StringUtils.EMPTY);
        contentDescriptorsForm.setValue(OrganisationSchemeDS.IS_EXTERNAL_REFERENCE,
                organisationSchemeMetamacDto.getIsExternalReference() != null ? (organisationSchemeMetamacDto.getIsExternalReference() ? MetamacWebCommon.getConstants().yes() : MetamacWebCommon
                        .getConstants().no()) : StringUtils.EMPTY);
        contentDescriptorsForm.setValue(OrganisationSchemeDS.FINAL, organisationSchemeMetamacDto.getFinalLogic() != null ? (organisationSchemeMetamacDto.getFinalLogic() ? MetamacWebCommon
                .getConstants().yes() : MetamacWebCommon.getConstants().no()) : StringUtils.EMPTY);

        // Class descriptors
        classDescriptorsForm.setValue(OrganisationSchemeDS.MAINTAINER, organisationSchemeMetamacDto.getMaintainer() != null
                ? organisationSchemeMetamacDto.getMaintainer().getCode()
                : StringUtils.EMPTY);

        // Production descriptors
        productionDescriptorsForm.setValue(OrganisationSchemeDS.PROC_STATUS, org.siemac.metamac.srm.web.client.utils.CommonUtils.getProcStatusName(organisationSchemeMetamacDto.getProcStatus()));

        // Diffusion descriptors
        diffusionDescriptorsForm.setValue(OrganisationSchemeDS.REPLACED_BY, organisationSchemeMetamacDto.getReplacedBy());
        diffusionDescriptorsForm.setValue(OrganisationSchemeDS.REPLACE_TO, organisationSchemeMetamacDto.getReplaceTo());
        diffusionDescriptorsForm.setValue(OrganisationSchemeDS.VALID_FROM, organisationSchemeMetamacDto.getValidFrom());
        diffusionDescriptorsForm.setValue(OrganisationSchemeDS.VALID_TO, organisationSchemeMetamacDto.getValidTo());
        diffusionDescriptorsForm.setValue(OrganisationSchemeDS.IS_EXTERNAL_PUBLICATION_FAILED, BooleanUtils.isTrue(organisationSchemeMetamacDto.getIsExternalPublicationFailed()) ? MetamacWebCommon
                .getConstants().yes() : StringUtils.EMPTY);
        diffusionDescriptorsForm.setValue(OrganisationSchemeDS.EXTERNAL_PUBLICATION_FAILED_DATE, DateUtils.getFormattedDate(organisationSchemeMetamacDto.getExternalPublicationFailedDate()));

        // Version responsibility
        versionResponsibilityForm.setValue(OrganisationSchemeDS.PRODUCTION_VALIDATION_USER, organisationSchemeMetamacDto.getProductionValidationUser());
        versionResponsibilityForm.setValue(OrganisationSchemeDS.PRODUCTION_VALIDATION_DATE, organisationSchemeMetamacDto.getProductionValidationDate());
        versionResponsibilityForm.setValue(OrganisationSchemeDS.DIFFUSION_VALIDATION_USER, organisationSchemeMetamacDto.getDiffusionValidationUser());
        versionResponsibilityForm.setValue(OrganisationSchemeDS.DIFFUSION_VALIDATION_DATE, organisationSchemeMetamacDto.getDiffusionValidationDate());
        versionResponsibilityForm.setValue(OrganisationSchemeDS.INTERNAL_PUBLICATION_USER, organisationSchemeMetamacDto.getInternalPublicationUser());
        versionResponsibilityForm.setValue(OrganisationSchemeDS.INTERNAL_PUBLICATION_DATE, organisationSchemeMetamacDto.getInternalPublicationDate());
        versionResponsibilityForm.setValue(OrganisationSchemeDS.EXTERNAL_PUBLICATION_USER, organisationSchemeMetamacDto.getExternalPublicationUser());
        versionResponsibilityForm.setValue(OrganisationSchemeDS.EXTERNAL_PUBLICATION_DATE, organisationSchemeMetamacDto.getExternalPublicationDate());

        // Annotations
        annotationsPanel.setAnnotations(organisationSchemeMetamacDto.getAnnotations());
    }

    public void setOrganisationSchemeEditionMode(OrganisationSchemeMetamacDto organisationSchemeMetamacDto) {
        // Identifiers
        identifiersEditionForm.setValue(OrganisationSchemeDS.CODE, organisationSchemeMetamacDto.getCode());
        identifiersEditionForm.setValue(OrganisationSchemeDS.CODE_VIEW, organisationSchemeMetamacDto.getCode());
        identifiersEditionForm.setValue(OrganisationSchemeDS.URI, organisationSchemeMetamacDto.getUri());
        identifiersEditionForm.setValue(OrganisationSchemeDS.URN, organisationSchemeMetamacDto.getUrn());
        identifiersEditionForm.setValue(OrganisationSchemeDS.VERSION_LOGIC, organisationSchemeMetamacDto.getVersionLogic());
        identifiersEditionForm.setValue(OrganisationSchemeDS.NAME, RecordUtils.getInternationalStringRecord(organisationSchemeMetamacDto.getName()));

        // Content descriptors
        contentDescriptorsEditionForm.setValue(OrganisationSchemeDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(organisationSchemeMetamacDto.getDescription()));
        contentDescriptorsEditionForm.setValue(OrganisationSchemeDS.IS_PARTIAL, organisationSchemeMetamacDto.getIsPartial() != null ? organisationSchemeMetamacDto.getIsPartial() : false);
        contentDescriptorsEditionForm.setValue(OrganisationSchemeDS.IS_EXTERNAL_REFERENCE,
                organisationSchemeMetamacDto.getIsExternalReference() != null ? (organisationSchemeMetamacDto.getIsExternalReference() ? MetamacWebCommon.getConstants().yes() : MetamacWebCommon
                        .getConstants().no()) : StringUtils.EMPTY);
        contentDescriptorsEditionForm.setValue(OrganisationSchemeDS.FINAL, organisationSchemeMetamacDto.getFinalLogic() != null ? (organisationSchemeMetamacDto.getFinalLogic() ? MetamacWebCommon
                .getConstants().yes() : MetamacWebCommon.getConstants().no()) : StringUtils.EMPTY);

        // Class descriptors
        classDescriptorsEditionForm.setValue(OrganisationSchemeDS.MAINTAINER, organisationSchemeMetamacDto.getMaintainer() != null
                ? organisationSchemeMetamacDto.getMaintainer().getCode()
                : StringUtils.EMPTY);

        // Production descriptors
        productionDescriptorsEditionForm
                .setValue(OrganisationSchemeDS.PROC_STATUS, org.siemac.metamac.srm.web.client.utils.CommonUtils.getProcStatusName(organisationSchemeMetamacDto.getProcStatus()));

        // Diffusion descriptors
        diffusionDescriptorsEditionForm.setValue(OrganisationSchemeDS.REPLACED_BY, organisationSchemeMetamacDto.getReplacedBy());
        diffusionDescriptorsEditionForm.setValue(OrganisationSchemeDS.REPLACE_TO, organisationSchemeMetamacDto.getReplaceTo());
        diffusionDescriptorsEditionForm.setValue(OrganisationSchemeDS.VALID_FROM, DateUtils.getFormattedDate(organisationSchemeMetamacDto.getValidFrom()));
        diffusionDescriptorsEditionForm.setValue(OrganisationSchemeDS.VALID_TO, DateUtils.getFormattedDate(organisationSchemeMetamacDto.getValidTo()));
        diffusionDescriptorsEditionForm.setValue(OrganisationSchemeDS.IS_EXTERNAL_PUBLICATION_FAILED, BooleanUtils.isTrue(organisationSchemeMetamacDto.getIsExternalPublicationFailed())
                ? MetamacWebCommon.getConstants().yes()
                : MetamacWebCommon.getConstants().no());
        diffusionDescriptorsEditionForm.setValue(OrganisationSchemeDS.EXTERNAL_PUBLICATION_FAILED_DATE, DateUtils.getFormattedDate(organisationSchemeMetamacDto.getExternalPublicationFailedDate()));

        // Version responsibility
        versionResponsibilityEditionForm.setValue(OrganisationSchemeDS.PRODUCTION_VALIDATION_USER, organisationSchemeMetamacDto.getProductionValidationUser());
        versionResponsibilityEditionForm.setValue(OrganisationSchemeDS.PRODUCTION_VALIDATION_DATE, organisationSchemeMetamacDto.getProductionValidationDate());
        versionResponsibilityEditionForm.setValue(OrganisationSchemeDS.DIFFUSION_VALIDATION_USER, organisationSchemeMetamacDto.getDiffusionValidationUser());
        versionResponsibilityEditionForm.setValue(OrganisationSchemeDS.DIFFUSION_VALIDATION_DATE, organisationSchemeMetamacDto.getDiffusionValidationDate());
        versionResponsibilityEditionForm.setValue(OrganisationSchemeDS.INTERNAL_PUBLICATION_USER, organisationSchemeMetamacDto.getInternalPublicationUser());
        versionResponsibilityEditionForm.setValue(OrganisationSchemeDS.INTERNAL_PUBLICATION_DATE, organisationSchemeMetamacDto.getInternalPublicationDate());
        versionResponsibilityEditionForm.setValue(OrganisationSchemeDS.EXTERNAL_PUBLICATION_USER, organisationSchemeMetamacDto.getExternalPublicationUser());
        versionResponsibilityEditionForm.setValue(OrganisationSchemeDS.EXTERNAL_PUBLICATION_DATE, organisationSchemeMetamacDto.getExternalPublicationDate());

        // Annotations
        annotationsEditionPanel.setAnnotations(organisationSchemeMetamacDto.getAnnotations());
    }

    public OrganisationSchemeMetamacDto getOrganisationSchemeDto() {
        // Identifiers
        organisationSchemeDto.setCode(identifiersEditionForm.getValueAsString(OrganisationSchemeDS.CODE));
        organisationSchemeDto.setName((InternationalStringDto) identifiersEditionForm.getValue(OrganisationSchemeDS.NAME));
        // Content descriptors
        organisationSchemeDto.setDescription((InternationalStringDto) contentDescriptorsEditionForm.getValue(OrganisationSchemeDS.DESCRIPTION));
        organisationSchemeDto.setIsPartial((contentDescriptorsEditionForm.getValue(OrganisationSchemeDS.IS_PARTIAL) != null && !StringUtils.isEmpty(contentDescriptorsEditionForm
                .getValueAsString(OrganisationSchemeDS.IS_PARTIAL))) ? Boolean.valueOf(contentDescriptorsEditionForm.getValueAsString(OrganisationSchemeDS.IS_PARTIAL)) : false);

        // Annotations
        organisationSchemeDto.getAnnotations().clear();
        organisationSchemeDto.getAnnotations().addAll(annotationsEditionPanel.getAnnotations());

        return organisationSchemeDto;
    }

    private void setEditionMode() {
        mainFormLayout.setEditionMode();
    }

}
