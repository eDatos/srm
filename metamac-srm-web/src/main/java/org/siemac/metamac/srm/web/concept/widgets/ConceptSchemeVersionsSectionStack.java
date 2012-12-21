package org.siemac.metamac.srm.web.concept.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.widgets.VersionsSectionStack;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeDS;
import org.siemac.metamac.srm.web.concept.utils.RecordUtils;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;

public class ConceptSchemeVersionsSectionStack extends VersionsSectionStack {

    public ConceptSchemeVersionsSectionStack(String title) {
        super(title);
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
        if (record != null) {
            listGrid.selectRecord(record);
        }
    }
}
