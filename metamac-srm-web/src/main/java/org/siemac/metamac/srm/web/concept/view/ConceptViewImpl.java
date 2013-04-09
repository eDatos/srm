package org.siemac.metamac.srm.web.concept.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptTypeDto;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptRoleEnum;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.constants.SrmWebConstants;
import org.siemac.metamac.srm.web.client.model.ds.RepresentationDS;
import org.siemac.metamac.srm.web.client.representation.widgets.StaticFacetForm;
import org.siemac.metamac.srm.web.client.utils.FacetFormUtils;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.client.widgets.AnnotationsPanel;
import org.siemac.metamac.srm.web.client.widgets.CustomVLayout;
import org.siemac.metamac.srm.web.client.widgets.RelatedResourceListItem;
import org.siemac.metamac.srm.web.client.widgets.SearchMultipleRelatedResourcePaginatedWindow;
import org.siemac.metamac.srm.web.client.widgets.SearchRelatedResourcePaginatedWindow;
import org.siemac.metamac.srm.web.code.model.ds.VariableFamilyDS;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeDS;
import org.siemac.metamac.srm.web.concept.presenter.ConceptPresenter;
import org.siemac.metamac.srm.web.concept.utils.CommonUtils;
import org.siemac.metamac.srm.web.concept.utils.ConceptsClientSecurityUtils;
import org.siemac.metamac.srm.web.concept.utils.ConceptsFormUtils;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptUiHandlers;
import org.siemac.metamac.srm.web.concept.widgets.ConceptFacetForm;
import org.siemac.metamac.srm.web.concept.widgets.ConceptsListItem;
import org.siemac.metamac.srm.web.concept.widgets.ConceptsTreeGrid;
import org.siemac.metamac.srm.web.concept.widgets.ConceptsTreeWindow;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.srm.web.shared.code.GetVariableFamiliesResult;
import org.siemac.metamac.srm.web.shared.code.GetVariablesResult;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.InformationWindow;
import org.siemac.metamac.web.common.client.widgets.TitleLabel;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextAreaItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultilanguageRichTextEditorItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.SearchViewTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.RepresentationDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RepresentationTypeEnum;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionStyle;
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

public class ConceptViewImpl extends ViewWithUiHandlers<ConceptUiHandlers> implements ConceptPresenter.ConceptView {

    private VLayout                                      panel;
    private InternationalMainFormLayout                  mainFormLayout;

    private ConceptsTreeGrid                             conceptsTreeGrid;

    // View forms
    private GroupDynamicForm                             identifiersForm;
    private GroupDynamicForm                             contentDescriptorsForm;
    private StaticFacetForm                              facetForm;
    private GroupDynamicForm                             classDescriptorsForm;
    private GroupDynamicForm                             relationBetweenConceptsForm;
    private GroupDynamicForm                             legalActsForm;
    private GroupDynamicForm                             commentsForm;
    private AnnotationsPanel                             annotationsPanel;

    // Edition forms
    private GroupDynamicForm                             identifiersEditionForm;
    private GroupDynamicForm                             contentDescriptorsEditionForm;
    private ConceptFacetForm                             facetEditionForm;
    private StaticFacetForm                              facetStaticEditionForm;
    private GroupDynamicForm                             classDescriptorsEditionForm;
    private GroupDynamicForm                             relationBetweenConceptsEditionForm;
    private GroupDynamicForm                             legalActsEditionForm;
    private GroupDynamicForm                             commentsEditionForm;
    private AnnotationsPanel                             annotationsEditionPanel;

    private List<ConceptTypeDto>                         conceptTypeDtos;

    private ConceptSchemeMetamacDto                      conceptSchemeMetamacDto;
    private List<ItemHierarchyDto>                       itemHierarchyDtos;
    private ConceptMetamacDto                            conceptDto;

    private SearchMultipleRelatedResourcePaginatedWindow searchRolesWindow;
    private SearchRelatedResourcePaginatedWindow         searchExtendsWindow;
    private SearchRelatedResourcePaginatedWindow         searchVariableWindow;
    private SearchRelatedResourcePaginatedWindow         searchCodelistForEnumeratedRepresentationWindow;

