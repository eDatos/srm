package org.siemac.metamac.srm.web.dsd.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.dsd.dto.DimensionVisualisationInfoDto;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.model.ds.RelatedResourceBaseDS;
import org.siemac.metamac.web.common.client.model.record.RelatedResourceBaseRecord;
import org.siemac.metamac.web.common.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.BaseCustomListGrid;
import org.siemac.metamac.web.common.client.widgets.InformationWindow;
import org.siemac.metamac.web.common.client.widgets.SearchWindow;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCanvasItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DimensionComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridFieldIfFunction;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;

public class DsdDimensionCodesVisualisationItem extends CustomCanvasItem {

    // LISTGRID DATASOURCE
    private static String                         DIMENSION_URN                 = "dim-urn";
    private static String                         DIMENSION_CODE                = "dim-code";
    private static String                         ORDER                         = "order";
    private static String                         ORDER_DTO                     = "order-dto";
    private static String                         ORDER_EDITION_RECORD          = "order-edition-record";
    private static String                         OPENNESS_LEVEL                = "open";
    private static String                         OPENNESS_LEVEL_DTO            = "open-dto";
    private static String                         OPENNESS_LEVEL_EDITION_RECORD = "open-edition-record";

    protected BaseCustomListGrid                  listGrid;

    private Map<String, List<RelatedResourceDto>> candidateOrderVisualisations;
    private Map<String, List<RelatedResourceDto>> candidateOpennessLevelVisualisations;

    private SearchVisualisationWindow             searchVisualisationWindow;

