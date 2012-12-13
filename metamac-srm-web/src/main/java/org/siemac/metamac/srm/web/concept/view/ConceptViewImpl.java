package org.siemac.metamac.srm.web.concept.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptTypeDto;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptRoleEnum;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.model.ds.RepresentationDS;
import org.siemac.metamac.srm.web.client.representation.widgets.StaticFacetForm;
import org.siemac.metamac.srm.web.client.widgets.AnnotationsPanel;
import org.siemac.metamac.srm.web.client.widgets.CustomVLayout;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;
import org.siemac.metamac.srm.web.concept.presenter.ConceptPresenter;
import org.siemac.metamac.srm.web.concept.utils.CommonUtils;
import org.siemac.metamac.srm.web.concept.utils.ConceptsClientSecurityUtils;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptUiHandlers;
import org.siemac.metamac.srm.web.concept.widgets.ConceptFacetForm;
import org.siemac.metamac.srm.web.concept.widgets.ConceptsListItem;
import org.siemac.metamac.srm.web.concept.widgets.ConceptsTreeGrid;
import org.siemac.metamac.srm.web.concept.widgets.ConceptsTreeWindow;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.utils.ExternalItemUtils;
import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.TitleLabel;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextAreaItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultilanguageRichTextEditorItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.RepresentationDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeRepresentationEnum;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
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
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class ConceptViewImpl extends ViewImpl implements ConceptPresenter.ConceptView {

    private ConceptUiHandlers           uiHandlers;

    private VLayout                     panel;
    private InternationalMainFormLayout mainFormLayout;

    private ConceptsTreeGrid            conceptsTreeGrid;

    // View forms
    private GroupDynamicForm            identifiersForm;
    private GroupDynamicForm            contentDescriptorsForm;
    private StaticFacetForm             facetForm;
    private GroupDynamicForm            classDescriptorsForm;
    private GroupDynamicForm            productionDescriptorsForm;
    private GroupDynamicForm            relationBetweenConceptsForm;
    private GroupDynamicForm            legalActsForm;
    private AnnotationsPanel            annotationsPanel;

    // Edition forms
    private GroupDynamicForm            identifiersEditionForm;
    private GroupDynamicForm            contentDescriptorsEditionForm;
    private ConceptFacetForm            facetEditionForm;
    private GroupDynamicForm            classDescriptorsEditionForm;
    private GroupDynamicForm            productionDescriptorsEditionForm;
    private GroupDynamicForm            relationBetweenConceptsEditionForm;
    private GroupDynamicForm            legalActsEditionForm;
    private AnnotationsPanel            annotationsEditionPanel;

    private List<ConceptTypeDto>        conceptTypeDtos;
    private List<ExternalItemDto>       codeLists;

    private ConceptSchemeMetamacDto     conceptSchemeMetamacDto;
    private List<ItemHierarchyDto>      itemHierarchyDtos;
    private ConceptMetamacDto           conceptDto;

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

                annotationsPanel.setTranslationsShowed(translationsShowed);
                annotationsEditionPanel.setTranslationsShowed(translationsShowed);
            }
        });

        // Save
        mainFormLayout.getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (validateEditionForms()) {
                    uiHandlers.saveConcept(getConceptDto(), getRelatedConcepts());
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
        this.uiHandlers = uiHandlers;
        conceptsTreeGrid.setUiHandlers(uiHandlers);
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == ConceptPresenter.TYPE_SetContextAreaContentConceptToolBar) {
            if (content != null) {
                Canvas[] canvas = ((ToolStrip) content).getMembers();
                for (int i = 0; i < canvas.length; i++) {
                    if (canvas[i] instanceof ToolStripButton) {
                        if (ToolStripButtonEnum.CONCEPTS.getValue().equals(((ToolStripButton) canvas[i]).getID())) {
                            ((ToolStripButton) canvas[i]).select();
                        }
                    }
                }
                panel.addMember(content, 0);
            }
        } else {
            // To support inheritance in your views it is good practice to call super.setInSlot when you can't handle the call.
            // Who knows, maybe the parent class knows what to do with this slot.
            super.setInSlot(slot, content);
        }
    }

    private void createViewForm() {
        // Identifiers Form
        identifiersForm = new GroupDynamicForm(getConstants().conceptIdentifiers());
        ViewTextItem code = new ViewTextItem(ConceptDS.CODE, getConstants().identifiableArtefactCode());
        ViewMultiLanguageTextItem name = new ViewMultiLanguageTextItem(ConceptDS.NAME, getConstants().nameableArtefactName());
        ViewMultiLanguageTextItem pluralName = new ViewMultiLanguageTextItem(ConceptDS.PLURAL_NAME, getConstants().conceptPluralName());
        ViewMultiLanguageTextItem acronym = new ViewMultiLanguageTextItem(ConceptDS.ACRONYM, getConstants().conceptAcronym());
        ViewTextItem uri = new ViewTextItem(ConceptDS.URI, getConstants().identifiableArtefactUri());
        ViewTextItem urn = new ViewTextItem(ConceptDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(ConceptDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        identifiersForm.setFields(code, name, pluralName, acronym, uri, urn, urnProvider);

        // Content descriptors
        contentDescriptorsForm = new GroupDynamicForm(getConstants().conceptContentDescriptors());
        ViewMultiLanguageTextItem description = new ViewMultiLanguageTextItem(ConceptDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        ViewMultiLanguageTextItem descriptionSource = new ViewMultiLanguageTextItem(ConceptDS.DESCRIPTION_SOURCE, getConstants().conceptDescriptionSource());
        ViewMultiLanguageTextItem context = new ViewMultiLanguageTextItem(ConceptDS.CONTEXT, getConstants().conceptContext());
        ViewMultiLanguageTextItem docMethod = new ViewMultiLanguageTextItem(ConceptDS.DOC_METHOD, getConstants().conceptDocMethod());
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
        contentDescriptorsForm.setFields(description, descriptionSource, context, docMethod, representation, representationView, enumeratedRepresentation);

        // Non enumerated representation
        facetForm = new StaticFacetForm();

        // Class descriptors
        classDescriptorsForm = new GroupDynamicForm(getConstants().conceptClassDescriptors());
        ViewTextItem sdmxRelatedArtefact = new ViewTextItem(ConceptDS.SDMX_RELATED_ARTEFACT, getConstants().conceptSdmxRelatedArtefact());
        sdmxRelatedArtefact.setShowIfCondition(getSdmxRelatedArtefactFormItemIfFunction());
        ViewTextItem type = new ViewTextItem(ConceptDS.TYPE, getConstants().conceptType());
        ViewTextItem roles = new ViewTextItem(ConceptDS.ROLES, getConstants().conceptRoles()); // TODO roles
        classDescriptorsForm.setFields(sdmxRelatedArtefact, type, roles);

        // Production descriptors
        productionDescriptorsForm = new GroupDynamicForm(getConstants().conceptProductionDescriptors());
        ViewMultiLanguageTextItem derivation = new ViewMultiLanguageTextItem(ConceptDS.DERIVATION, getConstants().conceptDerivation());
        productionDescriptorsForm.setFields(derivation);

        // Relation between concepts
        relationBetweenConceptsForm = new GroupDynamicForm(getConstants().conceptRelationBetweenConcepts());
        ViewTextItem extendsConcept = new ViewTextItem(ConceptDS.EXTENDS, getConstants().conceptExtends()); // TODO extends
        ConceptsListItem relatedConcepts = new ConceptsListItem(ConceptDS.RELATED_CONCEPTS, getConstants().conceptRelatedConcepts(), false);
        relationBetweenConceptsForm.setFields(extendsConcept, relatedConcepts);

        // Legal acts
        legalActsForm = new GroupDynamicForm(getConstants().conceptLegalActs());
        ViewMultiLanguageTextItem legalActs = new ViewMultiLanguageTextItem(ConceptDS.LEGAL_ACTS, getConstants().conceptLegalActs());
        legalActsForm.setFields(legalActs);

        // Annotations
        annotationsPanel = new AnnotationsPanel(true);

        mainFormLayout.addViewCanvas(identifiersForm);
        mainFormLayout.addViewCanvas(contentDescriptorsForm);
        mainFormLayout.addViewCanvas(facetForm);
        mainFormLayout.addViewCanvas(classDescriptorsForm);
        mainFormLayout.addViewCanvas(productionDescriptorsForm);
        mainFormLayout.addViewCanvas(relationBetweenConceptsForm);
        mainFormLayout.addViewCanvas(legalActsForm);
        mainFormLayout.addViewCanvas(annotationsPanel);
    }

    private void createEditionForm() {
        // Identifiers Form
        identifiersEditionForm = new GroupDynamicForm(getConstants().conceptIdentifiers());
        RequiredTextItem code = new RequiredTextItem(ConceptDS.CODE, getConstants().identifiableArtefactCode());
        code.setValidators(CommonWebUtils.getSemanticIdentifierCustomValidator());
        MultiLanguageTextItem name = new MultiLanguageTextItem(ConceptDS.NAME, getConstants().nameableArtefactName());
        name.setRequired(true);
        MultiLanguageTextItem pluralName = new MultiLanguageTextItem(ConceptDS.PLURAL_NAME, getConstants().conceptPluralName());
        MultiLanguageTextItem acronym = new MultiLanguageTextItem(ConceptDS.ACRONYM, getConstants().conceptAcronym());
        ViewTextItem uri = new ViewTextItem(ConceptDS.URI, getConstants().identifiableArtefactUri());
        ViewTextItem urn = new ViewTextItem(ConceptDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(ConceptDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        identifiersEditionForm.setFields(code, name, pluralName, acronym, uri, urn, urnProvider);

        // Content descriptors
        contentDescriptorsEditionForm = new GroupDynamicForm(getConstants().conceptContentDescriptors());
        MultiLanguageTextAreaItem description = new MultiLanguageTextAreaItem(ConceptDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        MultiLanguageTextItem descriptionSource = new MultiLanguageTextItem(ConceptDS.DESCRIPTION_SOURCE, getConstants().conceptDescriptionSource());
        MultiLanguageTextItem context = new MultiLanguageTextItem(ConceptDS.CONTEXT, getConstants().conceptContext());
        MultilanguageRichTextEditorItem docMethod = new MultilanguageRichTextEditorItem(ConceptDS.DOC_METHOD, getConstants().conceptDocMethod());
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
                    uiHandlers.retrieveCodeLists(conceptDto.getUrn());
                }
                return isRepresentationEnumerated;
            }
        });
        contentDescriptorsEditionForm.setFields(description, descriptionSource, context, docMethod, representation, enumeratedRepresentation);

        // Non enumerated representation
        facetEditionForm = new ConceptFacetForm();

        // Class descriptors
        classDescriptorsEditionForm = new GroupDynamicForm(getConstants().conceptClassDescriptors());
        RequiredSelectItem sdmxRelatedArtefact = new RequiredSelectItem(ConceptDS.SDMX_RELATED_ARTEFACT, getConstants().conceptSdmxRelatedArtefact());
        sdmxRelatedArtefact.setValueMap(CommonUtils.getConceptRoleHashMap());
        sdmxRelatedArtefact.setShowIfCondition(getSdmxRelatedArtefactFormItemIfFunction());
        SelectItem type = new SelectItem(ConceptDS.TYPE, getConstants().conceptType()); // Value map set in setConceptTypes method
        // TODO Roles
        classDescriptorsEditionForm.setFields(sdmxRelatedArtefact, type);

        // Production descriptors
        productionDescriptorsEditionForm = new GroupDynamicForm(getConstants().conceptProductionDescriptors());
        MultiLanguageTextItem derivation = new MultiLanguageTextItem(ConceptDS.DERIVATION, getConstants().conceptDerivation());
        productionDescriptorsEditionForm.setFields(derivation);

        // Relation between concepts
        relationBetweenConceptsEditionForm = new GroupDynamicForm(getConstants().conceptRelationBetweenConcepts());
        ViewTextItem extendsConcept = new ViewTextItem(ConceptDS.EXTENDS, getConstants().conceptExtends()); // TODO extends
        ConceptsListItem relatedConcepts = createRelatedConceptsItem(ConceptDS.RELATED_CONCEPTS, getConstants().conceptRelatedConcepts());
        relationBetweenConceptsEditionForm.setFields(extendsConcept, relatedConcepts);

        // Legal acts
        legalActsEditionForm = new GroupDynamicForm(getConstants().conceptLegalActs());
        MultilanguageRichTextEditorItem legalActs = new MultilanguageRichTextEditorItem(ConceptDS.LEGAL_ACTS, getConstants().conceptLegalActs());
        legalActsEditionForm.setFields(legalActs);

        // Annotations
        annotationsEditionPanel = new AnnotationsPanel(false);

        mainFormLayout.addEditionCanvas(identifiersEditionForm);
        mainFormLayout.addEditionCanvas(contentDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(facetEditionForm);
        mainFormLayout.addEditionCanvas(classDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(productionDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(relationBetweenConceptsEditionForm);
        mainFormLayout.addEditionCanvas(legalActsEditionForm);
        mainFormLayout.addEditionCanvas(annotationsEditionPanel);
    }

    @Override
    public void setConcept(ConceptMetamacDto conceptDto, List<ConceptMetamacDto> relatedConcepts) {
        this.conceptDto = conceptDto;

        uiHandlers.retrieveConceptListByScheme(conceptDto.getItemSchemeVersionUrn());

        // Load value lists
        uiHandlers.retrieveConceptTypes();

        String defaultLocalized = InternationalStringUtils.getLocalisedString(conceptDto.getName());
        String title = defaultLocalized != null ? defaultLocalized : StringUtils.EMPTY;
        mainFormLayout.setTitleLabelContents(title);

        mainFormLayout.setViewMode();

        setConceptViewMode(conceptDto, relatedConcepts);
        setConceptEditionMode(conceptDto, relatedConcepts);
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

    private void setConceptViewMode(ConceptMetamacDto conceptDto, List<ConceptMetamacDto> relatedConcepts) {
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
        // TODO roles

        // Production descriptors
        productionDescriptorsForm.setValue(ConceptDS.DERIVATION, RecordUtils.getInternationalStringRecord(conceptDto.getDerivation()));

        // Relation between concepts
        // TODO Extends
        ((ConceptsListItem) relationBetweenConceptsForm.getItem(ConceptDS.RELATED_CONCEPTS)).setDataConcepts(relatedConcepts);

        // Legal acts
        legalActsForm.setValue(ConceptDS.LEGAL_ACTS, RecordUtils.getInternationalStringRecord(conceptDto.getLegalActs()));

        // Annotations
        annotationsPanel.setAnnotations(conceptDto.getAnnotations());
    }

    private void setConceptEditionMode(ConceptMetamacDto conceptDto, List<ConceptMetamacDto> relatedConcepts) {
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
        // TODO roles

        // Production descriptors
        productionDescriptorsEditionForm.setValue(ConceptDS.DERIVATION, RecordUtils.getInternationalStringRecord(conceptDto.getDerivation()));

        // Relation between concepts
        // TODO Extends
        ((ConceptsListItem) relationBetweenConceptsEditionForm.getItem(ConceptDS.RELATED_CONCEPTS)).setDataConcepts(relatedConcepts);

        // Legal acts
        legalActsEditionForm.setValue(ConceptDS.LEGAL_ACTS, RecordUtils.getInternationalStringRecord(conceptDto.getLegalActs()));

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
        conceptDto.setSdmxRelatedArtefact(ConceptRoleEnum.valueOf(classDescriptorsEditionForm.getValueAsString(ConceptDS.SDMX_RELATED_ARTEFACT)));
        conceptDto.setType(classDescriptorsEditionForm.getValue(ConceptDS.TYPE) != null ? getConceptTypeDto(classDescriptorsEditionForm.getValueAsString(ConceptDS.TYPE)) : null);
        // TODO roles

        // Production descriptors
        conceptDto.setDerivation((InternationalStringDto) productionDescriptorsEditionForm.getValue(ConceptDS.DERIVATION));

        // Relation between concepts
        // TODO Extends
        // Related concepts get in getRelatedConcepts method

        // Legal acts
        conceptDto.setLegalActs((InternationalStringDto) legalActsEditionForm.getValue(ConceptDS.LEGAL_ACTS));

        // Annotations
        conceptDto.getAnnotations().clear();
        conceptDto.getAnnotations().addAll(annotationsEditionPanel.getAnnotations());

        return conceptDto;
    }

    public List<String> getRelatedConcepts() {
        return ((ConceptsListItem) relationBetweenConceptsEditionForm.getItem(ConceptDS.RELATED_CONCEPTS)).getConceptUrns();
    }

    private ConceptTypeDto getConceptTypeDto(String identifier) {
        for (ConceptTypeDto conceptTypeDto : conceptTypeDtos) {
            if (StringUtils.equals(identifier, conceptTypeDto.getIdentifier())) {
                return conceptTypeDto;
            }
        }
        return null;
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
                    return ConceptSchemeTypeEnum.OPERATION.equals(conceptSchemeMetamacDto.getType()) || ConceptSchemeTypeEnum.TRANSVERSAL.equals(conceptSchemeMetamacDto.getType());
                }
                return false;
            }
        };
    }

}
