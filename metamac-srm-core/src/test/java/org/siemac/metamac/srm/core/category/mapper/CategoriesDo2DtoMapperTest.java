package org.siemac.metamac.srm.core.category.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.category.serviceapi.utils.CategoriesMetamacAsserts;
import org.siemac.metamac.srm.core.category.serviceapi.utils.CategoriesMetamacDoMocks;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class CategoriesDo2DtoMapperTest extends SrmBaseTest {

    @Autowired
    private CategoriesDo2DtoMapper categoriesDo2DtoMapper;

    @Test
    public void testCategorySchemeMetamacDoToDto() {
        CategorySchemeVersionMetamac entity = CategoriesMetamacDoMocks.mockCategorySchemeFixedValues("agency01", "categoryScheme01", "01.000");
        CategorySchemeMetamacDto dto = categoriesDo2DtoMapper.categorySchemeMetamacDoToDto(entity);
        CategoriesMetamacAsserts.assertEqualsCategoryScheme(entity, dto);
    }

    @Test
    public void testCategoryMetamacDoToDto() {
        CategoryMetamac entity = mockCategoryWithAllMetadata();

        CategoryMetamacDto dto = categoriesDo2DtoMapper.categoryMetamacDoToDto(entity);
        CategoriesMetamacAsserts.assertEqualsCategory(entity, dto);
    }

    @Test
    public void testCategoryMetamacDoListToItemHierarchyDtoList() {
        List<CategoryMetamac> entities = new ArrayList<CategoryMetamac>();

        // -> Category1
        CategoryMetamac entity1 = mockCategoryWithAllMetadata();
        entities.add(entity1);
        // -> -> Category 1A
        CategoryMetamac entity1A = mockCategoryWithAllMetadata();
        entity1.addChildren(entity1A);
        // -> -> Category 1B
        CategoryMetamac entity1B = mockCategoryWithAllMetadata();
        entity1.addChildren(entity1B);
        // -> Category2
        CategoryMetamac entity2 = mockCategoryWithAllMetadata();
        entities.add(entity2);
        // -> Category3
        CategoryMetamac entity3 = mockCategoryWithAllMetadata();
        entities.add(entity3);
        // -> -> Category 3A
        CategoryMetamac entity3A = mockCategoryWithAllMetadata();
        entity3.addChildren(entity3A);
        // -> -> Category 3AA
        CategoryMetamac entity3AA = mockCategoryWithAllMetadata();
        entity3A.addChildren(entity3AA);

        List<ItemHierarchyDto> dtos = categoriesDo2DtoMapper.categoryMetamacDoListToItemHierarchyDtoList(entities);

        // Validate
        assertEquals(3, dtos.size());
        assertEquals(entity1.getNameableArtefact().getCode(), dtos.get(0).getItem().getCode());
        assertTrue(dtos.get(0).getItem() instanceof CategoryMetamacDto);
        assertEquals(2, dtos.get(0).getChildren().size());
        assertEquals(entity1A.getNameableArtefact().getCode(), dtos.get(0).getChildren().get(0).getItem().getCode());
        assertEquals(0, dtos.get(0).getChildren().get(0).getChildren().size());
        assertEquals(entity1B.getNameableArtefact().getCode(), dtos.get(0).getChildren().get(1).getItem().getCode());
        assertEquals(0, dtos.get(0).getChildren().get(1).getChildren().size());

        assertEquals(entity2.getNameableArtefact().getCode(), dtos.get(1).getItem().getCode());
        assertEquals(0, dtos.get(1).getChildren().size());

        assertEquals(entity3.getNameableArtefact().getCode(), dtos.get(2).getItem().getCode());
        assertEquals(1, dtos.get(2).getChildren().size());
        assertEquals(entity3A.getNameableArtefact().getCode(), dtos.get(2).getChildren().get(0).getItem().getCode());
        assertEquals(1, dtos.get(2).getChildren().get(0).getChildren().size());
        assertEquals(entity3AA.getNameableArtefact().getCode(), dtos.get(2).getChildren().get(0).getChildren().get(0).getItem().getCode());
        assertEquals(0, dtos.get(2).getChildren().get(0).getChildren().get(0).getChildren().size());
    }

    private CategoryMetamac mockCategoryWithAllMetadata() {
        CategorySchemeVersionMetamac categoryScheme = CategoriesMetamacDoMocks.mockCategorySchemeFixedValues("agency01", "categoryScheme01", "01.000");
        CategoryMetamac entity = CategoriesMetamacDoMocks.mockCategoryFixedValues("category01", categoryScheme, null);
        return entity;
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmCategoriesTest.xml";
    }

}