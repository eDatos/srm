package org.siemac.metamac.srm.rest.internal.v1_0.concept.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.RelatedConcepts;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.RoleConcepts;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.rest.common.SrmRestConstants;
import org.siemac.metamac.srm.rest.internal.v1_0.utils.Asserts;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

public class ConceptsAsserts extends Asserts {

    public static void assertEqualsResource(ConceptSchemeVersionMetamac expected, ResourceInternal actual) {
        MaintainableArtefact maintainableArtefact = expected.getMaintainableArtefact();
        String agency = maintainableArtefact.getMaintainer().getIdAsMaintainer();
        String code = maintainableArtefact.getCode();
        String version = maintainableArtefact.getVersionLogic();
        String expectedSelfLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/conceptschemes/" + agency + "/" + code + "/" + version;
        String expectedManagementLink = "http://localhost:8080/metamac-srm-web/#structuralResources/conceptSchemes/conceptScheme;id=" + agency + ":" + code + "(" + version + ")";
        Asserts.assertEqualsResource(expected, SrmRestConstants.KIND_CONCEPT_SCHEME, expectedSelfLink, expectedManagementLink, actual);
    }

    public static void assertEqualsResource(ItemSchemeVersion itemSchemeVersion, ConceptMetamac expected, ItemResult expectedItemResult, ResourceInternal actual) {
        MaintainableArtefact maintainableArtefact = itemSchemeVersion.getMaintainableArtefact();
        String agency = maintainableArtefact.getMaintainer().getIdAsMaintainer();
        String codeItemScheme = maintainableArtefact.getCode();
        String version = maintainableArtefact.getVersionLogic();
        String code = null;
        if (expected != null) {
            code = expected.getNameableArtefact().getCode();
        } else if (expectedItemResult != null) {
            code = expectedItemResult.getCode();
        }
        String expectedSelfLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/conceptschemes/" + agency + "/" + codeItemScheme + "/" + version + "/concepts/" + code;
        String expectedManagementLink = "http://localhost:8080/metamac-srm-web/#structuralResources/conceptSchemes/conceptScheme;id=" + agency + ":" + codeItemScheme + "(" + version + ")/concept;id="
                + code;
        if (expected != null) {
            Asserts.assertEqualsResource(expected, SrmRestConstants.KIND_CONCEPT, expectedSelfLink, expectedManagementLink, actual);
        } else if (expectedItemResult != null) {
            Asserts.assertEqualsResource(expectedItemResult, SrmRestConstants.KIND_CONCEPT, expectedSelfLink, expectedManagementLink, actual, itemSchemeVersion.getMaintainableArtefact()
                    .getIsImported());
        }
    }

    public static void assertEqualsRoleConcepts(List<ConceptMetamac> expecteds, RoleConcepts actuals) {
        if (CollectionUtils.isEmpty(expecteds)) {
            assertNull(actuals);
            return;
        }
        assertEquals(expecteds.size(), actuals.getTotal().intValue());
        for (int i = 0; i < expecteds.size(); i++) {
            assertEqualsResource(expecteds.get(i).getItemSchemeVersion(), expecteds.get(i), null, actuals.getRoles().get(i));
        }
    }

    public static void assertEqualsRelatedConcepts(List<ConceptMetamac> expecteds, RelatedConcepts actuals) {
        if (CollectionUtils.isEmpty(expecteds)) {
            assertNull(actuals);
            return;
        }
        assertEquals(expecteds.size(), actuals.getTotal().intValue());
        for (int i = 0; i < expecteds.size(); i++) {
            assertEqualsResource(expecteds.get(i).getItemSchemeVersion(), expecteds.get(i), null, actuals.getRelatedConcepts().get(i));
        }
    }
}