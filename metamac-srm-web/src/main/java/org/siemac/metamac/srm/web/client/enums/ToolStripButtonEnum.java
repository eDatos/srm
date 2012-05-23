package org.siemac.metamac.srm.web.client.enums;

import com.smartgwt.client.types.ValueEnum;

public enum ToolStripButtonEnum implements ValueEnum {

    DSD_LIST("dsd_list_button"), CONCEPTS("concepts_button"), ORGANISATIONS("org_button"), CLASSIFICATIONS("classif_button"), CATEGORIES("categ_button");

    private String value;

    ToolStripButtonEnum(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }

}
