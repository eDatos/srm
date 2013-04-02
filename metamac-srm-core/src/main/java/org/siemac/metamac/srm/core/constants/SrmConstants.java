package org.siemac.metamac.srm.core.constants;

import org.siemac.metamac.core.common.enume.domain.VersionPatternEnum;

public class SrmConstants {

    public static final String             SECURITY_APPLICATION_ID                                   = "GESTOR_RECURSOS_ESTRUCTURALES";

    // Configuration
    public static final String             METAMAC_ORGANISATION_URN                                  = "metamac.organisation.urn";
    public static final String             METAMAC_SRM_PRIMARY_MEASURE_DEFAULT_CONCEPT_ID_URN        = "metamac.srm.dsd.primary_measure.default_concept.urn";
    // Datasource
    public static final String             DB_URL                                                    = "metamac.srm.db.url";
    public static final String             DB_USERNAME                                               = "metamac.srm.db.username";
    public static final String             DB_PASSWORD                                               = "metamac.srm.db.password";
    public static final String             DB_DIALECT                                                = "metamac.srm.db.dialect";
    public static final String             DB_DRIVER_NAME                                            = "metamac.srm.db.driverName";
    public static final String             DB_DRIVER_NAME_ORACLE                                     = "oracle.jdbc.OracleDriver";
    public static final String             DB_DRIVER_NAME_MSSQL                                      = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

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
    public static final String             CSV_HEADER_VARIABLE_ELEMENT_CODE                          = "code";
    public static final String             CSV_HEADER_VARIABLE_ELEMENT_SHORT_NAME                    = "shortName";
}
