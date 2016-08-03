package org.siemac.metamac.srm.core.category.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.category.serviceapi.utils.CategoriesMetamacAsserts;
import org.siemac.metamac.srm.core.category.serviceapi.utils.CategoriesMetamacDtoMocks;
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
public class CategoriesDto2DoMapperTest extends SrmBaseTest {

    @Autowired
    private org.siemac.metamac.srm.core.category.mapper.CategoriesDto2DoMapper categoriesDto2DoMapper;

    @Test
    public void testCategorySchemeMetamacDtoToDo() throws MetamacException {
        CategorySchemeMetamacDto dto = CategoriesMetamacDtoMocks.mockCategorySchemeDto(AGENCY_ROOT_1_V1_CODE, AGENCY_ROOT_1_V1);
        CategorySchemeVersionMetamac entity = categoriesDto2DoMapper.categorySchemeMetamacDtoToDo(dto);
        CategoriesMetamacAsserts.assertEqualsCategoryScheme(dto, entity);
    }

    @Test
    public void testCategoryMetamacDoToDto() throws MetamacException {
        CategoryMetamacDto dto = CategoriesMetamacDtoMocks.mockCategoryDto();
        CategoryMetamac entity = categoriesDto2DoMapper.categoryMetamacDtoToDo(dto);
        CategoriesMetamacAsserts.assertEqualsCategory(dto, entity);
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmCategoriesTest.xml";
    }
}