package org.siemac.metamac.srm.core.base.serviceapi.utils;

import org.joda.time.DateTime;
import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.srm.core.base.domain.ItemScheme;
import org.siemac.metamac.srm.core.utils.SrmDoMocks;

public class BaseDoMocks {

    
    // -----------------------------------------------------------------
    // ITEM SCHEME
    // -----------------------------------------------------------------

    public static ItemScheme createItemScheme() {
        ItemScheme itemScheme = new ItemScheme();

        // IdentifiableArtefact
        String code = MetamacMocks.mockString(10);
        itemScheme.setIdLogic("conceptScheme-" + code);
        itemScheme.setUri("uri:urn:" + code);
        itemScheme.setUrn("http://sdmx/v2.1/conceptScheme/" + code);
        itemScheme.setReplacedBy(null);

        // NameableArtefact
        itemScheme.setName(SrmDoMocks.mockInternationalString());
        itemScheme.setDescription(SrmDoMocks.mockInternationalString());

        // MaintainableArtefact
        itemScheme.setVersionLogic("v1.0");
        itemScheme.setValidFrom(new DateTime());
        itemScheme.setValidTo(null);
        itemScheme.setFinalLogic(Boolean.FALSE);
        itemScheme.setIsExternalReference(Boolean.FALSE);
        itemScheme.setStructureURL(null);
        itemScheme.setServiceURL(null);

        itemScheme.setMaintainer(SrmDoMocks.mockAgencyExternalItemBt());

        return itemScheme;
    }
    
    public static ItemScheme createItemSchemeWithoutIdLogic() {
        ItemScheme itemScheme = createItemScheme();

        itemScheme.setIdLogic(null);
        
        return itemScheme;
    }
    
    public static ItemScheme createItemSchemeWithoutName() {
        ItemScheme itemScheme = createItemScheme();

        itemScheme.setName(null);
        
        return itemScheme;
    }
    
    public static ItemScheme createItemSchemeWithoutVersionLogic() {
        ItemScheme itemScheme = createItemScheme();

        itemScheme.setVersionLogic(null);
        
        return itemScheme;
    }
    
    public static ItemScheme createItemSchemeFinal() {
        ItemScheme itemScheme = createItemScheme();

        itemScheme.setFinalLogic(Boolean.TRUE);
        
        return itemScheme;
    }
    
    public static ItemScheme createItemSchemeWithoutMantainer() {
        ItemScheme itemScheme = createItemScheme();

        itemScheme.setMaintainer(null);
        
        return itemScheme;
    }

}
