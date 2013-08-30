package org.siemac.metamac.srm.core.base.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.siemac.metamac.common.test.utils.MetamacAsserts.assertEqualsDate;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.srm.core.base.domain.MiscValue;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class MiscMetamacServiceTest extends SrmBaseTest implements MiscMetamacServiceTestBase {

    @Autowired
    protected MiscMetamacService miscMetamacService;

    @Test
    @Override
    public void testFindOneMiscValueByName() throws Exception {
        String name = SrmConstants.MISC_VALUE_VARIABLE_ELEMENT_GEOGRAPHICAL_INFORMATION_LAST_UPDATED_DATE;
        MiscValue miscValue = miscMetamacService.findOneMiscValueByName(getServiceContextAdministrador(), name);

        // Validate
        assertEquals(Long.valueOf(100), miscValue.getId());
        assertEquals(name, miscValue.getName());
        assertEqualsDate("2011-01-01 01:02:03", miscValue.getDateValue());
        assertEquals(null, miscValue.getStringValue());
    }

    @Test
    public void testFindOneMiscValueByNameNotExist() throws Exception {
        String name = "not_exists";
        MiscValue miscValue = miscMetamacService.findOneMiscValueByName(getServiceContextAdministrador(), name);

        // Validate
        assertNull(miscValue);
    }

    @Override
    @Test
    public void testCreateOrUpdateMiscValue() throws Exception {
        String name = SrmConstants.MISC_VALUE_VARIABLE_ELEMENT_GEOGRAPHICAL_INFORMATION_LAST_UPDATED_DATE;
        DateTime value = new DateTime();
        MiscValue miscValue = miscMetamacService.createOrUpdateMiscValue(getServiceContextAdministrador(), name, value);

        // Validate
        assertEquals(Long.valueOf(100), miscValue.getId());
        assertEquals(name, miscValue.getName());
        assertTrue(DateUtils.isSameDay(new Date(), miscValue.getDateValue().toDate()));
        assertEquals(null, miscValue.getStringValue());
    }

    @Test
    public void testCreateOrUpdateMiscValueUnexisting() throws Exception {
        String name = "new_name";
        DateTime value = new DateTime();
        MiscValue miscValue = miscMetamacService.createOrUpdateMiscValue(getServiceContextAdministrador(), name, value);

        // Validate
        assertTrue(DateUtils.isSameDay(new Date(), miscValue.getCreatedDate().toDate()));
        assertEquals(name, miscValue.getName());
        assertTrue(DateUtils.isSameDay(new Date(), miscValue.getDateValue().toDate()));
        assertEquals(null, miscValue.getStringValue());
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmMiscTest.xml";
    }
}
