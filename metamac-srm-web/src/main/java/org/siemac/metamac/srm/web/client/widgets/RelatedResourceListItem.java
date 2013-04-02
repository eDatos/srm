package org.siemac.metamac.srm.web.client.widgets;

import org.siemac.metamac.web.common.client.widgets.form.fields.RelatedResourceBaseListItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

public class RelatedResourceListItem extends RelatedResourceBaseListItem<RelatedResourceDto> {

    public RelatedResourceListItem(String name, String title, boolean editionMode) {
        super(name, title, editionMode);
    }
}
