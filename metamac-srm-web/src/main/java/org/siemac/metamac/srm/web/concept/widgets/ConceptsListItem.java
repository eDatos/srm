package org.siemac.metamac.srm.web.concept.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;
import org.siemac.metamac.srm.web.concept.model.record.ConceptRecord;
import org.siemac.metamac.srm.web.concept.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.BaseCustomListGrid;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCanvasItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.SearchViewTextItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemDto;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.events.HasFormItemClickHandlers;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;

public class ConceptsListItem extends CustomCanvasItem {

    private BaseCustomListGrid listGrid;

    private SearchViewTextItem searchViewTextItem;

    public ConceptsListItem(String name, String title, boolean editionMode) {
        super(name, title);
        setCellStyle("dragAndDropCellStyle");

        ListGridField codeField = new ListGridField(ConceptDS.CODE, getConstants().conceptCode());
        codeField.setWidth("40%");
        ListGridField nameField = new ListGridField(ConceptDS.NAME, getConstants().conceptName());

        listGrid = new BaseCustomListGrid();
        listGrid.setAutoFitMaxRecords(6);
        listGrid.setAutoFitData(Autofit.VERTICAL);
        listGrid.setFields(codeField, nameField);

        HLayout hLayout = new HLayout();
        hLayout.addMember(listGrid);

        // In edition mode, add a search icon to edit concept list
        if (editionMode) {
            searchViewTextItem = new SearchViewTextItem();
            searchViewTextItem.setShowTitle(false);

            DynamicForm form = new DynamicForm();
            form.setFields(searchViewTextItem);

            hLayout.addMember(form);
        }

        setCanvas(hLayout);
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

    public HasFormItemClickHandlers getSearchIcon() {
        return searchViewTextItem.getSearchIcon();
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
