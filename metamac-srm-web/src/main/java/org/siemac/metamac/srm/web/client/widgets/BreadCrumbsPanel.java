package org.siemac.metamac.srm.web.client.widgets;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineHyperlink;
import com.google.gwt.user.client.ui.InlineLabel;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

public class BreadCrumbsPanel extends FlowPanel {

    public BreadCrumbsPanel() {
        super();
        setHeight("20px");
        setStyleName("breadCrumbPanel");
    }

    public void clearBreadCrumbs(int breadcrumbSize, PlaceManager placeManager) {
        this.clear();
        for (int i = 0; i < breadcrumbSize; ++i) {
            if (i > 0) {
                this.add(new InlineLabel(" > "));
            }
            InlineHyperlink link = new InlineHyperlink("Loading title...", placeManager.buildRelativeHistoryToken(i + 1));
            this.add(link);
        }
    }

    public void setBreadCrumbs(int index, String title) {
        InlineHyperlink hyperlink = (InlineHyperlink) this.getWidget(index * 2);
        if (title == null) {
            hyperlink.setHTML("Unknown title");
        } else {
            hyperlink.setHTML(title);
        }
    }

}
