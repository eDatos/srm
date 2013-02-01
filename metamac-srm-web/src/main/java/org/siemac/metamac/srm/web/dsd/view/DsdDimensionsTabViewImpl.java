package org.siemac.metamac.srm.web.dsd.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.constants.SrmWebConstants;
import org.siemac.metamac.srm.web.client.representation.widgets.StaticFacetForm;
import org.siemac.metamac.srm.web.client.utils.FacetFormUtils;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.client.widgets.AnnotationsPanel;
import org.siemac.metamac.srm.web.client.widgets.RelatedResourceListItem;
import org.siemac.metamac.srm.web.client.widgets.SearchMultipleRelatedResourcePaginatedWindow;
import org.siemac.metamac.srm.web.client.widgets.SearchRelatedResourcePaginatedWindow;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeDS;
import org.siemac.metamac.srm.web.dsd.model.ds.DimensionDS;
import org.siemac.metamac.srm.web.dsd.model.record.DimensionRecord;
import org.siemac.metamac.srm.web.dsd.presenter.DsdDimensionsTabPresenter;
import org.siemac.metamac.srm.web.dsd.utils.CommonUtils;
import org.siemac.metamac.srm.web.dsd.utils.DsdClientSecurityUtils;
import org.siemac.metamac.srm.web.dsd.utils.RecordUtils;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdDimensionsTabUiHandlers;
import org.siemac.metamac.srm.web.dsd.widgets.DsdFacetForm;
import org.siemac.metamac.srm.web.dsd.widgets.NewDimensionWindow;
import org.siemac.metamac.srm.web.dsd.widgets.RoleSelectItem;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.utils.ExternalItemUtils;
import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.SearchViewTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DimensionComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.FacetDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.RepresentationDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.FacetValueTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeComponent;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeDimensionComponent;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeRepresentationEnum;
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
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
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

    private ProcStatusEnum                               procStatus;

    private DimensionComponentDto                        dimensionComponentDto;
    private List<ExternalItemDto>                        codeLists;

    private VLayout                                      panel;
    private VLayout                                      selectedComponentLayout;
    private ListGrid                                     dimensionsGrid;
    private InternationalMainFormLayout                  mainFormLayout;

    private AnnotationsPanel                             viewAnnotationsPanel;
    private AnnotationsPanel                             editionAnnotationsPanel;

    // VIEW FORM

    private GroupDynamicForm                             form;
    private ViewTextItem                                 staticCode;
    private ViewTextItem                                 staticRoleItem;
    // private StaticTextItem staticPositionItem;
    // Representation
    private ViewTextItem                                 staticRepresentationTypeItem;
    private ViewTextItem                                 staticCodeListItem;
    private StaticFacetForm                              staticFacetForm;

    // EDITION FORM

    private GroupDynamicForm                             editionForm;
    private RequiredTextItem                             code;
    private ViewTextItem                                 staticCodeEdit;
    private RoleSelectItem                               roleItem;
    private SearchMultipleRelatedResourcePaginatedWindow searchRolesWindow;
    // Representation
    private CustomSelectItem                             representationTypeItem;
    private CustomSelectItem                             codeListItem;
    private DsdFacetForm                                 facetForm;

    private ToolStripButton                              newToolStripButton;
    private ToolStripButton                              deleteToolStripButton;

    private DeleteConfirmationWindow                     deleteConfirmationWindow;

    private NewDimensionWindow                           newDimensionWindow;
    private SearchRelatedResourcePaginatedWindow         searchConceptWindow;
    private SearchRelatedResourcePaginatedWindow         searchMeasureDimensionEnumeratedRepresentationWindow;

    @Inject
    public DsdDimensionsTabViewImpl() {
        super();
        panel = new VLayout();

        // ··················
        // List of dimensions
        // ··················

        // ToolStrip

        newToolStripButton = new ToolStripButton(MetamacSrmWeb.getConstants().actionNew(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.newListGrid().getURL());
        newToolStripButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                selectDimension(new DimensionComponentDto());
            }
        });

        deleteConfirmationWindow = new DeleteConfirmationWindow(MetamacSrmWeb.getConstants().dsdDeleteConfirmationTitle(), MetamacSrmWeb.getConstants().dsdDimensionDeleteConfirmation());
        deleteConfirmationWindow.setVisibility(Visibility.HIDDEN);

        deleteToolStripButton = new ToolStripButton(MetamacSrmWeb.getConstants().actionDelete(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.deleteListGrid().getURL());
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
        ListGridField idLogicField = new ListGridField(DimensionRecord.CODE, MetamacSrmWeb.getConstants().dsdDimensionsId());
        ListGridField typeField = new ListGridField(DimensionRecord.TYPE, MetamacSrmWeb.getConstants().dsdDimensionsType());
        ListGridField conceptField = new ListGridField(DimensionRecord.CONCEPT, MetamacSrmWeb.getConstants().concept());
        dimensionsGrid.setFields(idLogicField, typeField, conceptField);
        // ToolTip
        idLogicField.setShowHover(true);
        idLogicField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                DimensionRecord dimensionRecord = (DimensionRecord) record;
                return dimensionRecord.getCode();
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
                        showDeleteToolStripButton();
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
        form = new GroupDynamicForm(MetamacSrmWeb.getConstants().dsdDimensionDetails());
        staticCode = new ViewTextItem(DimensionDS.CODE_VIEW, MetamacSrmWeb.getConstants().dsdDimensionsId());
        ViewTextItem dimensionType = new ViewTextItem(DimensionDS.TYPE_VIEW, MetamacSrmWeb.getConstants().dsdDimensionsType());
        ViewTextItem concept = new ViewTextItem(DimensionDS.CONCEPT_VIEW, MetamacSrmWeb.getConstants().concept());

        // TODO Role: remove this item
        staticRoleItem = new ViewTextItem(DimensionDS.ROLE + "temp", MetamacSrmWeb.getConstants().dsdDimensionsRole());
        staticRoleItem.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return CommonUtils.isDimensionRoleVisible(dimensionComponentDto.getTypeDimensionComponent());
            }
        });
        RelatedResourceListItem conceptRoleItem = new RelatedResourceListItem(DimensionDS.ROLE, MetamacSrmWeb.getConstants().dsdDimensionsRole(), false);
        conceptRoleItem.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return CommonUtils.isDimensionRoleVisible(dimensionComponentDto.getTypeDimensionComponent());
            }
        });

        // staticPositionItem = new StaticTextItem("position-dim-view", MetamacSrmWeb.getConstants().dsdDimensionsPosition());
        staticRepresentationTypeItem = new ViewTextItem(DimensionDS.REPRESENTATION_TYPE, MetamacSrmWeb.getConstants().representation());
        staticCodeListItem = new ViewTextItem(DimensionDS.ENUMERATED_REPRESENTATION_CODE_LIST, MetamacSrmWeb.getConstants().dsdCodeList());
        ViewTextItem conceptScheme = new ViewTextItem(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME_VIEW, MetamacSrmWeb.getConstants().conceptScheme());

        ViewTextItem urn = new ViewTextItem(DimensionDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(DimensionDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());

        form.setFields(staticCode, dimensionType, concept, staticRoleItem, conceptRoleItem, staticRepresentationTypeItem, staticCodeListItem, conceptScheme, urn, urnProvider);

        staticFacetForm = new StaticFacetForm();

        // Annotations
        viewAnnotationsPanel = new AnnotationsPanel(true);

        mainFormLayout.addViewCanvas(form);
        mainFormLayout.addViewCanvas(staticFacetForm);
        mainFormLayout.addViewCanvas(viewAnnotationsPanel);
    }

    /**
     * Creates and returns the edition layout
     * 
     * @return
     */
    private void createEditionForm() {
        editionForm = new GroupDynamicForm(getConstants().dsdDimensionDetails());

        // Code

        code = new RequiredTextItem(DimensionDS.CODE, getConstants().dsdDimensionsId());
        code.setRedrawOnChange(true);
        code.setValidators(SemanticIdentifiersUtils.getDimensionIdentifierCustomValidator());
        code.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return CommonUtils.canDimensionCodeBeEdited(editionForm.getValueAsString(DimensionDS.TYPE));
            }
        });

        staticCodeEdit = new ViewTextItem(DimensionDS.CODE_VIEW, getConstants().dsdDimensionsId());
        staticCodeEdit.setRedrawOnChange(true);
        staticCodeEdit.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !CommonUtils.canDimensionCodeBeEdited(editionForm.getValueAsString(DimensionDS.TYPE));
            }
        });

        // Type

        ViewTextItem dimensionType = new ViewTextItem(DimensionDS.TYPE, getConstants().dsdDimensionsType());
        dimensionType.setShowIfCondition(FormItemUtils.getFalseFormItemIfFunction());
        ViewTextItem dimensionTypeView = new ViewTextItem(DimensionDS.TYPE_VIEW, getConstants().dsdDimensionsType());

        // Concept
        ViewTextItem concept = new ViewTextItem(DimensionDS.CONCEPT, MetamacSrmWeb.getConstants().concept());
        concept.setShowIfCondition(FormItemUtils.getFalseFormItemIfFunction());
        SearchViewTextItem conceptView = createConceptItem(DimensionDS.CONCEPT_VIEW, MetamacSrmWeb.getConstants().concept());

        // Role

        // TODO Remove this item
        roleItem = new RoleSelectItem(DimensionDS.ROLE + "temp", getConstants().dsdDimensionsRole());
        roleItem.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return CommonUtils.isDimensionRoleVisible(dimensionComponentDto.getTypeDimensionComponent());
            }
        });

        RelatedResourceListItem conceptRoleItem = createRoleItem(DimensionDS.ROLE, getConstants().dsdDimensionsRole());
        conceptRoleItem.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return CommonUtils.isDimensionRoleVisible(dimensionComponentDto.getTypeDimensionComponent());
            }
        });

        // Representation

        representationTypeItem = new CustomSelectItem(DimensionDS.REPRESENTATION_TYPE, getConstants().representation());
        representationTypeItem.setValueMap(org.siemac.metamac.srm.web.client.utils.CommonUtils.getTypeRepresentationEnumHashMap());
        representationTypeItem.setRedrawOnChange(true);
        representationTypeItem.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                // Show FacetForm if RepresentationTypeEnum = NON_NUMERATED
                FacetFormUtils.setFacetFormVisibility(facetForm, representationTypeItem.getValueAsString());
            }
        });
        CustomValidator measureCustomValidator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                // Measure dimensions must be enumerated
                if (CommonUtils.isDimensionTypeMeasureDimension(editionForm.getValueAsString(DimensionDS.TYPE))) {
                    return TypeRepresentationEnum.ENUMERATED.toString().equals(value) ? true : false;
                }
                return true;
            }
        };
        measureCustomValidator.setErrorMessage(MetamacSrmWeb.getMessages().errorRequiredEnumeratedRepresentationInMeasureDimension());
        CustomValidator timeCustomValidator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                // Time dimensions must have a non enumerated representation
                if (CommonUtils.isDimensionTypeTimeDimension(editionForm.getValueAsString(DimensionDS.TYPE))) {
                    return TypeRepresentationEnum.TEXT_FORMAT.toString().equals(value) ? true : false;
                }
                return true;
            }
        };
        timeCustomValidator.setErrorMessage(MetamacSrmWeb.getMessages().errorRequiredNonEnumeratedRepresentationInTimeDimension());
        representationTypeItem.setValidators(measureCustomValidator, timeCustomValidator);

        // Codelist

        codeListItem = new CustomSelectItem(DimensionDS.ENUMERATED_REPRESENTATION_CODE_LIST, MetamacSrmWeb.getConstants().dsdCodeList());
        codeListItem.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                // Show CodeList if RepresentationTypeEnum = ENUMERATED (except in MeasureDimension)
                return CommonUtils.isRepresentationTypeEnumerated(representationTypeItem.getValueAsString())
                        && !CommonUtils.isDimensionTypeMeasureDimension(editionForm.getValueAsString(DimensionDS.TYPE));
            }
        });

        // ConceptScheme

        ViewTextItem conceptScheme = new ViewTextItem(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME, MetamacSrmWeb.getConstants().conceptScheme());
        conceptScheme.setShowIfCondition(FormItemUtils.getFalseFormItemIfFunction());
        SearchViewTextItem conceptSchemeView = createMeasureDimensionEnumeratedRepresentationItem(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME_VIEW, MetamacSrmWeb.getConstants()
                .conceptScheme());

        ViewTextItem urn = new ViewTextItem(DimensionDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(DimensionDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());

        editionForm.setFields(code, staticCodeEdit, dimensionType, dimensionTypeView, concept, conceptView, roleItem, conceptRoleItem, representationTypeItem, codeListItem, conceptScheme,
                conceptSchemeView, urn, urnProvider);

        // Facet Form

        facetForm = new DsdFacetForm();
        facetForm.setVisibility(Visibility.HIDDEN);
        // TextType applicable to TimeDimension is restricted to those that represent time
        CustomValidator timeValidator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                if (CommonUtils.isDimensionTypeTimeDimension(editionForm.getValueAsString(DimensionDS.TYPE))) {
                    if (value != null) {
                        FacetValueTypeEnum f = FacetValueTypeEnum.valueOf((String) value);
                        return FacetFormUtils.representsTime(f) ? true : false;
                    }
                }
                return true;
            }
        };
        timeValidator.setErrorMessage(MetamacSrmWeb.getMessages().errorTextTypeInTimeDimension());
        facetForm.getTextType().setValidateOnChange(true);
        facetForm.getTextType().setRedrawOnChange(true);
        facetForm.getTextType().setValidators(timeValidator);

        // Annotations
        editionAnnotationsPanel = new AnnotationsPanel(false);

        mainFormLayout.addEditionCanvas(editionForm);
        mainFormLayout.addEditionCanvas(facetForm);
        mainFormLayout.addEditionCanvas(editionAnnotationsPanel);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setDsdDimensions(ProcStatusEnum procStatus, List<DimensionComponentDto> dimensionComponentDtos) {
        this.procStatus = procStatus;
        deselectDimension();

        // Security
        newToolStripButton.setVisibility(DsdClientSecurityUtils.canUpdateDimensions(procStatus) ? Visibility.VISIBLE : Visibility.HIDDEN);
        mainFormLayout.setCanEdit(DsdClientSecurityUtils.canUpdateDimensions(procStatus));

        dimensionsGrid.selectAllRecords();
        dimensionsGrid.removeSelectedData();
        dimensionsGrid.deselectAllRecords();
        for (DimensionComponentDto dimensionComponentDto : dimensionComponentDtos) {
            DimensionRecord dimensionRecord = RecordUtils.getDimensionRecord(dimensionComponentDto);
            dimensionsGrid.addData(dimensionRecord);
        }
    }

    @Override
    public void setConceptSchemes(GetRelatedResourcesResult result) {
        if (searchConceptWindow != null) {
            searchConceptWindow.getInitialSelectionItem().setValueMap(org.siemac.metamac.srm.web.client.utils.CommonUtils.getRelatedResourceHashMap(result.getRelatedResourceDtos()));
        }
    }

    @Override
    public void setConcepts(GetRelatedResourcesResult result) {
        if (searchConceptWindow != null) {
            searchConceptWindow.setRelatedResources(result.getRelatedResourceDtos());
            searchConceptWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getRelatedResourceDtos().size(), result.getTotalResults());
        }
    }

    @Override
    public void setConceptSchemesForMeasureDimensionEnumeratedRepresentation(GetRelatedResourcesResult result) {
        if (searchMeasureDimensionEnumeratedRepresentationWindow != null) {
            searchMeasureDimensionEnumeratedRepresentationWindow.setRelatedResources(result.getRelatedResourceDtos());
            searchMeasureDimensionEnumeratedRepresentationWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getRelatedResourceDtos().size(), result.getTotalResults());
        }
    }

    @Override
    public void setConceptsAsRole(List<RelatedResourceDto> concepts, int firstResult, int totalResults) {
        if (searchRolesWindow != null) {
            searchRolesWindow.setSourceRelatedResources(concepts);
            searchRolesWindow.refreshSourcePaginationInfo(firstResult, concepts.size(), totalResults);
        }
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
    public DimensionComponentDto getDsdDimension() {
        // Id
        dimensionComponentDto.setCode(code.getVisible() ? code.getValueAsString() : null);

        // Type
        dimensionComponentDto.setTypeDimensionComponent(TypeDimensionComponent.valueOf(editionForm.getValueAsString(DimensionDS.TYPE)));

        // Role
        dimensionComponentDto.getRole().clear();
        if (!TypeDimensionComponent.TIMEDIMENSION.equals(dimensionComponentDto.getTypeDimensionComponent())) {
            List<ExternalItemDto> roleConcepts = roleItem.getSelectedConcepts();
            dimensionComponentDto.getRole().addAll(roleConcepts);
        }
        // TODO Return roles as RelatedResourcesDto

        // Concept
        // TODO RelatedResourceDto instead of ExternalItemDto
        // dimensionComponentDto.setCptIdRef(StringUtils.isBlank(editionForm.getValueAsString(DimensionDS.CONCEPT)) ? null :
        // RelatedResourceUtils.createRelatedResourceDto(TypeExternalArtefactsEnum.CONCEPT,
        // editionForm.getValueAsString(DimensionDS.CONCEPT)));
        dimensionComponentDto.setCptIdRef(StringUtils.isBlank(editionForm.getValueAsString(DimensionDS.CONCEPT)) ? null : RelatedResourceUtils.createExternalItemDto(TypeExternalArtefactsEnum.CONCEPT,
                editionForm.getValueAsString(DimensionDS.CONCEPT)));

        // Representation
        if (representationTypeItem.getValue() != null && !representationTypeItem.getValue().toString().isEmpty()) {
            TypeRepresentationEnum representationType = TypeRepresentationEnum.valueOf(representationTypeItem.getValue().toString());

            if (dimensionComponentDto.getLocalRepresentation() == null) {
                dimensionComponentDto.setLocalRepresentation(new RepresentationDto());
            }

            // Code List
            if (TypeRepresentationEnum.ENUMERATED.equals(representationType)) {
                dimensionComponentDto.getLocalRepresentation().setTypeRepresentationEnum(TypeRepresentationEnum.ENUMERATED);
                if (TypeDimensionComponent.MEASUREDIMENSION.equals(dimensionComponentDto.getTypeDimensionComponent())) {
                    // TODO RelatedResourceDto instead of ExternalItemDto
                    dimensionComponentDto.getLocalRepresentation().setEnumerated(
                            StringUtils.isBlank(editionForm.getValueAsString(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME)) ? null : RelatedResourceUtils.createExternalItemDto(
                                    TypeExternalArtefactsEnum.CONCEPT_SCHEME, editionForm.getValueAsString(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME)));
                } else {
                    dimensionComponentDto.getLocalRepresentation().setEnumerated(ExternalItemUtils.getExternalItemDtoFromUrn(codeLists, codeListItem.getValueAsString()));
                }
                dimensionComponentDto.getLocalRepresentation().setNonEnumerated(null);
                // Facet
            } else if (TypeRepresentationEnum.TEXT_FORMAT.equals(representationType)) {
                dimensionComponentDto.getLocalRepresentation().setTypeRepresentationEnum(TypeRepresentationEnum.TEXT_FORMAT);
                FacetDto facetDto = facetForm.getFacet();
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
        staticCode.setValue(dimensionComponentDto.getCode());
        // URNs
        form.setValue(DimensionDS.URN, dimensionComponentDto.getUrn());
        form.setValue(DimensionDS.URN_PROVIDER, dimensionComponentDto.getUrnProvider());
        // Type
        form.setValue(DimensionDS.TYPE_VIEW, CommonUtils.getTypeDimensionComponentName(dimensionComponentDto.getTypeDimensionComponent()));
        // Concept
        form.setValue(DimensionDS.CONCEPT_VIEW, ExternalItemUtils.getExternalItemName(dimensionComponentDto.getCptIdRef())); // TODO RelatedResourceDto instead of ExternalItemDto

        // Role
        staticRoleItem.hide();
        staticRoleItem.clearValue();
        if (!TypeDimensionComponent.TIMEDIMENSION.equals(dimensionComponentDto.getTypeDimensionComponent())) {
            List<ExternalItemDto> roleConcepts = new ArrayList<ExternalItemDto>(dimensionComponentDto.getRole());
            staticRoleItem.setValue(CommonUtils.getRoleListToString(roleConcepts));
            staticRoleItem.show();
        }
        // Position
        // staticPositionItem.setValue(dimensionComponentDto.getOrderLogic().toString());
        // Representation
        staticFacetForm.hide();
        staticCodeListItem.hide();
        staticCodeListItem.clearValue();
        form.getItem(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME_VIEW).hide();
        staticRepresentationTypeItem.clearValue();
        staticFacetForm.clearValues();
        if (dimensionComponentDto.getLocalRepresentation() != null) {
            if (TypeRepresentationEnum.ENUMERATED.equals(dimensionComponentDto.getLocalRepresentation().getTypeRepresentationEnum())) {
                // Code List
                if (TypeExternalArtefactsEnum.CODELIST.equals(dimensionComponentDto.getLocalRepresentation().getEnumerated().getType())) {
                    staticRepresentationTypeItem.setValue(MetamacSrmWeb.getCoreMessages().typeRepresentationEnumENUMERATED());
                    if (!TypeDimensionComponent.MEASUREDIMENSION.equals(dimensionComponentDto.getTypeDimensionComponent())) {
                        staticCodeListItem.setValue(dimensionComponentDto.getLocalRepresentation().getEnumerated().getCode());
                        staticCodeListItem.show();
                    }
                    // ConceptScheme
                } else if (TypeExternalArtefactsEnum.CONCEPT_SCHEME.equals(dimensionComponentDto.getLocalRepresentation().getEnumerated().getType())) {
                    staticRepresentationTypeItem.setValue(MetamacSrmWeb.getCoreMessages().typeRepresentationEnumENUMERATED());
                    if (TypeDimensionComponent.MEASUREDIMENSION.equals(dimensionComponentDto.getTypeDimensionComponent())) {
                        form.setValue(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME_VIEW, ExternalItemUtils.getExternalItemName(dimensionComponentDto.getLocalRepresentation().getEnumerated())); // TODO
                                                                                                                                                                                                         // RelatedResourceDto
                                                                                                                                                                                                         // instead
                                                                                                                                                                                                         // of
                                                                                                                                                                                                         // ExternalItemDto
                        form.getItem(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME_VIEW).show();
                    }
                }
                // Facet
            } else if (TypeRepresentationEnum.TEXT_FORMAT.equals(dimensionComponentDto.getLocalRepresentation().getTypeRepresentationEnum())) {
                staticRepresentationTypeItem.setValue(MetamacSrmWeb.getCoreMessages().typeRepresentationEnumTEXT_FORMAT());
                FacetDto facetDto = dimensionComponentDto.getLocalRepresentation().getNonEnumerated();
                staticFacetForm.setFacet(facetDto);
                staticFacetForm.show();
            }
        }

        // Annotations
        viewAnnotationsPanel.setAnnotations(dimensionComponentDto.getAnnotations());

        form.markForRedraw();
    }

    private void setDimensionEditionMode(DimensionComponentDto dimensionComponentDto) {
        this.dimensionComponentDto = dimensionComponentDto;
        // Id
        code.setValue(dimensionComponentDto.getCode());
        staticCodeEdit.setValue(dimensionComponentDto.getCode());
        // URNs
        editionForm.setValue(DimensionDS.URN, dimensionComponentDto.getUrn());
        editionForm.setValue(DimensionDS.URN_PROVIDER, dimensionComponentDto.getUrnProvider());
        // Type
        editionForm.setValue(DimensionDS.TYPE, dimensionComponentDto.getTypeDimensionComponent().name());
        editionForm.setValue(DimensionDS.TYPE_VIEW, CommonUtils.getTypeDimensionComponentName(dimensionComponentDto.getTypeDimensionComponent()));

        // Concept
        editionForm.setValue(DimensionDS.CONCEPT, dimensionComponentDto.getCptIdRef() != null ? dimensionComponentDto.getCptIdRef().getUrn() : null);
        editionForm.setValue(DimensionDS.CONCEPT_VIEW, ExternalItemUtils.getExternalItemName(dimensionComponentDto.getCptIdRef())); // TODO RelatedResourceDto instead of ExternalItemDto

        // Role
        roleItem.clearValue();
        roleItem.setRoleConcepts(dimensionComponentDto.getRole());

        // Representation
        codeListItem.clearValue();
        editionForm.getItem(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME).clearValue();
        editionForm.getItem(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME_VIEW).clearValue();
        representationTypeItem.clearValue();
        facetForm.clearValues();
        if (dimensionComponentDto.getLocalRepresentation() != null) {
            if (TypeRepresentationEnum.ENUMERATED.equals(dimensionComponentDto.getLocalRepresentation().getTypeRepresentationEnum())) {
                representationTypeItem.setValue(TypeRepresentationEnum.ENUMERATED.toString());
                // Code List
                if (TypeExternalArtefactsEnum.CODELIST.equals(dimensionComponentDto.getLocalRepresentation().getEnumerated().getType())) {
                    if (!TypeDimensionComponent.MEASUREDIMENSION.equals(dimensionComponentDto.getTypeDimensionComponent())) {
                        // codeListItem.setValue(dimensionComponentDto.getLocalRepresentation().getEnumCodeList().getCodeId());
                        codeListItem.clearValue(); // don´t know the concept (which is the scheme?), so code list neither
                    }
                    // ConceptScheme
                } else if (TypeExternalArtefactsEnum.CONCEPT_SCHEME.equals(dimensionComponentDto.getLocalRepresentation().getEnumerated().getType())) {
                    if (TypeDimensionComponent.MEASUREDIMENSION.equals(dimensionComponentDto.getTypeDimensionComponent())) {
                        editionForm.setValue(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME, dimensionComponentDto.getLocalRepresentation().getEnumerated().getUrn());
                        editionForm.setValue(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME_VIEW,
                                ExternalItemUtils.getExternalItemName(dimensionComponentDto.getLocalRepresentation().getEnumerated()));
                    }
                }
                // Facet
            } else if (TypeRepresentationEnum.TEXT_FORMAT.equals(dimensionComponentDto.getLocalRepresentation().getTypeRepresentationEnum())) {
                representationTypeItem.setValue(TypeRepresentationEnum.TEXT_FORMAT.toString());
                FacetDto facetDto = dimensionComponentDto.getLocalRepresentation().getNonEnumerated();
                facetForm.setFacet(facetDto);
            }
        }
        FacetFormUtils.setFacetFormVisibility(facetForm, representationTypeItem.getValueAsString());

        // Annotations
        editionAnnotationsPanel.setAnnotations(dimensionComponentDto.getAnnotations());

        editionForm.markForRedraw();
    }

    @Override
    public boolean validate() {
        return Visibility.HIDDEN.equals(facetForm.getVisibility()) ? editionForm.validate(false) : (editionForm.validate(false) && facetForm.validate(false));
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
    public HasChangeHandlers onRoleConceptSchemeChange() {
        return roleItem.getConceptSchemeItem();
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
    private void selectDimension(final DimensionComponentDto dimensionSelected) {

        if (dimensionSelected.getId() == null) {

            // Create dimension
            newDimensionWindow = new NewDimensionWindow(getConstants().dsdDimensionCreate());
            newDimensionWindow.getAccept().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                @Override
                public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                    if (newDimensionWindow.validateForm()) {
                        TypeDimensionComponent dimensionType = newDimensionWindow.getDimensionType();
                        dimensionSelected.setTypeDimensionComponent(dimensionType);

                        // Set dimension in form
                        mainFormLayout.setTitleLabelContents(StringUtils.EMPTY);
                        deleteToolStripButton.hide();
                        dimensionsGrid.deselectAllRecords();
                        setDimensionEditionMode(dimensionSelected);
                        mainFormLayout.setEditionMode();

                        newDimensionWindow.markForDestroy();

                        selectedComponentLayout.show();
                        selectedComponentLayout.markForRedraw();
                    }
                }
            });

        } else {

            // Update dimension
            mainFormLayout.setTitleLabelContents(dimensionSelected.getCode());
            showDeleteToolStripButton();
            setDimension(dimensionSelected);
            mainFormLayout.setViewMode();

            selectedComponentLayout.show();
            selectedComponentLayout.markForRedraw();
        }

        // Clear errors
        editionForm.clearErrors(true);
        facetForm.clearErrors(true);
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

    private void showDeleteToolStripButton() {
        if (DsdClientSecurityUtils.canUpdateDimensions(procStatus)) {
            deleteToolStripButton.show();
        }
    }

    private RelatedResourceListItem createRoleItem(String name, String title) {
        RelatedResourceListItem relatedResources = new RelatedResourceListItem(name, title, true);
        relatedResources.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                final int ROLES_FIRST_RESULT = 0;
                final int ROLES_MAX_RESULTS = 5;
                searchRolesWindow = new SearchMultipleRelatedResourcePaginatedWindow(MetamacSrmWeb.getConstants().dsdDimensionsRole(), ROLES_MAX_RESULTS, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        getUiHandlers().retrieveConceptsAsRole(firstResult, maxResults, null);

                    }
                });
                getUiHandlers().retrieveConceptsAsRole(ROLES_FIRST_RESULT, ROLES_MAX_RESULTS, null);
                searchRolesWindow.setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                        getUiHandlers().retrieveConceptsAsRole(firstResult, maxResults, criteria);
                    }
                });
                searchRolesWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        List<RelatedResourceDto> selectedRelatedResources = searchRolesWindow.getSelectedRelatedResources();
                        ((RelatedResourceListItem) editionForm.getItem(DimensionDS.ROLE)).setRelatedResources(selectedRelatedResources);
                        searchRolesWindow.markForDestroy();
                    }
                });
            }
        });
        return relatedResources;
    }

    private SearchViewTextItem createConceptItem(String name, String title) {
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;
        final SearchViewTextItem conceptItem = new SearchViewTextItem(name, title);
        conceptItem.setRequired(true);
        conceptItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                final TypeDimensionComponent dimensionType = dimensionComponentDto.getTypeDimensionComponent();

                SelectItem conceptSchemeSelectItem = new SelectItem(ConceptSchemeDS.URN, getConstants().conceptScheme());
                searchConceptWindow = new SearchRelatedResourcePaginatedWindow(getConstants().conceptSelection(), MAX_RESULTS, conceptSchemeSelectItem, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        getUiHandlers().retrieveConcepts(dimensionType, firstResult, maxResults, searchConceptWindow.getRelatedResourceCriteria(), searchConceptWindow.getInitialSelectionValue());
                    }
                });

                // Load concept schemes and concepts (to populate the selection window)
                getUiHandlers().retrieveConceptSchemes(dimensionType, FIRST_RESULST, SrmWebConstants.NO_LIMIT_IN_PAGINATION);
                getUiHandlers().retrieveConcepts(dimensionType, FIRST_RESULST, MAX_RESULTS, null, null);

                searchConceptWindow.getInitialSelectionItem().addChangedHandler(new ChangedHandler() {

                    @Override
                    public void onChanged(ChangedEvent event) {
                        getUiHandlers().retrieveConcepts(dimensionType, FIRST_RESULST, MAX_RESULTS, searchConceptWindow.getRelatedResourceCriteria(), searchConceptWindow.getInitialSelectionValue());
                    }
                });

                searchConceptWindow.getListGridItem().getListGrid().setSelectionType(SelectionStyle.SINGLE);
                searchConceptWindow.getListGridItem().setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String concept) {
                        getUiHandlers().retrieveConcepts(dimensionType, firstResult, maxResults, concept, searchConceptWindow.getInitialSelectionValue());
                    }
                });

                searchConceptWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        RelatedResourceDto selectedConcept = searchConceptWindow.getSelectedRelatedResource();
                        searchConceptWindow.markForDestroy();
                        // Set selected concepts in form
                        editionForm.setValue(DimensionDS.CONCEPT, selectedConcept != null ? selectedConcept.getUrn() : null);
                        editionForm.setValue(DimensionDS.CONCEPT_VIEW, selectedConcept != null ? org.siemac.metamac.srm.web.client.utils.CommonUtils.getRelatedResourceName(selectedConcept) : null);
                    }
                });
            }
        });
        // Set requited with a custom validator
        CustomValidator customValidator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                if (conceptItem.getValue() != null) {
                    String conceptValue = String.valueOf(conceptItem.getValue());
                    return !StringUtils.isBlank(conceptValue);
                }
                return false;
            }
        };
        conceptItem.setValidators(customValidator);
        return conceptItem;
    }

    private SearchViewTextItem createMeasureDimensionEnumeratedRepresentationItem(String name, String title) {
        final SearchViewTextItem conceptSchemeItem = new SearchViewTextItem(name, title);
        conceptSchemeItem.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                // Show ConceptScheme if RepresentationTypeEnum = ENUMERATED and TypeDimensionComponent == MEASUREDIMENSION
                return CommonUtils.isRepresentationTypeEnumerated(representationTypeItem.getValueAsString())
                        && CommonUtils.isDimensionTypeMeasureDimension(editionForm.getValueAsString(DimensionDS.TYPE));
            }
        });
        // Info icon
        FormItemIcon measureDimensionInfo = new FormItemIcon();
        measureDimensionInfo.setSrc(org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.info().getURL());
        measureDimensionInfo.setPrompt(getConstants().dsdMeasureDimensionRepresentation());
        conceptSchemeItem.setIconVAlign(VerticalAlignment.TOP);
        conceptSchemeItem.setIcons(conceptSchemeItem.getSearchIcon(), measureDimensionInfo);

        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;
        conceptSchemeItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                searchMeasureDimensionEnumeratedRepresentationWindow = new SearchRelatedResourcePaginatedWindow(getConstants().conceptSchemeSelection(), MAX_RESULTS, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        getUiHandlers().retrieveConceptSchemesForMeasureDimensionEnumeratedRepresentation(firstResult, maxResults,
                                searchMeasureDimensionEnumeratedRepresentationWindow.getRelatedResourceCriteria());
                    }
                });

                // Load concept schemes (to populate the selection window)
                getUiHandlers().retrieveConceptSchemesForMeasureDimensionEnumeratedRepresentation(FIRST_RESULST, MAX_RESULTS, null);

                searchMeasureDimensionEnumeratedRepresentationWindow.getListGridItem().getListGrid().setSelectionType(SelectionStyle.SINGLE);
                searchMeasureDimensionEnumeratedRepresentationWindow.getListGridItem().setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                        getUiHandlers().retrieveConceptSchemesForMeasureDimensionEnumeratedRepresentation(firstResult, maxResults, criteria);
                    }
                });

                searchMeasureDimensionEnumeratedRepresentationWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        RelatedResourceDto selectedConceptScheme = searchMeasureDimensionEnumeratedRepresentationWindow.getSelectedRelatedResource();
                        searchMeasureDimensionEnumeratedRepresentationWindow.markForDestroy();
                        // Set selected concept scheme in form
                        editionForm.setValue(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME, selectedConceptScheme != null ? selectedConceptScheme.getUrn() : null);
                        editionForm.setValue(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME_VIEW,
                                selectedConceptScheme != null ? org.siemac.metamac.srm.web.client.utils.CommonUtils.getRelatedResourceName(selectedConceptScheme) : null);
                    }
                });
            }
        });
        // Set requited with a custom validator
        CustomValidator customValidator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                if (conceptSchemeItem.getValue() != null) {
                    String conceptValue = String.valueOf(conceptSchemeItem.getValue());
                    return !StringUtils.isBlank(conceptValue);
                }
                return false;
            }
        };
        conceptSchemeItem.setValidators(customValidator);
        return conceptSchemeItem;
    }
}
