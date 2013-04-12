package org.siemac.metamac.srm.web.server.listener;

import org.siemac.metamac.core.common.constants.shared.ConfigurationConstants;
import org.siemac.metamac.srm.core.constants.SrmConfigurationConstants;
import org.siemac.metamac.web.common.server.listener.ApplicationStartupListener;

public class SrmApplicationStartupListener extends ApplicationStartupListener {

    @Override
    public void checkConfiguration() {

        // SECURITY

        checkRequiredProperty(ConfigurationConstants.SECURITY_CAS_SERVER_URL_PREFIX);
        checkRequiredProperty(ConfigurationConstants.SECURITY_CAS_SERVICE_LOGIN_URL);
        checkRequiredProperty(ConfigurationConstants.SECURITY_CAS_SERVICE_LOGOUT_URL);
        checkRequiredProperty(ConfigurationConstants.SECURITY_TOLERANCE);

        // DATASOURCE

        checkRequiredProperty(SrmConfigurationConstants.DB_DRIVER_NAME);
        checkRequiredProperty(SrmConfigurationConstants.DB_URL);
        checkRequiredProperty(SrmConfigurationConstants.DB_USERNAME);
        checkRequiredProperty(SrmConfigurationConstants.DB_PASSWORD);
        checkRequiredProperty(SrmConfigurationConstants.DB_DIALECT);

        // API

        checkRequiredProperty(ConfigurationConstants.ENDPOINT_SRM_INTERNAL_API);
        checkRequiredProperty(ConfigurationConstants.ENDPOINT_STATISTICAL_OPERATIONS_INTERNAL_API);

        // OTHER CONFIGURATION PROPERTIES

        checkRequiredProperty(ConfigurationConstants.METAMAC_EDITION_LANGUAGES);
        checkRequiredProperty(ConfigurationConstants.METAMAC_NAVBAR_URL);
        checkRequiredProperty(ConfigurationConstants.METAMAC_ORGANISATION);
        checkRequiredProperty(ConfigurationConstants.METAMAC_ORGANISATION_URN);

        checkRequiredProperty(SrmConfigurationConstants.USER_GUIDE_FILE_NAME);

        checkOptionalProperty(SrmConfigurationConstants.DSD_PRIMARY_MEASURE_DEFAULT_CONCEPT_ID_URN);
        checkOptionalProperty(SrmConfigurationConstants.DSD_TIME_DIMENSION_DEFAULT_CONCEPT_ID_URN);
        checkOptionalProperty(SrmConfigurationConstants.DSD_MEASURE_DIMENSION_DEFAULT_CONCEPT_ID_URN);
    }
}
