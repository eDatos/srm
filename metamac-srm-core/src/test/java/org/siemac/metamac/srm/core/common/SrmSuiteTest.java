package org.siemac.metamac.srm.core.common;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.siemac.metamac.srm.core.base.serviceapi.BaseServiceTest;
import org.siemac.metamac.srm.core.common.tests.JavaSerializationTest;
import org.siemac.metamac.srm.core.common.tests.KryoTest;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsServiceTest;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacadeTest;
import org.siemac.metamac.srm.core.structure.serviceapi.DataStructureDefinitionServiceTest;

/**
 * Spring based transactional test with DbUnit support.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({BaseServiceTest.class, ConceptsServiceTest.class, SrmCoreServiceFacadeTest.class, DataStructureDefinitionServiceTest.class, JavaSerializationTest.class, KryoTest.class})
public class SrmSuiteTest {
}
