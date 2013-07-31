package org.siemac.metamac.srm.soap.external.v1_0.code.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.siemac.metamac.srm.soap.external.v1_0.code.utils.CodesDoMocks.mockCode;
import static org.siemac.metamac.srm.soap.external.v1_0.code.utils.CodesDoMocks.mockCodelist;
import static org.siemac.metamac.srm.soap.external.v1_0.code.utils.CodesDoMocks.mockCodelistFamily;
import static org.siemac.metamac.srm.soap.external.v1_0.code.utils.CodesDoMocks.mockCodelistWithCodes;
import static org.siemac.metamac.srm.soap.external.v1_0.code.utils.CodesDoMocks.mockVariable;
import static org.siemac.metamac.srm.soap.external.v1_0.code.utils.CodesDoMocks.mockVariableFamily;
import static org.siemac.metamac.srm.soap.external.v1_0.code.utils.CodesDoMocks.mockVariableWithFamilies;
import static org.siemac.metamac.srm.soap.external.v1_0.utils.Asserts.assertEqualsInternationalString;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.utils.MetamacAsserts;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.soap.common.v1_0.domain.Resource;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.Code;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.Codelist;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.CodelistFamilies;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.Codelists;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.VariableFamilies;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.Variables;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;
import org.siemac.metamac.srm.soap.external.v1_0.mapper.code.CodesDo2SoapMapperV10;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm-soap-external/applicationContext-test.xml"})
public class CodesDo2SoapMapperTest {

    @Autowired
    private CodesDo2SoapMapperV10 do2SoapExternalMapper;

    @Test
    public void testToVariableFamilies() {

        Integer limit = Integer.valueOf(10);
        Integer offset = Integer.valueOf(4);

        List<VariableFamily> source = new ArrayList<VariableFamily>();
        source.add(mockVariableFamily("variableFamily01"));
        source.add(mockVariableFamily("variableFamily02"));
        source.add(mockVariableFamily("variableFamily03"));

        Integer totalRows = source.size() * 5;
        PagedResult<VariableFamily> sources = new PagedResult<VariableFamily>(source, offset, source.size(), limit, totalRows, 0);

        // Transform
        VariableFamilies target = do2SoapExternalMapper.toVariableFamilies(sources, limit);

        // Validate
        assertEquals(limit.intValue(), target.getLimit().intValue());
        assertEquals(offset.intValue(), target.getOffset().intValue());
        assertEquals(totalRows.intValue(), target.getTotal().intValue());

        assertEquals(source.size(), target.getVariableFamilies().size());
        int i = 0;
        {
            org.siemac.metamac.soap.structural_resources.v1_0.domain.VariableFamily resource = target.getVariableFamilies().get(i++);
            assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableFamily=variableFamily01", resource.getUrn());
            assertEquals("variableFamily01", resource.getId());
            assertEqualsInternationalString("es", "name-variableFamily01 en Español", "en", "name-variableFamily01 in English", resource.getName());
        }
        {
            org.siemac.metamac.soap.structural_resources.v1_0.domain.VariableFamily resource = target.getVariableFamilies().get(i++);
            assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableFamily=variableFamily02", resource.getUrn());
            assertEquals("variableFamily02", resource.getId());
            assertEqualsInternationalString("es", "name-variableFamily02 en Español", "en", "name-variableFamily02 in English", resource.getName());
        }
        {
            org.siemac.metamac.soap.structural_resources.v1_0.domain.VariableFamily resource = target.getVariableFamilies().get(i++);
            assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableFamily=variableFamily03", resource.getUrn());
            assertEquals("variableFamily03", resource.getId());
            assertEqualsInternationalString("es", "name-variableFamily03 en Español", "en", "name-variableFamily03 in English", resource.getName());
        }
        assertEquals(source.size(), i);
    }

    @Test
    public void testToVariableFamily() {

        VariableFamily source = mockVariableFamily("variableFamily01");

        // Transform
        org.siemac.metamac.soap.structural_resources.v1_0.domain.VariableFamily target = do2SoapExternalMapper.toVariableFamily(source);

        // Validate
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableFamily=variableFamily01", target.getUrn());
        assertEquals("variableFamily01", target.getId());
        assertEqualsInternationalString("es", "name-variableFamily01 en Español", "en", "name-variableFamily01 in English", target.getName());
    }

