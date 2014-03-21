package org.siemac.metamac.srm.web.server.listener;

import javax.servlet.ServletContextEvent;

import org.apache.commons.lang.StringUtils;
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
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacBasicDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacBasicDto;
import org.siemac.metamac.srm.core.constants.SrmConfigurationConstants;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.criteria.CodelistVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.ConceptMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.OrganisationMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacBasicDto;
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

        LOG.info("**************************************************************");
        LOG.info("[metamac-srm-web] Checking application configuration");
        LOG.info("**************************************************************");

        // SECURITY

        checkSecurityProperties();

        // DATASOURCE

        checkRequiredProperty(SrmConfigurationConstants.DB_DRIVER_NAME);
        checkRequiredProperty(SrmConfigurationConstants.DB_URL);
        checkRequiredProperty(SrmConfigurationConstants.DB_USERNAME);
        checkRequiredProperty(SrmConfigurationConstants.DB_PASSWORD);
        checkRequiredProperty(SrmConfigurationConstants.DB_DIALECT);

        // WEB APPLICATIONS

        checkRequiredProperty(ConfigurationConstants.WEB_APPLICATION_SRM_INTERNAL_WEB);
        checkRequiredProperty(ConfigurationConstants.WEB_APPLICATION_STATISTICAL_OPERATIONS_INTERNAL_WEB);

        // API

        checkRequiredProperty(ConfigurationConstants.ENDPOINT_SRM_INTERNAL_API);
        checkRequiredProperty(ConfigurationConstants.ENDPOINT_STATISTICAL_OPERATIONS_INTERNAL_API);
        checkRequiredProperty(ConfigurationConstants.ENDPOINT_NOTICES_INTERNAL_API);

        // OTHER CONFIGURATION PROPERTIES

        // Common properties

        checkEditionLanguagesProperty();
        checkNavBarUrlProperty();
        checkOrganisationProperty();
        checkRequiredOrganisationUrn(ConfigurationConstants.METAMAC_ORGANISATION_URN);

        // SRM properties

        checkRequiredProperty(SrmConfigurationConstants.USER_GUIDE_FILE_NAME);
        checkRequiredProperty(SrmConfigurationConstants.JOB_DELETE_DEPRECATED_ENTITIES_CRON_EXPRESSION);
        checkRequiredProperty(SrmConfigurationConstants.VARIABLE_WORLD);
        checkRequiredProperty(SrmConfigurationConstants.VARIABLE_ELEMENT_WORLD);

        checkOptionalConceptUrn(SrmConfigurationConstants.DSD_PRIMARY_MEASURE_DEFAULT_CONCEPT_ID_URN);
        checkOptionalConceptUrn(SrmConfigurationConstants.DSD_TIME_DIMENSION_OR_ATTRIBUTE_DEFAULT_CONCEPT_ID_URN);
        checkOptionalConceptUrn(SrmConfigurationConstants.DSD_MEASURE_DIMENSION_OR_ATTRIBUTE_DEFAULT_CONCEPT_ID_URN);
        checkOptionalCodelistUrn(SrmConfigurationConstants.DEFAULT_CODELIST_GEOGRAPHICAL_GRANULARITY_URN);

        LOG.info("**************************************************************");
        LOG.info("[metamac-srm-web] Application configuration checked");
        LOG.info("**************************************************************");
    }

    private void checkRequiredOrganisationUrn(String propertyKey) {
        checkRequiredProperty(propertyKey);

        String organisationUrn = configurationService.getProperty(propertyKey);
        try {
            MetamacCriteria criteria = new MetamacCriteria();
            criteria.setPaginator(new MetamacCriteriaPaginator());
            criteria.getPaginator().setFirstResult(0);
            criteria.getPaginator().setMaximumResultSize(SrmWebConstants.NO_LIMIT_IN_PAGINATION);
            criteria.getPaginator().setCountTotalResults(true);
            criteria.setRestriction(new MetamacCriteriaPropertyRestriction(OrganisationMetamacCriteriaPropertyEnum.URN.name(), organisationUrn, OperationType.EQ));

            MetamacCriteriaResult<OrganisationMetamacBasicDto> result = srmCoreServiceFacade.findOrganisationsByCondition(getStartupServiceContext(), criteria);
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

    private void checkOptionalConceptUrn(String propertyKey) {
        checkOptionalProperty(propertyKey);

        String conceptUrn = configurationService.getProperty(propertyKey);
        if (StringUtils.isNotBlank(conceptUrn)) {
            try {
                MetamacCriteria criteria = new MetamacCriteria();
                criteria.setPaginator(new MetamacCriteriaPaginator());
                criteria.getPaginator().setFirstResult(0);
                criteria.getPaginator().setMaximumResultSize(SrmWebConstants.NO_LIMIT_IN_PAGINATION);
                criteria.getPaginator().setCountTotalResults(true);
                criteria.setRestriction(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.URN.name(), conceptUrn, OperationType.EQ));

                MetamacCriteriaResult<ConceptMetamacBasicDto> result = srmCoreServiceFacade.findConceptsByCondition(getStartupServiceContext(), criteria);
                if (result.getResults().isEmpty()) {
                    String errorMessage = "Property [" + propertyKey + "] is not properly filled. The concept URN specified is not correct. Aborting application startup...";
                    abortApplicationStartup(errorMessage);
                } else {
                    LOG.info("Property [" + propertyKey + "] filled");
                }
            } catch (MetamacException e) {
                abortApplicationStartup("Error checking the property [" + propertyKey + "]");
            }
        }
    }

    private void checkOptionalCodelistUrn(String propertyKey) {
        checkOptionalProperty(propertyKey);

        String codelistUrn = configurationService.getProperty(propertyKey);
        if (StringUtils.isNotBlank(codelistUrn)) {
            try {
                MetamacCriteria criteria = new MetamacCriteria();
                criteria.setPaginator(new MetamacCriteriaPaginator());
                criteria.getPaginator().setFirstResult(0);
                criteria.getPaginator().setMaximumResultSize(SrmWebConstants.NO_LIMIT_IN_PAGINATION);
                criteria.getPaginator().setCountTotalResults(true);
                criteria.setRestriction(new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.URN.name(), codelistUrn, OperationType.EQ));

                MetamacCriteriaResult<CodelistMetamacBasicDto> result = srmCoreServiceFacade.findCodelistsByCondition(getStartupServiceContext(), criteria);
                if (result.getResults().isEmpty()) {
                    String errorMessage = "Property [" + propertyKey + "] is not properly filled. The codelist URN specified is not correct. Aborting application startup...";
                    abortApplicationStartup(errorMessage);
                } else {
                    LOG.info("Property [" + propertyKey + "] filled");
                }
            } catch (MetamacException e) {
                abortApplicationStartup("Error checking the property [" + propertyKey + "]");
            }
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