    public DsdDimensionCodesVisualisationItem(String name, String title, boolean editionMode) {
        super(name, title);

        setCellStyle("dragAndDropCellStyle");

        listGrid = new BaseCustomListGrid();
        listGrid.setAutoFitMaxRecords(6);
        listGrid.setAutoFitData(Autofit.VERTICAL);

        // DIMENSION

        ListGridField dimensionField = new ListGridField(DIMENSION_CODE, getConstants().dsdDimensions());
        dimensionField.setHoverCustomizer(getFieldHoverCustomizer());

        // ORDER

        ListGridField orderField = new ListGridField(ORDER, getConstants().dsdDisplayOrder());
        orderField.setCanFilter(false);
        orderField.setHoverCustomizer(getFieldHoverCustomizer());
        orderField.setAlign(Alignment.RIGHT);

        ListGridField editOrderField = new ListGridField(ORDER_EDITION_RECORD, " "); // Do not show title in this column (an space is needed)
        editOrderField.setCanFilter(false);
        editOrderField.setCanEdit(false);
        editOrderField.setWidth(30);
        editOrderField.setAlign(Alignment.LEFT);
        editOrderField.setType(ListGridFieldType.IMAGE);
        editOrderField.setShowIfCondition(getEditionFieldIfFunction(editionMode));
        editOrderField.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                Record record = event.getRecord();
                if (record != null) {
                    showOrderSelectionWindow(record);
                }
            }
        });

        // OPENNESS LEVEL

        ListGridField opennessLevelField = new ListGridField(OPENNESS_LEVEL, getConstants().dsdHierarchyLevelsOpen());
        opennessLevelField.setCanFilter(false);
        opennessLevelField.setHoverCustomizer(getFieldHoverCustomizer());
        opennessLevelField.setAlign(Alignment.RIGHT);

        ListGridField editOpennessLevelField = new ListGridField(OPENNESS_LEVEL_EDITION_RECORD, " "); // Do not show title in this column (an space is needed)
        editOpennessLevelField.setCanFilter(false);
        editOpennessLevelField.setCanEdit(false);
        editOpennessLevelField.setWidth(30);
        editOpennessLevelField.setAlign(Alignment.LEFT);
        editOpennessLevelField.setType(ListGridFieldType.IMAGE);
        editOpennessLevelField.setShowIfCondition(getEditionFieldIfFunction(editionMode));
        editOpennessLevelField.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                Record record = event.getRecord();
                if (record != null) {
                    showOpennessLevelSelectionWindow(record);
                }
            }
        });

        listGrid.setFields(dimensionField, orderField, editOrderField, opennessLevelField, editOpennessLevelField);

        HLayout hLayout = new HLayout();
        hLayout.addMember(listGrid);
        hLayout.setStyleName("canvasCellStyle");

        setCanvas(hLayout);
    }

    private void showOrderSelectionWindow(final Record record) {
        showVisualisationSelectionWindow(record, ORDER, ORDER_DTO, candidateOrderVisualisations);
    }

    private void showOpennessLevelSelectionWindow(final Record record) {
        showVisualisationSelectionWindow(record, OPENNESS_LEVEL, OPENNESS_LEVEL_DTO, candidateOpennessLevelVisualisations);
    }

    private void showVisualisationSelectionWindow(final Record record, final String visualisationField, final String visualisationDtoField,
            Map<String, List<RelatedResourceDto>> candidateVisualisations) {
        String dimensionUrn = record.getAttribute(DIMENSION_URN);
        if (!StringUtils.isBlank(dimensionUrn)) {

            List<RelatedResourceDto> candidateOrders = candidateVisualisations.get(dimensionUrn);

            if (candidateOrders.isEmpty()) {
                // There is no visualisation for the dimension
                InformationWindow informationWindow = new InformationWindow(getConstants().codelistVisualisationSelection(), getMessages().dsdDimensionVisualisationNoValues());
                informationWindow.show();
            } else {
                // Show a window to select the visualisation for the dimension
                searchVisualisationWindow = new SearchVisualisationWindow();
                searchVisualisationWindow.setVisualisations(candidateOrders);
                searchVisualisationWindow.show();
                searchVisualisationWindow.getAcceptButton().addClickHandler(new ClickHandler() {

                    @Override
                    public void onClick(ClickEvent event) {
                        RelatedResourceDto selectedVisualisation = searchVisualisationWindow.getSelectedVisualisation();
                        if (selectedVisualisation != null) {
                            record.setAttribute(visualisationField, RelatedResourceUtils.getRelatedResourceName(selectedVisualisation));
                            record.setAttribute(visualisationDtoField, selectedVisualisation);
                        } else {
                            RelatedResourceDto nullRelatedResourceDto = null;
                            record.setAttribute(visualisationField, StringUtils.EMPTY);
                            record.setAttribute(visualisationDtoField, nullRelatedResourceDto);
                        }
                        listGrid.updateData(record);
                        listGrid.markForRedraw();
                        searchVisualisationWindow.markForDestroy();
                    }
                });
            }
        }
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

        record.setAttribute(DIMENSION_URN, dimensionComponentDto.getUrn());
        record.setAttribute(DIMENSION_CODE, dimensionComponentDto.getCode());

        if (currentDimensionVisualisationValues != null) {
            if (currentDimensionVisualisationValues.getDisplayOrder() != null) {
                record.setAttribute(ORDER, RelatedResourceUtils.getRelatedResourceName(currentDimensionVisualisationValues.getDisplayOrder()));
                record.setAttribute(ORDER_DTO, currentDimensionVisualisationValues.getDisplayOrder());
            }
            if (currentDimensionVisualisationValues.getHierarchyLevelsOpen() != null) {
                record.setAttribute(OPENNESS_LEVEL, RelatedResourceUtils.getRelatedResourceName(currentDimensionVisualisationValues.getHierarchyLevelsOpen()));
                record.setAttribute(OPENNESS_LEVEL_DTO, currentDimensionVisualisationValues.getHierarchyLevelsOpen());
            }
        }

        // to show the search icon in the edition columns
        record.setAttribute(ORDER_EDITION_RECORD, GlobalResources.RESOURCE.search().getURL());
        record.setAttribute(OPENNESS_LEVEL_EDITION_RECORD, GlobalResources.RESOURCE.search().getURL());
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

    private HoverCustomizer getFieldHoverCustomizer() {
        return new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                return value != null ? value.toString() : StringUtils.EMPTY;
            }
        };
    }

    private ListGridFieldIfFunction getEditionFieldIfFunction(final boolean editionMode) {
        return new ListGridFieldIfFunction() {

            @Override
            public boolean execute(ListGrid grid, ListGridField field, int fieldNum) {
                return editionMode;
            }
        };
    }

    public List<DimensionVisualisationInfoDto> getDimensionVisualisationInfoDtos() {
        List<DimensionVisualisationInfoDto> dimensionVisualisationInfoDtos = new ArrayList<DimensionVisualisationInfoDto>();

        ListGridRecord[] records = listGrid.getRecords();
        for (ListGridRecord record : records) {

            String dimensionUrn = record.getAttribute(DIMENSION_URN);
            String dimensionCode = record.getAttribute(DIMENSION_CODE);

            RelatedResourceDto order = (RelatedResourceDto) record.getAttributeAsObject(ORDER_DTO);
            RelatedResourceDto opennessLevel = (RelatedResourceDto) record.getAttributeAsObject(OPENNESS_LEVEL_DTO);

            if (order != null || opennessLevel != null) { // Only store the dimensions with some visualisation specified
                DimensionVisualisationInfoDto dimensionVisualisationInfoDto = new DimensionVisualisationInfoDto();
                dimensionVisualisationInfoDto.setType(RelatedResourceTypeEnum.DIMENSION);
                dimensionVisualisationInfoDto.setCode(dimensionCode);
                dimensionVisualisationInfoDto.setUrn(dimensionUrn);
                dimensionVisualisationInfoDto.setDisplayOrder(order);
                dimensionVisualisationInfoDto.setHierarchyLevelsOpen(opennessLevel);
                dimensionVisualisationInfoDtos.add(dimensionVisualisationInfoDto);
            }
        }

        return dimensionVisualisationInfoDtos;
    }

    /**
     * Window to select the visualisation for a dimension
     */
    private class SearchVisualisationWindow extends SearchWindow {

        public SearchVisualisationWindow() {
            super(getConstants().codelistVisualisationSelection());

            setAutoHeight();
            setAutoSize(true);

            listGrid.setMargin(10);
            listGrid.setHeight(100);
            listGrid.setAutoFitMaxRecords(5);
            listGrid.setAutoFitData(Autofit.VERTICAL);
            listGrid.setSelectionType(SelectionStyle.SINGLE);
            listGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);

            ListGridField codeField = new ListGridField(RelatedResourceBaseDS.CODE, MetamacWebCommon.getConstants().relatedResourceCode());
            ListGridField titleField = new ListGridField(RelatedResourceBaseDS.TITLE, MetamacWebCommon.getConstants().relatedResourceTitle());

            listGrid.setFields(codeField, titleField);
        }

        public void setVisualisations(List<RelatedResourceDto> visualisations) {
            for (RelatedResourceDto visualisation : visualisations) {
                RelatedResourceBaseRecord<RelatedResourceDto> record = RecordUtils.getRelatedResourceBaseRecord(visualisation);
                listGrid.addData(record);
            }
        }

        public RelatedResourceDto getSelectedVisualisation() {
            ListGridRecord record = listGrid.getSelectedRecord();
            if (record != null) {
                return (RelatedResourceDto) record.getAttributeAsObject(RelatedResourceBaseDS.DTO);
            }
            return null;
        }
    }
}
