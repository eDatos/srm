package org.siemac.metamac.srm.web.concept.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.widgets.ItemSchemeVersionsSectionStack;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeDS;
import org.siemac.metamac.srm.web.concept.utils.ConceptsRecordUtils;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.SortDirection;

public class ConceptSchemeVersionsSectionStack extends ItemSchemeVersionsSectionStack {

    public ConceptSchemeVersionsSectionStack(String title) {
        super(title);
    }

    public void setConceptSchemes(List<ConceptSchemeMetamacBasicDto> conceptSchemeDtos) {
        listGrid.selectAllRecords();
        listGrid.removeSelectedData();
        for (ConceptSchemeMetamacBasicDto conceptSchemeDto : conceptSchemeDtos) {
            listGrid.addData(ConceptsRecordUtils.getConceptSchemeRecord(conceptSchemeDto));
        }
        listGrid.sort(ConceptSchemeDS.VERSION_LOGIC, SortDirection.DESCENDING);
    }

    public void selectConceptScheme(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        RecordList recordList = listGrid.getRecordList();
        Record record = recordList.find(ConceptSchemeDS.URN, conceptSchemeMetamacDto.getUrn());
        if (record != null) {
            listGrid.selectRecord(record);
        }
    }
}
