package org.siemac.metamac.srm.rest.internal;

import org.siemac.metamac.rest.constants.RestConstants;

public class RestInternalConstants {

    public static String API_NAME                     = "srm";
    public static String API_VERSION_1_0              = "v1.0";

    public static String LINK_SUBPATH_CONCEPT_SCHEMES = "conceptschemes";
    public static String LINK_SUBPATH_CONCEPTS        = "concepts";

    public static String KIND_CONCEPT_SCHEMES         = API_NAME + RestConstants.KIND_SEPARATOR + "conceptschemes";
    public static String KIND_CONCEPT_SCHEME          = API_NAME + RestConstants.KIND_SEPARATOR + "conceptscheme";
    public static String KIND_CONCEPTS                = API_NAME + RestConstants.KIND_SEPARATOR + "concepts";
    public static String KIND_CONCEPT                 = API_NAME + RestConstants.KIND_SEPARATOR + "concept";
    public static String KIND_CONCEPT_TYPES           = API_NAME + RestConstants.KIND_SEPARATOR + "conceptTypes";

    public static String WILDCARD                     = "~all";
    public static String LATEST                       = "~latest";
}