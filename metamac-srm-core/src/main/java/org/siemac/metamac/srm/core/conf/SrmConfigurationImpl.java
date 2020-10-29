package org.siemac.metamac.srm.core.conf;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.constants.SrmConfigurationConstants;

import com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.DatabaseProvider;
import com.arte.statistic.sdmx.srm.core.conf.SdmxSrmConfigurationImpl;

public class SrmConfigurationImpl extends SdmxSrmConfigurationImpl implements SrmConfiguration {

    @Override
    public String retrieveHelpUrl() throws MetamacException {
        return retrieveProperty(SrmConfigurationConstants.HELP_URL);
    }

    @Override
    public String retrieveDocsPath() throws MetamacException {
        return retrieveProperty(SrmConfigurationConstants.DOCS_PATH);
    }

    @Override
    public String retrievePrimaryMeasureConceptIdUrnDefault() throws MetamacException {
        return retrieveProperty(SrmConfigurationConstants.DSD_PRIMARY_MEASURE_DEFAULT_CONCEPT_ID_URN);
    }

    @Override
    public String retrieveCodelistUrnForVariableElementGeographicalGranularity() throws MetamacException {
        return retrieveDefaultCodelistGeographicalGranularityUrn();
    }

    @Override
    public String retrieveVariableWorldUrn() throws MetamacException {
        return retrieveProperty(SrmConfigurationConstants.VARIABLE_WORLD);
    }

    @Override
    public String retrieveVariableElementWorldUrn() throws MetamacException {
        return retrieveProperty(SrmConfigurationConstants.VARIABLE_ELEMENT_WORLD);
    }

    @Override
    public String findJobDeleteDeprecatedEntitiesCronExpression() throws MetamacException {
        return findProperty(SrmConfigurationConstants.JOB_DELETE_DEPRECATED_ENTITIES_CRON_EXPRESSION);
    }

    @Override
    public Boolean isDatabaseOracle() throws MetamacException {
        String database = retrieveProperty(SrmConfigurationConstants.DB_DRIVER_NAME);
        return SrmConfigurationConstants.DB_DRIVER_NAME_ORACLE.equals(database);
    }

    @Override
    public Boolean isDatabaseSqlServer() throws MetamacException {
        String database = retrieveProperty(SrmConfigurationConstants.DB_DRIVER_NAME);
        return SrmConfigurationConstants.DB_DRIVER_NAME_MSSQL.equals(database);
    }

    @Override
    public Boolean isDatabasePostgreSQL() throws MetamacException {
        String database = retrieveProperty(SrmConfigurationConstants.DB_DRIVER_NAME);
        return SrmConfigurationConstants.DB_DRIVER_NAME_POSTGRESQL.equals(database);
    }

    @Override
    public DatabaseProvider getDataBaseProvider() throws MetamacException {
        if (isDatabaseOracle()) {
            return DatabaseProvider.ORACLE;
        } else if (isDatabaseSqlServer()) {
            return DatabaseProvider.MSSQL;
        } else if (isDatabasePostgreSQL()) {
            return DatabaseProvider.POSTGRESQL;
        }
        return null;
    }

    @Override
    public String retrieveDsdPrimaryMeasureDefaultConceptIdUrn() throws MetamacException {
        return retrieveProperty(SrmConfigurationConstants.DSD_PRIMARY_MEASURE_DEFAULT_CONCEPT_ID_URN);
    }

    @Override
    public String retrieveDsdMeasureDimensionOrAttributeDefaultConceptIdUrn() throws MetamacException {
        return retrieveProperty(SrmConfigurationConstants.DSD_MEASURE_DIMENSION_OR_ATTRIBUTE_DEFAULT_CONCEPT_ID_URN);
    }

    @Override
    public String retrieveDsdTimeDimensionOrAttributeDefaultConceptIdUrn() throws MetamacException {
        return retrieveProperty(SrmConfigurationConstants.DSD_TIME_DIMENSION_OR_ATTRIBUTE_DEFAULT_CONCEPT_ID_URN);
    }
}
