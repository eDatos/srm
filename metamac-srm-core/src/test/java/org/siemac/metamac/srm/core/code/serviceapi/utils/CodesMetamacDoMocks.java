package org.siemac.metamac.srm.core.code.serviceapi.utils;

import org.joda.time.DateTime;
import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.srm.core.base.utils.BaseDoMocks;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistOpennessVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.VariableElementResult;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;
import org.siemac.metamac.srm.core.common.service.utils.GeneratorUrnUtils;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;

import com.arte.statistic.sdmx.srm.core.base.domain.IdentifiableArtefact;
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
        codelistVersion.setDescriptionSource(mockInternationalString());
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
        target.setDescriptionSource(mockInternationalStringFixedValues("descriptionSource", getString(resourceID, version)));
        target.setIsRecommended(Boolean.TRUE);
        target.setAccessType(AccessTypeEnum.PUBLIC);
        target.setFamily(mockCodelistFamilyFixedValues("family1"));
        target.setVariable(mockVariableFixedValues("variable1"));
        target.setDefaultOrderVisualisation(mockCodelistOrderVisualisationFixedValues("defaultOrderVisualisation1"));
        target.setDefaultOpennessVisualisation(mockCodelistOpennessVisualisationFixedValues("defaultOpennessVisualisation1"));
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
        target.getNameableArtefact().setUrn(GeneratorUrnUtils.generateCodelistFamilyUrn(target));
        target.getNameableArtefact().setUrnProvider(target.getNameableArtefact().getUrn());
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
        target.getNameableArtefact().setUrn(GeneratorUrnUtils.generateVariableFamilyUrn(target));
        target.getNameableArtefact().setUrnProvider(target.getNameableArtefact().getUrn());
        return target;
    }

    // ------------------------------------------------------------------------------------
    // VARIABLES
    // ------------------------------------------------------------------------------------

    public static Variable mockVariable() {
        Variable variable = new Variable();
        variable.setNameableArtefact(mockNameableArtefact("code-" + MetamacMocks.mockString(5)));
        variable.setType(null);
        variable.setShortName(BaseDoMocks.mockInternationalString());
        variable.setValidFrom(new DateTime(2012, 10, 1, 10, 12, 13, 14));
        variable.setValidTo(new DateTime(2013, 10, 1, 10, 12, 13, 14));
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
        target.setType(null);
        target.setShortName(mockInternationalStringFixedValues("shortName", resourceID));
        target.setValidFrom(new DateTime(2012, 10, 1, 10, 12, 13, 14));
        target.setValidTo(new DateTime(2013, 10, 1, 10, 12, 13, 14));
        target.setNameableArtefact(new NameableArtefact());
        mockNameableArtefactFixedValues(target.getNameableArtefact(), resourceID, null);
        target.getNameableArtefact().setUrn(GeneratorUrnUtils.generateVariableUrn(target));
        target.getNameableArtefact().setUrnProvider(target.getNameableArtefact().getUrn());
        return target;
    }

    // ------------------------------------------------------------------------------------
    // VARIABLE ELEMENTS
    // ------------------------------------------------------------------------------------

    public static VariableElement mockVariableElement(Variable variable) {
        VariableElement variableElement = new VariableElement();
        variableElement.setIdentifiableArtefact(new IdentifiableArtefact());
        mockIdentifiableArtefact(variableElement.getIdentifiableArtefact(), "code-" + MetamacMocks.mockString(5));
        variableElement.setShortName(BaseDoMocks.mockInternationalString());
        variableElement.setComment(BaseDoMocks.mockInternationalString());
        variableElement.setValidFrom(new DateTime(2012, 10, 1, 10, 12, 13, 14));
        variableElement.setValidTo(new DateTime(2013, 10, 1, 10, 12, 13, 14));
        variableElement.setVariable(variable);
        return variableElement;
    }

    public static VariableElement mockVariableElementGeographical(Variable variable, CodeMetamac geographicalGranularity) {
        VariableElement variableElement = mockVariableElement(variable);
        variableElement.setLatitude(Double.valueOf(1));
        variableElement.setLongitude(Double.valueOf(2));
        variableElement.setShapeWkt("shape1");
        variableElement.setShapeGeojson("shape1Geojson");
        variableElement.setGeographicalGranularity(geographicalGranularity);
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
        target.setComment(mockInternationalStringFixedValues("comment", resourceID));
        target.setVariable(mockVariableFixedValues("variable01"));
        target.setValidFrom(new DateTime(2012, 10, 1, 10, 12, 13, 14));
        target.setValidTo(new DateTime(2013, 10, 1, 10, 12, 13, 14));
        target.setIdentifiableArtefact(new IdentifiableArtefact());
        mockIdentifiableArtefactFixedValues(target.getIdentifiableArtefact(), resourceID, null);
        target.getIdentifiableArtefact().setUrn(GeneratorUrnUtils.generateVariableElementUrn(target.getVariable(), target));
        target.getIdentifiableArtefact().setUrnProvider(target.getIdentifiableArtefact().getUrn());
        return target;
    }

    public static VariableElement mockVariableElementGeographicalFixedValues(String resourceID) {
        VariableElement target = mockVariableElementFixedValues(resourceID);
        target.setLatitude(Double.valueOf(1));
        target.setLongitude(Double.valueOf(2));
        target.setShapeWkt("shape1");
        target.setShapeGeojson("shape1Geojson");
        target.setGeographicalGranularity(mockCodeFixedValues("code1", mockCodelistFixedValues("agency01", "codelist01", "01.000"), null));
        return target;
    }

    public static VariableElementResult mockVariableElementResultFixedValues(String resourceID) {
        VariableElementResult target = new VariableElementResult();
        target.setCode(resourceID);
        target.setUrn("urn:sdmx:org.sdmx.infomodel.xxx=" + resourceID);
        target.getShortName().putAll(mockInternationalStringResultFixedValues("shortName", resourceID));
        target.getAnnotations().add(mockAnnotationResultFixedValues("code1"));
        target.getAnnotations().add(mockAnnotationResultFixedValues("code2"));
        return target;
    }

    public static VariableElementResult mockVariableElementResultGeographicalFixedValues(String resourceID) {
        VariableElementResult target = mockVariableElementResultFixedValues(resourceID);
        target.setLatitude(Double.valueOf(1));
        target.setLongitude(Double.valueOf(2));
        target.setShapeWkt("MULTIPOLYGON (((-17.9 28.8)))");
        target.setShapeGeojson("{(((-17.9 28.8)))}");
        // target.setGeographicalGranularity(mockCodeResultFixedValues("code1", null)); // TODO VariableElementResult.geographicalGranularity
        return target;
    }

    // ------------------------------------------------------------------------------------
    // CODELIST ORDER VISUALISATIONS
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

    // ------------------------------------------------------------------------------------
    // CODELIST OPENNESS VISUALISATIONS
    // ------------------------------------------------------------------------------------

    public static CodelistOpennessVisualisation mockCodelistOpennessVisualisation() {
        CodelistOpennessVisualisation codelistOpennessVisualisation = new CodelistOpennessVisualisation();
        codelistOpennessVisualisation.setNameableArtefact(mockNameableArtefact("code-" + MetamacMocks.mockString(5)));
        return codelistOpennessVisualisation;
    }

    public static CodelistOpennessVisualisation mockCodelistOpennessVisualisationFixedValues(String resourceID) {
        CodelistOpennessVisualisation target = new CodelistOpennessVisualisation();
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
