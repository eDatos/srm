package org.siemac.metamac.srm.core.constants;

import org.siemac.metamac.core.common.constants.shared.ConfigurationConstants;

public class SrmConfigurationConstants extends ConfigurationConstants {

    // PROPERTIES SPECIFIED IN THE DATA DIRECTORY

    // Configuration

    public static final String JOB_DELETE_DEPRECATED_ENTITIES_CRON_EXPRESSION = "metamac.srm.jobs.delete_deprecated_entities.cron_expression";

    public static final String HELP_URL                                       = "metamac.srm.help.url";
    public static final String DOCS_PATH                                      = "metamac.data.docs.srm.path";

    // DataSource
    public static final String DB_URL                                         = "metamac.srm.db.url";
    public static final String DB_USERNAME                                    = "metamac.srm.db.username";
    public static final String DB_PASSWORD                                    = "metamac.srm.db.password";
    public static final String DB_DIALECT                                     = "metamac.srm.db.dialect";
    public static final String DB_DRIVER_NAME                                 = "metamac.srm.db.driver_name";
    public static final String DB_DRIVER_NAME_ORACLE                          = "oracle.jdbc.OracleDriver";
    public static final String DB_DRIVER_NAME_MSSQL                           = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    // Misc
    public static final String VARIABLE_WORLD                                 = "metamac.srm.variables.variable_world.urn";
    public static final String VARIABLE_ELEMENT_WORLD                         = "metamac.srm.variables.variable_element_world.urn";
}
