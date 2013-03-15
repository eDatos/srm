package org.siemac.metamac.srm.core.criteria;

public enum DataStructureDefinitionVersionMetamacCriteriaPropertyEnum {

    CODE,
    URN,
    NAME,
    DESCRIPTION,
    PROC_STATUS,
    STATISTICAL_OPERATION_URN,
    DIMENSION_CONCEPT_URN,
    ATTRIBUTE_CONCEPT_URN,
    IS_LAST_VERSION,
    LATEST_FINAL,
    LATEST_PUBLIC,
    INTERNAL_PUBLICATION_DATE,
    INTERNAL_PUBLICATION_USER,
    EXTERNAL_PUBLICATION_DATE,
    EXTERNAL_PUBLICATION_USER;

    public String value() {
        return name();
    }

    public static DataStructureDefinitionVersionMetamacCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }

}
