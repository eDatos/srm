package org.siemac.metamac.srm.core.organisation.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.core.organisation.serviceapi.utils.OrganisationsMetamacAsserts;
import org.siemac.metamac.srm.core.organisation.serviceapi.utils.OrganisationsMetamacDoMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
public class OrganisationsDo2DtoMapperTest {

    @Autowired
    @Qualifier("organisationsDo2DtoMapper")
    private OrganisationsDo2DtoMapper organisationsDo2DtoMapper;

    @Test
    public void testOrganisationSchemeMetamacDoToDto() {
        OrganisationSchemeVersionMetamac entity = OrganisationsMetamacDoMocks.mockOrganisationScheme(OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME);
        OrganisationSchemeMetamacDto dto = organisationsDo2DtoMapper.organisationSchemeMetamacDoToDto(entity);
        OrganisationsMetamacAsserts.assertEqualsOrganisationScheme(entity, dto);
    }

}