    @Test
    public void testToVariables() {

        Integer limit = Integer.valueOf(10);
        Integer offset = Integer.valueOf(4);

        List<Variable> source = new ArrayList<Variable>();
        source.add(mockVariable("variable01"));
        source.add(mockVariable("variable02"));
        source.add(mockVariable("variable03"));

        Integer totalRows = source.size() * 5;
        PagedResult<Variable> sources = new PagedResult<Variable>(source, offset, source.size(), limit, totalRows, 0);

        // Transform
        Variables target = do2SoapExternalMapper.toVariables(sources, limit);

        // Validate
        assertEquals(limit.intValue(), target.getLimit().intValue());
        assertEquals(offset.intValue(), target.getOffset().intValue());
        assertEquals(totalRows.intValue(), target.getTotal().intValue());

        assertEquals(source.size(), target.getVariables().size());
        int i = 0;
        {
            org.siemac.metamac.soap.structural_resources.v1_0.domain.Variable resource = target.getVariables().get(i++);
            assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.Variable=variable01", resource.getUrn());
            assertEquals("variable01", resource.getId());
            assertEqualsInternationalString("es", "name-variable01 en Español", "en", "name-variable01 in English", resource.getName());
        }
        {
            org.siemac.metamac.soap.structural_resources.v1_0.domain.Variable resource = target.getVariables().get(i++);
            assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.Variable=variable02", resource.getUrn());
            assertEquals("variable02", resource.getId());
            assertEqualsInternationalString("es", "name-variable02 en Español", "en", "name-variable02 in English", resource.getName());
        }
        {
            org.siemac.metamac.soap.structural_resources.v1_0.domain.Variable resource = target.getVariables().get(i++);
            assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.Variable=variable03", resource.getUrn());
            assertEquals("variable03", resource.getId());
            assertEqualsInternationalString("es", "name-variable03 en Español", "en", "name-variable03 in English", resource.getName());
        }
        assertEquals(source.size(), i);
    }

    @Test
    public void testToVariable() {

        Variable source = mockVariableWithFamilies("variable01");

        // Transform
        org.siemac.metamac.soap.structural_resources.v1_0.domain.Variable target = do2SoapExternalMapper.toVariable(source);

        // Validate
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.Variable=variable01", target.getUrn());
        assertEquals("variable01", target.getId());
        assertEqualsInternationalString("es", "name-variable01 en Español", "en", "name-variable01 in English", target.getName());
        assertEquals(3, target.getFamilies().getFamilies().size());
        {
            Resource variableFamily = target.getFamilies().getFamilies().get(0);
            assertEquals("family01", variableFamily.getId());
            assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableFamily=family01", variableFamily.getUrn());
            assertEqualsInternationalString("es", "name-family01 en Español", "en", "name-family01 in English", variableFamily.getName());
        }
        {
            Resource variableFamily = target.getFamilies().getFamilies().get(1);
            assertEquals("family02", variableFamily.getId());
            assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableFamily=family02", variableFamily.getUrn());
            assertEqualsInternationalString("es", "name-family02 en Español", "en", "name-family02 in English", variableFamily.getName());
        }
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.Variable=replacedByVariable01", target.getReplacedBy().getUrn());
        assertEquals(BigInteger.valueOf(2), target.getReplaceTo().getTotal());
        {
            Resource variable = target.getReplaceTo().getReplaceTos().get(0);
            assertEquals("replaceToVariable01", variable.getId());
            assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.Variable=replaceToVariable01", variable.getUrn());
            assertEqualsInternationalString("es", "name-replaceToVariable01 en Español", "en", "name-replaceToVariable01 in English", variable.getName());
        }
        {
            Resource variable = target.getReplaceTo().getReplaceTos().get(1);
            assertEquals("replaceToVariable02", variable.getId());
            assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.Variable=replaceToVariable02", variable.getUrn());
            assertEqualsInternationalString("es", "name-replaceToVariable02 en Español", "en", "name-replaceToVariable02 in English", variable.getName());
        }
        assertEqualsInternationalString("es", "shortName-variable01 en Español", "en", "shortName-variable01 in English", target.getShortName());
        MetamacAsserts.assertEqualsDate("2012-10-01 10:12:13", target.getValidFrom());
        MetamacAsserts.assertEqualsDate("2013-10-01 10:12:13", target.getValidTo());
    }

