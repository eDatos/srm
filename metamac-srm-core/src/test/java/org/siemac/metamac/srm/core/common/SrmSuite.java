package org.siemac.metamac.srm.core.common;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.siemac.metamac.srm.core.base.mapper.BaseDo2DtoMapperTest;
import org.siemac.metamac.srm.core.category.mapper.CategoriesDo2DtoMapperTest;
import org.siemac.metamac.srm.core.category.mapper.CategoriesDto2DoMapperTest;
import org.siemac.metamac.srm.core.concept.mapper.ConceptsDo2DtoMapperTest;
import org.siemac.metamac.srm.core.concept.mapper.ConceptsDto2DoMapperTest;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsMetamacServiceTest;
import org.siemac.metamac.srm.core.dsd.serviceapi.DsdsMetamacServiceTest;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacadeConceptsSecurityTest;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacadeConceptsTest;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacadeDsdTest;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacadeOrganisationsSecurityTest;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacadeOrganisationsTest;
import org.siemac.metamac.srm.core.organisation.mapper.OrganisationsDo2DtoMapperTest;
import org.siemac.metamac.srm.core.organisation.mapper.OrganisationsDto2DoMapperTest;
import org.siemac.metamac.srm.core.organisation.serviceapi.OrganisationsMetamacServiceTest;

import com.arte.statistic.sdmx.srm.core.category.serviceapi.CategoriesServiceTest;

/**
 * Spring based transactional test with DbUnit support.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({SrmCoreServiceFacadeConceptsTest.class, SrmCoreServiceFacadeOrganisationsTest.class, SrmCoreServiceFacadeConceptsSecurityTest.class, SrmCoreServiceFacadeDsdTest.class,
        ConceptsMetamacServiceTest.class, BaseDo2DtoMapperTest.class, OrganisationsDo2DtoMapperTest.class, OrganisationsDto2DoMapperTest.class, ConceptsDo2DtoMapperTest.class,
        ConceptsDto2DoMapperTest.class, SrmCoreServiceFacadeOrganisationsSecurityTest.class, OrganisationsMetamacServiceTest.class, DsdsMetamacServiceTest.class, CategoriesServiceTest.class,
        CategoriesDto2DoMapperTest.class, CategoriesDo2DtoMapperTest.class})
public class SrmSuite {
}
