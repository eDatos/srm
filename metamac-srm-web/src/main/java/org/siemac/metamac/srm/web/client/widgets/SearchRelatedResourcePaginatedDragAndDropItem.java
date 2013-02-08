package org.siemac.metamac.srm.web.client.widgets;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.web.client.model.ds.RelatedResourceDS;
import org.siemac.metamac.srm.web.client.model.record.RelatedResourceRecord;
import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.fields.BaseSearchPaginatedDragAndDropItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;

public class SearchRelatedResourcePaginatedDragAndDropItem extends BaseSearchPaginatedDragAndDropItem {

    public SearchRelatedResourcePaginatedDragAndDropItem(String name, String title, int maxResults, PaginatedAction action) {
        super(name, title, maxResults, action);
        create(name, title, maxResults, FormItemUtils.FORM_ITEM_WIDTH, action);
    }

    public SearchRelatedResourcePaginatedDragAndDropItem(String name, String title, int maxResults, int formItemWidth, PaginatedAction action) {
        super(name, title, maxResults, formItemWidth, action);
        create(name, title, maxResults, formItemWidth, action);
    }

    private void create(String name, String title, int maxResults, int formItemWidth, PaginatedAction action) {
        ListGridField codeField = new ListGridField(RelatedResourceDS.CODE);
        codeField.setShowHover(true);
        codeField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                RelatedResourceRecord relatedResourceRecord = (RelatedResourceRecord) record;
                return relatedResourceRecord != null ? relatedResourceRecord.getCode() : new String();
            }
        });

        ListGridField titleField = new ListGridField(RelatedResourceDS.TITLE);
        titleField.setShowHover(true);
        titleField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                RelatedResourceRecord relatedResourceRecord = (RelatedResourceRecord) record;
                return relatedResourceRecord != null ? relatedResourceRecord.getTitle() : new String();
            }
        });

        ListGridField urnField = new ListGridField(RelatedResourceDS.URN);
        urnField.setShowHover(true);
        urnField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                RelatedResourceRecord relatedResourceRecord = (RelatedResourceRecord) record;
                return relatedResourceRecord != null ? relatedResourceRecord.getUrn() : new String();
            }
        });

        ListGridField deleteField = new ListGridField(DELETE_FIELD_NAME);
        deleteField.setType(ListGridFieldType.IMAGE);
        deleteField.setWidth("8%");
        deleteField.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (isRecordInTargetList((RelatedResourceRecord) event.getRecord())) {
                    targetList.removeData(event.getRecord());
                }
            }
        });

        sourceList.getListGrid().setFields(codeField, titleField, urnField);
        targetList.setFields(codeField, titleField, urnField, deleteField);
    }

    public void setSourceRelatedResources(List<RelatedResourceDto> relatedResources) {
        RelatedResourceRecord[] records = new RelatedResourceRecord[relatedResources.size()];
        for (int i = 0; i < relatedResources.size(); i++) {
            records[i] = org.siemac.metamac.srm.web.client.utils.RecordUtils.getRelatedResourceRecord(relatedResources.get(i));
            records[i].setAttribute(DELETE_FIELD_NAME, org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.deleteListGrid().getURL());
        }
        sourceList.getListGrid().setData(records);
    }

    public void setTargetRelatedResources(List<RelatedResourceDto> externalItems) {
        clearTargetList();
        for (RelatedResourceDto relatedResourceDto : externalItems) {
            RelatedResourceRecord record = org.siemac.metamac.srm.web.client.utils.RecordUtils.getRelatedResourceRecord(relatedResourceDto);
            record.setAttribute(DELETE_FIELD_NAME, org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.deleteListGrid().getURL());
            addNonDuplicatedRecordToTarget(record);
        }
    }

    public List<RelatedResourceDto> getSelectedRelatedResources() {
        List<RelatedResourceDto> selectedRelatedResources = new ArrayList<RelatedResourceDto>();
        ListGridRecord[] records = targetList.getRecords();
        for (int i = 0; i < records.length; i++) {
            RelatedResourceRecord record = (RelatedResourceRecord) records[i];
            selectedRelatedResources.add(record.getRelatedResourceDto());
        }
        return selectedRelatedResources;
    }

    @Override
    protected void addNonDuplicatedRecordToTarget(Record record) {
        String urn = record.getAttribute(RelatedResourceDS.URN);
        if (targetList.getRecordList().find(RelatedResourceDS.URN, urn) == null) {
            targetList.addData(record);
        }
    }

    public String getRelatedResourceCriteria() {
        return getSearchCriteria();
    }

    private boolean isRecordInTargetList(RelatedResourceRecord record) {
        ListGridRecord[] records = targetList.getRecords();
        for (int i = 0; i < records.length; i++) {
            RelatedResourceRecord relatedResourceRecord = (RelatedResourceRecord) records[i];
            if (record.getUrn() != null && record.getUrn().equals(relatedResourceRecord.getUrn())) {
                return true;
            }
        }
        return false;
    }
}