    @Test
    public void testToCodelistFamilies() {

        Integer limit = Integer.valueOf(10);
        Integer offset = Integer.valueOf(4);

        List<CodelistFamily> source = new ArrayList<CodelistFamily>();
        source.add(mockCodelistFamily("codelistFamily01"));
        source.add(mockCodelistFamily("codelistFamily02"));
        source.add(mockCodelistFamily("codelistFamily03"));

        Integer totalRows = source.size() * 5;
        PagedResult<CodelistFamily> sources = new PagedResult<CodelistFamily>(source, offset, source.size(), limit, totalRows, 0);

        // Transform
        CodelistFamilies target = do2SoapExternalMapper.toCodelistFamilies(sources, limit);

        // Validate
        assertEquals(limit.intValue(), target.getLimit().intValue());
        assertEquals(offset.intValue(), target.getOffset().intValue());
        assertEquals(totalRows.intValue(), target.getTotal().intValue());

        assertEquals(source.size(), target.getCodelistFamilies().size());
        int i = 0;
        {
            org.siemac.metamac.soap.structural_resources.v1_0.domain.CodelistFamily resource = target.getCodelistFamilies().get(i++);
            assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.CodelistFamily=codelistFamily01", resource.getUrn());
            assertEquals("codelistFamily01", resource.getId());
            assertEqualsInternationalString("es", "name-codelistFamily01 en Español", "en", "name-codelistFamily01 in English", resource.getName());
        }
        {
            org.siemac.metamac.soap.structural_resources.v1_0.domain.CodelistFamily resource = target.getCodelistFamilies().get(i++);
            assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.CodelistFamily=codelistFamily02", resource.getUrn());
            assertEquals("codelistFamily02", resource.getId());
            assertEqualsInternationalString("es", "name-codelistFamily02 en Español", "en", "name-codelistFamily02 in English", resource.getName());
        }
        {
            org.siemac.metamac.soap.structural_resources.v1_0.domain.CodelistFamily resource = target.getCodelistFamilies().get(i++);
            assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.CodelistFamily=codelistFamily03", resource.getUrn());
            assertEquals("codelistFamily03", resource.getId());
            assertEqualsInternationalString("es", "name-codelistFamily03 en Español", "en", "name-codelistFamily03 in English", resource.getName());
        }
        assertEquals(source.size(), i);
    }

    @Test
    public void testToCodelistFamily() {

        CodelistFamily source = mockCodelistFamily("codelistFamily01");

        // Transform
        org.siemac.metamac.soap.structural_resources.v1_0.domain.CodelistFamily target = do2SoapExternalMapper.toCodelistFamily(source);

        // Validate
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.CodelistFamily=codelistFamily01", target.getUrn());
        assertEquals("codelistFamily01", target.getId());
        assertEqualsInternationalString("es", "name-codelistFamily01 en Español", "en", "name-codelistFamily01 in English", target.getName());
    }

    @Test
    public void testToCodelists() {

        Integer limit = Integer.valueOf(10);
        Integer offset = Integer.valueOf(4);

        List<CodelistVersionMetamac> source = new ArrayList<CodelistVersionMetamac>();
        source.add(mockCodelist("agency01", "codelist01", "01.000"));
        source.add(mockCodelist("agency02", "codelist02", "02.000"));
        source.add(mockCodelist("agency03", "codelist03", "01.000"));

        Integer totalRows = source.size() * 5;
        PagedResult<CodelistVersionMetamac> sources = new PagedResult<CodelistVersionMetamac>(source, offset, source.size(), limit, totalRows, 0);

        // Transform
        Codelists target = do2SoapExternalMapper.toCodelists(sources, limit);

        // Validate
        assertEquals(limit.intValue(), target.getLimit().intValue());
        assertEquals(offset.intValue(), target.getOffset().intValue());
        assertEquals(totalRows.intValue(), target.getTotal().intValue());

        assertEquals(source.size(), target.getCodelists().size());
        int i = 0;
        {
            Resource resource = target.getCodelists().get(i++);
            assertEquals("codelist01", resource.getId());
            assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=agency01:codelist01(01.000)", resource.getUrn());
            assertEqualsInternationalString("es", "name-codelist01v01.000 en Español", "en", "name-codelist01v01.000 in English", resource.getName());
        }
        {
            Resource resource = target.getCodelists().get(i++);
            assertEquals("codelist02", resource.getId());
            assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=agency02:codelist02(02.000)", resource.getUrn());
            assertEqualsInternationalString("es", "name-codelist02v02.000 en Español", "en", "name-codelist02v02.000 in English", resource.getName());
        }
        {
            Resource resource = target.getCodelists().get(i++);
            assertEquals("codelist03", resource.getId());
            assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=agency03:codelist03(01.000)", resource.getUrn());
            assertEqualsInternationalString("es", "name-codelist03v01.000 en Español", "en", "name-codelist03v01.000 in English", resource.getName());
        }
        assertEquals(source.size(), i);
    }

