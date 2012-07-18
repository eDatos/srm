package org.siemac.metamac.srm.web.concept.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;
import org.siemac.metamac.domain.concept.enums.domain.ConceptSchemeProcStatusEnum;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.utils.ClientSecurityUtils;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeDS;
import org.siemac.metamac.srm.web.concept.presenter.ConceptSchemePresenter;
import org.siemac.metamac.srm.web.concept.utils.CommonUtils;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptSchemeUiHandlers;
import org.siemac.metamac.srm.web.concept.widgets.ConceptSchemeMainFormLayout;
import org.siemac.metamac.web.common.client.utils.DateUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.InformationWindow;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.BooleanItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class ConceptSchemeViewImpl extends ViewImpl implements ConceptSchemePresenter.ConceptSchemeView {

    private ConceptSchemeUiHandlers     uiHandlers;

    private VLayout                     panel;
    private ConceptSchemeMainFormLayout mainFormLayout;

    // View forms
    private GroupDynamicForm            identifiersForm;
    private GroupDynamicForm            contentDescriptorsForm;
    private GroupDynamicForm            classDescriptorsForm;
    private GroupDynamicForm            productionDescriptorsForm;
    private GroupDynamicForm            diffusionDescriptorsForm;

    // Edition forms
    private GroupDynamicForm            identifiersEditionForm;
    private GroupDynamicForm            contentDescriptorsEditionForm;
    private GroupDynamicForm            classDescriptorsEditionForm;
    private GroupDynamicForm            productionDescriptorsEditionForm;
    private GroupDynamicForm            diffusionDescriptorsEditionForm;

    private ConceptSchemeDto            conceptScheme;

    @Inject
    public ConceptSchemeViewImpl() {
        super();

        mainFormLayout = new ConceptSchemeMainFormLayout(ClientSecurityUtils.canEditConceptScheme());

        createViewForm();
        createEditionForm();

        panel = new VLayout();
        panel.addMember(mainFormLayout);
        bindEvents();
    }

    /* View specific functions */

    /*
     * GWTP will call setInSlot when a child presenter asks to be added under this view
     */
    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == ConceptSchemePresenter.TYPE_SetContextAreaContentConceptSchemeToolBar) {
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

    @Override
    public Widget asWidget() {
        return panel;
    }

    /* logic related functions */

    @Override
    public void setUiHandlers(ConceptSchemeUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    @Override
    public void setConceptScheme(ConceptSchemeDto conceptScheme) {
        this.conceptScheme = conceptScheme;

        String defaultLocalized = InternationalStringUtils.getLocalisedString(conceptScheme.getName());
        String title = defaultLocalized != null ? defaultLocalized : StringUtils.EMPTY;
        mainFormLayout.setTitleLabelContents(title);

        mainFormLayout.updatePublishSection(conceptScheme.getProcStatus());
        mainFormLayout.setViewMode();

        setConceptSchemeViewMode(conceptScheme);
        setConceptSchemeEditionMode(conceptScheme);
    }

    private void bindEvents() {
        mainFormLayout.getTranslateToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                boolean translationShowed = mainFormLayout.getTranslateToolStripButton().isSelected();
                identifiersForm.setTranslationsShowed(translationShowed);
                identifiersEditionForm.setTranslationsShowed(translationShowed);

                contentDescriptorsForm.setTranslationsShowed(translationShowed);
                contentDescriptorsEditionForm.setTranslationsShowed(translationShowed);
            }
        });

        // Edit: Add a custom handler to check scheme status before start editing
        mainFormLayout.getEditToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                // TODO: get scheme proc status
                ConceptSchemeProcStatusEnum status = conceptScheme.getProcStatus();
                if (ConceptSchemeProcStatusEnum.INTERNALLY_PUBLISHED.equals(status) || ConceptSchemeProcStatusEnum.EXTERNALLY_PUBLISHED.equals(status)) {
                    // Create a new version
                    final InformationWindow window = new InformationWindow(getMessages().conceptSchemeEditionInfo(), getMessages().conceptSchemeEditionInfoDetailedMessage());
                    window.show();
                } else {
                    // Default behavior
                    setEditionMode();
                }
            }
        });

        // Save
        mainFormLayout.getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                saveConceptScheme();
            }
        });

        // Life cycle
        mainFormLayout.getSendToPendingPublication().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.sendToPendingPublication(conceptScheme.getId());
            }
        });

        mainFormLayout.getRejectValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.rejectValidation(conceptScheme.getId());

            }
        });

        mainFormLayout.getPublishInternally().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.publishInternally(conceptScheme.getId());

            }
        });

        mainFormLayout.getPublishExternally().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.publishExternally(conceptScheme.getId());

            }
        });

        mainFormLayout.getVersioning().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.versioning(conceptScheme.getId());

            }
        });

    }

    private void createViewForm() {
        // Identifiers Form
        identifiersForm = new GroupDynamicForm(getConstants().conceptSchemeIdentifiers());
        ViewTextItem idLogic = new ViewTextItem(ConceptSchemeDS.ID_LOGIC, getConstants().conceptSchemeIdLogic());
        ViewMultiLanguageTextItem title = new ViewMultiLanguageTextItem(ConceptSchemeDS.NAME, getConstants().conceptSchemeTitle());
        ViewTextItem uri = new ViewTextItem(ConceptSchemeDS.URI, getConstants().conceptSchemeUri());
        ViewTextItem urn = new ViewTextItem(ConceptSchemeDS.URN, getConstants().conceptSchemeUrn());
        ViewTextItem version = new ViewTextItem(ConceptSchemeDS.VERSION, getConstants().conceptSchemeVersion());
        identifiersForm.setFields(idLogic, title, uri, urn, version);

        contentDescriptorsForm = new GroupDynamicForm(getConstants().conceptSchemeContentDescriptors());
        ViewMultiLanguageTextItem description = new ViewMultiLanguageTextItem(ConceptSchemeDS.DESCRIPTION, getConstants().conceptSchemeDescription());
        ViewTextItem partial = new ViewTextItem(ConceptSchemeDS.IS_PARTIAL, getConstants().conceptSchemePartial());
        contentDescriptorsForm.setFields(description, partial);

        classDescriptorsForm = new GroupDynamicForm(getConstants().conceptSchemeClassDescriptors());
        ViewTextItem agency = new ViewTextItem(ConceptSchemeDS.AGENCY, getConstants().conceptSchemeAgency());
        ViewTextItem type = new ViewTextItem(ConceptSchemeDS.TYPE, getConstants().conceptSchemeType());
        ViewTextItem operation = new ViewTextItem(ConceptSchemeDS.RELATED_OPERATION, getConstants().conceptSchemeOperation());
        classDescriptorsForm.setFields(type, operation, agency);

        productionDescriptorsForm = new GroupDynamicForm(getConstants().conceptSchemeProductionDescriptors());
        ViewTextItem procStatus = new ViewTextItem(ConceptSchemeDS.PROC_STATUS, getConstants().conceptSchemeProcStatus());
        productionDescriptorsForm.setFields(procStatus);

        diffusionDescriptorsForm = new GroupDynamicForm(getConstants().conceptSchemeDiffusionDescriptors());
        ViewTextItem startDate = new ViewTextItem(ConceptSchemeDS.VALID_FROM, getConstants().conceptSchemeStartDate());
        ViewTextItem endDate = new ViewTextItem(ConceptSchemeDS.VALID_TO, getConstants().conceptSchemeEndDate());
        diffusionDescriptorsForm.setFields(startDate, endDate);

        mainFormLayout.addViewCanvas(identifiersForm);
        mainFormLayout.addViewCanvas(contentDescriptorsForm);
        mainFormLayout.addViewCanvas(classDescriptorsForm);
        mainFormLayout.addViewCanvas(productionDescriptorsForm);
        mainFormLayout.addViewCanvas(diffusionDescriptorsForm);
    }

    private void createEditionForm() {
        // Identifiers Form
        identifiersEditionForm = new GroupDynamicForm(getConstants().conceptSchemeIdentifiers());
        ViewTextItem idLogic = new ViewTextItem(ConceptSchemeDS.ID_LOGIC, getConstants().conceptSchemeIdLogic());
        MultiLanguageTextItem title = new MultiLanguageTextItem(ConceptSchemeDS.NAME, getConstants().conceptSchemeTitle());
        title.setRequired(true);
        ViewTextItem uri = new ViewTextItem(ConceptSchemeDS.URI, getConstants().conceptSchemeUri());
        ViewTextItem urn = new ViewTextItem(ConceptSchemeDS.URN, getConstants().conceptSchemeUrn());
        ViewTextItem version = new ViewTextItem(ConceptSchemeDS.VERSION, getConstants().conceptSchemeVersion());
        identifiersEditionForm.setFields(idLogic, title, uri, urn, version);

        contentDescriptorsEditionForm = new GroupDynamicForm(getConstants().conceptSchemeContentDescriptors());
        MultiLanguageTextItem description = new MultiLanguageTextItem(ConceptSchemeDS.DESCRIPTION, getConstants().conceptSchemeDescription());
        BooleanItem partial = new BooleanItem(ConceptSchemeDS.IS_PARTIAL, getConstants().conceptSchemePartial());
        contentDescriptorsEditionForm.setFields(description, partial);

        classDescriptorsEditionForm = new GroupDynamicForm(getConstants().conceptSchemeClassDescriptors());
        SelectItem type = new SelectItem(ConceptSchemeDS.TYPE, getConstants().conceptSchemeType());
        SelectItem operation = new SelectItem(ConceptSchemeDS.RELATED_OPERATION, getConstants().conceptSchemeOperation());
        ViewTextItem agency = new ViewTextItem(ConceptSchemeDS.AGENCY, getConstants().conceptSchemeAgency());
        classDescriptorsEditionForm.setFields(type, operation, agency);

        productionDescriptorsEditionForm = new GroupDynamicForm(getConstants().conceptSchemeProductionDescriptors());
        ViewTextItem procStatus = new ViewTextItem(ConceptSchemeDS.PROC_STATUS, getConstants().conceptSchemeProcStatus());
        productionDescriptorsEditionForm.setFields(procStatus);

        diffusionDescriptorsEditionForm = new GroupDynamicForm(getConstants().conceptSchemeDiffusionDescriptors());
        ViewTextItem startDate = new ViewTextItem(ConceptSchemeDS.VALID_FROM, getConstants().conceptSchemeStartDate());
        ViewTextItem endDate = new ViewTextItem(ConceptSchemeDS.VALID_TO, getConstants().conceptSchemeEndDate());
        diffusionDescriptorsEditionForm.setFields(startDate, endDate);

        mainFormLayout.addEditionCanvas(identifiersEditionForm);
        mainFormLayout.addEditionCanvas(contentDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(classDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(productionDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(diffusionDescriptorsEditionForm);
    }

    public void setEditionMode() {
        mainFormLayout.setEditionMode();
    }

    public void setConceptSchemeViewMode(ConceptSchemeDto conceptSchemeDto) {
        identifiersForm.setValue(ConceptSchemeDS.ID_LOGIC, conceptSchemeDto.getIdLogic());
        identifiersForm.setValue(ConceptSchemeDS.URI, conceptSchemeDto.getUri());
        identifiersForm.setValue(ConceptSchemeDS.URN, conceptSchemeDto.getUrn());
        identifiersForm.setValue(ConceptSchemeDS.VERSION, conceptSchemeDto.getVersionLogic());
        identifiersForm.setValue(ConceptSchemeDS.NAME, RecordUtils.getInternationalStringRecord(conceptSchemeDto.getName()));

        contentDescriptorsForm.setValue(ConceptSchemeDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(conceptSchemeDto.getDescription()));
        contentDescriptorsForm.setValue(ConceptSchemeDS.IS_PARTIAL, (conceptSchemeDto.getIsPartial() != null && conceptSchemeDto.getIsPartial()) ? MetamacSrmWeb.getConstants().yes() : MetamacSrmWeb
                .getConstants().no());

        classDescriptorsForm.setValue(ConceptSchemeDS.AGENCY, conceptSchemeDto.getMaintainer() != null ? conceptSchemeDto.getMaintainer().getCode() : StringUtils.EMPTY);

        productionDescriptorsForm.setValue(ConceptSchemeDS.PROC_STATUS, CommonUtils.getConceptSchemeProcStatus(conceptSchemeDto));

        diffusionDescriptorsForm.setValue(ConceptSchemeDS.VALID_FROM, DateUtils.getFormattedDate(conceptSchemeDto.getValidFrom()));
        diffusionDescriptorsForm.setValue(ConceptSchemeDS.VALID_TO, DateUtils.getFormattedDate(conceptSchemeDto.getValidTo()));

        /*
         * statusForm.setValue(ConceptSchemeDS.FINAL, (conceptSchemeDto.getFinalLogic() != null && conceptSchemeDto.getFinalLogic()) ? MetamacSrmWeb.getConstants().yes() : MetamacSrmWeb.getConstants()
         * .no());
         * statusForm.setValue(ConceptSchemeDS.VALID_FROM, DateUtils.getFormattedDate(conceptSchemeDto.getValidFrom()));
         * statusForm.setValue(ConceptSchemeDS.VALID_TO, DateUtils.getFormattedDate(conceptSchemeDto.getValidTo()));
         */
    }

    public void setConceptSchemeEditionMode(ConceptSchemeDto conceptSchemeDto) {
        identifiersEditionForm.setValue(ConceptSchemeDS.ID_LOGIC, conceptSchemeDto.getIdLogic());
        identifiersEditionForm.setValue(ConceptSchemeDS.URI, conceptSchemeDto.getUri());
        identifiersEditionForm.setValue(ConceptSchemeDS.URN, conceptSchemeDto.getUrn());
        identifiersEditionForm.setValue(ConceptSchemeDS.VERSION, conceptSchemeDto.getVersionLogic());
        identifiersEditionForm.setValue(ConceptSchemeDS.NAME, RecordUtils.getInternationalStringRecord(conceptSchemeDto.getName()));

        contentDescriptorsEditionForm.setValue(ConceptSchemeDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(conceptSchemeDto.getDescription()));
        contentDescriptorsEditionForm.setValue(ConceptSchemeDS.IS_PARTIAL, conceptSchemeDto.getIsPartial() != null ? conceptSchemeDto.getIsPartial() : false);

        classDescriptorsEditionForm.setValue(ConceptSchemeDS.AGENCY, conceptSchemeDto.getMaintainer() != null ? conceptSchemeDto.getMaintainer().getCode() : StringUtils.EMPTY);

        productionDescriptorsEditionForm.setValue(ConceptSchemeDS.PROC_STATUS, CommonUtils.getConceptSchemeProcStatus(conceptSchemeDto));

        diffusionDescriptorsEditionForm.setValue(ConceptSchemeDS.VALID_FROM, DateUtils.getFormattedDate(conceptSchemeDto.getValidFrom()));
        diffusionDescriptorsEditionForm.setValue(ConceptSchemeDS.VALID_TO, DateUtils.getFormattedDate(conceptSchemeDto.getValidTo()));

        /*
         * statusEditionForm.setValue(ConceptSchemeDS.FINAL, (conceptSchemeDto.getFinalLogic() != null && conceptSchemeDto.getFinalLogic()) ? MetamacSrmWeb.getConstants().yes() : MetamacSrmWeb
         * .getConstants().no());
         */
    }

    public void saveConceptScheme() {
        if (identifiersEditionForm.validate(false)) {
            conceptScheme.setName((InternationalStringDto) identifiersEditionForm.getValue(ConceptSchemeDS.NAME));
            conceptScheme.setDescription((InternationalStringDto) contentDescriptorsEditionForm.getValue(ConceptSchemeDS.DESCRIPTION));
            conceptScheme.setIsPartial((Boolean) contentDescriptorsEditionForm.getValue(ConceptSchemeDS.IS_PARTIAL));
            uiHandlers.saveConceptScheme(conceptScheme);
        }
    }

}
