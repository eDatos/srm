package org.siemac.metamac.srm.web.dsd.view;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.dsd.presenter.DsdAttributesTabPresenter.DsdAttributesTabView;
import org.siemac.metamac.srm.web.dsd.presenter.DsdDimensionsTabPresenter.DsdDimensionsTabView;
import org.siemac.metamac.srm.web.dsd.presenter.DsdGeneralTabPresenter.DsdGeneralTabView;
import org.siemac.metamac.srm.web.dsd.presenter.DsdGroupKeysTabPresenter.DsdGroupKeysTabView;
import org.siemac.metamac.srm.web.dsd.presenter.DsdPresenter;
import org.siemac.metamac.srm.web.dsd.presenter.DsdPrimaryMeasureTabPresenter.DsdPrimaryMeasureTabView;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdUiHandlers;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.TitleLabel;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.HasTabSelectedHandlers;

public class DsdViewImpl extends ViewWithUiHandlers<DsdUiHandlers> implements DsdPresenter.DsdView {

    private VLayout    panel;

    private TitleLabel dsdTitle;

    private TabSet     tabSet;

    private Tab        generalTab;
    private Tab        primaryMeasureTab;
    private Tab        dimensionsTab;
    private Tab        attributesTab;
    private Tab        groupKeysTab;

    @Inject
    public DsdViewImpl(DsdGeneralTabView dsdGeneralTabView, DsdPrimaryMeasureTabView dsdPrimaryMeasureTabView, DsdDimensionsTabView dsdDimensionsTabView, DsdAttributesTabView dsdAttributesTabView,
            DsdGroupKeysTabView dsdGroupKeysTabView) {
        super();
        panel = new VLayout();

        dsdTitle = new TitleLabel();
        dsdTitle.setStyleName("sectionTitleLeftMargin");

        tabSet = new TabSet();

        generalTab = new Tab(MetamacSrmWeb.getConstants().dsdGeneral());
        generalTab.setPane((Canvas) dsdGeneralTabView.asWidget());

        primaryMeasureTab = new Tab(MetamacSrmWeb.getConstants().dsdPrimaryMeasure());
        primaryMeasureTab.setPane((Canvas) dsdPrimaryMeasureTabView.asWidget());

        dimensionsTab = new Tab(MetamacSrmWeb.getConstants().dsdDimensions());
        dimensionsTab.setPane((Canvas) dsdDimensionsTabView.asWidget());

        attributesTab = new Tab(MetamacSrmWeb.getConstants().dsdAttributes());
        attributesTab.setPane((Canvas) dsdAttributesTabView.asWidget());

        groupKeysTab = new Tab(MetamacSrmWeb.getConstants().dsdGroupKeys());
        groupKeysTab.setPane((Canvas) dsdGroupKeysTabView.asWidget());

        tabSet.addTab(generalTab);
        tabSet.addTab(primaryMeasureTab);
        tabSet.addTab(dimensionsTab);
        tabSet.addTab(attributesTab);
        tabSet.addTab(groupKeysTab);

        panel.addMember(dsdTitle);
        panel.addMember(tabSet);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setDsd(DataStructureDefinitionMetamacDto dsd) {
        dsdTitle.setContents(InternationalStringUtils.getLocalisedString(dsd.getName()) == null ? new String() : InternationalStringUtils.getLocalisedString(dsd.getName()));
    }

    @Override
    public TabSet getDsdTabSet() {
        return tabSet;
    }

    @Override
    public HasTabSelectedHandlers getGeneralTab() {
        return generalTab;
    }

    @Override
    public HasTabSelectedHandlers getPrimaryMeasureTab() {
        return primaryMeasureTab;
    }

    @Override
    public HasTabSelectedHandlers getDimensionsTab() {
        return dimensionsTab;
    }

    @Override
    public HasTabSelectedHandlers getAttributesTab() {
        return attributesTab;
    }

    @Override
    public HasTabSelectedHandlers getGroupKeysTab() {
        return groupKeysTab;
    }

}
