package org.siemac.metamac.srm.web.client.code.view;

import org.siemac.metamac.srm.web.client.code.presenter.CodelistFamilyListPresenter;
import org.siemac.metamac.srm.web.client.code.view.handlers.CodelistFamilyListUiHandlers;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.layout.VLayout;

public class CodelistFamilyListViewImpl extends ViewWithUiHandlers<CodelistFamilyListUiHandlers> implements CodelistFamilyListPresenter.CodelistFamilyListView {

    private VLayout panel;

    @Inject
    public CodelistFamilyListViewImpl() {
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
        if (slot == CodelistFamilyListPresenter.TYPE_SetContextAreaContentCodesToolBar) {
            if (content != null) {
                panel.addMember(content, 0);
            }
        } else {
            // To support inheritance in your views it is good practice to call super.setInSlot when you can't handle the call.
            // Who knows, maybe the parent class knows what to do with this slot.
            super.setInSlot(slot, content);
        }
    }
}
