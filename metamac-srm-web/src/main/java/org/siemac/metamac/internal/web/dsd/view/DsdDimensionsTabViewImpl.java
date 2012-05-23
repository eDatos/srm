package org.siemac.metamac.internal.web.dsd.view;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemBtDto;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.domain.srm.dto.DimensionComponentDto;
import org.siemac.metamac.domain.srm.dto.FacetDto;
import org.siemac.metamac.domain.srm.dto.RepresentationDto;
import org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum;
import org.siemac.metamac.domain.srm.enume.domain.TypeComponent;
import org.siemac.metamac.domain.srm.enume.domain.TypeDimensionComponent;
import org.siemac.metamac.domain.srm.enume.domain.TypeRepresentationEnum;
import org.siemac.metamac.internal.web.client.MetamacInternalWeb;
import org.siemac.metamac.internal.web.client.resources.GlobalResources;
import org.siemac.metamac.internal.web.dsd.enums.RepresentationTypeEnum;
import org.siemac.metamac.internal.web.dsd.model.record.DimensionRecord;
import org.siemac.metamac.internal.web.dsd.presenter.DsdDimensionsTabPresenter;
import org.siemac.metamac.internal.web.dsd.utils.CommonUtils;
import org.siemac.metamac.internal.web.dsd.utils.FacetFormUtils;
import org.siemac.metamac.internal.web.dsd.utils.RecordUtils;
import org.siemac.metamac.internal.web.dsd.view.handlers.DsdDimensionsTabUiHandlers;
import org.siemac.metamac.internal.web.dsd.widgets.AnnotationsPanel;
import org.siemac.metamac.internal.web.dsd.widgets.FacetForm;
import org.siemac.metamac.internal.web.dsd.widgets.RoleSelectItem;
import org.siemac.metamac.internal.web.dsd.widgets.StaticFacetForm;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.utils.ExternalItemUtils;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.ExternalSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.HasClickHandlers;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.HasChangeHandlers;
import com.smartgwt.client.widgets.form.validator.CustomValidator;
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

public class DsdDimensionsTabViewImpl extends ViewWithUiHandlers<DsdDimensionsTabUiHandlers> implements DsdDimensionsTabPresenter.DsdDimensionsTabView {

    private DimensionComponentDto       dimensionComponentDto;
    private List<ExternalItemBtDto>     codeLists;
    private List<ExternalItemBtDto>     conceptSchemes;
    private List<ExternalItemBtDto>     concepts;

    private VLayout                     panel;
    private VLayout                     selectedComponentLayout;
    private ListGrid                    dimensionsGrid;
    private InternationalMainFormLayout mainFormLayout;

    private AnnotationsPanel            viewAnnotationsPanel;
    private AnnotationsPanel            editionAnnotationsPanel;

    // VIEW FORM

    private ViewTextItem                staticIdLogic;
    private ViewTextItem                staticTypeItem;
    private ViewTextItem                staticConceptItem;
    private ViewTextItem                staticRoleItem;
    // private StaticTextItem staticPositionItem;
    // Representation
    private ViewTextItem                staticRepresentationTypeItem;
    private ViewTextItem                staticCodeListItem;
    private ViewTextItem                staticConceptSchemeItem;
    private StaticFacetForm             staticFacetForm;

    // EDITION FORM

    private GroupDynamicForm            form;
    private RequiredTextItem            idLogic;
    private ViewTextItem                staticIdLogicEdit;
    private ViewTextItem                staticTypeItemEdit;          // Type cannot be modified
    private RequiredSelectItem          typeItem;
    private ExternalSelectItem          conceptItem;
    private RoleSelectItem              roleItem;
    // Representation
    private SelectItem                  representationTypeItem;
    private SelectItem                  codeListItem;
    private SelectItem                  conceptSchemeItem;
    private FacetForm                   facetForm;

    private ToolStripButton             newToolStripButton;
    private ToolStripButton             deleteToolStripButton;

    private DeleteConfirmationWindow    deleteConfirmationWindow;

