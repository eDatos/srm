package org.siemac.metamac.srm.web.client.widgets;

import org.siemac.metamac.srm.web.client.utils.CommonUtils;

import com.smartgwt.client.widgets.form.fields.SelectItem;

public class BooleanSelectItem extends SelectItem {

    public BooleanSelectItem(String name, String title) {
        super(name, title);
        setValueMap(CommonUtils.getBooleanHashMap());
    }
}
