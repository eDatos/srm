package org.siemac.metamac.srm.web.category.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.web.category.utils.CategoriesClientSecurityUtils;
import org.siemac.metamac.srm.web.category.view.handlers.CategorySchemeUiHandlers;
import org.siemac.metamac.web.common.client.listener.UploadListener;
import org.siemac.metamac.web.common.client.widgets.CustomToolStripButton;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class CategorySchemeCategoriesPanel extends VLayout {

    private ToolStrip                toolStrip;
    private ToolStripButton          importCategoriesButton;
    private ToolStripButton          exportCategoriesButton;
    private ImportCategoriesWindow   importCategoriesWindow;

    private final CategoriesTreeGrid categoriesTreeGrid;

    private CategorySchemeUiHandlers uiHandlers;

    private CategorySchemeMetamacDto categorySchemeMetamacDto;

    public CategorySchemeCategoriesPanel() {

        importCategoriesWindow = new ImportCategoriesWindow();
        importCategoriesWindow.setUploadListener(new UploadListener() {

            @Override
            public void uploadFailed(String errorMessage) {
                uiHandlers.resourceImportationFailed(errorMessage);
            }
            @Override
            public void uploadComplete(String fileName) {
                uiHandlers.resourceImportationSucceed(fileName);
            }
        });

        toolStrip = new ToolStrip();

        importCategoriesButton = new CustomToolStripButton(getConstants().actionImportCategories(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.importResource().getURL());
        importCategoriesButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                importCategoriesWindow.show();
            }
        });
        toolStrip.addButton(importCategoriesButton);

        exportCategoriesButton = new CustomToolStripButton(getConstants().actionExportCategories(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.exportResource().getURL());
        exportCategoriesButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.exportCategories(categorySchemeMetamacDto.getUrn());
            }
        });
        toolStrip.addButton(exportCategoriesButton);

        categoriesTreeGrid = new CategoriesTreeGrid();

        VLayout categoriesLayout = new VLayout();
        categoriesLayout.setMargin(15);
        categoriesLayout.addMember(toolStrip);
        categoriesLayout.addMember(categoriesTreeGrid);

        addMember(categoriesLayout);
    }

    public void updateItemScheme(CategorySchemeMetamacDto categorySchemeMetamacDto) {
        this.categorySchemeMetamacDto = categorySchemeMetamacDto;
        categoriesTreeGrid.updateItemScheme(categorySchemeMetamacDto);

        updateButtonsVisibility(categorySchemeMetamacDto);
        importCategoriesWindow.setCategorycheme(categorySchemeMetamacDto);
    }

    public void setCategories(List<ItemVisualisationResult> categoryDtos) {
        // Category hierarchy
        categoriesTreeGrid.setUiHandlers(getUiHandlers()); // UiHandlers cannot be set in constructor because is still null
        categoriesTreeGrid.setCategories(categorySchemeMetamacDto, categoryDtos);
        // Set the max records to the size of the items list (plus the item scheme node)
        categoriesTreeGrid.setAutoFitMaxRecords(categoryDtos.size() + 1);
    }

    public void setUiHandlers(CategorySchemeUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
        importCategoriesWindow.setUiHandlers(uiHandlers);
    }

    public CategorySchemeUiHandlers getUiHandlers() {
        return this.uiHandlers;
    }

    private void updateButtonsVisibility(CategorySchemeMetamacDto categorySchemeMetamacDto) {
        updateImportCategoriesButtonVisibility(categorySchemeMetamacDto);
        updateExportCategoriesButtonVisibility(categorySchemeMetamacDto);
    }

    private void updateImportCategoriesButtonVisibility(CategorySchemeMetamacDto categorySchemeMetamacDto) {
        if (CategoriesClientSecurityUtils.canImportCategories(categorySchemeMetamacDto)) {
            importCategoriesButton.show();
        } else {
            importCategoriesButton.hide();
        }
    }

    private void updateExportCategoriesButtonVisibility(CategorySchemeMetamacDto categorySchemeMetamacDto) {
        if (CategoriesClientSecurityUtils.canExportCategories(categorySchemeMetamacDto)) {
            exportCategoriesButton.show();
        } else {
            exportCategoriesButton.hide();
        }
    }
}
