package org.siemac.metamac.srm.rest.internal.v1_0.utils;

import org.siemac.metamac.rest.common.v1_0.domain.ComparisonOperator;
import org.siemac.metamac.rest.common.v1_0.domain.LogicalOperator;
import org.siemac.metamac.rest.common.v1_0.domain.OrderOperator;

public class RestTestConstants {

    public static String NOT_EXISTS                  = "notexists";
    public static String AGENCY_1                    = "agency1";
    public static String AGENCY_2                    = "agency2";

    public static String ITEM_SCHEME_1_CODE          = "itemScheme1";
    public static String ITEM_SCHEME_1_VERSION_1     = "01.000";

    public static String ITEM_SCHEME_2_CODE          = "itemScheme2";
    public static String ITEM_SCHEME_2_VERSION_1     = "01.000";
    public static String ITEM_SCHEME_2_VERSION_2     = "02.000";
    public static String ITEM_SCHEME_3_CODE          = "itemScheme3";
    public static String ITEM_SCHEME_3_VERSION_1     = "01.000";

    public static String ITEM_1_CODE                 = "item1";
    public static String ITEM_2_CODE                 = "item2";
    public static String ITEM_3_CODE                 = "item3";

    public static String QUERY_ID_LIKE_1             = "ID " + ComparisonOperator.LIKE + " \"1\"";
    public static String QUERY_ID_LIKE_1_NAME_LIKE_2 = "ID " + ComparisonOperator.LIKE + " \"1\"" + " " + LogicalOperator.AND + " " + "NAME " + ComparisonOperator.LIKE + " \"2\"";

    public static String ORDER_BY_ID_DESC            = "ID " + OrderOperator.DESC;
}
