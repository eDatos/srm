package org.siemac.metamac.srm.web.client.widgets;

import org.siemac.metamac.srm.web.client.model.ds.VersionableResourceDS;
import org.siemac.metamac.web.common.client.widgets.CustomListGrid;

public class VersionableResourceCustomListGrid extends CustomListGrid {

    public VersionableResourceCustomListGrid() {
        setShowGroupSummary(true);
        setGroupStartOpen(com.smartgwt.client.types.GroupStartOpen.ALL);
        setGroupByField(VersionableResourceDS.RESOURCE_ID);
        setGroupTitleField(VersionableResourceDS.CODE);
    }
}
