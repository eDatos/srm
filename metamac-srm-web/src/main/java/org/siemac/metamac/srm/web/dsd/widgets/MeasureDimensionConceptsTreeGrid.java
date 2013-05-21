package org.siemac.metamac.srm.web.dsd.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.common.service.utils.shared.SrmUrnParserUtils;
import org.siemac.metamac.srm.core.concept.domain.shared.ConceptMetamacVisualisationResult;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.dsd.dto.MeasureDimensionPrecisionDto;
import org.siemac.metamac.srm.web.client.model.ds.ItemDS;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.code.model.ds.CodeDS;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;
import org.siemac.metamac.srm.web.concept.view.handlers.BaseConceptUiHandlers;
import org.siemac.metamac.srm.web.concept.widgets.ConceptsTreeGrid;
import org.siemac.metamac.srm.web.dsd.model.ds.DataStructureDefinitionDS;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.widgets.CustomLinkTreeGridField;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.tree.TreeGridField;

public class MeasureDimensionConceptsTreeGrid extends ConceptsTreeGrid {

    private final static String                CONCEPT_FIELD = "concept-field";

    private List<MeasureDimensionPrecisionDto> measureDimensionPrecisionDtos;

    public MeasureDimensionConceptsTreeGrid(boolean editionMode) {
        super();

        setShowFilterEditor(false);
        setCanCollapseGroup(false);

        // This field will show the code and the name of the concept
        codeField = new CustomLinkTreeGridField(CONCEPT_FIELD, getConstants().identifiableArtefactCode());
        codeField.setWidth("30%");

        TreeGridField showDecimalsPrecisionField = new TreeGridField(DataStructureDefinitionDS.SHOW_DECIMALS_PRECISION, getConstants().dsdDecimals());
        showDecimalsPrecisionField.setWidth("20%");
        showDecimalsPrecisionField.setCanEdit(editionMode);
        showDecimalsPrecisionField.setCanFilter(false);
        showDecimalsPrecisionField.setEditorType(new SelectItem(DataStructureDefinitionDS.SHOW_DECIMALS, getConstants().dsdShowDecimals()));
        showDecimalsPrecisionField.setValueMap(org.siemac.metamac.srm.web.dsd.utils.CommonUtils.getDsdShowDecimalsHashMap());
        showDecimalsPrecisionField.setShowHover(false);

        setFields(codeField, showDecimalsPrecisionField);

        // Add handler to enable the navigation

        cellClickHandlerRegistration.removeHandler();
        addCellClickHandler(new CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                Record record = event.getRecord();
                ListGridField field = getField(event.getColNum());
                if (ListGridFieldType.LINK.equals(field.getType()) && CONCEPT_FIELD.equals(field.getName())) {
                    String urn = record.getAttribute(ConceptDS.URN);
                    if (SrmUrnParserUtils.isConceptSchemeUrn(urn)) {
                        MeasureDimensionConceptsTreeGrid.this.uiHandlers.goTo(PlaceRequestUtils.buildAbsoluteConceptSchemePlaceRequest(urn));
                    } else if (SrmUrnParserUtils.isConceptUrn(urn)) {
                        MeasureDimensionConceptsTreeGrid.this.uiHandlers.goTo(PlaceRequestUtils.buildAbsoluteConceptPlaceRequest(urn));
                    }
                }
            }
        });
    }

    @Override
    public boolean canEditCell(int rowNum, int colNum) {
        // Disable editing the first row (contains the concept scheme)
        return rowNum != 0;
    }

    public void setMeasureDimensionPrecisions(List<MeasureDimensionPrecisionDto> measureDimensionPrecisionDtos) {
        this.measureDimensionPrecisionDtos = measureDimensionPrecisionDtos;
        updateRecords();
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

    private void updateRecords() {
        ListGridRecord[] records = getRecords();
        for (ListGridRecord record : records) {
            // update concept
            record.setAttribute(CONCEPT_FIELD, CommonWebUtils.getElementName(record.getAttribute(CodeDS.CODE), record.getAttribute(CodeDS.NAME)));
            // update decimals
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

    @Override
    public void setConcepts(ConceptSchemeMetamacDto conceptSchemeMetamacDto, List<ConceptMetamacVisualisationResult> itemHierarchyDtos) {
        super.setConcepts(conceptSchemeMetamacDto, itemHierarchyDtos);
        updateRecords();
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
    protected void onNodeContextClick(String nodeName, ItemVisualisationResult concept) {
        // Overwrite this method to do nothing
    }
}
