package org.siemac.metamac.srm.web.concept.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.widgets.VersionsSectionStack;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeDS;
import org.siemac.metamac.srm.web.concept.utils.RecordUtils;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.widgets.grid.ListGridField;

public class ConceptSchemeVersionsSectionStack extends VersionsSectionStack {

    public ConceptSchemeVersionsSectionStack(String title) {
        super(title);

        ListGridField codeField = new ListGridField(ConceptSchemeDS.CODE, getConstants().maintainableArtefactCode());
        codeField.setWidth("30%");

        ListGridField nameField = new ListGridField(ConceptSchemeDS.NAME, getConstants().maintainableArtefactName());

        ListGridField versionField = new ListGridField(ConceptSchemeDS.VERSION_LOGIC, getConstants().maintainableArtefactVersionLogic());
        versionField.setWidth("15%");

        listGrid.setFields(codeField, nameField, versionField);
    }

    public void setConceptSchemes(List<ConceptSchemeMetamacDto> conceptSchemeDtos) {
        listGrid.selectAllRecords();
        listGrid.removeSelectedData();
        for (ConceptSchemeMetamacDto conceptSchemeDto : conceptSchemeDtos) {
            listGrid.addData(RecordUtils.getConceptSchemeRecord(conceptSchemeDto));
        }
    }

    public void selectConceptScheme(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        RecordList recordList = listGrid.getRecordList();
        Record record = recordList.find(ConceptSchemeDS.URN, conceptSchemeMetamacDto.getUrn());
        listGrid.selectRecord(record);
    }

}
