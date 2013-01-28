package org.siemac.metamac.srm.web.dsd.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.web.concept.view.handlers.BaseConceptUiHandlers;
import org.siemac.metamac.srm.web.concept.widgets.ConceptsTreeGrid;
import org.siemac.metamac.srm.web.dsd.model.ds.DataStructureDefinitionDS;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.tree.TreeGridField;

public class MeasureDimensionConceptsTreeGrid extends ConceptsTreeGrid {

    public MeasureDimensionConceptsTreeGrid(boolean alwaysShowEditors) {
        super(alwaysShowEditors);

        setShowFilterEditor(false);

        TreeGridField showDecimalsPrecision = new TreeGridField(DataStructureDefinitionDS.SHOW_DECIMALS_PRECISION, getConstants().dsdDecimals());
        showDecimalsPrecision.setWidth("20%");
        showDecimalsPrecision.setCanEdit(true);
        showDecimalsPrecision.setCanFilter(false);
        showDecimalsPrecision.setEditorType(new SelectItem(DataStructureDefinitionDS.SHOW_DECIMALS, getConstants().dsdShowDecimals()));
        showDecimalsPrecision.setValueMap(org.siemac.metamac.srm.web.dsd.utils.CommonUtils.getDsdShowDecimalsHashMap());

        ListGridField[] fields = new ListGridField[3];
        System.arraycopy(getFields(), 0, fields, 0, 2);
        fields[fields.length - 1] = showDecimalsPrecision;

        fields[0].setCanEdit(false);
        fields[0].setCanFilter(false);
        fields[1].setCanEdit(false);
        fields[1].setCanFilter(false);

        setFields(fields);
    }

    @Override
    public void updateItemScheme(ItemSchemeDto itemSchemeDto) {
        // Do nothing
    }

    @Override
    public void setUiHandlers(BaseConceptUiHandlers uiHandlers) {
        // Do nothing
    }

    @Override
    protected void onNodeClick(String nodeName, String conceptUrn) {
        // Do nothing
    }

    @Override
    protected void onNodeContextClick(String nodeName, ItemDto concept) {
        // Do nothing
    }
}
