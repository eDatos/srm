package org.siemac.metamac.srm.rest.internal;

import org.siemac.metamac.rest.constants.RestConstants;

public class RestInternalConstants {

    public static String API_NAME                               = "srm";
    public static String API_VERSION_1_0                        = "v1.0";

    public static String LINK_SUBPATH_CONCEPT_SCHEMES           = "conceptschemes";
    public static String LINK_SUBPATH_CONCEPTS                  = "concepts";

    public static String LINK_SUBPATH_CATEGORY_SCHEMES          = "categoryschemes";
    public static String LINK_SUBPATH_CATEGORIES                = "categories";

    public static String LINK_SUBPATH_AGENCY_SCHEMES            = "agencyschemes";
    public static String LINK_SUBPATH_ORGANISATION_UNIT_SCHEMES = "organisationunitschemes";
    public static String LINK_SUBPATH_DATA_PROVIDER_SCHEMES     = "dataproviderschemes";
    public static String LINK_SUBPATH_DATA_CONSUMER_SCHEMES     = "dataconsumerschemes";
    public static String LINK_SUBPATH_ORGANISATION_SCHEMES      = "organisationschemes";

    public static String LINK_SUBPATH_AGENCIES                  = "agencies";

    public static String KIND_CONCEPT_SCHEMES                   = API_NAME + RestConstants.KIND_SEPARATOR + "conceptSchemes";
    public static String KIND_CONCEPT_SCHEME                    = API_NAME + RestConstants.KIND_SEPARATOR + "conceptScheme";
    public static String KIND_CONCEPTS                          = API_NAME + RestConstants.KIND_SEPARATOR + "concepts";
    public static String KIND_CONCEPT                           = API_NAME + RestConstants.KIND_SEPARATOR + "concept";
    public static String KIND_CONCEPT_TYPES                     = API_NAME + RestConstants.KIND_SEPARATOR + "conceptTypes";

    public static String KIND_CATEGORY_SCHEMES                  = API_NAME + RestConstants.KIND_SEPARATOR + "categorySchemes";
    public static String KIND_CATEGORY_SCHEME                   = API_NAME + RestConstants.KIND_SEPARATOR + "categoryScheme";
    public static String KIND_CATEGORIES                        = API_NAME + RestConstants.KIND_SEPARATOR + "categories";
    public static String KIND_CATEGORY                          = API_NAME + RestConstants.KIND_SEPARATOR + "category";

    public static String KIND_ORGANISATION_SCHEMES              = API_NAME + RestConstants.KIND_SEPARATOR + "organisationSchemes";
    public static String KIND_AGENCY_SCHEMES                    = API_NAME + RestConstants.KIND_SEPARATOR + "agencySchemes";
    public static String KIND_ORGANISATION_UNIT_SCHEMES         = API_NAME + RestConstants.KIND_SEPARATOR + "organisationUnitSchemes";
    public static String KIND_DATA_PROVIDER_SCHEMES             = API_NAME + RestConstants.KIND_SEPARATOR + "dataProviderSchemes";
    public static String KIND_DATA_CONSUMER_SCHEMES             = API_NAME + RestConstants.KIND_SEPARATOR + "dataConsumerSchemes";

    public static String KIND_AGENCY_SCHEME                     = API_NAME + RestConstants.KIND_SEPARATOR + "agencyScheme";
    public static String KIND_ORGANISATION_UNIT_SCHEME          = API_NAME + RestConstants.KIND_SEPARATOR + "organisationUnitScheme";
    public static String KIND_DATA_PROVIDER_SCHEME              = API_NAME + RestConstants.KIND_SEPARATOR + "dataProviderScheme";
    public static String KIND_DATA_CONSUMER_SCHEME              = API_NAME + RestConstants.KIND_SEPARATOR + "dataConsumerScheme";

    public static String WILDCARD                               = "~all";
    public static String LATEST                                 = "~latest";
}