package org.siemac.metamac.srm.web.dsd.view;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.dsd.presenter.DsdGeneralTabPresenter;
import org.siemac.metamac.srm.web.dsd.utils.DsdClientSecurityUtils;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdGeneralTabUiHandlers;
import org.siemac.metamac.srm.web.dsd.widgets.AnnotationsPanel;
import org.siemac.metamac.srm.web.dsd.widgets.DsdMainFormLayout;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
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

    private VLayout                   panel;

    private DsdMainFormLayout         mainFormLayout;

    private AnnotationsPanel          viewAnnotationsPanel;
    private AnnotationsPanel          editionAnnotationsPanel;

    // VIEW FORM

    private ViewTextItem              staticIdLogic;
    private ViewTextItem              staticVersion;
    private ViewTextItem              staticAgency;
    private ViewMultiLanguageTextItem staticNameItem;
    private ViewMultiLanguageTextItem staticDescriptionItem;
    private ViewTextItem              staticUriItem;
    private ViewTextItem              staticUrnItem;
    private ViewTextItem              staticFinalItem;
    private ViewTextItem              staticStartDateItem;
    private ViewTextItem              staticEndDateItem;
    private ViewTextItem              staticVersionEdit;
    private ViewTextItem              staticAgencyEdit;
    private ViewTextItem              staticUriItemEdit;
    private ViewTextItem              staticUrnItemEdit;
    private ViewTextItem              staticFinalItemEdit;
    private ViewTextItem              staticStartDateItemEdit;
    private ViewTextItem              staticEndDateItemEdit;
    private GroupDynamicForm          generalForm;

    // EDITION FORM

    private GroupDynamicForm          identifiersEditionForm;
    private GroupDynamicForm          generalEditionForm;
    private RequiredTextItem          codeItem;
    private MultiLanguageTextItem     nameItem;
    private MultiLanguageTextItem     descriptionItem;

    // private InternationalAnnotationsPanel annotationsPanel;

    private boolean                   translationsShowed;

    @Inject
    public DsdGeneralTabViewImpl() {
        super();
        panel = new VLayout();

        mainFormLayout = new DsdMainFormLayout(DsdClientSecurityUtils.canUpdateDsd());
        mainFormLayout.getTranslateToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                setTranslationsShowed(mainFormLayout.getTranslateToolStripButton().isSelected());
            }
        });

        createViewForm();
        createEditionForm();

        panel.addMember(mainFormLayout);

        // annotationsPanel = new InternationalAnnotationsPanel();
        // panel.addMember(annotationsPanel);
    }

    /**
     * Creates and returns the view layout
     * 
     * @return
     */
    private void createViewForm() {
        staticIdLogic = new ViewTextItem("id-logic-dsd-view", MetamacSrmWeb.getConstants().dsdIdentifier());

        // Name
        staticNameItem = new ViewMultiLanguageTextItem("name-dsd-view", MetamacSrmWeb.getConstants().dsdName());
        staticDescriptionItem = new ViewMultiLanguageTextItem("desc-dsd-view", MetamacSrmWeb.getConstants().dsdDescription());
        staticVersion = new ViewTextItem("version-dsd-view", MetamacSrmWeb.getConstants().dsdVersion());
        staticAgency = new ViewTextItem("agency-dsd-view", MetamacSrmWeb.getConstants().dsdAgency());
        staticUriItem = new ViewTextItem("uri-dsd-view", MetamacSrmWeb.getConstants().dsdUri());
        staticUrnItem = new ViewTextItem("urn-dsd-view", MetamacSrmWeb.getConstants().dsdUrn());
        staticFinalItem = new ViewTextItem("final-dsd-view", MetamacSrmWeb.getConstants().dsdFinalStructure());
        staticStartDateItem = new ViewTextItem("start-date-dsd-view", MetamacSrmWeb.getConstants().dsdValidFrom());
        staticEndDateItem = new ViewTextItem("end-date-dsd-view", MetamacSrmWeb.getConstants().dsdValidTo());

        // Identifiers Form
        GroupDynamicForm identifiersForm = new GroupDynamicForm(MetamacSrmWeb.getConstants().dsdIdentifiers());
        identifiersForm.setFields(staticIdLogic, staticUriItem, staticUrnItem, staticVersion);

        // General Form
        generalForm = new GroupDynamicForm(MetamacSrmWeb.getConstants().dsdDetails());
        generalForm.setFields(staticNameItem, staticDescriptionItem, staticAgency);

        // Status Form
        GroupDynamicForm statusForm = new GroupDynamicForm(MetamacSrmWeb.getConstants().dsdStatus());
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
        codeItem = new RequiredTextItem("idLogicDsd", MetamacSrmWeb.getConstants().dsdCode());
        codeItem.setValidators(CommonWebUtils.getSemanticIdentifierCustomValidator());

        // Name
        nameItem = new MultiLanguageTextItem("nameDsd", MetamacSrmWeb.getConstants().dsdName());
        nameItem.setRequired(true);

        // Description
        descriptionItem = new MultiLanguageTextItem("descDsd", MetamacSrmWeb.getConstants().dsdDescription());

        staticVersionEdit = new ViewTextItem("version-dsd-edit", MetamacSrmWeb.getConstants().dsdVersion());
        staticAgencyEdit = new ViewTextItem("agency-dsd-edit", MetamacSrmWeb.getConstants().dsdAgency());
        staticUriItemEdit = new ViewTextItem("uri-dsd-edit", MetamacSrmWeb.getConstants().dsdUri());
        staticUrnItemEdit = new ViewTextItem("urn-dsd-edit", MetamacSrmWeb.getConstants().dsdUrn());
        staticFinalItemEdit = new ViewTextItem("final-dsd-edit", MetamacSrmWeb.getConstants().dsdFinalStructure());
        staticStartDateItemEdit = new ViewTextItem("start-date-dsd-edit", MetamacSrmWeb.getConstants().dsdValidFrom());
        staticEndDateItemEdit = new ViewTextItem("end-date-dsd-edit", MetamacSrmWeb.getConstants().dsdValidTo());

        // Identifiers Form
        identifiersEditionForm = new GroupDynamicForm(MetamacSrmWeb.getConstants().dsdIdentifiers());
        identifiersEditionForm.setFields(codeItem, staticUriItemEdit, staticUrnItemEdit, staticVersionEdit);

        // General Form
        generalEditionForm = new GroupDynamicForm(MetamacSrmWeb.getConstants().dsdDetails());
        generalEditionForm.setFields(nameItem, descriptionItem, staticAgencyEdit);

        // Status Form
        GroupDynamicForm statusForm = new GroupDynamicForm(MetamacSrmWeb.getConstants().dsdStatus());
        statusForm.setFields(staticFinalItemEdit, staticStartDateItemEdit, staticEndDateItemEdit);

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
    public void setDsd(DataStructureDefinitionMetamacDto dsd) {
        mainFormLayout.setViewMode();
        // TODO mainFormLayout.updatePublishSection(dsd.getProcStatus());

        setDsdViewMode(dsd);
        setDsdEditionMode(dsd);

        // Annotations
        // annotationsPanel.setAnnotations(dsd.getAnnotations());

        // Clear errors
        identifiersEditionForm.clearErrors(true);
        generalEditionForm.clearErrors(true);
    }

    private void setDsdViewMode(DataStructureDefinitionMetamacDto dsd) {
        staticIdLogic.setValue(dsd.getCode());
        staticUriItem.setValue(dsd.getUri());
        staticUrnItem.setValue(dsd.getUrn());
        staticVersion.setValue(dsd.getVersionLogic());
        staticNameItem.setValue(dsd.getName());
        staticDescriptionItem.setValue(dsd.getDescription());
        staticAgency.setValue(dsd.getMaintainer() != null ? dsd.getMaintainer().getCode() : "");
        staticFinalItem.setValue((dsd.getFinalLogic() != null && dsd.getFinalLogic()) ? MetamacSrmWeb.getConstants().yes() : MetamacSrmWeb.getConstants().no());
        staticStartDateItem.setValue(dsd.getValidFrom());
        staticEndDateItem.setValue(dsd.getValidTo());
        // Annotations
        viewAnnotationsPanel.setAnnotations(dsd.getAnnotations());
    }

    private void setDsdEditionMode(DataStructureDefinitionMetamacDto dsd) {
        codeItem.setValue(dsd.getCode());
        staticUriItemEdit.setValue(dsd.getUri());
        staticUrnItemEdit.setValue(dsd.getUrn());
        staticVersionEdit.setValue(dsd.getVersionLogic());
        nameItem.setValue(dsd.getName());
        descriptionItem.setValue(dsd.getDescription());
        staticAgencyEdit.setValue(dsd.getMaintainer() != null ? dsd.getMaintainer().getCode() : "");
        staticFinalItemEdit.setValue((dsd.getFinalLogic() != null && dsd.getFinalLogic()) ? MetamacSrmWeb.getConstants().yes() : MetamacSrmWeb.getConstants().no());
        staticStartDateItemEdit.setValue(dsd.getValidFrom());
        staticEndDateItemEdit.setValue(dsd.getValidTo());
        // Annotations
        editionAnnotationsPanel.setAnnotations(dsd.getAnnotations());
    }

    @Override
    public boolean validate() {
        return identifiersEditionForm.validate() && generalEditionForm.validate();
    }

    @Override
    public HasClickHandlers getSave() {
        return mainFormLayout.getSave();
    }

    @Override
    public DataStructureDefinitionMetamacDto getDataStructureDefinitionDto(DataStructureDefinitionMetamacDto dsd) {
        dsd.setCode(codeItem.getValueAsString());
        dsd.setName(nameItem.getValue());
        dsd.setDescription(descriptionItem.getValue());

        // Annotations
        dsd.getAnnotations().clear();
        dsd.getAnnotations().addAll(editionAnnotationsPanel.getAnnotations());

        // Annotations
        // dsd.getAnnotations().clear();
        // dsd.getAnnotations().addAll(annotationsPanel.getAnnotations());
        return dsd;
    }

    @Override
    public void onDsdSaved(DataStructureDefinitionMetamacDto dsd) {
        setDsd(dsd);
        mainFormLayout.setViewMode();
    }

    public boolean isTranslationsShowed() {
        return translationsShowed;
    }

    public void setTranslationsShowed(boolean translationsShowed) {
        this.translationsShowed = translationsShowed;
        viewAnnotationsPanel.setTranslationsShowed(translationsShowed);
        editionAnnotationsPanel.setTranslationsShowed(translationsShowed);

        // Set translationsShowed value to international fields
        generalForm.setTranslationsShowed(translationsShowed);
        generalEditionForm.setTranslationsShowed(translationsShowed);
    }

}
