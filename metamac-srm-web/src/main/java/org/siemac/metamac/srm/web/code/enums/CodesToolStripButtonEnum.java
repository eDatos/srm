package org.siemac.metamac.srm.web.code.enums;

import com.smartgwt.client.types.ValueEnum;

public enum CodesToolStripButtonEnum implements ValueEnum {

    CODELIST_FAMILIES("cl_fam_button"), CODELISTS("cl_button"), CODES("codes_button"), VARIABLE_FAMILIES("var_fam_button"), VARIABLES("var_button"), VARIABLE_ELEMENTS("var_ele_button");

    private String value;

    CodesToolStripButtonEnum(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
