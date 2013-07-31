package org.siemac.metamac.srm.soap.external.v1_0.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.siemac.metamac.srm.soap.external.v1_0.code.utils.CodesMockitoVerify.verifyFindCodelists;
import static org.siemac.metamac.srm.soap.external.v1_0.code.utils.CodesMockitoVerify.verifyRetrieveCodelist;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria.Operator;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.siemac.metamac.common.test.utils.ConditionalCriteriaUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.soap.common.v1_0.domain.MetamacCriteria;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.Codelist;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.CodelistFamilies;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.Codelists;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.VariableFamilies;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.Variables;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.code.serviceapi.CodesMetamacService;
import org.siemac.metamac.srm.soap.external.v1_0.code.utils.CodesDoMocks;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Spring based transactional test with DbUnit support.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm-soap-external/applicationContext-test.xml"})
public class SrmSoapExternalFacadeV10CodesTest extends SrmSoapExternalFacadeV10BaseTest {

    private CodesMetamacService codesService;

    @Override
    @Before
    public void setUp() throws MetamacException {
        resetMocks();
    }

    @Test
    public void testFindVariableFamilies() throws Exception {
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        metamacCriteria.setCountTotalResults(Boolean.TRUE);
        metamacCriteria.setMaximumResultSize(BigInteger.valueOf(2));
        VariableFamilies variableFamilies = getSrmInterfaceV10().findVariableFamilies(metamacCriteria);

        // Validate
        assertNotNull(variableFamilies);
        assertEquals(BigInteger.valueOf(0), variableFamilies.getOffset());
        assertEquals(BigInteger.valueOf(2), variableFamilies.getLimit());
        assertEquals(BigInteger.valueOf(20), variableFamilies.getTotal());
        assertEquals(VARIABLE_FAMILY_01, variableFamilies.getVariableFamilies().get(0).getId());
        assertEquals(VARIABLE_FAMILY_02, variableFamilies.getVariableFamilies().get(1).getId());
    }

    @Test
    public void testFindVariables() throws Exception {
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        metamacCriteria.setCountTotalResults(Boolean.TRUE);
        metamacCriteria.setMaximumResultSize(BigInteger.valueOf(2));
        Variables variables = getSrmInterfaceV10().findVariables(metamacCriteria);

        // Validate
        assertNotNull(variables);
        assertEquals(BigInteger.valueOf(0), variables.getOffset());
        assertEquals(BigInteger.valueOf(2), variables.getLimit());
        assertEquals(BigInteger.valueOf(20), variables.getTotal());
        assertEquals(VARIABLE_01, variables.getVariables().get(0).getId());
        assertEquals(VARIABLE_02, variables.getVariables().get(1).getId());
    }

    @Test
    public void testFindCodelistFamilies() throws Exception {
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        metamacCriteria.setCountTotalResults(Boolean.TRUE);
        metamacCriteria.setMaximumResultSize(BigInteger.valueOf(2));
        CodelistFamilies codelistFamilies = getSrmInterfaceV10().findCodelistFamilies(metamacCriteria);

        // Validate
        assertNotNull(codelistFamilies);
        assertEquals(BigInteger.valueOf(0), codelistFamilies.getOffset());
        assertEquals(BigInteger.valueOf(2), codelistFamilies.getLimit());
        assertEquals(BigInteger.valueOf(20), codelistFamilies.getTotal());
        assertEquals(CODELIST_FAMILY_01, codelistFamilies.getCodelistFamilies().get(0).getId());
        assertEquals(CODELIST_FAMILY_02, codelistFamilies.getCodelistFamilies().get(1).getId());
    }

    @Test
    public void testFindCodelists() throws Exception {
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        metamacCriteria.setCountTotalResults(Boolean.TRUE);
        metamacCriteria.setMaximumResultSize(BigInteger.valueOf(2));
        Codelists codelists = getSrmInterfaceV10().findCodelists(metamacCriteria);

        // Validate
        assertNotNull(codelists);
        assertEquals(BigInteger.valueOf(0), codelists.getOffset());
        assertEquals(BigInteger.valueOf(2), codelists.getLimit());
        assertEquals(BigInteger.valueOf(20), codelists.getTotal());
        assertEquals(CODELIST_01_URN, codelists.getCodelists().get(0).getUrn());
        assertEquals(CODELIST_01, codelists.getCodelists().get(0).getId());
        assertEquals(CODELIST_02_URN, codelists.getCodelists().get(1).getUrn());
        assertEquals(CODELIST_02, codelists.getCodelists().get(1).getId());
        // Mockito verify
        verifyFindCodelists(codesService);
    }

    @Test
    public void testRetrieveCodelist() throws Exception {
        String urn = CODELIST_01_URN;
        Codelist codelist = getSrmInterfaceV10().retrieveCodelist(urn);

        assertNotNull(codelist);
        assertEquals(urn, codelist.getUrn());
        assertEquals(CODELIST_01, codelist.getId());
        assertEquals(VERSION_1, codelist.getVersion());
        // Mockito verify
        verifyRetrieveCodelist(codesService, urn);
    }

