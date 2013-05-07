package org.siemac.metamac.srm.web.dsd.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.dsd.dto.DimensionVisualisationInfoDto;
import org.siemac.metamac.srm.web.dsd.model.ds.DataStructureDefinitionDS;
import org.siemac.metamac.srm.web.dsd.model.ds.DimensionDS;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.widgets.BaseCustomListGrid;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCanvasItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DimensionComponentDto;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.EditorValueMapFunction;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;

public class DsdDimensionCodesVisualisationItem extends CustomCanvasItem {

    protected BaseCustomListGrid                  listGrid;

    private Map<String, List<RelatedResourceDto>> candidateOrderVisualisations;
    private Map<String, List<RelatedResourceDto>> candidateOpennessLevelVisualisations;

    public DsdDimensionCodesVisualisationItem(String name, String title, boolean editionMode) {
        super(name, title);

        setCellStyle("dragAndDropCellStyle");

        listGrid = new BaseCustomListGrid();
        listGrid.setAutoFitMaxRecords(6);
        listGrid.setAutoFitData(Autofit.VERTICAL);

        ListGridField dimensionField = new ListGridField(DataStructureDefinitionDS.DIMENSION_CODES_VISUALISATION, getConstants().dsdDimensions());
        dimensionField.setHoverCustomizer(getFieldHoverCustomizer());

        ListGridField orderField = new ListGridField(DataStructureDefinitionDS.DIMENSION_CODES_DISPLAY_ORDER, getConstants().dsdDisplayOrder());
        orderField.setCanEdit(editionMode);
        orderField.setCanFilter(false);
        orderField.setEditorType(new SelectItem(DataStructureDefinitionDS.DIMENSION_CODES_DISPLAY_ORDER, getConstants().dsdShowDecimals()));
        orderField.setEditorValueMapFunction(getOrderEditorValueMapFunction());
        orderField.setHoverCustomizer(getFieldHoverCustomizer());

        ListGridField opennessLevelField = new ListGridField(DataStructureDefinitionDS.DIMENSION_CODES_HIERARCHY_LEVELS_OPEN, getConstants().dsdHierarchyLevelsOpen());
        opennessLevelField.setCanEdit(editionMode);
        opennessLevelField.setCanFilter(false);
        opennessLevelField.setEditorType(new SelectItem(DataStructureDefinitionDS.DIMENSION_CODES_HIERARCHY_LEVELS_OPEN, getConstants().dsdShowDecimals()));
        opennessLevelField.setEditorValueMapFunction(getOpennessLevelEditorValueMapFunction());
        opennessLevelField.setHoverCustomizer(getFieldHoverCustomizer());
        // opennessLevelField.setDisplayField(DataStructureDefinitionDS.DIMENSION_CODES_HIERARCHY_LEVELS_OPEN);

        listGrid.setFields(dimensionField, orderField, opennessLevelField);

        HLayout hLayout = new HLayout();
        hLayout.addMember(listGrid);
        hLayout.setStyleName("canvasCellStyle");

        setCanvas(hLayout);
    }

    public void setDimensionsAndCandidateVisualisations(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto, List<DimensionComponentDto> dimensionComponentDtos,
            Map<String, List<RelatedResourceDto>> candidateOrderVisualisations, Map<String, List<RelatedResourceDto>> candidateOpennessLevelVisualisations) {
        this.candidateOrderVisualisations = candidateOrderVisualisations;
        this.candidateOpennessLevelVisualisations = candidateOpennessLevelVisualisations;

        listGrid.removeAllData();

        // Set all the DSD dimensions in the listGrid
        for (DimensionComponentDto dimension : dimensionComponentDtos) {
            DimensionVisualisationInfoDto currentDimensionVisualisationValues = getDimensionVisualisationInfoDtoByDimensionUrn(dataStructureDefinitionMetamacDto, dimension.getUrn());
            ListGridRecord record = getDimensionVisualisationRecord(dimension, currentDimensionVisualisationValues);
            listGrid.addData(record);
        }
    }

    private ListGridRecord getDimensionVisualisationRecord(DimensionComponentDto dimensionComponentDto, DimensionVisualisationInfoDto currentDimensionVisualisationValues) {
        ListGridRecord record = new ListGridRecord();
        record.setAttribute(DimensionDS.URN, dimensionComponentDto.getUrn());
        record.setAttribute(DataStructureDefinitionDS.DIMENSION_CODES_VISUALISATION, dimensionComponentDto.getCode());

        if (currentDimensionVisualisationValues != null) {
            if (currentDimensionVisualisationValues.getDisplayOrder() != null) {
                record.setAttribute(DataStructureDefinitionDS.DIMENSION_CODES_DISPLAY_ORDER, RelatedResourceUtils.getRelatedResourceName(currentDimensionVisualisationValues.getDisplayOrder()));
            }
            if (currentDimensionVisualisationValues.getHierarchyLevelsOpen() != null) {
                record.setAttribute(DataStructureDefinitionDS.DIMENSION_CODES_HIERARCHY_LEVELS_OPEN,
                        RelatedResourceUtils.getRelatedResourceName(currentDimensionVisualisationValues.getHierarchyLevelsOpen()));
            }
        }
        return record;
    }

    private DimensionVisualisationInfoDto getDimensionVisualisationInfoDtoByDimensionUrn(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto, String dimensionUrn) {
        List<DimensionVisualisationInfoDto> dimensionVisualisationInfoDtos = dataStructureDefinitionMetamacDto.getDimensionVisualisationInfos();
        for (DimensionVisualisationInfoDto dimensionVisualisationInfoDto : dimensionVisualisationInfoDtos) {
            if (StringUtils.equals(dimensionUrn, dimensionVisualisationInfoDto.getUrn())) {
                return dimensionVisualisationInfoDto;
            }
        }
        return null;
    }

    private EditorValueMapFunction getOrderEditorValueMapFunction() {
        return new EditorValueMapFunction() {

            @SuppressWarnings("rawtypes")
            @Override
            public Map getEditorValueMap(Map values, ListGridField field, ListGrid grid) {
                ListGridRecord record = grid.getSelectedRecord();
                String dimensionUrn = record.getAttribute(DimensionDS.URN);
                List<RelatedResourceDto> candidateOrders = candidateOrderVisualisations.get(dimensionUrn);
                return RelatedResourceUtils.getRelatedResourceLinkedHashMap(candidateOrders);
            }
        };
    }

    private EditorValueMapFunction getOpennessLevelEditorValueMapFunction() {
        return new EditorValueMapFunction() {

            @SuppressWarnings("rawtypes")
            @Override
            public Map getEditorValueMap(Map values, ListGridField field, ListGrid grid) {
                ListGridRecord record = grid.getSelectedRecord();
                String dimensionUrn = record.getAttribute(DimensionDS.URN);
                List<RelatedResourceDto> candidateOpennessLevels = candidateOpennessLevelVisualisations.get(dimensionUrn);
                return RelatedResourceUtils.getRelatedResourceLinkedHashMap(candidateOpennessLevels);
            }
        };
    }

    private HoverCustomizer getFieldHoverCustomizer() {
        return new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                return value != null ? value.toString() : StringUtils.EMPTY;
            }
        };
    }
}
