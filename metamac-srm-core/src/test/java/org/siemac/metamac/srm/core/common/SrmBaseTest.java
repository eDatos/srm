package org.siemac.metamac.srm.core.common;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaOrder;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaOrder.OrderTypeEnum;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.exception.CommonServiceExceptionType;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.base.dto.ItemMetamacBasicDto;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.sso.client.MetamacPrincipal;
import org.siemac.metamac.sso.client.MetamacPrincipalAccess;
import org.siemac.metamac.sso.client.SsoClientConstants;
import org.springframework.beans.factory.annotation.Value;

import com.arte.statistic.sdmx.srm.core.common.SdmxSrmBaseTest;
import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;

public abstract class SrmBaseTest extends SdmxSrmBaseTest {

    // Agencies
    protected static final String ORGANISATION_SCHEME_ROOT_1_V1                        = "urn:sdmx:org.sdmx.infomodel.base.AgencyScheme=SDMX:AGENCIES(1.0)";
    public static final String    AGENCY_ROOT_1_V1                                     = "urn:sdmx:org.sdmx.infomodel.base.Agency=SDMX:AGENCIES(1.0).SDMX01";
    public static final String    AGENCY_ROOT_2_V1                                     = "urn:sdmx:org.sdmx.infomodel.base.Agency=SDMX:AGENCIES(1.0).SDMX02";
    public static final String    AGENCY_ROOT_1_V1_CODE                                = "SDMX01";

    // Concepts schemes
    protected static final String CONCEPT_SCHEME_1_V1                                  = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME01(01.000)";
    protected static final String CONCEPT_SCHEME_1_V2                                  = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME01(02.000)";
    protected static final String CONCEPT_SCHEME_2_V1                                  = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME02(01.000)";
    protected static final String CONCEPT_SCHEME_3_V1                                  = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME03(01.000)";
    protected static final String CONCEPT_SCHEME_4_V1                                  = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME04(01.000)";
    protected static final String CONCEPT_SCHEME_5_V1                                  = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME05(01.000)";
    protected static final String CONCEPT_SCHEME_6_V1                                  = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME06(01.000)";
    protected static final String CONCEPT_SCHEME_7_V1                                  = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME07(01.000)";
    protected static final String CONCEPT_SCHEME_7_V2                                  = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME07(02.000)";
    protected static final String CONCEPT_SCHEME_8_V1                                  = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME08(01.000)";
    protected static final String CONCEPT_SCHEME_9_V1                                  = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME09(01.000)";
    protected static final String CONCEPT_SCHEME_10_V1                                 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME10(01.000)";
    protected static final String CONCEPT_SCHEME_10_V2                                 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME10(02.000)";
    protected static final String CONCEPT_SCHEME_10_V3                                 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME10(03.000)";
    protected static final String CONCEPT_SCHEME_11_V1                                 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME11(01.000)";
    protected static final String CONCEPT_SCHEME_12_V1                                 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME12(01.000)";
    protected static final String CONCEPT_SCHEME_13_V1                                 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME13(01.000)";
    protected static final String CONCEPT_SCHEME_14_V1                                 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX02:CONCEPTSCHEME14(01.000)";
    protected static final String CONCEPT_SCHEME_15_V1                                 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX02:CONCEPTSCHEME15(01.000)";
    protected static final String CONCEPT_SCHEME_16_V1                                 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX02:CONCEPTSCHEME16(01.000)";
    protected static final String CONCEPT_SCHEME_17_V1                                 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX02:CONCEPTSCHEME17(01.000)";

