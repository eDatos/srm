package org.siemac.metamac.srm.web.client.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.siemac.metamac.srm.web.client.model.ds.RelatedResourceDS;
import org.siemac.metamac.srm.web.client.model.record.RelatedResourceRecord;
import org.siemac.metamac.web.common.client.widgets.form.fields.BaseListItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class RelatedResourceListItem extends BaseListItem {

    public RelatedResourceListItem(String name, String title, boolean editionMode) {
        super(name, title, editionMode);

        ListGridField codeField = new ListGridField(RelatedResourceDS.CODE, getConstants().identifiableArtefactCode());
        codeField.setShowHover(true);
        codeField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                return ((RelatedResourceRecord) record).getCode();
            }
        });

        ListGridField nameField = new ListGridField(RelatedResourceDS.TITLE, getConstants().nameableArtefactName());
        nameField.setShowHover(true);
        nameField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                return ((RelatedResourceRecord) record).getTitle();
            }
        });

        ListGridField urnField = new ListGridField(RelatedResourceDS.URN, getConstants().identifiableArtefactUrn());
        urnField.setShowHover(true);
        urnField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                return ((RelatedResourceRecord) record).getUrn();
            }
        });

        listGrid.setFields(codeField, nameField, urnField);
    }

    public void setRelatedResources(List<RelatedResourceDto> relatedResourceDtos) {
        listGrid.removeAllData();
        for (RelatedResourceDto relatedResourceDto : relatedResourceDtos) {
            RelatedResourceRecord record = org.siemac.metamac.srm.web.client.utils.RecordUtils.getRelatedResourceRecord(relatedResourceDto);
            listGrid.addData(record);
        }
    }

    public void setRelatedResources(Set<RelatedResourceDto> relatedResourceDtos) {
        setRelatedResources(new ArrayList<RelatedResourceDto>(relatedResourceDtos));
    }

    public List<RelatedResourceDto> getRelatedResourceDtos() {
        List<RelatedResourceDto> selectedRelatedResourceDtos = new ArrayList<RelatedResourceDto>();
        ListGridRecord records[] = listGrid.getRecords();
        if (records != null) {
            for (ListGridRecord record : records) {
                RelatedResourceRecord relatedResourceRecord = (RelatedResourceRecord) record;
                selectedRelatedResourceDtos.add(relatedResourceRecord.getRelatedResourceDto());
            }

        }
        return selectedRelatedResourceDtos;
    }

    public List<String> getSelectedRelatedResourceUrns() {
        List<String> urns = new ArrayList<String>();
        ListGridRecord[] records = listGrid.getRecords();
        if (records != null) {
            for (ListGridRecord record : records) {
                RelatedResourceRecord relatedResourceRecord = (RelatedResourceRecord) record;
                urns.add(relatedResourceRecord.getUrn());
            }
        }
        return urns;
    }

    public List<RelatedResourceDto> getSelectedRelatedResources() {
        List<RelatedResourceDto> relatedResources = new ArrayList<RelatedResourceDto>();
        ListGridRecord[] records = listGrid.getRecords();
        if (records != null) {
            for (ListGridRecord record : records) {
                RelatedResourceRecord relatedResourceRecord = (RelatedResourceRecord) record;
                relatedResources.add(relatedResourceRecord.getRelatedResourceDto());
            }
        }
        return relatedResources;
    }
}
