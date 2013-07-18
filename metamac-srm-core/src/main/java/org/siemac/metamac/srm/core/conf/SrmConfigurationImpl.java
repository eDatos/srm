package org.siemac.metamac.srm.core.conf;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.constants.shared.ConfigurationConstants;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.constants.SrmConfigurationConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("srmConfiguration")
public class SrmConfigurationImpl implements SrmConfiguration {

    @Autowired
    private ConfigurationService configurationService;

    private String               maintainerUrnDefault;
    private String               primaryMeasureConceptIdUrnDefault;
    private String               variableElementGeographicalGranularityCodelistUrn;
    private Boolean              isDatabaseOracle;
    private Boolean              isDatabaseSqlServer;
    private List<String>         languages;
    private String               languageDefault;
    private String               statisticalOperationsInternalApiUrlBase;
    private String               statisticalOperationsInternalWebApplicationUrlBase;
    private String               jobDeleteDeprecatedEntitiesCronExpression;

    @Override
    public String retrieveMaintainerUrnDefault() throws MetamacException {
        if (maintainerUrnDefault == null) {
            maintainerUrnDefault = retrieveProperty(ConfigurationConstants.METAMAC_ORGANISATION_URN, Boolean.TRUE);
        }
        return maintainerUrnDefault;
    }

    @Override
    public String retrievePrimaryMeasureConceptIdUrnDefault() throws MetamacException {
        if (primaryMeasureConceptIdUrnDefault == null) {
            primaryMeasureConceptIdUrnDefault = retrieveProperty(SrmConfigurationConstants.DSD_PRIMARY_MEASURE_DEFAULT_CONCEPT_ID_URN, Boolean.FALSE);
        }
        return primaryMeasureConceptIdUrnDefault;
    }

    @Override
    public String retrieveCodelistUrnForVariableElementGeographicalGranularity() throws MetamacException {
        if (variableElementGeographicalGranularityCodelistUrn == null) {
            variableElementGeographicalGranularityCodelistUrn = retrieveProperty(SrmConfigurationConstants.VARIABLE_ELEMENT_GEOGRAPHICAL_GRANULARITY_CODELIST_URN, Boolean.TRUE);
        }
        return variableElementGeographicalGranularityCodelistUrn;
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

    @Override
    public List<String> retrieveLanguages() throws MetamacException {
        if (languages == null) {
            List<Object> languagesProperty = retrievePropertyList(ConfigurationConstants.METAMAC_EDITION_LANGUAGES, Boolean.TRUE);
            languages = new ArrayList<String>();
            if (!CollectionUtils.isEmpty(languagesProperty)) {
                for (Object languageProperty : languagesProperty) {
                    languages.add((String) languageProperty);
                }
            }
        }
        return languages;
    }

    @Override
    public String retrieveLanguageDefault() throws MetamacException {
        if (languageDefault == null) {
            List<String> languages = retrieveLanguages();
            if (!CollectionUtils.isEmpty(languages)) {
                languageDefault = languages.get(0);
            }
        }
        return languageDefault;
    }

    @Override
    public String retrieveStatisticalOperationsInternalApiUrlBase() throws MetamacException {
        if (statisticalOperationsInternalApiUrlBase == null) {
            statisticalOperationsInternalApiUrlBase = retrieveProperty(SrmConfigurationConstants.ENDPOINT_STATISTICAL_OPERATIONS_INTERNAL_API, Boolean.TRUE);
        }
        return statisticalOperationsInternalApiUrlBase;
    }

    @Override
    public String retrieveStatisticalOperationsInternalWebApplicationUrlBase() throws MetamacException {
        if (statisticalOperationsInternalWebApplicationUrlBase == null) {
            statisticalOperationsInternalWebApplicationUrlBase = retrieveProperty(SrmConfigurationConstants.WEB_APPLICATION_STATISTICAL_OPERATIONS_INTERNAL_WEB, Boolean.TRUE);
        }
        return statisticalOperationsInternalWebApplicationUrlBase;
    }

    @Override
    public String retrieveJobDeleteDeprecatedEntitiesCronExpression() throws MetamacException {
        if (jobDeleteDeprecatedEntitiesCronExpression == null) {
            jobDeleteDeprecatedEntitiesCronExpression = retrieveProperty(SrmConfigurationConstants.JOB_DELETE_DEPRECATED_ENTITIES_CRON_EXPRESSION, Boolean.FALSE);
        }
        return jobDeleteDeprecatedEntitiesCronExpression;
    }

    private String retrieveProperty(String propertyName, Boolean required) throws MetamacException {
        String propertyValue = configurationService.getProperty(propertyName);
        if (required && StringUtils.isEmpty(propertyValue)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CONFIGURATION_PROPERTY_NOT_FOUND).withMessageParameters(propertyName).build();
        }
        return propertyValue;
    }

    private List<Object> retrievePropertyList(String propertyName, Boolean required) throws MetamacException {
        List<Object> propertyValue = configurationService.getConfig().getList(propertyName);
        if (required && CollectionUtils.isEmpty(propertyValue)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CONFIGURATION_PROPERTY_NOT_FOUND).withMessageParameters(propertyName).build();
        }
        return propertyValue;
    }
}
