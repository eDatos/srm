package org.siemac.metamac.srm.web.client.widgets;

import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.events.HasRecordClickHandlers;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;

public class CustomSectionStack extends SectionStack {

    protected SectionStackSection section;
    protected ListGrid            listGrid;

    public CustomSectionStack(ListGrid listGrid, String title) {
        super();
        JSOHelper.setAttribute(this.getConfig(), "notifyAncestorsOnReflow", true); // BUGFIX to Scroll-back in SectionStacks
        setWidth100();
        setVisibilityMode(VisibilityMode.MULTIPLE);
        setAnimateSections(true);
        setOverflow(Overflow.VISIBLE);
        setHeight(26);
        setStyleName("customSectionStackStyle");

        section = new SectionStackSection(title);
        section.setExpanded(true);

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

        setSections(section);
    }

    public HasRecordClickHandlers getListGrid() {
        return listGrid;
    }
}
