package org.siemac.metamac.srm.web.category.widgets.view;

import org.siemac.metamac.srm.web.category.enums.CategoriesToolStripButtonEnum;
import org.siemac.metamac.srm.web.category.widgets.presenter.CategoriesToolStripPresenterWidget;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.web.common.client.widgets.CustomToolStripButton;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.HasClickHandlers;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class CategoriesToolStripViewImpl implements CategoriesToolStripPresenterWidget.CategoriesToolStripView {

    private final static String   RADIO_GROUP = "categories-radio-group";

    private ToolStrip             toolStrip;

    private CustomToolStripButton categorySchemesButton;
    private CustomToolStripButton categoriesButton;

    @Inject
    public CategoriesToolStripViewImpl() {
        super();
        toolStrip = new ToolStrip();
        toolStrip.setWidth100();
        toolStrip.setHeight(25);
        toolStrip.setAlign(Alignment.LEFT);

        categorySchemesButton = new CustomToolStripButton(MetamacSrmWeb.getConstants().categorySchemes());
        categorySchemesButton.setID(CategoriesToolStripButtonEnum.CATEGORY_SCHEMES.getValue());
        categorySchemesButton.setActionType(SelectionType.RADIO);
        categorySchemesButton.setRadioGroup(RADIO_GROUP);

        categoriesButton = new CustomToolStripButton(MetamacSrmWeb.getConstants().categories());
        categoriesButton.setID(CategoriesToolStripButtonEnum.CATEGORIES.getValue());
        categoriesButton.setActionType(SelectionType.RADIO);
        categoriesButton.setRadioGroup(RADIO_GROUP);

        toolStrip.addButton(categorySchemesButton);
        toolStrip.addButton(categoriesButton);
    }

    @Override
    public Widget asWidget() {
        return toolStrip;
    }

    @Override
    public void selectButton(CategoriesToolStripButtonEnum button) {
        Canvas[] canvas = toolStrip.getMembers();
        for (int i = 0; i < canvas.length; i++) {
            if (canvas[i] instanceof ToolStripButton) {
                if (button != null && button.getValue().equals(((ToolStripButton) canvas[i]).getID())) {
                    ((ToolStripButton) canvas[i]).select();
                } else {
                    ((ToolStripButton) canvas[i]).deselect();
                }
            }
        }
    }

    @Override
    public void addToSlot(Object slot, Widget content) {
    }

    @Override
    public void removeFromSlot(Object slot, Widget content) {
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
    }

    @Override
    public HasClickHandlers getCategorySchemesButton() {
        return categorySchemesButton;
    }

    @Override
    public HasClickHandlers getCategoriesButton() {
        return categoriesButton;
    }
}
