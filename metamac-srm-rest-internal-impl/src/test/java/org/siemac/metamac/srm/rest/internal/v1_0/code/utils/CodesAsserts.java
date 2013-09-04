package org.siemac.metamac.srm.rest.internal.v1_0.code.utils;

import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.rest.common.SrmRestConstants;
import org.siemac.metamac.srm.rest.internal.v1_0.utils.Asserts;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

public class CodesAsserts {

    public static void assertEqualsResource(CodelistVersionMetamac expected, ResourceInternal actual) {
        MaintainableArtefact maintainableArtefact = expected.getMaintainableArtefact();
        String agency = maintainableArtefact.getMaintainer().getIdAsMaintainer();
        String code = maintainableArtefact.getCode();
        String version = maintainableArtefact.getVersionLogic();
        String expectedSelfLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/codelists/" + agency + "/" + code + "/" + version;
        String expectedManagementLink = "http://localhost:8080/metamac-srm-web/#structuralResources/codelists/codelist;id=" + agency + ":" + code + "(" + version + ")";
        Asserts.assertEqualsResource(expected, SrmRestConstants.KIND_CODELIST, expectedSelfLink, expectedManagementLink, actual);
    }

    public static void assertEqualsResource(ItemSchemeVersion itemSchemeVersion, CodeMetamac expected, ItemResult expectedItemResult, ResourceInternal actual) {
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
        String expectedSelfLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/codelists/" + agency + "/" + codeItemScheme + "/" + version + "/codes/" + code;
        String expectedManagementLink = "http://localhost:8080/metamac-srm-web/#structuralResources/codelists/codelist;id=" + agency + ":" + codeItemScheme + "(" + version + ")/code;id=" + code;
        if (expected != null) {
            Asserts.assertEqualsResource(expected, SrmRestConstants.KIND_CODE, expectedSelfLink, expectedManagementLink, actual);
        } else if (expectedItemResult != null) {
            Asserts.assertEqualsResource(expectedItemResult, SrmRestConstants.KIND_CODE, expectedSelfLink, expectedManagementLink, actual, itemSchemeVersion.getMaintainableArtefact()
                    .getIsImported());
        }
    }

    public static void assertEqualsResource(VariableFamily expected, ResourceInternal actual) {
        String code = expected.getNameableArtefact().getCode();
        String expectedSelfLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/variablefamilies/" + code;
        String expectedManagementLink = "http://localhost:8080/metamac-srm-web/#structuralResources/variableFamilies/variableFamily;id=" + code;
        Asserts.assertEqualsResource(expected.getNameableArtefact(), SrmRestConstants.KIND_VARIABLE_FAMILY, expectedSelfLink, expectedManagementLink, actual, false);
    }

    public static void assertEqualsResource(Variable expected, ResourceInternal actual) {
        String code = expected.getNameableArtefact().getCode();
        String expectedSelfLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/variables/" + code;
        String expectedManagementLink = "http://localhost:8080/metamac-srm-web/#structuralResources/variables/variable;id=" + code;
        Asserts.assertEqualsResource(expected.getNameableArtefact(), SrmRestConstants.KIND_VARIABLE, expectedSelfLink, expectedManagementLink, actual, false);
    }

    public static void assertEqualsResource(VariableElement expected, ResourceInternal actual) {
        String code = expected.getIdentifiableArtefact().getCode();
        String variableID = expected.getVariable().getNameableArtefact().getCode();
        String expectedSelfLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/variables/" + variableID + "/variableelements/" + code;
        String expectedManagementLink = "http://localhost:8080/metamac-srm-web/#structuralResources/variables/variable;id=" + variableID + "/variableElement;id=" + code;
        Asserts.assertEqualsResource(expected.getIdentifiableArtefact(), SrmRestConstants.KIND_VARIABLE_ELEMENT, expectedSelfLink, expectedManagementLink, actual, false);
    }

    public static void assertEqualsResource(CodelistFamily expected, ResourceInternal actual) {
        String code = expected.getNameableArtefact().getCode();
        String expectedSelfLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/codelistfamilies/" + code;
        String expectedManagementLink = "http://localhost:8080/metamac-srm-web/#structuralResources/codelistFamilies/codelistFamily;id=" + code;
        Asserts.assertEqualsResource(expected.getNameableArtefact(), SrmRestConstants.KIND_CODELIST_FAMILY, expectedSelfLink, expectedManagementLink, actual, false);
    }
}