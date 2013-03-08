package org.siemac.metamac.srm.web.dsd.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getCoreMessages;

import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.web.client.constants.SrmWebConstants;
import org.siemac.metamac.srm.web.client.representation.widgets.StaticFacetForm;
import org.siemac.metamac.srm.web.client.utils.FacetFormUtils;
import org.siemac.metamac.srm.web.client.widgets.AnnotationsPanel;
import org.siemac.metamac.srm.web.client.widgets.SearchRelatedResourcePaginatedWindow;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeDS;
import org.siemac.metamac.srm.web.dsd.model.ds.PrimaryMeasureDS;
import org.siemac.metamac.srm.web.dsd.presenter.DsdPrimaryMeasureTabPresenter;
import org.siemac.metamac.srm.web.dsd.utils.CommonUtils;
import org.siemac.metamac.srm.web.dsd.utils.DsdClientSecurityUtils;
import org.siemac.metamac.srm.web.dsd.utils.DsdsFormUtils;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdPrimaryMeasureTabUiHandlers;
import org.siemac.metamac.srm.web.dsd.widgets.DsdFacetForm;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.SearchViewTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.FacetDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.RepresentationDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RepresentationTypeEnum;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.layout.VLayout;

public class DsdPrimaryMeasureTabViewImpl extends ViewWithUiHandlers<DsdPrimaryMeasureTabUiHandlers> implements DsdPrimaryMeasureTabPresenter.DsdPrimaryMeasureTabView {

    private VLayout                              panel;

    private InternationalMainFormLayout          mainFormLayout;

    // VIEW FORM
    private GroupDynamicForm                     form;
    private StaticFacetForm                      facetForm;
    private AnnotationsPanel                     viewAnnotationsPanel;

    // EDITION FORM
    private GroupDynamicForm                     editionForm;
    private DsdFacetForm                         facetEditionForm;
    private StaticFacetForm                      facetStaticEditionForm;
    private AnnotationsPanel                     editionAnnotationsPanel;

    private SearchRelatedResourcePaginatedWindow searchConceptWindow;
    private SearchRelatedResourcePaginatedWindow searchCodelistForEnumeratedRepresentationWindow;

    private DataStructureDefinitionMetamacDto    dataStructureDefinitionMetamacDto;
    private ComponentDto                         primaryMeasure;

