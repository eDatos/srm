package org.siemac.metamac.srm.web.dsd.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getCoreMessages;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.constants.SrmWebConstants;
import org.siemac.metamac.srm.web.client.representation.widgets.StaticFacetForm;
import org.siemac.metamac.srm.web.client.utils.FacetFormUtils;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.client.widgets.AnnotationsPanel;
import org.siemac.metamac.srm.web.client.widgets.NavigableListGrid;
import org.siemac.metamac.srm.web.client.widgets.RelatedResourceLinkItem;
import org.siemac.metamac.srm.web.client.widgets.RelatedResourceListItem;
import org.siemac.metamac.srm.web.client.widgets.SearchRelatedResourceLinkItem;
import org.siemac.metamac.srm.web.client.widgets.SearchRelatedResourceListItemWithSchemeFilterItem;
import org.siemac.metamac.srm.web.client.widgets.SearchRelatedResourcePaginatedWindow;
import org.siemac.metamac.srm.web.client.widgets.search.SearchSrmItemLinkItemWithSchemeFilterItem;
import org.siemac.metamac.srm.web.dsd.model.ds.DataAttributeDS;
import org.siemac.metamac.srm.web.dsd.model.record.DataAttributeRecord;
import org.siemac.metamac.srm.web.dsd.presenter.DsdAttributesTabPresenter;
import org.siemac.metamac.srm.web.dsd.utils.CommonUtils;
import org.siemac.metamac.srm.web.dsd.utils.DsdClientSecurityUtils;
import org.siemac.metamac.srm.web.dsd.utils.DsdRecordUtils;
import org.siemac.metamac.srm.web.dsd.utils.DsdsFormUtils;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdAttributesTabUiHandlers;
import org.siemac.metamac.srm.web.dsd.widgets.DsdFacetForm;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptSchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.RelatedResourceItemWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.RelatedResourceWebCriteria;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;
import org.siemac.metamac.web.common.client.widgets.CustomLinkListGridField;
import org.siemac.metamac.web.common.client.widgets.CustomListGridField;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.InformationWindow;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;
import org.siemac.metamac.web.common.client.widgets.handlers.CustomLinkItemNavigationClickHandler;
import org.siemac.metamac.web.common.client.widgets.handlers.ListRecordNavigationClickHandler;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataAttributeDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DescriptorDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DimensionComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.FacetDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.RelationshipDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.RepresentationDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.FacetValueTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RepresentationTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.SpecialAttributeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeComponent;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeDataAttribute;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeRelathionship;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.UsageStatus;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.HasClickHandlers;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.form.validator.CustomValidator;
import com.smartgwt.client.widgets.form.validator.RequiredIfFunction;
import com.smartgwt.client.widgets.form.validator.RequiredIfValidator;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class DsdAttributesTabViewImpl extends ViewWithUiHandlers<DsdAttributesTabUiHandlers> implements DsdAttributesTabPresenter.DsdAttributesTabView {

    private DataStructureDefinitionMetamacDto    dataStructureDefinitionMetamacDto;

    private DataAttributeDto                     dataAttributeDto;
    private List<DimensionComponentDto>          dimensionComponentDtos;
    private List<DescriptorDto>                  descriptorDtos;                                      // Group Keys

    private VLayout                              panel;
    private VLayout                              selectedComponentLayout;
    private NavigableListGrid                    attributesGrid;

    private InternationalMainFormLayout          mainFormLayout;

    private AnnotationsPanel                     viewAnnotationsPanel;
    private AnnotationsPanel                     editionAnnotationsPanel;

    // VIEW FORM
    private GroupDynamicForm                     form;
    private StaticFacetForm                      facetForm;

    // EDITION FORM
    private GroupDynamicForm                     editionForm;
    private DsdFacetForm                         facetEditionForm = null;
    private StaticFacetForm                      facetStaticEditionForm;

    private ToolStripButton                      newToolStripButton;
    private ToolStripButton                      deleteToolStripButton;

    private DeleteConfirmationWindow             deleteConfirmationWindow;

    private SearchRelatedResourcePaginatedWindow searchCodelistForEnumeratedRepresentationWindow;
    private SearchRelatedResourcePaginatedWindow searchConceptSchemeForEnumeratedRepresentationWindow;

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
        deleteConfirmationWindow.setVisible(false);

        deleteToolStripButton = new ToolStripButton(MetamacSrmWeb.getConstants().actionDelete(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.deleteListGrid().getURL());
        deleteToolStripButton.setVisible(false);
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

        attributesGrid = new NavigableListGrid();
        attributesGrid.setWidth100();
        attributesGrid.setHeight(150);
        attributesGrid.setSelectionType(SelectionStyle.SIMPLE);
        attributesGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
        CustomListGridField codeField = new CustomListGridField(DataAttributeDS.CODE, getConstants().dsdAttributeId());
        CustomListGridField usageField = new CustomListGridField(DataAttributeDS.USAGE_STATUS, getConstants().dsdAttributeUsageStatus());
        CustomListGridField specialAttributeTypeField = new CustomListGridField(DataAttributeDS.SPECIAL_ATTRIBUTE_TYPE, getConstants().dsdAttributeType());
        CustomListGridField relatedTo = new CustomListGridField(DataAttributeDS.RELATED_TO, getConstants().dsdAttributeRelatedWith());
        CustomLinkListGridField conceptField = new CustomLinkListGridField(DataAttributeDS.CONCEPT, getConstants().concept());
        attributesGrid.setFields(codeField, specialAttributeTypeField, usageField, relatedTo, conceptField);

        // Show attribute details when record clicked
        attributesGrid.addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (attributesGrid.getSelectedRecords() != null && attributesGrid.getSelectedRecords().length == 1) {
                    DataAttributeRecord record = (DataAttributeRecord) attributesGrid.getSelectedRecord();
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
        mainFormLayout.getTitleLabel().setStyleName("subsectionTitleWithNoLeftMargin");
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
        selectedComponentLayout.setVisible(false);

        panel.addMember(gridLayout);
        panel.addMember(selectedComponentLayout);
    }

    /**
     * Creates and returns the view layout
     * 
     * @return
     */
    private void createViewForm() {
        form = new GroupDynamicForm(getConstants().dsdAttributeDetails());
        ViewTextItem code = new ViewTextItem(DataAttributeDS.CODE, getConstants().dsdAttributeId());
        ViewTextItem type = new ViewTextItem(DataAttributeDS.SPECIAL_ATTRIBUTE_TYPE, getConstants().dsdAttributeType());
        RelatedResourceLinkItem concept = new RelatedResourceLinkItem(DataAttributeDS.CONCEPT, getConstants().concept(), getCustomLinkItemNavigationClickHandler());
        RelatedResourceListItem roleItem = new RelatedResourceListItem(DataAttributeDS.ROLE, getConstants().dsdAttributeRole(), false, getListRecordNavigationClickHandler());
        ViewTextItem usageStatusItem = new ViewTextItem(DataAttributeDS.USAGE_STATUS, getConstants().dsdAttributeUsageStatus());
        ViewTextItem relatedTo = new ViewTextItem(DataAttributeDS.RELATED_TO, getConstants().dsdAttributeRelatedWith());
        ViewTextItem staticGroupKeysForDimensionRelationshipItem = new ViewTextItem(DataAttributeDS.GROUP_KEY_FOR_DIMENSION_RELATIONSHIP, getConstants()
                .dsdAttributeGroupKeysForDimensionRelationship());
        ViewTextItem staticDimensionsForDimensionRelationshipItem = new ViewTextItem(DataAttributeDS.DIMENSION_FOR_DIMENSION_RELATIONSHIP, getConstants()
                .dsdAttributeDimensionsForDimensionRelationship());
        ViewTextItem staticGroupKeyFormForGroupRelationship = new ViewTextItem(DataAttributeDS.GROUP_KEY_FOR_GROUP_RELATIONSHIP, getConstants().dsdAttributeGroupKeyForGroupRelationship());

        ViewTextItem staticRepresentationTypeItem = new ViewTextItem(DataAttributeDS.REPRESENTATION_TYPE, getConstants().representation());
        RelatedResourceLinkItem codelist = new RelatedResourceLinkItem(DataAttributeDS.ENUMERATED_REPRESENTATION_CODELIST, getConstants().codelist(), getCustomLinkItemNavigationClickHandler());
        RelatedResourceLinkItem conceptScheme = new RelatedResourceLinkItem(DataAttributeDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME, getConstants().conceptScheme(),
                getCustomLinkItemNavigationClickHandler());

        ViewTextItem urn = new ViewTextItem(DataAttributeDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(DataAttributeDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        form.setFields(code, type, usageStatusItem, concept, roleItem, relatedTo, staticGroupKeysForDimensionRelationshipItem, staticDimensionsForDimensionRelationshipItem,
                staticGroupKeyFormForGroupRelationship, staticRepresentationTypeItem, codelist, conceptScheme, urn, urnProvider);

        facetForm = new StaticFacetForm();

        // Annotations
        viewAnnotationsPanel = new AnnotationsPanel(true);

        mainFormLayout.addViewCanvas(form);
        mainFormLayout.addViewCanvas(facetForm);
        mainFormLayout.addViewCanvas(viewAnnotationsPanel);
    }

    /**
     * Creates and returns the edition layout
     * 
     * @return
     */
    private void createEditionForm() {
        editionForm = new GroupDynamicForm(getConstants().dsdAttributeDetails());

        // Code

        RequiredTextItem code = new RequiredTextItem(DataAttributeDS.CODE, getConstants().dsdAttributeId());
        code.setValidators(SemanticIdentifiersUtils.getAttributeIdentifierCustomValidator());
        code.setShowIfCondition(getCodeFormItemIfFunction());

        ViewTextItem staticCode = new ViewTextItem(DataAttributeDS.CODE_VIEW, getConstants().dsdAttributeId());
        staticCode.setShowIfCondition(getStaticCodeFormItemIfFunction());

        // TYPE

        CustomSelectItem specialAttributeTypeItem = createSpecialAttributeTypeItem(DataAttributeDS.SPECIAL_ATTRIBUTE_TYPE, getConstants().dsdAttributeType());

        // USAGE STATUS

        RequiredSelectItem usageStatusItem = new RequiredSelectItem(DataAttributeDS.USAGE_STATUS, getConstants().dsdAttributeUsageStatus());
        usageStatusItem.setValueMap(CommonUtils.getUsageStatusHashMap());
        usageStatusItem.setShowIfCondition(getUsageStatusFormItemIfFunction());

        ViewTextItem staticUsageStatusItem = new ViewTextItem(DataAttributeDS.USAGE_STATUS_VIEW, getConstants().dsdAttributeUsageStatus());
        staticUsageStatusItem.setShowIfCondition(getStaticUsageStatusFormItemIfFunction());

        // CONCEPT

        SearchRelatedResourceLinkItem concept = createConceptItem();
        concept.setShowIfCondition(getConceptFormItemIfFunction());

        RelatedResourceLinkItem staticConcept = new RelatedResourceLinkItem(DataAttributeDS.CONCEPT_VIEW, getConstants().concept(), getCustomLinkItemNavigationClickHandler());
        staticConcept.setShowIfCondition(getStaticConceptFormItemIfFunction());

        // ROLE

        RelatedResourceListItem roleItem = createRoleItem();
        roleItem.setShowIfCondition(getRoleFormItemIfFunction());

        RelatedResourceListItem staticRoleItem = new RelatedResourceListItem(DataAttributeDS.ROLE_VIEW, getConstants().dsdAttributeRole(), false, getListRecordNavigationClickHandler());
        staticRoleItem.setShowIfCondition(getStaticRoleFormItemIfFunction());

        // RELATED TO

        final RequiredSelectItem relatedTo = new RequiredSelectItem(DataAttributeDS.RELATED_TO, getConstants().dsdAttributeRelatedWith());
        relatedTo.setValueMap(CommonUtils.getTypeRelationshipHashMap());
        relatedTo.setShowIfCondition(getRelatedToFormItemIfFunction());
        relatedTo.setValidateOnChange(true);
        relatedTo.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                relatedTo.validate();
                editionForm.markForRedraw();

            }
        });
        relatedTo.setValidators(new CustomValidator() {

            @Override
            protected boolean condition(Object value) {

                // The attributes with a type specified, can only be related to datasets (TypeRelathionship = NO_SPECIFIED_RELATIONSHIP)

                SpecialAttributeTypeEnum attributeType = !StringUtils.isBlank(editionForm.getValueAsString(DataAttributeDS.SPECIAL_ATTRIBUTE_TYPE)) ? SpecialAttributeTypeEnum.valueOf(editionForm
                        .getValueAsString(DataAttributeDS.SPECIAL_ATTRIBUTE_TYPE)) : null;
                TypeRelathionship typeRelathionship = !StringUtils.isBlank(editionForm.getValueAsString(DataAttributeDS.RELATED_TO)) ? TypeRelathionship.valueOf(editionForm
                        .getValueAsString(DataAttributeDS.RELATED_TO)) : null;
                if (attributeType != null && !TypeRelathionship.NO_SPECIFIED_RELATIONSHIP.equals(typeRelathionship)) {
                    return false;
                }
                return true;
            }
        });

        ViewTextItem staticRelatedTo = new ViewTextItem(DataAttributeDS.RELATED_TO_VIEW, getConstants().dsdAttributeRelatedWith());
        staticRelatedTo.setShowIfCondition(getStaticRelatedToFormItemIfFunction());

        // Relation: Group keys for dimension relationship

        CustomSelectItem groupKeysForDimensionRelationshipItem = new CustomSelectItem(DataAttributeDS.GROUP_KEY_FOR_DIMENSION_RELATIONSHIP, getConstants()
                .dsdAttributeGroupKeysForDimensionRelationship());
        groupKeysForDimensionRelationshipItem.setMultiple(true);
        groupKeysForDimensionRelationshipItem.setAllowEmptyValue(false);
        groupKeysForDimensionRelationshipItem.setPickListWidth(350);
        groupKeysForDimensionRelationshipItem.setShowIfCondition(getGroupKeysForDimensionRelationshipFormItemIfFunction());

        ViewTextItem staticGroupKeysForDimensionRelationshipItem = new ViewTextItem(DataAttributeDS.GROUP_KEY_FOR_DIMENSION_RELATIONSHIP_VIEW, getConstants()
                .dsdAttributeGroupKeysForDimensionRelationship());
        staticGroupKeysForDimensionRelationshipItem.setShowIfCondition(getStaticGroupKeysForDimensionRelationshipFormItemIfFunction());

        // Relation: Dimensions for dimension relationship

        RequiredSelectItem dimensionsForDimensionRelationshipItem = new RequiredSelectItem(DataAttributeDS.DIMENSION_FOR_DIMENSION_RELATIONSHIP, getConstants()
                .dsdAttributeDimensionsForDimensionRelationship());
        dimensionsForDimensionRelationshipItem.setMultiple(true);
        dimensionsForDimensionRelationshipItem.setAllowEmptyValue(false);
        dimensionsForDimensionRelationshipItem.setPickListWidth(350);
        dimensionsForDimensionRelationshipItem.setShowIfCondition(getDimensionsForDimensionRelationshipFormItemIfFunction());

        ViewTextItem staticDimensionsForDimensionRelationshipItem = new ViewTextItem(DataAttributeDS.DIMENSION_FOR_DIMENSION_RELATIONSHIP_VIEW, getConstants()
                .dsdAttributeDimensionsForDimensionRelationship());
        staticDimensionsForDimensionRelationshipItem.setShowIfCondition(getStaticDimensionsForDimensionRelationshipFormItemIfFunction());

        // Relation: Group Keys for group relationship

        RequiredSelectItem groupKeyFormForGroupRelationship = new RequiredSelectItem(DataAttributeDS.GROUP_KEY_FOR_GROUP_RELATIONSHIP, getConstants().dsdAttributeGroupKeyForGroupRelationship());
        groupKeyFormForGroupRelationship.setPickListWidth(350);
        groupKeyFormForGroupRelationship.setShowIfCondition(getGroupKeysForGroupRelationshipFormItemIfFunction());
        RequiredIfValidator ifValidator = new RequiredIfValidator(new RequiredIfFunction() {

            @Override
            public boolean execute(FormItem formItem, Object value) {
                return CommonUtils.isGroupRelationshipType(relatedTo.getValueAsString());
            }
        });
        groupKeyFormForGroupRelationship.setValidators(ifValidator);

        ViewTextItem staticGroupKeyFormForGroupRelationship = new ViewTextItem(DataAttributeDS.GROUP_KEY_FOR_GROUP_RELATIONSHIP_VIEW, getConstants().dsdAttributeGroupKeyForGroupRelationship());
        staticGroupKeyFormForGroupRelationship.setShowIfCondition(getStaticGroupKeysForGroupRelationshipFormItemIfFunction());

        // Representation

        // REPRESENTATION TYPE

        final CustomSelectItem representationTypeItem = createRepresentationTypeItem(DataAttributeDS.REPRESENTATION_TYPE, getConstants().representation());
        representationTypeItem.setShowIfCondition(getRepresentationTypeFormItemIfFunction());

        ViewTextItem staticRepresentationTypeItem = new ViewTextItem(DataAttributeDS.REPRESENTATION_TYPE_VIEW, getConstants().representation());
        staticRepresentationTypeItem.setShowIfCondition(getStaticRepresentationTypeFormItemIfFunction());

        // ENUMERATED REPRESENTATION

        // Codelist

        SearchRelatedResourceLinkItem codelist = createEnumeratedRepresentationItem(DataAttributeDS.ENUMERATED_REPRESENTATION_CODELIST, getConstants().codelist());
        codelist.setShowIfCondition(getCodelistEnumeratedRepresentationFormItemIfFunction()); // Shown in editionMode, only when the enumerated representation is editable

        RelatedResourceLinkItem codelistView = new RelatedResourceLinkItem(DataAttributeDS.ENUMERATED_REPRESENTATION_CODELIST_VIEW, getConstants().codelist(),
                getCustomLinkItemNavigationClickHandler());
        codelistView.setShowIfCondition(getStaticCodelistEnumeratedRepresentationFormItemIfFunction()); // This item is shown when the enumerated representation can not be edited

        // ConceptScheme

        SearchRelatedResourceLinkItem staticEditableConceptScheme = createMeasureAttributeEnumeratedRepresentationItem(DataAttributeDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME, getConstants()
                .conceptScheme());
        staticEditableConceptScheme.setShowIfCondition(getConceptSchemeEnumeratedRepresentationFormItemIfFunction()); // Shown in editionMode, only when the conceptScheme is editable

        RelatedResourceLinkItem staticConceptScheme = new RelatedResourceLinkItem(DataAttributeDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME_VIEW, getConstants().conceptScheme(),
                getCustomLinkItemNavigationClickHandler());
        staticConceptScheme.setShowIfCondition(getStaticConceptSchemeEnumeratedRepresentationFormItemIfFunction()); // This item is shown when the conceptScheme can not be edited

        // URNs

        ViewTextItem urn = new ViewTextItem(DataAttributeDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(DataAttributeDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());

        editionForm.setFields(code, staticCode, specialAttributeTypeItem, usageStatusItem, staticUsageStatusItem, concept, staticConcept, roleItem, staticRoleItem, relatedTo, staticRelatedTo,
                groupKeysForDimensionRelationshipItem, staticGroupKeysForDimensionRelationshipItem, dimensionsForDimensionRelationshipItem, staticDimensionsForDimensionRelationshipItem,
                groupKeyFormForGroupRelationship, staticGroupKeyFormForGroupRelationship, representationTypeItem, staticRepresentationTypeItem, codelist, codelistView, staticEditableConceptScheme,
                staticConceptScheme, urn, urnProvider);

        // Facet Form

        facetEditionForm = new DsdFacetForm();
        facetEditionForm.setVisible(false);
        // TextType applicable to time attributes is restricted to those that represent time
        CustomValidator timeValidator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                if (SpecialAttributeTypeEnum.TIME_EXTENDS.equals(getSpecialAttributeTypeFromEditionForm())) {
                    if (value != null) {
                        FacetValueTypeEnum f = FacetValueTypeEnum.valueOf((String) value);
                        return FacetFormUtils.representsTime(f) ? true : false;
                    }
                }
                return true;
            }
        };
        timeValidator.setErrorMessage(getMessages().errorTextTypeInDsdTimeDimensionOrAttribute());
        facetEditionForm.getTextType().setValidateOnChange(true);
        facetEditionForm.getTextType().setRedrawOnChange(true);
        facetEditionForm.getTextType().setValidators(timeValidator);

        facetStaticEditionForm = new StaticFacetForm();
        facetStaticEditionForm.setVisible(false);

        // Annotations
        editionAnnotationsPanel = new AnnotationsPanel(false);

        mainFormLayout.addEditionCanvas(editionForm);
        mainFormLayout.addEditionCanvas(facetEditionForm);
        mainFormLayout.addEditionCanvas(facetStaticEditionForm);
        mainFormLayout.addEditionCanvas(editionAnnotationsPanel);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setUiHandlers(DsdAttributesTabUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        attributesGrid.setUiHandlers(getUiHandlers());
    }

    @Override
    public void setDimensions(List<DimensionComponentDto> dimensionComponentDtos) {
        this.dimensionComponentDtos = dimensionComponentDtos;
        ((CustomSelectItem) editionForm.getItem(DataAttributeDS.DIMENSION_FOR_DIMENSION_RELATIONSHIP)).setValueMap(CommonUtils.getDimensionComponentDtoHashMap(dimensionComponentDtos));
    }

    @Override
    public void setGroupKeys(List<DescriptorDto> descriptorDtos) {
        this.descriptorDtos = descriptorDtos;
        LinkedHashMap<String, String> groupKeysMap = CommonUtils.getDescriptorDtoHashMap(descriptorDtos);
        LinkedHashMap<String, String> groupKeysMapWithEmpty = CommonUtils.getDescriptorDtoHashMapWithEmpty(descriptorDtos);
        ((CustomSelectItem) editionForm.getItem(DataAttributeDS.GROUP_KEY_FOR_GROUP_RELATIONSHIP)).setValueMap(groupKeysMapWithEmpty);
        ((CustomSelectItem) editionForm.getItem(DataAttributeDS.GROUP_KEY_FOR_DIMENSION_RELATIONSHIP)).setValueMap(groupKeysMap);
    }

    @Override
    public void setDsdAttributes(DataStructureDefinitionMetamacDto dsd, List<DataAttributeDto> dataAttributeDtos) {
        this.dataStructureDefinitionMetamacDto = dsd;
        deselectAttribute();

        // Security
        newToolStripButton.setVisible(DsdClientSecurityUtils.canCreateAttribute(dataStructureDefinitionMetamacDto));
        mainFormLayout.setCanEdit(DsdClientSecurityUtils.canUpdateAttribute(dataStructureDefinitionMetamacDto));

        attributesGrid.setData(DsdRecordUtils.getAttributeRecords(dataAttributeDtos));
    }

    @Override
    public void setConceptSchemes(GetRelatedResourcesResult result) {
        ((SearchSrmItemLinkItemWithSchemeFilterItem) editionForm.getItem(DataAttributeDS.CONCEPT)).setFilterResources(result.getRelatedResourceDtos(), result.getFirstResultOut(), result
                .getRelatedResourceDtos().size(), result.getTotalResults());
    }

    @Override
    public void setConcepts(GetRelatedResourcesResult result) {
        ((SearchSrmItemLinkItemWithSchemeFilterItem) editionForm.getItem(DataAttributeDS.CONCEPT)).setResources(result.getRelatedResourceDtos(), result.getFirstResultOut(), result
                .getRelatedResourceDtos().size(), result.getTotalResults());
    }

    @Override
    public void setDefaultConcept(RelatedResourceDto relatedResourceDto) {
        if (relatedResourceDto != null) {
            setConceptInEditionForm(relatedResourceDto);
        }
    }

    @Override
    public void setDefaultConceptSchemeEnumeratedRepresentation(RelatedResourceDto conceptScheme) {
        // This is only call when we are creating a measure attribute
        setEnumeratedRepresentationTypeInEditionForm();
        setConceptSchemeEnumerationRepresentationInEditionForm(conceptScheme);
        editionForm.markForRedraw();
        editionForm.validate();
    }

    @Override
    public void setConceptSchemesForAttributeRole(GetRelatedResourcesResult result) {
        ((SearchRelatedResourceListItemWithSchemeFilterItem) editionForm.getItem(DataAttributeDS.ROLE)).setFilterResources(result.getRelatedResourceDtos(), result.getFirstResultOut(),
                result.getTotalResults());
    }

    @Override
    public void setConceptsForAttributeRole(GetRelatedResourcesResult result) {
        ((SearchRelatedResourceListItemWithSchemeFilterItem) editionForm.getItem(DataAttributeDS.ROLE)).setResources(result.getRelatedResourceDtos(), result.getFirstResultOut(),
                result.getTotalResults());
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

    @Override
    public void setConceptSchemesForEnumeratedRepresentation(GetRelatedResourcesResult result) {
        if (searchConceptSchemeForEnumeratedRepresentationWindow != null) {
            searchConceptSchemeForEnumeratedRepresentationWindow.setRelatedResources(result.getRelatedResourceDtos());
            searchConceptSchemeForEnumeratedRepresentationWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getRelatedResourceDtos().size(), result.getTotalResults());
        }
    }

    private void setAttribute(DataAttributeDto dataAttributeDto) {
        setAttributeViewMode(dataAttributeDto);
        setAttributeEditionMode(dataAttributeDto);
    }

    private void setAttributeViewMode(DataAttributeDto dataAttributeDto) {
        this.dataAttributeDto = dataAttributeDto;

        // Code
        form.setValue(DataAttributeDS.CODE, dataAttributeDto.getCode());

        // Type
        form.setValue(DataAttributeDS.SPECIAL_ATTRIBUTE_TYPE, CommonUtils.getSpecialAttributeTypeName(dataAttributeDto.getSpecialAttributeType()));

        // URNs
        form.setValue(DataAttributeDS.URN, dataAttributeDto.getUrn());
        form.setValue(DataAttributeDS.URN_PROVIDER, dataAttributeDto.getUrnProvider());

        // Concept
        ((RelatedResourceLinkItem) form.getItem(DataAttributeDS.CONCEPT)).setRelatedResource(dataAttributeDto.getCptIdRef());

        // Role
        form.getItem(DataAttributeDS.ROLE).hide();
        form.getItem(DataAttributeDS.ROLE).clearValue();
        ((RelatedResourceListItem) form.getItem(DataAttributeDS.ROLE)).setRelatedResources(dataAttributeDto.getRole());
        form.getItem(DataAttributeDS.ROLE).show();

        // Usage Status
        form.setValue(DataAttributeDS.USAGE_STATUS, CommonUtils.getUsageStatusName(dataAttributeDto.getUsageStatus()));

        // RelateTo
        form.clearValue(DataAttributeDS.RELATED_TO);
        form.clearValue(DataAttributeDS.GROUP_KEY_FOR_GROUP_RELATIONSHIP);
        form.hideItem(DataAttributeDS.GROUP_KEY_FOR_GROUP_RELATIONSHIP);
        form.clearValue(DataAttributeDS.GROUP_KEY_FOR_DIMENSION_RELATIONSHIP);
        form.hideItem(DataAttributeDS.GROUP_KEY_FOR_DIMENSION_RELATIONSHIP);
        form.clearValue(DataAttributeDS.DIMENSION_FOR_DIMENSION_RELATIONSHIP);
        form.hideItem(DataAttributeDS.DIMENSION_FOR_DIMENSION_RELATIONSHIP);

        if (dataAttributeDto.getRelateTo() != null && dataAttributeDto.getRelateTo().getId() != null) {
            form.setValue(DataAttributeDS.RELATED_TO, CommonUtils.getTypeRelationshipName(dataAttributeDto.getRelateTo().getTypeRelathionship()));

            // Group keys for group relationship
            if (TypeRelathionship.GROUP_RELATIONSHIP.equals(dataAttributeDto.getRelateTo().getTypeRelathionship())) {
                form.setValue(DataAttributeDS.GROUP_KEY_FOR_GROUP_RELATIONSHIP, (dataAttributeDto.getRelateTo().getGroupKeyForGroupRelationship() == null) ? null : dataAttributeDto.getRelateTo()
                        .getGroupKeyForGroupRelationship().getCode());
                form.showItem(DataAttributeDS.GROUP_KEY_FOR_GROUP_RELATIONSHIP);
            }

            if (TypeRelathionship.DIMENSION_RELATIONSHIP.equals(dataAttributeDto.getRelateTo().getTypeRelathionship())) {
                // Group keys form dimension relationship
                form.setValue(DataAttributeDS.GROUP_KEY_FOR_DIMENSION_RELATIONSHIP, CommonUtils.getDescriptorListAsString(dataAttributeDto.getRelateTo().getGroupKeyForDimensionRelationship()));
                form.showItem(DataAttributeDS.GROUP_KEY_FOR_DIMENSION_RELATIONSHIP);

                // Dimensions for dimension relationship
                form.setValue(DataAttributeDS.DIMENSION_FOR_DIMENSION_RELATIONSHIP,
                        CommonUtils.getDimensionComponentListAsString(dataAttributeDto.getRelateTo().getDimensionForDimensionRelationship()));
                form.showItem(DataAttributeDS.DIMENSION_FOR_DIMENSION_RELATIONSHIP);
            }
        }

        // Representation
        facetForm.hide();
        form.getItem(DataAttributeDS.ENUMERATED_REPRESENTATION_CODELIST).hide();
        form.getItem(DataAttributeDS.ENUMERATED_REPRESENTATION_CODELIST).clearValue();
        form.getItem(DataAttributeDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME).hide();
        form.getItem(DataAttributeDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME).clearValue();
        form.getItem(DataAttributeDS.REPRESENTATION_TYPE).clearValue();
        facetForm.clearValues();
        if (dataAttributeDto.getLocalRepresentation() != null) {
            if (RepresentationTypeEnum.ENUMERATION.equals(dataAttributeDto.getLocalRepresentation().getRepresentationType())) {

                form.setValue(DataAttributeDS.REPRESENTATION_TYPE, MetamacSrmWeb.getCoreMessages().representationTypeEnumENUMERATION());

                if (RelatedResourceTypeEnum.CODELIST.equals(dataAttributeDto.getLocalRepresentation().getEnumeration().getType())) {

                    // CODELIST

                    if (!SpecialAttributeTypeEnum.MEASURE_EXTENDS.equals(dataAttributeDto.getSpecialAttributeType())) {
                        ((RelatedResourceLinkItem) form.getItem(DataAttributeDS.ENUMERATED_REPRESENTATION_CODELIST)).setRelatedResource(dataAttributeDto.getLocalRepresentation().getEnumeration());
                        form.getItem(DataAttributeDS.ENUMERATED_REPRESENTATION_CODELIST).show();
                    }

                } else if (RelatedResourceTypeEnum.CONCEPT_SCHEME.equals(dataAttributeDto.getLocalRepresentation().getEnumeration().getType())) {

                    // CONCEPTSCHEME

                    if (SpecialAttributeTypeEnum.MEASURE_EXTENDS.equals(dataAttributeDto.getSpecialAttributeType())) {
                        ((RelatedResourceLinkItem) form.getItem(DataAttributeDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME)).setRelatedResource(dataAttributeDto.getLocalRepresentation()
                                .getEnumeration());
                        form.getItem(DataAttributeDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME).show();
                    }
                }

            } else if (RepresentationTypeEnum.TEXT_FORMAT.equals(dataAttributeDto.getLocalRepresentation().getRepresentationType())) {

                // FACET

                form.setValue(DataAttributeDS.REPRESENTATION_TYPE, MetamacSrmWeb.getCoreMessages().representationTypeEnumTEXT_FORMAT());
                // Only one facet in a Representation
                FacetDto facetDto = dataAttributeDto.getLocalRepresentation().getTextFormat();
                facetForm.setFacet(facetDto);
                facetForm.show();
            }
        }

        // Annotations
        viewAnnotationsPanel.setAnnotations(dataAttributeDto.getAnnotations(), dataStructureDefinitionMetamacDto);
    }

    private void setAttributeEditionMode(DataAttributeDto dataAttributeDto) {
        this.dataAttributeDto = dataAttributeDto;

        // Code
        editionForm.setValue(DataAttributeDS.CODE, dataAttributeDto.getCode());
        editionForm.setValue(DataAttributeDS.CODE_VIEW, dataAttributeDto.getCode());

        // Type
        editionForm.setValue(DataAttributeDS.SPECIAL_ATTRIBUTE_TYPE, dataAttributeDto.getSpecialAttributeType() != null ? dataAttributeDto.getSpecialAttributeType().name() : null);

        // URNs
        editionForm.setValue(DataAttributeDS.URN, dataAttributeDto.getUrn());
        editionForm.setValue(DataAttributeDS.URN_PROVIDER, dataAttributeDto.getUrnProvider());

        // Concept
        setConceptInEditionForm(dataAttributeDto.getCptIdRef());

        // RelateTo
        editionForm.clearValue(DataAttributeDS.RELATED_TO);
        editionForm.clearValue(DataAttributeDS.GROUP_KEY_FOR_GROUP_RELATIONSHIP);
        editionForm.clearValue(DataAttributeDS.GROUP_KEY_FOR_DIMENSION_RELATIONSHIP);
        editionForm.clearValue(DataAttributeDS.DIMENSION_FOR_DIMENSION_RELATIONSHIP);

        if (dataAttributeDto.getRelateTo() != null && dataAttributeDto.getRelateTo().getId() != null) {
            editionForm.setValue(DataAttributeDS.RELATED_TO, dataAttributeDto.getRelateTo().getTypeRelathionship().toString());
            editionForm.setValue(DataAttributeDS.RELATED_TO_VIEW, CommonUtils.getTypeRelationshipName(dataAttributeDto.getRelateTo().getTypeRelathionship()));

            // Group keys for group relationship
            editionForm.setValue(DataAttributeDS.GROUP_KEY_FOR_GROUP_RELATIONSHIP, (dataAttributeDto.getRelateTo().getGroupKeyForGroupRelationship() == null) ? null : dataAttributeDto.getRelateTo()
                    .getGroupKeyForGroupRelationship().getUrn());
            editionForm.setValue(DataAttributeDS.GROUP_KEY_FOR_GROUP_RELATIONSHIP_VIEW, (dataAttributeDto.getRelateTo().getGroupKeyForGroupRelationship() == null) ? null : dataAttributeDto
                    .getRelateTo().getGroupKeyForGroupRelationship().getCode());

            // Group keys form dimension relationship
            ((CustomSelectItem) editionForm.getItem(DataAttributeDS.GROUP_KEY_FOR_DIMENSION_RELATIONSHIP)).setValues(CommonUtils.getUrnsFromDescriptorDtos(dataAttributeDto.getRelateTo()
                    .getGroupKeyForDimensionRelationship()));
            editionForm
                    .setValue(DataAttributeDS.GROUP_KEY_FOR_DIMENSION_RELATIONSHIP_VIEW, CommonUtils.getDescriptorListAsString(dataAttributeDto.getRelateTo().getGroupKeyForDimensionRelationship()));

            // Dimensions for dimension relationship
            ((CustomSelectItem) editionForm.getItem(DataAttributeDS.DIMENSION_FOR_DIMENSION_RELATIONSHIP)).setValues(CommonUtils.getUrnsFromDimensionComponentDtos(dataAttributeDto.getRelateTo()
                    .getDimensionForDimensionRelationship()));
            editionForm.setValue(DataAttributeDS.DIMENSION_FOR_DIMENSION_RELATIONSHIP_VIEW,
                    CommonUtils.getDimensionComponentListAsString(dataAttributeDto.getRelateTo().getDimensionForDimensionRelationship()));
        }

        // Role
        ((RelatedResourceListItem) editionForm.getItem(DataAttributeDS.ROLE)).setRelatedResources(dataAttributeDto.getRole());
        ((RelatedResourceListItem) editionForm.getItem(DataAttributeDS.ROLE_VIEW)).setRelatedResources(dataAttributeDto.getRole());

        // Usage Status
        editionForm.setValue(DataAttributeDS.USAGE_STATUS, (dataAttributeDto.getUsageStatus() == null) ? null : dataAttributeDto.getUsageStatus().toString());
        editionForm.setValue(DataAttributeDS.USAGE_STATUS_VIEW, CommonUtils.getUsageStatusName(dataAttributeDto.getUsageStatus()));

        // Representation
        editionForm.getItem(DataAttributeDS.ENUMERATED_REPRESENTATION_CODELIST).clearValue();
        editionForm.getItem(DataAttributeDS.ENUMERATED_REPRESENTATION_CODELIST_VIEW).clearValue();
        editionForm.getItem(DataAttributeDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME).clearValue();
        editionForm.getItem(DataAttributeDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME_VIEW).clearValue();
        editionForm.getItem(DataAttributeDS.REPRESENTATION_TYPE).clearValue();
        editionForm.getItem(DataAttributeDS.REPRESENTATION_TYPE_VIEW).clearValue();
        facetEditionForm.clearValues();
        facetStaticEditionForm.clearValues();
        if (dataAttributeDto.getLocalRepresentation() != null) {

            if (RepresentationTypeEnum.ENUMERATION.equals(dataAttributeDto.getLocalRepresentation().getRepresentationType())) {

                // ENUMERATED REPRESENTATION

                setEnumeratedRepresentationTypeInEditionForm();

                if (RelatedResourceTypeEnum.CODELIST.equals(dataAttributeDto.getLocalRepresentation().getEnumeration().getType())) {

                    // Codelist

                    if (!SpecialAttributeTypeEnum.MEASURE_EXTENDS.equals(dataAttributeDto.getSpecialAttributeType())) {
                        ((SearchRelatedResourceLinkItem) editionForm.getItem(DataAttributeDS.ENUMERATED_REPRESENTATION_CODELIST)).setRelatedResource(dataAttributeDto.getLocalRepresentation()
                                .getEnumeration());
                        ((RelatedResourceLinkItem) editionForm.getItem(DataAttributeDS.ENUMERATED_REPRESENTATION_CODELIST_VIEW)).setRelatedResource(dataAttributeDto.getLocalRepresentation()
                                .getEnumeration());
                    }

                } else if (RelatedResourceTypeEnum.CONCEPT_SCHEME.equals(dataAttributeDto.getLocalRepresentation().getEnumeration().getType())) {

                    // ConceptScheme

                    if (SpecialAttributeTypeEnum.MEASURE_EXTENDS.equals(dataAttributeDto.getSpecialAttributeType())) {

                        setConceptSchemeEnumerationRepresentationInEditionForm(dataAttributeDto.getLocalRepresentation().getEnumeration());
                    }
                }

            } else if (RepresentationTypeEnum.TEXT_FORMAT.equals(dataAttributeDto.getLocalRepresentation().getRepresentationType())) {

                // FACET

                editionForm.setValue(DataAttributeDS.REPRESENTATION_TYPE, RepresentationTypeEnum.TEXT_FORMAT.toString());
                editionForm.setValue(DataAttributeDS.REPRESENTATION_TYPE_VIEW, MetamacSrmWeb.getCoreMessages().representationTypeEnumTEXT_FORMAT());

                // Only one facet in a Representation
                FacetDto facetDto = dataAttributeDto.getLocalRepresentation().getTextFormat();
                facetEditionForm.setFacet(facetDto);
                facetStaticEditionForm.setFacet(facetDto);
            }
        }
        FacetFormUtils.setFacetFormVisibility(facetEditionForm, facetStaticEditionForm, editionForm.getValueAsString(DataAttributeDS.REPRESENTATION_TYPE), dataStructureDefinitionMetamacDto);
        editionForm.redraw();

        // Annotations
        editionAnnotationsPanel.setAnnotations(dataAttributeDto.getAnnotations(), dataStructureDefinitionMetamacDto);
    }

    private void setConceptInEditionForm(RelatedResourceDto concept) {
        ((SearchRelatedResourceLinkItem) editionForm.getItem(DataAttributeDS.CONCEPT)).setRelatedResource(concept);
        ((RelatedResourceLinkItem) editionForm.getItem(DataAttributeDS.CONCEPT_VIEW)).setRelatedResource(concept);

        // If the attribute has a measure type, and the enumerated representations has not been specified yet, load the concept scheme that the concept has as a enumerated representation (if it is has
        // been specified).

        if (concept != null) {
            SpecialAttributeTypeEnum specialAttributeTypeEnum = getSpecialAttributeTypeFromEditionForm();
            if (SpecialAttributeTypeEnum.MEASURE_EXTENDS.equals(specialAttributeTypeEnum)) {
                getUiHandlers().retrieveConceptSchemeEnumeratedRepresentationFromConcept(concept.getUrn());
            }
        }

        editionForm.validate(false);
    }

    private void setEnumeratedRepresentationTypeInEditionForm() {
        editionForm.setValue(DataAttributeDS.REPRESENTATION_TYPE, RepresentationTypeEnum.ENUMERATION.toString());
        editionForm.setValue(DataAttributeDS.REPRESENTATION_TYPE_VIEW, getCoreMessages().representationTypeEnumENUMERATION());
    }

    private void setConceptSchemeEnumerationRepresentationInEditionForm(RelatedResourceDto relatedResourceDto) {
        ((SearchRelatedResourceLinkItem) editionForm.getItem(DataAttributeDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME)).setRelatedResource(relatedResourceDto);
        ((RelatedResourceLinkItem) editionForm.getItem(DataAttributeDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME_VIEW)).setRelatedResource(relatedResourceDto);
    }

    @Override
    public List<DataAttributeDto> getSelectedAttributes() {
        if (attributesGrid.getSelectedRecords() != null) {
            List<DataAttributeDto> selectedAttributes = new ArrayList<DataAttributeDto>();
            ListGridRecord[] records = attributesGrid.getSelectedRecords();
            for (int i = 0; i < records.length; i++) {
                DataAttributeRecord record = (DataAttributeRecord) records[i];
                selectedAttributes.add(record.getDataAttributeDto());
            }
            return selectedAttributes;
        }
        return null;
    }

    @Override
    public DataAttributeDto getDsdAttribute() {
        // Code
        dataAttributeDto.setCode(editionForm.getValueAsString(DataAttributeDS.CODE));

        // Type
        dataAttributeDto.setSpecialAttributeType(getSpecialAttributeTypeFromEditionForm());

        // Role
        dataAttributeDto.getRole().clear();
        List<RelatedResourceDto> selectedRoles = ((RelatedResourceListItem) editionForm.getItem(DataAttributeDS.ROLE)).getSelectedRelatedResources();
        dataAttributeDto.getRole().addAll(selectedRoles);

        // Concept
        dataAttributeDto.setCptIdRef(getConceptFromEditionForm());

        // Usage Status
        dataAttributeDto.setUsageStatus(StringUtils.isBlank(editionForm.getValueAsString(DataAttributeDS.USAGE_STATUS)) ? null : UsageStatus.valueOf(editionForm
                .getValueAsString(DataAttributeDS.USAGE_STATUS)));

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
        TypeRelathionship typeRelathionship = TypeRelathionship.valueOf(editionForm.getValueAsString(DataAttributeDS.RELATED_TO));
        dataAttributeDto.getRelateTo().setTypeRelathionship(typeRelathionship);
        if (TypeRelathionship.GROUP_RELATIONSHIP.equals(typeRelathionship)) {
            DescriptorDto descriptorDto = CommonUtils.getDescriptorDtoWithSpecifiedUrn(descriptorDtos, editionForm.getValueAsString(DataAttributeDS.GROUP_KEY_FOR_GROUP_RELATIONSHIP));
            dataAttributeDto.getRelateTo().setGroupKeyForGroupRelationship(descriptorDto);
        } else if (TypeRelathionship.DIMENSION_RELATIONSHIP.equals(typeRelathionship)) {
            List<DescriptorDto> groupKeysRelation = CommonUtils.getDescriptorDtosWithSpecifiedUrns(descriptorDtos,
                    ((CustomSelectItem) editionForm.getItem(DataAttributeDS.GROUP_KEY_FOR_DIMENSION_RELATIONSHIP)).getValues());
            List<DimensionComponentDto> dimensionsRelation = CommonUtils.getDimensionComponentDtosWithSpecifiedUrns(dimensionComponentDtos,
                    ((CustomSelectItem) editionForm.getItem(DataAttributeDS.DIMENSION_FOR_DIMENSION_RELATIONSHIP)).getValues());
            dataAttributeDto.getRelateTo().getGroupKeyForDimensionRelationship().addAll(groupKeysRelation);
            dataAttributeDto.getRelateTo().getDimensionForDimensionRelationship().addAll(dimensionsRelation);
        }

        // Representation
        if (!StringUtils.isBlank(editionForm.getValueAsString(DataAttributeDS.REPRESENTATION_TYPE))) {
            RepresentationTypeEnum representationType = RepresentationTypeEnum.valueOf(editionForm.getValueAsString(DataAttributeDS.REPRESENTATION_TYPE));

            if (dataAttributeDto.getLocalRepresentation() == null) {
                dataAttributeDto.setLocalRepresentation(new RepresentationDto());
            }

            if (RepresentationTypeEnum.ENUMERATION.equals(representationType)) {

                dataAttributeDto.getLocalRepresentation().setRepresentationType(RepresentationTypeEnum.ENUMERATION);

                if (SpecialAttributeTypeEnum.MEASURE_EXTENDS.equals(dataAttributeDto.getSpecialAttributeType())) {

                    // ConceptScheme

                    dataAttributeDto.getLocalRepresentation().setEnumeration(getConceptSchemeEnumeratedRepresentationFromEditionForm());

                } else {

                    // Codelist

                    dataAttributeDto.getLocalRepresentation().setEnumeration(
                            ((SearchRelatedResourceLinkItem) editionForm.getItem(DataAttributeDS.ENUMERATED_REPRESENTATION_CODELIST)).getRelatedResourceDto());
                }

                dataAttributeDto.getLocalRepresentation().setTextFormat(null);

            } else if (RepresentationTypeEnum.TEXT_FORMAT.equals(representationType)) {

                // TEXT FORMAT (FACET)

                dataAttributeDto.getLocalRepresentation().setRepresentationType(RepresentationTypeEnum.TEXT_FORMAT);
                FacetDto facetDto = facetEditionForm.getFacet();
                dataAttributeDto.getLocalRepresentation().setTextFormat(facetDto);
                dataAttributeDto.getLocalRepresentation().setEnumeration(null);
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

    private RelatedResourceDto getConceptFromEditionForm() {
        return ((SearchRelatedResourceLinkItem) editionForm.getItem(DataAttributeDS.CONCEPT)).getRelatedResourceDto();
    }

    private SpecialAttributeTypeEnum getSpecialAttributeTypeFromEditionForm() {
        return !StringUtils.isBlank(editionForm.getValueAsString(DataAttributeDS.SPECIAL_ATTRIBUTE_TYPE)) ? SpecialAttributeTypeEnum.valueOf(editionForm
                .getValueAsString(DataAttributeDS.SPECIAL_ATTRIBUTE_TYPE)) : null;
    }

    private RelatedResourceDto getConceptSchemeEnumeratedRepresentationFromEditionForm() {
        return ((SearchRelatedResourceLinkItem) editionForm.getItem(DataAttributeDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME)).getRelatedResourceDto();
    }

    @Override
    public boolean validate() {
        return !facetEditionForm.isVisible() ? editionForm.validate(false) : (editionForm.validate(false) && facetEditionForm.validate(false));
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
        DataAttributeRecord record = DsdRecordUtils.getAttributeRecord(dataAttributeDto);
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
        facetEditionForm.clearErrors(true);

        selectedComponentLayout.show();
        selectedComponentLayout.redraw();
    }

    private void deselectAttribute() {
        selectedComponentLayout.hide();
        deleteToolStripButton.hide();
    }

    private void setTranslationsShowed(boolean translationsShowed) {
        viewAnnotationsPanel.setTranslationsShowed(translationsShowed);
        editionAnnotationsPanel.setTranslationsShowed(translationsShowed);
    }

    private void showDeleteToolStripButton() {
        if (DsdClientSecurityUtils.canDeleteAttribute(dataStructureDefinitionMetamacDto)) {
            deleteToolStripButton.show();
        }
    }

    //
    // FORM ITEMS
    //

    private CustomSelectItem createSpecialAttributeTypeItem(String name, String title) {
        CustomSelectItem item = new CustomSelectItem(DataAttributeDS.SPECIAL_ATTRIBUTE_TYPE, getConstants().dsdAttributeType());
        item.setValueMap(CommonUtils.getDataAttributeTypeHashMap());
        item.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                editionForm.validate(false);
                editionForm.markForRedraw();

                // If the selected type is time or measure, and if the concept has not been specified yet, load the default concept for the time or measure attribute (the default concepts are
                // specified in the data directory).

                RelatedResourceDto selectedConcept = getConceptFromEditionForm();
                if (selectedConcept == null) {
                    String value = event.getValue() != null ? (String) event.getValue() : null;
                    SpecialAttributeTypeEnum specialAttributeTypeEnum = CommonUtils.getSpecialAttributeTypeEnum(value);
                    if (SpecialAttributeTypeEnum.TIME_EXTENDS.equals(specialAttributeTypeEnum) || SpecialAttributeTypeEnum.MEASURE_EXTENDS.equals(specialAttributeTypeEnum)) {
                        getUiHandlers().retrieveDefaultConcept(specialAttributeTypeEnum, dataStructureDefinitionMetamacDto.getUrn());
                    }
                }
            }
        });
        return item;
    }

    private CustomSelectItem createRepresentationTypeItem(String name, String title) {
        final CustomSelectItem representationTypeItem = new CustomSelectItem(name, title);
        representationTypeItem.setValueMap(org.siemac.metamac.srm.web.client.utils.CommonUtils.getTypeRepresentationEnumHashMap());
        representationTypeItem.setRedrawOnChange(true);
        // Show FacetForm if RepresentationTypeEnum = NON_NUMERATED
        representationTypeItem.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                FacetFormUtils.setFacetFormVisibility(facetEditionForm, facetStaticEditionForm, representationTypeItem.getValueAsString(), dataStructureDefinitionMetamacDto);
            }
        });

        // MEASURE ATTRIBUTE VALIDATOR

        CustomValidator measureCustomValidator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                // Measure attributes must be enumerated
                if (CommonUtils.isAttributeTypeMeasureAttribute(editionForm.getValueAsString(DataAttributeDS.SPECIAL_ATTRIBUTE_TYPE))) {
                    return RepresentationTypeEnum.ENUMERATION.toString().equals(value);
                }
                return true;
            }
        };
        measureCustomValidator.setErrorMessage(getMessages().errorRequiredEnumeratedRepresentationInMeasureAttribute());

        // TIME ATTRIBUTE VALIDATOR

        CustomValidator timeCustomValidator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                // Time attributes must have a non enumerated representation
                if (CommonUtils.isAttributeTypeTimeAttribute(editionForm.getValueAsString(DataAttributeDS.SPECIAL_ATTRIBUTE_TYPE))) {
                    return RepresentationTypeEnum.TEXT_FORMAT.toString().equals(value);
                }
                return true;
            }
        };
        timeCustomValidator.setErrorMessage(getMessages().errorRequiredNonEnumeratedRepresentationInTimeAttribute());

        // SPATIAL DIMENSION VALIDATOR

        CustomValidator spatialCustomValidator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                // Spatial attributes cannot have a non enumerated representation
                if (SpecialAttributeTypeEnum.SPATIAL_EXTENDS.equals(getSpecialAttributeTypeFromEditionForm())) {
                    return !RepresentationTypeEnum.TEXT_FORMAT.toString().equals(value);
                }
                return true;
            }
        };
        spatialCustomValidator.setErrorMessage(getMessages().errorEnumeratedRepresentationInSpatialAttribute());

        representationTypeItem.setValidators(measureCustomValidator, timeCustomValidator, spatialCustomValidator);

        return representationTypeItem;
    }

    private SearchRelatedResourceLinkItem createConceptItem() {
        final String fieldName = DataAttributeDS.CONCEPT;

        SearchSrmItemLinkItemWithSchemeFilterItem item = new SearchSrmItemLinkItemWithSchemeFilterItem(fieldName, getConstants().concept(), SrmWebConstants.FORM_LIST_MAX_RESULTS,
                getCustomLinkItemNavigationClickHandler()) {

            @Override
            protected void retrieveItemSchemes(int firstResult, int maxResults, RelatedResourceWebCriteria webCriteria) {
                SpecialAttributeTypeEnum specialAttributeTypeEnum = getSpecialAttributeTypeFromEditionForm();
                retrieveConceptSchemes(specialAttributeTypeEnum, firstResult, maxResults, webCriteria.getCriteria(), webCriteria.isOnlyLastVersion());
            }
            @Override
            protected void retrieveItems(int firstResult, int maxResults, RelatedResourceItemWebCriteria webCriteria) {
                SpecialAttributeTypeEnum specialAttributeTypeEnum = getSpecialAttributeTypeFromEditionForm();
                retrieveConcepts(specialAttributeTypeEnum, firstResult, maxResults, webCriteria.getCriteria(), webCriteria.getItemSchemeUrn(), webCriteria.isItemSchemeLastVersion());
            }
        };
        item.setRequired(true);

        return item;
    }

    private void retrieveConceptSchemes(SpecialAttributeTypeEnum specialAttributeTypeEnum, int firstResult, int maxResults, String criteria, boolean isLastVersion) {
        ConceptSchemeWebCriteria conceptSchemeWebCriteria = new ConceptSchemeWebCriteria(criteria);
        conceptSchemeWebCriteria.setDsdUrn(dataStructureDefinitionMetamacDto.getUrn());
        conceptSchemeWebCriteria.setIsLastVersion(isLastVersion);

        getUiHandlers().retrieveConceptSchemes(specialAttributeTypeEnum, firstResult, maxResults, conceptSchemeWebCriteria);
    }

    private void retrieveConcepts(SpecialAttributeTypeEnum specialAttributeTypeEnum, int firstResult, int maxResults, String criteria, String conceptSchemeUrn, boolean isLastVersion) {
        ConceptWebCriteria conceptWebCriteria = new ConceptWebCriteria();
        conceptWebCriteria.setCriteria(criteria);
        conceptWebCriteria.setDsdUrn(dataStructureDefinitionMetamacDto.getUrn());
        conceptWebCriteria.setItemSchemeUrn(conceptSchemeUrn);
        conceptWebCriteria.setIsLastVersion(isLastVersion);

        getUiHandlers().retrieveConcepts(specialAttributeTypeEnum, firstResult, maxResults, conceptWebCriteria);
    }

    private RelatedResourceListItem createRoleItem() {

        final String fieldName = DataAttributeDS.ROLE;

        SearchRelatedResourceListItemWithSchemeFilterItem item = new SearchRelatedResourceListItemWithSchemeFilterItem(fieldName, getConstants().dsdAttributeRole(),
                SrmWebConstants.FORM_LIST_MAX_RESULTS, getListRecordNavigationClickHandler()) {

            @Override
            protected void retrieveItemSchemes(int firstResult, int maxResults, RelatedResourceWebCriteria webCriteria) {
                retrieveConceptSchemesForAttributeRole(firstResult, maxResults, webCriteria.getCriteria(), webCriteria.isOnlyLastVersion());
            }
            @Override
            protected void retrieveItems(int firstResult, int maxResults, RelatedResourceItemWebCriteria webCriteria) {
                retrieveConceptsForAttributeRole(firstResult, maxResults, webCriteria.getCriteria(), webCriteria.getItemSchemeUrn(), webCriteria.isItemSchemeLastVersion());
            }
        };

        return item;
    }

    private void retrieveConceptSchemesForAttributeRole(int firstResult, int maxResults, String criteria, boolean isLastVersion) {
        ConceptSchemeWebCriteria conceptSchemeWebCriteria = new ConceptSchemeWebCriteria(criteria);
        conceptSchemeWebCriteria.setDsdUrn(dataStructureDefinitionMetamacDto.getUrn());
        conceptSchemeWebCriteria.setIsLastVersion(isLastVersion);

        getUiHandlers().retrieveConceptSchemesForAttributeRole(firstResult, maxResults, conceptSchemeWebCriteria);
    }

    private void retrieveConceptsForAttributeRole(int firstResult, int maxResults, String criteria, String conceptSchemeUrn, boolean isLastVersion) {
        ConceptWebCriteria conceptWebCriteria = new ConceptWebCriteria();
        conceptWebCriteria.setCriteria(criteria);
        conceptWebCriteria.setItemSchemeUrn(conceptSchemeUrn);
        conceptWebCriteria.setDsdUrn(dataStructureDefinitionMetamacDto.getUrn());
        conceptWebCriteria.setIsLastVersion(isLastVersion);

        getUiHandlers().retrieveConceptsForAttributeRole(firstResult, maxResults, conceptWebCriteria);
    }

    private SearchRelatedResourceLinkItem createEnumeratedRepresentationItem(String name, String title) {
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;
        final SearchRelatedResourceLinkItem codelistItem = new SearchRelatedResourceLinkItem(name, title, getCustomLinkItemNavigationClickHandler());
        codelistItem.setRequired(true);
        codelistItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {

                RelatedResourceDto concept = ((SearchRelatedResourceLinkItem) editionForm.getItem(DataAttributeDS.CONCEPT)).getRelatedResourceDto();
                final String conceptUrn = concept != null ? concept.getUrn() : null;

                if (StringUtils.isBlank(conceptUrn)) {
                    // If a concept has not been selected, show a message and do not let the user to select a codelist
                    InformationWindow conceptRequiredWindow = new InformationWindow(getConstants().codelistSelection(), getConstants().dsdAttributeCodelistSelectionConceptRequired());
                    conceptRequiredWindow.show();
                } else {

                    final SpecialAttributeTypeEnum specialAttributeTypeEnum = CommonUtils.getSpecialAttributeTypeEnum(editionForm.getValueAsString(DataAttributeDS.SPECIAL_ATTRIBUTE_TYPE));

                    searchCodelistForEnumeratedRepresentationWindow = new SearchRelatedResourcePaginatedWindow(getConstants().codelistSelection(), MAX_RESULTS, new PaginatedAction() {

                        @Override
                        public void retrieveResultSet(int firstResult, int maxResults) {
                            retrieveCodelistsForEnumeratedRepresentation(firstResult, maxResults, searchCodelistForEnumeratedRepresentationWindow.getRelatedResourceCriteria(), conceptUrn,
                                    searchCodelistForEnumeratedRepresentationWindow.getIsLastVersionValue(), specialAttributeTypeEnum);
                        }
                    });
                    searchCodelistForEnumeratedRepresentationWindow.setInfoMessage(getConstants().dsdAttributeEnumeratedRepresentationInfoMessage());

                    searchCodelistForEnumeratedRepresentationWindow.showIsLastVersionItem();
                    searchCodelistForEnumeratedRepresentationWindow.getIsLastVersionItem().addChangedHandler(new ChangedHandler() {

                        @Override
                        public void onChanged(ChangedEvent event) {
                            retrieveCodelistsForEnumeratedRepresentation(FIRST_RESULST, MAX_RESULTS, searchCodelistForEnumeratedRepresentationWindow.getRelatedResourceCriteria(), conceptUrn,
                                    searchCodelistForEnumeratedRepresentationWindow.getIsLastVersionValue(), specialAttributeTypeEnum);
                        }
                    });

                    // Load codelists (to populate the selection window)
                    retrieveCodelistsForEnumeratedRepresentation(FIRST_RESULST, MAX_RESULTS, null, conceptUrn, searchCodelistForEnumeratedRepresentationWindow.getIsLastVersionValue(),
                            specialAttributeTypeEnum);

                    searchCodelistForEnumeratedRepresentationWindow.getListGridItem().getListGrid().setSelectionType(SelectionStyle.SINGLE);
                    searchCodelistForEnumeratedRepresentationWindow.getListGridItem().setSearchAction(new SearchPaginatedAction() {

                        @Override
                        public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                            retrieveCodelistsForEnumeratedRepresentation(firstResult, maxResults, criteria, conceptUrn, searchCodelistForEnumeratedRepresentationWindow.getIsLastVersionValue(),
                                    specialAttributeTypeEnum);
                        }
                    });

                    searchCodelistForEnumeratedRepresentationWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                        @Override
                        public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                            RelatedResourceDto selectedCodelist = searchCodelistForEnumeratedRepresentationWindow.getSelectedRelatedResource();
                            searchCodelistForEnumeratedRepresentationWindow.markForDestroy();
                            // Set selected codelist in form
                            ((SearchRelatedResourceLinkItem) editionForm.getItem(DataAttributeDS.ENUMERATED_REPRESENTATION_CODELIST)).setRelatedResource(selectedCodelist);
                            editionForm.validate(false);
                        }
                    });
                }
            }
        });
        return codelistItem;
    }

    private void retrieveCodelistsForEnumeratedRepresentation(int firstResult, int maxResults, String criteria, String conceptUrn, boolean isLastVersion, SpecialAttributeTypeEnum attributeType) {
        CodelistWebCriteria codelistWebCriteria = new CodelistWebCriteria();
        codelistWebCriteria.setCriteria(criteria);
        codelistWebCriteria.setConceptUrn(conceptUrn);
        codelistWebCriteria.setIsLastVersion(isLastVersion);

        getUiHandlers().retrieveCodelistsForEnumeratedRepresentation(firstResult, maxResults, codelistWebCriteria, attributeType);
    }

    private SearchRelatedResourceLinkItem createMeasureAttributeEnumeratedRepresentationItem(String name, String title) {
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;
        final SearchRelatedResourceLinkItem conceptSchemeItem = new SearchRelatedResourceLinkItem(name, title, getCustomLinkItemNavigationClickHandler());
        conceptSchemeItem.setRequired(true);
        conceptSchemeItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {

                searchConceptSchemeForEnumeratedRepresentationWindow = new SearchRelatedResourcePaginatedWindow(getConstants().conceptSchemeSelection(), MAX_RESULTS, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        retrieveConceptSchemesForEnumeratedRepresentation(firstResult, maxResults, searchConceptSchemeForEnumeratedRepresentationWindow.getRelatedResourceCriteria(),
                                searchConceptSchemeForEnumeratedRepresentationWindow.getIsLastVersionValue());
                    }
                });
                searchConceptSchemeForEnumeratedRepresentationWindow.setInfoMessage(getConstants().dsdAttributeEnumeratedRepresentationInfoMessage());

                searchConceptSchemeForEnumeratedRepresentationWindow.showIsLastVersionItem();
                searchConceptSchemeForEnumeratedRepresentationWindow.getIsLastVersionItem().addChangedHandler(new ChangedHandler() {

                    @Override
                    public void onChanged(ChangedEvent event) {
                        retrieveConceptSchemesForEnumeratedRepresentation(FIRST_RESULST, MAX_RESULTS, searchConceptSchemeForEnumeratedRepresentationWindow.getRelatedResourceCriteria(),
                                searchConceptSchemeForEnumeratedRepresentationWindow.getIsLastVersionValue());
                    }
                });

                // Load conceptSchemes (to populate the selection window)
                retrieveConceptSchemesForEnumeratedRepresentation(FIRST_RESULST, MAX_RESULTS, null, searchConceptSchemeForEnumeratedRepresentationWindow.getIsLastVersionValue());

                searchConceptSchemeForEnumeratedRepresentationWindow.getListGridItem().getListGrid().setSelectionType(SelectionStyle.SINGLE);
                searchConceptSchemeForEnumeratedRepresentationWindow.getListGridItem().setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                        retrieveConceptSchemesForEnumeratedRepresentation(firstResult, maxResults, criteria, searchConceptSchemeForEnumeratedRepresentationWindow.getIsLastVersionValue());
                    }
                });

                searchConceptSchemeForEnumeratedRepresentationWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        RelatedResourceDto selectedConceptScheme = searchConceptSchemeForEnumeratedRepresentationWindow.getSelectedRelatedResource();
                        searchConceptSchemeForEnumeratedRepresentationWindow.markForDestroy();
                        // Set selected conceptScheme in form
                        ((SearchRelatedResourceLinkItem) editionForm.getItem(DataAttributeDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME)).setRelatedResource(selectedConceptScheme);
                        editionForm.validate(false);
                    }
                });
            }
        });
        return conceptSchemeItem;
    }

    private void retrieveConceptSchemesForEnumeratedRepresentation(int firstResult, int maxResults, String criteria, boolean isLastVersion) {
        ConceptSchemeWebCriteria conceptSchemeWebCriteria = new ConceptSchemeWebCriteria();
        conceptSchemeWebCriteria.setCriteria(criteria);
        conceptSchemeWebCriteria.setDsdUrn(dataStructureDefinitionMetamacDto.getUrn());
        conceptSchemeWebCriteria.setIsLastVersion(isLastVersion);

        getUiHandlers().retrieveConceptSchemesForEnumeratedRepresentation(firstResult, maxResults, conceptSchemeWebCriteria);
    }

    // ------------------------------------------------------------------------------------------------------------
    // FORM ITEM IF FUNCTIONS
    // ------------------------------------------------------------------------------------------------------------

    // CODE

    private FormItemIfFunction getCodeFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return DsdsFormUtils.canAttributeCodeBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }

    private FormItemIfFunction getStaticCodeFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !DsdsFormUtils.canAttributeCodeBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }

    // CONCEPT

    private FormItemIfFunction getConceptFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return DsdsFormUtils.canAttributeConceptBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }

    private FormItemIfFunction getStaticConceptFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !DsdsFormUtils.canAttributeConceptBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }

    // USAGE STATUS

    private FormItemIfFunction getUsageStatusFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return DsdsFormUtils.canAttributeUsageStatusBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }

    private FormItemIfFunction getStaticUsageStatusFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !DsdsFormUtils.canAttributeUsageStatusBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }

    // ROLE

    private FormItemIfFunction getRoleFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return DsdsFormUtils.canAttributeRoleBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }

    private FormItemIfFunction getStaticRoleFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !DsdsFormUtils.canAttributeRoleBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }

    // RELATED TO

    private FormItemIfFunction getRelatedToFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return DsdsFormUtils.canAttributeRelatedToBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }

    private FormItemIfFunction getStaticRelatedToFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !DsdsFormUtils.canAttributeRelatedToBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }

    // GROUPS KEYS FOR DIMENSION RELATIONSHIP

    private FormItemIfFunction getGroupKeysForDimensionRelationshipFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return CommonUtils.isDimensionRelationshipType(editionForm.getValueAsString(DataAttributeDS.RELATED_TO))
                        && DsdsFormUtils.canAttributeGroupKeysForDimensionRelationshipBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }

    private FormItemIfFunction getStaticGroupKeysForDimensionRelationshipFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return CommonUtils.isDimensionRelationshipType(editionForm.getValueAsString(DataAttributeDS.RELATED_TO))
                        && !DsdsFormUtils.canAttributeGroupKeysForDimensionRelationshipBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }

    // DIMENSIONS FOR DIMENSION RELATIONSHIP

    private FormItemIfFunction getDimensionsForDimensionRelationshipFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return CommonUtils.isDimensionRelationshipType(editionForm.getValueAsString(DataAttributeDS.RELATED_TO))
                        && DsdsFormUtils.canAttributeDimensionsForDimensionRelationshipBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }

    private FormItemIfFunction getStaticDimensionsForDimensionRelationshipFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return CommonUtils.isDimensionRelationshipType(editionForm.getValueAsString(DataAttributeDS.RELATED_TO))
                        && !DsdsFormUtils.canAttributeDimensionsForDimensionRelationshipBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }

    // GROUPS KEYS FOR GROUP RELATIONSHIP

    private FormItemIfFunction getGroupKeysForGroupRelationshipFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return CommonUtils.isGroupRelationshipType(editionForm.getValueAsString(DataAttributeDS.RELATED_TO))
                        && DsdsFormUtils.canAttributeGroupKeysForGroupRelationshipBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }

    private FormItemIfFunction getStaticGroupKeysForGroupRelationshipFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return CommonUtils.isGroupRelationshipType(editionForm.getValueAsString(DataAttributeDS.RELATED_TO))
                        && !DsdsFormUtils.canAttributeGroupKeysForGroupRelationshipBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }

    // REPRESENTATION TYPE

    private FormItemIfFunction getRepresentationTypeFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return DsdsFormUtils.canAttributeRepresentationTypeBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }

    private FormItemIfFunction getStaticRepresentationTypeFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !DsdsFormUtils.canAttributeRepresentationTypeBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }

    // CODELIST (ENUMERATED REPRESENTATION)

    private FormItemIfFunction getCodelistEnumeratedRepresentationFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                // Shown when the representation type selected is ENUMERATION and the enumerated representation can be edited
                return CommonUtils.isAttributeCodelistEnumeratedRepresentationVisible(editionForm.getValueAsString(DataAttributeDS.REPRESENTATION_TYPE),
                        editionForm.getValueAsString(DataAttributeDS.SPECIAL_ATTRIBUTE_TYPE))
                        && DsdsFormUtils.canAttributeCodelistEnumeratedRepresentationBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }

    private FormItemIfFunction getStaticCodelistEnumeratedRepresentationFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                // Shown when the representation type selected is ENUMERATION and the enumerated representation can NOT be edited
                return CommonUtils.isAttributeCodelistEnumeratedRepresentationVisible(editionForm.getValueAsString(DataAttributeDS.REPRESENTATION_TYPE),
                        editionForm.getValueAsString(DataAttributeDS.SPECIAL_ATTRIBUTE_TYPE))
                        && !DsdsFormUtils.canAttributeCodelistEnumeratedRepresentationBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }

    // CONCEPT SCHEME (ENUMERATED REPRESENTATION)

    private FormItemIfFunction getConceptSchemeEnumeratedRepresentationFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return CommonUtils.isAttributeConceptSchemeEnumeratedRepresentationVisible(editionForm.getValueAsString(DataAttributeDS.REPRESENTATION_TYPE),
                        editionForm.getValueAsString(DataAttributeDS.SPECIAL_ATTRIBUTE_TYPE))
                        && DsdsFormUtils.canAttributeConceptSchemeEnumeratedRepresentationBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }

    private FormItemIfFunction getStaticConceptSchemeEnumeratedRepresentationFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return CommonUtils.isAttributeConceptSchemeEnumeratedRepresentationVisible(editionForm.getValueAsString(DataAttributeDS.REPRESENTATION_TYPE),
                        editionForm.getValueAsString(DataAttributeDS.SPECIAL_ATTRIBUTE_TYPE))
                        && !DsdsFormUtils.canAttributeConceptSchemeEnumeratedRepresentationBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }

    // ------------------------------------------------------------------------------------------------------------
    // CLICK HANDLERS
    // ------------------------------------------------------------------------------------------------------------

    private CustomLinkItemNavigationClickHandler getCustomLinkItemNavigationClickHandler() {
        return new CustomLinkItemNavigationClickHandler() {

            @Override
            public BaseUiHandlers getBaseUiHandlers() {
                return getUiHandlers();
            }
        };
    }

    private ListRecordNavigationClickHandler getListRecordNavigationClickHandler() {
        return new ListRecordNavigationClickHandler() {

            @Override
            public BaseUiHandlers getBaseUiHandlers() {
                return getUiHandlers();
            }
        };
    }
}
