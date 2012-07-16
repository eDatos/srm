package org.siemac.metamac.srm.core.service.utils;

import org.apache.commons.lang.RandomStringUtils;
import org.siemac.metamac.srm.core.structure.domain.AttributeDescriptor;
import org.siemac.metamac.srm.core.structure.domain.DataStructureDefinition;
import org.siemac.metamac.srm.core.structure.domain.DataflowDefinition;
import org.siemac.metamac.srm.core.structure.domain.DimensionDescriptor;
import org.siemac.metamac.srm.core.structure.domain.MeasureDescriptor;
import org.siemac.metamac.srm.core.structure.domain.PrimaryMeasure;
import org.siemac.metamac.srm.core.structure.domain.TimeDimension;

public class SdmxToolsServer {

    static final String timeDimensionID         = "TIME_PERIOD";
    static final String primaryMeasureID        = "OBS_VALUE";
    static final String measureListID           = "MeasureDescriptor";
    static final String dimensionListID         = "DimensionDescriptor";
    static final String attributeListID         = "AttributeDescriptor";
    static final String reportingYearStartDayID = "REPORTING_YEAR_START_DAY";

    public static String checkIfFixedId(Object element) {

        if (element instanceof TimeDimension) {
            return timeDimensionID;
        } else if (element instanceof PrimaryMeasure) {
            return primaryMeasureID;
        } else if (element instanceof MeasureDescriptor) {
            return measureListID;
        } else if (element instanceof DimensionDescriptor) {
            return dimensionListID;
        } else if (element instanceof AttributeDescriptor) {
            return attributeListID;
        } else if (element instanceof AttributeDescriptor) {
            return reportingYearStartDayID;
        }

        return null;

    }

    public static String generateIdForSDMXArtefact(Object element) {
        StringBuilder aux = new StringBuilder();

        // if (element instanceof Category) {
        // aux.append(SdmxAcronimEnum.get(Category.class.getSimpleName()).getNombre());
        // } else if (element instanceof Code) {
        // aux.append(SdmxAcronimEnum.get(Code.class.getSimpleName()).getNombre());
        // } else if (element instanceof Concept) {
        // aux.append(SdmxAcronimEnum.get(Concept.class.getSimpleName()).getNombre());
        // } else if (element instanceof Agency) {
        // aux.append(SdmxAcronimEnum.get(Agency.class.getSimpleName()).getNombre());
        // } else if (element instanceof DataConsumer) {
        // aux.append(SdmxAcronimEnum.get(DataConsumer.class.getSimpleName()).getNombre());
        // } else if (element instanceof DataProvider) {
        // aux.append(SdmxAcronimEnum.get(DataProvider.class.getSimpleName()).getNombre());
        // } else if (element instanceof OrganisationUnit) {
        // aux.append(SdmxAcronimEnum.get(OrganisationUnit.class.getSimpleName()).getNombre());
        // } else if (element instanceof ReportingCategory) {
        // aux.append(SdmxAcronimEnum.get(ReportingCategory.class.getSimpleName()).getNombre());
        // } else if (element instanceof Categorisation) {
        // aux.append(SdmxAcronimEnum.get(Categorisation.class.getSimpleName()).getNombre());
        // } else if (element instanceof CategoryScheme) {
        // aux.append(SdmxAcronimEnum.get(CategoryScheme.class.getSimpleName()).getNombre());
        // } else if (element instanceof CodeList) {
        // aux.append(SdmxAcronimEnum.get(CodeList.class.getSimpleName()).getNombre());
        // } else if (element instanceof ConceptScheme) {
        // aux.append(SdmxAcronimEnum.get(ConceptScheme.class.getSimpleName()).getNombre());
        // } else if (element instanceof AgencyScheme) {
        // aux.append(SdmxAcronimEnum.get(AgencyScheme.class.getSimpleName()).getNombre());
        // } else if (element instanceof DataConsumerScheme) {
        // aux.append(SdmxAcronimEnum.get(DataConsumerScheme.class.getSimpleName()).getNombre());
        // } else if (element instanceof DataProviderScheme) {
        // aux.append(SdmxAcronimEnum.get(DataProviderScheme.class.getSimpleName()).getNombre());
        // } else if (element instanceof OrganisationUnitScheme) {
        // aux.append(SdmxAcronimEnum.get(OrganisationUnitScheme.class.getSimpleName()).getNombre());
        // } else if (element instanceof ReportingTaxonomy) {
        // aux.append(SdmxAcronimEnum.get(ReportingTaxonomy.class.getSimpleName()).getNombre());
        // } else
        if (element instanceof DataStructureDefinition) {
            aux.append(SdmxAcronimEnum.get(DataStructureDefinition.class.getSimpleName()).getNombre());
        } else if (element instanceof DataflowDefinition) {
            aux.append(SdmxAcronimEnum.get(DataflowDefinition.class.getSimpleName()).getNombre());
        } else {
            aux.append("_").append((element.getClass().getSimpleName().equals("Class") ? ((Class) element).getSimpleName() : element.getClass().getSimpleName()));
        }

        aux.append("_").append(RandomStringUtils.randomNumeric(15));
        aux.append("_").append(RandomStringUtils.randomAlphabetic(15));

        return aux.toString();
    }

    /**
     * Used to check whether to delete orphan entities manually.
     * 
     * @param idOld
     * @param idNew
     * @return
     */
    public static boolean removeOld(Long idOld, Long idNew) {
        // Algorithm
        // if (idOld == null) {
        // ;
        // }
        // else {
        // if (idNew == null) {
        // return true;
        // }
        // else {
        // if (idNew == idOld) {
        // ;
        // }
        // else {
        // return true;
        // }
        // }
        // }
        if (idOld != null) {
            if (idNew == null) {
                return true;
            } else {
                if (idNew.compareTo(idOld) != 0) {
                    return true;
                }
            }
        }
        return false;
    }

}
