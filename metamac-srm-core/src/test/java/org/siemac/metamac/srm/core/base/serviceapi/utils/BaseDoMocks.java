package org.siemac.metamac.srm.core.base.serviceapi.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.joda.time.DateTime;
import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.core.common.ent.domain.ExternalItem;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.util.GeneratorUrnUtils;
import org.siemac.metamac.core.common.util.shared.VersionUtil;
import org.siemac.metamac.srm.core.base.domain.ComponentList;
import org.siemac.metamac.srm.core.base.domain.ItemScheme;
import org.siemac.metamac.srm.core.base.domain.ItemSchemeVersion;
import org.siemac.metamac.srm.core.base.domain.MaintainableArtefact;
import org.siemac.metamac.srm.core.structure.domain.Dimension;
import org.siemac.metamac.srm.core.structure.domain.DimensionDescriptor;
import org.siemac.metamac.srm.core.structure.domain.MeasureDescriptor;
import org.siemac.metamac.srm.core.structure.domain.MeasureDimension;


public class BaseDoMocks {
    
    private static final String AGENCY_MOCK = "AgencyMock";
    private static final String AGENCY_SCHEME_MOCK = "AgencySchemeMock";
    private static final String CONCEPT_SCHEME_MOCK = "ConceptSchemeMock";
    private static final String CONCEPT_MOCK = "ConceptMock";
    
    private static final String URI_MOCK = "lorem/ipsum/dolor/sit/amet";

    // TODO completar los comentados 
    
    // -----------------------------------------------------------------
    // ITEM SCHEME
    // -----------------------------------------------------------------

    public static void fillItemSchemeToCreate(ItemSchemeVersion itemSchemeVersion) {
        itemSchemeVersion.setItemScheme(new ItemScheme());
        String idLogic = "conceptScheme-" + MetamacMocks.mockString(10);
        itemSchemeVersion.setMaintainableArtefact(mockMaintainableArtefact(idLogic, "sdmx/v2.1/conceptScheme/" + idLogic));
    }
    
    public static MaintainableArtefact mockMaintainableArtefact(String idLogic, String uri) {

        MaintainableArtefact maintainableArtefact = new MaintainableArtefact();
        
        // IdentifiableArtefact
        maintainableArtefact.setIdLogic(idLogic);
        maintainableArtefact.setUri(uri);
        maintainableArtefact.setReplacedBy(null);

        // NameableArtefact
        maintainableArtefact.setName(mockInternationalString());
        maintainableArtefact.setDescription(mockInternationalString());

        // MaintainableArtefact
        maintainableArtefact.setFinalLogic(Boolean.FALSE);
        maintainableArtefact.setIsExternalReference(Boolean.FALSE);
        maintainableArtefact.setStructureURL(null);
        maintainableArtefact.setServiceURL(null);

        maintainableArtefact.setMaintainer(mockAgencyExternalItem());
        
        return maintainableArtefact;
    }
//    
//    public static ItemScheme createItemSchemeFinal() {
//        ItemScheme itemScheme = createItemScheme();
//
//        itemScheme.setFinalLogic(Boolean.TRUE);
//        
//        return itemScheme;
//    }
    
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

    public static ExternalItem mockAgencyExternalItem() {
        return new ExternalItem(AGENCY_MOCK, URI_MOCK, mockAgencyUrn(), TypeExternalArtefactsEnum.AGENCY);
    }

    public static ExternalItem mockConceptExternalItem() {
        return new ExternalItem(CONCEPT_MOCK, URI_MOCK, mockConceptUrn(), TypeExternalArtefactsEnum.CONCEPT);
    }
    
    public static String mockAgencyUrn() {
        return GeneratorUrnUtils.generateSdmxAgencyUrn(AGENCY_MOCK, AGENCY_SCHEME_MOCK, VersionUtil.VERSION_LOGIC_INITIAL_VERSION, AGENCY_MOCK);
    }
    
    public static String mockConceptUrn() {
        return GeneratorUrnUtils.generateSdmxConceptUrn(AGENCY_MOCK, CONCEPT_SCHEME_MOCK, VersionUtil.VERSION_LOGIC_INITIAL_VERSION, CONCEPT_MOCK);
    }    
    
    // -----------------------------------------------------------------
    // COMPONENT
    // -----------------------------------------------------------------
    
    public static List<Dimension> createDimension() {

        // List<Concept> concepts = sdmxBaseService.findAllConcepts(getServiceContext());

        Dimension dimension1 = new Dimension();
        dimension1.setIdLogic(RandomStringUtils.random(50, true, true));

        // Required
        dimension1.setUri(RandomStringUtils.random(50, true, true));
        dimension1.setCptIdRef(BaseDoMocks.mockConceptExternalItem());
        dimension1.setOrderLogic(1);

        // Some Auditory
        dimension1.setCreatedBy("Junit");
        dimension1.setCreatedDate(new DateTime());

        Dimension dimension2 = new Dimension();
        dimension2.setIdLogic(RandomStringUtils.random(50, true, true));

        // Required
        dimension2.setUri(RandomStringUtils.random(50, true, true));
        dimension2.setCptIdRef(BaseDoMocks.mockConceptExternalItem());
        dimension2.setOrderLogic(1);

        // Some Auditory
        dimension2.setCreatedBy("Junit");
        dimension2.setCreatedDate(new DateTime());

        List<Dimension> dimensions = new ArrayList<Dimension>();
        dimensions.add(dimension2);
        dimensions.add(dimension2);

        return dimensions;
    }
    
    public static MeasureDimension createMeasureDimension() {
        MeasureDimension measureDimension = new MeasureDimension();
        measureDimension.setIdLogic(RandomStringUtils.random(50, true, true));
        
        // Required
        measureDimension.setUri(RandomStringUtils.random(50, true, true));
        measureDimension.setCptIdRef(BaseDoMocks.mockConceptExternalItem());
        measureDimension.setOrderLogic(1);
        
        // Some Auditory
        measureDimension.setCreatedBy("Junit");
        measureDimension.setCreatedDate(new DateTime());

        return measureDimension;
    }

    // -----------------------------------------------------------------
    // COMPONENTLIST
    // -----------------------------------------------------------------
    
    public static ComponentList createDimensionDescriptor() {
        DimensionDescriptor dimensionDescriptor = new DimensionDescriptor();
        dimensionDescriptor.setIdLogic(RandomStringUtils.random(50, true, true));
        // dimensionDescriptor.getComponents().addAll(createDimension()); No CASCADE !!!!
        // Required
        dimensionDescriptor.setUri(RandomStringUtils.random(50, true, true));

        return dimensionDescriptor;
    }
    
    public static ComponentList createMeasureDimensionDescriptor() {
        MeasureDescriptor measureDescriptor = new MeasureDescriptor();
        measureDescriptor.setIdLogic(RandomStringUtils.random(50, true, true));
        // dimensionDescriptor.getComponents().addAll(createDimension()); No CASCADE !!!!
        // Required
        measureDescriptor.setUri(RandomStringUtils.random(50, true, true));

        return measureDescriptor;
    }

}
