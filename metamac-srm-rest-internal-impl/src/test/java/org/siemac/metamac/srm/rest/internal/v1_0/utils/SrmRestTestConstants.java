package org.siemac.metamac.srm.rest.internal.v1_0.utils;

import org.siemac.metamac.rest.common.v1_0.domain.ComparisonOperator;
import org.siemac.metamac.rest.common.v1_0.domain.LogicalOperator;
import org.siemac.metamac.rest.common.v1_0.domain.OrderOperator;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptCriteriaPropertyRestriction;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptSchemeCriteriaPropertyRestriction;

public class SrmRestTestConstants {

    public static String NOT_EXISTS                                 = "notexists";
    public static String AGENCY_1                                   = "agency1";
    public static String AGENCY_2                                   = "agency2";

    public static String CONCEPT_SCHEME_1_CODE                      = "conceptScheme1";
    public static String CONCEPT_SCHEME_1_VERSION_1                 = "01.000";

    public static String CONCEPT_SCHEME_2_CODE                      = "conceptScheme2";
    public static String CONCEPT_SCHEME_2_VERSION_1                 = "01.000";
    public static String CONCEPT_SCHEME_2_VERSION_2                 = "02.000";
    public static String CONCEPT_SCHEME_3_CODE                      = "conceptScheme3";
    public static String CONCEPT_SCHEME_3_VERSION_1                 = "01.000";

    public static String CONCEPT_1_CODE                             = "concept1";
    public static String CONCEPT_2_CODE                             = "concept2";
    public static String CONCEPT_3_CODE                             = "concept3";

    public static String QUERY_CONCEPT_SCHEME_ID_LIKE_1             = ConceptSchemeCriteriaPropertyRestriction.ID + " " + ComparisonOperator.LIKE + " \"1\"";
    public static String QUERY_CONCEPT_SCHEME_ID_LIKE_1_NAME_LIKE_2 = ConceptSchemeCriteriaPropertyRestriction.ID + " " + ComparisonOperator.LIKE + " \"1\"" + " " + LogicalOperator.AND + " "
                                                                            + ConceptSchemeCriteriaPropertyRestriction.NAME + " " + ComparisonOperator.LIKE + " \"2\"";
    public static String QUERY_CONCEPT_ID_LIKE_1_NAME_LIKE_2        = ConceptCriteriaPropertyRestriction.ID + " " + ComparisonOperator.LIKE + " \"1\"" + " " + LogicalOperator.AND + " "
                                                                            + ConceptCriteriaPropertyRestriction.NAME + " " + ComparisonOperator.LIKE + " \"2\"";

    public static String ORDER_BY_CONCEPT_SCHEME_ID_DESC            = ConceptSchemeCriteriaPropertyRestriction.ID + " " + OrderOperator.DESC;
    public static String ORDER_BY_CONCEPT_ID_DESC                   = ConceptCriteriaPropertyRestriction.ID + " " + OrderOperator.DESC;

}
