package org.siemac.metamac.srm.web.client.widgets;

import java.util.List;

import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionDto;
import org.siemac.metamac.srm.web.client.MetamacInternalWeb;
import org.siemac.metamac.srm.web.client.model.record.DsdRecord;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;

import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class DsdsItemsContextAreaListGrid extends ListGrid {

    public DsdsItemsContextAreaListGrid() {
        super();

        this.setShowAllRecords(true);

        ListGridField idLogicDsdField = new ListGridField(DsdRecord.ID_LOGIC, MetamacInternalWeb.getConstants().dsdIdLogic());
        ListGridField nameDsdField = new ListGridField(DsdRecord.NAME, MetamacInternalWeb.getConstants().dsdName());
        ListGridField descriptionDsdField = new ListGridField(DsdRecord.DESCRIPTION, MetamacInternalWeb.getConstants().dsdDescription());
        ListGridField finalStructureDsdField = new ListGridField(DsdRecord.FINAL_STRUCTURE, MetamacInternalWeb.getConstants().dsdFinalStructure());
        finalStructureDsdField.setType(ListGridFieldType.BOOLEAN);

        // ToolTip
        idLogicDsdField.setShowHover(true);
        idLogicDsdField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                DsdRecord dsdRecord = (DsdRecord) record;
                return dsdRecord.getIdLogic();
            }
        });
        nameDsdField.setShowHover(true);
        nameDsdField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                DsdRecord dsdRecord = (DsdRecord) record;
                return dsdRecord.getName();
            }
        });
        descriptionDsdField.setShowHover(true);
        descriptionDsdField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                DsdRecord dsdRecord = (DsdRecord) record;
                return dsdRecord.getDescription();
            }
        });
        this.setFields(idLogicDsdField, nameDsdField, descriptionDsdField, finalStructureDsdField);
    }

    public void setDsds(List<DataStructureDefinitionDto> dataStructureDefinitionDtos) {
        DsdRecord[] dsdRecords = new DsdRecord[dataStructureDefinitionDtos.size()];
        for (int i = 0; i < dataStructureDefinitionDtos.size(); i++) {
            dsdRecords[i] = createDsdRecord(dataStructureDefinitionDtos.get(i));
        }
        // Populate the List Grid
        this.setData(dsdRecords);
    }

    /**************************************************************************
     * Private methods
     **************************************************************************/

    private DsdRecord createDsdRecord(DataStructureDefinitionDto dsd) {
        return new DsdRecord(dsd.getId(), dsd.getIdLogic(), InternationalStringUtils.getLocalisedString(dsd.getName()), InternationalStringUtils.getLocalisedString(dsd.getDescription()),
                dsd.getFinalLogic(), dsd);
    }

}
