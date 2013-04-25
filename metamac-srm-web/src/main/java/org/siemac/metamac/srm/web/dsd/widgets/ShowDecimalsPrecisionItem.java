package org.siemac.metamac.srm.web.dsd.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.dsd.dto.MeasureDimensionPrecisionDto;
import org.siemac.metamac.web.common.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCanvasItem;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;

public class ShowDecimalsPrecisionItem extends CustomCanvasItem {

    private MeasureDimensionConceptsTreeGrid measureDimensionConceptsTreeGrid;

    public ShowDecimalsPrecisionItem(String name, String title, boolean editionMode) {
        super(name, title);
        setCellStyle("dragAndDropCellStyle");

        measureDimensionConceptsTreeGrid = new MeasureDimensionConceptsTreeGrid(editionMode);
        setCanvas(measureDimensionConceptsTreeGrid);

        if (editionMode) {

            // In edition mode, show a message to warn the user that these values should be filled when the measure dimension is not going to be modified

            FormItemIcon infoIcon = new FormItemIcon();
            infoIcon.setSrc(GlobalResources.RESOURCE.info().getURL());
            infoIcon.setPrompt(getConstants().dsdShowDecimalsPrecisionInfo());
            setIcons(infoIcon);

            setIconVAlign(VerticalAlignment.TOP);
        }
    }

    public void setMeasureDimensionPrecisions(List<MeasureDimensionPrecisionDto> measureDimensionPrecisionDtos) {
        measureDimensionConceptsTreeGrid.setMeasureDimensionPrecisions(measureDimensionPrecisionDtos);
    }

    public void setConcepts(ConceptSchemeMetamacDto conceptSchemeMetamacDto, List<ItemVisualisationResult> concepts) {
        measureDimensionConceptsTreeGrid.setItems(conceptSchemeMetamacDto, concepts);
    }

    public List<MeasureDimensionPrecisionDto> getMeasureDimensionPrecisionDtos() {
        return measureDimensionConceptsTreeGrid.getMeasureDimensionPrecisionDtos();
    }
}
