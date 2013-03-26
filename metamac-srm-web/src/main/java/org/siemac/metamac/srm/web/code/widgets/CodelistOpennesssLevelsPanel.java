package org.siemac.metamac.srm.web.code.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;
import org.siemac.metamac.srm.web.client.widgets.CodelistOpennessLevelsSectionStack;
import org.siemac.metamac.srm.web.code.view.handlers.CodelistUiHandlers;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.widgets.TitleLabel;

import com.smartgwt.client.widgets.layout.VLayout;

public class CodelistOpennesssLevelsPanel extends VLayout {

    private CodelistOpennessLevelsSectionStack codelistOpennessLevelsSectionStack;
    private TitleLabel                         opennessLevelTitle;
    private CodesEditableOpennessLevelTreeGrid codesOpennessLevelTreeGrid;

    public CodelistOpennesssLevelsPanel() {
        setMargin(15);
        setMembersMargin(10);

        codelistOpennessLevelsSectionStack = new CodelistOpennessLevelsSectionStack();
        addMember(codelistOpennessLevelsSectionStack);

        opennessLevelTitle = new TitleLabel();
        opennessLevelTitle.setStyleName("subsectionTitleWithNoLeftMargin");
        addMember(opennessLevelTitle);

        codesOpennessLevelTreeGrid = new CodesEditableOpennessLevelTreeGrid();
        addMember(codesOpennessLevelTreeGrid);
    }

    public void setOpennessLevels(List<CodelistVisualisationDto> opennessLevels) {
        codelistOpennessLevelsSectionStack.setCodelistVisualisations(opennessLevels);

        hideCodes();
    }

    public void setCodes(CodelistMetamacDto codelistMetamacDto, List<CodeMetamacVisualisationResult> codes, CodelistVisualisationDto codelistVisualisationDto) {
        codelistOpennessLevelsSectionStack.selectCodelistVisualisation(codelistVisualisationDto.getUrn());
        opennessLevelTitle.setContents(CommonWebUtils.getElementName(codelistVisualisationDto.getCode(), codelistVisualisationDto.getName()));
        codesOpennessLevelTreeGrid.setItems(codelistMetamacDto, codes, codelistVisualisationDto);

        showCodes();
    }

    public void setUiHandlers(CodelistUiHandlers uiHandlers) {
        codelistOpennessLevelsSectionStack.setUiHandlers(uiHandlers);
        codesOpennessLevelTreeGrid.setUiHandlers(uiHandlers);
    }

    public CodelistOpennessLevelsSectionStack getCodelistOpennessLevelsSectionStack() {
        return codelistOpennessLevelsSectionStack;
    }

    public TitleLabel getOpennessLevelTitle() {
        return opennessLevelTitle;
    }

    public CodesEditableOpennessLevelTreeGrid getCodesTreeGrid() {
        return codesOpennessLevelTreeGrid;
    }

    public void updateItemScheme(CodelistMetamacDto codelistMetamacDto) {
        codesOpennessLevelTreeGrid.updateItemScheme(codelistMetamacDto);
    }

    public void showCodes() {
        opennessLevelTitle.show();
        codesOpennessLevelTreeGrid.show();
    }

    public void hideCodes() {
        opennessLevelTitle.hide();
        codesOpennessLevelTreeGrid.hide();
    }
}
