package org.siemac.metamac.srm.web.dsd.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.BooleanUtils;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.core.common.util.shared.VersionUtil;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ItemSchemeMetamacProcStatusEnum;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.widgets.AnnotationsPanel;
import org.siemac.metamac.srm.web.client.widgets.VersionWindow;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeDS;
import org.siemac.metamac.srm.web.dsd.model.ds.DataStructureDefinitionDS;
import org.siemac.metamac.srm.web.dsd.presenter.DsdGeneralTabPresenter;
import org.siemac.metamac.srm.web.dsd.utils.DsdClientSecurityUtils;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdGeneralTabUiHandlers;
import org.siemac.metamac.srm.web.dsd.widgets.DsdMainFormLayout;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.utils.DateUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.InformationWindow;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextAreaItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.HasClickHandlers;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.layout.VLayout;

public class DsdGeneralTabViewImpl extends ViewWithUiHandlers<DsdGeneralTabUiHandlers> implements DsdGeneralTabPresenter.DsdGeneralTabView {

    private DsdGeneralTabUiHandlers           uiHandlers;

    private VLayout                           panel;

    private DsdMainFormLayout                 mainFormLayout;

    // VIEW FORM

    private GroupDynamicForm                  identifiersForm;
    private GroupDynamicForm                  generalForm;
    private GroupDynamicForm                  statusForm;
    private GroupDynamicForm                  versionResponsibilityForm;
    private AnnotationsPanel                  annotationsPanel;

    // EDITION FORM

    private GroupDynamicForm                  identifiersEditionForm;
    private GroupDynamicForm                  generalEditionForm;
    private GroupDynamicForm                  statusEditionForm;
    private GroupDynamicForm                  versionResponsibilityEditionForm;
    private AnnotationsPanel                  annotationsEditionPanel;

    private DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto;

