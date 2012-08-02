package org.siemac.metamac.srm.web.client.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.srm.core.concept.dto.MetamacConceptSchemeDto;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeDS;
import org.siemac.metamac.srm.web.concept.model.record.ConceptSchemeRecord;
import org.siemac.metamac.srm.web.concept.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.BaseCustomListGrid;

import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ConceptSchemeListGrid extends BaseCustomListGrid {

    public ConceptSchemeListGrid() {
        super();

        this.setShowAllRecords(true);

        ListGridField codeField = new ListGridField(ConceptSchemeDS.CODE, getConstants().conceptSchemeCode());
        ListGridField nameField = new ListGridField(ConceptSchemeDS.NAME, getConstants().conceptSchemeName());
        ListGridField descriptionField = new ListGridField(ConceptSchemeDS.DESCRIPTION, getConstants().conceptSchemeDescription());

        // ToolTip
        codeField.setShowHover(true);
        codeField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                ConceptSchemeRecord schemeRecord = (ConceptSchemeRecord) record;
                return schemeRecord.getCode();
            }
        });
        nameField.setShowHover(true);
        nameField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                ConceptSchemeRecord schemeRecord = (ConceptSchemeRecord) record;
                return schemeRecord.getName();
            }
        });
        descriptionField.setShowHover(true);
        descriptionField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                ConceptSchemeRecord schemeRecord = (ConceptSchemeRecord) record;
                return schemeRecord.getDescription();
            }
        });
        this.setFields(codeField, nameField, descriptionField);
    }

    public void setConceptSchemes(List<MetamacConceptSchemeDto> conceptSchemeDtos) {
        removeAllData();
        if (conceptSchemeDtos != null) {
            ConceptSchemeRecord[] schemeRecords = new ConceptSchemeRecord[conceptSchemeDtos.size()];
            for (int i = 0; i < conceptSchemeDtos.size(); i++) {
                schemeRecords[i] = RecordUtils.getConceptSchemeRecord(conceptSchemeDtos.get(i));
            }
            this.setData(schemeRecords);
        }
    }
}
