package org.siemac.metamac.srm.core.service.utils;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.siemac.metamac.srm.core.structure.domain.DataStructureDefinition;
import org.siemac.metamac.srm.core.structure.domain.DataflowDefinition;

public enum SdmxAcronimEnum {

    // CATEGORY("CTG", Category.class.getSimpleName()),
    // CODE("COD", Code.class.getSimpleName()),
    // CONCEPT("CPT", Concept.class.getSimpleName()),
    // AGENCY("AGY", Agency.class.getSimpleName()),
    // DATA_CONSUMER("DC", DataConsumer.class.getSimpleName()),
    // DATA_PROVIDER("DP", DataProvider.class.getSimpleName()),
    // ORGANISATION_UNIT("OU", OrganisationUnit.class.getSimpleName()),
    // REPORTING_CATEGORY("R-CTG", ReportingCategory.class.getSimpleName()),
    // CATEGORISATION("CTGS", Categorisation.class.getSimpleName()),
    // CATEGORY_SCHEME("CTG-S", CategoryScheme.class.getSimpleName()),
    // CODELIST("CL", CodeList.class.getSimpleName()),
    // CONCEPT_SCHEME("CPT-S", ConceptScheme.class.getSimpleName()),
    // AGENCY_SCHEME("AS", AgencyScheme.class.getSimpleName()),
    // DATA_CONSUMER_SCHEME("DCS", DataConsumerScheme.class.getSimpleName()),
    // DATA_PROVIDER_SCHEME("DPS", DataProviderScheme.class.getSimpleName()),
    // ORGANISATION_UNIT_SCHEME("OUS", OrganisationUnitScheme.class.getSimpleName()),
    // REPORTING_TAXONOMY("RT", ReportingTaxonomy.class.getSimpleName()),
    DATA_STRUCTURE_DEFINITION("DSD", DataStructureDefinition.class.getSimpleName()),
    DATA_FLOW_DEFINITION("DFD", DataflowDefinition.class.getSimpleName());

    private String                                    nombre;
    private String                                    classSimpleName;

    private static final Map<String, SdmxAcronimEnum> lookup = new HashMap<String, SdmxAcronimEnum>();

    static {
        for (SdmxAcronimEnum s : EnumSet.allOf(SdmxAcronimEnum.class))
            lookup.put(s.getClassSimpleName(), s);
    }

    public static SdmxAcronimEnum get(String campo) {
        return lookup.get(campo);
    }

    public String getNombre() {
        return nombre;
    }

    public String getClassSimpleName() {
        return classSimpleName;
    }

    private SdmxAcronimEnum(String nombre, String classSimpleName) {
        this.nombre = nombre;
        this.classSimpleName = classSimpleName;
    }

}
