package org.siemac.metamac.srm.core.code.serviceapi.utils;

import org.joda.time.DateTime;
import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.srm.core.base.utils.BaseDoMocks;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefact;
import com.arte.statistic.sdmx.srm.core.code.serviceapi.utils.CodesDoMocks;

public class CodesMetamacDoMocks extends CodesDoMocks {

    // ------------------------------------------------------------------------------------
    // CODELISTS
    // ------------------------------------------------------------------------------------

    public static CodelistVersionMetamac mockCodelist(OrganisationMetamac maintainer) {
        CodelistVersionMetamac codelistVersion = new CodelistVersionMetamac();

        codelistVersion.setShortName(mockInternationalString());
        codelistVersion.setIsRecommended(Boolean.FALSE);
        codelistVersion.setAccessType(AccessTypeEnum.RESTRICTED);

        mockCodelist(codelistVersion, maintainer);
        return codelistVersion;
    }

    public static CodelistVersionMetamac mockCodelistFixedValues(String agencyID, String resourceID, String version) {
        CodelistVersionMetamac target = new CodelistVersionMetamac();
        CodesDoMocks.mockCodelistFixedValues(target, agencyID, resourceID, version);

        // metamac
        target.setLifeCycleMetadata(BaseDoMocks.mockLifeCycleExternallyPublished());
        target.setShortName(mockInternationalStringFixedValues("shortName", getString(resourceID, version)));
        target.setIsRecommended(Boolean.TRUE);
        target.setAccessType(AccessTypeEnum.PUBLIC);
        target.setFamily(mockCodelistFamilyFixedValues("family1"));
        target.setVariable(mockVariableFixedValues("variable1"));
        target.setDefaultOrderVisualisation(mockCodelistOrderVisualisationFixedValues("defaultOrderVisualisation1"));
        return target;
    }

