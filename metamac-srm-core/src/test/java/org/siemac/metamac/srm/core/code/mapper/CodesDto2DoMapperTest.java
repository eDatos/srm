package org.siemac.metamac.srm.core.code.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacAsserts;
import org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacDtoMocks;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class CodesDto2DoMapperTest extends SrmBaseTest {

    @Autowired
    private org.siemac.metamac.srm.core.code.mapper.CodesDto2DoMapper codesDto2DoMapper;

    @Test
    public void testCodelistMetamacDtoToDo() throws MetamacException {
        CodelistMetamacDto dto = CodesMetamacDtoMocks.mockCodelistDto(AGENCY_ROOT_1_V1_CODE, AGENCY_ROOT_1_V1);
        dto.getReplaceToCodelistsUrn().add(CODELIST_10_V1);
        dto.getReplaceToCodelistsUrn().add(CODELIST_11_V1);

        CodelistVersionMetamac entity = codesDto2DoMapper.codelistDtoToDo(dto);
        CodesMetamacAsserts.assertEqualsCodelist(dto, entity);
    }

    @Test
    public void testCodeMetamacDoToDto() throws MetamacException {
        CodeMetamacDto dto = CodesMetamacDtoMocks.mockCodeDto();
        CodeMetamac entity = codesDto2DoMapper.codeDtoToDo(dto);
        CodesMetamacAsserts.assertEqualsCode(dto, entity);
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmCodesTest.xml";
    }
}