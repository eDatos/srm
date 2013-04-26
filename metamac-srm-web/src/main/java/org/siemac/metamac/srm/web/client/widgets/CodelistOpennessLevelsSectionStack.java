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
import org.siemac.metamac.srm.web.code.widgets.EditCodelistOpennessVisualisationWindow;
import org.siemac.metamac.web.common.client.widgets.CustomListGrid;
import org.siemac.metamac.web.common.client.widgets.InformationWindow;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;

public class CodelistOpennessLevelsSectionStack extends BaseCodelistVisualisationSectionStack {

    public CodelistOpennessLevelsSectionStack() {
        super(new CustomListGrid(), getConstants().codelistOpennessLevels());

        newCodelistVisualisationButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                final EditCodelistOpennessVisualisationWindow newCodelistOpennessLevelWindow = new EditCodelistOpennessVisualisationWindow(getConstants().codelistOpennessLevelCreate());
                newCodelistOpennessLevelWindow.setCodelistVisualisation(new CodelistVisualisationDto());
                newCodelistOpennessLevelWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (newCodelistOpennessLevelWindow.validateForm()) {
                            uiHandlers.saveCodelistOpennessLevel(newCodelistOpennessLevelWindow.getCodelistVisualisationDto());
                            newCodelistOpennessLevelWindow.destroy();
                        }
                    }
                });
            }
        });

        editCodelistVisualisationButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                CodelistVisualisationDto selectedOpennessLevel = getSelectedCodelistVisualisation();
                final EditCodelistOpennessVisualisationWindow editCodelistOpennessWindow = new EditCodelistOpennessVisualisationWindow(getConstants().codelistOpennessLevelEdit());
                editCodelistOpennessWindow.setCodelistVisualisation(selectedOpennessLevel);
                editCodelistOpennessWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (editCodelistOpennessWindow.validateForm()) {
                            uiHandlers.saveCodelistOpennessLevel(editCodelistOpennessWindow.getCodelistVisualisationDto());
                            editCodelistOpennessWindow.destroy();
                        }
                    }
                });
            }
        });

        deleteCodelistVisualisationButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (isAllExpandedOpennessLevelSelected()) {
                    // All expanded openness level can not be deleted
                    InformationWindow informationWindow = new InformationWindow(getMessages().codelistOpennessLevelAllExpandedInfoDeleteTitle(), getMessages()
                            .codelistOpennessLevelAllExpandedInfoDeleteMessage());
                    informationWindow.show();
                } else {
                    deleteConfirmationWindow.show();
                }
            }
        });

        deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.deleteCodelistOpennessLevel(CommonUtils.getUrnsFromSelectedCodelistVisualisations(listGrid.getSelectedRecords()));
                deleteConfirmationWindow.hide();
            }
        });

        listGrid.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // Clicking checkBox will be ignored
                    String opennessLevelUrn = ((CodelistVisualisationRecord) event.getRecord()).getAttribute(CodelistVisualisationDS.URN);
                    uiHandlers.retrieveCodesWithOpennessLevel(opennessLevelUrn);
                }
            }
        });
    }

    @Override
    protected void updateListGridNewButtonVisibility() {
        if (CodesClientSecurityUtils.canCreateCodelistOpennessVisualisation(codelistMetamacDto)) {
            newCodelistVisualisationButton.show();
        } else {
            newCodelistVisualisationButton.hide();
        }
    }

    @Override
    protected void updateListGridImportButtonVisibility() {
        // Openness levels cannot be imported
        importCodelistVisualisationButton.hide();
    }

    @Override
    protected void showListGridEditButton() {
        if (CodesClientSecurityUtils.canUpdateCodelistOpennessVisualisation(codelistMetamacDto)) {
            editCodelistVisualisationButton.show();
        }
    }

    @Override
    protected void showListGridDeleteButton() {
        if (CodesClientSecurityUtils.canDeleteCodelistOpennessVisualisation(codelistMetamacDto)) {
            deleteCodelistVisualisationButton.show();
        }
    }

    private boolean isAllExpandedOpennessLevelSelected() {
        List<String> opennessLevelCodes = CommonUtils.getVisualisationCodesFromSelectedCodelistVisualisations(listGrid.getSelectedRecords());
        for (String code : opennessLevelCodes) {
            if (SrmConstants.CODELIST_OPENNESS_VISUALISATION_ALL_EXPANDED_CODE.equals(code)) {
                return true;
            }
        }
        return false;
    }
}
