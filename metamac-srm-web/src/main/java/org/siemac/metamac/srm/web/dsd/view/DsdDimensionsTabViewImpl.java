package org.siemac.metamac.srm.web.dsd.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
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
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.InformationWindow;
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
import com.smartgwt.client.widgets.form.validator.CustomValidator;
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

public class DsdDimensionsTabViewImpl extends ViewWithUiHandlers<DsdDimensionsTabUiHandlers> implements DsdDimensionsTabPresenter.DsdDimensionsTabView {

    private DataStructureDefinitionMetamacDto            dataStructureDefinitionMetamacDto;

    private DimensionComponentDto                        dimensionComponentDto;

    private VLayout                                      panel;
    private VLayout                                      selectedComponentLayout;
    private ListGrid                                     dimensionsGrid;
    private InternationalMainFormLayout                  mainFormLayout;

    private AnnotationsPanel                             viewAnnotationsPanel;
    private AnnotationsPanel                             editionAnnotationsPanel;

    // VIEW FORM

    private GroupDynamicForm                             form;
    // Representation
    private ViewTextItem                                 staticRepresentationTypeItem;
    private StaticFacetForm                              staticFacetForm;

    // EDITION FORM

    private GroupDynamicForm                             editionForm;
    // Representation
    private CustomSelectItem                             representationTypeItem;
    private DsdFacetForm                                 facetForm;

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
        ViewTextItem code = new ViewTextItem(DimensionDS.CODE_VIEW, MetamacSrmWeb.getConstants().dsdDimensionsId());
        ViewTextItem dimensionType = new ViewTextItem(DimensionDS.TYPE_VIEW, MetamacSrmWeb.getConstants().dsdDimensionsType());
        ViewTextItem concept = new ViewTextItem(DimensionDS.CONCEPT_VIEW, MetamacSrmWeb.getConstants().concept());