    // Concepts
    protected static final String CONCEPT_SCHEME_1_V1_CONCEPT_1                        = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME01(01.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_1_V2_CONCEPT_1                        = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME01(02.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_1_V2_CONCEPT_2                        = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME01(02.000).CONCEPT02";
    protected static final String CONCEPT_SCHEME_1_V2_CONCEPT_2_1                      = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME01(02.000).CONCEPT0201";
    protected static final String CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1                    = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME01(02.000).CONCEPT020101";
    protected static final String CONCEPT_SCHEME_1_V2_CONCEPT_3                        = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME01(02.000).CONCEPT03";
    protected static final String CONCEPT_SCHEME_1_V2_CONCEPT_4                        = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME01(02.000).CONCEPT04";
    protected static final String CONCEPT_SCHEME_1_V2_CONCEPT_4_1                      = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME01(02.000).CONCEPT0401";
    protected static final String CONCEPT_SCHEME_1_V2_CONCEPT_4_1_1                    = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME01(02.000).CONCEPT040101";
    protected static final String CONCEPT_SCHEME_2_V1_CONCEPT_1                        = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME02(01.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_2_V1_CONCEPT_2                        = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME02(01.000).CONCEPT02";
    protected static final String CONCEPT_SCHEME_3_V1_CONCEPT_1                        = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME03(01.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_3_V1_CONCEPT_2                        = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME03(01.000).CONCEPT02";
    protected static final String CONCEPT_SCHEME_3_V1_CONCEPT_2_1                      = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME03(01.000).CONCEPT0201";
    protected static final String CONCEPT_SCHEME_3_V1_CONCEPT_2_1_1                    = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME03(01.000).CONCEPT020101";
    protected static final String CONCEPT_SCHEME_3_V1_CONCEPT_2_2                      = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME03(01.000).CONCEPT0202";
    protected static final String CONCEPT_SCHEME_4_V1_CONCEPT_1                        = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME04(01.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_5_V1_CONCEPT_1                        = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME05(01.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_6_V1_CONCEPT_1                        = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME06(01.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_7_V1_CONCEPT_1                        = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME07(01.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_7_V2_CONCEPT_1                        = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME07(02.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_8_V1_CONCEPT_1                        = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME08(01.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_10_V2_CONCEPT_1                       = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME10(02.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_10_V3_CONCEPT_1                       = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME10(03.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_11_V1_CONCEPT_1                       = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME11(01.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_12_V1_CONCEPT_1                       = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME12(01.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_13_V1_CONCEPT_1                       = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME13(01.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_13_V1_CONCEPT_2                       = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME13(01.000).CONCEPT02";
    protected static final String CONCEPT_SCHEME_13_V1_CONCEPT_3                       = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME13(01.000).CONCEPT03";
    protected static final String CONCEPT_SCHEME_14_V1_CONCEPT_1                       = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX02:CONCEPTSCHEME14(01.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_14_V1_CONCEPT_1_1                     = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX02:CONCEPTSCHEME14(01.000).CONCEPT0101";
    protected static final String CONCEPT_SCHEME_14_V1_CONCEPT_2                       = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX02:CONCEPTSCHEME14(01.000).CONCEPT02";
    protected static final String CONCEPT_SCHEME_14_V1_CONCEPT_3                       = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX02:CONCEPTSCHEME14(01.000).CONCEPT03";
    protected static final String CONCEPT_SCHEME_15_V1_CONCEPT_1                       = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX02:CONCEPTSCHEME15(01.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_15_V1_CONCEPT_2                       = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX02:CONCEPTSCHEME15(01.000).CONCEPT02";
    protected static final String CONCEPT_SCHEME_15_V1_CONCEPT_3                       = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX02:CONCEPTSCHEME15(01.000).CONCEPT03";
    protected static final String CONCEPT_SCHEME_16_V1_CONCEPT_1                       = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX02:CONCEPTSCHEME16(01.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_16_V1_CONCEPT_2                       = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX02:CONCEPTSCHEME16(01.000).CONCEPT02";
    protected static final String CONCEPT_SCHEME_17_V1_CONCEPT_1                       = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX02:CONCEPTSCHEME17(01.000).CONCEPT01";
    protected static final String CONCEPT_SCHEME_17_V1_CONCEPT_2                       = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX02:CONCEPTSCHEME17(01.000).CONCEPT02";

    // Concept types
    protected static final String CONCEPT_TYPE_DERIVED                                 = "DERIVED";
    protected static final String CONCEPT_TYPE_DIRECT                                  = "DIRECT";

    // Organisations schemes
    protected static final String ORGANISATION_SCHEME_1_V1                             = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnitScheme=SDMX01:ORGANISATIONSCHEME01(01.000)";
    protected static final String ORGANISATION_SCHEME_1_V2                             = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnitScheme=SDMX01:ORGANISATIONSCHEME01(02.000)";
    protected static final String ORGANISATION_SCHEME_2_V1                             = "urn:sdmx:org.sdmx.infomodel.base.DataConsumerScheme=SDMX01:DATA_CONSUMERS(01.000)";
    protected static final String ORGANISATION_SCHEME_3_V1                             = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnitScheme=SDMX01:ORGANISATIONSCHEME03(01.000)";
    protected static final String ORGANISATION_SCHEME_4_V1                             = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnitScheme=SDMX01:ORGANISATIONSCHEME04(01.000)";
    protected static final String ORGANISATION_SCHEME_5_V1                             = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnitScheme=SDMX01:ORGANISATIONSCHEME05(01.000)";
    protected static final String ORGANISATION_SCHEME_6_V1                             = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnitScheme=SDMX01:ORGANISATIONSCHEME06(01.000)";
    protected static final String ORGANISATION_SCHEME_7_V1                             = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnitScheme=SDMX01:ORGANISATIONSCHEME07(01.000)";
    protected static final String ORGANISATION_SCHEME_7_V2                             = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnitScheme=SDMX01:ORGANISATIONSCHEME07(02.000)";
    protected static final String ORGANISATION_SCHEME_8_V1                             = "urn:sdmx:org.sdmx.infomodel.base.AgencyScheme=SDMX02:AGENCIES(01.000)";
    protected static final String ORGANISATION_SCHEME_9_V1                             = "urn:sdmx:org.sdmx.infomodel.base.DataProviderScheme=SDMX01:DATA_PROVIDERS(01.000)";
    protected static final String ORGANISATION_SCHEME_10_V1                            = "urn:sdmx:org.sdmx.infomodel.base.AgencyScheme=SDMX02.ORGANISATION01:AGENCIES(01.000)";
    protected static final String ORGANISATION_SCHEME_11_V1                            = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnitScheme=SDMX02:ORGANISATIONSCHEME11(01.000)";
    protected static final String ORGANISATION_SCHEME_100_V1                           = ORGANISATION_SCHEME_ROOT_1_V1;

    // Organisations
    protected String              ORGANISATION_SCHEME_1_V1_ORGANISATION_1              = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME01(01.000).ORGANISATION01";
    protected String              ORGANISATION_SCHEME_1_V2_ORGANISATION_1              = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME01(02.000).ORGANISATION01";
    protected String              ORGANISATION_SCHEME_1_V2_ORGANISATION_2              = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME01(02.000).ORGANISATION02";
    protected String              ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1            = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME01(02.000).ORGANISATION0201";
    protected String              ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1_1          = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME01(02.000).ORGANISATION020101";
    protected String              ORGANISATION_SCHEME_1_V2_ORGANISATION_3              = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME01(02.000).ORGANISATION03";
    protected String              ORGANISATION_SCHEME_1_V2_ORGANISATION_4              = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME01(02.000).ORGANISATION04";
    protected String              ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1            = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME01(02.000).ORGANISATION0401";
    protected String              ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1_1          = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME01(02.000).ORGANISATION040101";
    protected String              ORGANISATION_SCHEME_2_V1_ORGANISATION_1              = "urn:sdmx:org.sdmx.infomodel.base.DataConsumer=SDMX01:DATA_CONSUMERS(01.000).ORGANISATION01";
    protected String              ORGANISATION_SCHEME_2_V1_ORGANISATION_2              = "urn:sdmx:org.sdmx.infomodel.base.DataConsumer=SDMX01:DATA_CONSUMERS(01.000).ORGANISATION02";
    protected String              ORGANISATION_SCHEME_3_V1_ORGANISATION_1              = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME03(01.000).ORGANISATION01";
    protected String              ORGANISATION_SCHEME_3_V1_ORGANISATION_2              = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME03(01.000).ORGANISATION02";
    protected String              ORGANISATION_SCHEME_3_V1_ORGANISATION_2_1            = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME03(01.000).ORGANISATION0201";
    protected String              ORGANISATION_SCHEME_3_V1_ORGANISATION_2_1_1          = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME03(01.000).ORGANISATION020101";
    protected String              ORGANISATION_SCHEME_3_V1_ORGANISATION_2_2            = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME03(01.000).ORGANISATION0202";
    protected String              ORGANISATION_SCHEME_4_V1_ORGANISATION_1              = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME04(01.000).ORGANISATION01";
    protected String              ORGANISATION_SCHEME_5_V1_ORGANISATION_1              = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME05(01.000).ORGANISATION01";
    protected String              ORGANISATION_SCHEME_6_V1_ORGANISATION_1              = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME06(01.000).ORGANISATION01";
    protected String              ORGANISATION_SCHEME_7_V2_ORGANISATION_1              = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME07(02.000).ORGANISATION01";
    protected String              ORGANISATION_SCHEME_8_V1_ORGANISATION_1              = "urn:sdmx:org.sdmx.infomodel.base.Agency=SDMX02:AGENCIES(01.000).ORGANISATION01";
    protected String              ORGANISATION_SCHEME_9_V1_ORGANISATION_1              = "urn:sdmx:org.sdmx.infomodel.base.DataProvider=SDMX01:DATA_PROVIDERS(01.000).ORGANISATION01";
    protected String              ORGANISATION_SCHEME_9_V1_ORGANISATION_2              = "urn:sdmx:org.sdmx.infomodel.base.DataProvider=SDMX01:DATA_PROVIDERS(01.000).ORGANISATION02";
    protected String              ORGANISATION_SCHEME_10_V1_ORGANISATION_1             = "urn:sdmx:org.sdmx.infomodel.base.Agency=SDMX02.ORGANISATION01:AGENCIES(01.000).ORGANISATION01";
    protected String              ORGANISATION_SCHEME_11_V1_ORGANISATION_1             = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX02:ORGANISATIONSCHEME11(01.000).ORGANISATION01";
    protected String              ORGANISATION_SCHEME_11_V1_ORGANISATION_1_1           = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX02:ORGANISATIONSCHEME11(01.000).ORGANISATION0101";
    protected String              ORGANISATION_SCHEME_11_V1_ORGANISATION_2             = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX02:ORGANISATIONSCHEME11(01.000).ORGANISATION02";
    protected String              ORGANISATION_SCHEME_11_V1_ORGANISATION_3             = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX02:ORGANISATIONSCHEME11(01.000).ORGANISATION03";
    protected String              ORGANISATION_SCHEME_100_V1_ORGANISATION_1            = AGENCY_ROOT_1_V1;
    protected String              ORGANISATION_SCHEME_100_V1_ORGANISATION_2            = AGENCY_ROOT_2_V1;

    // Category schemes
    protected String              CATEGORY_SCHEME_1_V1                                 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=SDMX01:CATEGORYSCHEME01(01.000)";
    protected String              CATEGORY_SCHEME_1_V2                                 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=SDMX01:CATEGORYSCHEME01(02.000)";
    protected String              CATEGORY_SCHEME_2_V1                                 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=SDMX01:CATEGORYSCHEME02(01.000)";
    protected String              CATEGORY_SCHEME_3_V1                                 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=SDMX01:CATEGORYSCHEME03(01.000)";
    protected String              CATEGORY_SCHEME_4_V1                                 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=SDMX01:CATEGORYSCHEME04(01.000)";
    protected String              CATEGORY_SCHEME_5_V1                                 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=SDMX01:CATEGORYSCHEME05(01.000)";
    protected String              CATEGORY_SCHEME_6_V1                                 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=SDMX01:CATEGORYSCHEME06(01.000)";
    protected String              CATEGORY_SCHEME_7_V1                                 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=SDMX01:CATEGORYSCHEME07(01.000)";
    protected String              CATEGORY_SCHEME_7_V2                                 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=SDMX01:CATEGORYSCHEME07(02.000)";
    protected String              CATEGORY_SCHEME_8_V1                                 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=SDMX02:CATEGORYSCHEME08(01.000)";

    // Categories
    protected String              CATEGORY_SCHEME_1_V1_CATEGORY_1                      = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME01(01.000).CATEGORY01";
    protected String              CATEGORY_SCHEME_1_V2_CATEGORY_1                      = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME01(02.000).CATEGORY01";
    protected String              CATEGORY_SCHEME_1_V2_CATEGORY_2                      = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME01(02.000).CATEGORY02";
    protected String              CATEGORY_SCHEME_1_V2_CATEGORY_2_1                    = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME01(02.000).CATEGORY02.CATEGORY0201";
    protected String              CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1                  = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME01(02.000).CATEGORY02.CATEGORY0201.CATEGORY020101";
    protected String              CATEGORY_SCHEME_1_V2_CATEGORY_3                      = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME01(02.000).CATEGORY03";
    protected String              CATEGORY_SCHEME_1_V2_CATEGORY_4                      = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME01(02.000).CATEGORY04";
    protected String              CATEGORY_SCHEME_1_V2_CATEGORY_4_1                    = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME01(02.000).CATEGORY04.CATEGORY0401";
    protected String              CATEGORY_SCHEME_1_V2_CATEGORY_4_1_1                  = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME01(02.000).CATEGORY04.CATEGORY0401.CATEGORY040101";
    protected String              CATEGORY_SCHEME_2_V1_CATEGORY_1                      = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME02(01.000).CATEGORY01";
    protected String              CATEGORY_SCHEME_2_V1_CATEGORY_2                      = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME02(01.000).CATEGORY02";
    protected String              CATEGORY_SCHEME_3_V1_CATEGORY_1                      = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME03(01.000).CATEGORY01";
    protected String              CATEGORY_SCHEME_3_V1_CATEGORY_2                      = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME03(01.000).CATEGORY02";
    protected String              CATEGORY_SCHEME_3_V1_CATEGORY_2_1                    = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME03(01.000).CATEGORY02.CATEGORY0201";
    protected String              CATEGORY_SCHEME_3_V1_CATEGORY_2_1_1                  = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME03(01.000).CATEGORY02.CATEGORY0201.CATEGORY020101";
    protected String              CATEGORY_SCHEME_3_V1_CATEGORY_2_2                    = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME03(01.000).CATEGORY02.CATEGORY0202";
    protected String              CATEGORY_SCHEME_4_V1_CATEGORY_1                      = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME04(01.000).CATEGORY01";
    protected String              CATEGORY_SCHEME_5_V1_CATEGORY_1                      = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME05(01.000).CATEGORY01";
    protected String              CATEGORY_SCHEME_5_V1_CATEGORY_2                      = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME05(01.000).CATEGORY02";
    protected String              CATEGORY_SCHEME_6_V1_CATEGORY_1                      = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME06(01.000).CATEGORY01";
    protected String              CATEGORY_SCHEME_7_V2_CATEGORY_1                      = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME07(02.000).CATEGORY01";
    protected String              CATEGORY_SCHEME_8_V1_CATEGORY_1                      = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX02:CATEGORYSCHEME08(01.000).CATEGORY01";
    protected String              CATEGORY_SCHEME_8_V1_CATEGORY_1_1                    = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX02:CATEGORYSCHEME08(01.000).CATEGORY01.CATEGORY0101";
    protected String              CATEGORY_SCHEME_8_V1_CATEGORY_2                      = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX02:CATEGORYSCHEME08(01.000).CATEGORY02";
    protected String              CATEGORY_SCHEME_8_V1_CATEGORY_3                      = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX02:CATEGORYSCHEME08(01.000).CATEGORY03";

    // Codelists
    protected static final String CODELIST_1_V1                                        = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST01(01.000)";
    protected static final String CODELIST_1_V2                                        = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST01(02.000)";
    protected static final String CODELIST_2_V1                                        = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST02(01.000)";
    protected static final String CODELIST_3_V1                                        = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST03(01.000)";
    protected static final String CODELIST_4_V1                                        = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST04(01.000)";
    protected static final String CODELIST_5_V1                                        = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST05(01.000)";
    protected static final String CODELIST_6_V1                                        = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST06(01.000)";
    protected static final String CODELIST_7_V1                                        = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST07(01.000)";
    protected static final String CODELIST_7_V2                                        = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST07(02.000)";
    protected static final String CODELIST_8_V1                                        = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST08(01.000)";
    protected static final String CODELIST_9_V1                                        = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST09(01.000)";
    protected static final String CODELIST_10_V1                                       = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST10(01.000)";
    protected static final String CODELIST_10_V2                                       = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST10(02.000)";
    protected static final String CODELIST_10_V3                                       = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST10(03.000)";
    protected static final String CODELIST_11_V1                                       = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST11(01.000)";
    protected static final String CODELIST_12_V1                                       = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST12(01.000)";
    protected static final String CODELIST_13_V1                                       = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST13(01.000)";
    protected static final String CODELIST_14_V1                                       = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX02:CODELIST14(01.000)";

    // Codes
    protected static final String CODELIST_1_V1_CODE_1                                 = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST01(01.000).CODE01";
    protected static final String CODELIST_1_V2_CODE_1                                 = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST01(02.000).CODE01";
    protected static final String CODELIST_1_V2_CODE_2                                 = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST01(02.000).CODE02";
    protected static final String CODELIST_1_V2_CODE_2_1                               = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST01(02.000).CODE0201";
    protected static final String CODELIST_1_V2_CODE_2_1_1                             = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST01(02.000).CODE020101";
    protected static final String CODELIST_1_V2_CODE_2_2                               = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST01(02.000).CODE0202";
    protected static final String CODELIST_1_V2_CODE_3                                 = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST01(02.000).CODE03";
    protected static final String CODELIST_1_V2_CODE_4                                 = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST01(02.000).CODE04";
    protected static final String CODELIST_1_V2_CODE_4_1                               = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST01(02.000).CODE0401";
    protected static final String CODELIST_1_V2_CODE_4_1_1                             = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST01(02.000).CODE040101";
    protected static final String CODELIST_2_V1_CODE_1                                 = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST02(01.000).CODE01";
    protected static final String CODELIST_2_V1_CODE_2                                 = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST02(01.000).CODE02";
    protected static final String CODELIST_3_V1_CODE_1                                 = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(01.000).CODE01";
    protected static final String CODELIST_3_V1_CODE_2                                 = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(01.000).CODE02";
    protected static final String CODELIST_3_V1_CODE_2_1                               = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(01.000).CODE0201";
    protected static final String CODELIST_3_V1_CODE_2_1_1                             = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(01.000).CODE020101";
    protected static final String CODELIST_3_V1_CODE_2_2                               = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(01.000).CODE0202";
    protected static final String CODELIST_4_V1_CODE_1                                 = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST04(01.000).CODE01";
    protected static final String CODELIST_5_V1_CODE_1                                 = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST05(01.000).CODE01";
    protected static final String CODELIST_6_V1_CODE_1                                 = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST06(01.000).CODE01";
    protected static final String CODELIST_7_V2_CODE_1                                 = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST07(02.000).CODE01";
    protected static final String CODELIST_8_V1_CODE_1                                 = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST08(01.000).CODE01";
    protected static final String CODELIST_10_V2_CODE_1                                = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST10(02.000).CODE01";
    protected static final String CODELIST_10_V3_CODE_1                                = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST10(03.000).CODE01";
    protected static final String CODELIST_11_V1_CODE_1                                = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST11(01.000).CODE01";
    protected static final String CODELIST_12_V1_CODE_1                                = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST12(01.000).CODE01";
    protected static final String CODELIST_13_V1_CODE_1                                = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST13(01.000).CODE01";
    protected static final String CODELIST_13_V1_CODE_2                                = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST13(01.000).CODE02";
    protected static final String CODELIST_13_V1_CODE_3                                = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST13(01.000).CODE03";
    protected static final String CODELIST_14_V1_CODE_1                                = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX02:CODELIST14(01.000).CODE01";
    protected static final String CODELIST_14_V1_CODE_1_1                              = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX02:CODELIST14(01.000).CODE0101";
    protected static final String CODELIST_14_V1_CODE_2                                = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX02:CODELIST14(01.000).CODE02";
    protected static final String CODELIST_14_V1_CODE_3                                = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX02:CODELIST14(01.000).CODE03";

    // Codelist families
    protected static final String CODELIST_FAMILY_1                                    = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.CodelistFamily=CODELIST_FAMILY_01";
    protected static final String CODELIST_FAMILY_2                                    = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.CodelistFamily=CODELIST_FAMILY_02";

    // Variable families
    protected static final String VARIABLE_FAMILY_1                                    = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableFamily=VARIABLE_FAMILY_01";
    protected static final String VARIABLE_FAMILY_2                                    = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableFamily=VARIABLE_FAMILY_02";
    protected static final String VARIABLE_FAMILY_3                                    = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableFamily=VARIABLE_FAMILY_03";
    protected static final String VARIABLE_FAMILY_4                                    = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableFamily=VARIABLE_FAMILY_04";

    // Variables
    protected static final String VARIABLE_1                                           = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.Variable=VARIABLE_01";
    protected static final String VARIABLE_2                                           = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.Variable=VARIABLE_02";
    protected static final String VARIABLE_3                                           = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.Variable=VARIABLE_03";
    protected static final String VARIABLE_4                                           = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.Variable=VARIABLE_04";
    protected static final String VARIABLE_5                                           = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.Variable=VARIABLE_05";
    protected static final String VARIABLE_6                                           = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.Variable=VARIABLE_06";

    // Variable elements
    protected static final String VARIABLE_2_VARIABLE_ELEMENT_1                        = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableElement=VARIABLE_02.VARIABLE_ELEMENT_01";
    protected static final String VARIABLE_2_VARIABLE_ELEMENT_2                        = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableElement=VARIABLE_02.VARIABLE_ELEMENT_02";
    protected static final String VARIABLE_2_VARIABLE_ELEMENT_3                        = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableElement=VARIABLE_02.VARIABLE_ELEMENT_03";
    protected static final String VARIABLE_2_VARIABLE_ELEMENT_4                        = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableElement=VARIABLE_02.VARIABLE_ELEMENT_04";
    protected static final String VARIABLE_2_VARIABLE_ELEMENT_5                        = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableElement=VARIABLE_02.VARIABLE_ELEMENT_05";
    protected static final String VARIABLE_2_VARIABLE_ELEMENT_6                        = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableElement=VARIABLE_02.VARIABLE_ELEMENT_06";
    protected static final String VARIABLE_2_VARIABLE_ELEMENT_7                        = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableElement=VARIABLE_02.VARIABLE_ELEMENT_07";
    protected static final String VARIABLE_5_VARIABLE_ELEMENT_1                        = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableElement=VARIABLE_05.VARIABLE_ELEMENT_01";
    protected static final String VARIABLE_5_VARIABLE_ELEMENT_2                        = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableElement=VARIABLE_05.VARIABLE_ELEMENT_02";
    protected static final String VARIABLE_5_VARIABLE_ELEMENT_3                        = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableElement=VARIABLE_05.VARIABLE_ELEMENT_03";

    // Variable elements operations
    protected static final String VARIABLE_2_VARIABLE_ELEMENT_OPERATION_1              = "VARIABLE_2_OPERATION_1";
    protected static final String VARIABLE_2_VARIABLE_ELEMENT_OPERATION_2              = "VARIABLE_2_OPERATION_2";
    protected static final String VARIABLE_2_VARIABLE_ELEMENT_OPERATION_3              = "VARIABLE_2_OPERATION_3";
    protected static final String VARIABLE_5_VARIABLE_ELEMENT_OPERATION_1              = "VARIABLE_5_OPERATION_1";

    // Codelist visualisations
    protected static final String CODELIST_1_V1_ORDER_VISUALISATION_01_ALPHABETICAL    = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.CodelistOrder=SDMX01:CODELIST01(01.000).ALPHABETICAL";
    protected static final String CODELIST_1_V2_ORDER_VISUALISATION_01_ALPHABETICAL    = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.CodelistOrder=SDMX01:CODELIST01(02.000).ALPHABETICAL";
    protected static final String CODELIST_1_V2_ORDER_VISUALISATION_02                 = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.CodelistOrder=SDMX01:CODELIST01(02.000).VISUALISATION02";
    protected static final String CODELIST_1_V2_ORDER_VISUALISATION_03                 = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.CodelistOrder=SDMX01:CODELIST01(02.000).VISUALISATION03";
    protected static final String CODELIST_7_V1_ORDER_VISUALISATION_01_ALPHABETICAL    = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.CodelistOrder=SDMX01:CODELIST07(01.000).ALPHABETICAL";

    protected static final String CODELIST_1_V1_OPENNESS_VISUALISATION_01_ALL_EXPANDED = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.CodelistOpennessLevels=SDMX01:CODELIST01(01.000).ALL_EXPANDED";
    protected static final String CODELIST_1_V2_OPENNESS_VISUALISATION_01_ALL_EXPANDED = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.CodelistOpennessLevels=SDMX01:CODELIST01(02.000).ALL_EXPANDED";
    protected static final String CODELIST_1_V2_OPENNESS_VISUALISATION_02              = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.CodelistOpennessLevels=SDMX01:CODELIST01(02.000).VISUALISATION02";
    protected static final String CODELIST_1_V2_OPENNESS_VISUALISATION_03              = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.CodelistOpennessLevels=SDMX01:CODELIST01(02.000).VISUALISATION03";
    protected static final String CODELIST_7_V1_OPENNESS_VISUALISATION_01_ALL_EXPANDED = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.CodelistOpennessLevels=SDMX01:CODELIST07(01.000).ALL_EXPANDED";

    // Dsd
    protected static String       DSD_1_V1                                             = "urn:sdmx:org.sdmx.infomodel.datastructure.DataStructure=SDMX01:DATASTRUCTUREDEFINITION01(01.000)";
    protected static String       DSD_1_V2                                             = "urn:sdmx:org.sdmx.infomodel.datastructure.DataStructure=SDMX01:DATASTRUCTUREDEFINITION01(02.000)";
    protected static String       DSD_2_V1                                             = "urn:sdmx:org.sdmx.infomodel.datastructure.DataStructure=SDMX01:DATASTRUCTUREDEFINITION02(01.000)";
    protected static String       DSD_3_V1                                             = "urn:sdmx:org.sdmx.infomodel.datastructure.DataStructure=SDMX01:DATASTRUCTUREDEFINITION03(01.000)";
    protected static String       DSD_4_V1                                             = "urn:sdmx:org.sdmx.infomodel.datastructure.DataStructure=SDMX01:DATASTRUCTUREDEFINITION04(01.000)";
    protected static String       DSD_5_V1                                             = "urn:sdmx:org.sdmx.infomodel.datastructure.DataStructure=SDMX01:DATASTRUCTUREDEFINITION05(01.000)";
    protected static String       DSD_6_V1                                             = "urn:sdmx:org.sdmx.infomodel.datastructure.DataStructure=SDMX02:DATASTRUCTUREDEFINITION06(01.000)";
    protected static String       DSD_7_V1                                             = "urn:sdmx:org.sdmx.infomodel.datastructure.DataStructure=SDMX01:DATASTRUCTUREDEFINITION07(01.000)";

    protected static String       DSD_6_V1_DIMENSION_1                                 = "urn:sdmx:org.sdmx.infomodel.datastructure.Dimension=SDMX02:DATASTRUCTUREDEFINITION06(01.000).dim-01";
    protected static String       DSD_6_V1_TIME_DIMENSION_1                            = "urn:sdmx:org.sdmx.infomodel.datastructure.TimeDimension=SDMX02:DATASTRUCTUREDEFINITION06(01.000).timeDimension-01";
    protected static String       DSD_6_V1_MEASURE_DIMENSION_1                         = "urn:sdmx:org.sdmx.infomodel.datastructure.MeasureDimension=SDMX02:DATASTRUCTUREDEFINITION06(01.000).measureDimension-01";

    protected static String       DSD_7_V1_ATTRIBUTE_DESCRIPTOR                        = "urn:sdmx:org.sdmx.infomodel.datastructure.AttributeDescriptor=SDMX01:DATASTRUCTUREDEFINITION07(01.000).AttributeDescriptor";
    protected static String       DSD_7_V1_MEASURE_DESCRIPTOR                          = "urn:sdmx:org.sdmx.infomodel.datastructure.MeasureDescriptor=SDMX01:DATASTRUCTUREDEFINITION07(01.000).MeasureDescriptor";
    protected static String       DSD_7_V1_GROUP_DIMENSION_DESCRIPTOR_2                = "urn:sdmx:org.sdmx.infomodel.datastructure.GroupDimensionDescriptor=SDMX01:DATASTRUCTUREDEFINITION07(01.000).groupDimensionDescriptor02";
    protected static String       DSD_7_V1_GROUP_DIMENSION_DESCRIPTOR_3                = "urn:sdmx:org.sdmx.infomodel.datastructure.GroupDimensionDescriptor=SDMX01:DATASTRUCTUREDEFINITION07(01.000).groupDimensionDescriptor03";
    protected static String       DSD_7_V1_DIMENSION_1                                 = "urn:sdmx:org.sdmx.infomodel.datastructure.Dimension=SDMX01:DATASTRUCTUREDEFINITION07(01.000).dim-01";
    protected static String       DSD_7_V1_TIME_DIMENSION_1                            = "urn:sdmx:org.sdmx.infomodel.datastructure.TimeDimension=SDMX01:DATASTRUCTUREDEFINITION07(01.000).TIME_PERIOD";
    protected static String       DSD_7_V1_DATA_ATTRIBUTE_1                            = "urn:sdmx:org.sdmx.infomodel.datastructure.DataAttribute=SDMX01:DATASTRUCTUREDEFINITION07(01.000).dataAttribute-01";
    protected static String       DSD_7_V1_DATA_ATTRIBUTE_3                            = "urn:sdmx:org.sdmx.infomodel.datastructure.DataAttribute=SDMX01:DATASTRUCTUREDEFINITION07(01.000).dataAttribute-03";
    protected static String       DSD_7_V1_PRIMARY_MEASURE                             = "urn:sdmx:org.sdmx.infomodel.datastructure.PrimaryMeasure=SDMX01:DATASTRUCTUREDEFINITION07(01.000).OBS_VALUE";

    // Other
    protected static final String NOT_EXISTS                                           = "not-exists";

    @Value("${metamac.srm.db.provider}")
    private String                databaseProvider;

    // Note: can not use global attributes to ServiceContexts because they can be modified in some tests

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
        tables.add("TB_M_CATEGORIES");
        tables.add("TB_M_CAT_SCHEMES_VERSIONS");
        tables.add("TB_M_CODELIST_FAMILIES");
        tables.add("TB_M_VARIABLES");
        tables.add("TB_M_CODELIST_ORDER_VIS");
        tables.add("TB_M_CODELIST_OPENNESS_VIS");
        tables.add("TB_M_CODELISTS_VERSIONS");
        tables.add("TB_M_VARIABLE_ELEMENTS");
        tables.add("TB_M_VAR_ELEM_OPERATIONS");
        tables.add("TB_M_VAR_ELEM_OP_SOURCES");
        tables.add("TB_M_VAR_ELEM_OP_TARGETS");
        tables.add("TB_M_CODES");
        tables.add("TB_M_CONCEPT_RELATED");
        tables.add("TB_M_CONCEPT_ROLES");
        tables.add("TB_M_LIS_CONCEPT_TYPES");
        tables.add("TB_M_QUANTITIES");
        tables.add("TB_M_CONCEPTS");
        tables.add("TB_M_CONCEPT_SCHEMES_VERSIONS");
        tables.add("TB_M_DATASTRUCTURE_VERSIONS");
        tables.add("TB_M_ORGANISATIONS");
        tables.add("TB_M_ORG_SCHEMES_VERSIONS");
        tables.add("TB_M_VARIABLE_FAMILIES");
        tables.add("TB_M_VAR_FAMILIES_VARIABLES");
        tables.add("TB_M_DIMENSION_ORDERS");
        tables.add("TB_M_MEASURE_DIM_PRECISIONS");
        tables.add("TB_M_DIM_VIS_INFO");
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
        tablePrimaryKeys.put("TB_M_VAR_FAMILIES_VARIABLES", Arrays.asList("VARIABLE_FAMILY_FK", "VARIABLE_FK"));
        tablePrimaryKeys.put("TB_M_DATASTRUCTURE_VERSIONS", Arrays.asList("TB_DATASTRUCTURE_VERSIONS"));
        tablePrimaryKeys.put("TB_M_VAR_ELEM_OP_TARGETS", Arrays.asList("TARGET_FK", "OPERATION_FK"));
        tablePrimaryKeys.put("TB_M_VAR_ELEM_OP_SOURCES", Arrays.asList("SOURCE_FK", "OPERATION_FK"));
        return tablePrimaryKeys;
    }
    @Override
    protected DataBaseProvider getDatabaseProvider() {
        return DataBaseProvider.valueOf(databaseProvider);
    }

    protected ServiceContext getServiceContextWithoutPrincipal() {
        return new ServiceContext("junit", "junit", "app");
    }

    protected MetamacCriteriaPaginator buildMetamacCriteriaPaginatorNoLimitsAndCountResults() {
        MetamacCriteriaPaginator paginator = new MetamacCriteriaPaginator();
        paginator.setFirstResult(0);
        paginator.setMaximumResultSize(Integer.MAX_VALUE);
        paginator.setCountTotalResults(Boolean.TRUE);
        return paginator;
    }

    protected List<MetamacCriteriaOrder> buildMetamacCriteriaOrderByUrn() {
        List<MetamacCriteriaOrder> orders = new ArrayList<MetamacCriteriaOrder>();
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.ASC);
        order.setPropertyName("URN");
        orders.add(order);
        return orders;
    }

    protected ItemVisualisationResult getItemVisualisationResult(List<ItemVisualisationResult> actuals, String codeUrn) {
        for (ItemVisualisationResult actual : actuals) {
            if (actual.getUrn().equals(codeUrn)) {
                return actual;
            }
        }
        fail("not found");
        return null;
    }

    protected CodeMetamacVisualisationResult getCodeMetamacVisualisationResult(List<CodeMetamacVisualisationResult> actuals, String codeUrn) {
        for (CodeMetamacVisualisationResult actual : actuals) {
            if (actual.getUrn().equals(codeUrn)) {
                return actual;
            }
        }
        fail("code not found");
        return null;
    }

    @SuppressWarnings("rawtypes")
    protected ItemMetamacBasicDto assertListContainsItemMetamacBasicDto(List items, String urn) {
        for (Iterator iterator = items.iterator(); iterator.hasNext();) {
            ItemMetamacBasicDto item = (ItemMetamacBasicDto) iterator.next();
            if (item.getUrn().equals(urn)) {
                return item;
            }
        }
        fail("List does not contain item with urn " + urn);
        return null;
    }

    @SuppressWarnings("rawtypes")
    protected ConceptMetamac assertListConceptsContainsConcept(List items, String urn) {
        for (Iterator iterator = items.iterator(); iterator.hasNext();) {
            ConceptMetamac concept = (ConceptMetamac) iterator.next();
            if (concept.getNameableArtefact().getUrn().equals(urn)) {
                return concept;
            }
        }
        fail("List does not contain item with urn " + urn);
        return null;
    }

    @SuppressWarnings("rawtypes")
    protected CodeMetamac assertListCodesContainsCode(List items, String urn) {
        for (Iterator iterator = items.iterator(); iterator.hasNext();) {
            CodeMetamac concept = (CodeMetamac) iterator.next();
            if (concept.getNameableArtefact().getUrn().equals(urn)) {
                return concept;
            }
        }
        fail("List does not contain item with urn " + urn);
        return null;
    }

    @SuppressWarnings("rawtypes")
    protected OrganisationMetamac assertListOrganisationsContainsOrganisation(List items, String urn) {
        for (Iterator iterator = items.iterator(); iterator.hasNext();) {
            OrganisationMetamac concept = (OrganisationMetamac) iterator.next();
            if (concept.getNameableArtefact().getUrn().equals(urn)) {
                return concept;
            }
        }
        fail("List does not contain item with urn " + urn);
        return null;
    }

    @SuppressWarnings("rawtypes")
    protected CategoryMetamac assertListCategoriesContainsCategory(List items, String urn) {
        for (Iterator iterator = items.iterator(); iterator.hasNext();) {
            CategoryMetamac category = (CategoryMetamac) iterator.next();
            if (category.getNameableArtefact().getUrn().equals(urn)) {
                return category;
            }
        }
        fail("List does not contain item with urn " + urn);
        return null;
    }

    protected MetamacExceptionItem assertListContainsExceptionItemOneParameter(MetamacException e, CommonServiceExceptionType serviceExceptionType, String parameter) {
        for (MetamacExceptionItem metamacExceptionItem : e.getExceptionItems()) {
            if (serviceExceptionType.getCode().equals(metamacExceptionItem.getCode()) && metamacExceptionItem.getMessageParameters().length == 1
                    && parameter.equals(metamacExceptionItem.getMessageParameters()[0])) {
                return metamacExceptionItem;
            }
        }
        fail("Exception item not found");
        return null;
    }
}
