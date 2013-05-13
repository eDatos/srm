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

}
