package com.arte.statistic.sdmx.srm.core.base.serviceapi.utils;

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

import com.arte.statistic.sdmx.srm.core.base.domain.ComponentList;
import com.arte.statistic.sdmx.srm.core.base.domain.EnumeratedRepresentation;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemScheme;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.Representation;
import com.arte.statistic.sdmx.srm.core.structure.domain.Dimension;
import com.arte.statistic.sdmx.srm.core.structure.domain.DimensionDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.MeasureDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.MeasureDimension;

public class BaseDoMocks {

    private static final String AGENCY_MOCK         = "AgencyMock";
    private static final String AGENCY_SCHEME_MOCK  = "AgencySchemeMock";
    private static final String CONCEPT_SCHEME_MOCK = "ConceptSchemeMock";
    private static final String CONCEPT_MOCK        = "ConceptMock";

    public static final String  URI_MOCK            = "lorem/ipsum/dolor/sit/amet";

    // -----------------------------------------------------------------
    // ITEM SCHEME
    // -----------------------------------------------------------------

    public static void fillItemSchemeToCreate(ItemSchemeVersion itemSchemeVersion) {
        itemSchemeVersion.setItemScheme(new ItemScheme());
        itemSchemeVersion.setIsPartial(Boolean.FALSE);
        String code = "itemScheme-" + MetamacMocks.mockString(10);
        itemSchemeVersion.setMaintainableArtefact(mockMaintainableArtefact(code, "sdmx/v2.1/itemScheme/" + code));
    }

    public static MaintainableArtefact mockMaintainableArtefact(String code, String uri) {

        MaintainableArtefact maintainableArtefact = new MaintainableArtefact();

        // IdentifiableArtefact
        maintainableArtefact.setCode(code);

        // NameableArtefact
        maintainableArtefact.setName(mockInternationalString());
        maintainableArtefact.setDescription(mockInternationalString());

        // MaintainableArtefact
        maintainableArtefact.setIsExternalReference(Boolean.FALSE);
        maintainableArtefact.setStructureURL(null);
        maintainableArtefact.setServiceURL(null);
        maintainableArtefact.setMaintainer(mockAgencyExternalItem());

        return maintainableArtefact;
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

    public static ExternalItem mockAgencyExternalItem() {
        return new ExternalItem(AGENCY_MOCK, URI_MOCK, mockAgencyUrn(), TypeExternalArtefactsEnum.AGENCY);
    }

    public static ExternalItem mockConceptExternalItem() {
        return new ExternalItem(CONCEPT_MOCK, URI_MOCK, mockConceptUrn(), TypeExternalArtefactsEnum.CONCEPT);
    }

    public static ExternalItem mockConceptSchemeExternalItem() {
        return new ExternalItem(CONCEPT_SCHEME_MOCK, URI_MOCK, mockConceptSchemeUrn(), TypeExternalArtefactsEnum.CONCEPT_SCHEME);
    }

    public static String mockAgencyUrn() {
        return GeneratorUrnUtils.generateSdmxAgencyUrn(AGENCY_MOCK, AGENCY_SCHEME_MOCK, VersionUtil.VERSION_INITIAL_VERSION, AGENCY_MOCK);
    }

    public static String mockConceptUrn() {
        return GeneratorUrnUtils.generateSdmxConceptUrn(AGENCY_MOCK, CONCEPT_SCHEME_MOCK, VersionUtil.VERSION_INITIAL_VERSION, CONCEPT_MOCK);
    }

    public static String mockConceptSchemeUrn() {
        return GeneratorUrnUtils.generateSdmxConceptSchemeUrn(AGENCY_MOCK, CONCEPT_SCHEME_MOCK, VersionUtil.VERSION_INITIAL_VERSION);
    }

    // -----------------------------------------------------------------
    // COMPONENT
    // -----------------------------------------------------------------

    public static List<Dimension> createDimension() {

        // List<Concept> concepts = sdmxBaseService.findAllConcepts(getServiceContext());

        Dimension dimension1 = new Dimension();
        dimension1.setCode(RandomStringUtils.random(50, true, true));

        // Required
        dimension1.setUri(RandomStringUtils.random(50, true, true));
        dimension1.setCptIdRef(BaseDoMocks.mockConceptExternalItem());
        dimension1.setOrderLogic(1);

        // Some Auditory
        dimension1.setCreatedBy("Junit");
        dimension1.setCreatedDate(new DateTime());

        Dimension dimension2 = new Dimension();
        dimension2.setCode(RandomStringUtils.random(50, true, true));

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
        measureDimension.setCode(RandomStringUtils.random(50, true, true));

        // Required
        measureDimension.setUri(RandomStringUtils.random(50, true, true));
        measureDimension.setCptIdRef(BaseDoMocks.mockConceptExternalItem());
        measureDimension.setOrderLogic(1);

        // Representation: ConceptScheme
        Representation representation = new EnumeratedRepresentation();
        ((EnumeratedRepresentation) representation).setEnumerated(BaseDoMocks.mockConceptSchemeExternalItem());
        measureDimension.setLocalRepresentation(representation);

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
        dimensionDescriptor.setCode(RandomStringUtils.random(50, true, true));
        // dimensionDescriptor.getComponents().addAll(createDimension()); No CASCADE !!!!
        // Required
        dimensionDescriptor.setUri(RandomStringUtils.random(50, true, true));

        return dimensionDescriptor;
    }

    public static ComponentList createMeasureDimensionDescriptor() {
        MeasureDescriptor measureDescriptor = new MeasureDescriptor();
        measureDescriptor.setCode(RandomStringUtils.random(50, true, true));
        // dimensionDescriptor.getComponents().addAll(createDimension()); No CASCADE !!!!
        // Required
        measureDescriptor.setUri(RandomStringUtils.random(50, true, true));

        return measureDescriptor;
    }

}