    public static CodelistVersionMetamac mockCodelistWithCodesFixedValues(String agencyID, String resourceID, String version) {

        CodelistVersionMetamac codelistVersion = mockCodelistFixedValues(agencyID, resourceID, version);

        // codes
        CodeMetamac code1 = mockCodeFixedValues("code1", codelistVersion, null);
        CodeMetamac code2 = mockCodeFixedValues("code2", codelistVersion, null);
        CodeMetamac code2A = mockCodeFixedValues("code2A", codelistVersion, code2);
        CodeMetamac code2B = mockCodeFixedValues("code2B", codelistVersion, code2);

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

    // ------------------------------------------------------------------------------------
    // CODES
    // ------------------------------------------------------------------------------------

    public static CodeMetamac mockCode() {
        CodeMetamac codeMetamac = new CodeMetamac();
        codeMetamac.setShortName(BaseDoMocks.mockInternationalString());
        mockCode(codeMetamac);
        return codeMetamac;
    }

    public static CodeMetamac mockCodeFixedValues(String resourceID, ItemSchemeVersion itemSchemeVersion, CodeMetamac parent) {
        CodeMetamac target = new CodeMetamac();
        CodesDoMocks.mockCodeFixedValues(target, resourceID, itemSchemeVersion, parent);

        // metamac
        target.setShortName(mockInternationalStringFixedValues("shortName", resourceID));
        target.setVariableElement(mockVariableElementFixedValues("variableElement1"));
        return target;
    }

    // ------------------------------------------------------------------------------------
    // CODELIST FAMILIES
    // ------------------------------------------------------------------------------------

    public static CodelistFamily mockCodelistFamily() {
        CodelistFamily codelistFamily = new CodelistFamily();
        codelistFamily.setNameableArtefact(mockNameableArtefact("code-" + MetamacMocks.mockString(5)));
        return codelistFamily;
    }

    public static CodelistFamily mockCodelistFamilyFixedValues(String resourceID) {
        CodelistFamily target = new CodelistFamily();
        target.getUuid(); // generate uuid
        target.setVersion(Long.valueOf(1));
        target.setCreatedBy("userCreatedBy");
        target.setCreatedDate(new DateTime(2012, 10, 1, 10, 12, 13, 14));
        target.setLastUpdatedBy("userLastUpdated");
        target.setLastUpdated(new DateTime(2012, 11, 5, 10, 12, 13, 14));
        target.setUpdateDate(new DateTime(2012, 11, 5, 10, 12, 13, 14));
        target.setNameableArtefact(new NameableArtefact());
        mockNameableArtefactFixedValues(target.getNameableArtefact(), resourceID, null);
        return target;
    }
    // ------------------------------------------------------------------------------------
    // VARIABLE FAMILIES
    // ------------------------------------------------------------------------------------

    public static VariableFamily mockVariableFamily() {
        VariableFamily variableFamily = new VariableFamily();
        variableFamily.setNameableArtefact(mockNameableArtefact("code-" + MetamacMocks.mockString(5)));
        return variableFamily;
    }

    public static VariableFamily mockVariableFamilyFixedValues(String resourceID) {
        VariableFamily target = new VariableFamily();
        target.getUuid(); // generate uuid
        target.setVersion(Long.valueOf(1));
        target.setCreatedBy("userCreatedBy");
        target.setCreatedDate(new DateTime(2012, 10, 1, 10, 12, 13, 14));
        target.setLastUpdatedBy("userLastUpdated");
        target.setLastUpdated(new DateTime(2012, 11, 5, 10, 12, 13, 14));
        target.setUpdateDate(new DateTime(2012, 11, 5, 10, 12, 13, 14));
        target.setNameableArtefact(new NameableArtefact());
        mockNameableArtefactFixedValues(target.getNameableArtefact(), resourceID, null);
        return target;
    }

    // ------------------------------------------------------------------------------------
    // VARIABLES
    // ------------------------------------------------------------------------------------

    public static Variable mockVariable() {
        Variable variable = new Variable();
        variable.setNameableArtefact(mockNameableArtefact("code-" + MetamacMocks.mockString(5)));
        variable.setShortName(BaseDoMocks.mockInternationalString());
        variable.setValidFrom(new DateTime());
        variable.setValidTo(new DateTime());
        return variable;
    }

    public static Variable mockVariableFixedValues(String resourceID) {
        Variable target = new Variable();
        target.getUuid(); // generate uuid
        target.setVersion(Long.valueOf(1));
        target.setCreatedBy("userCreatedBy");
        target.setCreatedDate(new DateTime(2012, 10, 1, 10, 12, 13, 14));
        target.setLastUpdatedBy("userLastUpdated");
        target.setLastUpdated(new DateTime(2012, 11, 5, 10, 12, 13, 14));
        target.setUpdateDate(new DateTime(2012, 11, 5, 10, 12, 13, 14));
        target.setShortName(mockInternationalStringFixedValues("shortName", resourceID));
        target.setNameableArtefact(new NameableArtefact());
        mockNameableArtefactFixedValues(target.getNameableArtefact(), resourceID, null);
        return target;
    }

    // ------------------------------------------------------------------------------------
    // VARIABLE ELEMENTS
    // ------------------------------------------------------------------------------------

    public static VariableElement mockVariableElement(Variable variable) {
        VariableElement variableElement = new VariableElement();
        variableElement.setNameableArtefact(mockNameableArtefact("code-" + MetamacMocks.mockString(5)));
        variableElement.setShortName(BaseDoMocks.mockInternationalString());
        variableElement.setValidFrom(new DateTime());
        variableElement.setValidTo(new DateTime());
        variableElement.setVariable(variable);
        return variableElement;
    }

    public static VariableElement mockVariableElementFixedValues(String resourceID) {
        VariableElement target = new VariableElement();
        target.getUuid(); // generate uuid
        target.setVersion(Long.valueOf(1));
        target.setCreatedBy("userCreatedBy");
        target.setCreatedDate(new DateTime(2012, 10, 1, 10, 12, 13, 14));
        target.setLastUpdatedBy("userLastUpdated");
        target.setLastUpdated(new DateTime(2012, 11, 5, 10, 12, 13, 14));
        target.setUpdateDate(new DateTime(2012, 11, 5, 10, 12, 13, 14));
        target.setShortName(mockInternationalStringFixedValues("shortName", resourceID));
        target.setVariable(mockVariableFixedValues("variable01"));
        target.setNameableArtefact(new NameableArtefact());
        mockNameableArtefactFixedValues(target.getNameableArtefact(), resourceID, null);
        return target;
    }

    // ------------------------------------------------------------------------------------
    // CODELIST VISUALISATIONS
    // ------------------------------------------------------------------------------------

    public static CodelistOrderVisualisation mockCodelistOrderVisualisation() {
        CodelistOrderVisualisation codelistOrderVisualisation = new CodelistOrderVisualisation();
        codelistOrderVisualisation.setNameableArtefact(mockNameableArtefact("code-" + MetamacMocks.mockString(5)));
        return codelistOrderVisualisation;
    }

    public static CodelistOrderVisualisation mockCodelistOrderVisualisationFixedValues(String resourceID) {
        CodelistOrderVisualisation target = new CodelistOrderVisualisation();
        target.getUuid(); // generate uuid
        target.setVersion(Long.valueOf(1));
        target.setCreatedBy("userCreatedBy");
        target.setCreatedDate(new DateTime(2012, 10, 1, 10, 12, 13, 14));
        target.setLastUpdatedBy("userLastUpdated");
        target.setLastUpdated(new DateTime(2012, 11, 5, 10, 12, 13, 14));
        target.setUpdateDate(new DateTime(2012, 11, 5, 10, 12, 13, 14));
        target.setNameableArtefact(new NameableArtefact());
        mockNameableArtefactFixedValues(target.getNameableArtefact(), resourceID, null);
        return target;
    }

}
