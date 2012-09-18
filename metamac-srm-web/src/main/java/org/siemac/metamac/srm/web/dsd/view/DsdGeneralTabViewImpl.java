package org.siemac.metamac.srm.web.dsd.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.BooleanUtils;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ItemSchemeMetamacProcStatusEnum;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.widgets.AnnotationsPanel;
import org.siemac.metamac.srm.web.client.widgets.VersionWindow;
import org.siemac.metamac.srm.web.dsd.model.ds.DataStructureDefinitionDS;
import org.siemac.metamac.srm.web.dsd.presenter.DsdGeneralTabPresenter;
import org.siemac.metamac.srm.web.dsd.utils.DsdClientSecurityUtils;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdGeneralTabUiHandlers;
import org.siemac.metamac.srm.web.dsd.widgets.DsdMainFormLayout;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
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
import com.smartgwt.client.widgets.layout.VLayout;

public class DsdGeneralTabViewImpl extends ViewWithUiHandlers<DsdGeneralTabUiHandlers> implements DsdGeneralTabPresenter.DsdGeneralTabView {

    private DsdGeneralTabUiHandlers           uiHandlers;

    private VLayout                           panel;

    private DsdMainFormLayout                 mainFormLayout;

    private AnnotationsPanel                  viewAnnotationsPanel;
    private AnnotationsPanel                  editionAnnotationsPanel;

    // VIEW FORM

    private GroupDynamicForm                  identifiersForm;
    private GroupDynamicForm                  generalForm;
    private GroupDynamicForm                  statusForm;

    // EDITION FORM

    private GroupDynamicForm                  identifiersEditionForm;
    private GroupDynamicForm                  generalEditionForm;
    private GroupDynamicForm                  statusEditionForm;

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

                viewAnnotationsPanel.setTranslationsShowed(translationsShowed);
                editionAnnotationsPanel.setTranslationsShowed(translationsShowed);
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

        // Annotations
        viewAnnotationsPanel = new AnnotationsPanel(true);

        mainFormLayout.addViewCanvas(identifiersForm);
        mainFormLayout.addViewCanvas(generalForm);
        mainFormLayout.addViewCanvas(statusForm);
        mainFormLayout.addViewCanvas(viewAnnotationsPanel);
    }

    /**
     * Creates and returns the edition layout
     * 
     * @return
     */
    private void createEditionForm() {
        // Identifiers Form
        identifiersEditionForm = new GroupDynamicForm(MetamacSrmWeb.getConstants().dsdIdentifiers());
        RequiredTextItem codeItem = new RequiredTextItem(DataStructureDefinitionDS.CODE, MetamacSrmWeb.getConstants().dsdCode());
        codeItem.setValidators(CommonWebUtils.getSemanticIdentifierCustomValidator());
        ViewTextItem staticUriItemEdit = new ViewTextItem(DataStructureDefinitionDS.URI, MetamacSrmWeb.getConstants().dsdUri());
        ViewTextItem staticUrnItemEdit = new ViewTextItem(DataStructureDefinitionDS.URN, MetamacSrmWeb.getConstants().dsdUrn());
        ViewTextItem staticVersionEdit = new ViewTextItem(DataStructureDefinitionDS.VERSION_LOGIC, MetamacSrmWeb.getConstants().dsdVersion());
        identifiersEditionForm.setFields(codeItem, staticUriItemEdit, staticUrnItemEdit, staticVersionEdit);

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

        // Annotations
        editionAnnotationsPanel = new AnnotationsPanel(false);

        mainFormLayout.addEditionCanvas(identifiersEditionForm);
        mainFormLayout.addEditionCanvas(generalEditionForm);
        mainFormLayout.addEditionCanvas(statusForm);
        mainFormLayout.addEditionCanvas(editionAnnotationsPanel);
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

        // Annotations
        viewAnnotationsPanel.setAnnotations(dsd.getAnnotations());
    }

    private void setDsdEditionMode(DataStructureDefinitionMetamacDto dsd) {
        // Identifiers form
        identifiersEditionForm.setValue(DataStructureDefinitionDS.CODE, dsd.getCode());
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

        // Annotations
        editionAnnotationsPanel.setAnnotations(dsd.getAnnotations());
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
        dataStructureDefinitionMetamacDto.getAnnotations().addAll(editionAnnotationsPanel.getAnnotations());
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
