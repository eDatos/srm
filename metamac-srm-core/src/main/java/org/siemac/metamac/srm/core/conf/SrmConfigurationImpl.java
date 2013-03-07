package org.siemac.metamac.srm.core.conf;

import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("srmConfiguration")
public class SrmConfigurationImpl implements SrmConfiguration {

    @Autowired
    private ConfigurationService configurationService;

    @Override
    public String retrieveMaintainerUrnDefault() throws MetamacException {
        return retrieveProperty(SrmConstants.METAMAC_ORGANISATION_URN, Boolean.TRUE);
    }

    @Override
    public String retrievePrimaryMeasureConceptIdUrnDefault() throws MetamacException {
        return retrieveProperty(SrmConstants.METAMAC_SRM_PRIMARY_MEASURE_DEFAULT_CONCEPT_ID_URN, Boolean.TRUE);
    }

    @Override
    public Boolean isDatabaseOracle() throws MetamacException {
        String database = retrieveProperty(SrmConstants.DB_DRIVER_NAME, Boolean.TRUE);
        return SrmConstants.DB_DRIVER_NAME_ORACLE.equals(database);
    }

    @Override
    public Boolean isDatabaseSqlServer() throws MetamacException {
        String database = retrieveProperty(SrmConstants.DB_DRIVER_NAME, Boolean.TRUE);
        return SrmConstants.DB_DRIVER_NAME_MSSQL.equals(database);
    }

    private String retrieveProperty(String propertyName, Boolean required) throws MetamacException {
        String propertyValue = configurationService.getProperty(propertyName);
        if (StringUtils.isBlank(propertyValue)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CONFIGURATION_PROPERTY_NOT_FOUND).withMessageParameters(propertyName).build();
        }
        return propertyValue;
    }
}