    @Inject
    public DsdGeneralTabViewImpl() {
        super();
        panel = new VLayout();

        mainFormLayout = new DsdMainFormLayout(DsdClientSecurityUtils.canUpdateDsd());
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

                generalForm.setTranslationsShowed(translationsShowed);
                generalEditionForm.setTranslationsShowed(translationsShowed);

                annotationsPanel.setTranslationsShowed(translationsShowed);
                annotationsEditionPanel.setTranslationsShowed(translationsShowed);
            }
        });

        // Edit: Add a custom handler to check scheme status before start editing
        mainFormLayout.getEditToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ItemSchemeMetamacProcStatusEnum status = dataStructureDefinitionMetamacDto.getProcStatus();
                if (ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED.equals(status) || ItemSchemeMetamacProcStatusEnum.EXTERNALLY_PUBLISHED.equals(status)) {
                    // Create a new version
                    final InformationWindow window = new InformationWindow(getMessages().dsdEditionInfo(), getMessages().dsdEditionInfoDetailedMessage());
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
                if (identifiersEditionForm.validate(false) && generalEditionForm.validate(false) && statusEditionForm.validate(false)) {
                    uiHandlers.saveDsd(getDataStructureDefinitionDto());
                }
            }
        });

        // Life cycle
        mainFormLayout.getSendToProductionValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.sendToProductionValidation(dataStructureDefinitionMetamacDto.getUrn(), dataStructureDefinitionMetamacDto.getProcStatus());
            }
        });
        mainFormLayout.getSendToDiffusionValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.sendToDiffusionValidation(dataStructureDefinitionMetamacDto.getUrn(), dataStructureDefinitionMetamacDto.getProcStatus());
            }
        });
        mainFormLayout.getRejectValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.rejectValidation(dataStructureDefinitionMetamacDto.getUrn(), dataStructureDefinitionMetamacDto.getProcStatus());
            }
        });
        mainFormLayout.getPublishInternally().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.publishInternally(dataStructureDefinitionMetamacDto.getUrn(), dataStructureDefinitionMetamacDto.getProcStatus());
            }
        });
        mainFormLayout.getPublishExternally().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.publishExternally(dataStructureDefinitionMetamacDto.getUrn(), dataStructureDefinitionMetamacDto.getProcStatus());
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
                            uiHandlers.versioning(dataStructureDefinitionMetamacDto.getUrn(), versionWindow.getSelectedVersion());
                            versionWindow.destroy();
                        }
                    }
                });
            }
        });
    }

    /**
     * Creates and returns the view layout
     * 
     * @return
     */
    private void createViewForm() {
        // Identifiers Form
        identifiersForm = new GroupDynamicForm(MetamacSrmWeb.getConstants().dsdIdentifiers());
        ViewTextItem staticIdLogic = new ViewTextItem(DataStructureDefinitionDS.CODE, MetamacSrmWeb.getConstants().dsdIdentifier());
        ViewTextItem staticUriItem = new ViewTextItem(DataStructureDefinitionDS.URI, MetamacSrmWeb.getConstants().dsdUri());
        ViewTextItem staticUrnItem = new ViewTextItem(DataStructureDefinitionDS.URN, MetamacSrmWeb.getConstants().dsdUrn());
        ViewTextItem staticVersion = new ViewTextItem(DataStructureDefinitionDS.VERSION_LOGIC, MetamacSrmWeb.getConstants().dsdVersion());
        identifiersForm.setFields(staticIdLogic, staticUriItem, staticUrnItem, staticVersion);

        // General Form
        generalForm = new GroupDynamicForm(MetamacSrmWeb.getConstants().dsdDetails());
        ViewMultiLanguageTextItem staticNameItem = new ViewMultiLanguageTextItem(DataStructureDefinitionDS.NAME, MetamacSrmWeb.getConstants().dsdName());
        ViewMultiLanguageTextItem staticDescriptionItem = new ViewMultiLanguageTextItem(DataStructureDefinitionDS.DESCRIPTION, MetamacSrmWeb.getConstants().dsdDescription());
        ViewTextItem staticAgency = new ViewTextItem(DataStructureDefinitionDS.AGENCY, MetamacSrmWeb.getConstants().dsdAgency());
        generalForm.setFields(staticNameItem, staticDescriptionItem, staticAgency);

        // Status Form
        statusForm = new GroupDynamicForm(MetamacSrmWeb.getConstants().dsdStatus());
        ViewTextItem staticFinalItem = new ViewTextItem(DataStructureDefinitionDS.FINAL, MetamacSrmWeb.getConstants().dsdFinalStructure());
        ViewTextItem staticStartDateItem = new ViewTextItem(DataStructureDefinitionDS.VALID_FROM, MetamacSrmWeb.getConstants().dsdValidFrom());
        ViewTextItem staticEndDateItem = new ViewTextItem(DataStructureDefinitionDS.VALID_TO, MetamacSrmWeb.getConstants().dsdValidTo());
        statusForm.setFields(staticFinalItem, staticStartDateItem, staticEndDateItem);

        // Version responsibility
        versionResponsibilityForm = new GroupDynamicForm(getConstants().lifeCycleVersionResponsibility());
        ViewTextItem productionValidationUser = new ViewTextItem(DataStructureDefinitionDS.PRODUCTION_VALIDATION_USER, getConstants().lifeCycleProductionValidationUser());
        ViewTextItem productionValidationDate = new ViewTextItem(DataStructureDefinitionDS.PRODUCTION_VALIDATION_DATE, getConstants().lifeCycleProductionValidationDate());
        ViewTextItem diffusionValidationUser = new ViewTextItem(DataStructureDefinitionDS.DIFFUSION_VALIDATION_USER, getConstants().lifeCycleDiffusionValidationUser());
        ViewTextItem diffusionValidationDate = new ViewTextItem(DataStructureDefinitionDS.DIFFUSION_VALIDATION_DATE, getConstants().lifeCycleDiffusionValidationDate());
        ViewTextItem internalPublicationUser = new ViewTextItem(DataStructureDefinitionDS.INTERNAL_PUBLICATION_USER, getConstants().lifeCycleInternalPublicationUser());
        ViewTextItem internalPublicationDate = new ViewTextItem(DataStructureDefinitionDS.INTERNAL_PUBLICATION_DATE, getConstants().lifeCycleInternalPublicationDate());
        ViewTextItem externalPublicationUser = new ViewTextItem(DataStructureDefinitionDS.EXTERNAL_PUBLICATION_USER, getConstants().lifeCycleExternalPublicationUser());
        ViewTextItem externalPublicationDate = new ViewTextItem(DataStructureDefinitionDS.EXTERNAL_PUBLICATION_DATE, getConstants().lifeCycleExternalPublicationDate());
        versionResponsibilityForm.setFields(productionValidationUser, productionValidationDate, diffusionValidationUser, diffusionValidationDate, internalPublicationUser, internalPublicationDate,
                externalPublicationUser, externalPublicationDate);

        // Annotations
        annotationsPanel = new AnnotationsPanel(true);

        mainFormLayout.addViewCanvas(identifiersForm);
        mainFormLayout.addViewCanvas(generalForm);
        mainFormLayout.addViewCanvas(statusForm);
        mainFormLayout.addViewCanvas(versionResponsibilityForm);
        mainFormLayout.addViewCanvas(annotationsPanel);
    }

    /**
     * Creates and returns the edition layout
     * 
     * @return
     */
    private void createEditionForm() {
        // Identifiers Form
        identifiersEditionForm = new GroupDynamicForm(MetamacSrmWeb.getConstants().dsdIdentifiers());

        RequiredTextItem code = new RequiredTextItem(DataStructureDefinitionDS.CODE, getConstants().dsdCode());
        code.setValidators(CommonWebUtils.getSemanticIdentifierCustomValidator());
        code.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                // CODE cannot be modified if status is INTERNALLY_PUBLISHED or EXTERNALLY_PUBLISHED, or if version is greater than VERSION_INITIAL_VERSION (01.000)
                return !((ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED.equals(dataStructureDefinitionMetamacDto.getProcStatus()) || ItemSchemeMetamacProcStatusEnum.EXTERNALLY_PUBLISHED
                        .equals(dataStructureDefinitionMetamacDto.getProcStatus())) || (!VersionUtil.VERSION_INITIAL_VERSION.equals(dataStructureDefinitionMetamacDto.getVersionLogic()) && !StringUtils
                        .isBlank(dataStructureDefinitionMetamacDto.getVersionLogic())));
            }
        });
        ViewTextItem staticCode = new ViewTextItem(DataStructureDefinitionDS.CODE_VIEW, getConstants().dsdCode());
        staticCode.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !form.getItem(DataStructureDefinitionDS.CODE).isVisible();
            }
        });

        ViewTextItem staticUriItemEdit = new ViewTextItem(DataStructureDefinitionDS.URI, MetamacSrmWeb.getConstants().dsdUri());
        ViewTextItem staticUrnItemEdit = new ViewTextItem(DataStructureDefinitionDS.URN, MetamacSrmWeb.getConstants().dsdUrn());
        ViewTextItem staticVersionEdit = new ViewTextItem(DataStructureDefinitionDS.VERSION_LOGIC, MetamacSrmWeb.getConstants().dsdVersion());
        identifiersEditionForm.setFields(code, staticCode, staticUriItemEdit, staticUrnItemEdit, staticVersionEdit);

        // General Form
        generalEditionForm = new GroupDynamicForm(MetamacSrmWeb.getConstants().dsdDetails());
        MultiLanguageTextItem nameItem = new MultiLanguageTextItem(DataStructureDefinitionDS.NAME, MetamacSrmWeb.getConstants().dsdName());
        nameItem.setRequired(true);
        MultiLanguageTextAreaItem descriptionItem = new MultiLanguageTextAreaItem(DataStructureDefinitionDS.DESCRIPTION, MetamacSrmWeb.getConstants().dsdDescription());
        ViewTextItem staticAgencyEdit = new ViewTextItem(DataStructureDefinitionDS.AGENCY, MetamacSrmWeb.getConstants().dsdAgency());
        generalEditionForm.setFields(nameItem, descriptionItem, staticAgencyEdit);

        // Status Form
        statusEditionForm = new GroupDynamicForm(MetamacSrmWeb.getConstants().dsdStatus());
        ViewTextItem staticFinalItemEdit = new ViewTextItem(DataStructureDefinitionDS.FINAL, MetamacSrmWeb.getConstants().dsdFinalStructure());
        ViewTextItem staticStartDateItemEdit = new ViewTextItem(DataStructureDefinitionDS.VALID_FROM, MetamacSrmWeb.getConstants().dsdValidFrom());
        ViewTextItem staticEndDateItemEdit = new ViewTextItem(DataStructureDefinitionDS.VALID_TO, MetamacSrmWeb.getConstants().dsdValidTo());
        statusEditionForm.setFields(staticFinalItemEdit, staticStartDateItemEdit, staticEndDateItemEdit);

        // Version responsibility
        versionResponsibilityEditionForm = new GroupDynamicForm(getConstants().lifeCycleVersionResponsibility());
        ViewTextItem productionValidationUser = new ViewTextItem(DataStructureDefinitionDS.PRODUCTION_VALIDATION_USER, getConstants().lifeCycleProductionValidationUser());
        ViewTextItem productionValidationDate = new ViewTextItem(DataStructureDefinitionDS.PRODUCTION_VALIDATION_DATE, getConstants().lifeCycleProductionValidationDate());
        ViewTextItem diffusionValidationUser = new ViewTextItem(DataStructureDefinitionDS.DIFFUSION_VALIDATION_USER, getConstants().lifeCycleDiffusionValidationUser());
        ViewTextItem diffusionValidationDate = new ViewTextItem(DataStructureDefinitionDS.DIFFUSION_VALIDATION_DATE, getConstants().lifeCycleDiffusionValidationDate());
        ViewTextItem internalPublicationUser = new ViewTextItem(DataStructureDefinitionDS.INTERNAL_PUBLICATION_USER, getConstants().lifeCycleInternalPublicationUser());
        ViewTextItem internalPublicationDate = new ViewTextItem(DataStructureDefinitionDS.INTERNAL_PUBLICATION_DATE, getConstants().lifeCycleInternalPublicationDate());
        ViewTextItem externalPublicationUser = new ViewTextItem(DataStructureDefinitionDS.EXTERNAL_PUBLICATION_USER, getConstants().lifeCycleExternalPublicationUser());
        ViewTextItem externalPublicationDate = new ViewTextItem(DataStructureDefinitionDS.EXTERNAL_PUBLICATION_DATE, getConstants().lifeCycleExternalPublicationDate());
        versionResponsibilityEditionForm.setFields(productionValidationUser, productionValidationDate, diffusionValidationUser, diffusionValidationDate, internalPublicationUser,
                internalPublicationDate, externalPublicationUser, externalPublicationDate);

        // Annotations
        annotationsEditionPanel = new AnnotationsPanel(false);

        mainFormLayout.addEditionCanvas(identifiersEditionForm);
        mainFormLayout.addEditionCanvas(generalEditionForm);
        mainFormLayout.addEditionCanvas(statusForm);
        mainFormLayout.addEditionCanvas(versionResponsibilityEditionForm);
        mainFormLayout.addEditionCanvas(annotationsEditionPanel);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setUiHandlers(DsdGeneralTabUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    @Override
    public void setDsd(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        this.dataStructureDefinitionMetamacDto = dataStructureDefinitionMetamacDto;

        mainFormLayout.updatePublishSection(dataStructureDefinitionMetamacDto.getProcStatus());
        mainFormLayout.setViewMode();

        setDsdViewMode(dataStructureDefinitionMetamacDto);
        setDsdEditionMode(dataStructureDefinitionMetamacDto);

        // Clear errors
        identifiersEditionForm.clearErrors(true);
        generalEditionForm.clearErrors(true);
    }

    private void setDsdViewMode(DataStructureDefinitionMetamacDto dsd) {
        // Identifiers form
        identifiersForm.setValue(DataStructureDefinitionDS.CODE, dsd.getCode());
        identifiersForm.setValue(DataStructureDefinitionDS.URI, dsd.getUri());
        identifiersForm.setValue(DataStructureDefinitionDS.URN, dsd.getUrn());
        identifiersForm.setValue(DataStructureDefinitionDS.VERSION_LOGIC, dsd.getVersionLogic());

        // General form
        generalForm.setValue(DataStructureDefinitionDS.NAME, RecordUtils.getInternationalStringRecord(dsd.getName()));
        generalForm.setValue(DataStructureDefinitionDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(dsd.getDescription()));
        generalForm.setValue(DataStructureDefinitionDS.AGENCY, dsd.getMaintainer() != null ? dsd.getMaintainer().getCode() : StringUtils.EMPTY); // TODO AGENCY

        // Status form
        statusForm.setValue(DataStructureDefinitionDS.FINAL, BooleanUtils.isTrue(dsd.getFinalLogic()) ? MetamacSrmWeb.getConstants().yes() : MetamacSrmWeb.getConstants().no());
        statusForm.setValue(DataStructureDefinitionDS.VALID_FROM, dsd.getValidFrom());
        statusForm.setValue(DataStructureDefinitionDS.VALID_TO, dsd.getValidTo());

        // Version responsibility
        versionResponsibilityForm.setValue(DataStructureDefinitionDS.PRODUCTION_VALIDATION_USER, dsd.getProductionValidationUser());
        versionResponsibilityForm.setValue(DataStructureDefinitionDS.PRODUCTION_VALIDATION_DATE, dsd.getProductionValidationDate());
        versionResponsibilityForm.setValue(DataStructureDefinitionDS.DIFFUSION_VALIDATION_USER, dsd.getDiffusionValidationUser());
        versionResponsibilityForm.setValue(DataStructureDefinitionDS.DIFFUSION_VALIDATION_DATE, DateUtils.getFormattedDate(dsd.getDiffusionValidationDate()));
        versionResponsibilityForm.setValue(DataStructureDefinitionDS.INTERNAL_PUBLICATION_USER, dsd.getInternalPublicationUser());
        versionResponsibilityForm.setValue(DataStructureDefinitionDS.INTERNAL_PUBLICATION_DATE, DateUtils.getFormattedDate(dsd.getInternalPublicationDate()));
        versionResponsibilityForm.setValue(DataStructureDefinitionDS.EXTERNAL_PUBLICATION_USER, dsd.getExternalPublicationUser());
        versionResponsibilityForm.setValue(DataStructureDefinitionDS.EXTERNAL_PUBLICATION_DATE, DateUtils.getFormattedDate(dsd.getExternalPublicationDate()));

        // Annotations
        annotationsPanel.setAnnotations(dsd.getAnnotations());
    }

    private void setDsdEditionMode(DataStructureDefinitionMetamacDto dsd) {
        // Identifiers form
        identifiersEditionForm.setValue(DataStructureDefinitionDS.CODE, dsd.getCode());
        identifiersEditionForm.setValue(DataStructureDefinitionDS.CODE_VIEW, dsd.getCode());
        identifiersEditionForm.setValue(DataStructureDefinitionDS.URI, dsd.getUri());
        identifiersEditionForm.setValue(DataStructureDefinitionDS.URN, dsd.getUrn());
        identifiersEditionForm.setValue(DataStructureDefinitionDS.VERSION_LOGIC, dsd.getVersionLogic());

        // General form
        generalEditionForm.setValue(DataStructureDefinitionDS.NAME, RecordUtils.getInternationalStringRecord(dsd.getName()));
        generalEditionForm.setValue(DataStructureDefinitionDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(dsd.getDescription()));
        generalEditionForm.setValue(DataStructureDefinitionDS.AGENCY, dsd.getMaintainer() != null ? dsd.getMaintainer().getCode() : StringUtils.EMPTY); // TODO AGENCY

        // Status form
        statusEditionForm.setValue(DataStructureDefinitionDS.FINAL, BooleanUtils.isTrue(dsd.getFinalLogic()) ? MetamacSrmWeb.getConstants().yes() : MetamacSrmWeb.getConstants().no());
        statusEditionForm.setValue(DataStructureDefinitionDS.VALID_FROM, dsd.getValidFrom());
        statusEditionForm.setValue(DataStructureDefinitionDS.VALID_TO, dsd.getValidTo());

        // Version responsibility
        versionResponsibilityEditionForm.setValue(ConceptSchemeDS.PRODUCTION_VALIDATION_USER, dsd.getProductionValidationUser());
        versionResponsibilityEditionForm.setValue(DataStructureDefinitionDS.PRODUCTION_VALIDATION_DATE, dsd.getProductionValidationDate());
        versionResponsibilityEditionForm.setValue(DataStructureDefinitionDS.DIFFUSION_VALIDATION_USER, dsd.getDiffusionValidationUser());
        versionResponsibilityEditionForm.setValue(DataStructureDefinitionDS.DIFFUSION_VALIDATION_DATE, DateUtils.getFormattedDate(dsd.getDiffusionValidationDate()));
        versionResponsibilityEditionForm.setValue(DataStructureDefinitionDS.INTERNAL_PUBLICATION_USER, dsd.getInternalPublicationUser());
        versionResponsibilityEditionForm.setValue(DataStructureDefinitionDS.INTERNAL_PUBLICATION_DATE, DateUtils.getFormattedDate(dsd.getInternalPublicationDate()));
        versionResponsibilityEditionForm.setValue(DataStructureDefinitionDS.EXTERNAL_PUBLICATION_USER, dsd.getExternalPublicationUser());
        versionResponsibilityEditionForm.setValue(DataStructureDefinitionDS.EXTERNAL_PUBLICATION_DATE, DateUtils.getFormattedDate(dsd.getExternalPublicationDate()));

        // Annotations
        annotationsEditionPanel.setAnnotations(dsd.getAnnotations());
    }

    @Override
    public HasClickHandlers getSave() {
        return mainFormLayout.getSave();
    }

    @Override
    public DataStructureDefinitionMetamacDto getDataStructureDefinitionDto() {
        // Identifiers form
        dataStructureDefinitionMetamacDto.setCode(identifiersEditionForm.getValueAsString(DataStructureDefinitionDS.CODE));

        // General form
        dataStructureDefinitionMetamacDto.setName((InternationalStringDto) generalEditionForm.getValue(DataStructureDefinitionDS.NAME));
        dataStructureDefinitionMetamacDto.setDescription((InternationalStringDto) generalEditionForm.getValue(DataStructureDefinitionDS.DESCRIPTION));

        // Annotations
        dataStructureDefinitionMetamacDto.getAnnotations().clear();
        dataStructureDefinitionMetamacDto.getAnnotations().addAll(annotationsEditionPanel.getAnnotations());
        return dataStructureDefinitionMetamacDto;
    }

    @Override
    public void onDsdSaved(DataStructureDefinitionMetamacDto dsd) {
        setDsd(dsd);
        mainFormLayout.setViewMode();
    }

    public void setEditionMode() {
        mainFormLayout.setEditionMode();
    }

}
