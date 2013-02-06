package org.siemac.metamac.srm.web.client.widgets;

import org.siemac.metamac.srm.web.client.resources.GlobalResources;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Label;

public class InformationLabel extends Label {

    public InformationLabel(String contents) {
        super(contents);
        setHeight(20);
        setIcon(GlobalResources.RESOURCE.info().getURL());
        setOverflow(Overflow.VISIBLE);
        setValign(VerticalAlignment.CENTER);
    }
}
