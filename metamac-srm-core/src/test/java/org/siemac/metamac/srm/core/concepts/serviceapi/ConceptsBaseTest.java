package org.siemac.metamac.srm.core.concepts.serviceapi;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.common.test.MetamacBaseTests;

public abstract class ConceptsBaseTest extends MetamacBaseTests {
    
    protected static Long                      NOT_EXISTS     = Long.valueOf(-1);


    // --------------------------------------------------------------------------------------------------------------
    // SERVICE CONTEXT
    // --------------------------------------------------------------------------------------------------------------

    @Override
    protected ServiceContext getServiceContextAdministrador() {
        ServiceContext serviceContext = super.getServiceContextWithoutPrincipal();
        
        // TODO: Descomentar cuando estén los roles
//        putMetamacPrincipalInServiceContext(serviceContext, AccessControlRoleEnum.ADMINISTRADOR);
        return serviceContext;

    } 


    // --------------------------------------------------------------------------------------------------------------
    // DBUNIT CONFIGURATION
    // --------------------------------------------------------------------------------------------------------------

    @Override
    protected String getDataSetFile() {
        return "dbunit/ConceptsServiceTest.xml";
    }


    @Override
    protected List<String> getTablesToRemoveContent() {
        List<String> tables = new ArrayList<String>();
        tables.add("TB_CONCEPT_SCHEMES");
        tables.add("TB_CONCEPTS");
        
        return tables;
    }


    @Override
    protected List<String> getSequencesToRestart() {
        List<String> sequences = new ArrayList<String>();
        sequences.add("SEQ_CONCEPT_SCHEMES");
        sequences.add("SEQ_CONCEPTS");
        
        return sequences;
    }
}
