package org.siemac.metamac.srm.web.code.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.domain.shared.CodeVariableElementNormalisationResult;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.resources.GlobalResources;
import org.siemac.metamac.srm.web.client.widgets.ConfirmationWindow;
import org.siemac.metamac.srm.web.code.view.handlers.CodelistUiHandlers;
import org.siemac.metamac.web.common.client.utils.ApplicationEditionLanguages;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class CodelistCodesVariableElementsPanel extends VLayout {

    private CodelistUiHandlers                      uiHandlers;

    private CodelistMetamacDto                      codelistMetamacDto;

    protected ToolStrip                             toolStrip;
    protected ToolStripButton                       editButton;
    protected ToolStripButton                       saveButton;
    protected ToolStripButton                       cancelButton;
    protected ToolStripButton                       normaliseButton;

    private CodesVariableElementsAssignmentTreeGrid codesVariableElementsAssignmentTreeGrid;

    public CodelistCodesVariableElementsPanel() {
        setMargin(15);
        setMembersMargin(2);

        toolStrip = new ToolStrip();

        // Edit

        editButton = createEditButton();
        toolStrip.addButton(editButton);

        // Save

        saveButton = createSaveButton();
        toolStrip.addButton(saveButton);

        // Cancel

        cancelButton = createCancelButton();
        toolStrip.addButton(cancelButton);

        // Normalise

        normaliseButton = createNormaliseButton();
        toolStrip.addSeparator();
        toolStrip.addButton(normaliseButton);

        addMember(toolStrip);

        codesVariableElementsAssignmentTreeGrid = new CodesVariableElementsAssignmentTreeGrid();
        addMember(codesVariableElementsAssignmentTreeGrid);
    }

    public void setCodes(CodelistMetamacDto codelist, List<CodeMetamacVisualisationResult> codes) {
        this.codelistMetamacDto = codelist;
        codesVariableElementsAssignmentTreeGrid.setItems(codelist, codes);
        setViewMode();
    }

    public void setCodesVariableElementsNormalised(List<CodeVariableElementNormalisationResult> codeVariableElementNormalisationResults) {
        codesVariableElementsAssignmentTreeGrid.setCodesVariableElementsNormalised(codelistMetamacDto, codeVariableElementNormalisationResults);
    }

    private void setViewMode() {
        showEditButton();
        saveButton.hide();
        cancelButton.hide();
        normaliseButton.hide();

        codesVariableElementsAssignmentTreeGrid.setViewMode();
    }

    private void setEditionMode() {
        editButton.hide();
        saveButton.show();
        cancelButton.show();
        normaliseButton.show();

        codesVariableElementsAssignmentTreeGrid.setEditionMode();
    }

    private void showEditButton() {
        // TODO Security
        editButton.show();
    }

    public void updateItemScheme(CodelistMetamacDto codelistMetamacDto) {
        this.codelistMetamacDto = codelistMetamacDto;
        codesVariableElementsAssignmentTreeGrid.updateItemScheme(codelistMetamacDto);
    }

    public CodelistUiHandlers getUiHandlers() {
        return uiHandlers;
    }

    public void setUiHandlers(CodelistUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    //
    // TOOLSTRIP BUTTONS
    //

    private ToolStripButton createEditButton() {
        ToolStripButton editButton = new ToolStripButton(MetamacSrmWeb.getConstants().actionEdit(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.editListGrid().getURL());
        editButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                setEditionMode();
            }
        });
        return editButton;
    }

    private ToolStripButton createSaveButton() {
        ToolStripButton saveButton = new ToolStripButton(MetamacSrmWeb.getConstants().actionSave(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.saveListGrid().getURL());
        saveButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().updateCodesVariableElements(codelistMetamacDto.getUrn(), codesVariableElementsAssignmentTreeGrid.getCodesVariableElements());
            }
        });
        return saveButton;
    }

    private ToolStripButton createCancelButton() {
        ToolStripButton cancelButton = new ToolStripButton(MetamacSrmWeb.getConstants().actionCancel(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.cancelListGrid()
                .getURL());
        cancelButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                setViewMode();
            }
        });
        return cancelButton;
    }

    private ToolStripButton createNormaliseButton() {
        ToolStripButton normaliseButton = new ToolStripButton(getConstants().actionNormalise(), GlobalResources.RESOURCE.fit().getURL());
        normaliseButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ConfirmationWindow versionCodelistWindow = new ConfirmationWindow(getConstants().codesVariableElementAssignment(), getConstants().codesVariableElementAssignmentMessage());
                versionCodelistWindow.getYesButton().addClickHandler(new ClickHandler() {

                    @Override
                    public void onClick(ClickEvent event) {
                        // Normalise all the codes
                        getUiHandlers().normaliseVariableElementsToCodes(codelistMetamacDto.getUrn(), ApplicationEditionLanguages.getCurrentLocale(), false);
                    }
                });
                versionCodelistWindow.getNoButton().addClickHandler(new ClickHandler() {

                    @Override
                    public void onClick(ClickEvent event) {
                        // Only normalise the codes without variable element
                        getUiHandlers().normaliseVariableElementsToCodes(codelistMetamacDto.getUrn(), ApplicationEditionLanguages.getCurrentLocale(), true);
                    }
                });
            }
        });
        return normaliseButton;
    }
}
