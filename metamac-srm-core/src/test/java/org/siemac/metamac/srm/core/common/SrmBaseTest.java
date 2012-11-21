package org.siemac.metamac.srm.core.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum;
import org.siemac.metamac.sso.client.MetamacPrincipal;
import org.siemac.metamac.sso.client.MetamacPrincipalAccess;
import org.siemac.metamac.sso.client.SsoClientConstants;
import org.springframework.beans.factory.annotation.Value;

import com.arte.statistic.sdmx.srm.core.common.SdmxSrmBaseTest;

public abstract class SrmBaseTest extends SdmxSrmBaseTest {

    // Agencies
    protected static final String ORGANISATION_SCHEME_ROOT_1_V1               = "urn:sdmx:org.sdmx.infomodel.base.AgencyScheme=SDMX:ORGANISATIONSCHEME100(01.000)";
    public static final String    AGENCY_ROOT_1_V1                            = "urn:sdmx:org.sdmx.infomodel.base.Agency=SDMX:ORGANISATIONSCHEME100(01.000).SDMX01";
    public static final String    AGENCY_ROOT_2_V1                            = "urn:sdmx:org.sdmx.infomodel.base.Agency=SDMX:ORGANISATIONSCHEME100(01.000).SDMX02";
    public static final String    AGENCY_ROOT_1_V1_CODE                       = "SDMX01";

    // Concepts schemes
    protected static final String CONCEPT_SCHEME_1_V1                         = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME01(01.000)";
    protected static final String CONCEPT_SCHEME_1_V2                         = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME01(02.000)";
    protected static final String CONCEPT_SCHEME_2_V1                         = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME02(01.000)";
    protected static final String CONCEPT_SCHEME_3_V1                         = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME03(01.000)";
    protected static final String CONCEPT_SCHEME_4_V1                         = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME04(01.000)";
    protected static final String CONCEPT_SCHEME_5_V1                         = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME05(01.000)";
    protected static final String CONCEPT_SCHEME_6_V1                         = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME06(01.000)";
    protected static final String CONCEPT_SCHEME_7_V1                         = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME07(01.000)";
    protected static final String CONCEPT_SCHEME_7_V2                         = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME07(02.000)";
    protected static final String CONCEPT_SCHEME_8_V1                         = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME08(01.000)";
    protected static final String CONCEPT_SCHEME_9_V1                         = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME09(01.000)";
    protected static final String CONCEPT_SCHEME_10_V1                        = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME10(01.000)";
    protected static final String CONCEPT_SCHEME_10_V2                        = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME10(02.000)";
    protected static final String CONCEPT_SCHEME_10_V3                        = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME10(03.000)";
    protected static final String CONCEPT_SCHEME_11_V1                        = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME11(01.000)";
    protected static final String CONCEPT_SCHEME_12_V1                        = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME12(01.000)";
    protected static final String CONCEPT_SCHEME_13_V1                        = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME13(01.000)";

