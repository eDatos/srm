package org.siemac.metamac.srm.web.client.widgets;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.util.shared.BooleanUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.web.common.client.model.record.RelatedResourceBaseRecord;
import org.siemac.metamac.web.common.client.widgets.form.fields.RelatedResourceBaseListItem;
import org.siemac.metamac.web.common.client.widgets.handlers.ListRecordNavigationClickHandler;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.validator.RequiredIfFunction;
import com.smartgwt.client.widgets.form.validator.RequiredIfValidator;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class RelatedResourceListItem extends RelatedResourceBaseListItem<RelatedResourceDto> {

    private boolean required;

    public RelatedResourceListItem(String name, String title, boolean editionMode, ListRecordNavigationClickHandler listRecordNavigationClickHandler) {
        super(name, title, editionMode, listRecordNavigationClickHandler);
    }

    @Override
    protected List<PlaceRequest> buildLocation(RelatedResourceDto relatedResourceDto) {
        return PlaceRequestUtils.buildAbsoluteResourcePlaceRequest(relatedResourceDto);
    }

    @SuppressWarnings("rawtypes")
    public List<String> getSelectedRelatedResourceUrns() {
        List<String> urns = new ArrayList<String>();
        ListGridRecord[] records = listGrid.getRecords();
        if (records != null) {
            for (ListGridRecord record : records) {
                RelatedResourceBaseRecord relatedResourceRecord = (RelatedResourceBaseRecord) record;
                urns.add(relatedResourceRecord.getUrn());
            }
        }
        return urns;
    }

    @Override
    public void setRequired(Boolean required) {
        this.required = BooleanUtils.isTrue(required);
        if (this.required) {
            setTitleStyle("requiredFormLabel");
            setValidators(new RequiredIfValidator(new RequiredIfFunction() {

                @Override
                public boolean execute(FormItem formItem, Object value) {
                    return RelatedResourceListItem.this.required ? listGrid.getRecords().length == 0 : false;
                }
            }));
        } else {
            setValidators();
        }
    }
    @Override
    public Boolean validate() {
        if (required) {
            return listGrid.getRecords().length > 0;
        }
        return true;
    }
}
