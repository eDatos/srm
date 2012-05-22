package org.siemac.metamac.internal.web.concept.view;

import static org.siemac.metamac.internal.web.client.MetamacInternalWeb.getMessages;
import static org.siemac.metamac.internal.web.client.MetamacInternalWeb.getConstants;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;
import org.siemac.metamac.internal.web.client.utils.ClientSecurityUtils;
import org.siemac.metamac.internal.web.concept.model.ds.ConceptSchemeDS;
import org.siemac.metamac.internal.web.concept.model.ds.ConceptSchemeProcStatusEnum;
import org.siemac.metamac.internal.web.concept.presenter.ConceptSchemePresenter;
import org.siemac.metamac.internal.web.concept.view.handlers.ConceptSchemeUiHandlers;
import org.siemac.metamac.internal.web.concept.widgets.ConceptSchemeMainFormLayout;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.InformationWindow;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.ViewImpl;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class ConceptSchemeViewImpl extends ViewImpl implements ConceptSchemePresenter.ConceptSchemeView, HasUiHandlers<ConceptSchemeUiHandlers> {

    private ConceptSchemeUiHandlers uiHandlers;
    private VLayout panel;
    private ConceptSchemeMainFormLayout mainFormLayout;
    
    /* View form */
    private GroupDynamicForm        identifiersForm;
    
    /* Edit form */
    private GroupDynamicForm        identifiersEditionForm;
    
    /* Data */
    private ConceptSchemeDto conceptScheme;
    
    
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
    
    public void bindEvents() {
        mainFormLayout.getTranslateToolStripButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                boolean translationShowed = mainFormLayout.getTranslateToolStripButton().isSelected();
                identifiersForm.setTranslationsShowed(translationShowed);
                identifiersEditionForm.setTranslationsShowed(translationShowed);
            }
        });
        
        // Edit: Add a custom handler to check scheme status before start editing
        mainFormLayout.getEditToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                //TODO: get scheme proc status
                ConceptSchemeProcStatusEnum status = ConceptSchemeProcStatusEnum.DRAFT;
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
        
        //Life cycle
        mainFormLayout.getSendToPendingPublication().addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.sendToPendingPublication(conceptScheme.getUuid());
            }
        });
        
        mainFormLayout.getRejectValidation().addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.rejectValidation(conceptScheme.getUuid());
                
            }
        });
        
        mainFormLayout.getPublishInternally().addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.publishInternally(conceptScheme.getUuid());
                
            }
        });
        
        mainFormLayout.getPublishExternally().addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.publishExternally(conceptScheme.getUuid());
                
            }
        });
        
        mainFormLayout.getVersioning().addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.versioning(conceptScheme.getUuid());
                
            }
        });
        
    }
    
    
    //TODO: PROCSTATUS
    public void createViewForm() {
        // Identifiers Form
        identifiersForm = new GroupDynamicForm(getConstants().conceptSchemeDetailIdentifiers());
        ViewTextItem idLogic = new ViewTextItem(ConceptSchemeDS.ID_LOGIC, getConstants().conceptSchemeDetailIdentifier());
        ViewTextItem uuid = new ViewTextItem(ConceptSchemeDS.UUID, getConstants().conceptSchemeDetailUuid());
        //ViewTextItem version = new ViewTextItem(ConceptSchemeDS., getConstants().indicDetailVersion());
        //ViewTextItem procStatus = new ViewTextItem(IndicatorDS.PROC_STATUS, getConstants().indicDetailProcStatus());
        ViewMultiLanguageTextItem title = new ViewMultiLanguageTextItem(ConceptSchemeDS.NAME, getConstants().conceptSchemeDetailTitle());
        identifiersForm.setFields(idLogic, uuid, /*version, procStatus,*/ title);
        
        mainFormLayout.addViewCanvas(identifiersEditionForm);
    }
    
    //TODO: PROCSTATUS
    public void createEditionForm() {
     // Identifiers Form
        identifiersEditionForm = new GroupDynamicForm(getConstants().conceptSchemeDetailIdentifiers());
        ViewTextItem idLogic = new ViewTextItem(ConceptSchemeDS.ID_LOGIC, getConstants().conceptSchemeDetailIdentifier());
        ViewTextItem uuid = new ViewTextItem(ConceptSchemeDS.UUID, getConstants().conceptSchemeDetailUuid());
        /*ViewTextItem version = new ViewTextItem(IndicatorDS.VERSION_NUMBER, getConstants().indicDetailVersion());
        ViewTextItem procStatus = new ViewTextItem(IndicatorDS.PROC_STATUS, getConstants().indicDetailProcStatus());*/
        MultiLanguageTextItem title = new MultiLanguageTextItem(ConceptSchemeDS.NAME, getConstants().conceptSchemeDetailTitle());
        title.setRequired(true);
        identifiersEditionForm.setFields(idLogic, uuid, /*version, procStatus,*/ title);
        
        mainFormLayout.addEditionCanvas(identifiersEditionForm);
    }
    
    @Override
    public void setConceptScheme(ConceptSchemeDto conceptScheme) {
        this.conceptScheme = conceptScheme;
        
        //TODO: PROCSTATUS mainFormLayout.updatePublishSection(conceptScheme.getProcStatus());
        mainFormLayout.setViewMode();
        
        setConceptSchemeViewMode(conceptScheme);
        setConceptSchemeEditionMode(conceptScheme);
    }
    
    public void setEditionMode() {
        mainFormLayout.setEditionMode();
    }
    
    public void setConceptSchemeViewMode(ConceptSchemeDto conceptSchemeDto) {
        identifiersForm.setValue(ConceptSchemeDS.ID_LOGIC, conceptSchemeDto.getIdLogic());
        identifiersForm.setValue(ConceptSchemeDS.UUID, conceptSchemeDto.getUuid());
        identifiersForm.setValue(ConceptSchemeDS.NAME, RecordUtils.getInternationalStringRecord(conceptSchemeDto.getName()));
    }
    
    public void setConceptSchemeEditionMode(ConceptSchemeDto conceptSchemeDto) {
        identifiersEditionForm.setValue(ConceptSchemeDS.ID_LOGIC, conceptSchemeDto.getIdLogic());
        identifiersEditionForm.setValue(ConceptSchemeDS.UUID, conceptSchemeDto.getUuid());
        identifiersEditionForm.setValue(ConceptSchemeDS.NAME, RecordUtils.getInternationalStringRecord(conceptSchemeDto.getName()));
    }
    
    public void saveConceptScheme() {
        if (identifiersEditionForm.validate(false)) {
            conceptScheme.setName((InternationalStringDto)identifiersEditionForm.getValue(ConceptSchemeDS.NAME));
            uiHandlers.saveConceptScheme(conceptScheme);
        }
    }
    
    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setUiHandlers(ConceptSchemeUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

}

