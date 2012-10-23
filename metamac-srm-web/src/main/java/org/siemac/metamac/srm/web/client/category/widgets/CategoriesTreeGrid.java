package org.siemac.metamac.srm.web.client.category.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.category.utils.CategoriesClientSecurityUtils;
import org.siemac.metamac.srm.web.client.category.view.handlers.BaseCategoryUiHandlers;
import org.siemac.metamac.srm.web.client.widgets.ItemsTreeGrid;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

public class CategoriesTreeGrid extends ItemsTreeGrid {

    private NewCategoryWindow        newCategoryWindow;
    private DeleteConfirmationWindow deleteConfirmationWindow;

    private MenuItem                 createCategoryMenuItem;
    private MenuItem                 deleteCategoryMenuItem;

    private CategorySchemeMetamacDto categorySchemeMetamacDto;
    private ItemDto                  selectedCategory;

    private BaseCategoryUiHandlers   uiHandlers;

    public CategoriesTreeGrid() {

        // Context menu

        createCategoryMenuItem = new MenuItem(MetamacSrmWeb.getConstants().categoryCreate());
        createCategoryMenuItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                newCategoryWindow = new NewCategoryWindow(MetamacSrmWeb.getConstants().categoryCreate());
                newCategoryWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (newCategoryWindow.validateForm()) {
                            CategoryMetamacDto categoryMetamacDto = newCategoryWindow.getNewCategoryDto();
                            categoryMetamacDto.setItemSchemeVersionUrn(categorySchemeMetamacDto.getUrn()); // Set category scheme URN
                            categoryMetamacDto.setItemParentUrn(selectedCategory != null ? selectedCategory.getUrn() : null); // Set category parent URN
                            CategoriesTreeGrid.this.uiHandlers.saveCategory(categoryMetamacDto);
                            newCategoryWindow.destroy();
                        }
                    }
                });
            }
        });

        deleteCategoryMenuItem = new MenuItem(MetamacSrmWeb.getConstants().categoryDelete());
        deleteConfirmationWindow = new DeleteConfirmationWindow(MetamacSrmWeb.getConstants().categoryDeleteConfirmationTitle(), MetamacSrmWeb.getConstants().categoryDeleteConfirmation());
        deleteConfirmationWindow.getYesButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                CategoriesTreeGrid.this.uiHandlers.deleteCategory(selectedCategory);
            }
        });
        deleteCategoryMenuItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });

        addItemsToContextMenu(createCategoryMenuItem, deleteCategoryMenuItem);
    }

    @Override
    public void setItems(ItemSchemeDto categorySchemeMetamacDto, List<ItemHierarchyDto> itemHierarchyDtos) {
        this.categorySchemeMetamacDto = (CategorySchemeMetamacDto) categorySchemeMetamacDto;
        super.setItems(categorySchemeMetamacDto, itemHierarchyDtos);
    }

    @Override
    public void updateItemScheme(ItemSchemeDto itemSchemeDto) {
        this.categorySchemeMetamacDto = (CategorySchemeMetamacDto) itemSchemeDto;
        super.updateItemScheme(itemSchemeDto);
    }

    public void setUiHandlers(BaseCategoryUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    @Override
    protected void onNodeClick(String nodeName, String categoryUrn) {
        if (!SCHEME_NODE_NAME.equals(nodeName)) {
            uiHandlers.goToCategory(categoryUrn);
        }
    }

    @Override
    protected void onNodeContextClick(String nodeName, ItemDto category) {
        selectedCategory = category;
        createCategoryMenuItem.setEnabled(CategoriesClientSecurityUtils.canCreateCategory(categorySchemeMetamacDto.getLifeCycle().getProcStatus()));
        deleteCategoryMenuItem.setEnabled(!SCHEME_NODE_NAME.equals(nodeName) && CategoriesClientSecurityUtils.canDeleteCategory(categorySchemeMetamacDto.getLifeCycle().getProcStatus()));
        showContextMenu();
    }

}