    @Inject
    public DsdPrimaryMeasureTabViewImpl() {
        super();
        panel = new VLayout();

        mainFormLayout = new InternationalMainFormLayout();
        mainFormLayout.getTranslateToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                boolean translationsShowed = mainFormLayout.getTranslateToolStripButton().isSelected();
                viewAnnotationsPanel.setTranslationsShowed(translationsShowed);
                editionAnnotationsPanel.setTranslationsShowed(translationsShowed);
            }
        });
        mainFormLayout.getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (validate()) {
                    getUiHandlers().savePrimaryMeasure(getDsdPrimaryMeasure());
                }
            }
        });

        createViewForm();
        createEditionForm();

        panel.addMember(mainFormLayout);
    }

    /**
     * Creates and returns the view layout
     * 
     * @return
     */
    private void createViewForm() {
        form = new GroupDynamicForm(getConstants().dsdPrimaryMeasureDetails());

        ViewTextItem code = new ViewTextItem(PrimaryMeasureDS.CODE, getConstants().identifiableArtefactCode());
        ViewTextItem urn = new ViewTextItem(PrimaryMeasureDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(PrimaryMeasureDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        ViewTextItem concept = new ViewTextItem(PrimaryMeasureDS.CONCEPT_VIEW, getConstants().concept());
        ViewTextItem staticRepresentationTypeItem = new ViewTextItem(PrimaryMeasureDS.REPRESENTATION_TYPE, getConstants().representation());
        ViewTextItem enumeratedRepresentation = new ViewTextItem(PrimaryMeasureDS.ENUMERATED_REPRESENTATION, getConstants().representationEnumerated());
        form.setFields(code, urn, urnProvider, concept, staticRepresentationTypeItem, enumeratedRepresentation);

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
        editionForm = new GroupDynamicForm(getConstants().dsdPrimaryMeasureDetails());

        ViewTextItem code = new ViewTextItem(PrimaryMeasureDS.CODE, getConstants().identifiableArtefactCode());
        ViewTextItem urn = new ViewTextItem(PrimaryMeasureDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(PrimaryMeasureDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());

        // CONCEPT

        ViewTextItem concept = new ViewTextItem(PrimaryMeasureDS.CONCEPT, getConstants().concept()); // This item is never shown. Stores the concept URN
        concept.setShowIfCondition(FormItemUtils.getFalseFormItemIfFunction());

        SearchViewTextItem staticEditableConcept = createConceptItem(PrimaryMeasureDS.CONCEPT_EDITION_VIEW, getConstants().concept()); // Shown in editionMode, only when the concept is editable
        staticEditableConcept.setShowIfCondition(getConceptFormItemIfFunction());

        ViewTextItem staticConcept = new ViewTextItem(PrimaryMeasureDS.CONCEPT_VIEW, getConstants().concept()); // This item is shown when the concept can not be edited
        staticConcept.setShowIfCondition(getStaticConceptFormItemIfFunction());

        // REPRESENTATION TYPE

        CustomSelectItem representationTypeItem = createRepresentationTypeItem(PrimaryMeasureDS.REPRESENTATION_TYPE, getConstants().representation());
        representationTypeItem.setShowIfCondition(getRepresentationTypeFormItemIfFunction());

        ViewTextItem staticRepresentationTypeItem = new ViewTextItem(PrimaryMeasureDS.REPRESENTATION_TYPE_VIEW, getConstants().representation());
        staticRepresentationTypeItem.setShowIfCondition(getStaticRepresentationTypeFormItemIfFunction());

        // ENUMERATED REPRESENTATION

        ViewTextItem enumeratedRepresentation = new ViewTextItem(PrimaryMeasureDS.ENUMERATED_REPRESENTATION, getConstants().representationEnumerated());
        enumeratedRepresentation.setShowIfCondition(FormItemUtils.getFalseFormItemIfFunction()); // This item is never shown. Stores the enumerated representation (codelist URN)

        SearchViewTextItem enumeratedRepresentationEditionView = createEnumeratedRepresentationItem(PrimaryMeasureDS.ENUMERATED_REPRESENTATION_EDITION_VIEW, getConstants().representationEnumerated());
        enumeratedRepresentationEditionView.setShowIfCondition(getEnumeratedRepresentationFormItemIfFunction()); // Shown in editionMode, only when the enumerated representation is editable

        ViewTextItem enumeratedRepresentationView = new ViewTextItem(PrimaryMeasureDS.ENUMERATED_REPRESENTATION_VIEW, getConstants().representationEnumerated());
        enumeratedRepresentationView.setShowIfCondition(getStaticEnumeratedRepresentationFormItemIfFunction()); // This item is shown when the enumerated representation can not be edited

        editionForm.setFields(code, urn, urnProvider, concept, staticEditableConcept, staticConcept, representationTypeItem, staticRepresentationTypeItem, enumeratedRepresentation,
                enumeratedRepresentationEditionView, enumeratedRepresentationView);

        // FACET

        facetEditionForm = new DsdFacetForm();
        facetEditionForm.setVisibility(Visibility.HIDDEN);
        facetStaticEditionForm = new StaticFacetForm();
        facetStaticEditionForm.setVisibility(Visibility.HIDDEN);

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
    public void setCodelistsForEnumeratedRepresentation(GetRelatedResourcesResult result) {
        if (searchCodelistForEnumeratedRepresentationWindow != null) {
            searchCodelistForEnumeratedRepresentationWindow.setRelatedResources(result.getRelatedResourceDtos());
            searchCodelistForEnumeratedRepresentationWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getRelatedResourceDtos().size(), result.getTotalResults());
        }
    }

    @Override
    public void setDsdPrimaryMeasure(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto, ComponentDto componentDto) {
        this.dataStructureDefinitionMetamacDto = dataStructureDefinitionMetamacDto;
        this.primaryMeasure = componentDto;

        mainFormLayout.setViewMode();

        // Security
        mainFormLayout.setCanEdit(DsdClientSecurityUtils.canUpdatePrimaryMeasure(dataStructureDefinitionMetamacDto.getLifeCycle().getProcStatus(),
                CommonUtils.getStatisticalOperationCodeFromDsd(dataStructureDefinitionMetamacDto)));

        setPrimaryMeasureViewMode(componentDto);
        setPrimaryMeasureEditionMode(componentDto);
    }

    private void setPrimaryMeasureViewMode(ComponentDto componentDto) {
        form.setValue(PrimaryMeasureDS.CODE, componentDto.getCode());
        form.setValue(PrimaryMeasureDS.URN, componentDto.getUrn());
        form.setValue(PrimaryMeasureDS.URN_PROVIDER, componentDto.getUrnProvider());

        // Concept
        form.setValue(PrimaryMeasureDS.CONCEPT_VIEW, RelatedResourceUtils.getRelatedResourceName(componentDto.getCptIdRef()));

        // Representation
        facetForm.hide();
        form.getItem(PrimaryMeasureDS.ENUMERATED_REPRESENTATION).hide();
        form.getItem(PrimaryMeasureDS.ENUMERATED_REPRESENTATION).clearValue();
        form.getItem(PrimaryMeasureDS.REPRESENTATION_TYPE).clearValue();
        facetForm.clearValues();
        if (componentDto.getLocalRepresentation() != null) {

            if (RepresentationTypeEnum.ENUMERATION.equals(componentDto.getLocalRepresentation().getRepresentationType())) {

                // Codelist

                form.getItem(PrimaryMeasureDS.REPRESENTATION_TYPE).setValue(getCoreMessages().representationTypeEnumENUMERATION());
                form.getItem(PrimaryMeasureDS.ENUMERATED_REPRESENTATION).setValue(RelatedResourceUtils.getRelatedResourceName(componentDto.getLocalRepresentation().getEnumeration()));
                form.getItem(PrimaryMeasureDS.ENUMERATED_REPRESENTATION).show();

            } else if (RepresentationTypeEnum.TEXT_FORMAT.equals(componentDto.getLocalRepresentation().getRepresentationType())) {

                // Facet

                form.getItem(PrimaryMeasureDS.REPRESENTATION_TYPE).setValue(getCoreMessages().representationTypeEnumTEXT_FORMAT());
                FacetDto facetDto = componentDto.getLocalRepresentation().getTextFormat();
                facetForm.setFacet(facetDto);
                facetForm.show();
            }
        }

        // Annotations
        viewAnnotationsPanel.setAnnotations(componentDto.getAnnotations());
    }

    private void setPrimaryMeasureEditionMode(ComponentDto componentDto) {
        editionForm.setValue(PrimaryMeasureDS.CODE, componentDto.getCode());
        editionForm.setValue(PrimaryMeasureDS.URN, componentDto.getUrn());
        editionForm.setValue(PrimaryMeasureDS.URN_PROVIDER, componentDto.getUrnProvider());

        // Concept
        editionForm.setValue(PrimaryMeasureDS.CONCEPT, componentDto.getCptIdRef() != null ? componentDto.getCptIdRef().getUrn() : null);
        editionForm.setValue(PrimaryMeasureDS.CONCEPT_EDITION_VIEW, RelatedResourceUtils.getRelatedResourceName(componentDto.getCptIdRef()));
        editionForm.setValue(PrimaryMeasureDS.CONCEPT_VIEW, RelatedResourceUtils.getRelatedResourceName(componentDto.getCptIdRef()));

        // Representation
        editionForm.getItem(PrimaryMeasureDS.ENUMERATED_REPRESENTATION_VIEW).clearValue();
        editionForm.getItem(PrimaryMeasureDS.ENUMERATED_REPRESENTATION_EDITION_VIEW).clearValue();
        editionForm.getItem(PrimaryMeasureDS.ENUMERATED_REPRESENTATION).clearValue();
        editionForm.getItem(PrimaryMeasureDS.REPRESENTATION_TYPE).clearValue();
        editionForm.getItem(PrimaryMeasureDS.REPRESENTATION_TYPE_VIEW).clearValue();
        facetEditionForm.clearValues();
        if (componentDto.getLocalRepresentation() != null) {

            if (RepresentationTypeEnum.ENUMERATION.equals(componentDto.getLocalRepresentation().getRepresentationType())) {

                // Codelist

                editionForm.getItem(PrimaryMeasureDS.REPRESENTATION_TYPE).setValue(RepresentationTypeEnum.ENUMERATION.toString());
                editionForm.getItem(PrimaryMeasureDS.REPRESENTATION_TYPE_VIEW).setValue(getCoreMessages().representationTypeEnumENUMERATION());
                editionForm.getItem(PrimaryMeasureDS.ENUMERATED_REPRESENTATION).setValue(componentDto.getLocalRepresentation().getEnumeration().getUrn());
                editionForm.getItem(PrimaryMeasureDS.ENUMERATED_REPRESENTATION_EDITION_VIEW).setValue(
                        RelatedResourceUtils.getRelatedResourceName(componentDto.getLocalRepresentation().getEnumeration()));
                editionForm.getItem(PrimaryMeasureDS.ENUMERATED_REPRESENTATION_VIEW).setValue(RelatedResourceUtils.getRelatedResourceName(componentDto.getLocalRepresentation().getEnumeration()));

            } else if (RepresentationTypeEnum.TEXT_FORMAT.equals(componentDto.getLocalRepresentation().getRepresentationType())) {

                // Facet

                editionForm.getItem(PrimaryMeasureDS.REPRESENTATION_TYPE).setValue(RepresentationTypeEnum.TEXT_FORMAT.toString());
                editionForm.getItem(PrimaryMeasureDS.REPRESENTATION_TYPE_VIEW).setValue(getCoreMessages().representationTypeEnumTEXT_FORMAT());
                FacetDto facetDto = componentDto.getLocalRepresentation().getTextFormat();
                facetEditionForm.setFacet(facetDto);
                facetStaticEditionForm.setFacet(facetDto);
            }
        }
        FacetFormUtils.setFacetFormVisibility(facetEditionForm, facetStaticEditionForm, editionForm.getValueAsString(PrimaryMeasureDS.REPRESENTATION_TYPE),
                dataStructureDefinitionMetamacDto.getMaintainer());
        editionForm.markForRedraw();

        // Clear errors
        editionForm.clearErrors(true);
        facetEditionForm.clearErrors(true);

        // Annotations
        editionAnnotationsPanel.setAnnotations(componentDto.getAnnotations());
    }

    public ComponentDto getDsdPrimaryMeasure() {
        // Concept
        primaryMeasure.setCptIdRef(StringUtils.isBlank(editionForm.getValueAsString(PrimaryMeasureDS.CONCEPT)) ? null : RelatedResourceUtils.createRelatedResourceDto(
                TypeExternalArtefactsEnum.CONCEPT, editionForm.getValueAsString(PrimaryMeasureDS.CONCEPT)));

        // Representation
        if (editionForm.getItem(PrimaryMeasureDS.REPRESENTATION_TYPE).getValue() != null && !editionForm.getItem(PrimaryMeasureDS.REPRESENTATION_TYPE).getValue().toString().isEmpty()) {
            RepresentationTypeEnum representationType = RepresentationTypeEnum.valueOf(editionForm.getItem(PrimaryMeasureDS.REPRESENTATION_TYPE).getValue().toString());

            if (primaryMeasure.getLocalRepresentation() == null) {
                primaryMeasure.setLocalRepresentation(new RepresentationDto());
            }

            if (RepresentationTypeEnum.ENUMERATION.equals(representationType)) {

                // Code List

                primaryMeasure.getLocalRepresentation().setRepresentationType(RepresentationTypeEnum.ENUMERATION);
                primaryMeasure.getLocalRepresentation().setEnumeration(
                        StringUtils.isBlank(editionForm.getValueAsString(PrimaryMeasureDS.ENUMERATED_REPRESENTATION)) ? null : RelatedResourceUtils.createRelatedResourceDto(
                                TypeExternalArtefactsEnum.CODELIST, editionForm.getValueAsString(PrimaryMeasureDS.ENUMERATED_REPRESENTATION)));
                primaryMeasure.getLocalRepresentation().setTextFormat(null);

            } else if (RepresentationTypeEnum.TEXT_FORMAT.equals(representationType)) {

                // Facet

                primaryMeasure.getLocalRepresentation().setRepresentationType(RepresentationTypeEnum.TEXT_FORMAT);
                FacetDto facetDto = facetEditionForm.getFacet();
                primaryMeasure.getLocalRepresentation().setTextFormat(facetDto);
                primaryMeasure.getLocalRepresentation().setEnumeration(null);
            }

        } else {
            // No representation
            primaryMeasure.setLocalRepresentation(null);
        }

        // Annotations
        primaryMeasure.getAnnotations().clear();
        primaryMeasure.getAnnotations().addAll(editionAnnotationsPanel.getAnnotations());
        return primaryMeasure;
    }

    @Override
    public void onPrimaryMeasureSaved(DataStructureDefinitionMetamacDto dsd, ComponentDto componentDto) {
        setDsdPrimaryMeasure(dsd, componentDto);
        mainFormLayout.setViewMode();
    }

    private boolean validate() {
        return Visibility.HIDDEN.equals(facetEditionForm.getVisibility()) ? editionForm.validate(false) : (editionForm.validate(false) && facetEditionForm.validate(false));
    }

    private CustomSelectItem createRepresentationTypeItem(String name, String title) {
        final CustomSelectItem representationTypeItem = new CustomSelectItem(name, title);
        representationTypeItem.setValueMap(org.siemac.metamac.srm.web.client.utils.CommonUtils.getTypeRepresentationEnumHashMap());
        representationTypeItem.setRedrawOnChange(true);
        // Show FacetForm if RepresentationTypeEnum = NON_NUMERATED
        representationTypeItem.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                FacetFormUtils.setFacetFormVisibility(facetEditionForm, facetStaticEditionForm, representationTypeItem.getValueAsString(), dataStructureDefinitionMetamacDto.getMaintainer());
            }
        });
        return representationTypeItem;
    }

    private SearchViewTextItem createConceptItem(String name, String title) {
        final int FIRST_RESULT = 0;
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
                getUiHandlers().retrieveConceptSchemes(FIRST_RESULT, SrmWebConstants.NO_LIMIT_IN_PAGINATION);
                getUiHandlers().retrieveConcepts(FIRST_RESULT, MAX_RESULTS, null, null);

                searchConceptWindow.getInitialSelectionItem().addChangedHandler(new ChangedHandler() {

                    @Override
                    public void onChanged(ChangedEvent event) {
                        getUiHandlers().retrieveConcepts(FIRST_RESULT, MAX_RESULTS, searchConceptWindow.getRelatedResourceCriteria(), searchConceptWindow.getInitialSelectionValue());
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
                        editionForm.setValue(PrimaryMeasureDS.CONCEPT, selectedConcept != null ? selectedConcept.getUrn() : null);
                        editionForm.setValue(PrimaryMeasureDS.CONCEPT_EDITION_VIEW,
                                selectedConcept != null ? org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourceName(selectedConcept) : null);
                        editionForm.validate(false);
                    }
                });
            }
        });
        return conceptItem;
    }

    private SearchViewTextItem createEnumeratedRepresentationItem(String name, String title) {
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;
        final SearchViewTextItem enumeratedRepresentationItem = new SearchViewTextItem(name, title);
        enumeratedRepresentationItem.setRequired(true);
        enumeratedRepresentationItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                searchCodelistForEnumeratedRepresentationWindow = new SearchRelatedResourcePaginatedWindow(getConstants().codelistSelection(), MAX_RESULTS, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        getUiHandlers().retrieveCodelistsForEnumeratedRepresentation(firstResult, maxResults, searchCodelistForEnumeratedRepresentationWindow.getRelatedResourceCriteria());
                    }
                });

                // Load codelists (to populate the selection window)
                getUiHandlers().retrieveCodelistsForEnumeratedRepresentation(FIRST_RESULST, MAX_RESULTS, null);

                searchCodelistForEnumeratedRepresentationWindow.getListGridItem().getListGrid().setSelectionType(SelectionStyle.SINGLE);
                searchCodelistForEnumeratedRepresentationWindow.getListGridItem().setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                        getUiHandlers().retrieveCodelistsForEnumeratedRepresentation(firstResult, maxResults, criteria);
                    }
                });

                searchCodelistForEnumeratedRepresentationWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        RelatedResourceDto selectedCodelist = searchCodelistForEnumeratedRepresentationWindow.getSelectedRelatedResource();
                        searchCodelistForEnumeratedRepresentationWindow.markForDestroy();
                        // Set selected codelist in form
                        editionForm.setValue(PrimaryMeasureDS.ENUMERATED_REPRESENTATION, selectedCodelist != null ? selectedCodelist.getUrn() : null);
                        editionForm.setValue(PrimaryMeasureDS.ENUMERATED_REPRESENTATION_EDITION_VIEW,
                                selectedCodelist != null ? org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourceName(selectedCodelist) : null);
                        editionForm.validate(false);
                    }
                });
            }
        });
        return enumeratedRepresentationItem;
    }

    // ------------------------------------------------------------------------------------------------------------
    // FORM ITEM IF FUNCTIONS
    // ------------------------------------------------------------------------------------------------------------

    // CONCEPT

    private FormItemIfFunction getConceptFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return DsdsFormUtils.canPrimaryMeasureConceptBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }

    private FormItemIfFunction getStaticConceptFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !DsdsFormUtils.canPrimaryMeasureConceptBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }

    // REPRESENTATION TYPE

    private FormItemIfFunction getRepresentationTypeFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return DsdsFormUtils.canPrimaryMeasureRepresentationTypeBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }

    private FormItemIfFunction getStaticRepresentationTypeFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !DsdsFormUtils.canPrimaryMeasureRepresentationTypeBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }

    // ENUMERATED REPRESENTATION

    private FormItemIfFunction getEnumeratedRepresentationFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                // Shown when the representation type selected is ENUMERATION and the enumerated representation can be edited
                return CommonUtils.isRepresentationTypeEnumerated(editionForm.getValueAsString(PrimaryMeasureDS.REPRESENTATION_TYPE))
                        && DsdsFormUtils.canPrimaryMeasureEnumeratedRepresentationBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }

    private FormItemIfFunction getStaticEnumeratedRepresentationFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                // Shown when the representation type selected is ENUMERATION and the enumerated representation can NOT be edited
                return CommonUtils.isRepresentationTypeEnumerated(editionForm.getValueAsString(PrimaryMeasureDS.REPRESENTATION_TYPE))
                        && !DsdsFormUtils.canPrimaryMeasureEnumeratedRepresentationBeEdited(dataStructureDefinitionMetamacDto);
            }
        };
    }
}
