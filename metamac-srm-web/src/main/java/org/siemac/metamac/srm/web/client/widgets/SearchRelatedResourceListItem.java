package org.siemac.metamac.srm.web.client.widgets;

import java.util.List;

import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.web.common.client.widgets.form.fields.SearchRelatedResourceBaseListItem;
import org.siemac.metamac.web.common.client.widgets.handlers.ListRecordNavigationClickHandler;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;

public abstract class SearchRelatedResourceListItem extends SearchRelatedResourceBaseListItem<RelatedResourceDto> {

    public SearchRelatedResourceListItem(String name, String title, ListRecordNavigationClickHandler navigationHnadler) {
        super(name, title, navigationHnadler);
    }

    public SearchRelatedResourceListItem(String name, String title) {
        super(name, title);
    }

    @Override
    protected List<PlaceRequest> buildLocation(RelatedResourceDto relatedResourceDto) {
        return PlaceRequestUtils.buildAbsoluteResourcePlaceRequest(relatedResourceDto);
    }
}
