package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.web.client.widgets.BaseCodelistVisualisationSectionStack;
import org.siemac.metamac.srm.web.code.model.ds.CodelistVisualisationDS;
import org.siemac.metamac.srm.web.code.model.record.CodelistVisualisationRecord;
import org.siemac.metamac.srm.web.code.utils.CodesClientSecurityUtils;
import org.siemac.metamac.srm.web.code.utils.CommonUtils;
import org.siemac.metamac.srm.web.dsd.listener.UploadListener;
import org.siemac.metamac.web.common.client.widgets.CustomListGrid;
import org.siemac.metamac.web.common.client.widgets.InformationWindow;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;

public class CodelistOrdersSectionStack extends BaseCodelistVisualisationSectionStack {

    private ImportCodeOrdersWindow importCodeOrdersWindow;

    public CodelistOrdersSectionStack() {
        super(new CustomListGrid(), getConstants().codelistOrders());

        // New

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

        // Edit

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

        // Delete

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

        // Import

        importCodelistVisualisationButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                importCodeOrdersWindow.show();
            }
        });

        importCodeOrdersWindow = new ImportCodeOrdersWindow();
        importCodeOrdersWindow.setUploadListener(new UploadListener() {

            @Override
            public void uploadFailed(String errorMessage) {
                uiHandlers.resourceImportationFailed(errorMessage);
            }
            @Override
            public void uploadComplete(String fileName) {
                uiHandlers.resourceImportationSucceed(fileName);
            }
        });

        // Export

        exportCodelistVisualisationButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.exportCodesOrder(codelistMetamacDto.getUrn());
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
    public void setCodelist(CodelistMetamacDto codelistMetamacDto) {
        super.setCodelist(codelistMetamacDto);
        importCodeOrdersWindow.setCodelist(codelistMetamacDto);
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
    protected void updateListGridImportButtonVisibility() {
        if (CodesClientSecurityUtils.canImportCodelistOrderVisualisations(codelistMetamacDto)) {
            importCodelistVisualisationButton.show();
        } else {
            importCodelistVisualisationButton.hide();
        }
    }

    @Override
    protected void updateListGridExportButtonVisibility() {
        if (CodesClientSecurityUtils.canExportCodesOrder(codelistMetamacDto)) {
            exportCodelistVisualisationButton.show();
        } else {
            exportCodelistVisualisationButton.hide();
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
