package org.siemac.metamac.srm.core.common;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.siemac.metamac.srm.core.base.mapper.BaseDo2DtoMapperTest;
import org.siemac.metamac.srm.core.category.mapper.CategoriesDo2DtoMapperTest;
import org.siemac.metamac.srm.core.category.mapper.CategoriesDto2DoMapperTest;
import org.siemac.metamac.srm.core.code.mapper.CodesDo2DtoMapperTest;
import org.siemac.metamac.srm.core.code.mapper.CodesDto2DoMapperTest;
import org.siemac.metamac.srm.core.code.serviceapi.CodesMetamacServiceTest;
import org.siemac.metamac.srm.core.concept.mapper.ConceptsDo2DtoMapperTest;
import org.siemac.metamac.srm.core.concept.mapper.ConceptsDto2DoMapperTest;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsMetamacServiceTest;
import org.siemac.metamac.srm.core.dsd.serviceapi.DataStructureDefinitionMetamacServiceTest;
import org.siemac.metamac.srm.core.error.SrmCheckTranslationsTest;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacadeConceptsSecurityTest;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacadeConceptsTest;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacadeDataStructureDefinitionTest;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacadeImportationCodesTest;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacadeImportationConceptTest;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacadeImportationDataStructureDefinitionTest;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacadeImportationOrganisationTest;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacadeOrganisationsSecurityTest;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacadeOrganisationsTest;
import org.siemac.metamac.srm.core.organisation.mapper.OrganisationsDo2DtoMapperTest;
import org.siemac.metamac.srm.core.organisation.mapper.OrganisationsDto2DoMapperTest;
import org.siemac.metamac.srm.core.organisation.serviceapi.OrganisationsMetamacServiceTest;

import com.arte.statistic.sdmx.srm.core.category.serviceapi.CategoriesServiceTest;
import com.arte.statistic.sdmx.srm.core.structure.mapper.DataStructureDefinitionDto2DoMapperTest;

/**
 * Spring based transactional test with DbUnit support.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({SrmCheckTranslationsTest.class, SrmCoreServiceFacadeConceptsTest.class, SrmCoreServiceFacadeOrganisationsTest.class, SrmCoreServiceFacadeConceptsSecurityTest.class,
        SrmCoreServiceFacadeDataStructureDefinitionTest.class, ConceptsMetamacServiceTest.class, BaseDo2DtoMapperTest.class, OrganisationsDo2DtoMapperTest.class, OrganisationsDto2DoMapperTest.class,
        ConceptsDo2DtoMapperTest.class, ConceptsDto2DoMapperTest.class, SrmCoreServiceFacadeOrganisationsSecurityTest.class, OrganisationsMetamacServiceTest.class,
        DataStructureDefinitionMetamacServiceTest.class, CategoriesServiceTest.class, CategoriesDto2DoMapperTest.class, CategoriesDo2DtoMapperTest.class, CodesMetamacServiceTest.class,
        CodesDo2DtoMapperTest.class, CodesDto2DoMapperTest.class, DataStructureDefinitionDto2DoMapperTest.class, SrmCoreServiceFacadeImportationDataStructureDefinitionTest.class,
        SrmCoreServiceFacadeImportationOrganisationTest.class, SrmCoreServiceFacadeImportationConceptTest.class, SrmCoreServiceFacadeImportationCodesTest.class})
public class SrmSuite {
}
