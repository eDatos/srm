package org.siemac.metamac.srm.soap.external.v1_0.code.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacDoMocksFixedValues.mockCode;
import static org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacDoMocksFixedValues.mockCodelist;
import static org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacDoMocksFixedValues.mockCodelistFamily;
import static org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacDoMocksFixedValues.mockCodelistWithCodes;
import static org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacDoMocksFixedValues.mockVariable;
import static org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacDoMocksFixedValues.mockVariableFamily;
import static org.siemac.metamac.srm.soap.external.v1_0.utils.Asserts.assertEqualsInternationalString;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.junit.Test;
import org.junit.runner.RunWith;
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
            assertEquals("variableFamily01", resource.getId());
            assertEqualsInternationalString("es", "name-variableFamily01 en Español", "en", "name-variableFamily01 in English", resource.getName());
        }
        {
            org.siemac.metamac.soap.structural_resources.v1_0.domain.VariableFamily resource = target.getVariableFamilies().get(i++);
            assertEquals("variableFamily02", resource.getId());
            assertEqualsInternationalString("es", "name-variableFamily02 en Español", "en", "name-variableFamily02 in English", resource.getName());
        }
        {
            org.siemac.metamac.soap.structural_resources.v1_0.domain.VariableFamily resource = target.getVariableFamilies().get(i++);
            assertEquals("variableFamily03", resource.getId());
            assertEqualsInternationalString("es", "name-variableFamily03 en Español", "en", "name-variableFamily03 in English", resource.getName());
        }
        assertEquals(source.size(), i);
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
            assertEquals("variable01", resource.getId());
            assertEqualsInternationalString("es", "name-variable01 en Español", "en", "name-variable01 in English", resource.getName());
        }
        {
            org.siemac.metamac.soap.structural_resources.v1_0.domain.Variable resource = target.getVariables().get(i++);
            assertEquals("variable02", resource.getId());
            assertEqualsInternationalString("es", "name-variable02 en Español", "en", "name-variable02 in English", resource.getName());
        }
        {
            org.siemac.metamac.soap.structural_resources.v1_0.domain.Variable resource = target.getVariables().get(i++);
            assertEquals("variable03", resource.getId());
            assertEqualsInternationalString("es", "name-variable03 en Español", "en", "name-variable03 in English", resource.getName());
        }
        assertEquals(source.size(), i);
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
            assertEquals("codelistFamily01", resource.getId());
            assertEqualsInternationalString("es", "name-codelistFamily01 en Español", "en", "name-codelistFamily01 in English", resource.getName());
        }
        {
            org.siemac.metamac.soap.structural_resources.v1_0.domain.CodelistFamily resource = target.getCodelistFamilies().get(i++);
            assertEquals("codelistFamily02", resource.getId());
            assertEqualsInternationalString("es", "name-codelistFamily02 en Español", "en", "name-codelistFamily02 in English", resource.getName());
        }
        {
            org.siemac.metamac.soap.structural_resources.v1_0.domain.CodelistFamily resource = target.getCodelistFamilies().get(i++);
            assertEquals("codelistFamily03", resource.getId());
            assertEqualsInternationalString("es", "name-codelistFamily03 en Español", "en", "name-codelistFamily03 in English", resource.getName());
        }
        assertEquals(source.size(), i);
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
            assertEquals("urn:codelist01:01.000", resource.getUrn());
            assertEqualsInternationalString("es", "name-codelist01v01.000 en Español", "en", "name-codelist01v01.000 in English", resource.getTitle());
        }
        {
            Resource resource = target.getCodelists().get(i++);
            assertEquals("codelist02", resource.getId());
            assertEquals("urn:codelist02:02.000", resource.getUrn());
            assertEqualsInternationalString("es", "name-codelist02v02.000 en Español", "en", "name-codelist02v02.000 in English", resource.getTitle());
        }
        {
            Resource resource = target.getCodelists().get(i++);
            assertEquals("codelist03", resource.getId());
            assertEquals("urn:codelist03:01.000", resource.getUrn());
            assertEqualsInternationalString("es", "name-codelist03v01.000 en Español", "en", "name-codelist03v01.000 in English", resource.getTitle());
        }
        assertEquals(source.size(), i);
    }

    @Test
    public void testToCodelist() {

        CodelistVersionMetamac source = mockCodelistWithCodes("agencyID1", "resourceID1", "01.123");

        // Transform
        Codelist target = do2SoapExternalMapper.toCodelist(source);

        // Validate (only Metamac metadata and some SDMX). Note: check with concrete values (not doing "getter" of source)
        assertEquals("urn:resourceID1:01.123", target.getUrn());
        assertEqualsInternationalString("es", "shortName-resourceID1v01.123 en Español", "en", "shortName-resourceID1v01.123 in English", target.getShortName());
        assertTrue(target.getIsRecommended());
        assertEquals("family1", target.getFamily().getId());
        assertEqualsInternationalString("es", "name-family1 en Español", "en", "name-family1 in English", target.getFamily().getTitle());
        assertEquals("variable1", target.getVariable().getId());
        assertEqualsInternationalString("es", "name-variable1 en Español", "en", "name-variable1 in English", target.getVariable().getTitle());

        // Codes (Metamac type)
        assertEquals(4, target.getCodes().size());
        int i = 0;
        {
            Code code = (Code) target.getCodes().get(i++);
            assertEquals("urn:code1", code.getUrn());
            assertEqualsInternationalString("es", "shortName-variableElement1 en Español", "en", "shortName-variableElement1 in English", code.getShortName());
        }
        {
            Code code = (Code) target.getCodes().get(i++);
            assertEquals("urn:code2", code.getUrn());
        }
        {
            Code code = (Code) target.getCodes().get(i++);
            assertEquals("urn:code2A", code.getUrn());
        }
        {
            Code code = (Code) target.getCodes().get(i++);
            assertEquals("urn:code2B", code.getUrn());
        }
        assertEquals(i, target.getCodes().size());
    }

    @Test
    public void testToCode() {
        CodelistVersionMetamac codeScheme = mockCodelist("agencyID1", "resourceID1", "01.123");
        CodeMetamac parent = mockCode("codeParent1", codeScheme, null);
        CodeMetamac source = mockCode("code2", codeScheme, parent);

        // Transform
        Code target = do2SoapExternalMapper.toCode(source);

        // Validate (only Metamac metadata and some SDMX). Note: check with concrete values (not doing "getter" of source)
        assertEquals("urn:code2", target.getUrn());
        assertEqualsInternationalString("es", "shortName-variableElement1 en Español", "en", "shortName-variableElement1 in English", target.getShortName());
        assertEquals("codeParent1", target.getParent().getRef().getId());
    }

    @Test
    public void testToCodeWithoutVariableElement() {
        CodelistVersionMetamac codeScheme = mockCodelist("agencyID1", "resourceID1", "01.123");
        codeScheme.getMaintainableArtefact().setIsImported(Boolean.TRUE);
        CodeMetamac source = mockCode("code2", codeScheme, null);
        source.setVariableElement(null);

        // Transform
        Code target = do2SoapExternalMapper.toCode(source);

        // Validate
        assertEqualsInternationalString("es", "shortName-code2 en Español", "en", "shortName-code2 in English", target.getShortName());
    }
}