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
import org.siemac.metamac.web.common.server.listener.InternalApplicationStartupListener;

public class SrmApplicationStartupListener extends InternalApplicationStartupListener {

    private static final Log     LOG = LogFactory.getLog(SrmApplicationStartupListener.class);

    private SrmCoreServiceFacade srmCoreServiceFacade;
    private ServiceContext       serviceContext;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        srmCoreServiceFacade = ApplicationContextProvider.getApplicationContext().getBean(SrmCoreServiceFacade.class);
        super.contextInitialized(sce);
    }

    // -----------------------------------------------------------------------------------
    // OVERRIDE METHODS
    // -----------------------------------------------------------------------------------

    @Override
    public void checkDatasourceProperties() {
        checkRequiredProperty(SrmConfigurationConstants.DB_DRIVER_NAME);
        checkRequiredProperty(SrmConfigurationConstants.DB_URL);
        checkRequiredProperty(SrmConfigurationConstants.DB_USERNAME);
        checkRequiredProperty(SrmConfigurationConstants.DB_PASSWORD);
        checkRequiredProperty(SrmConfigurationConstants.DB_DIALECT);
    }

    @Override
    public void checkWebApplicationsProperties() {
        checkRequiredProperty(ConfigurationConstants.WEB_APPLICATION_SRM_INTERNAL_WEB);
        checkRequiredProperty(ConfigurationConstants.WEB_APPLICATION_STATISTICAL_OPERATIONS_INTERNAL_WEB);
        checkRequiredProperty(ConfigurationConstants.WEB_APPLICATION_STATISTICAL_RESOURCES_INTERNAL_WEB);
    }

    @Override
    public void checkApiProperties() {
        checkRequiredProperty(ConfigurationConstants.ENDPOINT_SRM_INTERNAL_API);
        checkRequiredProperty(ConfigurationConstants.ENDPOINT_STATISTICAL_OPERATIONS_INTERNAL_API);
        checkRequiredProperty(ConfigurationConstants.ENDPOINT_NOTICES_INTERNAL_API);
        checkRequiredProperty(ConfigurationConstants.ENDPOINT_STATISTICAL_RESOURCES_INTERNAL_API);
    }

    @Override
    public void checkOtherModuleProperties() {
        // Common
        checkRequiredOrganisationUrn();

        // SRM
        checkRequiredProperty(SrmConfigurationConstants.HELP_URL);
        checkRequiredProperty(SrmConfigurationConstants.JOB_DELETE_DEPRECATED_ENTITIES_CRON_EXPRESSION);
        checkRequiredProperty(SrmConfigurationConstants.VARIABLE_WORLD);
        checkRequiredProperty(SrmConfigurationConstants.VARIABLE_ELEMENT_WORLD);

        checkOptionalConceptUrn(SrmConfigurationConstants.DSD_PRIMARY_MEASURE_DEFAULT_CONCEPT_ID_URN);
        checkOptionalConceptUrn(SrmConfigurationConstants.DSD_TIME_DIMENSION_OR_ATTRIBUTE_DEFAULT_CONCEPT_ID_URN);

        checkOptionalConceptUrn(SrmConfigurationConstants.DSD_MEASURE_DIMENSION_OR_ATTRIBUTE_DEFAULT_CONCEPT_ID_URN);
        checkOptionalCodelistUrn(SrmConfigurationConstants.DEFAULT_CODELIST_GEOGRAPHICAL_GRANULARITY_URN);
    }

    @Override
    public String projectName() {
        return "structural-resources-internal";
    }

    // -----------------------------------------------------------------------------------
    // AUXILIAR METHODS
    // -----------------------------------------------------------------------------------

    private void checkRequiredOrganisationUrn() {
        try {
            String organisationUrn = configurationService.retrieveOrganisationUrn();
            MetamacCriteria criteria = new MetamacCriteria();
            criteria.setPaginator(new MetamacCriteriaPaginator());
            criteria.getPaginator().setFirstResult(0);
            criteria.getPaginator().setMaximumResultSize(SrmWebConstants.NO_LIMIT_IN_PAGINATION);
            criteria.getPaginator().setCountTotalResults(true);
            criteria.setRestriction(new MetamacCriteriaPropertyRestriction(OrganisationMetamacCriteriaPropertyEnum.URN.name(), organisationUrn, OperationType.EQ));

            MetamacCriteriaResult<OrganisationMetamacBasicDto> result = srmCoreServiceFacade.findOrganisationsByCondition(getStartupServiceContext(), criteria);
            if (result.getResults().isEmpty()) {
                String errorMessage = "Property [" + SrmConfigurationConstants.METAMAC_ORGANISATION_URN
                        + "] is not properly filled. The organisation URN specified is not correct. Aborting application startup...";
                abortApplicationStartup(errorMessage);
            } else {
                LOG.info("Property [" + SrmConfigurationConstants.METAMAC_ORGANISATION_URN + "] filled");
            }
        } catch (MetamacException e) {
            abortApplicationStartup("Error checking the property [" + SrmConfigurationConstants.METAMAC_ORGANISATION_URN + "]");
        }
    }

    private void checkOptionalConceptUrn(String propertyKey) {
        try {
            // We use find because it's optional
            String conceptUrn = configurationService.findProperty(propertyKey);

            if (StringUtils.isNotBlank(conceptUrn)) {

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
            }
        } catch (MetamacException e) {
            abortApplicationStartup("Error checking the property [" + propertyKey + "]");
        }
    }

    private void checkOptionalCodelistUrn(String propertyKey) {
        try {
            // We use find because it's optional
            String codelistUrn = configurationService.findProperty(propertyKey);

            if (StringUtils.isNotBlank(codelistUrn)) {
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
            metamacPrincipal.getAccesses().add(new MetamacPrincipalAccess(SrmRoleEnum.ADMINISTRADOR.getName(), SrmConstants.APPLICATION_ID, null));

            serviceContext = new ServiceContext(metamacPrincipal.getUserId(), sessionId, SrmConstants.APPLICATION_ID);
            serviceContext.setProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE, metamacPrincipal);
        }
        return serviceContext;
    }
}
