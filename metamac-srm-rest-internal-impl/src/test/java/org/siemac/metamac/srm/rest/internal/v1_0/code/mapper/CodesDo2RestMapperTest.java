package org.siemac.metamac.srm.rest.internal.v1_0.code.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.siemac.metamac.common.test.utils.MetamacAsserts.assertEqualsDate;
import static org.siemac.metamac.rest.api.constants.RestApiConstants.WILDCARD_ALL;
import static org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesAsserts.assertEqualsResource;
import static org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesDoMocks.mockCode;
import static org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesDoMocks.mockCodeItemResult;
import static org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesDoMocks.mockCodelist;
import static org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesDoMocks.mockCodelistFamily;
import static org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesDoMocks.mockCodelistWithCodes;
import static org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesDoMocks.mockVariable;
import static org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesDoMocks.mockVariableElement;
import static org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesDoMocks.mockVariableElementResult;
import static org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesDoMocks.mockVariableFamily;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.Asserts.assertEqualsInternationalString;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.AGENCY_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.AGENCY_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ARTEFACT_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ARTEFACT_2_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_2_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_3_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_2_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_3_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ORDER_BY_ID_DESC;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1_NAME_LIKE_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.VERSION_2;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.utils.MetamacAsserts;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AccessType;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Code;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codelist;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodelistFamilies;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodelistFamily;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codelists;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ProcStatus;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Variable;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.VariableElement;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.VariableElements;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.VariableElementsGeoInfo;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.VariableElementsGeoInfoFeature;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.VariableFamilies;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.VariableFamily;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.VariableType;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Variables;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.VisualisationConfiguration;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.VariableElementResultSelection;
import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;
import org.siemac.metamac.srm.rest.common.SrmRestConstants;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.code.CodesDo2RestMapperV10;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm-rest-internal/applicationContext-test.xml"})
public class CodesDo2RestMapperTest {

    @Autowired
    private CodesDo2RestMapperV10 do2RestInternalMapper;

    @Test
    public void testToCodelists() {

        String agencyID = WILDCARD_ALL;
        String resourceID = WILDCARD_ALL;
        String query = QUERY_ID_LIKE_1_NAME_LIKE_2;
        String orderBy = ORDER_BY_ID_DESC;
        Integer limit = Integer.valueOf(4);
        Integer offset = Integer.valueOf(4);

        List<CodelistVersionMetamac> source = new ArrayList<CodelistVersionMetamac>();
        source.add(mockCodelist(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1));
        source.add(mockCodelist(AGENCY_1, ITEM_SCHEME_2_CODE, VERSION_1));
        source.add(mockCodelist(AGENCY_1, ITEM_SCHEME_2_CODE, VERSION_2));
        source.add(mockCodelist(AGENCY_2, ITEM_SCHEME_3_CODE, VERSION_1));

        Integer totalRows = source.size() * 5;
        PagedResult<CodelistVersionMetamac> sources = new PagedResult<CodelistVersionMetamac>(source, offset, source.size(), limit, totalRows, 0);

        // Transform
        Codelists target = do2RestInternalMapper.toCodelists(sources, agencyID, resourceID, query, orderBy, limit);

        // Validate
        assertEquals(SrmRestConstants.KIND_CODELISTS, target.getKind());

        String baseLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/codelists" + "/" + agencyID + "/" + resourceID + "?query=" + query + "&orderBy=" + orderBy;

        assertEquals(baseLink + "&limit=" + limit + "&offset=" + offset, target.getSelfLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getFirstLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getPreviousLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=8", target.getNextLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=16", target.getLastLink());

        assertEquals(limit.intValue(), target.getLimit().intValue());
        assertEquals(offset.intValue(), target.getOffset().intValue());
        assertEquals(totalRows.intValue(), target.getTotal().intValue());

        assertEquals(source.size(), target.getCodelists().size());
        for (int i = 0; i < source.size(); i++) {
            assertEqualsResource(source.get(i), target.getCodelists().get(i));
        }
    }

