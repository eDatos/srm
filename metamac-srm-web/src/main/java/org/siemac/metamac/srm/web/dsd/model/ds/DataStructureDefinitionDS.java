package org.siemac.metamac.srm.web.dsd.model.ds;

import org.siemac.metamac.srm.web.client.model.ds.VersionableResourceDS;

public class DataStructureDefinitionDS extends VersionableResourceDS {

    public static final String STATISTICAL_OPERATION         = "dsd-op";

    public static final String DIMENSION_CONCEPT             = "dsd-dim-con";      // for DSD advanced search
    public static final String ATTRIBUTE_CONCEPT             = "dsd-att-con";      // for DSD advanced search

    // VISUALISATION METADATA
    public static final String AUTO_OPEN                     = "dsd-auto-open";
    public static final String SHOW_DECIMALS                 = "dsd-show-decimals";
    public static final String DIMENSIONS_VISUALISATIONS     = "dsd-stub";
    public static final String SHOW_DECIMALS_PRECISION       = "dsd-show-dec-prec";
    public static final String DIMENSION_CODES_VISUALISATION = "dsd-dim-codes";

    public static final String DTO                           = "dsd-dto";
}
