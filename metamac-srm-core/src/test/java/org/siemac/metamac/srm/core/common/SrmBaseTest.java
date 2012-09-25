package org.siemac.metamac.srm.core.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum;
import org.siemac.metamac.sso.client.MetamacPrincipal;
import org.siemac.metamac.sso.client.MetamacPrincipalAccess;
import org.siemac.metamac.sso.client.SsoClientConstants;

import com.arte.statistic.sdmx.srm.core.common.SdmxSrmBaseTest;

public abstract class SrmBaseTest extends SdmxSrmBaseTest {

    // Concepts schemes
    protected static final String CONCEPT_SCHEME_1_V1               = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=ISTAC:CONCEPTSCHEME01(01.000)";
    protected static final String CONCEPT_SCHEME_1_V2               = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=ISTAC:CONCEPTSCHEME01(02.000)";
    protected static final String CONCEPT_SCHEME_2_V1               = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=ISTAC:CONCEPTSCHEME02(01.000)";
    protected static final String CONCEPT_SCHEME_3_V1               = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=ISTAC:CONCEPTSCHEME03(01.000)";
    protected static final String CONCEPT_SCHEME_4_V1               = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=ISTAC:CONCEPTSCHEME04(01.000)";
    protected static final String CONCEPT_SCHEME_5_V1               = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=ISTAC:CONCEPTSCHEME05(01.000)";
    protected static final String CONCEPT_SCHEME_6_V1               = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=ISTAC:CONCEPTSCHEME06(01.000)";
    protected static final String CONCEPT_SCHEME_7_V1               = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=ISTAC:CONCEPTSCHEME07(01.000)";
    protected static final String CONCEPT_SCHEME_7_V2               = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=ISTAC:CONCEPTSCHEME07(02.000)";
    protected static final String CONCEPT_SCHEME_8_V1               = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=ISTAC:CONCEPTSCHEME08(01.000)";
    protected static final String CONCEPT_SCHEME_9_V1               = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=ISTAC:CONCEPTSCHEME09(01.000)";
    protected static final String CONCEPT_SCHEME_10_V1              = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=ISTAC:CONCEPTSCHEME10(01.000)";
    protected static final String CONCEPT_SCHEME_10_V2              = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=ISTAC:CONCEPTSCHEME10(02.000)";
    protected static final String CONCEPT_SCHEME_10_V3              = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=ISTAC:CONCEPTSCHEME10(03.000)";
    protected static final String CONCEPT_SCHEME_11_V1              = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=ISTAC:CONCEPTSCHEME11(01.000)";
    protected static final String CONCEPT_SCHEME_12_V1              = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=ISTAC:CONCEPTSCHEME12(01.000)";

