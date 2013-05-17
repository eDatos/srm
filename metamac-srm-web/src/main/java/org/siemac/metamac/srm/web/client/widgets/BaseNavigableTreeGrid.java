package org.siemac.metamac.srm.web.client.widgets;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.dto.RelatedResourceBaseDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.web.common.client.resources.StyleUtils;
import org.siemac.metamac.web.common.client.utils.NavigationUtils;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;

import com.google.gwt.event.shared.HandlerRegistration;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.tree.TreeGrid;

public abstract class BaseNavigableTreeGrid<T extends RelatedResourceBaseDto> extends TreeGrid {

    protected BaseUiHandlers      uiHandlers;
    protected HandlerRegistration cellClickHandlerRegistration;

    public BaseNavigableTreeGrid() {
        super();
        // Clicks in link fields are only captured by CellClickHandler
        cellClickHandlerRegistration = addCellClickHandler(new CellClickHandler() {

            @SuppressWarnings("unchecked")
            @Override
            public void onCellClick(CellClickEvent event) {
                ListGridField field = getField(event.getColNum());
                Record record = event.getRecord();
                if (ListGridFieldType.LINK.equals(field.getType())) {
                    String propertyName = field.getName();
                    String dtoPropertyName = NavigationUtils.getDtoPropertyName(propertyName);
                    Object navigableResource = record.getAttributeAsObject(dtoPropertyName);
                    if (navigableResource != null) {
                        // Checking of object type must be done in this order!!! (external items are instance of related resource too)
                        if (navigableResource instanceof ExternalItemDto) {
                            NavigationUtils.goTo(((ExternalItemDto) navigableResource).getManagementAppUrl());
                        } else if (navigableResource instanceof RelatedResourceBaseDto) {
                            uiHandlers.goTo(buildLocation((T) navigableResource));
                        }
                    }
                }
            }
        });
    }

    @Override
    protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {
        ListGridField field = getField(colNum);
        if (ListGridFieldType.LINK.equals(field.getType()) && !StringUtils.isBlank(record.getAttribute(field.getName()))) {
            // Set clickable style if the cell is a link cell and if is not empty
            return StyleUtils.getClickableStyle();
        }
        return super.getCellCSSText(record, rowNum, colNum);
    }

    public void setUiHandlers(BaseUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    protected abstract List<PlaceRequest> buildLocation(T relatedResourceDto);
}
