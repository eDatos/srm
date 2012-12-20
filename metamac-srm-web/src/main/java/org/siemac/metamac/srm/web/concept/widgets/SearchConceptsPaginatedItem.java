package org.siemac.metamac.srm.web.concept.widgets;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;
import org.siemac.metamac.srm.web.concept.model.record.ConceptRecord;
import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.fields.BaseSearchPaginatedItem;

import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class SearchConceptsPaginatedItem extends BaseSearchPaginatedItem {

    public SearchConceptsPaginatedItem(String name, String title, int formItemWidth, int maxResults, PaginatedAction action) {
        super(name, title, formItemWidth, maxResults, action);
        create(name, title, formItemWidth, maxResults, action);
    }

    public SearchConceptsPaginatedItem(String name, String title, int maxResults, PaginatedAction action) {
        super(name, title, FormItemUtils.FORM_ITEM_WIDTH, maxResults, action);
        create(name, title, FormItemUtils.FORM_ITEM_WIDTH, maxResults, action);
    }

    private void create(String name, String title, int formItemWidth, int maxResults, PaginatedAction action) {

        // Set list grid fields

        ListGridField codeField = new ListGridField(ConceptDS.CODE, MetamacSrmWeb.getConstants().identifiableArtefactCode());
        codeField.setShowHover(true);
        codeField.setWidth("30%");
        codeField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                ConceptRecord conceptRecord = (ConceptRecord) record;
                return conceptRecord.getCode();
            }
        });

        ListGridField titleField = new ListGridField(ConceptDS.NAME, MetamacSrmWeb.getConstants().nameableArtefactName());
        titleField.setShowHover(true);
        titleField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                ConceptRecord conceptRecord = (ConceptRecord) record;
                return conceptRecord.getName();
            }
        });

        ListGridField urnField = new ListGridField(ConceptDS.URN, MetamacSrmWeb.getConstants().identifiableArtefactUrn());
        urnField.setShowHover(true);
        urnField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                ConceptRecord conceptRecord = (ConceptRecord) record;
                return conceptRecord.getUrn();
            }
        });

        paginatedCheckListGrid.getListGrid().setFields(codeField, titleField, urnField);
    }

    public void setConcepts(List<ConceptMetamacDto> conceptsMetamacDtos) {
        ConceptRecord[] records = new ConceptRecord[conceptsMetamacDtos.size()];
        for (int i = 0; i < conceptsMetamacDtos.size(); i++) {
            records[i] = org.siemac.metamac.srm.web.concept.utils.RecordUtils.getConceptRecord(conceptsMetamacDtos.get(i));
        }
        paginatedCheckListGrid.getListGrid().setData(records);
    }

    public List<ConceptMetamacDto> getSelectedConcepts() {
        ListGridRecord[] selectedRecords = paginatedCheckListGrid.getListGrid().getSelectedRecords();
        if (selectedRecords != null) {
            List<ConceptMetamacDto> selectedConcepts = new ArrayList<ConceptMetamacDto>();
            for (ListGridRecord record : selectedRecords) {
                selectedConcepts.add(((ConceptRecord) record).getConceptDto());
            }
            return selectedConcepts;
        }
        return null;
    }
}
