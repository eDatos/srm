package org.siemac.metamac.srm.web.server.listener;

import javax.servlet.ServletContextEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.constants.shared.ConfigurationConstants;
import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction.OperationType;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.srm.core.constants.SrmConfigurationConstants;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.criteria.OrganisationMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.web.client.constants.SrmWebConstants;
import org.siemac.metamac.sso.client.MetamacPrincipal;
import org.siemac.metamac.sso.client.MetamacPrincipalAccess;
import org.siemac.metamac.sso.client.SsoClientConstants;
import org.siemac.metamac.web.common.server.listener.ApplicationStartupListener;

public class SrmApplicationStartupListener extends ApplicationStartupListener {

    private static final Log     LOG = LogFactory.getLog(SrmApplicationStartupListener.class);

    private SrmCoreServiceFacade srmCoreServiceFacade;
    private ServiceContext       serviceContext;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        srmCoreServiceFacade = ApplicationContextProvider.getApplicationContext().getBean(SrmCoreServiceFacade.class);
        super.contextInitialized(sce);
    }

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

        // Common properties

        checkRequiredProperty(ConfigurationConstants.METAMAC_EDITION_LANGUAGES);
        checkRequiredProperty(ConfigurationConstants.METAMAC_NAVBAR_URL);
        checkRequiredProperty(ConfigurationConstants.METAMAC_ORGANISATION);
        checkOrganisationUrnProperty();

        // SRM properties

        checkRequiredProperty(SrmConfigurationConstants.USER_GUIDE_FILE_NAME);

        checkOptionalProperty(SrmConfigurationConstants.DSD_PRIMARY_MEASURE_DEFAULT_CONCEPT_ID_URN);
        checkOptionalProperty(SrmConfigurationConstants.DSD_TIME_DIMENSION_DEFAULT_CONCEPT_ID_URN);
        checkOptionalProperty(SrmConfigurationConstants.DSD_MEASURE_DIMENSION_DEFAULT_CONCEPT_ID_URN);
    }

    private void checkOrganisationUrnProperty() {
        String propertyKey = ConfigurationConstants.METAMAC_ORGANISATION_URN;
        checkRequiredProperty(propertyKey);

        String organisationUrn = configurationService.getProperty(propertyKey);
        try {
            MetamacCriteria criteria = new MetamacCriteria();
            criteria.setPaginator(new MetamacCriteriaPaginator());
            criteria.getPaginator().setFirstResult(0);
            criteria.getPaginator().setMaximumResultSize(SrmWebConstants.NO_LIMIT_IN_PAGINATION);
            criteria.getPaginator().setCountTotalResults(true);
            criteria.setRestriction(new MetamacCriteriaPropertyRestriction(OrganisationMetamacCriteriaPropertyEnum.URN.name(), organisationUrn, OperationType.EQ));

            MetamacCriteriaResult<OrganisationMetamacDto> result = srmCoreServiceFacade.findOrganisationsByCondition(getStartupServiceContext(), criteria);
            if (result.getResults().isEmpty()) {
                String errorMessage = "Property [" + propertyKey + "] is not properly filled. The organisation URN specified is not correct. Aborting application startup...";
                abortApplicationStartup(errorMessage);
            } else {
                LOG.info("Property [" + propertyKey + "] filled");
            }
        } catch (MetamacException e) {
            abortApplicationStartup("Error checking the property [" + propertyKey + "]");
        }
    }

    private ServiceContext getStartupServiceContext() {
        if (serviceContext == null) {
            String userId = "SRM_STARTUP_USER";
            String sessionId = "SRM_STARTUP_SESSION_ID";

            MetamacPrincipal metamacPrincipal = new MetamacPrincipal();
            metamacPrincipal.setUserId(userId);
            metamacPrincipal.getAccesses().add(new MetamacPrincipalAccess(SrmRoleEnum.ADMINISTRADOR.getName(), SrmConstants.SECURITY_APPLICATION_ID, null));

            serviceContext = new ServiceContext(metamacPrincipal.getUserId(), sessionId, SrmConstants.SECURITY_APPLICATION_ID);
            serviceContext.setProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE, metamacPrincipal);
        }
        return serviceContext;
    }
}
