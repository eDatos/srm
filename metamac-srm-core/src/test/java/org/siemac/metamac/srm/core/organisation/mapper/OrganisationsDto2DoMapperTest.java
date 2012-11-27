package org.siemac.metamac.srm.core.organisation.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.core.organisation.serviceapi.utils.OrganisationsMetamacAsserts;
import org.siemac.metamac.srm.core.organisation.serviceapi.utils.OrganisationsMetamacDtoMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
// @TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
// @Transactional
//@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class OrganisationsDto2DoMapperTest {

    @Autowired
    private org.siemac.metamac.srm.core.organisation.mapper.OrganisationsDto2DoMapper organisationsDto2DoMapper;

    @Test
    public void testOrganisationSchemeMetamacDtoToDo() throws MetamacException {
        OrganisationSchemeMetamacDto dto = OrganisationsMetamacDtoMocks.mockOrganisationScheme(OrganisationSchemeTypeEnum.AGENCY_SCHEME);
        OrganisationSchemeVersionMetamac entity = organisationsDto2DoMapper.organisationSchemeMetamacDtoToDo(dto);
        OrganisationsMetamacAsserts.assertEqualsOrganisationScheme(dto, entity);
    }

    @Test
    public void testOrganisationMetamacDoToDto() throws MetamacException {
        OrganisationMetamacDto dto = OrganisationsMetamacDtoMocks.mockOrganisationDto(OrganisationTypeEnum.AGENCY);
        OrganisationMetamac entity = organisationsDto2DoMapper.organisationMetamacDtoToDo(dto);
        OrganisationsMetamacAsserts.assertEqualsOrganisation(dto, entity);
    }
}