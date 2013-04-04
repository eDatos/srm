package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;
import org.siemac.metamac.srm.web.client.widgets.EditCodelistOpennessLevelWindow;
import org.siemac.metamac.srm.web.code.model.ds.CodeDS;
import org.siemac.metamac.srm.web.code.view.handlers.CodelistUiHandlers;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.widgets.TitleLabel;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class CodelistOpennessLevelTreePanel extends VLayout {

    private CodelistMetamacDto                      codelistMetamacDto;
    private List<CodeMetamacVisualisationResult>    codes;
    private CodelistVisualisationDto                codelistVisualisationDto;

    private CodelistUiHandlers                      uiHandlers;

    private TitleLabel                              opennessLevelTitle;
    private CodesOpennessLevelVisualisationTreeGrid codesOpennessLevelVisualisationTreeGrid;

    public CodelistOpennessLevelTreePanel() {
        // TITLE

        opennessLevelTitle = new TitleLabel();
        opennessLevelTitle.setStyleName("subsectionTitleWithNoLeftMargin");
        addMember(opennessLevelTitle);

        // TOOL STRIP

        ToolStrip toolStrip = new ToolStrip();
        toolStrip.setWidth100();
        ToolStripButton editButton = new ToolStripButton(getConstants().actionEdit(), RESOURCE.editListGrid().getURL());
        // TODO Security editButton.setVisibility(Visibility.HIDDEN);
        editButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                final EditCodelistOpennessLevelWindow editCodelistOpennessLevelWindow = new EditCodelistOpennessLevelWindow(getConstants().codelistOpennessLevelEdit());
                editCodelistOpennessLevelWindow.getCodesOpennessLevelEditableTreeGrid().setItems(codelistMetamacDto, codes, codelistVisualisationDto);
                editCodelistOpennessLevelWindow.getSaveClickHandlers().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {

                        editCodelistOpennessLevelWindow.getCodesOpennessLevelEditableTreeGrid().saveAllEdits();

                        Map<String, Boolean> modifiedOpennessLevels = new HashMap<String, Boolean>();

                        ListGridRecord[] records = editCodelistOpennessLevelWindow.getCodesOpennessLevelEditableTreeGrid().getRecords();
                        for (ListGridRecord record : records) {
                            Boolean valueBeforeEdition = record.getAttributeAsBoolean(CodeDS.OPENNESS_LEVEL_INITIAL);
                            Boolean valueAfterEdition = record.getAttributeAsBoolean(CodeDS.OPENNESS_LEVEL);

                            if (valueBeforeEdition != valueAfterEdition) {
                                modifiedOpennessLevels.put(record.getAttributeAsString(CodeDS.URN), valueAfterEdition);
                            }
                        }

                        uiHandlers.updateCodesOpennessLevel(codelistVisualisationDto.getUrn(), modifiedOpennessLevels);

                        editCodelistOpennessLevelWindow.markForDestroy();
                    }
                });
            }
        });
        toolStrip.addButton(editButton);
        addMember(toolStrip);

        // TREE

        codesOpennessLevelVisualisationTreeGrid = new CodesOpennessLevelVisualisationTreeGrid();
        addMember(codesOpennessLevelVisualisationTreeGrid);
    }

    public void setCodes(CodelistMetamacDto codelistMetamacDto, List<CodeMetamacVisualisationResult> codes, CodelistVisualisationDto codelistVisualisationDto) {
        this.codelistMetamacDto = codelistMetamacDto;
        this.codes = codes;
        this.codelistVisualisationDto = codelistVisualisationDto;

        opennessLevelTitle.setContents(CommonWebUtils.getElementName(codelistVisualisationDto.getCode(), codelistVisualisationDto.getName()));
        codesOpennessLevelVisualisationTreeGrid.setItems(codelistMetamacDto, codes, codelistVisualisationDto);
    }

    public void setUiHandlers(CodelistUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
        codesOpennessLevelVisualisationTreeGrid.setUiHandlers(uiHandlers);
    }

    public void updateItemScheme(CodelistMetamacDto codelistMetamacDto) {
        codesOpennessLevelVisualisationTreeGrid.updateItemScheme(codelistMetamacDto);
    }
}
