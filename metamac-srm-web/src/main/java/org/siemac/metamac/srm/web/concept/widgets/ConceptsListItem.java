package org.siemac.metamac.srm.web.concept.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;
import org.siemac.metamac.srm.web.concept.model.record.ConceptRecord;
import org.siemac.metamac.srm.web.concept.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.form.fields.BaseListItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemDto;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ConceptsListItem extends BaseListItem {

    public ConceptsListItem(String name, String title, boolean editionMode) {
        super(name, title, editionMode);

        ListGridField codeField = new ListGridField(ConceptDS.CODE, getConstants().identifiableArtefactCode());
        codeField.setWidth("40%");
        ListGridField nameField = new ListGridField(ConceptDS.NAME, getConstants().nameableArtefactName());

        listGrid.setFields(codeField, nameField);
    }

    public void setDataConcepts(List<ConceptMetamacDto> conceptMetamacDtos) {
        listGrid.removeAllData();
        for (ConceptMetamacDto conceptMetamacDto : conceptMetamacDtos) {
            ConceptRecord record = RecordUtils.getConceptRecord(conceptMetamacDto);
            listGrid.addData(record);
        }
    }

    public void setDataItems(List<ItemDto> itemDtos) {
        listGrid.removeAllData();
        for (ItemDto itemDto : itemDtos) {
            ConceptRecord record = RecordUtils.getConceptRecord(itemDto);
            listGrid.addData(record);
        }
    }

    public List<String> getConceptUrns() {
        List<String> urns = new ArrayList<String>();
        ListGridRecord[] records = listGrid.getRecords();
        if (records != null) {
            for (ListGridRecord record : records) {
                ConceptRecord conceptRecord = (ConceptRecord) record;
                urns.add(conceptRecord.getUrn());
            }
        }
        return urns;
    }
}
