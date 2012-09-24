package org.siemac.metamac.srm.web.dsd.view;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.representation.widgets.StaticFacetForm;
import org.siemac.metamac.srm.web.client.utils.FacetFormUtils;
import org.siemac.metamac.srm.web.client.widgets.AnnotationsPanel;
import org.siemac.metamac.srm.web.dsd.model.record.AttributeRecord;
import org.siemac.metamac.srm.web.dsd.presenter.DsdAttributesTabPresenter;
import org.siemac.metamac.srm.web.dsd.utils.CommonUtils;
import org.siemac.metamac.srm.web.dsd.utils.DsdClientSecurityUtils;
import org.siemac.metamac.srm.web.dsd.utils.RecordUtils;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdAttributesTabUiHandlers;
import org.siemac.metamac.srm.web.dsd.widgets.DsdFacetForm;
import org.siemac.metamac.srm.web.dsd.widgets.RoleSelectItem;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.utils.ExternalItemUtils;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ExternalSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataAttributeDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DescriptorDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DimensionComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.FacetDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.RelationshipDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.RepresentationDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeComponent;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeDataAttribute;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeRelathionship;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeRepresentationEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.UsageStatus;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.HasClickHandlers;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.HasChangeHandlers;
import com.smartgwt.client.widgets.form.validator.RequiredIfFunction;
import com.smartgwt.client.widgets.form.validator.RequiredIfValidator;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.HasSelectionChangedHandlers;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class DsdAttributesTabViewImpl extends ViewWithUiHandlers<DsdAttributesTabUiHandlers> implements DsdAttributesTabPresenter.DsdAttributesTabView {

    private ProcStatusEnum              procStatus;

    private DataAttributeDto            dataAttributeDto;
    private List<ExternalItemDto>       concepts;
    private List<ExternalItemDto>       codeLists;
    private List<DimensionComponentDto> dimensionComponentDtos;
    private List<DescriptorDto>         descriptorDtos;                              // Group Keys

    private VLayout                     panel;
    private VLayout                     selectedComponentLayout;
    private ListGrid                    attributesGrid;

    private InternationalMainFormLayout mainFormLayout;
    // private VLayout viewLayout;
    // private VLayout editionLayout;

    private AnnotationsPanel            viewAnnotationsPanel;
    private AnnotationsPanel            editionAnnotationsPanel;

    // VIEW FORM

    private ViewTextItem                staticIdLogic;
    private ViewTextItem                staticConceptItem;
    private ViewTextItem                staticRoleItem;
    private ViewTextItem                staticAssignmentStatusItem;
    // Relation
    private ViewTextItem                staticRelationType;
    private ViewTextItem                staticGroupKeysForDimensionRelationshipItem;
    private ViewTextItem                staticDimensionsForDimensionRelationshipItem;
    private ViewTextItem                staticGroupKeyFormForGroupRelationship;
    // Representation
    private ViewTextItem                staticRepresentationTypeItem;
    private ViewTextItem                staticCodeListItem;
    private StaticFacetForm             staticFacetForm;

    // EDITION FORM

    private GroupDynamicForm            form;
    private RequiredTextItem            idLogic;
    private ExternalSelectItem          conceptItem;
    private RoleSelectItem              roleItem;
    private RequiredSelectItem          assignmentStatusItem;
    // Relation
    private RequiredSelectItem          relationType;
    private CustomSelectItem            groupKeysForDimensionRelationshipItem;
    private RequiredSelectItem          dimensionsForDimensionRelationshipItem;      // Required if relationType == DIMENSION_RELATIONSHIP
    private RequiredSelectItem          groupKeyFormForGroupRelationship;            // Required if relationType == GROUP_RELATIONSHIP
    // Representation
    private CustomSelectItem            representationTypeItem;
    private CustomSelectItem            codeListItem;
    private DsdFacetForm                facetForm = null;

    private ToolStripButton             newToolStripButton;
    // private ToolStripButton editToolStripButton;
    // private ToolStripButton saveToolStripButton;
    // private ToolStripButton cancelToolStripButton;
    private ToolStripButton             deleteToolStripButton;

    private DeleteConfirmationWindow    importDsdWindow;

    @Inject
    public DsdAttributesTabViewImpl() {
        super();
        panel = new VLayout();

        // ··················
        // List of attributes
        // ··················

        // ToolStrip

        newToolStripButton = new ToolStripButton(MetamacSrmWeb.getConstants().actionNew(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.newListGrid().getURL());
        newToolStripButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                DataAttributeDto dataAttributeDto = new DataAttributeDto();
                dataAttributeDto.setTypeDataAttribute(TypeDataAttribute.DATA_ATTRIBUTE); // All the attributes are DATA_ATTRIBUTE
                selectAttribute(dataAttributeDto);
            }
        });

        importDsdWindow = new DeleteConfirmationWindow(MetamacSrmWeb.getConstants().dsdDeleteConfirmationTitle(), MetamacSrmWeb.getConstants().dsdAttributeDeleteConfirmation());
        importDsdWindow.setVisibility(Visibility.HIDDEN);

        deleteToolStripButton = new ToolStripButton(MetamacSrmWeb.getConstants().actionDelete(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.deleteListGrid().getURL());
        deleteToolStripButton.setVisibility(Visibility.HIDDEN);
        deleteToolStripButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                importDsdWindow.show();
            }
        });

        ToolStrip attributeGridToolStrip = new ToolStrip();
        attributeGridToolStrip.setWidth100();
        attributeGridToolStrip.addButton(newToolStripButton);
        attributeGridToolStrip.addSeparator();
        // attributeGridToolStrip.addButton(editToolStripButton);
        attributeGridToolStrip.addButton(deleteToolStripButton);

        // Grid

        attributesGrid = new ListGrid();
        attributesGrid.setWidth100();
        attributesGrid.setHeight(150);
        attributesGrid.setSelectionType(SelectionStyle.SIMPLE);
        attributesGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
        ListGridField codeField = new ListGridField(AttributeRecord.CODE, MetamacSrmWeb.getConstants().dsdAttributeId());
        ListGridField assigmentField = new ListGridField(AttributeRecord.ASSIGNMENT, MetamacSrmWeb.getConstants().dsdAttributeAssignmentStatus());
        ListGridField attributeConceptField = new ListGridField(AttributeRecord.CONCEPT, MetamacSrmWeb.getConstants().dsdAttributeConcept());
        attributesGrid.setFields(codeField, assigmentField, attributeConceptField);
        // ToolTip
        codeField.setShowHover(true);
        codeField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                AttributeRecord attributeRecord = (AttributeRecord) record;
                return attributeRecord.getCode();
            }
        });
        assigmentField.setShowHover(true);
        assigmentField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                AttributeRecord attributeRecord = (AttributeRecord) record;
                return attributeRecord.getAssigment();
            }
        });
        attributeConceptField.setShowHover(true);
        attributeConceptField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                AttributeRecord attributeRecord = (AttributeRecord) record;
                return attributeRecord.getConcept();
            }
        });
        // Show attribute details when record clicked
        attributesGrid.addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (attributesGrid.getSelectedRecords() != null && attributesGrid.getSelectedRecords().length == 1) {
                    AttributeRecord record = (AttributeRecord) attributesGrid.getSelectedRecord();
                    DataAttributeDto dataAttributeDto = record.getDataAttributeDto();
                    selectAttribute(dataAttributeDto);
                } else {
                    // No record selected
                    deselectAttribute();
                    if (attributesGrid.getSelectedRecords().length > 1) {
                        // Delete more than one dimension with one click
                        showDeleteToolStripButton();
                    }
                }
            }
        });
        attributesGrid.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // CheckBox is not clicked
                    attributesGrid.deselectAllRecords();
                    attributesGrid.selectRecord(event.getRecord());
                }
            }
        });

        VLayout gridLayout = new VLayout();
        gridLayout.setAutoHeight();
        gridLayout.setMargin(10);
        gridLayout.addMember(attributeGridToolStrip);
        gridLayout.addMember(attributesGrid);

        // ··············
        // Attribute Form
        // ··············

        mainFormLayout = new InternationalMainFormLayout();
        mainFormLayout.getTranslateToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                setTranslationsShowed(mainFormLayout.getTranslateToolStripButton().isSelected());
            }
        });
        mainFormLayout.getCancelToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                // If it is a new dimension, hide mainFormLayout
                if (dataAttributeDto.getId() == null) {
                    selectedComponentLayout.hide();
                }
            }
        });

        createViewForm();
        createEditionForm();

        selectedComponentLayout = new VLayout(10);
        selectedComponentLayout.addMember(mainFormLayout);
        selectedComponentLayout.setVisibility(Visibility.HIDDEN);

        panel.addMember(gridLayout);
        panel.addMember(selectedComponentLayout);
    }

    /**
     * Creates and returns the view layout
     * 
     * @return
     */
    private void createViewForm() {
        staticIdLogic = new ViewTextItem("id-attr-view", MetamacSrmWeb.getConstants().dsdAttributeId());
        staticConceptItem = new ViewTextItem("concept-attr-view", MetamacSrmWeb.getConstants().dsdAttributeConcept());
        staticRoleItem = new ViewTextItem("role-attribute-view", MetamacSrmWeb.getConstants().dsdAttributeRole());
        staticAssignmentStatusItem = new ViewTextItem("status-attr-view", MetamacSrmWeb.getConstants().dsdAttributeAssignmentStatus());
        staticRelationType = new ViewTextItem("relate-type-attr-view", MetamacSrmWeb.getConstants().dsdAttributeRelatedWith());
        staticGroupKeysForDimensionRelationshipItem = new ViewTextItem("groups-dim-attr-view", MetamacSrmWeb.getConstants().dsdAttributeGroupKeysForDimensionRelationship());
        staticDimensionsForDimensionRelationshipItem = new ViewTextItem("dims-dim-attr-view", MetamacSrmWeb.getConstants().dsdAttributeDimensionsForDimensionRelationship());
        staticGroupKeyFormForGroupRelationship = new ViewTextItem("relate-group-group-attr-view", MetamacSrmWeb.getConstants().dsdAttributeGroupKeyFormGroupRelationship());
        staticRepresentationTypeItem = new ViewTextItem("repr-dim-view", MetamacSrmWeb.getConstants().representation());
        staticCodeListItem = new ViewTextItem("repr-enum-code-list-view", MetamacSrmWeb.getConstants().dsdCodeList());

        GroupDynamicForm staticForm = new GroupDynamicForm(MetamacSrmWeb.getConstants().dsdAttributeDetails());
        staticForm.setFields(staticIdLogic, staticAssignmentStatusItem, staticConceptItem, staticRoleItem, staticRelationType, staticGroupKeysForDimensionRelationshipItem,
                staticDimensionsForDimensionRelationshipItem, staticGroupKeyFormForGroupRelationship, staticRepresentationTypeItem, staticCodeListItem);

        staticFacetForm = new StaticFacetForm();

        // Annotations
        viewAnnotationsPanel = new AnnotationsPanel(true);

        mainFormLayout.addViewCanvas(staticForm);
        mainFormLayout.addViewCanvas(staticFacetForm);
        mainFormLayout.addViewCanvas(viewAnnotationsPanel);

        // VLayout viewLayout = new VLayout(15);
        // viewLayout.setAutoHeight();
        // viewLayout.addMember(staticForm);
        // viewLayout.addMember(staticFacetForm);
        // viewLayout.addMember(viewAnnotationsPanel);

        // return viewLayout;
    }

    /**
     * Creates and returns the edition layout
     * 
     * @return
     */
    private void createEditionForm() {

        // ····
        // Form
        // ····

        // Role

        roleItem = new RoleSelectItem("role-attribute", MetamacSrmWeb.getConstants().dsdAttributeRole());

        // Id

        idLogic = new RequiredTextItem("id-attr", MetamacSrmWeb.getConstants().dsdAttributeId());
        idLogic.setValidators(CommonWebUtils.getSemanticIdentifierCustomValidator());

        // Concept

        conceptItem = new ExternalSelectItem("concept-attr", MetamacSrmWeb.getConstants().dsdAttributeConcept());
        conceptItem.setRequired(true);

        // Assignment Status

        assignmentStatusItem = new RequiredSelectItem("status-attr", MetamacSrmWeb.getConstants().dsdAttributeAssignmentStatus());
        assignmentStatusItem.setValueMap(CommonUtils.getUsageStatusHashMap());

        // Relation

        relationType = new RequiredSelectItem("relate-type-attr", MetamacSrmWeb.getConstants().dsdAttributeRelatedWith());
        relationType.setValueMap(CommonUtils.getTypeRelathionshipHashMap());
        relationType.setRedrawOnChange(true);

        // Relation: Group keys for dimension relationship

        groupKeysForDimensionRelationshipItem = new CustomSelectItem("groups-dim-attr", MetamacSrmWeb.getConstants().dsdAttributeGroupKeysForDimensionRelationship());
        groupKeysForDimensionRelationshipItem.setMultiple(true);
        groupKeysForDimensionRelationshipItem.setPickListWidth(350);
        // Show GroupKeyForDimensionRelationship if TypeRelathionship = DIMENSION_RELATIONSHIP
        groupKeysForDimensionRelationshipItem.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (relationType.getValueAsString() != null && !relationType.getValueAsString().isEmpty()) {
                    TypeRelathionship typeRelathionship = TypeRelathionship.valueOf(relationType.getValueAsString());
                    if (TypeRelathionship.DIMENSION_RELATIONSHIP.equals(typeRelathionship)) {
                        return true;
                    }
                }
                return false;
            }
        });

        // Relation: Dimensions for dimension relationship

        dimensionsForDimensionRelationshipItem = new RequiredSelectItem("dims-dim-attr", MetamacSrmWeb.getConstants().dsdAttributeDimensionsForDimensionRelationship());
        dimensionsForDimensionRelationshipItem.setMultiple(true);
        dimensionsForDimensionRelationshipItem.setPickListWidth(350);
        // Show DimensionsForDimensionRelationship if TypeRelathionship = DIMENSION_RELATIONSHIP
        dimensionsForDimensionRelationshipItem.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (relationType.getValueAsString() != null && !relationType.getValueAsString().isEmpty()) {
                    TypeRelathionship typeRelathionship = TypeRelathionship.valueOf(relationType.getValueAsString());
                    if (TypeRelathionship.DIMENSION_RELATIONSHIP.equals(typeRelathionship)) {
                        return true;
                    }
                }
                return false;
            }
        });

        // Relation: Group Keys for group relationship

        groupKeyFormForGroupRelationship = new RequiredSelectItem("relate-group-group-attr", MetamacSrmWeb.getConstants().dsdAttributeGroupKeyFormGroupRelationship());
        groupKeyFormForGroupRelationship.setType("comboBox");
        groupKeyFormForGroupRelationship.setPickListWidth(350);
        // Show GroupKeyForGroupRelationship if TypeRelathionship = GROUP_RELATIONSHIP
        groupKeyFormForGroupRelationship.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (relationType.getValueAsString() != null && !relationType.getValueAsString().isEmpty()) {
                    TypeRelathionship typeRelathionship = TypeRelathionship.valueOf(relationType.getValueAsString());
                    if (TypeRelathionship.GROUP_RELATIONSHIP.equals(typeRelathionship)) {
                        return true;
                    }
                }
                return false;
            }
        });
        // Required if relationType == GROUP_RELATIONSHIP
        RequiredIfValidator ifValidator = new RequiredIfValidator(new RequiredIfFunction() {

            @Override
            public boolean execute(FormItem formItem, Object value) {
                return TypeRelathionship.GROUP_RELATIONSHIP.toString().equals(relationType.getValueAsString()) ? true : false;
            }
        });
        groupKeyFormForGroupRelationship.setValidators(ifValidator);

        // Representation

        representationTypeItem = new CustomSelectItem("repr-dim", MetamacSrmWeb.getConstants().representation());
        representationTypeItem.setType("comboBox");
        representationTypeItem.setValueMap(org.siemac.metamac.srm.web.client.utils.CommonUtils.getTypeRepresentationEnumHashMap());
        representationTypeItem.setRedrawOnChange(true);
        // Show FacetForm if RepresentationTypeEnum = NON_NUMERATED
        representationTypeItem.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                FacetFormUtils.setFacetFormVisibility(facetForm, representationTypeItem.getValueAsString());
            }
        });

        codeListItem = new CustomSelectItem("repr-enum-code-list", MetamacSrmWeb.getConstants().dsdCodeList());
        codeListItem.setType("comboBox");
        // Show CodeList if RepresentationTypeEnum = ENUMERATED
        codeListItem.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return CommonUtils.isRepresentationTypeEnumerated(representationTypeItem.getValueAsString());
            }
        });

        form = new GroupDynamicForm(MetamacSrmWeb.getConstants().dsdAttributeDetails());
        form.setFields(idLogic, assignmentStatusItem, conceptItem, roleItem, relationType, groupKeysForDimensionRelationshipItem, dimensionsForDimensionRelationshipItem,
                groupKeyFormForGroupRelationship, representationTypeItem, codeListItem);

        // Facet Form

        facetForm = new DsdFacetForm();
        facetForm.setVisibility(Visibility.HIDDEN);

        // Annotations
        editionAnnotationsPanel = new AnnotationsPanel(false);

        // VLayout formLayout = new VLayout(15);
        // formLayout.setMargin(10);
        // formLayout.addMember(form);
        // formLayout.addMember(facetForm);
        // formLayout.addMember(editionAnnotationsPanel);
        //
        // VLayout editionLayout = new VLayout();
        // editionLayout.setVisibility(Visibility.HIDDEN);
        // editionLayout.setBorder("1px solid #d9d9d9");
        // editionLayout.setAutoHeight();
        // editionLayout.addMember(formToolStrip);
        // editionLayout.addMember(formLayout);

        // return editionLayout;

        mainFormLayout.addEditionCanvas(form);
        mainFormLayout.addEditionCanvas(facetForm);
        mainFormLayout.addEditionCanvas(editionAnnotationsPanel);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setConceptSchemes(List<ExternalItemDto> conceptSchemes) {
        LinkedHashMap<String, String> map = ExternalItemUtils.getExternalItemsHashMap(conceptSchemes);
        conceptItem.setSchemesValueMap(map);
        roleItem.setConceptSchemesValueMap(map);
    }

    @Override
    public void setConcepts(List<ExternalItemDto> concepts) {
        this.concepts = concepts;
        conceptItem.setItemsValueMap(ExternalItemUtils.getExternalItemsHashMap(concepts));
    }

    @Override
    public void setRoleConcepts(List<ExternalItemDto> roleConcepts) {
        roleItem.setConcepts(roleConcepts);
    }

    @Override
    public void setCodeLists(List<ExternalItemDto> codeLists) {
        this.codeLists = codeLists;
        codeListItem.setValueMap(ExternalItemUtils.getExternalItemsHashMap(codeLists));

    }

    @Override
    public void setDimensions(List<DimensionComponentDto> dimensionComponentDtos) {
        this.dimensionComponentDtos = dimensionComponentDtos;
        dimensionsForDimensionRelationshipItem.setValueMap(CommonUtils.getDimensionComponentDtoHashMap(dimensionComponentDtos));
    }

    @Override
    public void setGroupKeys(List<DescriptorDto> descriptorDtos) {
        this.descriptorDtos = descriptorDtos;
        LinkedHashMap<String, String> groupKeysMap = CommonUtils.getDescriptorDtoHashMap(descriptorDtos);
        groupKeyFormForGroupRelationship.setValueMap(groupKeysMap);
        groupKeysForDimensionRelationshipItem.setValueMap(groupKeysMap);
    }

    @Override
    public void setDsdAttributes(ProcStatusEnum procStatus, List<DataAttributeDto> dataAttributeDtos) {
        this.procStatus = procStatus;

        // Security
        newToolStripButton.setVisibility(DsdClientSecurityUtils.canUpdateAttributes(procStatus) ? Visibility.VISIBLE : Visibility.HIDDEN);
        mainFormLayout.setCanEdit(DsdClientSecurityUtils.canUpdateAttributes(procStatus));

        deselectAttribute();
        attributesGrid.selectAllRecords();
        attributesGrid.removeSelectedData();
        attributesGrid.deselectAllRecords();
        for (DataAttributeDto dataAttributeDto : dataAttributeDtos) {
            AttributeRecord record = RecordUtils.getAttributeRecord(dataAttributeDto);
            attributesGrid.addData(record);
        }
    }

    public void setAttribute(DataAttributeDto dataAttributeDto) {
        setAttributeViewMode(dataAttributeDto);
        setAttributeEditionMode(dataAttributeDto);
    }

    private void setAttributeViewMode(DataAttributeDto dataAttributeDto) {
        this.dataAttributeDto = dataAttributeDto;

        // Id
        staticIdLogic.setValue(dataAttributeDto.getCode());

        // Concept
        staticConceptItem.setValue(dataAttributeDto.getCptIdRef() == null ? null : dataAttributeDto.getCptIdRef().getCode());

        // Role
        List<ExternalItemDto> roleConcepts = new ArrayList<ExternalItemDto>(dataAttributeDto.getRole());
        staticRoleItem.setValue(CommonUtils.getRoleListToString(roleConcepts));

        // Assignment Status
        String value = (dataAttributeDto.getUsageStatus() == null) ? null : MetamacSrmWeb.getCoreMessages().getString(
                MetamacSrmWeb.getCoreMessages().usageStatus() + dataAttributeDto.getUsageStatus().getName());
        staticAssignmentStatusItem.setValue(value);

        // RelateTo
        staticRelationType.setValue(new String());
        staticGroupKeyFormForGroupRelationship.clearValue();
        staticGroupKeyFormForGroupRelationship.hide();
        staticGroupKeysForDimensionRelationshipItem.clearValue();
        staticGroupKeysForDimensionRelationshipItem.hide();
        staticDimensionsForDimensionRelationshipItem.clearValue();
        staticDimensionsForDimensionRelationshipItem.hide();

        if (dataAttributeDto.getRelateTo() != null && dataAttributeDto.getRelateTo().getId() != null) {
            String typeValue = MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().typeRelationship() + dataAttributeDto.getRelateTo().getTypeRelathionship().toString());
            staticRelationType.setValue(typeValue);

            // Group keys for group relationship
            if (TypeRelathionship.GROUP_RELATIONSHIP.equals(dataAttributeDto.getRelateTo().getTypeRelathionship())) {
                staticGroupKeyFormForGroupRelationship.setValue((dataAttributeDto.getRelateTo().getGroupKeyForGroupRelationship() == null) ? null : dataAttributeDto.getRelateTo()
                        .getGroupKeyForGroupRelationship().getCode());
                staticGroupKeyFormForGroupRelationship.show();
            }

            if (TypeRelathionship.DIMENSION_RELATIONSHIP.equals(dataAttributeDto.getRelateTo().getTypeRelathionship())) {
                // Group keys form dimension relationship
                staticGroupKeysForDimensionRelationshipItem.clearValue();
                List<DescriptorDto> attributeGroupKeys = new ArrayList<DescriptorDto>(dataAttributeDto.getRelateTo().getGroupKeyForDimensionRelationship());
                StringBuilder groupKeysBuilder = new StringBuilder();
                for (int i = 0; i < attributeGroupKeys.size(); i++) {
                    groupKeysBuilder.append(i != 0 ? ",  " : "");
                    groupKeysBuilder.append(attributeGroupKeys.get(i).getCode());
                }
                staticGroupKeysForDimensionRelationshipItem.setValue(groupKeysBuilder.toString());
                staticGroupKeysForDimensionRelationshipItem.show();

                // Dimensions for dimension relationship
                staticDimensionsForDimensionRelationshipItem.clearValue();
                List<DimensionComponentDto> attributeDimensions = new ArrayList<DimensionComponentDto>(dataAttributeDto.getRelateTo().getDimensionForDimensionRelationship());
                StringBuilder dimensionBuilder = new StringBuilder();
                for (int i = 0; i < attributeDimensions.size(); i++) {
                    dimensionBuilder.append(i != 0 ? ",  " : "");
                    dimensionBuilder.append(attributeDimensions.get(i).getCode());
                }
                staticDimensionsForDimensionRelationshipItem.setValue(dimensionBuilder.toString());
                staticDimensionsForDimensionRelationshipItem.show();
            }
        }

        // Representation
        staticFacetForm.hide();
        staticCodeListItem.hide();
        staticCodeListItem.clearValue();
        staticRepresentationTypeItem.clearValue();
        staticFacetForm.clearValues();
        if (dataAttributeDto.getLocalRepresentation() != null) {
            // Code List
            if (TypeRepresentationEnum.ENUMERATED.equals(dataAttributeDto.getLocalRepresentation().getTypeRepresentationEnum())) {
                staticCodeListItem.setValue(dataAttributeDto.getLocalRepresentation().getEnumerated().getCode());
                staticRepresentationTypeItem.setValue(MetamacSrmWeb.getCoreMessages().typeRepresentationEnumENUMERATED());
                staticCodeListItem.show();
                // Facet
            } else if (TypeRepresentationEnum.TEXT_FORMAT.equals(dataAttributeDto.getLocalRepresentation().getTypeRepresentationEnum())) {
                staticRepresentationTypeItem.setValue(MetamacSrmWeb.getCoreMessages().typeRepresentationEnumTEXT_FORMAT());
                // Only one facet in a Representation
                FacetDto facetDto = dataAttributeDto.getLocalRepresentation().getNonEnumerated();
                staticFacetForm.setFacet(facetDto);
                staticFacetForm.show();
            }
        }

        // Annotations
        viewAnnotationsPanel.setAnnotations(dataAttributeDto.getAnnotations());
    }

    private void setAttributeEditionMode(DataAttributeDto dataAttributeDto) {
        this.dataAttributeDto = dataAttributeDto;

        // Id
        idLogic.setValue(dataAttributeDto.getCode());

        // Concept
        conceptItem.clearValue(); // Clear concept value: which is the scheme of a concept?

        // RelateTo
        relationType.setValue(new String());
        groupKeyFormForGroupRelationship.clearValue();
        groupKeysForDimensionRelationshipItem.clearValue();
        dimensionsForDimensionRelationshipItem.clearValue();

        if (dataAttributeDto.getRelateTo() != null && dataAttributeDto.getRelateTo().getId() != null) {
            relationType.setValue(dataAttributeDto.getRelateTo().getTypeRelathionship().toString());
            // Group keys for group relationship
            groupKeyFormForGroupRelationship.setValue((dataAttributeDto.getRelateTo().getGroupKeyForGroupRelationship() == null) ? null : (dataAttributeDto.getRelateTo()
                    .getGroupKeyForGroupRelationship().getId() == null) ? null : dataAttributeDto.getRelateTo().getGroupKeyForGroupRelationship().getId().toString());

            // Group keys form dimension relationship
            groupKeysForDimensionRelationshipItem.clearValue();
            List<DescriptorDto> attributeGroupKeys = new ArrayList<DescriptorDto>(dataAttributeDto.getRelateTo().getGroupKeyForDimensionRelationship());
            List<String> groupKeys = new ArrayList<String>();
            for (DescriptorDto d : attributeGroupKeys) {
                groupKeys.add(d.getId().toString());
            }
            groupKeysForDimensionRelationshipItem.setValues(groupKeys.toArray(new String[0]));

            // Dimensions for dimension relationship
            dimensionsForDimensionRelationshipItem.clearValue();
            List<DimensionComponentDto> attributeDimensions = new ArrayList<DimensionComponentDto>(dataAttributeDto.getRelateTo().getDimensionForDimensionRelationship());
            List<String> dimensions = new ArrayList<String>();
            for (DimensionComponentDto d : attributeDimensions) {
                dimensions.add(d.getId().toString());
            }
            dimensionsForDimensionRelationshipItem.setValues(dimensions.toArray(new String[0]));
        }

        // Role
        roleItem.clearValue();
        roleItem.setRoleConcepts(dataAttributeDto.getRole());

        // Assignment Status
        assignmentStatusItem.setValue((dataAttributeDto.getUsageStatus() == null) ? null : dataAttributeDto.getUsageStatus().toString());

        // Representation
        codeListItem.clearValue();
        representationTypeItem.clearValue();
        facetForm.clearValues();
        if (dataAttributeDto.getLocalRepresentation() != null) {
            // Code List
            if (TypeRepresentationEnum.ENUMERATED.equals(dataAttributeDto.getLocalRepresentation().getTypeRepresentationEnum())) {
                // codeListItem.setValue(dataAttributeDto.getLocalRepresentation().getEnumCodeList().getCodeId());
                codeListItem.clearValue(); // don´t know the concept (which is the scheme?), so code list neither
                representationTypeItem.setValue(TypeRepresentationEnum.ENUMERATED.toString());
                // Facet
            } else if (TypeRepresentationEnum.TEXT_FORMAT.equals(dataAttributeDto.getLocalRepresentation().getTypeRepresentationEnum())) {
                representationTypeItem.setValue(TypeRepresentationEnum.TEXT_FORMAT.toString());
                // Only one facet in a Representation
                FacetDto facetDto = dataAttributeDto.getLocalRepresentation().getNonEnumerated();
                facetForm.setFacet(facetDto);
            }
        }
        FacetFormUtils.setFacetFormVisibility(facetForm, representationTypeItem.getValueAsString());
        form.redraw();

        // Annotations
        editionAnnotationsPanel.setAnnotations(dataAttributeDto.getAnnotations());
    }

    @Override
    public List<DataAttributeDto> getSelectedAttributes() {
        if (attributesGrid.getSelectedRecords() != null) {
            List<DataAttributeDto> selectedAttributes = new ArrayList<DataAttributeDto>();
            ListGridRecord[] records = attributesGrid.getSelectedRecords();
            for (int i = 0; i < records.length; i++) {
                AttributeRecord record = (AttributeRecord) records[i];
                selectedAttributes.add(record.getDataAttributeDto());
            }
            return selectedAttributes;
        }
        return null;
    }

    @Override
    public DataAttributeDto getDsdAttribute() {
        // Id
        dataAttributeDto.setCode(idLogic.getValueAsString());

        // Role
        dataAttributeDto.getRole().clear();
        List<ExternalItemDto> roleConcepts = roleItem.getSelectedConcepts();
        dataAttributeDto.getRole().addAll(roleConcepts);

        // Concept
        dataAttributeDto.setCptIdRef(conceptItem.getSelectedExternalItem(concepts));

        // Assignment Status
        dataAttributeDto.setUsageStatus((assignmentStatusItem.getValueAsString() == null || assignmentStatusItem.getValueAsString().isEmpty()) ? null : UsageStatus.valueOf(assignmentStatusItem
                .getValueAsString()));

        // Relation
        if (dataAttributeDto.getRelateTo() == null) {
            dataAttributeDto.setRelateTo(new RelationshipDto());
        }
        // - Clear relation
        dataAttributeDto.getRelateTo().setGroupKeyForGroupRelationship(null);
        if (dataAttributeDto.getRelateTo().getGroupKeyForDimensionRelationship() != null) {
            dataAttributeDto.getRelateTo().getGroupKeyForDimensionRelationship().clear();
        }
        if (dataAttributeDto.getRelateTo().getDimensionForDimensionRelationship() != null) {
            dataAttributeDto.getRelateTo().getDimensionForDimensionRelationship().clear();
        }
        // - Set relation
        TypeRelathionship typeRelathionship = TypeRelathionship.valueOf(relationType.getValueAsString());
        dataAttributeDto.getRelateTo().setTypeRelathionship(typeRelathionship);
        if (TypeRelathionship.GROUP_RELATIONSHIP.equals(typeRelathionship)) {
            DescriptorDto descriptorDto = getGroupKeysDto(groupKeyFormForGroupRelationship.getValueAsString());
            dataAttributeDto.getRelateTo().setGroupKeyForGroupRelationship(descriptorDto);
        } else if (TypeRelathionship.DIMENSION_RELATIONSHIP.equals(typeRelathionship)) {
            List<DescriptorDto> groupKeysRelation = getGroupKeys(groupKeysForDimensionRelationshipItem.getValues());
            List<DimensionComponentDto> dimensionsRelation = getDimensions(dimensionsForDimensionRelationshipItem.getValues());
            dataAttributeDto.getRelateTo().getGroupKeyForDimensionRelationship().addAll(groupKeysRelation);
            dataAttributeDto.getRelateTo().getDimensionForDimensionRelationship().addAll(dimensionsRelation);
        }

        // Representation
        if (representationTypeItem.getValue() != null && !representationTypeItem.getValue().toString().isEmpty()) {
            TypeRepresentationEnum representationType = TypeRepresentationEnum.valueOf(representationTypeItem.getValue().toString());

            if (dataAttributeDto.getLocalRepresentation() == null) {
                dataAttributeDto.setLocalRepresentation(new RepresentationDto());
            }

            // Code List
            if (TypeRepresentationEnum.ENUMERATED.equals(representationType)) {
                dataAttributeDto.getLocalRepresentation().setTypeRepresentationEnum(TypeRepresentationEnum.ENUMERATED);
                dataAttributeDto.getLocalRepresentation().setEnumerated(ExternalItemUtils.getExternalItemDtoFromUrn(codeLists, codeListItem.getValueAsString()));
                dataAttributeDto.getLocalRepresentation().setNonEnumerated(null);
                // Facet
            } else if (TypeRepresentationEnum.TEXT_FORMAT.equals(representationType)) {
                dataAttributeDto.getLocalRepresentation().setTypeRepresentationEnum(TypeRepresentationEnum.TEXT_FORMAT);
                FacetDto facetDto = facetForm.getFacet();
                dataAttributeDto.getLocalRepresentation().setNonEnumerated(facetDto);
                dataAttributeDto.getLocalRepresentation().setEnumerated(null);
            }
        } else {
            // No representation
            dataAttributeDto.setLocalRepresentation(null);
        }

        // If it is a new component, specify type
        if (dataAttributeDto.getId() == null) {
            dataAttributeDto.setTypeComponent(TypeComponent.DATA_ATTRIBUTE);
        }

        // Annotations
        dataAttributeDto.getAnnotations().clear();
        dataAttributeDto.getAnnotations().addAll(editionAnnotationsPanel.getAnnotations());

        return dataAttributeDto;
    }

    @Override
    public boolean validate() {
        return Visibility.HIDDEN.equals(facetForm.getVisibility()) ? form.validate(false) && conceptItem.validateItem() : (form.validate(false) && facetForm.validate(false) && conceptItem
                .validateItem());
    }

    @Override
    public HasClickHandlers getSave() {
        return mainFormLayout.getSave();
    }

    @Override
    public HasClickHandlers getDelete() {
        return importDsdWindow.getYesButton();
    }

    @Override
    public void onAttributeSaved(DataAttributeDto dataAttributeDto) {
        this.dataAttributeDto = dataAttributeDto;
        attributesGrid.removeSelectedData();
        AttributeRecord record = RecordUtils.getAttributeRecord(dataAttributeDto);
        attributesGrid.addData(record);
        attributesGrid.selectRecord(record);
        mainFormLayout.setViewMode();
    }

    @Override
    public HasChangeHandlers onConceptSchemeChange() {
        return conceptItem.getSchemeItem();
    }

    @Override
    public HasChangeHandlers onConceptChange() {
        return conceptItem.getItem();
    }

    @Override
    public HasChangeHandlers onRoleConceptSchemeChange() {
        return roleItem.getConceptSchemeItem();
    }

    @Override
    public HasChangeHandlers onRepresentationTypeChange() {
        return representationTypeItem;
    }

    @Override
    public HasSelectionChangedHandlers onAttributeSelected() {
        return attributesGrid;
    }

    private void selectAttribute(DataAttributeDto attributeSelected) {
        if (attributeSelected.getId() == null) {
            // New attribute
            mainFormLayout.setTitleLabelContents(new String());
            deleteToolStripButton.hide();
            attributesGrid.deselectAllRecords();
            setAttributeEditionMode(attributeSelected);
            mainFormLayout.setEditionMode();
        } else {
            mainFormLayout.setTitleLabelContents(attributeSelected.getCode());
            showDeleteToolStripButton();
            setAttribute(attributeSelected);
            mainFormLayout.setViewMode();
        }

        // Clear errors
        form.clearErrors(true);
        facetForm.clearErrors(true);

        selectedComponentLayout.show();
        selectedComponentLayout.redraw();
    }

    private void deselectAttribute() {
        selectedComponentLayout.hide();
        deleteToolStripButton.hide();
        // editToolStripButton.hide();
    }

    private List<DimensionComponentDto> getDimensions(String[] dimensions) {
        List<DimensionComponentDto> dimensionComponentDtos = new ArrayList<DimensionComponentDto>();
        for (String id : dimensions) {
            DimensionComponentDto dimensionComponentDto = getDimensionComponentDto(id);
            dimensionComponentDtos.add(dimensionComponentDto);
        }
        return dimensionComponentDtos;
    }

    private List<DescriptorDto> getGroupKeys(String[] groupKeys) {
        List<DescriptorDto> descriptorDtos = new ArrayList<DescriptorDto>();
        for (String id : groupKeys) {
            DescriptorDto descriptorDto = getGroupKeysDto(id);
            descriptorDtos.add(descriptorDto);
        }
        return descriptorDtos;
    }

    private DescriptorDto getGroupKeysDto(String id) {
        if (id != null) {
            Long idDescriptor = Long.valueOf(id);
            for (DescriptorDto d : descriptorDtos) {
                if (d.getId().compareTo(idDescriptor) == 0) {
                    return d;
                }
            }
        }
        return null;
    }

    private DimensionComponentDto getDimensionComponentDto(String id) {
        if (id != null) {
            Long idDimension = Long.valueOf(id);
            for (DimensionComponentDto d : dimensionComponentDtos) {
                if (d.getId().compareTo(idDimension) == 0) {
                    return d;
                }
            }
        }
        return null;
    }

    private void setTranslationsShowed(boolean translationsShowed) {
        viewAnnotationsPanel.setTranslationsShowed(translationsShowed);
        editionAnnotationsPanel.setTranslationsShowed(translationsShowed);
    }

    private void showDeleteToolStripButton() {
        if (DsdClientSecurityUtils.canUpdateAttributes(procStatus)) {
            deleteToolStripButton.show();
        }
    }

}
