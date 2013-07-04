package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.web.client.resources.GlobalResources;
import org.siemac.metamac.srm.web.code.utils.CodesClientSecurityUtils;
import org.siemac.metamac.srm.web.code.view.handlers.CodelistUiHandlers;
import org.siemac.metamac.srm.web.dsd.listener.UploadListener;
import org.siemac.metamac.srm.web.shared.code.GetCodelistsResult;
import org.siemac.metamac.web.common.client.widgets.CustomToolStripButton;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class CodelistCodesPanel extends VLayout {

    protected ToolStrip        toolStrip;
    protected ToolStripButton  importCodesButton;
    protected ToolStripButton  exportCodesButton;

    private ImportCodesWindow  importCodesWindow;

    private CodesTreeGrid      codesTreeGrid;

    private CodelistUiHandlers uiHandlers;

    private CodelistMetamacDto codelistMetamacDto;

    public CodelistCodesPanel() {
        setMargin(15);
        setMembersMargin(2);

        importCodesWindow = new ImportCodesWindow();
        importCodesWindow.setUploadListener(new UploadListener() {

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

        importCodesButton = new CustomToolStripButton(getConstants().actionImportCodes(), GlobalResources.RESOURCE.importResource().getURL());
        importCodesButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                importCodesWindow.show();
            }
        });
        toolStrip.addButton(importCodesButton);

        exportCodesButton = new CustomToolStripButton(getConstants().codesExport(), GlobalResources.RESOURCE.exportResource().getURL());
        exportCodesButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.exportCodes(codelistMetamacDto.getUrn());
            }
        });
        toolStrip.addButton(exportCodesButton);

        addMember(toolStrip);

        codesTreeGrid = new CodesTreeGrid();
        addMember(codesTreeGrid);
    }

    public void setUiHandlers(CodelistUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
        codesTreeGrid.setUiHandlers(uiHandlers);
    }

    public void updateItemScheme(CodelistMetamacDto codelistMetamacDto) {
        this.codelistMetamacDto = codelistMetamacDto;

        codesTreeGrid.updateItemScheme(codelistMetamacDto);

        updateButtonsVisibility(codelistMetamacDto);
        importCodesWindow.setCodelist(codelistMetamacDto);
    }

    public void setCodes(CodelistMetamacDto codelist, List<CodeMetamacVisualisationResult> codes) {
        this.codelistMetamacDto = codelist;
        codesTreeGrid.setItems(codelist, codes);
    }

    public void setCodelistsToCreateComplexCodelist(GetCodelistsResult result) {
        codesTreeGrid.setCodelistsToCreateComplexCodelist(result);
    }

    public void setCodesToCreateComplexCodelist(CodelistMetamacDto codelistMetamacDto, List<CodeMetamacVisualisationResult> codes) {
        codesTreeGrid.setCodesToCreateComplexCodelist(codelistMetamacDto, codes);
    }

    private void updateButtonsVisibility(CodelistMetamacDto codelistMetamacDto) {
        updateImportCodesButtonVisibility(codelistMetamacDto);
        updateExportCodesButtonVisibility(codelistMetamacDto);
    }

    private void updateImportCodesButtonVisibility(CodelistMetamacDto codelistMetamacDto) {
        if (CodesClientSecurityUtils.canImportCodes(codelistMetamacDto)) {
            importCodesButton.show();
        } else {
            importCodesButton.hide();
        }
    }

    private void updateExportCodesButtonVisibility(CodelistMetamacDto codelistMetamacDto) {
        if (CodesClientSecurityUtils.canExportCodes(codelistMetamacDto)) {
            exportCodesButton.show();
        } else {
            exportCodesButton.hide();
        }
    }
}
