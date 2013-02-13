package org.siemac.metamac.srm.core.organisation.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
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

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
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
    public void testOrganisationSchemeMetamacDoToDto() {
        OrganisationSchemeVersionMetamac entity = OrganisationsMetamacDoMocks.mockOrganisationSchemeFixedValues("agency01", "organisationScheme01", "01.000",
                OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME);

        OrganisationSchemeMetamacDto dto = organisationsDo2DtoMapper.organisationSchemeMetamacDoToDto(entity);
        OrganisationsMetamacAsserts.assertEqualsOrganisationScheme(entity, dto);
    }
    @Test
    public void testOrganisationMetamacDoToDto() {
        OrganisationMetamac entity = mockOrganisationWithAllMetadata(OrganisationSchemeTypeEnum.AGENCY_SCHEME, OrganisationTypeEnum.AGENCY);
        OrganisationMetamacDto dto = organisationsDo2DtoMapper.organisationMetamacDoToDto(entity);
        OrganisationsMetamacAsserts.assertEqualsOrganisation(entity, dto);
    }

    @Test
    public void testOrganisationMetamacDoListToItemHierarchyDtoList() {
        List<OrganisationMetamac> entities = new ArrayList<OrganisationMetamac>();

        // -> Organisation1
        OrganisationMetamac entity1 = mockOrganisationWithAllMetadata(OrganisationSchemeTypeEnum.AGENCY_SCHEME, OrganisationTypeEnum.ORGANISATION_UNIT);
        entities.add(entity1);
        // -> -> Organisation 1A
        OrganisationMetamac entity1A = mockOrganisationWithAllMetadata(OrganisationSchemeTypeEnum.AGENCY_SCHEME, OrganisationTypeEnum.ORGANISATION_UNIT);
        entity1.addChildren(entity1A);
        // -> -> Organisation 1B
        OrganisationMetamac entity1B = mockOrganisationWithAllMetadata(OrganisationSchemeTypeEnum.AGENCY_SCHEME, OrganisationTypeEnum.ORGANISATION_UNIT);
        entity1.addChildren(entity1B);
        // -> Organisation2
        OrganisationMetamac entity2 = mockOrganisationWithAllMetadata(OrganisationSchemeTypeEnum.AGENCY_SCHEME, OrganisationTypeEnum.ORGANISATION_UNIT);
        entities.add(entity2);
        // -> Organisation3
        OrganisationMetamac entity3 = mockOrganisationWithAllMetadata(OrganisationSchemeTypeEnum.AGENCY_SCHEME, OrganisationTypeEnum.ORGANISATION_UNIT);
        entities.add(entity3);
        // -> -> Organisation 3A
        OrganisationMetamac entity3A = mockOrganisationWithAllMetadata(OrganisationSchemeTypeEnum.AGENCY_SCHEME, OrganisationTypeEnum.ORGANISATION_UNIT);
        entity3.addChildren(entity3A);
        // -> -> Organisation 3AA
        OrganisationMetamac entity3AA = mockOrganisationWithAllMetadata(OrganisationSchemeTypeEnum.AGENCY_SCHEME, OrganisationTypeEnum.ORGANISATION_UNIT);
        entity3A.addChildren(entity3AA);

        List<ItemHierarchyDto> dtos = organisationsDo2DtoMapper.organisationMetamacDoListToItemHierarchyDtoList(entities);

        // Validate
        assertEquals(3, dtos.size());
        assertEquals(entity1.getNameableArtefact().getCode(), dtos.get(0).getItem().getCode());
        assertTrue(dtos.get(0).getItem() instanceof OrganisationMetamacDto);
        assertEquals(2, dtos.get(0).getChildren().size());
        assertEquals(entity1A.getNameableArtefact().getCode(), dtos.get(0).getChildren().get(0).getItem().getCode());
        assertEquals(0, dtos.get(0).getChildren().get(0).getChildren().size());
        assertEquals(entity1B.getNameableArtefact().getCode(), dtos.get(0).getChildren().get(1).getItem().getCode());
        assertEquals(0, dtos.get(0).getChildren().get(1).getChildren().size());

        assertEquals(entity2.getNameableArtefact().getCode(), dtos.get(1).getItem().getCode());
        assertEquals(0, dtos.get(1).getChildren().size());

        assertEquals(entity3.getNameableArtefact().getCode(), dtos.get(2).getItem().getCode());
        assertEquals(1, dtos.get(2).getChildren().size());
        assertEquals(entity3A.getNameableArtefact().getCode(), dtos.get(2).getChildren().get(0).getItem().getCode());
        assertEquals(1, dtos.get(2).getChildren().get(0).getChildren().size());
        assertEquals(entity3AA.getNameableArtefact().getCode(), dtos.get(2).getChildren().get(0).getChildren().get(0).getItem().getCode());
        assertEquals(0, dtos.get(2).getChildren().get(0).getChildren().get(0).getChildren().size());
    }

    private OrganisationMetamac mockOrganisationWithAllMetadata(OrganisationSchemeTypeEnum organisationSchemeTypeEnum, OrganisationTypeEnum type) {
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