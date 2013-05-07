package org.siemac.metamac.srm.soap.external.v1_0.service;

import org.junit.Before;
import org.junit.BeforeClass;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.soap.structural_resources.v1_0.MetamacStructuralResourcesInterfaceV10;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class SrmSoapExternalFacadeV10BaseTest {

    @Autowired
    private MetamacStructuralResourcesInterfaceV10 srmInterfaceV10;

    protected static String                        NOT_EXISTS         = "not-exists";
    protected static String                        VARIABLE_FAMILY_01 = "variableFamily01";
    protected static String                        VARIABLE_FAMILY_02 = "variableFamily02";
    protected static String                        VARIABLE_01        = "variable01";
    protected static String                        VARIABLE_02        = "variable02";
    protected static String                        CODELIST_FAMILY_01 = "codelistFamily01";
    protected static String                        CODELIST_FAMILY_02 = "codelistFamily02";
    protected static String                        AGENCY_1           = "agency01";
    protected static String                        CODELIST_01        = "codelist01";
    protected static String                        CODELIST_01_URN    = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=agency01:codelist01(01.000)";
    protected static String                        CODELIST_02        = "codelist02";
    protected static String                        CODELIST_02_URN    = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=agency01:codelist02(01.000)";
    protected static String                        VERSION_1          = "01.000";

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws MetamacException {
    }

    public MetamacStructuralResourcesInterfaceV10 getSrmInterfaceV10() {
        return srmInterfaceV10;
    }

    protected abstract void resetMocks() throws MetamacException;
}