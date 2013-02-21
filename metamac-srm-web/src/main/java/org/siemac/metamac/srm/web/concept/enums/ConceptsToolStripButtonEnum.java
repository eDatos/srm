package org.siemac.metamac.srm.web.concept.enums;

import com.smartgwt.client.types.ValueEnum;

public enum ConceptsToolStripButtonEnum implements ValueEnum {

    CONCEPT_SCHEMES("con_scheme_button"), CONCEPTS("con_button");

    private String value;

    ConceptsToolStripButtonEnum(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
