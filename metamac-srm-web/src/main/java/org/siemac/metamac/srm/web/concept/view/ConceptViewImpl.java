package org.siemac.metamac.srm.web.concept.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.concept.domain.shared.ConceptMetamacVisualisationResult;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacBasicDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptTypeDto;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptRoleEnum;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.constants.SrmWebConstants;
import org.siemac.metamac.srm.web.client.model.ds.RepresentationDS;
import org.siemac.metamac.srm.web.client.representation.widgets.StaticFacetForm;
import org.siemac.metamac.srm.web.client.utils.FacetFormUtils;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.client.widgets.AnnotationsPanel;
import org.siemac.metamac.srm.web.client.widgets.CustomVLayout;
import org.siemac.metamac.srm.web.client.widgets.RelatedResourceLinkItem;
import org.siemac.metamac.srm.web.client.widgets.RelatedResourceListItem;
import org.siemac.metamac.srm.web.client.widgets.SearchMultipleRelatedResourcePaginatedWindow;
import org.siemac.metamac.srm.web.client.widgets.SearchRelatedResourceLinkItem;
import org.siemac.metamac.srm.web.client.widgets.SearchRelatedResourcePaginatedWindow;
import org.siemac.metamac.srm.web.code.model.ds.CodelistDS;
import org.siemac.metamac.srm.web.code.model.ds.VariableFamilyDS;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeDS;
import org.siemac.metamac.srm.web.concept.presenter.ConceptPresenter;
import org.siemac.metamac.srm.web.concept.utils.CommonUtils;
import org.siemac.metamac.srm.web.concept.utils.ConceptsFormUtils;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptUiHandlers;
import org.siemac.metamac.srm.web.concept.widgets.ConceptFacetForm;
import org.siemac.metamac.srm.web.concept.widgets.ConceptMainFormLayout;
import org.siemac.metamac.srm.web.concept.widgets.ConceptsListItem;
import org.siemac.metamac.srm.web.concept.widgets.ConceptsTreeGrid;
import org.siemac.metamac.srm.web.concept.widgets.ConceptsTreeWindow;
import org.siemac.metamac.srm.web.concept.widgets.QuantityForm;
import org.siemac.metamac.srm.web.concept.widgets.ViewQuantityForm;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.srm.web.shared.code.GetVariableFamiliesResult;
import org.siemac.metamac.srm.web.shared.code.GetVariablesResult;
import org.siemac.metamac.srm.web.shared.criteria.ConceptSchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptWebCriteria;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;
import org.siemac.metamac.web.common.client.widgets.CustomSectionStack;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultilanguageRichTextEditorItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;
import org.siemac.metamac.web.common.client.widgets.handlers.CustomLinkItemNavigationClickHandler;
import org.siemac.metamac.web.common.client.widgets.handlers.ListRecordNavigationClickHandler;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
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
import com.smartgwt.client.widgets.form.validator.CustomValidator;
import com.smartgwt.client.widgets.layout.VLayout;

public class ConceptViewImpl extends ViewWithUiHandlers<ConceptUiHandlers> implements ConceptPresenter.ConceptView {

    private VLayout                                      panel;
    private ConceptMainFormLayout                        mainFormLayout;

    private ConceptsTreeGrid                             conceptsTreeGrid;

    // View forms
    private GroupDynamicForm                             identifiersForm;
    private GroupDynamicForm                             productionDescriptorsForm;
    private GroupDynamicForm                             contentDescriptorsForm;
    private StaticFacetForm                              facetForm;
    private GroupDynamicForm                             classDescriptorsForm;
    private GroupDynamicForm                             relationBetweenConceptsForm;
    private ViewQuantityForm                             quantityForm;
    private GroupDynamicForm                             legalActsForm;
    private GroupDynamicForm                             commentsForm;
    private AnnotationsPanel                             annotationsPanel;

    // Edition forms
    private GroupDynamicForm                             identifiersEditionForm;
    private GroupDynamicForm                             productionDescriptorsEditionForm;
    private GroupDynamicForm                             contentDescriptorsEditionForm;
    private ConceptFacetForm                             facetEditionForm;
    private StaticFacetForm                              facetStaticEditionForm;
    private GroupDynamicForm                             classDescriptorsEditionForm;
    private GroupDynamicForm                             relationBetweenConceptsEditionForm;
    private QuantityForm                                 quantityEditionForm;
    private GroupDynamicForm                             legalActsEditionForm;
    private GroupDynamicForm                             commentsEditionForm;
    private AnnotationsPanel                             annotationsEditionPanel;

    private List<ConceptTypeDto>                         conceptTypeDtos;

    private ConceptSchemeMetamacDto                      conceptSchemeMetamacDto;
    private List<ConceptMetamacVisualisationResult>      itemVisualisationResults;
    private ConceptMetamacDto                            conceptDto;

    private SearchMultipleRelatedResourcePaginatedWindow searchRolesWindow;
    private SearchRelatedResourcePaginatedWindow         searchExtendsWindow;
    private SearchRelatedResourcePaginatedWindow         searchVariableWindow;
    private SearchRelatedResourcePaginatedWindow         searchCodelistOrConceptSchemesForEnumeratedRepresentationWindow;

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
        CustomSectionStack sectionStack = new CustomSectionStack(getConstants().conceptSchemeConcepts());
        sectionStack.getDefaultSection().setItems(conceptsTreeGrid);
        conceptsListGridLayout.addMember(sectionStack);

        //
        // CONCEPT
        //

        mainFormLayout = new ConceptMainFormLayout();

        bindMainFormLayoutEvents();

        createViewForm();
        createEditionForm();

        VLayout subPanel = new VLayout();
        subPanel.setOverflow(Overflow.SCROLL);
        subPanel.addMember(conceptsListGridLayout);
        subPanel.addMember(mainFormLayout);

