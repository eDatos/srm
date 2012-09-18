package org.siemac.metamac.srm.web.client.widgets;

import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.events.HasRecordClickHandlers;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;

public class VersionsSectionStack extends SectionStack {

    protected ListGrid listGrid;

    public VersionsSectionStack(String title) {
        setWidth100();
        setVisibilityMode(VisibilityMode.MULTIPLE);
        setAnimateSections(true);
        setOverflow(Overflow.VISIBLE);
        setHeight(26);
        setStyleName("versionSectionStackStyle");

        SectionStackSection section = new SectionStackSection(title);
        section.setExpanded(false);

        Canvas rollUnderCanvasProperties = new Canvas();
        rollUnderCanvasProperties.setAnimateFadeTime(600);
        rollUnderCanvasProperties.setAnimateShowEffect(AnimationEffect.FADE);
        rollUnderCanvasProperties.setBackgroundColor("#ffe973");
        rollUnderCanvasProperties.setOpacity(50);

        listGrid = new ListGrid();
        listGrid.setAlternateRecordStyles(false);
        listGrid.setAutoFitMaxRecords(5);
        listGrid.setAutoFitData(Autofit.VERTICAL);
        listGrid.setRollUnderCanvasProperties(rollUnderCanvasProperties);
        listGrid.setShowRollOverCanvas(true);
        listGrid.setAnimateRollUnder(true);
        listGrid.setShowSelectionCanvas(true);
        listGrid.setAnimateSelectionUnder(true);
        listGrid.setLeaveScrollbarGap(false);

        section.setItems(listGrid);

        setSections(section);
    }

    public HasRecordClickHandlers getListGrid() {
        return listGrid;
    }

}
