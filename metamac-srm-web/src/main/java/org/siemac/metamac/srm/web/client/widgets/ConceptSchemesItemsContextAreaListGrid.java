package org.siemac.metamac.srm.web.client.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeDS;
import org.siemac.metamac.srm.web.concept.model.record.ConceptSchemeRecord;
import org.siemac.metamac.srm.web.concept.utils.RecordUtils;

import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ConceptSchemesItemsContextAreaListGrid extends ListGrid {

    public ConceptSchemesItemsContextAreaListGrid() {
        super();

        this.setShowAllRecords(true);

        ListGridField idLogicField = new ListGridField(ConceptSchemeDS.ID_LOGIC, getConstants().conceptSchemeIdLogic());
        ListGridField nameField = new ListGridField(ConceptSchemeDS.NAME, getConstants().conceptSchemeName());
        ListGridField descriptionField = new ListGridField(ConceptSchemeDS.DESCRIPTION, getConstants().conceptSchemeDescription());
        ListGridField finalStructureField = new ListGridField(ConceptSchemeDS.FINAL, getConstants().conceptSchemeFinalStructure());
        finalStructureField.setType(ListGridFieldType.BOOLEAN);

        // ToolTip
        idLogicField.setShowHover(true);
        idLogicField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                ConceptSchemeRecord schemeRecord = (ConceptSchemeRecord) record;
                return schemeRecord.getIdLogic();
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
        this.setFields(idLogicField, nameField, descriptionField, finalStructureField);
    }

    public void setConceptSchemes(List<ConceptSchemeDto> conceptSchemeDtos) {
        ConceptSchemeRecord[] schemeRecords = new ConceptSchemeRecord[conceptSchemeDtos.size()];
        for (int i = 0; i < conceptSchemeDtos.size(); i++) {
            schemeRecords[i] = RecordUtils.getConceptSchemeRecord(conceptSchemeDtos.get(i));
        }
        // Populate the List Grid
        this.setData(schemeRecords);
    }

}
