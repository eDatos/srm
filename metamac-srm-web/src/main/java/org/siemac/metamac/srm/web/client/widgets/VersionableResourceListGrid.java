package org.siemac.metamac.srm.web.client.widgets;

import org.siemac.metamac.srm.web.client.utils.ResourceListFieldUtils;
import org.siemac.metamac.web.common.client.widgets.BaseCustomListGrid;

public class VersionableResourceListGrid extends BaseCustomListGrid {

    public VersionableResourceListGrid() {
        super();
        this.setFields(ResourceListFieldUtils.getVersionableResourceFields());
    }
}
