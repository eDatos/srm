package org.siemac.metamac.srm.core.concepts.serviceapi;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.srm.core.base.domain.ItemScheme;
import org.siemac.metamac.srm.core.concept.domain.ConceptScheme;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsService;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsServiceTestBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Spring based transactional test with DbUnit support.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:oracle/core/applicationContext-oracle-test.xml"})
public class ConceptsServiceTest extends ConceptsBaseTest implements ConceptsServiceTestBase {

    @Autowired
    protected ConceptsService conceptsService;
    

    /**************************************************************************
     * CONCEPT SCHEMES
     **************************************************************************/
    
    @Test
    public void testFindConceptSchemeById() throws Exception {
        // TODO Auto-generated method stub
        fail("testFindConceptSchemeById not implemented");
    }

    @Test
    public void testCreateConceptScheme() throws Exception {
        
        ItemScheme itemScheme = new ItemScheme();
        itemScheme.setFinalLogic(Boolean.FALSE);
        itemScheme.setIsPartial(Boolean.FALSE);
        itemScheme.setIdLogic("PRUEBA-CONCEPT-SCHEME");
        
        ConceptScheme conceptScheme = new ConceptScheme();
        conceptScheme.setItemScheme(itemScheme);

        // Create
        ConceptScheme conceptSchemeCreated = conceptsService.createConceptScheme(getServiceContextAdministrador(), conceptScheme);

        // Validate properties are not in Dto
        String uuid = conceptSchemeCreated.getUuid();
        Long version = conceptSchemeCreated.getVersion();
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

    
    /**************************************************************************
     * CONCEPTS
     **************************************************************************/
    
    @Test
    public void testFindConceptById() throws Exception {
        // TODO Auto-generated method stub
        fail("testFindConceptById not implemented");
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
    public void testFindConceptSchemeConcepts() throws Exception {
        // TODO Auto-generated method stub
        fail("testFindConceptSchemeConcepts not implemented");
    }

    @Test
    public void testFindConceptByCondition() throws Exception {
        // TODO Auto-generated method stub
        fail("testFindConceptByCondition not implemented");
    }
    
}
