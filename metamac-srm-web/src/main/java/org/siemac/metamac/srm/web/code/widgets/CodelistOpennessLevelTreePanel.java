package org.siemac.metamac.srm.web.code.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;
import org.siemac.metamac.srm.web.code.view.handlers.CodelistUiHandlers;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.widgets.TitleLabel;

import com.smartgwt.client.widgets.layout.VLayout;

public class CodelistOpennessLevelTreePanel extends VLayout {

    private TitleLabel                              opennessLevelTitle;
    private CodesOpennessLevelVisualisationTreeGrid codesOpennessLevelVisualisationTreeGrid;

    public CodelistOpennessLevelTreePanel() {
        opennessLevelTitle = new TitleLabel();
        opennessLevelTitle.setStyleName("subsectionTitleWithNoLeftMargin");
        addMember(opennessLevelTitle);

        codesOpennessLevelVisualisationTreeGrid = new CodesOpennessLevelVisualisationTreeGrid();
        addMember(codesOpennessLevelVisualisationTreeGrid);
    }

    public void setCodes(CodelistMetamacDto codelistMetamacDto, List<CodeMetamacVisualisationResult> codes, CodelistVisualisationDto codelistVisualisationDto) {
        opennessLevelTitle.setContents(CommonWebUtils.getElementName(codelistVisualisationDto.getCode(), codelistVisualisationDto.getName()));
        codesOpennessLevelVisualisationTreeGrid.setItems(codelistMetamacDto, codes, codelistVisualisationDto);
    }

    public void setUiHandlers(CodelistUiHandlers uiHandlers) {
        codesOpennessLevelVisualisationTreeGrid.setUiHandlers(uiHandlers);
    }

    public void updateItemScheme(CodelistMetamacDto codelistMetamacDto) {
        codesOpennessLevelVisualisationTreeGrid.updateItemScheme(codelistMetamacDto);
    }
}
