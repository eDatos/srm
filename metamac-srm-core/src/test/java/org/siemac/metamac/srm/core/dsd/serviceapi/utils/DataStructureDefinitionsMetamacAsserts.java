package org.siemac.metamac.srm.core.dsd.serviceapi.utils;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.domain.DimensionOrder;
import org.siemac.metamac.srm.core.dsd.domain.MeasureDimensionPrecision;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.dsd.dto.MeasureDimensionPrecisionDto;

import com.arte.statistic.sdmx.srm.core.structure.domain.DataStructureDefinitionVersion;
import com.arte.statistic.sdmx.srm.core.structure.serviceapi.utils.DataStructureDefinitionAsserts;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

public class DataStructureDefinitionsMetamacAsserts extends DataStructureDefinitionAsserts {

    public static void assertEqualsDataStructureDefinition(DataStructureDefinitionVersion expected, DataStructureDefinitionVersion actual) {

        // Metamac

        // Sdmx
        DataStructureDefinitionAsserts.assertEqualsDataStructureDefinition(expected, actual);
    }

    public static void assertEqualsDataStructureDefinition(DataStructureDefinitionMetamacDto expected, DataStructureDefinitionVersionMetamac actual) {

        // Metamac
        assertEquals(expected.getAutoOpen(), actual.getAutoOpen());
        assertEquals(expected.getShowDecimals(), actual.getShowDecimals());

        assertDimensionOrders(expected.getHeadingDimensions(), actual.getHeadingDimensions());
        assertDimensionOrders(expected.getStubDimensions(), actual.getStubDimensions());

        assertDecimalPrecisions(expected.getShowDecimalsPrecisions(), actual.getShowDecimalsPrecisions());

        // Sdmx
        // DataStructureDefinitionAsserts.assertEqualsDataStructureDefinition(expected, actual);

    }
    private static void assertDimensionOrders(List<RelatedResourceDto> expected, List<DimensionOrder> actual) {
        for (int i = 0; i < expected.size(); i++) {
            assertDimensionOrder(expected.get(i), actual.get(i), i + 1);
        }
    }

    private static void assertDimensionOrder(RelatedResourceDto expected, DimensionOrder actual, Integer i) {
        assertEquals(expected.getUrn(), actual.getDimension().getUrn());
        assertEquals(i, actual.getDimOrder());
    }

    private static void assertDecimalPrecisions(List<MeasureDimensionPrecisionDto> expected, List<MeasureDimensionPrecision> actual) {
        for (int i = 0; i < expected.size(); i++) {
            assertDecimalPrecision(expected.get(i), actual.get(i), i + 1);
        }
    }

    private static void assertDecimalPrecision(MeasureDimensionPrecisionDto expected, MeasureDimensionPrecision actual, Integer i) {
        assertEquals(expected.getUrn(), actual.getConcept().getNameableArtefact().getUrn());
        assertEquals(expected.getShowDecimalPrecision(), actual.getShowDecimalPrecision());
    }
}
