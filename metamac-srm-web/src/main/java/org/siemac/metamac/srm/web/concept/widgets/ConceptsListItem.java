package org.siemac.metamac.srm.web.concept.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.common.service.utils.shared.SrmUrnParserUtils;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacBasicDto;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;
import org.siemac.metamac.srm.web.concept.model.record.ConceptRecord;
import org.siemac.metamac.srm.web.concept.utils.ConceptsRecordUtils;
import org.siemac.metamac.web.common.client.widgets.form.fields.BaseListItem;
import org.siemac.metamac.web.common.client.widgets.handlers.ListRecordNavigationClickHandler;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ConceptsListItem extends BaseListItem {

    public ConceptsListItem(String name, String title, boolean editionMode, ListRecordNavigationClickHandler recordClickHandler) {
        super(name, title, editionMode);

        listGrid.addRecordClickHandler(recordClickHandler);

        ListGridField codeField = new ListGridField(ConceptDS.CODE, getConstants().identifiableArtefactCode());
        codeField.setWidth("40%");
        ListGridField nameField = new ListGridField(ConceptDS.NAME, getConstants().nameableArtefactName());

        listGrid.setFields(codeField, nameField);
    }

    public void setDataConcepts(List<ConceptMetamacBasicDto> conceptMetamacDtos) {
        listGrid.removeAllData();
        for (ConceptMetamacBasicDto conceptMetamacDto : conceptMetamacDtos) {
            ConceptRecord record = ConceptsRecordUtils.getConceptRecord(conceptMetamacDto);
            addConceptRecordToListGrid(record);
        }
    }

    public void setDataItems(List<ItemVisualisationResult> itemVisualisationResults) {
        listGrid.removeAllData();
        for (ItemVisualisationResult item : itemVisualisationResults) {
            ConceptRecord record = ConceptsRecordUtils.getConceptRecord(item);
            addConceptRecordToListGrid(record);
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

    private void addConceptRecordToListGrid(ConceptRecord record) {
        String conceptUrn = record.getUrn();
        String conceptSchemeUrn = SrmUrnParserUtils.getConceptSchemeUrnFromConceptUrn(conceptUrn);
        record.setLocation(PlaceRequestUtils.buildAbsoluteConceptPlaceRequest(conceptSchemeUrn, conceptUrn));
        listGrid.addData(record);
    }
}
