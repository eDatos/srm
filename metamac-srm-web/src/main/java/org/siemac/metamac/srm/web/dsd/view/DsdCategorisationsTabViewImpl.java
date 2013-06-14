package org.siemac.metamac.srm.web.dsd.view;

import java.util.List;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.web.dsd.presenter.DsdCategorisationsTabPresenter;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdCategorisationsTabUiHandlers;
import org.siemac.metamac.srm.web.dsd.widgets.DsdCategorisationsPanel;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;

import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.widgets.layout.VLayout;

public class DsdCategorisationsTabViewImpl extends ViewWithUiHandlers<DsdCategorisationsTabUiHandlers> implements DsdCategorisationsTabPresenter.DsdCategorisationsTabView {

    private VLayout                 panel;

    private DsdCategorisationsPanel categorisationsPanel;

    @Inject
    public DsdCategorisationsTabViewImpl() {
        super();
        panel = new VLayout();

        categorisationsPanel = new DsdCategorisationsPanel();

        panel.addMember(categorisationsPanel);
    }

    @Override
    public void setUiHandlers(DsdCategorisationsTabUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        this.categorisationsPanel.setUiHandlers(uiHandlers);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setCategorisations(DataStructureDefinitionMetamacDto dataStructureDefinitionDto, List<CategorisationDto> categorisationDtos) {
        categorisationsPanel.setCategorisations(categorisationDtos);
        categorisationsPanel.updateVisibility(dataStructureDefinitionDto);
    }

    @Override
    public void setCategorySchemesForCategorisations(GetRelatedResourcesResult result) {
        categorisationsPanel.setCategorySchemes(result);
    }

    @Override
    public void setCategoriesForCategorisations(GetRelatedResourcesResult result) {
        categorisationsPanel.setCategories(result);
    }
}
