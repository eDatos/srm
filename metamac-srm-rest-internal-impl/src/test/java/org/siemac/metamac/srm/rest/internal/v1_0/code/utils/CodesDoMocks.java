package org.siemac.metamac.srm.rest.internal.v1_0.code.utils;

import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;
import org.siemac.metamac.srm.rest.internal.v1_0.utils.DoMocks;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefact;

public class CodesDoMocks extends DoMocks {

    public static CodelistVersionMetamac mockCodelist(String agencyID, String resourceID, String version) {
        CodelistVersionMetamac target = new CodelistVersionMetamac();
        target.setLifeCycleMetadata(mockLifecycleExternallyPublished());
        target.setShortName(mockInternationalString("shortName", getString(resourceID, version)));
        target.setIsRecommended(Boolean.TRUE);
        target.setAccessType(AccessTypeEnum.PUBLIC);
        target.setFamily(mockCodelistFamily("family1"));
        target.setVariable(mockVariable("variable1"));
        target.setDefaultOrderVisualisation(mockCodelistOrderVisualisation("defaultOrderVisualisation1"));
        mockItemSchemeVersion(target, resourceID, version, agencyID);
        return target;
    }
    public static CodelistVersionMetamac mockCodelistWithCodes(String agencyID, String resourceID, String version) {

        CodelistVersionMetamac codelistVersion = mockCodelist(agencyID, resourceID, version);

        // codes
        CodeMetamac code1 = mockCode("code1", codelistVersion, null);
        CodeMetamac code2 = mockCode("code2", codelistVersion, null);
        CodeMetamac code2A = mockCode("code2A", codelistVersion, code2);
        CodeMetamac code2B = mockCode("code2B", codelistVersion, code2);

        // codes hierarchy
        codelistVersion.addItem(code1);
        codelistVersion.addItemsFirstLevel(code1);
        codelistVersion.addItem(code2);
        codelistVersion.addItemsFirstLevel(code2);
        codelistVersion.addItem(code2A);
        codelistVersion.addItem(code2B);
        code2.addChildren(code2A);
        code2.addChildren(code2B);

        return codelistVersion;
    }

    public static CodeMetamac mockCode(String resourceID, ItemSchemeVersion itemSchemeVersion, CodeMetamac parent) {
        CodeMetamac target = new CodeMetamac();
        target.setShortName(mockInternationalString("shortName", resourceID));
        target.setVariableElement(mockVariableElement("variableElement1"));
        mockItem(target, resourceID, itemSchemeVersion, parent);
        return target;
    }

    public static CodelistFamily mockCodelistFamily(String resourceID) {
        CodelistFamily target = new CodelistFamily();
        target.setNameableArtefact(new NameableArtefact());
        mockNameableArtefact(target.getNameableArtefact(), resourceID, null);
        return target;
    }

    public static Variable mockVariable(String resourceID) {
        Variable target = new Variable();
        target.setNameableArtefact(new NameableArtefact());
        target.setShortName(mockInternationalString("shortName", resourceID));
        mockNameableArtefact(target.getNameableArtefact(), resourceID, null);
        return target;
    }

    public static VariableElement mockVariableElement(String resourceID) {
        VariableElement target = new VariableElement();
        target.setNameableArtefact(new NameableArtefact());
        target.setShortName(mockInternationalString("shortName", resourceID));
        mockNameableArtefact(target.getNameableArtefact(), resourceID, null);
        return target;
    }

    public static CodelistOrderVisualisation mockCodelistOrderVisualisation(String resourceID) {
        CodelistOrderVisualisation target = new CodelistOrderVisualisation();
        target.setNameableArtefact(new NameableArtefact());
        mockNameableArtefact(target.getNameableArtefact(), resourceID, null);
        return target;
    }
}