package org.siemac.metamac.srm.web.dsd.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.web.dsd.model.ds.DataStructureDefinitionDS;
import org.siemac.metamac.web.common.client.widgets.BaseCustomListGrid;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCanvasItem;

import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HLayout;

public class DsdDimensionCodesVisualisationItem extends CustomCanvasItem {

    protected BaseCustomListGrid listGrid;

    public DsdDimensionCodesVisualisationItem(String name, String title, boolean editionMode) {
        super(name, title);

        setCellStyle("dragAndDropCellStyle");

        listGrid = new BaseCustomListGrid();
        listGrid.setAutoFitMaxRecords(6);
        listGrid.setAutoFitData(Autofit.VERTICAL);

        ListGridField dimensionField = new ListGridField(DataStructureDefinitionDS.DIMENSION_CODES_VISUALISATION, getConstants().dsdDimensions());

        ListGridField orderField = new ListGridField(DataStructureDefinitionDS.DIMENSION_CODES_DISPLAY_ORDER, getConstants().dsdDisplayOrder());
        orderField.setCanEdit(editionMode);
        orderField.setCanFilter(false);
        orderField.setEditorType(new SelectItem(DataStructureDefinitionDS.DIMENSION_CODES_DISPLAY_ORDER, getConstants().dsdShowDecimals()));

        ListGridField opennessLevelField = new ListGridField(DataStructureDefinitionDS.DIMENSION_CODES_HIERARCHY_LEVELS_OPEN, getConstants().dsdHierarchyLevelsOpen());
        opennessLevelField.setCanEdit(editionMode);
        opennessLevelField.setCanFilter(false);
        opennessLevelField.setEditorType(new SelectItem(DataStructureDefinitionDS.DIMENSION_CODES_HIERARCHY_LEVELS_OPEN, getConstants().dsdShowDecimals()));

        listGrid.setFields(dimensionField, orderField, opennessLevelField);

        HLayout hLayout = new HLayout();
        hLayout.addMember(listGrid);
        hLayout.setStyleName("canvasCellStyle");

        setCanvas(hLayout);
    }
}
