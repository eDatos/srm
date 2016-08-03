package org.siemac.metamac.srm.web.organisation.enums;

import com.smartgwt.client.types.ValueEnum;

public enum OrganisationsToolStripButtonEnum implements ValueEnum {

    ORGANISATION_SCHEMES("org_scheme_button"), ORGANISATIONS("organisation_button");

    private String value;

    OrganisationsToolStripButtonEnum(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
