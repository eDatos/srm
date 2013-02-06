package org.siemac.metamac.srm.rest.internal.v1_0.code.utils;

import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.v1_0.utils.Asserts;

import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;

public class CodesAsserts extends Asserts {

    public static void assertEqualsResource(CodelistVersionMetamac expected, Resource actual) {
        MaintainableArtefact maintainableArtefact = expected.getMaintainableArtefact();
        String expectedSelfLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/codelists/" + maintainableArtefact.getMaintainer().getIdAsMaintainer() + "/" + maintainableArtefact.getCode() + "/"
                + maintainableArtefact.getVersionLogic();

        assertEqualsResource(expected, RestInternalConstants.KIND_CODELIST, expectedSelfLink, actual);
    }

    public static void assertEqualsResource(CodeMetamac expected, Resource actual) {
        MaintainableArtefact maintainableArtefact = expected.getItemSchemeVersion().getMaintainableArtefact();
        String expectedSelfLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/codelists/" + maintainableArtefact.getMaintainer().getIdAsMaintainer() + "/" + maintainableArtefact.getCode() + "/"
                + maintainableArtefact.getVersionLogic() + "/codes/" + expected.getNameableArtefact().getCode();
        assertEqualsResource(expected, RestInternalConstants.KIND_CODE, expectedSelfLink, actual);
    }
}