package org.siemac.metamac.srm.web.code.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.web.client.resources.GlobalResources;

import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class CodelistCodesVariableElementsPanel extends VLayout {

    protected ToolStrip                             toolStrip;
    protected ToolStripButton                       normaliseButton;

    private CodesVariableElementsAssignmentTreeGrid codesVariableElementsAssignmentTreeGrid;

    public CodelistCodesVariableElementsPanel() {
        setMargin(15);
        setMembersMargin(2);

        toolStrip = new ToolStrip();
        normaliseButton = new ToolStripButton(getConstants().actionNormalise(), GlobalResources.RESOURCE.fit().getURL());
        toolStrip.addButton(normaliseButton);
        addMember(toolStrip);

        codesVariableElementsAssignmentTreeGrid = new CodesVariableElementsAssignmentTreeGrid();
        addMember(codesVariableElementsAssignmentTreeGrid);
    }

    public void setCodes(CodelistMetamacDto codelist, List<CodeMetamacVisualisationResult> codes) {
        codesVariableElementsAssignmentTreeGrid.setItems(codelist, codes);
    }
}
