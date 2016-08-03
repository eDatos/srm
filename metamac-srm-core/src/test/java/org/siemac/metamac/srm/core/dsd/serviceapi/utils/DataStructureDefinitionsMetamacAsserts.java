package org.siemac.metamac.srm.core.dsd.serviceapi.utils;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.siemac.metamac.srm.core.base.utils.BaseAsserts;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.domain.DimensionOrder;
import org.siemac.metamac.srm.core.dsd.domain.DimensionVisualisationInfo;
import org.siemac.metamac.srm.core.dsd.domain.MeasureDimensionPrecision;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacBasicDto;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.dsd.dto.DimensionVisualisationInfoDto;
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

        assertDimensionVisualizationInfoEntities(expected.getDimensionVisualisationInfos(), actual.getDimensionVisualisationInfos());

        // Sdmx
        DataStructureDefinitionAsserts.assertEqualsDataStructureDefinition(expected, actual);
    }

    public static void assertEqualsDataStructureDefinition(DataStructureDefinitionVersionMetamac expected, DataStructureDefinitionMetamacBasicDto actual) {
        BaseAsserts.assertEqualsStructureBasicDto(expected, expected.getLifeCycleMetadata(), actual);
    }

    public static void assertEqualsDataStructureDefinition(DataStructureDefinitionMetamacDto dto, DataStructureDefinitionVersionMetamac entity, MapperEnum mapperEnum) {

        // Metamac
        assertEquals(dto.getAutoOpen(), entity.getAutoOpen());
        assertEquals(dto.getShowDecimals(), entity.getShowDecimals());
        BaseAsserts.assertEqualsExternalItemStatisticalOperations(entity.getStatisticalOperation(), dto.getStatisticalOperation(), mapperEnum);

        assertDimensionOrders(dto.getHeadingDimensions(), entity.getHeadingDimensions());
        assertDimensionOrders(dto.getStubDimensions(), entity.getStubDimensions());

        assertDecimalPrecisions(dto.getShowDecimalsPrecisions(), entity.getShowDecimalsPrecisions());

        assertDimensionVisualizationInfo(dto.getDimensionVisualisationInfos(), entity.getDimensionVisualisationInfos());

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

    private static void assertDimensionVisualizationInfo(List<DimensionVisualisationInfoDto> expected, List<DimensionVisualisationInfo> actual) {
        for (int i = 0; i < expected.size(); i++) {
            assertDimensionVisualizationInfo(expected.get(i), actual.get(i));
        }
    }

    private static void assertDimensionVisualizationInfo(DimensionVisualisationInfoDto expected, DimensionVisualisationInfo actual) {
        assertEquals(expected.getDisplayOrder().getUrn(), actual.getDimension().getUrn());
        assertEquals(expected.getHierarchyLevelsOpen().getUrn(), actual.getHierarchyLevelsOpen().getNameableArtefact().getUrn());
    }

    private static void assertDimensionVisualizationInfoEntities(List<DimensionVisualisationInfo> expected, List<DimensionVisualisationInfo> actual) {
        for (int i = 0; i < expected.size(); i++) {
            assertDimensionVisualizationInfoEntity(expected.get(i), actual.get(i));
        }
    }

    private static void assertDimensionVisualizationInfoEntity(DimensionVisualisationInfo expected, DimensionVisualisationInfo actual) {
        assertEquals(expected.getDimension().getUrn(), actual.getDimension().getUrn());
        assertEquals(expected.getHierarchyLevelsOpen().getNameableArtefact().getUrn(), actual.getHierarchyLevelsOpen().getNameableArtefact().getUrn());
        assertEquals(expected.getDisplayOrder().getNameableArtefact().getUrn(), actual.getDisplayOrder().getNameableArtefact().getUrn());
    }
}
