package com.arte.statistic.sdmx.srm.core.common;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacadeCoreMockedTest;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacadeTest;

/**
 * Spring based transactional test with DbUnit support.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({SrmCoreServiceFacadeTest.class, SrmCoreServiceFacadeCoreMockedTest.class})
public class SrmSuite {
}
