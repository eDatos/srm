package org.siemac.metamac.srm.rest.internal.v1_0.dsd.utils;

import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.serviceapi.utils.DataStructureDefinitionMetamacDoMocks;

import com.arte.statistic.sdmx.srm.core.base.domain.Annotation;
import com.arte.statistic.sdmx.srm.core.base.domain.Component;
import com.arte.statistic.sdmx.srm.core.base.domain.ComponentList;
import com.arte.statistic.sdmx.srm.core.base.domain.IdentifiableArtefact;

public class DataStructuresDoMocks {

    public static DataStructureDefinitionVersionMetamac mockDataStructure(String agencyID, String resourceID, String version) {
        DataStructureDefinitionVersionMetamac target = DataStructureDefinitionMetamacDoMocks.mockDataStructureDefinitionVersionMetamacFixedValues(agencyID, resourceID, version);
        clearAnnotationsRemainingOne(target);
        return target;
    }

    public static DataStructureDefinitionVersionMetamac mockDataStructureWithComponents(String agencyID, String resourceID, String version) {
        DataStructureDefinitionVersionMetamac target = DataStructureDefinitionMetamacDoMocks.mockDataStructureDefinitionVersionMetamacFixedValuesWithComponents(agencyID, resourceID, version);
        clearAnnotationsRemainingOne(target);
        return target;
    }

    private static void clearAnnotationsRemainingOne(DataStructureDefinitionVersionMetamac source) {
        Annotation annotation = source.getMaintainableArtefact().getAnnotations().iterator().next();
        source.getMaintainableArtefact().getAnnotations().clear();
        source.getMaintainableArtefact().addAnnotation(annotation);

        for (ComponentList componentList : source.getGrouping()) {
            clearAnnotationsRemainingOne(componentList);
        }
    }

    private static void clearAnnotationsRemainingOne(ComponentList source) {
        clearAnnotationsIdentifiableArtefactRemainingOne(source);
        for (Component component : source.getComponents()) {
            clearAnnotationsIdentifiableArtefactRemainingOne(component);
        }
    }

    private static void clearAnnotationsIdentifiableArtefactRemainingOne(IdentifiableArtefact source) {
        Annotation annotation = null;
        for (Annotation annotationSource : source.getAnnotations()) {
            if ("code1".equals(annotationSource.getCode()) && "title-code1".equals(annotationSource.getTitle())) {
                annotation = annotationSource;
            }
        }
        source.getAnnotations().clear();
        if (annotation != null) {
            source.addAnnotation(annotation);
        }
    }

}