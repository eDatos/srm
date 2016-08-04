package org.siemac.metamac.srm.web.external;

import javax.servlet.ServletContextEvent;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.listener.ApplicationStartupListener;
import org.siemac.metamac.core.common.util.WebUtils;
import org.siemac.metamac.srm.core.constants.SrmConfigurationConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationStartup extends ApplicationStartupListener {

	private static final Logger log = LoggerFactory.getLogger(ApplicationStartup.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		super.contextInitialized(sce);
		try {
			WebUtils.setOrganisation(configurationService.retrieveOrganisation());
			WebUtils.setApiBaseURL(configurationService.retrieveSrmExternalApiUrlBase());

			WebUtils.setApiStyleHeaderUrl(configurationService.retrieveApiStyleHeaderUrl());
			WebUtils.setApiStyleCssUrl(configurationService.retrieveApiStyleCssUrl());
			WebUtils.setApiStyleFooterUrl(configurationService.retrieveApiStyleFooterUrl());
		} catch (MetamacException e) {
			log.error("Error retrieving application configuration", e);
		}
	}

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
		checkRequiredProperty(SrmConfigurationConstants.ENDPOINT_SRM_EXTERNAL_SOAP);
		checkRequiredProperty(SrmConfigurationConstants.ENDPOINT_STATISTICAL_OPERATIONS_EXTERNAL_API);
		checkRequiredProperty(SrmConfigurationConstants.METAMAC_ORGANISATION_URN);
	}
}
