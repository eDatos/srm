package org.siemac.metamac.srm.rest.internal.v1_0.category.utils;

import static org.junit.Assert.assertEquals;

import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.v1_0.utils.Asserts;

import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.category.domain.Categorisation;

public class CategoriesAsserts extends Asserts {

    public static void assertEqualsResource(CategorySchemeVersionMetamac expected, Resource actual) {
        MaintainableArtefact maintainableArtefact = expected.getMaintainableArtefact();
        String expectedSelfLink = "http://data.istac.es/apis/srm/v1.0/categoryschemes/" + maintainableArtefact.getMaintainer().getIdAsMaintainer() + "/" + maintainableArtefact.getCode() + "/"
                + maintainableArtefact.getVersionLogic();

        assertEqualsResource(expected, RestInternalConstants.KIND_CATEGORY_SCHEME, expectedSelfLink, actual);
    }

    public static void assertEqualsResource(CategoryMetamac expected, Resource actual) {
        MaintainableArtefact maintainableArtefact = expected.getItemSchemeVersion().getMaintainableArtefact();
        String expectedSelfLink = "http://data.istac.es/apis/srm/v1.0/categoryschemes/" + maintainableArtefact.getMaintainer().getIdAsMaintainer() + "/" + maintainableArtefact.getCode() + "/"
                + maintainableArtefact.getVersionLogic() + "/categories/" + expected.getNameableArtefact().getCode();
        assertEqualsResource(expected, RestInternalConstants.KIND_CATEGORY, expectedSelfLink, actual);
    }

    public static void assertEqualsResource(Categorisation expected, Resource actual) {
        MaintainableArtefact maintainableArtefact = expected.getMaintainableArtefact();
        String expectedSelfLink = "http://data.istac.es/apis/srm/v1.0/categorisations/" + maintainableArtefact.getMaintainer().getIdAsMaintainer() + "/" + maintainableArtefact.getCode() + "/"
                + maintainableArtefact.getVersionLogic();

        assertEquals(RestInternalConstants.KIND_CATEGORISATION, actual.getKind());
        assertEquals(expected.getMaintainableArtefact().getCode(), actual.getId());
        assertEquals(expected.getMaintainableArtefact().getUrnProvider(), actual.getUrn());
        assertEquals(RestInternalConstants.KIND_CATEGORISATION, actual.getSelfLink().getKind());
        assertEquals(expectedSelfLink, actual.getSelfLink().getHref());
        assertEqualsInternationalString(expected.getMaintainableArtefact().getName(), actual.getTitle());
    }
}