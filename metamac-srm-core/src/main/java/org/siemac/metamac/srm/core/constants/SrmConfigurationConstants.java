package org.siemac.metamac.srm.core.constants;

import org.siemac.metamac.core.common.constants.shared.ConfigurationConstants;

public class SrmConfigurationConstants extends ConfigurationConstants {

    // PROPERTIES SPECIFIED IN THE DATA DIRECTORY

    // Configuration

    public static final String DSD_PRIMARY_MEASURE_DEFAULT_CONCEPT_ID_URN   = "metamac.srm.dsd.primary_measure.default_concept.urn";
    public static final String DSD_TIME_DIMENSION_DEFAULT_CONCEPT_ID_URN    = "metamac.srm.dsd.time_dimension.default_concept.urn";
    public static final String DSD_MEASURE_DIMENSION_DEFAULT_CONCEPT_ID_URN = "metamac.srm.dsd.measure_dimension.default_concept.urn";

    public static final String USER_GUIDE_FILE_NAME                         = "metamac.srm.user.guide.file.name";

    // DataSource

    public static final String DB_URL                                       = "metamac.srm.db.url";
    public static final String DB_USERNAME                                  = "metamac.srm.db.username";
    public static final String DB_PASSWORD                                  = "metamac.srm.db.password";
    public static final String DB_DIALECT                                   = "metamac.srm.db.dialect";
    public static final String DB_DRIVER_NAME                               = "metamac.srm.db.driverName";
    public static final String DB_DRIVER_NAME_ORACLE                        = "oracle.jdbc.OracleDriver";
    public static final String DB_DRIVER_NAME_MSSQL                         = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
}