    @Test
    public void testToCodelist() throws MetamacException {

        CodelistVersionMetamac source = mockCodelistWithCodes("agencyID1", "resourceID1", "01.123");

        // Transform
        Codelist target = do2RestInternalMapper.toCodelist(source);

        // Validate (only Metamac metadata and some SDMX). Note: check with concrete values (not doing "getter" of source)
        assertEquals(SrmRestConstants.KIND_CODELIST, target.getKind());
        assertEquals("resourceID1", target.getId());
        assertEquals("01.123", target.getVersion());
        assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=agencyID1:resourceID1(01.123)", target.getUrn());
        assertEquals(null, target.getUrnProvider());
        String selfLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/codelists/agencyID1/resourceID1/01.123";
        assertEquals(SrmRestConstants.KIND_CODELIST, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals(target.getSelfLink().getHref(), target.getUri());
        assertEquals(SrmRestConstants.KIND_CODELISTS, target.getParentLink().getKind());
        assertEquals("http://data.istac.es/apis/structural-resources-internal/v1.0/codelists", target.getParentLink().getHref());
        assertEquals("http://localhost:8080/metamac-srm-web/#structuralResources/codelists/codelist;id=agencyID1:resourceID1(01.123)", target.getManagementAppLink());
        assertEqualsInternationalString("es", "comment-resourceID1v01.123 en Español", "en", "comment-resourceID1v01.123 in English", target.getComment());
        assertEqualsInternationalString("es", "shortName-resourceID1v01.123 en Español", "en", "shortName-resourceID1v01.123 in English", target.getShortName());
        assertEqualsInternationalString("es", "descriptionSource-resourceID1v01.123 en Español", "en", "descriptionSource-resourceID1v01.123 in English", target.getDescriptionSource());
        assertTrue(target.isIsRecommended());
        assertEquals(AccessType.PUBLIC, target.getAccessType());
        assertEquals("family1", target.getFamily().getId());
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.CodelistFamily=family1", target.getFamily().getUrn());
        assertEqualsInternationalString("es", "name-family1 en Español", "en", "name-family1 in English", target.getFamily().getName());
        assertEquals(SrmRestConstants.KIND_CODELIST_FAMILY, target.getFamily().getSelfLink().getKind());
        assertEquals("http://data.istac.es/apis/structural-resources-internal/v1.0/codelistfamilies/family1", target.getFamily().getSelfLink().getHref());
        assertEquals("variable1", target.getVariable().getId());
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.Variable=variable1", target.getVariable().getUrn());
        assertEqualsInternationalString("es", "name-variable1 en Español", "en", "name-variable1 in English", target.getVariable().getName());
        assertEquals(SrmRestConstants.KIND_VARIABLE, target.getVariable().getSelfLink().getKind());
        assertEquals("http://data.istac.es/apis/structural-resources-internal/v1.0/variables/variable1", target.getVariable().getSelfLink().getHref());
        // replaceX no tested, because it is necessary a repository access
        // assertEquals("replaceTo", target.getReplaceToVersion());
        // assertEquals("replacedBy", target.getReplacedByVersion());

        assertEquals(SrmRestConstants.KIND_CODELIST, target.getReplacedBy().getKind());
        assertEquals("http://data.istac.es/apis/structural-resources-internal/v1.0/codelists/agencyID1/codelistReplacedBy/01.000", target.getReplacedBy().getSelfLink().getHref());
        assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=agencyID1:codelistReplacedBy(01.000)", target.getReplacedBy().getUrn());

        assertEquals(SrmRestConstants.KIND_CODELISTS, target.getReplaceTo().getKind());
        assertEquals(BigInteger.valueOf(2), target.getReplaceTo().getTotal());
        assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=agencyID1:codelistReplaceTo1(01.000)", target.getReplaceTo().getReplaceTos().get(0).getUrn());
        assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=agencyID1:codelistReplaceTo3(03.000)", target.getReplaceTo().getReplaceTos().get(1).getUrn());

        assertEquals(ProcStatus.EXTERNALLY_PUBLISHED, target.getLifeCycle().getProcStatus());
        assertEqualsDate(new DateTime(2009, 9, 1, 1, 1, 1, 1), target.getLifeCycle().getProductionValidationDate());
        assertEquals("production-user", target.getLifeCycle().getProductionValidationUser());
        assertEqualsDate(new DateTime(2010, 10, 2, 1, 1, 1, 1), target.getLifeCycle().getDiffusionValidationDate());
        assertEquals("diffusion-user", target.getLifeCycle().getDiffusionValidationUser());
        assertEqualsDate(new DateTime(2011, 11, 3, 1, 1, 1, 1), target.getLifeCycle().getInternalPublicationDate());
        assertEquals("internal-publication-user", target.getLifeCycle().getInternalPublicationUser());
        assertEqualsDate(new DateTime(2012, 12, 4, 1, 1, 1, 1), target.getLifeCycle().getExternalPublicationDate());
        assertEquals("external-publication-user", target.getLifeCycle().getExternalPublicationUser());
        assertEqualsDate(new DateTime(2013, 10, 1, 10, 12, 13, 14), target.getResourceCreatedDate());

        assertEquals(BigInteger.valueOf(3), target.getOrderConfigurations().getTotal());
        {
            VisualisationConfiguration configuration = target.getOrderConfigurations().getVisualisationConfigurations().get(0);
            assertEquals("order1", configuration.getId());
            assertEqualsInternationalString("es", "order-order1 en Español", "en", "order-order1 in English", configuration.getName());
            assertEquals(true, configuration.isDefault());
        }
        {
            VisualisationConfiguration configuration = target.getOrderConfigurations().getVisualisationConfigurations().get(1);
            assertEquals("order2", configuration.getId());
            assertEqualsInternationalString("es", "order-order2 en Español", "en", "order-order2 in English", configuration.getName());
            assertEquals(null, configuration.isDefault());
        }
        {
            VisualisationConfiguration configuration = target.getOrderConfigurations().getVisualisationConfigurations().get(2);
            assertEquals("order3", configuration.getId());
            assertEqualsInternationalString("es", "order-order3 en Español", "en", "order-order3 in English", configuration.getName());
            assertEquals(null, configuration.isDefault());
        }

        assertEquals(BigInteger.valueOf(2), target.getOpennessConfigurations().getTotal());
        {
            VisualisationConfiguration configuration = target.getOpennessConfigurations().getVisualisationConfigurations().get(0);
            assertEquals("openness1", configuration.getId());
            assertEqualsInternationalString("es", "openness-openness1 en Español", "en", "openness-openness1 in English", configuration.getName());
            assertEquals(null, configuration.isDefault());
        }
        {
            VisualisationConfiguration configuration = target.getOpennessConfigurations().getVisualisationConfigurations().get(1);
            assertEquals("openness2", configuration.getId());
            assertEqualsInternationalString("es", "openness-openness2 en Español", "en", "openness-openness2 in English", configuration.getName());
            assertEquals(true, configuration.isDefault());
        }

        assertEquals(BigInteger.ONE, target.getChildLinks().getTotal());
        assertEquals(SrmRestConstants.KIND_CODES, target.getChildLinks().getChildLinks().get(0).getKind());
        assertEquals(selfLink + "/codes", target.getChildLinks().getChildLinks().get(0).getHref());
    }

    @Test
    public void testToCodelistImported() throws MetamacException {

        CodelistVersionMetamac source = mockCodelist("agencyID1", "resourceID1", "01.123");
        source.getMaintainableArtefact().setIsImported(Boolean.TRUE);
        source.getMaintainableArtefact().setUriProvider("uriProviderDb");
        source.getMaintainableArtefact().setUrnProvider("urnProvider");

        // Transform
        Codelist target = do2RestInternalMapper.toCodelist(source);

        // Validate
        assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=agencyID1:resourceID1(01.123)", target.getUrn());
        assertEquals("urnProvider", target.getUrnProvider());
        assertEquals("uriProviderDb", target.getUri());
    }

    @Test
    public void testToCodelistReplacedBy() throws MetamacException {

        CodelistVersionMetamac source = mockCodelist("agencyID1", "resourceID1", "01.123");
        source.setReplacedByCodelist(mockCodelist("agencyID2", "codelistReplacedBy", "01.000"));

        {
            // not final
            source.getReplacedByCodelist().getMaintainableArtefact().setFinalLogicClient(false);
            Codelist target = do2RestInternalMapper.toCodelist(source);
            assertEquals(null, target.getReplacedBy());
        }
        {
            // not public
            source.getReplacedByCodelist().getMaintainableArtefact().setFinalLogicClient(true);
            source.getReplacedByCodelist().setAccessType(AccessTypeEnum.RESTRICTED);
            Codelist target = do2RestInternalMapper.toCodelist(source);
            assertEquals(null, target.getReplacedBy());
        }
        {
            // can be provided
            source.getReplacedByCodelist().getMaintainableArtefact().setFinalLogicClient(true);
            source.getReplacedByCodelist().setAccessType(AccessTypeEnum.PUBLIC);
            Codelist target = do2RestInternalMapper.toCodelist(source);
            assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=agencyID2:codelistReplacedBy(01.000)", target.getReplacedBy().getUrn());
        }
    }

    @Test
    public void testToCodes() {

        String agencyID = WILDCARD_ALL;
        String codeSchemeID = WILDCARD_ALL;
        String version = WILDCARD_ALL;
        String query = QUERY_ID_LIKE_1_NAME_LIKE_2;
        String orderBy = ORDER_BY_ID_DESC;
        Integer limit = Integer.valueOf(4);
        Integer offset = Integer.valueOf(4);
        Set<String> fields = null; 

        CodelistVersionMetamac codeScheme1 = mockCodelist(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1);
        CodelistVersionMetamac codeScheme2 = mockCodelist(AGENCY_1, ITEM_SCHEME_2_CODE, VERSION_1);
        List<CodeMetamac> source = new ArrayList<CodeMetamac>();
        source.add(mockCode(ITEM_1_CODE, codeScheme1, null));
        source.add(mockCode(ITEM_2_CODE, codeScheme1, null));
        source.add(mockCode(ITEM_3_CODE, codeScheme1, null));
        source.add(mockCode(ITEM_1_CODE, codeScheme2, null));

        Integer totalRows = source.size() * 5;
        PagedResult<CodeMetamac> sources = new PagedResult<CodeMetamac>(source, offset, source.size(), limit, totalRows, 0);

        // Transform
        Codes target = do2RestInternalMapper.toCodes(sources, agencyID, codeSchemeID, version, query, orderBy, limit, fields);

        // Validate
        assertEquals(SrmRestConstants.KIND_CODES, target.getKind());

        String baseLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/codelists" + "/" + agencyID + "/" + codeSchemeID + "/" + version + "/codes?query=" + query + "&orderBy="
                + orderBy;
        assertEquals(baseLink + "&limit=" + limit + "&offset=" + offset, target.getSelfLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getFirstLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getPreviousLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=8", target.getNextLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=16", target.getLastLink());

        assertEquals(limit.intValue(), target.getLimit().intValue());
        assertEquals(offset.intValue(), target.getOffset().intValue());
        assertEquals(totalRows.intValue(), target.getTotal().intValue());

        assertEquals(source.size(), target.getCodes().size());
        for (int i = 0; i < source.size(); i++) {
            assertEqualsResource(source.get(i).getItemSchemeVersion(), source.get(i), null, target.getCodes().get(i));
        }
    }

    @Test
    public void testToCodesItemResult() {

        String agencyID = AGENCY_1;
        String codelistID = ITEM_SCHEME_1_CODE;
        String version = VERSION_1;

        CodelistVersionMetamac codelist = mockCodelist(agencyID, codelistID, version);
        List<ItemResult> sources = new ArrayList<ItemResult>();
        sources.add(mockCodeItemResult(ITEM_1_CODE, null, 1, true));
        sources.add(mockCodeItemResult(ITEM_2_CODE, null, 2, true));
        sources.add(mockCodeItemResult(ITEM_3_CODE, null, 3, false));

        // Transform
        Codes target = do2RestInternalMapper.toCodes(sources, codelist, null);

        // Validate
        assertEquals(SrmRestConstants.KIND_CODES, target.getKind());

        assertEquals("http://data.istac.es/apis/structural-resources-internal/v1.0/codelists" + "/" + agencyID + "/" + codelistID + "/" + version + "/codes", target.getSelfLink());
        assertEquals(null, target.getFirstLink());
        assertEquals(null, target.getPreviousLink());
        assertEquals(null, target.getNextLink());
        assertEquals(null, target.getLastLink());

        assertEquals(null, target.getLimit());
        assertEquals(null, target.getOffset());
        assertEquals(sources.size(), target.getTotal().intValue());

        assertEquals(sources.size(), target.getCodes().size());
        for (int i = 0; i < sources.size(); i++) {
            assertEqualsResource(codelist, null, sources.get(i), target.getCodes().get(i));
        }
    }

    @Test
    public void testToCode() {
        CodelistVersionMetamac codeScheme = mockCodelist("agencyID1", "resourceID1", "01.123");
        CodeMetamac parent = mockCode("codeParent1", codeScheme, null);
        CodeMetamac source = mockCode("code2", codeScheme, parent);

        // Transform
        Code target = do2RestInternalMapper.toCode(source);

        // Validate (only Metamac metadata and some SDMX). Note: check with concrete values (not doing "getter" of source)
        assertEquals(SrmRestConstants.KIND_CODE, target.getKind());
        assertEquals("code2", target.getId());
        assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Code=agencyID1:resourceID1(01.123).code2", target.getUrn());
        assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Code=agencyID1:resourceID1(01.123).code2", target.getUrn());

        String parentLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/codelists/agencyID1/resourceID1/01.123/codes";
        String selfLink = parentLink + "/code2";
        assertEquals(SrmRestConstants.KIND_CODE, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals(target.getSelfLink().getHref(), target.getUri());
        assertEquals(SrmRestConstants.KIND_CODES, target.getParentLink().getKind());
        assertEquals(parentLink, target.getParentLink().getHref());
        assertNull(target.getChildLinks());
        assertEquals("http://localhost:8080/metamac-srm-web/#structuralResources/codelists/codelist;id=agencyID1:resourceID1(01.123)/code;id=code2", target.getManagementAppLink());

        assertEqualsInternationalString("es", "comment-code2 en Español", "en", "comment-code2 in English", target.getComment());
        assertEqualsInternationalString("es", "shortName-variableElement-code2 en Español", "en", "shortName-variableElement-code2 in English", target.getShortName());
        assertEquals("variableElement-code2", target.getVariableElement().getId());
        assertEqualsInternationalString("es", "shortName-variableElement-code2 en Español", "en", "shortName-variableElement-code2 in English", target.getVariableElement().getName());
        assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Code=agencyID1:resourceID1(01.123).codeParent1", target.getParent());
    }

    @Test
    public void testToCodeImported() {
        CodelistVersionMetamac codeScheme = mockCodelist("agencyID1", "resourceID1", "01.123");
        codeScheme.getMaintainableArtefact().setIsImported(Boolean.TRUE);
        CodeMetamac source = mockCode("code2", codeScheme, null);
        source.getNameableArtefact().setUrnProvider("urnProvider");
        source.getNameableArtefact().setUriProvider("uriProviderDb");

        // Transform
        Code target = do2RestInternalMapper.toCode(source);

        // Validate
        assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Code=agencyID1:resourceID1(01.123).code2", target.getUrn());
        assertEquals("urnProvider", target.getUrnProvider());
        assertEquals("uriProviderDb", target.getUri());
    }

    @Test
    public void testToCodeWithoutVariableElement() {
        CodelistVersionMetamac codeScheme = mockCodelist("agencyID1", "resourceID1", "01.123");
        codeScheme.getMaintainableArtefact().setIsImported(Boolean.TRUE);
        CodeMetamac source = mockCode("code2", codeScheme, null);
        source.setVariableElement(null);

        // Transform
        Code target = do2RestInternalMapper.toCode(source);

        // Validate
        assertEqualsInternationalString("es", "shortName-code2 en Español", "en", "shortName-code2 in English", target.getShortName());
    }

    @Test
    public void testToVariableFamilies() {

        String query = QUERY_ID_LIKE_1_NAME_LIKE_2;
        String orderBy = ORDER_BY_ID_DESC;
        Integer limit = Integer.valueOf(4);
        Integer offset = Integer.valueOf(4);

        List<org.siemac.metamac.srm.core.code.domain.VariableFamily> source = new ArrayList<org.siemac.metamac.srm.core.code.domain.VariableFamily>();
        source.add(mockVariableFamily("variableFamily1"));
        source.add(mockVariableFamily("variableFamily2"));
        source.add(mockVariableFamily("variableFamily3"));
        source.add(mockVariableFamily("variableFamily4"));

        Integer totalRows = source.size() * 5;
        PagedResult<org.siemac.metamac.srm.core.code.domain.VariableFamily> sources = new PagedResult<org.siemac.metamac.srm.core.code.domain.VariableFamily>(source, offset, source.size(), limit,
                totalRows, 0);

        // Transform
        VariableFamilies target = do2RestInternalMapper.toVariableFamilies(sources, query, orderBy, limit);

        // Validate
        assertEquals(SrmRestConstants.KIND_VARIABLE_FAMILIES, target.getKind());

        String baseLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/variablefamilies?query=" + query + "&orderBy=" + orderBy;

        assertEquals(baseLink + "&limit=" + limit + "&offset=" + offset, target.getSelfLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getFirstLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getPreviousLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=8", target.getNextLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=16", target.getLastLink());

        assertEquals(limit.intValue(), target.getLimit().intValue());
        assertEquals(offset.intValue(), target.getOffset().intValue());
        assertEquals(totalRows.intValue(), target.getTotal().intValue());

        assertEquals(source.size(), target.getVariableFamilies().size());
        for (int i = 0; i < source.size(); i++) {
            assertEqualsResource(source.get(i), target.getVariableFamilies().get(i));
        }
    }

    @Test
    public void testToVariableFamily() throws MetamacException {

        org.siemac.metamac.srm.core.code.domain.VariableFamily source = mockVariableFamily("variableFamily1");

        // Transform
        VariableFamily target = do2RestInternalMapper.toVariableFamily(source);

        // Validate
        assertEquals(SrmRestConstants.KIND_VARIABLE_FAMILY, target.getKind());
        assertEquals("variableFamily1", target.getId());
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableFamily=variableFamily1", target.getUrn());
        assertEquals(null, target.getUrnProvider());
        String selfLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/variablefamilies/variableFamily1";
        assertEquals(SrmRestConstants.KIND_VARIABLE_FAMILY, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals("http://localhost:8080/metamac-srm-web/#structuralResources/variableFamilies/variableFamily;id=variableFamily1", target.getManagementAppLink());
        assertEqualsInternationalString("es", "name-variableFamily1 en Español", "en", "name-variableFamily1 in English", target.getName());
    }

    @Test
    public void testToVariables() {

        String query = QUERY_ID_LIKE_1_NAME_LIKE_2;
        String orderBy = ORDER_BY_ID_DESC;
        Integer limit = Integer.valueOf(4);
        Integer offset = Integer.valueOf(4);

        List<org.siemac.metamac.srm.core.code.domain.Variable> source = new ArrayList<org.siemac.metamac.srm.core.code.domain.Variable>();
        source.add(mockVariable("variable1"));
        source.add(mockVariable("variable2"));
        source.add(mockVariable("variable3"));
        source.add(mockVariable("variable4"));

        Integer totalRows = source.size() * 5;
        PagedResult<org.siemac.metamac.srm.core.code.domain.Variable> sources = new PagedResult<org.siemac.metamac.srm.core.code.domain.Variable>(source, offset, source.size(), limit, totalRows, 0);

        // Transform
        Variables target = do2RestInternalMapper.toVariables(sources, query, orderBy, limit);

        // Validate
        assertEquals(SrmRestConstants.KIND_VARIABLES, target.getKind());

        String baseLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/variables?query=" + query + "&orderBy=" + orderBy;

        assertEquals(baseLink + "&limit=" + limit + "&offset=" + offset, target.getSelfLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getFirstLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getPreviousLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=8", target.getNextLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=16", target.getLastLink());

        assertEquals(limit.intValue(), target.getLimit().intValue());
        assertEquals(offset.intValue(), target.getOffset().intValue());
        assertEquals(totalRows.intValue(), target.getTotal().intValue());

        assertEquals(source.size(), target.getVariables().size());
        for (int i = 0; i < source.size(); i++) {
            assertEqualsResource(source.get(i), target.getVariables().get(i));
        }
    }

    @Test
    public void testToVariable() throws MetamacException {

        org.siemac.metamac.srm.core.code.domain.Variable source = mockVariable("variable1");

        // Transform
        Variable target = do2RestInternalMapper.toVariable(source);

        // Validate
        assertEquals(SrmRestConstants.KIND_VARIABLE, target.getKind());
        assertEquals("variable1", target.getId());
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.Variable=variable1", target.getUrn());
        assertEquals(null, target.getUrnProvider());
        String selfLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/variables/variable1";
        assertEquals(SrmRestConstants.KIND_VARIABLE, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals("http://localhost:8080/metamac-srm-web/#structuralResources/variables/variable;id=variable1", target.getManagementAppLink());
        assertEqualsInternationalString("es", "name-variable1 en Español", "en", "name-variable1 in English", target.getName());
        assertEqualsInternationalString("es", "shortName-variable1 en Español", "en", "shortName-variable1 in English", target.getShortName());
        MetamacAsserts.assertEqualsDate("2012-10-01 10:12:13", target.getValidFrom());
        MetamacAsserts.assertEqualsDate("2013-10-01 10:12:13", target.getValidTo());
        assertEquals(VariableType.GEOGRAPHICAL, target.getType());

        assertEquals(SrmRestConstants.KIND_VARIABLE, target.getReplacedBy().getKind());
        assertEquals("http://data.istac.es/apis/structural-resources-internal/v1.0/variables/variableReplacedBy1", target.getReplacedBy().getSelfLink().getHref());
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.Variable=variableReplacedBy1", target.getReplacedBy().getUrn());

        assertEquals(SrmRestConstants.KIND_VARIABLES, target.getReplaceTo().getKind());
        assertEquals(BigInteger.valueOf(2), target.getReplaceTo().getTotal());
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.Variable=variableReplaceTo1", target.getReplaceTo().getReplaceTos().get(0).getUrn());
        assertEquals("http://data.istac.es/apis/structural-resources-internal/v1.0/variables/variableReplaceTo1", target.getReplaceTo().getReplaceTos().get(0).getSelfLink().getHref());
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.Variable=variableReplaceTo2", target.getReplaceTo().getReplaceTos().get(1).getUrn());

        assertEquals(SrmRestConstants.KIND_VARIABLE_FAMILIES, target.getFamilies().getKind());
        assertEquals(BigInteger.valueOf(3), target.getFamilies().getTotal());
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableFamily=variableFamily1", target.getFamilies().getFamilies().get(0).getUrn());
        assertEquals("http://data.istac.es/apis/structural-resources-internal/v1.0/variablefamilies/variableFamily1", target.getFamilies().getFamilies().get(0).getSelfLink().getHref());
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableFamily=variableFamily2", target.getFamilies().getFamilies().get(1).getUrn());
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableFamily=variableFamily3", target.getFamilies().getFamilies().get(2).getUrn());

        assertEquals(BigInteger.ONE, target.getChildLinks().getTotal());
        assertEquals(SrmRestConstants.KIND_VARIABLE_ELEMENTS, target.getChildLinks().getChildLinks().get(0).getKind());
        assertEquals(selfLink + "/variableelements", target.getChildLinks().getChildLinks().get(0).getHref());
    }

    @Test
    public void testToVariableElements() {
        String query = QUERY_ID_LIKE_1_NAME_LIKE_2;
        String orderBy = ORDER_BY_ID_DESC;
        Integer limit = Integer.valueOf(4);
        Integer offset = Integer.valueOf(4);

        List<org.siemac.metamac.srm.core.code.domain.VariableElement> source = new ArrayList<org.siemac.metamac.srm.core.code.domain.VariableElement>();
        source.add(mockVariableElement("variableElement1"));
        source.add(mockVariableElement("variableElement2"));
        source.add(mockVariableElement("variableElement3"));
        source.add(mockVariableElement("variableElement4"));

        Integer totalRows = source.size() * 5;
        PagedResult<org.siemac.metamac.srm.core.code.domain.VariableElement> sources = new PagedResult<org.siemac.metamac.srm.core.code.domain.VariableElement>(source, offset, source.size(), limit,
                totalRows, 0);

        // Transform
        VariableElements target = do2RestInternalMapper.toVariableElements(sources, "variable01", query, orderBy, limit);

        // Validate
        assertEquals(SrmRestConstants.KIND_VARIABLE_ELEMENTS, target.getKind());

        String baseLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/variables/variable01/variableelements?query=" + query + "&orderBy=" + orderBy;

        assertEquals(baseLink + "&limit=" + limit + "&offset=" + offset, target.getSelfLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getFirstLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getPreviousLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=8", target.getNextLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=16", target.getLastLink());

        assertEquals(limit.intValue(), target.getLimit().intValue());
        assertEquals(offset.intValue(), target.getOffset().intValue());
        assertEquals(totalRows.intValue(), target.getTotal().intValue());

        assertEquals(source.size(), target.getVariableElements().size());
        for (int i = 0; i < source.size(); i++) {
            assertEqualsResource(source.get(i), target.getVariableElements().get(i));
        }
    }

    @Test
    public void testToVariableElementsGeoJson() {

        List<org.siemac.metamac.srm.core.code.domain.VariableElementResult> sources = new ArrayList<org.siemac.metamac.srm.core.code.domain.VariableElementResult>();
        sources.add(mockVariableElementResult(ARTEFACT_1_CODE));
        sources.add(mockVariableElementResult(ARTEFACT_2_CODE));

        VariableElementResultSelection selection = new VariableElementResultSelection();
        selection.setLongitudeLatitude(true);
        selection.setShapeGeojson(true);
        selection.setGeographicalGranularity(true);

        // Transform
        DateTime lastUpdated = new DateTime(2013, 1, 2, 3, 10, 50, 0);
        String target = do2RestInternalMapper.toVariableElementsGeoJson(sources, selection, lastUpdated);

        // Validate
        assertEquals(
                "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"id\":\"artefact1\",\"geometry\":{\"type\":\"MultiPolygon\",\"coordinates\":[[[[-17, 28],[-15, 50]]]]},\"properties\": {\"urn\": \"urn:sdmx:org.sdmx.infomodel.xxx=artefact1\",\"lastUpdatedDate\": \"2013-01-02T03:10:50.000Z\",\"longitude\": 1.0,\"latitude\": 2.0,\"geographicalGranularity\": \"urn:sdmx:org.sdmx.infomodel.codelist.Code=ISTAC:codelist01(01.000).geo-artefact1\"}},{\"type\":\"Feature\",\"id\":\"artefact2\",\"geometry\":{\"type\":\"MultiPolygon\",\"coordinates\":[[[[-19, 30],[-17.90, 28.85],[-14, 15]]]]},\"properties\": {\"urn\": \"urn:sdmx:org.sdmx.infomodel.xxx=artefact2\",\"lastUpdatedDate\": \"2013-01-02T03:10:50.000Z\",\"longitude\": 3.0,\"latitude\": 4.0,\"geographicalGranularity\": \"urn:sdmx:org.sdmx.infomodel.codelist.Code=ISTAC:codelist01(01.000).geo-artefact2\"}}]}",
                target);
    }

    @Test
    public void testToVariableElementsGeoXml() {

        List<org.siemac.metamac.srm.core.code.domain.VariableElementResult> sources = new ArrayList<org.siemac.metamac.srm.core.code.domain.VariableElementResult>();
        sources.add(mockVariableElementResult(ARTEFACT_1_CODE));
        sources.add(mockVariableElementResult(ARTEFACT_2_CODE));

        VariableElementResultSelection selection = new VariableElementResultSelection();
        selection.setLongitudeLatitude(true);
        selection.setShapeWkt(true);
        selection.setGeographicalGranularity(true);

        // Transform
        DateTime lastUpdated = new DateTime(2013, 1, 2, 3, 10, 50, 0);
        VariableElementsGeoInfo target = do2RestInternalMapper.toVariableElementsGeoXml(sources, selection, lastUpdated);

        // Validate
        assertEquals("FeatureCollection", target.getType());
        assertEquals(BigInteger.valueOf(2), target.getFeatures().getTotal());
        {
            VariableElementsGeoInfoFeature feature = target.getFeatures().getFeatures().get(0);
            assertEquals("Feature", feature.getType());
            assertEquals("artefact1", feature.getId());
            assertEquals("urn:sdmx:org.sdmx.infomodel.xxx=artefact1", feature.getProperties().getUrn());
            assertEquals("MULTIPOLYGON [[[[-17, 28],[-15, 50]]]]", feature.getGeometryWKT());
            assertEquals(Double.valueOf(1.0D), feature.getProperties().getLongitude());
            assertEquals(Double.valueOf(2.0D), feature.getProperties().getLatitude());
            assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Code=ISTAC:codelist01(01.000).geo-artefact1", feature.getProperties().getGeographicalGranularity().getUrn());
            MetamacAsserts.assertEqualsDate("2013-01-02 03:10:50", feature.getProperties().getLastUpdatedDate());
        }
        {
            VariableElementsGeoInfoFeature feature = target.getFeatures().getFeatures().get(1);
            assertEquals("Feature", feature.getType());
            assertEquals("artefact2", feature.getId());
            assertEquals("urn:sdmx:org.sdmx.infomodel.xxx=artefact2", feature.getProperties().getUrn());
            assertEquals("MULTIPOLYGON [[[[-17, 28],[-15, 50]]]]", feature.getGeometryWKT());
            assertEquals(Double.valueOf(3.0D), feature.getProperties().getLongitude());
            assertEquals(Double.valueOf(4.0D), feature.getProperties().getLatitude());
            assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Code=ISTAC:codelist01(01.000).geo-artefact2", feature.getProperties().getGeographicalGranularity().getUrn());
        }
    }

    @Test
    public void testToVariableElement() throws MetamacException {

        org.siemac.metamac.srm.core.code.domain.VariableElement source = mockVariableElement("variableElement1");

        // Transform
        VariableElement target = do2RestInternalMapper.toVariableElement(source);

        // Validate
        assertEquals(SrmRestConstants.KIND_VARIABLE_ELEMENT, target.getKind());
        assertEquals("variableElement1", target.getId());
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableElement=variable01.variableElement1", target.getUrn());
        assertEquals(null, target.getUrnProvider());
        String selfLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/variables/variable01/variableelements/variableElement1";
        assertEquals(SrmRestConstants.KIND_VARIABLE_ELEMENT, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals("http://localhost:8080/metamac-srm-web/#structuralResources/variables/variable;id=variable01/variableElement;id=variableElement1", target.getManagementAppLink());
        assertEqualsInternationalString("es", "shortName-variableElement1 en Español", "en", "shortName-variableElement1 in English", target.getName());
        assertEqualsInternationalString("es", "comment-variableElement1 en Español", "en", "comment-variableElement1 in English", target.getComment());
        MetamacAsserts.assertEqualsDate("2012-10-01 10:12:13", target.getValidFrom());
        MetamacAsserts.assertEqualsDate("2013-10-01 10:12:13", target.getValidTo());

        assertEquals(SrmRestConstants.KIND_VARIABLE_ELEMENT, target.getReplacedBy().getKind());
        assertEquals("http://data.istac.es/apis/structural-resources-internal/v1.0/variables/variable01/variableelements/variableElementReplacedBy1", target.getReplacedBy().getSelfLink().getHref());
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableElement=variable01.variableElementReplacedBy1", target.getReplacedBy().getUrn());

        assertEquals(SrmRestConstants.KIND_VARIABLE_ELEMENTS, target.getReplaceTo().getKind());
        assertEquals(BigInteger.valueOf(2), target.getReplaceTo().getTotal());
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableElement=variable01.variableElementReplaceTo1", target.getReplaceTo().getReplaceTos().get(0).getUrn());
        assertEquals("http://data.istac.es/apis/structural-resources-internal/v1.0/variables/variable01/variableelements/variableElementReplaceTo1", target.getReplaceTo().getReplaceTos().get(0)
                .getSelfLink().getHref());
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableElement=variable01.variableElementReplaceTo2", target.getReplaceTo().getReplaceTos().get(1).getUrn());

        assertEquals(SrmRestConstants.KIND_VARIABLE, target.getVariable().getKind());
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.Variable=variable01", target.getVariable().getUrn());
        assertEquals("http://data.istac.es/apis/structural-resources-internal/v1.0/variables/variable01", target.getVariable().getSelfLink().getHref());

        assertEquals(SrmRestConstants.KIND_VARIABLE_ELEMENTS, target.getParentLink().getKind());
        assertEquals("http://data.istac.es/apis/structural-resources-internal/v1.0/variables/variable01/variableelements", target.getParentLink().getHref());

        assertEquals(BigInteger.ONE, target.getChildLinks().getTotal());
        assertEquals(null, target.getChildLinks().getChildLinks().get(0).getKind());
        assertEquals(selfLink + "/geoinfo", target.getChildLinks().getChildLinks().get(0).getHref());
    }

    @Test
    public void testToCodelistFamilies() {

        String query = QUERY_ID_LIKE_1_NAME_LIKE_2;
        String orderBy = ORDER_BY_ID_DESC;
        Integer limit = Integer.valueOf(4);
        Integer offset = Integer.valueOf(4);

        List<org.siemac.metamac.srm.core.code.domain.CodelistFamily> source = new ArrayList<org.siemac.metamac.srm.core.code.domain.CodelistFamily>();
        source.add(mockCodelistFamily("codelistFamily1"));
        source.add(mockCodelistFamily("codelistFamily2"));
        source.add(mockCodelistFamily("codelistFamily3"));
        source.add(mockCodelistFamily("codelistFamily4"));

        Integer totalRows = source.size() * 5;
        PagedResult<org.siemac.metamac.srm.core.code.domain.CodelistFamily> sources = new PagedResult<org.siemac.metamac.srm.core.code.domain.CodelistFamily>(source, offset, source.size(), limit,
                totalRows, 0);

        // Transform
        CodelistFamilies target = do2RestInternalMapper.toCodelistFamilies(sources, query, orderBy, limit);

        // Validate
        assertEquals(SrmRestConstants.KIND_CODELIST_FAMILIES, target.getKind());

        String baseLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/codelistfamilies?query=" + query + "&orderBy=" + orderBy;

        assertEquals(baseLink + "&limit=" + limit + "&offset=" + offset, target.getSelfLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getFirstLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getPreviousLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=8", target.getNextLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=16", target.getLastLink());

        assertEquals(limit.intValue(), target.getLimit().intValue());
        assertEquals(offset.intValue(), target.getOffset().intValue());
        assertEquals(totalRows.intValue(), target.getTotal().intValue());

        assertEquals(source.size(), target.getCodelistFamilies().size());
        for (int i = 0; i < source.size(); i++) {
            assertEqualsResource(source.get(i), target.getCodelistFamilies().get(i));
        }
    }

    @Test
    public void testToCodelistFamily() throws MetamacException {

        org.siemac.metamac.srm.core.code.domain.CodelistFamily source = mockCodelistFamily("codelistFamily1");

        // Transform
        CodelistFamily target = do2RestInternalMapper.toCodelistFamily(source);

        // Validate
        assertEquals(SrmRestConstants.KIND_CODELIST_FAMILY, target.getKind());
        assertEquals("codelistFamily1", target.getId());
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.CodelistFamily=codelistFamily1", target.getUrn());
        assertEquals(null, target.getUrnProvider());
        String selfLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/codelistfamilies/codelistFamily1";
        assertEquals(SrmRestConstants.KIND_CODELIST_FAMILY, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals("http://localhost:8080/metamac-srm-web/#structuralResources/codelistFamilies/codelistFamily;id=codelistFamily1", target.getManagementAppLink());
        assertEqualsInternationalString("es", "name-codelistFamily1 en Español", "en", "name-codelistFamily1 in English", target.getName());
    }
}