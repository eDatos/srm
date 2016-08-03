package org.siemac.metamac.srm.web.concept.widgets.view;

import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.concept.enums.ConceptsToolStripButtonEnum;
import org.siemac.metamac.srm.web.concept.widgets.presenter.ConceptsToolStripPresenterWidget;
import org.siemac.metamac.web.common.client.widgets.CustomToolStripButton;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.HasClickHandlers;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class ConceptsToolStripViewImpl implements ConceptsToolStripPresenterWidget.ConceptsToolStripView {

    private final static String   RADIO_GROUP = "concepts-radio-group";

    private ToolStrip             toolStrip;

    private CustomToolStripButton conceptSchemesButton;
    private CustomToolStripButton conceptsButton;

    @Inject
    public ConceptsToolStripViewImpl() {
        super();
        toolStrip = new ToolStrip();
        toolStrip.setWidth100();
        toolStrip.setHeight(25);
        toolStrip.setAlign(Alignment.LEFT);

        conceptSchemesButton = new CustomToolStripButton(MetamacSrmWeb.getConstants().conceptSchemes());
        conceptSchemesButton.setID(ConceptsToolStripButtonEnum.CONCEPT_SCHEMES.getValue());
        conceptSchemesButton.setActionType(SelectionType.RADIO);
        conceptSchemesButton.setRadioGroup(RADIO_GROUP);

        conceptsButton = new CustomToolStripButton(MetamacSrmWeb.getConstants().concepts());
        conceptsButton.setID(ConceptsToolStripButtonEnum.CONCEPTS.getValue());
        conceptsButton.setActionType(SelectionType.RADIO);
        conceptsButton.setRadioGroup(RADIO_GROUP);

        toolStrip.addButton(conceptSchemesButton);
        toolStrip.addButton(conceptsButton);
    }

    @Override
    public Widget asWidget() {
        return toolStrip;
    }

    @Override
    public void selectButton(ConceptsToolStripButtonEnum button) {
        Canvas[] canvas = toolStrip.getMembers();
        for (int i = 0; i < canvas.length; i++) {
            if (canvas[i] instanceof ToolStripButton) {
                if (button != null && button.getValue().equals(((ToolStripButton) canvas[i]).getID())) {
                    ((ToolStripButton) canvas[i]).select();
                } else {
                    ((ToolStripButton) canvas[i]).deselect();
                }
            }
        }
    }

    @Override
    public void addToSlot(Object slot, Widget content) {
    }

    @Override
    public void removeFromSlot(Object slot, Widget content) {
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
    }

    @Override
    public HasClickHandlers getConceptSchemesButton() {
        return conceptSchemesButton;
    }

    @Override
    public HasClickHandlers getConceptButton() {
        return conceptsButton;
    }
}
