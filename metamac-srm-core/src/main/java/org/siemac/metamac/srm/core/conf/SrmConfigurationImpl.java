package org.siemac.metamac.srm.core.conf;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.constants.SrmConfigurationConstants;

import com.arte.statistic.sdmx.srm.core.conf.SdmxSrmConfigurationImpl;

public class SrmConfigurationImpl extends SdmxSrmConfigurationImpl implements SrmConfiguration {

    private String jobDeleteDeprecatedEntitiesCronExpression;

    @Override
    public String retrieveUserGuideFileName() throws MetamacException {
        return retrieveProperty(SrmConfigurationConstants.USER_GUIDE_FILE_NAME, Boolean.TRUE);
    }

    @Override
    public String retrieveDocsPath() throws MetamacException {
        return retrieveProperty(SrmConfigurationConstants.DOCS_PATH, Boolean.TRUE);
    }

    @Override
    public String retrievePrimaryMeasureConceptIdUrnDefault() throws MetamacException {
        return retrieveProperty(SrmConfigurationConstants.DSD_PRIMARY_MEASURE_DEFAULT_CONCEPT_ID_URN, Boolean.FALSE);
    }

    @Override
    public String retrieveCodelistUrnForVariableElementGeographicalGranularity() throws MetamacException {
        return retrieveDefaultCodelistGeographicalGranularityUrn();
    }

    @Override
    public String retrieveVariableWorldUrn() throws MetamacException {
        return retrieveProperty(SrmConfigurationConstants.VARIABLE_WORLD, Boolean.TRUE);
    }

    @Override
    public String retrieveVariableElementWorldUrn() throws MetamacException {
        return retrieveProperty(SrmConfigurationConstants.VARIABLE_ELEMENT_WORLD, Boolean.TRUE);
    }

    @Override
    public String retrieveJobDeleteDeprecatedEntitiesCronExpression() throws MetamacException {
        return retrieveProperty(SrmConfigurationConstants.JOB_DELETE_DEPRECATED_ENTITIES_CRON_EXPRESSION, Boolean.FALSE);
    }

    @Override
    public Boolean isDatabaseOracle() throws MetamacException {
        String database = retrieveProperty(SrmConfigurationConstants.DB_DRIVER_NAME, Boolean.TRUE);
        return SrmConfigurationConstants.DB_DRIVER_NAME_ORACLE.equals(database);
    }

    @Override
    public Boolean isDatabaseSqlServer() throws MetamacException {
        String database = retrieveProperty(SrmConfigurationConstants.DB_DRIVER_NAME, Boolean.TRUE);
        return SrmConfigurationConstants.DB_DRIVER_NAME_MSSQL.equals(database);
    }

    @Override
    public String retrieveDsdPrimaryMeasureDefaultConceptIdUrn() throws MetamacException {
        return retrieveProperty(SrmConfigurationConstants.DSD_PRIMARY_MEASURE_DEFAULT_CONCEPT_ID_URN, Boolean.TRUE);
    }

    @Override
    public String retrieveDsdMeasureDimensionOrAttributeDefaultConceptIdUrn() throws MetamacException {
        return retrieveProperty(SrmConfigurationConstants.DSD_MEASURE_DIMENSION_OR_ATTRIBUTE_DEFAULT_CONCEPT_ID_URN, Boolean.TRUE);
    }

    @Override
    public String retrieveDsdTimeDimensionOrAttributeDefaultConceptIdUrn() throws MetamacException {
        return retrieveProperty(SrmConfigurationConstants.DSD_TIME_DIMENSION_OR_ATTRIBUTE_DEFAULT_CONCEPT_ID_URN, Boolean.TRUE);
    }

}
