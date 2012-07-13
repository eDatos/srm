package org.siemac.metamac.srm.core.common;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.common.test.MetamacBaseTests;

public abstract class SrmBaseTest extends MetamacBaseTests {

    protected static Long NOT_EXISTS                 = Long.valueOf(-1);

    protected static Long CONCEPT_SCHEME_1           = Long.valueOf(1);
    protected static Long CONCEPT_SCHEME_2           = Long.valueOf(2);

    protected static Long CONCEPT_SCHEME_1_CONCEPT_1 = Long.valueOf(1);
    protected static Long CONCEPT_SCHEME_1_CONCEPT_2 = Long.valueOf(2);

    // --------------------------------------------------------------------------------------------------------------
    // SERVICE CONTEXT
    // --------------------------------------------------------------------------------------------------------------

    @Override
    protected ServiceContext getServiceContextAdministrador() {
        ServiceContext serviceContext = super.getServiceContextWithoutPrincipal();
        
        // TODO: Descomentar cuando esten los roles
        // putMetamacPrincipalInServiceContext(serviceContext, AccessControlRoleEnum.ADMINISTRADOR);
        return serviceContext;
    }


    // TODO: Descomentar cuando ya esten definidos los roles
//    private void putMetamacPrincipalInServiceContext(ServiceContext serviceContext, AccessControlRoleEnum role) {
//        MetamacPrincipal metamacPrincipal = new MetamacPrincipal();
//        metamacPrincipal.setUserId(serviceContext.getUserId());
//        metamacPrincipal.getAccesses().add(new MetamacPrincipalAccess(role.getName(), SrmConstants.SECURITY_APPLICATION_ID, null));
//        serviceContext.setProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE, metamacPrincipal);
//    }

    // --------------------------------------------------------------------------------------------------------------
    // DBUNIT CONFIGURATION
    // --------------------------------------------------------------------------------------------------------------

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmCoreTest.xml";
    }

    @Override
    protected List<String> getTablesToRemoveContent() {
        List<String> tables = new ArrayList<String>();
        
        tables.add("TB_LOCALISED_STRINGS");
        tables.add("TB_ANNOTATIONS");
        tables.add("TB_DIMREL_GROUPDIM");
        tables.add("TB_DIMREL_DIMCOM");
        tables.add("TB_ATTR_RELATIONSHIPS");
        tables.add("TB_DIMC_EXTITEM");
        tables.add("TB_MEAD_EXTITEM");
        tables.add("TB_DATTR_EXTITEM");
        tables.add("TB_EXTERNAL_ITEMS");
        tables.add("TB_CONCEPTS");
        tables.add("TB_CONCEPT_SCHEMES");
        tables.add("TB_INTERNATIONAL_STRINGS");
        tables.add("TB_FACETS");
        tables.add("TB_REPRESENTATIONS");
        tables.add("TB_COMPLIST_COMP");
        tables.add("TB_ANNOTABLE_ARTEFACTS");
        
        return tables;
    }

    @Override
    protected List<String> getSequencesToRestart() {
        List<String> sequences = new ArrayList<String>();
        sequences.add("SEQ_ANNOTABLE");
        sequences.add("SEQ_ANNOTATION");
        sequences.add("SEQ_I18NSTRS");
        sequences.add("SEQ_L10NSTRS");
        sequences.add("SEQ_CONTACT");
        sequences.add("SEQ_REPREN");
        sequences.add("SEQ_FACET");
        sequences.add("SEQ_ATTR_RELAT");
        sequences.add("SEQ_EXTERNAL_ITEMS");
        
        sequences.add("SEQ_CONCEPT_SCHEMES");
        sequences.add("SEQ_CONCEPTS");
        
        return sequences;
    }

}
