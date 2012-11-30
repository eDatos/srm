package org.siemac.metamac.srm.rest.internal;

import org.siemac.metamac.rest.constants.RestConstants;

public class RestInternalConstants {

    public static String API_NAME                               = "srm";
    public static String API_VERSION_1_0                        = "v1.0";

    public static String PARAMETER_AGENCY_ID                    = "agencyID";
    public static String PARAMETER_RESOURCE_ID                  = "resourceID";
    public static String PARAMETER_VERSION                      = "version";
    public static String PARAMETER_CONCEPT_ID                   = "conceptID";
    public static String PARAMETER_CATEGORY_ID                  = "categoryID";
    public static String PARAMETER_ORGANISATION_ID              = "organisationID";

    public static String LINK_SUBPATH_CONCEPT_SCHEMES           = "conceptschemes";
    public static String LINK_SUBPATH_CONCEPTS                  = "concepts";

    public static String LINK_SUBPATH_CATEGORY_SCHEMES          = "categoryschemes";
    public static String LINK_SUBPATH_CATEGORIES                = "categories";
    public static String LINK_SUBPATH_CATEGORISATIONS           = "categorisations";

    public static String LINK_SUBPATH_AGENCY_SCHEMES            = "agencyschemes";
    public static String LINK_SUBPATH_ORGANISATION_UNIT_SCHEMES = "organisationunitschemes";
    public static String LINK_SUBPATH_DATA_PROVIDER_SCHEMES     = "dataproviderschemes";
    public static String LINK_SUBPATH_DATA_CONSUMER_SCHEMES     = "dataconsumerschemes";
    public static String LINK_SUBPATH_ORGANISATION_SCHEMES      = "organisationschemes";

    public static String LINK_SUBPATH_AGENCIES                  = "agencies";
    public static String LINK_SUBPATH_ORGANISATION_UNITS        = "organisationunits";
    public static String LINK_SUBPATH_DATA_PROVIDERS            = "dataproviders";
    public static String LINK_SUBPATH_DATA_CONSUMERS            = "dataconsumers";
    public static String LINK_SUBPATH_ORGANISATIONS             = "organisations";

    public static String KIND_CONCEPT_SCHEMES                   = API_NAME + RestConstants.KIND_SEPARATOR + "conceptSchemes";
    public static String KIND_CONCEPT_SCHEME                    = API_NAME + RestConstants.KIND_SEPARATOR + "conceptScheme";
    public static String KIND_CONCEPTS                          = API_NAME + RestConstants.KIND_SEPARATOR + "concepts";
    public static String KIND_CONCEPT                           = API_NAME + RestConstants.KIND_SEPARATOR + "concept";
    public static String KIND_CONCEPT_TYPES                     = API_NAME + RestConstants.KIND_SEPARATOR + "conceptTypes";

    public static String KIND_CATEGORY_SCHEMES                  = API_NAME + RestConstants.KIND_SEPARATOR + "categorySchemes";
    public static String KIND_CATEGORY_SCHEME                   = API_NAME + RestConstants.KIND_SEPARATOR + "categoryScheme";
    public static String KIND_CATEGORIES                        = API_NAME + RestConstants.KIND_SEPARATOR + "categories";
    public static String KIND_CATEGORY                          = API_NAME + RestConstants.KIND_SEPARATOR + "category";
    public static String KIND_CATEGORISATIONS                   = API_NAME + RestConstants.KIND_SEPARATOR + "categorisations";
    public static String KIND_CATEGORISATION                    = API_NAME + RestConstants.KIND_SEPARATOR + "categorisation";

    public static String KIND_ORGANISATION_SCHEMES              = API_NAME + RestConstants.KIND_SEPARATOR + "organisationSchemes";
    public static String KIND_AGENCY_SCHEMES                    = API_NAME + RestConstants.KIND_SEPARATOR + "agencySchemes";
    public static String KIND_ORGANISATION_UNIT_SCHEMES         = API_NAME + RestConstants.KIND_SEPARATOR + "organisationUnitSchemes";
    public static String KIND_DATA_PROVIDER_SCHEMES             = API_NAME + RestConstants.KIND_SEPARATOR + "dataProviderSchemes";
    public static String KIND_DATA_CONSUMER_SCHEMES             = API_NAME + RestConstants.KIND_SEPARATOR + "dataConsumerSchemes";

    public static String KIND_ORGANISATION_SCHEME               = API_NAME + RestConstants.KIND_SEPARATOR + "organisationScheme";
    public static String KIND_AGENCY_SCHEME                     = API_NAME + RestConstants.KIND_SEPARATOR + "agencyScheme";
    public static String KIND_ORGANISATION_UNIT_SCHEME          = API_NAME + RestConstants.KIND_SEPARATOR + "organisationUnitScheme";
    public static String KIND_DATA_PROVIDER_SCHEME              = API_NAME + RestConstants.KIND_SEPARATOR + "dataProviderScheme";
    public static String KIND_DATA_CONSUMER_SCHEME              = API_NAME + RestConstants.KIND_SEPARATOR + "dataConsumerScheme";

    public static String KIND_ORGANISATIONS                     = API_NAME + RestConstants.KIND_SEPARATOR + "organisations";
    public static String KIND_AGENCIES                          = API_NAME + RestConstants.KIND_SEPARATOR + "agencies";
    public static String KIND_ORGANISATION_UNITS                = API_NAME + RestConstants.KIND_SEPARATOR + "organisationUnits";
    public static String KIND_DATA_PROVIDERS                    = API_NAME + RestConstants.KIND_SEPARATOR + "dataProviders";
    public static String KIND_DATA_CONSUMERS                    = API_NAME + RestConstants.KIND_SEPARATOR + "dataConsumers";

    public static String KIND_ORGANISATION                      = API_NAME + RestConstants.KIND_SEPARATOR + "organisation";
    public static String KIND_AGENCY                            = API_NAME + RestConstants.KIND_SEPARATOR + "agency";
    public static String KIND_ORGANISATION_UNIT                 = API_NAME + RestConstants.KIND_SEPARATOR + "organisationUnit";
    public static String KIND_DATA_PROVIDER                     = API_NAME + RestConstants.KIND_SEPARATOR + "dataProvider";
    public static String KIND_DATA_CONSUMER                     = API_NAME + RestConstants.KIND_SEPARATOR + "dataConsumer";

    public static String WILDCARD                               = "~all";
    public static String LATEST                                 = "~latest";
}