package org.siemac.metamac.srm.core.conf;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.constants.SrmConfigurationConstants;

import com.arte.statistic.sdmx.srm.core.conf.SdmxSrmConfigurationImpl;

public class SrmConfigurationImpl extends SdmxSrmConfigurationImpl implements SrmConfiguration {

    private String  primaryMeasureConceptIdUrnDefault;
    private String  variableWorldUrn;
    private String  variableElementWorldUrn;
    private Boolean isDatabaseOracle;
    private Boolean isDatabaseSqlServer;
    private String  jobDeleteDeprecatedEntitiesCronExpression;

    @Override
    public String retrievePrimaryMeasureConceptIdUrnDefault() throws MetamacException {
        if (primaryMeasureConceptIdUrnDefault == null) {
            primaryMeasureConceptIdUrnDefault = retrieveProperty(SrmConfigurationConstants.DSD_PRIMARY_MEASURE_DEFAULT_CONCEPT_ID_URN, Boolean.FALSE);
        }
        return primaryMeasureConceptIdUrnDefault;
    }

    @Override
    public String retrieveCodelistUrnForVariableElementGeographicalGranularity() throws MetamacException {
        return retrieveDefaultCodelistGeographicalGranularityUrn();
    }

    @Override
    public String retrieveVariableWorldUrn() throws MetamacException {
        if (variableWorldUrn == null) {
            variableWorldUrn = retrieveProperty(SrmConfigurationConstants.VARIABLE_WORLD, Boolean.TRUE);
        }
        return variableWorldUrn;
    }

    @Override
    public String retrieveVariableElementWorldUrn() throws MetamacException {
        if (variableElementWorldUrn == null) {
            variableElementWorldUrn = retrieveProperty(SrmConfigurationConstants.VARIABLE_ELEMENT_WORLD, Boolean.TRUE);
        }
        return variableElementWorldUrn;
    }

    @Override
    public String retrieveJobDeleteDeprecatedEntitiesCronExpression() throws MetamacException {
        if (jobDeleteDeprecatedEntitiesCronExpression == null) {
            jobDeleteDeprecatedEntitiesCronExpression = retrieveProperty(SrmConfigurationConstants.JOB_DELETE_DEPRECATED_ENTITIES_CRON_EXPRESSION, Boolean.FALSE);
        }
        return jobDeleteDeprecatedEntitiesCronExpression;
    }

    @Override
    public Boolean isDatabaseOracle() throws MetamacException {
        if (isDatabaseOracle == null) {
            String database = retrieveProperty(SrmConfigurationConstants.DB_DRIVER_NAME, Boolean.TRUE);
            isDatabaseOracle = SrmConfigurationConstants.DB_DRIVER_NAME_ORACLE.equals(database);
        }
        return isDatabaseOracle;
    }

    @Override
    public Boolean isDatabaseSqlServer() throws MetamacException {
        if (isDatabaseSqlServer == null) {
            String database = retrieveProperty(SrmConfigurationConstants.DB_DRIVER_NAME, Boolean.TRUE);
            isDatabaseSqlServer = SrmConfigurationConstants.DB_DRIVER_NAME_MSSQL.equals(database);
        }
        return isDatabaseSqlServer;
    }

}