        panel.addMember(subPanel);
    }

    private void bindMainFormLayoutEvents() {
        // Translations
        mainFormLayout.getTranslateToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                boolean translationsShowed = mainFormLayout.getTranslateToolStripButton().isSelected();
                identifiersForm.setTranslationsShowed(translationsShowed);
                identifiersEditionForm.setTranslationsShowed(translationsShowed);

                productionDescriptorsForm.setTranslationsShowed(translationsShowed);
                productionDescriptorsEditionForm.setTranslationsShowed(translationsShowed);

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

        mainFormLayout.getDeleteConfirmationWindow().getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deleteConcept(conceptDto);
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
        quantityForm.setUiHandlers(uiHandlers);
        quantityEditionForm.setUiHandlers(uiHandlers);
    }

    private void createViewForm() {
        // Identifiers Form
        identifiersForm = new GroupDynamicForm(getConstants().formIdentifiers());
        ViewTextItem code = new ViewTextItem(ConceptDS.CODE, getConstants().identifiableArtefactCode());
        ViewMultiLanguageTextItem name = new ViewMultiLanguageTextItem(ConceptDS.NAME, getConstants().nameableArtefactName());
        ViewMultiLanguageTextItem pluralName = new ViewMultiLanguageTextItem(ConceptDS.PLURAL_NAME, getConstants().conceptPluralName());
        ViewMultiLanguageTextItem acronym = new ViewMultiLanguageTextItem(ConceptDS.ACRONYM, getConstants().conceptAcronym());
        ViewTextItem uriProvider = new ViewTextItem(ConceptDS.URI, getConstants().identifiableArtefactUriProvider());
        ViewTextItem urn = new ViewTextItem(ConceptDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(ConceptDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        identifiersForm.setFields(code, name, pluralName, acronym, uriProvider, urn, urnProvider);

        // Production descriptors
        productionDescriptorsForm = new GroupDynamicForm(getConstants().formProductionDescriptors());
        ViewTextItem creationDate = new ViewTextItem(ConceptDS.CREATION_DATE, getConstants().identifiableArtefactCreationDate());
        productionDescriptorsForm.setFields(creationDate);

        // Content descriptors
        contentDescriptorsForm = new GroupDynamicForm(getConstants().formContentDescriptors());
        ViewMultiLanguageTextItem description = new ViewMultiLanguageTextItem(ConceptDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        ViewMultiLanguageTextItem descriptionSource = new ViewMultiLanguageTextItem(ConceptDS.DESCRIPTION_SOURCE, getConstants().conceptDescriptionSource());
        ViewMultiLanguageTextItem context = new ViewMultiLanguageTextItem(ConceptDS.CONTEXT, getConstants().conceptContext());
        ViewMultiLanguageTextItem docMethod = new ViewMultiLanguageTextItem(ConceptDS.DOC_METHOD, getConstants().conceptDocMethod());
        RelatedResourceLinkItem variable = new RelatedResourceLinkItem(ConceptDS.VARIABLE, getConstants().variable(), getCustomLinkItemNavigationClickHandler());
        variable.setShowIfCondition(getVariableViewFormItemIfFunction());
        ViewTextItem type = new ViewTextItem(ConceptDS.TYPE, getConstants().conceptType());
        RelatedResourceListItem roles = new RelatedResourceListItem(ConceptDS.ROLES, getConstants().conceptRoles(), false, getListRecordNavigationClickHandler());
        roles.setShowIfCondition(getRolesFormItemIfFunction());
        ViewTextItem representation = new ViewTextItem(RepresentationDS.TYPE, getConstants().representation());
        representation.setShowIfCondition(FormItemUtils.getFalseFormItemIfFunction());
        ViewTextItem representationView = new ViewTextItem(RepresentationDS.TYPE_VIEW, getConstants().representation());
        RelatedResourceLinkItem enumeratedRepresentation = new RelatedResourceLinkItem(RepresentationDS.ENUMERATED, getConstants().codelist(), getCustomLinkItemNavigationClickHandler());
        enumeratedRepresentation.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return org.siemac.metamac.srm.web.dsd.utils.CommonUtils.isRepresentationTypeEnumerated(form.getValueAsString(RepresentationDS.TYPE));
            }
        });
        contentDescriptorsForm.setFields(description, descriptionSource, context, docMethod, variable, type, roles, representation, representationView, enumeratedRepresentation);

        // Non enumerated representation
        facetForm = new StaticFacetForm();

        // Class descriptors
        classDescriptorsForm = new GroupDynamicForm(getConstants().formClassDescriptors());
        ViewMultiLanguageTextItem derivation = new ViewMultiLanguageTextItem(ConceptDS.DERIVATION, getConstants().conceptDerivation());
        ViewTextItem sdmxRelatedArtefact = new ViewTextItem(ConceptDS.SDMX_RELATED_ARTEFACT, getConstants().conceptSdmxRelatedArtefact());
        sdmxRelatedArtefact.setShowIfCondition(getSdmxRelatedArtefactFormItemIfFunction());
        classDescriptorsForm.setFields(derivation, sdmxRelatedArtefact);

        // Relation between concepts
        relationBetweenConceptsForm = new GroupDynamicForm(getConstants().conceptRelationBetweenConcepts());
        RelatedResourceLinkItem extendsConcept = new RelatedResourceLinkItem(ConceptDS.EXTENDS, getConstants().conceptExtends(), getCustomLinkItemNavigationClickHandler());
        ConceptsListItem relatedConcepts = new ConceptsListItem(ConceptDS.RELATED_CONCEPTS, getConstants().conceptRelatedConcepts(), false, getListRecordNavigationClickHandler());
        relationBetweenConceptsForm.setFields(extendsConcept, relatedConcepts);

        // Quantity
        quantityForm = new ViewQuantityForm(getConstants().conceptQuantity());

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
        mainFormLayout.addViewCanvas(productionDescriptorsForm);
        mainFormLayout.addViewCanvas(contentDescriptorsForm);
        mainFormLayout.addViewCanvas(facetForm);
        mainFormLayout.addViewCanvas(classDescriptorsForm);
        mainFormLayout.addViewCanvas(relationBetweenConceptsForm);
        mainFormLayout.addViewCanvas(quantityForm);
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
        ViewTextItem uriProvider = new ViewTextItem(ConceptDS.URI, getConstants().identifiableArtefactUriProvider());
        ViewTextItem urn = new ViewTextItem(ConceptDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(ConceptDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        identifiersEditionForm.setFields(code, staticCode, name, pluralName, acronym, uriProvider, urn, urnProvider);

        // Production descriptors
        productionDescriptorsEditionForm = new GroupDynamicForm(getConstants().formProductionDescriptors());
        ViewTextItem creationDate = new ViewTextItem(ConceptDS.CREATION_DATE, getConstants().identifiableArtefactCreationDate());
        productionDescriptorsEditionForm.setFields(creationDate);

        // CONTENT DESCRIPTORS
        contentDescriptorsEditionForm = new GroupDynamicForm(getConstants().formContentDescriptors());
        MultilanguageRichTextEditorItem description = new MultilanguageRichTextEditorItem(ConceptDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        MultilanguageRichTextEditorItem descriptionSource = new MultilanguageRichTextEditorItem(ConceptDS.DESCRIPTION_SOURCE, getConstants().conceptDescriptionSource());
        MultilanguageRichTextEditorItem context = new MultilanguageRichTextEditorItem(ConceptDS.CONTEXT, getConstants().conceptContext());
        MultilanguageRichTextEditorItem docMethod = new MultilanguageRichTextEditorItem(ConceptDS.DOC_METHOD, getConstants().conceptDocMethod());
        SearchRelatedResourceLinkItem variable = createVariableItem(ConceptDS.VARIABLE, getConstants().variable());
        variable.setShowIfCondition(getVariableFormItemIfFunction());
        RelatedResourceLinkItem staticVariable = new RelatedResourceLinkItem(ConceptDS.VARIABLE_VIEW, getConstants().variable(), getCustomLinkItemNavigationClickHandler());
        staticVariable.setShowIfCondition(getStaticVariableFormItemIfFunction());
        SelectItem type = new SelectItem(ConceptDS.TYPE, getConstants().conceptType()); // Value map set in setConceptTypes method
        RelatedResourceListItem roles = createRolesItem(ConceptDS.ROLES, getConstants().conceptRoles());
        roles.setShowIfCondition(getRolesFormItemIfFunction());

        // Representation type

        final CustomSelectItem representationType = createRepresentationTypeItem(RepresentationDS.TYPE, getConstants().representation());

        ViewTextItem staticRepresentationType = new ViewTextItem(RepresentationDS.TYPE_VIEW, getConstants().representation());
        staticRepresentationType.setShowIfCondition(getStaticRepresentationTypeFormItemIfFunction());

        // Enumerated representation: Codelist or ConceptScheme (only for concepts from a measure scheme)

        SearchRelatedResourceLinkItem enumeratedRepresentation = createEnumeratedRepresentationItem(RepresentationDS.ENUMERATED, MetamacSrmWeb.getConstants().conceptEnumeratedRepresentation());
        enumeratedRepresentation.setShowIfCondition(getEnumeratedRepresentationFormItemIfFunction()); // Shown in editionMode, only when the enumerated representation is editable

        RelatedResourceLinkItem enumeratedRepresentationView = new RelatedResourceLinkItem(RepresentationDS.ENUMERATED_VIEW, MetamacSrmWeb.getConstants().conceptEnumeratedRepresentation(),
                getCustomLinkItemNavigationClickHandler());
        enumeratedRepresentationView.setShowIfCondition(getStaticEnumeratedRepresentationFormItemIfFunction()); // This item is shown when the enumerated representation can not be edited

        contentDescriptorsEditionForm.setFields(description, descriptionSource, context, docMethod, variable, staticVariable, type, roles, representationType, staticRepresentationType,
                enumeratedRepresentation, enumeratedRepresentationView);

        // NON ENUMERATED REPRESENTATION
        facetEditionForm = new ConceptFacetForm();
        facetStaticEditionForm = new StaticFacetForm();

        // CLASS DESCRIPTORS
        classDescriptorsEditionForm = new GroupDynamicForm(getConstants().formClassDescriptors());
        MultilanguageRichTextEditorItem derivation = new MultilanguageRichTextEditorItem(ConceptDS.DERIVATION, getConstants().conceptDerivation());
        RequiredSelectItem sdmxRelatedArtefact = new RequiredSelectItem(ConceptDS.SDMX_RELATED_ARTEFACT, getConstants().conceptSdmxRelatedArtefact());
        sdmxRelatedArtefact.setValueMap(CommonUtils.getConceptRoleHashMap());
        sdmxRelatedArtefact.setShowIfCondition(getSdmxRelatedArtefactFormItemIfFunction());
        classDescriptorsEditionForm.setFields(derivation, sdmxRelatedArtefact);

        // RELATION BETWEEN CONCEPTS

        relationBetweenConceptsEditionForm = new GroupDynamicForm(getConstants().conceptRelationBetweenConcepts());
        SearchRelatedResourceLinkItem extendsConcept = createExtendsItem(ConceptDS.EXTENDS, getConstants().conceptExtends());
        ConceptsListItem relatedConcepts = createRelatedConceptsItem(ConceptDS.RELATED_CONCEPTS, getConstants().conceptRelatedConcepts());
        relationBetweenConceptsEditionForm.setFields(extendsConcept, relatedConcepts);

        quantityEditionForm = new QuantityForm(getConstants().conceptQuantity());

        // LEGAL ACTS

        legalActsEditionForm = new GroupDynamicForm(getConstants().conceptLegalActs());
        MultilanguageRichTextEditorItem legalActs = new MultilanguageRichTextEditorItem(ConceptDS.LEGAL_ACTS, getConstants().conceptLegalActs());
        legalActsEditionForm.setFields(legalActs);

        // COMMENTS

        commentsEditionForm = new GroupDynamicForm(getConstants().nameableArtefactComments());
        MultilanguageRichTextEditorItem comments = new MultilanguageRichTextEditorItem(ConceptDS.COMMENTS, getConstants().nameableArtefactComments());
        commentsEditionForm.setFields(comments);

        // ANNOTATIONS

        annotationsEditionPanel = new AnnotationsPanel(false);

        mainFormLayout.addEditionCanvas(identifiersEditionForm);
        mainFormLayout.addEditionCanvas(productionDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(contentDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(facetEditionForm);
        mainFormLayout.addEditionCanvas(facetStaticEditionForm);
        mainFormLayout.addEditionCanvas(classDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(relationBetweenConceptsEditionForm);
        mainFormLayout.addEditionCanvas(quantityEditionForm);
        mainFormLayout.addEditionCanvas(legalActsEditionForm);
        mainFormLayout.addEditionCanvas(commentsEditionForm);
        mainFormLayout.addEditionCanvas(annotationsEditionPanel);
    }

    @Override
    public void setConcept(ConceptMetamacDto conceptDto, List<RelatedResourceDto> roles, List<ConceptMetamacBasicDto> relatedConcepts, ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        setConceptScheme(conceptSchemeMetamacDto);
        setConcept(conceptDto, roles, relatedConcepts);
    }

    @Override
    public void setConcept(ConceptMetamacDto conceptDto, List<RelatedResourceDto> roles, List<ConceptMetamacBasicDto> relatedConcepts) {
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
        mainFormLayout.setConceptScheme(conceptSchemeMetamacDto);

        markFormsForRedraw();
    }

    @Override
    public void setConceptList(ConceptSchemeMetamacDto conceptSchemeMetamacDto, List<ConceptMetamacVisualisationResult> itemVisualisationDtos) {
        this.itemVisualisationResults = itemVisualisationDtos;
        conceptsTreeGrid.setConcepts(conceptSchemeMetamacDto, itemVisualisationDtos);
        conceptsTreeGrid.selectItem(conceptDto.getUrn());
    }

    @Override
    public void setConceptTypes(List<ConceptTypeDto> conceptTypeDtos) {
        this.conceptTypeDtos = conceptTypeDtos;
        contentDescriptorsEditionForm.getItem(ConceptDS.TYPE).setValueMap(CommonUtils.getConceptTypeHashMap(conceptTypeDtos));
    }

    @Override
    public void setCodelistsOrConceptSchemesForEnumeratedRepresentation(GetRelatedResourcesResult result) {
        if (searchCodelistOrConceptSchemesForEnumeratedRepresentationWindow != null) {

            searchCodelistOrConceptSchemesForEnumeratedRepresentationWindow.setRelatedResources(result.getRelatedResourceDtos());
            searchCodelistOrConceptSchemesForEnumeratedRepresentationWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getRelatedResourceDtos().size(), result.getTotalResults());

            if (result.getRelatedResourceDtos().isEmpty()) {
                // if there is no results, show an info message (maybe the attribute concept has no variable)
                searchCodelistOrConceptSchemesForEnumeratedRepresentationWindow.showInfoMessage();
            } else {
                searchCodelistOrConceptSchemesForEnumeratedRepresentationWindow.hideInfoMessage();
            }
        }
    }

    private void setConceptViewMode(ConceptMetamacDto conceptDto, List<RelatedResourceDto> roles, List<ConceptMetamacBasicDto> relatedConcepts) {
        // Identifiers Form
        identifiersForm.setValue(ConceptDS.CODE, conceptDto.getCode());
        identifiersForm.setValue(ConceptDS.NAME, RecordUtils.getInternationalStringRecord(conceptDto.getName()));
        identifiersForm.setValue(ConceptDS.PLURAL_NAME, RecordUtils.getInternationalStringRecord(conceptDto.getPluralName()));
        identifiersForm.setValue(ConceptDS.ACRONYM, RecordUtils.getInternationalStringRecord(conceptDto.getAcronym()));
        identifiersForm.setValue(ConceptDS.URI, conceptDto.getUriProvider());
        identifiersForm.setValue(ConceptDS.URN, conceptDto.getUrn());
        identifiersForm.setValue(ConceptDS.URN_PROVIDER, conceptDto.getUrnProvider());

        // Production descriptors form
        productionDescriptorsForm.setValue(ConceptDS.CREATION_DATE, conceptDto.getCreatedDate());

        // Content descriptors
        contentDescriptorsForm.setValue(ConceptDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(conceptDto.getDescription()));
        contentDescriptorsForm.setValue(ConceptDS.DESCRIPTION_SOURCE, RecordUtils.getInternationalStringRecord(conceptDto.getDescriptionSource()));
        contentDescriptorsForm.setValue(ConceptDS.CONTEXT, RecordUtils.getInternationalStringRecord(conceptDto.getContext()));
        contentDescriptorsForm.setValue(ConceptDS.DOC_METHOD, RecordUtils.getInternationalStringRecord(conceptDto.getDocMethod()));
        ((RelatedResourceLinkItem) contentDescriptorsForm.getItem(ConceptDS.VARIABLE)).setRelatedResource(conceptDto.getVariable());
        contentDescriptorsForm.setValue(RepresentationDS.TYPE, conceptDto.getCoreRepresentation() != null ? conceptDto.getCoreRepresentation().getRepresentationType().name() : null);
        contentDescriptorsForm.setValue(RepresentationDS.TYPE_VIEW,
                conceptDto.getCoreRepresentation() != null
                        ? org.siemac.metamac.srm.web.client.utils.CommonUtils.getTypeRepresentationName(conceptDto.getCoreRepresentation().getRepresentationType())
                        : null);
        ((RelatedResourceLinkItem) contentDescriptorsForm.getItem(RepresentationDS.ENUMERATED)).setRelatedResource(conceptDto.getCoreRepresentation() != null ? conceptDto.getCoreRepresentation()
                .getEnumeration() : null);
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
        classDescriptorsForm.setValue(ConceptDS.DERIVATION, RecordUtils.getInternationalStringRecord(conceptDto.getDerivation()));
        classDescriptorsForm.setValue(ConceptDS.SDMX_RELATED_ARTEFACT, CommonUtils.getConceptRoleName(conceptDto.getSdmxRelatedArtefact()));

        // Relation between concepts
        ((RelatedResourceLinkItem) relationBetweenConceptsForm.getItem(ConceptDS.EXTENDS)).setRelatedResource(conceptDto.getConceptExtends());
        ((ConceptsListItem) relationBetweenConceptsForm.getItem(ConceptDS.RELATED_CONCEPTS)).setDataConcepts(relatedConcepts);

        // Quantity Form
        quantityForm.setValue(conceptDto.getQuantity());
        quantityForm.setVisible(conceptSchemeMetamacDto != null && ConceptSchemeTypeEnum.MEASURE.equals(conceptSchemeMetamacDto.getType()));

        // Legal acts
        legalActsForm.setValue(ConceptDS.LEGAL_ACTS, RecordUtils.getInternationalStringRecord(conceptDto.getLegalActs()));

        // Comments
        commentsForm.setValue(ConceptDS.COMMENTS, RecordUtils.getInternationalStringRecord(conceptDto.getComment()));

        // Annotations
        annotationsPanel.setAnnotations(conceptDto.getAnnotations(), conceptSchemeMetamacDto);
    }

    private void setConceptEditionMode(ConceptMetamacDto conceptDto, List<RelatedResourceDto> roles, List<ConceptMetamacBasicDto> relatedConcepts) {
        // IDENTIFIERS FORM
        identifiersEditionForm.setValue(ConceptDS.CODE, conceptDto.getCode());
        identifiersEditionForm.setValue(ConceptDS.CODE_VIEW, conceptDto.getCode());
        identifiersEditionForm.setValue(ConceptDS.NAME, RecordUtils.getInternationalStringRecord(conceptDto.getName()));
        identifiersEditionForm.setValue(ConceptDS.PLURAL_NAME, RecordUtils.getInternationalStringRecord(conceptDto.getPluralName()));
        identifiersEditionForm.setValue(ConceptDS.ACRONYM, RecordUtils.getInternationalStringRecord(conceptDto.getAcronym()));
        identifiersEditionForm.setValue(ConceptDS.URI, conceptDto.getUriProvider());
        identifiersEditionForm.setValue(ConceptDS.URN, conceptDto.getUrn());
        identifiersEditionForm.setValue(ConceptDS.URN_PROVIDER, conceptDto.getUrnProvider());

        // PRODUCTION DESCRIPTORS FORM
        productionDescriptorsEditionForm.setValue(ConceptDS.CREATION_DATE, conceptDto.getCreatedDate());

        // CONTENT DESCRIPTORS
        contentDescriptorsEditionForm.setValue(ConceptDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(conceptDto.getDescription()));
        contentDescriptorsEditionForm.setValue(ConceptDS.DESCRIPTION_SOURCE, RecordUtils.getInternationalStringRecord(conceptDto.getDescriptionSource()));
        contentDescriptorsEditionForm.setValue(ConceptDS.CONTEXT, RecordUtils.getInternationalStringRecord(conceptDto.getContext()));
        contentDescriptorsEditionForm.setValue(ConceptDS.DOC_METHOD, RecordUtils.getInternationalStringRecord(conceptDto.getDocMethod()));
        ((SearchRelatedResourceLinkItem) contentDescriptorsEditionForm.getItem(ConceptDS.VARIABLE)).setRelatedResource(conceptDto.getVariable());
        ((RelatedResourceLinkItem) contentDescriptorsEditionForm.getItem(ConceptDS.VARIABLE_VIEW)).setRelatedResource(conceptDto.getVariable());
        contentDescriptorsEditionForm.setValue(RepresentationDS.TYPE, conceptDto.getCoreRepresentation() != null ? conceptDto.getCoreRepresentation().getRepresentationType().name() : null);
        contentDescriptorsEditionForm.setValue(RepresentationDS.TYPE_VIEW,
                conceptDto.getCoreRepresentation() != null
                        ? org.siemac.metamac.srm.web.client.utils.CommonUtils.getTypeRepresentationName(conceptDto.getCoreRepresentation().getRepresentationType())
                        : null);

        ((SearchRelatedResourceLinkItem) contentDescriptorsEditionForm.getItem(RepresentationDS.ENUMERATED)).setRelatedResource(conceptDto.getCoreRepresentation() != null ? conceptDto
                .getCoreRepresentation().getEnumeration() : null);
        ((RelatedResourceLinkItem) contentDescriptorsEditionForm.getItem(RepresentationDS.ENUMERATED_VIEW)).setRelatedResource(conceptDto.getCoreRepresentation() != null ? conceptDto
                .getCoreRepresentation().getEnumeration() : null);

        contentDescriptorsEditionForm.setValue(ConceptDS.TYPE, conceptDto.getConceptType() != null ? conceptDto.getConceptType().getIdentifier() : null);
        ((RelatedResourceListItem) contentDescriptorsEditionForm.getItem(ConceptDS.ROLES)).setRelatedResources(roles);

        // FACET (Non enumerated representation)
        facetEditionForm.setFacet(conceptDto.getCoreRepresentation() != null ? conceptDto.getCoreRepresentation().getTextFormat() : null);
        facetStaticEditionForm.setFacet(conceptDto.getCoreRepresentation() != null ? conceptDto.getCoreRepresentation().getTextFormat() : null);
        if (conceptSchemeMetamacDto != null) {
            FacetFormUtils.setFacetFormVisibility(facetEditionForm, facetStaticEditionForm, contentDescriptorsEditionForm.getValueAsString(RepresentationDS.TYPE), conceptSchemeMetamacDto);
        }

        // CLASS DESCRIPTORS
        classDescriptorsEditionForm.setValue(ConceptDS.DERIVATION, RecordUtils.getInternationalStringRecord(conceptDto.getDerivation()));
        classDescriptorsEditionForm.setValue(ConceptDS.SDMX_RELATED_ARTEFACT, conceptDto.getSdmxRelatedArtefact() != null ? conceptDto.getSdmxRelatedArtefact().name() : StringUtils.EMPTY);

        // RELATION BETWEEN CONCEPTS
        ((SearchRelatedResourceLinkItem) relationBetweenConceptsEditionForm.getItem(ConceptDS.EXTENDS)).setRelatedResource(conceptDto.getConceptExtends());

        ((ConceptsListItem) relationBetweenConceptsEditionForm.getItem(ConceptDS.RELATED_CONCEPTS)).setDataConcepts(relatedConcepts);

        // Quantity
        quantityEditionForm.setValue(conceptDto.getQuantity());
        quantityEditionForm.setVisible(conceptSchemeMetamacDto != null && ConceptSchemeTypeEnum.MEASURE.equals(conceptSchemeMetamacDto.getType()));

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
        conceptDto.setVariable(getVariableFromEditionForm());
        if (!StringUtils.isEmpty(contentDescriptorsEditionForm.getValueAsString(RepresentationDS.TYPE))) {
            if (conceptDto.getCoreRepresentation() == null) {
                conceptDto.setCoreRepresentation(new RepresentationDto());
            }
            conceptDto.getCoreRepresentation().setRepresentationType(
                    !StringUtils.isEmpty(contentDescriptorsEditionForm.getValueAsString(RepresentationDS.TYPE)) ? RepresentationTypeEnum.valueOf(contentDescriptorsEditionForm
                            .getValueAsString(RepresentationDS.TYPE)) : null);
            if (RepresentationTypeEnum.ENUMERATION.equals(conceptDto.getCoreRepresentation().getRepresentationType())) {
                conceptDto.getCoreRepresentation().setTextFormat(null);
                conceptDto.getCoreRepresentation().setEnumeration(((SearchRelatedResourceLinkItem) contentDescriptorsEditionForm.getItem(RepresentationDS.ENUMERATED)).getRelatedResourceDto());
            } else if (RepresentationTypeEnum.TEXT_FORMAT.equals(conceptDto.getCoreRepresentation().getRepresentationType())) {
                conceptDto.getCoreRepresentation().setEnumeration(null);
                conceptDto.getCoreRepresentation().setTextFormat(facetEditionForm.getFacet());
            }
        } else {
            conceptDto.setCoreRepresentation(null);
        }
        conceptDto.setConceptType(contentDescriptorsEditionForm.getValue(ConceptDS.TYPE) != null ? getConceptTypeDto(contentDescriptorsEditionForm.getValueAsString(ConceptDS.TYPE)) : null);
        // Roles get in getRoles method

        // Class descriptors
        conceptDto.setDerivation((InternationalStringDto) classDescriptorsEditionForm.getValue(ConceptDS.DERIVATION));
        conceptDto.setSdmxRelatedArtefact(getSdmxRelatedArtefactFromEditionForm());

        // Relation between concepts
        conceptDto.setConceptExtends(((SearchRelatedResourceLinkItem) relationBetweenConceptsEditionForm.getItem(ConceptDS.EXTENDS)).getRelatedResourceDto());
        // Related concepts get in getRelatedConcepts method

        conceptDto.setQuantity(quantityEditionForm.getValue(conceptDto.getQuantity()));

        // Legal acts
        conceptDto.setLegalActs((InternationalStringDto) legalActsEditionForm.getValue(ConceptDS.LEGAL_ACTS));

        // Comments
        conceptDto.setComment((InternationalStringDto) commentsEditionForm.getValue(ConceptDS.COMMENTS));

        // Annotations
        conceptDto.getAnnotations().clear();
        conceptDto.getAnnotations().addAll(annotationsEditionPanel.getAnnotations());

        return conceptDto;
    }

    private RelatedResourceDto getVariableFromEditionForm() {
        if (contentDescriptorsEditionForm.getItem(ConceptDS.VARIABLE).isVisible()) {
            return ((SearchRelatedResourceLinkItem) contentDescriptorsEditionForm.getItem(ConceptDS.VARIABLE)).getRelatedResourceDto();
        } else {
            return ((RelatedResourceLinkItem) contentDescriptorsEditionForm.getItem(ConceptDS.VARIABLE_VIEW)).getRelatedResourceDto();
        }
    }

    private ConceptRoleEnum getSdmxRelatedArtefactFromEditionForm() {
        return !StringUtils.isBlank(classDescriptorsEditionForm.getValueAsString(ConceptDS.SDMX_RELATED_ARTEFACT)) ? ConceptRoleEnum.valueOf(classDescriptorsEditionForm
                .getValueAsString(ConceptDS.SDMX_RELATED_ARTEFACT)) : null;
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
            List<RelatedResourceDto> families = RelatedResourceUtils.getVariableFamilyBasicDtosAsRelatedResourceDtos(result.getFamilies());
            searchVariableWindow.getInitialSelectionItem().setValueMap(org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourceHashMap(families));
        }
    }

    @Override
    public void setVariables(GetVariablesResult result) {
        if (searchVariableWindow != null) {
            searchVariableWindow.setRelatedResources(RelatedResourceUtils.getVariableBasicDtosAsRelatedResourceDtos(result.getVariables()));
            searchVariableWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getVariables().size(), result.getTotalResults());
        }
    }

    @Override
    public void setCodelistsForQuantityUnitFilter(List<RelatedResourceDto> codelistDtos, int firstResult, int totalResults) {
        if (quantityEditionForm != null) {
            quantityEditionForm.setCodelistsForQuantityUnitFilter(codelistDtos, firstResult, totalResults);
        }
    }

    @Override
    public void setCodeThatCanBeQuantityUnit(List<RelatedResourceDto> codesDtos, int firstResult, int totalResults) {
        if (quantityEditionForm != null) {
            quantityEditionForm.setCodesForQuantityUnit(codesDtos, firstResult, totalResults);
        }
    }

    @Override
    public void setCodelistsForQuantityBaseLocationFilter(List<RelatedResourceDto> codelistDtos, int firstResult, int totalResults) {
        if (quantityEditionForm != null) {
            quantityEditionForm.setCodelistsForQuantityBaseLocationFilter(codelistDtos, firstResult, totalResults);
        }
    }

    @Override
    public void setCodeThatCanBeQuantityBaseLocation(List<RelatedResourceDto> codesDtos, int firstResult, int totalResults) {
        if (quantityEditionForm != null) {
            quantityEditionForm.setCodeThatCanBeQuantityBaseLocation(codesDtos, firstResult, totalResults);
        }
    }

    @Override
    public void setConceptSchemesForQuantityBaseFilter(List<RelatedResourceDto> conceptSchemesDtos, int firstResult, int totalResults) {
        if (quantityEditionForm != null) {
            quantityEditionForm.setConceptSchemesForQuantityBaseFilter(conceptSchemesDtos, firstResult, totalResults);
        }
    }

    @Override
    public void setConceptThatCanBeQuantityBase(List<RelatedResourceDto> conceptDtos, int firstResult, int totalResults) {
        if (quantityEditionForm != null) {
            quantityEditionForm.setConceptsForQuantityBase(conceptDtos, firstResult, totalResults);
        }
    }
    @Override
    public void setConceptSchemesForQuantityNumeratorFilter(List<RelatedResourceDto> conceptSchemesDtos, int firstResult, int totalResults) {
        if (quantityEditionForm != null) {
            quantityEditionForm.setConceptSchemesForQuantityNumeratorFilter(conceptSchemesDtos, firstResult, totalResults);
        }
    }

    @Override
    public void setConceptThatCanBeQuantityNumerator(List<RelatedResourceDto> conceptDtos, int firstResult, int totalResults) {
        if (quantityEditionForm != null) {
            quantityEditionForm.setConceptsForQuantityNumerator(conceptDtos, firstResult, totalResults);
        }
    }
    @Override
    public void setConceptSchemesForQuantityDenominatorFilter(List<RelatedResourceDto> conceptSchemesDtos, int firstResult, int totalResults) {
        if (quantityEditionForm != null) {
            quantityEditionForm.setConceptSchemesForQuantityDenominatorFilter(conceptSchemesDtos, firstResult, totalResults);
        }
    }

    @Override
    public void setConceptThatCanBeQuantityDenominator(List<RelatedResourceDto> conceptDtos, int firstResult, int totalResults) {
        if (quantityEditionForm != null) {
            quantityEditionForm.setConceptsForQuantityDenominator(conceptDtos, firstResult, totalResults);
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

        RelatedResourceListItem rolesItem = new RelatedResourceListItem(name, title, true, getListRecordNavigationClickHandler());
        rolesItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent arg0) {
                searchRolesWindow = new SearchMultipleRelatedResourcePaginatedWindow(getConstants().conceptSelection(), MAX_RESULTS,
                        new SelectItem(ConceptSchemeDS.URN, getConstants().conceptScheme()), new PaginatedAction() {

                            @Override
                            public void retrieveResultSet(int firstResult, int maxResults) {
                                retrieveConceptsThatCanBeRole(firstResult, maxResults, searchRolesWindow.getRelatedResourceCriteria(), searchRolesWindow.getInitialSelectionValue(),
                                        searchRolesWindow.getIsLastVersionValue());
                            }
                        });

                searchRolesWindow.showIsLastVersionItem();
                searchRolesWindow.getIsLastVersionItem().addChangedHandler(new ChangedHandler() {

                    @Override
                    public void onChanged(ChangedEvent event) {
                        retrieveConceptSchemesWithConceptsThatCanBeRole(FIRST_RESULT, SrmWebConstants.NO_LIMIT_IN_PAGINATION, searchRolesWindow.getIsLastVersionValue());
                        retrieveConceptsThatCanBeRole(FIRST_RESULT, MAX_RESULTS, searchRolesWindow.getRelatedResourceCriteria(), searchRolesWindow.getInitialSelectionValue(),
                                searchRolesWindow.getIsLastVersionValue());
                    }
                });

                // Load the list of concepts and concept schemes that can be roles
                retrieveConceptSchemesWithConceptsThatCanBeRole(FIRST_RESULT, SrmWebConstants.NO_LIMIT_IN_PAGINATION, searchRolesWindow.getIsLastVersionValue());
                retrieveConceptsThatCanBeRole(FIRST_RESULT, MAX_RESULTS, null, null, searchRolesWindow.getIsLastVersionValue());

                searchRolesWindow.getInitialSelectionItem().addChangedHandler(new ChangedHandler() {

                    @Override
                    public void onChanged(ChangedEvent event) {
                        retrieveConceptsThatCanBeRole(FIRST_RESULT, MAX_RESULTS, searchRolesWindow.getRelatedResourceCriteria(), searchRolesWindow.getInitialSelectionValue(),
                                searchRolesWindow.getIsLastVersionValue());
                    }
                });

                // Set the selected concepts
                List<RelatedResourceDto> selectedRoles = ((RelatedResourceListItem) contentDescriptorsEditionForm.getItem(ConceptDS.ROLES)).getRelatedResourceDtos();
                searchRolesWindow.setTargetRelatedResources(selectedRoles);

                searchRolesWindow.setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String concept) {
                        retrieveConceptsThatCanBeRole(firstResult, maxResults, concept, searchRolesWindow.getInitialSelectionValue(), searchRolesWindow.getIsLastVersionValue());
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

    private void retrieveConceptSchemesWithConceptsThatCanBeRole(int firstResult, int maxResults, boolean isLastVersion) {
        ConceptSchemeWebCriteria conceptSchemeWebCriteria = new ConceptSchemeWebCriteria();
        conceptSchemeWebCriteria.setIsLastVersion(isLastVersion);

        getUiHandlers().retrieveConceptSchemesWithConceptsThatCanBeRole(firstResult, maxResults, conceptSchemeWebCriteria);
    }

    private void retrieveConceptsThatCanBeRole(int firstResult, int maxResults, String criteria, String conceptSchemeUrn, boolean isLastVersion) {
        ConceptWebCriteria conceptWebCriteria = new ConceptWebCriteria();
        conceptWebCriteria.setCriteria(criteria);
        conceptWebCriteria.setItemSchemeUrn(conceptSchemeUrn);
        conceptWebCriteria.setIsLastVersion(isLastVersion);

        getUiHandlers().retrieveConceptsThatCanBeRole(firstResult, maxResults, conceptWebCriteria);
    }

    private SearchRelatedResourceLinkItem createExtendsItem(String name, String title) {
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;
        SearchRelatedResourceLinkItem extendsItem = new SearchRelatedResourceLinkItem(name, title, getCustomLinkItemNavigationClickHandler());
        extendsItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {

                searchExtendsWindow = new SearchRelatedResourcePaginatedWindow(getConstants().conceptSelection(), MAX_RESULTS, new SelectItem(ConceptSchemeDS.URN, getConstants().conceptScheme()),
                        new PaginatedAction() {

                            @Override
                            public void retrieveResultSet(int firstResult, int maxResults) {
                                retrieveConceptsThatCanBeExtended(firstResult, maxResults, searchExtendsWindow.getRelatedResourceCriteria(), searchExtendsWindow.getInitialSelectionValue(),
                                        searchExtendsWindow.getIsLastVersionValue());
                            }
                        });

                searchExtendsWindow.showIsLastVersionItem();
                searchExtendsWindow.getIsLastVersionItem().addChangedHandler(new ChangedHandler() {

                    @Override
                    public void onChanged(ChangedEvent event) {
                        retrieveConceptSchemesWithConceptsThatCanBeExtended(FIRST_RESULST, SrmWebConstants.NO_LIMIT_IN_PAGINATION, searchExtendsWindow.getIsLastVersionValue());
                        retrieveConceptsThatCanBeExtended(FIRST_RESULST, MAX_RESULTS, null, null, searchExtendsWindow.getIsLastVersionValue());
                    }
                });

                // Load concepts and concept scheme that can be extended (to populate the selection window)
                retrieveConceptSchemesWithConceptsThatCanBeExtended(FIRST_RESULST, SrmWebConstants.NO_LIMIT_IN_PAGINATION, searchExtendsWindow.getIsLastVersionValue());
                retrieveConceptsThatCanBeExtended(FIRST_RESULST, MAX_RESULTS, null, null, searchExtendsWindow.getIsLastVersionValue());

                searchExtendsWindow.getInitialSelectionItem().addChangedHandler(new ChangedHandler() {

                    @Override
                    public void onChanged(ChangedEvent event) {
                        retrieveConceptsThatCanBeExtended(FIRST_RESULST, MAX_RESULTS, searchExtendsWindow.getRelatedResourceCriteria(), searchExtendsWindow.getInitialSelectionValue(),
                                searchExtendsWindow.getIsLastVersionValue());
                    }
                });

                searchExtendsWindow.getListGridItem().getListGrid().setSelectionType(SelectionStyle.SINGLE);
                searchExtendsWindow.getListGridItem().setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String concept) {
                        retrieveConceptsThatCanBeExtended(firstResult, maxResults, concept, searchExtendsWindow.getInitialSelectionValue(), searchExtendsWindow.getIsLastVersionValue());
                    }
                });
                searchExtendsWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent arg0) {
                        RelatedResourceDto selectedConcept = searchExtendsWindow.getSelectedRelatedResource();
                        searchExtendsWindow.markForDestroy();
                        // Set selected concepts in form
                        ((SearchRelatedResourceLinkItem) relationBetweenConceptsEditionForm.getItem(ConceptDS.EXTENDS)).setRelatedResource(selectedConcept);
                    }
                });
            }
        });
        return extendsItem;
    }

    private void retrieveConceptSchemesWithConceptsThatCanBeExtended(int firstResult, int maxResults, boolean isLastVersion) {
        ConceptSchemeWebCriteria conceptSchemeWebCriteria = new ConceptSchemeWebCriteria();
        conceptSchemeWebCriteria.setIsLastVersion(isLastVersion);

        getUiHandlers().retrieveConceptSchemesWithConceptsThatCanBeExtended(firstResult, maxResults, conceptSchemeWebCriteria);
    }

    private void retrieveConceptsThatCanBeExtended(int firstResult, int maxResults, String criteria, String conceptSchemeUrn, boolean isLastVersion) {
        ConceptWebCriteria conceptWebCriteria = new ConceptWebCriteria();
        conceptWebCriteria.setCriteria(criteria);
        conceptWebCriteria.setItemSchemeUrn(conceptSchemeUrn);
        conceptWebCriteria.setIsLastVersion(isLastVersion);

        getUiHandlers().retrieveConceptsThatCanBeExtended(firstResult, maxResults, conceptWebCriteria);
    }

    private ConceptsListItem createRelatedConceptsItem(String name, String title) {
        ConceptsListItem relatedConceptsItem = new ConceptsListItem(name, title, true, getListRecordNavigationClickHandler());
        relatedConceptsItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                final ConceptsTreeWindow extendedConceptWindow = new ConceptsTreeWindow(getConstants().conceptSelection());
                extendedConceptWindow.setConcepts(conceptSchemeMetamacDto, itemVisualisationResults);

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
                        List<ItemVisualisationResult> itemVisualisationResults = extendedConceptWindow.getSelectedConcepts();
                        extendedConceptWindow.markForDestroy();
                        // Set selected concepts in form
                        ((ConceptsListItem) relationBetweenConceptsEditionForm.getItem(ConceptDS.RELATED_CONCEPTS)).setDataItems(itemVisualisationResults);
                    }
                });
            }
        });
        return relatedConceptsItem;
    }

    private boolean validateEditionForms() {
        return identifiersEditionForm.validate(false) && contentDescriptorsEditionForm.validate(false) && (facetEditionForm.isVisible() ? facetEditionForm.validate(false) : true)
                && contentDescriptorsEditionForm.validate(false) && classDescriptorsEditionForm.validate(false) && relationBetweenConceptsEditionForm.validate(false)
                && (quantityEditionForm.isVisible() ? quantityEditionForm.validate(false) : true) && legalActsEditionForm.validate(false);
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
                    return CommonUtils.isMetadataVariableVisible(conceptSchemeMetamacDto.getType()) && ConceptsFormUtils.canConceptVariableBeEdited(conceptSchemeMetamacDto);
                }
                return false;
            }
        };
    }

    private FormItemIfFunction getVariableViewFormItemIfFunction() {
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

    private FormItemIfFunction getStaticVariableFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem arg0, Object arg1, DynamicForm arg2) {
                if (conceptSchemeMetamacDto != null) {
                    return CommonUtils.isMetadataVariableVisible(conceptSchemeMetamacDto.getType()) && !ConceptsFormUtils.canConceptVariableBeEdited(conceptSchemeMetamacDto);
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

    private SearchRelatedResourceLinkItem createVariableItem(String name, String title) {
        final int FIRST_RESULT = 0;
        final int MAX_RESULTS = 8;

        SearchRelatedResourceLinkItem variableItem = new SearchRelatedResourceLinkItem(name, title, getCustomLinkItemNavigationClickHandler());
        variableItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                SelectItem variableFamilySelectItem = new SelectItem(VariableFamilyDS.URN, getConstants().variableFamily());
                searchVariableWindow = new SearchRelatedResourcePaginatedWindow(getConstants().variableSelection(), MAX_RESULTS, variableFamilySelectItem, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        getUiHandlers().retrieveVariables(firstResult, maxResults, searchVariableWindow.getRelatedResourceCriteria(), searchVariableWindow.getInitialSelectionValue());
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
                        ((SearchRelatedResourceLinkItem) contentDescriptorsEditionForm.getItem(ConceptDS.VARIABLE)).setRelatedResource(selectedVariable);

                        // Reset the codelist associated with the enumerated representation of the concept (the codelist depends on the variable selected)
                        contentDescriptorsEditionForm.setValue(RepresentationDS.ENUMERATED, StringUtils.EMPTY);
                    }
                });
            }
        });
        return variableItem;
    };

    private CustomSelectItem createRepresentationTypeItem(String name, String title) {
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

        // Validators
        CustomValidator representationTypeValidator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                if (value != null && !StringUtils.isBlank(value.toString())) {
                    // Concepts from ROLE or GLOSARRY schemes cannot have a representation (enumerated or non enumerated)
                    return !ConceptSchemeTypeEnum.ROLE.equals(conceptSchemeMetamacDto.getType()) && !ConceptSchemeTypeEnum.GLOSSARY.equals(conceptSchemeMetamacDto.getType());
                }
                return true;
            }
        };
        representationTypeValidator.setErrorMessage(getConstants().conceptRepresentationValidatorMessage());
        representationType.setValidators(representationTypeValidator);

        return representationType;
    }

    private SearchRelatedResourceLinkItem createEnumeratedRepresentationItem(String name, String title) {
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;
        final SearchRelatedResourceLinkItem searchItem = new SearchRelatedResourceLinkItem(name, title, getCustomLinkItemNavigationClickHandler());
        searchItem.setRequired(true);
        searchItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {

                final String conceptUrn = conceptDto.getUrn();
                RelatedResourceDto selectedVariable = getVariableFromEditionForm();
                final String variableUrn = selectedVariable != null ? selectedVariable.getUrn() : null;

                RelatedResourceTypeEnum defaultResourceType = RelatedResourceTypeEnum.CODELIST;

                CustomSelectItem resourceType = new CustomSelectItem("resource-type", getConstants().resourceType());
                resourceType.setValueMap(CommonUtils.getResourceTypeThatCanBeEnumeratedRepresentationForConcept());
                resourceType.setValue(defaultResourceType.name());
                resourceType.addChangedHandler(new ChangedHandler() {

                    @Override
                    public void onChanged(ChangedEvent event) {
                        RelatedResourceTypeEnum resourceType = CommonUtils.getRelatedResourceTypeEnum(searchCodelistOrConceptSchemesForEnumeratedRepresentationWindow.getInitialSelectionValue());

                        getUiHandlers().retrieveCodelistsOrConceptSchemesForEnumeratedRepresentation(resourceType, variableUrn, FIRST_RESULST, MAX_RESULTS,
                                searchCodelistOrConceptSchemesForEnumeratedRepresentationWindow.getRelatedResourceCriteria(), conceptUrn,
                                searchCodelistOrConceptSchemesForEnumeratedRepresentationWindow.getIsLastVersionValue());
                    }
                });

                searchCodelistOrConceptSchemesForEnumeratedRepresentationWindow = new SearchRelatedResourcePaginatedWindow(getConstants().enumeratedRepresentationSelection(), MAX_RESULTS,
                        resourceType, new PaginatedAction() {

                            @Override
                            public void retrieveResultSet(int firstResult, int maxResults) {
                                RelatedResourceTypeEnum resourceType = CommonUtils.getRelatedResourceTypeEnum(searchCodelistOrConceptSchemesForEnumeratedRepresentationWindow
                                        .getInitialSelectionValue());

                                getUiHandlers().retrieveCodelistsOrConceptSchemesForEnumeratedRepresentation(resourceType, variableUrn, firstResult, maxResults,
                                        searchCodelistOrConceptSchemesForEnumeratedRepresentationWindow.getRelatedResourceCriteria(), conceptUrn,
                                        searchCodelistOrConceptSchemesForEnumeratedRepresentationWindow.getIsLastVersionValue());
                            }
                        });

                // This message will only be shown when the concept scheme is not a measure one
                searchCodelistOrConceptSchemesForEnumeratedRepresentationWindow.setInfoMessage(getConstants().conceptEnumeratedRepresentationInfoMessage());
                searchCodelistOrConceptSchemesForEnumeratedRepresentationWindow.showInfoMessage();

                searchCodelistOrConceptSchemesForEnumeratedRepresentationWindow.showIsLastVersionItem();
                searchCodelistOrConceptSchemesForEnumeratedRepresentationWindow.getIsLastVersionItem().addChangedHandler(new ChangedHandler() {

                    @Override
                    public void onChanged(ChangedEvent event) {

                        RelatedResourceTypeEnum resourceType = CommonUtils.getRelatedResourceTypeEnum(searchCodelistOrConceptSchemesForEnumeratedRepresentationWindow.getInitialSelectionValue());

                        getUiHandlers().retrieveCodelistsOrConceptSchemesForEnumeratedRepresentation(resourceType, variableUrn, FIRST_RESULST, MAX_RESULTS,
                                searchCodelistOrConceptSchemesForEnumeratedRepresentationWindow.getRelatedResourceCriteria(), conceptUrn,
                                searchCodelistOrConceptSchemesForEnumeratedRepresentationWindow.getIsLastVersionValue());
                    }
                });

                // Load codelists (to populate the selection window)
                getUiHandlers().retrieveCodelistsOrConceptSchemesForEnumeratedRepresentation(defaultResourceType, variableUrn, FIRST_RESULST, MAX_RESULTS, null, conceptUrn,
                        searchCodelistOrConceptSchemesForEnumeratedRepresentationWindow.getIsLastVersionValue());

                searchCodelistOrConceptSchemesForEnumeratedRepresentationWindow.getListGridItem().getListGrid().setSelectionType(SelectionStyle.SINGLE);
                searchCodelistOrConceptSchemesForEnumeratedRepresentationWindow.getListGridItem().setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String criteria) {

                        RelatedResourceTypeEnum resourceType = CommonUtils.getRelatedResourceTypeEnum(searchCodelistOrConceptSchemesForEnumeratedRepresentationWindow.getInitialSelectionValue());

                        getUiHandlers().retrieveCodelistsOrConceptSchemesForEnumeratedRepresentation(resourceType, variableUrn, firstResult, maxResults, criteria, conceptUrn,
                                searchCodelistOrConceptSchemesForEnumeratedRepresentationWindow.getIsLastVersionValue());
                    }
                });

                searchCodelistOrConceptSchemesForEnumeratedRepresentationWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        RelatedResourceDto selectedItemScheme = searchCodelistOrConceptSchemesForEnumeratedRepresentationWindow.getSelectedRelatedResource();
                        searchCodelistOrConceptSchemesForEnumeratedRepresentationWindow.markForDestroy();
                        // Set selected itemScheme in form
                        ((SearchRelatedResourceLinkItem) contentDescriptorsEditionForm.getItem(RepresentationDS.ENUMERATED)).setRelatedResource(selectedItemScheme);
                        contentDescriptorsEditionForm.validate(false);
                    }
                });
            }
        });
        return searchItem;
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

        quantityEditionForm.markForRedraw();
        quantityForm.markForRedraw();

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
