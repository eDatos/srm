package org.siemac.metamac.srm.core.dsd.serviceapi.utils;

import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.domain.DimensionOrder;
import org.siemac.metamac.srm.core.dsd.domain.MeasureDimensionPrecision;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;

import com.arte.statistic.sdmx.srm.core.structure.domain.DimensionComponent;
import com.arte.statistic.sdmx.srm.core.structure.serviceapi.utils.DataStructureDefinitionDoMocks;

public class DataStructureDefinitionMetamacDoMocks extends DataStructureDefinitionDoMocks {

    public static DataStructureDefinitionVersionMetamac mockDataStructureDefinitionVersionMetamac(OrganisationMetamac maintainer) {

        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = new DataStructureDefinitionVersionMetamac();

        mockDataStructureDefinition(dataStructureDefinitionVersionMetamac, maintainer);

        return dataStructureDefinitionVersionMetamac;
    }

    public static DimensionOrder mockDimensionOrder(Integer order, DimensionComponent dimension) {
        DimensionOrder dimensionOrder = new DimensionOrder();
        dimensionOrder.setDimension(dimension);
        dimensionOrder.setDimOrder(order);

        return dimensionOrder;
    }

    public static MeasureDimensionPrecision mockMeasureDimensionPrecision(Integer decimalPrecision, ConceptMetamac concept) {
        MeasureDimensionPrecision measureDimensionPrecision = new MeasureDimensionPrecision();
        measureDimensionPrecision.setConcept(concept);
        measureDimensionPrecision.setShowDecimalPrecision(decimalPrecision);

        return measureDimensionPrecision;
    }
}
