package org.siemac.metamac.srm.web.dsd.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
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
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdPrimaryMeasureTabUiHandlers;
import org.siemac.metamac.srm.web.dsd.widgets.DsdFacetForm;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.utils.ExternalItemUtils;
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
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeRepresentationEnum;
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
import com.smartgwt.client.widgets.form.fields.events.HasChangeHandlers;
import com.smartgwt.client.widgets.layout.VLayout;

public class DsdPrimaryMeasureTabViewImpl extends ViewWithUiHandlers<DsdPrimaryMeasureTabUiHandlers> implements DsdPrimaryMeasureTabPresenter.DsdPrimaryMeasureTabView {

    private List<ExternalItemDto>                codeLists;

    private VLayout                              panel;

    private InternationalMainFormLayout          mainFormLayout;

    // VIEW FORM
    private GroupDynamicForm                     form;
    private ViewTextItem                         staticRepresentationTypeItem;
    private ViewTextItem                         staticCodeListItem;
    private StaticFacetForm                      staticFacetForm;
    private AnnotationsPanel                     viewAnnotationsPanel;

    // EDITION FORM
    private GroupDynamicForm                     editionForm;
    private CustomSelectItem                     representationTypeItem;
    private CustomSelectItem                     codeListItem;
    private DsdFacetForm                         facetForm;
    private AnnotationsPanel                     editionAnnotationsPanel;

    private SearchRelatedResourcePaginatedWindow searchConceptWindow;

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
        form = new GroupDynamicForm(MetamacSrmWeb.getConstants().dsdPrimaryMeasureDetails());

