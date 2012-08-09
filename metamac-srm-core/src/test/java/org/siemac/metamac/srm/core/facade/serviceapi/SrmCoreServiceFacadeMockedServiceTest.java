package org.siemac.metamac.srm.core.facade.serviceapi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.arte.statistic.sdmx.srm.core.common.SdmxSrmBaseTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/include/core-mockito.xml", "classpath:spring/srm/applicationContext-test.xml"})
public class SrmCoreServiceFacadeMockedServiceTest extends SdmxSrmBaseTest {

    // TODO completar tests de facade
    
    
    @Autowired
    protected SrmCoreServiceFacade srmCoreServiceFacade;

    private String                 CONCEPT_SCHEME_1_V1 = "urn:sdmx:org.sdmx.infomodel.concepscheme.ConceptScheme=ISTAC:CONCEPTSCHEME01(01.000)";

    @Test
    public void testDeleteConceptScheme() throws Exception {
        srmCoreServiceFacade.deleteConceptScheme(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V1);
    }
}
