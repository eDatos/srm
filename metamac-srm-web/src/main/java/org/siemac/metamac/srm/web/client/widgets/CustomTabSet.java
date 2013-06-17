package org.siemac.metamac.srm.web.client.widgets;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.tab.TabSet;

public class CustomTabSet extends TabSet {

    public CustomTabSet() {
        setHeight(50);
        setStyleName("marginTop15");
        setOverflow(Overflow.VISIBLE);
        setPaneContainerOverflow(Overflow.VISIBLE);
    }
}
