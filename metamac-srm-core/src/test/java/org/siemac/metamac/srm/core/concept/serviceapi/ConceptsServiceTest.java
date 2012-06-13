package org.siemac.metamac.srm.core.concept.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.utils.MetamacAsserts;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.error.MetamacCoreExceptionType;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.concept.domain.ConceptScheme;
import org.siemac.metamac.srm.core.concept.serviceapi.utils.ConceptsAsserts;
import org.siemac.metamac.srm.core.concept.serviceapi.utils.ConceptsDoMocks;
import org.siemac.metamac.srm.core.utils.SrmAsserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
public class ConceptsServiceTest extends SrmBaseTest  implements ConceptsServiceTestBase {
    
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
        
        SrmAsserts.assertEqualsInternationalString(conceptScheme.getItemScheme().getName(), "es", "Nombre conceptScheme-1-v1", "en", "Name conceptScheme-1-v1");
        
    }
    
    @Test
    public void testFindConceptSchemeByIdParameterRequired() throws Exception {
        Long id = null;
        
        try {
            conceptsService.findConceptSchemeById(getServiceContextWithoutPrincipal(), id);
            fail("parameter required"); 
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(MetamacCoreExceptionType.PARAMETER_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.ID, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }
    
    @Test
    public void testFindConceptSchemeByIdNotFound() throws Exception {
        Long id = NOT_EXISTS;
        
        try {
            conceptsService.findConceptSchemeById(getServiceContextWithoutPrincipal(), id);
            fail("not found"); 
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(MetamacCoreExceptionType.CONCEPT_SCHEME_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(id, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateConceptScheme() throws Exception {
        ConceptScheme conceptScheme = ConceptsDoMocks.createConceptScheme();
        
        ConceptScheme createdConceptScheme = conceptsService.createConceptScheme(getServiceContextWithoutPrincipal(), conceptScheme);
        
        // Validate
        assertNotNull(createdConceptScheme);
        assertNotNull(createdConceptScheme.getCreatedBy());
        assertNotNull(createdConceptScheme.getCreatedDate());
        assertNotNull(createdConceptScheme.getLastUpdatedBy());
        assertNotNull(createdConceptScheme.getLastUpdated());
        
        assertNotNull(createdConceptScheme.getVersion());
        assertNotNull(createdConceptScheme.getId());
        assertNotNull(createdConceptScheme.getUuid());
        
        ConceptsAsserts.assertEqualsConceptScheme(conceptScheme, createdConceptScheme);
    }
    
    @Test
    public void testCreateConceptSchemeRequiredParameter() throws Exception {
        try {
            conceptsService.createConceptScheme(getServiceContextWithoutPrincipal(), null);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(MetamacCoreExceptionType.PARAMETER_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_SCHEME, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
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
