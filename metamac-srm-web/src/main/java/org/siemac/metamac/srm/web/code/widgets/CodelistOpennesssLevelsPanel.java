package org.siemac.metamac.srm.web.code.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;
import org.siemac.metamac.srm.web.client.widgets.CodelistOpennessLevelsSectionStack;
import org.siemac.metamac.srm.web.code.view.handlers.CodelistUiHandlers;

import com.smartgwt.client.widgets.layout.VLayout;

public class CodelistOpennesssLevelsPanel extends VLayout {

    private CodelistOpennessLevelsSectionStack codelistOpennessLevelsSectionStack;
    private CodelistOpennessLevelTreePanel     codelistOpennessLevelTreePanel;

    public CodelistOpennesssLevelsPanel() {
        setMargin(15);
        setMembersMargin(10);

        // LIST OF OPENNESS LEVELS

        codelistOpennessLevelsSectionStack = new CodelistOpennessLevelsSectionStack();
        addMember(codelistOpennessLevelsSectionStack);

        // VISUALISATION OF AN OPENNESS LEVEL (TREE)

        codelistOpennessLevelTreePanel = new CodelistOpennessLevelTreePanel();
        addMember(codelistOpennessLevelTreePanel);
    }

    public void setOpennessLevels(List<CodelistVisualisationDto> opennessLevels) {
        codelistOpennessLevelsSectionStack.setCodelistVisualisations(opennessLevels);

        hideCodes();
    }

    public void setCodes(CodelistMetamacDto codelistMetamacDto, List<CodeMetamacVisualisationResult> codes, CodelistVisualisationDto codelistVisualisationDto) {
        codelistOpennessLevelsSectionStack.selectCodelistVisualisation(codelistVisualisationDto.getUrn());
        codelistOpennessLevelTreePanel.setCodes(codelistMetamacDto, codes, codelistVisualisationDto);

        showCodes();
    }

    public void setUiHandlers(CodelistUiHandlers uiHandlers) {
        codelistOpennessLevelsSectionStack.setUiHandlers(uiHandlers);
        codelistOpennessLevelTreePanel.setUiHandlers(uiHandlers);
    }

    public CodelistOpennessLevelsSectionStack getCodelistOpennessLevelsSectionStack() {
        return codelistOpennessLevelsSectionStack;
    }

    public void updateItemScheme(CodelistMetamacDto codelistMetamacDto) {
        codelistOpennessLevelTreePanel.updateItemScheme(codelistMetamacDto);
    }

    public void showCodes() {
        codelistOpennessLevelTreePanel.show();
    }

    public void hideCodes() {
        codelistOpennessLevelTreePanel.hide();
    }
}
