package org.siemac.metamac.srm.web.dsd.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacBasicDto;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.model.record.DsdRecord;
import org.siemac.metamac.srm.web.client.widgets.MaintainableArtefactTabSet;
import org.siemac.metamac.srm.web.dsd.presenter.DsdAttributesTabPresenter.DsdAttributesTabView;
import org.siemac.metamac.srm.web.dsd.presenter.DsdCategorisationsTabPresenter.DsdCategorisationsTabView;
import org.siemac.metamac.srm.web.dsd.presenter.DsdDimensionsTabPresenter.DsdDimensionsTabView;
import org.siemac.metamac.srm.web.dsd.presenter.DsdGeneralTabPresenter.DsdGeneralTabView;
import org.siemac.metamac.srm.web.dsd.presenter.DsdGroupKeysTabPresenter.DsdGroupKeysTabView;
import org.siemac.metamac.srm.web.dsd.presenter.DsdPresenter;
import org.siemac.metamac.srm.web.dsd.presenter.DsdPrimaryMeasureTabPresenter.DsdPrimaryMeasureTabView;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdUiHandlers;
import org.siemac.metamac.srm.web.dsd.widgets.DsdVersionsSectionStack;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.TitleLabel;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.HasTabSelectedHandlers;

public class DsdViewImpl extends ViewWithUiHandlers<DsdUiHandlers> implements DsdPresenter.DsdView {

    private VLayout                           panel;

    private DsdVersionsSectionStack           versionsSectionStack;

    private TitleLabel                        dsdTitle;

    private MaintainableArtefactTabSet        tabSet;

    private Tab                               generalTab;
    private Tab                               primaryMeasureTab;
    private Tab                               dimensionsTab;
    private Tab                               attributesTab;
    private Tab                               groupKeysTab;
    private Tab                               categorisationsTab;

    private DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto;

    @Inject
    public DsdViewImpl(DsdGeneralTabView dsdGeneralTabView, DsdPrimaryMeasureTabView dsdPrimaryMeasureTabView, DsdDimensionsTabView dsdDimensionsTabView, DsdAttributesTabView dsdAttributesTabView,
            DsdGroupKeysTabView dsdGroupKeysTabView, DsdCategorisationsTabView dsdCategorisationsTabView) {
        super();
        panel = new VLayout();

        // Versions

        versionsSectionStack = new DsdVersionsSectionStack(getConstants().dsdVersions());
        versionsSectionStack.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                String urn = ((DsdRecord) event.getRecord()).getUrn();
                getUiHandlers().goToDsd(urn);
            }
        });

        dsdTitle = new TitleLabel();
        dsdTitle.setStyleName("sectionTitleLeftMargin");

        tabSet = new MaintainableArtefactTabSet();

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

        categorisationsTab = new Tab(MetamacSrmWeb.getConstants().categorisations());
        categorisationsTab.setPane((Canvas) dsdCategorisationsTabView.asWidget());

        tabSet.addTab(generalTab);
        tabSet.addTab(primaryMeasureTab);
        tabSet.addTab(dimensionsTab);
        tabSet.addTab(attributesTab);
        tabSet.addTab(groupKeysTab);
        tabSet.addTab(categorisationsTab);

        //
        // PANEL LAYOUT
        //

        VLayout subPanel = new VLayout();
        subPanel.setOverflow(Overflow.SCROLL);
        subPanel.setMembersMargin(5);
        subPanel.addMember(versionsSectionStack);

        VLayout tabSubPanel = new VLayout();
        tabSubPanel.addMember(dsdTitle);
        tabSubPanel.addMember(tabSet);
        tabSubPanel.setMargin(15);
        subPanel.addMember(tabSubPanel);

        panel.addMember(subPanel);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setDsd(DataStructureDefinitionMetamacDto dsd) {
        this.dataStructureDefinitionMetamacDto = dsd;
        dsdTitle.setContents(InternationalStringUtils.getLocalisedString(dsd.getName()) == null ? new String() : InternationalStringUtils.getLocalisedString(dsd.getName()));
    }

    @Override
    public void setDsdVersions(List<DataStructureDefinitionMetamacBasicDto> dataStructureDefinitionMetamacDtos) {
        versionsSectionStack.setDataStructureDefinitions(dataStructureDefinitionMetamacDtos);
        versionsSectionStack.selectDataStructureDefinition(dataStructureDefinitionMetamacDto);
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

    @Override
    public HasTabSelectedHandlers getCategorisationsTab() {
        return categorisationsTab;
    }
}
