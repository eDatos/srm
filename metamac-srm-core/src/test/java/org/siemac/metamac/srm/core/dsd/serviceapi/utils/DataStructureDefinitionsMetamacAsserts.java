package org.siemac.metamac.srm.core.dsd.serviceapi.utils;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.domain.DimensionOrder;
import org.siemac.metamac.srm.core.dsd.domain.MeasureDimensionPrecision;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.dsd.dto.MeasureDimensionPrecisionDto;

import com.arte.statistic.sdmx.srm.core.structure.serviceapi.utils.DataStructureDefinitionAsserts;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

public class DataStructureDefinitionsMetamacAsserts extends DataStructureDefinitionAsserts {

    public static void assertEqualsDataStructureDefinition(DataStructureDefinitionVersionMetamac expected, DataStructureDefinitionVersionMetamac actual) {

        // Metamac
        assertEquals(expected.getAutoOpen(), actual.getAutoOpen());
        assertEquals(expected.getShowDecimals(), actual.getShowDecimals());
        assertEqualsExternalItem(expected.getStatisticalOperation(), actual.getStatisticalOperation());

        assertDimensionOrdersEntity(expected.getHeadingDimensions(), actual.getHeadingDimensions());
        assertDimensionOrdersEntity(expected.getStubDimensions(), actual.getStubDimensions());

        assertDecimalPrecisionsEntity(expected.getShowDecimalsPrecisions(), actual.getShowDecimalsPrecisions());

        // Sdmx
        DataStructureDefinitionAsserts.assertEqualsDataStructureDefinition(expected, actual);
    }

    public static void assertEqualsDataStructureDefinition(DataStructureDefinitionMetamacDto expected, DataStructureDefinitionVersionMetamac actual) {

        // Metamac
        assertEquals(expected.getAutoOpen(), actual.getAutoOpen());
        assertEquals(expected.getShowDecimals(), actual.getShowDecimals());
        assertEqualsExternalItem(expected.getStatisticalOperation(), actual.getStatisticalOperation());

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

    private static void assertDimensionOrdersEntity(List<DimensionOrder> expected, List<DimensionOrder> actual) {
        for (int i = 0; i < expected.size(); i++) {
            assertDimensionOrder(expected.get(i), actual.get(i), i + 1);
        }
    }

    private static void assertDimensionOrder(DimensionOrder expected, DimensionOrder actual, Integer i) {
        assertEquals(expected.getDimension().getUrn(), actual.getDimension().getUrn());
        assertEquals(i, actual.getDimOrder());
    }

    private static void assertDecimalPrecisionsEntity(List<MeasureDimensionPrecision> expected, List<MeasureDimensionPrecision> actual) {
        for (int i = 0; i < expected.size(); i++) {
            assertDecimalPrecision(expected.get(i), actual.get(i), i + 1);
        }
    }

    private static void assertDecimalPrecision(MeasureDimensionPrecision expected, MeasureDimensionPrecision actual, Integer i) {
        assertEquals(expected.getConcept().getNameableArtefact().getUrn(), actual.getConcept().getNameableArtefact().getUrn());
        assertEquals(expected.getShowDecimalPrecision(), actual.getShowDecimalPrecision());
    }
}
