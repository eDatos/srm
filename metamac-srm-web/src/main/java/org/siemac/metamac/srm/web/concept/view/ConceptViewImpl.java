package org.siemac.metamac.srm.web.concept.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptTypeDto;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptRoleEnum;
import org.siemac.metamac.srm.web.client.constants.SrmWebConstants;
import org.siemac.metamac.srm.web.client.model.ds.RepresentationDS;
import org.siemac.metamac.srm.web.client.representation.widgets.StaticFacetForm;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.client.widgets.AnnotationsPanel;
import org.siemac.metamac.srm.web.client.widgets.CustomVLayout;
import org.siemac.metamac.srm.web.client.widgets.RelatedResourceListItem;
import org.siemac.metamac.srm.web.client.widgets.SearchMultipleRelatedResourcePaginatedWindow;
import org.siemac.metamac.srm.web.client.widgets.SearchRelatedResourcePaginatedWindow;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeDS;
import org.siemac.metamac.srm.web.concept.presenter.ConceptPresenter;
import org.siemac.metamac.srm.web.concept.utils.CommonUtils;
import org.siemac.metamac.srm.web.concept.utils.ConceptsClientSecurityUtils;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptUiHandlers;
import org.siemac.metamac.srm.web.concept.widgets.ConceptFacetForm;
import org.siemac.metamac.srm.web.concept.widgets.ConceptsListItem;
import org.siemac.metamac.srm.web.concept.widgets.ConceptsTreeGrid;
import org.siemac.metamac.srm.web.concept.widgets.ConceptsTreeWindow;
import org.siemac.metamac.srm.web.shared.code.GetVariablesResult;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.utils.ExternalItemUtils;
import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
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
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeRepresentationEnum;
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
    private GroupDynamicForm                             productionDescriptorsForm;
    private GroupDynamicForm                             relationBetweenConceptsForm;
    private GroupDynamicForm                             legalActsForm;
    private GroupDynamicForm                             commentsForm;
    private AnnotationsPanel                             annotationsPanel;

    // Edition forms
    private GroupDynamicForm                             identifiersEditionForm;
    private GroupDynamicForm                             contentDescriptorsEditionForm;
    private ConceptFacetForm                             facetEditionForm;
    private GroupDynamicForm                             classDescriptorsEditionForm;
    private GroupDynamicForm                             productionDescriptorsEditionForm;
    private GroupDynamicForm                             relationBetweenConceptsEditionForm;
    private GroupDynamicForm                             legalActsEditionForm;
    private GroupDynamicForm                             commentsEditionForm;
    private AnnotationsPanel                             annotationsEditionPanel;

    private List<ConceptTypeDto>                         conceptTypeDtos;
    private List<ExternalItemDto>                        codeLists;

    private ConceptSchemeMetamacDto                      conceptSchemeMetamacDto;
    private List<ItemHierarchyDto>                       itemHierarchyDtos;
    private ConceptMetamacDto                            conceptDto;

    private SearchMultipleRelatedResourcePaginatedWindow searchRolesWindow;
    private SearchRelatedResourcePaginatedWindow         searchExtendsWindow;
    private SearchRelatedResourcePaginatedWindow         searchVariableWindow;

    @Inject
    public ConceptViewImpl() {
        super();
        panel = new VLayout();
        panel.setHeight100();
        panel.setOverflow(Overflow.SCROLL);

        //
        // CONCEPTS HIERARCHY
        //

        conceptsTreeGrid = new ConceptsTreeGrid();

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

                productionDescriptorsForm.setTranslationsShowed(translationsShowed);
                productionDescriptorsEditionForm.setTranslationsShowed(translationsShowed);

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

        panel.addMember(conceptsListGridLayout);
        panel.addMember(mainFormLayout);
    }

    @Override
    public Widget asWidget() {
        return panel;
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
        ViewTextItem representation = new ViewTextItem(RepresentationDS.TYPE, getConstants().representation());
        representation.setShowIfCondition(FormItemUtils.getFalseFormItemIfFunction());
        ViewTextItem representationView = new ViewTextItem(RepresentationDS.TYPE_VIEW, getConstants().representation());
        ViewTextItem enumeratedRepresentation = new ViewTextItem(RepresentationDS.ENUMERATED, getConstants().representationCodeList());
        enumeratedRepresentation.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return TypeRepresentationEnum.ENUMERATED.name().equals(form.getValueAsString(RepresentationDS.TYPE));
            }
        });
        contentDescriptorsForm.setFields(description, descriptionSource, context, docMethod, variable, representation, representationView, enumeratedRepresentation);

        // Non enumerated representation
        facetForm = new StaticFacetForm();

        // Class descriptors
        classDescriptorsForm = new GroupDynamicForm(getConstants().formClassDescriptors());
        ViewTextItem sdmxRelatedArtefact = new ViewTextItem(ConceptDS.SDMX_RELATED_ARTEFACT, getConstants().conceptSdmxRelatedArtefact());
        sdmxRelatedArtefact.setShowIfCondition(getSdmxRelatedArtefactFormItemIfFunction());
        ViewTextItem type = new ViewTextItem(ConceptDS.TYPE, getConstants().conceptType());
        RelatedResourceListItem roles = new RelatedResourceListItem(ConceptDS.ROLES, getConstants().conceptRoles(), false);
        roles.setShowIfCondition(getRolesFormItemIfFunction());
        classDescriptorsForm.setFields(sdmxRelatedArtefact, type, roles);

        // Production descriptors
        productionDescriptorsForm = new GroupDynamicForm(getConstants().formProductionDescriptors());
        ViewMultiLanguageTextItem derivation = new ViewMultiLanguageTextItem(ConceptDS.DERIVATION, getConstants().conceptDerivation());
        productionDescriptorsForm.setFields(derivation);

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
        mainFormLayout.addViewCanvas(productionDescriptorsForm);
        mainFormLayout.addViewCanvas(relationBetweenConceptsForm);
        mainFormLayout.addViewCanvas(legalActsForm);
        mainFormLayout.addViewCanvas(commentsForm);
        mainFormLayout.addViewCanvas(annotationsPanel);
    }

    private void createEditionForm() {
        // Identifiers Form
        identifiersEditionForm = new GroupDynamicForm(getConstants().formIdentifiers());
        RequiredTextItem code = new RequiredTextItem(ConceptDS.CODE, getConstants().identifiableArtefactCode());
        code.setValidators(SemanticIdentifiersUtils.getConceptIdentifierCustomValidator());
        MultiLanguageTextItem name = new MultiLanguageTextItem(ConceptDS.NAME, getConstants().nameableArtefactName());
        name.setRequired(true);
        MultiLanguageTextItem pluralName = new MultiLanguageTextItem(ConceptDS.PLURAL_NAME, getConstants().conceptPluralName());
        MultiLanguageTextItem acronym = new MultiLanguageTextItem(ConceptDS.ACRONYM, getConstants().conceptAcronym());
        ViewTextItem uri = new ViewTextItem(ConceptDS.URI, getConstants().identifiableArtefactUri());
        ViewTextItem urn = new ViewTextItem(ConceptDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(ConceptDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        identifiersEditionForm.setFields(code, name, pluralName, acronym, uri, urn, urnProvider);

        // Content descriptors
        contentDescriptorsEditionForm = new GroupDynamicForm(getConstants().formContentDescriptors());
        MultiLanguageTextAreaItem description = new MultiLanguageTextAreaItem(ConceptDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        MultiLanguageTextItem descriptionSource = new MultiLanguageTextItem(ConceptDS.DESCRIPTION_SOURCE, getConstants().conceptDescriptionSource());
        MultiLanguageTextItem context = new MultiLanguageTextItem(ConceptDS.CONTEXT, getConstants().conceptContext());
        MultilanguageRichTextEditorItem docMethod = new MultilanguageRichTextEditorItem(ConceptDS.DOC_METHOD, getConstants().conceptDocMethod());
        ViewTextItem variable = new ViewTextItem(ConceptDS.VARIABLE, getConstants().variable());
        variable.setShowIfCondition(FormItemUtils.getFalseFormItemIfFunction());
        SearchViewTextItem variableView = createVariableItem(ConceptDS.VARIABLE_VIEW, getConstants().variable());
        final CustomSelectItem representation = new CustomSelectItem(RepresentationDS.TYPE, getConstants().representation());
        representation.setValueMap(org.siemac.metamac.srm.web.client.utils.CommonUtils.getTypeRepresentationEnumHashMap());
        representation.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                if (TypeRepresentationEnum.TEXT_FORMAT.name().equals(representation.getValueAsString())) {
                    facetEditionForm.show();
                } else {
                    facetEditionForm.hide();
                }
                contentDescriptorsEditionForm.markForRedraw();
            }
        });
        RequiredSelectItem enumeratedRepresentation = new RequiredSelectItem(RepresentationDS.ENUMERATED, getConstants().representationCodeList()); // Value map set in setCodeLists method
        enumeratedRepresentation.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                boolean isRepresentationEnumerated = TypeRepresentationEnum.ENUMERATED.name().equals(form.getValueAsString(RepresentationDS.TYPE));
                if (isRepresentationEnumerated) {
                    getUiHandlers().retrieveCodeLists(conceptDto.getUrn());
                }
                return isRepresentationEnumerated;
            }
        });
        contentDescriptorsEditionForm.setFields(description, descriptionSource, context, docMethod, variable, variableView, representation, enumeratedRepresentation);

        // Non enumerated representation
        facetEditionForm = new ConceptFacetForm();

        // Class descriptors
        classDescriptorsEditionForm = new GroupDynamicForm(getConstants().formClassDescriptors());
        RequiredSelectItem sdmxRelatedArtefact = new RequiredSelectItem(ConceptDS.SDMX_RELATED_ARTEFACT, getConstants().conceptSdmxRelatedArtefact());
        sdmxRelatedArtefact.setValueMap(CommonUtils.getConceptRoleHashMap());
        sdmxRelatedArtefact.setShowIfCondition(getSdmxRelatedArtefactFormItemIfFunction());
        SelectItem type = new SelectItem(ConceptDS.TYPE, getConstants().conceptType()); // Value map set in setConceptTypes method
        RelatedResourceListItem roles = createRolesItem(ConceptDS.ROLES, getConstants().conceptRoles());
        roles.setShowIfCondition(getRolesFormItemIfFunction());
        classDescriptorsEditionForm.setFields(sdmxRelatedArtefact, type, roles);

        // Production descriptors
        productionDescriptorsEditionForm = new GroupDynamicForm(getConstants().formProductionDescriptors());
        MultiLanguageTextItem derivation = new MultiLanguageTextItem(ConceptDS.DERIVATION, getConstants().conceptDerivation());
        productionDescriptorsEditionForm.setFields(derivation);

        // Relation between concepts
        relationBetweenConceptsEditionForm = new GroupDynamicForm(getConstants().conceptRelationBetweenConcepts());
        ViewTextItem extendsConcept = new ViewTextItem(ConceptDS.EXTENDS, getConstants().conceptExtends());
        extendsConcept.setShowIfCondition(FormItemUtils.getFalseFormItemIfFunction());
        SearchViewTextItem extendsConceptView = createExtendsItem(ConceptDS.EXTENDS_VIEW, getConstants().conceptExtends());
        ConceptsListItem relatedConcepts = createRelatedConceptsItem(ConceptDS.RELATED_CONCEPTS, getConstants().conceptRelatedConcepts());
        relationBetweenConceptsEditionForm.setFields(extendsConcept, extendsConceptView, relatedConcepts);

        // Legal acts
        legalActsEditionForm = new GroupDynamicForm(getConstants().conceptLegalActs());
        MultilanguageRichTextEditorItem legalActs = new MultilanguageRichTextEditorItem(ConceptDS.LEGAL_ACTS, getConstants().conceptLegalActs());
        legalActsEditionForm.setFields(legalActs);

        // Comments
        commentsEditionForm = new GroupDynamicForm(getConstants().nameableArtefactComments());
        MultiLanguageTextAreaItem comments = new MultiLanguageTextAreaItem(ConceptDS.COMMENTS, getConstants().nameableArtefactComments());
        commentsEditionForm.setFields(comments);

        // Annotations
        annotationsEditionPanel = new AnnotationsPanel(false);

        mainFormLayout.addEditionCanvas(identifiersEditionForm);
        mainFormLayout.addEditionCanvas(contentDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(facetEditionForm);
        mainFormLayout.addEditionCanvas(classDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(productionDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(relationBetweenConceptsEditionForm);
        mainFormLayout.addEditionCanvas(legalActsEditionForm);
        mainFormLayout.addEditionCanvas(commentsEditionForm);
        mainFormLayout.addEditionCanvas(annotationsEditionPanel);
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
    }

    @Override
    public void setConceptList(ConceptSchemeMetamacDto conceptSchemeMetamacDto, List<ItemHierarchyDto> itemHierarchyDtos) {
        this.conceptSchemeMetamacDto = conceptSchemeMetamacDto;
        this.itemHierarchyDtos = itemHierarchyDtos;
        conceptsTreeGrid.setItems(conceptSchemeMetamacDto, itemHierarchyDtos);
        conceptsTreeGrid.selectItem(conceptDto);

        // Security
        mainFormLayout.setCanEdit(ConceptsClientSecurityUtils.canUpdateConcept(conceptSchemeMetamacDto.getLifeCycle().getProcStatus(), conceptSchemeMetamacDto.getType(),
                CommonUtils.getRelatedOperationCode(conceptSchemeMetamacDto)));

        classDescriptorsForm.markForRedraw();
        classDescriptorsEditionForm.markForRedraw();
    }

    @Override
    public void setConceptTypes(List<ConceptTypeDto> conceptTypeDtos) {
        this.conceptTypeDtos = conceptTypeDtos;
        classDescriptorsEditionForm.getItem(ConceptDS.TYPE).setValueMap(CommonUtils.getConceptTypeHashMap(conceptTypeDtos));
    }

    @Override
    public void setCodeLists(List<ExternalItemDto> codeLists) {
        this.codeLists = codeLists;
        contentDescriptorsEditionForm.getItem(RepresentationDS.ENUMERATED).setValueMap(ExternalItemUtils.getExternalItemsHashMap(codeLists));
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
        contentDescriptorsForm.setValue(ConceptDS.VARIABLE_VIEW, org.siemac.metamac.srm.web.client.utils.CommonUtils.getRelatedResourceName(conceptDto.getVariable()));
        contentDescriptorsForm.setValue(RepresentationDS.TYPE, conceptDto.getCoreRepresentation() != null ? conceptDto.getCoreRepresentation().getTypeRepresentationEnum().name() : null);
        contentDescriptorsForm.setValue(
                RepresentationDS.TYPE_VIEW,
                conceptDto.getCoreRepresentation() != null ? org.siemac.metamac.srm.web.client.utils.CommonUtils.getTypeRepresentationName(conceptDto.getCoreRepresentation()
                        .getTypeRepresentationEnum()) : null);
        contentDescriptorsForm.setValue(RepresentationDS.ENUMERATED,
                conceptDto.getCoreRepresentation() != null ? ExternalItemUtils.getExternalItemName(conceptDto.getCoreRepresentation().getEnumerated()) : null);
        contentDescriptorsForm.markForRedraw();

        // Non enumerated representation
        if (conceptDto.getCoreRepresentation() != null && TypeRepresentationEnum.TEXT_FORMAT.equals(conceptDto.getCoreRepresentation().getTypeRepresentationEnum())) {
            facetForm.show();
        } else {
            facetForm.hide();
        }
        facetForm.setFacet(conceptDto.getCoreRepresentation() != null ? conceptDto.getCoreRepresentation().getNonEnumerated() : null);
        facetForm.markForRedraw();

        // Class descriptors
        classDescriptorsForm.setValue(ConceptDS.SDMX_RELATED_ARTEFACT, CommonUtils.getConceptRoleName(conceptDto.getSdmxRelatedArtefact()));
        classDescriptorsForm.setValue(ConceptDS.TYPE, conceptDto.getType() != null ? CommonWebUtils.getElementName(conceptDto.getType().getIdentifier(), conceptDto.getType().getDescription()) : null);
        ((RelatedResourceListItem) classDescriptorsForm.getItem(ConceptDS.ROLES)).setRelatedResources(roles);

        // Production descriptors
        productionDescriptorsForm.setValue(ConceptDS.DERIVATION, RecordUtils.getInternationalStringRecord(conceptDto.getDerivation()));

        // Relation between concepts
        relationBetweenConceptsForm.setValue(ConceptDS.EXTENDS_VIEW, org.siemac.metamac.srm.web.client.utils.CommonUtils.getRelatedResourceName(conceptDto.getConceptExtends()));
        ((ConceptsListItem) relationBetweenConceptsForm.getItem(ConceptDS.RELATED_CONCEPTS)).setDataConcepts(relatedConcepts);

        // Legal acts
        legalActsForm.setValue(ConceptDS.LEGAL_ACTS, RecordUtils.getInternationalStringRecord(conceptDto.getLegalActs()));

        // Comments
        commentsForm.setValue(ConceptDS.COMMENTS, RecordUtils.getInternationalStringRecord(conceptDto.getComment()));

        // Annotations
        annotationsPanel.setAnnotations(conceptDto.getAnnotations());
    }

    private void setConceptEditionMode(ConceptMetamacDto conceptDto, List<RelatedResourceDto> roles, List<ConceptMetamacDto> relatedConcepts) {
        // Identifiers Form
        identifiersEditionForm.setValue(ConceptDS.CODE, conceptDto.getCode());
        identifiersEditionForm.setValue(ConceptDS.NAME, RecordUtils.getInternationalStringRecord(conceptDto.getName()));
        identifiersEditionForm.setValue(ConceptDS.PLURAL_NAME, RecordUtils.getInternationalStringRecord(conceptDto.getPluralName()));
        identifiersEditionForm.setValue(ConceptDS.ACRONYM, RecordUtils.getInternationalStringRecord(conceptDto.getAcronym()));
        identifiersEditionForm.setValue(ConceptDS.URI, conceptDto.getUriProvider());
        identifiersEditionForm.setValue(ConceptDS.URN, conceptDto.getUrn());
        identifiersEditionForm.setValue(ConceptDS.URN_PROVIDER, conceptDto.getUrnProvider());

        // Content descriptors
        contentDescriptorsEditionForm.setValue(ConceptDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(conceptDto.getDescription()));
        contentDescriptorsEditionForm.setValue(ConceptDS.DESCRIPTION_SOURCE, RecordUtils.getInternationalStringRecord(conceptDto.getDescriptionSource()));
        contentDescriptorsEditionForm.setValue(ConceptDS.CONTEXT, RecordUtils.getInternationalStringRecord(conceptDto.getContext()));
        contentDescriptorsEditionForm.setValue(ConceptDS.DOC_METHOD, RecordUtils.getInternationalStringRecord(conceptDto.getDocMethod()));
        contentDescriptorsEditionForm.setValue(ConceptDS.VARIABLE_VIEW, org.siemac.metamac.srm.web.client.utils.CommonUtils.getRelatedResourceName(conceptDto.getVariable()));
        contentDescriptorsEditionForm.setValue(ConceptDS.VARIABLE, conceptDto.getVariable() != null ? conceptDto.getVariable().getUrn() : StringUtils.EMPTY);
        contentDescriptorsEditionForm.setValue(RepresentationDS.TYPE, conceptDto.getCoreRepresentation() != null ? conceptDto.getCoreRepresentation().getTypeRepresentationEnum().name() : null);
        contentDescriptorsEditionForm.setValue(RepresentationDS.ENUMERATED, conceptDto.getCoreRepresentation() != null && conceptDto.getCoreRepresentation().getEnumerated() != null ? conceptDto
                .getCoreRepresentation().getEnumerated().getUrn() : null);

        // Non enumerated representation
        if (conceptDto.getCoreRepresentation() != null && TypeRepresentationEnum.TEXT_FORMAT.equals(conceptDto.getCoreRepresentation().getTypeRepresentationEnum())) {
            facetEditionForm.show();
        } else {
            facetEditionForm.hide();
        }
        facetEditionForm.setFacet(conceptDto.getCoreRepresentation() != null ? conceptDto.getCoreRepresentation().getNonEnumerated() : null);

        // Class descriptors
        classDescriptorsEditionForm.setValue(ConceptDS.SDMX_RELATED_ARTEFACT, conceptDto.getSdmxRelatedArtefact() != null ? conceptDto.getSdmxRelatedArtefact().name() : StringUtils.EMPTY);
        classDescriptorsEditionForm.setValue(ConceptDS.TYPE, conceptDto.getType() != null ? conceptDto.getType().getIdentifier() : null);
        ((RelatedResourceListItem) classDescriptorsEditionForm.getItem(ConceptDS.ROLES)).setRelatedResources(roles);

        // Production descriptors
        productionDescriptorsEditionForm.setValue(ConceptDS.DERIVATION, RecordUtils.getInternationalStringRecord(conceptDto.getDerivation()));

        // Relation between concepts
        relationBetweenConceptsEditionForm.setValue(ConceptDS.EXTENDS_VIEW, org.siemac.metamac.srm.web.client.utils.CommonUtils.getRelatedResourceName(conceptDto.getConceptExtends()));
        relationBetweenConceptsEditionForm.setValue(ConceptDS.EXTENDS, conceptDto.getConceptExtends() != null ? conceptDto.getConceptExtends().getUrn() : StringUtils.EMPTY);

        ((ConceptsListItem) relationBetweenConceptsEditionForm.getItem(ConceptDS.RELATED_CONCEPTS)).setDataConcepts(relatedConcepts);

        // Legal acts
        legalActsEditionForm.setValue(ConceptDS.LEGAL_ACTS, RecordUtils.getInternationalStringRecord(conceptDto.getLegalActs()));

        // Comments
        commentsEditionForm.setValue(ConceptDS.COMMENTS, RecordUtils.getInternationalStringRecord(conceptDto.getComment()));

        // Annotations
        annotationsEditionPanel.setAnnotations(conceptDto.getAnnotations());
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
            conceptDto.getCoreRepresentation().setTypeRepresentationEnum(
                    !StringUtils.isEmpty(contentDescriptorsEditionForm.getValueAsString(RepresentationDS.TYPE)) ? TypeRepresentationEnum.valueOf(contentDescriptorsEditionForm
                            .getValueAsString(RepresentationDS.TYPE)) : null);
            if (TypeRepresentationEnum.ENUMERATED.equals(conceptDto.getCoreRepresentation().getTypeRepresentationEnum())) {
                conceptDto.getCoreRepresentation().setNonEnumerated(null);
                conceptDto.getCoreRepresentation().setEnumerated(ExternalItemUtils.getExternalItemDtoFromUrn(codeLists, contentDescriptorsEditionForm.getValueAsString(RepresentationDS.ENUMERATED)));
            } else if (TypeRepresentationEnum.TEXT_FORMAT.equals(conceptDto.getCoreRepresentation().getTypeRepresentationEnum())) {
                conceptDto.getCoreRepresentation().setEnumerated(null);
                conceptDto.getCoreRepresentation().setNonEnumerated(facetEditionForm.getFacet());
            }
        } else {
            conceptDto.setCoreRepresentation(null);
        }

        // Class descriptors
        conceptDto.setSdmxRelatedArtefact(!StringUtils.isBlank(classDescriptorsEditionForm.getValueAsString(ConceptDS.SDMX_RELATED_ARTEFACT)) ? ConceptRoleEnum.valueOf(classDescriptorsEditionForm
                .getValueAsString(ConceptDS.SDMX_RELATED_ARTEFACT)) : null);
        conceptDto.setType(classDescriptorsEditionForm.getValue(ConceptDS.TYPE) != null ? getConceptTypeDto(classDescriptorsEditionForm.getValueAsString(ConceptDS.TYPE)) : null);
        // Roles get in getRoles method

        // Production descriptors
        conceptDto.setDerivation((InternationalStringDto) productionDescriptorsEditionForm.getValue(ConceptDS.DERIVATION));

        // Relation between concepts
        conceptDto.setConceptExtends(StringUtils.isBlank(relationBetweenConceptsEditionForm.getValueAsString(ConceptDS.EXTENDS)) ? null : RelatedResourceUtils.createRelatedResourceDto(
                TypeExternalArtefactsEnum.CONCEPT, relationBetweenConceptsEditionForm.getValueAsString(ConceptDS.EXTENDS)));
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
            searchRolesWindow.getInitialSelectionItem().setValueMap(org.siemac.metamac.srm.web.client.utils.CommonUtils.getRelatedResourceHashMap(conceptSchemes));
        }
    }

    @Override
    public void setConceptSchemesWithConceptsThatCanBeExtended(List<RelatedResourceDto> conceptSchemes) {
        if (searchExtendsWindow != null) {
            searchExtendsWindow.getInitialSelectionItem().setValueMap(org.siemac.metamac.srm.web.client.utils.CommonUtils.getRelatedResourceHashMap(conceptSchemes));
        }
    }

    @Override
    public void setVariables(GetVariablesResult result) {
        if (searchVariableWindow != null) {
            searchVariableWindow.setRelatedResources(RelatedResourceUtils.getRelatedResourceDtosFromVariableDtos(result.getVariables()));
            searchVariableWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getVariables().size(), result.getTotalResults());
        }
    }

    private List<String> getRelatedConcepts() {
        return ((ConceptsListItem) relationBetweenConceptsEditionForm.getItem(ConceptDS.RELATED_CONCEPTS)).getConceptUrns();
    }

    private List<String> getRoles() {
        return ((RelatedResourceListItem) classDescriptorsEditionForm.getItem(ConceptDS.ROLES)).getSelectedRelatedResourceUrns();
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
                                getUiHandlers().retrieveConceptsThatCanBeRole(firstResult, maxResults, searchRolesWindow.getRelatedResourceCriteria(), searchRolesWindow.getIntialSelectionValue());
                            }
                        });

                // Load the list of concepts and concept schemes that can be roles
                getUiHandlers().retrieveConceptsThatCanBeRole(FIRST_RESULT, MAX_RESULTS, null, null);
                getUiHandlers().retrieveConceptSchemesWithConceptsThatCanBeRole(FIRST_RESULT, SrmWebConstants.NO_LIMIT_IN_PAGINATION);

                searchRolesWindow.getInitialSelectionItem().addChangedHandler(new ChangedHandler() {

                    @Override
                    public void onChanged(ChangedEvent event) {
                        getUiHandlers().retrieveConceptsThatCanBeRole(FIRST_RESULT, MAX_RESULTS, searchRolesWindow.getRelatedResourceCriteria(), searchRolesWindow.getIntialSelectionValue());
                    }
                });

                // Set the selected concepts
                List<RelatedResourceDto> selectedRoles = ((RelatedResourceListItem) classDescriptorsEditionForm.getItem(ConceptDS.ROLES)).getRelatedResourceDtos();
                searchRolesWindow.setTargetRelatedResources(selectedRoles);

                searchRolesWindow.setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String concept) {
                        getUiHandlers().retrieveConceptsThatCanBeRole(firstResult, maxResults, concept, searchRolesWindow.getIntialSelectionValue());
                    }
                });
                searchRolesWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent arg0) {
                        List<RelatedResourceDto> conceptDtos = searchRolesWindow.getSelectedRelatedResources();
                        searchRolesWindow.markForDestroy();
                        // Set selected concepts in form
                        ((RelatedResourceListItem) classDescriptorsEditionForm.getItem(ConceptDS.ROLES)).setRelatedResources(conceptDtos);
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
                                selectedConcept != null ? org.siemac.metamac.srm.web.client.utils.CommonUtils.getRelatedResourceName(selectedConcept) : null);
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
                && classDescriptorsEditionForm.validate(false) && productionDescriptorsEditionForm.validate(false) && relationBetweenConceptsEditionForm.validate(false)
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

    private ConceptTypeDto getConceptTypeDto(String identifier) {
        for (ConceptTypeDto conceptTypeDto : conceptTypeDtos) {
            if (StringUtils.equals(identifier, conceptTypeDto.getIdentifier())) {
                return conceptTypeDto;
            }
        }
        return null;
    }

    private SearchViewTextItem createVariableItem(String name, String title) {
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;

        SearchViewTextItem variableItem = new SearchViewTextItem(name, title);
        variableItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                searchVariableWindow = new SearchRelatedResourcePaginatedWindow(getConstants().variableSelection(), MAX_RESULTS, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        getUiHandlers().retrieveVariables(FIRST_RESULST, MAX_RESULTS, searchVariableWindow.getRelatedResourceCriteria());
                    }
                });

                // Load variables (to populate the selection window)
                getUiHandlers().retrieveVariables(FIRST_RESULST, MAX_RESULTS, null);

                searchVariableWindow.getListGridItem().getListGrid().setSelectionType(SelectionStyle.SINGLE);
                searchVariableWindow.getListGridItem().setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                        getUiHandlers().retrieveVariables(firstResult, maxResults, criteria);
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
                                selectedVariable != null ? org.siemac.metamac.srm.web.client.utils.CommonUtils.getRelatedResourceName(selectedVariable) : null);
                    }
                });
            }
        });
        return variableItem;
    };
}
