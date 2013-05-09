package org.siemac.metamac.srm.core.organisation.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacBasicDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.core.organisation.serviceapi.utils.OrganisationsMetamacAsserts;
import org.siemac.metamac.srm.core.organisation.serviceapi.utils.OrganisationsMetamacDoMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class OrganisationsDo2DtoMapperTest extends SrmBaseTest {

    @Autowired
    private OrganisationsDo2DtoMapper organisationsDo2DtoMapper;

    @Test
    public void testOrganisationSchemeMetamacDoToDto() throws MetamacException {
        OrganisationSchemeVersionMetamac entity = OrganisationsMetamacDoMocks.mockOrganisationSchemeFixedValues("agency01", "organisationScheme01", "01.000",
                OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME);

        OrganisationSchemeMetamacDto dto = organisationsDo2DtoMapper.organisationSchemeMetamacDoToDto(entity);
        OrganisationsMetamacAsserts.assertEqualsOrganisationScheme(entity, dto);
    }

    @Test
    public void testOrganisationSchemeMetamacDoToBasicDto() throws MetamacException {
        OrganisationSchemeVersionMetamac entity = OrganisationsMetamacDoMocks.mockOrganisationSchemeFixedValues("agency01", "organisationScheme01", "01.000",
                OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME);
        OrganisationSchemeMetamacBasicDto dto = organisationsDo2DtoMapper.organisationSchemeMetamacDoToBasicDto(entity);
        OrganisationsMetamacAsserts.assertEqualsOrganisationScheme(entity, dto);
    }

    @Test
    public void testOrganisationMetamacDoToDto() throws MetamacException {
        OrganisationMetamac entity = mockOrganisationWithAllMetadata(OrganisationSchemeTypeEnum.AGENCY_SCHEME, OrganisationTypeEnum.AGENCY);
        OrganisationMetamacDto dto = organisationsDo2DtoMapper.organisationMetamacDoToDto(entity);
        OrganisationsMetamacAsserts.assertEqualsOrganisation(entity, dto);
    }

    @Test
    public void testOrganisationMetamacDoToBasicDto() throws MetamacException {
        OrganisationMetamac entity = mockOrganisationWithAllMetadata(OrganisationSchemeTypeEnum.AGENCY_SCHEME, OrganisationTypeEnum.AGENCY);
        OrganisationMetamacBasicDto dto = organisationsDo2DtoMapper.organisationMetamacDoToBasicDto(entity);
        OrganisationsMetamacAsserts.assertEqualsOrganisation(entity, dto);
    }

    private OrganisationMetamac mockOrganisationWithAllMetadata(OrganisationSchemeTypeEnum organisationSchemeTypeEnum, OrganisationTypeEnum type) throws MetamacException {
        OrganisationSchemeVersionMetamac organisationScheme = OrganisationsMetamacDoMocks.mockOrganisationSchemeFixedValues("agency01", "organisationScheme01", "01.000",
                OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME);
        OrganisationMetamac entity = OrganisationsMetamacDoMocks.mockOrganisationFixedValues("organisation01", organisationScheme, null, OrganisationTypeEnum.DATA_PROVIDER);
        return entity;
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmOrganisationsTest.xml";
    }
}