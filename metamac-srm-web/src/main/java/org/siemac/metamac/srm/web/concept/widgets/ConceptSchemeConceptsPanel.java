package org.siemac.metamac.srm.web.concept.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.srm.core.concept.domain.shared.ConceptMetamacVisualisationResult;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.web.concept.utils.CommonUtils;
import org.siemac.metamac.srm.web.concept.utils.ConceptsClientSecurityUtils;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptSchemeUiHandlers;
import org.siemac.metamac.web.common.client.listener.UploadListener;
import org.siemac.metamac.web.common.client.widgets.CustomToolStripButton;
import org.siemac.metamac.web.common.client.widgets.InformationLabel;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class ConceptSchemeConceptsPanel extends VLayout {

    private ToolStrip               toolStrip;
    private ToolStripButton         importConceptsButton;
    private ToolStripButton         exportConceptsButton;
    private ImportConceptsWindow    importConceptsWindow;

    private final InformationLabel  conceptsNoVisibleInfoMessage;
    private final ConceptsTreeGrid  conceptsTreeGrid;

    private ConceptSchemeUiHandlers uiHandlers;

    private ConceptSchemeMetamacDto conceptSchemeDto;

    public ConceptSchemeConceptsPanel() {
        setMargin(15);

        importConceptsWindow = new ImportConceptsWindow();
        importConceptsWindow.setUploadListener(new UploadListener() {

            @Override
            public void uploadFailed(String errorMessage) {
                uiHandlers.resourceImportationFailed(errorMessage);
            }
            @Override
            public void uploadComplete(String fileName) {
                uiHandlers.resourceImportationSucceed(fileName);
            }
        });

        toolStrip = new ToolStrip();

        importConceptsButton = new CustomToolStripButton(getConstants().actionImportConcepts(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.importResource().getURL());
        importConceptsButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                importConceptsWindow.show();
            }
        });
        toolStrip.addButton(importConceptsButton);

        exportConceptsButton = new CustomToolStripButton(getConstants().actionExportConcepts(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.exportResource().getURL());
        exportConceptsButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.exportConcepts(conceptSchemeDto.getUrn());
            }
        });
        toolStrip.addButton(exportConceptsButton);

        conceptsNoVisibleInfoMessage = new InformationLabel(getConstants().conceptSchemeConceptsNoVisibleInfoMessage());
        conceptsNoVisibleInfoMessage.setMargin(10);

        conceptsTreeGrid = new ConceptsTreeGrid();

        addMember(conceptsNoVisibleInfoMessage);
        addMember(toolStrip);
        addMember(conceptsTreeGrid);
    }

    public void updateItemScheme(ConceptSchemeMetamacDto conceptSchemeDto) {
        this.conceptSchemeDto = conceptSchemeDto;

        // Update concept scheme in tree grid
        conceptsTreeGrid.updateItemScheme(conceptSchemeDto);
        // The concepts scheme type can be null (if have been imported). Do not show concepts until the concept scheme type is specified.
        if (conceptSchemeDto.getType() == null) {
            conceptsNoVisibleInfoMessage.show();
            conceptsTreeGrid.hide();
        } else {
            conceptsNoVisibleInfoMessage.hide();
            conceptsTreeGrid.show();
        }

        updateButtonsVisibility(conceptSchemeDto);
        importConceptsWindow.setConceptScheme(conceptSchemeDto);
    }

    public void setConcepts(ConceptSchemeMetamacDto conceptSchemeDto, List<ConceptMetamacVisualisationResult> itemHierarchyDtos) {
        this.conceptSchemeDto = conceptSchemeDto;
        conceptsTreeGrid.setConcepts(conceptSchemeDto, itemHierarchyDtos);
        // Set the max records to the size of the items list (plus the item scheme node)
        conceptsTreeGrid.setAutoFitMaxRecords(itemHierarchyDtos.size() + 1);
    }

    public void setUiHandlers(ConceptSchemeUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
        conceptsTreeGrid.setUiHandlers(uiHandlers);
        importConceptsWindow.setUiHandlers(uiHandlers);
    }

    private void updateButtonsVisibility(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        updateImportConceptsButtonVisibility(conceptSchemeMetamacDto);
        updateExportConceptsButtonVisibility(conceptSchemeMetamacDto);
    }

    private void updateImportConceptsButtonVisibility(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        if (ConceptsClientSecurityUtils.canImportConcepts(conceptSchemeMetamacDto, CommonUtils.getRelatedOperationCode(conceptSchemeMetamacDto))) {
            importConceptsButton.show();
        } else {
            importConceptsButton.hide();
        }
    }

    private void updateExportConceptsButtonVisibility(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        if (ConceptsClientSecurityUtils.canExportConcepts(conceptSchemeMetamacDto)) {
            exportConceptsButton.show();
        } else {
            exportConceptsButton.hide();
        }
    }
}