    @Test
    public void testToCodelist() throws MetamacException {

        CodelistVersionMetamac source = mockCodelist("agencyID1", "resourceID1", "01.123");

        // Transform
        Codelist target = do2SoapExternalMapper.toCodelist(source);

        // Validate (only Metamac metadata and some SDMX). Note: check with concrete values (not doing "getter" of source)
        assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=agencyID1:resourceID1(01.123)", target.getUrn());
        assertEqualsInternationalString("es", "shortName-resourceID1v01.123 en Español", "en", "shortName-resourceID1v01.123 in English", target.getShortName());
        assertEqualsInternationalString("es", "descriptionSource-resourceID1v01.123 en Español", "en", "descriptionSource-resourceID1v01.123 in English", target.getDescriptionSource());
        assertTrue(target.getIsRecommended());
        assertEquals("family1", target.getFamily().getId());
        assertEqualsInternationalString("es", "name-family1 en Español", "en", "name-family1 in English", target.getFamily().getName());
        assertEquals("variable1", target.getVariable().getId());
        assertEqualsInternationalString("es", "name-variable1 en Español", "en", "name-variable1 in English", target.getVariable().getName());

        // replaceX no tested, because it is necessary a repository access
        // assertEquals("replaceTo", target.getReplaceToVersion());
        // assertEquals("replacedBy", target.getReplacedByVersion());

        assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=agencyID1:codelistReplacedBy(01.000)", target.getReplacedBy().getUrn());
        assertEquals("codelistReplacedBy", target.getReplacedBy().getId());

        assertEquals(BigInteger.valueOf(2), target.getReplaceTo().getTotal());
        assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=agencyID1:codelistReplaceTo1(01.000)", target.getReplaceTo().getReplaceTos().get(0).getUrn());
        assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=agencyID1:codelistReplaceTo3(03.000)", target.getReplaceTo().getReplaceTos().get(1).getUrn());
    }

    @Test
    public void testToCodelistImported() throws MetamacException {

        CodelistVersionMetamac source = mockCodelistWithCodes("agencyID1", "resourceID1", "01.123");
        source.getMaintainableArtefact().setIsImported(Boolean.TRUE);
        source.getMaintainableArtefact().setUriProvider("uriProviderDb");
        source.getMaintainableArtefact().setUrnProvider("urnProvider");

        // Transform
        Codelist target = do2SoapExternalMapper.toCodelist(source);

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
            source.getReplacedByCodelist().getMaintainableArtefact().setPublicLogic(false);
            Codelist target = do2SoapExternalMapper.toCodelist(source);
            assertEquals(null, target.getReplacedBy());
        }
        {
            // not public
            source.getReplacedByCodelist().getMaintainableArtefact().setPublicLogic(true);
            source.getReplacedByCodelist().setAccessType(AccessTypeEnum.RESTRICTED);
            Codelist target = do2SoapExternalMapper.toCodelist(source);
            assertEquals(null, target.getReplacedBy());
        }
        {
            // can be provided
            source.getReplacedByCodelist().getMaintainableArtefact().setPublicLogic(true);
            source.getReplacedByCodelist().setAccessType(AccessTypeEnum.PUBLIC);
            Codelist target = do2SoapExternalMapper.toCodelist(source);
            assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=agencyID2:codelistReplacedBy(01.000)", target.getReplacedBy().getUrn());
        }
    }

    @Test
    public void testToCode() {
        CodelistVersionMetamac codelist = mockCodelist("agencyID1", "resourceID1", "01.123");
        CodeMetamac parent = mockCode("codeParent1", codelist, null);
        CodeMetamac source = mockCode("code2", codelist, parent);

        // Transform
        Code target = do2SoapExternalMapper.toCode(source);

        // Validate (only Metamac metadata and some SDMX). Note: check with concrete values (not doing "getter" of source)
        assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Code=agencyID1:resourceID1(01.123).code2", target.getUrn());
        assertEqualsInternationalString("es", "shortName-variableElement1 en Español", "en", "shortName-variableElement1 in English", target.getShortName());
        assertEquals("codeParent1", target.getParent().getRef().getId());
    }

    @Test
    public void testToCodeImported() {
        CodelistVersionMetamac codelist = mockCodelist("agencyID1", "resourceID1", "01.123");
        codelist.getMaintainableArtefact().setIsImported(Boolean.TRUE);
        CodeMetamac source = mockCode("code2", codelist, null);
        source.getNameableArtefact().setUrnProvider("urnProvider");
        source.getNameableArtefact().setUriProvider("uriProviderDb");

        // Transform
        Code target = do2SoapExternalMapper.toCode(source);

        // Validate
        assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Code=agencyID1:resourceID1(01.123).code2", target.getUrn());
        assertEquals("urnProvider", target.getUrnProvider());
        assertEquals("uriProviderDb", target.getUri());
    }

    @Test
    public void testToCodeWithoutVariableElement() {
        CodelistVersionMetamac codelist = mockCodelist("agencyID1", "resourceID1", "01.123");
        codelist.getMaintainableArtefact().setIsImported(Boolean.TRUE);
        CodeMetamac source = mockCode("code2", codelist, null);
        source.setVariableElement(null);

        // Transform
        Code target = do2SoapExternalMapper.toCode(source);

        // Validate
        assertEqualsInternationalString("es", "shortName-code2 en Español", "en", "shortName-code2 in English", target.getShortName());
    }
}