package org.siemac.metamac.srm.rest.internal;

import org.siemac.metamac.rest.constants.RestConstants;

public class RestInternalConstants {

    public static String API_NAME                      = "srm";
    public static String API_VERSION_1_0               = "v1.0";

    public static String LINK_SUBPATH_CONCEPT_SCHEMES  = "conceptschemes";
    public static String LINK_SUBPATH_CONCEPTS         = "concepts";

    public static String LINK_SUBPATH_CATEGORY_SCHEMES = "categoryschemes";
    public static String LINK_SUBPATH_CATEGORIES       = "categories";

    public static String KIND_CONCEPT_SCHEMES          = API_NAME + RestConstants.KIND_SEPARATOR + "conceptschemes";
    public static String KIND_CONCEPT_SCHEME           = API_NAME + RestConstants.KIND_SEPARATOR + "conceptscheme";
    public static String KIND_CONCEPTS                 = API_NAME + RestConstants.KIND_SEPARATOR + "concepts";
    public static String KIND_CONCEPT                  = API_NAME + RestConstants.KIND_SEPARATOR + "concept";
    public static String KIND_CONCEPT_TYPES            = API_NAME + RestConstants.KIND_SEPARATOR + "conceptTypes";

    public static String KIND_CATEGORY_SCHEMES         = API_NAME + RestConstants.KIND_SEPARATOR + "categoryschemes";
    public static String KIND_CATEGORY_SCHEME          = API_NAME + RestConstants.KIND_SEPARATOR + "categoryscheme";
    public static String KIND_CATEGORIES               = API_NAME + RestConstants.KIND_SEPARATOR + "categories";
    public static String KIND_CATEGORY                 = API_NAME + RestConstants.KIND_SEPARATOR + "category";

    public static String WILDCARD                      = "~all";
    public static String LATEST                        = "~latest";
}