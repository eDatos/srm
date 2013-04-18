package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.web.client.model.ds.ItemDS;
import org.siemac.metamac.srm.web.code.model.ds.CodeDS;
import org.siemac.metamac.web.common.client.resources.StyleUtils;
import org.siemac.metamac.web.common.client.utils.ListGridUtils;

import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;

public class BaseCodesSimpleTreeGrid extends TreeGrid {

    public BaseCodesSimpleTreeGrid() {
        super();

        setHeight(175);
        setAutoFitMaxRecords(10);
        setAutoFitData(Autofit.VERTICAL);
        setShowOpenIcons(true);
        setShowDropIcons(false);
        setShowSelectedStyle(true);
        setShowPartialSelection(true);
        setShowConnectors(true);
        setLeaveScrollbarGap(false);
        setSelectionAppearance(SelectionAppearance.CHECKBOX);
        setCascadeSelection(false);
        setRollUnderCanvasProperties(StyleUtils.getRollUnderCanvasProperties());

        TreeGridField codeField = new TreeGridField(ItemDS.CODE, getConstants().identifiableArtefactCode());
        codeField.setWidth("45%");

        TreeGridField nameField = new TreeGridField(ItemDS.NAME, getConstants().nameableArtefactName());

        TreeGridField orderField = new TreeGridField(CodeDS.ORDER, getConstants().codeOrder());
        orderField.setShowIfCondition(ListGridUtils.getFalseListGridFieldIfFunction());
        orderField.setCanSort(true);

        setFields(codeField, nameField, orderField);

        // Order by ORDER field

        setCanSort(true);
        setSortField(CodeDS.ORDER);
    }
}