    @Override
    protected void resetMocks() throws MetamacException {
        codesService = ApplicationContextProvider.getApplicationContext().getBean(CodesMetamacService.class);
        reset(codesService);
        mockFindVariableFamiliesByCondition();
        mockFindVariablesByCondition();
        mockFindCodelistFamiliesByCondition();
        mockFindCodelistsByCondition();
    }

    @SuppressWarnings("unchecked")
    private void mockFindVariableFamiliesByCondition() throws MetamacException {
        when(codesService.findVariableFamiliesByCondition(any(ServiceContext.class), any(List.class), any(PagingParameter.class))).thenAnswer(new Answer<PagedResult<VariableFamily>>() {

            @Override
            public org.fornax.cartridges.sculptor.framework.domain.PagedResult<VariableFamily> answer(InvocationOnMock invocation) throws Throwable {
                // any
                List<VariableFamily> variableFamilies = new ArrayList<VariableFamily>();
                variableFamilies.add(CodesDoMocks.mockVariableFamily(VARIABLE_FAMILY_01));
                variableFamilies.add(CodesDoMocks.mockVariableFamily(VARIABLE_FAMILY_02));
                return new PagedResult<VariableFamily>(variableFamilies, 0, variableFamilies.size(), variableFamilies.size(), variableFamilies.size() * 10, 0);
            };
        });
    }

    @SuppressWarnings("unchecked")
    private void mockFindVariablesByCondition() throws MetamacException {
        when(codesService.findVariablesByCondition(any(ServiceContext.class), any(List.class), any(PagingParameter.class))).thenAnswer(new Answer<PagedResult<Variable>>() {

            @Override
            public org.fornax.cartridges.sculptor.framework.domain.PagedResult<Variable> answer(InvocationOnMock invocation) throws Throwable {
                // any
                List<Variable> variables = new ArrayList<Variable>();
                variables.add(CodesDoMocks.mockVariable(VARIABLE_01));
                variables.add(CodesDoMocks.mockVariable(VARIABLE_02));
                return new PagedResult<Variable>(variables, 0, variables.size(), variables.size(), variables.size() * 10, 0);
            };
        });
    }

    @SuppressWarnings("unchecked")
    private void mockFindCodelistFamiliesByCondition() throws MetamacException {
        when(codesService.findCodelistFamiliesByCondition(any(ServiceContext.class), any(List.class), any(PagingParameter.class))).thenAnswer(new Answer<PagedResult<CodelistFamily>>() {

            @Override
            public org.fornax.cartridges.sculptor.framework.domain.PagedResult<CodelistFamily> answer(InvocationOnMock invocation) throws Throwable {
                // any
                List<CodelistFamily> codelistFamilies = new ArrayList<CodelistFamily>();
                codelistFamilies.add(CodesDoMocks.mockCodelistFamily(CODELIST_FAMILY_01));
                codelistFamilies.add(CodesDoMocks.mockCodelistFamily(CODELIST_FAMILY_02));
                return new PagedResult<CodelistFamily>(codelistFamilies, 0, codelistFamilies.size(), codelistFamilies.size(), codelistFamilies.size() * 10, 0);
            };
        });
    }

    @SuppressWarnings("unchecked")
    private void mockFindCodelistsByCondition() throws MetamacException {
        when(codesService.findCodelistsByCondition(any(ServiceContext.class), any(List.class), any(PagingParameter.class))).thenAnswer(new Answer<PagedResult<CodelistVersionMetamac>>() {

            @Override
            public org.fornax.cartridges.sculptor.framework.domain.PagedResult<CodelistVersionMetamac> answer(InvocationOnMock invocation) throws Throwable {

                List<ConditionalCriteria> conditions = (List<ConditionalCriteria>) invocation.getArguments()[1];
                ConditionalCriteria propertyUrn = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Or, CodelistVersionMetamacProperties.maintainableArtefact()
                        .urnProvider());
                if (propertyUrn == null) {
                    // any
                    List<CodelistVersionMetamac> codelists = new ArrayList<CodelistVersionMetamac>();
                    codelists.add(CodesDoMocks.mockCodelist(AGENCY_1, CODELIST_01, VERSION_1));
                    codelists.add(CodesDoMocks.mockCodelist(AGENCY_1, CODELIST_02, VERSION_1));
                    return new PagedResult<CodelistVersionMetamac>(codelists, 0, codelists.size(), codelists.size(), codelists.size() * 10, 0);
                } else {
                    // Retrieve one
                    String urn = (String) propertyUrn.getFirstOperant();
                    CodelistVersionMetamac codelistVersion = null;
                    if (NOT_EXISTS.equals(urn)) {
                        codelistVersion = null;
                    } else if (CODELIST_01_URN.equals(urn)) {
                        codelistVersion = CodesDoMocks.mockCodelistWithCodes(AGENCY_1, CODELIST_01, VERSION_1);
                    } else if (CODELIST_02_URN.equals(urn)) {
                        codelistVersion = CodesDoMocks.mockCodelistWithCodes(AGENCY_1, CODELIST_02, VERSION_1);
                    } else {
                        fail();
                    }
                    List<CodelistVersionMetamac> codelists = new ArrayList<CodelistVersionMetamac>();
                    if (codelistVersion != null) {
                        codelists.add(codelistVersion);
                    }
                    return new PagedResult<CodelistVersionMetamac>(codelists, 0, codelists.size(), codelists.size());
                }
            };
        });
    }
}
