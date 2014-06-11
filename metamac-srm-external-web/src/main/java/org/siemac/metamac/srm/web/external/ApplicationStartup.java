package org.siemac.metamac.srm.web.external;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.listener.ApplicationStartupListener;
import org.siemac.metamac.srm.core.constants.SrmConfigurationConstants;

public class ApplicationStartup extends ApplicationStartupListener {

    @Override
    public String projectName() {
        return "structural-resources";
    }

    @Override
    public void checkApplicationProperties() throws MetamacException {
        // Datasource
        checkRequiredProperty(SrmConfigurationConstants.DB_DRIVER_NAME);
        checkRequiredProperty(SrmConfigurationConstants.DB_URL);
        checkRequiredProperty(SrmConfigurationConstants.DB_USERNAME);
        checkRequiredProperty(SrmConfigurationConstants.DB_PASSWORD);
        checkRequiredProperty(SrmConfigurationConstants.DB_DIALECT);

        // Api
        checkRequiredProperty(SrmConfigurationConstants.ENDPOINT_SRM_EXTERNAL_API);
        checkRequiredProperty(SrmConfigurationConstants.ENDPOINT_STATISTICAL_OPERATIONS_EXTERNAL_API);
        checkRequiredProperty(SrmConfigurationConstants.METAMAC_ORGANISATION_URN);
    }

}
