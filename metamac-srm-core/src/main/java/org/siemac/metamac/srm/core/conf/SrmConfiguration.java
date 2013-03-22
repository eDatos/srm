package org.siemac.metamac.srm.core.conf;

import org.siemac.metamac.core.common.exception.MetamacException;

public interface SrmConfiguration {

    /**
     * Retrieve organisation that is maintainer default to create artefacts
     */
    public String retrieveMaintainerUrnDefault() throws MetamacException;

    /**
     * Retrieve the concept ID for concept identification default of primary measure to create artefacts
     */
    public String retrievePrimaryMeasureConceptIdUrnDefault() throws MetamacException;

    /**
     * Check if Database is Oracle
     */
    public Boolean isDatabaseOracle() throws MetamacException;

    /**
     * Check if Database is SQL Server
     */
    public Boolean isDatabaseSqlServer() throws MetamacException;

    /**
     * Language as default
     */
    public String retrieveLanguageDefault() throws MetamacException;

}
