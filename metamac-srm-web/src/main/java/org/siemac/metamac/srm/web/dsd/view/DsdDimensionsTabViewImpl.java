package org.siemac.metamac.srm.web.dsd.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getCoreMessages;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.web.client.constants.SrmWebConstants;
import org.siemac.metamac.srm.web.client.representation.widgets.StaticFacetForm;
import org.siemac.metamac.srm.web.client.utils.FacetFormUtils;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.client.widgets.AnnotationsPanel;
import org.siemac.metamac.srm.web.client.widgets.RelatedResourceLinkItem;
import org.siemac.metamac.srm.web.client.widgets.RelatedResourceListItem;
import org.siemac.metamac.srm.web.client.widgets.SearchMultipleRelatedResourcePaginatedWindow;
import org.siemac.metamac.srm.web.client.widgets.SearchRelatedResourceLinkItem;
import org.siemac.metamac.srm.web.client.widgets.SearchRelatedResourcePaginatedWindow;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeDS;
import org.siemac.metamac.srm.web.dsd.model.ds.DimensionDS;
import org.siemac.metamac.srm.web.dsd.model.record.DimensionRecord;
import org.siemac.metamac.srm.web.dsd.presenter.DsdDimensionsTabPresenter;
import org.siemac.metamac.srm.web.dsd.utils.CommonUtils;
import org.siemac.metamac.srm.web.dsd.utils.DsdClientSecurityUtils;
import org.siemac.metamac.srm.web.dsd.utils.DsdRecordUtils;
import org.siemac.metamac.srm.web.dsd.utils.DsdsFormUtils;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdDimensionsTabUiHandlers;
import org.siemac.metamac.srm.web.dsd.widgets.DsdFacetForm;
import org.siemac.metamac.srm.web.dsd.widgets.NewDimensionWindow;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.srm.web.shared.criteria.ConceptSchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptWebCriteria;
import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;
import org.siemac.metamac.web.common.client.widgets.BaseCustomListGrid;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.InformationWindow;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;
import org.siemac.metamac.web.common.client.widgets.handlers.CustomLinkItemNavigationClickHandler;
import org.siemac.metamac.web.common.client.widgets.handlers.ListRecordNavigationClickHandler;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DimensionComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.FacetDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.RepresentationDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.FacetValueTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RepresentationTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeComponent;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeDimensionComponent;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.VerticalAlignment;
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
import com.smartgwt.client.widgets.form.validator.CustomValidator;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class DsdDimensionsTabViewImpl extends ViewWithUiHandlers<DsdDimensionsTabUiHandlers> implements DsdDimensionsTabPresenter.DsdDimensionsTabView {

    private DataStructureDefinitionMetamacDto            dataStructureDefinitionMetamacDto;

    private DimensionComponentDto                        dimensionComponentDto;

    private VLayout                                      panel;
    private VLayout                                      selectedComponentLayout;
    private BaseCustomListGrid                           dimensionsGrid;
    private InternationalMainFormLayout                  mainFormLayout;

    private AnnotationsPanel                             viewAnnotationsPanel;
    private AnnotationsPanel                             editionAnnotationsPanel;

    // VIEW FORM

    private GroupDynamicForm                             form;
    private StaticFacetForm                              facetForm;

    // EDITION FORM

    private GroupDynamicForm                             editionForm;
    private DsdFacetForm                                 facetEditionForm;
    private StaticFacetForm                              facetStaticEditionForm;

    private ToolStripButton                              newToolStripButton;
    private ToolStripButton                              deleteToolStripButton;

    private DeleteConfirmationWindow                     deleteConfirmationWindow;

    private NewDimensionWindow                           newDimensionWindow;
    private SearchRelatedResourcePaginatedWindow         searchConceptWindow;
    private SearchMultipleRelatedResourcePaginatedWindow searchConceptsForRolesWindow;
    private SearchRelatedResourcePaginatedWindow         searchConceptSchemeForEnumeratedRepresentationWindow;
    private SearchRelatedResourcePaginatedWindow         searchCodelistForEnumeratedRepresentationWindow;

    @Inject
    public DsdDimensionsTabViewImpl() {
        super();
        panel = new VLayout();

        // ··················
        // List of dimensions
        // ··················

        // ToolStrip

        newToolStripButton = new ToolStripButton(getConstants().actionNew(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.newListGrid().getURL());
        newToolStripButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                selectDimension(new DimensionComponentDto());
            }
        });

        deleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().dsdDeleteConfirmationTitle(), getConstants().dsdDimensionDeleteConfirmation());
        deleteConfirmationWindow.setVisible(false);

        deleteToolStripButton = new ToolStripButton(getConstants().actionDelete(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.deleteListGrid().getURL());
        deleteToolStripButton.setVisible(false);
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

        dimensionsGrid = new BaseCustomListGrid();
        dimensionsGrid.setWidth100();
        dimensionsGrid.setHeight(150);
        dimensionsGrid.setSelectionType(SelectionStyle.SIMPLE);
        dimensionsGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
        ListGridField idLogicField = new ListGridField(DimensionRecord.CODE, getConstants().dsdDimensionsId());
        ListGridField typeField = new ListGridField(DimensionRecord.TYPE, getConstants().dsdDimensionsType());
        ListGridField conceptField = new ListGridField(DimensionRecord.CONCEPT, getConstants().concept());
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
                if (dimensionComponentDto.getId() == null) {
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
        form = new GroupDynamicForm(getConstants().dsdDimensionDetails());
        ViewTextItem code = new ViewTextItem(DimensionDS.CODE_VIEW, getConstants().dsdDimensionsId());
        ViewTextItem dimensionType = new ViewTextItem(DimensionDS.TYPE_VIEW, getConstants().dsdDimensionsType());
        RelatedResourceLinkItem concept = new RelatedResourceLinkItem(DimensionDS.CONCEPT, getConstants().concept(), getCustomLinkItemNavigationClickHandler());

        RelatedResourceListItem conceptRoleItem = new RelatedResourceListItem(DimensionDS.ROLE, getConstants().dsdDimensionsRole(), false, getListRecordNavigationClickHandler());
        conceptRoleItem.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return CommonUtils.isDimensionRoleVisible(dimensionComponentDto.getTypeDimensionComponent());
            }
        });

        ViewTextItem staticRepresentationTypeItem = new ViewTextItem(DimensionDS.REPRESENTATION_TYPE, getConstants().representation());
        RelatedResourceLinkItem codelist = new RelatedResourceLinkItem(DimensionDS.ENUMERATED_REPRESENTATION_CODELIST, getConstants().codelist(), getCustomLinkItemNavigationClickHandler());
        RelatedResourceLinkItem conceptScheme = new RelatedResourceLinkItem(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME, getConstants().conceptScheme(),
                getCustomLinkItemNavigationClickHandler());

        ViewTextItem urn = new ViewTextItem(DimensionDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(DimensionDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());

        form.setFields(code, dimensionType, concept, conceptRoleItem, staticRepresentationTypeItem, codelist, conceptScheme, urn, urnProvider);

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
        editionForm = new GroupDynamicForm(getConstants().dsdDimensionDetails());

        // Code

        RequiredTextItem code = new RequiredTextItem(DimensionDS.CODE, getConstants().dsdDimensionsId());
        code.setRedrawOnChange(true);
        code.setValidators(SemanticIdentifiersUtils.getDimensionIdentifierCustomValidator());
        code.setShowIfCondition(getCodeFormItemIfFunction());

        ViewTextItem staticCodeEdit = new ViewTextItem(DimensionDS.CODE_VIEW, getConstants().dsdDimensionsId());
        staticCodeEdit.setRedrawOnChange(true);
        staticCodeEdit.setShowIfCondition(getStaticCodeFormItemIfFunction());

        // TYPE

        ViewTextItem dimensionType = new ViewTextItem(DimensionDS.TYPE, getConstants().dsdDimensionsType());
        dimensionType.setShowIfCondition(FormItemUtils.getFalseFormItemIfFunction());
        ViewTextItem dimensionTypeView = new ViewTextItem(DimensionDS.TYPE_VIEW, getConstants().dsdDimensionsType());

        // CONCEPT

        SearchRelatedResourceLinkItem concept = createConceptItem(DimensionDS.CONCEPT, getConstants().concept()); // Shown in editionMode, only when the concept is editable
        concept.setShowIfCondition(getConceptFormItemIfFunction());

        RelatedResourceLinkItem staticConcept = new RelatedResourceLinkItem(DimensionDS.CONCEPT_VIEW, getConstants().concept(), getCustomLinkItemNavigationClickHandler()); // This item is shown when
                                                                                                                                                                            // the concept can not be
                                                                                                                                                                            // edited
        staticConcept.setShowIfCondition(getStaticConceptFormItemIfFunction());

        // ROLES

        RelatedResourceListItem conceptRoleItem = createRoleItem(DimensionDS.ROLE, getConstants().dsdDimensionsRole());
        conceptRoleItem.setShowIfCondition(getRoleFormItemIfFunction());

        RelatedResourceListItem staticConceptRoleItem = new RelatedResourceListItem(DimensionDS.ROLE_VIEW, getConstants().dsdDimensionsRole(), false, getListRecordNavigationClickHandler());
        staticConceptRoleItem.setShowIfCondition(getStaticRoleFormItemIfFunction());

        // REPRESENTATION TYPE

        CustomSelectItem representationTypeItem = createRepresentationTypeItem(DimensionDS.REPRESENTATION_TYPE, getConstants().representation());
        representationTypeItem.setShowIfCondition(getRepresentationTypeFormItemIfFunction());

        ViewTextItem staticRepresentationTypeItem = new ViewTextItem(DimensionDS.REPRESENTATION_TYPE_VIEW, getConstants().representation());
        staticRepresentationTypeItem.setShowIfCondition(getStaticRepresentationTypeFormItemIfFunction());

        // ENUMERATED REPRESENTATION

        // Codelist

        SearchRelatedResourceLinkItem staticEditableCodelist = createEnumeratedRepresentationItem(DimensionDS.ENUMERATED_REPRESENTATION_CODELIST, getConstants().codelist());
        staticEditableCodelist.setShowIfCondition(getCodelistEnumeratedRepresentationFormItemIfFunction()); // Shown in editionMode, only when the codelist is editable

        RelatedResourceLinkItem staticCodelist = new RelatedResourceLinkItem(DimensionDS.ENUMERATED_REPRESENTATION_CODELIST_VIEW, getConstants().codelist(), getCustomLinkItemNavigationClickHandler());
        staticCodelist.setShowIfCondition(getStaticCodelistEnumeratedRepresentationFormItemIfFunction()); // This item is shown when the codelist can not be edited

        // ConceptScheme

        SearchRelatedResourceLinkItem staticEditableConceptScheme = createMeasureDimensionEnumeratedRepresentationItem(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME, getConstants()
                .conceptScheme());
        staticEditableConceptScheme.setShowIfCondition(getConceptSchemeEnumeratedRepresentationFormItemIfFunction()); // Shown in editionMode, only when the conceptScheme is editable

        RelatedResourceLinkItem staticConceptScheme = new RelatedResourceLinkItem(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME_VIEW, getConstants().conceptScheme(),
                getCustomLinkItemNavigationClickHandler());
        staticConceptScheme.setShowIfCondition(getStaticConceptSchemeEnumeratedRepresentationFormItemIfFunction()); // This item is shown when the conceptScheme can not be edited

        // URNs

        ViewTextItem urn = new ViewTextItem(DimensionDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(DimensionDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());

        editionForm.setFields(code, staticCodeEdit, dimensionType, dimensionTypeView, concept, staticConcept, conceptRoleItem, staticConceptRoleItem, representationTypeItem,
                staticRepresentationTypeItem, staticEditableCodelist, staticCodelist, staticEditableConceptScheme, staticConceptScheme, urn, urnProvider);

        // FACET

        facetEditionForm = new DsdFacetForm();
        facetEditionForm.setVisible(false);
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
        timeValidator.setErrorMessage(getMessages().errorTextTypeInTimeDimension());
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
    public void setDsdDimensions(DataStructureDefinitionMetamacDto dsd, List<DimensionComponentDto> dimensionComponentDtos) {
        this.dataStructureDefinitionMetamacDto = dsd;
        deselectDimension();

        // Security
        newToolStripButton.setVisible(DsdClientSecurityUtils.canCreateDimension(dataStructureDefinitionMetamacDto));
        mainFormLayout.setCanEdit(DsdClientSecurityUtils.canUpdateDimension(dataStructureDefinitionMetamacDto));

        dimensionsGrid.setData(DsdRecordUtils.getDimensionRecords(dimensionComponentDtos));
    }

    @Override
    public void setDefaultDimensionToCreate(DimensionComponentDto dimensionComponentDto) {
        mainFormLayout.setTitleLabelContents(StringUtils.EMPTY);
        deleteToolStripButton.hide();
        dimensionsGrid.deselectAllRecords();
        setDimensionEditionMode(dimensionComponentDto);
        mainFormLayout.setEditionMode();

        newDimensionWindow.markForDestroy();

        selectedComponentLayout.show();
        selectedComponentLayout.markForRedraw();
    }

    @Override
    public void setConceptSchemes(GetRelatedResourcesResult result) {
        if (searchConceptWindow != null) {
            searchConceptWindow.getInitialSelectionItem().setValueMap(org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourceHashMap(result.getRelatedResourceDtos()));
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
    public void setConceptSchemesForDimensionRole(GetRelatedResourcesResult result) {
        if (searchConceptsForRolesWindow != null) {
            searchConceptsForRolesWindow.getInitialSelectionItem().setValueMap(org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourceHashMap(result.getRelatedResourceDtos()));
        }
    }

    @Override
    public void setConceptsForDimensionRole(GetRelatedResourcesResult result) {
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
            // If there is no results, show an info message (maybe the dimension concept has no variable)
            if (result.getRelatedResourceDtos().size() > 0) {
                searchCodelistForEnumeratedRepresentationWindow.hideInfoMessage();
            } else {
                searchCodelistForEnumeratedRepresentationWindow.showInfoMessage();
            }
        }
    }

    @Override
    public void setConceptSchemesForMeasureDimensionEnumeratedRepresentation(GetRelatedResourcesResult result) {
        if (searchConceptSchemeForEnumeratedRepresentationWindow != null) {
            searchConceptSchemeForEnumeratedRepresentationWindow.setRelatedResources(result.getRelatedResourceDtos());
            searchConceptSchemeForEnumeratedRepresentationWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getRelatedResourceDtos().size(), result.getTotalResults());
        }
    }

    @Override
    public void setConceptsAsRole(List<RelatedResourceDto> concepts, int firstResult, int totalResults) {
        if (searchConceptsForRolesWindow != null) {
            searchConceptsForRolesWindow.setSourceRelatedResources(concepts);
            searchConceptsForRolesWindow.refreshSourcePaginationInfo(firstResult, concepts.size(), totalResults);
        }
    }

    @Override
    public void setDefaultConceptSchemeEnumeratedRepresentation(RelatedResourceDto conceptScheme) {
        // This is only call when we are creating a measure dimension
        setEnumeratedRepresentationTypeInEditionForm();
        setConceptSchemeEnumerationRepresentationInEditionForm(conceptScheme);
        editionForm.markForRedraw();
        editionForm.validate();
    }

    @Override
    public DimensionComponentDto getDsdDimension() {
        // Id
        dimensionComponentDto.setCode(editionForm.getItem(DimensionDS.CODE).getVisible() ? editionForm.getValueAsString(DimensionDS.CODE) : null);

        // Type
        dimensionComponentDto.setTypeDimensionComponent(getDimensionTypeFromEditionForm());
        dimensionComponentDto.setSpecialDimensionType(CommonUtils.getSpecialDimensionType(editionForm.getValueAsString(DimensionDS.TYPE)));

        // Concept
        dimensionComponentDto.setCptIdRef(((SearchRelatedResourceLinkItem) editionForm.getItem(DimensionDS.CONCEPT)).getRelatedResourceDto());

        // Role
        dimensionComponentDto.getRole().clear();
        if (!TypeDimensionComponent.TIMEDIMENSION.equals(dimensionComponentDto.getTypeDimensionComponent())) {
            List<RelatedResourceDto> selectedRoles = ((RelatedResourceListItem) editionForm.getItem(DimensionDS.ROLE)).getSelectedRelatedResources();
            dimensionComponentDto.getRole().addAll(selectedRoles);
        }

        // Representation
        if (!StringUtils.isBlank(editionForm.getValueAsString(DimensionDS.REPRESENTATION_TYPE))) {
            RepresentationTypeEnum representationType = RepresentationTypeEnum.valueOf(editionForm.getValueAsString(DimensionDS.REPRESENTATION_TYPE));

            if (dimensionComponentDto.getLocalRepresentation() == null) {
                dimensionComponentDto.setLocalRepresentation(new RepresentationDto());
            }

            if (RepresentationTypeEnum.ENUMERATION.equals(representationType)) {

                dimensionComponentDto.getLocalRepresentation().setRepresentationType(RepresentationTypeEnum.ENUMERATION);

                if (TypeDimensionComponent.MEASUREDIMENSION.equals(dimensionComponentDto.getTypeDimensionComponent())) {
                    dimensionComponentDto.getLocalRepresentation().setEnumeration(getConceptSchemeEnumeratedRepresentationFromEditionForm());
                } else {
                    dimensionComponentDto.getLocalRepresentation().setEnumeration(
                            ((SearchRelatedResourceLinkItem) editionForm.getItem(DimensionDS.ENUMERATED_REPRESENTATION_CODELIST)).getRelatedResourceDto());
                }

                dimensionComponentDto.getLocalRepresentation().setTextFormat(null);

            } else if (RepresentationTypeEnum.TEXT_FORMAT.equals(representationType)) {

                // Facet

                dimensionComponentDto.getLocalRepresentation().setRepresentationType(RepresentationTypeEnum.TEXT_FORMAT);
                FacetDto facetDto = facetEditionForm.getFacet();
                dimensionComponentDto.getLocalRepresentation().setTextFormat(facetDto);
                dimensionComponentDto.getLocalRepresentation().setEnumeration(null);
            }
        } else {
            // No representation
            dimensionComponentDto.setLocalRepresentation(null);
        }

        if (dimensionComponentDto.getId() == null) {
            dimensionComponentDto.setTypeComponent(TypeComponent.DIMENSION_COMPONENT);
        }

        // Annotations
        dimensionComponentDto.getAnnotations().clear();
        dimensionComponentDto.getAnnotations().addAll(editionAnnotationsPanel.getAnnotations());

        return dimensionComponentDto;
    }

    private TypeDimensionComponent getDimensionTypeFromEditionForm() {
        return CommonUtils.getTypeDimensionComponent(editionForm.getValueAsString(DimensionDS.TYPE));
    }

    private RelatedResourceDto getConceptSchemeEnumeratedRepresentationFromEditionForm() {
        return ((SearchRelatedResourceLinkItem) editionForm.getItem(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME)).getRelatedResourceDto();
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

    private void setDimension(DimensionComponentDto dimensionComponentDto) {
        setDimensionViewMode(dimensionComponentDto);
        setDimensionEditionMode(dimensionComponentDto);
    }

    private void setDimensionViewMode(DimensionComponentDto dimensionComponentDto) {
        this.dimensionComponentDto = dimensionComponentDto;
        // Id
        form.setValue(DimensionDS.CODE_VIEW, dimensionComponentDto.getCode());
        // URNs
        form.setValue(DimensionDS.URN, dimensionComponentDto.getUrn());
        form.setValue(DimensionDS.URN_PROVIDER, dimensionComponentDto.getUrnProvider());
        // Type
        form.setValue(DimensionDS.TYPE_VIEW, CommonUtils.getDimensionTypeName(dimensionComponentDto));
        // Concept
        ((RelatedResourceLinkItem) form.getItem(DimensionDS.CONCEPT)).setRelatedResource(dimensionComponentDto.getCptIdRef());

        // Role
        form.getItem(DimensionDS.ROLE).hide();
        form.getItem(DimensionDS.ROLE).clearValue();
        if (!TypeDimensionComponent.TIMEDIMENSION.equals(dimensionComponentDto.getTypeDimensionComponent())) {
            ((RelatedResourceListItem) form.getItem(DimensionDS.ROLE)).setRelatedResources(dimensionComponentDto.getRole());
            form.getItem(DimensionDS.ROLE).show();
        }

        // Representation
        facetForm.hide();
        form.getItem(DimensionDS.ENUMERATED_REPRESENTATION_CODELIST).hide();
        form.getItem(DimensionDS.ENUMERATED_REPRESENTATION_CODELIST).clearValue();
        form.getItem(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME).hide();
        form.getItem(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME).clearValue();
        form.getItem(DimensionDS.REPRESENTATION_TYPE).clearValue();
        facetForm.clearValues();
        if (dimensionComponentDto.getLocalRepresentation() != null) {
            if (RepresentationTypeEnum.ENUMERATION.equals(dimensionComponentDto.getLocalRepresentation().getRepresentationType())) {

                form.getItem(DimensionDS.REPRESENTATION_TYPE).setValue(getCoreMessages().representationTypeEnumENUMERATION());

                if (RelatedResourceTypeEnum.CODELIST.equals(dimensionComponentDto.getLocalRepresentation().getEnumeration().getType())) {

                    // CODELIST

                    if (!TypeDimensionComponent.MEASUREDIMENSION.equals(dimensionComponentDto.getTypeDimensionComponent())) {
                        ((RelatedResourceLinkItem) form.getItem(DimensionDS.ENUMERATED_REPRESENTATION_CODELIST)).setRelatedResource(dimensionComponentDto.getLocalRepresentation().getEnumeration());
                        form.getItem(DimensionDS.ENUMERATED_REPRESENTATION_CODELIST).show();
                    }

                } else if (RelatedResourceTypeEnum.CONCEPT_SCHEME.equals(dimensionComponentDto.getLocalRepresentation().getEnumeration().getType())) {

                    // CONCEPTSCHEME

                    if (TypeDimensionComponent.MEASUREDIMENSION.equals(dimensionComponentDto.getTypeDimensionComponent())) {
                        ((RelatedResourceLinkItem) form.getItem(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME)).setRelatedResource(dimensionComponentDto.getLocalRepresentation()
                                .getEnumeration());
                        form.getItem(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME).show();
                    }
                }

            } else if (RepresentationTypeEnum.TEXT_FORMAT.equals(dimensionComponentDto.getLocalRepresentation().getRepresentationType())) {

                // TEXT FORMAT (FACET)

                form.getItem(DimensionDS.REPRESENTATION_TYPE).setValue(getCoreMessages().representationTypeEnumTEXT_FORMAT());
                FacetDto facetDto = dimensionComponentDto.getLocalRepresentation().getTextFormat();
                facetForm.setFacet(facetDto);
                facetForm.show();
            }
        }

        // Annotations
        viewAnnotationsPanel.setAnnotations(dimensionComponentDto.getAnnotations(), dataStructureDefinitionMetamacDto);

        form.markForRedraw();
    }

    private void setDimensionEditionMode(DimensionComponentDto dimensionComponentDto) {
        this.dimensionComponentDto = dimensionComponentDto;
        // Id
        editionForm.setValue(DimensionDS.CODE, dimensionComponentDto.getCode());
        editionForm.setValue(DimensionDS.CODE_VIEW, dimensionComponentDto.getCode());
        // URNs
        editionForm.setValue(DimensionDS.URN, dimensionComponentDto.getUrn());
        editionForm.setValue(DimensionDS.URN_PROVIDER, dimensionComponentDto.getUrnProvider());
        // Type
        editionForm.setValue(DimensionDS.TYPE, CommonUtils.getDimensionTypeAsString(dimensionComponentDto));
        editionForm.setValue(DimensionDS.TYPE_VIEW, CommonUtils.getDimensionTypeName(dimensionComponentDto));

        // Concept
        ((SearchRelatedResourceLinkItem) editionForm.getItem(DimensionDS.CONCEPT)).setRelatedResource(dimensionComponentDto.getCptIdRef());
        ((RelatedResourceLinkItem) editionForm.getItem(DimensionDS.CONCEPT_VIEW)).setRelatedResource(dimensionComponentDto.getCptIdRef());

        // Role
        ((RelatedResourceListItem) editionForm.getItem(DimensionDS.ROLE)).setRelatedResources(dimensionComponentDto.getRole());
        ((RelatedResourceListItem) editionForm.getItem(DimensionDS.ROLE_VIEW)).setRelatedResources(dimensionComponentDto.getRole());

        // Representation
        editionForm.getItem(DimensionDS.ENUMERATED_REPRESENTATION_CODELIST).clearValue();
        editionForm.getItem(DimensionDS.ENUMERATED_REPRESENTATION_CODELIST_VIEW).clearValue();
        editionForm.getItem(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME).clearValue();
        editionForm.getItem(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME_VIEW).clearValue();
        editionForm.getItem(DimensionDS.REPRESENTATION_TYPE).clearValue();
        editionForm.getItem(DimensionDS.REPRESENTATION_TYPE_VIEW).clearValue();
        facetEditionForm.clearValues();
        facetStaticEditionForm.clearValues();
        if (dimensionComponentDto.getLocalRepresentation() != null) {

            if (RepresentationTypeEnum.ENUMERATION.equals(dimensionComponentDto.getLocalRepresentation().getRepresentationType())) {

                // ENUMERATED REPRESENTATION

                setEnumeratedRepresentationTypeInEditionForm();

                if (RelatedResourceTypeEnum.CODELIST.equals(dimensionComponentDto.getLocalRepresentation().getEnumeration().getType())) {

                    // Codelist

                    if (!TypeDimensionComponent.MEASUREDIMENSION.equals(dimensionComponentDto.getTypeDimensionComponent())) {
                        ((SearchRelatedResourceLinkItem) editionForm.getItem(DimensionDS.ENUMERATED_REPRESENTATION_CODELIST)).setRelatedResource(dimensionComponentDto.getLocalRepresentation()
                                .getEnumeration());
                        ((RelatedResourceLinkItem) editionForm.getItem(DimensionDS.ENUMERATED_REPRESENTATION_CODELIST_VIEW)).setRelatedResource(dimensionComponentDto.getLocalRepresentation()
                                .getEnumeration());
                    }

                } else if (RelatedResourceTypeEnum.CONCEPT_SCHEME.equals(dimensionComponentDto.getLocalRepresentation().getEnumeration().getType())) {

                    // ConceptScheme

                    if (TypeDimensionComponent.MEASUREDIMENSION.equals(dimensionComponentDto.getTypeDimensionComponent())) {

                        setConceptSchemeEnumerationRepresentationInEditionForm(dimensionComponentDto.getLocalRepresentation().getEnumeration());
                    }
                }

            } else if (RepresentationTypeEnum.TEXT_FORMAT.equals(dimensionComponentDto.getLocalRepresentation().getRepresentationType())) {

                // TEXT FORMAT (FACET)

                editionForm.setValue(DimensionDS.REPRESENTATION_TYPE, RepresentationTypeEnum.TEXT_FORMAT.toString());
                editionForm.setValue(DimensionDS.REPRESENTATION_TYPE_VIEW, getCoreMessages().representationTypeEnumTEXT_FORMAT());

                FacetDto facetDto = dimensionComponentDto.getLocalRepresentation().getTextFormat();
                facetEditionForm.setFacet(facetDto);
                facetStaticEditionForm.setFacet(facetDto);
            }
        }
        FacetFormUtils.setFacetFormVisibility(facetEditionForm, facetStaticEditionForm, editionForm.getValueAsString(DimensionDS.REPRESENTATION_TYPE), dataStructureDefinitionMetamacDto);
        editionForm.markForRedraw();

        // Annotations
        editionAnnotationsPanel.setAnnotations(dimensionComponentDto.getAnnotations(), dataStructureDefinitionMetamacDto);
    }

    private void setEnumeratedRepresentationTypeInEditionForm() {
        editionForm.setValue(DimensionDS.REPRESENTATION_TYPE, RepresentationTypeEnum.ENUMERATION.toString());
        editionForm.setValue(DimensionDS.REPRESENTATION_TYPE_VIEW, getCoreMessages().representationTypeEnumENUMERATION());
    }

    private void setConceptSchemeEnumerationRepresentationInEditionForm(RelatedResourceDto relatedResourceDto) {
        ((SearchRelatedResourceLinkItem) editionForm.getItem(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME)).setRelatedResource(relatedResourceDto);
        ((RelatedResourceLinkItem) editionForm.getItem(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME_VIEW)).setRelatedResource(relatedResourceDto);
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
    public void onDimensionSaved(DimensionComponentDto dimensionComponentDto) {
        this.dimensionComponentDto = dimensionComponentDto;
        dimensionsGrid.removeSelectedData();
        DimensionRecord record = DsdRecordUtils.getDimensionRecord(dimensionComponentDto);
        dimensionsGrid.addData(record);
        dimensionsGrid.selectRecord(record);
        mainFormLayout.setViewMode();
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
                        getUiHandlers().createDefaultDimension(dataStructureDefinitionMetamacDto.getUrn(), newDimensionWindow.getDimensionType(), newDimensionWindow.getSpecialDimensionType());
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
        facetEditionForm.clearErrors(true);
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
        if (DsdClientSecurityUtils.canDeleteDimension(dataStructureDefinitionMetamacDto)) {
            deleteToolStripButton.show();
        }
    }

    private CustomSelectItem createRepresentationTypeItem(String name, String title) {
        final CustomSelectItem representationTypeItem = new CustomSelectItem(name, title);
        representationTypeItem.setValueMap(org.siemac.metamac.srm.web.client.utils.CommonUtils.getTypeRepresentationEnumHashMap());
        representationTypeItem.setRedrawOnChange(true);
        representationTypeItem.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                // Show FacetForm if RepresentationTypeEnum = NON_NUMERATED
                FacetFormUtils.setFacetFormVisibility(facetEditionForm, facetStaticEditionForm, representationTypeItem.getValueAsString(), dataStructureDefinitionMetamacDto);
            }
        });

        // Measure dimension validator

        CustomValidator measureCustomValidator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                // Measure dimensions must be enumerated
                if (CommonUtils.isDimensionTypeMeasureDimension(editionForm.getValueAsString(DimensionDS.TYPE))) {
                    return RepresentationTypeEnum.ENUMERATION.toString().equals(value);
                }
                return true;
            }
        };
        measureCustomValidator.setErrorMessage(getMessages().errorRequiredEnumeratedRepresentationInMeasureDimension());

        // Time dimension validator

        CustomValidator timeCustomValidator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                // Time dimensions must have a non enumerated representation
                if (CommonUtils.isDimensionTypeTimeDimension(editionForm.getValueAsString(DimensionDS.TYPE))) {
                    return RepresentationTypeEnum.TEXT_FORMAT.toString().equals(value);
                }
                return true;
            }
        };
        timeCustomValidator.setErrorMessage(getMessages().errorRequiredNonEnumeratedRepresentationInTimeDimension());

        representationTypeItem.setValidators(measureCustomValidator, timeCustomValidator);
        return representationTypeItem;
    }

    private RelatedResourceListItem createRoleItem(String name, String title) {
        final int FIRST_RESULT = 0;
        final int MAX_RESULTS = 8;

        RelatedResourceListItem relatedResources = new RelatedResourceListItem(name, title, true, getListRecordNavigationClickHandler());
        relatedResources.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                SelectItem conceptSchemeSelectItem = new SelectItem(ConceptSchemeDS.URN, getConstants().conceptScheme());
                searchConceptsForRolesWindow = new SearchMultipleRelatedResourcePaginatedWindow(getConstants().dsdDimensionsRole(), MAX_RESULTS, conceptSchemeSelectItem, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        getUiHandlers().retrieveConceptsForDimensionRole(firstResult, maxResults, searchConceptsForRolesWindow.getRelatedResourceCriteria(),
                                searchConceptsForRolesWindow.getInitialSelectionValue());

                    }
                });

                // Load concept schemes and concepts (to populate the selection window)
                getUiHandlers().retrieveConceptSchemesForDimensionRole(FIRST_RESULT, SrmWebConstants.NO_LIMIT_IN_PAGINATION);
                getUiHandlers().retrieveConceptsForDimensionRole(FIRST_RESULT, MAX_RESULTS, null, null);

                // Set the selected concepts
                List<RelatedResourceDto> selectedConcepts = ((RelatedResourceListItem) editionForm.getItem(DimensionDS.ROLE)).getRelatedResourceDtos();
                searchConceptsForRolesWindow.setTargetRelatedResources(selectedConcepts);

                searchConceptsForRolesWindow.getInitialSelectionItem().addChangedHandler(new ChangedHandler() {

                    @Override
                    public void onChanged(ChangedEvent event) {
                        getUiHandlers().retrieveConceptsForDimensionRole(FIRST_RESULT, MAX_RESULTS, searchConceptsForRolesWindow.getRelatedResourceCriteria(),
                                searchConceptsForRolesWindow.getInitialSelectionValue());
                    }
                });

                searchConceptsForRolesWindow.setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                        getUiHandlers().retrieveConceptsForDimensionRole(firstResult, maxResults, criteria, searchConceptsForRolesWindow.getInitialSelectionValue());
                    }
                });
                searchConceptsForRolesWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        List<RelatedResourceDto> selectedRelatedResources = searchConceptsForRolesWindow.getSelectedRelatedResources();
                        ((RelatedResourceListItem) editionForm.getItem(DimensionDS.ROLE)).setRelatedResources(selectedRelatedResources);
                        searchConceptsForRolesWindow.markForDestroy();
                    }
                });
            }
        });
        return relatedResources;
    }

    private SearchRelatedResourceLinkItem createConceptItem(String name, String title) {
        final int FIRST_RESULT = 0;
        final int MAX_RESULTS = 8;
        final SearchRelatedResourceLinkItem conceptItem = new SearchRelatedResourceLinkItem(name, title, getCustomLinkItemNavigationClickHandler());
        conceptItem.setRequired(true);
        conceptItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                final TypeDimensionComponent dimensionType = dimensionComponentDto.getTypeDimensionComponent();

                SelectItem conceptSchemeSelectItem = new SelectItem(ConceptSchemeDS.URN, getConstants().conceptScheme());
                searchConceptWindow = new SearchRelatedResourcePaginatedWindow(getConstants().conceptSelection(), MAX_RESULTS, conceptSchemeSelectItem, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        retrieveConcepts(dimensionType, firstResult, maxResults, searchConceptWindow.getRelatedResourceCriteria(), searchConceptWindow.getInitialSelectionValue(),
                                searchConceptWindow.getIsLastVersionValue());
                    }
                });

                searchConceptWindow.showIsLastVersionItem();
                searchConceptWindow.getIsLastVersionItem().addChangedHandler(new ChangedHandler() {

                    @Override
                    public void onChanged(ChangedEvent event) {
                        retrieveConceptSchemes(dimensionType, FIRST_RESULT, SrmWebConstants.NO_LIMIT_IN_PAGINATION, searchConceptWindow.getIsLastVersionValue());
                        retrieveConcepts(dimensionType, FIRST_RESULT, MAX_RESULTS, searchConceptWindow.getRelatedResourceCriteria(), searchConceptWindow.getInitialSelectionValue(),
                                searchConceptWindow.getIsLastVersionValue());
                    }
                });

                // Load concept schemes and concepts (to populate the selection window)
                retrieveConceptSchemes(dimensionType, FIRST_RESULT, SrmWebConstants.NO_LIMIT_IN_PAGINATION, searchConceptWindow.getIsLastVersionValue());
                retrieveConcepts(dimensionType, FIRST_RESULT, MAX_RESULTS, null, null, searchConceptWindow.getIsLastVersionValue());

                searchConceptWindow.getInitialSelectionItem().addChangedHandler(new ChangedHandler() {

                    @Override
                    public void onChanged(ChangedEvent event) {
                        retrieveConcepts(dimensionType, FIRST_RESULT, MAX_RESULTS, searchConceptWindow.getRelatedResourceCriteria(), searchConceptWindow.getInitialSelectionValue(),
                                searchConceptWindow.getIsLastVersionValue());
                    }
                });

                searchConceptWindow.getListGridItem().getListGrid().setSelectionType(SelectionStyle.SINGLE);
                searchConceptWindow.getListGridItem().setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String concept) {
                        retrieveConcepts(dimensionType, firstResult, maxResults, concept, searchConceptWindow.getInitialSelectionValue(), searchConceptWindow.getIsLastVersionValue());
                    }
                });

                searchConceptWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        RelatedResourceDto selectedConcept = searchConceptWindow.getSelectedRelatedResource();
                        searchConceptWindow.markForDestroy();
                        // Set selected concepts in form
                        ((SearchRelatedResourceLinkItem) editionForm.getItem(DimensionDS.CONCEPT)).setRelatedResource(selectedConcept);

                        TypeDimensionComponent dimensionType = getDimensionTypeFromEditionForm();
                        if (TypeDimensionComponent.MEASUREDIMENSION.equals(dimensionType)) {
                            // If the dimension is a measure dimension, the concept select may have an enumerated representation (with a conceptScheme). If so, set this conceptScheme as the default
                            // enumerated representation of the dimension (only if the enumerated representation of the dimension was empty)

                            if (selectedConcept != null) {
                                RelatedResourceDto conceptSchemeEnumeratedRepresentation = getConceptSchemeEnumeratedRepresentationFromEditionForm();
                                if (conceptSchemeEnumeratedRepresentation == null) {
                                    getUiHandlers().retrieveConceptSchemeEnumeratedRepresentationFromConcept(selectedConcept.getUrn());
                                }
                            }
                        } else {
                            // When a concept is selected, reset the value of the codelist (the codelist depends on the concept)
                            ((SearchRelatedResourceLinkItem) editionForm.getItem(DimensionDS.ENUMERATED_REPRESENTATION_CODELIST)).clearRelatedResource();
                        }

                        editionForm.markForRedraw();
                        editionForm.validate(false);
                    }
                });
            }
        });
        return conceptItem;
    }

    private void retrieveConceptSchemes(TypeDimensionComponent dimensionType, int firstResult, int maxResults, boolean isLastVersion) {
        ConceptSchemeWebCriteria conceptSchemeWebCriteria = new ConceptSchemeWebCriteria();
        conceptSchemeWebCriteria.setDsdUrn(dataStructureDefinitionMetamacDto.getUrn());
        conceptSchemeWebCriteria.setIsLastVersion(isLastVersion);

        getUiHandlers().retrieveConceptSchemes(dimensionType, firstResult, maxResults, conceptSchemeWebCriteria);
    }

    private void retrieveConcepts(TypeDimensionComponent dimensionType, int firstResult, int maxResults, String criteria, String conceptSchemeUrn, boolean isLastVersion) {
        ConceptWebCriteria conceptWebCriteria = new ConceptWebCriteria();
        conceptWebCriteria.setCriteria(criteria);
        conceptWebCriteria.setDsdUrn(dataStructureDefinitionMetamacDto.getUrn());
        conceptWebCriteria.setItemSchemeUrn(conceptSchemeUrn);
        conceptWebCriteria.setIsLastVersion(isLastVersion);

        getUiHandlers().retrieveConcepts(dimensionType, firstResult, maxResults, conceptWebCriteria);
    }

    private SearchRelatedResourceLinkItem createMeasureDimensionEnumeratedRepresentationItem(String name, String title) {
        final SearchRelatedResourceLinkItem conceptSchemeItem = new SearchRelatedResourceLinkItem(name, title, getCustomLinkItemNavigationClickHandler());
        conceptSchemeItem.setRequired(true);
        // Info icon
        FormItemIcon measureDimensionInfo = new FormItemIcon();
        measureDimensionInfo.setSrc(org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.info().getURL());
        measureDimensionInfo.setPrompt(getConstants().dsdMeasureDimensionRepresentation());

        conceptSchemeItem.setIconVAlign(VerticalAlignment.TOP);
        conceptSchemeItem.setIconWidth(14);
        conceptSchemeItem.setIconHeight(14);
        conceptSchemeItem.setIcons(conceptSchemeItem.getSearchIcon(), conceptSchemeItem.getClearIcon(), measureDimensionInfo);

        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;
        conceptSchemeItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                searchConceptSchemeForEnumeratedRepresentationWindow = new SearchRelatedResourcePaginatedWindow(getConstants().conceptSchemeSelection(), MAX_RESULTS, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        retrieveConceptSchemesForMeasureDimensionEnumeratedRepresentation(firstResult, maxResults, searchConceptSchemeForEnumeratedRepresentationWindow.getRelatedResourceCriteria(),
                                searchConceptSchemeForEnumeratedRepresentationWindow.getIsLastVersionValue());
                    }
                });

                searchConceptSchemeForEnumeratedRepresentationWindow.showIsLastVersionItem();
                searchConceptSchemeForEnumeratedRepresentationWindow.getIsLastVersionItem().addChangedHandler(new ChangedHandler() {

                    @Override
                    public void onChanged(ChangedEvent event) {
                        retrieveConceptSchemesForMeasureDimensionEnumeratedRepresentation(FIRST_RESULST, MAX_RESULTS,
                                searchConceptSchemeForEnumeratedRepresentationWindow.getRelatedResourceCriteria(), searchConceptSchemeForEnumeratedRepresentationWindow.getIsLastVersionValue());
                    }
                });

                // Load concept schemes (to populate the selection window)
                retrieveConceptSchemesForMeasureDimensionEnumeratedRepresentation(FIRST_RESULST, MAX_RESULTS, null, searchConceptSchemeForEnumeratedRepresentationWindow.getIsLastVersionValue());

                searchConceptSchemeForEnumeratedRepresentationWindow.getListGridItem().getListGrid().setSelectionType(SelectionStyle.SINGLE);
                searchConceptSchemeForEnumeratedRepresentationWindow.getListGridItem().setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                        retrieveConceptSchemesForMeasureDimensionEnumeratedRepresentation(firstResult, maxResults, criteria,
                                searchConceptSchemeForEnumeratedRepresentationWindow.getIsLastVersionValue());
                    }
                });

                searchConceptSchemeForEnumeratedRepresentationWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        RelatedResourceDto selectedConceptScheme = searchConceptSchemeForEnumeratedRepresentationWindow.getSelectedRelatedResource();
                        searchConceptSchemeForEnumeratedRepresentationWindow.markForDestroy();
                        // Set selected concept scheme in form
                        ((SearchRelatedResourceLinkItem) editionForm.getItem(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME)).setRelatedResource(selectedConceptScheme);
                        editionForm.validate(false);
                    }
                });
            }
        });
        return conceptSchemeItem;
    }

    private void retrieveConceptSchemesForMeasureDimensionEnumeratedRepresentation(int firstResult, int maxResults, String criteria, boolean isLastVersion) {
        ConceptSchemeWebCriteria conceptSchemeWebCriteria = new ConceptSchemeWebCriteria();
        conceptSchemeWebCriteria.setCriteria(criteria);
        conceptSchemeWebCriteria.setDsdUrn(dataStructureDefinitionMetamacDto.getUrn());
        conceptSchemeWebCriteria.setIsLastVersion(isLastVersion);

        getUiHandlers().retrieveConceptSchemesForMeasureDimensionEnumeratedRepresentation(firstResult, maxResults, conceptSchemeWebCriteria);
    }

    private SearchRelatedResourceLinkItem createEnumeratedRepresentationItem(String name, String title) {
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;
        final SearchRelatedResourceLinkItem codelistItem = new SearchRelatedResourceLinkItem(name, title, getCustomLinkItemNavigationClickHandler());
        codelistItem.setRequired(true);
        codelistItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {

                RelatedResourceDto concept = ((SearchRelatedResourceLinkItem) editionForm.getItem(DimensionDS.CONCEPT)).getRelatedResourceDto();
                final String conceptUrn = concept != null ? concept.getUrn() : null;

                if (StringUtils.isBlank(conceptUrn)) {
                    // If a concept has not been selected, show a message and do not let the user to select a codelist
                    InformationWindow conceptRequiredWindow = new InformationWindow(getConstants().codelistSelection(), getConstants().dsdDimensionCodelistSelectionConceptRequired());
                    conceptRequiredWindow.show();
                } else {

                    final boolean isSpatialDimension = CommonUtils.isDimensionTypeSpatialDimension(editionForm.getValueAsString(DimensionDS.TYPE));

                    searchCodelistForEnumeratedRepresentationWindow = new SearchRelatedResourcePaginatedWindow(getConstants().codelistSelection(), MAX_RESULTS, new PaginatedAction() {

                        @Override
                        public void retrieveResultSet(int firstResult, int maxResults) {
                            getUiHandlers().retrieveCodelistsForEnumeratedRepresentation(firstResult, maxResults, searchCodelistForEnumeratedRepresentationWindow.getRelatedResourceCriteria(),
                                    conceptUrn, isSpatialDimension);
                        }
                    });
                    searchCodelistForEnumeratedRepresentationWindow.setInfoMessage(getConstants().dsdDimensionEnumeratedRepresentationInfoMessage());

                    // Load codelists (to populate the selection window)
                    getUiHandlers().retrieveCodelistsForEnumeratedRepresentation(FIRST_RESULST, MAX_RESULTS, null, conceptUrn, isSpatialDimension);

                    searchCodelistForEnumeratedRepresentationWindow.getListGridItem().getListGrid().setSelectionType(SelectionStyle.SINGLE);
                    searchCodelistForEnumeratedRepresentationWindow.getListGridItem().setSearchAction(new SearchPaginatedAction() {

                        @Override
                        public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                            getUiHandlers().retrieveCodelistsForEnumeratedRepresentation(firstResult, maxResults, criteria, conceptUrn, isSpatialDimension);
                        }
                    });

                    searchCodelistForEnumeratedRepresentationWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                        @Override
                        public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                            RelatedResourceDto selectedCodelist = searchCodelistForEnumeratedRepresentationWindow.getSelectedRelatedResource();
                            searchCodelistForEnumeratedRepresentationWindow.markForDestroy();
                            // Set selected codelist in form
                            ((SearchRelatedResourceLinkItem) editionForm.getItem(DimensionDS.ENUMERATED_REPRESENTATION_CODELIST)).setRelatedResource(selectedCodelist);
                            editionForm.validate(false);
                        }
                    });
                }
            }
        });
        return codelistItem;
    }

    // ------------------------------------------------------------------------------------------------------------
    // FORM ITEM IF FUNCTIONS
    // ------------------------------------------------------------------------------------------------------------

    // CODE

    private FormItemIfFunction getCodeFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return DsdsFormUtils.canDimensionCodeBeEdited(dataStructureDefinitionMetamacDto, editionForm.getValueAsString(DimensionDS.TYPE));
            }
        };
    }

    private FormItemIfFunction getStaticCodeFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !DsdsFormUtils.canDimensionCodeBeEdited(dataStructureDefinitionMetamacDto, editionForm.getValueAsString(DimensionDS.TYPE));
            }
        };
    }

    // CONCEPT

    private FormItemIfFunction getConceptFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return DsdsFormUtils.canDimensionConceptBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }

    private FormItemIfFunction getStaticConceptFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !DsdsFormUtils.canDimensionConceptBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }

    // ROLE

    private FormItemIfFunction getRoleFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return CommonUtils.isDimensionRoleVisible(dimensionComponentDto.getTypeDimensionComponent()) && DsdsFormUtils.canDimensionRoleBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }

    private FormItemIfFunction getStaticRoleFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return CommonUtils.isDimensionRoleVisible(dimensionComponentDto.getTypeDimensionComponent()) && !DsdsFormUtils.canDimensionRoleBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }

    // REPRESENTATION TYPE

    private FormItemIfFunction getRepresentationTypeFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return DsdsFormUtils.canDimensionRepresentationTypeBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }

    private FormItemIfFunction getStaticRepresentationTypeFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !DsdsFormUtils.canDimensionRepresentationTypeBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }

    // CODELIST (ENUMERATED REPRESENTATION)

    private FormItemIfFunction getCodelistEnumeratedRepresentationFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return CommonUtils.isDimensionCodelistEnumeratedRepresentationVisible(editionForm.getValueAsString(DimensionDS.REPRESENTATION_TYPE), editionForm.getValueAsString(DimensionDS.TYPE))
                        && DsdsFormUtils.canDimensionCodelistEnumeratedRepresentationBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }

    private FormItemIfFunction getStaticCodelistEnumeratedRepresentationFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return CommonUtils.isDimensionCodelistEnumeratedRepresentationVisible(editionForm.getValueAsString(DimensionDS.REPRESENTATION_TYPE), editionForm.getValueAsString(DimensionDS.TYPE))
                        && !DsdsFormUtils.canDimensionCodelistEnumeratedRepresentationBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }

    // CONCEPT SCHEME (ENUMERATED REPRESENTATION)

    private FormItemIfFunction getConceptSchemeEnumeratedRepresentationFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return CommonUtils.isDimensionConceptSchemeEnumeratedRepresentationVisible(editionForm.getValueAsString(DimensionDS.REPRESENTATION_TYPE),
                        editionForm.getValueAsString(DimensionDS.TYPE))
                        && DsdsFormUtils.canDimensionConceptSchemeEnumeratedRepresentationBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }

    private FormItemIfFunction getStaticConceptSchemeEnumeratedRepresentationFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return CommonUtils.isDimensionConceptSchemeEnumeratedRepresentationVisible(editionForm.getValueAsString(DimensionDS.REPRESENTATION_TYPE),
                        editionForm.getValueAsString(DimensionDS.TYPE))
                        && !DsdsFormUtils.canDimensionConceptSchemeEnumeratedRepresentationBeEdited(dataStructureDefinitionMetamacDto);
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
