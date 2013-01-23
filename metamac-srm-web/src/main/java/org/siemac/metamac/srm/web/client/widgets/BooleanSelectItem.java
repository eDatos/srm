package org.siemac.metamac.srm.web.client.widgets;

import org.siemac.metamac.srm.web.client.enums.BooleanItemEnum;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;

import com.smartgwt.client.widgets.form.fields.SelectItem;

public class BooleanSelectItem extends SelectItem {

    public BooleanSelectItem(String name, String title) {
        super(name, title);
        setValueMap(CommonUtils.getBooleanHashMap());
    }

    public void setBooleanValue(Boolean value) {
        BooleanItemEnum booleanItemEnum = CommonUtils.getBooleanItemEnum(value);
        setValue(booleanItemEnum.toString());
    }

    public Boolean getBooleanValue() {
        return CommonUtils.getBooleanValue(getValueAsString());
    }
}
