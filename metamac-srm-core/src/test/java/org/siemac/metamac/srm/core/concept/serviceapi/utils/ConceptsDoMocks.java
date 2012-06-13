package org.siemac.metamac.srm.core.concept.serviceapi.utils;

import org.siemac.metamac.srm.core.base.serviceapi.utils.BaseDoMocks;
import org.siemac.metamac.srm.core.concept.domain.ConceptScheme;


public class ConceptsDoMocks {
    
    public static ConceptScheme createConceptScheme() {
        ConceptScheme conceptScheme = new ConceptScheme();
        
        conceptScheme.setItemScheme(BaseDoMocks.createItemScheme());
        
        return conceptScheme;
        
    }
    
    public static ConceptScheme createConceptSchemeWithoutItemScheme() {
        ConceptScheme conceptScheme = new ConceptScheme();
        return conceptScheme;
    }
    
    public static ConceptScheme createConceptSchemeWithoutIdLogic() {
        ConceptScheme conceptScheme = new ConceptScheme();
        
        conceptScheme.setItemScheme(BaseDoMocks.createItemSchemeWithoutIdLogic());
        
        return conceptScheme;
    }
    
    public static ConceptScheme createConceptSchemeWithoutMantainer() {
        ConceptScheme conceptScheme = new ConceptScheme();
        
        conceptScheme.setItemScheme(BaseDoMocks.createItemSchemeWithoutMantainer());
        
        return conceptScheme;
    }
    
    public static ConceptScheme createConceptSchemeWithoutName() {
        ConceptScheme conceptScheme = new ConceptScheme();
        
        conceptScheme.setItemScheme(BaseDoMocks.createItemSchemeWithoutName());
        
        return conceptScheme;
    }
    
    public static ConceptScheme createConceptSchemeWithoutVersionLogic() {
        ConceptScheme conceptScheme = new ConceptScheme();
        
        conceptScheme.setItemScheme(BaseDoMocks.createItemSchemeWithoutVersionLogic());
        
        return conceptScheme;
    }


}
