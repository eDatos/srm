package org.siemac.metamac.srm.core.conf;

import org.siemac.metamac.core.common.exception.MetamacException;

import com.arte.statistic.sdmx.srm.core.conf.SdmxSrmConfiguration;

public interface SrmConfiguration extends SdmxSrmConfiguration {

    public String retrieveUserGuideFileName() throws MetamacException;

    public String retrieveDocsPath() throws MetamacException;

    /**
     * Retrieves the concept ID for concept identification default of primary measure to create artefacts
     */
    public String retrievePrimaryMeasureConceptIdUrnDefault() throws MetamacException;

    /**
     * Retrieves the codelist URN for geographical granularity of variable elements
     */
    public String retrieveCodelistUrnForVariableElementGeographicalGranularity() throws MetamacException;

    /**
     * Retrieves the URN for special variable WORLD
     */
    public String retrieveVariableWorldUrn() throws MetamacException;

    /**
     * Retrieves the URN for special variable element WORLD
     */
    public String retrieveVariableElementWorldUrn() throws MetamacException;

    /**
     * Retrieves the Cron expression to perform a cleanup of deleted entities
     */
    public String retrieveJobDeleteDeprecatedEntitiesCronExpression() throws MetamacException;

    /**
     * Checks if Database is Oracle
     */
    public Boolean isDatabaseOracle() throws MetamacException;

    /**
     * Checks if Database is SQL Server
     */
    public Boolean isDatabaseSqlServer() throws MetamacException;

}
