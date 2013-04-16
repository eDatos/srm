package org.siemac.metamac.srm.web.client.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.web.code.model.ds.CodelistVisualisationDS;
import org.siemac.metamac.srm.web.code.model.record.CodelistVisualisationRecord;
import org.siemac.metamac.srm.web.code.utils.CodesClientSecurityUtils;
import org.siemac.metamac.srm.web.code.utils.CommonUtils;
import org.siemac.metamac.srm.web.code.widgets.EditCodelistOrderVisualisationWindow;
import org.siemac.metamac.web.common.client.widgets.CustomListGrid;
import org.siemac.metamac.web.common.client.widgets.InformationWindow;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;

public class CodelistOrdersSectionStack extends BaseCodelistVisualisationSectionStack {

    public CodelistOrdersSectionStack() {
        super(new CustomListGrid(), getConstants().codelistOrders());

        newCodelistVisualisationButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                final EditCodelistOrderVisualisationWindow newCodelistOrderWindow = new EditCodelistOrderVisualisationWindow(getConstants().codelistOrderCreate());
                newCodelistOrderWindow.setCodelistVisualisation(new CodelistVisualisationDto());
                newCodelistOrderWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (newCodelistOrderWindow.validateForm()) {
                            uiHandlers.saveCodelistOrder(newCodelistOrderWindow.getCodelistVisualisationDto());
                            newCodelistOrderWindow.destroy();
                        }
                    }
                });
            }
        });

        editCodelistVisualisationButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                CodelistVisualisationDto selectedOrder = getSelectedCodelistVisualisation();
                final EditCodelistOrderVisualisationWindow editCodelistOrderWindow = new EditCodelistOrderVisualisationWindow(getConstants().codelistOrderEdit());
                editCodelistOrderWindow.setCodelistVisualisation(selectedOrder);
                editCodelistOrderWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (editCodelistOrderWindow.validateForm()) {
                            uiHandlers.saveCodelistOrder(editCodelistOrderWindow.getCodelistVisualisationDto());
                            editCodelistOrderWindow.destroy();
                        }
                    }
                });
            }
        });

        deleteCodelistVisualisationButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (isAlphabeticalOrderSelected()) {
                    // Alphabetical order can not be deleted
                    InformationWindow informationWindow = new InformationWindow(getMessages().codelistOrderAlphabeticalInfoDeleteTitle(), getMessages().codelistOrderAlphabeticalInfoDeleteMessage());
                    informationWindow.show();
                } else {
                    deleteConfirmationWindow.show();
                }
            }
        });

        deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.deleteCodelistOrders(CommonUtils.getUrnsFromSelectedCodelistVisualisations(listGrid.getSelectedRecords()));
                deleteConfirmationWindow.hide();
            }
        });

        listGrid.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // Clicking checkBox will be ignored
                    String orderUrn = ((CodelistVisualisationRecord) event.getRecord()).getAttribute(CodelistVisualisationDS.URN);
                    uiHandlers.retrieveCodesWithOrder(orderUrn);
                }
            }
        });
    }

    @Override
    protected void updateListGridNewButtonVisibility() {
        if (CodesClientSecurityUtils.canCreateCodelistOrderVisualisation(codelistMetamacDto)) {
            newCodelistVisualisationButton.show();
        } else {
            newCodelistVisualisationButton.hide();
        }
    }

    @Override
    protected void showListGridEditButton() {
        if (CodesClientSecurityUtils.canUpdateCodelistOrderVisualisation(codelistMetamacDto)) {
            editCodelistVisualisationButton.show();
        }
    }

    @Override
    protected void showListGridDeleteButton() {
        if (CodesClientSecurityUtils.canDeleteCodelistOrderVisualisation(codelistMetamacDto)) {
            deleteCodelistVisualisationButton.show();
        }
    }

    private boolean isAlphabeticalOrderSelected() {
        List<String> orderCodes = CommonUtils.getVisualisationCodesFromSelectedCodelistVisualisations(listGrid.getSelectedRecords());
        for (String orderCode : orderCodes) {
            if (SrmConstants.CODELIST_ORDER_VISUALISATION_ALPHABETICAL_CODE.equals(orderCode)) {
                return true;
            }
        }
        return false;
    }
}
