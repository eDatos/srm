package org.siemac.metamac.srm.web.client.widgets;

import java.util.List;

import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.web.common.client.widgets.form.fields.SearchRelatedResourceBaseLinkItem;
import org.siemac.metamac.web.common.client.widgets.handlers.CustomLinkItemNavigationClickHandler;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;

public class SearchRelatedResourceLinkItem extends SearchRelatedResourceBaseLinkItem<RelatedResourceDto> {

    public SearchRelatedResourceLinkItem(String name, String title, CustomLinkItemNavigationClickHandler clickHandler) {
        super(name, title, clickHandler);
    }

    @Override
    protected List<PlaceRequest> buildLocation(RelatedResourceDto relatedResourceDto) {
        return PlaceRequestUtils.buildAbsoluteResourcePlaceRequest(relatedResourceDto);
    }
}
