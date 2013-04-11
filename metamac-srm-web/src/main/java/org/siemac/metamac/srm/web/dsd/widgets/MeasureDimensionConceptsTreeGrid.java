package org.siemac.metamac.srm.web.dsd.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.dsd.dto.MeasureDimensionPrecisionDto;
import org.siemac.metamac.srm.web.client.model.ds.ItemDS;
import org.siemac.metamac.srm.web.concept.view.handlers.BaseConceptUiHandlers;
import org.siemac.metamac.srm.web.concept.widgets.ConceptsTreeGrid;
import org.siemac.metamac.srm.web.dsd.model.ds.DataStructureDefinitionDS;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.tree.TreeGridField;

public class MeasureDimensionConceptsTreeGrid extends ConceptsTreeGrid {

    private List<MeasureDimensionPrecisionDto> measureDimensionPrecisionDtos;

    public MeasureDimensionConceptsTreeGrid(boolean editionMode) {
        super();

        setShowFilterEditor(false);

        TreeGridField showDecimalsPrecision = new TreeGridField(DataStructureDefinitionDS.SHOW_DECIMALS_PRECISION, getConstants().dsdDecimals());
        showDecimalsPrecision.setWidth("20%");
        showDecimalsPrecision.setCanEdit(editionMode);
        showDecimalsPrecision.setCanFilter(false);
        showDecimalsPrecision.setEditorType(new SelectItem(DataStructureDefinitionDS.SHOW_DECIMALS, getConstants().dsdShowDecimals()));
        showDecimalsPrecision.setValueMap(org.siemac.metamac.srm.web.dsd.utils.CommonUtils.getDsdShowDecimalsHashMap());

        ListGridField[] fields = new ListGridField[3];
        System.arraycopy(getAllFields(), 0, fields, 0, 2);
        fields[fields.length - 1] = showDecimalsPrecision;

        fields[0].setCanEdit(false);
        fields[0].setCanFilter(false);
        fields[1].setCanEdit(false);
        fields[1].setCanFilter(false);

        setFields(fields);
    }

    @Override
    public boolean canEditCell(int rowNum, int colNum) {
        // Disable editing the first row (contains the concept scheme)
        return rowNum != 0;
    }

    public void setMeasureDimensionPrecisions(List<MeasureDimensionPrecisionDto> measureDimensionPrecisionDtos) {
        this.measureDimensionPrecisionDtos = measureDimensionPrecisionDtos;
        updateDecimalsPrecision();
    }

    public List<MeasureDimensionPrecisionDto> getMeasureDimensionPrecisionDtos() {
        List<MeasureDimensionPrecisionDto> measureDimensionPrecisionDtos = new ArrayList<MeasureDimensionPrecisionDto>();
        ListGridRecord listGridRecords[] = getRecords();
        for (ListGridRecord record : listGridRecords) {
            String value = record.getAttributeAsString(DataStructureDefinitionDS.SHOW_DECIMALS_PRECISION);
            if (!StringUtils.isBlank(value)) {
                Integer showDecimalsPrecision = Integer.valueOf(value);
                MeasureDimensionPrecisionDto measureDimensionPrecisionDto = new MeasureDimensionPrecisionDto();
                measureDimensionPrecisionDto.setShowDecimalPrecision(showDecimalsPrecision);
                measureDimensionPrecisionDto.setCode(record.getAttribute(ItemDS.CODE));
                measureDimensionPrecisionDto.setType(RelatedResourceTypeEnum.CONCEPT);
                measureDimensionPrecisionDto.setUrn(record.getAttributeAsString(ItemDS.URN));
                measureDimensionPrecisionDtos.add(measureDimensionPrecisionDto);
            }
        }
        return measureDimensionPrecisionDtos;
    }

    @Override
    public void setItems(ItemSchemeDto conceptSchemeMetamacDto, List<ItemHierarchyDto> itemHierarchyDtos) {
        super.setItems(conceptSchemeMetamacDto, itemHierarchyDtos);
        updateDecimalsPrecision();
    }

    @Override
    public void updateItemScheme(ItemSchemeDto itemSchemeDto) {
        // Overwrite this method to do nothing
    }

    @Override
    public void setUiHandlers(BaseConceptUiHandlers uiHandlers) {
        // Overwrite this method to do nothing
    }

    @Override
    protected void onNodeClick(String nodeName, String conceptUrn) {
        // Overwrite this method to do nothing
    }

    @Override
    protected void onNodeContextClick(String nodeName, ItemDto concept) {
        // Overwrite this method to do nothing
    }

    private void updateDecimalsPrecision() {
        ListGridRecord[] records = getRecords();
        for (ListGridRecord record : records) {
            record.setAttribute(DataStructureDefinitionDS.SHOW_DECIMALS_PRECISION, getDecimalsByConcept(record.getAttribute(ItemDS.URN)));
        }
        markForRedraw();
    }

    public Integer getDecimalsByConcept(String conceptUrn) {
        for (MeasureDimensionPrecisionDto measureDimensionPrecisionDto : measureDimensionPrecisionDtos) {
            if (StringUtils.equals(measureDimensionPrecisionDto.getUrn(), conceptUrn)) {
                return measureDimensionPrecisionDto.getShowDecimalPrecision();
            }
        }
        return null;
    }
}
