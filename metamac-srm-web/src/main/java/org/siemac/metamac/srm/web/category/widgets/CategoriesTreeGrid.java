package org.siemac.metamac.srm.web.category.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.web.category.utils.CategoriesClientSecurityUtils;
import org.siemac.metamac.srm.web.category.view.handlers.BaseCategoryUiHandlers;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.utils.ResourceFieldUtils;
import org.siemac.metamac.srm.web.client.widgets.ItemsTreeGrid;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;
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
    private ItemVisualisationResult  selectedCategory;

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

    public void setCategories(CategorySchemeMetamacDto categorySchemeMetamacDto, List<ItemVisualisationResult> itemVisualisationResults) {
        this.categorySchemeMetamacDto = categorySchemeMetamacDto;
        super.setItems(categorySchemeMetamacDto, itemVisualisationResults);
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
    protected void onNodeClick(String nodeName, String urn) {
        if (SCHEME_NODE_NAME.equals(nodeName)) {
            uiHandlers.goToCategoryScheme(urn);
        } else {
            uiHandlers.goToCategory(urn);
        }
    }

    @Override
    protected void onNodeContextClick(String nodeName, ItemVisualisationResult category) {
        selectedCategory = category;
        createCategoryMenuItem.setEnabled(canCreateCategory());
        deleteCategoryMenuItem.setEnabled(canDeleteCategory(nodeName));
        showContextMenu();
    }

    private boolean canCreateCategory() {
        return CategoriesClientSecurityUtils.canCreateCategory(categorySchemeMetamacDto);
    }

    private boolean canDeleteCategory(String nodeName) {
        return !SCHEME_NODE_NAME.equals(nodeName) && CategoriesClientSecurityUtils.canDeleteCategory(categorySchemeMetamacDto);
    }

    @Override
    protected com.smartgwt.client.widgets.viewer.DetailViewerField[] getDetailViewerFields() {
        return ResourceFieldUtils.getCategoryDetailViewerFields();
    }
}