    @Inject
    public ConceptViewImpl() {
        super();
        panel = new VLayout();
        panel.setHeight100();

        //
        // CONCEPTS HIERARCHY
        //

        conceptsTreeGrid = new ConceptsTreeGrid();
        conceptsTreeGrid.setAutoFitMaxRecords(10);

        CustomVLayout conceptsListGridLayout = new CustomVLayout();
        conceptsListGridLayout.addMember(new TitleLabel(getConstants().conceptSchemeConcepts()));
        conceptsListGridLayout.addMember(conceptsTreeGrid);

        //
        // CONCEPT
        //

        mainFormLayout = new InternationalMainFormLayout();

        // Translations
        mainFormLayout.getTranslateToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                boolean translationsShowed = mainFormLayout.getTranslateToolStripButton().isSelected();
                identifiersForm.setTranslationsShowed(translationsShowed);
                identifiersEditionForm.setTranslationsShowed(translationsShowed);

                contentDescriptorsForm.setTranslationsShowed(translationsShowed);
                contentDescriptorsEditionForm.setTranslationsShowed(translationsShowed);

                classDescriptorsForm.setTranslationsShowed(translationsShowed);
                classDescriptorsEditionForm.setTranslationsShowed(translationsShowed);

                relationBetweenConceptsForm.setTranslationsShowed(translationsShowed);
                relationBetweenConceptsEditionForm.setTranslationsShowed(translationsShowed);

                legalActsForm.setTranslationsShowed(translationsShowed);
                legalActsEditionForm.setTranslationsShowed(translationsShowed);

                commentsForm.setTranslationsShowed(translationsShowed);
                commentsEditionForm.setTranslationsShowed(translationsShowed);

                annotationsPanel.setTranslationsShowed(translationsShowed);
                annotationsEditionPanel.setTranslationsShowed(translationsShowed);
            }
        });

        // Save
        mainFormLayout.getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (validateEditionForms()) {
                    getUiHandlers().saveConcept(getConceptDto(), getRoles(), getRelatedConcepts());
                }
            }
        });

        createViewForm();
        createEditionForm();

        VLayout subPanel = new VLayout();
        subPanel.setOverflow(Overflow.SCROLL);
        subPanel.addMember(conceptsListGridLayout);
        subPanel.addMember(mainFormLayout);

        panel.addMember(subPanel);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == ConceptPresenter.TYPE_SetContextAreaContentConceptsToolBar) {
            if (content != null) {
                panel.addMember(content, 0);
            }
        } else {
            // To support inheritance in your views it is good practice to call super.setInSlot when you can't handle the call.
            // Who knows, maybe the parent class knows what to do with this slot.
            super.setInSlot(slot, content);
        }
    }

    @Override
    public void setUiHandlers(ConceptUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        conceptsTreeGrid.setUiHandlers(uiHandlers);
    }

    private void createViewForm() {
        // Identifiers Form
        identifiersForm = new GroupDynamicForm(getConstants().formIdentifiers());
        ViewTextItem code = new ViewTextItem(ConceptDS.CODE, getConstants().identifiableArtefactCode());
        ViewMultiLanguageTextItem name = new ViewMultiLanguageTextItem(ConceptDS.NAME, getConstants().nameableArtefactName());
        ViewMultiLanguageTextItem pluralName = new ViewMultiLanguageTextItem(ConceptDS.PLURAL_NAME, getConstants().conceptPluralName());
        ViewMultiLanguageTextItem acronym = new ViewMultiLanguageTextItem(ConceptDS.ACRONYM, getConstants().conceptAcronym());
        ViewTextItem uri = new ViewTextItem(ConceptDS.URI, getConstants().identifiableArtefactUri());
        ViewTextItem urn = new ViewTextItem(ConceptDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(ConceptDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        identifiersForm.setFields(code, name, pluralName, acronym, uri, urn, urnProvider);

        // Content descriptors
        contentDescriptorsForm = new GroupDynamicForm(getConstants().formContentDescriptors());
        ViewMultiLanguageTextItem description = new ViewMultiLanguageTextItem(ConceptDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        ViewMultiLanguageTextItem descriptionSource = new ViewMultiLanguageTextItem(ConceptDS.DESCRIPTION_SOURCE, getConstants().conceptDescriptionSource());
        ViewMultiLanguageTextItem context = new ViewMultiLanguageTextItem(ConceptDS.CONTEXT, getConstants().conceptContext());
        ViewMultiLanguageTextItem docMethod = new ViewMultiLanguageTextItem(ConceptDS.DOC_METHOD, getConstants().conceptDocMethod());
        ViewTextItem variable = new ViewTextItem(ConceptDS.VARIABLE_VIEW, getConstants().variable());
        variable.setShowIfCondition(getVariableFormItemIfFunction());
        ViewTextItem sdmxRelatedArtefact = new ViewTextItem(ConceptDS.SDMX_RELATED_ARTEFACT, getConstants().conceptSdmxRelatedArtefact());
        sdmxRelatedArtefact.setShowIfCondition(getSdmxRelatedArtefactFormItemIfFunction());
        ViewTextItem type = new ViewTextItem(ConceptDS.TYPE, getConstants().conceptType());
        RelatedResourceListItem roles = new RelatedResourceListItem(ConceptDS.ROLES, getConstants().conceptRoles(), false);
        roles.setShowIfCondition(getRolesFormItemIfFunction());
        ViewTextItem representation = new ViewTextItem(RepresentationDS.TYPE, getConstants().representation());
        representation.setShowIfCondition(FormItemUtils.getFalseFormItemIfFunction());
        ViewTextItem representationView = new ViewTextItem(RepresentationDS.TYPE_VIEW, getConstants().representation());
        ViewTextItem enumeratedRepresentation = new ViewTextItem(RepresentationDS.ENUMERATED_CODELIST, getConstants().codelist());
        enumeratedRepresentation.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return org.siemac.metamac.srm.web.dsd.utils.CommonUtils.isRepresentationTypeEnumerated(form.getValueAsString(RepresentationDS.TYPE));
            }
        });
        contentDescriptorsForm.setFields(description, descriptionSource, context, docMethod, variable, sdmxRelatedArtefact, type, roles, representation, representationView, enumeratedRepresentation);

        // Non enumerated representation
        facetForm = new StaticFacetForm();

        // Production descriptors
        classDescriptorsForm = new GroupDynamicForm(getConstants().formClassDescriptors());
        ViewMultiLanguageTextItem derivation = new ViewMultiLanguageTextItem(ConceptDS.DERIVATION, getConstants().conceptDerivation());
        classDescriptorsForm.setFields(derivation);

        // Relation between concepts
        relationBetweenConceptsForm = new GroupDynamicForm(getConstants().conceptRelationBetweenConcepts());
        ViewTextItem extendsConcept = new ViewTextItem(ConceptDS.EXTENDS_VIEW, getConstants().conceptExtends());
        ConceptsListItem relatedConcepts = new ConceptsListItem(ConceptDS.RELATED_CONCEPTS, getConstants().conceptRelatedConcepts(), false);
        relationBetweenConceptsForm.setFields(extendsConcept, relatedConcepts);

        // Legal acts
        legalActsForm = new GroupDynamicForm(getConstants().conceptLegalActs());
        ViewMultiLanguageTextItem legalActs = new ViewMultiLanguageTextItem(ConceptDS.LEGAL_ACTS, getConstants().conceptLegalActs());
        legalActsForm.setFields(legalActs);

        // Comments
        commentsForm = new GroupDynamicForm(getConstants().nameableArtefactComments());
        ViewMultiLanguageTextItem comments = new ViewMultiLanguageTextItem(ConceptDS.COMMENTS, getConstants().nameableArtefactComments());
        commentsForm.setFields(comments);

        // Annotations
        annotationsPanel = new AnnotationsPanel(true);

        mainFormLayout.addViewCanvas(identifiersForm);
        mainFormLayout.addViewCanvas(contentDescriptorsForm);
        mainFormLayout.addViewCanvas(facetForm);
        mainFormLayout.addViewCanvas(classDescriptorsForm);
        mainFormLayout.addViewCanvas(relationBetweenConceptsForm);
        mainFormLayout.addViewCanvas(legalActsForm);
        mainFormLayout.addViewCanvas(commentsForm);
        mainFormLayout.addViewCanvas(annotationsPanel);
    }

    private void createEditionForm() {
        // IDENTIFIERS FORM
        identifiersEditionForm = new GroupDynamicForm(getConstants().formIdentifiers());

        RequiredTextItem code = new RequiredTextItem(ConceptDS.CODE, getConstants().identifiableArtefactCode());
        code.setValidators(SemanticIdentifiersUtils.getConceptIdentifierCustomValidator());
        code.setShowIfCondition(getCodeFormItemIfFunction());

        ViewTextItem staticCode = new ViewTextItem(ConceptDS.CODE_VIEW, getConstants().identifiableArtefactCode());
        staticCode.setShowIfCondition(getStaticCodeFormItemIfFunction());

        MultiLanguageTextItem name = new MultiLanguageTextItem(ConceptDS.NAME, getConstants().nameableArtefactName());
        name.setRequired(true);
        MultiLanguageTextItem pluralName = new MultiLanguageTextItem(ConceptDS.PLURAL_NAME, getConstants().conceptPluralName());
        MultiLanguageTextItem acronym = new MultiLanguageTextItem(ConceptDS.ACRONYM, getConstants().conceptAcronym());
        ViewTextItem uri = new ViewTextItem(ConceptDS.URI, getConstants().identifiableArtefactUri());
        ViewTextItem urn = new ViewTextItem(ConceptDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(ConceptDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        identifiersEditionForm.setFields(code, staticCode, name, pluralName, acronym, uri, urn, urnProvider);

        // CONTENT DESCRIPTORS
        contentDescriptorsEditionForm = new GroupDynamicForm(getConstants().formContentDescriptors());
        MultiLanguageTextAreaItem description = new MultiLanguageTextAreaItem(ConceptDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        MultiLanguageTextItem descriptionSource = new MultiLanguageTextItem(ConceptDS.DESCRIPTION_SOURCE, getConstants().conceptDescriptionSource());
        MultiLanguageTextItem context = new MultiLanguageTextItem(ConceptDS.CONTEXT, getConstants().conceptContext());
        MultilanguageRichTextEditorItem docMethod = new MultilanguageRichTextEditorItem(ConceptDS.DOC_METHOD, getConstants().conceptDocMethod());
        ViewTextItem variable = new ViewTextItem(ConceptDS.VARIABLE, getConstants().variable());
        variable.setShowIfCondition(FormItemUtils.getFalseFormItemIfFunction());
        SearchViewTextItem variableView = createVariableItem(ConceptDS.VARIABLE_VIEW, getConstants().variable());
        variableView.setShowIfCondition(getVariableFormItemIfFunction());
        RequiredSelectItem sdmxRelatedArtefact = new RequiredSelectItem(ConceptDS.SDMX_RELATED_ARTEFACT, getConstants().conceptSdmxRelatedArtefact());
        sdmxRelatedArtefact.setValueMap(CommonUtils.getConceptRoleHashMap());
        sdmxRelatedArtefact.setShowIfCondition(getSdmxRelatedArtefactFormItemIfFunction());
        SelectItem type = new SelectItem(ConceptDS.TYPE, getConstants().conceptType()); // Value map set in setConceptTypes method
        RelatedResourceListItem roles = createRolesItem(ConceptDS.ROLES, getConstants().conceptRoles());
        roles.setShowIfCondition(getRolesFormItemIfFunction());

        // Representation type

        final CustomSelectItem representationType = new CustomSelectItem(RepresentationDS.TYPE, getConstants().representation());
        representationType.setValueMap(org.siemac.metamac.srm.web.client.utils.CommonUtils.getTypeRepresentationEnumHashMap());
        representationType.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                if (conceptSchemeMetamacDto != null) {
                    FacetFormUtils.setFacetFormVisibility(facetEditionForm, facetStaticEditionForm, representationType.getValueAsString(), conceptSchemeMetamacDto);
                }
                contentDescriptorsEditionForm.markForRedraw();
            }
        });
        representationType.setShowIfCondition(getRepresentationTypeFormItemIfFunction());

        ViewTextItem staticRepresentationType = new ViewTextItem(RepresentationDS.TYPE_VIEW, getConstants().representation());
        staticRepresentationType.setShowIfCondition(getStaticRepresentationTypeFormItemIfFunction());

        // Enumerated representation (Codelist)

        ViewTextItem codelist = new ViewTextItem(RepresentationDS.ENUMERATED_CODELIST, MetamacSrmWeb.getConstants().codelist());
        codelist.setShowIfCondition(FormItemUtils.getFalseFormItemIfFunction()); // This item is never shown. Stores the enumerated representation (codelist URN)

        SearchViewTextItem codelistEditionView = createEnumeratedRepresentationItem(RepresentationDS.ENUMERATED_CODELIST_EDITION_VIEW, MetamacSrmWeb.getConstants().codelist());
        codelistEditionView.setShowIfCondition(getEnumeratedRepresentationFormItemIfFunction()); // Shown in editionMode, only when the enumerated representation is editable

        ViewTextItem codelistView = new ViewTextItem(RepresentationDS.ENUMERATED_CODELIST_VIEW, MetamacSrmWeb.getConstants().codelist());
        codelistView.setShowIfCondition(getStaticEnumeratedRepresentationFormItemIfFunction()); // This item is shown when the enumerated representation can not be edited

        contentDescriptorsEditionForm.setFields(description, descriptionSource, context, docMethod, variable, variableView, sdmxRelatedArtefact, type, roles, representationType,
                staticRepresentationType, codelist, codelistEditionView, codelistView);

        // NON ENUMERATED REPRESENTATION
        facetEditionForm = new ConceptFacetForm();
        facetStaticEditionForm = new StaticFacetForm();

        // PRODUCTION DESCRIPTORS
        classDescriptorsEditionForm = new GroupDynamicForm(getConstants().formClassDescriptors());
        MultiLanguageTextItem derivation = new MultiLanguageTextItem(ConceptDS.DERIVATION, getConstants().conceptDerivation());
        classDescriptorsEditionForm.setFields(derivation);

        // RELATION BETWEEN CONCEPTS
        relationBetweenConceptsEditionForm = new GroupDynamicForm(getConstants().conceptRelationBetweenConcepts());
        ViewTextItem extendsConcept = new ViewTextItem(ConceptDS.EXTENDS, getConstants().conceptExtends());
        extendsConcept.setShowIfCondition(FormItemUtils.getFalseFormItemIfFunction());
        SearchViewTextItem extendsConceptView = createExtendsItem(ConceptDS.EXTENDS_VIEW, getConstants().conceptExtends());
        ConceptsListItem relatedConcepts = createRelatedConceptsItem(ConceptDS.RELATED_CONCEPTS, getConstants().conceptRelatedConcepts());
        relationBetweenConceptsEditionForm.setFields(extendsConcept, extendsConceptView, relatedConcepts);

        // LEGAL ACTS
        legalActsEditionForm = new GroupDynamicForm(getConstants().conceptLegalActs());
        MultilanguageRichTextEditorItem legalActs = new MultilanguageRichTextEditorItem(ConceptDS.LEGAL_ACTS, getConstants().conceptLegalActs());
        legalActsEditionForm.setFields(legalActs);

        // COMMENTS
        commentsEditionForm = new GroupDynamicForm(getConstants().nameableArtefactComments());
        MultiLanguageTextAreaItem comments = new MultiLanguageTextAreaItem(ConceptDS.COMMENTS, getConstants().nameableArtefactComments());
        commentsEditionForm.setFields(comments);

        // ANNOTATIONS
        annotationsEditionPanel = new AnnotationsPanel(false);

        mainFormLayout.addEditionCanvas(identifiersEditionForm);
        mainFormLayout.addEditionCanvas(contentDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(facetEditionForm);
        mainFormLayout.addEditionCanvas(facetStaticEditionForm);
        mainFormLayout.addEditionCanvas(classDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(relationBetweenConceptsEditionForm);
        mainFormLayout.addEditionCanvas(legalActsEditionForm);
        mainFormLayout.addEditionCanvas(commentsEditionForm);
        mainFormLayout.addEditionCanvas(annotationsEditionPanel);
    }

    @Override
    public void setConcept(ConceptMetamacDto conceptDto, List<RelatedResourceDto> roles, List<ConceptMetamacDto> relatedConcepts, ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        setConceptScheme(conceptSchemeMetamacDto);
        setConcept(conceptDto, roles, relatedConcepts);
    }

    @Override
    public void setConcept(ConceptMetamacDto conceptDto, List<RelatedResourceDto> roles, List<ConceptMetamacDto> relatedConcepts) {
        this.conceptDto = conceptDto;

        getUiHandlers().retrieveConceptsByScheme(conceptDto.getItemSchemeVersionUrn());

        // Load value lists
        getUiHandlers().retrieveConceptTypes();

        // Set title
        String defaultLocalized = InternationalStringUtils.getLocalisedString(conceptDto.getName());
        String title = defaultLocalized != null ? defaultLocalized : StringUtils.EMPTY;
        mainFormLayout.setTitleLabelContents(title);

        mainFormLayout.setViewMode();

        setConceptViewMode(conceptDto, roles, relatedConcepts);
        setConceptEditionMode(conceptDto, roles, relatedConcepts);

        markFormsForRedraw();
    }

    private void setConceptScheme(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        this.conceptSchemeMetamacDto = conceptSchemeMetamacDto;

        // Security
        mainFormLayout.setCanEdit(ConceptsClientSecurityUtils.canUpdateConcept(conceptSchemeMetamacDto.getLifeCycle().getProcStatus(), conceptSchemeMetamacDto.getType(),
                CommonUtils.getRelatedOperationCode(conceptSchemeMetamacDto)));

        markFormsForRedraw();

    }

    @Override
    public void setConceptList(ConceptSchemeMetamacDto conceptSchemeMetamacDto, List<ItemHierarchyDto> itemHierarchyDtos) {
        this.itemHierarchyDtos = itemHierarchyDtos;
        conceptsTreeGrid.setItems(conceptSchemeMetamacDto, itemHierarchyDtos);
        conceptsTreeGrid.selectItem(conceptDto.getUrn());
    }

    @Override
    public void setConceptTypes(List<ConceptTypeDto> conceptTypeDtos) {
        this.conceptTypeDtos = conceptTypeDtos;
        contentDescriptorsEditionForm.getItem(ConceptDS.TYPE).setValueMap(CommonUtils.getConceptTypeHashMap(conceptTypeDtos));
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

    private void setConceptViewMode(ConceptMetamacDto conceptDto, List<RelatedResourceDto> roles, List<ConceptMetamacDto> relatedConcepts) {
        // Identifiers Form
        identifiersForm.setValue(ConceptDS.CODE, conceptDto.getCode());
        identifiersForm.setValue(ConceptDS.NAME, RecordUtils.getInternationalStringRecord(conceptDto.getName()));
        identifiersForm.setValue(ConceptDS.PLURAL_NAME, RecordUtils.getInternationalStringRecord(conceptDto.getPluralName()));
        identifiersForm.setValue(ConceptDS.ACRONYM, RecordUtils.getInternationalStringRecord(conceptDto.getAcronym()));
        identifiersForm.setValue(ConceptDS.URI, conceptDto.getUriProvider());
        identifiersForm.setValue(ConceptDS.URN, conceptDto.getUrn());
        identifiersForm.setValue(ConceptDS.URN_PROVIDER, conceptDto.getUrnProvider());

        // Content descriptors
        contentDescriptorsForm.setValue(ConceptDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(conceptDto.getDescription()));
        contentDescriptorsForm.setValue(ConceptDS.DESCRIPTION_SOURCE, RecordUtils.getInternationalStringRecord(conceptDto.getDescriptionSource()));
        contentDescriptorsForm.setValue(ConceptDS.CONTEXT, RecordUtils.getInternationalStringRecord(conceptDto.getContext()));
        contentDescriptorsForm.setValue(ConceptDS.DOC_METHOD, RecordUtils.getInternationalStringRecord(conceptDto.getDocMethod()));
        contentDescriptorsForm.setValue(ConceptDS.VARIABLE_VIEW, org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourceName(conceptDto.getVariable()));
        contentDescriptorsForm.setValue(RepresentationDS.TYPE, conceptDto.getCoreRepresentation() != null ? conceptDto.getCoreRepresentation().getRepresentationType().name() : null);
        contentDescriptorsForm.setValue(RepresentationDS.TYPE_VIEW,
                conceptDto.getCoreRepresentation() != null
                        ? org.siemac.metamac.srm.web.client.utils.CommonUtils.getTypeRepresentationName(conceptDto.getCoreRepresentation().getRepresentationType())
                        : null);
        contentDescriptorsForm.setValue(RepresentationDS.ENUMERATED_CODELIST,
                conceptDto.getCoreRepresentation() != null ? RelatedResourceUtils.getRelatedResourceName(conceptDto.getCoreRepresentation().getEnumeration()) : null);
        contentDescriptorsForm.setValue(ConceptDS.SDMX_RELATED_ARTEFACT, CommonUtils.getConceptRoleName(conceptDto.getSdmxRelatedArtefact()));
        contentDescriptorsForm.setValue(ConceptDS.TYPE,
                conceptDto.getConceptType() != null ? CommonWebUtils.getElementName(conceptDto.getConceptType().getIdentifier(), conceptDto.getConceptType().getDescription()) : null);
        ((RelatedResourceListItem) contentDescriptorsForm.getItem(ConceptDS.ROLES)).setRelatedResources(roles);
        contentDescriptorsForm.markForRedraw();

        // Non enumerated representation
        if (conceptDto.getCoreRepresentation() != null && RepresentationTypeEnum.TEXT_FORMAT.equals(conceptDto.getCoreRepresentation().getRepresentationType())) {
            facetForm.show();
        } else {
            facetForm.hide();
        }
        facetForm.setFacet(conceptDto.getCoreRepresentation() != null ? conceptDto.getCoreRepresentation().getTextFormat() : null);
        facetForm.markForRedraw();

        // Class descriptors

        // Production descriptors
        classDescriptorsForm.setValue(ConceptDS.DERIVATION, RecordUtils.getInternationalStringRecord(conceptDto.getDerivation()));

        // Relation between concepts
        relationBetweenConceptsForm.setValue(ConceptDS.EXTENDS_VIEW, org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourceName(conceptDto.getConceptExtends()));
        ((ConceptsListItem) relationBetweenConceptsForm.getItem(ConceptDS.RELATED_CONCEPTS)).setDataConcepts(relatedConcepts);

        // Legal acts
        legalActsForm.setValue(ConceptDS.LEGAL_ACTS, RecordUtils.getInternationalStringRecord(conceptDto.getLegalActs()));

        // Comments
        commentsForm.setValue(ConceptDS.COMMENTS, RecordUtils.getInternationalStringRecord(conceptDto.getComment()));

        // Annotations
        annotationsPanel.setAnnotations(conceptDto.getAnnotations(), conceptSchemeMetamacDto);
    }

    private void setConceptEditionMode(ConceptMetamacDto conceptDto, List<RelatedResourceDto> roles, List<ConceptMetamacDto> relatedConcepts) {
        // IDENTIFIERS FORM
        identifiersEditionForm.setValue(ConceptDS.CODE, conceptDto.getCode());
        identifiersEditionForm.setValue(ConceptDS.CODE_VIEW, conceptDto.getCode());
        identifiersEditionForm.setValue(ConceptDS.NAME, RecordUtils.getInternationalStringRecord(conceptDto.getName()));
        identifiersEditionForm.setValue(ConceptDS.PLURAL_NAME, RecordUtils.getInternationalStringRecord(conceptDto.getPluralName()));
        identifiersEditionForm.setValue(ConceptDS.ACRONYM, RecordUtils.getInternationalStringRecord(conceptDto.getAcronym()));
        identifiersEditionForm.setValue(ConceptDS.URI, conceptDto.getUriProvider());
        identifiersEditionForm.setValue(ConceptDS.URN, conceptDto.getUrn());
        identifiersEditionForm.setValue(ConceptDS.URN_PROVIDER, conceptDto.getUrnProvider());

        // CONTENT DESCRIPTORS
        contentDescriptorsEditionForm.setValue(ConceptDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(conceptDto.getDescription()));
        contentDescriptorsEditionForm.setValue(ConceptDS.DESCRIPTION_SOURCE, RecordUtils.getInternationalStringRecord(conceptDto.getDescriptionSource()));
        contentDescriptorsEditionForm.setValue(ConceptDS.CONTEXT, RecordUtils.getInternationalStringRecord(conceptDto.getContext()));
        contentDescriptorsEditionForm.setValue(ConceptDS.DOC_METHOD, RecordUtils.getInternationalStringRecord(conceptDto.getDocMethod()));
        contentDescriptorsEditionForm.setValue(ConceptDS.VARIABLE_VIEW, org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourceName(conceptDto.getVariable()));
        contentDescriptorsEditionForm.setValue(ConceptDS.VARIABLE, conceptDto.getVariable() != null ? conceptDto.getVariable().getUrn() : StringUtils.EMPTY);
        contentDescriptorsEditionForm.setValue(RepresentationDS.TYPE, conceptDto.getCoreRepresentation() != null ? conceptDto.getCoreRepresentation().getRepresentationType().name() : null);
        contentDescriptorsEditionForm.setValue(RepresentationDS.TYPE_VIEW,
                conceptDto.getCoreRepresentation() != null
                        ? org.siemac.metamac.srm.web.client.utils.CommonUtils.getTypeRepresentationName(conceptDto.getCoreRepresentation().getRepresentationType())
                        : null);
        contentDescriptorsEditionForm.setValue(RepresentationDS.ENUMERATED_CODELIST, conceptDto.getCoreRepresentation() != null && conceptDto.getCoreRepresentation().getEnumeration() != null
                ? conceptDto.getCoreRepresentation().getEnumeration().getUrn()
                : null);
        contentDescriptorsEditionForm.setValue(RepresentationDS.ENUMERATED_CODELIST_EDITION_VIEW, conceptDto.getCoreRepresentation() != null
                && conceptDto.getCoreRepresentation().getEnumeration() != null ? RelatedResourceUtils.getRelatedResourceName(conceptDto.getCoreRepresentation().getEnumeration()) : null);
        contentDescriptorsEditionForm.setValue(RepresentationDS.ENUMERATED_CODELIST_VIEW,
                conceptDto.getCoreRepresentation() != null ? RelatedResourceUtils.getRelatedResourceName(conceptDto.getCoreRepresentation().getEnumeration()) : null);
        contentDescriptorsEditionForm.setValue(ConceptDS.SDMX_RELATED_ARTEFACT, conceptDto.getSdmxRelatedArtefact() != null ? conceptDto.getSdmxRelatedArtefact().name() : StringUtils.EMPTY);
        contentDescriptorsEditionForm.setValue(ConceptDS.TYPE, conceptDto.getConceptType() != null ? conceptDto.getConceptType().getIdentifier() : null);
        ((RelatedResourceListItem) contentDescriptorsEditionForm.getItem(ConceptDS.ROLES)).setRelatedResources(roles);

        // FACET (Non enumerated representation)
        facetEditionForm.setFacet(conceptDto.getCoreRepresentation() != null ? conceptDto.getCoreRepresentation().getTextFormat() : null);
        facetStaticEditionForm.setFacet(conceptDto.getCoreRepresentation() != null ? conceptDto.getCoreRepresentation().getTextFormat() : null);
        if (conceptSchemeMetamacDto != null) {
            FacetFormUtils.setFacetFormVisibility(facetEditionForm, facetStaticEditionForm, contentDescriptorsEditionForm.getValueAsString(RepresentationDS.TYPE), conceptSchemeMetamacDto);
        }

        // CLASS DESCRIPTORS

        // PRODUCTION DESCRIPTORS
        classDescriptorsEditionForm.setValue(ConceptDS.DERIVATION, RecordUtils.getInternationalStringRecord(conceptDto.getDerivation()));

        // RELATION BETWEEN CONCEPTS
        relationBetweenConceptsEditionForm.setValue(ConceptDS.EXTENDS_VIEW, org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourceName(conceptDto.getConceptExtends()));
        relationBetweenConceptsEditionForm.setValue(ConceptDS.EXTENDS, conceptDto.getConceptExtends() != null ? conceptDto.getConceptExtends().getUrn() : StringUtils.EMPTY);

        ((ConceptsListItem) relationBetweenConceptsEditionForm.getItem(ConceptDS.RELATED_CONCEPTS)).setDataConcepts(relatedConcepts);

        // LEGAL ACTS
        legalActsEditionForm.setValue(ConceptDS.LEGAL_ACTS, RecordUtils.getInternationalStringRecord(conceptDto.getLegalActs()));

        // COMMENTS
        commentsEditionForm.setValue(ConceptDS.COMMENTS, RecordUtils.getInternationalStringRecord(conceptDto.getComment()));

        // ANNOTATIONS
        annotationsEditionPanel.setAnnotations(conceptDto.getAnnotations(), conceptSchemeMetamacDto);
    }

    private ConceptMetamacDto getConceptDto() {
        // Identifiers Form
        conceptDto.setCode(identifiersEditionForm.getValueAsString(ConceptDS.CODE));
        conceptDto.setName((InternationalStringDto) identifiersEditionForm.getValue(ConceptDS.NAME));
        conceptDto.setPluralName((InternationalStringDto) identifiersEditionForm.getValue(ConceptDS.PLURAL_NAME));
        conceptDto.setAcronym((InternationalStringDto) identifiersEditionForm.getValue(ConceptDS.ACRONYM));

        // Content descriptors
        conceptDto.setDescription((InternationalStringDto) contentDescriptorsEditionForm.getValue(ConceptDS.DESCRIPTION));
        conceptDto.setDescriptionSource((InternationalStringDto) contentDescriptorsEditionForm.getValue(ConceptDS.DESCRIPTION_SOURCE));
        conceptDto.setContext((InternationalStringDto) contentDescriptorsEditionForm.getValue(ConceptDS.CONTEXT));
        conceptDto.setDocMethod((InternationalStringDto) contentDescriptorsEditionForm.getValue(ConceptDS.DOC_METHOD));
        conceptDto.setVariable(RelatedResourceUtils.createRelatedResourceDto(contentDescriptorsEditionForm.getValueAsString(ConceptDS.VARIABLE)));
        if (!StringUtils.isEmpty(contentDescriptorsEditionForm.getValueAsString(RepresentationDS.TYPE))) {
            if (conceptDto.getCoreRepresentation() == null) {
                conceptDto.setCoreRepresentation(new RepresentationDto());
            }
            conceptDto.getCoreRepresentation().setRepresentationType(
                    !StringUtils.isEmpty(contentDescriptorsEditionForm.getValueAsString(RepresentationDS.TYPE)) ? RepresentationTypeEnum.valueOf(contentDescriptorsEditionForm
                            .getValueAsString(RepresentationDS.TYPE)) : null);
            if (RepresentationTypeEnum.ENUMERATION.equals(conceptDto.getCoreRepresentation().getRepresentationType())) {
                conceptDto.getCoreRepresentation().setTextFormat(null);
                conceptDto.getCoreRepresentation().setEnumeration(
                        StringUtils.isBlank(contentDescriptorsEditionForm.getValueAsString(RepresentationDS.ENUMERATED_CODELIST)) ? null : RelatedResourceUtils.createRelatedResourceDto(
                                RelatedResourceTypeEnum.CODELIST, contentDescriptorsEditionForm.getValueAsString(RepresentationDS.ENUMERATED_CODELIST)));
            } else if (RepresentationTypeEnum.TEXT_FORMAT.equals(conceptDto.getCoreRepresentation().getRepresentationType())) {
                conceptDto.getCoreRepresentation().setEnumeration(null);
                conceptDto.getCoreRepresentation().setTextFormat(facetEditionForm.getFacet());
            }
        } else {
            conceptDto.setCoreRepresentation(null);
        }
        conceptDto.setSdmxRelatedArtefact(!StringUtils.isBlank(contentDescriptorsEditionForm.getValueAsString(ConceptDS.SDMX_RELATED_ARTEFACT)) ? ConceptRoleEnum.valueOf(contentDescriptorsEditionForm
                .getValueAsString(ConceptDS.SDMX_RELATED_ARTEFACT)) : null);
        conceptDto.setConceptType(contentDescriptorsEditionForm.getValue(ConceptDS.TYPE) != null ? getConceptTypeDto(contentDescriptorsEditionForm.getValueAsString(ConceptDS.TYPE)) : null);
        // Roles get in getRoles method

        // Class descriptors

        // Production descriptors
        conceptDto.setDerivation((InternationalStringDto) classDescriptorsEditionForm.getValue(ConceptDS.DERIVATION));

        // Relation between concepts
        conceptDto.setConceptExtends(StringUtils.isBlank(relationBetweenConceptsEditionForm.getValueAsString(ConceptDS.EXTENDS)) ? null : RelatedResourceUtils.createRelatedResourceDto(
                RelatedResourceTypeEnum.CONCEPT, relationBetweenConceptsEditionForm.getValueAsString(ConceptDS.EXTENDS)));
        // Related concepts get in getRelatedConcepts method

        // Legal acts
        conceptDto.setLegalActs((InternationalStringDto) legalActsEditionForm.getValue(ConceptDS.LEGAL_ACTS));

        // Comments
        conceptDto.setComment((InternationalStringDto) commentsEditionForm.getValue(ConceptDS.COMMENTS));

        // Annotations
        conceptDto.getAnnotations().clear();
        conceptDto.getAnnotations().addAll(annotationsEditionPanel.getAnnotations());

        return conceptDto;
    }

    @Override
    public void setConceptThatCanBeRoles(List<RelatedResourceDto> conceptDtos, int firstResult, int totalResults) {
        if (searchRolesWindow != null) {
            searchRolesWindow.setSourceRelatedResources(conceptDtos);
            searchRolesWindow.refreshSourcePaginationInfo(firstResult, conceptDtos.size(), totalResults);
        }
    }

    @Override
    public void setConceptThatCanBeExtended(List<RelatedResourceDto> conceptDtos, int firstResult, int totalResults) {
        if (searchExtendsWindow != null) {
            searchExtendsWindow.setRelatedResources(conceptDtos);
            searchExtendsWindow.refreshSourcePaginationInfo(firstResult, conceptDtos.size(), totalResults);
        }
    }

    @Override
    public void setConceptSchemesWithConceptsThatCanBeRole(List<RelatedResourceDto> conceptSchemes) {
        if (searchRolesWindow != null) {
            searchRolesWindow.getInitialSelectionItem().setValueMap(org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourceHashMap(conceptSchemes));
        }
    }

    @Override
    public void setConceptSchemesWithConceptsThatCanBeExtended(List<RelatedResourceDto> conceptSchemes) {
        if (searchExtendsWindow != null) {
            searchExtendsWindow.getInitialSelectionItem().setValueMap(org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourceHashMap(conceptSchemes));
        }
    }

    @Override
    public void setVariableFamilies(GetVariableFamiliesResult result) {
        if (searchVariableWindow != null) {
            List<RelatedResourceDto> families = RelatedResourceUtils.getVariableFamilyDtosAsRelatedResourceDtos(result.getFamilies());
            searchVariableWindow.getInitialSelectionItem().setValueMap(org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourceHashMap(families));
        }
    }

    @Override
    public void setVariables(GetVariablesResult result) {
        if (searchVariableWindow != null) {
            searchVariableWindow.setRelatedResources(RelatedResourceUtils.getVariableDtosAsRelatedResourceDtos(result.getVariables()));
            searchVariableWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getVariables().size(), result.getTotalResults());
        }
    }

    private List<String> getRelatedConcepts() {
        return ((ConceptsListItem) relationBetweenConceptsEditionForm.getItem(ConceptDS.RELATED_CONCEPTS)).getConceptUrns();
    }

    private List<String> getRoles() {
        return ((RelatedResourceListItem) contentDescriptorsEditionForm.getItem(ConceptDS.ROLES)).getSelectedRelatedResourceUrns();
    }

    private RelatedResourceListItem createRolesItem(String name, String title) {
        final int FIRST_RESULT = 0;
        final int MAX_RESULTS = 8;

        RelatedResourceListItem rolesItem = new RelatedResourceListItem(name, title, true);
        rolesItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent arg0) {
                searchRolesWindow = new SearchMultipleRelatedResourcePaginatedWindow(getConstants().conceptSelection(), MAX_RESULTS,
                        new SelectItem(ConceptSchemeDS.URN, getConstants().conceptScheme()), new PaginatedAction() {

                            @Override
                            public void retrieveResultSet(int firstResult, int maxResults) {
                                getUiHandlers().retrieveConceptsThatCanBeRole(firstResult, maxResults, searchRolesWindow.getRelatedResourceCriteria(), searchRolesWindow.getInitialSelectionValue());
                            }
                        });

                // Load the list of concepts and concept schemes that can be roles
                getUiHandlers().retrieveConceptsThatCanBeRole(FIRST_RESULT, MAX_RESULTS, null, null);
                getUiHandlers().retrieveConceptSchemesWithConceptsThatCanBeRole(FIRST_RESULT, SrmWebConstants.NO_LIMIT_IN_PAGINATION);

                searchRolesWindow.getInitialSelectionItem().addChangedHandler(new ChangedHandler() {

                    @Override
                    public void onChanged(ChangedEvent event) {
                        getUiHandlers().retrieveConceptsThatCanBeRole(FIRST_RESULT, MAX_RESULTS, searchRolesWindow.getRelatedResourceCriteria(), searchRolesWindow.getInitialSelectionValue());
                    }
                });

                // Set the selected concepts
                List<RelatedResourceDto> selectedRoles = ((RelatedResourceListItem) contentDescriptorsEditionForm.getItem(ConceptDS.ROLES)).getRelatedResourceDtos();
                searchRolesWindow.setTargetRelatedResources(selectedRoles);

                searchRolesWindow.setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String concept) {
                        getUiHandlers().retrieveConceptsThatCanBeRole(firstResult, maxResults, concept, searchRolesWindow.getInitialSelectionValue());
                    }
                });
                searchRolesWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent arg0) {
                        List<RelatedResourceDto> conceptDtos = searchRolesWindow.getSelectedRelatedResources();
                        searchRolesWindow.markForDestroy();
                        // Set selected concepts in form
                        ((RelatedResourceListItem) contentDescriptorsEditionForm.getItem(ConceptDS.ROLES)).setRelatedResources(conceptDtos);
                    }
                });
            }
        });
        return rolesItem;
    }

    private SearchViewTextItem createExtendsItem(String name, String title) {
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;
        SearchViewTextItem extendsItem = new SearchViewTextItem(name, title);
        extendsItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {

                searchExtendsWindow = new SearchRelatedResourcePaginatedWindow(getConstants().conceptSelection(), MAX_RESULTS, new SelectItem(ConceptSchemeDS.URN, getConstants().conceptScheme()),
                        new PaginatedAction() {

                            @Override
                            public void retrieveResultSet(int firstResult, int maxResults) {
                                getUiHandlers().retrieveConceptsThatCanBeExtended(firstResult, maxResults, searchExtendsWindow.getRelatedResourceCriteria(),
                                        searchExtendsWindow.getInitialSelectionValue());
                            }
                        });

                // Load concepts and concept scheme that can be extended (to populate the selection window)
                getUiHandlers().retrieveConceptsThatCanBeExtended(FIRST_RESULST, MAX_RESULTS, null, null);
                getUiHandlers().retrieveConceptSchemesWithConceptsThatCanBeExtended(FIRST_RESULST, SrmWebConstants.NO_LIMIT_IN_PAGINATION);

                searchExtendsWindow.getInitialSelectionItem().addChangedHandler(new ChangedHandler() {

                    @Override
                    public void onChanged(ChangedEvent event) {
                        getUiHandlers().retrieveConceptsThatCanBeExtended(FIRST_RESULST, MAX_RESULTS, searchExtendsWindow.getRelatedResourceCriteria(), searchExtendsWindow.getInitialSelectionValue());
                    }
                });

                searchExtendsWindow.getListGridItem().getListGrid().setSelectionType(SelectionStyle.SINGLE);
                searchExtendsWindow.getListGridItem().setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String concept) {
                        getUiHandlers().retrieveConceptsThatCanBeExtended(firstResult, maxResults, concept, searchExtendsWindow.getInitialSelectionValue());
                    }
                });
                searchExtendsWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent arg0) {
                        RelatedResourceDto selectedConcept = searchExtendsWindow.getSelectedRelatedResource();
                        searchExtendsWindow.markForDestroy();
                        // Set selected concepts in form
                        relationBetweenConceptsEditionForm.setValue(ConceptDS.EXTENDS, selectedConcept != null ? selectedConcept.getUrn() : null);
                        relationBetweenConceptsEditionForm.setValue(ConceptDS.EXTENDS_VIEW,
                                selectedConcept != null ? org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourceName(selectedConcept) : null);
                    }
                });
            }
        });
        return extendsItem;
    }

    private ConceptsListItem createRelatedConceptsItem(String name, String title) {
        ConceptsListItem relatedConceptsItem = new ConceptsListItem(name, title, true);
        relatedConceptsItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                final ConceptsTreeWindow extendedConceptWindow = new ConceptsTreeWindow(getConstants().conceptSelection());
                extendedConceptWindow.setConcepts(conceptSchemeMetamacDto, itemHierarchyDtos);

                // Disable selection of current concept and concept scheme
                RecordList records = extendedConceptWindow.getConceptsTreeGrid().getDataAsRecordList();
                if (records != null && !records.isEmpty()) {
                    Record conceptSchemeRecord = records.find(ConceptDS.CODE, conceptSchemeMetamacDto.getCode());
                    Record currentConceptRecord = records.find(ConceptDS.URN, conceptDto.getUrn());
                    int conceptSchemeRecordIndex = extendedConceptWindow.getConceptsTreeGrid().getRecordIndex(conceptSchemeRecord);
                    int currentConceptRecordIndex = extendedConceptWindow.getConceptsTreeGrid().getRecordIndex(currentConceptRecord);
                    extendedConceptWindow.getConceptsTreeGrid().getRecord(conceptSchemeRecordIndex).setEnabled(false);
                    extendedConceptWindow.getConceptsTreeGrid().getRecord(currentConceptRecordIndex).setEnabled(false);
                }

                // Select related concepts
                List<String> selectedRelatedConcepts = ((ConceptsListItem) relationBetweenConceptsEditionForm.getItem(ConceptDS.RELATED_CONCEPTS)).getConceptUrns();
                extendedConceptWindow.selectConcepts(selectedRelatedConcepts);

                extendedConceptWindow.getSaveClickHandlers().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        List<ItemDto> itemDtos = extendedConceptWindow.getSelectedConcepts();
                        extendedConceptWindow.markForDestroy();
                        // Set selected concepts in form
                        ((ConceptsListItem) relationBetweenConceptsEditionForm.getItem(ConceptDS.RELATED_CONCEPTS)).setDataItems(itemDtos);
                    }
                });
            }
        });
        return relatedConceptsItem;
    }

    private boolean validateEditionForms() {
        return identifiersEditionForm.validate(false) && contentDescriptorsEditionForm.validate(false) && (facetEditionForm.isVisible() ? facetEditionForm.validate(false) : true)
                && contentDescriptorsEditionForm.validate(false) && classDescriptorsEditionForm.validate(false) && relationBetweenConceptsEditionForm.validate(false)
                && legalActsEditionForm.validate(false);
    }

    private FormItemIfFunction getSdmxRelatedArtefactFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (conceptSchemeMetamacDto != null) {
                    return CommonUtils.isMetadataSdmxRelatedArtefactVisible(conceptSchemeMetamacDto.getType());
                }
                return false;
            }
        };
    }

    private FormItemIfFunction getRolesFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem arg0, Object arg1, DynamicForm arg2) {
                if (conceptSchemeMetamacDto != null) {
                    return CommonUtils.isMetadataRolesVisible(conceptSchemeMetamacDto.getType());
                }
                return false;
            }
        };
    }

    private FormItemIfFunction getVariableFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem arg0, Object arg1, DynamicForm arg2) {
                if (conceptSchemeMetamacDto != null) {
                    return CommonUtils.isMetadataVariableVisible(conceptSchemeMetamacDto.getType());
                }
                return false;
            }
        };
    }

    private ConceptTypeDto getConceptTypeDto(String identifier) {
        for (ConceptTypeDto conceptTypeDto : conceptTypeDtos) {
            if (StringUtils.equals(identifier, conceptTypeDto.getIdentifier())) {
                return conceptTypeDto;
            }
        }
        return null;
    }

    private SearchViewTextItem createVariableItem(String name, String title) {
        final int FIRST_RESULT = 0;
        final int MAX_RESULTS = 8;

        SearchViewTextItem variableItem = new SearchViewTextItem(name, title);
        variableItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                SelectItem variableFamilySelectItem = new SelectItem(VariableFamilyDS.URN, getConstants().variableFamily());
                searchVariableWindow = new SearchRelatedResourcePaginatedWindow(getConstants().variableSelection(), MAX_RESULTS, variableFamilySelectItem, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        getUiHandlers().retrieveVariables(FIRST_RESULT, MAX_RESULTS, searchVariableWindow.getRelatedResourceCriteria(), searchVariableWindow.getInitialSelectionValue());
                    }
                });

                // Load variables (to populate the selection window)
                getUiHandlers().retrieveVariableFamilies(FIRST_RESULT, SrmWebConstants.NO_LIMIT_IN_PAGINATION, null);
                getUiHandlers().retrieveVariables(FIRST_RESULT, MAX_RESULTS, null, null);

                searchVariableWindow.getInitialSelectionItem().addChangedHandler(new ChangedHandler() {

                    @Override
                    public void onChanged(ChangedEvent event) {
                        getUiHandlers().retrieveVariables(FIRST_RESULT, MAX_RESULTS, searchVariableWindow.getRelatedResourceCriteria(), searchVariableWindow.getInitialSelectionValue());
                    }
                });

                searchVariableWindow.getListGridItem().getListGrid().setSelectionType(SelectionStyle.SINGLE);
                searchVariableWindow.getListGridItem().setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                        getUiHandlers().retrieveVariables(firstResult, maxResults, criteria, searchVariableWindow.getInitialSelectionValue());
                    }
                });

                searchVariableWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent arg0) {
                        RelatedResourceDto selectedVariable = searchVariableWindow.getSelectedRelatedResource();
                        searchVariableWindow.markForDestroy();

                        // Set selected family in form
                        contentDescriptorsEditionForm.setValue(ConceptDS.VARIABLE, selectedVariable != null ? selectedVariable.getUrn() : null);
                        contentDescriptorsEditionForm.setValue(ConceptDS.VARIABLE_VIEW,
                                selectedVariable != null ? org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourceName(selectedVariable) : null);

                        // Reset the codelist associated with the enumerated representation of the concept (the codelist depends on the variable selected)
                        contentDescriptorsEditionForm.setValue(RepresentationDS.ENUMERATED_CODELIST, StringUtils.EMPTY);
                        contentDescriptorsEditionForm.setValue(RepresentationDS.ENUMERATED_CODELIST_EDITION_VIEW, StringUtils.EMPTY);
                    }
                });
            }
        });
        return variableItem;
    };

    private SearchViewTextItem createEnumeratedRepresentationItem(String name, String title) {
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;
        final SearchViewTextItem codelistItem = new SearchViewTextItem(name, title);
        codelistItem.setRequired(true);
        codelistItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {

                final String conceptUrn = conceptDto.getUrn();

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
                    searchCodelistForEnumeratedRepresentationWindow.setInfoMessage(getConstants().conceptEnumeratedRepresentationInfoMessage());
                    searchCodelistForEnumeratedRepresentationWindow.showInfoMessage();

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
                            contentDescriptorsEditionForm.setValue(RepresentationDS.ENUMERATED_CODELIST, selectedCodelist != null ? selectedCodelist.getUrn() : null);
                            contentDescriptorsEditionForm.setValue(RepresentationDS.ENUMERATED_CODELIST_EDITION_VIEW, selectedCodelist != null
                                    ? org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourceName(selectedCodelist)
                                    : null);
                            contentDescriptorsEditionForm.validate(false);
                        }
                    });
                }
            }
        });
        return codelistItem;
    }

    private void markFormsForRedraw() {
        identifiersForm.markForRedraw();
        identifiersEditionForm.markForRedraw();

        contentDescriptorsForm.markForRedraw();
        contentDescriptorsEditionForm.markForRedraw();

        classDescriptorsForm.markForRedraw();
        classDescriptorsEditionForm.markForRedraw();

        relationBetweenConceptsForm.markForRedraw();
        relationBetweenConceptsEditionForm.markForRedraw();

        legalActsForm.markForRedraw();
        legalActsEditionForm.markForRedraw();

        commentsForm.markForRedraw();
        commentsEditionForm.markForRedraw();

        annotationsPanel.markForRedraw();
        annotationsEditionPanel.markForRedraw();

        if (conceptSchemeMetamacDto != null) {
            FacetFormUtils.setFacetFormVisibility(facetEditionForm, facetStaticEditionForm, contentDescriptorsEditionForm.getValueAsString(RepresentationDS.TYPE), conceptSchemeMetamacDto);
        }
    }

    // ------------------------------------------------------------------------------------------------------------
    // FORM ITEM IF FUNCTIONS
    // ------------------------------------------------------------------------------------------------------------

    // CODE

    private FormItemIfFunction getCodeFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return ConceptsFormUtils.canConceptCodeBeEdited(conceptSchemeMetamacDto);
            }
        };
    }

    private FormItemIfFunction getStaticCodeFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !ConceptsFormUtils.canConceptCodeBeEdited(conceptSchemeMetamacDto);
            }
        };
    }

    // REPRESENTATION TYPE

    private FormItemIfFunction getRepresentationTypeFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return ConceptsFormUtils.canConceptRepresentationTypeBeEdited(conceptSchemeMetamacDto);
            }
        };
    }

    private FormItemIfFunction getStaticRepresentationTypeFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !ConceptsFormUtils.canConceptRepresentationTypeBeEdited(conceptSchemeMetamacDto);
            }
        };
    }

    // ENUMERATED REPRESENTATION

    private FormItemIfFunction getEnumeratedRepresentationFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                // Shown when the representation type selected is ENUMERATION and the enumerated representation can be edited
                return org.siemac.metamac.srm.web.dsd.utils.CommonUtils.isRepresentationTypeEnumerated(contentDescriptorsEditionForm.getValueAsString(RepresentationDS.TYPE))
                        && ConceptsFormUtils.canConceptEnumeratedRepresentationBeEdited(conceptSchemeMetamacDto);
            }
        };
    }

    private FormItemIfFunction getStaticEnumeratedRepresentationFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                // Shown when the representation type selected is ENUMERATION and the enumerated representation can NOT be edited
                return org.siemac.metamac.srm.web.dsd.utils.CommonUtils.isRepresentationTypeEnumerated(contentDescriptorsEditionForm.getValueAsString(RepresentationDS.TYPE))
                        && !ConceptsFormUtils.canConceptEnumeratedRepresentationBeEdited(conceptSchemeMetamacDto);
            }
        };
    }
}
