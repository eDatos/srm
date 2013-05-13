package org.siemac.metamac.srm.core.constants;

import org.siemac.metamac.core.common.enume.domain.VersionPatternEnum;

public class SrmConstants {

    public static final String             SECURITY_APPLICATION_ID                                   = "GESTOR_RECURSOS_ESTRUCTURALES";

    // Artefacts
    public static final VersionPatternEnum VERSION_PATTERN_METAMAC                                   = VersionPatternEnum.XX_YYY;
    // Codelists
    public static final int                CODELIST_ORDER_VISUALISATION_MAXIMUM_NUMBER               = 20;
    public static final String             CODELIST_ORDER_VISUALISATION_ALPHABETICAL_CODE            = "ALPHABETICAL";
    public static final Integer            CODELIST_ORDER_VISUALISATION_ALPHABETICAL_COLUMN_INDEX    = Integer.valueOf(1);
    public static final int                CODELIST_OPENNESS_VISUALISATION_MAXIMUM_NUMBER            = 20;
    public static final String             CODELIST_OPENNESS_VISUALISATION_ALL_EXPANDED_CODE         = "ALL_EXPANDED";
    public static final Integer            CODELIST_OPENNESS_VISUALISATION_ALL_EXPANDED_COLUMN_INDEX = Integer.valueOf(1);
    public static final Boolean            CODELIST_OPENNESS_VISUALISATION_DEFAULT_VALUE             = Boolean.TRUE;

    // Csv importations
    public static final String             CSV_SEPARATOR                                             = "\t";
    public static final String             CSV_LINE_SEPARATOR                                        = "\n";
    public static final String             CSV_HEADER_INTERNATIONAL_STRING_SEPARATOR                 = "#";
    public static final String             CSV_HEADER_CODE                                           = "code";
    public static final String             CSV_HEADER_PARENT                                         = "parent";
    public static final String             CSV_HEADER_NAME                                           = "name";
    public static final String             CSV_HEADER_DESCRIPTION                                    = "description";
    public static final String             CSV_HEADER_SHORT_NAME                                     = "shortName";
    public static final String             CSV_HEADER_VARIABLE_ELEMENT                               = "variableElement";
    public static final String             CSV_HEADER_LABEL                                          = "label";
    public static final String             CSV_HEADER_LEVEL                                          = "level";
    public static final String             CSV_HEADER_ORDER                                          = "order";

    // Misc
    public static final int                METADATA_SHORT_NAME_MAXIMUM_LENGTH                        = 100;
}