        ViewTextItem code = new ViewTextItem(PrimaryMeasureDS.CODE, getConstants().identifiableArtefactCode());
        ViewTextItem urn = new ViewTextItem(PrimaryMeasureDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(PrimaryMeasureDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        ViewTextItem concept = new ViewTextItem(PrimaryMeasureDS.CONCEPT_VIEW, MetamacSrmWeb.getConstants().concept());
        staticRepresentationTypeItem = new ViewTextItem(PrimaryMeasureDS.REPRESENTATION_TYPE, MetamacSrmWeb.getConstants().representation());
        staticCodeListItem = new ViewTextItem(PrimaryMeasureDS.ENUMERATED_REPRESENTATION, MetamacSrmWeb.getConstants().dsdCodeList());
        form.setFields(code, urn, urnProvider, concept, staticRepresentationTypeItem, staticCodeListItem);

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
        editionForm = new GroupDynamicForm(MetamacSrmWeb.getConstants().dsdPrimaryMeasureDetails());
        ViewTextItem code = new ViewTextItem(PrimaryMeasureDS.CODE, getConstants().identifiableArtefactCode());
        ViewTextItem urn = new ViewTextItem(PrimaryMeasureDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(PrimaryMeasureDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        ViewTextItem concept = new ViewTextItem(PrimaryMeasureDS.CONCEPT, getConstants().concept());
        concept.setShowIfCondition(FormItemUtils.getFalseFormItemIfFunction());
        SearchViewTextItem conceptView = createConceptItem(PrimaryMeasureDS.CONCEPT_VIEW, getConstants().concept());

        representationTypeItem = new CustomSelectItem(PrimaryMeasureDS.REPRESENTATION_TYPE, MetamacSrmWeb.getConstants().representation());
        representationTypeItem.setValueMap(org.siemac.metamac.srm.web.client.utils.CommonUtils.getTypeRepresentationEnumHashMap());
        representationTypeItem.setRedrawOnChange(true);
        // Show FacetForm if RepresentationTypeEnum = NON_NUMERATED
        representationTypeItem.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                FacetFormUtils.setFacetFormVisibility(facetForm, representationTypeItem.getValueAsString());
            }
        });

        codeListItem = new CustomSelectItem(PrimaryMeasureDS.ENUMERATED_REPRESENTATION, MetamacSrmWeb.getConstants().dsdCodeList());
        // Show CodeList if RepresentationTypeEnum = ENUMERATED
        codeListItem.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return CommonUtils.isRepresentationTypeEnumerated(representationTypeItem.getValueAsString());
            }
        });

        editionForm.setFields(code, urn, urnProvider, concept, conceptView, representationTypeItem, codeListItem);

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
    public void setCodeLists(List<ExternalItemDto> codeLists) {
        this.codeLists = codeLists;
        codeListItem.setValueMap(ExternalItemUtils.getExternalItemsHashMap(codeLists));
    }

    @Override
    public void setDsdPrimaryMeasure(ProcStatusEnum procStatus, ComponentDto componentDto) {
        this.primaryMeasure = componentDto;

        mainFormLayout.setViewMode();

        // Security
        mainFormLayout.setCanEdit(DsdClientSecurityUtils.canUpdatePrimaryMeasure(procStatus));

        setPrimaryMeasureViewMode(componentDto);
        setPrimaryMeasureEditionMode(componentDto);
    }

    private void setPrimaryMeasureViewMode(ComponentDto componentDto) {
        form.setValue(PrimaryMeasureDS.CODE, componentDto.getCode());
        form.setValue(PrimaryMeasureDS.URN, componentDto.getUrn());
        form.setValue(PrimaryMeasureDS.URN_PROVIDER, componentDto.getUrnProvider());

        // Concept
        form.setValue(PrimaryMeasureDS.CONCEPT_VIEW, ExternalItemUtils.getExternalItemName(componentDto.getCptIdRef())); // TODO RelatedResourceDto instead of ExternalItemDto

        // Representation
        staticFacetForm.hide();
        staticCodeListItem.hide();
        staticCodeListItem.clearValue();
        staticRepresentationTypeItem.clearValue();
        staticFacetForm.clearValues();
        if (componentDto.getLocalRepresentation() != null) {
            if (TypeRepresentationEnum.ENUMERATED.equals(componentDto.getLocalRepresentation().getTypeRepresentationEnum())) {
                staticCodeListItem.setValue(componentDto.getLocalRepresentation().getEnumerated().getCode());
                staticRepresentationTypeItem.setValue(MetamacSrmWeb.getCoreMessages().typeRepresentationEnumENUMERATED());
                staticCodeListItem.show();
                // Facet
            } else if (TypeRepresentationEnum.TEXT_FORMAT.equals(componentDto.getLocalRepresentation().getTypeRepresentationEnum())) {
                staticRepresentationTypeItem.setValue(MetamacSrmWeb.getCoreMessages().typeRepresentationEnumTEXT_FORMAT());
                FacetDto facetDto = componentDto.getLocalRepresentation().getNonEnumerated();
                staticFacetForm.setFacet(facetDto);
                staticFacetForm.show();
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
        editionForm.setValue(PrimaryMeasureDS.CONCEPT_VIEW, ExternalItemUtils.getExternalItemName(componentDto.getCptIdRef())); // TODO RelatedResourceDto instead of ExternalItemDto

        // Representation
        codeListItem.clearValue();
        representationTypeItem.clearValue();
        facetForm.clearValues();
        facetForm.clearValues();
        if (componentDto.getLocalRepresentation() != null) {
            // Code List
            if (TypeRepresentationEnum.ENUMERATED.equals(componentDto.getLocalRepresentation().getTypeRepresentationEnum())) {
                // codeListItem.setValue(componentDto.getLocalRepresentation().getEnumCodeList().getCodeId());
                codeListItem.clearValue(); // don´t know the concept (which is the scheme?), so code list neither
                representationTypeItem.setValue(TypeRepresentationEnum.ENUMERATED.toString());
                // Facet
            } else if (TypeRepresentationEnum.TEXT_FORMAT.equals(componentDto.getLocalRepresentation().getTypeRepresentationEnum())) {
                representationTypeItem.setValue(TypeRepresentationEnum.TEXT_FORMAT.toString());
                FacetDto facetDto = componentDto.getLocalRepresentation().getNonEnumerated();
                facetForm.setFacet(facetDto);
            }
        }
        FacetFormUtils.setFacetFormVisibility(facetForm, representationTypeItem.getValueAsString());
        editionForm.redraw();

        // Clear errors
        editionForm.clearErrors(true);
        facetForm.clearErrors(true);

        // Annotations
        editionAnnotationsPanel.setAnnotations(componentDto.getAnnotations());
    }

    public ComponentDto getDsdPrimaryMeasure() {
        // Concept
        // TODO RelatedResourceDto instead of ExternalItemDto
        // primaryMeasure.setCptIdRef(StringUtils.isBlank(editionForm.getValueAsString(PrimaryMeasureDS.CONCEPT)) ? null :
        // RelatedResourceUtils.createRelatedResourceDto(TypeExternalArtefactsEnum.CONCEPT,
        // editionForm.getValueAsString(PrimaryMeasureDS.CONCEPT)));
        primaryMeasure.setCptIdRef(StringUtils.isBlank(editionForm.getValueAsString(PrimaryMeasureDS.CONCEPT)) ? null : RelatedResourceUtils.createExternalItemDto(TypeExternalArtefactsEnum.CONCEPT,
                editionForm.getValueAsString(PrimaryMeasureDS.CONCEPT)));

        // Representation
        if (representationTypeItem.getValue() != null && !representationTypeItem.getValue().toString().isEmpty()) {
            TypeRepresentationEnum representationType = TypeRepresentationEnum.valueOf(representationTypeItem.getValue().toString());

            if (primaryMeasure.getLocalRepresentation() == null) {
                primaryMeasure.setLocalRepresentation(new RepresentationDto());
            }

            // Code List
            if (TypeRepresentationEnum.ENUMERATED.equals(representationType)) {
                primaryMeasure.getLocalRepresentation().setTypeRepresentationEnum(TypeRepresentationEnum.ENUMERATED);
                primaryMeasure.getLocalRepresentation().setEnumerated(ExternalItemUtils.getExternalItemDtoFromUrn(codeLists, codeListItem.getValueAsString()));
                primaryMeasure.getLocalRepresentation().setNonEnumerated(null);
                // Facet
            } else if (TypeRepresentationEnum.TEXT_FORMAT.equals(representationType)) {
                primaryMeasure.getLocalRepresentation().setTypeRepresentationEnum(TypeRepresentationEnum.TEXT_FORMAT);
                FacetDto facetDto = facetForm.getFacet();
                primaryMeasure.getLocalRepresentation().setNonEnumerated(facetDto);
                primaryMeasure.getLocalRepresentation().setEnumerated(null);
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
    public void onPrimaryMeasureSaved(ProcStatusEnum procStatus, ComponentDto componentDto) {
        setDsdPrimaryMeasure(procStatus, componentDto);
        mainFormLayout.setViewMode();
    }

    @Override
    public HasChangeHandlers onRepresentationTypeChange() {
        return representationTypeItem;
    }

    private boolean validate() {
        return Visibility.HIDDEN.equals(facetForm.getVisibility()) ? editionForm.validate(false) : (editionForm.validate(false) && facetForm.validate(false));
    }

    private SearchViewTextItem createConceptItem(String name, String title) {
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;
        SearchViewTextItem conceptItem = new SearchViewTextItem(name, title);
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
                        editionForm.setValue(PrimaryMeasureDS.CONCEPT, selectedConcept != null ? selectedConcept.getUrn() : null);
                        editionForm.setValue(PrimaryMeasureDS.CONCEPT_VIEW, selectedConcept != null
                                ? org.siemac.metamac.srm.web.client.utils.CommonUtils.getRelatedResourceName(selectedConcept)
                                : null);
                    }
                });
            }
        });
        return conceptItem;
    }
}