    // Concepts
    protected static final String CONCEPT_SCHEME_1_V1_CONCEPT_1               = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME01(01.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_1_V2_CONCEPT_1               = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME01(02.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_1_V2_CONCEPT_2               = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME01(02.000).CONCEPT02";
    protected static final String CONCEPT_SCHEME_1_V2_CONCEPT_2_1             = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME01(02.000).CONCEPT0201";
    protected static final String CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1           = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME01(02.000).CONCEPT020101";
    protected static final String CONCEPT_SCHEME_1_V2_CONCEPT_3               = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME01(02.000).CONCEPT03";
    protected static final String CONCEPT_SCHEME_1_V2_CONCEPT_4               = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME01(02.000).CONCEPT04";
    protected static final String CONCEPT_SCHEME_1_V2_CONCEPT_4_1             = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME01(02.000).CONCEPT0401";
    protected static final String CONCEPT_SCHEME_1_V2_CONCEPT_4_1_1           = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME01(02.000).CONCEPT040101";
    protected static final String CONCEPT_SCHEME_2_V1_CONCEPT_1               = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME02(01.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_2_V1_CONCEPT_2               = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME02(01.000).CONCEPT02";
    protected static final String CONCEPT_SCHEME_3_V1_CONCEPT_1               = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME03(01.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_3_V1_CONCEPT_2               = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME03(01.000).CONCEPT02";
    protected static final String CONCEPT_SCHEME_3_V1_CONCEPT_2_1             = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME03(01.000).CONCEPT0201";
    protected static final String CONCEPT_SCHEME_3_V1_CONCEPT_2_1_1           = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME03(01.000).CONCEPT020101";
    protected static final String CONCEPT_SCHEME_3_V1_CONCEPT_2_2             = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME03(01.000).CONCEPT0202";
    protected static final String CONCEPT_SCHEME_4_V1_CONCEPT_1               = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME04(01.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_5_V1_CONCEPT_1               = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME05(01.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_6_V1_CONCEPT_1               = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME06(01.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_7_V2_CONCEPT_1               = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME07(02.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_8_V1_CONCEPT_1               = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME08(01.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_10_V2_CONCEPT_1              = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME10(02.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_10_V3_CONCEPT_1              = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME10(03.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_11_V1_CONCEPT_1              = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME11(01.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_12_V1_CONCEPT_1              = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME12(01.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_13_V1_CONCEPT_1              = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME13(01.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_13_V1_CONCEPT_2              = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME13(01.000).CONCEPT02";
    protected static final String CONCEPT_SCHEME_13_V1_CONCEPT_3              = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME13(01.000).CONCEPT03";

    // Concept types
    protected static final String CONCEPT_TYPE_DERIVED                        = "DERIVED";
    protected static final String CONCEPT_TYPE_DIRECT                         = "DIRECT";

    // Organisations schemes
    protected static final String ORGANISATION_SCHEME_1_V1                    = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnitScheme=SDMX01:ORGANISATIONSCHEME01(01.000)";
    protected static final String ORGANISATION_SCHEME_1_V2                    = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnitScheme=SDMX01:ORGANISATIONSCHEME01(02.000)";
    protected static final String ORGANISATION_SCHEME_2_V1                    = "urn:sdmx:org.sdmx.infomodel.base.AgencyScheme=SDMX01:ORGANISATIONSCHEME02(01.000)";
    protected static final String ORGANISATION_SCHEME_3_V1                    = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnitScheme=SDMX01:ORGANISATIONSCHEME03(01.000)";
    protected static final String ORGANISATION_SCHEME_4_V1                    = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnitScheme=SDMX01:ORGANISATIONSCHEME04(01.000)";
    protected static final String ORGANISATION_SCHEME_5_V1                    = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnitScheme=SDMX01:ORGANISATIONSCHEME05(01.000)";
    protected static final String ORGANISATION_SCHEME_6_V1                    = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnitScheme=SDMX01:ORGANISATIONSCHEME06(01.000)";
    protected static final String ORGANISATION_SCHEME_7_V1                    = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnitScheme=SDMX01:ORGANISATIONSCHEME07(01.000)";
    protected static final String ORGANISATION_SCHEME_7_V2                    = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnitScheme=SDMX01:ORGANISATIONSCHEME07(02.000)";
    protected static final String ORGANISATION_SCHEME_8_V1                    = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnitScheme=SDMX02:ORGANISATIONSCHEME08(01.000)";
    protected static final String ORGANISATION_SCHEME_9_V1                    = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnitScheme=SDMX02.ORGANISATION01:ORGANISATIONSCHEME09(01.000)";
    protected static final String ORGANISATION_SCHEME_100_V1                  = ORGANISATION_SCHEME_ROOT_1_V1;

    // Organisations
    protected String              ORGANISATION_SCHEME_1_V1_ORGANISATION_1     = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME01(01.000).ORGANISATION01";
    protected String              ORGANISATION_SCHEME_1_V2_ORGANISATION_1     = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME01(02.000).ORGANISATION01";
    protected String              ORGANISATION_SCHEME_1_V2_ORGANISATION_2     = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME01(02.000).ORGANISATION02";
    protected String              ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1   = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME01(02.000).ORGANISATION0201";
    protected String              ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1_1 = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME01(02.000).ORGANISATION020101";
    protected String              ORGANISATION_SCHEME_1_V2_ORGANISATION_3     = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME01(02.000).ORGANISATION03";
    protected String              ORGANISATION_SCHEME_1_V2_ORGANISATION_4     = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME01(02.000).ORGANISATION04";
    protected String              ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1   = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME01(02.000).ORGANISATION0401";
    protected String              ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1_1 = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME01(02.000).ORGANISATION040101";
    protected String              ORGANISATION_SCHEME_2_V1_ORGANISATION_1     = "urn:sdmx:org.sdmx.infomodel.base.Agency=SDMX01:ORGANISATIONSCHEME02(01.000).ORGANISATION01";
    protected String              ORGANISATION_SCHEME_2_V1_ORGANISATION_2     = "urn:sdmx:org.sdmx.infomodel.base.Agency=SDMX01:ORGANISATIONSCHEME02(01.000).ORGANISATION02";
    protected String              ORGANISATION_SCHEME_3_V1_ORGANISATION_1     = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME03(01.000).ORGANISATION01";
    protected String              ORGANISATION_SCHEME_3_V1_ORGANISATION_2     = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME03(01.000).ORGANISATION02";
    protected String              ORGANISATION_SCHEME_3_V1_ORGANISATION_2_1   = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME03(01.000).ORGANISATION0201";
    protected String              ORGANISATION_SCHEME_3_V1_ORGANISATION_2_1_1 = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME03(01.000).ORGANISATION020101";
    protected String              ORGANISATION_SCHEME_3_V1_ORGANISATION_2_2   = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME03(01.000).ORGANISATION0202";
    protected String              ORGANISATION_SCHEME_4_V1_ORGANISATION_1     = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME04(01.000).ORGANISATION01";
    protected String              ORGANISATION_SCHEME_5_V1_ORGANISATION_1     = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME05(01.000).ORGANISATION01";
    protected String              ORGANISATION_SCHEME_6_V1_ORGANISATION_1     = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME06(01.000).ORGANISATION01";
    protected String              ORGANISATION_SCHEME_7_V2_ORGANISATION_1     = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME07(02.000).ORGANISATION01";
    protected String              ORGANISATION_SCHEME_8_V1_ORGANISATION_1     = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX02:ORGANISATIONSCHEME08(01.000).ORGANISATION01";
    protected String              ORGANISATION_SCHEME_9_V1_ORGANISATION_1     = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX02.ORGANISATION01:ORGANISATIONSCHEME09(01.000).ORGANISATION01";
    protected String              ORGANISATION_SCHEME_100_V1_ORGANISATION_1   = AGENCY_ROOT_1_V1;
    protected String              ORGANISATION_SCHEME_100_V1_ORGANISATION_2   = AGENCY_ROOT_2_V1;

    // Category schemes
    protected String              CATEGORY_SCHEME_1_V1                        = "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=SDMX01:CATEGORYSCHEME01(01.000)";
    protected String              CATEGORY_SCHEME_1_V2                        = "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=SDMX01:CATEGORYSCHEME01(02.000)";
    protected String              CATEGORY_SCHEME_2_V1                        = "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=SDMX01:CATEGORYSCHEME02(01.000)";
    protected String              CATEGORY_SCHEME_3_V1                        = "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=SDMX01:CATEGORYSCHEME03(01.000)";
    protected String              CATEGORY_SCHEME_4_V1                        = "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=SDMX01:CATEGORYSCHEME04(01.000)";
    protected String              CATEGORY_SCHEME_5_V1                        = "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=SDMX01:CATEGORYSCHEME05(01.000)";
    protected String              CATEGORY_SCHEME_6_V1                        = "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=SDMX01:CATEGORYSCHEME06(01.000)";
    protected String              CATEGORY_SCHEME_7_V1                        = "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=SDMX01:CATEGORYSCHEME07(01.000)";
    protected String              CATEGORY_SCHEME_7_V2                        = "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=SDMX01:CATEGORYSCHEME07(02.000)";

    // Categories
    protected String              CATEGORY_SCHEME_1_V1_CATEGORY_1             = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME01(01.000).CATEGORY01";
    protected String              CATEGORY_SCHEME_1_V2_CATEGORY_1             = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME01(02.000).CATEGORY01";
    protected String              CATEGORY_SCHEME_1_V2_CATEGORY_2             = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME01(02.000).CATEGORY02";
    protected String              CATEGORY_SCHEME_1_V2_CATEGORY_2_1           = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME01(02.000).CATEGORY02.CATEGORY0201";
    protected String              CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1         = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME01(02.000).CATEGORY02.CATEGORY0201.CATEGORY020101";
    protected String              CATEGORY_SCHEME_1_V2_CATEGORY_3             = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME01(02.000).CATEGORY03";
    protected String              CATEGORY_SCHEME_1_V2_CATEGORY_4             = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME01(02.000).CATEGORY04";
    protected String              CATEGORY_SCHEME_1_V2_CATEGORY_4_1           = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME01(02.000).CATEGORY04.CATEGORY0401";
    protected String              CATEGORY_SCHEME_1_V2_CATEGORY_4_1_1         = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME01(02.000).CATEGORY04.CATEGORY0401.CATEGORY040101";
    protected String              CATEGORY_SCHEME_2_V1_CATEGORY_1             = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME02(01.000).CATEGORY01";
    protected String              CATEGORY_SCHEME_2_V1_CATEGORY_2             = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME02(01.000).CATEGORY02";
    protected String              CATEGORY_SCHEME_3_V1_CATEGORY_1             = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME03(01.000).CATEGORY01";
    protected String              CATEGORY_SCHEME_3_V1_CATEGORY_2             = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME03(01.000).CATEGORY02";
    protected String              CATEGORY_SCHEME_3_V1_CATEGORY_2_1           = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME03(01.000).CATEGORY02.CATEGORY0201";
    protected String              CATEGORY_SCHEME_3_V1_CATEGORY_2_1_1         = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME03(01.000).CATEGORY02.CATEGORY0201.CATEGORY020101";
    protected String              CATEGORY_SCHEME_3_V1_CATEGORY_2_2           = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME03(01.000).CATEGORY02.CATEGORY0202";
    protected String              CATEGORY_SCHEME_4_V1_CATEGORY_1             = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME04(01.000).CATEGORY01";
    protected String              CATEGORY_SCHEME_5_V1_CATEGORY_1             = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME05(01.000).CATEGORY01";
    protected String              CATEGORY_SCHEME_5_V1_CATEGORY_2             = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME05(01.000).CATEGORY02";
    protected String              CATEGORY_SCHEME_6_V1_CATEGORY_1             = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME06(01.000).CATEGORY01";
    protected String              CATEGORY_SCHEME_7_V2_CATEGORY_1             = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME07(02.000).CATEGORY01";

    // Codelists
    protected static final String CODELIST_1_V1                               = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST01(01.000)";
    protected static final String CODELIST_1_V2                               = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST01(02.000)";
    protected static final String CODELIST_2_V1                               = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST02(01.000)";
    protected static final String CODELIST_3_V1                               = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST03(01.000)";
    protected static final String CODELIST_4_V1                               = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST04(01.000)";
    protected static final String CODELIST_5_V1                               = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST05(01.000)";
    protected static final String CODELIST_6_V1                               = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST06(01.000)";
    protected static final String CODELIST_7_V1                               = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST07(01.000)";
    protected static final String CODELIST_7_V2                               = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST07(02.000)";
    protected static final String CODELIST_8_V1                               = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST08(01.000)";
    protected static final String CODELIST_9_V1                               = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST09(01.000)";
    protected static final String CODELIST_10_V1                              = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST10(01.000)";
    protected static final String CODELIST_10_V2                              = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST10(02.000)";
    protected static final String CODELIST_10_V3                              = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST10(03.000)";
    protected static final String CODELIST_11_V1                              = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST11(01.000)";
    protected static final String CODELIST_12_V1                              = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST12(01.000)";
    protected static final String CODELIST_13_V1                              = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST13(01.000)";

    // Codes
    protected static final String CODELIST_1_V1_CODE_1                        = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST01(01.000).CODE01";
    protected static final String CODELIST_1_V2_CODE_1                        = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST01(02.000).CODE01";
    protected static final String CODELIST_1_V2_CODE_2                        = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST01(02.000).CODE02";
    protected static final String CODELIST_1_V2_CODE_2_1                      = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST01(02.000).CODE0201";
    protected static final String CODELIST_1_V2_CODE_2_1_1                    = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST01(02.000).CODE020101";
    protected static final String CODELIST_1_V2_CODE_3                        = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST01(02.000).CODE03";
    protected static final String CODELIST_1_V2_CODE_4                        = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST01(02.000).CODE04";
    protected static final String CODELIST_1_V2_CODE_4_1                      = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST01(02.000).CODE0401";
    protected static final String CODELIST_1_V2_CODE_4_1_1                    = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST01(02.000).CODE040101";
    protected static final String CODELIST_2_V1_CODE_1                        = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST02(01.000).CODE01";
    protected static final String CODELIST_2_V1_CODE_2                        = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST02(01.000).CODE02";
    protected static final String CODELIST_3_V1_CODE_1                        = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(01.000).CODE01";
    protected static final String CODELIST_3_V1_CODE_2                        = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(01.000).CODE02";
    protected static final String CODELIST_3_V1_CODE_2_1                      = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(01.000).CODE0201";
    protected static final String CODELIST_3_V1_CODE_2_1_1                    = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(01.000).CODE020101";
    protected static final String CODELIST_3_V1_CODE_2_2                      = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(01.000).CODE0202";
    protected static final String CODELIST_4_V1_CODE_1                        = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST04(01.000).CODE01";
    protected static final String CODELIST_5_V1_CODE_1                        = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST05(01.000).CODE01";
    protected static final String CODELIST_6_V1_CODE_1                        = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST06(01.000).CODE01";
    protected static final String CODELIST_7_V2_CODE_1                        = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST07(02.000).CODE01";
    protected static final String CODELIST_8_V1_CODE_1                        = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST08(01.000).CODE01";
    protected static final String CODELIST_10_V2_CODE_1                       = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST10(02.000).CODE01";
    protected static final String CODELIST_10_V3_CODE_1                       = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST10(03.000).CODE01";
    protected static final String CODELIST_11_V1_CODE_1                       = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST11(01.000).CODE01";
    protected static final String CODELIST_12_V1_CODE_1                       = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST12(01.000).CODE01";
    protected static final String CODELIST_13_V1_CODE_1                       = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST13(01.000).CODE01";
    protected static final String CODELIST_13_V1_CODE_2                       = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST13(01.000).CODE02";
    protected static final String CODELIST_13_V1_CODE_3                       = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST13(01.000).CODE03";

    // Codelist families
    protected static final String CODELIST_FAMILY_1                           = "codelist-family-1";

    // Other
    protected static final String NOT_EXISTS                                  = "not-exists";

    @Value("${metamac.srm.db.provider}")
    private String                databaseProvider;

    // --------------------------------------------------------------------------------------------------------------
    // SERVICE CONTEXT
    // --------------------------------------------------------------------------------------------------------------

    @Override
    protected ServiceContext getServiceContextAdministrador() {
        ServiceContext serviceContext = getServiceContextWithoutPrincipal();
        putMetamacPrincipalInServiceContext(serviceContext, SrmRoleEnum.ADMINISTRADOR);
        return serviceContext;
    }

    protected ServiceContext getServiceContextTecnicoApoyoNormalizacion() {
        ServiceContext serviceContext = getServiceContextWithoutPrincipal();
        putMetamacPrincipalInServiceContext(serviceContext, SrmRoleEnum.TECNICO_APOYO_NORMALIZACION);
        return serviceContext;
    }

    protected ServiceContext getServiceContextTecnicoNormalizacion() {
        ServiceContext serviceContext = getServiceContextWithoutPrincipal();
        putMetamacPrincipalInServiceContext(serviceContext, SrmRoleEnum.TECNICO_NORMALIZACION);
        return serviceContext;
    }

    protected ServiceContext getServiceContextJefeNormalizacion() {
        ServiceContext serviceContext = getServiceContextWithoutPrincipal();
        putMetamacPrincipalInServiceContext(serviceContext, SrmRoleEnum.JEFE_NORMALIZACION);
        return serviceContext;
    }

    protected ServiceContext getServiceContextJefeNormalizacionWithOperation1() {
        ServiceContext serviceContext = getServiceContextWithoutPrincipal();
        putMetamacPrincipalInServiceContext(serviceContext, SrmRoleEnum.JEFE_NORMALIZACION, "Operation1");
        return serviceContext;
    }

    protected ServiceContext getServiceContextTecnicoApoyoProduccion() {
        ServiceContext serviceContext = getServiceContextWithoutPrincipal();
        putMetamacPrincipalInServiceContext(serviceContext, SrmRoleEnum.TECNICO_APOYO_PRODUCCION);
        return serviceContext;
    }

    protected ServiceContext getServiceContextTecnicoProduccion() {
        ServiceContext serviceContext = getServiceContextWithoutPrincipal();
        putMetamacPrincipalInServiceContext(serviceContext, SrmRoleEnum.TECNICO_PRODUCCION);
        return serviceContext;
    }

    protected ServiceContext getServiceContextJefeProduccion() {
        ServiceContext serviceContext = getServiceContextWithoutPrincipal();
        putMetamacPrincipalInServiceContext(serviceContext, SrmRoleEnum.JEFE_PRODUCCION);
        return serviceContext;
    }

    protected ServiceContext getServiceContextWithoutAccesses() {
        ServiceContext ctxWithoutAcceses = getServiceContextWithoutPrincipal();

        MetamacPrincipal metamacPrincipal = new MetamacPrincipal();
        metamacPrincipal.setUserId(ctxWithoutAcceses.getUserId());
        metamacPrincipal.getAccesses().clear();
        ctxWithoutAcceses.setProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE, metamacPrincipal);
        return ctxWithoutAcceses;
    }

    protected ServiceContext getServiceContextWithoutAccessToApplication() {

        ServiceContext ctxWithoutAccessToApplication = getServiceContextWithoutPrincipal();

        MetamacPrincipal metamacPrincipal = new MetamacPrincipal();
        metamacPrincipal.setUserId(ctxWithoutAccessToApplication.getUserId());
        metamacPrincipal.getAccesses().add(new MetamacPrincipalAccess(SrmRoleEnum.JEFE_PRODUCCION.getName(), NOT_EXISTS, null));
        ctxWithoutAccessToApplication.setProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE, metamacPrincipal);
        return ctxWithoutAccessToApplication;
    }

    protected ServiceContext getServiceContextWithoutSrmRole() {
        ServiceContext ctxWithoutAccessToApplication = getServiceContextWithoutPrincipal();

        MetamacPrincipal metamacPrincipal = new MetamacPrincipal();
        metamacPrincipal.setUserId(ctxWithoutAccessToApplication.getUserId());
        metamacPrincipal.getAccesses().add(new MetamacPrincipalAccess(NOT_EXISTS, SrmConstants.SECURITY_APPLICATION_ID, null));
        ctxWithoutAccessToApplication.setProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE, metamacPrincipal);
        return ctxWithoutAccessToApplication;
    }

    private void putMetamacPrincipalInServiceContext(ServiceContext serviceContext, SrmRoleEnum role) {
        putMetamacPrincipalInServiceContext(serviceContext, role, null);
    }

    private void putMetamacPrincipalInServiceContext(ServiceContext serviceContext, SrmRoleEnum role, String operation) {
        MetamacPrincipal metamacPrincipal = new MetamacPrincipal();
        metamacPrincipal.setUserId(serviceContext.getUserId());
        metamacPrincipal.getAccesses().add(new MetamacPrincipalAccess(role.getName(), SrmConstants.SECURITY_APPLICATION_ID, operation));
        serviceContext.setProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE, metamacPrincipal);
    }

    // --------------------------------------------------------------------------------------------------------------
    // DBUNIT CONFIGURATION
    // --------------------------------------------------------------------------------------------------------------

    @Override
    protected List<String> getTableNamesOrderedByFKDependency() {
        List<String> tables = new ArrayList<String>();
        tables.addAll(super.getTableNamesOrderedByFKDependency());
        tables.add("TB_M_LIS_CONCEPT_TYPES");
        tables.add("TB_M_CONCEPTS");
        tables.add("TB_M_CONCEPT_ROLES");
        tables.add("TB_M_CONCEPT_RELATED");
        tables.add("TB_M_CONCEPT_SCHEMES_VERSIONS");
        tables.add("TB_M_DATASTRUCTURE_VERSIONS");
        tables.add("TB_M_ORGANISATIONS");
        tables.add("TB_M_ORG_SCHEMES_VERSIONS");
        tables.add("TB_M_CATEGORIES");
        tables.add("TB_M_CAT_SCHEMES_VERSIONS");
        tables.add("TB_M_CODELIST_FAMILIES");
        tables.add("TB_M_CODES");
        tables.add("TB_M_CODELISTS_VERSIONS");
        return tables;
    }

    @Override
    protected Map<String, List<String>> getTablePrimaryKeys() {
        Map<String, List<String>> tablePrimaryKeys = super.getTablePrimaryKeys();
        tablePrimaryKeys.put("TB_M_CONCEPT_SCHEMES_VERSIONS", Arrays.asList("TB_CONCEPT_SCHEMES_VERSIONS"));
        tablePrimaryKeys.put("TB_M_CONCEPTS", Arrays.asList("TB_CONCEPTS"));
        tablePrimaryKeys.put("TB_M_ORG_SCHEMES_VERSIONS", Arrays.asList("TB_ORG_SCHEMES_VERSIONS"));
        tablePrimaryKeys.put("TB_M_ORGANISATIONS", Arrays.asList("TB_ORGANISATIONS"));
        tablePrimaryKeys.put("TB_M_CAT_SCHEMES_VERSIONS", Arrays.asList("TB_CAT_SCHEMES_VERSIONS"));
        tablePrimaryKeys.put("TB_M_CATEGORIES", Arrays.asList("TB_CATEGORIES"));
        tablePrimaryKeys.put("TB_M_CODELISTS_VERSIONS", Arrays.asList("TB_CODELISTS_VERSIONS"));
        tablePrimaryKeys.put("TB_M_CODES", Arrays.asList("TB_CODES"));
        tablePrimaryKeys.put("TB_M_CONCEPT_ROLES", Arrays.asList("CONCEPT_ROLE_FK", "CONCEPT_FK"));
        tablePrimaryKeys.put("TB_M_CONCEPT_RELATED", Arrays.asList("CONCEPT_RELATED_FK", "CONCEPT_FK"));
        return tablePrimaryKeys;
    }

    @Override
    protected ServiceContext getServiceContextWithoutPrincipal() {
        return new ServiceContext("junit", "junit", "app");
    }

    @Override
    protected DataBaseProvider getDatabaseProvider() {
        return DataBaseProvider.valueOf(databaseProvider);
    }
}
