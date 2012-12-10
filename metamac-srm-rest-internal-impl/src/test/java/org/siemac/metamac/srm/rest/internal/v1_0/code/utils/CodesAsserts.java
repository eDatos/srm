package org.siemac.metamac.srm.rest.internal.v1_0.code.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Code;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Codelist;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.v1_0.utils.Asserts;

import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.CodeType;

public class CodesAsserts extends Asserts {

    public static void assertEqualsCodelist(CodelistVersionMetamac source, Codelist target) {
        assertEquals(RestInternalConstants.KIND_CODELIST, target.getKind());
        String parentLink = "http://data.istac.es/apis/srm/v1.0/codelists";
        String selfLink = parentLink + "/" + source.getMaintainableArtefact().getMaintainer().getIdAsMaintainer() + "/" + source.getMaintainableArtefact().getCode() + "/"
                + source.getMaintainableArtefact().getVersionLogic();
        assertEquals(RestInternalConstants.KIND_CODELIST, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals(RestInternalConstants.KIND_CODELISTS, target.getParentLink().getKind());
        assertEquals(parentLink, target.getParentLink().getHref());
        assertEquals(source.getMaintainableArtefact().getReplaceToVersion(), target.getReplaceToVersion());
        assertEquals(BigInteger.ONE, target.getChildLinks().getTotal());
        assertEquals(RestInternalConstants.KIND_CODES, target.getChildLinks().getChildLinks().get(0).getKind());
        assertEquals(selfLink + "/codes", target.getChildLinks().getChildLinks().get(0).getHref());

        // Codes (SDMX type)
        assertEquals(source.getItems().size(), target.getCodes().size());
        for (int i = 0; i < source.getItems().size(); i++) {
            assertTrue(target.getCodes().get(i) instanceof CodeType);
            assertFalse(target.getCodes().get(i) instanceof Code);

            assertEqualsCodeSdmx((CodeMetamac) source.getItems().get(i), target.getCodes().get(i));
        }
    }
    public static void assertEqualsCodeSdmx(CodeMetamac source, CodeType target) {
        // Only test some metadata because SDMX metadata is tested in SDMX project
        // Test something...
        assertEquals(source.getNameableArtefact().getCode(), target.getId());
        assertEquals(source.getNameableArtefact().getUrn(), target.getUrn());
        assertEqualsNullability(source.getParent(), target.getParent());
        if (source.getParent() != null) {
            assertEquals(source.getParent().getNameableArtefact().getCode(), target.getParent().getRef().getId());
        }
    }

    public static void assertEqualsCode(CodeMetamac source, Code target) {

        assertEquals(RestInternalConstants.KIND_CODE, target.getKind());
        String parentLink = "http://data.istac.es/apis/srm/v1.0/codelists" + "/" + source.getItemSchemeVersion().getMaintainableArtefact().getMaintainer().getIdAsMaintainer() + "/"
                + source.getItemSchemeVersion().getMaintainableArtefact().getCode() + "/" + source.getItemSchemeVersion().getMaintainableArtefact().getVersionLogic() + "/codes";
        String selfLink = parentLink + "/" + source.getNameableArtefact().getCode();
        assertEquals(RestInternalConstants.KIND_CODE, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals(RestInternalConstants.KIND_CODES, target.getParentLink().getKind());
        assertEquals(parentLink, target.getParentLink().getHref());
        assertNull(target.getChildLinks());

        // Sdmx
        assertEqualsCodeSdmx(source, target);
    }

    public static void assertEqualsResource(CodelistVersionMetamac expected, Resource actual) {
        MaintainableArtefact maintainableArtefact = expected.getMaintainableArtefact();
        String expectedSelfLink = "http://data.istac.es/apis/srm/v1.0/codelists/" + maintainableArtefact.getMaintainer().getIdAsMaintainer() + "/" + maintainableArtefact.getCode() + "/"
                + maintainableArtefact.getVersionLogic();

        assertEqualsResource(expected, RestInternalConstants.KIND_CODELIST, expectedSelfLink, actual);
    }
    
    public static void assertEqualsResource(CodeMetamac expected, Resource actual) {
        MaintainableArtefact maintainableArtefact = expected.getItemSchemeVersion().getMaintainableArtefact();
        String expectedSelfLink = "http://data.istac.es/apis/srm/v1.0/codelists/" + maintainableArtefact.getMaintainer().getIdAsMaintainer() + "/" + maintainableArtefact.getCode() + "/"
                + maintainableArtefact.getVersionLogic() + "/codes/" + expected.getNameableArtefact().getCode();
        assertEqualsResource(expected, RestInternalConstants.KIND_CODE, expectedSelfLink, actual);
    }
}