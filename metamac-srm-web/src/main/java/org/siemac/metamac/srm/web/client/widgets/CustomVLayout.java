package org.siemac.metamac.srm.web.client.widgets;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.layout.VLayout;

public class CustomVLayout extends VLayout {

    public CustomVLayout() {
        setMargin(15);
        setOverflow(Overflow.VISIBLE);
        setAutoHeight();
    }
}
