package org.siemac.metamac.srm.core.dsd.serviceapi.utils;

import org.siemac.metamac.core.common.ent.domain.ExternalItem;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.srm.core.base.utils.BaseDoMocks;
import org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacDoMocks;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.serviceapi.utils.ConceptsMetamacDoMocks;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.domain.DimensionOrder;
import org.siemac.metamac.srm.core.dsd.domain.DimensionVisualizationInfo;
import org.siemac.metamac.srm.core.dsd.domain.MeasureDimensionPrecision;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;

import com.arte.statistic.sdmx.srm.core.structure.domain.DimensionComponent;
import com.arte.statistic.sdmx.srm.core.structure.serviceapi.utils.DataStructureDefinitionDoMocks;

public class DataStructureDefinitionMetamacDoMocks extends DataStructureDefinitionDoMocks {

    public static DataStructureDefinitionVersionMetamac mockDataStructureDefinitionVersionMetamac(OrganisationMetamac maintainer) {
        DataStructureDefinitionVersionMetamac target = new DataStructureDefinitionVersionMetamac();
        mockDataStructureDefinition(target, maintainer);
        return target;
    }

    public static DataStructureDefinitionVersionMetamac mockDataStructureDefinitionVersionMetamacFixedValues(String agencyID, String resourceID, String version) {
        DataStructureDefinitionVersionMetamac target = new DataStructureDefinitionVersionMetamac();
        target.setLifeCycleMetadata(BaseDoMocks.mockLifeCycleExternallyPublished());
        target.setStatisticalOperation(mockOperationExternalItem("operation-" + resourceID));
        target.setAutoOpen(Boolean.TRUE);
        target.setShowDecimals(Integer.valueOf(3));
        // Sdmx metadata
        mockDataStructureDefinitionFixedValues(target, agencyID, resourceID, version);
        return target;
    }

    public static DataStructureDefinitionVersionMetamac mockDataStructureDefinitionVersionMetamacFixedValuesWithComponents(String agencyID, String resourceID, String version) {
        DataStructureDefinitionVersionMetamac target = mockDataStructureDefinitionVersionMetamacFixedValues(agencyID, resourceID, version);
        mockDataStructureDefinitionFixedValuesWithComponents(target, agencyID, resourceID, version);

        target.addHeadingDimension(mockDimensionOrder(Integer.valueOf(0), mockDimensionFixedValues("dimension01")));
        target.addHeadingDimension(mockDimensionOrder(Integer.valueOf(1), mockDimensionFixedValues("dimension02")));
        target.addStubDimension(mockDimensionOrder(Integer.valueOf(0), mockDimensionFixedValues("dimension03")));
        target.addStubDimension(mockDimensionOrder(Integer.valueOf(1), mockDimensionFixedValues("dimension04")));
        target.addStubDimension(mockDimensionOrder(Integer.valueOf(2), mockDimensionFixedValues("dimension05")));

        target.addShowDecimalsPrecision(mockMeasureDimensionPrecision(Integer.valueOf(2), ConceptsMetamacDoMocks.mockConceptFixedValues("concept01", null, null)));
        target.addShowDecimalsPrecision(mockMeasureDimensionPrecision(Integer.valueOf(5), ConceptsMetamacDoMocks.mockConceptFixedValues("concept02", null, null)));

        return target;
    }

    public static DimensionOrder mockDimensionOrder(Integer order, DimensionComponent dimension) {
        DimensionOrder target = new DimensionOrder();
        target.setDimension(dimension);
        target.setDimOrder(order);
        return target;
    }

    public static MeasureDimensionPrecision mockMeasureDimensionPrecision(Integer decimalPrecision, ConceptMetamac concept) {
        MeasureDimensionPrecision target = new MeasureDimensionPrecision();
        target.setConcept(concept);
        target.setShowDecimalPrecision(decimalPrecision);
        return target;
    }

    public static ExternalItem mockOperationExternalItem(String code) {
        ExternalItem target = new ExternalItem();
        target.setCode(code);
        target.setUri("/operations/" + code);
        target.setUrn("urn:" + code);
        target.setType(TypeExternalArtefactsEnum.STATISTICAL_OPERATION);
        target.setManagementAppUrl("managementAppUrl" + code);
        target.setTitle(null);
        return target;
    }

    public static DimensionVisualizationInfo mockDimensionVisualizationInfo(DimensionComponent dimension) {
        DimensionVisualizationInfo target = new DimensionVisualizationInfo();
        target.setDimension(dimension);
        target.setDisplayOrder(CodesMetamacDoMocks.mockCodelistOrderVisualisation());
        target.setHierarchyLevelsOpen(CodesMetamacDoMocks.mockCodelistOpennessVisualisation());
        return target;
    }
}
