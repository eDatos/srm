package org.siemac.metamac.srm.core.constants;

import org.siemac.metamac.core.common.enume.domain.VersionPatternEnum;

public class SrmConstants {

    public static final String             SECURITY_APPLICATION_ID                                = "GESTOR_RECURSOS_ESTRUCTURALES";

    // Configuration
    public static final String             METAMAC_ORGANISATION_URN                               = "metamac.organisation.urn";
    public static final String             METAMAC_SRM_PRIMARY_MEASURE_DEFAULT_CONCEPT_ID_URN     = "metamac.srm.dsd.primary_measure.default_concept.urn";
    public static final String             DB_DRIVER_NAME                                         = "metamac.srm.db.driverName";
    public static final String             DB_DRIVER_NAME_ORACLE                                  = "oracle.jdbc.OracleDriver";
    public static final String             DB_DRIVER_NAME_MSSQL                                   = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    // Artefacts
    public static final VersionPatternEnum VERSION_PATTERN_METAMAC                                = VersionPatternEnum.XX_YYY;
    // Codelists
    public static final int                CODELIST_ORDER_VISUALISATION_MAXIMUM_NUMBER            = 20;
    public static final String             CODELIST_ORDER_VISUALISATION_ALPHABETICAL_CODE         = "ALPHABETICAL";
    public static final Integer            CODELIST_ORDER_VISUALISATION_ALPHABETICAL_COLUMN_INDEX = Integer.valueOf(1);
}
