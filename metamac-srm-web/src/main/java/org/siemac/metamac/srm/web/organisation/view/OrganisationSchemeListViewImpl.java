package org.siemac.metamac.srm.web.organisation.view;

import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.organisation.presenter.OrganisationSchemeListPresenter;
import org.siemac.metamac.srm.web.organisation.view.handlers.OrganisationSchemeListUiHandlers;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class OrganisationSchemeListViewImpl extends ViewWithUiHandlers<OrganisationSchemeListUiHandlers> implements OrganisationSchemeListPresenter.OrganisationSchemeListView {

    private VLayout panel;

    @Inject
    public OrganisationSchemeListViewImpl() {
        super();
        panel = new VLayout();
        panel.setHeight100();
        panel.setOverflow(Overflow.SCROLL);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == OrganisationSchemeListPresenter.TYPE_SetContextAreaContentOrganisationSchemeListToolBar) {
            if (content != null) {
                Canvas[] canvas = ((ToolStrip) content).getMembers();
                for (int i = 0; i < canvas.length; i++) {
                    if (canvas[i] instanceof ToolStripButton) {
                        if (ToolStripButtonEnum.ORGANISATIONS.getValue().equals(((ToolStripButton) canvas[i]).getID())) {
                            ((ToolStripButton) canvas[i]).select();
                        }
                    }
                }
                panel.addMember(content, 0);
            }
        } else {
            // To support inheritance in your views it is good practice to call super.setInSlot when you can't handle the call.
            // Who knows, maybe the parent class knows what to do with this slot.
            super.setInSlot(slot, content);
        }
    }

}
