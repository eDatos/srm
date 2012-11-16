package org.siemac.metamac.srm.core.code.serviceapi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring based transactional test with DbUnit support.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
public class CodesMetamacServiceTest extends SrmBaseTest implements CodesMetamacServiceTestBase {

    @Autowired
    protected CodesMetamacService codesMetamacService;

    @Test
    public void testRetrieveCodelistByUrn() throws Exception {
        // TODO Auto-generated method stub
        // assertTrue("testRetrieveCodelistByUrn not implemented");
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmCodesTest.xml";
    }
}
