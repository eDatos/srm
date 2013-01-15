package org.siemac.metamac.srm.core.dsd.serviceapi.utils;

import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacDoMocks;
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

        // Metamac
        dataStructureDefinitionVersionMetamac.setAutoOpen(true);
        dataStructureDefinitionVersionMetamac.setShowDecimals(2);

        dataStructureDefinitionVersionMetamac.addHeadingDimension(mockDimensionOrder(1, mockDimension()));
        dataStructureDefinitionVersionMetamac.addHeadingDimension(mockDimensionOrder(2, mockTimeDimension()));

        dataStructureDefinitionVersionMetamac.addStubDimension(mockDimensionOrder(1, mockMeasureDimension()));

        dataStructureDefinitionVersionMetamac.addShowDecimalsPrecision(mockMeasureDimensionPrecision(5, CodesMetamacDoMocks.mockCode()));
        dataStructureDefinitionVersionMetamac.addShowDecimalsPrecision(mockMeasureDimensionPrecision(4, CodesMetamacDoMocks.mockCode()));

        return dataStructureDefinitionVersionMetamac;
    }

    public static DimensionOrder mockDimensionOrder(Integer order, DimensionComponent dimension) {
        DimensionOrder dimensionOrder = new DimensionOrder();
        dimensionOrder.setDimension(dimension);
        dimensionOrder.setDimOrder(order);

        return dimensionOrder;
    }

    public static MeasureDimensionPrecision mockMeasureDimensionPrecision(Integer decimalPrecision, CodeMetamac code) {
        MeasureDimensionPrecision measureDimensionPrecision = new MeasureDimensionPrecision();
        measureDimensionPrecision.setCode(code);
        measureDimensionPrecision.setShowDecimalPrecision(decimalPrecision);

        return measureDimensionPrecision;
    }
}
