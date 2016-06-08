package org.siemac.metamac.srm.web.external.urlrewrite;

import javax.servlet.ServletException;

import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.core.common.util.urlrewrite.AbstractRewriteMatch;
import org.siemac.metamac.srm.rest.external.constants.SrmRestExternalConstants;

class SrmRewriteMatch extends AbstractRewriteMatch {

    private static final String  SRM_API_PREFIX       = "structural-resources";
    private static final String  SOAP_API_PREFIX      = "soap";

    private ConfigurationService configurationService = null;

    @Override
    protected String[] getAcceptedApiPrefixes() {
        return new String[]{SRM_API_PREFIX, SOAP_API_PREFIX};
    }

    @Override
    protected String getLatestApiVersion() {
        return SrmRestExternalConstants.API_VERSION_1_0;
    }

    @Override
    protected String getApiBaseUrl() throws ServletException {
        try {
            return SOAP_API_PREFIX.equals(getCurrentApiPrefix()) ? getConfigurationService().retrieveSrmExternalSoapUrlBase() : getConfigurationService().retrieveSrmExternalApiUrlBase();
        } catch (MetamacException e) {
            throw new ServletException("Error retrieving configuration property of the external API URL base", e);
        }
    }

    private ConfigurationService getConfigurationService() {
        if (configurationService == null) {
            configurationService = ApplicationContextProvider.getApplicationContext().getBean(ConfigurationService.class);
        }
        return configurationService;
    }
}
