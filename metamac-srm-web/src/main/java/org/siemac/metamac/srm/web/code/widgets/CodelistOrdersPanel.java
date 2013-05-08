package org.siemac.metamac.srm.web.code.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;
import org.siemac.metamac.srm.web.code.view.handlers.CodelistUiHandlers;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.widgets.TitleLabel;

import com.smartgwt.client.widgets.layout.VLayout;

public class CodelistOrdersPanel extends VLayout {

    private CodelistOrdersSectionStack codelistOrdersSectionStack;
    private TitleLabel                 orderTitle;
    private CodesOrderTreeGrid         codesOrderTreeGrid;

    public CodelistOrdersPanel() {
        setMargin(15);
        setMembersMargin(10);

        codelistOrdersSectionStack = new CodelistOrdersSectionStack();
        addMember(codelistOrdersSectionStack);

        orderTitle = new TitleLabel();
        orderTitle.setStyleName("subsectionTitleWithNoLeftMargin");
        addMember(orderTitle);

        codesOrderTreeGrid = new CodesOrderTreeGrid();
        addMember(codesOrderTreeGrid);
    }

    public void setOrders(List<CodelistVisualisationDto> orders) {
        codelistOrdersSectionStack.setCodelistVisualisations(orders);

        hideCodes();
    }

    public void setCodes(CodelistMetamacDto codelistMetamacDto, List<CodeMetamacVisualisationResult> codes, CodelistVisualisationDto codelistOrderVisualisationDto) {
        codelistOrdersSectionStack.selectCodelistVisualisation(codelistOrderVisualisationDto.getUrn());
        orderTitle.setContents(CommonWebUtils.getElementName(codelistOrderVisualisationDto.getCode(), codelistOrderVisualisationDto.getName()));
        codesOrderTreeGrid.setItems(codelistMetamacDto, codes, codelistOrderVisualisationDto);

        showCodes();
    }

    public void setUiHandlers(CodelistUiHandlers uiHandlers) {
        codelistOrdersSectionStack.setUiHandlers(uiHandlers);
        codesOrderTreeGrid.setUiHandlers(uiHandlers);
    }

    public CodelistOrdersSectionStack getCodelistOrdersSectionStack() {
        return codelistOrdersSectionStack;
    }

    public TitleLabel getOrderTitle() {
        return orderTitle;
    }

    public CodesOrderTreeGrid getCodesTreeGrid() {
        return codesOrderTreeGrid;
    }

    public void updateItemScheme(CodelistMetamacDto codelistMetamacDto) {
        codesOrderTreeGrid.updateItemScheme(codelistMetamacDto);
    }

    public void showCodes() {
        orderTitle.show();
        codesOrderTreeGrid.show();
    }

    public void hideCodes() {
        orderTitle.hide();
        codesOrderTreeGrid.hide();
    }
}
