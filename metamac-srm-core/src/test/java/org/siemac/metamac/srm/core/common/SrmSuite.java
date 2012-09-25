package org.siemac.metamac.srm.core.common;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsMetamacServiceTest;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacadeConceptsSecurityTest;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacadeConceptsTest;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacadeDsdTest;
import org.siemac.metamac.srm.core.organisation.mapper.OrganisationsDo2DtoMapperTest;

/**
 * Spring based transactional test with DbUnit support.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({SrmCoreServiceFacadeConceptsTest.class, SrmCoreServiceFacadeConceptsSecurityTest.class, SrmCoreServiceFacadeDsdTest.class, ConceptsMetamacServiceTest.class,
        OrganisationsDo2DtoMapperTest.class})
public class SrmSuite {
}
