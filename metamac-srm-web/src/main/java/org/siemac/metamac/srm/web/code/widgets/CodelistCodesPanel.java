package org.siemac.metamac.srm.web.code.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.web.code.view.handlers.CodelistUiHandlers;
import org.siemac.metamac.srm.web.shared.code.GetCodelistsResult;

import com.smartgwt.client.widgets.layout.VLayout;

public class CodelistCodesPanel extends VLayout {

    private CodesTreeGrid codesTreeGrid;

    public CodelistCodesPanel() {
        setMargin(15);
        codesTreeGrid = new CodesTreeGrid();
        addMember(codesTreeGrid);
    }

    public void setUiHandlers(CodelistUiHandlers uiHandlers) {
        codesTreeGrid.setUiHandlers(uiHandlers);
    }

    public void updateItemScheme(CodelistMetamacDto codelistMetamacDto) {
        codesTreeGrid.updateItemScheme(codelistMetamacDto);
    }

    public void setItems(CodelistMetamacDto codelist, List<CodeMetamacVisualisationResult> codes) {
        codesTreeGrid.setItems(codelist, codes);
    }

    public void setCodelistsToCreateComplexCodelist(GetCodelistsResult result) {
        codesTreeGrid.setCodelistsToCreateComplexCodelist(result);
    }

    public void setCodesToCreateComplexCodelist(CodelistMetamacDto codelistMetamacDto, List<CodeMetamacVisualisationResult> codes) {
        codesTreeGrid.setCodesToCreateComplexCodelist(codelistMetamacDto, codes);
    }
}