    // Concepts
    protected static final String CONCEPT_SCHEME_1_V1_CONCEPT_1     = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ISTAC:CONCEPTSCHEME01(01.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_1_V2_CONCEPT_1     = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ISTAC:CONCEPTSCHEME01(02.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_1_V2_CONCEPT_2     = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ISTAC:CONCEPTSCHEME01(02.000).CONCEPT02";
    protected static final String CONCEPT_SCHEME_1_V2_CONCEPT_2_1   = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ISTAC:CONCEPTSCHEME01(02.000).CONCEPT0201";
    protected static final String CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ISTAC:CONCEPTSCHEME01(02.000).CONCEPT020101";
    protected static final String CONCEPT_SCHEME_1_V2_CONCEPT_3     = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ISTAC:CONCEPTSCHEME01(02.000).CONCEPT03";
    protected static final String CONCEPT_SCHEME_1_V2_CONCEPT_4     = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ISTAC:CONCEPTSCHEME01(02.000).CONCEPT04";
    protected static final String CONCEPT_SCHEME_1_V2_CONCEPT_4_1   = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ISTAC:CONCEPTSCHEME01(02.000).CONCEPT0401";
    protected static final String CONCEPT_SCHEME_1_V2_CONCEPT_4_1_1 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ISTAC:CONCEPTSCHEME01(02.000).CONCEPT040101";
    protected static final String CONCEPT_SCHEME_2_V1_CONCEPT_1     = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ISTAC:CONCEPTSCHEME02(01.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_2_V1_CONCEPT_2     = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ISTAC:CONCEPTSCHEME02(01.000).CONCEPT02";
    protected static final String CONCEPT_SCHEME_3_V1_CONCEPT_1     = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ISTAC:CONCEPTSCHEME03(01.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_3_V1_CONCEPT_2     = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ISTAC:CONCEPTSCHEME03(01.000).CONCEPT02";
    protected static final String CONCEPT_SCHEME_3_V1_CONCEPT_2_1   = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ISTAC:CONCEPTSCHEME03(01.000).CONCEPT0201";
    protected static final String CONCEPT_SCHEME_3_V1_CONCEPT_2_1_1 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ISTAC:CONCEPTSCHEME03(01.000).CONCEPT020101";
    protected static final String CONCEPT_SCHEME_3_V1_CONCEPT_2_2   = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ISTAC:CONCEPTSCHEME03(01.000).CONCEPT0202";
    protected static final String CONCEPT_SCHEME_4_V1_CONCEPT_1     = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ISTAC:CONCEPTSCHEME04(01.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_5_V1_CONCEPT_1     = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ISTAC:CONCEPTSCHEME05(01.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_6_V1_CONCEPT_1     = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ISTAC:CONCEPTSCHEME06(01.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_7_V2_CONCEPT_1     = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ISTAC:CONCEPTSCHEME07(02.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_8_V1_CONCEPT_1     = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ISTAC:CONCEPTSCHEME08(01.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_10_V2_CONCEPT_1    = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ISTAC:CONCEPTSCHEME10(02.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_10_V3_CONCEPT_1    = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ISTAC:CONCEPTSCHEME10(03.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_11_V1_CONCEPT_1    = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ISTAC:CONCEPTSCHEME11(01.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_12_V1_CONCEPT_1    = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ISTAC:CONCEPTSCHEME12(01.000).CONCEPT01";

    // Concept types
    protected static final String CONCEPT_TYPE_DERIVED              = "DERIVED";
    protected static final String CONCEPT_TYPE_DIRECT               = "DIRECT";

    // Organisations schemes
    protected static final String ORGANISATION_SCHEME_1_V1          = "urn:sdmx:org.sdmx.infomodel.organisationscheme.OrganisationScheme=ISTAC:ORGANISATIONSCHEME01(01.000)";

    // Other
    protected static final String NOT_EXISTS                        = "not-exists";

    // --------------------------------------------------------------------------------------------------------------
    // SERVICE CONTEXT
    // --------------------------------------------------------------------------------------------------------------

    @Override
    protected ServiceContext getServiceContextAdministrador() {
        ServiceContext serviceContext = getServiceContextWithoutPrincipal();
        putMetamacPrincipalInServiceContext(serviceContext, SrmRoleEnum.ADMINISTRADOR);
        return serviceContext;
    }

    protected ServiceContext getServiceContextTecnicoApoyoNormalizacion() {
        ServiceContext serviceContext = getServiceContextWithoutPrincipal();
        putMetamacPrincipalInServiceContext(serviceContext, SrmRoleEnum.TECNICO_APOYO_NORMALIZACION);
        return serviceContext;
    }

    protected ServiceContext getServiceContextTecnicoNormalizacion() {
        ServiceContext serviceContext = getServiceContextWithoutPrincipal();
        putMetamacPrincipalInServiceContext(serviceContext, SrmRoleEnum.TECNICO_NORMALIZACION);
        return serviceContext;
    }

    protected ServiceContext getServiceContextJefeNormalizacion() {
        ServiceContext serviceContext = getServiceContextWithoutPrincipal();
        putMetamacPrincipalInServiceContext(serviceContext, SrmRoleEnum.JEFE_NORMALIZACION);
        return serviceContext;
    }

    protected ServiceContext getServiceContextJefeNormalizacionWithOperation1() {
        ServiceContext serviceContext = getServiceContextWithoutPrincipal();
        putMetamacPrincipalInServiceContext(serviceContext, SrmRoleEnum.JEFE_NORMALIZACION, "Operation1");
        return serviceContext;    }

    protected ServiceContext getServiceContextTecnicoApoyoProduccion() {
        ServiceContext serviceContext = getServiceContextWithoutPrincipal();
        putMetamacPrincipalInServiceContext(serviceContext, SrmRoleEnum.TECNICO_APOYO_PRODUCCION);
        return serviceContext;
    }

    protected ServiceContext getServiceContextTecnicoProduccion() {
        ServiceContext serviceContext = getServiceContextWithoutPrincipal();
        putMetamacPrincipalInServiceContext(serviceContext, SrmRoleEnum.TECNICO_PRODUCCION);
        return serviceContext;
    }

    protected ServiceContext getServiceContextJefeProduccion() {
        ServiceContext serviceContext = getServiceContextWithoutPrincipal();
        putMetamacPrincipalInServiceContext(serviceContext, SrmRoleEnum.JEFE_PRODUCCION);
        return serviceContext;
    }

    protected ServiceContext getServiceContextWithoutAccesses() {
        ServiceContext ctxWithoutAcceses = getServiceContextWithoutPrincipal();

        MetamacPrincipal metamacPrincipal = new MetamacPrincipal();
        metamacPrincipal.setUserId(ctxWithoutAcceses.getUserId());
        metamacPrincipal.getAccesses().clear();
        ctxWithoutAcceses.setProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE, metamacPrincipal);
        return ctxWithoutAcceses;
    }

    protected ServiceContext getServiceContextWithoutAccessToApplication() {

        ServiceContext ctxWithoutAccessToApplication = getServiceContextWithoutPrincipal();

        MetamacPrincipal metamacPrincipal = new MetamacPrincipal();
        metamacPrincipal.setUserId(ctxWithoutAccessToApplication.getUserId());
        metamacPrincipal.getAccesses().add(new MetamacPrincipalAccess(SrmRoleEnum.JEFE_PRODUCCION.getName(), NOT_EXISTS, null));
        ctxWithoutAccessToApplication.setProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE, metamacPrincipal);
        return ctxWithoutAccessToApplication;
    }

    protected ServiceContext getServiceContextWithoutSrmRole() {
        ServiceContext ctxWithoutAccessToApplication = getServiceContextWithoutPrincipal();

        MetamacPrincipal metamacPrincipal = new MetamacPrincipal();
        metamacPrincipal.setUserId(ctxWithoutAccessToApplication.getUserId());
        metamacPrincipal.getAccesses().add(new MetamacPrincipalAccess(NOT_EXISTS, SrmConstants.SECURITY_APPLICATION_ID, null));
        ctxWithoutAccessToApplication.setProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE, metamacPrincipal);
        return ctxWithoutAccessToApplication;
    }

    private void putMetamacPrincipalInServiceContext(ServiceContext serviceContext, SrmRoleEnum role) {
        putMetamacPrincipalInServiceContext(serviceContext, role, null);
    }

    private void putMetamacPrincipalInServiceContext(ServiceContext serviceContext, SrmRoleEnum role, String operation) {
        MetamacPrincipal metamacPrincipal = new MetamacPrincipal();
        metamacPrincipal.setUserId(serviceContext.getUserId());
        metamacPrincipal.getAccesses().add(new MetamacPrincipalAccess(role.getName(), SrmConstants.SECURITY_APPLICATION_ID, operation));
        serviceContext.setProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE, metamacPrincipal);
    }

    // --------------------------------------------------------------------------------------------------------------
    // DBUNIT CONFIGURATION
    // --------------------------------------------------------------------------------------------------------------

    @Override
    protected List<String> getTablesToRemoveContent() {
        List<String> tables = new ArrayList<String>();
        tables.add("TB_M_CONCEPTS");
        tables.add("TB_M_CONCEPT_SCHEMES_VERSIONS");
        tables.add("TB_M_LIS_CONCEPT_TYPES");
        tables.add("TB_M_ORG_SCHEMES_VERSIONS");
        tables.addAll(super.getTablesToRemoveContent());
        return tables;
    }

    @Override
    protected List<String> getSequencesToRestart() {
        List<String> sequences = super.getSequencesToRestart();
        sequences.addAll(super.getSequencesToRestart());
        sequences.add("SEQ_M_CONCEPT_SCHEMES_VERSIONS");
        sequences.add("SEQ_M_CONCEPTS");
        sequences.add("SEQ_M_CONCEPT_TYPES");
        sequences.add("SEQ_M_ORG_SCHEMES_VERSIONS");
        return sequences;
    }

    @Override
    protected Map<String, String> getTablePrimaryKeys() {
        Map<String, String> tablePrimaryKeys = super.getTablePrimaryKeys();
        tablePrimaryKeys.put("TB_M_CONCEPT_SCHEMES_VERSIONS", "TB_CONCEPT_SCHEMES_VERSIONS");
        tablePrimaryKeys.put("TB_M_CONCEPTS", "TB_CONCEPTS");
        tablePrimaryKeys.put("TB_M_ORG_SCHEMES_VERSIONS", "TB_ORG_SCHEMES_VERSIONS");
        return tablePrimaryKeys;
    }

    @Override
    protected ServiceContext getServiceContextWithoutPrincipal() {
        return new ServiceContext("junit", "junit", "app");
    }
}
