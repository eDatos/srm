package org.siemac.metamac.srm.core.dsd.serviceapi.utils;

import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.dsd.dto.MeasureDimensionPrecisionDto;

import com.arte.statistic.sdmx.srm.core.structure.serviceapi.utils.DataStructureDefinitionDtoMocks;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;

public class DataStructureDefinitionMetamacDtoMocks extends DataStructureDefinitionDtoMocks {

    public static DataStructureDefinitionMetamacDto mockDataStructureDefinitionMetamacDto() {

        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = new DataStructureDefinitionMetamacDto();
        structureDtoMock(dataStructureDefinitionMetamacDto, "DSD_01");

        // ADD Metamac metadata
        dataStructureDefinitionMetamacDto.setAutoOpen(true);
        dataStructureDefinitionMetamacDto.setShowDecimals(6);
        dataStructureDefinitionMetamacDto.setStatisticalOperation(MetamacMocks.mockExternalItemDto("urn:operation", TypeExternalArtefactsEnum.STATISTICAL_OPERATION));

        return dataStructureDefinitionMetamacDto;
    }

    public static DataStructureDefinitionMetamacDto mockHeading(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto, String... dims) {

        for (int i = 0; i < dims.length; i++) {
            dataStructureDefinitionMetamacDto.addHeadingDimension(createDimensionRelatedResourceDto(dims[i]));
        }

        return dataStructureDefinitionMetamacDto;
    }

    public static DataStructureDefinitionMetamacDto mockStub(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto, String... dims) {

        for (int i = 0; i < dims.length; i++) {
            dataStructureDefinitionMetamacDto.addStubDimension(createDimensionRelatedResourceDto(dims[i]));
        }

        return dataStructureDefinitionMetamacDto;
    }

    private static RelatedResourceDto createDimensionRelatedResourceDto(String urn) {
        RelatedResourceDto relatedResourceDto = new RelatedResourceDto();
        relatedResourceDto.setType(RelatedResourceTypeEnum.DIMENSION);
        relatedResourceDto.setUrn(urn);

        return relatedResourceDto;
    }

    public static DataStructureDefinitionMetamacDto mockShowDecimalsPrecision(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto, String... codes) {

        for (int i = 0; i < codes.length; i++) {
            dataStructureDefinitionMetamacDto.addShowDecimalsPrecision(createMeasureDimensionPrecisionDto(codes[i], (i + 1) % 6));
        }

        return dataStructureDefinitionMetamacDto;
    }

    private static MeasureDimensionPrecisionDto createMeasureDimensionPrecisionDto(String urn, Integer precision) {
        MeasureDimensionPrecisionDto measureDimensionPrecisionDto = new MeasureDimensionPrecisionDto();
        measureDimensionPrecisionDto.setType(RelatedResourceTypeEnum.CONCEPT);
        measureDimensionPrecisionDto.setUrn(urn);
        measureDimensionPrecisionDto.setShowDecimalPrecision(precision);

        return measureDimensionPrecisionDto;
    }
}
