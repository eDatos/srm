package org.siemac.metamac.srm.web.code.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistOrderVisualisationDto;
import org.siemac.metamac.srm.web.client.widgets.CodelistOrdersSectionStack;
import org.siemac.metamac.srm.web.code.view.handlers.CodelistUiHandlers;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.widgets.TitleLabel;

import com.smartgwt.client.widgets.layout.VLayout;

public class CodelistOrdersPanel extends VLayout {

    private CodelistOrdersSectionStack codelistOrdersSectionStack;
    private TitleLabel                 orderTitle;
    private CodesOrderTreeGrid         codesOrderTreeGrid;

    public CodelistOrdersPanel() {
        setMembersMargin(10);

        codelistOrdersSectionStack = new CodelistOrdersSectionStack();
        addMember(codelistOrdersSectionStack);

        orderTitle = new TitleLabel();
        orderTitle.setStyleName("subsectionTitle");
        addMember(orderTitle);

        codesOrderTreeGrid = new CodesOrderTreeGrid();
        addMember(codesOrderTreeGrid);
    }

    public void setOrders(List<CodelistOrderVisualisationDto> orders) {
        codelistOrdersSectionStack.setCodelistOrders(orders);

        hideCodes();
    }

    public void setCodes(CodelistMetamacDto codelistMetamacDto, List<CodeMetamacVisualisationResult> codes, CodelistOrderVisualisationDto codelistOrderVisualisationDto) {
        codelistOrdersSectionStack.selectCodelistOrder(codelistOrderVisualisationDto.getUrn());
        orderTitle.setContents(CommonWebUtils.getElementName(codelistOrderVisualisationDto.getCode(), codelistOrderVisualisationDto.getName()));
        codesOrderTreeGrid.setItems(codelistMetamacDto, codes, codelistOrderVisualisationDto);

        showCodes();
    }

    public void setUiHandlers(CodelistUiHandlers uiHandlers) {
        codelistOrdersSectionStack.setUiHandlers(uiHandlers);
        codesOrderTreeGrid.setUiHandlers(uiHandlers);
    }

    public CodelistOrdersSectionStack getcodelistCodelistOrdersSectionStack() {
        return codelistOrdersSectionStack;
    }

    public TitleLabel getOrderTitle() {
        return orderTitle;
    }

    public CodesOrderTreeGrid getCodesTreeGrid() {
        return codesOrderTreeGrid;
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
