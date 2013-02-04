package org.siemac.metamac.srm.rest.internal.v1_0.concept.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.RelatedConcepts;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.RoleConcepts;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.v1_0.utils.Asserts;

import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;

public class ConceptsAsserts extends Asserts {

    public static void assertEqualsResource(ConceptSchemeVersionMetamac expected, Resource actual) {
        MaintainableArtefact maintainableArtefact = expected.getMaintainableArtefact();
        String expectedSelfLink = "http://data.istac.es/apis/srm/v1.0/conceptschemes/" + maintainableArtefact.getMaintainer().getIdAsMaintainer() + "/" + maintainableArtefact.getCode() + "/"
                + maintainableArtefact.getVersionLogic();

        assertEqualsResource(expected, RestInternalConstants.KIND_CONCEPT_SCHEME, expectedSelfLink, actual);
    }

    public static void assertEqualsResource(ConceptMetamac expected, Resource actual) {
        MaintainableArtefact maintainableArtefact = expected.getItemSchemeVersion().getMaintainableArtefact();
        String expectedSelfLink = "http://data.istac.es/apis/srm/v1.0/conceptschemes/" + maintainableArtefact.getMaintainer().getIdAsMaintainer() + "/" + maintainableArtefact.getCode() + "/"
                + maintainableArtefact.getVersionLogic() + "/concepts/" + expected.getNameableArtefact().getCode();
        assertEqualsResource(expected, RestInternalConstants.KIND_CONCEPT, expectedSelfLink, actual);
    }

    public static void assertEqualsRoleConcepts(List<ConceptMetamac> expecteds, RoleConcepts actuals) {
        if (CollectionUtils.isEmpty(expecteds)) {
            assertNull(actuals);
            return;
        }
        assertEquals(expecteds.size(), actuals.getTotal().intValue());
        for (int i = 0; i < expecteds.size(); i++) {
            assertEqualsResource(expecteds.get(i), actuals.getRoles().get(i));
        }
    }

    public static void assertEqualsRelatedConcepts(List<ConceptMetamac> expecteds, RelatedConcepts actuals) {
        if (CollectionUtils.isEmpty(expecteds)) {
            assertNull(actuals);
            return;
        }
        assertEquals(expecteds.size(), actuals.getTotal().intValue());
        for (int i = 0; i < expecteds.size(); i++) {
            assertEqualsResource(expecteds.get(i), actuals.getRelatedConcepts().get(i));
        }
    }
}