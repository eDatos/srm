package org.siemac.metamac.srm.web.client.widgets;

import org.siemac.metamac.srm.web.client.model.ds.VersionableResourceDS;
import org.siemac.metamac.web.common.client.utils.ListGridUtils;

public class VersionableResourceCustomListGrid extends NavigableListGrid {

    public VersionableResourceCustomListGrid() {
        ListGridUtils.setCheckBoxSelectionType(this);
        setShowGroupSummary(true);
        setGroupStartOpen(com.smartgwt.client.types.GroupStartOpen.ALL);
        setGroupTitleField(VersionableResourceDS.CODE);
        setGroupNodeStyle("customGroupNode");
        setCanSort(false);
    }
}