        RelatedResourceListItem conceptRoleItem = new RelatedResourceListItem(DimensionDS.ROLE, MetamacSrmWeb.getConstants().dsdDimensionsRole(), false);
        conceptRoleItem.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return CommonUtils.isDimensionRoleVisible(dimensionComponentDto.getTypeDimensionComponent());
            }
        });

        staticRepresentationTypeItem = new ViewTextItem(DimensionDS.REPRESENTATION_TYPE, MetamacSrmWeb.getConstants().representation());
        ViewTextItem codelist = new ViewTextItem(DimensionDS.ENUMERATED_REPRESENTATION_CODELIST_VIEW, getConstants().codelist());
        ViewTextItem conceptScheme = new ViewTextItem(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME_VIEW, MetamacSrmWeb.getConstants().conceptScheme());

        ViewTextItem urn = new ViewTextItem(DimensionDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(DimensionDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());

        form.setFields(code, dimensionType, concept, conceptRoleItem, staticRepresentationTypeItem, codelist, conceptScheme, urn, urnProvider);

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

        RequiredTextItem code = new RequiredTextItem(DimensionDS.CODE, getConstants().dsdDimensionsId());
        code.setRedrawOnChange(true);
        code.setValidators(SemanticIdentifiersUtils.getDimensionIdentifierCustomValidator());
        code.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return CommonUtils.canDimensionCodeBeEdited(editionForm.getValueAsString(DimensionDS.TYPE));
            }
        });

        ViewTextItem staticCodeEdit = new ViewTextItem(DimensionDS.CODE_VIEW, getConstants().dsdDimensionsId());
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

        RelatedResourceListItem conceptRoleItem = createRoleItem(DimensionDS.ROLE, getConstants().dsdDimensionsRole());

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
        ViewTextItem codelist = new ViewTextItem(DimensionDS.ENUMERATED_REPRESENTATION_CODELIST, MetamacSrmWeb.getConstants().codelist());
        codelist.setShowIfCondition(FormItemUtils.getFalseFormItemIfFunction());
        SearchViewTextItem codelistView = createEnumeratedRepresentationItem(DimensionDS.ENUMERATED_REPRESENTATION_CODELIST_VIEW, MetamacSrmWeb.getConstants().codelist());

        // ConceptScheme

        ViewTextItem conceptScheme = new ViewTextItem(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME, MetamacSrmWeb.getConstants().conceptScheme());
        conceptScheme.setShowIfCondition(FormItemUtils.getFalseFormItemIfFunction());
        SearchViewTextItem conceptSchemeView = createMeasureDimensionEnumeratedRepresentationItem(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME_VIEW, MetamacSrmWeb.getConstants()
                .conceptScheme());

        ViewTextItem urn = new ViewTextItem(DimensionDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(DimensionDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());

        editionForm.setFields(code, staticCodeEdit, dimensionType, dimensionTypeView, concept, conceptView, conceptRoleItem, representationTypeItem, codelist, codelistView, conceptScheme,
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
    public void setDsdDimensions(DataStructureDefinitionMetamacDto dsd, List<DimensionComponentDto> dimensionComponentDtos) {
        this.dataStructureDefinitionMetamacDto = dsd;
        deselectDimension();

        // Security
        ProcStatusEnum procStatus = dsd.getLifeCycle().getProcStatus();
        String operationCode = CommonUtils.getStatisticalOperationCodeFromDsd(dsd);
        newToolStripButton.setVisibility(DsdClientSecurityUtils.canUpdateDimensions(procStatus, operationCode) ? Visibility.VISIBLE : Visibility.HIDDEN);
        mainFormLayout.setCanEdit(DsdClientSecurityUtils.canUpdateDimensions(procStatus, operationCode));

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
    public DimensionComponentDto getDsdDimension() {
        // Id
        dimensionComponentDto.setCode(editionForm.getItem(DimensionDS.CODE).getVisible() ? editionForm.getValueAsString(DimensionDS.CODE) : null);

        // Type
        dimensionComponentDto.setTypeDimensionComponent(CommonUtils.getTypeDimensionComponent(editionForm.getValueAsString(DimensionDS.TYPE)));
        dimensionComponentDto.setSpecialDimensionType(CommonUtils.getSpecialDimensionType(editionForm.getValueAsString(DimensionDS.TYPE)));

        // Concept
        dimensionComponentDto.setCptIdRef(StringUtils.isBlank(editionForm.getValueAsString(DimensionDS.CONCEPT)) ? null : RelatedResourceUtils.createRelatedResourceDto(
                TypeExternalArtefactsEnum.CONCEPT, editionForm.getValueAsString(DimensionDS.CONCEPT)));

        // Role
        dimensionComponentDto.getRole().clear();
        if (!TypeDimensionComponent.TIMEDIMENSION.equals(dimensionComponentDto.getTypeDimensionComponent())) {
            List<RelatedResourceDto> selectedRoles = ((RelatedResourceListItem) editionForm.getItem(DimensionDS.ROLE)).getSelectedRelatedResources();
            dimensionComponentDto.getRole().addAll(selectedRoles);
        }

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
                    dimensionComponentDto.getLocalRepresentation().setEnumerated(
                            StringUtils.isBlank(editionForm.getValueAsString(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME)) ? null : RelatedResourceUtils.createRelatedResourceDto(
                                    TypeExternalArtefactsEnum.CONCEPT_SCHEME, editionForm.getValueAsString(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME)));
                } else {
                    dimensionComponentDto.getLocalRepresentation().setEnumerated(
                            StringUtils.isBlank(editionForm.getValueAsString(DimensionDS.ENUMERATED_REPRESENTATION_CODELIST)) ? null : RelatedResourceUtils.createRelatedResourceDto(
                                    TypeExternalArtefactsEnum.CODELIST, editionForm.getValueAsString(DimensionDS.ENUMERATED_REPRESENTATION_CODELIST)));
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

        if (dimensionComponentDto.getId() == null) {
            dimensionComponentDto.setTypeComponent(TypeComponent.DIMENSION_COMPONENT);
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
        form.setValue(DimensionDS.CODE_VIEW, dimensionComponentDto.getCode());
        // URNs
        form.setValue(DimensionDS.URN, dimensionComponentDto.getUrn());
        form.setValue(DimensionDS.URN_PROVIDER, dimensionComponentDto.getUrnProvider());
        // Type
        form.setValue(DimensionDS.TYPE_VIEW, CommonUtils.getDimensionTypeName(dimensionComponentDto));
        // Concept
        form.setValue(DimensionDS.CONCEPT_VIEW, RelatedResourceUtils.getRelatedResourceName(dimensionComponentDto.getCptIdRef()));

        // Role
        form.getItem(DimensionDS.ROLE).hide();
        form.getItem(DimensionDS.ROLE).clearValue();
        if (!TypeDimensionComponent.TIMEDIMENSION.equals(dimensionComponentDto.getTypeDimensionComponent())) {
            ((RelatedResourceListItem) form.getItem(DimensionDS.ROLE)).setRelatedResources(dimensionComponentDto.getRole());
            form.getItem(DimensionDS.ROLE).show();
        }

        // Representation
        staticFacetForm.hide();
        form.getItem(DimensionDS.ENUMERATED_REPRESENTATION_CODELIST_VIEW).hide();
        form.getItem(DimensionDS.ENUMERATED_REPRESENTATION_CODELIST_VIEW).clearValue();
        form.getItem(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME_VIEW).hide();
        form.getItem(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME_VIEW).clearValue();
        staticRepresentationTypeItem.clearValue();
        staticFacetForm.clearValues();
        if (dimensionComponentDto.getLocalRepresentation() != null) {
            if (TypeRepresentationEnum.ENUMERATED.equals(dimensionComponentDto.getLocalRepresentation().getTypeRepresentationEnum())) {

                if (TypeExternalArtefactsEnum.CODELIST.equals(dimensionComponentDto.getLocalRepresentation().getEnumerated().getType())) {

                    // Code List
                    staticRepresentationTypeItem.setValue(MetamacSrmWeb.getCoreMessages().typeRepresentationEnumENUMERATED());
                    if (!TypeDimensionComponent.MEASUREDIMENSION.equals(dimensionComponentDto.getTypeDimensionComponent())) {
                        form.setValue(DimensionDS.ENUMERATED_REPRESENTATION_CODELIST_VIEW, RelatedResourceUtils.getRelatedResourceName(dimensionComponentDto.getLocalRepresentation().getEnumerated()));
                        form.getItem(DimensionDS.ENUMERATED_REPRESENTATION_CODELIST_VIEW).show();
                    }

                } else if (TypeExternalArtefactsEnum.CONCEPT_SCHEME.equals(dimensionComponentDto.getLocalRepresentation().getEnumerated().getType())) {

                    // ConceptScheme
                    staticRepresentationTypeItem.setValue(MetamacSrmWeb.getCoreMessages().typeRepresentationEnumENUMERATED());
                    if (TypeDimensionComponent.MEASUREDIMENSION.equals(dimensionComponentDto.getTypeDimensionComponent())) {
                        form.setValue(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME_VIEW,
                                RelatedResourceUtils.getRelatedResourceName(dimensionComponentDto.getLocalRepresentation().getEnumerated()));
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
        editionForm.setValue(DimensionDS.CODE, dimensionComponentDto.getCode());
        editionForm.setValue(DimensionDS.CODE_VIEW, dimensionComponentDto.getCode());
        // URNs
        editionForm.setValue(DimensionDS.URN, dimensionComponentDto.getUrn());
        editionForm.setValue(DimensionDS.URN_PROVIDER, dimensionComponentDto.getUrnProvider());
        // Type
        editionForm.setValue(DimensionDS.TYPE, CommonUtils.getDimensionTypeAsString(dimensionComponentDto));
        editionForm.setValue(DimensionDS.TYPE_VIEW, CommonUtils.getDimensionTypeName(dimensionComponentDto));

        // Concept
        editionForm.setValue(DimensionDS.CONCEPT, dimensionComponentDto.getCptIdRef() != null ? dimensionComponentDto.getCptIdRef().getUrn() : null);
        editionForm.setValue(DimensionDS.CONCEPT_VIEW, RelatedResourceUtils.getRelatedResourceName(dimensionComponentDto.getCptIdRef()));

        // Role
        ((RelatedResourceListItem) editionForm.getItem(DimensionDS.ROLE)).setRelatedResources(dimensionComponentDto.getRole());

        // Representation
        editionForm.getItem(DimensionDS.ENUMERATED_REPRESENTATION_CODELIST).clearValue();
        editionForm.getItem(DimensionDS.ENUMERATED_REPRESENTATION_CODELIST_VIEW).clearValue();
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
                        editionForm.setValue(DimensionDS.ENUMERATED_REPRESENTATION_CODELIST, dimensionComponentDto.getLocalRepresentation().getEnumerated().getUrn());
                        editionForm.setValue(DimensionDS.ENUMERATED_REPRESENTATION_CODELIST_VIEW,
                                RelatedResourceUtils.getRelatedResourceName(dimensionComponentDto.getLocalRepresentation().getEnumerated()));
                    }
                    // ConceptScheme
                } else if (TypeExternalArtefactsEnum.CONCEPT_SCHEME.equals(dimensionComponentDto.getLocalRepresentation().getEnumerated().getType())) {
                    if (TypeDimensionComponent.MEASUREDIMENSION.equals(dimensionComponentDto.getTypeDimensionComponent())) {
                        editionForm.setValue(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME, dimensionComponentDto.getLocalRepresentation().getEnumerated().getUrn());
                        editionForm.setValue(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME_VIEW,
                                RelatedResourceUtils.getRelatedResourceName(dimensionComponentDto.getLocalRepresentation().getEnumerated()));
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
                        dimensionSelected.setTypeDimensionComponent(newDimensionWindow.getDimensionType());
                        dimensionSelected.setSpecialDimensionType(newDimensionWindow.getSpecialDimensionType());

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
        if (DsdClientSecurityUtils.canUpdateDimensions(dataStructureDefinitionMetamacDto.getLifeCycle().getProcStatus(),
                CommonUtils.getStatisticalOperationCodeFromDsd(dataStructureDefinitionMetamacDto))) {
            deleteToolStripButton.show();
        }
    }

    private RelatedResourceListItem createRoleItem(String name, String title) {
        final int FIRST_RESULT = 0;
        final int MAX_RESULTS = 8;

        RelatedResourceListItem relatedResources = new RelatedResourceListItem(name, title, true);
        relatedResources.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return CommonUtils.isDimensionRoleVisible(dimensionComponentDto.getTypeDimensionComponent());
            }
        });
        relatedResources.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                SelectItem conceptSchemeSelectItem = new SelectItem(ConceptSchemeDS.URN, getConstants().conceptScheme());
                searchConceptsForRolesWindow = new SearchMultipleRelatedResourcePaginatedWindow(MetamacSrmWeb.getConstants().dsdDimensionsRole(), MAX_RESULTS, conceptSchemeSelectItem,
                        new PaginatedAction() {

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

    private SearchViewTextItem createConceptItem(String name, String title) {
        final int FIRST_RESULT = 0;
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
                getUiHandlers().retrieveConceptSchemes(dimensionType, FIRST_RESULT, SrmWebConstants.NO_LIMIT_IN_PAGINATION);
                getUiHandlers().retrieveConcepts(dimensionType, FIRST_RESULT, MAX_RESULTS, null, null);

                searchConceptWindow.getInitialSelectionItem().addChangedHandler(new ChangedHandler() {

                    @Override
                    public void onChanged(ChangedEvent event) {
                        getUiHandlers().retrieveConcepts(dimensionType, FIRST_RESULT, MAX_RESULTS, searchConceptWindow.getRelatedResourceCriteria(), searchConceptWindow.getInitialSelectionValue());
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
                        editionForm.setValue(DimensionDS.CONCEPT_VIEW, selectedConcept != null
                                ? org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourceName(selectedConcept)
                                : null);

                        // When a concept is selected, reset the value of the codelist (the codelist depends on the concept)
                        editionForm.setValue(DimensionDS.ENUMERATED_REPRESENTATION_CODELIST, StringUtils.EMPTY);
                        editionForm.setValue(DimensionDS.ENUMERATED_REPRESENTATION_CODELIST_VIEW, StringUtils.EMPTY);

                        editionForm.markForRedraw();
                        editionForm.validate(false);
                    }
                });
            }
        });
        return conceptItem;
    }

    private SearchViewTextItem createMeasureDimensionEnumeratedRepresentationItem(String name, String title) {
        final SearchViewTextItem conceptSchemeItem = new SearchViewTextItem(name, title);
        conceptSchemeItem.setRequired(true);
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
                searchConceptSchemeForEnumeratedRepresentationWindow = new SearchRelatedResourcePaginatedWindow(getConstants().conceptSchemeSelection(), MAX_RESULTS, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        getUiHandlers().retrieveConceptSchemesForMeasureDimensionEnumeratedRepresentation(firstResult, maxResults,
                                searchConceptSchemeForEnumeratedRepresentationWindow.getRelatedResourceCriteria());
                    }
                });

                // Load concept schemes (to populate the selection window)
                getUiHandlers().retrieveConceptSchemesForMeasureDimensionEnumeratedRepresentation(FIRST_RESULST, MAX_RESULTS, null);

                searchConceptSchemeForEnumeratedRepresentationWindow.getListGridItem().getListGrid().setSelectionType(SelectionStyle.SINGLE);
                searchConceptSchemeForEnumeratedRepresentationWindow.getListGridItem().setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                        getUiHandlers().retrieveConceptSchemesForMeasureDimensionEnumeratedRepresentation(firstResult, maxResults, criteria);
                    }
                });

                searchConceptSchemeForEnumeratedRepresentationWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        RelatedResourceDto selectedConceptScheme = searchConceptSchemeForEnumeratedRepresentationWindow.getSelectedRelatedResource();
                        searchConceptSchemeForEnumeratedRepresentationWindow.markForDestroy();
                        // Set selected concept scheme in form
                        editionForm.setValue(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME, selectedConceptScheme != null ? selectedConceptScheme.getUrn() : null);
                        editionForm.setValue(DimensionDS.ENUMERATED_REPRESENTATION_CONCEPT_SCHEME_VIEW,
                                selectedConceptScheme != null ? org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourceName(selectedConceptScheme) : null);
                        editionForm.validate(false);
                    }
                });
            }
        });
        return conceptSchemeItem;
    }

    private SearchViewTextItem createEnumeratedRepresentationItem(String name, String title) {
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;
        final SearchViewTextItem codelistItem = new SearchViewTextItem(name, title);
        codelistItem.setRequired(true);
        codelistItem.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                // Show CodeList if RepresentationTypeEnum = ENUMERATED (except in MeasureDimension)
                return CommonUtils.isRepresentationTypeEnumerated(representationTypeItem.getValueAsString())
                        && !CommonUtils.isDimensionTypeMeasureDimension(editionForm.getValueAsString(DimensionDS.TYPE));
            }
        });
        codelistItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {

                final String conceptUrn = editionForm.getValueAsString(DimensionDS.CONCEPT);

                if (StringUtils.isBlank(conceptUrn)) {
                    // If a concept has not been selected, show a message and do not let the user to select a codelist
                    InformationWindow conceptRequiredWindow = new InformationWindow(getConstants().codelistSelection(), getConstants().dsdDimensionCodelistSelectionConceptRequired());
                    conceptRequiredWindow.show();
                } else {
                    searchCodelistForEnumeratedRepresentationWindow = new SearchRelatedResourcePaginatedWindow(getConstants().codelistSelection(), MAX_RESULTS, new PaginatedAction() {

                        @Override
                        public void retrieveResultSet(int firstResult, int maxResults) {
                            getUiHandlers().retrieveCodelistsForEnumeratedRepresentation(firstResult, maxResults, searchCodelistForEnumeratedRepresentationWindow.getRelatedResourceCriteria(),
                                    conceptUrn);
                        }
                    });
                    searchCodelistForEnumeratedRepresentationWindow.setInfoMessage(getConstants().dsdDimensionEnumeratedRepresentationInfoMessage());

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
                            editionForm.setValue(DimensionDS.ENUMERATED_REPRESENTATION_CODELIST, selectedCodelist != null ? selectedCodelist.getUrn() : null);
                            editionForm.setValue(DimensionDS.ENUMERATED_REPRESENTATION_CODELIST_VIEW,
                                    selectedCodelist != null ? org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourceName(selectedCodelist) : null);
                            editionForm.validate(false);
                        }
                    });
                }
            }
        });
        return codelistItem;
    }
}
