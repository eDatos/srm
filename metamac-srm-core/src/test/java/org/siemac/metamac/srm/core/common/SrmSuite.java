package org.siemac.metamac.srm.core.common;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.siemac.metamac.srm.core.base.mapper.BaseDo2DtoMapperTest;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsMetamacServiceTest;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacadeConceptsSecurityTest;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacadeConceptsTest;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacadeDsdTest;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacadeOrganisationsSecurityTest;
import org.siemac.metamac.srm.core.organisation.mapper.OrganisationsDto2DoMapperTest;

import com.arte.statistic.sdmx.srm.core.organisation.mapper.OrganisationsDo2DtoMapperTest;

/**
 * Spring based transactional test with DbUnit support.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({SrmCoreServiceFacadeConceptsTest.class, SrmCoreServiceFacadeConceptsSecurityTest.class, SrmCoreServiceFacadeDsdTest.class, ConceptsMetamacServiceTest.class,
        BaseDo2DtoMapperTest.class, OrganisationsDo2DtoMapperTest.class, OrganisationsDto2DoMapperTest.class, SrmCoreServiceFacadeOrganisationsSecurityTest.class})
public class SrmSuite {
}
