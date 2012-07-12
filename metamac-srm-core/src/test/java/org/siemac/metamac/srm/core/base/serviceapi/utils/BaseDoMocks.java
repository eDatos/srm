package org.siemac.metamac.srm.core.base.serviceapi.utils;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.core.common.ent.domain.ExternalItem;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.util.GeneratorUrnUtils;
import org.siemac.metamac.core.common.util.shared.VersionUtil;
import org.siemac.metamac.srm.core.base.domain.ItemScheme;

public class BaseDoMocks {
    
    private static final String AGENCY_MOCK = "AgencyMock";
    private static final String AGENCY_SCHEME_MOCK = "AgencySchemeMock";
    private static final String CONCEPT_SCHEME_MOCK = "ConceptSchemeMock";
    private static final String CONCEPT_MOCK = "ConceptMock";

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

    public static ExternalItem mockAgencyExternalItemBt() {
        return new ExternalItem(AGENCY_MOCK, StringUtils.EMPTY, mockAgencyUrn(), TypeExternalArtefactsEnum.AGENCY);
    }

    public static ExternalItem mockConceptExternalItem() {
        return new ExternalItem(CONCEPT_MOCK, StringUtils.EMPTY, mockConceptUrn(), TypeExternalArtefactsEnum.CONCEPT);
    }
    
    public static String mockAgencyUrn() {
        return GeneratorUrnUtils.generateSdmxAgencyUrn(AGENCY_MOCK, AGENCY_SCHEME_MOCK, VersionUtil.VERSION_LOGIC, AGENCY_MOCK);
    }
    
    public static String mockConceptUrn() {
        return GeneratorUrnUtils.generateSdmxConceptUrn(AGENCY_MOCK, CONCEPT_SCHEME_MOCK, VersionUtil.VERSION_LOGIC, CONCEPT_MOCK);
    }    

}
