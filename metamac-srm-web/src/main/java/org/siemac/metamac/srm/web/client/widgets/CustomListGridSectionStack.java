package org.siemac.metamac.srm.web.client.widgets;

import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.events.HasRecordClickHandlers;

public class CustomListGridSectionStack extends CustomSectionStack {

    protected ListGrid listGrid;

    public CustomListGridSectionStack(ListGrid listGrid, String title) {
        super(title);
        createListGrid(listGrid);
    }

    public CustomListGridSectionStack(ListGrid listGrid, String title, String styleName) {
        super(title, styleName);
        createListGrid(listGrid);
    }

    private void createListGrid(ListGrid listGrid) {
        Canvas rollUnderCanvasProperties = new Canvas();
        rollUnderCanvasProperties.setAnimateFadeTime(600);
        rollUnderCanvasProperties.setAnimateShowEffect(AnimationEffect.FADE);
        rollUnderCanvasProperties.setBackgroundColor("#ffe973");
        rollUnderCanvasProperties.setOpacity(50);

        this.listGrid = listGrid;
        this.listGrid.setAlternateRecordStyles(false);
        this.listGrid.setAutoFitMaxRecords(5);
        this.listGrid.setAutoFitData(Autofit.VERTICAL);
        this.listGrid.setRollUnderCanvasProperties(rollUnderCanvasProperties);
        this.listGrid.setShowRollOverCanvas(true);
        this.listGrid.setAnimateRollUnder(true);
        this.listGrid.setShowSelectionCanvas(true);
        this.listGrid.setAnimateSelectionUnder(true);
        this.listGrid.setLeaveScrollbarGap(false);
    }

    public HasRecordClickHandlers getListGrid() {
        return listGrid;
    }
}
