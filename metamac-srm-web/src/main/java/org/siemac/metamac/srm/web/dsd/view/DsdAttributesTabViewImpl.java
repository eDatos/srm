package org.siemac.metamac.srm.web.dsd.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

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
import org.siemac.metamac.srm.web.dsd.model.ds.DataAttributeDS;
import org.siemac.metamac.srm.web.dsd.model.record.AttributeRecord;
import org.siemac.metamac.srm.web.dsd.presenter.DsdAttributesTabPresenter;
import org.siemac.metamac.srm.web.dsd.utils.CommonUtils;
import org.siemac.metamac.srm.web.dsd.utils.DsdClientSecurityUtils;
import org.siemac.metamac.srm.web.dsd.utils.RecordUtils;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdAttributesTabUiHandlers;
import org.siemac.metamac.srm.web.dsd.widgets.DsdFacetForm;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.utils.ExternalItemUtils;
import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.InformationWindow;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.SearchViewTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
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
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.form.validator.RequiredIfFunction;
import com.smartgwt.client.widgets.form.validator.RequiredIfValidator;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class DsdAttributesTabViewImpl extends ViewWithUiHandlers<DsdAttributesTabUiHandlers> implements DsdAttributesTabPresenter.DsdAttributesTabView {

    private ProcStatusEnum                               procStatus;

    private DataAttributeDto                             dataAttributeDto;
    private List<DimensionComponentDto>                  dimensionComponentDtos;
    private List<DescriptorDto>                          descriptorDtos;                                 // Group Keys

    private VLayout                                      panel;
    private VLayout                                      selectedComponentLayout;
    private ListGrid                                     attributesGrid;

    private InternationalMainFormLayout                  mainFormLayout;

    private AnnotationsPanel                             viewAnnotationsPanel;
    private AnnotationsPanel                             editionAnnotationsPanel;

    // VIEW FORM

    private GroupDynamicForm                             form;
    private ViewTextItem                                 staticAssignmentStatusItem;
    // Relation
    private ViewTextItem                                 staticRelationType;
    private ViewTextItem                                 staticGroupKeysForDimensionRelationshipItem;
    private ViewTextItem                                 staticDimensionsForDimensionRelationshipItem;
    private ViewTextItem                                 staticGroupKeyFormForGroupRelationship;
    // Representation
    private ViewTextItem                                 staticRepresentationTypeItem;
    private StaticFacetForm                              staticFacetForm;

    // EDITION FORM

    private GroupDynamicForm                             editionForm;
    private RequiredSelectItem                           assignmentStatusItem;
    // Relation
    private RequiredSelectItem                           relationType;
    private CustomSelectItem                             groupKeysForDimensionRelationshipItem;
    private RequiredSelectItem                           dimensionsForDimensionRelationshipItem;         // Required if relationType == DIMENSION_RELATIONSHIP
    private RequiredSelectItem                           groupKeyFormForGroupRelationship;               // Required if relationType == GROUP_RELATIONSHIP
    // Representation
    private CustomSelectItem                             representationTypeItem;
    private DsdFacetForm                                 facetForm = null;

    private ToolStripButton                              newToolStripButton;
    private ToolStripButton                              deleteToolStripButton;

    private DeleteConfirmationWindow                     deleteConfirmationWindow;

    private SearchRelatedResourcePaginatedWindow         searchConceptWindow;
    private SearchMultipleRelatedResourcePaginatedWindow searchConceptsForRolesWindow;
    private SearchRelatedResourcePaginatedWindow         searchCodelistForEnumeratedRepresentationWindow;

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

        deleteConfirmationWindow = new DeleteConfirmationWindow(MetamacSrmWeb.getConstants().dsdDeleteConfirmationTitle(), MetamacSrmWeb.getConstants().dsdAttributeDeleteConfirmation());
        deleteConfirmationWindow.setVisibility(Visibility.HIDDEN);

        deleteToolStripButton = new ToolStripButton(MetamacSrmWeb.getConstants().actionDelete(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.deleteListGrid().getURL());
        deleteToolStripButton.setVisibility(Visibility.HIDDEN);
        deleteToolStripButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });

        ToolStrip attributeGridToolStrip = new ToolStrip();
        attributeGridToolStrip.setWidth100();
        attributeGridToolStrip.addButton(newToolStripButton);
        attributeGridToolStrip.addSeparator();
        attributeGridToolStrip.addButton(deleteToolStripButton);

        // Grid

        attributesGrid = new ListGrid();
        attributesGrid.setWidth100();
        attributesGrid.setHeight(150);
        attributesGrid.setSelectionType(SelectionStyle.SIMPLE);
        attributesGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
        ListGridField codeField = new ListGridField(AttributeRecord.CODE, MetamacSrmWeb.getConstants().dsdAttributeId());
        ListGridField assigmentField = new ListGridField(AttributeRecord.ASSIGNMENT, MetamacSrmWeb.getConstants().dsdAttributeAssignmentStatus());
        ListGridField attributeConceptField = new ListGridField(AttributeRecord.CONCEPT, MetamacSrmWeb.getConstants().concept());
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
        form = new GroupDynamicForm(MetamacSrmWeb.getConstants().dsdAttributeDetails());
        ViewTextItem staticCode = new ViewTextItem(DataAttributeDS.CODE, MetamacSrmWeb.getConstants().dsdAttributeId());
        ViewTextItem concept = new ViewTextItem(DataAttributeDS.CONCEPT_VIEW, MetamacSrmWeb.getConstants().concept());
        RelatedResourceListItem roleItem = new RelatedResourceListItem(DataAttributeDS.ROLE, MetamacSrmWeb.getConstants().dsdAttributeRole(), false);
        staticAssignmentStatusItem = new ViewTextItem(DataAttributeDS.ASSIGMENT_STATUS, MetamacSrmWeb.getConstants().dsdAttributeAssignmentStatus());
        staticRelationType = new ViewTextItem(DataAttributeDS.RELATED_WITH, MetamacSrmWeb.getConstants().dsdAttributeRelatedWith());
        staticGroupKeysForDimensionRelationshipItem = new ViewTextItem(DataAttributeDS.GROUP_KEY_FOR_DIMENSION_RELATIONSHIP, MetamacSrmWeb.getConstants()
                .dsdAttributeGroupKeysForDimensionRelationship());
        staticDimensionsForDimensionRelationshipItem = new ViewTextItem(DataAttributeDS.DIMENSION_FOR_DIMENSION_RELATIONSHIP, MetamacSrmWeb.getConstants()
                .dsdAttributeDimensionsForDimensionRelationship());
        staticGroupKeyFormForGroupRelationship = new ViewTextItem(DataAttributeDS.GROUP_KEY_FOR_GROUP_RELATIONSHIP, MetamacSrmWeb.getConstants().dsdAttributeGroupKeyFormGroupRelationship());
        staticRepresentationTypeItem = new ViewTextItem(DataAttributeDS.REPRESENTATION_TYPE, MetamacSrmWeb.getConstants().representation());
        ViewTextItem codelist = new ViewTextItem(DataAttributeDS.ENUMERATED_REPRESENTATION_CODELIST_VIEW, getConstants().codelist());
        ViewTextItem urn = new ViewTextItem(DataAttributeDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(DataAttributeDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        form.setFields(staticCode, staticAssignmentStatusItem, concept, roleItem, staticRelationType, staticGroupKeysForDimensionRelationshipItem, staticDimensionsForDimensionRelationshipItem,
                staticGroupKeyFormForGroupRelationship, staticRepresentationTypeItem, codelist, urn, urnProvider);

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

        // ····
        // Form
        // ····
        editionForm = new GroupDynamicForm(MetamacSrmWeb.getConstants().dsdAttributeDetails());

        // Id

        RequiredTextItem code = new RequiredTextItem(DataAttributeDS.CODE, MetamacSrmWeb.getConstants().dsdAttributeId());
        code.setValidators(SemanticIdentifiersUtils.getAttributeIdentifierCustomValidator());

        // Assignment Status

        assignmentStatusItem = new RequiredSelectItem(DataAttributeDS.ASSIGMENT_STATUS, MetamacSrmWeb.getConstants().dsdAttributeAssignmentStatus());
        assignmentStatusItem.setValueMap(CommonUtils.getUsageStatusHashMap());

        // Concept

        ViewTextItem concept = new ViewTextItem(DataAttributeDS.CONCEPT, MetamacSrmWeb.getConstants().concept());
        concept.setShowIfCondition(FormItemUtils.getFalseFormItemIfFunction());
        SearchViewTextItem conceptView = createConceptItem(DataAttributeDS.CONCEPT_VIEW, MetamacSrmWeb.getConstants().concept());

        // Role

        RelatedResourceListItem roleItem = createRoleItem(DataAttributeDS.ROLE, getConstants().dsdDimensionsRole());

        // Relation

        relationType = new RequiredSelectItem(DataAttributeDS.RELATED_WITH, MetamacSrmWeb.getConstants().dsdAttributeRelatedWith());
        relationType.setValueMap(CommonUtils.getTypeRelathionshipHashMap());
        relationType.setRedrawOnChange(true);

        // Relation: Group keys for dimension relationship

        groupKeysForDimensionRelationshipItem = new CustomSelectItem(DataAttributeDS.GROUP_KEY_FOR_DIMENSION_RELATIONSHIP, MetamacSrmWeb.getConstants().dsdAttributeGroupKeysForDimensionRelationship());
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

        dimensionsForDimensionRelationshipItem = new RequiredSelectItem(DataAttributeDS.DIMENSION_FOR_DIMENSION_RELATIONSHIP, MetamacSrmWeb.getConstants()
                .dsdAttributeDimensionsForDimensionRelationship());
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

        groupKeyFormForGroupRelationship = new RequiredSelectItem(DataAttributeDS.GROUP_KEY_FOR_GROUP_RELATIONSHIP, MetamacSrmWeb.getConstants().dsdAttributeGroupKeyFormGroupRelationship());
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

        representationTypeItem = new CustomSelectItem(DataAttributeDS.REPRESENTATION_TYPE, MetamacSrmWeb.getConstants().representation());
        representationTypeItem.setValueMap(org.siemac.metamac.srm.web.client.utils.CommonUtils.getTypeRepresentationEnumHashMap());
        representationTypeItem.setRedrawOnChange(true);
        // Show FacetForm if RepresentationTypeEnum = NON_NUMERATED
        representationTypeItem.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                FacetFormUtils.setFacetFormVisibility(facetForm, representationTypeItem.getValueAsString());
            }
        });

        ViewTextItem codelist = new ViewTextItem(DataAttributeDS.ENUMERATED_REPRESENTATION_CODELIST, MetamacSrmWeb.getConstants().codelist());
        codelist.setShowIfCondition(FormItemUtils.getFalseFormItemIfFunction());
        SearchViewTextItem codelistView = createEnumeratedRepresentationItem(DataAttributeDS.ENUMERATED_REPRESENTATION_CODELIST_VIEW, MetamacSrmWeb.getConstants().codelist());

        ViewTextItem urn = new ViewTextItem(DataAttributeDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(DataAttributeDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());

        editionForm.setFields(code, assignmentStatusItem, concept, conceptView, roleItem, relationType, groupKeysForDimensionRelationshipItem, dimensionsForDimensionRelationshipItem,
                groupKeyFormForGroupRelationship, representationTypeItem, codelist, codelistView, urn, urnProvider);

        // Facet Form

        facetForm = new DsdFacetForm();
        facetForm.setVisibility(Visibility.HIDDEN);

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
    public void setConceptSchemesForAttributeRole(GetRelatedResourcesResult result) {
        if (searchConceptsForRolesWindow != null) {
            searchConceptsForRolesWindow.getInitialSelectionItem().setValueMap(org.siemac.metamac.srm.web.client.utils.CommonUtils.getRelatedResourceHashMap(result.getRelatedResourceDtos()));
        }
    }

    @Override
    public void setConceptsForAttributeRole(GetRelatedResourcesResult result) {
        if (searchConceptsForRolesWindow != null) {
            searchConceptsForRolesWindow.setSourceRelatedResources(result.getRelatedResourceDtos());
            searchConceptsForRolesWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getRelatedResourceDtos().size(), result.getTotalResults());
        }
    }

    @Override
    public void setCodelistsForEnumeratedRepresentation(GetRelatedResourcesResult result) {
        if (searchCodelistForEnumeratedRepresentationWindow != null) {
            searchCodelistForEnumeratedRepresentationWindow.setRelatedResources(result.getRelatedResourceDtos());
            searchCodelistForEnumeratedRepresentationWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getRelatedResourceDtos().size(), result.getTotalResults());
            // If there is no results, show an info message (maybe the attribute concept has no variable)
            if (result.getRelatedResourceDtos().size() > 0) {
                searchCodelistForEnumeratedRepresentationWindow.hideInfoMessage();
            } else {
                searchCodelistForEnumeratedRepresentationWindow.showInfoMessage();
            }
        }
    }

    private void setAttribute(DataAttributeDto dataAttributeDto) {
        setAttributeViewMode(dataAttributeDto);
        setAttributeEditionMode(dataAttributeDto);
    }

    private void setAttributeViewMode(DataAttributeDto dataAttributeDto) {
        this.dataAttributeDto = dataAttributeDto;

        // Id
        form.setValue(DataAttributeDS.CODE, dataAttributeDto.getCode());

        // URNs
        form.setValue(DataAttributeDS.URN, dataAttributeDto.getUrn());
        form.setValue(DataAttributeDS.URN_PROVIDER, dataAttributeDto.getUrnProvider());

        // Concept
        form.setValue(DataAttributeDS.CONCEPT_VIEW, ExternalItemUtils.getExternalItemName(dataAttributeDto.getCptIdRef())); // TODO RelatedResourceDto instead of ExternalItemDto

        // Role
        form.getItem(DataAttributeDS.ROLE).hide();
        form.getItem(DataAttributeDS.ROLE).clearValue();
        // TODO RelatedResourceDto instead of ExternalItemDto
        ((RelatedResourceListItem) form.getItem(DataAttributeDS.ROLE)).setRelatedResources(RelatedResourceUtils.createRelatedResourceDtosFromExternalItemDtos(dataAttributeDto.getRole()));
        form.getItem(DataAttributeDS.ROLE).show();

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
        form.getItem(DataAttributeDS.ENUMERATED_REPRESENTATION_CODELIST_VIEW).hide();
        form.getItem(DataAttributeDS.ENUMERATED_REPRESENTATION_CODELIST_VIEW).clearValue();
        staticRepresentationTypeItem.clearValue();
        staticFacetForm.clearValues();
        if (dataAttributeDto.getLocalRepresentation() != null) {
            // Code List
            if (TypeRepresentationEnum.ENUMERATED.equals(dataAttributeDto.getLocalRepresentation().getTypeRepresentationEnum())) {
                // Codelist
                staticRepresentationTypeItem.setValue(MetamacSrmWeb.getCoreMessages().typeRepresentationEnumENUMERATED());
                form.setValue(DataAttributeDS.ENUMERATED_REPRESENTATION_CODELIST_VIEW, ExternalItemUtils.getExternalItemName(dataAttributeDto.getLocalRepresentation().getEnumerated())); // TODO
                                                                                                                                                                                          // RelatedResourceDto
                                                                                                                                                                                          // instead of
                                                                                                                                                                                          // ExternalItemDto
                form.getItem(DataAttributeDS.ENUMERATED_REPRESENTATION_CODELIST_VIEW).show();
            } else if (TypeRepresentationEnum.TEXT_FORMAT.equals(dataAttributeDto.getLocalRepresentation().getTypeRepresentationEnum())) {
                // Facet
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
        editionForm.setValue(DataAttributeDS.CODE, dataAttributeDto.getCode());

        // URNs
        editionForm.setValue(DataAttributeDS.URN, dataAttributeDto.getUrn());
        editionForm.setValue(DataAttributeDS.URN_PROVIDER, dataAttributeDto.getUrnProvider());

        // Concept
        editionForm.setValue(DataAttributeDS.CONCEPT, dataAttributeDto.getCptIdRef() != null ? dataAttributeDto.getCptIdRef().getUrn() : null);
        editionForm.setValue(DataAttributeDS.CONCEPT_VIEW, ExternalItemUtils.getExternalItemName(dataAttributeDto.getCptIdRef())); // TODO RelatedResourceDto instead of ExternalItemDto

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
        // TODO RelatedResourceDto instead of ExternalItemDto
        ((RelatedResourceListItem) editionForm.getItem(DataAttributeDS.ROLE)).setRelatedResources(RelatedResourceUtils.createRelatedResourceDtosFromExternalItemDtos(dataAttributeDto.getRole()));

        // Assignment Status
        assignmentStatusItem.setValue((dataAttributeDto.getUsageStatus() == null) ? null : dataAttributeDto.getUsageStatus().toString());

        // Representation
        editionForm.getItem(DataAttributeDS.ENUMERATED_REPRESENTATION_CODELIST).clearValue();
        editionForm.getItem(DataAttributeDS.ENUMERATED_REPRESENTATION_CODELIST_VIEW).clearValue();
        representationTypeItem.clearValue();
        facetForm.clearValues();
        if (dataAttributeDto.getLocalRepresentation() != null) {
            if (TypeRepresentationEnum.ENUMERATED.equals(dataAttributeDto.getLocalRepresentation().getTypeRepresentationEnum())) {
                representationTypeItem.setValue(TypeRepresentationEnum.ENUMERATED.toString());
                editionForm.setValue(DataAttributeDS.ENUMERATED_REPRESENTATION_CODELIST, dataAttributeDto.getLocalRepresentation().getEnumerated().getUrn());
                editionForm.setValue(DataAttributeDS.ENUMERATED_REPRESENTATION_CODELIST_VIEW, ExternalItemUtils.getExternalItemName(dataAttributeDto.getLocalRepresentation().getEnumerated())); // TODO
                                                                                                                                                                                                 // RelatedResourceDto
                                                                                                                                                                                                 // instead
                                                                                                                                                                                                 // of
                                                                                                                                                                                                 // ExternalItemDto
            } else if (TypeRepresentationEnum.TEXT_FORMAT.equals(dataAttributeDto.getLocalRepresentation().getTypeRepresentationEnum())) {
                // Facet
                representationTypeItem.setValue(TypeRepresentationEnum.TEXT_FORMAT.toString());
                // Only one facet in a Representation
                FacetDto facetDto = dataAttributeDto.getLocalRepresentation().getNonEnumerated();
                facetForm.setFacet(facetDto);
            }
        }
        FacetFormUtils.setFacetFormVisibility(facetForm, representationTypeItem.getValueAsString());
        editionForm.redraw();

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
        dataAttributeDto.setCode(editionForm.getValueAsString(DataAttributeDS.CODE));

        // Role
        // TODO RelatedResourceDto instead of ExternalItemDto
        dataAttributeDto.getRole().clear();
        List<RelatedResourceDto> selectedRoles = ((RelatedResourceListItem) editionForm.getItem(DataAttributeDS.ROLE)).getSelectedRelatedResources();
        dataAttributeDto.getRole().addAll(RelatedResourceUtils.createExternalItemDtosFromRelatedResourceDtos(selectedRoles));

        // Concept
        // TODO RelatedResourceDto instead of ExternalItemDto
        // dataAttributeDto.setCptIdRef(StringUtils.isBlank(editionForm.getValueAsString(DataAttributeDS.CONCEPT)) ? null :
        // RelatedResourceUtils.createRelatedResourceDto(TypeExternalArtefactsEnum.CONCEPT,
        // editionForm.getValueAsString(DataAttributeDS.CONCEPT)));
        dataAttributeDto.setCptIdRef(StringUtils.isBlank(editionForm.getValueAsString(DataAttributeDS.CONCEPT)) ? null : RelatedResourceUtils.createExternalItemDto(TypeExternalArtefactsEnum.CONCEPT,
                editionForm.getValueAsString(DataAttributeDS.CONCEPT)));

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
                // TODO RelatedResourceDto instead of ExternalItemDto
                dataAttributeDto.getLocalRepresentation().setEnumerated(
                        StringUtils.isBlank(editionForm.getValueAsString(DataAttributeDS.ENUMERATED_REPRESENTATION_CODELIST)) ? null : RelatedResourceUtils.createExternalItemDto(
                                TypeExternalArtefactsEnum.CODELIST, editionForm.getValueAsString(DataAttributeDS.ENUMERATED_REPRESENTATION_CODELIST)));
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
    public void onAttributeSaved(DataAttributeDto dataAttributeDto) {
        this.dataAttributeDto = dataAttributeDto;
        attributesGrid.removeSelectedData();
        AttributeRecord record = RecordUtils.getAttributeRecord(dataAttributeDto);
        attributesGrid.addData(record);
        attributesGrid.selectRecord(record);
        mainFormLayout.setViewMode();
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
        editionForm.clearErrors(true);
        facetForm.clearErrors(true);

        selectedComponentLayout.show();
        selectedComponentLayout.redraw();
    }

    private void deselectAttribute() {
        selectedComponentLayout.hide();
        deleteToolStripButton.hide();
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

    private SearchViewTextItem createConceptItem(String name, String title) {
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;
        final SearchViewTextItem conceptItem = new SearchViewTextItem(name, title);
        conceptItem.setRequired(true);
        conceptItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                SelectItem conceptSchemeSelectItem = new SelectItem(ConceptSchemeDS.URN, getConstants().conceptScheme());
                searchConceptWindow = new SearchRelatedResourcePaginatedWindow(getConstants().conceptSelection(), MAX_RESULTS, conceptSchemeSelectItem, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        getUiHandlers().retrieveConcepts(firstResult, maxResults, searchConceptWindow.getRelatedResourceCriteria(), searchConceptWindow.getInitialSelectionValue());
                    }
                });

                // Load concept schemes and concepts (to populate the selection window)
                getUiHandlers().retrieveConceptSchemes(FIRST_RESULST, SrmWebConstants.NO_LIMIT_IN_PAGINATION);
                getUiHandlers().retrieveConcepts(FIRST_RESULST, MAX_RESULTS, null, null);

                searchConceptWindow.getInitialSelectionItem().addChangedHandler(new ChangedHandler() {

                    @Override
                    public void onChanged(ChangedEvent event) {
                        getUiHandlers().retrieveConcepts(FIRST_RESULST, MAX_RESULTS, searchConceptWindow.getRelatedResourceCriteria(), searchConceptWindow.getInitialSelectionValue());
                    }
                });

                searchConceptWindow.getListGridItem().getListGrid().setSelectionType(SelectionStyle.SINGLE);
                searchConceptWindow.getListGridItem().setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String concept) {
                        getUiHandlers().retrieveConcepts(firstResult, maxResults, concept, searchConceptWindow.getInitialSelectionValue());
                    }
                });

                searchConceptWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        RelatedResourceDto selectedConcept = searchConceptWindow.getSelectedRelatedResource();
                        searchConceptWindow.markForDestroy();
                        // Set selected concepts in form
                        editionForm.setValue(DataAttributeDS.CONCEPT, selectedConcept != null ? selectedConcept.getUrn() : null);
                        editionForm.setValue(DataAttributeDS.CONCEPT_VIEW, selectedConcept != null ? org.siemac.metamac.srm.web.client.utils.CommonUtils.getRelatedResourceName(selectedConcept) : null);

                        // When a concept is selected, reset the value of the codelist (the codelist depends on the concept)
                        editionForm.setValue(DataAttributeDS.ENUMERATED_REPRESENTATION_CODELIST, StringUtils.EMPTY);
                        editionForm.setValue(DataAttributeDS.ENUMERATED_REPRESENTATION_CODELIST_VIEW, StringUtils.EMPTY);

                        editionForm.markForRedraw();
                        editionForm.validate(false);
                    }
                });
            }
        });
        return conceptItem;
    }

    private RelatedResourceListItem createRoleItem(String name, String title) {
        final int FIRST_RESULT = 0;
        final int MAX_RESULTS = 8;

        RelatedResourceListItem relatedResources = new RelatedResourceListItem(name, title, true);
        relatedResources.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                SelectItem conceptSchemeSelectItem = new SelectItem(ConceptSchemeDS.URN, getConstants().conceptScheme());
                searchConceptsForRolesWindow = new SearchMultipleRelatedResourcePaginatedWindow(MetamacSrmWeb.getConstants().dsdAttributeRole(), MAX_RESULTS, conceptSchemeSelectItem,
                        new PaginatedAction() {

                            @Override
                            public void retrieveResultSet(int firstResult, int maxResults) {
                                getUiHandlers().retrieveConceptsForAttributeRole(firstResult, maxResults, searchConceptsForRolesWindow.getRelatedResourceCriteria(),
                                        searchConceptsForRolesWindow.getInitialSelectionValue());

                            }
                        });

                // Load concept schemes and concepts (to populate the selection window)
                getUiHandlers().retrieveConceptSchemesForAttributeRole(FIRST_RESULT, SrmWebConstants.NO_LIMIT_IN_PAGINATION);
                getUiHandlers().retrieveConceptsForAttributeRole(FIRST_RESULT, MAX_RESULTS, null, null);

                // Set the selected concepts
                List<RelatedResourceDto> selectedConcepts = ((RelatedResourceListItem) editionForm.getItem(DataAttributeDS.ROLE)).getRelatedResourceDtos();
                searchConceptsForRolesWindow.setTargetRelatedResources(selectedConcepts);

                searchConceptsForRolesWindow.getInitialSelectionItem().addChangedHandler(new ChangedHandler() {

                    @Override
                    public void onChanged(ChangedEvent event) {
                        getUiHandlers().retrieveConceptsForAttributeRole(FIRST_RESULT, MAX_RESULTS, searchConceptsForRolesWindow.getRelatedResourceCriteria(),
                                searchConceptsForRolesWindow.getInitialSelectionValue());
                    }
                });

                searchConceptsForRolesWindow.setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                        getUiHandlers().retrieveConceptsForAttributeRole(firstResult, maxResults, criteria, searchConceptsForRolesWindow.getInitialSelectionValue());
                    }
                });
                searchConceptsForRolesWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        List<RelatedResourceDto> selectedRelatedResources = searchConceptsForRolesWindow.getSelectedRelatedResources();
                        ((RelatedResourceListItem) editionForm.getItem(DataAttributeDS.ROLE)).setRelatedResources(selectedRelatedResources);
                        searchConceptsForRolesWindow.markForDestroy();
                    }
                });
            }
        });
        return relatedResources;
    }

    private SearchViewTextItem createEnumeratedRepresentationItem(String name, String title) {
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;
        final SearchViewTextItem codelistItem = new SearchViewTextItem(name, title);
        codelistItem.setRequired(true);
        codelistItem.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                // Show CodeList if RepresentationTypeEnum = ENUMERATED
                return CommonUtils.isRepresentationTypeEnumerated(representationTypeItem.getValueAsString());
            }
        });
        codelistItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {

                final String conceptUrn = editionForm.getValueAsString(DataAttributeDS.CONCEPT);

                if (StringUtils.isBlank(conceptUrn)) {
                    // If a concept has not been selected, show a message and do not let the user to select a codelist
                    InformationWindow conceptRequiredWindow = new InformationWindow(getConstants().codelistSelection(), getConstants().dsdAttributeCodelistSelectionConceptRequired());
                    conceptRequiredWindow.show();
                } else {
                    searchCodelistForEnumeratedRepresentationWindow = new SearchRelatedResourcePaginatedWindow(getConstants().codelistSelection(), MAX_RESULTS, new PaginatedAction() {

                        @Override
                        public void retrieveResultSet(int firstResult, int maxResults) {
                            getUiHandlers().retrieveCodelistsForEnumeratedRepresentation(firstResult, maxResults, searchCodelistForEnumeratedRepresentationWindow.getRelatedResourceCriteria(),
                                    conceptUrn);
                        }
                    });
                    searchCodelistForEnumeratedRepresentationWindow.setInfoMessage(getConstants().dsdAttributeEnumeratedRepresentationInfoMessage());

                    // Load codelists (to populate the selection window)
                    getUiHandlers().retrieveCodelistsForEnumeratedRepresentation(FIRST_RESULST, MAX_RESULTS, null, conceptUrn);

                    searchCodelistForEnumeratedRepresentationWindow.getListGridItem().getListGrid().setSelectionType(SelectionStyle.SINGLE);
                    searchCodelistForEnumeratedRepresentationWindow.getListGridItem().setSearchAction(new SearchPaginatedAction() {

                        @Override
                        public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                            getUiHandlers().retrieveCodelistsForEnumeratedRepresentation(firstResult, maxResults, criteria, conceptUrn);
                        }
                    });

                    searchCodelistForEnumeratedRepresentationWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                        @Override
                        public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                            RelatedResourceDto selectedCodelist = searchCodelistForEnumeratedRepresentationWindow.getSelectedRelatedResource();
                            searchCodelistForEnumeratedRepresentationWindow.markForDestroy();
                            // Set selected codelist in form
                            editionForm.setValue(DataAttributeDS.ENUMERATED_REPRESENTATION_CODELIST, selectedCodelist != null ? selectedCodelist.getUrn() : null);
                            editionForm.setValue(DataAttributeDS.ENUMERATED_REPRESENTATION_CODELIST_VIEW,
                                    selectedCodelist != null ? org.siemac.metamac.srm.web.client.utils.CommonUtils.getRelatedResourceName(selectedCodelist) : null);
                            editionForm.validate(false);
                        }
                    });
                }
            }
        });
        return codelistItem;
    }
}
