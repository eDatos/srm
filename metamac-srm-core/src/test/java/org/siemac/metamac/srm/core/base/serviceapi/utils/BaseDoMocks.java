package org.siemac.metamac.srm.core.base.serviceapi.utils;

import org.joda.time.DateTime;
import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.core.common.bt.domain.ExternalItemBt;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.srm.core.base.domain.ItemScheme;

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
        itemScheme.setName(mockInternationalString());
        itemScheme.setDescription(mockInternationalString());

        // MaintainableArtefact
        itemScheme.setVersionLogic("v1.0");
        itemScheme.setValidFrom(new DateTime());
        itemScheme.setValidTo(null);
        itemScheme.setFinalLogic(Boolean.FALSE);
        itemScheme.setIsExternalReference(Boolean.FALSE);
        itemScheme.setStructureURL(null);
        itemScheme.setServiceURL(null);

        itemScheme.setMaintainer(mockAgencyExternalItemBt());

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
    
 // -----------------------------------------------------------------
    // INTERNATIONAL STRING
    // -----------------------------------------------------------------

    public static InternationalString mockInternationalString() {
        InternationalString internationalString = new InternationalString();
        LocalisedString es = new LocalisedString();
        es.setLabel(MetamacMocks.mockString(10) + " en Español");
        es.setLocale("es");
        LocalisedString en = new LocalisedString();
        en.setLabel(MetamacMocks.mockString(10) + " in English");
        en.setLocale("en");
        internationalString.addText(es);
        internationalString.addText(en);
        return internationalString;
    }

    // -----------------------------------------------------------------
    // EXTERNAL ITEM
    // -----------------------------------------------------------------

    public static ExternalItemBt mockAgencyExternalItemBt() {
        String code = MetamacMocks.mockString(10);
        return new ExternalItemBt("http://sdmx/v2.1/agency/" + code, code, TypeExternalArtefactsEnum.AGENCY);
    }

    public static ExternalItemBt mockConceptExternalItemBt() {
        String conceptScheme = MetamacMocks.mockString(10);
        String code = MetamacMocks.mockString(10);
        return new ExternalItemBt("http://sdmx/v2.1/concept/" + conceptScheme + "/" + code, code, TypeExternalArtefactsEnum.CONCEPT);
    }

}