    @Inject
    public DsdDimensionsTabViewImpl() {
        super();
        panel = new VLayout();

        // ··················
        // List of dimensions
        // ··················

        // ToolStrip

        newToolStripButton = new ToolStripButton(MetamacInternalWeb.getConstants().actionNew(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.newListGrid().getURL());
        newToolStripButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                selectDimension(new DimensionComponentDto());
            }
        });

        deleteConfirmationWindow = new DeleteConfirmationWindow(MetamacInternalWeb.getConstants().dsdDeleteConfirmationTitle(), MetamacInternalWeb.getConstants().dsdDimensionDeleteConfirmation());
        deleteConfirmationWindow.setVisibility(Visibility.HIDDEN);

        deleteToolStripButton = new ToolStripButton(MetamacInternalWeb.getConstants().actionDelete(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.deleteListGrid().getURL());
        deleteToolStripButton.setVisibility(Visibility.HIDDEN);
        deleteToolStripButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });

        ToolStrip dimensionGridToolStrip = new ToolStrip();
        dimensionGridToolStrip.setWidth100();
        dimensionGridToolStrip.addButton(newToolStripButton);
        dimensionGridToolStrip.addSeparator();
        dimensionGridToolStrip.addButton(deleteToolStripButton);

        // Grid

        dimensionsGrid = new ListGrid();
        dimensionsGrid.setWidth100();
        dimensionsGrid.setHeight(150);
        dimensionsGrid.setSelectionType(SelectionStyle.SIMPLE);
        dimensionsGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
        ListGridField idLogicField = new ListGridField(DimensionRecord.ID_LOGIC, MetamacInternalWeb.getConstants().dsdDimensionsId());
        ListGridField typeField = new ListGridField(DimensionRecord.TYPE, MetamacInternalWeb.getConstants().dsdDimensionsType());
        ListGridField conceptField = new ListGridField(DimensionRecord.CONCEPT, MetamacInternalWeb.getConstants().dsdDimensionsConcept());
        dimensionsGrid.setFields(idLogicField, typeField, conceptField);
        // ToolTip
        idLogicField.setShowHover(true);
        idLogicField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                DimensionRecord dimensionRecord = (DimensionRecord) record;
                return dimensionRecord.getIdLogic();
            }
        });
        typeField.setShowHover(true);
        typeField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                DimensionRecord dimensionRecord = (DimensionRecord) record;
                return dimensionRecord.getType();
            }
        });
        conceptField.setShowHover(true);
        conceptField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                DimensionRecord dimensionRecord = (DimensionRecord) record;
                return dimensionRecord.getConcept();
            }
        });
        // Show dimension details when record clicked
        dimensionsGrid.addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (dimensionsGrid.getSelectedRecords() != null && dimensionsGrid.getSelectedRecords().length == 1) {
                    DimensionRecord record = (DimensionRecord) dimensionsGrid.getSelectedRecord();
                    DimensionComponentDto dimensionSelected = record.getDimensionComponentDto();
                    selectDimension(dimensionSelected);
                } else {
                    // No record selected
                    deselectDimension();
                    if (dimensionsGrid.getSelectedRecords().length > 1) {
                        // Delete more than one dimension with one click
                        deleteToolStripButton.show();
                    }
                }
            }
        });
        dimensionsGrid.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // CheckBox is not clicked
                    dimensionsGrid.deselectAllRecords();
                    dimensionsGrid.selectRecord(event.getRecord());
                }
            }
        });

        VLayout gridLayout = new VLayout();
        gridLayout.setAutoHeight();
        gridLayout.setMargin(10);
        gridLayout.addMember(dimensionGridToolStrip);
        gridLayout.addMember(dimensionsGrid);

        // ··············
        // Dimension Form
        // ··············

        // Title

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
                if (dimensionComponentDto.getId() == null) {
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
        staticIdLogic = new ViewTextItem("id-dim-view", MetamacInternalWeb.getConstants().dsdDimensionsId());
        staticTypeItem = new ViewTextItem("type-dim-view", MetamacInternalWeb.getConstants().dsdDimensionsType());
        staticConceptItem = new ViewTextItem("concept-dim-view", MetamacInternalWeb.getConstants().dsdDimensionsConcept());
        staticRoleItem = new ViewTextItem("role-dim-view", MetamacInternalWeb.getConstants().dsdDimensionsRole());
        // staticPositionItem = new StaticTextItem("position-dim-view", MetamacInternalWeb.getConstants().dsdDimensionsPosition());
        staticRepresentationTypeItem = new ViewTextItem("repr-dim-view", MetamacInternalWeb.getConstants().dsdRepresentation());
        staticCodeListItem = new ViewTextItem("repr-enum-code-list-view", MetamacInternalWeb.getConstants().dsdCodeList());
        staticConceptSchemeItem = new ViewTextItem("repr-enum-concept-scheme-view", MetamacInternalWeb.getConstants().conceptScheme());

        GroupDynamicForm staticForm = new GroupDynamicForm(MetamacInternalWeb.getConstants().dsdDimensionDetails());
        staticForm.setFields(staticIdLogic, staticTypeItem, staticConceptItem, staticRoleItem, staticRepresentationTypeItem, staticCodeListItem, staticConceptSchemeItem);

        staticFacetForm = new StaticFacetForm();

        // Annotations
        viewAnnotationsPanel = new AnnotationsPanel(true);

        mainFormLayout.addViewCanvas(staticForm);
        mainFormLayout.addViewCanvas(staticFacetForm);
        mainFormLayout.addViewCanvas(viewAnnotationsPanel);
    }

    /**
     * Creates and returns the edition layout
     * 
     * @return
     */
    private void createEditionForm() {
        // Id

        idLogic = new RequiredTextItem("id-dim", MetamacInternalWeb.getConstants().dsdDimensionsId());
        idLogic.setRedrawOnChange(true);
        idLogic.setValidators(CommonWebUtils.getSemanticIdentifierCustomValidator());

        staticIdLogicEdit = new ViewTextItem("static-id-dim-view", MetamacInternalWeb.getConstants().dsdDimensionsId());
        staticIdLogicEdit.setRedrawOnChange(true);
        // Do not edit idLogic if TypeDimensionComponent == TIMEDIMENSION or TypeDimensionComponent == MEASUREDIMENSION
        idLogic.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (TypeDimensionComponent.TIMEDIMENSION.toString().equals(typeItem.getValueAsString()) || TypeDimensionComponent.MEASUREDIMENSION.toString().equals(typeItem.getValueAsString())) {
                    return false;
                } else {
                    return true;
                }
            }
        });
        staticIdLogicEdit.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (TypeDimensionComponent.TIMEDIMENSION.toString().equals(typeItem.getValueAsString()) || TypeDimensionComponent.MEASUREDIMENSION.toString().equals(typeItem.getValueAsString())) {
                    return true;
                } else {
                    return false;
                }
            }
        });

        // Type (read only)

        staticTypeItemEdit = new ViewTextItem("type-dim-view-edit", MetamacInternalWeb.getConstants().dsdDimensionsType());

        // Type

        typeItem = new RequiredSelectItem("type-dim", MetamacInternalWeb.getConstants().dsdDimensionsType());
        typeItem.setType("comboBox");
        LinkedHashMap<String, String> typeValueMap = new LinkedHashMap<String, String>();
        for (TypeDimensionComponent t : TypeDimensionComponent.values()) {
            String value = MetamacInternalWeb.getCoreMessages().getString(MetamacInternalWeb.getCoreMessages().typeDimensionComponent() + t.getName());
            typeValueMap.put(t.toString(), value);
        }
        typeItem.setValueMap(typeValueMap);
        typeItem.setRedrawOnChange(true);
        FormItemIcon infoIcon = new FormItemIcon();
        infoIcon.setHeight(14);
        infoIcon.setWidth(14);
        infoIcon.setSrc(GlobalResources.RESOURCE.info().getURL());
        infoIcon.setPrompt(MetamacInternalWeb.getMessages().infoDimensionType());
        typeItem.setIcons(infoIcon);
        typeItem.setIconVAlign(VerticalAlignment.CENTER);
        typeItem.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                // Redraw the form after changing dimension type
                form.clearErrors(true);
            }
        });

        // Concept

        conceptItem = new ExternalSelectItem("concept-dim", MetamacInternalWeb.getConstants().dsdDimensionsConcept());
        conceptItem.setRequired(true);
        conceptItem.setType("comboBox");

        // Role

        roleItem = new RoleSelectItem("role-dim", MetamacInternalWeb.getConstants().dsdDimensionsRole());
        // Do not show Role if TypeDimensionComponent = TIMEDIMENSION
        roleItem.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (typeItem.getValueAsString() != null) {
                    TypeDimensionComponent typeDimensionComponent = TypeDimensionComponent.valueOf(typeItem.getValueAsString());
                    if (TypeDimensionComponent.DIMENSION.equals(typeDimensionComponent) || TypeDimensionComponent.MEASUREDIMENSION.equals(typeDimensionComponent)) {
                        return true;
                    }
                }
                return false;
            }
        });

        // Representation

        representationTypeItem = new SelectItem("repr-dim", MetamacInternalWeb.getConstants().dsdRepresentation());
        representationTypeItem.setType("comboBox");
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(new String(), new String());
        for (RepresentationTypeEnum r : RepresentationTypeEnum.values()) {
            String value = MetamacInternalWeb.getCoreMessages().getString(MetamacInternalWeb.getCoreMessages().representationTypeEnum() + r.getName());
            valueMap.put(r.toString(), value);
        }
        representationTypeItem.setValueMap(valueMap);
        representationTypeItem.setRedrawOnChange(true);
        // Show FacetForm if RepresentationTypeEnum = NON_NUMERATED
        representationTypeItem.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                FacetFormUtils.setFacetFormVisibility(facetForm, representationTypeItem.getValueAsString());
            }
        });
        // Measure dimensions must be enumerated
        CustomValidator measureCustomValidator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                if (TypeDimensionComponent.MEASUREDIMENSION.toString().equals(typeItem.getValueAsString())) {
                    return RepresentationTypeEnum.ENUMERATED.toString().equals(value) ? true : false;
                }
                return true;
            }
        };
        measureCustomValidator.setErrorMessage(MetamacInternalWeb.getMessages().errorRequiredEnumeratedRepresentationInMeasureDimension());
        // Time dimensions must have a non enumerated representation
        CustomValidator timeCustomValidator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                if (TypeDimensionComponent.TIMEDIMENSION.toString().equals(typeItem.getValueAsString())) {
                    return RepresentationTypeEnum.NON_NUMERATED.toString().equals(value) ? true : false;
                }
                return true;
            }
        };
        timeCustomValidator.setErrorMessage(MetamacInternalWeb.getMessages().errorRequiredNonEnumeratedRepresentationInTimeDimension());
        representationTypeItem.setValidators(measureCustomValidator, timeCustomValidator);

        // Code List

        codeListItem = new SelectItem("repr-enum-code-list", MetamacInternalWeb.getConstants().dsdCodeList());
        codeListItem.setType("comboBox");
        // Show CodeList if RepresentationTypeEnum = ENUMERATED (except in MeasureDimension)
        codeListItem.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return CommonUtils.isRepresentationTypeEnumerated(representationTypeItem.getValueAsString()) && !TypeDimensionComponent.MEASUREDIMENSION.toString().equals(typeItem.getValueAsString());
            }
        });

        conceptSchemeItem = new SelectItem("repr-enum-concept-scheme", MetamacInternalWeb.getConstants().conceptScheme());
        conceptSchemeItem.setType("comboBox");
        // Show CodeList if RepresentationTypeEnum = ENUMERATED and TypeDimensionComponent == MEASUREDIMENSION
        conceptSchemeItem.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return CommonUtils.isRepresentationTypeEnumerated(representationTypeItem.getValueAsString()) && TypeDimensionComponent.MEASUREDIMENSION.toString().equals(typeItem.getValueAsString());
            }
        });

        form = new GroupDynamicForm(MetamacInternalWeb.getConstants().dsdDimensionDetails());
        form.setFields(idLogic, staticIdLogicEdit, staticTypeItemEdit, typeItem, conceptItem, roleItem, representationTypeItem, codeListItem, conceptSchemeItem);

        // Facet Form

        facetForm = new FacetForm();
        // TextType applicable to TimeDimension is restricted to those that represent time
        CustomValidator timeValidator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                if (TypeDimensionComponent.TIMEDIMENSION.toString().equals(typeItem.getValueAsString())) {
                    if (value != null) {
                        FacetValueTypeEnum f = FacetValueTypeEnum.valueOf((String) value);
                        return FacetFormUtils.representsTime(f) ? true : false;
                    }
                }
                return true;
            }
        };
        timeValidator.setErrorMessage(MetamacInternalWeb.getMessages().errorTextTypeInTimeDimension());
        facetForm.getTextType().setValidateOnChange(true);
        facetForm.getTextType().setRedrawOnChange(true);
        facetForm.getTextType().setValidators(timeValidator);

        // Annotations
        editionAnnotationsPanel = new AnnotationsPanel(false);

        mainFormLayout.addEditionCanvas(form);
        mainFormLayout.addEditionCanvas(facetForm);
        mainFormLayout.addEditionCanvas(editionAnnotationsPanel);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setDsdDimensions(List<DimensionComponentDto> dimensionComponentDtos) {
        deselectDimension();
        dimensionsGrid.selectAllRecords();
        dimensionsGrid.removeSelectedData();
        dimensionsGrid.deselectAllRecords();
        for (DimensionComponentDto dimensionComponentDto : dimensionComponentDtos) {
            DimensionRecord dimensionRecord = RecordUtils.getDimensionRecord(dimensionComponentDto);
            dimensionsGrid.addData(dimensionRecord);
        }
    }

    @Override
    public void setConceptSchemes(List<ExternalItemBtDto> conceptSchemes) {
        this.conceptSchemes = conceptSchemes;
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        for (ExternalItemBtDto scheme : conceptSchemes) {
            map.put(scheme.getCodeId(), scheme.getCodeId());
        }
        conceptItem.setSchemesValueMap(map);
        roleItem.setConceptSchemesValueMap(map);
        conceptSchemeItem.setValueMap(map);
    }

    @Override
    public void setConcepts(List<ExternalItemBtDto> concepts) {
        this.concepts = concepts;
        LinkedHashMap<String, String> conceptsMap = new LinkedHashMap<String, String>();
        for (ExternalItemBtDto concept : concepts) {
            conceptsMap.put(concept.getCodeId(), concept.getCodeId());
        }
        conceptItem.setItemsValueMap(conceptsMap);
    }

    @Override
    public void setRoleConcepts(List<ExternalItemBtDto> roleConcepts) {
        roleItem.setConcepts(roleConcepts);
    }

    @Override
    public void setCodeLists(List<ExternalItemBtDto> codeLists) {
        this.codeLists = codeLists;
        LinkedHashMap<String, String> codeListsMap = new LinkedHashMap<String, String>();
        for (ExternalItemBtDto codeList : codeLists) {
            codeListsMap.put(codeList.getCodeId(), codeList.getCodeId());
        }
        codeListItem.setValueMap(codeListsMap);
    }

    @Override
    public DimensionComponentDto getDsdDimension() {
        // Id
        dimensionComponentDto.setIdLogic(idLogic.getVisible() ? idLogic.getValueAsString() : null);

        // Type
        dimensionComponentDto.setTypeDimensionComponent(TypeDimensionComponent.valueOf(typeItem.getValueAsString()));

        // Role
        dimensionComponentDto.getRole().clear();
        if (!TypeDimensionComponent.TIMEDIMENSION.equals(dimensionComponentDto.getTypeDimensionComponent())) {
            List<ExternalItemBtDto> roleConcepts = roleItem.getSelectedConcepts();
            dimensionComponentDto.getRole().addAll(roleConcepts);
        }

        // Concept
        dimensionComponentDto.setCptIdRef(conceptItem.getSelectedExternalItem(concepts));

        // Representation
        if (representationTypeItem.getValue() != null && !representationTypeItem.getValue().toString().isEmpty()) {
            RepresentationTypeEnum representationType = RepresentationTypeEnum.valueOf(representationTypeItem.getValue().toString());

            if (dimensionComponentDto.getLocalRepresentation() == null) {
                dimensionComponentDto.setLocalRepresentation(new RepresentationDto());
            }

            // Code List
            if (RepresentationTypeEnum.ENUMERATED.equals(representationType)) {
                dimensionComponentDto.getLocalRepresentation().setTypeRepresentationEnum(TypeRepresentationEnum.ENUMERATED);
                if (TypeDimensionComponent.MEASUREDIMENSION.equals(dimensionComponentDto.getTypeDimensionComponent())) {
                    dimensionComponentDto.getLocalRepresentation().setEnumerated(ExternalItemUtils.getExternalItemBtDtoFromCodeId(conceptSchemes, conceptSchemeItem.getValueAsString()));
                } else {
                    dimensionComponentDto.getLocalRepresentation().setEnumerated(ExternalItemUtils.getExternalItemBtDtoFromCodeId(codeLists, codeListItem.getValueAsString()));
                }
                dimensionComponentDto.getLocalRepresentation().setNonEnumerated(null);
                // Facet
            } else if (RepresentationTypeEnum.NON_NUMERATED.equals(representationType)) {
                dimensionComponentDto.getLocalRepresentation().setTypeRepresentationEnum(TypeRepresentationEnum.TEXT_FORMAT);
                FacetDto facetDto = facetForm.getFacet(dimensionComponentDto.getLocalRepresentation().getNonEnumerated() == null ? new FacetDto() : dimensionComponentDto.getLocalRepresentation()
                        .getNonEnumerated());
                dimensionComponentDto.getLocalRepresentation().setNonEnumerated(facetDto);
                dimensionComponentDto.getLocalRepresentation().setEnumerated(null);
            }
        } else {
            // No representation
            dimensionComponentDto.setLocalRepresentation(null);
        }

        // If it is a new component, specify type and Position (if needed)
        if (dimensionComponentDto.getId() == null) {
            dimensionComponentDto.setTypeComponent(TypeComponent.DIMENSION_COMPONENT);
            // if (TypeDimensionComponent.MEASUREDIMENSION.equals(dimensionComponentDto.getTypeDimensionComponent())) {
            // dimensionComponentDto.setOrderLogic(-1);
            // } else if (TypeDimensionComponent.TIMEDIMENSION.equals(dimensionComponentDto.getTypeDimensionComponent())) {
            // dimensionComponentDto.setOrderLogic(0);
            // }
        }

        // Annotations
        dimensionComponentDto.getAnnotations().clear();
        dimensionComponentDto.getAnnotations().addAll(editionAnnotationsPanel.getAnnotations());

        return dimensionComponentDto;
    }

    @Override
    public List<DimensionComponentDto> getSelectedDimensions() {
        if (dimensionsGrid.getSelectedRecords() != null) {
            ListGridRecord[] records = dimensionsGrid.getSelectedRecords();
            List<DimensionComponentDto> selectedDimensions = new ArrayList<DimensionComponentDto>();
            for (int i = 0; i < records.length; i++) {
                DimensionRecord record = (DimensionRecord) records[i];
                selectedDimensions.add(record.getDimensionComponentDto());
            }
            return selectedDimensions;
        }
        return null;
    }

    public void setDimension(DimensionComponentDto dimensionComponentDto) {
        setDimensionViewMode(dimensionComponentDto);
        setDimensionEditionMode(dimensionComponentDto);
    }

    private void setDimensionViewMode(DimensionComponentDto dimensionComponentDto) {
        this.dimensionComponentDto = dimensionComponentDto;
        // Id
        staticIdLogic.setValue(dimensionComponentDto.getIdLogic());
        // Type
        String value = (dimensionComponentDto.getTypeDimensionComponent() == null) ? null : MetamacInternalWeb.getCoreMessages().getString(
                MetamacInternalWeb.getCoreMessages().typeDimensionComponent() + dimensionComponentDto.getTypeDimensionComponent().toString());
        staticTypeItem.setValue(value);
        // Concept
        staticConceptItem.setValue(dimensionComponentDto.getCptIdRef() == null ? null : dimensionComponentDto.getCptIdRef().getCodeId());
        // Role
        staticRoleItem.hide();
        staticRoleItem.clearValue();
        if (!TypeDimensionComponent.TIMEDIMENSION.equals(dimensionComponentDto.getTypeDimensionComponent())) {
            List<ExternalItemBtDto> roleConcepts = new ArrayList<ExternalItemBtDto>(dimensionComponentDto.getRole());
            staticRoleItem.setValue(CommonUtils.getRoleListToString(roleConcepts));
            staticRoleItem.show();
        }
        // Position
        // staticPositionItem.setValue(dimensionComponentDto.getOrderLogic().toString());
        // Representation
        staticFacetForm.hide();
        staticCodeListItem.hide();
        staticCodeListItem.clearValue();
        staticConceptSchemeItem.hide();
        staticConceptSchemeItem.hide();
        staticRepresentationTypeItem.clearValue();
        staticFacetForm.clearValues();
        if (dimensionComponentDto.getLocalRepresentation() != null) {
            if (RepresentationTypeEnum.ENUMERATED.equals(dimensionComponentDto.getLocalRepresentation().getTypeRepresentationEnum())) {
                // Code List
                if (TypeExternalArtefactsEnum.CODELIST.equals(dimensionComponentDto.getLocalRepresentation().getEnumerated().getType())) {
                    staticRepresentationTypeItem.setValue(MetamacInternalWeb.getCoreMessages().representationTypeEnumENUMERATED());
                    if (!TypeDimensionComponent.MEASUREDIMENSION.equals(dimensionComponentDto.getTypeDimensionComponent())) {
                        staticCodeListItem.setValue(dimensionComponentDto.getLocalRepresentation().getEnumerated().getCodeId());
                        staticCodeListItem.show();
                    }
                    // ConceptScheme
                } else if (TypeExternalArtefactsEnum.CONCEPT_SCHEME.equals(dimensionComponentDto.getLocalRepresentation().getEnumerated().getType())) {
                    staticRepresentationTypeItem.setValue(MetamacInternalWeb.getCoreMessages().representationTypeEnumENUMERATED());
                    if (TypeDimensionComponent.MEASUREDIMENSION.equals(dimensionComponentDto.getTypeDimensionComponent())) {
                        staticConceptSchemeItem.setValue(dimensionComponentDto.getLocalRepresentation().getEnumerated().getCodeId());
                        staticConceptSchemeItem.show();
                    }
                }
                // Facet
            } else if (RepresentationTypeEnum.NON_NUMERATED.equals(dimensionComponentDto.getLocalRepresentation().getTypeRepresentationEnum())) {
                staticRepresentationTypeItem.setValue(MetamacInternalWeb.getCoreMessages().representationTypeEnumNON_NUMERATED());
                FacetDto facetDto = dimensionComponentDto.getLocalRepresentation().getNonEnumerated();
                staticFacetForm.setFacet(facetDto);
                staticFacetForm.show();
            }
        }

        // Annotations
        viewAnnotationsPanel.setAnnotations(dimensionComponentDto.getAnnotations());
    }

    private void setDimensionEditionMode(DimensionComponentDto dimensionComponentDto) {
        this.dimensionComponentDto = dimensionComponentDto;
        // Id
        idLogic.setValue(dimensionComponentDto.getIdLogic());
        staticIdLogicEdit.setValue(dimensionComponentDto.getIdLogic());
        // Type
        typeItem.setValue((dimensionComponentDto.getTypeDimensionComponent() == null) ? null : dimensionComponentDto.getTypeDimensionComponent().toString());
        String value = (dimensionComponentDto.getTypeDimensionComponent() == null) ? null : MetamacInternalWeb.getCoreMessages().getString(
                MetamacInternalWeb.getCoreMessages().typeDimensionComponent() + dimensionComponentDto.getTypeDimensionComponent().toString());
        staticTypeItemEdit.setValue(value);
        if (dimensionComponentDto.getId() == null) {
            typeItem.show();
            staticTypeItemEdit.hide();
        } else {
            staticTypeItemEdit.show();
            typeItem.hide();
        }

        // Concept
        conceptItem.clearValue(); // Clear concept value: which is the scheme of a concept?

        // Role
        roleItem.clearValue();
        roleItem.setRoleConcepts(dimensionComponentDto.getRole());

        // Representation
        codeListItem.clearValue();
        conceptSchemeItem.clearValue();
        representationTypeItem.clearValue();
        facetForm.clearValues();
        if (dimensionComponentDto.getLocalRepresentation() != null) {
            if (RepresentationTypeEnum.ENUMERATED.equals(dimensionComponentDto.getLocalRepresentation().getTypeRepresentationEnum())) {
                representationTypeItem.setValue(RepresentationTypeEnum.ENUMERATED.toString());
                // Code List
                if (TypeExternalArtefactsEnum.CODELIST.equals(dimensionComponentDto.getLocalRepresentation().getEnumerated().getType())) {
                    if (!TypeDimensionComponent.MEASUREDIMENSION.equals(dimensionComponentDto.getTypeDimensionComponent())) {
                        // codeListItem.setValue(dimensionComponentDto.getLocalRepresentation().getEnumCodeList().getCodeId());
                        codeListItem.clearValue(); // don´t know the concept (which is the scheme?), so code list neither
                    }
                    // ConceptScheme
                } else if (TypeExternalArtefactsEnum.CONCEPT_SCHEME.equals(dimensionComponentDto.getLocalRepresentation().getEnumerated().getType())) {
                    if (TypeDimensionComponent.MEASUREDIMENSION.equals(dimensionComponentDto.getTypeDimensionComponent())) {
                        conceptSchemeItem.setValue(dimensionComponentDto.getLocalRepresentation().getEnumerated().getCodeId());
                    }
                }
                // Facet
            } else if (RepresentationTypeEnum.NON_NUMERATED.equals(dimensionComponentDto.getLocalRepresentation().getTypeRepresentationEnum())) {
                representationTypeItem.setValue(RepresentationTypeEnum.NON_NUMERATED.toString());
                FacetDto facetDto = dimensionComponentDto.getLocalRepresentation().getNonEnumerated();
                facetForm.setFacet(facetDto);
            }
        }
        FacetFormUtils.setFacetFormVisibility(facetForm, representationTypeItem.getValueAsString());
        form.redraw();

        // Annotations
        editionAnnotationsPanel.setAnnotations(dimensionComponentDto.getAnnotations());
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
        return deleteConfirmationWindow.getYesButton();
    }

    @Override
    public void onDimensionSaved(DimensionComponentDto dimensionComponentDto) {
        this.dimensionComponentDto = dimensionComponentDto;
        dimensionsGrid.removeSelectedData();
        DimensionRecord record = RecordUtils.getDimensionRecord(dimensionComponentDto);
        dimensionsGrid.addData(record);
        dimensionsGrid.selectRecord(record);
        mainFormLayout.setViewMode();
    }

    @Override
    public HasChangeHandlers onConceptSchemeChange() {
        return conceptItem.getSchemeItem();
    }

    @Override
    public HasChangeHandlers onRoleConceptSchemeChange() {
        return roleItem.getConceptSchemeItem();
    }

    @Override
    public HasChangeHandlers onConceptChange() {
        return conceptItem.getItem();
    }

    @Override
    public HasSelectionChangedHandlers onDimensionSelected() {
        return dimensionsGrid;
    }

    @Override
    public HasChangeHandlers onRepresentationTypeChange() {
        return representationTypeItem;
    }

    /**
     * Select dimension in ListGrid
     * 
     * @param dimensionSelected
     */
    private void selectDimension(DimensionComponentDto dimensionSelected) {
        if (dimensionSelected.getId() == null) {
            // New dimension
            mainFormLayout.setTitleLabelContents(new String());
            deleteToolStripButton.hide();
            dimensionsGrid.deselectAllRecords();
            setDimensionEditionMode(dimensionSelected);
            mainFormLayout.setEditionMode();
        } else {
            mainFormLayout.setTitleLabelContents(dimensionSelected.getIdLogic());
            deleteToolStripButton.show();
            setDimension(dimensionSelected);
            mainFormLayout.setViewMode();
        }

        // Clear errors
        form.clearErrors(true);
        facetForm.clearErrors(true);

        selectedComponentLayout.show();
        selectedComponentLayout.redraw();
    }

    /**
     * DeSelect dimension in ListGrid
     */
    private void deselectDimension() {
        selectedComponentLayout.hide();
        deleteToolStripButton.hide();
    }

    private void setTranslationsShowed(boolean translationsShowed) {
        viewAnnotationsPanel.setTranslationsShowed(translationsShowed);
        editionAnnotationsPanel.setTranslationsShowed(translationsShowed);
    }

}
