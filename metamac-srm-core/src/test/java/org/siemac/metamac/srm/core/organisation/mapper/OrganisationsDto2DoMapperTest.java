package org.siemac.metamac.srm.core.organisation.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.core.organisation.serviceapi.utils.OrganisationsMetamacAsserts;
import org.siemac.metamac.srm.core.organisation.serviceapi.utils.OrganisationsMetamacDtoMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
//@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
//@Transactional
public class OrganisationsDto2DoMapperTest {

    @Autowired
    private org.siemac.metamac.srm.core.organisation.mapper.OrganisationsDto2DoMapper organisationsDto2DoMapper;

    @Test
    public void testOrganisationSchemeMetamacDoToDto() throws MetamacException {
        OrganisationSchemeMetamacDto dto = OrganisationsMetamacDtoMocks.mockOrganisationScheme();
        OrganisationSchemeVersionMetamac entity = organisationsDto2DoMapper.organisationSchemeMetamacDtoToDo(dto);
        OrganisationsMetamacAsserts.assertEqualsOrganisationScheme(dto, entity);
    }

    // TODO Organisations
}
