package org.siemac.metamac.srm.web.client.widgets;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.model.ds.RelatedResourceDS;
import org.siemac.metamac.srm.web.client.model.record.RelatedResourceRecord;
import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.fields.BaseSearchPaginatedItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.smartgwt.client.widgets.form.validator.CustomValidator;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class SearchRelatedResourcePaginatedItem extends BaseSearchPaginatedItem {

    public SearchRelatedResourcePaginatedItem(String name, String title, int formItemWidth, int maxResults, PaginatedAction action) {
        super(name, title, formItemWidth, maxResults, action);
        create(name, title, formItemWidth, maxResults);
    }

    public SearchRelatedResourcePaginatedItem(String name, String title, int maxResults, PaginatedAction action) {
        super(name, title, FormItemUtils.FORM_ITEM_WIDTH, maxResults, action);
        create(name, title, FormItemUtils.FORM_ITEM_WIDTH, maxResults);
    }

    private void create(String name, String title, int formItemWidth, int maxResults) {

        // Set list grid fields

        ListGridField codeField = new ListGridField(RelatedResourceDS.CODE, MetamacSrmWeb.getConstants().identifiableArtefactCode());
        codeField.setShowHover(true);
        codeField.setWidth("30%");
        codeField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                RelatedResourceRecord relatedResourceRecord = (RelatedResourceRecord) record;
                return relatedResourceRecord.getCode();
            }
        });

        ListGridField titleField = new ListGridField(RelatedResourceDS.TITLE, MetamacSrmWeb.getConstants().nameableArtefactName());
        titleField.setShowHover(true);
        titleField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                RelatedResourceRecord relatedResourceRecord = (RelatedResourceRecord) record;
                return relatedResourceRecord != null ? relatedResourceRecord.getTitle() : StringUtils.EMPTY;
            }
        });

        ListGridField urnField = new ListGridField(RelatedResourceDS.URN, MetamacSrmWeb.getConstants().identifiableArtefactUrn());
        urnField.setShowHover(true);
        urnField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                RelatedResourceRecord relatedResourceRecord = (RelatedResourceRecord) record;
                return relatedResourceRecord.getUrn();
            }
        });

        paginatedCheckListGrid.getListGrid().setFields(codeField, titleField, urnField);
    }

    public void setRelatedResources(List<RelatedResourceDto> relatedResources) {
        RelatedResourceRecord[] records = new RelatedResourceRecord[relatedResources.size()];
        for (int i = 0; i < relatedResources.size(); i++) {
            records[i] = org.siemac.metamac.srm.web.client.utils.RecordUtils.getRelatedResourceRecord(relatedResources.get(i));
        }
        paginatedCheckListGrid.getListGrid().setData(records);
    }

    public RelatedResourceDto getSelectedRelatedResource() {
        ListGridRecord selectedRecord = paginatedCheckListGrid.getListGrid().getSelectedRecord();
        if (selectedRecord != null) {
            return ((RelatedResourceRecord) selectedRecord).getRelatedResourceDto();
        }
        return null;
    }

    public String getRelatedResourceCriteria() {
        return searchItem.getValueAsString();
    }

    public void setRequired(boolean required) {
        if (required) {
            setTitleStyle("requiredFormLabel");
            CustomValidator customValidator = new CustomValidator() {

                @Override
                protected boolean condition(Object value) {
                    return getSelectedRelatedResource() != null;
                }
            };
            setValidators(customValidator);
        }
    }
}
