package org.siemac.metamac.srm.web.dsd.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.dsd.dto.MeasureDimensionPrecisionDto;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCanvasItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;

public class ShowDecimalsPrecisionItem extends CustomCanvasItem {

    private MeasureDimensionConceptsTreeGrid measureDimensionConceptsTreeGrid;

    public ShowDecimalsPrecisionItem(String name, String title, boolean editionMode) {
        super(name, title);
        setCellStyle("dragAndDropCellStyle");

        measureDimensionConceptsTreeGrid = new MeasureDimensionConceptsTreeGrid(editionMode);
        setCanvas(measureDimensionConceptsTreeGrid);
    }

    public void setMeasureDimensionPrecisions(List<MeasureDimensionPrecisionDto> measureDimensionPrecisionDtos) {
        measureDimensionConceptsTreeGrid.setMeasureDimensionPrecisions(measureDimensionPrecisionDtos);
    }

    public void setConcepts(ConceptSchemeMetamacDto conceptSchemeMetamacDto, List<ItemHierarchyDto> concepts) {
        measureDimensionConceptsTreeGrid.setItems(conceptSchemeMetamacDto, concepts);
    }

    public List<MeasureDimensionPrecisionDto> getMeasureDimensionPrecisionDtos() {
        return measureDimensionConceptsTreeGrid.getMeasureDimensionPrecisionDtos();
    }
}
