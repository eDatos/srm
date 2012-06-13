package org.siemac.metamac.srm.core.common;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.siemac.metamac.srm.core.base.serviceapi.SdmxBaseServiceTest;
import org.siemac.metamac.srm.core.common.test.SDMXResources;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsServiceTest;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacadeTest;
import org.siemac.metamac.srm.core.structure.serviceapi.DataStructureDefinitionServiceTest;

/**
 * Spring based transactional test with DbUnit support.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({SdmxBaseServiceTest.class, SDMXResources.class, ConceptsServiceTest.class,
    SrmCoreServiceFacadeTest.class, DataStructureDefinitionServiceTest.class})
public class SrmSuiteTest {
}
