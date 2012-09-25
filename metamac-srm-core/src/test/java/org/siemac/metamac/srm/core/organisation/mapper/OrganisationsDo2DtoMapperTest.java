package org.siemac.metamac.srm.core.organisation.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.organisation.serviceapi.utils.OrganisationsMetamacDoMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.sdmx.v2_1.domain.dto.organisation.OrganisationSchemeDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
public class OrganisationsDo2DtoMapperTest extends SrmBaseTest {

    @Autowired
    @Qualifier("organisationsDo2DtoMapper")
    private org.siemac.metamac.srm.core.organisation.mapper.OrganisationsDo2DtoMapper do2DtoMapper;

    @Test
    public void testOrganisationSchemeDoToDto() {
        OrganisationSchemeDto organisationSchemeDto = do2DtoMapper.organisationSchemeMetamacDoToDto(OrganisationsMetamacDoMocks.mockOrganisationScheme(OrganisationSchemeTypeEnum.AGENCY_SCHEME));
        // TODO
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmOrganisationsTest.xml";
    }

}
