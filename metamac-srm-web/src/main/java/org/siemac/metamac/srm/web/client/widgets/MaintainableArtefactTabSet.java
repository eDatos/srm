package org.siemac.metamac.srm.web.client.widgets;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.tab.TabSet;

public class MaintainableArtefactTabSet extends TabSet {

    public MaintainableArtefactTabSet() {
        setStyleName("marginTop15");
        setHeight100();
        setOverflow(Overflow.VISIBLE);
        setPaneContainerOverflow(Overflow.VISIBLE);
    }
}
