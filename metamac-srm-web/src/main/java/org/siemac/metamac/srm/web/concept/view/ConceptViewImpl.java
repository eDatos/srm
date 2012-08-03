package org.siemac.metamac.srm.web.concept.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.concept.dto.MetamacConceptDto;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;
import org.siemac.metamac.srm.web.concept.presenter.ConceptPresenter;
import org.siemac.metamac.srm.web.concept.utils.ConceptClientSecurityUtils;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptUiHandlers;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultilanguageRichTextEditorItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class ConceptViewImpl extends ViewImpl implements ConceptPresenter.ConceptView {

    private ConceptUiHandlers           uiHandlers;

    private VLayout                     panel;
    private InternationalMainFormLayout mainFormLayout;

    // View forms
    private GroupDynamicForm            identifiersForm;
    private GroupDynamicForm            contentDescriptorsForm;
    private GroupDynamicForm            classDescriptorsForm;
    private GroupDynamicForm            productionDescriptorsForm;
    private GroupDynamicForm            relationBetweenConceptsForm;
    private GroupDynamicForm            legalActsForm;

    // Edition forms
    private GroupDynamicForm            identifiersEditionForm;
    private GroupDynamicForm            contentDescriptorsEditionForm;
    private GroupDynamicForm            classDescriptorsEditionForm;
    private GroupDynamicForm            productionDescriptorsEditionForm;
    private GroupDynamicForm            relationBetweenConceptsEditionForm;
    private GroupDynamicForm            legalActsEditionForm;

    private MetamacConceptDto           conceptDto;

    @Inject
    public ConceptViewImpl() {
        super();
        panel = new VLayout();
        panel.setHeight100();
        panel.setOverflow(Overflow.SCROLL);
        mainFormLayout = new InternationalMainFormLayout(ConceptClientSecurityUtils.canEditConcept());

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
            }
        });

        // Save
        mainFormLayout.getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (identifiersEditionForm.validate(false) && contentDescriptorsEditionForm.validate(false) && classDescriptorsEditionForm.validate(false)
                        && productionDescriptorsEditionForm.validate(false) && relationBetweenConceptsEditionForm.validate(false) && legalActsEditionForm.validate(false)) {
                    uiHandlers.saveConcept(getConceptDto());
                }
            }
        });

        createViewForm();
        createEditionForm();

        panel.addMember(mainFormLayout);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setUiHandlers(ConceptUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
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
        ViewTextItem code = new ViewTextItem(ConceptDS.CODE, getConstants().conceptCode());
        ViewMultiLanguageTextItem name = new ViewMultiLanguageTextItem(ConceptDS.NAME, getConstants().conceptName());
        ViewMultiLanguageTextItem pluralName = new ViewMultiLanguageTextItem(ConceptDS.PLURAL_NAME, getConstants().conceptPluralName());
        ViewMultiLanguageTextItem acronym = new ViewMultiLanguageTextItem(ConceptDS.ACRONYM, getConstants().conceptAcronym());
        ViewTextItem uri = new ViewTextItem(ConceptDS.URI, getConstants().conceptUri());
        ViewTextItem urn = new ViewTextItem(ConceptDS.URN, getConstants().conceptUrn());
        identifiersForm.setFields(code, name, pluralName, acronym, uri, urn);

        // Content descriptors
        contentDescriptorsForm = new GroupDynamicForm(getConstants().conceptContentDescriptors());
        ViewMultiLanguageTextItem description = new ViewMultiLanguageTextItem(ConceptDS.DESCRIPTION, getConstants().conceptDescription());
        ViewMultiLanguageTextItem descriptionSource = new ViewMultiLanguageTextItem(ConceptDS.DESCRIPTION_SOURCE, getConstants().conceptDescriptionSource());
        ViewMultiLanguageTextItem context = new ViewMultiLanguageTextItem(ConceptDS.CONTEXT, getConstants().conceptContext());
        ViewMultiLanguageTextItem docMethod = new ViewMultiLanguageTextItem(ConceptDS.DOC_METHOD, getConstants().conceptDocMethod());
        contentDescriptorsForm.setFields(description, descriptionSource, context, docMethod);

        // Class descriptors
        classDescriptorsForm = new GroupDynamicForm(getConstants().conceptClassDescriptors());
        ViewTextItem sdmxRelatedArtefact = new ViewTextItem(ConceptDS.SDMX_RELATED_ARTEFACT, getConstants().conceptSdmxRelatedArtefact());
        ViewTextItem type = new ViewTextItem(ConceptDS.TYPE, getConstants().conceptType());
        ViewTextItem roles = new ViewTextItem(ConceptDS.ROLES, getConstants().conceptRoles());
        classDescriptorsForm.setFields(sdmxRelatedArtefact, type, roles);

        // Production descriptors
        productionDescriptorsForm = new GroupDynamicForm(getConstants().conceptProductionDescriptors());
        ViewMultiLanguageTextItem derivation = new ViewMultiLanguageTextItem(ConceptDS.DERIVATION, getConstants().conceptDerivation());
        productionDescriptorsForm.setFields(derivation);

        // Relation between concepts
        relationBetweenConceptsForm = new GroupDynamicForm(getConstants().conceptRelationBetweenConcepts());
        ViewTextItem extendsConcept = new ViewTextItem(ConceptDS.EXTENDS, getConstants().conceptExtends());
        ViewTextItem relatedConcepts = new ViewTextItem(ConceptDS.RELATED_CONCEPTS, getConstants().conceptRelatedConcepts());
        relationBetweenConceptsForm.setFields(extendsConcept, relatedConcepts);

        // Legal acts
        legalActsForm = new GroupDynamicForm(getConstants().conceptLegalActs());
        ViewMultiLanguageTextItem legalActs = new ViewMultiLanguageTextItem(ConceptDS.LEGAL_ACTS, getConstants().conceptLegalActs());
        legalActsForm.setFields(legalActs);

        mainFormLayout.addViewCanvas(identifiersForm);
        mainFormLayout.addViewCanvas(contentDescriptorsForm);
        mainFormLayout.addViewCanvas(classDescriptorsForm);
        mainFormLayout.addViewCanvas(productionDescriptorsForm);
        mainFormLayout.addViewCanvas(relationBetweenConceptsForm);
        mainFormLayout.addViewCanvas(legalActsForm);
    }

    private void createEditionForm() {
        // Identifiers Form
        identifiersEditionForm = new GroupDynamicForm(getConstants().conceptIdentifiers());
        RequiredTextItem code = new RequiredTextItem(ConceptDS.CODE, getConstants().conceptCode());
        MultiLanguageTextItem name = new MultiLanguageTextItem(ConceptDS.NAME, getConstants().conceptName());
        name.setRequired(true);
        MultiLanguageTextItem pluralName = new MultiLanguageTextItem(ConceptDS.PLURAL_NAME, getConstants().conceptPluralName());
        MultiLanguageTextItem acronym = new MultiLanguageTextItem(ConceptDS.ACRONYM, getConstants().conceptAcronym());
        ViewTextItem uri = new ViewTextItem(ConceptDS.URI, getConstants().conceptUri());
        ViewTextItem urn = new ViewTextItem(ConceptDS.URN, getConstants().conceptUrn());
        identifiersEditionForm.setFields(code, name, pluralName, acronym, uri, urn);

        // Content descriptors
        contentDescriptorsEditionForm = new GroupDynamicForm(getConstants().conceptContentDescriptors());
        MultiLanguageTextItem description = new MultiLanguageTextItem(ConceptDS.DESCRIPTION, getConstants().conceptDescription());
        MultiLanguageTextItem descriptionSource = new MultiLanguageTextItem(ConceptDS.DESCRIPTION_SOURCE, getConstants().conceptDescriptionSource());
        MultiLanguageTextItem context = new MultiLanguageTextItem(ConceptDS.CONTEXT, getConstants().conceptContext());
        MultilanguageRichTextEditorItem docMethod = new MultilanguageRichTextEditorItem(ConceptDS.DOC_METHOD, getConstants().conceptDocMethod());
        contentDescriptorsEditionForm.setFields(description, descriptionSource, context, docMethod);

        // Class descriptors
        classDescriptorsEditionForm = new GroupDynamicForm(getConstants().conceptClassDescriptors());

        // Production descriptors
        productionDescriptorsEditionForm = new GroupDynamicForm(getConstants().conceptProductionDescriptors());
        MultiLanguageTextItem derivation = new MultiLanguageTextItem(ConceptDS.DERIVATION, getConstants().conceptDerivation());
        productionDescriptorsEditionForm.setFields(derivation);

        // Relation between concepts
        relationBetweenConceptsEditionForm = new GroupDynamicForm(getConstants().conceptRelationBetweenConcepts());

        // Legal acts
        legalActsEditionForm = new GroupDynamicForm(getConstants().conceptLegalActs());
        MultilanguageRichTextEditorItem legalActs = new MultilanguageRichTextEditorItem(ConceptDS.LEGAL_ACTS, getConstants().conceptLegalActs());
        legalActsEditionForm.setFields(legalActs);

        mainFormLayout.addEditionCanvas(identifiersEditionForm);
        mainFormLayout.addEditionCanvas(contentDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(classDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(productionDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(relationBetweenConceptsEditionForm);
        mainFormLayout.addEditionCanvas(legalActsEditionForm);
    }
    @Override
    public void setConcept(MetamacConceptDto conceptDto) {
        this.conceptDto = conceptDto;

        String defaultLocalized = InternationalStringUtils.getLocalisedString(conceptDto.getName());
        String title = defaultLocalized != null ? defaultLocalized : StringUtils.EMPTY;
        mainFormLayout.setTitleLabelContents(title);

        mainFormLayout.setViewMode();

        setConceptViewMode(conceptDto);
        setConceptEditionMode(conceptDto);
    }

    private void setConceptViewMode(MetamacConceptDto conceptDto) {
        // Identifiers Form
        identifiersForm.setValue(ConceptDS.CODE, conceptDto.getCode());
        identifiersForm.setValue(ConceptDS.NAME, RecordUtils.getInternationalStringRecord(conceptDto.getName()));
        identifiersForm.setValue(ConceptDS.PLURAL_NAME, RecordUtils.getInternationalStringRecord(conceptDto.getPluralName()));
        identifiersForm.setValue(ConceptDS.ACRONYM, RecordUtils.getInternationalStringRecord(conceptDto.getAcronym()));
        identifiersForm.setValue(ConceptDS.URI, conceptDto.getUri());
        identifiersForm.setValue(ConceptDS.URN, conceptDto.getUrn());

        // Content descriptors
        contentDescriptorsForm.setValue(ConceptDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(conceptDto.getDescription()));
        contentDescriptorsForm.setValue(ConceptDS.DESCRIPTION_SOURCE, RecordUtils.getInternationalStringRecord(conceptDto.getDescriptionSource()));
        contentDescriptorsForm.setValue(ConceptDS.CONTEXT, RecordUtils.getInternationalStringRecord(conceptDto.getContext()));
        contentDescriptorsForm.setValue(ConceptDS.DOC_METHOD, RecordUtils.getInternationalStringRecord(conceptDto.getDocMethod()));

        // Class descriptors

        // Production descriptors
        productionDescriptorsForm.setValue(ConceptDS.DERIVATION, RecordUtils.getInternationalStringRecord(conceptDto.getDerivation()));

        // Relation between concepts

        // Legal acts
        legalActsForm.setValue(ConceptDS.LEGAL_ACTS, RecordUtils.getInternationalStringRecord(conceptDto.getLegalActs()));

    }

    private void setConceptEditionMode(MetamacConceptDto conceptDto) {
        // Identifiers Form
        identifiersEditionForm.setValue(ConceptDS.CODE, conceptDto.getCode());
        identifiersEditionForm.setValue(ConceptDS.NAME, RecordUtils.getInternationalStringRecord(conceptDto.getName()));
        identifiersEditionForm.setValue(ConceptDS.PLURAL_NAME, RecordUtils.getInternationalStringRecord(conceptDto.getPluralName()));
        identifiersEditionForm.setValue(ConceptDS.ACRONYM, RecordUtils.getInternationalStringRecord(conceptDto.getAcronym()));
        identifiersEditionForm.setValue(ConceptDS.URI, conceptDto.getUri());
        identifiersEditionForm.setValue(ConceptDS.URN, conceptDto.getUrn());

        // Content descriptors
        contentDescriptorsEditionForm.setValue(ConceptDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(conceptDto.getDescription()));
        contentDescriptorsEditionForm.setValue(ConceptDS.DESCRIPTION_SOURCE, RecordUtils.getInternationalStringRecord(conceptDto.getDescriptionSource()));
        contentDescriptorsEditionForm.setValue(ConceptDS.CONTEXT, RecordUtils.getInternationalStringRecord(conceptDto.getContext()));
        contentDescriptorsEditionForm.setValue(ConceptDS.DOC_METHOD, RecordUtils.getInternationalStringRecord(conceptDto.getDocMethod()));

        // Class descriptors

        // Production descriptors
        productionDescriptorsEditionForm.setValue(ConceptDS.DERIVATION, RecordUtils.getInternationalStringRecord(conceptDto.getDerivation()));

        // Relation between concepts

        // Legal acts
        legalActsEditionForm.setValue(ConceptDS.LEGAL_ACTS, RecordUtils.getInternationalStringRecord(conceptDto.getLegalActs()));
    }

    private MetamacConceptDto getConceptDto() {
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

        // Class descriptors

        // Production descriptors
        conceptDto.setDerivation((InternationalStringDto) productionDescriptorsEditionForm.getValue(ConceptDS.DERIVATION));

        // Relation between concepts

        // Legal acts
        conceptDto.setLegalActs((InternationalStringDto) legalActsEditionForm.getValue(ConceptDS.LEGAL_ACTS));

        return conceptDto;
    }

}
