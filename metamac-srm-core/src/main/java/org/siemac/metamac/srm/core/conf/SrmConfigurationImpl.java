package org.siemac.metamac.srm.core.conf;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.constants.shared.ConfigurationConstants;
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

    private String               maintainerUrnDefault;
    private String               primaryMeasureConceptIdUrnDefault;
    private Boolean              isDatabaseOracle;
    private Boolean              isDatabaseSqlServer;
    private String               languageDefault;

    @Override
    public String retrieveMaintainerUrnDefault() throws MetamacException {
        if (maintainerUrnDefault == null) {
            maintainerUrnDefault = retrieveProperty(SrmConstants.METAMAC_ORGANISATION_URN, Boolean.TRUE);
        }
        return maintainerUrnDefault;
    }

    @Override
    public String retrievePrimaryMeasureConceptIdUrnDefault() throws MetamacException {
        if (primaryMeasureConceptIdUrnDefault == null) {
            primaryMeasureConceptIdUrnDefault = retrieveProperty(SrmConstants.METAMAC_SRM_PRIMARY_MEASURE_DEFAULT_CONCEPT_ID_URN, Boolean.TRUE);
        }
        return primaryMeasureConceptIdUrnDefault;
    }

    @Override
    public Boolean isDatabaseOracle() throws MetamacException {
        if (isDatabaseOracle == null) {
            String database = retrieveProperty(SrmConstants.DB_DRIVER_NAME, Boolean.TRUE);
            isDatabaseOracle = SrmConstants.DB_DRIVER_NAME_ORACLE.equals(database);
        }
        return isDatabaseOracle;
    }

    @Override
    public Boolean isDatabaseSqlServer() throws MetamacException {
        if (isDatabaseSqlServer == null) {
            String database = retrieveProperty(SrmConstants.DB_DRIVER_NAME, Boolean.TRUE);
            isDatabaseSqlServer = SrmConstants.DB_DRIVER_NAME_MSSQL.equals(database);
        }
        return isDatabaseSqlServer;
    }

    @Override
    public String retrieveLanguageDefault() throws MetamacException {
        if (languageDefault == null) {
            List<Object> languages = retrievePropertyList(ConfigurationConstants.PROP_EDITION_LANGUAGES, Boolean.FALSE);
            if (!CollectionUtils.isEmpty(languages)) {
                languageDefault = (String) languages.get(0);
            }
        }
        return languageDefault;
    }

    private String retrieveProperty(String propertyName, Boolean required) throws MetamacException {
        String propertyValue = configurationService.getProperty(propertyName);
        if (StringUtils.isBlank(propertyValue)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CONFIGURATION_PROPERTY_NOT_FOUND).withMessageParameters(propertyName).build();
        }
        return propertyValue;
    }

    private List<Object> retrievePropertyList(String propertyName, Boolean required) throws MetamacException {
        List<Object> propertyValue = configurationService.getConfig().getList(propertyName);
        if (CollectionUtils.isEmpty(propertyValue)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CONFIGURATION_PROPERTY_NOT_FOUND).withMessageParameters(propertyName).build();
        }
        return propertyValue;
    }
}
