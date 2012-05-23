package org.siemac.metamac.srm.web.dsd.view;

import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionDto;
import org.siemac.metamac.srm.web.client.MetamacInternalWeb;
import org.siemac.metamac.srm.web.dsd.presenter.DsdGeneralTabPresenter;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdGeneralTabUiHandlers;
import org.siemac.metamac.srm.web.dsd.widgets.AnnotationsPanel;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
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

    private VLayout                     panel;

    private InternationalMainFormLayout mainFormLayout;

    private AnnotationsPanel            viewAnnotationsPanel;
    private AnnotationsPanel            editionAnnotationsPanel;

    // VIEW FORM

    private ViewTextItem                staticIdLogic;
    private ViewTextItem                staticVersion;
    private ViewTextItem                staticAgency;
    private ViewMultiLanguageTextItem   staticNameItem;
    private ViewMultiLanguageTextItem   staticDescriptionItem;
    private ViewTextItem                staticUriItem;
    private ViewTextItem                staticUrnItem;
    private ViewTextItem                staticFinalItem;
    private ViewTextItem                staticStartDateItem;
    private ViewTextItem                staticEndDateItem;
    private ViewTextItem                staticVersionEdit;
    private ViewTextItem                staticAgencyEdit;
    private ViewTextItem                staticUriItemEdit;
    private ViewTextItem                staticUrnItemEdit;
    private ViewTextItem                staticFinalItemEdit;
    private ViewTextItem                staticStartDateItemEdit;
    private ViewTextItem                staticEndDateItemEdit;
    private GroupDynamicForm            generalForm;

    // EDITION FORM

    private GroupDynamicForm            identifiersEditionForm;
    private GroupDynamicForm            generalEditionForm;
    private RequiredTextItem            idLogicItem;
    private MultiLanguageTextItem       nameItem;
    private MultiLanguageTextItem       descriptionItem;

    // private InternationalAnnotationsPanel annotationsPanel;

    private boolean                     translationsShowed;

    @Inject
    public DsdGeneralTabViewImpl() {
        super();
        panel = new VLayout();

        mainFormLayout = new InternationalMainFormLayout();
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
        staticIdLogic = new ViewTextItem("id-logic-dsd-view", MetamacInternalWeb.getConstants().dsdIdentifier());

        // Name
        staticNameItem = new ViewMultiLanguageTextItem("name-dsd-view", MetamacInternalWeb.getConstants().dsdName());
        staticDescriptionItem = new ViewMultiLanguageTextItem("desc-dsd-view", MetamacInternalWeb.getConstants().dsdDescription());
        staticVersion = new ViewTextItem("version-dsd-view", MetamacInternalWeb.getConstants().dsdVersion());
        staticAgency = new ViewTextItem("agency-dsd-view", MetamacInternalWeb.getConstants().dsdAgency());
        staticUriItem = new ViewTextItem("uri-dsd-view", MetamacInternalWeb.getConstants().dsdUri());
        staticUrnItem = new ViewTextItem("urn-dsd-view", MetamacInternalWeb.getConstants().dsdUrn());
        staticFinalItem = new ViewTextItem("final-dsd-view", MetamacInternalWeb.getConstants().dsdFinalStructure());
        staticStartDateItem = new ViewTextItem("start-date-dsd-view", MetamacInternalWeb.getConstants().dsdStartDate());
        staticEndDateItem = new ViewTextItem("end-date-dsd-view", MetamacInternalWeb.getConstants().dsdEndDate());

        // Identifiers Form
        GroupDynamicForm identifiersForm = new GroupDynamicForm(MetamacInternalWeb.getConstants().dsdIdentifiers());
        identifiersForm.setFields(staticIdLogic, staticUriItem, staticUrnItem, staticVersion);

        // General Form
        generalForm = new GroupDynamicForm(MetamacInternalWeb.getConstants().dsdDetails());
        generalForm.setFields(staticNameItem, staticDescriptionItem, staticAgency);

        // Status Form
        GroupDynamicForm statusForm = new GroupDynamicForm(MetamacInternalWeb.getConstants().dsdStatus());
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
        idLogicItem = new RequiredTextItem("idLogicDsd", MetamacInternalWeb.getConstants().dsdIdLogic());
        idLogicItem.setValidators(CommonWebUtils.getSemanticIdentifierCustomValidator());

        // Name
        nameItem = new MultiLanguageTextItem("nameDsd", MetamacInternalWeb.getConstants().dsdName());
        nameItem.setRequired(true);

        // Description
        descriptionItem = new MultiLanguageTextItem("descDsd", MetamacInternalWeb.getConstants().dsdDescription());

        staticVersionEdit = new ViewTextItem("version-dsd-edit", MetamacInternalWeb.getConstants().dsdVersion());
        staticAgencyEdit = new ViewTextItem("agency-dsd-edit", MetamacInternalWeb.getConstants().dsdAgency());
        staticUriItemEdit = new ViewTextItem("uri-dsd-edit", MetamacInternalWeb.getConstants().dsdUri());
        staticUrnItemEdit = new ViewTextItem("urn-dsd-edit", MetamacInternalWeb.getConstants().dsdUrn());
        staticFinalItemEdit = new ViewTextItem("final-dsd-edit", MetamacInternalWeb.getConstants().dsdFinalStructure());
        staticStartDateItemEdit = new ViewTextItem("start-date-dsd-edit", MetamacInternalWeb.getConstants().dsdStartDate());
        staticEndDateItemEdit = new ViewTextItem("end-date-dsd-edit", MetamacInternalWeb.getConstants().dsdEndDate());

        // Identifiers Form
        identifiersEditionForm = new GroupDynamicForm(MetamacInternalWeb.getConstants().dsdIdentifiers());
        identifiersEditionForm.setFields(idLogicItem, staticUriItemEdit, staticUrnItemEdit, staticVersionEdit);

        // General Form
        generalEditionForm = new GroupDynamicForm(MetamacInternalWeb.getConstants().dsdDetails());
        generalEditionForm.setFields(nameItem, descriptionItem, staticAgencyEdit);

        // Status Form
        GroupDynamicForm statusForm = new GroupDynamicForm(MetamacInternalWeb.getConstants().dsdStatus());
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
    public void setDsd(DataStructureDefinitionDto dsd) {
        mainFormLayout.setViewMode();

        setDsdViewMode(dsd);
        setDsdEditionMode(dsd);

        // Annotations
        // annotationsPanel.setAnnotations(dsd.getAnnotations());

        // Clear errors
        identifiersEditionForm.clearErrors(true);
        generalEditionForm.clearErrors(true);
    }

    private void setDsdViewMode(DataStructureDefinitionDto dsd) {
        staticIdLogic.setValue(dsd.getIdLogic());
        staticUriItem.setValue(dsd.getUri());
        staticUrnItem.setValue(dsd.getUrn());
        staticVersion.setValue(dsd.getVersionLogic());
        staticNameItem.setValue(dsd.getName());
        staticDescriptionItem.setValue(dsd.getDescription());
        staticAgency.setValue(dsd.getMaintainer() != null ? dsd.getMaintainer().getCodeId() : "");
        staticFinalItem.setValue((dsd.getFinalLogic() != null && dsd.getFinalLogic()) ? MetamacInternalWeb.getConstants().yes() : MetamacInternalWeb.getConstants().no());
        staticStartDateItem.setValue(dsd.getValidFrom());
        staticEndDateItem.setValue(dsd.getValidTo());
        // Annotations
        viewAnnotationsPanel.setAnnotations(dsd.getAnnotations());
    }

    private void setDsdEditionMode(DataStructureDefinitionDto dsd) {
        idLogicItem.setValue(dsd.getIdLogic());
        staticUriItemEdit.setValue(dsd.getUri());
        staticUrnItemEdit.setValue(dsd.getUrn());
        staticVersionEdit.setValue(dsd.getVersionLogic());
        nameItem.setValue(dsd.getName());
        descriptionItem.setValue(dsd.getDescription());
        staticAgencyEdit.setValue(dsd.getMaintainer() != null ? dsd.getMaintainer().getCodeId() : "");
        staticFinalItemEdit.setValue((dsd.getFinalLogic() != null && dsd.getFinalLogic()) ? MetamacInternalWeb.getConstants().yes() : MetamacInternalWeb.getConstants().no());
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
    public DataStructureDefinitionDto getDataStructureDefinitionDto(DataStructureDefinitionDto dsd) {
        dsd.setIdLogic(idLogicItem.getValueAsString());
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
    public void onDsdSaved(DataStructureDefinitionDto dsd) {
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
