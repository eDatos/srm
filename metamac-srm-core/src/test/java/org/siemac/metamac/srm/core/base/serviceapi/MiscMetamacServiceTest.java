package org.siemac.metamac.srm.core.base.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.siemac.metamac.common.test.utils.MetamacAsserts.assertEqualsDate;
import static org.siemac.metamac.common.test.utils.MetamacAsserts.assertEqualsMetamacExceptionItem;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.base.domain.MiscValue;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
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

    @Test
    @Override
    public void testFindLastUpdatedVariableElementsGeographicalInformation() throws Exception {
        DateTime value = miscMetamacService.findLastUpdatedVariableElementsGeographicalInformation(getServiceContextAdministrador());

        // Validate
        assertEqualsDate("2011-01-01 01:02:03", value);
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

    @Test
    public void testCreateOrUpdateMiscValueErrorMetadataRequired() throws Exception {
        String name = null;
        DateTime value = null;
        try {
            miscMetamacService.createOrUpdateMiscValue(getServiceContextAdministrador(), name, value);
            fail("wrong metadata");
        } catch (MetamacException e) {
            assertEquals(2, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.PARAMETER_REQUIRED, 1, new Serializable[]{ServiceExceptionParameters.MISC_VALUE_NAME}, e.getExceptionItems().get(0));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.PARAMETER_REQUIRED, 1, new Serializable[]{ServiceExceptionParameters.MISC_VALUE_VALUE}, e.getExceptionItems().get(1));
        }
    }

    @Override
    @Test
    public void testUpdateLastUpdatedVariableElementsGeographicalInformation() throws Exception {
        DateTime value = new DateTime();
        miscMetamacService.updateLastUpdatedVariableElementsGeographicalInformation(getServiceContextAdministrador(), value);

        // Validate
        MiscValue miscValue = miscMetamacService.findOneMiscValueByName(getServiceContextAdministrador(), SrmConstants.MISC_VALUE_VARIABLE_ELEMENT_GEOGRAPHICAL_INFORMATION_LAST_UPDATED_DATE);
        assertTrue(DateUtils.isSameDay(value.toDate(), miscValue.getDateValue().toDate()));
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmMiscTest.xml";
    }
}
