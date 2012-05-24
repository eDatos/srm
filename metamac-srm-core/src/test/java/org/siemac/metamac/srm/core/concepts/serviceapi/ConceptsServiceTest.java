package org.siemac.metamac.srm.core.concept.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.utils.MetamacAsserts;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.srm.core.concept.domain.ConceptScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
public class ConceptsServiceTest extends ConceptsBaseTest  implements ConceptsServiceTestBase {
    
    @Autowired
    protected ConceptsService conceptsService;
    
    @Test
    public void testFindConceptSchemeById() throws Exception {
        Long id = CONCEPT_SCHEME_1;
        
        ConceptScheme conceptScheme = conceptsService.findConceptSchemeById(getServiceContextWithoutPrincipal(), id);
        
        assertEquals(Long.valueOf(1), conceptScheme.getId());
        assertEquals("conceptScheme-1", conceptScheme.getUuid());
        assertEquals(Long.valueOf(1), conceptScheme.getVersion());
        
        assertEquals("user1", conceptScheme.getCreatedBy());
        MetamacAsserts.assertEqualsDate("2011-01-01 01:02:03", conceptScheme.getCreatedDate());
        assertEquals("user2", conceptScheme.getLastUpdatedBy());
        MetamacAsserts.assertEqualsDate("2011-01-22 01:02:03", conceptScheme.getLastUpdated());
        
        assertEquals("annotableArtefact-1", conceptScheme.getItemScheme().getUuid());
        assertEquals("conceptScheme-1", conceptScheme.getItemScheme().getIdLogic());
        assertEquals("conceptScheme-1", conceptScheme.getItemScheme().getIdLogic());
        assertEquals("uri:urn:22f5f72e-4275-4f4a-ae05-ca4da0131fbc", conceptScheme.getItemScheme().getUri());
        assertEquals("http://sdmx/v2.1/conceptScheme/conceptScheme-1/v1", conceptScheme.getItemScheme().getUrn());
        assertNull(conceptScheme.getItemScheme().getReplacedBy());
        
        assertEquals("http://sdmx/v2.1/agency/standAloneAgencies/ISTAC", conceptScheme.getItemScheme().getMaintainer().getUriInt());
        assertEquals("ISTAC", conceptScheme.getItemScheme().getMaintainer().getCodeId());
        assertEquals(TypeExternalArtefactsEnum.AGENCY, conceptScheme.getItemScheme().getMaintainer().getType());
        
        SrmAsserts.assertEqualsInternationalStringDto(conceptScheme.getItemScheme().getna, locale1, label1, locale2, label2)
        
    }

    @Test
    public void testCreateConceptScheme() throws Exception {
        // TODO Auto-generated method stub
        fail("testCreateConceptScheme not implemented");
    }

    @Test
    public void testUpdateConceptScheme() throws Exception {
        // TODO Auto-generated method stub
        fail("testUpdateConceptScheme not implemented");
    }

    @Test
    public void testDeleteConceptScheme() throws Exception {
        // TODO Auto-generated method stub
        fail("testDeleteConceptScheme not implemented");
    }

    @Test
    public void testFindAllConceptSchemes() throws Exception {
        // TODO Auto-generated method stub
        fail("testFindAllConceptSchemes not implemented");
    }

    @Test
    public void testFindConceptSchemeByCondition() throws Exception {
        // TODO Auto-generated method stub
        fail("testFindConceptSchemeByCondition not implemented");
    }

    @Test
    public void testCreateConcept() throws Exception {
        // TODO Auto-generated method stub
        fail("testCreateConcept not implemented");
    }

    @Test
    public void testUpdateConcept() throws Exception {
        // TODO Auto-generated method stub
        fail("testUpdateConcept not implemented");
    }

    @Test
    public void testDeleteConcept() throws Exception {
        // TODO Auto-generated method stub
        fail("testDeleteConcept not implemented");
    }

    @Test
    public void testFindConceptById() throws Exception {
        // TODO Auto-generated method stub
        fail("testFindConceptById not implemented");
    }

    @Test
    public void testFindConceptSchemeConcepts() throws Exception {
        // TODO Auto-generated method stub
        fail("testFindConceptSchemeConcepts not implemented");
    }
}
