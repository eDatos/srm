package org.siemac.metamac.srm.core.conf;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;

public interface SrmConfiguration {

    /**
     * Retrieves organisation that is maintainer default to create artefacts
     */
    public String retrieveMaintainerUrnDefault() throws MetamacException;

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
     * Checks if Database is Oracle
     */
    public Boolean isDatabaseOracle() throws MetamacException;

    /**
     * Checks if Database is SQL Server
     */
    public Boolean isDatabaseSqlServer() throws MetamacException;

    /**
     * Retrieves all languages managed by service
     */
    public List<String> retrieveLanguages() throws MetamacException;

    /**
     * Retrieves language as default
     */
    public String retrieveLanguageDefault() throws MetamacException;

    /**
     * Retrieves url base of Statistical Operation Internal API
     */
    public String retrieveStatisticalOperationsInternalApiUrlBase() throws MetamacException;

    /**
     * Retrieves url base of Statistical Operation Internal Web
     */
    public String retrieveStatisticalOperationsInternalWebApplicationUrlBase() throws MetamacException;

    public String retrieveJobDeleteDeprecatedEntitiesCronExpression() throws MetamacException;

}
